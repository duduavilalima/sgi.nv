(function(){

	'use strict'

	angular
		.module('app.historicoconciliacaobancaria')

		.controller('HistoricoConciliacaoBancariaListarCtrl', listController);

function listController($scope, $controller, $state, EnumService, $mdDialog, $rootScope, $timeout, HistoricoConciliacaoBancaria, DialogConfirm){

	$controller('GridController', {$scope: $scope, Resource: HistoricoConciliacaoBancaria});

	function init() {

		$scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = "Pesquisar";

        carregarTipoAcao();
        carregarSituacao();
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

	$scope.getSituacao = function(situacao) {

		if(situacao === 'Ativo'){
			return " ";

		}else{
			return "Inativo";
		}
	}
    var cellTemplateSituacao = '<span>{{grid.appScope.getSituacao(row.entity.situacao)}}</span>';

	var acoesAdicionais = [
		{ nome : 'editar.objeto', label: 'Editar', icone : 'icon-pencil', tooltip: 'Editar os dados deste históricos de conciliação bancária'}
		,
		{ nome : 'remover.objeto', label: 'Remover', icone : 'icon-close', tooltip: 'Remover este históricos de conciliação bancária (esta operação não poderá ser desfeita)'}
	];


	/* CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */
	var columnDefs = [{
			name: 'Descrição (início)',
			field: 'descricao',
			width: '60%',
			pinnedLeft: false,
			tooltip: ['field.descricao'],
			funcao: ['exibir', 'row.entity.codigo']
		},{
			name: 'Tipo de ação',
			field: 'tipoAcao',
			width: '20%',
			cellClass: 'text-left'
		},{
			name: 'Situação',
			field: 'situacao',
			cellTemplate: cellTemplateSituacao,
			width: '10%',
			cellClass: 'text-center'
		},{
			name: 'Opções',
			width: '10%',
			pinnedRight: false,
			enableSorting: false,
			acoesAdicionais: acoesAdicionais
	}];

	$scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'historicoconciliacao', 'e');

	$scope.setDefaultParams = function() {
		 $scope.paginationOptions.sortField = 'codigo';
		 $scope.paginationOptions.sortFieldString = false;
		 $scope.paginationOptions.sortDir = 'asc';
	}

	$scope.setDefaultParams();

	$scope.getPage = function(page) {
		$scope.getPageGrid($scope.gridPesquisa, page);
	};

	$scope.buscar = function() {
		$rootScope.loadingProgress = true;
		$scope.setDefaultParams();
		$scope.getPage(1);
	};	/* FIM DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */

	/* Inicio da Dialog Remover */
	$scope.remover = function(item) {
		// Chamada do serviço que criará um dialog de confirmação.
		DialogConfirm.show(
			null,
			item,
			'Remover histórico da conciliação bancária: Confirmação',
			'Deseja realmente remover este histórico da conciliação bancária (esta operação não poderá ser desfeita)?',
			'Confirmar',
			'Cancelar',
			confirmRemover,
			cancelRemover);
	};

	// Função adicionada na chamada do dialog para confirmação.
	function confirmRemover(object, fnHide) {

		// Remover da tela de Pesquisa!
		HistoricoConciliacaoBancaria.remover({id: object.codigo})
			.$promise.then(function(data) {

				$scope.notification.success("Histórico da conciliação bancária removido com sucesso.");

				var index = null;
				angular.forEach($scope.result, function(value, key) {
					if (value.codigo === object.codigo) {
						index = $scope.result.indexOf(value);
						return;
					}
				});

				if (index != null) {
					$scope.result.splice(index, 1);
					index = null;
				}

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
	/* Fim da Dialog Remover */

	// Botão Limpar
	$scope.reset = function() {
		$scope.result = null;
		$scope.clear();
	};

	$scope.exibir = function(codigo) {
		$state.go('historico_conciliacao_bancaria.view', { objectId: codigo });
	};

	$scope.editar = function(item) {
		$state.go('historico_conciliacao_bancaria.edit', { objectId: item.codigo });
	};

	init();
};

})();
