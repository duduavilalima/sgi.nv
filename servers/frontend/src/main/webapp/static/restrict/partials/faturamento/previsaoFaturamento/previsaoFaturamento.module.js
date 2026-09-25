(function() {
    'use strict';

    angular
        .module('app.previsaoFaturamento', ['utils', 'services', 'enumservice'])

        .constant('moduleUrl', 'partials/faturamento/previsaoFaturamento')

        .factory('PrevisaoFaturamento', previsaoFaturamentoFactory)

        .config(previsaoFaturamento);

    function previsaoFaturamentoFactory($resource, $restServer) {
        return $resource($restServer + 'rest/centrofinanceiroprevisao/:id/:action', null, {
            get: {
                method: 'GET'
            },
            update: {
                method: 'PUT'
            },
            create: {
                method: 'GET',
                params: {'action':'create'}
            },
            buscarPrevisoes: {
            	method: 'POST',
				url: $restServer + 'rest/centrofinanceiroprevisao/buscarPrevisoes',
				isArray: true
            },
            updatePrevisoes: {
            	method: 'POST',
				url: $restServer + 'rest/centrofinanceiroprevisao/updatePrevisoes'
            }
        });
    };

    function previsaoFaturamento(moduleUrl, $stateProvider) {
        $stateProvider
            .state('previsaoFaturamento', {
				abstract: true,
				url: '/previsaoFaturamento',
				template: "<div ui-view></div>",
				data: {
					modulo: 'Faturamento',
					titulo: 'Previsão de faturamento'
				}
			})
            .state('previsaoFaturamento.filtro', {
                url: '/filtro',
                templateUrl: moduleUrl + '/previsaoFaturamento.filtro.html',
                data: {
                    titulo: 'Previsão de faturamento',
                    subtitulo: 'Filtro'
                }
            })
            .state('previsaoFaturamento.exibir', {
				url: '/exibir',
				params: {
					param_competencia: null,
                    param_tipo_faturamento: null,
//                    param_ordem: null,
//                    param_cod_empresa: null,
                    param_gerencia: null,
                    param_cl: null,
                    param_screen_mode: null
				},
				templateUrl: moduleUrl + '/previsaoFaturamento.detail.html',
				data: {
					titulo: 'Previsão de faturamento',
					subtitulo: 'Exibição'
				}
			})
			.state('previsaoFaturamento.editar', {
				url: '/exibir',
				params: {
					param_competencia: null,
					param_tipo_faturamento: null,
//                    param_ordem: null,
//                    param_cod_empresa: null,
					param_gerencia: null,
					param_cl: null,
					param_screen_mode: null
				},
				templateUrl: moduleUrl + '/previsaoFaturamento.detail.html',
				data: {
					titulo: 'Previsão de faturamento',
					subtitulo: 'Editar'
				}
			})
    };

})();
