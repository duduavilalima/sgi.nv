(function () {
	'use strict';

	angular
		.module('app.perfil')
		.controller('PerfilListarCtrl', listController)
		.controller('PerfilDetailCtrl', detailController);

	var $moduleUrl = 'partials/administracao/perfil';

	// TODO Remover após o modelo ter sido definido
	var opcoesTipoAcesso = ["Interno", "Fornecedor"];
	var opcoesTipoAcessoPesquisa = [""].concat(opcoesTipoAcesso);
	var opcoesObrigatoriedade = ["Gerência", "Dados bancários"];
	var opcoesObrigatoriedadePesquisa = [""].concat(opcoesObrigatoriedade);
	var opcoesObrigaGerencia = [ {mensagem: "Sim", valor: true} , {mensagem: "Não", valor: false} ];
	var opcoesObrigaDadosBancarios = opcoesObrigaGerencia;
	var opcoesFuncionalidades = [
		{
			modulo: {
				sistema: { nome: "Sistema 1" },
				nome: "Módulo 1"
			},
			descricao: "Teste 1",
			tipo: "Menu",
			label: "Teste Menu 1"
		}, {
			modulo: {
				sistema: { nome: "Sistema 2" },
				nome: "Módulo 2"
			},
			descricao: "Teste 2",
			tipo: "Menu",
			label: "Teste Menu 2"
		}, {
			modulo: {
				sistema: { nome: "Sistema 3" },
				nome: "Módulo 3"
			},
			descricao: "Teste 3",
			tipo: "Menu",
			label: "Teste Menu 3"
		}
	];

	function listController($scope, $state, $rootScope, $controller, $mdDialog, Perfil) {
		$controller('GridController', {
			$scope: $scope,
			Resource: Perfil
		});

		var columnDefs = [
			{	name: 'Nome',
				field: 'nome'
			},{
				name: 'Descrição',
				field: 'descricao'
			},{
				name: 'Tipo de acesso',
				field: 'tipoAcesso',
				cellClass: 'text-center'
			},{
				name: 'Obrigatório gerência',
				field: 'obrigatorioGerencia',
				cellClass: 'text-center'
			},{
				name: 'Obrigatório banco',
				field: 'obrigatorioBanco',
				cellClass: 'text-center'
			},{
				name: 'Opções',
				width: '5%',
				pinnedRight: false,
				enableSorting: false,
				acoesAdicionais: acoesAdicionais
			}
		];

		var acoesAdicionais = [
			{
				nome : 'editar.objeto',
				label: 'Editar',
				icone : 'icon-pencil',
				tooltip: 'Editar perfil'
			}, {
				nome : 'remover.objeto',
				label: 'Remover',
				icone : 'icon-pencil',
				tooltip: 'Remover perfil'
			}
		];

		function init() {
			$scope.modulo = $state.current.data.modulo;
			$scope.titulo = $state.current.data.titulo;
			$scope.subtitulo = $state.current.data.subtitulo;

			// Propriedades
			$scope.listTipoAcesso = opcoesTipoAcessoPesquisa;
			$scope.listObrigatoriedade = opcoesObrigatoriedadePesquisa;
			$scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'perfil', 'e');

			// Funções
			$scope.buscar = function() {
				$rootScope.loadingProgress = true;
				$scope.setDefaultParams();
				$scope.getPage(1);
			}

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

			$scope.exibirGrid = function(grid) {
				return angular.isDefined(grid) && grid != null && grid.totalItems > 0;
			};

			$scope.setDefaultParams = function() {
				$scope.paginationOptions.sortField = 'codigo';
				$scope.paginationOptions.sortFieldString = false;
				$scope.paginationOptions.sortDir = 'asc';
			}
			$scope.setDefaultParams();

			$scope.getPage = function(page) {
				$scope.getPageGrid($scope.gridPesquisa, page);
			};

			$scope.exibir = function(codigo) {
				$state.go('perfil.view', { objectId: codigo });
			};

			$scope.editar = function(item) {
				$state.go('perfil.edit', { objectId: item.codigo });
			};

			$scope.remover = function(item) {
				$scope.showConfirm(item);
			};
		}

		init();
	}

	function detailController($scope, $state, $rootScope, $controller, $mdDialog) {

		var configAlertaRemover =  {
			templateUrl: $moduleUrl + '/dialogs/dialog_remover_perfil.html',
			scope: $scope,
			preserveScope: true,
			clickOutsideToClose: true,
			escapeToClose: true
		};

		var alertaRemover = $mdDialog.confirm()
			.title('Remover perfil: Confirmação')
			.textContent('Deseja realmente remover este perfil (esta operação não poderá ser desfeita)?')
			.ok('REMOVER')
			.cancel('CANCELAR');

		function init() {

			$scope.modulo = $state.current.data.modulo;
			$scope.titulo = $state.current.data.titulo;
			$scope.subtitulo = $state.current.data.subtitulo;

			$scope.listTipoAcesso = opcoesTipoAcesso;
			$scope.listObrigaGerencia = opcoesObrigaGerencia;
			$scope.listObrigaDadosBancarios = opcoesObrigaDadosBancarios;
			$scope.listFuncionalidades = opcoesFuncionalidades;

			$scope.object = { codigo: 1 };
			$scope.object.funcionalidades = [
				{
					modulo: {
						sistema: { nome: "Sistema 1" },
						nome: "Módulo 1"
					},
					descricao: "Teste 1",
					tipo: "Menu",
					label: "Teste Menu 1"
				}, {
					modulo: {
						sistema: { nome: "Sistema 2" },
						nome: "Módulo 2"
					},
					descricao: "Teste 2",
					tipo: "Menu",
					label: "Teste Menu 2"
				}, {
					modulo: {
						sistema: { nome: "Sistema 3" },
						nome: "Módulo 3"
					},
					descricao: "Teste 3",
					tipo: "Menu",
					label: "Teste Menu 3"
				}
			];
			$scope.alertaRemover = null;

			$scope.editar = function() {
				$state.go('perfil.edit', { objectId: $scope.object.codigo });
			};

			$scope.retornar = function() {
				$state.go('perfil.listar');
			};

			$scope.iniciarRemover = function() {
				$state.alertaRemover = $mdDialog.show(configAlertaRemover);
			};

			$scope.cancelarRemover = function() {
				$mdDialog.hide($state.alertaRemover);
				$state.alertaRemover = null;
			};

			$scope.remover = function() {
				$mdDialog.hide($state.alertaRemover);
				$state.alertaRemover = null;
				$state.go('perfil.listar');
			};

			$scope.salvar = function() {
				if ($scope.screenMode === 'NEW') {
					$state.go('perfil.listar');
				} else {
					$state.go('perfil.view', { objectId: $scope.object.codigo });
				}
			};

			$scope.cancelarEdicao = function() {
				if ($scope.screenMode === 'NEW') {
					$state.go('perfil.listar');
				} else {
					$state.go('perfil.view', { objectId: $scope.object.codigo });
				}
			}

			if ($scope.screenMode === 'NEW') {
				newObj();
			} else {
				loadObj();
			}
		}

		function loadObj(){
			/*
			var pPerfil = Perfil.get({id: $stateParams.objectId }).$promise;

			pPerfil.then(function(data) {
				$scope.object = data;
				// ...

			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});
			*/
		}

		function newObj() {
			/*
			$scope.object = new Perfil();
			// ...
			*/
		}

		init();
	}

})();
