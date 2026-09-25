(function() {
    'use strict';

    function verificarFaltaDadosBancariosEnvio($scope, pagamentosVO, opcoes, callback) {
        var pagamentoIncompleto = null;
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
                $scope.notification.error("", "O favorecido do pagamento destacado em vermelho na " +
                    (pagamentoIncompleto.numeroLinha) +
                    "ª linha não possui dado(s) bancário(s) cadastrado(s).");
            }
        }

        $scope.object.faltamDadosBancarios = !!pagamentoIncompleto;
    }

    angular
        .module('app.relacaobancaria')
        .controller('ConciliacaoBancariaCtrl', conciliacaoBancariaController);

    function conciliacaoBancariaController($scope, $state, $stateParams, $mdDialog, $rootScope, RelacaoBancaria) {

        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;
        $scope.statusPagamentoCombo = 'Pago';

        $scope.todo_motivo_estorno = false;


        function init_valores_total() {
            $scope.total = 0;
            $scope.total_pago = 0;
            $scope.total_estornado = 0;
        }

        init_valores_total();

        $scope.listSituacaoes = ['Pago', 'Estornado'];

        $scope.action = false;
        $scope.baixaBancariaHabilitado = false;

        function modificarStatusPagamentos(data, status) {
            angular.forEach(data.relacaoBancariaPagamentosVO, function(item) {
                item.statusConciliacaoBancaria = status;
            });
            calcularValores();
            verificarSituacaoPagamento();
        }

        function calcularValores() {
            if ($scope.object != null) {
                init_valores_total();
                angular.forEach($scope.object.relacaoBancariaPagamentosVO, function(item) {
                    $scope.total = $scope.total + angular.copy(item.valorAPagar);
                    if ("Estornado" === item.statusConciliacaoBancaria) {
                        $scope.total_estornado = $scope.total_estornado + angular.copy(item.valorAPagar);
                    } else if ("Pago" === item.statusConciliacaoBancaria) {
                        $scope.total_pago = $scope.total_pago + angular.copy(item.valorAPagar);
                    }
                });
            }
        }
        $scope.$on('changeStatusPagamento', function() {
            changeStatusPagamento();
        });

        function changeStatusPagamento() {
            if ("Estornado" === $scope.statusPagamentoCombo) {
                $scope.todo_motivo_estorno = true;
            } else {
                $scope.todo_motivo_estorno = false;
                angular.forEach($scope.object.relacaoBancariaPagamentosVO, function(value) {
                    delete value.retornoBancario;
                });
            }
            modificarStatusPagamentos($scope.object, $scope.statusPagamentoCombo);
        }

        function verificarSituacaoPagamento() {
            if ($scope.object.relacaoBancariaPagamentosVO && $scope.object.relacaoBancariaPagamentosVO.length > 0) {
                var tamanhoLista = $scope.object.relacaoBancariaPagamentosVO.length;
                var numeroPagamentoComSituacao = 0;

                angular.forEach($scope.object.relacaoBancariaPagamentosVO, function(item) {
                    if (item.statusConciliacaoBancaria) {
                        numeroPagamentoComSituacao++;
                    }
                });

                if (numeroPagamentoComSituacao === tamanhoLista) {
                    $scope.baixaBancariaHabilitado = true;
                } else {
                    $scope.baixaBancariaHabilitado = false;
                }
            }
        }

        function loadObj() {
            $scope.object = null;

            RelacaoBancaria.get({
                id: $stateParams.objectId
            }).$promise.then(function(data) {

                $scope.object = data;

                // Carregar os itens separadamente.
                RelacaoBancaria.obterListaDeItensRelacao({
                    id: $stateParams.objectId
                }).$promise.then(function(data_itens) {
                    $scope.object.relacaoBancariaPagamentosVO = data_itens;

                    verificarFaltaDadosBancariosEnvio($scope, $scope.object.relacaoBancariaPagamentosVO, function(value) {
                        $scope.object.qtdPagamentos = value.qtdadePagamentos + $scope.object.qtdPagamentos;
                        $scope.object.total = $scope.object.total + value.valorAPagar;
                    });

                    calcularValores();

                    modificarStatusPagamentos($scope.object, 'Pago');

                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'load', response);
                    $rootScope.loadingProgress = false;
                });

            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
                $rootScope.loadingProgress = false;
            });
        }

        $scope.cancelar = function() {
            $state.go("relacaobancaria.view", {
                objectId: $scope.object.codigo
            });
        };

        $scope.realizarBaixaBancaria = function() {
            var realizar = true;
            angular.forEach($scope.object.relacaoBancariaPagamentosVO, function(item) {
                if (item.statusConciliacaoBancaria === "Estornado" && (!item.retornoBancario || !item.retornoBancario.codigo)) {
                    if (item.numero !== '-') {
                        $scope.notification.atencao("Estorno não informado", "É necessário informar o motivo de estorno do pagamento " + item.numero);
                    } else {
                        $scope.notification.atencao("Estorno não informado", "É necessário informar o motivo de estorno dos pagamentos do fornecedor " + item.credor.nome);
                    }
                    realizar = false;
                }
            });

            if (realizar) {
                showConfirmBaixaBancaria();
            }
        };

        /* INICIO AÇÃO BAIXA BANCARIA MANUAL*/
        var confirmBaixaBancariaFn = function(fn_hide, action) {
            var relacaoBancariaBaixaBancaria = RelacaoBancaria.realizarBaixaBancaria(null, $scope.object).$promise;
            action = true;
            relacaoBancariaBaixaBancaria.then(function(data) {
                var title = 'Realizar baixa bancária';
                var message = 'Baixa bancária realizada com sucesso';
                $scope.notification.success(title, message);

                fn_hide();
                $state.go("relacaobancaria.view", {
                    objectId: $scope.object.codigo
                });
                action = false;
            }).catch(function(response) {
                $scope.action = false;
                action = false;
                $rootScope.loadingProgress = false;
                console.error('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
            });
        };

        function showConfirmBaixaBancaria() {
            $mdDialog.show({
                controller: DialogControllerRealizarBaixaBancaria,
                locals: {
                    fn: confirmBaixaBancariaFn
                },
                templateUrl: 'partials/tesouraria/relacao_bancaria/html/partials/dialog_realizar_baixa_bancaria.html',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                fullscreen: false
            });
        }

        function DialogControllerRealizarBaixaBancaria($scope, $mdDialog, fn) {
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
        /* FIM AÇÃO BAIXA MANUAL */

        $scope.changePagamento = function(item) {
            delete item.retornoBancario;
            calcularValores();
            verificarSituacaoPagamento();
        };

        $scope.adicionarMotivoEstorno = function(ev, item) {
            RelacaoBancaria.retornobancarioestornado(null).$promise.then(function(data) {

                motivoEstorno(ev, item, data, fnConfirmarItem);

            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });
        };

        $scope.adicionarMotivoEstornoTodos = function(ev) {
            RelacaoBancaria.retornobancarioestornado(null).$promise.then(function(data) {

                motivoEstorno(ev, {}, data, fnConfirmarTodosItem);

            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });
        };

        function fnConfirmarTodosItem(object, fn_hide) {
            angular.forEach($scope.object.relacaoBancariaPagamentosVO, function(value) {
                value.retornoBancario = angular.copy(object.retornoBancario);
            });
            fn_hide();
        }

        function fnConfirmarItem(object, fn_hide) {
            fn_hide();
        }

        function motivoEstorno(event, item, lista, fnConfirmar) {
            $mdDialog.show({
                controller: DialogControllerMotivoEstorno,
                locals: {
                    $mdDialog: $mdDialog,
                    titulo: "Motivos de estorno - Relação Bancária",
                    item: item,
                    lista: lista,
                    fnConfirmar: fnConfirmar
                },
                templateUrl: 'partials/tesouraria/relacao_bancaria/html/partials/motivo_estorno.html',
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: false,
                fullscreen: false
            });
        }

        function DialogControllerMotivoEstorno($scope, $mdDialog, titulo, item, lista, fnConfirmar) {
            $scope.titulo = titulo;
            $scope.object = item;
            $scope.listMotivos = lista;

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };

            $scope.confirm = function() {
                fnConfirmar($scope.object, $scope.hide);
            };
        }

        loadObj();
    }

    function conciliacaoManualController($scope, $state, $stateParams, $mdDialog, $rootScope, RelacaoBancaria) {

        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;
        $scope.selected = [];


        $scope.isIndeterminate = function() {
            return ($scope.selected.length !== 0 &&
                $scope.selected.length !== $scope.object.relacaoBancariaPagamentosVO.length);
        };

        $scope.exists = function(item, list) {
            return list.indexOf(item) > -1;
        };

        $scope.toggle = function(item, list) {
            var idx = list.indexOf(item);
            if (idx > -1) {
                item.selecionado = false;
                list.splice(idx, 1);
            } else {
                item.selecionado = true;
                list.push(item);
            }
        };

        $scope.isChecked = function() {
            if ($scope.object != null && $scope.object.relacaoBancariaPagamentosVO != null) {
                return $scope.selected.length === $scope.object.relacaoBancariaPagamentosVO.length;
            }
        };

        $scope.toggleAll = function() {
            if ($scope.selected.length === $scope.object.relacaoBancariaPagamentosVO.length) {
                $scope.selected = [];
            } else if ($scope.selected.length === 0 || $scope.selected.length > 0) {
                $scope.selected = $scope.object.relacaoBancariaPagamentosVO.slice(0);
            }
        };

        $scope.action = false;

        function loadObj() {
            $scope.object = null;
            RelacaoBancaria.get({
                id: $stateParams.objectId
            }).$promise.then(function(data) {

                $scope.object = angular.copy(data);

                // Carregar os itens separadamente.
                RelacaoBancaria.obterListaDeItensRelacao({
                    id: $stateParams.objectId
                }).$promise.then(function(data_itens) {
                    $scope.object.relacaoBancariaPagamentosVO = data_itens;
                    angular.forEach($scope.object.relacaoBancariaPagamentosVO, function(value) {
                        $scope.object.qtdPagamentos = value.qtdadePagamentos + $scope.object.qtdPagamentos;
                    });
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'load', response);
                    $rootScope.loadingProgress = false;
                });

            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
                $rootScope.loadingProgress = false;
            });
        }

        $scope.cancelar = function() {
            $state.go("relacaobancaria.view", {
                objectId: $scope.object.codigo
            });
        };

        $scope.realizarBaixaManual = function() {
            if ($scope.object.motivoBaixaManual != null && $scope.object.motivoBaixaManual.trim() !== '') {
                $scope.showConfirmBaixaManual();
            } else {
                $scope.notification.error("Motivo da baixa manual é um campo de preenchimento obrigatório.");
            }
        };

        /* INICIO AÇÃO BAIXA BANCARIA MANUAL*/
        var confirmBaixaManualFn = function(fn_hide) {
            var relacaoBancariaBaixaBancaria = RelacaoBancaria.baixamanual(null, $scope.object).$promise;

            relacaoBancariaBaixaBancaria.then(function(data) {
                var title = 'Realizar baixa manual';
                var message = 'Baixa manual realizada com sucesso';
                $scope.notification.success(title, message);

                fn_hide();
                $state.go("relacaobancaria.view", {
                    objectId: $scope.object.codigo
                });
            }).catch(function(response) {
                $scope.action = false;
                $rootScope.loadingProgress = false;
                console.error('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
            });
        };

        $scope.showConfirmBaixaManual = function() {
            $mdDialog.show({
                controller: DialogControllerRealizarBaixaBancaria,
                locals: {
                    fn: confirmBaixaManualFn
                },
                templateUrl: 'partials/tesouraria/relacao_bancaria/html/partials/dialog_conciliar_relacao_bancaria.html',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        function DialogControllerRealizarBaixaBancaria($scope, $mdDialog, fn) {

            $scope.confirm = function() {
                fn($scope.hide);
            };

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
        }
        /* FIM AÇÃO BAIXA MANUAL */

        loadObj();
    }

})();
