(function() {
	'use strict';

	angular
		.module('app.despesas', [])

		.constant('moduleUrl', 'partials/fluxocaixa/cadastros/despesas')

		.factory('Despesas', despesasFactory)

		.config(config);

		function despesasFactory($resource, $restServer) {
			return $resource($restServer + 'rest/despesas/:id/:action', null, {
				get: {
					method: 'GET'
				},
				create: {
					method: 'GET',
					params: {'action':'create'}
				},
				update: {
					method: 'PUT'
				},
				qbe: {
					method: 'POST',
					params: {'action':'busca'}
				},
				remover: {
					method: 'GET',
                    params: {'action':'remover'}
				}
			});
		};

		function config(moduleUrl, $stateProvider) {
			$stateProvider
				.state('despesas', {
					abstract: true,
					url: '/fluxocaixa/despesas',
					template: "<div ui-view></div>"
				})
				.state('despesas.list', {
					url: '/listar',
					templateUrl: moduleUrl + '/despesas.listar.html',
					controller: 'DespesasListarCtrl',
					data: {
						titulo: 'Despesas',
						modulo: 'Fluxo de caixa',
						subtitulo: 'Pesquisar'
					}
				})
				.state('despesas.new', {
					url: '/novo',
					templateUrl: moduleUrl + '/despesas.detail.html',
					controller: createController,
					data: {
						titulo: 'Despesas',
						modulo: 'Fluxo de caixa',
						subtitulo: 'Novo'
					}
				})
				.state('despesas.view', {
					url: '/{objectId:int}',
					templateUrl: moduleUrl + '/despesas.detail.html',
					controller: viewController,
					data: {
						titulo: 'Despesas',
						modulo: 'Fluxo de caixa',
						subtitulo: ''
					}
				})
				.state('despesas.edit', {
					url: '/{objectId:int}/editar',
					templateUrl: moduleUrl + '/despesas.detail.html',
					controller: editController,
					data: {
						titulo: 'Despesas',
						modulo: 'Fluxo de caixa',
						subtitulo: 'Editar'
					}
				});
		};

})();
