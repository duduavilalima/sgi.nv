(function(){

	'use strict'

	angular.module('app.grafico', ['gridshore.c3js.chart'])

	.constant('moduleUrl', 'partials/fluxocaixa/grafico')

	.config(config);

	function config(moduleUrl, $stateProvider) {
		$stateProvider
			.state('grafico', {
				abstract: true,
				url: '/fluxocaixa/grafico',
				template: "<div ui-view></div>"
			})
			.state('grafico.graficos', {
				url: '/grafico',
				templateUrl: moduleUrl + '/grafico.html',
				data: {
					titulo: 'Fluxo de caixa - Gráfico',
					modulo: 'Fluxo de caixa',
					subtitulo: ''
				}
			});
		}
})();
