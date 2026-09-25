(function () {
	"use strict";


	angular.module('app.contasareceber').controller('ContasAReceberListarCtrl', contasAReceberListarCtrlFn);

	function contasAReceberListarCtrlFn($scope, $controller, $state, NotaFiscalFaturamento, EnumService, $mdDialog, $rootScope, $timeout, Unidade, Parcela, ContaCorrente, CalculadorFaturamento, Pessoa, UsuarioLogadoService) {
		$controller('GridController', {
			$scope: $scope,
			Resource: Parcela
		});

		$scope.modulo = $state.current.data.modulo;
		$scope.titulo = $state.current.data.titulo;
		$scope.subtitulo = $state.current.data.subtitulo;

		$scope.object = {};
		$scope.object.notaSaida = {};
		$scope.labelVcto = '';
		$scope.result = null;
		$scope.dataPesquisa = null;
		$scope.dataDe = null;
		$scope.dataAte = null;

		$scope.buscarEmpresas = function () {
			let codigosUnidadesIgnoradas = [8];
			Unidade.unidadesporempresaexceto(null, codigosUnidadesIgnoradas).$promise.then(function (data) {
				$scope.empresaList = [];
				$scope.empresaList.push({
					codigo: null,
					codnome: ''
				});
				angular.forEach(data, function (value, key) {
					$scope.empresaList.push(value);
				});
			}).catch(function (response) {
				console.error(response);
				$scope.$emit('crud-critical', 'search', response);
			});
		};

		$scope.$on('changeEmpresa', function () {
			if ($scope.object.notaSaida.empresa.codigo === null) {
				$timeout(function () {
					delete $scope.object.notaSaida.empresa;
				}, 200);
			}
		});

		$scope.buscarEmpresas();

		$scope.montarLabelAgenciaConta = function (contaCorrente) {
			let conteudo = CalculadorFaturamento.montarCampoAgenciaConta(contaCorrente);
			if (CalculadorFaturamento.isCampoVazio(conteudo) || conteudo.trim() === '/')
				return ' ';
			else
				return conteudo;
		};

		//FIXME: a listaSituacao está sendo criada no frontend, mas deveria ser criada no backend.
		$scope.listaSituacao = [{
				nome: '',
				nomeStatus: '',
				ordemStatus: null
			},
			{
				nome: 'EM_RECEBIMENTO',
				nomeStatus: 'Em recebimento',
				ordemStatus: 1
			},
			{
				nome: 'FINALIZADA',
				nomeStatus: 'Recebida',
				ordemStatus: 2
			},
			{
				nome: 'CANCELADA',
				nomeStatus: 'Cancelada',
				ordemStatus: 3
			}
		];

		$scope.$on('changeSituacao', function () {
			if ($scope.object.situacao === '' || $scope.object.situacao === null) {
				delete $scope.object.situacao;
			}
		});

		$scope.listaData = [{
				nomeData: '',
				ordemData: null
			},
			{
				nomeData: 'Emissão',
				ordemData: 1
			},
			{
				nomeData: 'Vencimento',
				ordemData: 2
			},
			{
				nomeData: 'Pagamento',
				ordemData: 3
			},
			{
				nomeData: 'Agendamento',
				ordemData: 5
			},
			{
				nomeData: 'Contabilização',
				ordemData: 4
			},
			{
				nomeData: 'Cancelamento',
				ordemData: 6
			}
		];

		$scope.$on('changeData', function () {
			if (!$scope.dataPesquisa || !$scope.dataPesquisa.ordemData) {
				delete $scope.dataDe;
				delete $scope.dataAte;
			}
		});

		$scope.listaItem = [];
			const carregarListaItem = () => {
				EnumService.enumValues({
					pacote: 'br.com.araujoabreu.sgi.administracao.domain',
					classe: 'NaturezaFornecimento',
					enum: 'TipoFornecimento'
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


		/**
		 * Pesquisa de Clientes - inicio
		 */
		$scope.searchTextCliente = null;
		$scope.selectedItemCliente = null;
		$scope.resultsClient = [];

		$scope.getClientes = function (query) {
			$rootScope.loadingProgress = true;
			let resultCliente = AutocompleteService.getPessoas({
				nome: query
			}).$promise;

			let clientesArray = [];
			return resultCliente.then(function (clientes) {
				$rootScope.loadingProgress = false;
				angular.forEach(clientes, function (cliente) {
					clientesArray.push(cliente);
				})
				return clientesArray;
			});
		}

		$scope.querySearchCliente = function (query) {
			$rootScope.loadingProgress = true;
			let obj = {};
			obj.nomeFantasiaDocumento = query;
			obj.tipoPessoa = 'Pessoa Jurídica';
			obj.situacao = 'Ativo';
			obj.ordenacao = 'documento';
			obj.clientePrincipalTO = {};
			obj.clientePrincipalTO.codigo = -1;

			let clientes = [];
			let resultado = Pessoa.page(null, obj).$promise;

			return resultado.then(function (data) {
				$rootScope.loadingProgress = false;
				angular.forEach(data.content, function (cliente) {
					clientes.push(cliente);
				});
				return clientes;
			}).catch(function (response) {
				$scope.$emit('crud-critical', 'search', response);
				clientes = [];
				return clientes;
			});
		};

		$scope.selectedItemChange = function (item, tipo) {
			if (tipo == 'cliente') {
				if (angular.isDefined(item)) {
					$scope.object.cliente = {};
					$scope.object.cliente.codigo = item.codigo;
					$scope.object.cliente.nome = item.nome;
					$scope.centros = [];
					$scope.buscarCentros(item);
				} else {
					$scope.resultsClient = [];
					delete $scope.object.centroFinanceiro;
					delete $scope.selectedItemCliente;
					delete $scope.object.cliente;
					$scope.resultsSubClient = [];
					$scope.centros = [];
					delete $scope.object.subcliente;
				}
			} else if (tipo == 'subcliente') {
				if (angular.isDefined(item)) {
					$scope.object.subcliente = {};
					$scope.object.subcliente.codigo = item.codigo;
					$scope.object.subcliente.nome = item.nome;
				} else {
					$scope.resultsSubClient = [];
					delete $scope.selectedItemSubCliente;
					delete $scope.object.subcliente;
				}
			}
		};

		/**
		 * Pesquisa de Clientes - fim
		 */
		$scope.reset = function () {
			$scope.result = null;
			$scope.clear();
			$scope.object = {}
			$scope.object.notaSaida = {};
			$scope.dataPesquisa = null;
			$scope.dataDe = null;
			$scope.dataAte = null;
			deletarDatas();
		};

		/* CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */

		$scope.getSituacao = function (situacao) {
			let nomeSituacao = situacao;

			if (situacao == 'Finalizada (recebida)') {
				nomeSituacao = 'Recebida';
			}
			if (situacao == 'Aberta (NF não processada)') {
				nomeSituacao = 'Aberta';
			}
			if (situacao == 'Em recebimento') {
				//FIXME \xa0 - it is a NO-BREAK SPACE
				nomeSituacao = 'Em' + '\xa0' + 'recebimento';
			}

			return nomeSituacao;
		}

		$scope.getTipoItem = function (tipo) {
			if(tipo == "Só serviço") {
				return "Serv"
			} else if (tipo == "Serviço/Material") {
				return "S/M"
			} else if (tipo == "Só material") {
				return "Mat"
			} else {
				return "";
			}
		}

		$scope.getBanco = function (contaCorrente, tipoDocumentoRecebimento) {
			let codigoNumero = contaCorrente.banco.codigo;
			let codigoString = "" + codigoNumero;
			let tamanhoCodigo = codigoString.length;

			let displayBanco = "";

			for (var i = 0; i < 3 - tamanhoCodigo; i++) {
				displayBanco = displayBanco.concat("0");
			}

			displayBanco = displayBanco.concat(codigoString);

			if (tipoDocumentoRecebimento === 'Boleto') {
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

		let cellTemplateSituacao = '<span>{{grid.appScope.getSituacao(row.entity.situacao)}}</span>';

		let cellTemplateTipoItem = '<span style="white-space: nowrap;">{{grid.appScope.getTipoItem(row.entity.notaSaida.tipoFornecimento)}}</span>';

		let cellTemplateCLSituacao = '<span style="white-space: nowrap;">{{grid.appScope.getCLSituacao(row.entity.notaSaida.faturamentoEtapaTO.faturamento.centroFinanceiro.situacao)}}</span>';

		let cellTemplateCLSituacaoTachado = '<span style="white-space: nowrap; {{grid.appScope.getCLSituacaoTachado(row.entity.notaSaida.faturamentoEtapaTO.faturamento.centroFinanceiro.situacao)}} ">{{row.entity.notaSaida.faturamentoEtapaTO.faturamento.centroFinanceiro.identificadorFinal}}</span>';

		let cellTemplateClienteSub = '<span style="white-space: nowrap;">{{grid.appScope.getSituacao(row.entity.nomeFantasiaDocumento)}}</span>';

		let cellTemplateBanco = '<span style="white-space: nowrap;">{{grid.appScope.getBanco(row.entity.contaCorrenteRecebimento, row.entity.tipoDocumentoRecebimento)}}</span>';

		let acoesAdicionais = [{
				nome: 'receber.objeto',
				label: 'Receber',
				icone: 'icon-cash',
				tooltip: 'Receber esta parcela'
			},
			{
				nome: 'parcelar.objeto',
				label: 'Parcelar',
				icone: 'icon-cash-multiple',
				tooltip: 'Parcelar a nota fiscal'
			}
		];


		//FIXME: displayName e para usar o texto sem modificaçao.
		let columnDefs = [{
				name: 'Número',
				field: 'notaSaida.numero',
				enableSorting: false,
				sortFieldString: false,
				width: '9%',
				pinnedLeft: false,
				funcao: ['exibir', 'row.entity']
			},
			{
				name: 'Parcela',
				field: 'ordem',
				width: '5%',
				cellClass: 'text-center'
			},
			{
				name: 'Itens',
				field: 'notaSaida.tipoFornecimento',
				cellTemplate: cellTemplateTipoItem,
				tooltip: ['field'],
				enableSorting: false,
				sortFieldString: false,
				width: '4%',
				pinnedLeft: false
			},
			{
				name: 'Situação',
				cellTemplate: cellTemplateSituacao,
				field: 'situacao',
				width: '8%',
				tooltip: ['field.situacao']
			},
			{
				name: 'Cliente/Sub (nome fantasia)',
				cellTemplate: cellTemplateClienteSub,
				displayName: 'Cliente/Sub (nome fantasia)',
				field: 'nomeFantasiaDocumento',
				tooltip: ['field.nomeFantasiaDocumento']
			},
			{
				name: 'CL',
				field: 'notaSaida.faturamentoEtapaTO.faturamento.centroFinanceiro.identificadorFinal',
				cellTemplate: cellTemplateCLSituacaoTachado,
				width: '7%',
				tooltip: ['field.notaSaida.faturamentoEtapaTO.faturamento.centroFinanceiro.identificadorFinal', ' - ', 'field.notaSaida.faturamentoEtapaTO.faturamento.centroFinanceiro.nome', `${cellTemplateCLSituacao}`],
				cellClass: 'text-center'
			},
			{
				name: 'Banco',
				displayName: 'Banco',
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
				field: 'valorLiquido',
				width: '8%',
				cellFilter: 'formatacaoMoeda',
				cellClass: 'text-right'
			},
			{
				name: 'Emissão',
				field: 'notaSaida.dataEmissao',
				width: '7%',
				cellFilter: 'formatacaoData',
				cellClass: 'text-center'
			},
			{
				name: 'Vencimento',
				field: 'dataVencimento',
				width: '7%',
				cellFilter: 'formatacaoData',
				cellClass: 'text-center'
			},
			{
				name: 'Pagamento',
				field: 'dataPagamento',
				width: '7%',
				cellFilter: 'formatacaoData',
				cellClass: 'text-center'
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

		UsuarioLogadoService.possuiUmPerfilLista(['faturamento-cns-gerencia']).$promise.then((result) => {
			$scope.temPerfilFaturamentoCnsGerencia = result.possuiUmPerfilLista;
		});

		UsuarioLogadoService.possuiUmPerfilLista(['faturamento-analista']).$promise.then((result) => {
			$scope.temPerfilAnalista = result.possuiUmPerfilLista;
		});

		$scope.exibirAcao = function (acao, row) {
			if (acao === 'receber') {
				if (row.entity.situacao === 'Em recebimento' && $scope.temPerfilAnalista && !$scope.temPerfilFaturamentoCnsGerencia) {
					return true;
				}
			}
			if (acao === 'parcelar') {
				if (row.entity.situacao === 'Em recebimento' && $scope.temPerfilAnalista && !$scope.temPerfilFaturamentoCnsGerencia) {
					return true;
				}
			}
			if (acao === 'editar') {
				if (row.entity.situacao === 'Em recebimento' && $scope.temPerfilAnalista && !$scope.temPerfilFaturamentoCnsGerencia) {
					return true;
				}
			}
			if (acao === 'exibir') {
				return true;
			}
			if (acao === 'imprimir') {
				return true;
			}
			return false;
		};

		$scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'parcela', 'a');

		$scope.setDefaultParams = function () {
			$scope.paginationOptions.sortField = 'notaSaidaParcela.codigo';
			$scope.paginationOptions.sortFieldString = false;
			$scope.paginationOptions.sortDir = 'asc';
			//      $scope.paginationOptions.pageSize = 5; //parametrizar para utilização do teste automatizado.
		}
		$scope.setDefaultParams();

		$scope.getPage = function (page) {
			$scope.getPageGrid($scope.gridPesquisa, page);
		};

		function definirDatasPesquisa() {
			deletarDatas();

			if ($scope.dataPesquisa && $scope.dataPesquisa.ordemData) {

				switch ($scope.dataPesquisa.ordemData) {
					case 1:
						if ($scope.dataDe) {
							$scope.object.dataEmissaoDe = angular.copy($scope.dataDe);
						}

						if ($scope.dataAte) {
							$scope.object.dataEmissaoAte = angular.copy($scope.dataAte);
						}
						break;
					case 2:
						if ($scope.dataDe) {
							$scope.object.dataVencimentoDe = angular.copy($scope.dataDe);
						}

						if ($scope.dataAte) {
							$scope.object.dataVencimentoAte = angular.copy($scope.dataAte);
						}
						break;
					case 3:
						if ($scope.dataDe) {
							$scope.object.dataPagamentoDe = angular.copy($scope.dataDe);
						}

						if ($scope.dataAte) {
							$scope.object.dataPagamentoAte = angular.copy($scope.dataAte);
						}
						break;
					case 4:
						if ($scope.dataDe) {
							$scope.object.dataContabilizacaoDe = angular.copy($scope.dataDe);
						}

						if ($scope.dataAte) {
							$scope.object.dataContabilizacaoAte = angular.copy($scope.dataAte);
						}
						break;
					case 5:
						if ($scope.dataDe) {
							$scope.object.dataAgendamentoDe = angular.copy($scope.dataDe);
						}

						if ($scope.dataAte) {
							$scope.object.dataAgendamentoAte = angular.copy($scope.dataAte);
						}
						break;
					case 6:
						if ($scope.dataDe) {
							$scope.object.dataResponsavelCancelamentoDe = angular.copy($scope.dataDe);
						}

						if ($scope.dataAte) {
							$scope.object.dataResponsavelCancelamentoAte = angular.copy($scope.dataAte);
						}
						break;
					default:
						deletarDatas();
				}
			}
		}

		function deletarDatas() {
			delete $scope.object.dataEmissaoDe;
			delete $scope.object.dataEmissaoAte;
			delete $scope.object.dataVencimentoDe;
			delete $scope.object.dataVencimentoAte;
			delete $scope.object.dataPagamentoDe;
			delete $scope.object.dataPagamentoAte;
			delete $scope.object.dataContabilizacaoDe;
			delete $scope.object.dataContabilizacaoAte;
			delete $scope.object.dataAgendamentoDe;
			delete $scope.object.dataAgendamentoAte;
		}

		/* FIM DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */

		$scope.buscar = function () {
			definirDatasPesquisa();

			$rootScope.loadingProgress = true;
			$scope.setDefaultParams();
			$scope.getPage(1);
		};

		$scope.$watch('result', function () {
			$rootScope.loadingProgress = false;

			if ($scope.result && $scope.result.length === 1 && $scope.paginationOptions && $scope.paginationOptions.totalPages === 1) {
				let item = $scope.result[0];
				if (item.situacao === "Em recebimento") {
					$state.go('contasareceber.exibir', {
						objectId: item.codigo
					});
				}
			}
		});

		/**
		 * Pesquisa do Código do Serviço
		 */
		$scope.querySearchServico = function (query) {
			$rootScope.loadingProgress = true;
			let obj = {};
			obj.nome = query;
			obj.situacao = 'Ativo';

			let servicos = [];
			let resultado = NotaFiscalFaturamento.qbeST(null, obj).$promise;

			return resultado.then(function (data) {
				$rootScope.loadingProgress = false;
				angular.forEach(data, function (servico) {
					servicos.push(servico);
				})
				return servicos;
			}).catch(function (response) {
				$scope.$emit('crud-critical', 'search', response);
				servicos = [];
				return servicos;
			});
		};

		$scope.selectedItemChange = function (item) {
			if (angular.isDefined(item)) {
				if (angular.isUndefined($scope.object.notaSaida)) {
					$scope.object.notaSaida = {};
				}
				$scope.object.notaSaida.servicoTipo = {};
				$scope.object.notaSaida.servicoTipo.codigo = item.codigo;
				$scope.object.notaSaida.servicoTipo.nome = item.nome;
				$scope.object.notaSaida.servicoTipo.sigla = item.sigla;
			} else {
				$scope.resultsServico = [];
				delete $scope.selectedItemServico;
				if (angular.isDefined($scope.object.notaSaida)) {
					delete $scope.object.notaSaida.servicoTipo;
				}
			}
		};

		$scope.exibir = function (notaFiscalParcela) {
			let codigo = notaFiscalParcela.codigo;
			// $state.go('contasareceber.exibir', {objectId: notaFiscalParcela.codigo});
			window.open(`/sgi/nv/app/main.html#!/contasareceber/${codigo}/exibir`);
		};

		$scope.imprimir = function(notaFiscalParcela) {
                    var url = $state.href('contasareceber.imprimir', {
                        objectId: notaFiscalParcela.codigo
                    });
                    window.open(url, '_blank');
                };

		$scope.editar = function (notaFiscalParcela) {
			$state.go('contasareceber.editar', {
				objectId: notaFiscalParcela.codigo
			});
		};

		$scope.receber = function (notaFiscalParcela) {
			$state.go('contasareceber.receber', {
				objectId: notaFiscalParcela.codigo
			});
		};

		$scope.parcelar = function (notaFiscalParcela) {
			$state.go('notafiscal.exibir_por_acao', {
				objectId: notaFiscalParcela.notaSaida.codigo,
				acao: 'parcelar',
				from: 'contasareceber.exibir'
			});
		};

	};

})();
