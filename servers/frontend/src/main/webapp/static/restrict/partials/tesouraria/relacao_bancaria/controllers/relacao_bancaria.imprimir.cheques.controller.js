(function() {
    'use strict';

    angular
        .module('app.relacaobancaria')
        .controller('ImprimirChequesCtrl', impressaoChequeController);

    function impressaoChequeController($scope, $state, $stateParams, $mdDialog, $rootScope, RelacaoBancaria) {

        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;
        $scope.tipoImpressao = $stateParams.tipo_cheque;
        $scope.selected = [];
        $scope.selectAll = false;
        $scope.action = false;

        $scope.isIndeterminate = function() {
            $scope.selectAll = $scope.selected.length !== 0 && $scope.selected.length === $scope.object.relacaoBancariaPagamentosVO.length;
            return ($scope.selected.length !== 0 && $scope.selected.length !== $scope.object.relacaoBancariaPagamentosVO.length);
        };

        $scope.chequeAnteriorChange = function(ev) {
            let cheque = Number($scope.object.chequeAnterior);
            $scope.object.relacaoBancariaPagamentosVO.forEach((item) => {
                if (item.selecionado) {
                    if ($scope.chequeAnteriorOriginal !== $scope.object.chequeAnterior) {
                        cheque = cheque + 1;
                        item.cheque = cheque;
                    }
                }
            });
        };

        $scope.exists = function(item, list) {
            return list.indexOf(item) > -1;
        };

        $scope.toggle = function(item) {
            $scope.selected = [];

            if (item.selecionado) {
                item.chequeOriginal = item.cheque;
            } else {
                item.cheque = Number(item.chequeOriginal);
            }

            let cheque = Number($scope.object.chequeAnterior);

            $scope.object.relacaoBancariaPagamentosVO.forEach((value) => {
                if (value.selecionado) {
                    if ($scope.chequeAnteriorOriginal !== $scope.object.chequeAnterior) {
                        cheque = cheque + 1;
                        value.cheque = cheque;
                    }
                    $scope.selected.push(item);
                }
            });

            $scope.isIndeterminate();
        };

        $scope.isChecked = function() {
            if ($scope.object != null && $scope.object.relacaoBancariaPagamentosVO != null) {
                return $scope.selected.length === $scope.object.relacaoBancariaPagamentosVO.length;
            }
        };

        $scope.toggleAll = function() {
            let chequeAnterior = Number($scope.object.chequeAnterior);
            $scope.selectAll = !$scope.selectAll;
            $scope.selected = [];
            $scope.object.relacaoBancariaPagamentosVO.forEach((value) => {
                if ($scope.selectAll) {
                    $scope.selected.push(value);
                    value.chequeOriginal = value.cheque;
                    value.selecionado = true;
                    chequeAnterior = chequeAnterior + 1;
                    value.cheque = chequeAnterior;
                } else {
                    $scope.selectAll = false;
                    value.selecionado = false;
                    value.cheque = Number(value.chequeOriginal);
                    $scope.selected = [];
                }
            });
            $scope.isIndeterminate();
        };

        function loadObj() {
            $scope.object = null;
            RelacaoBancaria.get({
                    id: $stateParams.objectId
                }).$promise
                .then(function(data) {
                    $scope.object = angular.copy(data);
                    $scope.chequeAnteriorOriginal = $scope.object.chequeAnterior;
                })
                .then(response => {
                    // Carregar os itens separadamente.
                    RelacaoBancaria.obterListaDeItensRelacao({
                            id: $stateParams.objectId
                        }).$promise
                        .then(function(data_itens) {
                            $scope.object.relacaoBancariaPagamentosVO = data_itens;
                            angular.forEach($scope.object.relacaoBancariaPagamentosVO, function(value) {
                                $scope.object.qtdPagamentos = value.qtdadePagamentos + $scope.object.qtdPagamentos;
                                $scope.object.total = $scope.object.total + value.valorAPagar;
                            });
                        }).catch(function(response) {
                            $scope.$emit('crud-critical', 'load', response);
                            $rootScope.loadingProgress = false;
                        });
                })
                .catch(function(response) {
                    $scope.$emit('crud-critical', 'load', response);
                    $rootScope.loadingProgress = false;
                });
        }

        $scope.cancelar = function() {
            $state.go("relacaobancaria.view", {
                objectId: $scope.object.codigo
            });
        };

        $scope.imprimirCheque = function() {
            $scope.showConfirmImprimirCheque();
        };

        /* INICIO AÇÃO BAIXA BANCARIA MANUAL*/
        var confirmChequesFn = function(fn_hide) {
            RelacaoBancaria.imprimircheques({
                'action': $scope.tipoImpressao
            }, $scope.object).$promise.then(function(data) {
                var title = 'Impressão de cheques';
                var message = 'Cheque(s) enviado(s) para impressão.';
                $scope.notification.success(title, message);
                $state.go('relacaobancaria.view', {
                    objectId: $stateParams.objectId
                });
                fn_hide();
            }).catch(function(response) {
                $scope.action = false;
                fn_hide();
                $rootScope.loadingProgress = false;
                console.error('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
            });
        };

        $scope.showConfirmImprimirCheque = function() {
            var msg = "";
            if ($scope.tipoImpressao === 'cheque') {
                if ($scope.selected.length > 1) {
                    msg = "Deseja realmente imprimir o(s) " + $scope.selected.length + " cheque(s) selecionado(s)?";
                } else if ($scope.selected.length == 1) {
                    msg = "Deseja realmente imprimir o cheque selecionado?";
                }
            } else if ($scope.tipoImpressao === 'endosso') {
                if ($scope.selected.length > 1) {
                    msg = "Deseja realmente imprimir o endosso do(s) " + $scope.selected.length + " cheque(s) selecionado(s)?";
                } else if ($scope.selected.length == 1) {
                    msg = "Deseja realmente imprimir o endosso do cheque selecionado?";
                }
            } else if ($scope.tipoImpressao === 'copia') {
                if ($scope.selected.length > 1) {
                    msg = "Deseja realmente imprimir a cópia do(s) " + $scope.selected.length + " cheque(s) selecionado(s)?";
                } else if ($scope.selected.length == 1) {
                    msg = "Deseja realmente imprimir o endosso do cheque selecionado?";
                }
            }

            if ($scope.selected.length >= 1) {
                $mdDialog.show({
                    controller: DialogController,
                    locals: {
                        msg: msg,
                        title: "Relação bancária - Impressão de cheque: Confirmação",
                        fn: confirmChequesFn,
                        selecionados: $scope.selected.length
                    },
                    templateUrl: 'partials/tesouraria/relacao_bancaria/html/partials/dialog_confirm_imprimir_cheque.html',
                    parent: angular.element(document.body),
                    clickOutsideToClose: false,
                    fullscreen: false
                });
            } else {
                $scope.notification.error("Não há cheques selecionados.");
            }
        };

        function DialogController($scope, $mdDialog, msg, title, fn, selecionados) {

            $scope.msg = msg;
            $scope.title = title;
            $scope.action = false;

            $scope.confirm = function() {
                $scope.action = true;
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

        function azul() {

            /*var $scope.object.itensRecebidos = 0;
            var $scope.object.itensNaoRecebidos = 0;


            angular.forEach($scope.gridPesquisa.data, function(value, key) {
            	itens.push(value.itemRelacao);

            	$scope.object.total = $scope.object.total + value.total;
            });*/
        }


        loadObj();
    }

})();