(function() {
    'use strict';

    angular
        .module('app.gerencia', [])
        .constant('moduleUrl', 'partials/administracao/gerencia')
        .factory('Gerencia', gerenciaFactory);

    function gerenciaFactory($resource, $restServer) {
        return $resource($restServer + 'rest/gerencia/:id/:action', null, {
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
            cancelar: {
                method: 'POST',
                params: {
                    'action': 'cancelar'
                }
            },
            buscarListaColaborador: {
                method: 'POST',
                params: {
                    'action': 'buscarListaColaborador'
                },
                isArray: true
            },
            buscarByUnidade: {
                method: 'POST',
                params: {
                    'action': 'buscarbyunidade'
                },
                isArray: true
            },
            buscarTodasGerenciasHabFat: {
                method: 'GET',
                url: $restServer + 'rest/gerencia/buscartodasgerenciasHabFat/',
                isArray: true
            },
            getGerenciaSimplesById: {
                method: 'GET',
                url: $restServer + 'rest/gerencia/getGerenciaSimplesById/:id'
			},
			getGerenciaSimplesAtivas: {
                method: 'GET',
                url: $restServer + 'rest/gerencia/getgerenciasimplesativas/',
                isArray: true
            },
			porPerfilUsuarioLogado: {
                method: 'GET',
                url: $restServer + 'rest/gerencia/perfil/diretoria/:id',
                isArray: true
            }
        });
    }

})();