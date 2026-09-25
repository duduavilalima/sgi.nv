(function () {
	'use strict';

	angular
		.module('app.banco', [])
		.constant('moduleUrl', 'partials/tesouraria/cadastros/banco')
		.factory('Banco', bancoFactory)
		.config(config);

	function bancoFactory($resource, $restServer) {
		return $resource($restServer + 'rest/banco/:id/:action', null, {
			todos: {
				method: 'GET',
				params: { 'action': 'todosordenado' },
				isArray: true
			},
			todosordenado: {
				method: 'GET',
				params: { 'action': 'todosordenado' },
				isArray: true
			},
		});
	}

	function config(moduleUrl, $stateProvider) {
		$stateProvider
			.state('banco', {
				abstract: true,
				url: '/tesouraria/banco',
				template: "<div ui-view></div>"
			})
			.state('banco.list', {
				url: '/listar',
				templateUrl: moduleUrl + '/banco.listar.html',
				controller: 'BancoListarCtrl',
				data: {
					titulo: 'Bancos',
					modulo: 'Tesouraria',
					subtitulo: 'Pesquisar'
				}
			})
			.state('banco.new', {
				url: '/novo',
				templateUrl: moduleUrl + '/banco.detail.html',
				controller: createController,
				data: {
					titulo: 'Banco',
					modulo: 'Tesouraria',
					subtitulo: 'Novo'
				}
			})
			.state('banco.view', {
				url: '/{objectId:int}',
				templateUrl: moduleUrl + '/banco.detail.html',
				controller: viewController,
				data: {
					titulo: 'Banco',
					modulo: 'Tesouraria',
					subtitulo: ''
				}
			})
			.state('banco.edit', {
				url: '/{objectId:int}/editar',
				templateUrl: moduleUrl + '/banco.detail.html',
				controller: editController,
				data: {
					titulo: 'Banco',
					modulo: 'Tesouraria',
					subtitulo: 'Editar'
				}
			});
	}

})();
