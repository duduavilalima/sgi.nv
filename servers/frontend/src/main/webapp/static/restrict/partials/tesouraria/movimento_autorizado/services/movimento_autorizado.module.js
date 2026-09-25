(function() {
	'use strict';

	angular
		.module('app.movimentoautorizado', [])

		.constant('htmlUrl', 'partials/tesouraria/movimento_autorizado/html')

        .factory('MovimentoAutorizado', movimentoAutorizadoFactory)

		.config(config);

		function movimentoAutorizadoFactory($resource, $restServer) {
			return $resource($restServer + 'rest/tesouraria/movimentoautorizado/:id/:action', null, {
				cancelar:{
					method:'POST',
					params: {'action':'cancelar'}
				},
				atualizar:{
					method:'POST',
					params: {'action':'atualizarpagamento'}
				}
			});
		};

		function config(htmlUrl, $stateProvider) {
			$stateProvider
			    .state('movimentoautorizado', {
            		abstract: true,
            		url: '/tesouraria/movimentoautorizado',
            		template: "<div ui-view></div>"
            	})
				.state('movimentoautorizado.list', {
					url: '/listar',
					templateUrl: htmlUrl + '/movimento_autorizado.list.html',
					controller: 'MovimentoAutorizadoListarCtrl',
					data: {
						titulo: 'Movimento Autorizado',
						modulo: 'Tesouraria',
						subtitulo: ''
					}
				})
				.state('movimentoautorizado.new', {
					url: '/novo',
					templateUrl: htmlUrl + '/movimento_autorizado.detail.html',
					controller: createController,
					data: {
						titulo: 'Movimento Autorizado',
                        modulo: 'Tesouraria',
						subtitulo: 'Novo'
					}
				})
				.state('movimentoautorizado.view', {
					url: '/{objectId:int}',
					templateUrl: htmlUrl + '/movimento_autorizado.detail.html',
					controller: viewController,
					data: {
						titulo: 'Movimento Autorizado',
                        modulo: 'Tesouraria',
						subtitulo: ''
					}
				})
				.state('movimentoautorizado.edit', {
					url: '/{objectId:int}/editar',
					templateUrl: htmlUrl + '/movimento_autorizado.detail.html',
					controller: editController,
					data: {
						titulo: 'Movimento Autorizado',
                        modulo: 'Tesouraria',
						subtitulo: 'Editar'
					}
				});
		};

})();
