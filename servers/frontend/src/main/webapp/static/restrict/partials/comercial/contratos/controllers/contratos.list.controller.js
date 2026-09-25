angular
    .module('app.contratos')

    .controller('ContratosListarCtrl', listController);

function listController($scope, $state, $rootScope, $controller, EnumService, Unidade, Contrato, CentroFinanceiro, Diretoria, Gerencia) {
    $controller('GridController', {
        $scope: $scope,
        Resource: Contrato
    });

    const $ctrl = this;

    const init = () => {
        $ctrl.modulo = $state.current.data.modulo;
        $ctrl.titulo = $state.current.data.titulo;
        $ctrl.subtitulo = "Pesquisar";
        $ctrl.object = $scope.object;


        initCampoSituacao($scope.object);
        initCampoEmpresa($scope.object);
        initCampoEstagioComercial($scope.object);
        initCampoDiretoria($scope.object);

    };

    const initCampoSituacao = (object) => {
        object.status = undefined;
        $ctrl.listSituacoes = [];

        let pStatusContrato = EnumService.enumValues({
            pacote: 'br.com.araujoabreu.sgi.administracao.domain',
            classe: 'Contrato',
            enum: 'SituacaoContrato'
        }).$promise;

        pStatusContrato.then((data) => {
            $ctrl.listSituacoes.push('');
            angular.forEach(data, (value, key) => {
                $ctrl.listSituacoes.push(value);
            });
        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    const initCampoEstagioComercial = (object) => {
        object.estagioComercial = undefined;
        $ctrl.listComercial = [];

        let pContrato = EnumService.enumValues({
            pacote: 'br.com.araujoabreu.sgi.administracao.domain',
            classe: 'Contrato',
            enum: 'EstagioComercial'
        }).$promise;

        pContrato.then((data) => {
            $ctrl.listComercial.push({value : ''});
            data.forEach(item => {
                $ctrl.listComercial.push({value: item});
            });

        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    const initCampoDiretoria = (object) => {
        $ctrl.listDiretoria = [];
        
        let pDiretoria = Diretoria.buscarDiretoria(undefined, {}).$promise;
        
        pDiretoria.then((data) => {
            data.sort((a, b) => {
                if(a.descricao < b.descricao) { return -1; }
                if(a.descricao > b.descricao) { return 1; }
                return 0;
            });
            if (data.length == 1) {
                $ctrl.listDiretoria.push(...data);
                $scope.object.diretoria = $ctrl.listDiretoria[0];      
                $ctrl.carregarGerencia();       
            }else{
                $ctrl.listDiretoria.push({codigo: undefined, descricao: ''});
                $ctrl.listDiretoria.push(...data);
            }
            
        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };
        //FIXME COLOCAR EM SERVIÇO
    const initCampoEmpresa = () => {
        $ctrl.listEmpresas = [];

        let codigosUnidadesIgnoradas = [8];
        let pUnidade = Unidade.unidadesporempresaexceto(undefined, codigosUnidadesIgnoradas).$promise;

        pUnidade.then((data) => {
            $ctrl.listEmpresas.push({codigo: undefined, codnome: ''});
            angular.forEach(data, (value, key) => {
                // TODO: Refatorar
                if (value.emitirNfFaturamento) {
                    $ctrl.listEmpresas.push(value);
                }
            });
        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    /**
     * --------------------
     *  onCHANGES [INICIO]
     * --------------------
     */

    $ctrl.carregarGerencia = () => {
        $ctrl.listGerencias = [];

        if (!$scope.object.diretoria || !$scope.object.diretoria.codigo) {
            delete $scope.object.diretoria;
            delete $ctrl.listGerencias;
            delete $scope.object.gerencia;

        } else {
            let pGerencia = Gerencia.porPerfilUsuarioLogado($ctrl.object.diretoria).$promise;
    
            pGerencia.then((data) => {
                $ctrl.listGerencias.push({
                    codigo: undefined,
                    nome: ""
                });
                angular.forEach(data, (value, key) => {
                    $ctrl.listGerencias.push(value);
                });
            }).catch((response) => {
                $scope.$emit('crud-critical', 'load', response);
            });
        }
    };

    $ctrl.changeEstComercial = () => {
        if (!$scope.object.estagioComercial) {
            delete $scope.object.estagioComercial;
        }
    };

    $ctrl.changeEmpresa = () => {
        if (!$scope.object.empresa.codigo) {
            delete $scope.object.empresa;
        }
    };

    /**
     * --------------------
     *  onCHANGES [FIM]
     * --------------------
     */

    $ctrl.buscar = () => {
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

    // Botão Limpar
    $ctrl.reset = () => {
        $ctrl.listGerencias = [];
        $scope.result = undefined;
        $scope.object = {};
    };


    /** INICIO DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA  */

    let acoesAdicionais = [{
        nome: '$ctrl.exibir.objeto',
        label: 'Exibir',
        icone: 'icon-eye',
        tooltip: 'Exibir contrato'
    },
    {
        nome: '$ctrl.editar.objeto',
        label: 'Editar',
        icone: 'icon-pencil',
        tooltip: 'Editar contrato'
    }];
    // Serve para colocar na grid o ícone editar
    $ctrl.exibirAcao = (acao, row) => {
        if (acao === 'editar') {
            if ((row.entity.situacao === 'Ativo') || (row.entity.situacao === 'Em elaboração') || (row.entity.situacao === 'Em revisão')) {
                return true;
            }
        }
        return false;
    };

    let columnDefs = [{
        name: 'Número',
        field: 'numero',
        width: '10%',
        enableSorting: false,
        sortFieldString: false,
        pinnedLeft: false,
        funcao: ['$ctrl.exibir', 'row.entity']
    }, {
        name: 'Cliente',
        field: 'cliente.nome',
        cellClass: 'text-left text-truncate',
        tooltip: ['field'],
        width: '35%'
    }, {
        name: 'Situação',
        field: 'situacao',
        width: '10%',
        cellClass: 'text-left'
    }, {
        name: 'Estágio comercial',
        field: 'estagioComercial',
        cellClass: 'text-left text-truncate',
        tooltip: ['field'],
        width: '10%'
    }, {
        name: 'Assinatura',
        field: 'dataAssinatura',
        cellFilter: 'formatacaoData',
        width: '10%',
        cellClass: 'text-center'
    }, {
        name: 'Vigência',
        field: 'dataFimVigencia',
        width: '10%',
        cellFilter: 'formatacaoData',
        cellClass: 'text-center'
    }, {
        name: 'Valor total',
        field: 'valorTotal',
        width: '9%',
        cellFilter: 'formatacaoMoeda',
        cellClass: 'text-right'
    }, {
        name: 'Opções',
        width: '6%',
        pinnedRight: false,
        enableSorting: false,
        acoesAdicionais: acoesAdicionais
    }];

    $scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'contratos', 'e');

    $scope.setDefaultParams = () => {
        $scope.paginationOptions.sortField = 'codigo';
        $scope.paginationOptions.sortFieldString = false;
        $scope.paginationOptions.sortDir = 'asc';
    };
    $scope.setDefaultParams();

    $scope.getPage = (page) => {
        $scope.getPageGrid($scope.gridPesquisa, page);
    };

    $ctrl.exibir = (item) => {
        window.open($state.href('contratos.view', {objectId: item.codigo}, {absolute: true}));
    };

    $ctrl.editar = (item) => {
        $state.go('contratos.edit', {
            objectId: item.codigo
        });
    };

    $ctrl.remover = (item) => {
        $scope.showConfirm(item);
    };

    init();
    /** FIM DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA  */
}