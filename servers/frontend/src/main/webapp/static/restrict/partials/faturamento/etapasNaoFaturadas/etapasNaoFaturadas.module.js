(function() {
    'use strict';

    angular
        .module('app.etapasNaoFaturadas', ['utils', 'services', 'enumservice', 'autocompleteservice', 'usuariologadoservice'])
        .constant('moduleUrl', 'partials/faturamento/Etapasnaofaturadas')
        .factory('Etapasnaofaturadas', EtapasnaofaturadasFactory)
        .config(configEtapasnaofaturadas);

    function EtapasnaofaturadasFactory($resource, $restServer) {
        return $resource($restServer + 'rest/faturamento/etapanaofaturada/:prefixoAction/:id/:action', null, {
            get: {
                method: 'GET'
            },
            create: {
                method: 'GET',
                params: {'action': 'create'}
            },
            update: {
                method: 'PUT'
            },
            qbe: {
                method: 'POST',
                params: {'action': 'busca'}
            },
            validarCL: {
                method: 'GET',
                params: {'prefixoAction': 'validarCL'}
            }
        });
    }

    function configEtapasnaofaturadas(moduleUrl, $stateProvider) {
        $stateProvider
            .state('etapasnaofaturadas', {
                abstract: true,
                url: '/etapasNaoFaturadas',
                template: "<div ui-view></div>",
                data: {
                    modulo: 'Faturamento',
                    titulo: 'Etapas não faturadas'
                }
            })
            .state('etapasnaofaturadas.listar', {
                url: '/listar',
                templateUrl: 'partials/faturamento/etapasNaoFaturadas/etapasNaoFaturadas.list.html',
                data: {
                    subtitulo: 'Pesquisar'
                }
            })
            .state('etapasnaofaturadas.novo', {
                url: '/{objectIdEtapa:int}/novo?acao&from',
                templateUrl: 'partials/faturamento/notasFiscais/html/notasFiscais.detail.html',
                controller: createController,
                data: {
                    titulo: 'Nota fiscal',
                    subtitulo: 'Novo'
                },
            })
            .state('etapasnaofaturadas.exibir', {
                url: '/{objectId:int}/exibir',
                templateUrl: 'partials/faturamento/etapasNaoFaturadas/cadastro.html',
                controller: viewController,
                data: {
                    subtitulo: 'Exibição'
                },
            })
            .state('etapasnaofaturadas.editar', {
                url: '/{objectId:int}/editar',
                templateUrl: 'partials/faturamento/etapasNaoFaturadas/cadastro.html',
                controller: editController,
                data: {
                    subtitulo: 'Edição'
                },
            });
    };

})();
