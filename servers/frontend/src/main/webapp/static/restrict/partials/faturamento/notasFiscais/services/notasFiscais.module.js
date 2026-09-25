(function() {
    'use strict';

    angular
        .module('app.notaFiscalFaturamento', ['utils', 'services', 'enumservice', 'autocompleteservice'])
        .constant('moduleUrl', 'partials/faturamento/notafiscal')
        .factory('NotaFiscalFaturamento', notafiscalFaturamentoFactory)
        .config(notafiscalFaturamentoConfig);

    function notafiscalFaturamentoFactory($resource, $restServer) {
        return $resource($restServer + 'rest/faturamento/notafiscal/:id/:action', null, {
            create: {
                method: 'POST',
                params: {
                    'action': 'montar'
                }
            },
            salvarNF: {
                method: 'POST',
                params: {
                    'action': 'salvarnf'
                }
            },
            numerarNF: {
                method: 'POST',
                params: {
                    'action': 'numerarnf'
                }
            },
            cancelarNotaFiscal: {
                method: 'POST',
                params: {
                    'action': 'cancelarnotafiscal'
                }
            },
            cancelarNumeracaoNF: {
                method: 'POST',
                params: {
                    'action': 'cancelarnumeracaonf'
                }
            },
            confirmarCadastroNF: {
                method: 'POST',
                params: {
                    'action': 'confirmarcadastronf'
                }
            },
            removerNF: {
                method: 'POST',
                params: {
                    'action': 'removernf'
                }
            },
            qbeNumNF: {
                method: 'POST',
                url: $restServer + 'rest/faturamento/notafiscal/busca/numero/notafiscal'
            },
            qbeNF: {
                method: 'POST',
                url: $restServer + 'rest/faturamento/notafiscal/busca/notafiscal'
            },
            buscarNfImpressao: {
                method: 'GET',
                url: $restServer + 'rest/faturamento/notafiscal/buscarnfimpressao/:id'
            },
            qbeST: {
                method: 'POST',
                params: {
                    'action': 'servicotipo'
                },
                isArray: true
            },
            alterarValorBruto: {
                method: 'POST',
                params: {
                    'action': 'alterarValorBruto'
                }
            },
            salvarProtocolo: {
                method: 'POST',
                params: {
                    'action': 'salvarProtocolo'
                }
            },
            pesquisaPorNumeroAutoComplete: {
                method: 'POST',
                params: {
                    'action': 'pesquisaPorNumeroAutoComplete'
                },
                isArray: true
            },
            atualizarAssociacaoNfSubstituta: {
                method: 'POST',
                params: {
                    'action': 'atualizarAssociacaoNfSubstituta'
                }
            },
            verificarNfLote: {
                method: 'GET',
                url: $restServer + 'rest/faturamento/notafiscal/verificarNfLote/:id'
            },
			verificarContaContabilCliente: {
				method: 'GET',
				url: $restServer + 'rest/faturamento/notafiscal/verificarContaContabilCliente/:id'
			}
        });
    };

    function notafiscalFaturamentoConfig(moduleUrl, $stateProvider) {
        $stateProvider
            .state('notafiscal', {
                abstract: true,
                url: '/notafiscal',
                template: "<div ui-view></div>",
                data: {
                    modulo: 'Faturamento',
                    titulo: 'Notas fiscais'
                }
            })
            .state('notafiscal.listar', {
                url: '/listar',
                templateUrl: 'partials/faturamento/notasFiscais/html/notasFiscais.list.html',
                data: {
                    subtitulo: 'Pesquisar'
                },
            })
            .state('notafiscal.novo', {
                url: '/{objectIdEtapa:int}/novo?acao&from&objectId',
                templateUrl: 'partials/faturamento/notasFiscais/html/notasFiscais.detail.html',
                controller: createController,
                data: {
                    subtitulo: 'Novo'
                },
            })
            .state('notafiscal.exibir', {
                url: '/{objectId:int}/exibir',
                templateUrl: 'partials/faturamento/notasFiscais/html/notasFiscais.exibir.html',
                controller: viewController,
                data: {
                    subtitulo: 'Exibição'
                },
            })
            .state('notafiscal.exibir_por_acao', {
                url: '/{objectId:int}/exibir?acao&from',
                templateUrl: 'partials/faturamento/notasFiscais/html/notasFiscais.exibir.html',
                controller: viewController,
                data: {
                    subtitulo: 'Exibição'
                },
            })
            .state('notafiscal.imprimir', {
                url: '/{objectId:int}/{tipoimpressao}/imprimir',
                templateUrl: 'partials/faturamento/notasFiscais/html/notasFiscais.imprimir.html',
                controller: viewController,
                data: {
                    subtitulo: 'Imprimir'
                },
            });
    }

})();
