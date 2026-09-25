angular.module('app.notaDebito')
    .factory('NotasDebitosService', function() {
        let service = {};

        const catchError = (response) => {
            $rootScope.$emit('crud-critical', 'load', response);
        };

        service.getPercentualAliquota = (argCategoriaTributo) => {
            if (argCategoriaTributo && argCategoriaTributo.aliquotas && argCategoriaTributo.aliquotas[0]) {
                return argCategoriaTributo.aliquotas[0].percentual;
            } else if (argCategoriaTributo && argCategoriaTributo.percentualAliquota) {
                return Number(argCategoriaTributo.percentualAliquota.replace(',','.'));
            } else {
                return 0.00;
            }
        };

        service.calculaValorTributo = (baseCalculo, categoriaTributo, codigoIBGE) => {
            let valorTributo = Number(baseCalculo * (service.getPercentualAliquota(categoriaTributo) / 100));

            if (codigoIBGE && codigoIBGE === 3304557) {
                let valorTributoString = valorTributo.toString(10);
                return Number(valorTributoString.substring(0, valorTributoString.indexOf('.') + 3));
            } else {
                return valorTributo;
            }
        };

        return service;
    })
    .factory('DialogControllerEditarNotaTributoAliquotaNd', function() {
        return function DialogControllerEditarNotaTributoAliquotaNd($scope, $mdDialog, isNew, notaTributoAliquota, NotasDebitosService, NotasFiscaisService, listaNotaTributoAliquota, isISS, Tributo, CategoriaTributo, faturamento, cidadeFaturamento, variacaoTributo, notification, gerenciarVarTabela) {
            $scope.isNew = isNew;
            $scope.faturamentoAliquota = notaTributoAliquota;
            $scope.listaDeTributos = [];
            $scope.listaCategoriaTributo = [];
            $scope.listaFaturamentoAliquota = listaNotaTributoAliquota;
            $scope.isISS = isISS;
            $scope.Tributo = Tributo;
            $scope.CategoriaTributo = CategoriaTributo;
            $scope.faturamento = faturamento;
            $scope.variacaoTributo = variacaoTributo;
            $scope.objetoTributo = undefined;
            $scope.cidadeISS = cidadeFaturamento;
            $scope.listaUF = [];
            $scope.stringUf = '';
            $scope.categoriaTributoOriginal = {};
            $scope.objetoTributoOriginal = {};

            $scope.valorValidoMin = true;
            $scope.valorValidoMax = true;

            $scope.iniciandoDialog = true;

            //Função para preencher o combo de categoria de tributos para quando o tributo for ISS.
            $scope.buscarCategoriaTributoIss = function(paramStringUf, paramCodigo) {
                let filtroCategoriaTributo = {};
                filtroCategoriaTributo.cidade = {};
                filtroCategoriaTributo.cidade.uf = paramStringUf;
                filtroCategoriaTributo.cidade.codigo = paramCodigo;
                filtroCategoriaTributo.habilitaFaturamento = true;

                $scope.CategoriaTributo.page(undefined, filtroCategoriaTributo)
                    .$promise.then(function(data) {
                        $scope.listaCategoriaTributo = data.content;
                    }).catch(function(response) {
                        console.error('crud-critical', 'dialog', response);
                        notification.criticalError(response.status, response.statusText, response.data);
                    });
            };

            //Função para preencher o combo de categoria de tributos para quando o tributo não for ISS.
            $scope.buscarCategoriaTributos = function(cTributo) {
                let filtroCategoriaTributo = {};
                filtroCategoriaTributo.tributoTO = {};
                filtroCategoriaTributo.tributoTO.codigo = cTributo;
                filtroCategoriaTributo.habilitaFaturamento = true;

                $scope.CategoriaTributo.page(undefined, filtroCategoriaTributo)
                    .$promise.then(function(data) {

                        if (data && data.content) {
                            let listaOrdenada = data.content.sort(function(a, b) {
                                return NotasDebitosService.getPercentualAliquota(a) - NotasDebitosService.getPercentualAliquota(b);
                            });

                            $scope.listaCategoriaTributo = listaOrdenada;
                        }

                    }).catch(function(response) {
                        console.error('crud-critical', 'dialog', response);
                        notification.criticalError(response.status, response.statusText, response.data);
                    });
            };

            //Caso o tributo não seja novo, verificar se o tributo é ISS. Se for, modifica a variavel "$scope.isISS"
            if ($scope.faturamentoAliquota !== undefined) {
                if ($scope.faturamentoAliquota.aliquota.categoriaTributo.tributoTO.codigo == 4) {
                    $scope.isISS = true;
                }
            }

            /**
             * Preenche a combo de tributos (não ISS).
             */
            if (!$scope.isISS) {

                let obj = {};
                obj.tipo = "FEDERAL";
                obj.exibeNota = true;

                $scope.Tributo.page(undefined, obj)
                    .$promise.then(function(data) {
                        $scope.listaDeTributos = data.content;

                        angular.forEach($scope.listaFaturamentoAliquota, function(value, key) {
                            let codTributo = value.aliquota.categoriaTributo.tributoTO.codigo;

                            let indexTributo = undefined;
                            angular.forEach($scope.listaDeTributos, function(objTributo, objKey) {
                                if ($scope.isNew) {
                                    if (objTributo.codigo == codTributo) {
                                        indexTributo = $scope.listaDeTributos.indexOf(objTributo);
                                    }
                                } else {
                                    if (objTributo.codigo == codTributo && objTributo.codigo != $scope.faturamentoAliquota.aliquota.categoriaTributo.tributoTO.codigo) {
                                        indexTributo = $scope.listaDeTributos.indexOf(objTributo);
                                    }
                                }
                            });

                            if (indexTributo != undefined) {
                                $scope.listaDeTributos.splice(indexTributo, 1);
                                indexTributo = undefined;
                            }

                        });

                    }).catch(function(response) {
                        console.error('crud-critical', 'dialog', response);
                        notification.criticalError(response.status, response.statusText, response.data);
                    });

            }

            /**
             * Preenche campos para o modo de edição de acordo com o tipo de tributo.
             */
            $scope.copiaFaturamentoAliquota = {};

            if ($scope.isNew) {
                if ($scope.isISS) {
                    $scope.objetoTributo = {
                        codigo: 4,
                        nome: 'ISS'
                    };

                    $scope.buscarCategoriaTributoIss($scope.cidadeISS.uf, $scope.cidadeISS.codigo);
                }
            } else {
                $scope.copiaFaturamentoAliquota = angular.copy($scope.faturamentoAliquota);
                $scope.objetoTributo = $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo.tributoTO;
                $scope.copiaValor = $scope.copiaFaturamentoAliquota.valor;
                $scope.categoriaTributoOriginal = angular.copy($scope.copiaFaturamentoAliquota.aliquota.categoriaTributo);

                if ($scope.isISS) {
                    $scope.buscarCategoriaTributoIss($scope.cidadeISS.uf, $scope.cidadeISS.codigo);
                } else {
                    $scope.buscarCategoriaTributos($scope.objetoTributo.codigo);
                    $scope.objetoTributoOriginal = angular.copy($scope.objetoTributo);
                }
            }

            /**
             * Como o combo "Categoria do tributo" muda de acordo com o tributo escolhido, é preciso selecionar o item
             * da lista quando for mostrado no modo de edição.
             */
            $scope.selecionarCategoriaTributo = function(argCategoriaTributo) {
                if (!$scope.isNew && !$scope.desabilitarCampo($scope.copiaFaturamentoAliquota.aliquota.categoriaTributo)) {
                    if ($scope.copiaFaturamentoAliquota.aliquota.categoriaTributo.codigo === argCategoriaTributo.codigo) {
                        return true;
                    }
                }
                return false;
            };

            //VALIDAÇÃO
            $scope.desabilitarCampo = function(campo) {
                if (typeof(campo) === "undefined" || campo === undefined || campo === '') {
                    return true;
                } else {
                    return false;
                }
            };

            $scope.variavelValido = function(campo) {
                if ($scope.desabilitarCampo(campo)) {
                    return false;
                } else {
                    return true;
                }
            };

            $scope.minValor = function() {
                return $scope.copiaValor - $scope.variacaoTributo;
            };

            $scope.maxValor = function() {
                return $scope.copiaValor + $scope.variacaoTributo;
            };

            //Funções chamadas na mudança de combos
            $scope.$on('comboTributoNome', function() {
                if (isNew || $scope.objetoTributoOriginal.codigo != $scope.objetoTributo.codigo) {
                    if (angular.isDefined($scope.copiaFaturamentoAliquota.aliquota)) {
                        $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo = undefined;
                        $scope.copiaFaturamentoAliquota.aliquota.percentual = undefined;

                        $scope.copiaFaturamentoAliquota.baseCalculo = undefined;
                        $scope.copiaFaturamentoAliquota.valor = undefined;
                        $scope.copiaValor = angular.copy($scope.copiaFaturamentoAliquota.valor);
                    }

                    $scope.objetoTributoOriginal = angular.copy($scope.objetoTributo);
                    $scope.categoriaTributoOriginal.codigo = -1;
                }

                $scope.buscarCategoriaTributos($scope.objetoTributo.codigo);
            });

            $scope.getPercentualAliquota = NotasFiscaisService.getPercentualAliquota;

            $scope.$watch('copiaFaturamentoAliquota.baseCalculo', function() {

                if ($scope.iniciandoDialog) {
                    $scope.iniciandoDialog = false;
                } else {
                    if ($scope.variavelValido($scope.copiaFaturamentoAliquota.aliquota)) {
                        $scope.copiaFaturamentoAliquota.aliquota.percentual = NotasFiscaisService.getPercentualAliquota($scope.copiaFaturamentoAliquota.aliquota.categoriaTributo);

						var idCidade = $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo.cidade?$scope.copiaFaturamentoAliquota.aliquota.categoriaTributo.cidade.id:undefined;
                        $scope.copiaFaturamentoAliquota.valor = NotasFiscaisService.calculaValorTributo(
                            $scope.copiaFaturamentoAliquota.baseCalculo,
                            $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo,
                            idCidade);

                        $scope.copiaValor = angular.copy($scope.copiaFaturamentoAliquota.valor);

                        $scope.categoriaTributoOriginal = angular.copy($scope.copiaFaturamentoAliquota.aliquota.categoriaTributo);
                    }
                }
            });

            $scope.$on('comboCategoriaTributo', function() {
                if ($scope.copiaFaturamentoAliquota.aliquota &&
                    $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo &&
                    $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo.aliquotas &&
                    $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo.aliquotas.length > 0) {
                    $scope.copiaFaturamentoAliquota.aliquota.codigo = $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo.aliquotas[0].codigo;
                    $scope.copiaFaturamentoAliquota.aliquota.codigoCategoriaTributo = $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo.aliquotas[0].codigoCategoriaTributo;
                    $scope.copiaFaturamentoAliquota.aliquota.id = $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo.aliquotas[0].id;
                    $scope.copiaFaturamentoAliquota.aliquota.percentual = $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo.aliquotas[0].percentual;
                }

                if ($scope.variavelValido($scope.copiaFaturamentoAliquota.aliquota)) {
                    $scope.copiaFaturamentoAliquota.aliquota.percentual = NotasFiscaisService.getPercentualAliquota($scope.copiaFaturamentoAliquota.aliquota.categoriaTributo);
                }

                if (isNew || $scope.categoriaTributoOriginal.codigo != $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo.codigo) {

                    //Calcular Base de cálculo e valor do tributo
                    $scope.copiaFaturamentoAliquota.baseCalculo = angular.copy(faturamento.valor);

                    if ($scope.objetoTributo.codigo == 6) {
                        $scope.copiaFaturamentoAliquota.baseCalculo = $scope.faturamento.valor * ($scope.faturamento.percentualServico / 100);
                    } else {
                        $scope.copiaFaturamentoAliquota.baseCalculo = angular.copy($scope.faturamento.valor);
                    }

                    $scope.copiaFaturamentoAliquota.valor = NotasFiscaisService.calculaValorTributo(
                        $scope.copiaFaturamentoAliquota.baseCalculo,
                        $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo,
                        $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo.cidade.id);

                    $scope.copiaValor = angular.copy($scope.copiaFaturamentoAliquota.valor);

                    $scope.categoriaTributoOriginal = angular.copy($scope.copiaFaturamentoAliquota.aliquota.categoriaTributo);
                }
            });

            //Fim - Funções chamadas na mudança de combos


            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
            $scope.salvar = function(event) {

                $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo.tributoTO = $scope.objetoTributo;

                if (isNew) {
                    $scope.listaFaturamentoAliquota.push($scope.copiaFaturamentoAliquota);
                } else {
                    angular.merge($scope.faturamentoAliquota, $scope.copiaFaturamentoAliquota);
                }

                gerenciarVarTabela();
                $mdDialog.hide();

            };
        };
    });
