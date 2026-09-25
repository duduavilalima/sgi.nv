(function() {
    'use strict';

    angular
        .module('app.relacaobancaria')
        .controller('RelacaoBancariaDetailCtrl', detailController);

    function detailController($scope, $state, $window, $stateParams, $mdDialog, $q, $rootScope, RelacaoBancaria, Unidade, ContaCorrente, Banco, CentroFinanceiro, CalculadorFaturamento,
        Tributo, DadosBancarios, PessoaJuridica, Parametro, Pagamento, Feriado, EnumService, BoletoService, DateTimeService, UsuarioLogadoService) {
        function init() {
            $scope.modulo = $state.current.data.modulo;
            $scope.titulo = $state.current.data.titulo;
            $scope.subtitulo = $state.current.data.subtitulo;

            $scope.action = false;

            $scope.object = {};
            $scope.empresa = {};
            $scope.object.banco = undefined;
            $scope.object.contaCorrente = undefined;
            $scope.object.situacaoNovo = "Aberta";
            $scope.object.qtdPagamentos = 0; //qtadade pagamentos, ao adicionar no popup

            $scope.object.impressao2via = 'Não';
            $scope.object.impressaoEndosso = 'Não';
            $scope.object.impressaoCheque = 'Não';
            $scope.listPgtoIndividual = [{
                value: true,
                desc: 'Sim'
            }, {
                value: false,
                desc: 'Não'
            }];

            $scope.exibeTotais = true;
            $scope.object.totalPago = 0;
            $scope.object.totalItens = 0;
            $scope.object.valorTotal = 0;

            $scope.listaFeriados = [];

            $scope.possuiPerfilOperadorTesouraria = false;

            $scope.changeSituacao = function() {
                if ($scope.object.status === '') {
                    delete $scope.object.status;
                }
            };

            $scope.changeBanco = function() {
                if ($scope.object.banco.codigo === undefined) {
                    $scope.object.banco = undefined;
                    $scope.object.contaCorrente = undefined;
                    $scope.agenciaContaList = [];
                    $scope.object.listMeiosPgto = [];
                } else {
                    initCampoAgencia($scope.object);
                }
            };

            $scope.changeAgencia = function() {
                if ($scope.object.contaCorrente.codigo === undefined) {
                    delete $scope.object.contaCorrente;
                    delete $scope.object.listMeiosPgto;
                }
            };
            $rootScope.loadingProgress = false;

            if ($scope.screenMode === 'NEW') {
                carregarTipoDocumento();
                carregarTributos();
                buscarEmpresas();
                buscarListaMeioPgto();
                newObj();
            } else {
                loadObj();
            }
        }

        $scope.listSituacaoes = ["Aberta", "A processar (internet banking)", "Em processamento (internet banking)", "Aguardando retorno", "Processando retorno (internet banking)", "Finalizada",
            "Cancelada"
        ];

        //FIXME: Refatorar, pois o parametro 'pagamentosVO' não está sendo utilizado.
        function verificarFaltaDadosBancariosEnvio($scope, pagamentosVO, opcoes, callback) {
            var pagamentoIncompleto = undefined;
            var opcoesPadrao = {
                notificandoFalta: true
            };
            opcoes = opcoes || opcoesPadrao;
            if (typeof opcoes === 'function') {
                callback = opcoes;
                opcoes = opcoesPadrao;
            }


            angular.forEach($scope.object.relacaoBancariaPagamentosVO, function(value, key) {
                value.classeCssErro = '';

                if (!!value.faltandoDadosBancariosEnvio) {
                    value.classeCssErro = 'linha-erro';

                    if (pagamentoIncompleto) {
                        pagamentoIncompleto = true;
                    } else {
                        pagamentoIncompleto = {
                            numeroLinha: key + 1,
                            pagamento: value
                        };
                    }
                }

                if (callback) callback(value, key);
            });

            if (opcoes.notificandoFalta) {
                if (pagamentoIncompleto === true) {
                    $scope.notification.error("", "Os favorecidos dos pagamentos destacados em vermelho não possuem dados bancários cadastrados.");
                } else if (pagamentoIncompleto) {
                    $scope.notification.error("", "O favorecido do pagamento destacado em vermelho na " + (pagamentoIncompleto.numeroLinha) + "ª linha não possui dado(s) bancário(s) cadastrado(s).");
                }
            }

            $scope.object.faltamDadosBancarios = !!pagamentoIncompleto;
        }

        var buscarListaMeioPgto = () => {
            return $q(function(resolve, reject) {
                EnumService.enumValues({
                        pacote: 'br.com.araujoabreu.sgi.tesouraria.domain',
                        classe: 'RelacaoBancaria',
                        enum: 'MeioPagamento'
                    })
                    .$promise.then(function(data) {
                        $scope.listMeiosPgto = [];
                        $scope.listMeiosPgtoSemInternet = [];
                        $scope.listMeiosPgtoInternet = [];

                        angular.forEach(data, function(value, key) {
                            if ("Ordem de crédito" !== value) {
                                $scope.listMeiosPgto.push(value);
                            }
                            $scope.listMeiosPgtoInternet.push(value);
                            //ao iterar a lista, se for o tipo abaixo, nao adiciona
                            if (value !== 'Internet banking' && "Ordem de crédito" !== value) {
                                $scope.listMeiosPgtoSemInternet.push(value);
                            }

                        });

                        let listasMeiosPgto = [];
                        listasMeiosPgto.push($scope.listMeiosPgto);
                        listasMeiosPgto.push($scope.listMeiosPgtoSemInternet);
                        listasMeiosPgto.push($scope.listMeiosPgtoInternet);

                        resolve(listasMeiosPgto);
                    }).catch(function(response) {
                        $scope.$emit('crud-critical', 'load', response);
                        reject(response);
                    });
            });
        };

        function buscarEmpresas() {
            var filter = {tipoUnidade: 'MATRIZ'};

            Unidade.page(undefined, filter)
                .$promise.then(function(data) {
                    $scope.empresaList = [];
                    angular.forEach(data.content, function(value, key) {
                        $scope.empresaList.push(value);
                    });
                    $rootScope.loadingProgress = false;

                }).catch(function(response) {
                    $rootScope.loadingProgress = false;
                    console.error(response);
                    $scope.$emit('crud-critical', 'search', response);
                });
        }

        $scope.$on('changeEmpresa', function() {
            if ($scope.object.unidade.codigo === undefined) {
                delete $scope.object.unidade;
                $scope.bancoList = [];
            } else {
                initCampoBanco($scope.object);
            }
            delete $scope.object.banco;
            $scope.object.contaCorrente = undefined;
            $scope.agenciaContaList = [];
        });

        $scope.$on('changeAgencia', function() {
            delete $scope.object.meioPagamento;
            delete $scope.object.chequeAnterior;
        });

        $scope.$on('changeMeioPagamento', function() {
            if ($scope.object.unidade && $scope.object.contaCorrente && $scope.object.contaCorrente.banco) {
                if ("Cheque" === $scope.object.meioPagamento) {
                    buscarUltimoChequeRBfn();
                } else {
                    $scope.object.chequeAnterior = undefined;
                    if ($scope.object.relacaoBancariaPagamentosVO && $scope.object.relacaoBancariaPagamentosVO.length > 0) {
                        $scope.object.relacaoBancariaPagamentosVO.forEach((value) => {
                            value.cheque = undefined;
                        });
                    }
                }
            }
        });

        //FIXME: Verificar se o método está sendo utilizado.
        const buscarChequeInicialfn = () => {
            const buscarUltimoChequeRB = RelacaoBancaria.buscarultimocheque({
                id: $scope.object.contaCorrente.codigo
            }).$promise;
            buscarUltimoChequeRB.then(data => {
                $scope.chequeInicial = data.chequeInicial;
            });
        };

        const buscarUltimoChequeRBfn = () => {
            var buscarUltimoChequeRB = RelacaoBancaria.buscarultimocheque({id: $scope.object.contaCorrente.codigo}).$promise;
            buscarUltimoChequeRB.then(data => {
                if (data.chequeAnterior !== undefined) {
                    $scope.object.chequeAnterior = data.chequeAnterior;
                    $scope.chequeLimite = $scope.object.chequeAnterior;
                    let chequeAdicionado = data.chequeAnterior + 1;
                    if ($scope.object.relacaoBancariaPagamentosVO && $scope.object.relacaoBancariaPagamentosVO.length > 0) {
                        $scope.object.relacaoBancariaPagamentosVO.forEach((value) => {
                            value.cheque = chequeAdicionado++;
                        });
                    }
                }
            });
        };

        $scope.atualizarChequesItensRelacaoFn = () => {
            if ($scope.object.chequeAnterior !== undefined) {
                $scope.chequeLimite = $scope.object.chequeAnterior;
                let chequeAdicionado = $scope.object.chequeAnterior + 1;
                if ($scope.object.relacaoBancariaPagamentosVO && $scope.object.relacaoBancariaPagamentosVO.length > 0) {
                    $scope.object.relacaoBancariaPagamentosVO.forEach((value) => {
                        value.cheque = chequeAdicionado++;
                    });
                }
            }
        };

        function preencherLimiteUltimoCheque() {
            RelacaoBancaria.buscarultimocheque({
                id: $scope.object.contaCorrente.codigo
            }).$promise.then(function(data) {
                if (data.chequeAnterior !== undefined) {
                    $scope.chequeLimite = $scope.object.chequeAnterior;
                }
            });
        }

        //FIXME: Verificar se o método está sendo utilizado.
        var habilitaMeioPgto = function() {
            if (angular.isDefined($scope) && angular.isDefined($scope.object.unidade) && $scope.object.contaCorrente && $scope.object.contaCorrente.banco) {
                $scope.habilitaMeioPgto = true;
                return;
            }
            $scope.habilitaMeioPgto = true;
        };

        //TODO: Ao adicionar pgtos, qdo o pop-up fechar, invocar a função que incrementa o contador
        //		$scope.qtdadePagamentosRb = function() {
        //			return $scope.object.qtdPagamentos;
        //		}
        //
        //		$scope.qtdadePagamentosRb();

        function carregarTributos() {

            var filtroTributo = {};

            filtroTributo.exibeNota = true;

            Tributo.page(undefined, filtroTributo)
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

        //FIXME: Verificar se o método está sendo utilizado.
        function initCampoEmpresa() {
            $scope.listEmpresas = [];

            var pUnidade = Unidade.page(undefined, {
                tipoUnidade: "Matriz"
            }).$promise;

            pUnidade.then(function(data) {
                $scope.listEmpresas.push({
                    codigo: undefined,
                    codnome: ""
                });
                angular.forEach(data.content, function(value, key) {
                    $scope.listEmpresas.push(value);
                });

            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });
        }

        function initCampoBanco(object) {
            $scope.bancoList = [];

            if (!angular.isUndefined(object.unidade) && object.unidade !== undefined && object.unidade.codigo !== undefined) {
                var objContaCorrente = {};
                var pContaCorrente = ContaCorrente.obterContas(undefined, {
                    pessoa: {
                        codigo: object.unidade.codigo
                    },
                    tipoContaCorrente: "Normal"
                }).$promise;

                pContaCorrente.then(function(data) {
                    $scope.bancoList.push({
                        codigo: undefined,
                        nome: ''
                    });

                    angular.forEach(data, function(value, key) {
                        $scope.bancoList.push(value.banco);
                    });

                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'load', response);
                });
            }
        }

        function initCampoAgencia(object) {
            $scope.agenciaContaList = [];

            if (!angular.isUndefined(object.unidade) && object.unidade !== undefined && object.unidade.codigo !== undefined) {
                var objContaCorrente = {};
                var pContaCorrente = (undefined, {
                    pessoa: {
                        codigo: object.unidade.codigo
                    }
                }).$promise;

                pContaCorrente.then(function(data) {
                    $scope.agenciaContaList.push({
                        codigo: undefined
                    });

                    angular.forEach(data, function(value, key) {
                        if (!angular.isUndefined(object.banco) && object.banco !== undefined && object.banco.codigo === value.banco.codigo) {
                            $scope.agenciaContaList.push(value);
                        }
                    });

                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'load', response);
                });
            }
        }

        $scope.$on('changeBanco', function() {
            if (angular.isDefined($scope) && angular.isDefined($scope.object.contaCorrente.banco) && (angular.isDefined($scope.object.contaCorrente.banco.codigo) && $scope.object
                    .contaCorrente.banco.codigo !== undefined)) {
                $scope.object.contaCorrenteRecebimento = {};
                $scope.object.contaCorrenteRecebimento.banco = $scope.object.contaCorrente.banco;
                carregarContasCorrentes($scope.object.contaCorrente.banco.codigo, $scope.object.unidade.codigo)
                    .then(() => {
                        var contaCorrente = {};
                        contaCorrente.banco = $scope.object.contaCorrente.banco;
                        $scope.object.contaCorrente = contaCorrente;
                        delete $scope.object.meioPagamento;
                        delete $scope.object.chequeAnterior;
                    });
            } else {
                delete $scope.contaCorrenteRecebimento;
                $scope.agenciaContaList = [];
                delete $scope.object.meioPagamento;
                delete $scope.object.chequeAnterior;
            }
        });

        function carregarContasCorrentes(codigoBanco, codigoPessoa) {
            return $q(function(resolve, reject) {
                // CARREGAR CONTAS CORRENTES
                var contaCorrente = {};
                contaCorrente.banco = {};
                contaCorrente.banco.codigo = codigoBanco;
                contaCorrente.pessoa = {};
                contaCorrente.pessoa.codigo = codigoPessoa;

                ContaCorrente.obterContas(contaCorrente).$promise.then(function(data) {
                    $scope.agenciaContaList = [];

                    angular.forEach(data, function(value, key) {
                        $scope.agenciaContaList.push(value);
                    });

                    resolve($scope.agenciaContaList);
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'contrato load', response);
                    $scope.agenciaContaList = [];
                    reject(response);
                });
            });
        }

        $scope.montarLabelAgenciaConta = function(contaCorrente) {
            var conteudo = CalculadorFaturamento.montarCampoAgenciaConta(contaCorrente);
            if (CalculadorFaturamento.isCampoVazio(conteudo) || conteudo.trim() === '/')
                return ' ';
            else
                return conteudo;
        };

        $scope.cancelar = function() {
            if ($scope.screenMode === 'NEW') {
                $state.go('relacaobancaria.list');
            } else if ($scope.screenMode === 'EDIT') {
                $state.go('relacaobancaria.view', {
                    objectId: $scope.object.codigo
                });
            }
        };

        $scope.exibeMotivosRetorno = false;
        var exibeMotivosRetorno = function() {
            angular.forEach($scope.object.relacaoBancariaPagamentosVO, function(item) {
                if ("Inválido/Estornado" === item.statusPagamento) {
                    $scope.exibeMotivosRetorno = true;
                    return;
                }
                $scope.exibeMotivosRetorno = false;
            });
        };

        function loadObj() {
            $rootScope.loadingProgress = true;
            $scope.object = undefined;
            $scope.action = true;

			UsuarioLogadoService.possuiUmPerfilLista(['operador-tesouraria'])
				.$promise.then((result) => {
					$scope.possuiPerfilOperadorTesouraria = result.possuiUmPerfilLista;
				});

            let rb_get = () => RelacaoBancaria.get({
                id: $stateParams.objectId
            }).$promise;
            let rb_obterListaDeItensRelacao = () => RelacaoBancaria.obterListaDeItensRelacao({
                id: $stateParams.objectId
            }).$promise;

            let load_itens = (data_itens) => {
                $scope.action = true;
                $scope.btnFab = false;
                $scope.isOpen = false;

                /*TODO: Função para mostrar o icone quando os botões do final da tela não estiverem aparecendo*/
                $scope.selectedMode = 'md-fling';

                $scope.selectedDirection = 'up';

                if (data_itens.length > 10) {
                    $scope.btnFab = true;
                    $(document).ready(function() {
                        $('md-content').bind('scroll', function() {
                            if ($(this).scrollTop() + $(this).innerHeight() >= this.scrollHeight) {
                                $scope.btnFab = false;
                                $scope.btnBottom = true;
                            } else {
                                $scope.btnFab = true;
                                $scope.btnBottom = false;
                            }
                        });
                    });
                } else {
                    $scope.btnFab = false;
                }

                if (data_itens > 0) {
                    $('md-content').scrollTop();
                }

                $scope.object.qtdPagamentos = 0;
                $scope.object.total = 0;

                $scope.object.relacaoBancariaPagamentosVO = data_itens;

                carregarItensRecebidos($scope.object.relacaoBancariaPagamentosVO);
                carregarItensNaoRecebidos($scope.object.relacaoBancariaPagamentosVO);

                verificarFaltaDadosBancariosEnvio($scope, $scope.object.relacaoBancariaPagamentosVO, function(value) {
                    $scope.object.qtdPagamentos = value.qtdadePagamentos + $scope.object.qtdPagamentos;
                    $scope.object.total = $scope.object.total + value.valorAPagar;
                });

                montarResumo($scope.object);

            };

            //TODO: Refatorar validação de status
            let validar_edicao = (objRelacaoBancaria) => {
                if ($scope.screenMode === 'EDIT') {
                    let statusRB = objRelacaoBancaria.status;
                    if (statusRB && (statusRB === "Aguardando retorno" || statusRB === "Processando retorno (internet banking)" || statusRB === "Finalizada" || statusRB === "Cancelada")) {
                        $state.go('relacaobancaria.view', {
                            objectId: objRelacaoBancaria.codigo
                        });
                        $scope.notification.error("", "Não é permitido edição par ao status atual do registro.");
                    }
                }
            };

            let final = (data) => {
                isConciliacaoBancaria();
                habilitaBotaoRemoverRB();
                exibeMotivosRetorno();
                $scope.action = true;

                if ($scope.screenMode === 'EDIT') {
                    if ("Cheque" === $scope.object.meioPagamento) {
                        preencherLimiteUltimoCheque();
                        $scope.chequeAnteriorOriginal = angular.copy($scope.object.chequeAnterior);
                    } else if ($scope.screenMode === 'NEW') {
                        $scope.object.chequeAnterior = undefined;
                    }
                    carregarTipoDocumento();
                    carregarTributos();
                }

                if ($scope.screenMode === 'EDIT') {
                    initEdit($scope.object);
                } else if ($scope.screenMode === 'VIEW' && $scope.object.status === "Aberta") {
                    buscarFeriados();
                }
            };

            let load = (data) => {
                validar_edicao(data);
                $scope.action = true;
                $scope.object = angular.copy(data);
                $scope.contaCorrenteAux = angular.copy(data.contaCorrente);
                $scope.object.relacaoBancariaPagamentosVO = undefined;
                $scope.object.dataPagamento = DateTimeService.ajustarData($scope.object.dataPagamento);
            };

            return Promise
                .resolve()
                .then(rb_get)
                .then(load)
                .then(final)
                .then(rb_obterListaDeItensRelacao)
                .then(load_itens) // Carregar os itens separadamente.
                .then(() => {
                    $scope.action = false;
                    $scope.$apply();
                })
                .catch(function(response) {
                    $scope.$emit('crud-critical', 'load', response);
                    $rootScope.loadingProgress = false;
                    $scope.action = false;
                });
        }

        function initEdit(object) {
            $scope.listEmpresas = [];
            $scope.bancoList = [];
            $scope.agenciaContaList = [];

            // FIX: Corrigir isso, pois "alguém" está apagando a data.
            let dataPagamentoAux = angular.copy($scope.object.dataPagamento);

            let pUnidade = () => Unidade.page(undefined, {
                tipoUnidade: "Matriz"
            }).$promise;
            let pContaCorrente = () => ContaCorrente.obterContas(undefined, {
                pessoa: {
                    codigo: object.unidade.codigo
                }
            }).$promise;

            let pUnidade_result = (data) => {
                angular.forEach(data.content, function(value, key) {
                    $scope.listEmpresas.push(value);
                });
                $rootScope.loadingProgress = false;
            };

            let pContaCorrente_result = (data) => {
                $scope.bancoList.push({
                    codigo: undefined,
                    nome: ''
                });
                $scope.agenciaContaList = [];

                angular.forEach(data, function(value, key) {
                    $scope.bancoList.push(value.banco);

                    if (object.contaCorrente !== undefined && !angular.isUndefined(object.contaCorrente.banco) && object.contaCorrente.banco !== undefined && object.contaCorrente.banco.codigo ===
                        value.banco.codigo) {
                        $scope.agenciaContaList.push(value);
                    }

                });
                $rootScope.loadingProgress = false;
                $scope.object.contaCorrente = $scope.contaCorrenteAux;
                return carregarContasCorrentes($scope.object.contaCorrente.banco.codigo, $scope.object.unidade.codigo);
            };

            return Promise
                .resolve()
                .then(pUnidade)
                .then(pUnidade_result)
                .then(pContaCorrente)
                .then(pContaCorrente_result)
                .then(buscarListaMeioPgto)
                .then(() => {
                    $scope.object.dataPagamento = dataPagamentoAux;
                    $scope.$apply();
                })
                .catch(function(response) {
                    $rootScope.loadingProgress = false;
                    $scope.$emit('crud-critical', 'load', response);
                });
        }

        function carregarItensRecebidos(objectItemRelacao) {
            $scope.object.itensRecebidos = 0;

            angular.forEach(objectItemRelacao, function(value) {
                if (value.statusPagamento === 'Pago') {
                    $scope.object.itensRecebidos += 1;
                }
            });
        }

        function carregarItensNaoRecebidos(objectItemRelacao) {
            $scope.object.itensNaoRecebidos = 0;
            angular.forEach(objectItemRelacao, function(value) {
                if (value.statusPagamento !== 'Pago') {
                    $scope.object.itensNaoRecebidos += 1;
                }
            });
        }

        function newObj() {
            $scope.object = new RelacaoBancaria();
            $scope.object.$get();
        }

        $scope.editar = function() {
            $state.go('relacaobancaria.edit', {
                objectId: $scope.object.codigo
            });
        };


        $scope.retornar = function() {
            $state.go('relacaobancaria.list');
        };


        /**
         * Salvando Relação Bancária
         */
        $scope.salvarRelacaoBancaria = function() {
            if (!$scope.object.relacaoBancariaPagamentosVO || $scope.object.relacaoBancariaPagamentosVO.length <= 0) {
                $scope.notification.error("", "Deve haver, pelo menos, um pagamento selecionado.");
                return;
            }

            let error = false;
            let isInternetBanking = false;
            let isTipoDocumentoInvalido = false;

            if ($scope.object && $scope.object.meioPagamento && $scope.object.meioPagamento === "Internet banking") {
                isInternetBanking = true;
            }

            $scope.object.relacaoBancariaPagamentosVO.forEach((item) => {
                if (!item.bancoSelecionado.homologado) {
                    error = true;
                }

                // Atributo para colorir a linha da tabela de pagamentos. Indicando item inválido.
                item.dadosBancariosSelecionadoNaoHomolog = tipoDocumentoInvalido($scope.object.contaCorrente.banco, item);

                if (item.dadosBancariosSelecionadoNaoHomolog === true) {
                    isTipoDocumentoInvalido = true;
                }
            });

            if (error && isInternetBanking && isTipoDocumentoInvalido) {
                $scope.notification.error("",
                    "Não é possível para o internet banking, realizar transferência para o banco diferente do banco da relação bancária. Neste caso, user DOC ou TED");
                return;
            } else {
                $scope.object.relacaoBancariaPagamentosVO.forEach((item) => {
                    item.dadosBancariosSelecionadoNaoHomolog = false;
                });
            }

            $scope.action = true;
            let datePag = new Date($scope.object.dataPagamento);
            datePag.setHours(23);
            datePag.setMinutes(59);
            datePag.setSeconds(59);
            if ($scope.object.meioPagamento === "Internet banking" && datePag.getTime() < new Date().getTime()) {
                $scope.notification.error("", "A data de pagamento para internet banking não pode ser  anterior a data de hoje");
                $scope.action = false;
                return;
            }

            if ($scope.screenMode === 'NEW') {
                $scope.object.$save()
                    .then(function(data) {
                        $scope.notification.success("Novo", "Relação bancária inserida com sucesso");
                        $scope.action = false;
                        $state.go('relacaobancaria.view', {
                            objectId: data.codigo
                        });
                    }).catch(function(response) {
                        $scope.action = false;
                        $scope.$emit('crud-critical', 'save', response);
                    });
            } else if ($scope.screenMode === 'EDIT') {
                $scope.object.$update({
                        id: $scope.objectId
                    })
                    .then(function(data) {
                        $scope.notification.success("Edição", "Relação bancária atualizada com sucesso");
                        $scope.action = false;
                        $state.go('relacaobancaria.view', {
                            objectId: data.codigo
                        });
                    }).catch(function(response) {
                        $scope.action = false;
                        $scope.$emit('crud-critical', 'save', response);
                    });
            }
        };

        //Função para verificar se o item com o tipo "Depósito em conta/Transferência" tem o mesmo banco da Relação Bancária.
        function tipoDocumentoInvalido(bancoRelacaoBancaria, item) {
            let codigoBancoRB = bancoRelacaoBancaria.codigo;
            let codigoBancoItem = item.bancoSelecionado.codigo;

            if (codigoBancoRB !== codigoBancoItem && item.tipoDocumentoPagamento === "Depósito em conta/Transferência") {
                return true;
            }

            return false;
        }

        var isConciliacaoBancaria = function() {
            if (angular.isDefined($scope.object) && $scope.object.status === 'Aguardando retorno') {
                if ("Cheque" === $scope.object.meioPagamento || "Bankline manual" === $scope.object.meioPagamento) {
                    $scope.isConciliacao = true;
                    return;
                }
            }
            $scope.isConciliacao = false;
        };

        var habilitaBotaoRemoverRB = function() {
            if ($scope.object !== undefined && ($scope.object.status === 'Aguardando retorno' || $scope.object.status === 'Aberta')) {
                $scope.habilitaBotaoRemover = true;
                return;
            }
            $scope.habilitaBotaoRemover = false;
        };

        //Utilizado no popup de adicionar pagamentos
        $scope.listaBancos = [];

        function carregarTodosBancos() {
            $scope.listaBancos = [];
            Banco.todosordenado().$promise.then(function(data) {
                $scope.listaBancos.push({
                    banco: {
                        nome: ''
                    }
                });

                angular.forEach(data, function(value) {
                    $scope.listaBancos.push(value);
                });

            }).catch(function(response) {
                $scope.$emit('crud-critical', 'contrato load', response);
                $scope.listaBancos = [];
            });
        }

        //Utilizado no popup de adicionar pagamentos
        $scope.listaTipoDoc = [];

        function carregarTipoDocumento() {
            $scope.listaTipoDoc = [];
            EnumService.enumValues({
                    pacote: 'br.com.araujoabreu.sgi.contasApagar.domain',
                    classe: 'Pagamento',
                    enum: 'TipoDocumentoPagamento'
                })
                .$promise.then(function(data) {
                    $scope.listaTipoDoc.push("");

                    angular.forEach(data, function(value) {
                        $scope.listaTipoDoc.push(value);
                    });
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'load', response);
                });
        }

        //Utilizado no popup de adicionar pagamentos
        $scope.listaGerencias = [];

        function carregarGerencias() {
            $scope.listaGerencias = CentroFinanceiro.buscarGerenciasPorUnidade({
                id: $scope.object.unidade.codigo
            });
        }


        $scope.adicionar = function(relacao) {
            carregarTodosBancos();
            carregarGerencias();
            carregarTipoDocumento();
            $scope.showAdicionar(relacao);
        };

        $scope.showAdicionar = function(objRelacao) {
            $mdDialog.show({
                controller: DialogControllerAdicionarPagamentosRelacaoBancaria,
                locals: {
                    resource: RelacaoBancaria,
                    notification: $scope.notification
                },
                templateUrl: 'partials/tesouraria/relacao_bancaria/html/partials/dialog_adicionar_pagamentos.html',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                fullscreen: false,
                scope: $scope,
                preserveScope: true,
            });
        };

        function DialogControllerAdicionarPagamentosRelacaoBancaria($scope, $mdDialog, resource, notification) {
            $scope.selecionar = false;
            $scope.action = false;

            $scope.selecionarTodos = function(sel) {
                angular.forEach($scope.object.pesquisaPagamentosVO, function(vo) {
                    vo.selecionado = sel;
                });
            };

            $scope.limpar = function() {
                $scope.object.pesquisaPagamentoRBTO = {
                    numero: undefined,
                    codigoBanco: undefined,
                    nomeCredor: undefined,
                    valorDe: undefined,
                    valorAte: undefined,
                    codigoGerencia: undefined,
                    tipoDocPagamento: undefined
                };
                $scope.object.pesquisaPagamentosVO = undefined;
            };

            $scope.limpar();

            $scope.hide = function() {
                $mdDialog.hide();
            };

            $scope.cancel = function() {
                $mdDialog.cancel();
            };



            //Ajustar para exibir mensagens de erro
            $scope.mostrarPesquisa = function() {
                $scope.pesquisa = true;
                $scope.action = true;
                $scope.object.pesquisaPagamentosVO = undefined;
                resource.pesquisarpagamentos($scope.object).$promise.then(function(data) {
                    $scope.object = data;
                    verificarFaltaDadosBancariosEnvio($scope, $scope.object.relacaoBancariaPagamentosVO, {
                        notificandoFalta: false
                    });
                    if (data.pesquisaPagamentosVO.length >= 300) {
                        notification.atencao("", "O resultado da pesquisa é muito extenso. Somente os primeiros 300 registros serão exibidos.");
                    }
                    $scope.action = false;
                }).catch(function(response) {
                    console.error(response);
                    notification.error("", response.data);
                    $scope.action = false;
                });

            };

            //Ajustar para exibir mensagens de erro
            $scope.adicionarPagamentos = function() {
                resource.adicionarpagamentos($scope.object).$promise.then(function(data) {
                    $scope.object = data;
                    $scope.object.qtdPagamentos = 0;

                    verificarFaltaDadosBancariosEnvio($scope, $scope.object.relacaoBancariaPagamentosVO, function(value, key) {
                        $scope.object.qtdPagamentos = $scope.object.qtdPagamentos + value.qtdadePagamentos;
                    });

                }).catch(function(response) {
                    console.error(response);
                    notification.error("", response.data);
                });

                $scope.hide();
            };
        }


        $scope.remover = function() {
            $scope.showConfirmRemover($scope.object);
        };

        $scope.showConfirmRemover = function(objRelacao) {
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

                    var index = undefined;
                    angular.forEach($scope.listaResultado, function(value, key) {
                        if (value.codigo === $scope.object.codigo) {
                            index = $scope.listaResultado.indexOf(value);
                        }
                    });

                    if (index !== undefined) {
                        $scope.listaResultado.splice(index, 1);
                        index = undefined;
                    }

                    delete $scope.object;
                    $scope.cancel();
                    loadingProgress = false;
                    $scope.action = false;
                    $state.go('relacaobancaria.list');


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

        /* INICIO AÇÃO CANCELAR RB */
        var confirmCancelFn = function(fn_hide, action) {
            action = true;
            var relacaoBancariaCancelar = RelacaoBancaria.cancel({
                id: $scope.object.codigo
            }).$promise;

            relacaoBancariaCancelar.then(function(data) {

                var title = 'Cancelar Relação bancária';
                var message = 'Relação bancária ' + $scope.object.numero + ' cancelada com sucesso';
                $scope.notification.success(title, message);

                loadObj().then(() => {
                    fn_hide();
                    action = false;
                });
            }).catch(function(response) {
                $scope.action = false;
                action = false;
                $rootScope.loadingProgress = false;
                console.error('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
            });

        };

        $scope.showConfirmCancelarRB = function(objRelacao) {
            $mdDialog.show({
                controller: DialogControllerCancelarRelacaoBancaria,
                locals: {
                    fn: confirmCancelFn
                },
                templateUrl: 'partials/tesouraria/relacao_bancaria/html/partials/dialog_cancelar_relacao_bancaria.html',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        $scope.cancelarRb = function() {
            $scope.showConfirmCancelarRB($scope.object);
        };

        function DialogControllerCancelarRelacaoBancaria($scope, $mdDialog, fn) {
            $scope.action = false;
            $scope.confirm = function() {
                $scope.action = true;
                fn($scope.hide, $scope.action);
            };

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
        }
        /* FIM AÇÃO CANCELAR RB */

        /* INICIO AÇÃO ENVIAR RB */
        var confirmCancelEnviarFn = function(fn_hide, action) {
            $scope.botaoEnviarAction = true;

            let error = false;

            let dadosBancariosSelecionadoNaoHomolog = false;
            let vencimentoBoletoDataAnterior = false;
            let dataPagRelacaoBancaria = converterParaData($scope.object.dataPagamento);

            $scope.object.relacaoBancariaPagamentosVO.forEach((value) => {
                if (value.itemRelacao.listaPagamentos[0].pagamento.tipoDocumentoPagamento === 'Boleto') {
                    let dateVencimentoBoleto = buscarDataVencimentoBoleto(value);

                    if (dateVencimentoBoleto) {
                        let datePag = converterParaData(dateVencimentoBoleto);

                        if (datePag.getTime() < dataPagRelacaoBancaria.getTime()) {

                            let proximoDiaUtilRelacaoBancaria = procurarProximoDiaUtil(dataPagRelacaoBancaria);
                            let proximoDiaUtilPagamento = procurarProximoDiaUtil(datePag);

                            if (proximoDiaUtilPagamento.getTime() < proximoDiaUtilRelacaoBancaria.getTime()) {
                                value.dadosBancariosSelecionadoNaoHomolog = true;
                                vencimentoBoletoDataAnterior = true;
                            }
                        }
                    }
                }

                if (value.tipoDocumentoPagamento === 'Depósito em conta/Transferência') {
                    if (value.dadosBancarios === undefined && value.dadosBancarios.dataHomologacao === undefined) {
                        dadosBancariosSelecionadoNaoHomolog = true;
                        value.dadosBancariosSelecionadoNaoHomolog = true;
                    }
                }
            });

            if (vencimentoBoletoDataAnterior && $scope.object.meioPagamento === "Internet banking") {
                $scope.action = false;
                $scope.botaoEnviarAction = false;
                $scope.notification.error("", 'O vencimento do(s) boleto(s) em vermelho é anterior à data de pagamento da relação bancária.');
                fn_hide();
                return;
            }

            if (dadosBancariosSelecionadoNaoHomolog && $scope.object.meioPagamento === "Internet banking") {
                $scope.notification.error("",
                    'Existe(m) pagamento(s) para "Depósito/Transf." com dados bancários selecionados não homologados (indicados em vermelho). O envio da relação bancária não será permitido.'
                );
                $scope.action = false;
                $scope.botaoEnviarAction = false;
                fn_hide();
                return;
            }

            if ($scope.object.meioPagamento === "Internet banking") {
                $scope.object.relacaoBancariaPagamentosVO.forEach(function(vo) {
                    if (vo.tipoDocumentoPagamento !== "Boleto" &&
                        vo.tipoDocumentoPagamento !== "Depósito em conta/Transferência" &&
                        vo.tipoDocumentoPagamento !== "DOC" &&
                        vo.tipoDocumentoPagamento !== "TED") {
                        error = true;
                    }
                });
                if (error) {
                    action = false;
                    $scope.action = false;
                    $scope.botaoEnviarAction = false;
                    fn_hide();
                    $scope.notification.error("O envio não é possível, pois existem pagamentos não permitidos para o Internet banking.");
                    return false;
                }
            }

            if (temPagamentoDuplicado($scope.object.relacaoBancariaPagamentosVO)) {
                action = false;
                $scope.action = false;
                $scope.botaoEnviarAction = false;
                fn_hide();
                $scope.notification.error("O envio não é possível, pois existem pagamentos repetidos.");
                return false;
            }

            var relacaoBancariaEnviar = RelacaoBancaria.send({
                id: $scope.object.codigo
            }).$promise;

            relacaoBancariaEnviar.then(function(data) {

                var title = 'Enviar Relação bancária';
                var message = 'Relação bancária ' + $scope.object.numero + ' enviada com sucesso';
                $scope.notification.success(title, message);

                loadObj().then(() => {
                    action = false;
                    fn_hide();
                    $scope.botaoEnviarAction = false;
                });
            }).catch(function(response) {
                $scope.action = false;
                $scope.botaoEnviarAction = false;
                action = false;
                $rootScope.loadingProgress = false;
                console.error('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
                fn_hide();
            });
        };

        // FIXME: Remover este método futuramento, se o problema do timezone do horário de verão for corrigido no sistema.
        // Converte a string no formato "2018-04-01T00:00:00.000+0000" para um objeto javascript do tipo data
        function converterParaData(dataInicial) {
            let dataFormatada = moment.utc(dataInicial).utcOffset("Z").format('YYYY-MM-DD');
            // FIXME: Refatorar
            if (dataFormatada !== 'Invalid date') {
                let dataTipoData = moment(dataFormatada).toDate();
                return dataTipoData;
            } else {
                return undefined;
            }
        }

        function procurarProximoDiaUtil(data) {
            let dataPesquisa = angular.copy(data);

            while (isDiaUtil(dataPesquisa) === false) {
                dataPesquisa.setDate(dataPesquisa.getDate() + 1);
            }

            return dataPesquisa;
        }

        function isDiaUtil(data) {
            let resultado = true;

            // verifica se o dia é sábado ou domingo
            if (data.getDay() === 6 || data.getDay() === 0) {
                resultado = false;
            }

            // se não for sábado e domingo, verifica se é feriado
            if (resultado === true && isFeriado(data)) {
                resultado = false;
            }

            return resultado;
        }

        function isFeriado(data) {

            let dia = Number(moment(data).format("DD"));
            let mes = Number(moment(data).format("MM"));
            let ano = Number(moment(data).format("YYYY"));
            let resultado = false;

            angular.forEach($scope.listaFeriados, function(value) {

                let feriadoDia = value.dia;
                let feriadoMes = value.mes;
                let feriadoAno = value.ano;

                if (feriadoAno === undefined) {
                    if (feriadoDia === dia && feriadoMes === mes) {
                        resultado = true;
                    }
                } else {
                    if (feriadoDia === dia && feriadoMes === mes && feriadoAno === ano) {
                        resultado = true;
                    }
                }
            });

            return resultado;
        }

        // Para validação de envio da RB
        function buscarFeriados() {
            Feriado.buscarTodos().$promise.then(function(data) {
                $scope.listaFeriados = [];

                angular.forEach(data, function(value) {
                    $scope.listaFeriados.push(value);
                });

            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });
        }

        // Buscar data de vencimento no boleto. Procurando na linha digitavel ou código de barras
        function buscarDataVencimentoBoleto(relacaoBancariaPagamentosVO) {
            let dataVencimento;

            if (relacaoBancariaPagamentosVO.linhaDigitavel && relacaoBancariaPagamentosVO.linhaDigitavel.trim().length > 0) {
                dataVencimento = BoletoService.dataVencimentoLinhaDigitavel(relacaoBancariaPagamentosVO.linhaDigitavel);
            } else {
                dataVencimento = BoletoService.dataVencimentoCodigoBarras(relacaoBancariaPagamentosVO.codigoBarras);
            }

            return dataVencimento;
        }

        $scope.showConfirmEnviar = function(objRelacao) {
            $mdDialog.show({
                controller: DialogControllerEnviarRelacaoBancaria,
                locals: {
                    fn: confirmCancelEnviarFn
                },
                templateUrl: 'partials/tesouraria/relacao_bancaria/html/partials/dialog_enviar_relacao_bancaria.html',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        function DialogControllerEnviarRelacaoBancaria($scope, $mdDialog, fn) {
            $scope.action = false;
            $scope.confirm = function() {
                $scope.action = true;
                $scope.action = fn($scope.hide, $scope.action);
            };

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
        };

        $scope.enviar = function() {
            if (envioValidado()) {
                $scope.action = true;
                $scope.showConfirmEnviar($scope.object);
            }
        };

        function envioValidado() {

            $scope.object.relacaoBancariaPagamentosVO.forEach((item) => {
                item.dadosBancariosSelecionadoNaoHomolog = false;
            });

            let validacao_1 = true;
            let validacao_2 = true;
            let validacao_3 = true;

            validacao_1 = validarPagamentoHomologado();
            validacao_2 = validarValores();
            validacao_3 = validarDadosBancarios();

            return validacao_1 && validacao_2 && validacao_3;
        }

        //Quando o meio de pagamento for 'Internet banking', todos os pagamentos de 'Depósito em conta/Transferência' devem estar homologados.
        function validarPagamentoHomologado() {
            let resultado = true;

            if ($scope.object && $scope.object.meioPagamento && $scope.object.meioPagamento === "Internet banking") {
                $scope.object.relacaoBancariaPagamentosVO.forEach((item) => {
                    if (item.tipoDocumentoPagamento === "Depósito em conta/Transferência" && item.bancoSelecionado.homologado === false) {
                        resultado = false;
                        item.dadosBancariosSelecionadoNaoHomolog = true;
                    }
                });
            }

            if (!resultado) {
                $scope.notification.error("",
                    "Existe(m) pagamento(s) para \"Depósito/Transf.\" com dados bancários selecionados não homologados (indicados em vermelho). O envio da relação bancária não será permitido."
                );
            }

            return resultado;
        }

        // SGINV-1750
        // Quando o meio de pagamento for 'Internet banking',
        // os pagamentos que forem do tipo documento igual a "Boleto", devem ter o valor da linha digitavel igual ao  "Valor da parcela" ou ao valor "A pagar".
        function validarValores() {
            let resultado = true;

            if ($scope.object && $scope.object.meioPagamento && $scope.object.meioPagamento === "Internet banking") {
                $scope.object.relacaoBancariaPagamentosVO.forEach((item) => {
                    if (item.tipoDocumentoPagamento === "Boleto") {

                        if (item.linhaDigitavel && item.linhaDigitavel.length > 0) {

                            let valorLinhaDigitavel = getValorDaLinhaDigitavel(item.linhaDigitavel);
                            let valorDaParcela = item.valor;
                            let valorAPagar = item.valorAPagar;

                            if (valorLinhaDigitavel > 0 && valorLinhaDigitavel !== valorDaParcela && valorLinhaDigitavel !== valorAPagar) {
                                resultado = false;
                                item.dadosBancariosSelecionadoNaoHomolog = true;
                            }

                        } else {
                            resultado = false;
                            item.dadosBancariosSelecionadoNaoHomolog = true;
                        }
                    }
                });
            }

            if (!resultado) {
                $scope.notification.error("", "O valor do(s) boleto(s) em vermelho é diferente do valor total da parcela ou do valor a pagar. O envio da relação bancária não será permitido");
            }

            return resultado;
        }

        var validarDadosBancarios = () => {

            let resultado = -1;
            let error = false;

            if ($scope.object.relacaoBancariaPagamentosVO && $scope.object.relacaoBancariaPagamentosVO.length > 0) {
                $scope.object.relacaoBancariaPagamentosVO.forEach((item, key) => {

                    if (item.itemRelacao && item.itemRelacao.listaPagamentos && item.itemRelacao.listaPagamentos.length > 0) {
                        item.itemRelacao.listaPagamentos.forEach((pgto) => {
                            if (pgto.pagamento && pgto.pagamento.dadosBancarios && pgto.pagamento.dadosBancarios.situacao === 'Inativo' && !error) {
                                if (pgto.pagamento.tipoDocumentoPagamento !== 'Boleto' &&
                                    pgto.pagamento.tipoDocumentoPagamento !== 'Guia' &&
                                    pgto.pagamento.tipoDocumentoPagamento !== 'Concessionária') {
                                    error = true;
                                    resultado = key + 1;
                                    return;
                                }
                            }
                        });
                    }
                });
            }

            if (resultado > 0) {
                $scope.notification.error("", `A ${resultado}ª linha possui dados bancários inativos. O envio da relação bancária não será permitido.`);
                $scope.object.relacaoBancariaPagamentosVO[resultado -1].dadosBancariosSelecionadoNaoHomolog = true;
                return false;
            } else {
                return true;
            }
        };

        function getValorDaLinhaDigitavel(linhaDigitavel) {

            if (linhaDigitavel && linhaDigitavel.trim().length === 47) {
                let linha = linhaDigitavel.trim();
                let valorString = linha.substring(37, 47);
                let valorFormatado = valorString.substring(0, 8) + "." + valorString.substring(8, 10);
                let valorNumerico = Number(valorFormatado);
                return valorNumerico;
            }

            return undefined;
        }

        //FIXME: verificar se pode usar mascara ao inves deste método.
        $scope.formatarLinhaDigitavel = function(linhaDigitavel) {
            let linha = linhaDigitavel.trim();
            let resultado = linhaDigitavel;

            if (linha.length > 0) {
                let linhaFormatada = "";
                linhaFormatada = linhaFormatada.concat(linha.substring(0, 5));
                linhaFormatada = linhaFormatada.concat(".");
                linhaFormatada = linhaFormatada.concat(linha.substring(5, 10));
                linhaFormatada = linhaFormatada.concat(".");
                linhaFormatada = linhaFormatada.concat(linha.substring(10, 15));
                linhaFormatada = linhaFormatada.concat(".");
                linhaFormatada = linhaFormatada.concat(linha.substring(15, 21));
                linhaFormatada = linhaFormatada.concat(".");
                linhaFormatada = linhaFormatada.concat(linha.substring(21, 26));
                linhaFormatada = linhaFormatada.concat(".");
                linhaFormatada = linhaFormatada.concat(linha.substring(26, 32));
                linhaFormatada = linhaFormatada.concat(".");
                linhaFormatada = linhaFormatada.concat(linha.substring(32, 33));
                linhaFormatada = linhaFormatada.concat(".");
                linhaFormatada = linhaFormatada.concat(linha.substring(33, 47));

                resultado = linhaFormatada;
            }

            return resultado;
        };

        /**
         * Método para verificar se exite pagamentos duplicados na relação bancária.
         */
        var temPagamentoDuplicado = (listaRelacaoBancariaPagamentosVO) => {
            let resultado = false;
            let conjCodPag = new Set();

            listaRelacaoBancariaPagamentosVO.forEach((vo) => {

                vo.itemRelacao.listaPagamentos.forEach((itemRelacaoPagamento) => {
                    let codPag = itemRelacaoPagamento.pagamento.codigo;

                    if (conjCodPag.has(codPag)) {
                        resultado = true;
                    } else {
                        conjCodPag.add(codPag);
                    }
                });

                // Foi usado o atributo "dadosBancariosSelecionadoNaoHomolog" pois no HTML já está configurado para deixar a linha em vermelho
                // de acordo com o comportamento deste atributo.
                if (resultado) {
                    if (!listaRelacaoBancariaPagamentosVO.some((item) => { return item.dadosBancariosSelecionadoNaoHomolog; })) {
                        vo.dadosBancariosSelecionadoNaoHomolog = true;
                    }
                }
            });

            return resultado;
        };

        /* FIM AÇÃO ENVIAR RB */


        //FIXME: Verificar se o método está sendo utilizado.
        //totalizacao das RBs com ptgos validos (pagos)
        var totaisRBsListadas = function totaisRBsListadas() {
            var page = $scope.gridPesquisa.data;
            $scope.object.total = 0;
            var itens = [];

            angular.forEach($scope.gridPesquisa.data, function(value, key) {
                itens.push(value.itemRelacao);
                $scope.object.total = $scope.object.total + value.total;
            });

            if (itens !== undefined && itens) {
                angular.forEach(itens, function(value, key) {
                    $scope.object.totalPago = value.totalPago;
                    $scope.object.totalItens = value.totalPgtos;
                });
            }
        };

        $scope.removerPagamento = function(item, index) {
            confirmRemoverPgtoFn(item, index, undefined);
            /* FIM REMOVER PGTO */
        };

        /* INICIO AÇÃO REMOVER PGTO*/
        var confirmRemoverPgtoFn = function(item, index, fn_hide) {
            $scope.object.relacaoBancariaPagamentosVO.splice(index, 1);
            $scope.object.qtdPagamentos = $scope.object.qtdPagamentos - item.qtdadePagamentos;
            if ($scope.object.itensRemovidos === undefined) {
                $scope.object.itensRemovidos = [];
            }
            if (item.codigo !== undefined) {
                $scope.object.itensRemovidos.push(item.codigo);
            }
            $scope.atualizarChequesItensRelacaoFn();
        };

        //FIXME: Verificar se o método está sendo utilizado.
        var showConfirmRemoverPgto = function(item, index) {
            $mdDialog.show({
                controller: DialogControllerRemoverPagamentoRelacaoBancaria,
                locals: {
                    fn: confirmRemoverPgtoFn,
                    item: item,
                    index: index
                },
                templateUrl: 'partials/tesouraria/relacao_bancaria/html/partials/dialog_remocao_pagamento_relacao_bancaria.html',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        function DialogControllerRemoverPagamentoRelacaoBancaria($scope, $mdDialog, fn, item, index) {

            $scope.action = false;

            $scope.confirm = function() {
                $scope.action = true;
                fn(item, index, $scope.hide);
            };

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
        }


        /* INICIO AÇÃO EDITAR PGTO*/
        var confirmEditarPgtoFn = function(item, fn_hide, action, fn_ajustar_botoes) {
            RelacaoBancaria.editItem(undefined, item).$promise.then(function(data) {
                $scope.notification.success('Dados do pagamento', 'Pagamento ' + item.numero + ' alterado com sucesso');
                verificarFaltaDadosBancariosEnvio($scope, $scope.object.relacaoBancariaPagamentosVO,
                    function(value, index) {
                        if (value.codigo === item.codigo &&
                            value.numero === item.numero) {
                            value.valorAPagar = data.valorAPagar;
                            value.valorJuros = data.valorJuros;
                            value.valorMulta = data.valorMulta;
                            $scope.object.relacaoBancariaPagamentosVO[index] = undefined;
                            $scope.object.relacaoBancariaPagamentosVO[index] = data;
                            return;
                        }
                    });

                fn_hide();
                fn_ajustar_botoes();
                if (item.codigo !== undefined) {
                    loadObj();
                }
            }).catch(function(response) {
                fn_ajustar_botoes();
                $rootScope.loadingProgress = false;
                console.error('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
            });

        };

        var itemPopUp = {};
        var showConfirmEditarPgto = function(ev, item) {
            itemPopUp = angular.copy(item);
            var rb = angular.copy($scope.object);

            delete rb.relacaoBancariaPagamentosVO;
            item.relacaoBancaria = rb;

            RelacaoBancaria.getItemRelacaoVO(item).$promise.then(data => {
                $mdDialog.show({
                    controller: DialogControllerEditarPagamentoRelacaoBancaria,
                    locals: {
                        fn: confirmEditarPgtoFn,
                        item: data
                    },
                    templateUrl: 'partials/tesouraria/relacao_bancaria/html/partials/editar_pagamento.detail.html',
                    parent: angular.element(document.body),
                    clickOutsideToClose: false,
                    fullscreen: false
                });
            }).catch(response => {
                console.error('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
            });
        };

        $scope.cancelarEditarPgto = function(codigo) {
            $state.go('relacaobancaria.view', {
                objectId: codigo
            });
        };

        function DialogControllerEditarPagamentoRelacaoBancaria(DialogConfirm, $scope, $mdDialog, $timeout, fn, item) {
            var dtPgto = moment(item.dataPagamento).format('YYYY-MM-DD');
            var momentObj = moment(dtPgto, 'YYYY-MM-DD').toDate();
            $scope.action = false;

            if (item.tipoDocumentoPagamento === 'Outros') {
                item.codigoBarras = undefined;
                item.linhaDigitavel = undefined;
            }

            $scope.itemOriginal = angular.copy(item);

            let pagamentoConvertido = converterData(item.dataPagamento);
            let vencimentoConvertido = converterData(item.dataVencimento);

            item.dataPagamento = pagamentoConvertido;
            item.dataVencimento = vencimentoConvertido;

            /*
			  A 'dataPagamento' está vindo num formato parecido com esse => "2018-04-01T00:00:00.000+0000".
			  Neste formato a conversão que é feita na tela pelo angular/tt-date-picker acaba alterando a data para um valor incorreto.
			  Outro problema é que como a 'dataPagamento' está no tipo "String", ocorre um bug na tela, fazendo a data desaparecer. Pra corrigir esse problema, a data precisa estar no tipo "Date" ao inves de "String".
			  SOLUÇÃO: Aqui é usado o moment.js para retornar a data certa no tipo "Date".
			*/
            function converterData(dataInicial) {
                let dataFormatada = moment.utc(dataInicial).utcOffset("Z").format('YYYY-MM-DD');
                let dataTipoData = moment(dataFormatada).toDate();
                return dataTipoData;
            }

            $scope.enumTipoNumBoleto = ['Linha digitável', 'Código de barras'];

            $scope.$on('changeInfoBoleto', function() {
                exibirLDCodBarr();
            });

            function exibirLDCodBarr() {
                if ($scope.object.infoBoleto === 'Linha digitável') {
                    $scope.exibeLinhaDigitavel = true;

                } else {
                    $scope.exibeLinhaDigitavel = false;
                }
            }

            $scope.changeSelecaoBanco = function(item) {
                angular.forEach($scope.object.listDadosBancarios, function(value) {
                    value.selecao = false;
                    if (value.id === item.id) {
                        value.selecao = true;
                        $scope.object.dadosBancarios = value;
                    }
                });
            };

            EnumService.enumValues({
                    pacote: 'br.com.araujoabreu.sgi.contasApagar.domain',
                    classe: 'Pagamento',
                    enum: 'TipoDocumentoPagamento'
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

            $scope.$on('changeTipoDocPag', function() {
                statusCamposCodBarrLD();
                $scope.object.codigoBarras = '';
                $scope.object.linhaDigitavel = '';
            });

            /* Inicio da Dialog Homologação */
            $scope.homologarDadosBancarios = function(item, object) {
                let itemRelacaoPagamento = object.itemRelacao.listaPagamentos[0];
                item.codigoPagamento = itemRelacaoPagamento.pagamento.id;
                // Chamada do serviço que criará um dialog de confirmação.
                DialogConfirm.show(
                    undefined,
                    item,
                    'Homologar manualmente dados bancários: Confirmação',
                    'Deseja homologar manualmente estes dados bancários?',
                    'Confirmar',
                    'Cancelar',
                    confirmHomologar,
                    cancelHomologar);
            };

            function confirmHomologar(object, fnHide) {
                DadosBancarios.homologarDadosBancariosManual(object)
                    .$promise.then(function(data) {
                        object = data;

                        // Esta função veio do dialog.
                        fnHide();

                        $scope.confirm();

                        //$scope.notification.success("Sucesso");
                    }).catch(function(response) {
                        $scope.$emit('crud-critical', 'dialog', response);
                    });
            }

            function cancelHomologar(fnHide) {
                fnHide();
                $mdDialog.show({
                    controller: DialogControllerEditarPagamentoRelacaoBancaria,
                    locals: {
                        fn: fn,
                        item: item
                    },
                    templateUrl: 'partials/tesouraria/relacao_bancaria/html/partials/editar_pagamento.detail.html',
                    parent: angular.element(document.body),
                    clickOutsideToClose: false,
                    fullscreen: false
                });
            }
            /*Fim da Dialog Homologação*/

            function isTributo() {
                return $scope.object.tAorTR;
            }

            /* Inicio da Dialog Conta Contabil */

            $scope.contaContabil = function(tipoContaContabil) {
                $mdDialog.show({
                    controller: DialogContaContabil,
                    locals: {
                        pagamento: $scope.object,
                        tipoContaContabil: tipoContaContabil,
                        notification: $scope.notification
                    },
                    templateUrl: 'partials/tesouraria/movimento_autorizado_boletos/html/partials/dialog_conta_contabil.html',
                    parent: angular.element(document.body),
                    clickOutsideToClose: false,
                    fullscreen: false
                });
            };

            $timeout(function() {
                //FIXME deve existir algum "bug" no componente de data com dialog, pois a data só funcionou usando "moment" e com $timeout
                $scope.object = angular.copy(item);

                if ($scope.object.dadosBancarios === undefined) {
                    $scope.object.listDadosBancarios.map(dadosbancarios => {
                        if (dadosbancarios.principal) {
                            dadosbancarios.selecao = true;
                        } else {
                            dadosbancarios.selecao = false;
                        }
                    });
                } else {
                    $scope.object.listDadosBancarios.map(dadosbancarios => {
                        if (dadosbancarios.codigo === $scope.object.dadosBancarios.codigo) {
                            dadosbancarios.selecao = true;
                        } else {
                            dadosbancarios.selecao = false;
                        }
                    });
                }

                $scope.object.informacaoNumeroBoletoSelecionado = 'Linha digitável';
                $scope.object.infoBoleto = 'Linha digitável';

                exibirLDCodBarr();

                statusCamposCodBarrLD();

                var itemAux = angular.copy(item);

                if (item.tAorTR) {
                    //$scope.object.valorLiquido = item.total - $scope.object.valorTributos - $scope.object.valorAdiantamento - $scope.object.valorDevolucao -  $scope.object.valorOutrosDescontos;
                    $scope.exibirLabelTributo = true;
                } else {
                    //$scope.object.valorLiquido = $scope.object.valorNota;
                    $scope.exibirLabelTributo = false;
                }
            }, 100);

            $scope.ajustarBotoes = function() {
                $scope.action = false;
            };

            $scope.confirm = function() {
                $scope.action = true;
                if (angular.isDefined($scope.object.linhaDigitavel) && $scope.object.linhaDigitavel !== undefined) {
                    $scope.object.linhaDigitavel = $scope.object.linhaDigitavel.replace(/\./g, '');
                }
                fn($scope.object, $scope.hide, $scope.action, $scope.ajustarBotoes);
            };

            $scope.hide = function() {
                item = angular.copy($scope.itemOriginal);
                $mdDialog.hide();
            };

            $scope.cancel = function() {
                $mdDialog.cancel();
                $scope.object.valorDesconto = 0;
                $scope.object.valorMulta = 0;
                $scope.object.valorJuros = 0;
                item = angular.copy($scope.itemOriginal);
                $scope.object.valorAPagar = $scope.object.total;
            };

            function DialogContaContabil($scope, $timeout, $mdDialog, $state, pagamento, tipoContaContabil, notification, AutocompleteService) {

                $scope.title = tipoContaContabil.toLowerCase();
                $scope.contaContabil = undefined;
                var aux = undefined;

                function checkListaContaContabil() {

                    if (!angular.isUndefined(pagamento.listaPgtoCC) && pagamento.listaPgtoCC !== undefined) {

                        var contaContabilPorTipo = undefined;

                        angular.forEach(pagamento.listaPgtoCC, function(value, key) {
                            if (value.tipo === tipoContaContabil) {
                                contaContabilPorTipo = value.contaContabil;
                            }
                        })

                        if (contaContabilPorTipo !== undefined) {
                            aux = contaContabilPorTipo;
                            $scope.contaContabil = contaContabilPorTipo;
                        }
                    }
                }

                checkListaContaContabil();

                // INICIO DO AUTOCOMPLETE DE CONTA CONTÁBIL
                function autoCompleteContaContabil() {

                    $scope.searchTextContaContabil = undefined;
                    $scope.selectedItemContaContabil = aux;
                    $scope.resultsContaContabil = [];

                    $scope.fnSelectedItemContaContabil = function(item) {
                        $scope.contaContabil = item;
                    };

                    $scope.querySearchContaContabil = function(query) {
                        $rootScope.loadingProgress = true;

                        var filtro = {};
                        filtro.nome = query;

                        var resultContaContabilPromise = AutocompleteService.getContaContabil(filtro).$promise;

                        var contaContabilArray = [];

                        return resultContaContabilPromise.then(function(contaContabil) {

                            $rootScope.loadingProgress = false;

                            angular.forEach(contaContabil, function(contaContabil) {
                                contaContabilArray.push(contaContabil);
                            })
                            return contaContabilArray;
                        });
                    }
                } // FIM DO AUTOCOMPLETE DE CONTA CONTÁBIL

                autoCompleteContaContabil();

                $scope.confirm = function() {
                    if ($scope.contaContabil === undefined) {
                        console.error("Conta contábil é um campo de preenchimento obrigatório.");
                        notification.error("Conta contábil é um campo de preenchimento obrigatório.");
                    } else {
                        var jaTemNaLista = false;

                        angular.forEach(pagamento.listaPgtoCC, function(value, key) {
                            if (value.tipo === tipoContaContabil) {
                                value.contaContabil = $scope.contaContabil;
                                jaTemNaLista = true;
                                return;
                            }
                        });

                        if (!jaTemNaLista) {
                            var objPagamento = {
                                codigo: pagamento.codigo
                            };
                            var objPagamentoContaContabil = {};

                            objPagamentoContaContabil.codigo = undefined;
                            objPagamentoContaContabil.tipo = tipoContaContabil;
                            objPagamentoContaContabil.pagamento = objPagamento;
                            objPagamentoContaContabil.contaContabil = $scope.contaContabil;

                            pagamento.listaPgtoCC.push(objPagamentoContaContabil);
                        }

                        $scope.hide();
                        showConfirmEditarPgto(undefined, pagamento);
                    }

                };

                $scope.hide = function() {
                    $mdDialog.hide();
                };
                $scope.cancel = function() {
                    $mdDialog.cancel();
                    showConfirmEditarPgto(undefined, itemPopUp);
                };
            } /* Fim da Dialog  Conta Contabil */


            function statusCamposCodBarrLD() {
                switch ($scope.object.tipoDocumentoPagamento) {
                    case 'Guia':
                        $scope.tipoGuia = true;
                        $scope.tipoBoleto = false;
                        $scope.tipoConc = false;
                        break;

                    case 'Concessionária':
                        $scope.tipoGuia = false;
                        $scope.tipoBoleto = false;
                        $scope.tipoConc = true;
                        break;

                    default:
                        $scope.tipoGuia = false;
                        $scope.tipoBoleto = true;
                        $scope.tipoConc = false;
                }

                if ($scope.object.tipoDocumentoPagamento === 'Boleto' || $scope.object.tipoDocumentoPagamento === 'Concessionária' || ($scope.object.tipoDocumentoPagamento === 'Guia' &&
                        isTributo())) {
                    $scope.disableInfoBoleto = false;
                    $scope.disableLDCodBarr = false;
                    exibirLDCodBarr();
                } else {
                    $scope.disableInfoBoleto = true;
                    $scope.disableLDCodBarr = true;
                    $scope.object.codigoBarras = $scope.object.codigoBarras !== undefined ? $scope.object.codigoBarras : '';
                    $scope.object.linhaDigitavel = $scope.object.linhaDigitavel !== undefined ? $scope.object.linhaDigitavel : '';
                }
            }

            $scope.$on('changeValorPagar', function() {
                var multa = $scope.object.valorMulta;
                var juros = $scope.object.valorJuros;
                var descontos = $scope.object.valorDesconto;
                var valorLiquido = $scope.object.valorLiquido;

                var valorPagar = (valorLiquido + juros + multa) - descontos;
                $scope.object.valorAPagar = valorPagar;

                if (valorPagar <= 0) {
                    $scope.colorValor = 'red';
                    $scope.object.valorAPagar = 0;
                } else {
                    $scope.colorValor = 'black';
                }

            });
        }

        $scope.editarPagamento = function(ev, item) {
            showConfirmEditarPgto(ev, item);
        };

        /* FIM EDITAR PGTO */

        $scope.imprimir = function() {
            if ($scope.object.codigo) {
                var url;

                Parametro.page({
                    chave: 'layout.relatorios.temp.url'
                }).$promise.then(function(parametro) {
                    var caminhoBase = '/relatorio/frameset?__locale=pt_BR&__format=pdf&__showtitle=false&__report=report/sgi/tesouraria/';

                    if (!parametro || !parametro.valor) {
                        // Se a chave não foi encontrada, ou se o valor for vazio/nulo, consideramos que estamos em ambiente de
                        // desenvolvimento, e geramos a página com base no ambiente local (localhost), assim como era feito
                        // anteriormente.
                        url = window.location.hostname + ":8081/sgi" + caminhoBase;

                    } else {
                        // Se a chave foi encontrada, consideramos que o valor do parâmetro é a url base (no geral, hostname +
                        // caminho de contexto). O resto da URL será gerado dinamicamente, de acordo com as opções escolhidas
                        // pelo usuário.
                        url = parametro.valor + caminhoBase;
                    }

                    url = url.concat("relacao_bancaria.rptdesign");
                    url = url.concat("&codigoRelacaoBancaria=" + $scope.object.codigo);

                    $window.open(url);

                }).catch(function(response) {
                    console.error('crud-critical', 'dialog', response);
                    $scope.action = false;
                    $scope.notification.criticalError(response.status, response.statusText, response.data);
                });


                //                var producao = false; // para testes e desenv. Em produçao, nao esquecer de setar para "true"
                //                var producao = true;
                //
                //                var url = "../../relatorio/frameset?__locale=pt_BR&__format=pdf&__showtitle=false&__report=report/sgi/tesouraria/";
                //
                //                if (!producao) {
                //                    url = 'http://localhost' + ":8081/sgi/relatorio/frameset?__locale=pt_BR&__format=pdf&__showtitle=false&__report=report/sgi/tesouraria/";
                //                }
            }
        };

        $scope.imprimirCheque = function(ev) {
            $scope.action = true;

            function imprimir_cheques(tipo, fn_hide) {
                if (tipo === 'selecionar') {
                    fn_hide();
                    $state.go('relacaobancaria.imprimir_cheques', {
                        objectId: $scope.object.codigo,
                        tipo_cheque: 'cheque'
                    });
                    $scope.action = false;
                } else if (tipo === 'todos') {
                    var relacaoBancariaBaixaBancaria = RelacaoBancaria.imprimircheques({
                        'action': 'chequetodos'
                    }, $scope.object).$promise.then(function(data) {
                        var title = 'Impressão de cheques';
                        var message = 'Cheque(s) enviado(s) para impressão.';
                        $scope.object.impressaoCheque = true;
                        $scope.notification.success(title, message);
                        fn_hide();
                        $scope.action = false;
                    }).catch(function(response) {
                        $scope.action = false;
                        $rootScope.loadingProgress = false;
                        console.error('crud-critical', 'dialog', response);
                        $scope.notification.criticalError(response.status, response.statusText, response.data);
                    });

                }
            }

            function DialogControllerImprimirCheque($scope, $mdDialog, msg, title, tooltip, rb, fn) {
                $scope.object = rb;
                $scope.msg = msg;
                $scope.title = title;
                $scope.tooltip = tooltip;
                $scope.action = false;

                $scope.confirmTodos = function() {
                    $scope.action = true;
                    fn('todos', $scope.hide);
                };

                $scope.confirmCheque = function() {
                    $scope.action = true;
                    fn('selecionar', $scope.hide);
                };

                $scope.hide = function() {
                    $mdDialog.hide();
                };
                $scope.cancel = function() {
                    $mdDialog.cancel();
                };
            }

            $mdDialog.show({
                controller: DialogControllerImprimirCheque,
                locals: {
                    title: 'Confirmar impressão de cheque(s)',
                    msg: 'Confirma a impressão de cheque(s) desta Relação Bancária?',
                    tooltip: 'Imprimir todos o(s) cheque(s) da RB',
                    rb: $scope.object,
                    fn: imprimir_cheques
                },
                templateUrl: 'partials/tesouraria/relacao_bancaria/html/partials/dialog_imprimir_cheque.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true,
                arialabel: 'Imprimir cheque'
            });

        };

        $scope.imprimirCopia = function(ev) {
            $scope.action = true;

            function imprimir_cheques(tipo, fn_hide) {
                if (tipo === 'selecionar') {
                    fn_hide();
                    $state.go('relacaobancaria.imprimir_cheques', {
                        objectId: $scope.object.codigo,
                        tipo_cheque: 'copia'
                    });
                    $scope.action = false;
                } else if (tipo === 'todos') {
                    var relacaoBancariaBaixaBancaria = RelacaoBancaria.imprimircheques({
                            'action': 'chequetodoscopia'
                        }, $scope.object)
                        .$promise.then(function(data) {
                            var title = 'Impressão de cheques';
                            var message = 'Cheque(s) enviado(s) para impressão.';
                            $scope.notification.success(title, message);

                            fn_hide();
                            $scope.action = false;
                        }).catch(function(response) {
                            $scope.action = false;
                            $rootScope.loadingProgress = false;
                            console.error('crud-critical', 'dialog', response);
                            $scope.notification.criticalError(response.status, response.statusText, response.data);
                        });

                }
            }

            function DialogControllerImprimirCheque($scope, $mdDialog, msg, title, tooltip, rb, fn) {
                $scope.object = rb;
                $scope.msg = msg;
                $scope.title = title;
                $scope.tooltip = tooltip;
                $scope.action = false;

                $scope.confirmTodos = function() {
                    $scope.action = true;
                    fn('todos', $scope.hide);
                };

                $scope.confirmCheque = function() {
                    $scope.action = true;
                    fn('selecionar', $scope.hide);
                };

                $scope.hide = function() {
                    $mdDialog.hide();
                };
                $scope.cancel = function() {
                    $mdDialog.cancel();
                };
            }

            $mdDialog.show({
                controller: DialogControllerImprimirCheque,
                locals: {
                    title: 'Confirma a impressão da cópia',
                    msg: 'Confirma a impressão da cópia do(s) cheque(s) desta Relação Bancária?',
                    tooltip: 'Imprimir cópia de todos o(s) cheque(s) da RB',
                    rb: $scope.object,
                    fn: imprimir_cheques
                },
                templateUrl: 'partials/tesouraria/relacao_bancaria/html/partials/dialog_imprimir_cheque_copia.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true,
                arialabel: 'Imprimir cheque'
            });
        };

        $scope.imprimirEndosso = function(ev) {
            function imprimir_cheques(tipo, fn_hide) {
                $scope.action = true;
                if (tipo === 'selecionar') {
                    fn_hide();
                    $state.go('relacaobancaria.imprimir_cheques', {
                        objectId: $scope.object.codigo,
                        tipo_cheque: 'endosso'
                    });
                    $scope.action = false;
                } else if (tipo === 'todos') {
                    var relacaoBancariaBaixaBancaria = RelacaoBancaria.imprimircheques({
                        'action': 'chequetodosendosso'
                    }, $scope.object).$promise.then(function(data) {
                        var title = 'Impressão de cheques';
                        var message = 'Cheque(s) enviado(s) para impressão.';
                        $scope.notification.success(title, message);

                        fn_hide();
                        $scope.object.impressaoEndosso = true;
                        $scope.action = false;
                    }).catch(function(response) {
                        $scope.action = false;
                        $rootScope.loadingProgress = false;
                        console.error('crud-critical', 'dialog', response);
                        $scope.notification.criticalError(response.status, response.statusText, response.data);
                    });

                }
            }

            function DialogControllerImprimirCheque($scope, $mdDialog, msg, title, tooltip, rb, fn) {
                $scope.object = rb;
                $scope.msg = msg;
                $scope.title = title;
                $scope.tooltip = tooltip;
                $scope.action = false;

                $scope.confirmTodos = function() {
                    $scope.action = true;
                    fn('todos', $scope.hide);
                };

                $scope.confirmCheque = function() {
                    $scope.action = true;
                    fn('selecionar', $scope.hide);
                };

                $scope.hide = function() {
                    $mdDialog.hide();
                };
                $scope.cancel = function() {
                    $mdDialog.cancel();
                };
            }

            $mdDialog.show({
                controller: DialogControllerImprimirCheque,
                locals: {
                    title: 'Confirma a impressão de endosso',
                    msg: 'Confirma a impressão de endosso do(s) cheque(s) desta Relação Bancária?',
                    tooltip: 'Imprimir todos o(s) endosso(s) do(s) cheque(s) da RB',
                    rb: $scope.object,
                    fn: imprimir_cheques
                },
                templateUrl: 'partials/tesouraria/relacao_bancaria/html/partials/dialog_imprimir_cheque_copia.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true,
                arialabel: 'Imprimir cheque'
            });
        };

        $scope.realizarBaixaBancaria = function() {
            $state.go('relacaobancaria.baixa', {
                objectId: $scope.object.codigo
            });
        };

        $scope.conciliarRB = function() {
            $state.go('relacaobancaria.manual', {
                objectId: $scope.object.codigo
            });
        };

        $scope.estornoBaixaRB = function() {
            $state.go('relacaobancaria.estornomanual', {
                objectId: $scope.object.codigo,
            });
        };

        $scope.exibirDetalheContas = function(item, ev) {
            var useFullScreen = true;
            var codigoConta = undefined;

            if (item.dadosBancarios !== undefined && item.dadosBancarios.codigo !== undefined) {
                codigoConta = item.dadosBancarios.codigo;
            } else if (item.bancoSelecionado !== undefined && item.bancoSelecionado.codigo !== undefined) {
                codigoConta = item.bancoSelecionado.codigo;
            }

            if (codigoConta !== undefined) {
                $scope.listaDadosBancarios = DadosBancarios.popup({
                    id: item.credor.codigo
                });

                $mdDialog.show({
                    controller: DialogControllerContas,
                    locals: {
                        listaDadosBancarios: $scope.listaDadosBancarios,
                        contaSelecionada: codigoConta,
                        statusRB: $scope.object.status
                    },
                    templateUrl: 'js/util/templates/detalhecontas.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: true,
                    fullscreen: useFullScreen,
                    arialabel: 'Detalhe pessoa'
                });
            }
        };

        function DialogControllerPessoa($scope, $mdDialog, pessoa, titulo) {
            $scope.pessoa = pessoa;
            $scope.titulo = titulo;

            $scope.fechar = function() {
                $mdDialog.cancel();
            };
        }

        $scope.exibirDetalhePessoa = function(credor, ev) {
            var useFullScreen = true;

            var pessoa = PessoaJuridica.popup({
                id: credor.codigo,
                tipo: credor.tipoPessoa
            });

            $mdDialog.show({
                controller: DialogControllerPessoa,
                locals: {
                    pessoa: pessoa,
                    titulo: 'Credor / Favorecido'
                },
                templateUrl: 'partials/tesouraria/relacao_bancaria/html/partials/dialog_detalhepessoa.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true,
                fullscreen: useFullScreen,
                arialabel: 'Credor / Favorecido'
            });
        };

        $scope.exibirListaPagamentos = function(item, ev) {
            var useFullScreen = true;

            $mdDialog.show({
                controller: DialogControllerListaPagamentos,
                locals: {
                    pagamentos: item.itemRelacao.listaPagamentos,
                    titulo: 'Pagamentos'
                },
                templateUrl: 'partials/tesouraria/relacao_bancaria/html/partials/dialog_listapagamentos.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true,
                fullscreen: useFullScreen,
                arialabel: 'Pagamentos'
            });
        };

		function DialogControllerListaPagamentos($scope, $mdDialog, pagamentos, titulo) {
			$scope.pagamentos = pagamentos;
			$scope.titulo = titulo;

			$scope.fechar = function() {
				$mdDialog.cancel();
			};
		}

        function DialogControllerContas($scope, $mdDialog, listaDadosBancarios, contaSelecionada, statusRB) {
            //criada esta variavel abaixo para exibir somente o dado bancario que está homologado, se e somente se a RB = Finalizada.
            $scope.exibeSimples = false;
            listaDadosBancarios.$promise.then(function(data) {
                $scope.listaDadosBancarios = data;

                angular.forEach(data, function(value) {
                    if (value.codigo === contaSelecionada) {
                        $scope.contaSelecionada = value.codigo;
                    }
                });
            });

            //criada esta verificação para caso a RB esteja Finalizada, exibe os dados bancários somente da conta homologada, uma tabela mais simples. (variavel "exibeSimples")
            if (statusRB === 'Finalizada') {
                $scope.exibeSimples = true;
            }

            $scope.fechar = function() {
                $mdDialog.cancel();
            };
        };

        $scope.expandedResume = function(item, expand, lista) {
            angular.forEach(lista, function(value) {
                value.expanded = false;
            });
            item.expanded = expand;
        };

        $scope.listaResumo = [{
                id: 1,
                nome: "Boleto Banco Itaú",
                qtd: "0",
                valor: "0,00",
                tipo: ""
            },
            {
                id: 2,
                nome: "Boleto outros bancos",
                qtd: "0",
                valor: "0,00",
                tipo: ""
            },
            {
                id: 3,
                nome: "Concessionárias",
                qtd: "0",
                valor: "0,00",
                tipo: ""
            },
            {
                id: 4,
                nome: "Depósito/transferência - Conta corrente",
                qtd: "0",
                valor: "0,00",
                tipo: ""
            },
            {
                id: 5,
                nome: "Depósito/transferência - Conta poupança",
                qtd: "0",
                valor: "0,00",
                tipo: ""
            },
            {
                id: 6,
                nome: "Depósito/transferência - Conta salário",
                qtd: "0",
                valor: "0,00",
                tipo: ""
            },
            {
                id: 7,
                nome: "TED",
                qtd: "0",
                valor: "0,00",
                tipo: ""
            },
            {
                id: 8,
                nome: "DOC",
                qtd: "0",
                valor: "0,00",
                tipo: ""
            },
            {
                id: 9,
                nome: "Guia",
                qtd: "0",
                valor: "0,00",
                tipo: ""
            },
            {
                id: 10,
                nome: "Outros",
                qtd: "0",
                valor: "0,00",
                tipo: ""
            },
            {
                id: 11,
                nome: "DDA",
                qtd: "0",
                valor: "0,00",
                tipo: ""
            }
        ];


        function montarResumo(relacaoBancaria) {

            var listaBancoItau = [];
            var qtdBancoItau = 0;
            var valorBancoItau = 0;

            var listaOutroBanco = [];
            var qtdOutroBanco = 0;
            var valorOutroBanco = 0;

            var listaConcessionaria = [];
            var qtdConcessionaria = 0;
            var valorConcessionaria = 0;

            var listaDepositoCC = [];
            var qtdDepositoCC = 0;
            var valorDepositoCC = 0;

            var listaDepositoCP = [];
            var qtdDepositoCP = 0;
            var valorDepositoCP = 0;

            var listaDepositoCS = [];
            var qtdDepositoCS = 0;
            var valorDepositoCS = 0;

            var listaTed = [];
            var qtdTed = 0;
            var valorTed = 0;

            var listaDoc = [];
            var qtdDoc = 0;
            var valorDoc = 0;

            var listaGuia = [];
            var qtdGuia = 0;
            var valorGuia = 0;

            var listaOutros = [];
            var qtdOutros = 0;
            var valorOutros = 0;

            var listaDda = [];
            var qtdDda = 0;
            var valorDda = 0;

            angular.forEach(relacaoBancaria.relacaoBancariaPagamentosVO, function(value) {
                if ("Boleto" === value.tipoDocumentoPagamento) {

                    //Banco Itaú
                    if (value.bancoSelecionado && value.bancoSelecionado.codigo === 341) {
                        listaBancoItau.push(value);
                        qtdBancoItau = qtdBancoItau + value.qtdadePagamentos;
                        valorBancoItau = valorBancoItau + value.total;
                    } else {
                        listaOutroBanco.push(value);
                        qtdOutroBanco = qtdOutroBanco + value.qtdadePagamentos;
                        valorOutroBanco = valorOutroBanco + value.total;
                    }
                }
                if ("Concessionária" === value.tipoDocumentoPagamento) {
                    listaConcessionaria.push(value);
                    qtdConcessionaria = qtdConcessionaria + value.qtdadePagamentos;
                    valorConcessionaria = valorConcessionaria + value.total;
                }
                if ("Depósito em conta/Transferência" === value.tipoDocumentoPagamento) {
                    if (value.listDadosBancarios && value.listDadosBancarios.length > 0) {
                        if (value.listDadosBancarios[0].tipoConta === "Conta corrente") {
                            listaDepositoCC.push(value);
                            qtdDepositoCC = qtdDepositoCC + value.qtdadePagamentos;
                            valorDepositoCC = valorDepositoCC + value.total;
                        }
                        if (value.listDadosBancarios[0].tipoConta === "Conta poupança") {
                            listaDepositoCP.push(value);
                            qtdDepositoCP = qtdDepositoCP + value.qtdadePagamentos;
                            valorDepositoCP = valorDepositoCP + value.total;
                        }
                        if (value.listDadosBancarios[0].tipoConta === "Conta salário") {
                            listaDepositoCS.push(value);
                            qtdDepositoCS = qtdDepositoCS + value.qtdadePagamentos;
                            valorDepositoCS = valorDepositoCS + value.total;
                        } else if (value.listDadosBancarios[0].tipoConta === undefined) {
                            listaOutros.push(value);
                            qtdOutros = qtdOutros + value.qtdadePagamentos;
                            valorOutros = valorOutros + value.total;
                        }
                    } else {
                        // FIXME: Quando não tem listDadosBancarios, "jogando" em conta correte.
                        listaDepositoCC.push(value);
                        qtdDepositoCC = qtdDepositoCC + value.qtdadePagamentos;
                        valorDepositoCC = valorDepositoCC + value.total;
                    }
                }
                if ("TED" === value.tipoDocumentoPagamento) {
                    listaTed.push(value);
                    qtdTed = qtdTed + value.qtdadePagamentos;
                    valorTed = valorTed + value.total;
                }
                if ("DOC" === value.tipoDocumentoPagamento) {
                    listaDoc.push(value);
                    qtdDoc = qtdDoc + value.qtdadePagamentos;
                    valorDoc = valorDoc + value.total;
                }
                if ("Guia" === value.tipoDocumentoPagamento) {
                    listaGuia.push(value);
                    qtdGuia = qtdGuia + value.qtdadePagamentos;
                    valorGuia = valorGuia + value.total;
                }
                if ("Outros" === value.tipoDocumentoPagamento) {
                    listaOutros.push(value);
                    qtdOutros = qtdOutros + value.qtdadePagamentos;
                    valorOutros = valorOutros + value.total;
                }
                if ("DDA" === value.tipoDocumentoPagamento) {
                    listaDda.push(value);
                    qtdDda = qtdDda + value.qtdadePagamentos;
                    valorDda = valorDda + value.total;
                }
            });

            $scope.listaResumo[0].valor = valorBancoItau;
            $scope.listaResumo[0].qtd = qtdBancoItau;
            $scope.listaResumo[0].relacaoBancariaPagamentosVO = listaBancoItau;
            $scope.listaResumo[0].expanded = false;

            $scope.listaResumo[1].valor = valorOutroBanco;
            $scope.listaResumo[1].qtd = qtdOutroBanco;
            $scope.listaResumo[1].relacaoBancariaPagamentosVO = listaOutroBanco;
            $scope.listaResumo[1].expanded = false;

            $scope.listaResumo[2].valor = valorConcessionaria;
            $scope.listaResumo[2].qtd = qtdConcessionaria;
            $scope.listaResumo[2].relacaoBancariaPagamentosVO = listaConcessionaria;
            $scope.listaResumo[2].expanded = false;

            $scope.listaResumo[3].valor = valorDepositoCC;
            $scope.listaResumo[3].qtd = qtdDepositoCC;
            $scope.listaResumo[3].relacaoBancariaPagamentosVO = listaDepositoCC;
            $scope.listaResumo[3].expanded = false;

            $scope.listaResumo[4].valor = valorDepositoCP;
            $scope.listaResumo[4].qtd = qtdDepositoCP;
            $scope.listaResumo[4].relacaoBancariaPagamentosVO = listaDepositoCP;
            $scope.listaResumo[4].expanded = false;

            $scope.listaResumo[5].valor = valorDepositoCS;
            $scope.listaResumo[5].qtd = qtdDepositoCS;
            $scope.listaResumo[5].relacaoBancariaPagamentosVO = listaDepositoCS;
            $scope.listaResumo[5].expanded = false;

            $scope.listaResumo[6].valor = valorTed;
            $scope.listaResumo[6].qtd = qtdTed;
            $scope.listaResumo[6].relacaoBancariaPagamentosVO = listaTed;
            $scope.listaResumo[6].expanded = false;

            $scope.listaResumo[7].valor = valorDoc;
            $scope.listaResumo[7].qtd = qtdDoc;
            $scope.listaResumo[7].relacaoBancariaPagamentosVO = listaDoc;
            $scope.listaResumo[7].expanded = false;

            $scope.listaResumo[8].valor = valorGuia;
            $scope.listaResumo[8].qtd = qtdGuia;
            $scope.listaResumo[8].relacaoBancariaPagamentosVO = listaGuia;
            $scope.listaResumo[8].expanded = false;

            $scope.listaResumo[9].valor = valorOutros;
            $scope.listaResumo[9].qtd = qtdOutros;
            $scope.listaResumo[9].relacaoBancariaPagamentosVO = listaOutros;
            $scope.listaResumo[9].expanded = false;

            $scope.listaResumo[10].valor = valorDda;
            $scope.listaResumo[10].qtd = qtdDda;
            $scope.listaResumo[10].relacaoBancariaPagamentosVO = listaDda;
            $scope.listaResumo[10].expanded = false;

        }

        $scope.downloadComprovante = function(item) {
            RelacaoBancaria.baixarComprovanteExiste({
                id: item.codigo
            }).$promise.then(function(data) {
                $window.location = '/sgi/nv/rest/tesouraria/relacaobancaria/baixarComprovante/' + item.codigo;
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
                $rootScope.loadingProgress = false;
            });
        };

        $scope.top = function() {
            var tela = $('md-content');
            tela.prop("scrollTop", tela.prop("scrollHeight"));
        };

        $scope.bottom = function() {
            $("md-content").scrollTop(0);
        };

        function DialogControllerPagamento($scope, $mdDialog, pagamento) {
            $scope.pagamento = pagamento;

            $scope.fechar = function() {
                $mdDialog.cancel();
            };
        }

        $scope.exibirDetalhePagamento = function(itemRelacaoVO, ev) {
            let tipo = itemRelacaoVO.itemRelacao.listaPgtos[0].tipo;
            let codigo = itemRelacaoVO.itemRelacao.listaPgtos[0].codigo;

            if (tipo === undefined) {
                tipo = 'ninformado';
            }
            Pagamento.getCodigoNota({
                id: codigo
            }).$promise.then((parcelaNota) => {
                Pagamento.popup({
                    id: codigo,
                    tipo: tipo,
                    codNota: parcelaNota.codigo === undefined ? 0 : parcelaNota.codigo
                }).$promise.then((pagamentoTO) => {
                    $mdDialog.show({
                        controller: DialogControllerPagamento,
                        locals: {
                            pagamento: pagamentoTO
                        },
                        templateUrl: 'js/util/templates/detalhepagamento.html',
                        parent: angular.element(document.body),
                        targetEvent: ev,
                        clickOutsideToClose: true,
                        fullscreen: true,
                        arialabel: 'Detalhe pagamento'
                    });
                });
            });
        };

		//Habilita o botão de Estorno Baixa Manual, de acordo com as regras do issue SGINV-2950
        $scope.habilitarEstornoBaixa = function() {

        	let habilitar = false;
        	//Verificar se não houve não possui comprovante e se não houve estorno manual.
        	angular.forEach($scope.object.relacaoBancariaPagamentosVO, function(vo, key) {
//        	console.log('vo.retornoBancario.identificacaoErro: ', vo.retornoBancario.identificacaoErro)
				if (
					(vo.caminhoComprovante == undefined || vo.caminhoComprovante == null)
					&& (vo.retornoBancario != undefined && vo.retornoBancario != null && (vo.retornoBancario.identificacaoErro != undefined && !vo.retornoBancario.identificacaoErro.includes('SGI')) )
				)
					habilitar = true;
			});
//			console.log('habilitar: ', habilitar);
//			console.log('this.possuiPerfilOperadorTesouraria: ', this.possuiPerfilOperadorTesouraria);
//			console.log('$scope.object.meioPagamento == \'Internet banking\': ', $scope.object.meioPagamento == 'Internet banking');
//			console.log('$scope.object.status == \'Finalizada\': ', $scope.object.status == 'Finalizada');
//			console.log('($scope.object.colaboradorEstornoBaixaManual != undefined && $scope.object.colaboradorEstornoBaixaManual != null): ', ($scope.object.colaboradorEstornoBaixaManual != undefined && $scope.object.colaboradorEstornoBaixaManual != null));
//			console.log('($scope.object.dataEstornoBaixaManual != undefined && $scope.object.dataEstornoBaixaManual != null): ', ($scope.object.dataEstornoBaixaManual != undefined && $scope.object.dataEstornoBaixaManual != null));
//			console.log('');

        	let resultado =
        		habilitar
        		&& this.possuiPerfilOperadorTesouraria
        		&& ($scope.object.meioPagamento == 'Internet banking')
        		&& ($scope.object.status == 'Finalizada')
        		&& ($scope.object.colaboradorEstornoBaixaManual != undefined && $scope.object.colaboradorEstornoBaixaManual != null)
        		&& ($scope.object.dataEstornoBaixaManual != undefined && $scope.object.dataEstornoBaixaManual != null)
        		;

        	return resultado;
        }

        init();

    }

})();
