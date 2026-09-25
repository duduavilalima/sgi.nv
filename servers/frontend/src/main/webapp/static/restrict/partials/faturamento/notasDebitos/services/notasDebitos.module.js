(function() {
    'use strict';

    angular
        .module('app.notaDebito', ['utils', 'services', 'enumservice', 'autocompleteservice'])
        .constant('moduleUrl', 'partials/faturamento/notadebito')
        .factory('NotaDebito', notadebitoFactory)
        .factory('MotivoRetornoDocumentoNotaDebito', notadebitomotivoretornoFactory)
        .config(notadebitoConfig);

	function notadebitomotivoretornoFactory($resource, $restServer) {
		return $resource($restServer + 'rest/motivo/retorno/notadebito/:id/:action', null, {
				retornarNotaDebito: {
					method: 'POST',
					url: $restServer + 'rest/motivo/retorno/notadebito/retornarNotaDebito'
				}
            });
        };

    function notadebitoFactory($resource, $restServer) {
        return $resource($restServer + 'rest/faturamento/notadebito/:id/:action', null, {
            create: {
                method: 'GET',
                url: $restServer + 'rest/faturamento/notadebito/montar/:idEtapa'
            },
            salvarND: {
                method: 'POST',
                params: {
                    'action': 'salvarnd'
                }
            },
            numerarND: {
                method: 'POST',
                params: {
                    'action': 'numerarnd'
                }
            },
            cancelarNotaDebito: {
                method: 'POST',
                params: {
                    'action': 'cancelarnotadebito'
                }
            },
            cancelarNumeracaoND: {
                method: 'POST',
                params: {
                    'action': 'cancelarnumeracaond'
                }
            },
            confirmarCadastroND: {
                method: 'POST',
                params: {
                    'action': 'confirmarcadastrond'
                }
            },
            removerND: {
                method: 'POST',
                params: {
                    'action': 'removernd'
                }
            },
            qbeNumND: {
                method: 'POST',
                url: $restServer + 'rest/faturamento/notadebito/busca/numero/notadebito'
            },
            qbeND: {
                method: 'POST',
                url: $restServer + 'rest/faturamento/notadebito/busca/notadebito'
            },
            qbeST: {
				method: 'POST',
				params: {
					'action': 'servicotipo'
				},
				isArray: true
			},
            buscarNdImpressao: {
                method: 'GET',
                url: $restServer + 'rest/faturamento/notadebito/buscarndimpressao/:id'
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
            atualizarAssociacaoNdSubstituta: {
                method: 'POST',
                params: {
                    'action': 'atualizarAssociacaoNdSubstituta'
                }
            },
            receberNotaDebito: {
            	method: 'POST',
            	params: {
            		'action': 'receber'
            	}
            },
            verificarNdLote: {
                method: 'GET',
                url: $restServer + 'rest/faturamento/notadebito/verificarNdLote/:id'
            },
            buscarPorId: {
				method: 'GET',
				url: $restServer + 'rest/faturamento/notadebito/buscarporid/:id'
			}
        });
    };

    function notadebitoConfig(moduleUrl, $stateProvider) {
        $stateProvider
            .state('notadebito', {
                abstract: true,
                url: '/notadebito',
                template: "<div ui-view></div>",
                data: {
                    modulo: 'Faturamento',
                    titulo: 'Notas de débito'
                }
            })
            .state('notadebito.listar', {
                url: '/listar',
                templateUrl: 'partials/faturamento/notasDebitos/html/notasDebitos.list.html',
                data: {
                    subtitulo: 'Pesquisar'
                },
            })
            .state('notadebito.novo', {
                url: '/{objectIdEtapa:int}/novo?acao&from&objectId',
                templateUrl: 'partials/faturamento/notasDebitos/html/notasDebitos.detail.html',
                controller: createController,
                data: {
                    subtitulo: 'Novo'
                },
            })
            .state('notadebito.exibir', {
                url: '/{objectId:int}/exibir',
                templateUrl: 'partials/faturamento/notasDebitos/html/notasDebitos.exibir.html',
                controller: viewController,
                data: {
                    subtitulo: 'Exibição'
                },
            })
            .state('notadebito.exibir_por_acao', {
                url: '/{objectId:int}/exibir?acao&from',
                templateUrl: 'partials/faturamento/notasDebitos/html/notasDebitos.exibir.html',
                controller: viewController,
                data: {
                    subtitulo: 'Exibição'
                },
            })
            .state('notadebito.imprimir', {
                url: '/{objectId:int}/imprimir',
                templateUrl: 'partials/faturamento/notasDebitos/html/notasDebitos.imprimir.html',
                controller: viewController,
                data: {
                    subtitulo: 'Imprimir'
                },
            });
    }

})();
