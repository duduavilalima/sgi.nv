(function() {
	'use strict';

	angular
		.module('app.regional', [])

		.constant('moduleUrl', 'partials/administracao/regional')

		.factory('Regional', regionalFactory);

		function regionalFactory($resource, $restServer) {
			return $resource($restServer + 'rest/regional/:id/:action', null, {
				get: {
					method: 'GET'
				},
				create: {
					method: 'GET',
					params: {'action':'create'}
				},
				update: {
					method: 'PUT'
				},
				qbe: {
					method: 'POST',
					params: {'action':'busca'}
				},
				remover: {
					method: 'GET',
                    params: {'action':'remover'}
				},
				buscarPorCodigoUnidade: {
					 method: 'GET',
					 params: {'action':'listar'},
					 isArray: true
				},
				buscarListaAtiva: {
					 method: 'GET',
					 params: {'action':'listaAtiva'},
					 isArray: true
				}
			});
		};

})();
