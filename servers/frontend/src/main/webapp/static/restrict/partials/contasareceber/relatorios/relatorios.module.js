(function() {
    'use strict';

    angular
        .module('app.contasareceberRelatorios', ['utils', 'services', 'enumservice'])

        .constant('moduleUrl', 'partials/contasareceber/relatorios')

        .config(configCCRelatorios);


    function configCCRelatorios(moduleUrl, $stateProvider) {
        $stateProvider
			.state('contasareceber.relatorios', {
				url: '/relatorios',
				templateUrl: 'partials/contasareceber/relatorios/contasareceber.relatorio.html',
				controller: 'ContasareceberRelatoriosListarCtrl',
				data: {
					titulo: 'Relatórios',
					subtitulo: 'Relatório',
					modulo: 'Contas a receber'
				}
			});
    };

})();
