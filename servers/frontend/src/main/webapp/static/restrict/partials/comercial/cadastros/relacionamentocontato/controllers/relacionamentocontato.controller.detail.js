angular
	.module('app.relacionamentocontato')

	.controller('RelacionamentoContatoDetailCtrl', detailController);

function detailController($scope, $state, $stateParams, $rootScope, DialogConfirm, Perfil, RelacionamentoContato) {
	const $ctrl = this;


	function initScope($rootScope, $ctrl, $state, Perfil) {
		$ctrl.modulo = $state.current.data.modulo;
		$ctrl.titulo = $state.current.data.titulo;

		$ctrl.object = {};
		$ctrl.object.nome = undefined;
		$ctrl.object.perfil = undefined;
		$ctrl.listPerfis = [];

		$rootScope.loadingProgress = true;

		Perfil.buscarTodos(undefined).$promise.then(function (perfis) {
			$ctrl.listPerfis = perfis;
			$rootScope.loadingProgress = false;

		}).catch(function (response) {
			console.error('crud-critical', 'dialog', response);
			$rootScope.loadingProgress = false;
			$scope.notification.criticalError(response.status, response.statusText, response.data);
		});
	}

	const catchError = (response, title) => {
		console.error(response);
		$scope.$emit('crud-critical', title, response);
	};

	const catchErrorLoad = response => {
		catchError(response, 'load');
	};

	const init = () => {
		initScope($rootScope, $ctrl, $state, Perfil);

		if ($scope.screenMode === 'NEW') {
			newObj();
		} else {
			loadObj();
		}

	};

	const loadObj = () => {

		const pRelacionamentoContato = RelacionamentoContato.get({ id: $stateParams.objectId }).$promise;

		pRelacionamentoContato.then((data) => {
			$ctrl.object = data;

		}).catch(catchErrorLoad);
	};

	const newObj = () => {
		$ctrl.object = new RelacionamentoContato();
		$ctrl.object.perfil = new Perfil();
	};

	$ctrl.editar = () => {
		$state.go('relacionamentocontato.edit', { objectId: $ctrl.object.codigo });
	};

	$ctrl.retornar = () => {
		$state.go('relacionamentocontato.list');
	};

	$ctrl.cancelarEdit = () => {
		if ($scope.screenMode === 'NEW') {
			$state.go('relacionamentocontato.list');
		} else if ($scope.screenMode === 'EDIT') {
			$state.go('relacionamentocontato.view', { objectId: $ctrl.object.codigo });
		}
	};

	$ctrl.salvar = () => {
		if ($scope.screenMode === 'NEW') {
			let pSave = $ctrl.object.$save();
			pSave.then((data) => {
				$scope.notification.success("Novo", "Relacionamento de contato inserido com sucesso.");
				$state.go('relacionamentocontato.view', { objectId: data.codigo });
			}).catch(catchError);

		} else if ($scope.screenMode === 'EDIT') {
			let entity = angular.copy($ctrl.object);
			RelacionamentoContato.update({ id: $ctrl.object.codigo }, entity).$promise.then((data) => {
				$scope.notification.success("Edição", "Relacionamento de contato atualizado com sucesso.");
				$state.go('relacionamentocontato.view', { objectId: data.codigo });
			}).catch((response) => {
				$scope.$emit('crud-critical', 'save', response);
			});
		}
	};

	/** REMOVER OBRA */
	$ctrl.remover = (ev) => {
		let tituloDialog = "Relacionamento de contato - Remover: Confirmação";
		let msgDialog = "Deseja realmente remover este relacionamento de contato (esta operação não poderá ser desfeita)?";
		let tituloBotaoConfirmar = "CONFIRMAR";
		let tituloBotaoCancelar = "CANCELAR";

		let funcaoCancelar = (fnHide) => {
			fnHide();
		};

		let funcaoConfirmar = (object, fnHide) => {
			let pObras = RelacionamentoContato.delete({ id: object.codigo }).$promise;

			pObras.then((data) => {

				let title = 'Remover Relacionamento de contato';
				let message = `Relacionamento de contato ${object.nome} removido com sucesso.`;
				$scope.notification.success(title, message);

				fnHide();
				$state.go('relacionamentocontato.list');

			}).catch((response) => {
				console.error('crud-critical', 'dialog', response);
				$scope.notification.criticalError(response.status, response.statusText, response.data);
				fnHide();
			});
		};

		DialogConfirm.show(ev, $ctrl.object, tituloDialog, msgDialog, tituloBotaoConfirmar, tituloBotaoCancelar, funcaoConfirmar, funcaoCancelar);
	};

	init();
}
