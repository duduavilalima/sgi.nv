(function() {
    angular.module('app.rubricas', [])

        .constant('moduleUrl', 'partials/custos/cadastros/rubricas/html')
        .factory('Rubricas', rubricasFactory)
        .config(config);

    function rubricasFactory($resource, $restServer) {
        return $resource(`${$restServer}rest/despesacustos/:primaryAction/:id/:action`, null, {
            obterListaDespesasSimplesAtivas: {
				method: 'GET',
				params: { 'primaryAction': 'obterListaDespesasSimplesAtivas' },
				isArray: true
			}
        });
    }

    function config(moduleUrl, $stateProvider) {
        $stateProvider
            .state('rubricas', {
                abstract: true,
                url: '/custos/cadastros/rubricas',
                template: "<div ui-view></div>"
            }).state('rubricas.listar', {
                url: '/listar',
                templateUrl: `${moduleUrl}/list.html`,
                controller: 'RubricasListarCtrl',
                controllerAs: '$ctrl'
            }).state('rubricas.novo', {
                url: '/novo',
                templateUrl: `${moduleUrl}/new.html`,
                controller: createController,
                controllerAs: '$ctrl'
            }).state('rubricas.editar', {
                url: '/{objectId:int}/editar',
                templateUrl: `${moduleUrl}/edit.html`,
                controller: editController,
                controllerAs: '$ctrl'
            })
            .state('rubricas.exibir', {
                url: '/{objectId:int}',
                templateUrl:`${moduleUrl}/view.html`,
                controller: viewController
            }).state('rubricas.exibir_action', {
                url: '/{objectId:int}?acao&from',
                templateUrl:`${moduleUrl}/edit.html`,
                controller: viewController,
                controllerAs: '$ctrl'
            });
    }
})();
