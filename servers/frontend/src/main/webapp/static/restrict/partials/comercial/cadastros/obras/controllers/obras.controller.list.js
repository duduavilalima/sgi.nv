angular
    .module('app.obras')

    .controller('ObrasListarCtrl', listController);

function listController($scope, $state, $rootScope, $controller, $mdDialog, EnumService, Unidade, ContaCorrente, ContaContabil, Lancamentos, Obras, Endereco, DialogConfirm, AutocompleteService, Cidade) {
    const $ctrl = this;
    
    $controller('GridController', {
        $scope: $scope,
        Resource: Obras
    });
    const init = () => {
        $ctrl.modulo = $state.current.data.modulo;
        $ctrl.titulo = $state.current.data.titulo;
        $ctrl.subtitulo = "Pesquisar";

        $ctrl.object = {};
        $ctrl.object.nome = null;
        $ctrl.object.situacao = null;
        $ctrl.object.inicioVigencia = null;
        $ctrl.object.fimVigencia = null;
        $ctrl.object.cidade = null;
        $ctrl.object.uf = null;
        $ctrl.object.tipo = null;
        $ctrl.object.setor = null;
        $ctrl.object.clientePrincipal = null;
        $ctrl.object.tipoObra = null;
        $ctrl.ufCidades = [];
        $ctrl.listSituacao = [];
        $ctrl.listSetores = [];
        $ctrl.listTipos = [];

        initCampoUF();
        initCampoSituacao();
        initCampoTipo();
        initCampoSetor();

        initOnChanges();

        // console.log($ctrl);
    };

    const initCampoUF = () => {
        $ctrl.listEstados = ['', 'AC', 'AL', 'AM', 'AP', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MG', 'MS', 'MT', 'PA', 'PB', 'PE', 'PI', 'PR', 'RJ', 'RN', 'RO', 'RR', 'RS', 'SC', 'SE', 'SP', 'TO'];
    };

    const initCampoSituacao = () => {
        var pStatusObra = EnumService.enumValues({
            pacote: 'br.com.araujoabreu.sgi.enumerators',
            classe: 'null',
            enum: 'Situacao'
        }).$promise;

        pStatusObra.then((data) => {
            $ctrl.listSituacao.push('');
            angular.forEach(data, (value, key) => {
                $ctrl.listSituacao.push(value);
            });

        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };


    const initCampoSetor = () => {

        let pSetores = EnumService.enumValues({
            pacote: 'br.com.araujoabreu.sgi.enumerators',
            classe: 'null',
            enum: 'Setor'
        }).$promise;

        pSetores.then((data) => {
            $ctrl.listSetores.push('');
            angular.forEach(data, (value, key) => {
                $ctrl.listSetores.push(value);
            });

        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    //TIPOOBRAS
    const initCampoTipo = () => {
        Obras.buscarTipoObra().$promise.then((data) => {

            $ctrl.listTipos.push('');
            data.sort((a, b) => {
                return a.nome - b.nome
            });
            angular.forEach(data, function(value) {
                $ctrl.listTipos.push(value);
            });
        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
            $ctrl.listTipos = [];
        });
    };
    //FIM TIPOOBRAS

    //AUTOCOMPLETE CIDADE
    $scope.querySearchCidade = (query) => {
        let nomeCidade = query;
        let listaCidade = [];

        let pCidade = Cidade.buscarListaPorNome(nomeCidade).$promise;

        return pCidade.then((data) => {
            angular.forEach(data, (value, key) => {
                listaCidade.push(value);
            });
            return listaCidade;

        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
            listaCidade = [];
            return listaCidade;
        });
    };

    $scope.selectedItemCidadeChange = (item) => {
        $scope.object.cidade = item;
        $scope.object.uf = item.uf;
    };
    //FIM AUTOCOMPLETE

    /** ONCHANGES */
    const initOnChanges = () => {
        $ctrl.changeSituacao = () => {
            if ($ctrl.object.situacao === '') {
                delete $ctrl.object.situacao;
            }
        };

        $ctrl.changeTipoObra = () => {
            if ('' === $ctrl.object.tipoObra) {
                delete $ctrl.object.tipoObra;
            }
        };

        $ctrl.changeUf = () => {
            if ('' === $ctrl.object.uf) {
                delete $ctrl.object.uf;
            }
        };

        $ctrl.changeSetor = () => {
            if ('' === $ctrl.object.setor) {
                delete $ctrl.object.setor;
            }
        };

        $ctrl.changeSituacao = () => {
            if ('' === $ctrl.object.situacao) {
                delete $ctrl.object.situacao;
            }
        };
    };

    $scope.buscar = () => {
        $rootScope.loadingProgress = true;
        $scope.setDefaultParams();
        $scope.getPage(1);
    };

    $scope.$on('crud-success', (event, action, data) => {
        $rootScope.loadingProgress = false;
    });

    $scope.$on('changeCidade', () => {
        $ctrl.mudarCidadeUf();
    });

    $scope.$watch('result', () => {
        $rootScope.loadingProgress = false;
    });


    $scope.reset = () => {
        $scope.listSetores = [];
        $scope.ufCidades = [];


        $scope.result = undefined;
        $scope.clear();

        document.querySelectorAll('input[type=search]')[0].value = '';
    };


    /** INICIO DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA  */
    $ctrl.getAgenciaConta = (contaCorrente) => {
        return contaCorrente.agencia + " / " + contaCorrente.conta + "-" + contaCorrente.digito;
    };
    let cellTemplateAgenciaConta = '<span>{{grid.appScope.getAgenciaConta(row.entity.contaCorrente)}}</span>';

    $ctrl.getContaContabil = (contaContabil) => {
        return contaContabil.numero + " - " + contaContabil.descricao;
    };
    let cellTemplateContaContabil = '<span>{{grid.appScope.getContaContabil(row.entity.contaContabil)}}</span>';

    $ctrl.getStatusObra = (situacao) => {
        if (situacao === 'Inativa') {
            return "Inativa";
        }
        if (situacao === 'Ativa') {
            return "Ativa";
        }

    };
    let cellTemplateStatusObra = '<span>{{row.entity.situacao}}</span>';

    let acoesAdicionais = [{
            nome: 'editar.objeto',
            label: 'Editar',
            icone: 'icon-pencil',
            tooltip: 'Editar os dados desta obra'
        },
        {
            nome: 'remover.objeto',
            label: 'Remover',
            icone: 'icon-close',
            tooltip: 'Remover esta obra (esta operação não poderá ser desfeita)'
        }
    ];

    const columnDefs = [{
            name: 'Nome',
            field: 'nome',
            width: '29%',
            enableSorting: false,
            sortFieldString: false,
            pinnedLeft: false,
            funcao: ['exibir', 'row.entity.codigo']
        },
        {
            name: 'Cliente',
            field: 'cliente',
            width: '30%',
            cellClass: 'text-left'
        },
        {
            name: 'Setor',
            field: 'setor',
            width: '9%'
        },
        {
            name: 'Início vigência',
            field: 'inicioVigencia',
            width: '9%',
            cellFilter: 'formatacaoData',
            cellClass: 'text-center'
        },
        {
            name: 'Fim vigência',
            field: 'fimVigencia',
            width: '9%',
            cellFilter: 'formatacaoData',
            cellClass: 'text-center'
        },
        {
            name: 'Situação',
            cellTemplate: cellTemplateStatusObra,
            width: '7%',
            cellClass: 'text-left'
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

    $scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'obra', 'e');

    $scope.setDefaultParams = () => {
        $scope.paginationOptions.sortField = 'codigo';
        $scope.paginationOptions.sortFieldString = false;
        $scope.paginationOptions.sortDir = 'asc';
    };
    $scope.setDefaultParams();

    $scope.getPage = (page) => {
        $scope.getPageGrid($scope.gridPesquisa, page);
    };


    $ctrl.exibirAcao = (acao, row) => {
        if (acao === 'editar') {
            if (row.entity.status === 'Aberto') {
                return true;
            }
        }
        if (acao === 'remover') {
            if (row.entity.status === 'Aberto') {
                return true;
            }
        }
        if (acao === 'exibir') {
            return true;
        }
        return false;
    };

    $ctrl.novaObra = () => {
        $state.go('obras.new');
    };


    $scope.exibir = (codigo) => {
        $state.go('obras.view', {
            objectId: codigo
        });
    };

    $scope.editar = (item) => {
        $state.go('obras.edit', {
            objectId: item.codigo
        });
    };

    $scope.remover = (item) => {
        $scope.showConfirm(item);
    };

    $scope.showConfirm = (objObra) => {
        let tituloDialog = "Obra - Remover: Confirmação";
        let msgDialog = "Deseja realmente remover esta obra (esta operação não poderá ser desfeita)?";
        let tituloBotaoConfirmar = "CONFIRMAR";
        let tituloBotaoCancelar = "CANCELAR";

        const funcaoCancelar = (fnHide) => {
            fnHide();
        };

        const funcaoConfirmar = (object, fnHide) => {
            let pObras = Obras.delete({
                id: object.codigo
            }).$promise;

            pObras.then((data) => {

                let title = 'Remover Obra';
                let message = 'Obra' + object.numero + ' removida com sucesso.';
                $scope.notification.success(title, message);

                let index;
                angular.forEach($scope.result, (value, key) => {
                    if (value.codigo === object.codigo) {
                        index = $scope.result.indexOf(value);
                        return;
                    }
                });

                if (index != undefined) {
                    $scope.result.splice(index, 1);
                    index = undefined;
                }

                fnHide();

            }).catch((response) => {
                console.error('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
            });
        };

        DialogConfirm.show(undefined, objObra, tituloDialog, msgDialog, tituloBotaoConfirmar, tituloBotaoCancelar, funcaoConfirmar, funcaoCancelar);
    };

    init();

    /** FIM DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA  */

}