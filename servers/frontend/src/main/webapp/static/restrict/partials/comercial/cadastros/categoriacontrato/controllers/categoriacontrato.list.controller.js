angular
	.module('app.categoriacontrato')
	.controller('categoriacontratoListarCtrl', listController);

function listController($scope, $controller, $state, EnumService, $rootScope, CategoriaContrato ) {

	$controller('GridController', {$scope: $scope, Resource: CategoriaContrato});

	const $ctrl = this;

	init = () => {
		carregarTipoCategoriaContrato();
	};

	// Carrega a combo de pesquisa Situação
	carregarTipoCategoriaContrato = () => {
		EnumService.enumValues({ pacote: 'br.com.araujoabreu.sgi.administracao.domain', classe: 'CategoriaContrato', enum: 'TipoCategoriaContrato' })
			.$promise.then(function(data) {
				$ctrl.listtipo = [];
				$ctrl.listtipo.push('');
				$ctrl.listtipo.push(...data);
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});
	};

	let acoesAdicionais = [{nome : '$ctrl.editar.objeto', label: 'Editar', icone : 'icon-pencil', tooltip: 'Editar os dados desta categoria de contrato'}];

	/* CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */
	let columnDefs = [{
		name: 'Descrição',
		field: 'descricao',
		enableSorting: false,
		sortFieldString: false,
		pinnedLeft: false,
		funcao: ['$ctrl.exibir', 'row.entity.codigo']
	}, {
		name: 'Tipo',
		field: 'tipo',
		width: '15%'

	}, {
		name: 'Criador',
		field: 'criador',
		width: '40%'
	}, {
		name: 'Opções',
		width: '6%',
		pinnedRight: false,
		enableSorting: false,
		acoesAdicionais: acoesAdicionais
	}];

	$scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'categoriacontrato', 'e');

	$scope.setDefaultParams = function() {
		$scope.paginationOptions.sortField = 'codigo';
		$scope.paginationOptions.sortFieldString = false;
		$scope.paginationOptions.sortDir = 'asc';
	};

	$scope.setDefaultParams();

	$scope.getPage = function(page) {
		$scope.getPageGrid($scope.gridPesquisa, page);
	};
	/* FIM DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */

	$ctrl.buscar = function() {
		$rootScope.loadingProgress = true;
		$scope.setDefaultParams();
		$scope.getPage(1);
	};

	$ctrl.exibir = function(codigo) {
		$state.go('categoriacontrato.view', {
			objectId: codigo
		});
	};

	// Botão Limpar
	$ctrl.reset = function() {
		$scope.result = undefined;
		$scope.clear();
	};

	$ctrl.editar = function(item) {
		$state.go('categoriacontrato.edit', {
			objectId: item.codigo
		});
	};

	init();
}
