angular
	.module('app.relacionamentocontato')

	.controller('RelacionamentoContatoListarCtrl', listController);

function listController($scope, $state, $rootScope, $controller, DialogConfirm, Perfil, RelacionamentoContato) {
	const $ctrl = this;

	$controller('GridController', {
		$scope: $scope,
		Resource: RelacionamentoContato
	});

	const initScope = ($rootScope, $ctrl, $state, Perfil) => {
		$ctrl.modulo = $state.current.data.modulo;
		$ctrl.titulo = $state.current.data.titulo;

		$ctrl.object = {};
		$ctrl.object.nome = undefined;
		$ctrl.object.perfil = undefined;
		$ctrl.listPerfis = [];

		$rootScope.loadingProgress = true;

		Perfil.buscarTodos(undefined).$promise.then((perfis) => {
			$ctrl.listPerfis = perfis;
			$rootScope.loadingProgress = false;

		}).catch((response) => {
			console.error('crud-critical', 'dialog', response);
			$rootScope.loadingProgress = false;
			$scope.notification.criticalError(response.status, response.statusText, response.data);
		});
	};

	const init = () => {
		$ctrl.subtitulo = "Pesquisar";

		initScope($rootScope, $ctrl, $state, Perfil);
	};

	$scope.buscar = () => {
		$rootScope.loadingProgress = true;
		$scope.setDefaultParams();
		$scope.getPage(1);
	};

	$scope.$on('crud-success', (event, action, data) => {
		$rootScope.loadingProgress = false;
	});

	$scope.$watch('result', () => {
		$rootScope.loadingProgress = false;
	});


	$scope.reset = () => {
		$scope.result = undefined;
		$scope.clear();
	};

	const acoesAdicionais = [
		{
			nome: '$ctrl.editar.objeto', label: 'Editar', icone: 'icon-pencil', tooltip: 'Editar os dados deste perfil'
		}
	];

	const columnDefs = [
		{
			name: 'Nome',
			field: 'nome',
			width: '50%',
			cellClass: 'text-left',
			enableSorting: false,
			sortFieldString: false,
			pinnedLeft: false,
			funcao: ['exibir', 'row.entity.codigo']
		},
		{
			name: 'Perfil',
			field: 'perfil.descricao',
			width: '40%',
			cellClass: 'text-left'
		},
		{
			name: 'Opções',
			width: '10%',
			pinnedRight: false,
			enableSorting: false,
			acoes: ['exibir'],
			acoesAdicionais: acoesAdicionais
		}
	];

	$scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'obra', 'e');

	$scope.setDefaultParams = () => {
		$scope.paginationOptions.sortField = 'codigo';
		$scope.paginationOptions.sortFieldString = false;
		$scope.paginationOptions.sortDir = 'asc';
	};
	$scope.setDefaultParams();

	$scope.getPage = (page) => {
		$scope.getPageGrid($scope.gridPesquisa, page);
	};


	$ctrl.exibirAcao = (acao, row) => {
		if (acao === 'editar') {
			if (row.entity.status === 'Aberto') {
				return true;
			}
		}
		if (acao === 'remover') {
			if (row.entity.status === 'Aberto') {
				return true;
			}
		}
		if (acao === 'exibir') {
			return true;
		}
		return false;
	};

	$ctrl.novo = () => {
		$state.go('relacionamentocontato.new');
	};

	$scope.exibir = (codigo) => {
		$state.go('relacionamentocontato.view', { objectId: codigo });
	};

	$ctrl.editar = (item) => {
		$state.go('relacionamentocontato.edit', { objectId: item.codigo });
	};

	$ctrl.remover = (item) => {
		$ctrl.showConfirm(item);
	};

	$ctrl.showConfirm = (objRelacionamentoContato) => {
		let tituloDialog = `${$ctrl.titulo} - Remover: Confirmação`;
		let msgDialog = "Deseja realmente remover este relacionamento de contato (esta operação não poderá ser desfeita)?";
		let tituloBotaoConfirmar = "CONFIRMAR";
		let tituloBotaoCancelar = "CANCELAR";

		const funcaoCancelar = (fnHide) => {
			fnHide();
		};

		const funcaoConfirmar = (object, fnHide) => {
			let pRelacionamentoContato = RelacionamentoContato.delete({ id: object.codigo }).$promise;

			pRelacionamentoContato.then((data) => {

				let title = 'Remover Relacionamento de Contato';
				let message = `Relacionamento de Contato ${object.nome} removido com sucesso.`;
				$scope.notification.success(title, message);

				fnHide();

			}).catch((response) => {
				console.error('crud-critical', 'dialog', response);
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
		};

		DialogConfirm.show(undefined, objRelacionamentoContato, tituloDialog, msgDialog, tituloBotaoConfirmar, tituloBotaoCancelar, funcaoConfirmar, funcaoCancelar);
	};

	init();

	/** FIM DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA  */

}
