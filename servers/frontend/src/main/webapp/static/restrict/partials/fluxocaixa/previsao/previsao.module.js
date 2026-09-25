(function() {
	'use strict';

	angular
		.module('app.previsao', [])

		.constant('moduleUrl', 'partials/fluxocaixa/previsao')

        .factory('Previsao', previsaoFactory)

		.config(config);

		function previsaoFactory($resource, $restServer) {
			return $resource($restServer + 'rest/previsao/:id/:action', null, {
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
			    .state('previsao', {
            		abstract: true,
            		url: '/fluxocaixa/previsao',
            		template: "<div ui-view></div>"
            	})
				.state('previsao.list', {
					url: '/listar',
					templateUrl: moduleUrl + '/previsao.list.html',
					controller: 'PrevisaoListarCtrl',
					data: {
						titulo: 'Previsão',
						modulo: 'Fluxo de Caixa',
						subtitulo: 'Pesquisar'
					}
				})
				.state('previsao.new', {
					url: '/novo',
					templateUrl: moduleUrl + '/previsao.detail.html',
					controller: createController,
					data: {
						titulo: 'Previsão',
                        modulo: 'Fluxo de Caixa',
						subtitulo: 'Novo'
					}
				})
				.state('previsao.view', {
					url: '/{objectId:int}',
					templateUrl: moduleUrl + '/previsao.detail.html',
					controller: viewController,
					data: {
						titulo: 'Previsão',
                        modulo: 'Fluxo de Caixa',
						subtitulo: ''
					}
				})
				.state('previsao.edit', {
					url: '/{objectId:int}/editar',
					templateUrl: moduleUrl + '/previsao.detail.html',
					controller: editController,
					data: {
						titulo: 'Previsão',
                        modulo: 'Fluxo de Caixa',
						subtitulo: 'Editar'
					}
				});
		};

})();
