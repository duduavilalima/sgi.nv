(function(){

	'use strict'

	angular
		.module('app.historicoconciliacaobancaria')

		.controller('HistoricoConciliacaoBancariaDetailCtrl', detailController);

function detailController($scope, $controller, $state, $mdDialog, $rootScope, $timeout, $stateParams, HistoricoConciliacaoBancaria, EnumService, DialogConfirm, HistoricoConciliacaoContaContabil){

	function init() {

		$scope.modulo = $state.current.data.modulo;
		$scope.titulo = $state.current.data.titulo;
		$scope.subtitulo = $state.current.data.subtitulo;
		$scope.desabilitarBotaoNovaCC = false;

		if ($scope.screenMode === 'NEW') {
			newObj();
		} else {
			loadObj();
		}
	};

    function loadObj() {
	   var pHistoricoConciliacao = HistoricoConciliacaoBancaria.get({id: $stateParams.objectId }).$promise;

	   pHistoricoConciliacao.then(function(data) {
			$scope.object = data;

			 carregarTipoAcao();
			 carregarSituacao();
			 carregarAbaDadosContabeis();

	   }).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
	   });
	};


	function newObj() {
		$scope.object = new HistoricoConciliacaoBancaria();
		$scope.object.situacao = "Ativo";

		carregarTipoAcao();
	}

	function carregarTipoAcao() {
		 EnumService.enumValues({ pacote: 'br.com.araujoabreu.sgi.tesouraria.domain', classe: 'HistoricoConciliacao', enum: 'TipoAcao' })
		 .$promise.then(function(data) {

			$scope.listTipoAcao = [];
			$scope.listTipoAcao.push('');

			angular.forEach(data, function(value) {
			 $scope.listTipoAcao.push(value);
			});
		 }).catch(function(response) {
			 $scope.$emit('crud-critical', 'load', response);
		 });
	};

	function carregarSituacao() {
	 EnumService.enumValues({ pacote: 'br.com.araujoabreu.sgi.enumerators', classe: 'null', enum: 'Situacao' })
	 .$promise.then(function(data) {

		$scope.situacaoList = [];

		angular.forEach(data, function(value) {
			var temp1 = {};
			temp1.nome = value;
		 $scope.situacaoList.push(temp1);
		});
	 }).catch(function(response) {
		 $scope.$emit('crud-critical', 'load', response);
	 });
	};

	function carregarAbaDadosContabeis(){
		 HistoricoConciliacaoBancaria.buscarHistoricoCCC({id: $scope.objectId}).$promise.then(function(data){

			$scope.listaContaContabil = angular.copy(data);

			if($scope.listaContaContabil.length === 2){
				$scope.desabilitarBotaoNovaCC = true;
			}else{
				$scope.desabilitarBotaoNovaCC = false;
			}

		 }).catch(function(response){
		 	$scope.$emit('crud-critical', 'historico', response);

		 });
	};

	 $scope.editar = function() {
		$state.go('historico_conciliacao_bancaria.edit', { objectId: $scope.object.codigo });
	 };

	$scope.retornar = function() {
		$state.go('historico_conciliacao_bancaria.list');
	};

	$scope.cancelarEdit = function() {
		if ($scope.screenMode === 'NEW') {
			$state.go('historico_conciliacao_bancaria.list');

		} else if ($scope.screenMode === 'EDIT') {
			$state.go('historico_conciliacao_bancaria.view', { objectId: $scope.object.codigo });
		}
	};

	/* Inicio da Dialog Remover histórico da conciliação bancária */
    	$scope.removerH = function(item) {
    		// Chamada do serviço que criará um dialog de confirmação.
    		DialogConfirm.show(
    			null,
    			$scope.object,
    			'Remover histórico da conciliação bancária: Confirmação',
    			'Deseja realmente remover este histórico da conciliação bancária (esta operação não poderá ser desfeita)?',
    			'Confirmar',
    			'Cancelar',
    			confirmRemoverH,
    			cancelRemoverH);
    	};

    	// Função adicionada na chamada do dialog para confirmação.
    	function confirmRemoverH(object, fnHide) {

    		// Remover da tela de exibição!
    		HistoricoConciliacaoBancaria.remover({id: object.codigo})
    			.$promise.then(function(data) {

    				$scope.notification.success("Histórico da conciliação bancária removido com sucesso.");

    				// Esta função veio do dialog.
    				fnHide();
    				$state.go('historico_conciliacao_bancaria.list');


    			}).catch(function(response) {
    				$scope.$emit('crud-critical', 'dialog', response);
    				$scope.notification.criticalError(response.status, response.statusText, response.data);
                    fnHide();
    		});
    	};

    	function cancelRemoverH(fnHide) {
    		fnHide();
    	};
    	/* Fim da Dialog Remover histórico da conciliação bancária */

	$scope.salvar = function() {
	    $scope.action = true;
		if ($scope.screenMode === 'NEW') {
			var pSave = $scope.object.$save();

			pSave.then(function(data) {
				$scope.notification.success("Novo", "Histórico de conciliação bancária inserido com sucesso.");
				$state.go('historico_conciliacao_bancaria.view', { objectId: data.codigo });
				$scope.action = false;
			}).catch(function(response){
				$scope.notification.error("", response.data.message);
				$scope.action = false;
			});
		} else if ($scope.screenMode === 'EDIT') {

			var pUpdate = $scope.object.$update({ id: $scope.object.codigo });

			pUpdate.then(function(data){
				$scope.notification.success("Edição", "Histórico de conciliação bancária atualizado com sucesso.");
				$state.go('historico_conciliacao_bancaria.view', { objectId: data.codigo });
				$scope.action = false;
			}).catch(function(response){
				$scope.notification.error("", response.data.message);
				$scope.action = false;
			});
		}
	};

	/* Inicio da Dialog Excluir Histórico Conciliação Conta Contábil */
		$scope.excluirDadosContabeis = function(item) {
			// Chamada do serviço que criará um dialog de confirmação.
			DialogConfirm.show(
				null,
				item,
				'Remover associação conta contábil x histórico: Confirmação',
				'Deseja realmente remover esta associação entre a conta contábil e o histórico da conciliação bancária (esta operação não poderá ser desfeita)?',
				'Confirmar',
				'Cancelar',
				confirmRemover,
				cancelRemover);
		};

		// Função adicionada na chamada do dialog para confirmação.
		function confirmRemover(item, fnHide) {

			// Remover da tela de exibição!
			HistoricoConciliacaoContaContabil.remover({id: item.codigo})
				.$promise.then(function(data) {

					$scope.notification.success("Conta contábil removida com sucesso.");
                    carregarAbaDadosContabeis();

					fnHide();

				}).catch(function(response) {
					$scope.$emit('crud-critical', 'dialog', response);
					$scope.notification.criticalError(response.status, response.statusText, response.data);
					fnHide();
			});
		};

		function cancelRemover(fnHide) {
			fnHide();
		};
		/* Fim da Dialog Remover histórico da conciliação bancária */

	/* Inicio da Dialog Conta Contabil */
	$scope.novaContaContabil = function(item) {

		$mdDialog.show({
			controller: DialogContaContabil,
			locals: {
				listhistoricoConciliacao: $scope.listaContaContabil,
				notification: $scope.notification,
				historicoConciliacao: $scope.object,
				item: item
			},
			templateUrl: 'partials/tesouraria/cadastros/historico_conciliacao_bancaria/dialogs/dialog_associar_conta_contabil.html',
			parent: angular.element(document.body),
			clickOutsideToClose: false,
			fullscreen: false
		});
	};

	function DialogContaContabil($scope, $timeout, $mdDialog, $state, listhistoricoConciliacao, historicoConciliacao, notification, AutocompleteService, Unidade, item ) {

		$scope.title = null;
		$scope.contaContabil = null;
		$scope.object = {};
		$scope.habilitarComboEmpresa = false;
		$scope.habilitarContaContabil = true;

		if(item == null){
			$scope.title = "Novo";
			$scope.habilitarComboEmpresa = true;

		}else{
			$scope.title = "Edição";
			$scope.habilitarContaContabil = false;
		}

		var aux1 = null;

		function campoEmpresa() {
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

		campoEmpresa();

		function checkListaContaContabil() {

			if ( !angular.isUndefined(item) && item != null ) {

				var contaContabilPorTipo = null;

				angular.forEach(listhistoricoConciliacao, function(value, key) {

					if(item.codigo == value.codigo){
						contaContabilPorTipo = value.contaContabil;
						$scope.object.unidade  = item.unidade;
					}
				})

				if (contaContabilPorTipo != null) {
					aux1 = contaContabilPorTipo;
					$scope.contaContabil = contaContabilPorTipo;
				}
			}
		}

		checkListaContaContabil();

		$scope.changeEmpresa = function() {

			$scope.habilitarContaContabil = false;

			if ($scope.object.unidade.codigo === null) {
				delete $scope.object.unidade;
			}
			$scope.selectedItemContaContabil = null;
			$scope.object.contaContabil = null;
			$scope.searchTextContaContabil = "";

		};

		// INICIO DO AUTOCOMPLETE DE CONTA CONTÁBIL
		function autoCompleteContaContabil(){

			$scope.searchTextContaContabil = null;
			$scope.selectedItemContaContabil = aux1;
			$scope.resultsContaContabil = [];

			$scope.fnSelectedItemContaContabil = function(item) {
				$scope.contaContabil = item;
			};

			$scope.querySearchContaContabil = function(query) {
				$rootScope.loadingProgress = true;

				var filtro = {};
				filtro.unidade = {};
				filtro.unidade.codigo = $scope.object.unidade.codigo;
				filtro.nome = query;

				var resultContaContabilPromise = AutocompleteService.getContaContabilPorEmpresa(filtro).$promise;

				var contaContabilArray = [];

				return resultContaContabilPromise.then(function(contaContabil) {

					$rootScope.loadingProgress = false;

					angular.forEach(contaContabil, function(contaContabil) {
						contaContabilArray.push(contaContabil);
					})
					return contaContabilArray;
				});
			}
		} // FIM DO AUTOCOMPLETE DE CONTA CONTÁBIL

		autoCompleteContaContabil();

		$scope.confirm = function() {

			if ($scope.contaContabil == null) {
				notification.error("Conta contábil é um campo de preenchimento obrigatório.");

			} else {

				var jaTemNaLista = false;

				angular.forEach(listhistoricoConciliacao, function(value, key) {

					if((item == null) && (value.unidade.codigo === $scope.object.unidade.codigo)){
						jaTemNaLista = true;
						notification.atencao("Conta contábil já foi cadastrada para esta empresa.");
					}
					return;
				});

				if (!jaTemNaLista) {
					var objPagamento = {codigo: listhistoricoConciliacao.codigo };

					$scope.object.historicoConciliacao = historicoConciliacao;
					$scope.object.contaContabil = $scope.contaContabil;
					$scope.object.numeroContaContabil = $scope.contaContabil.numero+ " - "+ $scope.contaContabil.descricao;
					$scope.object.unidade = $scope.object.unidade;

					if(item == null){
						// Serve p/ salvar as abas separadamente
						HistoricoConciliacaoContaContabil.save($scope.object)
							.$promise.then(function(data) {

							listhistoricoConciliacao.push($scope.object);
							notification.success("Conta contábil inserida com sucesso.");
							carregarAbaDadosContabeis();

						}).catch(function(response){
							$scope.$emit('crud-critical', 'save', response);
						});
					}else{

						$scope.object.codigo = item.codigo;

						HistoricoConciliacaoContaContabil.update({ id: $scope.object.codigo }, $scope.object)
							.$promise.then(function(data) {

							notification.success("Conta contábil atualizada com sucesso.");
							carregarAbaDadosContabeis();

						}).catch(function(response){
							$scope.$emit('crud-critical', 'update', response);
						});
					}
				}

				$scope.hide();

			}
		};

		$scope.hide = function() {
			$mdDialog.hide();
		};
		$scope.cancel = function() {
			$mdDialog.cancel();
		};
	}/* Fim da Dialog  Conta Contabil */


	init();

};
})();
