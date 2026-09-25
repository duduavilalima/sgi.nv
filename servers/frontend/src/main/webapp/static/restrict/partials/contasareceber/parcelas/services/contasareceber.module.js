(function() {
    'use strict';

    angular
        .module('app.contasareceber', ['utils', 'services', 'enumservice', 'autocompleteservice'])
        .constant('moduleUrl', 'partials/contasareceber/parcelas/html')
        .factory('Parcela', parcelaFactory)
        .factory('NotaSaidaParcelaBaixaRetencao', parcelaBaixaRetencaoFactory)
        .config(configContasAReceber);

	function parcelaFactory($resource, $restServer) {
		return $resource($restServer + 'rest/contasareceber/parcela/:id/:action', null, {
			updateParcela: {
				method: 'PUT',
				url: $restServer + 'rest/contasareceber/parcela/update/:id'
			},
			updateParcelaRecebimento: {
				method: 'PUT',
				url: $restServer + 'rest/contasareceber/parcela/recebimento/:id'
			},
			cancelarParcelaRecebimento: {
				method: 'GET',
				url: $restServer + 'rest/contasareceber/parcela/recebimento/cancelar/:id'
			},
			updateAgendamento: {
				method: 'PUT',
				url: $restServer + 'rest/contasareceber/parcela/updateAgendamento/:id'
			},
			confirmarOuAjustarDataAgendamento: {
				method: 'POST',
				url: $restServer + 'rest/contasareceber/parcela/confirmarOuAjustarDataAgendamento'
			},
			marcarCobrancaCronica: {
				method: 'PUT',
				url: $restServer + 'rest/contasareceber/parcela/updateMarcaCobrancaCronica/:id'
			},
			desmarcarCobrancaCronica: {
				method: 'PUT',
				url: $restServer + 'rest/contasareceber/parcela/updateDesmarcaCobrancaCronica/:id'
			},
			salvarObservacao: {
				method: 'POST',
				params: {'action': 'salvarObservacao'}
			},
            verificarLote: {
                method: 'GET',
                url: $restServer + 'rest/contasareceber/parcela/verificarLote/:id'
            }

		});
    }

    function parcelaBaixaRetencaoFactory($resource, $restServer) {
		return $resource($restServer + 'rest/notaSaidaBaixaRetencao/:pre_action/:id/:action', null, {
			obterBaixasRetencaoDaNotaSaidaParcela: {
				method: 'GET',
                params: {"pre_action":"baixas"},
                isArray: true
            },
            removerBaixaRetencao: {
				method: 'GET',
				url: $restServer + 'rest/notaSaidaBaixaRetencao/baixas/remover/:id'
            },
            verificarLoteBaixas: {
                method: 'GET',
				url: $restServer + 'rest/notaSaidaBaixaRetencao/verificarLoteBaixas/:id'
            }
		});
	}

    function configContasAReceber(moduleUrl, $stateProvider) {
        $stateProvider
            .state('contasareceber', {
                abstract: true,
                url: '/contasareceber',
                template: "<div ui-view></div>",
                data: {
                    modulo: 'Contas a receber',
                    titulo: 'Notas fiscais (parcelas)'
                }
            })
            .state('contasareceber.listar', {
                url: '/listar',
                templateUrl: moduleUrl + '/contasareceber.list.html',
                data: {
                    titulo: 'Notas fiscais (parcelas)',
                    subtitulo: 'Pesquisar',
                    modulo: 'Contas a receber'
                },
            })
            .state('contasareceber.exibir', {
                url: '/{objectId:int}/exibir',
                templateUrl: moduleUrl + '/partials/parcela_exibicao.html',
                controller: viewController,
                data: {
                    titulo: 'Nota fiscal (parcela)',
                    subtitulo: 'Exibição',
                    modulo: 'Contas a receber'
                },
            })
            .state('contasareceber.editar', {
                url: '/{objectId:int}/editar',
                templateUrl: moduleUrl + '/partials/parcela_edicao.html',
                controller: editController,
                data: {
                    titulo: 'Nota fiscal (parcela)',
                    subtitulo: 'Edição',
                    modulo: 'Contas a receber'
                },
            })
            .state('contasareceber.receber', {
                url: '/{objectId:int}/receber',
                templateUrl: moduleUrl + '/partials/parcela_edicao_abas.html',
                controller: receberController,
                data: {
                    titulo: 'Nota fiscal (parcela)',
                    subtitulo: 'Receber',
                    modulo: 'Contas a receber'
                },
            })
            .state('contasareceber.imprimir', {
				url: '/{objectId:int}/imprimir',
				templateUrl: 'partials/contasareceber/parcelas/html/contasareceber.imprimir.html',
				controller: viewController,
				data: {
					subtitulo: 'Imprimir'
				},
			});
    }
})();
