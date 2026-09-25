(function() {
	'use strict';

	angular
		.module('app.lancamentos', [])

		.constant('htmlUrl', 'partials/tesouraria/lancamentos/html')

		.factory('Lancamentos', lancamentosFactory)

		.config(config);

		function lancamentosFactory($resource, $restServer) {
			return $resource($restServer + 'rest/lancamentos/:id/:action', null, {
				cancelar: {
					method: 'POST',
					url: $restServer + 'rest/lancamentos/cancelar'
				}
			});
		};

		function config(htmlUrl, $stateProvider) {
			$stateProvider
				.state('lancamentos', {
					abstract: true,
					url: '/tesouraria/lancamentos',
					template: "<div ui-view></div>"
				})
				.state('lancamentos.list', {
					url: '/listar',
					templateUrl: htmlUrl + '/lancamentos.list.html',
					controller: 'LancamentosListarCtrl',
					data: {
						titulo: 'Lançamentos',
						modulo: 'Tesouraria',
						subtitulo: ''
					}
				})
				.state('lancamentos.new', {
					url: '/novo',
					templateUrl: htmlUrl + '/lancamentos.detail.html',
					controller: createController,
					data: {
						titulo: 'Lançamentos',
						modulo: 'Tesouraria',
						subtitulo: 'Novo'
					}
				})
				.state('lancamentos.view', {
					url: '/{objectId:int}',
					templateUrl: htmlUrl + '/lancamentos.detail.html',
					controller: viewController,
					data: {
						titulo: 'Lançamentos',
						modulo: 'Tesouraria',
						subtitulo: ''
					}
				})
				.state('lancamentos.edit', {
					url: '/{objectId:int}/editar',
					templateUrl: htmlUrl + '/lancamentos.detail.html',
					controller: editController,
					data: {
						titulo: 'Lançamentos',
						modulo: 'Tesouraria',
						subtitulo: 'Editar'
					}
				});
		};

})();
