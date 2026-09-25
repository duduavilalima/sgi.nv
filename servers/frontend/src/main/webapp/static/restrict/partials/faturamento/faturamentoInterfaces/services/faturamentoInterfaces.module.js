(function() {
    'use strict';

    angular
        .module('app.faturamentoInterfaces', ['utils', 'services', 'enumservice'])

        .constant('moduleUrl', 'partials/faturamento/faturamentoInterfaces/html')

        .factory('FaturamentoInterfaces', faturamentoInterfacesFactory)

        .config(faturamentoInterfaces);

    function faturamentoInterfacesFactory($resource, $restServer) {
        return $resource($restServer + 'rest/faturamento/interfaces/:id/:action', null, {
            create: {
                method: 'GET',
                params: {
                    'action': 'create'
                }
            },
            get: {
                method: 'GET'
            },
            reprocessar: {
                method: 'GET',
                params: {
                    'action': 'reprocessar'
                }
            },
            processar: {
                method: 'GET',
                params: {
                    'action': 'processar'
                }
            },
            confirmarEnvio: {
                method: 'GET',
                params: {
                    'action': 'confirmarEnvio'
                }
            },
            baixaranexo: {
                method: 'GET',
                url: $restServer + 'rest/faturamento/interfaces/baixaranexo/:id'
            },
            verificaranexo: {
                method: 'POST',
                params: {
                    'action': 'verificaranexo'
                }
            },
            anexolista: {
                method: 'GET',
                params: {
                    'action': 'anexolista'
                },
                isArray: true
            },
            qtdlote: {
                method: 'POST',
                params: {
                    'action': 'qtdlote'
                },
                cache: true
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
            copy: {
                method: 'POST',
                params: {
                    'action': 'copiar'
                }
            },
            deleteList: {
                method: 'POST',
                params: {
                    'action': 'deletar/'
                }
            },
            getNotasFiscaisLotePrefeitura: {
                method: 'GET',
                url: $restServer + 'rest/faturamento/interfaces/listaNotaFiscalPrefeitura/:id',
                isArray: true
            },
            verificarNotaFiscalTipoPrefeitura: {
                method: 'POST',
                params: {
                    'action': 'verificarNotaFiscalTipoPrefeitura'
                },
                isArray: true
            }
        });
    };

    function faturamentoInterfaces(moduleUrl, $stateProvider) {
        $stateProvider
            .state('interfaces', {
                abstract: true,
                url: '/interfaces',
                template: "<div ui-view></div>",
                data: {
                    modulo: 'Faturamento',
                    titulo: 'Lote'
                }
            })
            .state('interfaces.listar', {
                url: '/listar',
                templateUrl: 'partials/faturamento/faturamentoInterfaces/html/faturamentoInterfaces.list.html',
                data: {
                    subtitulo: 'Pesquisar'
                }
            })
            .state('interfaces.novo', {
                url: '/novo',
                templateUrl: 'partials/faturamento/faturamentoInterfaces/html/faturamentoInterfaces.detail.html',
                controller: createController,
                data: {
                    subtitulo: 'Novo'
                },
            })
            .state('interfaces.exibir', {
                url: '/{objectId:int}/exibir',
                templateUrl: 'partials/faturamento/faturamentoInterfaces/html/faturamentoInterfaces.detail.html',
                controller: viewController,
                data: {
                    subtitulo: 'Exibição'
                },
            })
            .state('interfaces.editar', {
                url: '/{objectId:int}/editar',
                templateUrl: 'partials/faturamento/faturamentoInterfaces/html/faturamentoInterfaces.detail.html',
                controller: viewController,
                data: {
                    titulo: 'Lote',
                    subtitulo: 'Edição'
                },
            })
    };

})();
