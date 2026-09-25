(function() {
    angular
        .module('app.orcamento-empresarial', [])
        .constant('moduleUrl', 'partials/custos/cadastros/orcamento-empresarial/html')
        .factory('OrcamentoEmpresarial', orcamentoEmpresarialFactory)
        .factory('FileUploadCustos', fileUploadCustos)
        .config(config);

    function fileUploadCustos($http, $resource, $restServer) {
        // return function(importacaoOrcamentoEmpresarialTO, successCallback, errorCallback) {
        //     return $http({
        //         method: 'POST',
        //         url: $restServer + `rest/custos/upload/planilha/${importacaoOrcamentoEmpresarialTO.fileName.split(".")[0]}/${importacaoOrcamentoEmpresarialTO.fileName.split(".")[1]}`,
        //         data: importacaoOrcamentoEmpresarialTO,
        //         // headers: { 'Content-Type': undefined },
        //         transformRequest: angular.identity
        //     }).then(successCallback, errorCallback);
        // };
        return $resource(`${$restServer}rest/custos/upload/planilha/:file/:sufixo`, null, {
            importacaoOrcamentoEmpresaria: {
				method: 'POST'
			}
        });
    }

    function orcamentoEmpresarialFactory($resource, $restServer) {
        return $resource($restServer + 'rest/previsaocustos/:primaryAction/:id/:action', null, {});
    }

    function config(moduleUrl, $stateProvider) {
        $stateProvider
            .state('orcamento-empresarial', {
                abstract: true,
                url: '/custos/cadastros/orcamento-empresarial',
                template: "<div ui-view></div>"
            }).state('orcamento-empresarial.importacao', {
                url: '/importacao',
                templateUrl: moduleUrl + '/importacao.html',
                controller: 'OrcamentoEmpresarialImportacaoCtrl',
                controllerAs: '$ctrl'
            }).state('orcamento-empresarial.listar', {
                url: '/listar',
                templateUrl: moduleUrl + '/list.html',
                controller: 'OrcamentoEmpresarialListarCtrl',
                controllerAs: '$ctrl'
            }).state('orcamento-empresarial.novo', {
                url: '/novo',
                templateUrl: moduleUrl + '/new.html',
                controller: createController,
                controllerAs: '$ctrl'
            }).state('orcamento-empresarial.editar', {
                url: '/{objectId:int}/editar',
                templateUrl: moduleUrl + '/edit.html',
                controller: editController,
                controllerAs: '$ctrl'
            }).state('orcamento-empresarial.exibir', {
                url: '/{objectId:int}',
                templateUrl: moduleUrl + '/view.html',
                controller: viewController,
                controllerAs: '$ctrl'
            }).state('orcamento-empresarial.exibir_action', {
                url: '/{objectId:int}?acao&from',
                templateUrl: moduleUrl + '/edit.html',
                controller: viewController,
                controllerAs: '$ctrl'
            });
    }
})();
