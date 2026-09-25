(function() {
    angular.module('app.custos.relatorios', [])

        .constant('moduleUrl', 'partials/custos/relatorios/html')

        .config(configRelatoriosCustos);

    function configRelatoriosCustos(moduleUrl, $stateProvider) {
        $stateProvider
            .state('relatorio_custos', {
                abstract: true,
                url: '/custos',
                template: "<div ui-view></div>"
            })
            .state('relatorio_custos.relatorio', {
                url: '/relatorio',
                templateUrl: `${moduleUrl}/relatorio.html`,
                controller: 'RelatorioCustosCtrl',
                controllerAs: '$ctrl'
            });
    }
})();