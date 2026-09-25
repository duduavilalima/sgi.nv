(function () {

	'use strict'

	angular
		.module('app.banco')
		.controller('BancoListarCtrl', listController);

	function listController($scope, $controller, $state, $rootScope, Banco, DialogConfirm) {

		$controller('GridController', { $scope: $scope, Resource: Banco });
		$controller('SearchController', { $scope: $scope, Resource: Banco });

		function init() {
            $scope.modulo = $state.current.data.modulo;
			$scope.titulo = $state.current.data.titulo;
			$scope.subtitulo = $state.current.data.subtitulo;
			$scope.object = {};
        }

		$scope.novaBanco = function () {
			$state.go('banco.new');
		};

		$scope.exibir = function (codigo) {
			$state.go('banco.view', { objectId: codigo });
		};

		$scope.editar = function (item) {
			$state.go('banco.edit', { objectId: item.codigo });
		};

		var acoesAdicionais = [
			{
				nome: 'editar.objeto', label: 'Editar', icone: 'icon-pencil', tooltip: 'Editar os dados deste banco'
			},
			{
				nome: 'remover.objeto', label: 'Remover', icone: 'icon-close', tooltip: 'Remover este banco (esta operação não poderá ser desfeita)'
			}
		];

		var cellTemplateSituacao = "<span>{{row.entity.situacao === 'Ativo' ? '' : row.entity.situacao}}</span>";
		var cellTemplateCodigo = "<span><a href='#!/tesouraria/banco/{{row.entity.codigo}}'>{{ row.entity.codigo < 10 ? '00' + row.entity.codigo : (row.entity.codigo > 9 && row.entity.codigo < 100 ? '0' + row.entity.codigo : row.entity.codigo) }}</a></span>";

		/* CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */
		var columnDefs = [{
			name: 'Código',
			field: 'codigo',
			width: '10%',
			cellTemplate: cellTemplateCodigo,
			cellClass: 'text-center',
			pinnedLeft: false,
			funcao: ['exibir', 'row.entity.codigo']
		}, {
			name: 'Nome',
			field: 'nome',
			width: '30%'
		}, {
			name: 'Descrição',
			field: 'descricao',
			width: '35%',
			cellClass: 'text-left'
		}, {
			name: 'Situação',
			field: 'situacao',
			cellTemplate: cellTemplateSituacao,
			width: '20%',
			cellClass: 'text-center',
		}, {
			name: 'Opções',
			width: '5%',
			pinnedRight: false,
			enableSorting: false,
			acoesAdicionais: acoesAdicionais
		}];


		$scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'banco', 'e');

		$scope.setDefaultParams = function () {
			$scope.paginationOptions.sortField = 'codigo';
			$scope.paginationOptions.sortFieldString = false;
			$scope.paginationOptions.sortDir = 'asc';
		}
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

		$scope.reset = function () {
			$scope.result = null;
			$scope.clear();
		};

		$scope.remover = function (item) {
			// Chamada do serviço que criará um dialog de confirmação.
			DialogConfirm.show(
				null,
				item,
				'Remover banco: Confirmação',
				'Deseja realmente remover este banco (esta operação não poderá ser desfeita)?',
				'Confirmar',
				'Cancelar',
				confirmRemover,
				cancelRemover);
		};

		// Função adicionada na chamada do dialog para confirmação.
		function confirmRemover(object, fnHide) {
			Banco.delete({ id: object.codigo }).$promise.then(function (data) {
				$scope.notification.success("Banco removida com sucesso.");

				var index = null;
				// Framework do grid usar um array chamado "$scope.result"
				angular.forEach($scope.result, function (value, key) {
					if (value.codigo === object.codigo) {
						index = key;
						return;
					}
				});

				if (index !== null) {
					$scope.result.splice(index, 1);
					index = null;
				}

				$scope.reset();
				$scope.buscar();

				// Esta função veio do dialog.
				fnHide();
			}).catch(function (response) {
				fnHide();
				console.error('crud-critical', 'dialog', response);
				$scope.$emit('crud-critical', 'dialog', response);
			});
		};

		// Função adicionada na chamada do dialog para cancelamento do mesmo.
		function cancelRemover(fnHide) {
			// Esta função veio do dialog.
			// Aqui você pode fazer qualquer ação ao cancelar um dialog de confirmação.
			fnHide();
		};


		init();
	};
})();
