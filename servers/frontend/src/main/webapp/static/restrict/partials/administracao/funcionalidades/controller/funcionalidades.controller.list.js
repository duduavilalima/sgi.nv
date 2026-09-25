(function () {
    'use strict';

    angular
        .module('app.funcionalidades')

        .controller('FuncionalidadesListarCtrl', listController);

    function listController($scope, $state, $rootScope, $controller, $mdDialog, EnumService, Funcionalidades) {
        $controller('GridController', {
            $scope: $scope,
            Resource: Funcionalidades
        });

        const $crtl = this;

        const init = () => {
            $scope.modulo = $state.current.data.modulo;
            $scope.titulo = $state.current.data.titulo;
            $scope.subtitulo = "Pesquisar";

            $scope.object = {};
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
            $scope.mudarCidadeUf();
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
        $scope.getAgenciaConta = (contaCorrente) => {
            return `${contaCorrente.agencia} / ${contaCorrente.conta}-${contaCorrente.digito}`;
        };
        let cellTemplateAgenciaConta = '<span>{{grid.appScope.getAgenciaConta(row.entity.contaCorrente)}}</span>';

        $scope.getContaContabil = (contaContabil) => {
            return `${contaContabil.numero} - ${contaContabil.descricao}`;
        };
        let cellTemplateContaContabil = '<span>{{grid.appScope.getContaContabil(row.entity.contaContabil)}}</span>';

        $scope.getStatusObra = (situacao) => {
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

        let columnDefs = [{
            name: 'Nome',
            field: 'nome',
            width: '29%',
            enableSorting: false,
            sortFieldString: false,
            pinnedLeft: false
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
            enableSorting: false
        }
        ];

        $scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'obra', 'e');

        $scope.setDefaultParams = function () {
            $scope.paginationOptions.sortField = 'codigo';
            $scope.paginationOptions.sortFieldString = false;
            $scope.paginationOptions.sortDir = 'asc';
        };
        $scope.setDefaultParams();

        $scope.getPage = (page) => {
            $scope.getPageGrid($scope.gridPesquisa, page);
        };


        $scope.exibirAcao = (acao, row) => {
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

        $crtl.novaObra = () => {
            $state.go('obras.new');
        };


        $crtl.exibir = (codigo) => {
            $state.go('obras.view', {
                objectId: codigo
            });
        };

        $crtl.editar = (item) => {
            $state.go('obras.edit', {
                objectId: item.codigo
            });
        };

        $crtl.remover = (item) => {
            $scope.showConfirm(item);
        };

        $crtl.showConfirm = (objObra) => {
            let tituloDialog = "Obra - Remover: Confirmação";
            let msgDialog = "Deseja realmente remover esta obra (esta operação não poderá ser desfeita)?";
            let tituloBotaoConfirmar = "CONFIRMAR";
            let tituloBotaoCancelar = "CANCELAR";

            let funcaoCancelar = (fnHide) => {
                fnHide();
            };

            let funcaoConfirmar = (object, fnHide) => {
                let pObras = Obras.delete({
                    id: object.codigo
                }).$promise;

                pObras.then((data) => {

                    let title = 'Remover Obra';
                    let message = `Obra ${object.numero}removida com sucesso.`;
                    $scope.notification.success(title, message);

                    let index = undefined;
                    forEach($scope.result, (value, key) => {
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
})();    