angular
    .module('app.orcamento-empresarial')
    .controller('OrcamentoEmpresarialListarCtrl', listOrcamentoEmpresarialController);

function listOrcamentoEmpresarialController($scope, $controller, $state, OrcamentoEmpresarial, $rootScope, UsuarioLogadoService) {
    $controller('GridController', {
        $scope: $scope,
        Resource: OrcamentoEmpresarial
    });

    const $ctrl = this;

    let columnDefs = [];

    $scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'previsaoCustos', 'e');

    $scope.setDefaultParams = function() {
        $scope.paginationOptions.sortField = 'previsaoCustos.descricao';
        $scope.paginationOptions.sortFieldString = false;
        $scope.paginationOptions.sortDir = 'asc';
    };

    $scope.setDefaultParams();

    $scope.getPage = function(page) {
        $scope.getPageGrid($scope.gridPesquisa, page);
    };

    /* FIM DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */

    $scope.$on('crud-success', function(event, action, data) {
        $rootScope.loadingProgress = false;
    });

    $scope.$watch('result', function() {
        $rootScope.loadingProgress = false;
    });

    $scope.exibir = function(codigo) {
        window.open($state.href('orcamento-empresarial.exibir', {
            objectId: codigo
        }, {
            absolute: true
        }));
    };

    $scope.editar = function(codigo) {
        window.open($state.href('orcamento-empresarial.editar', {
            objectId: codigo
        }, {
            absolute: true
        }));
    };

    $ctrl.novo = function() {
        $state.go('orcamento-empresarial.novo');
    };

    $ctrl.buscar = function() {
        $rootScope.loadingProgress = true;
        $scope.setDefaultParams();
        $scope.getPage(1);
    };

    $ctrl.reset = function() {
        $scope.result = null;
        $scope.clear();
    };

}