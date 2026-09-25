(function () {
	'use strict';

	angular
		.module('app.relacionamentocontato', [])

		.constant('moduleUrl', 'partials/comercial/cadastros/relacionamentocontato')

		.factory('RelacionamentoContato', relacionamentosContatoFactory)

		.config(config);

	function relacionamentosContatoFactory($resource, $restServer) {
		return $resource(`${$restServer}rest/relacionamentocontato/:id/:action`, undefined, {
			buscarTodos: {
				method: 'GET',
				url: `${$restServer}rest/relacionamentocontato/buscarTodos`,
				isArray: true
			}
		});
	}

	function config(moduleUrl, $stateProvider) {
		$stateProvider
			.state('relacionamentocontato', {
				abstract: true,
				url: '/comercial/cadastros/relacionamentocontato',
				template: "<div ui-view></div>"
			})
			.state('relacionamentocontato.list', {
				url: '/listar',
				templateUrl: `${moduleUrl}/html/list.html`,
				controller: 'RelacionamentoContatoListarCtrl',
				controllerAs: '$ctrl',
				data: {
					titulo: 'Relacionamentos de Contato',
					modulo: 'Comercial',
					subtitulo: ''
				}
			})
			.state('relacionamentocontato.new', {
				url: '/novo',
				templateUrl: `${moduleUrl}/html/new.html`,
				controller: createController,
				data: {
					titulo: 'Relacionamentos de Contato',
					modulo: 'Comercial',
					subtitulo: 'Novo'
				}
			})
			.state('relacionamentocontato.view', {
				url: '/{objectId:int}',
				templateUrl: `${moduleUrl}/html/view.html`,
				controller: viewController,
				data: {
					titulo: 'Relacionamentos de Contato',
					modulo: 'Comercial',
					subtitulo: ''
				}
			})
			.state('relacionamentocontato.edit', {
				url: '/{objectId:int}/editar',
				templateUrl: `${moduleUrl}/html/edit.html`,
				controller: editController,
				data: {
					titulo: 'Relacionamentos de Contato',
					modulo: 'Comercial',
					subtitulo: 'Editar'
				}
			});
	}

})();
