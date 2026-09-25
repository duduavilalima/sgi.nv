angular
    .module('app.formacontratacao')

    .controller('formacontratacaoListarCtrl', listController);

function listController($scope, $state, $rootScope, $controller, FormaContratacao, EnumService) {
    const $ctrl = this;
    $controller('GridController', {
        $scope: $scope,
        Resource: FormaContratacao
    });

    const init=() => {
        $ctrl.modulo = $state.current.data.modulo;
        $ctrl.titulo = $state.current.data.titulo;
        $ctrl.subtitulo = "Pesquisar";

        initCampoTipo();
    };

    const initCampoTipo=() => {
        let pTiposContratacoes = EnumService.enumValues({
            pacote: 'br.com.araujoabreu.sgi.administracao.domain',
            classe: 'FormaContratacao',
            enum: 'TipoContratacao'
        }).$promise;

        pTiposContratacoes.then((data) => {
            $scope.listTipos = [];
            $scope.listTipos.push('');
            angular.forEach(data, (value) => {
                $scope.listTipos.push(value);
            });

        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    $scope.buscar = () => {
        $rootScope.loadingProgress = true;
        $scope.setDefaultParams();
        $scope.getPage(1);
    };

    $scope.$on('crud-success', (event, action, data) => {
        $rootScope.loadingProgress = false;
    });

    $scope.$watch('result', () => {
        $rootScope.loadingProgress = false;
    });

    $scope.reset = () => {
        $scope.result = undefined;
        $scope.clear();
    };

    $scope.setDefaultParams = () => {
        $scope.paginationOptions.sortField = 'codigo';
        $scope.paginationOptions.sortFieldString = false;
        $scope.paginationOptions.sortDir = 'asc';
    };

    $scope.setDefaultParams();

    $ctrl.changeTipo = () => {
        if ($ctrl.object.tipo.codigo === undefined) {
            $ctrl.object.tipo = undefined;
        } else {
            initCampoTipo($ctrl.object);
        }
    };

    /** INICIO DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA  */

    $ctrl.getTipo = (tipo) => {
        if (tipo === 'Cliente') {
            return "Cliente";
        }
        if (tipo === 'Cliente') {
            return "Cliente";
        }
        if (tipo === 'Fornecedor') {
            return "Fornecedor";
        }
        if (tipo === 'Cliente/Fornecedor') {
            return "Cliente/Fornecedor";
        }

    };

    let acoesAdicionais = [
        {
        nome: 'editar.objeto',
        label: 'Editar',
        icone: 'icon-pencil',
        tooltip: 'Editar os dados desta forma de contratação'
        }
    ];

    let columnDefs = [{
        name: 'Nome',
        field: 'nome',
        width: '30%',
        enableSorting: false,
        sortFieldString: false,
        pinnedLeft: false,
        funcao: ['exibir', 'row.entity.codigo']
    },
    {
        name: 'Descrição',
        field: 'descricao',
        width: '30%',
        cellClass: 'text-left'
    },
    {
        name: 'Tipo',
        field: 'tipo',
        width: '13%'
    },
    {
        name: 'Criador',
        field: 'criador',
        width: '20%'
    },
    {
        name: 'Opções',
        width: '7%',
        pinnedRight: false,
        enableSorting: false,
        acoes: ['exibir'],
        acoesAdicionais: acoesAdicionais
    }
    ];

    $scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'formacontratacao', 'e');

    $scope.getPage = (page) => {
        $scope.getPageGrid($scope.gridPesquisa, page);
    };

    $scope.exibir = (codigo) => {
        $state.go('formacontratacao.view', {
            objectId: codigo
        });
    };

    $scope.editar = (item) => {
        $state.go('formacontratacao.edit', {
            objectId: item.codigo
        });
    };

    init();

}