(function() {
	'use strict';

	angular
		.module('app.categoriacontrato', [])
		.constant('moduleUrl', 'partials/comercial/cadastros/categoriacontrato/html')
        .factory('CategoriaContrato', categoriacontratoFactory)
		.config(config);

		function categoriacontratoFactory($resource, $restServer) {
			return $resource(`${$restServer}rest/categoriacontrato/:id/:action`, undefined, {
				cancelar:{
					method:'POST',
					params: {action:'cancelar'}
				}
			});
		}

		function config(moduleUrl, $stateProvider) {
			$stateProvider
			    .state('categoriacontrato', {
            		abstract: true,
            		url: '/comercial/cadastros/categoriacontrato',
            		template: '<div ui-view></div>'
            	})
				.state('categoriacontrato.list', {
					url: '/listar',
					templateUrl: `${moduleUrl}/list.html`,
					controller: 'categoriacontratoListarCtrl',
					controllerAs: '$ctrl'
				})
				.state('categoriacontrato.new', {
					url: '/novo',
					templateUrl: `${moduleUrl}/new.html`,
					controller: createController,
					controllerAs: '$ctrl'
				})
				.state('categoriacontrato.view', {
					url: '/{objectId:int}',
					templateUrl: `${moduleUrl}/view.html`,
					controller: viewController,
					controllerAs: '$ctrl'
				})
				.state('categoriacontrato.edit', {
					url: '/{objectId:int}/editar',
					templateUrl: `${moduleUrl}/edit.html`,
					controller: editController,
					controllerAs: '$ctrl'
				});
		}

})();
