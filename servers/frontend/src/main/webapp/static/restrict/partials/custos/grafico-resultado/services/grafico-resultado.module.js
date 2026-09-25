(function() {
    angular.module('app.grafico-resultado', ['gridshore.c3js.chart', 'FBAngular'])

        .constant('moduleUrl', 'partials/custos/grafico-resultado/html')

        .config(config);

    function config(moduleUrl, $stateProvider) {
        $stateProvider
            .state('custos', {
                abstract: true,
                url: '/custos/grafico-resultado',
                template: "<div ui-view></div>"
            })
            .state('custos.graficos', {
                url: '/grafico-resultados',
                templateUrl: `${moduleUrl}/grafico-resultado.html`,
                data: {
                    titulo: 'Gráfico de resultados',
                    modulo: 'Custos',
                    subtitulo: ''
                }
            });
    }
})();