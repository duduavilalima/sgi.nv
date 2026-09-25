(function(){

	'use strict'

	angular
		.module('app.fluxocaixa.relatorios', [])

		.constant('moduleUrl', 'partials/fluxocaixa/relatorios')

		.config(config);

		function config(moduleUrl, $stateProvider) {
			$stateProvider
				.state('fluxocaixa_relatorios', {
					abstract: true,
					url: '/fluxocaixa',
					template: "<div ui-view></div>"
				})
				.state('fluxocaixa_relatorios.list', {
					url: '/relatorios',
					templateUrl: moduleUrl + '/relatorio.html',
					controller: 'FluxoCaixaRelatoriosCtrl',
					data: {
						titulo: 'Relatorios',
						modulo: 'Fluxo de Caixa',
						subtitulo: ''
					}
				});
		};
})();
