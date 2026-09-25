(function() {
    'use strict';

    angular
        .module('app.relacaobancaria', [])

        .constant('htmlUrl', 'partials/tesouraria/relacao_bancaria/html')

        .factory('RelacaoBancaria', relacaoBancariaFactory)

        .config(config);

    function relacaoBancariaFactory($resource, $restServer) {
        return $resource($restServer +
            'rest/tesouraria/relacaobancaria/:actionItem/:id/:action', null, {
                cancel: {
                    method: 'GET',
                    params: {
                        'action': 'cancelar'
                    }
                },
                send: {
                    method: 'GET',
                    params: {
                        'action': 'enviar'
                    }
                },
                conciliar: {
                    method: 'GET',
                    params: {
                        'action': 'conciliar'
                    }
                },
                baixamanual: {
                    method: 'POST',
                    params: {
                        'action': 'baixamanual'
                    }
                },
                estornobaixamanual: {
                    method: 'POST',
                    params: {
                        'action': 'estornobaixamanual'
                    }
                },
                realizarBaixaBancaria: {
                    method: 'POST',
                    params: {
                        'action': 'realizarbaixabancaria'
                    }
                },
                removeItem: {
                    method: 'GET',
                    params: {
                        'actionItem': 'itemrelacao',
                        'action': 'remover'
                    }
                },
                editItem: {
                    method: 'POST',
                    params: {
                        'actionItem': 'itemrelacao',
                        'action': 'editar'
                    }
                },
                pesquisarpagamentos: {
                    method: 'POST',
                    params: {
                        'action': 'pesquisarpagamentos'
                    }
                },
                adicionarpagamentos: {
                    method: 'POST',
                    params: {
                        'action': 'adicionarpagamentos'
                    }
                },
                retornobancarioestornado: {
                    method: 'GET',
                    params: {
                        'actionItem': 'retornobancario',
                        'action': 'estornado'
                    },
                    isArray: true
                },
                buscarultimocheque: {
                    method: 'GET',
                    params: {
                        'action': 'buscarultimocheque'
                    }
                },
                imprimircheques: {
                    method: 'POST',
                    params: {
                        'actionItem': 'imprimircheques'
                    }
                },
                itensrelacao: {
                    method: 'GET',
                    params: {
                        'actionItem': 'itemrelacao'
                    },
                    isArray: true
                },
                obterListaDeItensRelacao: {
                    method: 'GET',
                    params: {
                        'action': 'itensrelacao'
                    },
                    isArray: true
                },
                baixarComprovante: {
                    method: 'GET',
                    params: {
                        'action': 'baixarComprovante'
                    },
                },
                baixarComprovanteExiste: {
                    method: 'GET',
                    url: $restServer + 'rest/tesouraria/relacaobancaria/baixarComprovante/existe/:id',
                },
                getItemRelacaoVO: {
                    method: 'POST',
                    url: $restServer + 'rest/tesouraria/relacaobancaria/itemRelacao/vo',
                }
            });
    }

    function config(htmlUrl, $stateProvider) {
        $stateProvider
            .state('relacaobancaria', {
                abstract: true,
                url: '/tesouraria/relacaobancaria',
                template: "<div ui-view></div>",
                data: {
                    titulo: 'Relação bancária',
                    modulo: 'Tesouraria',
                }
            })
            .state('relacaobancaria.list', {
                url: '/listar',
                templateUrl: htmlUrl + '/relacao_bancaria.list.html',
                data: {
                    subtitulo: ''
                }
            })
            .state('relacaobancaria.new', {
                url: '/novo',
                templateUrl: htmlUrl + '/relacao_bancaria.detail.html',
                controller: createController,
                data: {
                    subtitulo: 'Novo'
                }
            })
            .state('relacaobancaria.view', {
                url: '/{objectId:int}',
                templateUrl: htmlUrl + '/relacao_bancaria.detail.html',
                controller: viewController,
                data: {
                    subtitulo: ''
                }
            })
            .state('relacaobancaria.edit', {
                url: '/{objectId:int}/editar',
                templateUrl: htmlUrl + '/relacao_bancaria.detail.html',
                controller: editController,
                data: {
                    subtitulo: 'Editar'
                }
            })
            .state('relacaobancaria.baixa', {
                url: '/{objectId:int}/conciliacaobancaria',
                templateUrl: htmlUrl + '/baixa_bancaria_pagamento.detail.html',
                controller: 'ConciliacaoBancariaCtrl',
                data: {
                    subtitulo: 'Conciliacao bancária'
                }
            })
            .state('relacaobancaria.manual', {
                url: '/{objectId:int}/conciliacaomanual',
                templateUrl: htmlUrl + '/conciliacao_manual_pagamento.detail.html',
                controller: 'ConciliacaoManualCtrl',
                data: {
                    subtitulo: 'Baixa Bancária Manual'
                }
            })
            .state('relacaobancaria.estornomanual', {
                url: '/{objectId:int}/estornobaixamanual',
                templateUrl: htmlUrl + '/conciliacao_manual_pagamento.detail.html',
                controller: 'ConciliacaoManualCtrl',
                data: {
                    subtitulo: 'Estorno Baixa Bancária Manual',
                    estorno: true,
                }
            })
            .state('relacaobancaria.imprimir_cheques', {
                url: '/{objectId:int}/imprimir/{tipo_cheque:string}',
                templateUrl: htmlUrl + '/imprimir_cheques.detail.html',
                controller: 'ImprimirChequesCtrl',
                data: {
                    subtitulo: 'Impressão de cheques'
                }
            });
    }
})();
