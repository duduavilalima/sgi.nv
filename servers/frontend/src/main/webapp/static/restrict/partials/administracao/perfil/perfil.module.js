(function() {
	'use strict';

	angular
		.module('app.perfil', [])
		.constant('moduleUrl', 'partials/administracao/perfil')
		.factory('Perfil', perfilFactory)
		.config(config);

	function perfilFactory($resource, $restServer) {
		return $resource($restServer + 'rest/perfil/:id/:action', null, {
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
			buscarTodos:{
				method: 'GET',
				isArray: true,
				url: $restServer + 'rest/perfil/listarAtivos'
			}
		});
	};

	function config(moduleUrl, $stateProvider) {
		var modulo = "Administração";
		var titulo = "Perfis";

		$stateProvider
			.state('perfil', {
				abstract: true,
				url: '/administracao/perfil',
				template: "<div ui-view></div>"
			})
			.state('perfil.listar', {
				url: '/listar',
				templateUrl: moduleUrl + '/perfil.list.html',
				controller: 'PerfilListarCtrl',
				data: {
					titulo: titulo,
					modulo: modulo,
					subtitulo: ''
				}
			})
			.state('perfil.novo', {
				url: '/novo',
				templateUrl: moduleUrl + '/perfil.detail.html',
				controller: createController,
				data: {
					titulo: titulo,
					modulo: modulo,
					subtitulo: 'Novo'
				}
			})
			.state('perfil.view', {
				url: '/{objectId:int}',
				templateUrl: moduleUrl + '/perfil.detail.html',
				controller: viewController,
				data: {
					titulo: titulo,
					modulo: modulo,
					subtitulo: ''
				}
			})
			.state('perfil.edit', {
				url: '/{objectId:int}/editar',
				templateUrl: moduleUrl + '/perfil.detail.html',
				controller: editController,
				data: {
					titulo: titulo,
					modulo: modulo,
					subtitulo: 'Editar'
				}
			});
	};

})();
