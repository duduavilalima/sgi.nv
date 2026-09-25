(function () {
	'use strict';

	angular
		.module('app.lancamentos')

		.controller('LancamentosDetailCtrl', detailController);

	function detailController($scope, $state, $stateParams, $mdDialog, $rootScope, Lancamentos, Unidade, ContaCorrente, ContaContabil, AutocompleteService, DialogConfirm) {

		init();

		function init() {
			$scope.modulo = $state.current.data.modulo;
			$scope.titulo = $state.current.data.titulo;
			$scope.object = null;

			$scope.selectedItemContaContabil = null;

			if ($scope.screenMode === 'NEW') {
				newObj();
			} else {
				loadObj();
			}
		}

		function loadObj() {
		    $scope.action = true;
		    $rootScope.loadingProgress = true;
			var pLancamento = Lancamentos.get({id: $stateParams.objectId }).$promise;

			pLancamento.then(function(data) {
				$scope.object = data;

				if ($scope.screenMode === 'EDIT') {
					if (edicaoValida(data)) {
						initEdit(data);
					} else {
						$state.go('lancamentos.view', { objectId: data.codigo });
						$scope.notification.error("", "Não é permitido edição par ao status atual do registro.");
					}
				}
				$scope.action = false;
				$rootScope.loadingProgress = false;
			}).catch(function(response) {
			    $scope.action = false;
			    $rootScope.loadingProgress = false;
				$scope.$emit('crud-critical', 'load', response);
			});
		}

		function newObj() {
			$scope.listEmpresas = [];
			$scope.listBancos = [];
			$scope.listAgencias = [];

			$scope.object = new Lancamentos();
			$scope.object.status = "Aberto";
			$scope.object.debitoCredito = "D";

			initCampoEmpresa($scope.object);
			initOnChanges();
			initCampoDebitoCredito();
		}

		function initEdit(object) {
			$scope.listEmpresas = [];
			$scope.listBancos = [];
			$scope.listAgencias = [];

			initCampoDebitoCredito();

			var pUnidade = Unidade.page(null, {tipoUnidade: "Matriz"}).$promise;
			var pContaCorrente = ContaCorrente.obterContas(null, { pessoa: {codigo: object.unidade.codigo} }).$promise;

			pUnidade.then(function(data){
				$scope.listEmpresas.push({codigo: null, codnome: ""});
				angular.forEach(data, function(value, key) {
					$scope.listEmpresas.push(value);
				});

				return pContaCorrente;

			}).then(function(data){
				$scope.listBancos.push({codigo: null, nome : ''});
				$scope.listAgencias.push({codigo: null});

				angular.forEach(data, function(value, key) {
					$scope.listBancos.push(value.banco);

					if(!angular.isUndefined(object.banco) && object.banco !== null && object.banco.codigo === value.banco.codigo) {
						$scope.listAgencias.push(value);
					}

				});

				$scope.selectedItemContaContabil = object.contaContabil;

				initOnChanges();

			}).catch(function(response) {
				 $scope.$emit('crud-critical', 'load', response);
			});
		}

		//TODO: Refatorar validação de status
		var edicaoValida = (object) => {
			let resultado = true;

			if (object && object.status !== "Aberto") {
				resultado = false;
			}

			return resultado;
		};

		function initCampoEmpresa(object) {
			$scope.listEmpresas = [];

			var pUnidade = Unidade.page(null, {tipoUnidade: "Matriz"}).$promise;

			pUnidade.then(function(data){
				$scope.listEmpresas.push({codigo: null, codnome: ""});
				angular.forEach(data.content, function(value, key) {
					$scope.listEmpresas.push(value);
				});

			}).catch(function(response) {
				 $scope.$emit('crud-critical', 'load', response);
			});
		}

		function initCampoBanco(object) {
			$scope.listBancos = [];

			if(!angular.isUndefined(object.unidade) && object.unidade !== null && object.unidade.codigo !== null) {
				var objContaCorrente = {};
				var pContaCorrente = ContaCorrente.obterContas(null, { pessoa: {codigo: object.unidade.codigo} }).$promise;

				pContaCorrente.then(function(data) {
					$scope.listBancos.push({codigo: null, nome : ''});

					angular.forEach(data, function(value, key) {
						$scope.listBancos.push(value.banco);
					})

				}).catch(function(response) {
					$scope.$emit('crud-critical', 'load', response);
				});
			}
		}

		function initCampoAgencia(object) {
			$scope.listAgencias = [];

			if(!angular.isUndefined(object.unidade) && object.unidade !== null && object.unidade.codigo !== null) {
				var objContaCorrente = {};
				var pContaCorrente = ContaCorrente.obterContas(null, { pessoa: {codigo: object.unidade.codigo} }).$promise;

				pContaCorrente.then(function(data) {
					$scope.listAgencias.push({codigo: null});

					angular.forEach(data, function(value, key) {
						if(!angular.isUndefined(object.banco) && object.banco !== null && object.banco.codigo === value.banco.codigo) {
							$scope.listAgencias.push(value);
						}
					});

				}).catch(function(response) {
					$scope.$emit('crud-critical', 'load', response);
				});
			}
		}

		function initCampoDebitoCredito() {
			$scope.listDebitoCredito = ["C", "D"];
		}

		$scope.querySearchContaContabil = function(query) {
			var listaContaContabil = [];

            var filtro = {};
            filtro.unidade = {};
            filtro.unidade.codigo = $scope.object.unidade.codigo;
            filtro.nome = query;

			var pContaContabil = AutocompleteService.getContaContabilPorEmpresa(filtro).$promise;

			return pContaContabil.then(function(data) {

				angular.forEach(data, function(value, key) {
					listaContaContabil.push(value);
				});

				return listaContaContabil;
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
				listaContaContabil = [];
				return listaContaContabil;
			});

		};

		/** ONCHANGES */
		function initOnChanges() {

			$scope.changeEmpresa = function() {
				if($scope.object.unidade.codigo === null) {
					delete $scope.object.unidade;
				}

				initCampoBanco($scope.object);

				$scope.object.banco = null;
				$scope.object.contaCorrente = null;
				$scope.listAgencias = [];

//				delete $scope.object.contaContabil;
//				document.querySelectorAll('input[type=search]')[0].value = '';
			};

			$scope.changeBanco = function() {
				if($scope.object.banco.codigo === null) {
					delete $scope.object.banco;
				}

				initCampoAgencia($scope.object);

				$scope.object.contaCorrente = null;
			};

			$scope.changeAgencia = function() {
				if($scope.object.contaCorrente.codigo === null) {
					delete $scope.object.contaCorrente;
				}
			};

			$scope.changeDebitoCredito = function() {
				if ($scope.object.debitoCredito === "") {
					delete $scope.object.debitoCredito;
				}
			};

		}



		$scope.editar = function() {
			$state.go('lancamentos.edit', { objectId: $scope.object.codigo });
		};

		$scope.retornar = function() {
			$state.go('lancamentos.list');
		};

		$scope.cancelarEdit = function() {
			if ($scope.screenMode === 'NEW') {
				$state.go('lancamentos.list');
			} else if ($scope.screenMode === 'EDIT') {
				$state.go('lancamentos.view', { objectId: $scope.object.codigo });
			}
		};

		$scope.selectedItemChange = function(item) {
			$scope.object.contaContabil = item;
		};

		$scope.salvar = function() {
		    $scope.action = true;
			if ($scope.screenMode === 'NEW') {

				var pSave = $scope.object.$save();

				pSave.then(function(data){
					$scope.notification.success("Novo", "Lançamento inserido com sucesso.");
					$state.go('lancamentos.view', { objectId: data.codigo });
					$scope.action = false;
				}).catch(function(response){
				    $scope.action = false;
					$scope.$emit('crud-critical', 'save', response);
				});

			} else if ($scope.screenMode === 'EDIT') {

				var pUpdate = $scope.object.$update({ id: $scope.object.codigo });

				pUpdate.then(function(data){
					$scope.notification.success("Edição", "Lançamento atualizado com sucesso.");
					$state.go('lancamentos.view', { objectId: data.codigo });
					$scope.action = false;
				}).catch(function(response){
				    $scope.action = false;
					$scope.$emit('crud-critical', 'save', response);
				});
			}
		};

		$scope.salvarNovo = function() {
		    $scope.action = true;
			var pSave = $scope.object.$save();

			pSave.then(function(data){
				$scope.notification.success("Novo", "Lançamento inserido com sucesso.");
				$state.reload();
				$scope.action = false;
			}).catch(function(response){
			    $scope.action = false;
				$scope.$emit('crud-critical', 'save', response);
			});
		};

		/** REMOVER LANÇAMENTO */
		$scope.remover = function(ev) {
			var tituloDialog = "Lançamento - Remover: Confirmação";
			var msgDialog = "Deseja realmente remover este lançamento (esta operação não poderá ser desfeita)?";
			var tituloBotaoConfirmar = "CONFIRMAR";
			var tituloBotaoCancelar = "CANCELAR";

			var funcaoCancelar = function(fnHide) {
				fnHide();
			};

			var funcaoConfirmar = function(object, fnHide) {
				var pLancamento = Lancamentos.delete({id: object.codigo}).$promise;

				pLancamento.then(function(data){

					var title = 'Remover Lançamento';
					var message = 'Lançamento ' + object.numero + ' removido com sucesso.';
					$scope.notification.success(title, message);

					fnHide();
					$state.go('lancamentos.list');

				}).catch(function(response){
					console.error('crud-critical', 'dialog', response);
					$scope.notification.criticalError(response.status, response.statusText, response.data);
				});
			};

			DialogConfirm.show(ev, $scope.object, tituloDialog, msgDialog, tituloBotaoConfirmar, tituloBotaoCancelar, funcaoConfirmar, funcaoCancelar);
		};

		/** CANCELAR LANÇAMENTO */
		$scope.cancelar = function(ev) {
			var tituloDialog = "Lançamento - Cancelar: Confirmação";
			var msgDialog = "Deseja realmente cancelar este lançamento (esta operação não poderá ser desfeita)?";
			var tituloBotaoConfirmar = "CONFIRMAR";
			var tituloBotaoCancelar = "CANCELAR";

			var funcaoCancelar = function(fnHide) {
				fnHide();
			};

			var funcaoConfirmar = function(object, fnHide) {
				var pLancamento = Lancamentos.cancelar(object.codigo).$promise;

				pLancamento.then(function(data){
					fnHide();

					var title = 'Cancelar Lançamento';
					var message = 'Lançamento ' + object.numero + ' cancelado com sucesso.';
					$scope.notification.success(title, message);

					loadObj();
				}).catch(function(response){
					console.error('crud-critical', 'dialog', response);
					$scope.notification.criticalError(response.status, response.statusText, response.data);
				});
			};

			DialogConfirm.show(ev, $scope.object, tituloDialog, msgDialog, tituloBotaoConfirmar, tituloBotaoCancelar, funcaoConfirmar, funcaoCancelar);
		};

	};

})();
