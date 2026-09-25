(function() {
    angular.module('app.comparativos', ['gridshore.c3js.chart', 'FBAngular'])

        .constant('moduleUrl', 'partials/custos/comparativos/html')

        .config(configComparativos);

    function configComparativos(moduleUrl, $stateProvider) {
        $stateProvider
            .state('comparativos', {
                abstract: true,
                url: '/custos',
                template: "<div ui-view></div>"
            })
            .state('comparativos.graficos', {
                url: '/grafico-comparativos',
                templateUrl: `${moduleUrl}/comparativos.html`,
                controller: 'ComparativosCtrl',
                controllerAs: '$ctrl'
            });
    }
})();