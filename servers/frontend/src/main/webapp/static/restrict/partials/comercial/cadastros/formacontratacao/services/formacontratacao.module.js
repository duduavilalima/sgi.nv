(function() {
	'use strict';

	angular
		.module('app.formacontratacao', [])

		.constant('moduleUrl', 'partials/comercial/cadastros/formacontratacao')

        .factory('FormaContratacao', formaContratacaoFactory)
		
		.config(config);

		function formaContratacaoFactory($resource, $restServer) {
			return $resource(`${$restServer}rest/formacontratacao/:id/:action`, null, {
				cancelar:{
					method:'POST',
					params: {'action':'cancelar'}
				}
			});
		}

		function config(moduleUrl, $stateProvider) {
			$stateProvider
			    .state('formacontratacao', {
            		abstract: true,
            		url: '/comercial/cadastros/formacontratacao',
            		template: "<div ui-view></div>"
            	})
				.state('formacontratacao.list', {
					url: '/listar',
					templateUrl: `${moduleUrl}/html/list.html`,
					controller: 'formacontratacaoListarCtrl',
					data: {
						titulo: 'Forma de contratação',
						modulo: 'Comercial',
						subtitulo: ''
					}
				})
				.state('formacontratacao.new', {
					url: '/novo',
					templateUrl: `${moduleUrl}/html/new.html`,
					controller: createController,
					data: {
						titulo: 'Forma de contratação',
                        modulo: 'Comercial',
						subtitulo: 'Novo'
					}
				})
				.state('formacontratacao.view', {
					url: '/{objectId:int}',
					templateUrl: `${moduleUrl}/html/view.html`,
					controller: viewController,
					data: {
						titulo: 'Forma de contratação',
                        modulo: 'Comercial',
						subtitulo: ''
					}
				})
				.state('formacontratacao.edit', {
					url: '/{objectId:int}/editar',
					templateUrl: `${moduleUrl}/html/edit.html`,
					controller: editController,
					data: {
						titulo: 'Forma de contratação',
                        modulo: 'Comercial',
						subtitulo: 'Editar'
					}
				});
		}

})();
