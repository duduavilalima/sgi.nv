(function() {
    'use strict';

    angular
        .module('app.movimentoautorizado')
        .controller('MovimentoAutorizadoListarCtrl', listController);

    function listController($scope, $controller, $state, EnumService, $mdDialog, $rootScope, UsuarioLogadoService, Unidade, CentroFinanceiro, Tributo, MovimentoAutorizado, AutocompleteService,
        Banco, DialogConfirm, uiGridConstants) {

        $controller('GridController', {
            $scope: $scope,
            Resource: MovimentoAutorizado
        });

        function init() {

            if (localStorage.getItem("movimentoAutorizadoPesquisa")) {
                $scope.object = JSON.parse(localStorage.getItem("movimentoAutorizadoPesquisa"));
                let timeDe = angular.copy($scope.object.pd);
                let timeAte = angular.copy($scope.object.pa);
                setTimeout(() => {
                    $scope.$apply(function() {
                        if (timeDe != null) {
                            $scope.object.pagamentoDe = new Date(timeDe);
                            $scope.object.pd = timeDe;
                        }

                        if (timeAte != null) {
                            $scope.object.pagamentoAte = new Date(timeAte);
                            $scope.object.pa = timeAte;
                        }

                        if ($scope.object.contaContabil) {
                            $scope.searchTextContaContabil = $scope.object.contaContabil.numero + ' - ' + $scope.object.contaContabil.descricao;
                        }
                        $scope.fnSelectedItemContaContabil($scope.object.contaContabil);
                    });
                    $rootScope.loadingProgress = true;
                    $scope.setDefaultParams();
                    $scope.getPage(1);
                }, 800);
            }

            $scope.modulo = $state.current.data.modulo;
            $scope.titulo = $state.current.data.titulo;
            $scope.subtitulo = "Pesquisar";

            $scope.usuario = UsuarioLogadoService.getUsuarioLogado();

            $scope.banco = {};
            $scope.action = false;

            $scope.object = {};
            $scope.object.empresa = null;

            buscarEmpresas();
            carregarTipoDocumento();
            carregarGerencias();
            carregarBancos();
            carregarTributos();
            verificarPerfilDiretoria();

            $scope.changeEmpresa = function() {
                if ($scope.object.empresa.codigo === null || $scope.object.empresa.codnome === '') {
                    $scope.object.contaContabil = null;
                    $scope.searchTextContaContabil = null;
                    $scope.selectedItemContaContabil = null;
                    document.querySelectorAll('input[type=search]')[0].value = '';
                }
            };
        }

        function buscarEmpresas() {
            let filter = {
                tipoUnidade: 'MATRIZ'
            };
            Unidade.page(null, filter)
                .$promise.then(function(data) {
                    $scope.empresaList = [];
                    $scope.empresaList.push({
                        codigo: null,
                        codnome: ''
                    });

                    //Adiciona a lista e ordena em ordem alfabetica.
                    $scope.empresaList.push(...data.content.sort((a,b) => {
                        if(a.codnome < b.codnome) { return -1; }
                        if(a.codnome > b.codnome) { return 1; }
                        return 0;
                    }));

                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'search', response);
                });
        }

        function carregarTipoDocumento() {
            EnumService.enumValues({
                    pacote: 'br.com.araujoabreu.sgi.administracao.enums',
                    classe: 'null',
                    enum: 'TipoDocumentoPagamentoPesquisa'
                })
                .$promise.then(function(data) {
                    $scope.listTipoDoc = [];
                    $scope.listTipoDoc.push("");

                    angular.forEach(data, function(value) {
                        $scope.listTipoDoc.push(value);
                    });
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'load', response);
                });
        }

        function carregarGerencias() {
            $scope.listGerencias = CentroFinanceiro.buscarTodasGerencias();
        }

        function carregarBancos() {
            Banco.todos().$promise.then(function(data) {
                $scope.bancoList = [];
                $scope.bancoList.push({
                    banco: {
                        nome: ''
                    }
                });

                angular.forEach(data, function(value, key) {
                    $scope.bancoList.push(value);
                });

            }).catch(function(response) {
                $scope.$emit('crud-critical', 'contrato load', response);
                $scope.bancoList = [];
                $scope.agenciaContaList = [];
            });
        }

        function carregarTributos() {

            let filtroTributo = {};

            filtroTributo.exibeNota = true;

            Tributo.page(null, filtroTributo)
                .$promise.then(function(data) {

                    $scope.listTributos = [];

                    angular.forEach(data.content, function(value, key) {
                        $scope.listTributos.push(value);
                    });

                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'contrato load', response);
                    $scope.tributosList = [];
                });
        }

        $scope.validaValorDeAte = function() {
            if (angular.isDefined($scope.object.valorDe) && $scope.object.valorDe != null && angular.isDefined($scope.object.valorAte) && $scope.object.valorAte != null) {
                $scope.object.valorDe = Number($scope.object.valorDe);
                $scope.object.valorAte = Number($scope.object.valorAte);

                if ($scope.object.valorDe > $scope.object.valorAte) {
                    return true;
                } else
                if ($scope.object.valorAte < $scope.object.valorDe) {
                    return true;
                }
                return false;
            }
        };

        /* CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */

        $scope.getTipoDocPagamento = function(tipoDocumentoPagamento) {
            if (tipoDocumentoPagamento == null) return "";

            switch (tipoDocumentoPagamento) {
                case "Depósito em conta/Transferência":
                    tipoDocumentoPagamento = 'Depósito/Transf.';
                    break;
            }

            return tipoDocumentoPagamento;
        };
        let cellTemplateTipoDocPag = '<span>{{grid.appScope.getTipoDocPagamento(row.entity.tipoDocumentoPagamento)}}</span>';

        $scope.getAjustarColunaGerencia = function(entity) {

            if (entity.tipo != 'NF' && entity.tipo != 'NP') {
                return "";
            }

            return entity.gerencia.nome;
        };
        let cellTemplateTipoColunaGerencia = '<div style="height: 16px; width: 98%; overflow: hidden;">{{ grid.appScope.getAjustarColunaGerencia(row.entity) }}</div>';

        $scope.ajustarColunaCredor = function(entity) {

            if (entity.tipo === 'TA' || entity.tipo === 'TR') {
                if (entity.tributoExibicao) {
                    return entity.tributoExibicao.nome;
                }
                return "";
            }

            return entity.credor.nome;
        };
        let cellTemplateCredor = '<span>{{ grid.appScope.ajustarColunaCredor(row.entity) }}</span>';

        // INICIO DO AUTOCOMPLETE DE CONTA CONTÁBIL
        function autoCompleteContaContabil() {

            $scope.searchTextContaContabil = null;
            $scope.selectedItemContaContabil = null;
            $scope.resultsContaContabil = [];

            $scope.fnSelectedItemContaContabil = function(item) {
                $scope.object.contaContabil = item;
            };

            $scope.querySearchContaContabil = function(query) {
                $rootScope.loadingProgress = true;

                let filtro = {};
                filtro.nome = query;
                filtro.unidade = $scope.object.empresa;

                let resultContaContabilPromise = AutocompleteService.getContaContabilPorEmpresa(filtro).$promise;

                let contaContabilArray = [];

                return resultContaContabilPromise.then(function(contaContabil) {

                    $rootScope.loadingProgress = false;

                    angular.forEach(contaContabil, function(contaContabil) {
                        contaContabilArray.push(contaContabil);
                    });
                    return contaContabilArray;
                });
            };
        } // FIM DO AUTOCOMPLETE DE CONTA CONTÁBIL

        autoCompleteContaContabil();

        let objIconEditar = {
            nome: 'editar.objeto',
            label: 'Editar',
            icone: 'icon-pencil',
            tooltip: 'Editar dados de tesouraria'
        };
        let objIconRetornar = {
            nome: 'retornar.objeto',
            label: 'Retornar para autorização',
            icone: 'icon-arrow-left',
            tooltip: 'Retornar o pagamento para autorização'
        };

        let acoesAdicionais = [];

        function verificarPerfilDiretoria() {

            UsuarioLogadoService.possuiUmPerfilLista('financeiro-diretor').$promise.then(function(data) {

                if (data.possuiUmPerfilLista) {
                    acoesAdicionais.push(objIconEditar);
                    acoesAdicionais.push(objIconRetornar);

                } else {
                    acoesAdicionais.push(objIconEditar);
                }

                $scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'movimentoautorizado', 'e');

                $scope.gridPesquisa.showColumnFooter = true;

            }).catch(function(response) {
                $rootScope.loadingProgress = false;
                $rootScope.$emit('crud-critical', 'load', response);
            });
        }

        let cellTemplateCheckBox = "<md-checkbox ng-model='row.entity.selecionar' aria-label='Selecionar'></md-checkbox>";

        let columnDefs = [{
            name: 'Número',
            field: 'numero',
            enableSorting: false,
            sortFieldString: false,
            width: '10%',
            pinnedLeft: false,
            displayName: 'Número'
        }, {
            name: 'Pagamento',
            field: 'dataPagamento',
            width: '6%',
            cellClass: 'text-center',
            cellFilter: 'formatacaoData'
        }, {
            name: 'Doc. pgto.',
            displayName: 'Documento para pagamento',
            field: 'tipo',
            width: '7%',
            cellClass: 'text-left',
            cellTemplate: cellTemplateTipoDocPag,
            headerTooltip: true,
            tooltip: ['field.tipoDocumentoPagamento']
        }, {
            name: 'Gerência',
            field: 'gerencia.nome',
            celalterarPagamentolClass: 'text-over',
            cellTemplate: cellTemplateTipoColunaGerencia,
            tooltip: ['field.gerencia.identificadorFinal', ' - ', 'field.gerencia.nome']
        }, {
            name: 'Banco',
            field: 'dadosBancarios.banco.nome',
            width: '10%',
            cellClass: 'text-over',
            detalheContas: ['credor.codigo', 'dadosBancarios.codigo'],
            tooltip: ['field.dadosBancarios.banco.nome']
        }, {
            name: 'Credor',
            field: 'credor.nome',
            width: '26%',
            cellClass: 'text-over',
            cellTemplate: cellTemplateCredor,
            detalhePessoa: ['credor.codigo', 'credor.tipoPessoa'],
            tooltip: ['field.credor.nome']

        }, {
            name: 'A pagar',
            displayName: 'A pagar',
            field: 'valorPagar',
            width: '10%',
            cellFilter: 'formatacaoMoeda',
            cellClass: 'text-right',
            detalhePagamento: ['codigo', 'tipo', 'codigoNota'],
            aggregationType: uiGridConstants.aggregationTypes.sum,
            aggregationHideLabel: true,
            footerCellClass: 'text-right',
            footerCellFilter: 'formatacaoMoeda',
        }, {
            name: 'Seleção',
            field: 'selecionar',
            enableSorting: false,
            sortFieldString: false,
            cellClass: 'text-center',
            width: '6%',
            pinnedLeft: false,
            cellTemplate: cellTemplateCheckBox
        }, {
            name: 'Opções',
            width: '5%',
            pinnedRight: false,
            enableSorting: false,
            cellClass: 'text-center',
            acoesAdicionais: acoesAdicionais
        }];

        $scope.setDefaultParams = function() {
            $scope.paginationOptions.sortField = 'parcelaNota.codigo';
            $scope.paginationOptions.sortFieldString = false;
            $scope.paginationOptions.sortDir = 'asc';
        };
        $scope.setDefaultParams();

        $scope.getPage = function(page) {
            $scope.getPageGrid($scope.gridPesquisa, page);
        };

        $scope.retornar = function(item) {
            DialogConfirm.show(
                null,
                item,
                'Retornar pagamento para autorização: Confirmação',
                'Deseja realmente <b>retornar o pagamento para a autorização</b> e cancelar a autorização atual?',
                'Confirmar',
                'Cancelar',
                confirmRetorno,
                cancelRetorno);
        };

        function confirmRetorno(object, fnHide) {
            $scope.action = true;
            MovimentoAutorizado.cancelar(object.codigo).$promise.then(function(data) {
                let index = null;
                angular.forEach($scope.result, function(value, key) {
                    if (value.codigo === object.codigo) {
                        index = $scope.result.indexOf(value);
                        return;
                    }
                });

                if (index != null) {
                    $scope.result.splice(index, 1);
                    index = null;
                }

                fnHide();

                $scope.notification.success("Pagamento (" + object.numero + ") retornado para autorização.");
                $scope.action = false;
            }).catch(function(response) {
                $scope.action = false;
                $scope.$emit('crud-critical', 'dialog', response);
            });
        }

        function cancelRetorno(fnHide) {
            fnHide();
        }

        /*Dialog de alterar pagamento*/
        $scope.alterarPagamento = function(ev) {
            let pagamentosSelecionados = [];
            $scope.result.forEach((value) => {
                if (value.selecionar) {
                    pagamentosSelecionados.push(value);
                }
            });
            if (pagamentosSelecionados.length > 0) {
                $mdDialog.show({
                    controller: DialogPagamentosController,
                    templateUrl: 'partials/tesouraria/movimento_autorizado/html/dialogs/dialog_alterar_pagamentos.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    locals: {
                        pagamentos: pagamentosSelecionados,
                        fnAtualizarPagamento: fnAtualizarPagamento
                    },
                    clickOutsideToClose: true
                });
            } else {
                $scope.notification.atencao(null, "É necessário selecionar, pelo menos, um pagamento para alteração.");
            }
        };

        let dialogPagamentoSelecionadoFnHide = null;

        let fnAtualizarPagamento = (tipoDoc, dataPagamento, fnHide) => {
            let p1 = new Promise(
                function(resolve, reject) {
                    $scope.result.forEach((pg) => {
                        if (pg.selecionar) {
                            MovimentoAutorizado.atualizar({
                                    id: pg.codigo,
                                    tipoDocumentoPagamento: tipoDoc,
                                    dataPagamento: dataPagamento != null && dataPagamento != '' ? dataPagamento.getTime() : null
                                })
                                .$promise.then((pagamento) => {
                                    pg = pagamento;
                                    pg.selecionar = true;
                                });
                        }
                    });
                    resolve(true);
                });

            p1.then((data) => {
                $rootScope.loadingProgress = true;
                dialogPagamentoSelecionadoFnHide = fnHide;
                $scope.setDefaultParams();
                $scope.getPage(1);
                $('html, md-content, #titulo_resultado').animate({
                    scrollTop: 0
                }, 3000);
            });
        };

        function DialogPagamentosController($scope, $mdDialog, pagamentos, fnAtualizarPagamento) {
            $scope.listTipoDoc = [];
            $scope.object = {};
            $scope.object.quantidadesPagamentos = pagamentos.length;
            $scope.object.tipoDocumentoPagamento = null;
            $scope.object.dataPagamento = null;
            $scope.action = false;
            $scope.dataPreenchida = false;

            $scope.onChangeDate = function(data) {
                if (data === null || data === '') {
                    $scope.dataPreenchida = false;
                } else {
                    $scope.dataPreenchida = true;
                }
            };

            EnumService.enumValues({
                    pacote: 'br.com.araujoabreu.sgi.contasApagar.domain',
                    classe: 'Pagamento',
                    enum: 'TipoDocumentoPagamento'
                })
                .$promise.then(function(data) {
                    $scope.listTipoDoc = [];
                    angular.forEach(data, function(value) {
                        $scope.listTipoDoc.push(value);
                    });
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'load', response);
                });

            $scope.confirm = function() {
                $scope.action = true;
                fnAtualizarPagamento($scope.object.tipoDocumentoPagamento, $scope.object.dataPagamento, $scope.hide);
            };

            $scope.hide = function() {
                $mdDialog.hide();
            };

            $scope.cancel = function() {
                $mdDialog.cancel();
            };

            $scope.answer = function(answer) {
                $mdDialog.hide(answer);
            };
        }

        /******************************/

        $scope.buscar = function() {
            $rootScope.loadingProgress = true;
            $scope.setDefaultParams();
            $scope.getPage(1);
            if ($scope.object.pagamentoDe instanceof Date) {
                $scope.object.pagamentoDe = $scope.object.pagamentoDe.getTime();
                $scope.object.pd = $scope.object.pagamentoDe;
            } else if (!Number.isInteger($scope.object.pagamentoDe)) {
                $scope.object.pagamentoDe = null;
                $scope.object.pd = null;
            }
            if ($scope.object.pagamentoAte instanceof Date) {
                $scope.object.pagamentoAte = $scope.object.pagamentoAte.getTime();
                $scope.object.pa = $scope.object.pagamentoAte;
            } else if (!Number.isInteger($scope.object.pagamentoAte)) {
                $scope.object.pagamentoAte = null;
                $scope.object.pa = null;
            }
            localStorage.setItem("movimentoAutorizadoPesquisa", JSON.stringify($scope.object));
        };

        $scope.$on('crud-success', function(event, action, data) {
            $rootScope.loadingProgress = false;
        });

        $scope.$watch('result', function() {
            $rootScope.loadingProgress = false;
            if (angular.isFunction(dialogPagamentoSelecionadoFnHide)) {
                dialogPagamentoSelecionadoFnHide();
            }
        });

        $scope.listar = function() {
            $state.go('movimentoautorizado.list');
        };

        $scope.exibir = function(codigo) {
            $state.go('movimentoautorizado.view', {
                objectId: codigo
            });
        };

        $scope.editar = function(item) {
            $state.go('movimentoautorizado.edit', {
                objectId: item.codigo
            });
        };

        $scope.novo = function() {
            $state.go('movimentoautorizado.new', {});
        };

        $scope.reset = function() {
            $scope.result = null;
            $scope.object = {};
            localStorage.removeItem("movimentoAutorizadoPesquisa");
            $scope.clear();
            delete $scope.tributoPesquisa;
            document.querySelectorAll('input[type=search]')[0].value = '';
        };

        $scope.desabilitarBtnAltPag = () => {
            return !$scope.result.some( item => { return item.selecionar; } );
        };

        init();

    }

})();
