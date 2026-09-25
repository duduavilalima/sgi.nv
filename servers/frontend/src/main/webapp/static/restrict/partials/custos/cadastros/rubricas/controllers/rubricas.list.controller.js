angular.module('app.rubricas').controller('RubricasListarCtrl', listRubricasController);

function listRubricasController($scope, $controller, $state, Rubricas, $rootScope) {
    $controller('GridController', {
        $scope: $scope,
        Resource: Rubricas
    });

    const $ctrl = this;

    $ctrl.situacaoList = [{
            code: undefined,
            desc: ''
        },
        {
            code: 'ATIVA',
            desc: 'Ativa'
        },
        {
            code: 'INATIVA',
            desc: 'Inativa'
        }
    ];

    $ctrl.tipoList = [{
            code: undefined,
            desc: ''
        },
        {
            code: 'RECEBIMENTO',
            desc: 'Recebimento'
        },
        {
            code: 'CUSTO',
            desc: 'Custo'
        },
        {
            code: 'DESPESA_CENTRO_CUSTOS',
            desc: 'Despesa de centro de custos (não disponível para CL, somente gerências, diretorias e empresas)'
        },
        {
            code: 'RESULTADO',
            desc: 'Resultado (soma dos recebimentos e custos)'
        }
    ];

    $ctrl.operacaoList = [
		{
            code: undefined,
            desc: ''
        },
        {
            code: 'SOMA',
            desc: 'Soma'
        },
        {
            code: 'SUBTRACAO',
            desc: 'Subtração'
        }
    ];

    let columnDefs = [{
        name: 'Descrição',
        field: 'descricao',
        enableSorting: false,
        sortFieldString: false,
        cellClass: 'text-left',
        pinnedLeft: false,
        funcao: ['exibir', 'row.entity.codigo']
    }, {
        name: 'Pai',
		field: 'pai.descricao',
		tooltip: ['field'],
        cellClass: 'text-left',
        width: '20%'
    }, {
        name: 'Ordem',
        field: 'ordem',
        cellClass: 'text-center',
        width: '5%'
    }, {
        name: 'Situação',
        field: 'situacao',
        width: '10%',
        cellTemplate: "<span>{{ row.entity.situacao === 'INATIVA' ? 'Inativa' : 'Ativa' }}</span>",
        cellClass: 'text-center'
    }, {
        name: 'Tipo',
        field: 'tipo',
		cellClass: 'text-center',
        cellTemplate: "<span>{{row.entity.tipo === 'RECEBIMENTO' ? 'Recebimento' : row.entity.tipo === 'CUSTO' ? 'Custo' : row.entity.tipo === 'DESPESA_CENTRO_CUSTOS' ? 'Despesa de centro de custos' : row.entity.tipo === 'RESULTADO' ? 'Resultado (soma dos recebimentos e custos)' : 'Não informado'}}</span>",
        width: '15%'
    }, {
        name: 'Operação',
        displayName: 'Operação',
        cellTemplate: "<span>{{ row.entity.operacao === 'SOMA' ? 'Soma' : 'Subtração' }}</span>",
        cellClass: 'text-center',
        field: 'operacao',
        width: '8%'
    }, {
        name: 'Opções',
        acoes: ['exibir', 'editar'],
        cellClass: 'text-center',
        width: '8%',
        pinnedRight: false,
        enableSorting: false
    }];

    $scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'despesaCustos', 'e');

    $scope.setDefaultParams = function() {
        $scope.paginationOptions.sortField = 'despesaCustos.descricao';
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

    $scope.listar = function() {
        $state.go('rubricas.listar');
    };

    $scope.exibir = function(codigo) {
        $state.go('rubricas.exibir', {
            objectId: codigo
        });
        // window.open($state.href('rubricas.exibir', {objectId: codigo}, {absolute: true}));
    };

    $scope.editar = function(codigo) {
        $state.go('rubricas.editar', {
            objectId: codigo
        });
        // window.open($state.href('rubricas.editar', {objectId: codigo}, {absolute: true}));
    };

    $ctrl.novo = function() {
        $state.go('rubricas.novo');
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
