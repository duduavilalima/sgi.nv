(function() {
    'use strict';

    angular
        .module('app.faturamento', ['utils', 'services', 'enumservice', 'autocompleteservice', 'usuariologadoservice'])
        .constant('moduleUrl', 'partials/faturamento/faturamento')
        .factory('Faturamento', faturamentoFactory)
        .factory('FaturamentoEtapa', faturamentoEtapaFactory)
        .factory('FaturamentoAliquota', faturamentoAliquotaFactory)
        .factory('FaturamentoRetencao', faturamentoRetencaoFactory)
        .factory('FaturamentoContato', faturamentoContatoFactory)
        .config(configFaturamento);

    function faturamentoFactory($resource, $restServer) {
        return $resource($restServer + 'rest/faturamento/:primaryAction/:id/:action', null, {
            notaFiscal: {
                method: 'POST',
                url: $restServer + 'rest/faturamento/busca/notafiscal'
            },
            notaFiscalPaginado: {
                method: 'POST',
                url: $restServer + 'rest/faturamento/busca/notafiscal/paginado'
            },
            notaDebitoPaginado: {
				method: 'POST',
				url: $restServer + 'rest/faturamento/busca/notadebito/paginado'
			},
            faturamentoanterior: {
                method: 'GET',
                params: {
                    'primaryAction': 'faturamentoanterior'
                }
            },
            notaFiscalAbertaCheck: {
                method: 'GET',
                url: $restServer + 'rest/faturamento/notafiscalabertacheck/:id'
            },
            verificarContaContabilCliente: {
				method: 'GET',
				url: $restServer + 'rest/faturamento/verificarContaContabilCliente/:id'
			}
        });
    }

    function faturamentoEtapaFactory($resource, $restServer) {
        return $resource($restServer + 'rest/faturamento/etapa/:id/:action', null, {
            proximoNumeroEtapaByFatId: {
                method: 'GET',
                url: $restServer + 'rest/faturamento/etapa/proximonumeroetapa/:id'
            },
            getByCodNota: {
                method: 'GET',
                url: $restServer + 'rest/faturamento/etapa/getByCodNota/:id'
            },
            verificarDescricaoEtapaDeFaturamento: {
                method: 'POST',
                url: $restServer + 'rest/faturamento/etapa/verificarDescricaoEtapaDeFaturamento'
            },
            etapaAFaturarCheck: {
                method: 'GET',
                url: $restServer + 'rest/faturamento/etapa/etapaafaturarcheck/:id'
            },
			getByCodigo: {
				method: 'GET',
				url: $restServer + 'rest/faturamento/etapa/getbycodigo/:id'
			}

        });
    }

    function faturamentoAliquotaFactory($resource, $restServer) {
        return $resource($restServer + 'rest/faturamentoaliquota/:id/:action', null, {});
    }

    function faturamentoRetencaoFactory($resource, $restServer) {
        return $resource($restServer + 'rest/faturamentoretencao/:id', null, {
            listarPorFaturamento: {
                method: 'GET',
                url: $restServer + 'rest/faturamentoretencao/listarporfaturamento/:id',
                isArray: true
            }
        });
    }

    function faturamentoContatoFactory($resource, $restServer) {
        return $resource($restServer + 'rest/faturamentocontato/:id', null, {
            listaByFatCod: {
                method: 'GET',
                url: $restServer + 'rest/faturamentocontato/listaByFatCod/:id',
                isArray: true
            },
            remover: {
                method: 'GET',
                url: $restServer + 'rest/faturamentocontato/remover/:id'
            }
        });
    }

    function configFaturamento(moduleUrl, $stateProvider) {
        $stateProvider
            .state('faturamento', {
                abstract: true,
                url: '/faturamento',
                template: "<div ui-view></div>",
                data: {
                    modulo: 'Faturamento',
                    titulo: 'Faturamento'
                }
            }).state('faturamento.listar', {
                url: '/listar',
                templateUrl: moduleUrl + '/faturamento.list.html',
                data: {
                    subtitulo: 'Pesquisar'
                }
            }).state('faturamento.novo', {
                url: '/novo',
                templateUrl: moduleUrl + '/faturamento.detail.novo.html',
                controller: createController,
                data: {
                    subtitulo: 'Novo'
                },
            }).state('faturamento.exibir', {
                url: '/{objectId:int}/exibir',
                templateUrl: moduleUrl + '/faturamento.detail.html',
                controller: viewController,
                data: {
                    subtitulo: 'Exibição'
                },
            }).state('faturamento.exibir_action', {
                url: '/{objectId:int}/exibir?acao&from',
                templateUrl: moduleUrl + '/faturamento.detail.html',
                controller: viewController,
                data: {
                    subtitulo: 'Exibição'
                },
            });
    }

})();
