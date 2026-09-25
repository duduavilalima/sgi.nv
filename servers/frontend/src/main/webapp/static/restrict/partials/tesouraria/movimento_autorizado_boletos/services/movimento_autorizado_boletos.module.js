(function() {
	'use strict';

	angular
		.module('app.movimentoautorizadoboletos', [])

		.constant('htmlUrl', 'partials/tesouraria/movimento_autorizado_boletos/html')

        .factory('MovimentoAutorizadoBoletos', movimentoAutorizadoBoletosFactory)

		.config(config);

		function movimentoAutorizadoBoletosFactory($resource, $restServer) {
			return $resource($restServer + 'rest/tesouraria/movimentoautorizadoboletos/:id/:action', null, {
				atualizarcodigobarras: {
					method: 'PUT',
					params: {'action':'atualizarcodigobarras'}
				}
			});
		};

		function config(htmlUrl, $stateProvider) {
			$stateProvider
			    .state('movimentoautorizadoboletos', {
            		abstract: true,
            		url: '/tesouraria/movimentoautorizadoboletos',
            		template: "<div ui-view></div>"
            	})
				.state('movimentoautorizadoboletos.list', {
					url: '/listar',
					templateUrl: htmlUrl + '/movimento_autorizado_boletos.list.html',
					data: {
						titulo: 'Movimento Autorizado (Boletos)',
						modulo: 'Tesouraria',
						subtitulo: 'Pesquisar'
					}
				})
				.state('movimentoautorizadoboletos.new', {
					url: '/novo',
					templateUrl: htmlUrl + '/movimento_autorizado_boletos.detail.html',
					controller: createController,
					data: {
						titulo: 'Movimento Autorizado (Boletos)',
                        modulo: 'Tesouraria',
						subtitulo: 'Novo'
					}
				})
				.state('movimentoautorizadosboletos.view', {
					url: '/{objectId:int}',
					templateUrl: htmlUrl + '/movimento_autorizado_boletos.detail.html',
					controller: viewController,
					data: {
						titulo: 'Movimento Autorizado (Boletos)',
                        modulo: 'Tesouraria',
						subtitulo: ''
					}
				})
				.state('movimentoautorizadoboletos.edit', {
					url: '/{objectId:int}/editar',
					templateUrl: htmlUrl + '/movimento_autorizado_boletos.detail.html',
					controller: editController,
					data: {
						titulo: 'Movimento Autorizado (Boletos)',
                        modulo: 'Tesouraria',
						subtitulo: 'Editar'
					}
				});
		};

})();
