(function() {
    'use strict';

    angular
        .module('app.movimentoautorizado')
        .controller('MovimentoAutorizadoDetailCtrl', detailController);

    function detailController($scope, $state, $stateParams, $mdDialog, $rootScope, EnumService, MovimentoAutorizado, DadosBancarios, UsuarioLogadoService, DialogConfirm) {

        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;
        $scope.action = false;
        $scope.dadosbancarioList = null;

        $scope.usuario = UsuarioLogadoService.getUsuarioLogado();

        function loadObj() {
            $rootScope.loadingProgress = true;
            $scope.action = true;

            var pMovimento = MovimentoAutorizado.get({
                id: $stateParams.objectId
            }).$promise;

            pMovimento.then(function(data) {
                $scope.object = data;

                $scope.object.infoBoleto = "Linha digitável";
                carregarTipoInformacaoNumeroBoleto();
                carregarListaTipoInforBoleto();
                $scope.exibeLinhaDigitavel = true;

                verificarPerfilHomologador();
                carregarTipoDocumentoPag();

                carregarTipoDocumentoEditar();
                disableCampoComIconeEditar();
                $scope.hideBancos = true;

                if (!isTributo() && $scope.object.dadosBancarios != null) {
                    $scope.hideBancos = false;
                    carregarDadosBancarios();
                }

                $scope.exibirLabelTributo = isTributo();

                $rootScope.loadingProgress = false;
                $scope.action = false;
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
                $rootScope.loadingProgress = false;
                $scope.action = false;
            });
        }

        function carregarTipoDocumentoPag() {

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

            if ($scope.object.tipoDocumentoPagamento === 'Boleto' ||
                $scope.object.tipoDocumentoPagamento === 'Concessionária' ||
                ($scope.object.tipoDocumentoPagamento === 'Guia' &&
                    isTributo())) {

                $scope.disableInfoBoleto = false;
                $scope.disableLDCodBarr = false;

            } else {
                $scope.disableInfoBoleto = true;
                $scope.disableLDCodBarr = true;
                $scope.object.codigoBarras = '';
                $scope.object.linhaDigitavel = '';
            }

        }

        $scope.changeTipoDocPag = function() {
            carregarTipoDocumentoPag();
        };

        function carregarTipoDocumentoEditar() {
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
        }

        function carregarListaTipoInforBoleto() {
            EnumService.enumValues({
                    pacote: 'br.com.araujoabreu.sgi.administracao.enums',
                    classe: 'null',
                    enum: 'TipoInformacaoNumeroBoleto'
                })
                .$promise.then(function(data) {
                    $scope.listTiposBoletos = [];

                    angular.forEach(data, function(value) {
                        $scope.listTiposBoletos.push(value);
                    });
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'load', response);
                });
        }

        function carregarTipoInformacaoNumeroBoleto() {

            if ($scope.object.infoBoleto === 'Linha digitável') {
                $scope.exibeLinhaDigitavel = true;

            } else {
                $scope.exibeLinhaDigitavel = false;
            }
        }

        $scope.changeInfoBoleto = function() {
            carregarTipoInformacaoNumeroBoleto();
        };

        function carregarDadosBancarios() {
            var filtro = {};
            filtro.pessoa = $scope.object.credor;
            $scope.dadosbancarioList = null;

            DadosBancarios.page(filtro).$promise.then(function(data) {

                $scope.dadosbancarioList = data.content;

                angular.forEach($scope.dadosbancarioList, function(value, key) {
                    if (value.codigo === $scope.object.dadosBancarios.codigo) {
                        value.selecao = true;
                        return;
                    } else {
                        value.selecao = false;
                    }
                });

            }).catch(function(response) {
                $scope.$emit('crud-critical', 'contrato load', response);
                $scope.dadosbancarioList = [];
            });
        }

        function isTributo() {
            return ($scope.object.tipo != null && $scope.object.tipo === 'TA') || $scope.object.tipo === 'TR';
        }

        $scope.cancelar = function() {
            $state.go('movimentoautorizado.list', {});
        };

        $scope.salvar = function() {
            $scope.action = true;
            var pUpdate = $scope.object.$update({
                id: $scope.object.codigo
            });

            pUpdate.then(function(data) {
                $scope.notification.success(null, "Dados do pagamento atualizados com sucesso.");
                $state.go('movimentoautorizado.list');
                $scope.action = false;
            }).catch(function(response) {
                $scope.action = false;
                $scope.$emit('crud-critical', 'save', response);
            });
        };

        $scope.changeSelecaoBanco = function(item) {
            angular.forEach($scope.dadosbancarioList, function(value) {
                value.selecao = false;
            });
            $scope.object.dadosBancarios = item;
            item.selecao = true;
        };

        /* Inicio da Dialog Conta Contabil */
        $scope.contaContabil = function(tipoContaContabil) {

            $mdDialog.show({
                controller: DialogContaContabil,
                locals: {
                    movAutorizado: $scope.object,
                    tipoContaContabil: tipoContaContabil,
                    notification: $scope.notification
                },
                templateUrl: 'partials/tesouraria/movimento_autorizado/html/dialogs/dialog_conta_contabil.html',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        function DialogContaContabil($scope, $mdDialog, movAutorizado, tipoContaContabil, notification, AutocompleteService) {

            $scope.title = tipoContaContabil.toLowerCase();
            $scope.contaContabil = null;
            var aux = null;

            function checkListaContaContabil() {

                if (!angular.isUndefined(movAutorizado.listaPgtoCC) && movAutorizado.listaPgtoCC != null) {

                    var contaContabilPorTipo = null;

                    angular.forEach(movAutorizado.listaPgtoCC, function(value, key) {
                        if (value.tipo === tipoContaContabil) {
                            contaContabilPorTipo = value.contaContabil;
                        }
                    })

                    if (contaContabilPorTipo != null) {
                        aux = contaContabilPorTipo;
                        $scope.contaContabil = contaContabilPorTipo;
                    }
                }
            }

            checkListaContaContabil();

            // INICIO DO AUTOCOMPLETE DE CONTA CONTÁBIL
            function autoCompleteContaContabil() {

                $scope.searchTextContaContabil = null;
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

                if ($scope.contaContabil == null) {
                    notification.error("Conta contábil é um campo de preenchimento obrigatório.");

                } else {

                    var jaTemNaLista = false;

                    angular.forEach(movAutorizado.listaPgtoCC, function(value, key) {
                        if (value.tipo === tipoContaContabil) {
                            value.contaContabil = $scope.contaContabil;
                            jaTemNaLista = true;
                            return;
                        }
                    });

                    if (!jaTemNaLista) {
                        var objPagamento = {
                            codigo: movAutorizado.codigo
                        };
                        var objPagamentoContaContabil = {};

                        objPagamentoContaContabil.codigo = null;
                        objPagamentoContaContabil.tipo = tipoContaContabil;
                        objPagamentoContaContabil.pagamento = objPagamento;
                        objPagamentoContaContabil.contaContabil = $scope.contaContabil;

                        movAutorizado.listaPgtoCC.push(objPagamentoContaContabil);
                    }

                    $scope.hide();
                }
            };

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
        } /* Fim da Dialog  Conta Contabil */

        function disableCampoComIconeEditar() {
            var dataPgt = $scope.object.dataPagamento;
            var dataVen = $scope.object.dataVencimento;

            if (isTributo()) {
                if (dataPgt != null && dataVen != null) {

                    $scope.disableField = (moment(dataPgt).endOf("day").isBefore(moment(dataVen).endOf("day")));
                }
            } else {
                $scope.disableField = false;
            }
        }

        $scope.changeValorPagar = function() {
            var multa = $scope.object.valorMulta;
            var juros = $scope.object.valorJuros;
            var descontos = $scope.object.valorDesconto;
            var valueLiquido = $scope.object.valor_liquido;

            var valuePagar = valueLiquido + juros + multa - descontos;

            if (valuePagar <= 0) {

                $scope.object.valorAPagar = 0;
            } else {

                $scope.object.valorAPagar = valuePagar;
            }
        };

        function verificarPerfilHomologador() {
            UsuarioLogadoService.possuiUmPerfilLista('homologacao-manual').$promise.then(function(data) {

                $scope.habilitarHomologBanco = data.possuiUmPerfilLista;

            }).catch(function(response) {
                $rootScope.loadingProgress = false;
                $rootScope.$emit('crud-critical', 'load', response);
            });
        };

        /* Inicio da Dialog Homologação */
        $scope.homologarDadosBancarios = function(item) {
            // Chamada do serviço que criará um dialog de confirmação.
            DialogConfirm.show(
                null,
                item,
                'Homologar manualmente dados bancários: Confirmação',
                'Deseja homologar manualmente estes dados bancários?',
                'Confirmar',
                'Cancelar',
                confirmHomologar,
                cancelHomologar);
        };

        function confirmHomologar(object, fnHide) {
            $scope.action = true;
            object.codigoPagamento = $scope.object.codigo;
            DadosBancarios.homologarDadosBancariosManual(object).$promise.then(function(data) {
                fnHide();
                $scope.dadosbancarioList = null;
                $scope.action = false;
                loadObj();
                $scope.notification.success(null, "Os dados bancários selecionados foram homologados com sucesso.");
            }).catch(function(response) {
                $scope.action = false;
                $scope.$emit('crud-critical', 'dialog', response);
            });
        }

        function cancelHomologar(fnHide) {
            fnHide();
        }
        /*Fim da Dialog Homologação*/

        loadObj();

    }

})();