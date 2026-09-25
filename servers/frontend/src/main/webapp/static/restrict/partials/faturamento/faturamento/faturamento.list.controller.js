(function () {
	'use strict';

	angular
		.module('app.faturamento')
		.controller('FaturamentoListarCtrl', listController);

	function listController($scope, $controller, $state, Faturamento, EnumService, AutocompleteService, Cidade, $rootScope, $timeout, UsuarioLogadoService) {
		$controller('GridController', {
			$scope: $scope,
			Resource: Faturamento
		});

		$scope.modulo = $state.current.data.modulo;
		$scope.titulo = $state.current.data.titulo;
		$scope.subtitulo = $state.current.data.subtitulo;

		$scope.usuario = UsuarioLogadoService.getUsuarioLogado();

		EnumService.enumValues({
				pacote: 'br.com.araujoabreu.sgi.faturamento.domain',
				classe: 'Faturamento',
				enum: 'Status'
			})
			.$promise.then(function (data) {
				$scope.enumStatus = [];
				$scope.enumStatus = data.filter(value => value !== "Em recebimento" && value !== "Recebido");
			}).catch(function (response) {
				$scope.$emit('crud-critical', 'load', response);
			});


		$scope.$on('changeSituacao', function () {
			if ($scope.object.situacao === '') {
				$timeout(function () {
					delete $scope.object.situacao;
				}, 200);
			}
		});

		EnumService.enumValues({
				pacote: 'br.com.araujoabreu.sgi.faturamento.domain',
				classe: 'Faturamento',
				enum: 'TipoFaturamento'
			})
			.$promise.then(function (data) {
				$scope.enumTipo = [];
				$scope.enumTipo.push('');
				angular.forEach(data, function (value, key) {
					$scope.enumTipo.push(value);
				});
			}).catch(function (response) {
				$scope.$emit('crud-critical', 'load', response);
			});


		$scope.$on('changeTipo', function () {
			if ($scope.object.tipo === '') {
				$timeout(function () {
					delete $scope.object.tipo;
				}, 200);
			}
		});

		EnumService.enumValues({
				pacote: 'br.com.araujoabreu.sgi.faturamento.domain',
				classe: 'Faturamento',
				enum: 'TipoDocumentoFaturamento'
			})
			.$promise.then(function (data) {
				$scope.enumTipoDocFat = [];
				$scope.enumTipoDocFat.push('');
				angular.forEach(data, function (value, key) {
					$scope.enumTipoDocFat.push(value);
				});
		}).catch(function (response) {
			$scope.$emit('crud-critical', 'load', response);
		});


		$scope.$on('changeTipo', function () {
			if ($scope.object.tipo === '') {
				$timeout(function () {
					delete $scope.object.tipo;
				}, 200);
			}
		});

		EnumService.enumValues({
				pacote: 'br.com.araujoabreu.sgi.faturamento.domain',
				classe: 'Faturamento',
				enum: 'TipoFornecimentoNF'
			})
			.$promise.then(function (data) {
				$scope.enumTipoItens = [];
				$scope.enumTipoItens.push('');
				angular.forEach(data, function (value, key) {
					$scope.enumTipoItens.push(value);
				});
		}).catch(function (response) {
			$scope.$emit('crud-critical', 'load', response);
		});


		$scope.$on('changeTipoFornecimento', function () {
			if ($scope.object.tipoFornecimento === '') {
				$timeout(function () {
					delete $scope.object.tipoFornecimento;
				}, 200);
			}
		});

		/* CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */

		/* Criar métodos de formatação específicos para pesquisa */
		//Coluna Tipo
		$scope.getTipoContrato = function (tipo) {
			if (tipo == null) return "";

			return tipo == 'Contratual' ? 'C' : 'E';
		};

		$scope.getDocumentoFaturamento = function (docFat) {
			if (docFat == null) return "";

			return docFat == 'Nota fiscal' ? 'NF' : 'ND';
		};

		$scope.getDocumentoFaturamentoTT = function (docFatTt) {
        			if (docFatTt == null) return "";

        			return docFatTt == 'Nota fiscal' ? 'Nota fiscal' : 'Nota de débito';
        		};

		$scope.getTipoFornecimento = function (tipoFornecimento) {
			if(tipoFornecimento == "Só serviço") {
				return "Serv"
			} else if (tipoFornecimento == "Serviço/Material") {
				return "S/M"
			} else if (tipoFornecimento == "Só material") {
				return "Mat"
			} else {
				return "";
			}
			return tipoFornecimento;
		};

		var cellTemplateDocFat = '<span>{{grid.appScope.getDocumentoFaturamento(row.entity.tipoDocumentoFaturamento)}}</span>';

		var cellTemplateTipo = '<span>{{grid.appScope.getTipoContrato(row.entity.tipo)}}</span>';

		var cellTemplateCliente = '<div style="height: 16px; width: 98%; overflow: hidden;">{{row.entity.cliente.nome}}</div>';

		var cellTemplateSituacao = '<span style="white-space: nowrap;">{{row.entity.situacao}}</span>';

		var cellTemplateTipoItem = '<span>{{grid.appScope.getTipoFornecimento(row.entity.tipoFornecimento)}}</span>';

		var cellTemplateTipoTooltipDF = '<span>{{grid.appScope.getDocumentoFaturamentoTT(row.entity.tipoDocumentoFaturamento)}}</span>';

		var acoesAdicionais = [
			//		{
			//			nome : 'download.objeto', label: 'Download', icone : 'icon-check', tooltip: 'Realizar o download deste faturamento'
			//		}
			//		,
			//		{
			//			nome : 'reprocessar', label: 'Reprocessar', icone : 'icon-check', tooltip: 'Reprocessar este faturamento'
			//		}
		];

		//FIXME: displayName e para usar o texto sem modificaçao.
		var columnDefs = [{
			name: 'Número',
			field: 'numero',
			enableSorting: false,
			sortFieldString: false,
			width: '10%',
			cellClass: 'text-left',
			pinnedLeft: false,
			funcao: ['exibir', 'row.entity.codigo']
		}, {
			name: 'Situação',
			field: 'situacao',
			cellTemplate: cellTemplateSituacao,
			tooltip: ['field'],
			width: '10%'
		}, {
			name: 'Tp.',
			field: 'tipo',
			width: '4%',
			cellTemplate: cellTemplateTipo,
			tooltip: ['field'],
			cellClass: 'text-center'
		}, {
			name: 'Itens',
			field: 'tipoFornecimento',
			cellTemplate: cellTemplateTipoItem,
			tooltip: ['field'],
			width: '4%',
			cellClass: 'text-center'
		}, {
			name: 'D/F',
			field: 'tipoDocumentoFaturamento',
			width: '4%',
			cellTemplate: cellTemplateDocFat,
			tooltip: [`${cellTemplateTipoTooltipDF}`],
			cellClass: 'text-center'
		},{
			name: 'Identificação',
			field: 'identificacao',
			tooltip: ['field.identificacao'],
			width: '20%'
		}, {
			name: 'Cliente/Sub (nome fantasia)',
			displayName: 'Cliente/Sub (nome fantasia)',
			field: 'nomeCliente',
			cellTemplate: cellTemplateCliente,
			tooltip: ['field.cliente.documento', ' - ', 'field.cliente.nome'], /*, detalhePessoa: 'field.cliente.codigo'*/
			width: '18%'
		}, {
			name: 'CL',
			displayName: 'CL',
			field: 'centroFinanceiro.identificadorFinal',
			width: '5%',
			cellClass: 'text-center',
			tooltip: ['field.centroFinanceiro.identificadorFinal', ' - ', 'field.centroFinanceiro.nome']
		}, {
			name: 'Contrato',
			field: 'centroFinanceiro.contratoTO.numero',
			cellClass: 'text-center',
			cellTemplate: '<a ng-show="true" href="/sgi/nv/app/main.html#!/comercial/contratos/{{row.entity.centroFinanceiro.contratoTO.codigo}}" target="_blank">{{ row.entity.centroFinanceiro.contratoTO.numero }}</a>',
			width: '10%'
		}, {
			name: 'Valor total',
			displayName: 'Valor total',
			field: 'valor',
			width: '10%',
			cellFilter: 'formatacaoMoeda',
			cellClass: 'text-right'
		}, {
			name: 'Opções',
			acoes: ['exibir'],
			width: '5%',
			pinnedRight: false,
			enableSorting: false
		}];

		$scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'faturamento', 'e');

		$scope.setDefaultParams = function () {
			$scope.paginationOptions.sortField = 'faturamento.numero';
			$scope.paginationOptions.sortFieldString = false;
			$scope.paginationOptions.sortDir = 'desc';
			//      $scope.paginationOptions.pageSize = 5; //parametrizar para utilização do teste automatizado.
		};

		$scope.setDefaultParams();

		$scope.getPage = function (page) {
			$scope.getPageGrid($scope.gridPesquisa, page);
		};

		/* FIM DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */

		$scope.buscar = function () {
			$rootScope.loadingProgress = true;
			$scope.setDefaultParams();
			$scope.getPage(1);
		};

		$scope.$on('crud-success', function (event, action, data) {
			$rootScope.loadingProgress = false;
		});

		$scope.$watch('result', function () {
			$rootScope.loadingProgress = false;
		});

		$scope.listar = function () {
			$state.go('faturamento.listar');
		};

		$scope.exibir = function (codigo) {
			window.open($state.href('faturamento.exibir', {objectId: codigo}, {absolute: true}));
		};

		$scope.exibirContrato = function (codigo) {
			window.open(`/sgi/nv/app/main.html#!/comercial/contratos/${codigo}`);
		};

		$scope.editar = function (item) {
			// $state.go('faturamento.exibir', {objectId: item.codigo});
			window.open(`/sgi/nv/app/main.html#!/faturamento/${codigo}/editar`);
		};

		$scope.getPessoas = function (texto) {
			return AutocompleteService.getPessoas({
				nome: texto
			});
		};

		$scope.reset = function () {
			$scope.result = null;
			$scope.object = {};
			$scope.selectedItemCidade = undefined;
			$scope.clear();
		};

		//AUTOCOMPLETE CIDADE
		$scope.querySearchCidade = (query) => {
			let filtro = query;
			let pCidade = Cidade.buscarListaPorNome(filtro).$promise;

			return pCidade.then(function(data) {
				let listaCidade = [];
				listaCidade.push(...data);
				return listaCidade;
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
				return [];
			});
		};

		$scope.selectedItemCidadeChange = (item) => {
			if (item && item.codigo) {
				$scope.object.codigoCidadePrestacaoServico = item.codigo;
			} else {
				$scope.object.codigoCidadePrestacaoServico = undefined;
			}
		};

	}


})();
