angular.module('app.faturamentoInterfaces')

    .controller('FaturamentoInterfacesCadastroCtrl', ['$window', '$scope', '$state', '$stateParams', 'FaturamentoInterfaces', '$mdDialog', 'EnumService', 'Unidade', '$rootScope', 'CalculadorFaturamento', 'FaturamentoEtapa', 'UsuarioLogadoService', '$q', function($window, $scope, $state, $stateParams, FaturamentoInterfaces,
        $mdDialog, EnumService, Unidade, $rootScope, CalculadorFaturamento, FaturamentoEtapa, UsuarioLogadoService, $q) {

        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;
        $scope.titulo_pesquisa = $state.current.data.titulo_pesquisa;
        $scope.action = false;
        $scope.listaNotasFiscais = undefined;
        $scope.usuario = UsuarioLogadoService.getUsuarioLogado();

        var conjLoteCidades = new Set();
        conjLoteCidades.add('Nota carioca');
        conjLoteCidades.add('Nota Norte (Belém)');
        conjLoteCidades.add('Nota Paulistana');
        conjLoteCidades.add('Nota BH');
        conjLoteCidades.add('Nota Brasília');
        conjLoteCidades.add('NF/Material');

        $scope.loadObj = function() {
            $scope.action = true;
            $scope.objectId = $stateParams.objectId;

            if (!$scope.objectId) {
                $scope.$emit('crud-error', 'invalid-id');
                $scope.action = false;
                return;
            } else {
                $rootScope.loadingProgress = true;
                FaturamentoInterfaces.get({
                        id: $scope.objectId
                    }).$promise
                    .then(function(data) {
                        $scope.object = angular.copy(data);
                        data.dataEmissao = new Date(data.dataEmissao);
                        data.dataEvento = new Date(data.dataEvento);
                        data.dataCancelamento = new Date(data.dataCancelamento);
                        data.dataCriacao = data.dataCriacao == undefined ? undefined : new Date(data.dataCriacao);
                        $scope.original = angular.copy(data);
                        $rootScope.loadingProgress = false;
                        $scope.action = false;
                    }).then(function(data2) {
                        if (conjLoteCidades.has($scope.object.tipoLoteInterface)) {
                            $rootScope.loadingProgress = true;
                            $scope.exibirTabelaNotaCarioca = true;
                            FaturamentoInterfaces.getNotasFiscaisLotePrefeitura({
                                    id: $scope.objectId
                                }).$promise
                                .then(function(dataNotas) {
                                    $scope.listaNotasFiscais = dataNotas;
                                    $scope.totalNotas = 0;
                                    $scope.listaNotasFiscais.forEach((item) => {
                                        $scope.totalNotas += item.valor;
                                    });
                                    definirLinhasVermelhas($scope.listaNotasFiscais);
                                    $rootScope.loadingProgress = false;
                                }).catch(function(response) {
                                    $rootScope.loadingProgress = false;
                                    $scope.action = false;
                                    $scope.$emit('crud-critical', 'load', response);
                                });

                        } else if ($scope.object.tipoLoteInterface === 'NF/Material') {
							  $rootScope.loadingProgress = true;
							  $scope.exibirTabelaNotaCarioca = true;
							  FaturamentoInterfaces.getNotasFiscaisLotePrefeitura({id: $scope.objectId}).$promise
								  .then(function(dataNotas) {
									  $scope.listaNotasFiscais = dataNotas;
									  $scope.totalNotas = 0;
									  $scope.listaNotasFiscais.forEach((item) => {
										  buscarNomeClienteNF(item);
										  $scope.totalNotas += item.valor;
									  });
									  $rootScope.loadingProgress = false;
								  }).catch(function(response) {
									  $rootScope.loadingProgress = false;
									  $scope.action = false;
									  $scope.$emit('crud-critical', 'load', response);
								  });
						} else if ($scope.object.tipoLoteInterface === 'Portal Oi') {
                            $rootScope.loadingProgress = true;
                            $scope.exibirTabelaPortalOi = true;
                            FaturamentoInterfaces.getNotasFiscaisLotePrefeitura({id: $scope.objectId}).$promise
                                .then(function(dataNotas) {
                                    $scope.listaNotasFiscais = dataNotas;
                                    $scope.totalNotas = 0;
                                    $scope.listaNotasFiscais.forEach((item) => {
                                        buscarNomeClienteNF(item);
                                        $scope.totalNotas += item.valor;
                                    });
                                    $rootScope.loadingProgress = false;
                                }).catch(function(response) {
                                    $rootScope.loadingProgress = false;
                                    $scope.action = false;
                                    $scope.$emit('crud-critical', 'load', response);
                                });
                        } else if ($scope.object.tipoLoteInterface === 'Boletos (Itaú)') {
                            $rootScope.loadingProgress = true;
                            $scope.exibirTabelaBoletoItau = true;
                            FaturamentoInterfaces.getNotasFiscaisLotePrefeitura({id: $scope.objectId}).$promise
                                .then(function(dataNotas) {
                                    $scope.listaNotasFiscais = dataNotas;
                                    $scope.totalNotas = 0;
                                    $scope.listaNotasFiscais.forEach((item) => {
                                        buscarNomeClienteNF(item);
                                        $scope.totalNotas += item.valor;
                                    });
                                    $rootScope.loadingProgress = false;
                                }).catch(function(response) {
                                    $rootScope.loadingProgress = false;
                                    $scope.action = false;
                                    $scope.$emit('crud-critical', 'load', response);
                                });
                        }
                    })
                    .catch(function(response) {
                        $rootScope.loadingProgress = false;
                        $scope.action = false;
                        $scope.$emit('crud-critical', 'load', response);
                    });
            }
        };

        function buscarNomeClienteNF(item) {
            $rootScope.loadingProgress = true;

            FaturamentoEtapa.getByCodNota({
                id: item.codigo
            }).$promise.then(function(data) {
                item.nomeCliente = data.faturamento.cliente.nome;
            }).catch(function(response) {
                $rootScope.loadingProgress = false;
                $scope.action = false;
                $scope.$emit('crud-critical', 'load', response);
            });

        }

        $scope.newObj = function() {
            $rootScope.loadingProgress = true;
            $scope.action = true;
            FaturamentoInterfaces.get().$promise.then(function(data, status, headers, config) {
                $rootScope.loadingProgress = false;
                $scope.object = new FaturamentoInterfaces();
                delete data.dataEmissao;
                delete data.dataEvento;
                delete data.dataCancelamento;
                $scope.object = angular.merge(data, $scope.object);
                $scope.action = false;
            }, function(response) {
                $rootScope.loadingProgress = false;
                $scope.action = false;
                $scope.$emit('crud-critical', 'load', response);
            });
        };

        var buscarSituacaoLote = () => {
            return $q(function(resolve, reject) {
                EnumService.enumValues({
                    pacote: 'br.com.araujoabreu.sgi.contabilidade.domain',
                    classe: 'LoteInterface',
                    enum: 'SituacaoLoteInterface'
                }).$promise.then(function(data) {
                    $scope.enumStatus = data;
                    resolve($scope.enumStatus);
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'load', response);
                    reject(response);
                });
            });
        };

        var buscarTipoLote = () => {
            return EnumService.enumValues({
                    pacote: 'br.com.araujoabreu.sgi.contabilidade.domain',
                    classe: 'LoteInterface',
                    enum: 'TipoLoteInterface'
                })
                .$promise.then(function(data) {
                    $scope.enumTipo = [];
                    $scope.enumTipo.push({
                        tipo: '',
                        desabilitado: false,
                        empresaRelacionado: []
                    });

                    $scope.enumTipo.push({
                        tipo: data[17],
                        desabilitado: false,
                        empresaRelacionado: [1, 4]
                    }, {
                        tipo: data[18],
                        desabilitado: false,
                        empresaRelacionado: [1, 4]
                    }, {
                        tipo: '--------------------------------------',
                        desabilitado: true,
                        empresaRelacionado: []
                    }, {
                        tipo: data[16],
                        desabilitado: false,
                        empresaRelacionado: [1]
                    }, {
                        tipo: data[19],
                        desabilitado: false,
                        empresaRelacionado: [4]
                    }, {
                        tipo: data[20],
                        desabilitado: false,
                        empresaRelacionado: [9]
                    }, {
                        tipo: data[21],
                        desabilitado: false,
                        empresaRelacionado: [5]
                    }, {
                        tipo: data[22],
                        desabilitado: false,
                        empresaRelacionado: [3]
                    }, {
						tipo: '--------------------------------------',
						desabilitado: true,
						empresaRelacionado: []
					}, {
						tipo: data[25],
						desabilitado: false,
						empresaRelacionado: [1, 4]
					});


                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'load', response);
                });
        };

        buscarSituacaoLote();
        buscarTipoLote();

        var buscarEmpresas = () => {
            const codigosUnidadesIgnoradas = [0]; // vide SGINV-1405

            // Ordenar pelo codigo da matriz e pelo codigo da unidade
            let ordenarPorMatriz = (a, b) => {
                let cod_a = a.codigo;
                let cod_b = b.codigo;
                let codMatriz_a = a.matriz && a.matriz.codigo ? a.matriz.codigo : a.codigo;
                let codMatriz_b = b.matriz && b.matriz.codigo ? b.matriz.codigo : b.codigo;

                if (codMatriz_a == codMatriz_b) {
                    return (cod_a < cod_b) ? -1 : (cod_a > cod_b) ? 1 : 0;
                } else {
                    return (codMatriz_a < codMatriz_b) ? -1 : 1;
                }
            };

            Unidade.unidadesporempresaexceto(undefined, codigosUnidadesIgnoradas).$promise.then(function(data) {
                $scope.empresaList = [];
                angular.forEach(data.sort(ordenarPorMatriz), function(value, key) {
                    if (value.emitirNfFaturamento) {
                        $scope.empresaList.push(value);
                    }
                });
                $scope.empresaListComFiltro = angular.copy($scope.empresaList);
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'search', response);
            });
        };

        buscarEmpresas();

        $scope.$on('changeTipoLoteInterface', function() {
            if ($scope.object.tipoLoteInterface === '') {
                delete $scope.object.tipoLoteInterface;
                $scope.empresaListComFiltro = angular.copy($scope.empresaList);
            } else {

                let tipo = $scope.enumTipo.find((item) => {
                    return item.tipo === $scope.object.tipoLoteInterface;
                });

                $scope.empresaListComFiltro = $scope.empresaList.filter((itemEmpresa) => {
                    return tipo.empresaRelacionado.some((itemCodEmpresa) => {
                        return itemCodEmpresa === itemEmpresa.codigo;
                    });
                });

                if ($scope.object.unidade && $scope.object.unidade.codigo) {
                    if (!tipo.empresaRelacionado.some((item) => {
                            return item === $scope.object.unidade.codigo;
                        })) {
                        delete $scope.object.unidade;
                    }
                }
            }
            $scope.renderizarLblDataCancelamento();
            $scope.object.dataEvento = undefined;
        });

        $scope.renderizarLblDataCancelamento = function() {
            if (angular.isDefined($scope.object) && $scope.object.tipoLoteInterface === 'FATC - Cancelamentos fora do período') {
                return true;
            }
            return false;
        }

        $scope.listar = function() {
            $state.go('interfaces.listar');
        };

        function definirLinhasVermelhas(listaNotaSaidaTO) {
            if (listaNotaSaidaTO && listaNotaSaidaTO.length > 0) {
                let itemAnterior;

                angular.forEach(listaNotaSaidaTO, function(item, key) {
                    if (itemAnterior) {
                        let numero = Number(item.numero.replace("NF/", ""));
                        let numeroAnterior = Number(itemAnterior.numero.replace("NF/", ""));

                        if ((numero - numeroAnterior) > 1) {
                            item.linhaVermelha = true;
                        }
                    }

                    itemAnterior = angular.copy(item);
                });
            }
        }

        $scope.salvarLote = function() {
            if (!loteInterfaceValido()) {
                return;
            }

            $rootScope.loadingProgress = true;
            $scope.action = true;

            if ($scope.object.tipoLoteInterface == 'FATN - Faturamento emitido no período' ||
                $scope.object.tipoLoteInterface == 'FATC - Cancelamentos fora do período' ||
                $scope.object.tipoLoteInterface == 'DSFP - 2% sobre faturamento (desoneração da folha de pagamento)') {

                $scope.object.dataEvento.setDate(1);
            }
            if ($scope.screenMode === 'NEW') {
                // verificar se há dados para criar o lote.

                let pQtdLote = FaturamentoInterfaces.qtdlote($scope.object).$promise;

                pQtdLote.then(function(data) {

                    // qtdLote = -1. Caso não tenham escolhido os tipos: FATN, FATC, INSS, DSFP, Nota Carioca, IRRF, Boleto Itaú, Portal Oi, CSLL e PIS/COFINS/ISS
                    // qtdLote > 0.  Caso tenham escolhido os tipos:FATN, FATC, INSS, DSFP, Nota Carioca, IRRF, Boleto Itaú, Portal Oi, CSLL e PIS/COFINS/ISS. E tenha achado dados para gerar o lote.
                    // qtdLote = 0.  Caso tenham escolhido os tipos: FATN, FATC, INSS, DSFP, Nota Carioca, IRRF, Boleto Itaú, Portal Oi, CSLL e PIS/COFINS/ISS. E não tenha achado dados para gerar o lote.
                    var qtdLote = data.qtdLote;

                    if (qtdLote != 0 || CalculadorFaturamento.isCampoVazioNumerico(qtdLote)) {

                        let pSaveFaturamentoInterface = FaturamentoInterfaces.save($scope.object).$promise;
                        pSaveFaturamentoInterface.then(function(data) {
                            $rootScope.loadingProgress = false;
                            $scope.action = false;
                            $scope.notification.success("", "Lote salvo com sucesso");
                            $state.go('interfaces.exibir', {
                                objectId: data.codigo
                            });
                        }).catch(function(response) {
                            $rootScope.loadingProgress = false;
                            $scope.action = false;
                            $scope.$emit('crud-critical', 'save', response);
                        });

                    } else if (qtdLote == 0) {
                        $rootScope.loadingProgress = false;
                        $scope.action = false;
                        $scope.notification.atencao("Não foi possível gerar o lote, pois não há dados suficientes para a geração.");
                    }

                }).catch(function(response) {
                    $rootScope.loadingProgress = false;
                    $scope.action = false;
                    $scope.$emit('crud-critical', 'save', response);
                });

            } else if ($scope.screenMode === 'EDIT') {
                FaturamentoInterfaces.update($scope.object, $scope.objectId).$promise
                    .then(function(data) {
                        $scope.notification.success(undefined, "Lote reprocessado com sucesso");
                        $state.go('interfaces.exibir', {
                            objectId: data.codigo
                        });
                    }).catch(function(response) {
                        $scope.action = false;
                        $scope.$emit('crud-critical', 'save', response);
                    });
            }
        };

        var loteInterfaceValido = () => {
            let resultado = true;

            if ($scope.object.unidade.codigo != 1 && $scope.object.unidade.codigo != 4) {
                if ($scope.object.tipoLoteInterface == 'Boletos (Itaú)' || $scope.object.tipoLoteInterface == 'Portal Oi') {
                    $scope.notification.error('', 'Para este tipo de lote, apenas as matrizes podem ser selecionadas.');
                    resultado = false;
                }
            }

            return resultado;
        };

        // FRAMEWORK NÃO LANÇA SUCESSO PARA CRIAÇÃO DE OBJETO NOVO //
        if ($scope.screenMode === 'NEW') {
            $scope.newObj();
        } else if ($scope.screenMode === 'EDIT' || $scope.screenMode === 'VIEW') {
            $scope.loadObj();
        }

        $scope.changeDataEmissao = function() {
            if (conjLoteCidades.has($scope.object.tipoLoteInterface)) {
                $scope.action = true;
                $rootScope.loadingProgress = true;
                $scope.exibirTabelaNotaCarioca = false;
                $scope.exibirTabelaNotaCarioca = false;
                FaturamentoInterfaces.verificarNotaFiscalTipoPrefeitura($scope.object).$promise.then((data) => {
                    $scope.listaNotasFiscais = data;
                    $scope.totalNotas = 0;
                    $scope.listaNotasFiscais.forEach((item) => {
                        $scope.totalNotas += item.valor;
                    });
                    $scope.exibirTabelaNotaCarioca = true;
                    $rootScope.loadingProgress = false;
                    $scope.action = false;
                }).catch((error) => {
                    $scope.action = false;
                    $rootScope.loadingProgress = false;
                    console.error(error);
                });
            }
        };

        // BOTÃO DOWNLOAD
        $scope.download = function() {
            FaturamentoInterfaces.anexolista({
                id: $scope.object.codigo
            }).$promise.then(function(data) {

                var listaAnexo = data;

                if (!CalculadorFaturamento.isCampoVazio(listaAnexo) && listaAnexo.length > 0) {

                    FaturamentoInterfaces.verificaranexo($scope.object.codigo).$promise
                        .then(function(data) {
                            $window.location = `/sgi/nv/rest/faturamento/interfaces/baixaranexo/${$scope.object.codigo}`;
                        }).catch(function(response) {
                            console.error(response);
                            $rootScope.loadingProgress = false;
                            if (response.data) {
                                $scope.notification.error("", response.data.message);
                            } else {
                                $scope.notification.error("", response);
                            }
                        });

                } else {
                    $scope.notification.atencao(undefined, "Não há anexos para este lote.");
                }

            }).catch(function(response) {
                $scope.notification.criticalError(response.status, response.statusText, response.data);
            });

        };

        // BOTÃO REPROCESSAR
        $scope.reprocessarLote = function() {
            $mdDialog.show({
                controller: DialogControllerReprocessarLote,
                locals: {
                    faturamentoInterface: $scope.object,
                    resource: FaturamentoInterfaces,
                    notification: $scope.notification,
                    loadingProgress: $rootScope.loadingProgress,
                    fnLoadObj: $scope.loadObj
                },
                templateUrl: 'partials/faturamento/faturamentoInterfaces/html/partials/dialog_reprocessar_lote.html',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        // BOTÃO PROCESSAR
        $scope.exibirProcessar = function() {
        	return $scope.object.tipoLoteInterface == 'NF/Material' && ($scope.usuario.login == 'eavila' || $scope.usuario.login == 'mwaldeck');
        }

        $scope.processarLote = function() {
			FaturamentoInterfaces.processar({
					id: $scope.object.codigo
				})
				.$promise.then(function(data) {
					title = 'Processar Lote';
					message = 'Lote LT/' + $scope.object.codigo + ' processado com sucesso.';
					$scope.notification.success(title, message);
					loadingProgress = false;
					$scope.loadObj();
				}).catch(function(response) {
					$scope.action = false;
					console.error('crud-critical', 'dialog', response);
					notification.criticalError(response.status, response.statusText, response.data);
				});
        };


        //==== DIALOG REPROCESSAR LOTE ====//
        function DialogControllerReprocessarLote($scope, $timeout, $mdDialog, $state, faturamentoInterface, resource, notification, loadingProgress, fnLoadObj) {
            $scope.action = false;
            $scope.confirm = function() {
                loadingProgress = true;
                $scope.action = true;
                FaturamentoInterfaces.reprocessar({
                        id: faturamentoInterface.codigo
                    })
                    .$promise.then(function(data) {
                        $timeout(function() {
                            title = 'Reprocessar Lote';
                            message = 'Lote LT/' + faturamentoInterface.codigo + ' reprocessado com sucesso.';
                            notification.success(title, message);
                            $scope.cancel();
                            loadingProgress = false;
                            $scope.action = false;
                            fnLoadObj();
                        }, 500);
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

    }]);
