(function(){

	'use strict'

	angular
		.module('app.comercialRelatorios', [])

		.constant('htmlUrl', 'partials/comercial')

		.config(configComercial);

    function configComercial(htmlUrl, $stateProvider) {
        $stateProvider
			.state('comercialRelatorios', {
				abstract: true,
				url: '/comercial',
				template: "<div ui-view></div>"
			})
			.state('comercialRelatorios.relatorios', {
			 url: '/relatorios',
			 templateUrl: htmlUrl + '/relatorios/html/view.html',
			 controller: 'RelatoriosComercialCtrl',
			 controllerAs: '$ctrl',
			 data: {
				 titulo: 'Relatórios',
				 subtitulo: 'Pesquisar'
			 }
			})
    };
})();
