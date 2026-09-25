(function(){

	'use strict'

	angular
		.module('app.relatorio', [])

		.constant('moduleUrl', 'partials/tesouraria/relatorios')
		.config(config);

		function config(moduleUrl, $stateProvider) {
			$stateProvider
				.state('relatorios', {
					abstract: true,
					url: '/tesouraria/relatorios',
					template: "<div ui-view></div>"
				})
				.state('relatorios.list', {
					url: '/relatorios',
					templateUrl: moduleUrl + '/relatorio.html',
					controller: 'RelatoriosCtrl',
					data: {
						titulo: 'Relatorios',
						modulo: 'Tesouraria',
						subtitulo: ''
					}
				});
		};
})();
