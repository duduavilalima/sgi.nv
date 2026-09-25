(function() {
    'use strict';

    angular
        .module('app.relacaobancaria')

        .controller('ConciliacaoManualCtrl', conciliacaoManualController);

    function conciliacaoManualController($scope, $state, $stateParams, $mdDialog, $rootScope, RelacaoBancaria, DadosBancarios, PessoaJuridica) {

        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;
        $scope.selected = [];
        $scope.estorno = $state.current.data.estorno;

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
                    	if (value.statusPagamento === 'Pago' && $scope.estorno)
                    		value.selecionado = true
                        $scope.object.qtdPagamentos = value.qtdadePagamentos + $scope.object.qtdPagamentos;
                        $scope.object.total = $scope.object.total + value.valorAPagar;
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

        $scope.estornarBaixaManual = function() {
			$scope.showConfirmEstornoBaixaManual();
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

        var confirmEstornoBaixaManualFn = function(fn_hide) {
            var relacaoBancariaBaixaBancaria = RelacaoBancaria.estornobaixamanual(null, $scope.object).$promise;

            relacaoBancariaBaixaBancaria.then(function(data) {
                var title = 'Realizar estorno baixa manual';
                var message = 'Estorno da baixa manual realizada com sucesso';
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

        $scope.showConfirmEstornoBaixaManual = function() {
            $mdDialog.show({
                controller: DialogControllerRealizarBaixaBancaria,
                locals: {
                    fn: confirmEstornoBaixaManualFn
                },
                templateUrl: 'partials/tesouraria/relacao_bancaria/html/partials/dialog_estornar_relacao_bancaria.html',
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

        function DialogControllerPessoa($scope, $mdDialog, pessoa, titulo) {
            $scope.pessoa = pessoa;
            $scope.titulo = titulo;

            $scope.fechar = function() {
                $mdDialog.cancel();
            };
        }
        /* FIM AÇÃO BAIXA MANUAL */

        loadObj();

    }

})();
