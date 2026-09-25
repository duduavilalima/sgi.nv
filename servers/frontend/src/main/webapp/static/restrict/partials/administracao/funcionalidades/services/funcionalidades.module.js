(function() {
    'use strict';

    angular
        .module('app.funcionalidades', [])

        .constant('moduleUrl', 'partials/administracao/funcionalidades')

        .factory('Funcionalidades', funcionalidadesFactory)

        .config(config);

    function funcionalidadesFactory($resource, $restServer) {
        return $resource(`${$restServer}rest/funcionalidades/:id/:action`, undefined, {
            get: {
                method: 'GET'
            },
            create: {
                method: 'GET',
                params: {
                    'action': 'create'
                }
            },
            update: {
                method: 'PUT'
            },
            qbe: {
                method: 'POST',
                params: {
                    'action': 'busca'
                }
            },
            remover: {
                method: 'GET',
                params: {
                    'action': 'remover'
                }
            }
        });
    }

    function config(moduleUrl, $stateProvider) {
        $stateProvider
            .state('funcionalidades', {
                abstract: true,
                url: '/administracao/funcionalidades',
                template: "<div ui-view></div>"
            })
            .state('funcionalidades.list', {
                url: '/listar',
                templateUrl: `${moduleUrl}/html/funcionalidades.list.html`,
                controller: 'FuncionalidadesListarCtrl',
                data: {
                    titulo: 'Funcionalidades',
                    modulo: 'Administração',
                    subtitulo: ''
                }
            })
            .state('funcionalidades.new', {
                url: '/novo',
                templateUrl: `${moduleUrl}/html/funcionalidades.detail.html`,
                controller: createController,
                data: {
                    titulo: 'Funcionalidades',
                    modulo: 'Administração',
                    subtitulo: 'Novo'
                }
            })
            .state('funcionalidades.view', {
                url: '/{objectId:int}',
                templateUrl: `${moduleUrl}/html/funcionalidades.detail.html`,
                controller: viewController,
                data: {
                    titulo: 'Funcionalidades',
                    modulo: 'Administração',
                    subtitulo: ''
                }
            })
            .state('funcionalidades.edit', {
                url: '/{objectId:int}/editar',
                templateUrl: `${moduleUrl}/html/funcionalidades.detail.html`,
                controller: editController,
                data: {
                    titulo: 'Funcionalidades',
                    modulo: 'Administração',
                    subtitulo: 'Editar'
                }
            });
    }

})();