(function() {
    'use strict';

    angular
        .module('app.relacaobancaria')
        .controller('RelacaoBancariaListarCtrl', listController);

    function listController($scope, $controller, $state, $mdDialog, $rootScope, EnumService, DateTimeService, Unidade, ContaCorrente, CalculadorFaturamento, RelacaoBancaria, Tributo, uiGridConstants) {
        $controller('GridController', {
            $scope: $scope,
            Resource: RelacaoBancaria
        });

        let init = () => {
            $rootScope.loadingProgress = true;

            $scope.modulo = $state.current.data.modulo;
            $scope.titulo = $state.current.data.titulo;
            $scope.subtitulo = 'Pesquisar';
            $scope.abaDefault = 0;
            $scope.object = {};

            $scope.object.numero = null;
            $scope.object.status = null;
            $scope.object.unidade = null;
            $scope.object.banco = null;
            $scope.object.contaCorrente = null;

            $scope.exibeTotais = true;
            $scope.object.totalPago = 0;
            $scope.object.totalItens = 0;
            $scope.object.valorTotal = 0;

            $scope.listSituacaoes = ["Aberta", "A processar (internet banking)", "Em processamento (internet banking)", "Aguardando retorno", "Processando retorno (internet banking)",
                "Finalizada", "Cancelada"
            ]; //MUDAR, PEGAR O ENUM

            carregarTributos();

            Promise
                .resolve()
                .then(() => Unidade.page(null, {
                    tipoUnidade: "Matriz"
                }).$promise)
                .then(buscarEmpresas)
                .then(() => EnumService.enumValues({
                    pacote: 'br.com.araujoabreu.sgi.tesouraria.domain',
                    classe: 'RelacaoBancaria',
                    enum: 'MeioPagamento'
                }).$promise)
                .then(enumValuesResolve)
                .then(() => {
                    $rootScope.loadingProgress = false;
                })
                .catch(catchError);
        };

        var catchError = (response) => {
            $rootScope.loadingProgress = false;
            $scope.action = false;
            $scope.changeEmpresaAction = false;
            $scope.changeBancoAction = false;
            $scope.$emit('crud-critical', 'tributo load', response);
        };

        var enumValuesResolve = (data) => {
            $scope.listMeiosPgto = [];
            angular.forEach(data, function(value) {
                if ("Ordem de crédito" !== value) {
                    $scope.listMeiosPgto.push(value);
                }
            });
        };

        var buscarEmpresas = function(data) {
            $scope.empresaList = [];
            angular.forEach(data.content, function(value) {
                $scope.empresaList.push(value);
            });
        };

        $scope.onTabChanges = function(currentTabIndex) {
            localStorage.setItem('active', currentTabIndex);
        };

        /* CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */
        $scope.getAgenciaConta = function(contaCorrente) {
            return contaCorrente.agencia + " / " + contaCorrente.conta + "-" + contaCorrente.digito;
        };
        var cellTemplateAgenciaConta = '<span>{{grid.appScope.getAgenciaConta(row.entity.contaCorrente)}}</span>';

        $scope.get2aVia = function(impressao2via) {
            return impressao2via ? 'X' : '';
        };
        var cellTemplate2aVia = '<span>{{grid.appScope.get2aVia(row.entity.impressao2via)}}</span>';

        $scope.getValorChq = function(impressaoCheque) {
            return impressaoCheque ? 'X' : '';
        };
        var cellTemplateChq = '<span>{{grid.appScope.getValorChq(row.entity.impressaoCheque)}}</span>';

        $scope.getImpressaoEndosso = function(impressaoEndosso) {
            return impressaoEndosso ? 'X' : '';
        };
        var cellTemplateImpressaoEndosso = '<span>{{grid.appScope.getImpressaoEndosso(row.entity.impressaoEndosso)}}</span>';

        $scope.ajustarDataPagamento = (dataPagamento) => {
            let resultado = DateTimeService.ajustarData(dataPagamento);
            return resultado;
        };

        var cellTemplatePagamento = '<span>{{ grid.appScope.ajustarDataPagamento(row.entity.dataPagamento) | formatacaoData }}</span>';

        var acoesAdicionais = [{
                nome: 'exibir',
                label: 'Exibir',
                icone: 'icon-eye',
                tooltip: 'Exibir os dados desta relação bancária'
            },
            {
                nome: 'editar.objeto',
                label: 'Editar',
                icone: 'icon-pencil',
                tooltip: 'Editar os dados desta relação bancária'
            },
            {
                nome: 'remover.objeto',
                label: 'Remover',
                icone: 'icon-close',
                tooltip: 'Remover esta relação bancária (esta operação não poderá ser desfeita)'
            }
        ];

        $scope.exibirDetalheTotal = function(rb, ev) {
            var useFullScreen = true;

            $mdDialog.show({
                controller: DialogControllerTotal,
                locals: {
                    rb: rb
                },
                templateUrl: 'partials/tesouraria/relacao_bancaria/html/partials/dialog_detalhe_total.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true,
                fullscreen: useFullScreen,
                arialabel: 'Detalhe total'
            });
        };

        function DialogControllerTotal($scope, $mdDialog, rb) {
            $scope.codigoRB = rb.codigo;

            $scope.listaValores = [{
                    label: 'A pagar',
                    valor: rb.totalRestantes,
                    numpags: rb.totalPgtosRestantes
                },
                {
                    label: 'Pago',
                    valor: rb.totalPago,
                    numpags: rb.totalPgtosPagos
                },
                {
                    label: 'Inválido/Estornado',
                    valor: rb.totalEstornados,
                    numpags: rb.totalPgtosEstornados
                }
            ];

            $scope.fechar = function() {
                $mdDialog.cancel();
            };
        }

        var columnDefs = [{
            name: 'Número',
            field: 'numero',
            enableSorting: false,
            sortFieldString: false,
            width: '10%',
            pinnedLeft: false,
            funcao: ['exibir', 'row.entity.codigo']
        }, {
            name: 'Situação',
            field: 'status',
            tooltip: ['field'],
            cellClass: 'text-center text-truncate',
            width: '10%'
        }, {
            name: 'Pagamento',
            field: 'dataPagamento',
            cellTemplate: cellTemplatePagamento,
            width: '10%',
            displayName: 'Data de pagamento',
            cellClass: 'text-center',
            headerTooltip: true
        }, {
            name: 'Meio pagto.',
            field: 'meioPagamento',
            displayName: 'Meio de pagamento',
            cellClass: 'text-center text-truncate',
            tooltip: ['field'],
            width: '10%',
            headerTooltip: true
        }, {
            name: 'Banco',
            field: 'contaCorrente.banco.nome',
            //                width: '10%'
        }, {
            name: 'Agência/Conta',
            field: 'contaCorrente',
            cellTemplate: cellTemplateAgenciaConta,
            width: '13%',
            aggregationHideLabel: true,
            cellClass: 'text-center text-truncate',
            footerCellClass: 'text-right',
            footerCellTemplate: '<div class="ui-grid-cell-contents">Valor total: </div>',
            footerCellFilter: 'formatacaoMoeda',
            pinnedLeft: false
        }, {
            name: 'Total',
            displayName: 'Total',
            field: 'totalItens',
            width: '10%',
            cellFilter: 'formatacaoMoeda',
            cellClass: 'text-right',
            aggregationType: uiGridConstants.aggregationTypes.sum,
            aggregationHideLabel: true,
            footerCellClass: 'text-right',
            footerCellFilter: 'formatacaoMoeda',
            detalhePopup: ['exibirDetalheTotal(row.entity, $event)', '']
        }, {
            name: 'Nº pagtos.',
            displayName: 'Número de pagamentos',
            field: 'totalPgtosItens',
            width: '5%',
            aggregationType: uiGridConstants.aggregationTypes.sum,
            aggregationHideLabel: true,
            footerCellClass: 'text-right',
            cellClass: 'text-right',
            headerTooltip: true
        }, {
            name: 'Chq',
            //                displayName: 'Chq',
            field: 'meioPagamento',
            cellTemplate: cellTemplateChq,
            displayName: 'Indica se os cheques foram impressos',
            width: '5%',
            cellClass: 'text-center',
            headerTooltip: true

        }, {
            name: 'End',
            //                displayName: 'End',
            field: 'impressaoEndosso',
            cellTemplate: cellTemplateImpressaoEndosso,
            displayName: 'Indica se o endosso (verso) dos cheques foram impressos',
            width: '5%',
            cellClass: 'text-center',
            headerTooltip: true

        }, {
            name: '2ª v',
            displayName: 'Indica se a 2ª via dos cheques foram impressas',
            field: '2ª v',
            cellTemplate: cellTemplate2aVia,
            width: '5%',
            cellClass: 'text-center',
            headerTooltip: true

        }, {
            name: 'Opções',
            width: '6%',
            pinnedRight: false,
            enableSorting: false,
            acoesAdicionais: acoesAdicionais
        }];

        $scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'relação bancária', 'a');

        $scope.gridPesquisa.showColumnFooter = false;
        $scope.gridPesquisa.showGridFooter = true;
        $scope.gridPesquisa.gridFooterTemplate =
            "<div></br> <div style='border: medium double; display: table; padding: 10px; margin-left: 15px;'><b> Valor total: {{grid.appScope.object.total | currency: '': 2}} ({{grid.appScope.object.totalItens}} pagamentos) </br> Valor pago: {{grid.appScope.object.totalPago | currency: '': 2}} ({{grid.appScope.object.totalPgtosPagos}} pagamentos) </b></div></div>";

        $scope.setDefaultParams = function() {
            $scope.paginationOptions.sortField = 'relacaoBancaria.numero';
            $scope.paginationOptions.sortFieldString = false;
            $scope.paginationOptions.sortDir = 'asc';
        };
        $scope.setDefaultParams();

        $scope.getPage = function(page) {
            $scope.getPageGrid($scope.gridPesquisa, page);
        };

        const totaisRBsListadas = function totaisRBsListadas() {
            $scope.object.total = 0;
            $scope.object.totalPago = 0;
            $scope.object.totalItens = 0;
            $scope.object.totalPgtosPagos = 0;
            let itens = [];

            let totalItens = 0;
            let total = 0;
            let totalPago = 0;
            let totalPgtosPagos = 0;

            angular.forEach($scope.gridPesquisa.data, function(value) {
                itens.push(value.itemRelacao);
                total = total + value.totalItens;
                totalItens = totalItens + value.totalPgtosItens;
                totalPgtosPagos = totalPgtosPagos + value.totalPgtosPagos;
            });

            if (itens != null && itens) {
                angular.forEach(itens, function(value) {
                    totalPago = totalPago + value.totalPago;
                });
            }

            $scope.object.totalItens = totalItens;
            $scope.object.total = total;
            $scope.object.totalPago = totalPago;
            $scope.object.totalPgtosPagos = totalPgtosPagos;
        };

        $scope.buscar = function() {
            $scope.exibeTotais = true;
            $rootScope.loadingProgress = true;
            $scope.setDefaultParams();
            $scope.getPage(1);
        };

        $scope.$on('crud-success', function(event, action, data) {
            $rootScope.loadingProgress = false;
        });

        $scope.$watch('result', function() {
            totaisRBsListadas();
            $rootScope.loadingProgress = false;
        });

        $scope.listar = function() {
            $state.go('relacaobancaria.list');
        };

        $scope.exibir = function(codigo) {
            const url = $state.href('relacaobancaria.view', {
                objectId: codigo
            });
            window.open(url, '_blank');
        };

        $scope.editar = function(item) {
            const url = $state.href('relacaobancaria.edit', {
                objectId: item.codigo
            });
            window.open(url, '_blank');
        };

        $scope.novo = function() {
            $state.go('relacaobancaria.new', {});
        };

        $scope.reset = function() {
            $scope.result = null;
            $scope.bancoList = [];
            $scope.agenciaContaList = [];

            $scope.clear();
            cleared();
            $scope.exibeTotais = false;
        };

        function cleared() {
            $scope.abaDefault = 0;
        }

        $scope.exibirAcao = function(acao, row) {
            if (acao === 'editar') {
                if (row.entity.status === 'Aberta') {
                    return true;
                }
            }
            if (acao === 'remover') {
                if (row.entity.status === 'Aberta') {
                    return true;
                }
            }
            if (acao === 'exibir') {
                return true;
            }
            return false;
        };

        /* FIM DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */

        $scope.$on('changeEmpresa', function() {
            delete $scope.object.banco;
            delete $scope.object.meioPagamento;
            delete $scope.object.chequeAnterior;
            $scope.object.contaCorrente = null;
            $scope.listAgencias = [];
            $scope.agenciaContaList = [];
            $scope.bancoList = [];
            $scope.changeEmpresaAction = true;

            if ($scope.object.unidade != null && $scope.object.unidade.codigo === null) {
                delete $scope.object.unidade;
                $scope.object.bancoPesquisa = null;
                $scope.object.contaCorrente.banco = null;
            } else {
                initCampoBanco($scope.object);
            }
        });

        function initCampoBanco(object) {
            $scope.bancoList = [];
            $scope.object.bancoPesquisa = null;
            $scope.agenciaContaList = [];

            if (object.unidade && object.unidade !== null && object.unidade.codigo !== null) {
                var obterContasThen = (data) => {
                    angular.forEach(data, function(value) {
                        if (!verificarSeJaExisteBanco(value.banco.codigo, $scope.bancoList)) {
                            $scope.bancoList.push(value.banco);
                        }
                    });
                };
                return Promise
                    .resolve()
                    .then(() => ContaCorrente.obterContas(null, {
                        pessoa: {
                            codigo: object.unidade.codigo
                        }
                    }).$promise)
                    .then(obterContasThen)
                    .then(() => {
                        $scope.changeEmpresaAction = false;
                        $scope.$apply();
                    })
                    .catch(catchError);
            } else {
                return Promise.resolve();
            }
        }

        $scope.$on('changeBanco', function() {
            if (angular.isDefined($scope) && angular.isDefined($scope.object.bancoPesquisa) && angular.isDefined($scope.object.bancoPesquisa.codigo)) {
                $scope.object.contaCorrente = {};
                $scope.object.contaCorrente.banco = $scope.object.bancoPesquisa;
                carregarContasCorrentes($scope.object.contaCorrente.banco.codigo, $scope.object.unidade.codigo);
            } else {
                delete $scope.contaCorrente;
                $scope.agenciaContaList = [];
                $scope.object.listMeiosPgto = [];
            }
        });

        function carregarContasCorrentes(codigoBanco, codigoPessoa) {
            $scope.changeBancoAction = true;
            let obterContas = () => ContaCorrente.obterContas(null, {
                banco: {
                    codigo: codigoBanco
                },
                pessoa: {
                    codigo: codigoPessoa
                }
            }).$promise;
            let obterContas_result = (data) => {
                $scope.agenciaContaList = [];
                angular.forEach(data, function(value, key) {
                    $scope.agenciaContaList.push(value);
                });
            };
            return Promise
                .resolve()
                .then(obterContas)
                .then(obterContas_result)
                .then(() => {
                    $scope.object.listMeiosPgto = [];
                })
                .then(() => {
                    $scope.changeBancoAction = false;
                    $scope.$apply();
                })
                .catch(catchError);
        }

        function verificarSeJaExisteBanco(codigo, list) {
            var existe = false;
            angular.forEach(list, function(value, key) {
                if (value.codigo === codigo) {
                    existe = true;
                    return;
                }
            });
            return existe;
        }

        $scope.montarLabelAgenciaConta = function(contaCorrente) {
            var conteudo = CalculadorFaturamento.montarCampoAgenciaConta(contaCorrente);
            if (CalculadorFaturamento.isCampoVazio(conteudo) || conteudo.trim() === '/')
                return ' ';
            else
                return conteudo;
        };

        function carregarTributos() {

            var filtroTributo = {};

            filtroTributo.exibeNota = true;

            Tributo.page(null, filtroTributo)
                .$promise.then(function(data) {

                    $scope.listTributos = [];

                    angular.forEach(data.content, function(value, key) {
                        $scope.listTributos.push(value);
                    });

                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'tributo load', response);
                    $scope.tributosList = [];
                });
        }

        $scope.$on('crud-success', function(event, action, data) {
            $rootScope.loadingProgress = false;
        });

        $scope.$watch('result', function() {
            $rootScope.loadingProgress = false;
        });

        $scope.remover = function(item) {
            $scope.showConfirm(item);
        };

        $scope.showConfirm = function(objRelacao) {
            $mdDialog.show({
                controller: DialogControllerRemoverRelacaoBancaria,
                locals: {
                    relacaobancaria: objRelacao,
                    resource: RelacaoBancaria,
                    lista: $scope.result,
                    notification: $scope.notification,
                    loadingProgress: $rootScope.loadingProgress
                },
                templateUrl: 'partials/tesouraria/relacao_bancaria/html/partials/dialog_remocao_relacao_bancaria.html',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        function DialogControllerRemoverRelacaoBancaria($scope, $timeout, $mdDialog, $state, relacaobancaria, resource, lista, notification, loadingProgress) {
            $scope.object = relacaobancaria;
            $scope.action = false;
            $scope.resource = resource;
            $scope.listaResultado = lista;

            $scope.confirm = function() {

                var relacaoBancariaRemocao = $scope.resource.delete({
                    id: $scope.object.codigo
                }).$promise;

                relacaoBancariaRemocao.then(function(data) {

                    var title = 'Remover Relação bancária';
                    var message = 'Relação bancária ' + $scope.object.numero + ' removida com sucesso';
                    notification.success(title, message);

                    var index = null;
                    angular.forEach($scope.listaResultado, function(value, key) {
                        if (value.codigo === $scope.object.codigo) {
                            index = $scope.listaResultado.indexOf(value);
                        }
                    });

                    if (index != null) {
                        $scope.listaResultado.splice(index, 1);
                        index = null;
                    }

                    delete $scope.object;
                    $scope.cancel();
                    loadingProgress = false;
                    $scope.action = false;

                }).catch(function(response) {
                    $scope.action = false;
                    console.error('crud-critical', 'dialog', response);
                    notification.criticalError(response.status, response.statusText, response.data);
                });

            };

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
        }

        init();
    }
})();