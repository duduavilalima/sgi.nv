(function() {
    'use strict';

    angular
        .module('app.faturamentoRelatorios', ['utils', 'services', 'enumservice'])

        .constant('moduleUrl', 'partials/faturamento')

        .config(configFaturamento);

    function configFaturamento(moduleUrl, $stateProvider) {
        $stateProvider
			 .state('faturamento.relatorios', {
				 url: '/relatorios',
				 templateUrl: moduleUrl + '/relatorios/relatorio.html',
				 data: {
					 titulo: 'Relatórios',
					 subtitulo: 'Pesquisar'
				 }
			 })
    };

})();
