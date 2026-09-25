(function(){

	'use strict'

	angular
		.module('app.despesas')

		.controller('DespesasListarCtrl', listController)

		.controller('DespesasDetailCtrl', detailController);

function listController($scope, $controller, $state, EnumService, $mdDialog, $rootScope, $timeout, Despesas, UsuarioLogadoService){

    $controller('GridController', {$scope: $scope, Resource: Despesas});

	function init() {
		$scope.modulo = $state.current.data.modulo;
		$scope.titulo = $state.current.data.titulo;
		$scope.subtitulo = $state.current.data.subtitulo;
		$scope.object = {};

		$scope.usuario = UsuarioLogadoService.getUsuarioLogado();

		carregarSituacao();
		getUsuarioLogado();
	}

	function carregarSituacao() {
		 EnumService.enumValues({ pacote: 'br.com.araujoabreu.sgi.enumerators', classe: 'null', enum: 'Situacao' })
		 .$promise.then(function(data) {

			$scope.situacaoList = [];
			var temp = {};
			temp.nome = '';
			$scope.situacaoList.push(temp);

			angular.forEach(data, function(value) {
				var temp1 = {};
				temp1.nome = value;
			 $scope.situacaoList.push(temp1);
			});
		 }).catch(function(response) {
			 $scope.$emit('crud-critical', 'load', response);
		 });
	};

	var objIconEditar = { nome : 'editar.objeto', label: 'Editar', icone : 'icon-pencil', tooltip: 'Editar os dados desta despesa' };
	var acoesAdicionais = [];

	function getUsuarioLogado(){

		UsuarioLogadoService.possuiUmPerfilLista('admin').$promise.then(function(data) {

			if (data.possuiUmPerfilLista) {
				acoesAdicionais.push(objIconEditar);

			} else {
				acoesAdicionais.push('');
			}

			$scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'despesas', 'e');

			$scope.gridPesquisa.showColumnFooter = true;

	   }).catch(function(response) {
		   $rootScope.loadingProgress = false;
		   $rootScope.$emit('crud-critical', 'load', response);
	   });
    }

	$scope.getSituacao = function(situacao) {

		if(situacao === 'Ativo'){
			return " ";

		}else{
			return "Inativo";
		}
	}
	var cellTemplateSituacao = '<span>{{grid.appScope.getSituacao(row.entity.situacao)}}</span>';

    /* CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */
    var columnDefs = [{
		name: 'Descrição',
		field: 'descricao',
		width: '75%',
		pinnedLeft: false,
		funcao: ['exibir', 'row.entity.codigo']
	}, {
		name: 'Subconta',
		field: 'sigla',
		width: '10%',
		cellClass: 'text-center'
	}, {
		name: 'Situação',
		field: 'situacao',
		cellTemplate: cellTemplateSituacao,
		width: '10%',
		cellClass: 'text-center'
	}, {
		name: 'Opções',
		width: '5%',
		pinnedRight: false,
		enableSorting: false,
		acoesAdicionais: acoesAdicionais
	}];

    $scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'despesas', 'e');

    $scope.setDefaultParams = function() {
         $scope.paginationOptions.sortField = 'codigo';
         $scope.paginationOptions.sortFieldString = false;
         $scope.paginationOptions.sortDir = 'asc';
    }
     $scope.setDefaultParams();

     $scope.getPage = function(page) {
         $scope.getPageGrid($scope.gridPesquisa, page);
     };
    /* FIM DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */

    $scope.buscar = function() {
		$rootScope.loadingProgress = true;
		$scope.setDefaultParams();
		$scope.getPage(1);
	};

	$scope.$on('crud-success', function(event, action, data) {
		$rootScope.loadingProgress = false;
	});

	$scope.$watch('result', function() {
		$rootScope.loadingProgress = false;
	});

    $scope.novaConta = function(){
        $state.go('despesas.new');
    };

    $scope.exibir = function(codigo){
        $state.go('despesas.view', { objectId: codigo});
    };

    $scope.editar = function(item){
        $state.go('despesas.edit', { objectId: item.codigo});
    };

	$scope.reset = function() {
		$scope.result = null;
		$scope.clear();
	};

    init();

};


function detailController($scope, $controller, $state, $mdDialog, $rootScope, $stateParams, EnumService, DialogConfirm, Despesas){

    function init() {

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
		var pDespesas = Despesas.get({id: $stateParams.objectId }).$promise;

		pDespesas.then(function(data) {
			$scope.object = data;

				carregarSituacao();

		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});
	}

	function newObj() {
		$scope.object = new Despesas();
		$scope.object.situacao = "Ativo";
	}

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

	$scope.salvar = function(){

		if($scope.screenMode === 'NEW'){

			$scope.object.$save()
			.then(function(data){

				$scope.notification.success(null, "Despesa salva com sucesso.");
				$state.go('despesas.view', {objectId: data.codigo});

			}).catch(function(response){
				$scope.$emit('crud-critical', 'save', response);
			});
		} else if ($scope.screenMode === 'EDIT') {
			$scope.object.$update({id: $scope.objectId})
			.then(function(data) {

				$scope.notification.success(null, "Despesa atualizada com sucesso");
				$state.go('despesas.view', {objectId: data.codigo});

			}).catch(function(response) {
				$scope.$emit('crud-critical', 'save', response);
			});
		}
	};

	$scope.editar = function() {
		$state.go('despesas.edit', { objectId: $scope.object.codigo });
	};

	$scope.retornar = function() {
		$state.go('despesas.list');
	};

	$scope.cancelar = function() {
		if ($scope.screenMode === 'NEW') {
			$state.go('despesas.list');
		} else if ($scope.screenMode === 'EDIT') {
			$state.go('despesas.view', { objectId: $scope.object.codigo });
		}
	};

	$scope.remover = function(item) {
		$scope.showConfirm(item);
	};

	/* Inicio da Dialog Remover Despesa */
	$scope.remover = function(item) {
		// Chamada do serviço que criará um dialog de confirmação.
		DialogConfirm.show(
			null,
			$scope.object,
			'Remover despesa: Confirmação',
			'Deseja realmente remover esta despesa (esta operação não poderá ser desfeita)?',
			'Confirmar',
			'Cancelar',
			confirmRemover,
			cancelRemover);
	};

	// Função adicionada na chamada do dialog para confirmação.
	function confirmRemover(object, fnHide) {

		// Remover da tela de exibição!
		Despesas.remover({id: object.codigo})
			.$promise.then(function(data) {

				$scope.notification.success("Despesa removida com sucesso.");

				// Esta função veio do dialog.
				fnHide();
				$state.go('despesas.list');

			}).catch(function(response) {
				$scope.$emit('crud-critical', 'dialog', response);
				$scope.notification.criticalError(response.status, response.statusText, response.data);
				fnHide();
		});
	};

	function cancelRemover(fnHide) {
		fnHide();
	};
	/* Fim da Dialog Remover Despesa */

  	init();
};
})();
