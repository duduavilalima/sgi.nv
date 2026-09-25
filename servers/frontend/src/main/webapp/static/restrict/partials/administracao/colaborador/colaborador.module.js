(function() {
	'use strict';

	angular
		.module('app.colaborador', [])

		.constant('moduleUrl', 'partials/administracao/colaborador')

		.factory('Colaborador', colaboradorFactory)

		function colaboradorFactory($resource, $restServer) {
			return $resource($restServer + 'rest/colaborador/:id/:action', null, {
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
				cancelar: {
					method:'POST',
					params: {'action':'cancelar'}
				},
				buscarListaColaborador: {
					method: 'POST',
					params: {'action':'buscarListaColaborador'},
					isArray: true
				}
			});
		};

})();
