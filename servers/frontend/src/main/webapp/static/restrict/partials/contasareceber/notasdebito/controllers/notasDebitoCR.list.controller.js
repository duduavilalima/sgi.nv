(function () {
	"use strict";

	angular.module('app.notasDebitoCR').controller('NotasDebitoCRListarCtrl', notasDebitoCRListarCtrlFn);

	function notasDebitoCRListarCtrlFn(
		$scope,
		$controller,
		$state,
		NotaDebito,
		NotaFiscalFaturamento,
		EnumService,
		$mdDialog,
		$rootScope,
		Unidade,
		UsuarioLogadoService
		) {

		$controller('SearchController', {
			$scope: $scope,
			Resource: NotaDebito
		});

		$controller('GridController', {
			$scope: $scope,
			Resource: NotaDebito
		});

		$scope.modulo = $state.current.data.modulo;
		$scope.titulo = $state.current.data.titulo;
		$scope.subtitulo = $state.current.data.subtitulo;

		$scope.exibirBotoes = false;
		$scope.object = {};
		$scope.result = undefined;

		$scope.verificarPermissao = () => {
			this.isAction = true;
			// Exibir apenas quando for usuários do faturamento;
			UsuarioLogadoService.apenasFaturamento().$promise.then(response => {
				this.isAction = false;
				$scope.exibirBotoes = response.value;
			});
		};

		$scope.verificarPermissao();

		$scope.buscarEmpresas = function() {
			var codigosUnidadesIgnoradas = [8]; // vide SGINV-1405

			Unidade.unidadesporempresaexceto(undefined, codigosUnidadesIgnoradas).$promise.then(function(data) {
				$scope.empresaList = [];
				$scope.empresaList.push({
					codigo: -1,
					codnome: ''
				});
				angular.forEach(data, function(value, key) {
					$scope.empresaList.push(value);
				});
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'search', response);
			});
		};

		$scope.$on('changeEmpresa', function() {
			if ($scope.object.empresa.codigo == -1) {
				delete $scope.object.empresa;
			}
		});

		$scope.buscarEmpresas();

		EnumService.enumValues({
			pacote: 'br.com.araujoabreu.sgi.compras.domain',
			classe: 'NotaDebito',
			enum: 'SituacaoNotaDebito'
		})
		.$promise.then(function (data) {
			$scope.enumStatus = [];
			$scope.enumStatus.push('');
			angular.forEach(data, function(value, key){
				if (value !== 'Aberta')
					$scope.enumStatus.push(value)
			});
		}).catch(function (response) {
			$scope.$emit('crud-critical', 'load', response);
		});

		$scope.$on('changeSituacao', function () {
			if ($scope.object.situacao === '') {
				delete $scope.object.situacao;
			}
		});

		$scope.listaItem = [];
			const carregarListaItem = () => {
				EnumService.enumValues({
					pacote: 'br.com.araujoabreu.sgi.faturamento.domain',
					classe: 'Faturamento',
					enum: 'TipoFornecimentoNF'
				})
				.$promise.then((data) => {
					$scope.listaItem.push('');
						angular.forEach(data, (value) => {
							$scope.listaItem.push(value);
						});
				}).catch((response) => {
					$scope.$emit('crud-critical', 'load', response);
				});
			};

		carregarListaItem();

		$scope.$on('changeItem', function() {
			if ($scope.object.tipoItem === '') {
				delete $scope.object.tipoItem;
			}
		});

		$scope.reset = function() {
			$scope.result = undefined;
			$scope.clear();
			delete $scope.object.servicoTipo;
			delete $scope.searchTextServico;
			delete $scope.object.servicoTipoCodigo;
			$scope.object = {};

//			document.querySelectorAll('input[type=search]')[0].value = '';
		};

		// FIXME: Refatorar
		const salvarProtocolo = function(nf) {
			$scope.actionProtocolo = true;
			$rootScope.loadingProgress = true;
			$scope.action = true;
			NotaDebito.salvarProtocolo(nf).$promise.then(function(data) {
				$scope.notification.success(undefined, `Protocolo da ${nd.numero} atualizado com sucesso.`);
				$rootScope.loadingProgress = false;
				$scope.actionProtocolo = false;
				$scope.action = false;
			}).catch(function(response) {
				$rootScope.loadingProgress = false;
				$scope.actionProtocolo = false;
				$scope.action = false;
				$scope.$emit('crud-critical', 'search', response);
				console.error(response);
			});
		};

		$scope.protocolo = function(notaDebito) {
			// FIXME: Refatorar
			$mdDialog.show({
				controller: DialogControllerInserirProtocolo,
				locals: {
					fnSalvar: salvarProtocolo,
					nd: notaDebito
				},
				templateUrl: 'partials/contasareceber/notasDebito/html/partials/dialog_protocolo_nd.html',
				parent: angular.element(document.body),
				targetEvent: undefined,
				clickOutsideToClose: false,
				fullscreen: false
			});
		};
		// FIXME: Refatorar
		function DialogControllerInserirProtocolo($scope, $mdDialog, fnSalvar, nd) {
			$scope.notaDebito = nd;
			$scope.copiaProtocoloEntrega = nd.protocoloEntrega;
			$scope.cancel = function() {
				$mdDialog.cancel();
			};
			$scope.confirmar = function() {
				$scope.notaDebito.protocoloEntrega = $scope.copiaProtocoloEntrega;
				fnSalvar($scope.notaDebito);
				$mdDialog.cancel();
			};
		}
		/**
		* CAMPOS DATAS
		*/

		$scope.tipoDataEnum =
			[
				{
					id : 1,
					tipo: ""
				},
				{
					id : 2,
					tipo: "Emissão"
				},
				{
					id : 3,
					tipo: "Vencimento"
				},
				{
					id : 4,
					tipo: "Agendamento"
				},
				{
					id : 5,
					tipo: "Pagamento"
				},
				{
					id : 6,
					tipo: "Contabilização"
				},
				{
					id : 7,
					tipo: "Cancelamento"
				}
			];

		function limparCamposData1() {
			 $scope.object.data1 = undefined;
			 $scope.object.dataDe = undefined;
			 $scope.object.dataAte = undefined;
			 $scope.lblDataDe = 'Data de';
			 $scope.lblDataAte = 'Data até';
		}

		limparCamposData1();

		let labelCamposDatas = (label, posicaoCampoData) => {
			if (label) {
				$scope.lblDataDe = `${label} de`;
				$scope.lblDataAte = `${label} até`;
			} else {
				if (posicaoCampoData === 1) {
					limparCamposData1();
				}
			}
		};

		let acaoDataMap = new Map();
		acaoDataMap.set(1, {label: undefined, fn: labelCamposDatas});
		acaoDataMap.set(2, {label: 'Emissão', fn: labelCamposDatas});
		acaoDataMap.set(3, {label: 'Vencimento', fn: labelCamposDatas});
		acaoDataMap.set(4, {label: 'Agendamento', fn: labelCamposDatas});
		acaoDataMap.set(5, {label: 'Pagamento', fn: labelCamposDatas});
		acaoDataMap.set(6, {label: 'Contabilização', fn: labelCamposDatas});
		acaoDataMap.set(7, {label: 'Cancelamento', fn: labelCamposDatas});

		$scope.$on('changeData1', function() {
			if ($scope.object.data1) {
				acaoDataMap.get($scope.object.data1.id).fn(acaoDataMap.get($scope.object.data1.id).label, 1);
				if ($scope.object.data1)
					$scope.object.tipoData = $scope.object.data1.tipo;
			}
			$scope.$applyAsync();
		});

		/* CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */
		/* Criar métodos de formatação específicos para pesquisa */

		$scope.getTipoItem = function (tipo) {
			if(tipo == "Somente serviço") {
				return "Serv"
			} else if (tipo == "Serviço/Material") {
				return "S/M"
			} else if (tipo == "Somente material") {
				return "Mat"
			} else {
				return "";
			}
		}

		$scope.getBanco = function (contaCorrente) {
			let codigoNumero = contaCorrente.banco.codigo;
			let codigoString = "" + codigoNumero;
			let tamanhoCodigo = codigoString.length;

			let displayBanco = "";

			for (var i = 0; i < 3 - tamanhoCodigo; i++) {
				displayBanco = displayBanco.concat("0");
			}

			displayBanco = displayBanco.concat(codigoString);

			if (contaCorrente.boletoFaturamento) {
				displayBanco = displayBanco.concat(" (b)");
			}

			return displayBanco;
		}

		$scope.getCLSituacao = function (situacao) {
			if(situacao == 'Ativo' || situacao == 'Encerrado, mas com faturamento') {
				return '';
			} else {
				return ` (${situacao})`
			}
		}

		$scope.getCLSituacaoTachado = function(situacaoTachado) {
			if(situacaoTachado == 'Ativo' || situacaoTachado == 'Encerrado, mas com faturamento') {
				return '';
			} else {
				return 'text-decoration: line-through;';
			}
		}

		$scope.getTipoContrato = function(tipo) {
			if (tipo == undefined) return "";

			return tipo == 'Contratual' ? 'C' : 'E';
		};

		$scope.getCLSituacao = function(situacao) {
			if ((situacao == 'Ativo' || situacao == 'Encerrado, mas com faturamento')){
				return "";
			} else {
				return ` (${situacao})`;
			}
		};
		$scope.getCLSituacaoTachado = function(situacaoTachado) {
			if (situacaoTachado == 'Ativo'  || situacaoTachado == 'Encerrado, mas com faturamento'){
				return "";
			} else {
				return "text-decoration: line-through;";
			}
		};

		let acoesAdicionais = [
			{
				nome: 'protocolo.objeto',
				label: 'Protocolo',
				icone: 'icon-file-document-box',
				tooltip: 'Incluir protocolo'
			}
		];

		$scope.exibirAcao = function(acao, row) {
			if (acao === 'protocolo') {
				if (row.entity.status === 'Em recebimento' && $scope.exibirBotoes) {
					return true;
				}
			}
			if (acao === 'imprimir') {
				return true;
			}
			if (acao === 'exibir') {
				return true;
			}
			return false;
		};

		let cellTemplateCLSituacao = '<span style="white-space: nowrap;">{{ grid.appScope.getCLSituacao(row.entity.centroFinanceiro.situacao) }}</span>';

        let cellTemplateCLSituacaoTachado = '<span style="white-space: nowrap; {{ grid.appScope.getCLSituacaoTachado(row.entity.centroFinanceiro.situacao) }} ">{{ row.entity.centroFinanceiro.identificadorFinal }}</span>';

		let cellTemplateSituacao = '<span>{{grid.appScope.getSituacao(row.entity.situacao)}}</span>';

		let cellTemplateTipoItem = '<span style="white-space: nowrap;">{{grid.appScope.getTipoItem(row.entity.notaSaida.tipoFornecimento)}}</span>';

		let cellTemplateClienteSub = '<span style="white-space: nowrap;">{{grid.appScope.getSituacao(row.entity.nomeFantasiaDocumento)}}</span>';

		let cellTemplateBanco = '<span style="white-space: nowrap;">{{grid.appScope.getBanco(row.entity.contaCorrentePrevista)}}</span>';

		//FIXME: displayName e para usar o texto sem modificaçao.
		var columnDefs = [{
				name: 'Número',
				field: 'numero',
				enableSorting: false,
				sortFieldString: false,
				width: '9%',
				pinnedLeft: false,
				funcao: ['exibir', 'row.entity']
			},
			{
				name: 'Itens',
				field: 'notaDebito.tipoFornecimento',
				cellTemplate: cellTemplateTipoItem,
				tooltip: ['field'],
				enableSorting: false,
				sortFieldString: false,
				width: '4%',
				pinnedLeft: false
			},
			{
				name: 'Situação',
				field: 'situacao',
				tooltip: ['field'],
				width: '8%'
			},
			{
				name: 'Cliente/Sub (nome fantasia)',
				displayName: 'Cliente/Sub (nome fantasia)',
				field: 'cliente.nomeFantasia',
				tooltip: ['field.cliente.nomeFantasia']
			},
			{
				name: 'CL',
				displayName: 'CL',
				field: 'centroFinanceiro.identificadorFinal',
				cellTemplate: cellTemplateCLSituacaoTachado,
				cellClass: 'text-center',
				width: '7%',
				tooltip: ['field.centroFinanceiro.identificadorFinal', ' - ', 'field.centroFinanceiro.nome', `${cellTemplateCLSituacao}`]
			},
			{
				name: 'Banco',
				displayName: 'Banco',
				field: 'contaCorrenteRecebimento.banco.codigo',
				cellTemplate: cellTemplateBanco,
				width: '7%',
				cellClass: 'text-center',
				tooltip: ['field.contaCorrenteRecebimento.banco.nome', ' / ', 'field.contaCorrenteRecebimento.agencia', ' / ', 'field.contaCorrenteRecebimento.conta']
			},
			{
				name: 'Bruto',
				field: 'valorBruto',
				width: '8%',
				cellFilter: 'formatacaoMoeda',
				cellClass: 'text-right'
			},
			{
				name: 'Líquido',
				field: 'valorLiquidoRecebido',
				width: '8%',
				cellFilter: 'formatacaoMoeda',
				cellClass: 'text-right'
			},
			{
				name: 'Emissão',
				field: 'dataEmissao',
				cellClass: 'text-center',
				width: '7%',
				cellFilter: 'formatacaoData'
			},
			{
				name: 'Vencimento',
				field: 'dataVencimento',
				cellClass: 'text-center',
				width: '7%',
				cellFilter: 'formatacaoData'
			},
			{
				name: 'Pagamento',
				field: 'dataPagamento',
				cellClass: 'text-center',
				width: '7%',
				cellFilter: 'formatacaoData'
			},
			{
				name: 'Opções',
				acoes: ['exibir.objeto', 'imprimir.objeto'],
				acoesAdicionais: acoesAdicionais,
				width: '5%',
				pinnedRight: false,
				enableSorting: false
			}
		];

		$scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'nota de débito', 'a');

		$scope.setDefaultParams = function() {
			$scope.paginationOptions.sortField = 'notaDebito.numero';
			$scope.paginationOptions.sortFieldString = false;
			$scope.paginationOptions.sortDir = 'asc';
			//      $scope.paginationOptions.pageSize = 5; //parametrizar para utilização do teste automatizado.
		};

		$scope.setDefaultParams();

		$scope.getPage = function(page) {
			$scope.getPageGrid($scope.gridPesquisa, page);
		};

		/* FIM DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */

		$scope.buscar = function() {
			$scope.object.origemPesquisa = 'contas_a_receber';
			$rootScope.loadingProgress = true;
			$scope.setDefaultParams();
			$scope.getPage(1);
		};

		$scope.$watch('result', function() {
			$rootScope.loadingProgress = false;
		});

		$scope.exibir = function(notaDebito) {
			window.open(`/sgi/nv/app/main.html#!/contasareceber/notasdebito/${notaDebito.codigo}/exibir`, '_blank');
		};

		$scope.imprimir = function(notaDebito) {
			var url = $state.href('notadebito.imprimir', {
				objectId: notaDebito.codigo
			});
			window.open(url, '_blank');
		};
	};
})();
