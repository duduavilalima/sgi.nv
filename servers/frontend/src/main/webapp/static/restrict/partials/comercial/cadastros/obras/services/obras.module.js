(function () {
	'use strict';

	angular
		.module('app.obras', [])

		.constant('moduleUrl', 'partials/comercial/cadastros/obras')

		.factory('Obras', obrasFactory)

		.config(config);

	function obrasFactory($resource, $restServer) {
		return $resource(`${$restServer}rest/obras/:id/:action`, undefined, {
			cancelar: {
				method: 'POST',
				params: { 'action': 'cancelar' }
			},

			buscarTipoObra: {
				method: 'POST',
				url: `${$restServer}rest/obras/buscartipoobra`,
				isArray: true
			}
		});
	}

	function config(moduleUrl, $stateProvider) {
		$stateProvider
			.state('obras', {
				abstract: true,
				url: '/comercial/cadastros/obras',
				template: "<div ui-view></div>"
			})
			.state('obras.list', {
				url: '/listar',
				templateUrl: `${moduleUrl}/html/list.html`,
				controller: 'ObrasListarCtrl',
				controllerAs: '$ctrl',
				data: {
					titulo: 'Obras',
					modulo: 'Comercial',
					subtitulo: 'Pesquisar'
				}
			})
			.state('obras.new', {
				url: '/novo',
				templateUrl: `${moduleUrl}/html/new.html`,
				controller: createController,
				data: {
					titulo: 'Obras',
					modulo: 'Comercial',
					subtitulo: 'Novo'
				}
			})
			.state('obras.view', {
				url: '/{objectId:int}',
				templateUrl: `${moduleUrl}/html/view.html`,
				controller: viewController,
				data: {
					titulo: 'Obras',
					modulo: 'Comercial',
					subtitulo: ''
				}
			})
			.state('obras.edit', {
				url: '/{objectId:int}/editar',
				templateUrl: `${moduleUrl}/html/edit.html`,
				controller: editController,
				data: {
					titulo: 'Obras',
					modulo: 'Comercial',
					subtitulo: 'Editar'
				}
			});
	}

})();
