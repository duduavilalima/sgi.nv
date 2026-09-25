(function () {
	'use strict';

	angular
    	.module('app.previsao')

    	.controller('PrevisaoListarCtrl', listController)

    	.controller('PrevisaoDetailCtrl', detailController);


	function listController($scope, $state, $rootScope, $controller, $mdDialog, EnumService, DialogConfirm, AutocompleteService, Previsao, Unidade, Diretoria) {
		$controller('GridController', {
			$scope: $scope,
			Resource: Previsao
		});

		function init() {
			$scope.modulo = $state.current.data.modulo;
			$scope.titulo = $state.current.data.titulo;
			$scope.subtitulo = "Pesquisar";

			$scope.object = {};
			initCampoEmpresa($scope.object);
            initCampoDiretoria($scope.object);

		}

		function initCampoEmpresa(object) {
			$scope.listEmpresas = Unidade.unidadesporempresa();
		}

		function initCampoDiretoria(object) {
			$scope.listDiretorias = [];

			var pDiretoria = Diretoria.buscarDiretoriasDeContratos(null, {}).$promise;

			pDiretoria.then(function(data){

				$scope.listDiretorias.push({codigo: null, descricao: ''});
				angular.forEach(data, function(value, key) {
					$scope.listDiretorias.push(value);
				});

			}).catch(function(response) {
				 $scope.$emit('crud-critical', 'load', response);
			});
		}

		$scope.buscar = function() {
			$rootScope.loadingProgress = true;
			$scope.setDefaultParams();
			$scope.getPage(1);
		};

		$scope.novo = function() {
			$state.go('previsao.new');
		};

		$scope.$on('crud-success', function(event, action, data) {
			$rootScope.loadingProgress = false;
		});

		$scope.$watch('result', function() {
			$rootScope.loadingProgress = false;
		});

		$scope.reset = function() {

			$scope.result = null;
			$scope.clear();
		};

        var acoesAdicionais = [
            {
                nome : 'editar.objeto', label: 'Editar', icone : 'icon-pencil', tooltip: 'Editar os dados desta previsão'
            }
        ];

		/** INICIO DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA  */
		var columnDefs = [
			{
				name: 'Despesa',
				field: 'despesaFluxoCaixa.descricao',
				width: '35%',
				pinnedLeft: false,
				funcao: ['exibir', 'row.entity']
			},
			{
				name: 'Empresa',
				field: 'unidade.codnome',
				width: '20%'
			},
			{
				name: 'Diretoria',
 				field: 'diretoria.nome',
				width: '15%'
			},
			{
				name: 'Data',
   				field: 'data',
				width: '15%',
				cellFilter: 'formatacaoData',
                cellClass: 'text-center'
			},
			{
				name: '	Valor',
				field: 'valor',
				width: '10%',
				cellFilter: 'formatacaoMoeda',
                cellClass: 'text-right'
			},
			{
				name: 'Opções',
				width: '5%',
				pinnedRight: false,
				enableSorting: false,
				acoesAdicionais: acoesAdicionais
			}
		];

		$scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'previsao', 'e');

		$scope.setDefaultParams = function() {
			$scope.paginationOptions.sortField = 'codigo';
			$scope.paginationOptions.sortFieldString = false;
			$scope.paginationOptions.sortDir = 'asc';
		}
		$scope.setDefaultParams();

		$scope.getPage = function(page) {
			$scope.getPageGrid($scope.gridPesquisa, page);
		};

		$scope.exibir = function(item) {
			$state.go('previsao.view', { objectId: item.codigo });
		};

		$scope.editar = function(item) {
			$state.go('previsao.edit', { objectId: item.codigo });
		};


		init();

		/** FIM DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA  */

	};

	function detailController($scope, $state, $stateParams, $mdDialog, $rootScope, EnumService, AutocompleteService, DialogConfirm, Previsao, Unidade, Diretoria) {

		function init() {

			$scope.habilitar = true;
			$scope.modulo = $state.current.data.modulo;
			$scope.titulo = $state.current.data.titulo;
			$scope.object = null;

			if ($scope.screenMode === 'NEW') {
				newObj();
			} else {
				loadObj();
			}
		}

		function loadObj() {
			var pPrevisao = Previsao.get({id: $stateParams.objectId }).$promise;

			pPrevisao.then(function(data) {
				$scope.object = data;

				if ($scope.screenMode === 'EDIT') {
					initEdit(data);
				}

			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});
		}

		$scope.editar = function() {
			$state.go('previsao.edit', { objectId: $scope.object.codigo });
		};

		$scope.retornar = function() {
			$state.go('previsao.list');
		};

		$scope.cancelarEdit = function() {
			if ($scope.screenMode === 'NEW') {
				$state.go('previsao.list');
			} else if ($scope.screenMode === 'EDIT') {
				$state.go('previsao.view', { objectId: $scope.object.codigo });
			}
		};

		$scope.salvar = function(){
			if($scope.screenMode === 'NEW'){
				$scope.object.$save()
				.then(function(data){

					$scope.notification.success("Novo", "Previsão salva com sucesso.");
					$state.go('previsao.view', {objectId: data.codigo});

				}).catch(function(response){
					$scope.$emit('crud-critical', 'save', response);
				});
			} else if ($scope.screenMode === 'EDIT') {
				$scope.object.$update({id: $scope.objectId})
				.then(function(data) {

					$scope.notification.success("Edição", "Previsão atualizada com sucesso");
					$state.go('previsao.view', {objectId: data.codigo});

				}).catch(function(response) {
					$scope.$emit('crud-critical', 'save', response);
				});
			}
		};


		$scope.remover = function(item) {
			$scope.showConfirm(item);
		};

		$scope.remover = function(ev) {
				var tituloDialog = "Remover previsão: Confirmação";
				var msgDialog = "Deseja realmente remover esta previsão (esta operação não poderá ser desfeita)?";
				var tituloBotaoConfirmar = "CONFIRMAR";
				var tituloBotaoCancelar = "CANCELAR";

				var funcaoCancelar = function(fnHide) {
					fnHide();
				};

				var funcaoConfirmar = function(object, fnHide) {
					var pPrevisao = Previsao.remover({id: object.codigo}).$promise;

					pPrevisao.then(function(data){

						var title = 'Remover Previsão';
						var message = 'Previsao removida com sucesso.';

						$scope.notification.success(title, message);

						fnHide();
						$state.go('previsao.list');

					}).catch(function(response){
						console.error('crud-critical', 'dialog', response);
						$scope.notification.criticalError(response.status, response.statusText, response.data);
						fnHide();
					});
				};

				DialogConfirm.show(ev, $scope.object, tituloDialog, msgDialog, tituloBotaoConfirmar, tituloBotaoCancelar, funcaoConfirmar, funcaoCancelar);
		};


		function newObj() {
			$scope.object = new Previsao();
			initCampoEmpresa($scope.object);
			initCampoDiretoria($scope.object);
		}

		$scope.selectedItemDespesa = '';

		function initEdit(object) {
			$scope.selectedItemDespesa = $scope.object.despesaFluxoCaixa;
			initCampoEmpresa($scope.object);
            initCampoDiretoria($scope.object);
		}

		function initCampoEmpresa(object) {
			$scope.listEmpresas = Unidade.unidadesporempresa();
		}

		function initCampoDiretoria(object) {
			$scope.listDiretorias = [];

			var pDiretoria = Diretoria.buscarDiretoriasDeContratos(null, {}).$promise;

			pDiretoria.then(function(data){

				$scope.listDiretorias.push({codigo: null, descricao: ''});
				angular.forEach(data, function(value, key) {
					$scope.listDiretorias.push(value);
				});

			}).catch(function(response) {
				 $scope.$emit('crud-critical', 'load', response);
			});
		}

		//AUTOCOMPLETE DESPESA
		$scope.querySearchDespesa = function(query) {
			var listaDespesa = [];
			var filtro = {};
			filtro.nome = query;

			var pDespesa = AutocompleteService.getDespesaFluxoCaixa(filtro).$promise;

			return pDespesa.then(function(data) {

				angular.forEach(data, function(value, key) {
					listaDespesa.push(value);
				});

				return listaDespesa;
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
				listaDespesa = [];
				return listaDespesa;
			});

		};

		$scope.selectedItemDespesaChange = function(item) {
			$scope.object.despesaFluxoCaixa = item;
		};
		//FIM AUTOCOMPLETE


		init();

	};

})();
