(function() {
    'use strict';

    angular
        .module('app.tiposServico', ['utils', 'services', 'enumservice'])

        .constant('moduleUrl', 'partials/faturamento/tiposServico')

        .factory('TiposServico', tiposServicoFactory)

        .config(tiposServico);

    function tiposServicoFactory($resource, $restServer) {
        return $resource($restServer + 'rest/servico_tipo/:id/:action', null, {
            buscarServicoTipo: {
				 method: 'GET',
				 url: $restServer + 'rest/servico_tipo/buscarservicotipo'
			},
			buscarReinfServicoByCodServicoTipo: {
				method: 'GET',
				url: $restServer + 'rest/servico_tipo/buscarReinfServicoByCodServicoTipo/:id'
			}
        });
    };

    function tiposServico(moduleUrl, $stateProvider) {
        $stateProvider
            .state('tiposServico', {
                abstract: true,
                url: '/tiposservico',
                template: "<div ui-view></div>",
                data: {
                    modulo: 'Faturamento',
                    titulo: 'Tipos de serviço'
                }
            })
            .state('tiposServico.list', {
                url: '/listar',
                templateUrl: 'partials/faturamento/tiposServico/tiposServico.list.html',
                data: {
                    titulo: 'Tipos de serviço',
                    subtitulo: 'Pesquisar'
                }
            })
            .state('tiposServico.novo', {
                url: '/novo',
                templateUrl: 'partials/faturamento/tiposServico/tiposServico.detail.html',
                controller: createController,
                data: {
                    titulo: 'Tipos de serviço',
                    subtitulo: 'Novo'
                },
            })
            .state('tiposServico.exibir', {
                url: '/{objectId:int}/exibir',
                templateUrl: 'partials/faturamento/tiposServico/tiposServico.detail.html',
                controller: viewController,
                data: {
                    titulo: 'Tipos de serviço',
                    subtitulo: 'Exibição'
                },
            })
            .state('tiposServico.editar', {
                url: '/{objectId:int}/editar',
                templateUrl: 'partials/faturamento/tiposServico/tiposServico.detail.html',
                controller: editController,
                data: {
                    titulo: 'Tipos de serviço',
                    subtitulo: 'Edição'
                },
            })
    };

})();
