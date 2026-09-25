(function() {
    'use strict';

    angular
        .module('app.usuario', ['services', 'usuariologadoservice', 'enumservice'])

        .constant('moduleUrl', 'partials/administracao/usuario')

        .factory('Usuario', usuarioFactory)

        .config(configUsuario);

    function usuarioFactory($resource, $restServer) {
        return $resource($restServer + 'rest/usuario/:id/:action', null, {
			copy: {
				method: 'POST',
				url: $restServer + 'rest/usuario/copiar'
			},
			deleteList: {
				method: 'POST',
				url: $restServer + 'rest/usuario/deletar/'
			},
			usuarioLogado: {
				method: 'POST',
				url: $restServer + 'rest/usuario/usuariologado/'
			},
			possuiTodosPerfisLista: {
				method: 'POST',
				url: $restServer + 'rest/usuario/possuitodosperfislista/'
			},
			possuiUmPerfilLista: {
				method: 'POST',
				url: $restServer + 'rest/usuario/possuiumperfillista/'
			},
			apenasFaturamento: {
				 method: 'GET',
				 url: $restServer + 'rest/usuario/apenasfaturamento/'
			}
        });
    };

    function configUsuario(moduleUrl, $stateProvider) {
        $stateProvider
            .state('usuario', {
				abstract: true,
				url: '/administracao/usuario',
				template: "<div ui-view></div>",
				data: { modulo : 'Administração', titulo: 'Usuários' }
			})
			.state('usuario.listar', {
				url: '/listar',
				templateUrl: 'partials/administracao/usuario/usuario.list.html',
				data: { subtitulo: 'Pesquisar'}
			})
			.state('usuario.novo', {
				url: '/create',
				templateUrl: 'partials/administracao/usuario/usuario.datail.html',
				controller: createController,
				data: { subtitulo:  'Novo', screenMode : 'NEW'},
			})
			.state('usuario.exibir', {
				url: '/{objectId:int}/exibir',
				templateUrl: 'partials/administracao/usuario/usuario.datail.html',
				controller: viewController,
				data: { titulo: 'Usuário', subtitulo:  'Exibição', screenMode : 'VIEW'},
			})
			.state('usuario.editar', {
				url: '/{objectId:int}/editar',
				templateUrl: 'partials/administracao/usuario/usuario.datail.html',
				controller: viewController,
				data: { titulo: 'Usuário', subtitulo:  'Edição', screenMode : 'EDIT'},
			});
    };

})();
