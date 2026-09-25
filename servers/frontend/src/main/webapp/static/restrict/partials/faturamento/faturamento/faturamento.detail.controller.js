	function PagerService() {
	    // service definition
	    let service = {};

	    service.GetPager = GetPager;

	    return service;

	    // service implementation
	    function GetPager(totalItems, currentPage, pageSize) {
	        // default to first page
	        currentPage = currentPage || 1;

	        // default page size is 10
	        pageSize = pageSize || 10;

	        // calculate total pages
	        let totalPages = Math.ceil(totalItems / pageSize);

	        let startPage, endPage;
	        if (totalPages <= 10) {
	            // less than 10 total pages so show all
	            startPage = 1;
	            endPage = totalPages;
	        } else {
	            // more than 10 total pages so calculate start and end pages
	            if (currentPage <= 6) {
	                startPage = 1;
	                endPage = 10;
	            } else if (currentPage + 4 >= totalPages) {
	                startPage = totalPages - 9;
	                endPage = totalPages;
	            } else {
	                startPage = currentPage - 5;
	                endPage = currentPage + 4;
	            }
	        }

	        // calculate start and end item indexes
	        let startIndex = (currentPage - 1) * pageSize;
	        let endIndex = Math.min(startIndex + pageSize - 1, totalItems - 1);

	        // create an array of pages to ng-repeat in the pager control
	        let pages = _.range(startPage, endPage + 1);

	        // return object with all pager properties required by the view
	        return {
	            totalItems: totalItems,
	            currentPage: currentPage,
	            pageSize: pageSize,
	            totalPages: totalPages,
	            startPage: startPage,
	            endPage: endPage,
	            startIndex: startIndex,
	            endIndex: endIndex,
	            pages: pages
	        };
	    }
	}

	function complementarDiscriminacaoFinalComTributos($scope, discriminacaoParteFinal) {
	    let linha = "";
	    let possuiPcc = $scope.percentualPcc && $scope.percentualPcc > 0;
	    let possuiIss = $scope.issRetidoCliente && $scope.percentualIss && $scope.percentualIss > 0;
	    let possuiInss = $scope.percentualInss && $scope.percentualInss > 0;
	    let possuiIrrf = $scope.percentualIrrf && $scope.percentualIrrf > 0;
	    let separadorPadrao = "    ";

	    if (possuiPcc || possuiInss || possuiIss || possuiIrrf) {
	        let separador = "";

	        if (possuiPcc) {
	            linha = linha + separador + "PCC:  " + $scope.percentualPcc + '%';
	            separador = separadorPadrao;
	        }

	        if (possuiInss) {
	            linha = linha + separador + "INSS:  " + $scope.percentualInss + '%';
	            separador = separadorPadrao;
	        }

	        if (possuiIrrf) {
	            linha = linha + separador + "IRRF:  " + $scope.percentualIrrf + '%';
	            separador = separadorPadrao;
	        }

	        if (possuiIss) {
	            linha = linha + separador + "ISS:  " + $scope.percentualIss + '%';
	            separador = separadorPadrao;
	        }
	    }

	    if (linha) {
	        discriminacaoParteFinal = discriminacaoParteFinal + "\n" + linha;
	    }

	    return discriminacaoParteFinal;
	}

	function inicializarValoresPercentuaisTributosFaturamento($scope) {

	    $scope.subtotalPercentualTributo = 0;
	    $scope.subTotalValorTributo = 0;
	    $scope.totalPercentualTributo = 0;
	    $scope.totalValorTributo = 0;

	    $scope.percentualIss = 0;
	    $scope.percentualInss = 0;
	    $scope.percentualPcc = 0;
	    $scope.percentualIrrf = 0;

	    $scope.valorIss = 0;
	    $scope.valorInss = 0;
	    $scope.valorPcc = 0;
	    $scope.valorIrrf = 0;

	    if (!('issRetidoCliente' in $scope)) {
	        let obj = undefined;

	        if ($scope.object) {
	            obj = $scope.object.faturamento ? $scope.object.faturamento : $scope.object;
	        }

	        if (obj) {
	            $scope.issRetidoCliente = obj.issRetidoCliente || false;
	        }
	    }
	}

	function calcularValoresPercentuaisTributosFaturamento($scope) {
	    inicializarValoresPercentuaisTributosFaturamento($scope);
	    if (!angular.isArray($scope.listaFaturamentoAliquota) || !$scope.listaFaturamentoAliquota.length) return;

	    let total_1 = 0;
	    let total_2 = 0;
	    let total_3 = 0;
	    let total_4 = 0;

	    angular.forEach($scope.listaFaturamentoAliquota, function(obj, key) {
	        let codigoTributo = obj.aliquota.categoriaTributo.tributoTO.codigo;

	        let varPercentual = obj.aliquota.percentual;
	        let varValor = obj.valor;

	        if (codigoTributo !== 4) {

	            if (angular.isDefined(varPercentual) && varPercentual !== undefined) {
	                total_1 += Number(varPercentual);
	            }

	            if (angular.isDefined(varValor) && varValor !== undefined) {
	                total_2 += Number(varValor);
	            }

	        } else {

	            if (angular.isDefined(varPercentual) && varPercentual !== undefined) {
	                total_3 += Number(varPercentual);
	            }

	            if (angular.isDefined(varValor) && varValor !== undefined) {
	                total_4 += Number(varValor);
	            }

	        }

	        switch (obj.aliquota.categoriaTributo.tributoTO.nome) {
	            case 'PIS/COFINS/CSLL':
	                $scope.percentualPcc = varPercentual;
	                $scope.valorPcc = varValor;
	                break;
	            case 'INSS':
	                $scope.percentualInss = varPercentual;
	                $scope.valorInss = varValor;
	                break;
	            case 'ISS':
	                $scope.percentualIss = varPercentual;
	                $scope.valorIss = varValor;
	                break;
	            case 'IRRF':
	            case 'IRPJ':
	                $scope.percentualIrrf = varPercentual;
	                $scope.valorIrrf = varValor;
	                break;
	        }
	    });

	    $scope.subtotalPercentualTributo = total_1;
	    $scope.subTotalValorTributo = total_2;
	    $scope.totalPercentualTributo = total_1 + total_3;
	    $scope.totalValorTributo = total_2 + total_4;
	}

	(function() {
	    'use strict';

	    angular
	        .module('app.faturamento')
	        .controller('FaturamentoCadastroCtrl', faturamentoCadastroCtrlFn);

	    function faturamentoCadastroCtrlFn($scope, $state, $stateParams, $timeout, Faturamento, CalculadorFaturamento, $mdDialog, CentroFinanceiro, Pessoa, EnumService, $rootScope, $document, Contrato,
	        FaturamentoEtapa, Tributo, CategoriaTributo, Parametro, ContaCorrente, TipoCobrancaFaturamento, Contato, AutocompleteService, $filter, MotivoRetornoDialog,
	        MotivoRetornoFaturamento, FaturamentoAliquota, MotivoRetornoEtapa, ContratoCidadePrestacaoServicos, NotaFiscalFaturamento, Unidade, $window, $q, PessoaJuridica, FaturamentoContato,
	        RelacionamentoContato, Telefone, DialogConfirm, TiposServico, Etapasnaofaturadas, UsuarioLogadoService, Cidade) {
	        $scope.action = false;

	        $scope.modulo = $state.current.data.modulo;
	        $scope.titulo = $state.current.data.titulo;
	        $scope.subtitulo = $state.current.data.subtitulo;

	        $scope.divMatSerList = [];
	        $scope.tipoList = ["Contratual", "Extra contratual"];
	        $scope.ordemList = ["Centro de lucro/Cliente", "Cliente/Centro de lucro"];
			$scope.tipoDocFatList = [];
			$scope.tipoDeItensList = [];

	        $scope.object = new Faturamento();
	        $scope.objectCopy = {};
	        $scope.object.listaFaturamentoAliquota = [];

	        $scope.vencimento1 = true;
	        $scope.vencimento2 = true;

	        UsuarioLogadoService.apenasFaturamento().$promise.then(function(data) {
	            $scope.habilitarBotoesFaturamento = data.value;
	        }).catch(function(response) {
	            $rootScope.loadingProgress = false;
	            $rootScope.$emit('crud-critical', 'load', response);
	        });

	        /**
	         * Indica se a cidade da unidade do contrato é igual
	         * a cidade do "Local de prestação dos serviços" (ABA CONTRATO)
	         */
	        $scope.cidadesIguais = false;

	        $scope.listaCidadesISS = [];
	        $scope.cidadeUnidadeContrato = undefined;
	        $scope.valueCidadeISS = '';
	        $scope.object.botaoDesabilitado = true;
	        $scope.cidUnidade = undefined;
	        $scope.cidadeDoContrato = undefined;
	        $scope.comboCidadeIssDesabilitado = false;
	        $scope.cidadeDaUnidadeMatrizDoContrato = undefined;

	        let catchError = (response) => {
	            $rootScope.loadingProgress = false;
	            $scope.action = false;
	            $scope.$emit('crud-critical', 'load', response);
	            reject(false);
	        };


	        $scope.exibirDetalheCliente = function(codigo, tipoPessoa, tipo, ev) {
	            if (codigo === undefined) {
	                return;
	            }

	            let useFullScreen = true;

	            if (tipo === undefined)
	                tipo = 'ninformado';

	            $scope.pessoa = PessoaJuridica.popupFaturamento({
	                id: codigo,
	                tipo: tipoPessoa,
	                idCl: $scope.object.centroFinanceiro.codigo
	            });

	            $rootScope.loadingProgress = true;
	            $scope.action = true;

	            $scope.pessoa.$promise.then((dados) => {
	                $rootScope.loadingProgress = false;
	                $scope.action = false;
	                $mdDialog.show({
	                    controller: DialogControllerPessoa,
	                    locals: {
	                        pessoa: dados,
	                        tipo: tipo
	                    },
	                    templateUrl: 'js/util/templates/detalhecliente.html',
	                    parent: angular.element(document.body),
	                    targetEvent: ev,
	                    clickOutsideToClose: true,
	                    fullscreen: useFullScreen,
	                    arialabel: 'Detalhe cliente'
	                });
	            });
	        };

	        function DialogControllerPessoa($scope, $mdDialog, pessoa, tipo) {
	            $scope.pessoa = pessoa;
	            $scope.pessoa.tipoPessoaJuridica = tipo;
	            $scope.fechar = function() {
	                $mdDialog.cancel();
	            };
	        }


	        $scope.valoreNotaFiscal = function(ev, nf) {
	            $mdDialog.show({
	                controller: DialogDetalhesValoresNotaFiscal,
	                templateUrl: 'partials/faturamento/faturamento/partials/dialog_detalhes_valores_faturamento_nf.html',
	                parent: angular.element(document.body),
	                targetEvent: ev,
	                locals: {
	                    nf: nf
	                },
	                clickOutsideToClose: false,
	                fullscreen: false
	            });

	            function DialogDetalhesValoresNotaFiscal($scope, $mdDialog, nf) {
	                $scope.object = nf;
	                $scope.hide = function() {
	                    $mdDialog.hide();
	                };
	                $scope.cancel = function() {
	                    $mdDialog.cancel();
	                };
	            }
	        };

	        $scope.valorTotalNd = function (nd) {
				return nd.valorBruto - nd.valorDescontoPrevisto + nd.valorDevolucaoPrevisto;
			};

	        $scope.valoreNotaDebito = function(ev, nd) {
				$mdDialog.show({
					controller: DialogDetalhesValoresNotaDebito,
					templateUrl: 'partials/faturamento/faturamento/partials/dialog_detalhes_valores_faturamento_nd.html',
					parent: angular.element(document.body),
					targetEvent: ev,
					locals: {
						nd: nd,
						total: $scope.valorTotalNd(nd)
					},
					clickOutsideToClose: false,
					fullscreen: false
				});

				function DialogDetalhesValoresNotaDebito($scope, $mdDialog, nd, total) {
					$scope.object = nd;
					$scope.object.total = total;
					$scope.hide = function() {
						$mdDialog.hide();
					};
					$scope.cancel = function() {
						$mdDialog.cancel();
					};
				}
			};

	        $scope.openNewTargetContrato = function() {
	            $window.open('/sgi/nv/app/main.html#!/comercial/contratos/' + $scope.object.centroFinanceiro.contratoTO.codigo);
	        };

	        $scope.procurarCidadeUnidadeContrato = function() {
	            let objCidadeRj = undefined;

	            // Variavel para indicar que o faturamento tem serviço na cidade do Rio de Janeiro/RJ.
	            let temServicoRj = false;

	            if ($scope.object.servicoTipo && $scope.object.servicoTipo.tipoFaturamento) {
	                if ($scope.object.servicoTipo.tipoFaturamento === 'Habilitado com cidade') {
	                    temServicoRj = true;
	                    objCidadeRj = {
	                        cidadeUf: 'RIO DE JANEIRO/RJ',
	                        codigo: 3304557,
	                        id: 3304557,
	                        nome: 'RIO DE JANEIRO',
	                        uf: 'RJ'
	                    };
	                }
				} else {
					$scope.object.servicoTipo = undefined;
					$scope.searchTextServico  = undefined;
				}

	            //BUSCA A CIDADE DA UNIDADE DO CONTRATO
	            Contrato.cidadeUnidade($scope.object.centroFinanceiro.contratoTO).$promise.then(function(objCidade) {
	                $scope.cidadeUnidadeContrato = objCidade;
	                $scope.cidadeDoContrato = angular.copy(objCidade);

	                let idsDasCidades = new Set();
	                idsDasCidades.add($scope.cidadeUnidadeContrato.codigo);

	                if ($scope.object.cidadePrestacaoServico != undefined) {
	                    idsDasCidades.add($scope.object.cidadePrestacaoServico.codigo);
	                }

	                if (temServicoRj) {
	                    idsDasCidades.add(objCidadeRj.codigo);
	                }

	                if (!$scope.object.cidadePrestacaoServico) {
	                    $scope.notification.atencao("Atenção", "Escolha um local de prestação dos serviços.");
	                }

	                if (idsDasCidades.size === 1) {
	                    $scope.cidadesIguais = true;
	                    $scope.valueCidadeISS = $scope.cidadeUnidadeContrato.uf + '/' + $scope.cidadeUnidadeContrato.nome;
	                    $scope.object.botaoDesabilitado = false;
	                    $scope.cidUnidade = angular.copy($scope.cidadeUnidadeContrato);
	                } else {
	                    $scope.cidadesIguais = false;
	                    $scope.valueCidadeISS = '';
	                    $scope.object.botaoDesabilitado = true;

	                    let listaCidadesISS = [];
	                    let cidadesNaLista = new Set();

	                    listaCidadesISS.push({
	                        objeto: undefined,
	                        texto: ''
	                    });

	                    listaCidadesISS.push({
	                        objeto: $scope.cidadeUnidadeContrato,
	                        texto: '' + $scope.cidadeUnidadeContrato.uf + '/' + $scope.cidadeUnidadeContrato.nome + ''
	                    });

	                    cidadesNaLista.add($scope.cidadeUnidadeContrato.codigo);

	                    if ($scope.object.cidadePrestacaoServico != undefined && !cidadesNaLista.has($scope.object.cidadePrestacaoServico.codigo)) {
	                        listaCidadesISS.push({
	                            objeto: $scope.object.cidadePrestacaoServico,
	                            texto: '' + $scope.object.cidadePrestacaoServico.uf + '/' + $scope.object.cidadePrestacaoServico.nome + ''
	                        });

	                        cidadesNaLista.add($scope.object.cidadePrestacaoServico.codigo);
	                    }

	                    if (temServicoRj && !cidadesNaLista.has(objCidadeRj.codigo)) {
	                        listaCidadesISS.push({
	                            objeto: objCidadeRj,
	                            texto: '' + objCidadeRj.uf + '/' + objCidadeRj.nome + ''
	                        });

	                        cidadesNaLista.add(objCidadeRj.codigo);
	                    }

	                    $scope.listaCidadesISS = listaCidadesISS;
	                }

	            }).catch(catchError);

	            Contrato.cidadeUnidadeMatriz($scope.object.centroFinanceiro.contratoTO).$promise.then(function(objCidade) {
	                $scope.cidadeDaUnidadeMatrizDoContrato = angular.copy(objCidade);
	            }).catch(catchError);

	        };

	        //Define o "Tipo de itens"
			$scope.definirTipoDeItens = function() {
				if ($scope.object) {
					let pTipoDeItens = EnumService.enumValues({
						pacote: 'br.com.araujoabreu.sgi.faturamento.domain',
						classe: 'Faturamento',
						enum: 'TipoFornecimentoNF'
					}).$promise;

					pTipoDeItens.then(function(data) {
						$scope.tipoDeItensList = data;
					}).catch(catchError);
				}
			};

	        //Define o tipo do "Documento para pagamento"
	        $scope.definirDocumentoPagamento = function() {
	        	if ($scope.modeInformacoes) {
					if ($scope.object && $scope.object.contaCorrente && (typeof $scope.object.contaCorrente.boletoFaturamento !== 'undefined') && $scope.object.contaCorrente.boletoFaturamento)
						delete $scope.object.tipoDocumentoPagamento;
	            	else
	            		$scope.object.tipoDocumentoPagamento = 'Depósito em conta/Transferência';
				}
	        };

			//Define o tipo do "Documento para o faturamento"
			$scope.definirDocumentoFaturamento = function() {
				if ($scope.object) {
					let pTipoDocumentoFaturamento = EnumService.enumValues({
						pacote: 'br.com.araujoabreu.sgi.faturamento.domain',
						classe: 'Faturamento',
						enum: 'TipoDocumentoFaturamento'
					}).$promise;

					pTipoDocumentoFaturamento.then(function(data) {
						if ($scope.object.codigo == null || $scope.object.codigo == 0) {
							$scope.tipoDocFatList.push('');
							angular.forEach(data, function (value) {
								$scope.tipoDocFatList.push(value);
							});
						}
						else
							$scope.tipoDocFatList = data;

					}).catch(catchError);
				}
			};

			$scope.$on('changeTipoDocumentoFaturamento', function () {
				if ($scope.object.tipoDocumentoFaturamento === '') {
					delete $scope.object.tipoDocumentoFaturamento;
					$timeout(function () {
						delete $scope.object.tipoDocumentoFaturamento;
					}, 200);
				}
			});

			//Define o tipo do "FornecimentoNF"
			$scope.definirTipoFornecimentoNF = function() {
				if ($scope.object) {

					let pTipoFornecimentoNF = EnumService.enumValues({
						pacote: 'br.com.araujoabreu.sgi.faturamento.domain',
						classe: 'Faturamento',
						enum: 'TipoFornecimentoNF'
					}).$promise;

					pTipoFornecimentoNF.then(function(data) {
						$scope.divMatSerList = data;

					}).catch(catchError);
				}
			};


	        $scope.listar = function() {
	            $state.go('faturamento.listar');
	        };

	        $scope.possuiNfNd = function() { //verifica se o faturamento já está associado a alguma NF ou ND
	            if ($scope.object.faturamentoEtapas && $scope.object.faturamentoEtapas.length > 0
	            	&& (
							($scope.object.faturamentoEtapas[0].notaDebito && $scope.object.faturamentoEtapas[0].notaDebito.id &&  $scope.object.faturamentoEtapas[0].notaDebito.id > 0)
							|| ($scope.object.faturamentoEtapas[0].notaSaida && $scope.object.faturamentoEtapas[0].notaSaida.id &&  $scope.object.faturamentoEtapas[0].notaSaida.id > 0)
	            		)
	            	) {
	            	return true;
	            }

	            return false;
	        };

	        function loadObj() {
	            return new Promise((resolve, reject) => {
	                $scope.action = true;
	                $scope.object = {};
	                $scope.objectId = $stateParams.objectId;
	                if (!$scope.objectId) {
	                    $scope.$emit('crud-error', 'invalid-id');
	                    $scope.action = false;
	                    reject(true);
	                } else {
	                    Faturamento.get({id: $scope.objectId}).$promise.then(function(data) {
							$scope.carregarDados(data)
								.then($scope.carregarTipoCobranca())
								.then($scope.carregarBancos(data.centroFinanceiro.unidade.matriz.codigo))
								.then($scope.carregarContasCorrentes(data.contaCorrente.banco.codigo, data.centroFinanceiro.unidade.matriz.codigo))
								.then($scope.definirDocumentoFaturamento())
								.then($scope.definirTipoDeItens())
								.then($scope.definirTipoFornecimentoNF())
								.then($scope.carregarEtapas(0))
								.then(() => {
									$scope.action = false;
									resolve(true);
								});
						}).catch(catchError);
	                }
	            });
	        }

	        function newObj() {
	            $rootScope.loadingProgress = true;
	            Faturamento.get()
	                .$promise.then(function(data, status, headers, config) {
	                    $rootScope.loadingProgress = false;
	                    $scope.object = new Faturamento();
	                    $scope.object = angular.merge(data, $scope.object);
	                    $scope.carregarTipoCobranca();
	                    $scope.object.issRetidoCliente = false;
	                    $scope.object.valorTributos = 0;
	                    $scope.object.valorServico = 0;
	                    $scope.object.valorMaterial = 0;
	                    $scope.object.valorLiquido = 0;
	                    $scope.object.percentualMaterial = 0;
	                    $scope.object.percentualServico = 0;
	                    $scope.habilitarVenc1 = false;
	                    $scope.habilitarVenc2 = false;
	                    $scope.object.numeroEtapa = '001';
	                    $scope.object.situacaoEtapa = 'Aberto';
	                    $scope.object.ordem = 'Centro de lucro/Cliente';
	                    $scope.object.valorTributosEtapa = 0;
	                    $scope.object.valorServicoEtapa = 0;
	                    $scope.object.valorMaterialEtapa = 0;
	                    $scope.object.valorLiquidoEtapa = 0;
	                    $scope.object.percentualMaterialEtapa = 0;
	                    $scope.object.percentualServicoEtapa = 0;
	                    $scope.object.valorRetencao = 0;

	                    $scope.object.percentualRetencaoContratual = 0;
	                    $scope.object.valorRetencaoContratual = 0;
	                    $scope.object.percentualRetencaoAcessoria = 0;
	                    $scope.object.valorRetencaoAcessoria = 0;
	                    $scope.object.tipoDocumentoPagamento = 'Depósito em conta/Transferência';

	                    $scope.definirTipoDeItens();
	                    $scope.definirDocumentoFaturamento();
	                    $scope.definirTipoFornecimentoNF();
	                }, function(response) {
	                    $rootScope.loadingProgress = false;
	                    $scope.$emit('crud-critical', 'load', response);
	                });
	        }

	        // FRAMEWORK NÃO LANÇA SUCESSO PARA CRIAÇÃO DE OBJETO NOVO //
	        if ($scope.screenMode === 'NEW') {
	            newObj();
	        } else if ($scope.screenMode === 'EDIT' || $scope.screenMode === 'VIEW') {
	            loadObj();
	        }

	        $scope.cancelarFaturamento = function(event) {
	            let motivoFn = () => {
	                let chaveMotivo = 'cancelar-fat';
	                let titulo = 'Motivos de Retorno - Cancelar faturamento';

	                // Função que será executada no confirmar no dialog de motivo de retorno.
	                $scope.confirmarMotivo = function(motivoRetornoDocumento, fn_Hide) {
	                    // MotivoRetornoDocumentoFaturamentoTO
	                    $scope.motivoRetornoDocumentoFaturamento = {};
	                    $scope.motivoRetornoDocumentoFaturamento.faturamento = {
	                        codigo: $scope.object.codigo
	                    };
	                    $scope.motivoRetornoDocumentoFaturamento.motivoRetornoDocumento = motivoRetornoDocumento;

	                    $rootScope.loadingProgress = true;

	                    MotivoRetornoFaturamento.save($scope.motivoRetornoDocumentoFaturamento)
	                        .$promise.then(function(data) {
	                            $rootScope.loadingProgress = false;
	                            $scope.notification.success("", "Faturamento cancelado com sucesso");
								fn_Hide();
	                            loadObj();
	                        }).catch(catchError);
	                };

	                // Função que será executada no cancelar no dialog de motivo de retorno.
	                $scope.cancelarMotivo = function(fn_Hide) {
	                    fn_Hide();
	                };

	                // Passar para o serviço, as funções que serão chamadas ao confirmar ou cancelar o motivo de retorno.
	                MotivoRetornoDialog.show(event, chaveMotivo, titulo, $scope.confirmarMotivo, $scope.cancelarMotivo);
	            };

	            Faturamento.notaFiscalAbertaCheck({
	                id: $scope.object.codigo
	            }).$promise.then(function(dataNf) {
	                if (dataNf.value) {

	                    const cancelarComEtapaAFaturar = () => {
							let tituloDialog = "Faturamento - Cancelamento";
							let msgDialog = "Este faturamento possui NF(s) ainda em aberto. Essa(s) NF(s) pode(m) continuar com o fluxo normalmente.";
							let tituloBotaoFechar = "FECHAR";

							let funcaoConfirmar = function() {
								motivoFn();
							};

							DialogConfirm.show(undefined, $scope.object, tituloDialog, msgDialog, tituloBotaoFechar, undefined, funcaoConfirmar, undefined);
						};
					cancelarComEtapaAFaturar();
	                } else {
	                    FaturamentoEtapa.etapaAFaturarCheck({
	                        id: $scope.object.codigo
	                    }).$promise.then(function(dataEtapa) {
	                        if (dataEtapa.value) {
	                            const cancelarComEtapaAFaturar = () => {
										let tituloDialog = "Faturamento - Cancelamento";
										let msgDialog = "Este faturamento possui etapa(s) ainda não faturada(s). Após o cancelamento, esta(s) etapa(s) não poderá(ão) ser faturada(s).";
										let tituloBotaoConfirmar = "CONFIRMAR";
										let tituloBotaoCancelar = "CANCELAR";

										let funcaoCancelar = function(fnHide) {
											fnHide();
										};

										let funcaoConfirmar = function() {
											motivoFn();
										};

										DialogConfirm.show(undefined, $scope.object, tituloDialog, msgDialog, tituloBotaoConfirmar, tituloBotaoCancelar, funcaoConfirmar, funcaoCancelar);
									};
								cancelarComEtapaAFaturar();
	                        } else {
								motivoFn();
							}
	                    });
	                }
	            });
	        };

	        $scope.salvarFaturamento = function() {
	        	if ($scope.object.tipoDocumentoPagamento == undefined || $scope.object.tipoDocumentoPagamento == null)
	        		$scope.object.tipoDocumentoPagamento = $scope.original.tipoDocumentoPagamento;

	            return new Promise((resolve, reject) => {
	                $scope.action = true;
	                if (CalculadorFaturamento.isCampoVazioNumerico($scope.object.percentualRetencao)) {
	                    $scope.object.percentualRetencao = 0;
	                }

	                if ($scope.object.faturamentoRetencoes && $scope.object.faturamentoRetencoes.length > 0) {
	                    angular.forEach($scope.object.faturamentoRetencoes, function(value, key) {
	                        if (value.tipo.chave === 'contratual') {
	                            value.percentual = $scope.object.percentualRetencaoContratual;
	                            value.tipo = {
	                                codigo: 1,
	                                chave: 'contratual'
	                            };
	                            value.situacao = {
	                                codigo: 1
	                            };
	                        } else if (value.tipo.chave === 'acessoria') {
	                            value.percentual = $scope.object.percentualRetencaoAcessoria;
	                            value.tipo = {
	                                codigo: 2,
	                                chave: 'acessoria'
	                            };
	                            value.situacao = {
	                                codigo: 1
	                            };
	                        }
	                    });
	                } else {
	                    $scope.object.faturamentoRetencoes = [];
	                    $scope.object.faturamentoRetencoes.push({
	                        percentual: $scope.object.percentualRetencaoContratual,
	                        tipo: {
	                            codigo: 1,
	                            chave: 'contratual'
	                        },
	                        situacao: {
	                            codigo: 1
	                        }
	                    });
	                    $scope.object.faturamentoRetencoes.push({
	                        percentual: $scope.object.percentualRetencaoAcessoria,
	                        tipo: {
	                            codigo: 2,
	                            chave: 'acessoria'
	                        },
	                        situacao: {
	                            codigo: 1
	                        }
	                    });
	                }

	                // FIXME: Corrigir isso urgente. Não esquecer... onChange da combo de banco.
	                if (!$scope.object.contaCorrente.agencia) {
	                    $scope.object.tipoDocumentoPagamento = $scope.original.tipoDocumentoPagamento;
	                    $scope.object.contaCorrente = $scope.original.contaCorrente;
	                }

	                $rootScope.loadingProgress = true;
	                $scope.prepararFaturamentoAliquotas().then(() => {
	                    if ($scope.screenMode === 'NEW') {
	                        $scope.object.$save().then(function(data) {
	                            $scope.action = false;
	                            salvarContatos(data);
	                            $scope.notification.success("", "Faturamento salvo com sucesso");
								Faturamento.verificarContaContabilCliente({id: $scope.object.id}).$promise.then(function(data){
									if (data && data.warnings && data.warnings.length > 0) {
										$scope.notification.atencao('', data.warnings[0]);
									}
								});
	                            $state.go('faturamento.exibir', { objectId: data.codigo });
	                            resolve(true);
	                        }).catch(catchError);
	                    } else {
	                        let faturamentoRetencoes = angular.copy($scope.object.faturamentoRetencoes);
	                        Faturamento.update({id: $scope.objectId}, $scope.object).$promise.then(function(data) {
	                            data.faturamentoRetencoes = faturamentoRetencoes;
	                            $scope.carregarDados(data);
	                            if ($scope.centroDeLucroAlterado) {
	                                $scope.notification.atencao(
	                                    "Como o centro de lucro foi alterado, é necessário informar a cidade para faturamento na aba Contrato e tributo ISS na aba Tributos."
	                                );
	                                $scope.centroDeLucroAlterado = false;
	                            }
	                            $scope.action = false;
	                            $scope.notification.success("", "Faturamento atualizado com sucesso");
                                Faturamento.verificarContaContabilCliente({id: $scope.object.id}).$promise.then(function(data){
									if (data && data.warnings && data.warnings.length > 0) {
										$scope.notification.atencao('', data.warnings[0]);
									}
								});
								Faturamento.get({id: $scope.objectId}).$promise.then(function(data) {
                                	$scope.carregarDados(data)
                                	.then($scope.carregarTipoCobranca())
                                	.then($scope.carregarBancos(data.centroFinanceiro.unidade.matriz.codigo))
                                	.then($scope.carregarContasCorrentes(data.contaCorrente.banco.codigo, data.centroFinanceiro.unidade.matriz.codigo))
                                	.then($scope.definirDocumentoFaturamento())
                                	.then($scope.definirTipoDeItens())
                                	.then($scope.definirTipoFornecimentoNF())
                                	.then($scope.carregarEtapas(0))
                                	.then(() => {
                                		$scope.action = false;
                                		resolve(true);
                                	});
                                }).catch(catchError);
	                            resolve(true);
	                        }).catch(function(response) {
	                            $scope.action = false;
	                            $scope.$emit('crud-critical', 'save', response);
	                            $scope.object = angular.copy($scope.original);
	                            $scope.gerenciarClientesDadosPrincipais();
	                            reject(false);
	                        });
	                    }
	                });
	            });
	        };

	        $scope.onEtapasTabSelected = function() {
	            if (!$scope.etapasTabSelectedOnce) {
	                $scope.carregarEtapas(0).then(() => {
	                    $scope.etapasTabSelectedOnce = true;
	                });
	            }
	        };

	        $scope.onNotasFiscaisTabSelected = function() {
	            if (!$scope.notasFiscaisTabSelectedOnce) {
	                $scope.carregarNotasFiscais(0).then(() => {
	                    $scope.notasFiscaisTabSelectedOnce = true;
	                });
	            }
	        };

	        $scope.onNotasDebitoTabSelected = function() {
				if (!$scope.notasDebitoTabSelectedOnce) {
					$scope.carregarNotasDebito(0).then(() => {
						$scope.notasDebitoTabSelectedOnce = true;
					});
				}
			};

	        $scope.carregarDados = function(data) {
	            return new Promise((resolve, reject) => {
	                $rootScope.loadingProgress = false;
	                $scope.object = angular.copy(data);
	                $scope.object.ordem = 'Centro de lucro/Cliente';
	                $scope.original = angular.copy(data);
	                $scope.object.valorLiquido = 0;

	                $scope.object.percentualRetencaoContratual = 0;
	                $scope.object.valorRetencaoContratual = 0;
	                $scope.object.percentualRetencaoAcessoria = 0;
	                $scope.object.valorRetencaoAcessoria = 0;

	                if ($scope.original.faturamentoRetencoes) {
	                    angular.forEach($scope.object.faturamentoRetencoes, function(value, key) {
	                        if (value.tipo.chave === 'contratual') {
	                            $scope.object.percentualRetencaoContratual = value.percentual;
	                        } else if (value.tipo.chave === 'acessoria') {
	                            $scope.object.percentualRetencaoAcessoria = value.percentual;
	                        }
	                    });
	                }

	                $scope.object.faturamentoRetencoes = angular.copy($scope.original.faturamentoRetencoes);

	                $scope.object.valorTributos = 0;

	                $scope.carregarFaturamentoAliquotas($scope.objectId).then(function() {
	                    $scope.ajustarTipoCobrancaCombo(false);
	                    if (!$scope.object.centroFinanceiro.permiteClienteLivre)
	                    	$scope.buscarCentros(undefined, $scope.object.centroFinanceiro.contratoTO);

	                    $rootScope.loadingProgress = true;
	                    MotivoRetornoFaturamento.page(undefined, {faturamento: {codigo: $scope.objectId}}).$promise.then(function(data) {
	                        $rootScope.loadingProgress = false;
	                        $scope.listMotivos = data.content;
	                    }).catch(catchError);

	                    $scope.object.dataVencimento1 = new Date($scope.object.dataVencimento1);
	                    if (angular.isDefined($scope.object.dataVencimento2) && $scope.object.dataVencimento2 != undefined) {
	                        $scope.object.dataVencimento2 = new Date($scope.object.dataVencimento2);
	                    }

	                    if ($scope.screenMode === 'VIEW' && $rootScope.$lastState.name === 'etapasnaofaturadas.novo') {
	                        $scope.index = 6;
	                    } else if ($scope.screenMode === 'VIEW' && ($rootScope.$lastState.name === 'notafiscal.exibir_por_acao' || $rootScope.$lastState.name ===
	                            'notafiscal.imprimir')) {
	                        $scope.index = 7;
	                    }

	                    $scope.procurarCidadeUnidadeContrato();
	                    $scope.object.valorRetencaoContratual = $scope.calcularValorRetencaoContratual();
	                    $scope.object.valorRetencaoAcessoria = $scope.calcularValorRetencaoAcessoria();
	                    CalculadorFaturamento.divideMaterialServico($scope.object, $scope.screenMode);
	                    buscarListaContato(data.codigo);

	                    $scope.objectCopy = angular.copy($scope.object);

	                    resolve(true);
	                });
	            });
	        };

	        //inicio - inicialização das variavéis
	        $scope.percentualMin = true;
	        $scope.percentualMax = true;

	        $scope.changeTitulo = function() {
	            if ($scope.screenMode === 'VIEW') {
	                $scope.subtitulo = 'Exibição';
	            } else if ($scope.screenMode === 'EDIT') {
	                $scope.subtitulo = 'Edição';
	            }
	        };

	        $scope.changeTitulo();

	        //==== PARTE PARA AJUSTAR QUALQUER COISA APÓS ERRO DE BACK-END ====//
	        $scope.$on('crud-critical', function(event, action, data) {
	            if ($scope.screenMode != 'NEW') {
	                $scope.screenMode = 'VIEW';
	            }
	            // USDADOS NA EDIÇÃO POR ABA //
	            $scope.modeDadosPrincipais = false;
	            $scope.modeContrato = false;
	            $scope.modeInformacoes = false;
	            $scope.modeTributos = false;
	            $scope.modeDiscriminacao = false;
	        });


	        /**
	         * Pesquisa do Código do Serviço
	         */
	        $scope.querySearchServico = function(query) {
	            $rootScope.loadingProgress = true;
	            let obj = {};
	            obj.nome = query;
	            obj.situacao = 'Ativo';

	            let servicos = [];
	            let resultado = NotaFiscalFaturamento.qbeST({}, obj).$promise;

	            return resultado.then(function(data) {
	                $rootScope.loadingProgress = false;
	                angular.forEach(data, function(servico) {
	                    servico.siglaNome = servico.sigla + ' - ' + servico.nome;
	                    servicos.push(servico);
	                });
	                return servicos;
	            }).catch(function(response) {
	                console.error(response);
	                $scope.$emit('crud-critical', 'search', response);
	                servicos = [];
	                return servicos;
	            });
	        };

	        $scope.selectedItemChangeService = function(item) {
	            if (item) {
	                if (!$scope.object) {
	                    $scope.object = {};
	                }
	                $scope.object.servicoTipo = {};
	                $scope.object.servicoTipo.codigo = item.codigo;
	                $scope.object.servicoTipo.nome = item.nome;
	                $scope.object.servicoTipo.sigla = item.sigla;
	                $scope.object.servicoTipo.siglaNome = item.sigla + ' - ' + item.nome;
	                $scope.object.servicoTipo.tipoFaturamento = item.tipoFaturamento;

	                $scope.selectedItemServico = item;
	                $scope.selectedItemServico.siglaNome = item.sigla + ' - ' + item.nome;

	                buscarReinfServico(item.codigo);

	                if ($scope.screenMode === 'NEW') {
	                    $scope.procurarCidadeUnidadeContrato();
	                }
	            } else {
					$scope.resultsServico = [];
					delete $scope.selectedItemServico;
	                if ($scope.object) {
						delete $scope.object.servicoTipo;
	                }
	            }

	            gerenciarListaCidadeISS();
	        };

	        $scope.reinfServico = {};

	        function buscarReinfServico(codigoServicoTipo) {
	            TiposServico.buscarReinfServicoByCodServicoTipo({
	                id: codigoServicoTipo
	            }).$promise.then(function(data) {
	                $scope.reinfServico = data;
	            }).catch(function(response) {
	                $scope.$emit('crud-critical', 'search', response);
	            });
	        }


	        let cidadeAdicionadaPeloCodigoServico = false;
	        let codCidadeAdicionadaPeloCodigoServico = undefined;

	        function gerenciarListaCidadeISS() {

	            // adiciona a cidade a combo de Cidade ISS (Aba tributo)
	            if ($scope.object.servicoTipo) {
	                if ($scope.object.servicoTipo.tipoFaturamento === "Habilitado com cidade") {

	                    let codigoCidadeUnidadeMatriz = $scope.cidadeDaUnidadeMatrizDoContrato.codigo;
	                    let temCidadeUnidadeMatriz = false;

	                    angular.forEach($scope.listaCidadesISS, function(value, key) {
	                        if (value && value.objeto && value.objeto.codigo && value.objeto.codigo === codigoCidadeUnidadeMatriz) {
	                            temCidadeUnidadeMatriz = true;
	                        }
	                    });

	                    if (!temCidadeUnidadeMatriz) {
	                        $scope.listaCidadesISS.push({
	                            objeto: {
	                                cidadeUf: $scope.cidadeDaUnidadeMatrizDoContrato.cidadeUf,
	                                codigo: $scope.cidadeDaUnidadeMatrizDoContrato.codigo,
	                                id: $scope.cidadeDaUnidadeMatrizDoContrato.id,
	                                nome: $scope.cidadeDaUnidadeMatrizDoContrato.nome,
	                                uf: $scope.cidadeDaUnidadeMatrizDoContrato.uf
	                            },
	                            texto: $scope.cidadeDaUnidadeMatrizDoContrato.uf + "/" + $scope.cidadeDaUnidadeMatrizDoContrato.nome
	                        });

	                        cidadeAdicionadaPeloCodigoServico = true;
	                        codCidadeAdicionadaPeloCodigoServico = $scope.cidadeDaUnidadeMatrizDoContrato.codigo;
	                    }
	                }

	                // remove a cidade a combo de Cidade ISS (Aba tributo)
	            } else {
	                if (cidadeAdicionadaPeloCodigoServico) {
	                    let index;

	                    angular.forEach($scope.listaCidadesISS, function(value, key) {
	                        if (value && value.objeto && value.objeto.codigo && value.objeto.codigo === codCidadeAdicionadaPeloCodigoServico) {
	                            index = key;
	                        }
	                    });

	                    $scope.listaCidadesISS.splice(index, 1);

	                    cidadeAdicionadaPeloCodigoServico = false;
	                    codCidadeAdicionadaPeloCodigoServico = undefined;
	                }
	            }
	        }

	        $scope.exibirProximoNf = function() {
	            if ($scope.paramsNfs && ($scope.paramsNfs.pageNumber != $scope.paramsNfs.totalPages) && ($scope.paramsNfs.totalPages > 0)) {
	                return true;
	            }
	            return false;
	        };

	        $scope.exibirAnteriorNf = function() {
	            if ($scope.paramsNfs && $scope.paramsNfs.pageNumber > 0) {
	                return true;
	            }
	            return false;
	        };

	        $scope.getPageNf = (page) => {
	            $scope.carregarNotasFiscais(page);
	        };


	        $scope.carregarNotasFiscais = function(numberPage) {
	            return new Promise((resolve, reject) => {
	                $scope.paramsNfs = {};
	                $scope.paramsNfs.sortField = 'numero';
	                $scope.paramsNfs.sortFieldString = false;
	                $scope.paramsNfs.pageSize = 10;
	                $scope.paramsNfs.sortDir = 'desc';
	                $scope.paramsNfs.pageNumber = numberPage;

	                $scope.object.notasFiscais = undefined;

	                Faturamento.notaFiscalPaginado($scope.paramsNfs, {
	                    id: $scope.objectId
	                }).$promise.then(function(data) {

	                    data.contratualValor = ($scope.object.percentualRetencaoContratual * data.valorPagar) / 100;
	                    data.contratualPerc = $scope.object.percentualRetencaoContratual;
	                    data.acessoriaValor = ($scope.object.percentualRetencaoAcessoria * data.valorPagar) / 100;
	                    data.acessoriaPerc = $scope.object.percentualRetencaoAcessoria;

	                    $scope.paramsNfs.pageNumber = data.number;
	                    $scope.paramsNfs.totalOfElements = data.totalElements;
	                    $scope.paramsNfs.totalPages = data.totalPages;

	                    $scope.object.notasFiscais = data.content;

	                    $scope.totalAPagarNF = 0;
	                    $scope.totalBrutoNF = 0;
	                    $scope.totalTributosNF = 0;
	                    $scope.totalDescontosNF = 0;
	                    $scope.totalMultaJurosNF = 0;
	                    $scope.totalValorPagoNF = 0;
	                    $scope.totalValorPermutaNF = 0;
	                    $scope.totalValorRetencoesNF = 0;

	                    data.content.forEach((element) => {
	                        $scope.calcularTotaisNF(element);
	                    });

	                    $scope.exibirLoadNF = false;

	                    resolve(true);
	                }).catch(function(response) {
	                    $scope.exibirLoadNF = false;
	                    $scope.$emit('crud-critical', 'load', response);
	                    reject(true);
	                });
	            });
	        };

	        $scope.getPageNd = (page) => {
				$scope.carregarNotasDebito(page);
			};

			$scope.carregarNotasDebito = function(numberPage) {
				return new Promise((resolve, reject) => {
					$scope.paramsNds = {};
					$scope.paramsNds.sortField = 'numero';
					$scope.paramsNds.sortFieldString = false;
					$scope.paramsNds.pageSize = 10;
					$scope.paramsNds.sortDir = 'desc';
					$scope.paramsNds.pageNumber = numberPage;

					$scope.object.notasDebito = undefined;

					Faturamento.notaDebitoPaginado($scope.paramsNds, {
						id: $scope.objectId
					}).$promise.then(function(data) {

						data.contratualValor = ($scope.object.percentualRetencaoContratual * data.valorPagar) / 100;
						data.contratualPerc = $scope.object.percentualRetencaoContratual;
						data.acessoriaValor = ($scope.object.percentualRetencaoAcessoria * data.valorPagar) / 100;
						data.acessoriaPerc = $scope.object.percentualRetencaoAcessoria;

						$scope.paramsNds.pageNumber = data.number;
						$scope.paramsNds.totalOfElements = data.totalElements;
						$scope.paramsNds.totalPages = data.totalPages;

						$scope.object.notasDebito = data.content;

						$scope.totalAPagarND = 0;
						$scope.totalBrutoND = 0;
						$scope.totalTributosND = 0;
						$scope.totalDescontosND = 0;
						$scope.totalMultaJurosND = 0;
						$scope.totalValorPagoND = 0;
						$scope.totalValorPermutaND = 0;
						$scope.totalValorRetencoesND = 0;

						data.content.forEach((element) => {
							$scope.calcularTotaisND(element);
						});

						$scope.exibirLoadND = false;

						resolve(true);
					}).catch(function(response) {
						$scope.exibirLoadND = false;
						$scope.$emit('crud-critical', 'load', response);
						reject(true);
					});
				});
			};

	        $scope.exibirProximoEtapas = function() {
	            if ($scope.paramsEtapas && ($scope.paramsEtapas.page != $scope.paramsEtapas.totalPages) && ($scope.paramsEtapas.totalPages > 0)) {
	                return true;
	            }
	            return false;
	        };

	        $scope.exibirAnteriorEtapas = function() {
	            if ($scope.paramsEtapas && $scope.paramsEtapas.pageNumber > 0) {
	                return true;
	            }
	            return false;
	        };

	        $scope.getPageEtapas = (page) => {
	            $scope.carregarEtapas(page);
	        };

	        $scope.getPageEtapasNd = (page) => {
				$scope.carregarEtapasNd(page);
			};

	        // CÓDIGO ABAIXO USADO NO CARREGAMENTO DAS ETAPAS
	        $scope.carregarEtapas = function(numberPage) {
	            return new Promise((resolve, reject) => {
	                $rootScope.loadingProgress = true;
	                let obj = {};
	                obj.codigoFaturamento = $scope.objectId;

	                $scope.paramsEtapas = {};
	                $scope.paramsEtapas.sortField = 'numero';
	                $scope.paramsEtapas.sortFieldString = false;
	                $scope.paramsEtapas.pageSize = 10;
	                $scope.paramsEtapas.sortDir = 'desc';
	                $scope.paramsEtapas.pageNumber = numberPage;

	                $scope.object.faturamentoEtapas = undefined;
	                FaturamentoEtapa.page($scope.paramsEtapas, obj).$promise.then(function(data) {
	                    $scope.object.faturamentoEtapas = data.content;

	                    $scope.paramsEtapas.pageNumber = data.number;
	                    $scope.paramsEtapas.totalOfElements = data.totalElements;
	                    $scope.paramsEtapas.totalPages = data.totalPages;

	                    for (let i = 0, j = $scope.object.faturamentoEtapas.length; i < $scope.object.faturamentoEtapas.length; i++, j--) {
	                        $scope.exibirLoadNF = true;
	                        let etapa = $scope.object.faturamentoEtapas[i];
	                        CalculadorFaturamento.dialogEtapa($scope.object, etapa, $scope.getTotalPercentualTributo());
	                    }
	                    $rootScope.loadingProgress = false;
	                    $scope.exibirLoadNF = false;
	                    resolve(true);
	                }).catch(function(response) {
	                    $scope.$emit('crud-critical', 'load', response);
	                    $scope.exibirLoadNF = false;
	                    $scope.object.faturamentoEtapas = [];
	                    reject(false);
	                });
	            });
	        };

	        $scope.carregarEtapasNd = function(numberPage) {
				return new Promise((resolve, reject) => {
					$rootScope.loadingProgress = true;
					let obj = {};
					obj.codigoFaturamento = $scope.objectId;

					$scope.paramsEtapas = {};
					$scope.paramsEtapas.sortField = 'numero';
					$scope.paramsEtapas.sortFieldString = false;
					$scope.paramsEtapas.pageSize = 10;
					$scope.paramsEtapas.sortDir = 'desc';
					$scope.paramsEtapas.pageNumber = numberPage;

					$scope.object.faturamentoEtapas = undefined;
					FaturamentoEtapa.page($scope.paramsEtapas, obj).$promise.then(function(data) {
						$scope.object.faturamentoEtapas = data.content;

						$scope.paramsEtapas.pageNumber = data.number;
						$scope.paramsEtapas.totalOfElements = data.totalElements;
						$scope.paramsEtapas.totalPages = data.totalPages;

						//			$scope.object.notasFiscais = [];

						for (let i = 0, j = $scope.object.faturamentoEtapas.length; i < $scope.object.faturamentoEtapas.length; i++, j--) {
							$scope.exibirLoadND = true;
							let etapa = $scope.object.faturamentoEtapas[i];
							CalculadorFaturamento.dialogEtapa($scope.object, etapa, $scope.getTotalPercentualTributo());

							// Vefirica se a etapa tem associação com uma nota de saida (nota fiscal),
							// acrescentando dados ao array que é utilizado na tabela da aba de notas fiscais.
							//                if (etapa.notaSaida.codigo != undefined) {
							//                    $scope.exibirLoadNF = true;
							//                    Faturamento.notaFiscal(etapa).$promise.then(function(data2) {
							//                        data2.contratualValor = ($scope.object.percentualRetencaoContratual * data2.valorPagar)/100;
							//                        data2.contratualPerc = $scope.object.percentualRetencaoContratual;
							//                        data2.acessoriaValor = ($scope.object.percentualRetencaoAcessoria * data2.valorPagar)/100;
							//                        data2.acessoriaPerc = $scope.object.percentualRetencaoAcessoria;
							//
							//                        $scope.object.notasFiscais.push(data2);
							//
							//                        $scope.calcularTotaisNF(data2);
							//
							//                        $scope.exibirLoadNF = false;
							//                    }).catch(function(response) {
							//                        $scope.exibirLoadNF = false;
							//                        $scope.$emit('crud-critical', 'load', response);
							//                    });
							//                }
						}
						$rootScope.loadingProgress = false;
						$scope.exibirLoadND = false;
						resolve(true);
					}).catch(function(response) {
						$scope.$emit('crud-critical', 'load', response);
						$scope.exibirLoadND = false;
						$scope.object.faturamentoEtapas = [];
						reject(false);
					});
				});
			};

	        // CÓDIGO ABAIXO USADO NO CARREGAMENTO DOS FATURAMENTOS ALIQUOTAS (TRIBUTOS)
	        $scope.carregarFaturamentoAliquotas = function(codFaturamento) {
	            return new Promise(function(resolve, reject) {
	                $rootScope.loadingProgress = true;

					if ($scope.object.tipoFornecimento === 'Só material') {
						resolve({executou: true});
						return;
					}

					if ($scope.object.tipoFornecimento === 'Só material') {
						resolve({executou: true});
						return;
					}

					if ($scope.object.tipoFornecimento === 'Só material') {
						resolve({executou: true});
						return;
					}

					if ($scope.object.tipoFornecimento === 'Só material') {
						resolve({executou: true});
						return;
					}

	                FaturamentoAliquota.page({}, {codigoFaturamento: codFaturamento}).$promise.then(function(data) {
	                    $rootScope.loadingProgress = false;
	                    $scope.listaFaturamentoAliquota = [];
	                    $scope.object.faturamentoAliquotas = [];

	                    $scope.object.faturamentoAliquotas = data.content;

	                    if (angular.isDefined($scope.object.faturamentoAliquotas)) {
	                        angular.forEach($scope.object.faturamentoAliquotas, function(value, key) {
	                            $rootScope.loadingProgress = true;

	                            CategoriaTributo.page(undefined, {
	                                codigo: value.aliquota.codigoCategoriaTributo
	                            }).$promise.then(function(data) {
	                                $rootScope.loadingProgress = false;

	                                let tributo = {};

	                                if ($scope.screenMode != 'NEW') {
	                                    tributo.codigo = value.codigo;
	                                    tributo.codigoFaturamento = value.codigofaturamento;
	                                }

	                                tributo.aliquota = {
	                                    codigo: value.aliquota.codigo,
	                                    percentual: value.aliquota.percentual,
	                                    categoriaTributo: data.content[0]
	                                };

	                                tributo.baseCalculo = value.baseCalculo;
	                                tributo.valor = value.valor;

	                                $scope.listaFaturamentoAliquota.push(tributo);

	                                $scope.gerenciarVariaveisDaTabelaTributos();

	                                if ($scope.screenMode === 'NEW' && $scope.listaFaturamentoAliquota.length === $scope.object.faturamentoAliquotas.length) {
	                                    if (angular.isDefined($scope.montouFaturamentoAnterior)) {
	                                        /**
	                                         * Tratamento para a variavel "$scope.cidUnidade". Responsavel pela combo "Cidade ISS" na aba de tributos
	                                         */
	                                        let cidadeISSDoFaturamentoAnterior = $scope.getCidadeISS();

	                                        if (cidadeISSDoFaturamentoAnterior != undefined) {
	                                            $scope.cidUnidade = angular.copy(cidadeISSDoFaturamentoAnterior);

	                                        } else {
	                                            if ($scope.cidadesIguais) {
	                                                $scope.cidUnidade = angular.copy($scope.cidadeDoContrato);
	                                            } else {
	                                                $scope.cidUnidade = undefined;
	                                            }
	                                        }

	                                        if ($scope.verificaCidadesDivergentes()) {
	                                            $scope.cidadesDivergentes = true;
	                                            $scope.notification.atencao("Atenção",
	                                                "Cidade para faturamento diferente da cidade do ISS. Selecione a cidade correta do ISS.");
	                                        }
	                                    }
	                                }
	                            }).catch(function(response) {
	                                $rootScope.loadingProgress = false;
	                                $scope.$emit('crud-critical', 'load', response);
	                            });
	                        });
	                    }
	                    resolve({executou: true});
	                }).catch(function(response) {
	                    $scope.$emit('crud-critical', 'load', response);
	                    $scope.object.faturamentoEtapas = [];
	                    reject({
	                        executou: false
	                    });
	                });
	            });
	        };

	        //====  EDIÇÃO POR ABA ====//
	        $scope.modeDadosPrincipais = false;
	        $scope.modeContrato = false;
	        $scope.modeInformacoesComplementares = false;
	        $scope.modeTributo = false;
	        $scope.modeDiscriminacao = false;

	        //Para habilitar abas de faturamentos que não estejam na situação 'Aberto' ou 'Cancelado'.
	        $scope.modeEdicaoPosAberta = false;

	        /* ABA DADOS PRINCIPAIS */
	        $scope.editarDadosPrincipais = function() {
	            $scope.action = true;
	            //FIXME: NECESSÁRIO, POIS A DATA VEM COMO LONG E O MATERIAL SÓ ACEITA DATE.
	            $scope.object.dataVencimento1 = new Date($scope.object.dataVencimento1);
	            if (angular.isDefined($scope.object.dataVencimento2) && $scope.object.dataVencimento2 != undefined) {
	                $scope.object.dataVencimento2 = new Date($scope.object.dataVencimento2);
	            }

	            // USADO O PROCEDIMENTO ABAIXO APENAS PARA SUAVISAR A TRANSIÇÃO PARA O USUÁRIO //
	            $rootScope.loadingProgress = true;

	            if ($scope.object && $scope.object.situacao && ($scope.object.situacao !== 'Aberto' && $scope.object.situacao !== 'Cancelado')) {
	                $scope.modeEdicaoPosAberta = true;
	            } else {
	                $scope.modeDadosPrincipais = true;
	            }

	            $scope.original = angular.copy($scope.object);

	            $scope.gerenciarClientesDadosPrincipais();

	            $scope.screenMode = 'EDIT';
	            $scope.changeTitulo();
	            $rootScope.loadingProgress = false;
	            // $scope.buscarCentros($scope.object.cliente, null);

	            $scope.action = false;
	        };

	        $scope.salvarDadosPrincipais = function(form) {
	            $scope.action = true;
	            $rootScope.loadingProgress = true;
	            $scope.modeDadosPrincipais = false;
	            $scope.modeEdicaoPosAberta = false;
	            $rootScope.loadingProgress = false;
	            if ($scope.original.periodo !== $scope.object.periodo) {
	                $scope.buscarContatos()
	                    .then(function(data) {
	                        $scope.montarCampoDiscrimininacaoCentral(data);
	                    }).then(function(data) {
	                        $scope.salvarFaturamento();
	                        $scope.screenMode = 'VIEW';
	                        $scope.changeTitulo();
	                        document.querySelectorAll('input[type=search]')[1].value = '';
	                    });
	            } else {
	                $scope.salvarFaturamento();
	                $scope.screenMode = 'VIEW';
	                $scope.changeTitulo();
	                document.querySelectorAll('input[type=search]')[1].value = '';
	            }
	        };

	        $scope.gerenciarClientesDadosPrincipais = function() {
	            $scope.searchTextCliente = $scope.object.cliente.nome;
	            $scope.selectedItemCliente = $scope.object.cliente;

	            if ($scope.object.subcliente != undefined && angular.isDefined($scope.object.subcliente.nome)) {
	                $scope.searchTextSubCliente = $scope.object.subcliente.nome;
	            } else {
	                delete $scope.searchTextSubCliente;
	            }

	            if ($scope.object.subcliente != undefined && angular.isDefined($scope.object.subcliente)) {
	                if (angular.isDefined($scope.object.subcliente.codigo)) {
	                    $scope.selectedItemSubCliente = $scope.object.subcliente;
	                }
	            } else {
	                delete $scope.selectedItemSubCliente;
				}

				if ($scope.object.centroFinanceiro) {
					$scope.selectedItemCliente = $scope.object.cliente;
					$scope.selectedItemCentroLucro = $scope.object.centroFinanceiro;
				}
	        };

	        /* ABA CONTRATO */
	        $scope.editarContrato = function() {
	            $scope.original = angular.copy($scope.object);
	            $scope.changeTitulo();
	            $scope.carregarCidadesFaturamento()
	                .then(() => {
	                    $scope.screenMode = 'EDIT';
	                    $scope.modeContrato = true;
	                });
	        };

	        $scope.getNumero = function(etapa) {
				if (etapa.notaSaida && etapa.notaSaida.numero)
	        		return etapa.notaSaida.numero;
				if (etapa.notaDebito && etapa.notaDebito.numero)
					return etapa.notaDebito.numero;
	        }

	        $scope.salvarContrato = function(form) {
	            $rootScope.loadingProgress = true;
	            $scope.action = true;
	            form.$setDirty();
	            form.$setUntouched();
	            form.$setPristine();
	            //FIXME: refatorar
	            if ($scope.original.resumo !== $scope.object.resumo) {
	                $scope.buscarContatos().then(function(data) {
	                    $scope.montarCampoDiscrimininacaoInicial(data);
	                    if ($scope.original.cidadePrestacaoServico.nome !== $scope.object.cidadePrestacaoServico.nome) {
	                        $scope.montarCampoDiscrimininacaoCentral(data);
	                    }
	                    $scope.action = false;
	                }).then(function(data) {
	                    $scope.modeContrato = false;
	                    $scope.action = false;
	                    $rootScope.loadingProgress = false;
	                    $scope.salvarFaturamento().then(() => {
	                        $scope.screenMode = 'VIEW';
	                        $scope.changeTitulo();
	                    });
	                });
	            } else if ($scope.original.cidadePrestacaoServico.nome !== $scope.object.cidadePrestacaoServico.nome) {
	                $scope.buscarContatos().then(function(data) {
	                    $scope.montarCampoDiscrimininacaoCentral(data);
	                    $scope.action = false;
	                }).then(function(data) {
	                    $scope.modeContrato = false;
	                    $rootScope.loadingProgress = false;
	                    $scope.action = false;
	                    $scope.salvarFaturamento().then(() => {
	                        $scope.screenMode = 'VIEW';
	                        $scope.changeTitulo();
	                    });
	                });
	            } else {
	                $scope.modeContrato = false;
	                $rootScope.loadingProgress = false;
	                $scope.action = false;
	                $scope.salvarFaturamento().then(() => {
	                    $scope.screenMode = 'VIEW';
	                    $scope.changeTitulo();
	                });
	            }
	        };

	        /* ABA CONTATO */

	        // PARA O MODO DE EDIÇÃO/EXIBIÇÃO
	        $scope.listContato = [];

	        function buscarListaContato(codigoFaturamento) {
	            $scope.listContato = [];

	            let pFatContato = FaturamentoContato.listaByFatCod({
	                id: codigoFaturamento
	            }).$promise;

	            pFatContato.then(function(data) {
	                angular.forEach(data, function(value) {
	                    $scope.listContato.push(value);
	                });
	            }).catch(function(response) {
	                $scope.$emit('crud-critical', 'contrato load', response);
	            });
	        };

	        let fnResetListaContato = function() {
	            $scope.listContato = [];
	            buscarListaContato($scope.object.codigo);
	        };

	        $scope.editarContato = function(ev, item) {
	            let isNovoFat = false;

	            if ($scope.screenMode === 'NEW') {
	                isNovoFat = true;
	            }

	            $mdDialog.show({
	                controller: DialogContato,
	                locals: {
	                    notification: $scope.notification,
	                    faturamento: $scope.object,
	                    listContato: $scope.listContato,
	                    item: item,
	                    fnResetListaContato: fnResetListaContato,
	                    isNovoFat: isNovoFat
	                },
	                templateUrl: 'partials/faturamento/faturamento/partials/dialog_faturamento_contato.html',
	                parent: angular.element(document.body),
	                targetEvent: ev,
	                clickOutsideToClose: false,
	                fullscreen: false
	            });
	        };

	        function DialogContato($scope, $mdDialog, notification, faturamento, item, listContato, fnResetListaContato, isNovoFat) {
	            $scope.title = undefined;
	            $scope.object = {};
	            let codFaturamento = faturamento.id;
	            $scope.desabilitar1 = true;
	            $scope.desabilitar2 = true;

	            if (item === undefined) {
	                $scope.title = "Novo";

	            } else {
	                $scope.title = "Edição";
	                $scope.object = angular.copy(item);

	                //Desabilitar campos de telefones
	                if ($scope.object.contato.telefone1 != undefined) {
	                    $scope.desabilitar1 = false;
	                }
	                if ($scope.object.contato.telefone2 != undefined) {
	                    $scope.desabilitar2 = false;
	                }
	            }

	            carregarRelacionamento();
	            initCampoTipoTelefone();
	            carregarOperadoras();

	            /* CARREGAR COMBOS */
	            function carregarRelacionamento() {
	                RelacionamentoContato.buscarTodos().$promise.then(function(data) {
	                    $scope.listRelacionamento = angular.copy(data);
	                }).catch(function(response) {
	                    $scope.$emit('crud-critical', 'classificacao', response);
	                });
	            };

	            function initCampoTipoTelefone() {
	                let pTipoTelefone = EnumService.enumValues({
	                    pacote: 'br.com.fasolti.corporativo.pessoa.domain',
	                    classe: 'Telefone',
	                    enum: 'TipoTelefone'
	                }).$promise;

	                pTipoTelefone.then(function(data) {
	                    $scope.listTipoTelefone = [];
	                    $scope.listTipoTelefone.push("");
	                    angular.forEach(data, function(value) {
	                        $scope.listTipoTelefone.push(value);
	                    });

	                }).catch(function(response) {
	                    $scope.$emit('crud-critical', 'load', response);
	                });
	            }

	            function carregarOperadoras() {
	                Telefone.buscarOperadora().$promise.then(function(data) {
	                    $scope.listOperadoras = angular.copy(data);
	                }).catch(function(response) {
	                    $scope.$emit('crud-critical', 'classificacao', response);
	                });
	            };

	            /*ON CHANGE*/
	            $scope.desabilitarCamposTel1 = function() {
	                if ($scope.object.contato.telefone1.tipo === undefined || $scope.object.contato.telefone1.tipo === '') {
	                    $scope.desabilitar1 = true;
	                    $scope.object.contato.telefone1 = undefined;
	                } else {
	                    $scope.desabilitar1 = false;
	                }
	            }

	            $scope.desabilitarCamposTel2 = function() {
	                if ($scope.object.contato.telefone2.tipo === undefined || $scope.object.contato.telefone2.tipo === '') {
	                    $scope.desabilitar2 = true;
	                    $scope.object.contato.telefone2 = undefined;
	                } else {
	                    $scope.desabilitar2 = false;
	                }
	            }

	            $scope.confirm = function() {
	                /* Verificar se foi adicionado telefone ou e-mail */
	                if (!$scope.object.contato.telefone1 && !$scope.object.contato.telefone2 && !$scope.object.contato.email) {
	                    notification.atencao("Adicione um telefone ou e-mail.");
	                } else {
	                    if (item === undefined) {
	                        $scope.object.faturamento = faturamento;

	                        if (!listContato || listContato.length === 0) {
	                            $scope.object.ordem = 1;
	                        } else {
	                            let ultimoItem = listContato[listContato.length - 1];
	                            $scope.object.ordem = ultimoItem.ordem + 1;
	                        }

	                        if (isNovoFat) {

	                            if ($scope.object.contato.telefone1) {
	                                $scope.object.contato.telefone1.dddTelefoneRamal = getDDDTelefoneRamal($scope.object.contato.telefone1);
	                            }

	                            if ($scope.object.contato.telefone2) {
	                                $scope.object.contato.telefone2.dddTelefoneRamal = getDDDTelefoneRamal($scope.object.contato.telefone2);
	                            }

	                            listContato.push($scope.object);
	                            notification.success("Contato inserido com sucesso.");

	                        } else {
	                            // Salvar
	                            FaturamentoContato.save($scope.object).$promise.then(function(data) {
	                                fnResetListaContato();
	                                notification.success("Contato inserido com sucesso.");
	                            }).catch(function(response) {
	                                $scope.$emit('crud-critical', 'save', response);
	                                notification.criticalError(response.status, response.statusText, response.data);
	                            });
	                        }

	                    } else {

	                        if (isNovoFat) {
	                            item.faturamento = angular.copy($scope.object.faturamento);
	                            item.contato = angular.copy($scope.object.contato);
	                            item.ordem = angular.copy($scope.object.ordem);

	                            if (item.contato.telefone1) {
	                                item.contato.telefone1.dddTelefoneRamal = getDDDTelefoneRamal(item.contato.telefone1);
	                            }

	                            if (item.contato.telefone2) {
	                                item.contato.telefone2.dddTelefoneRamal = getDDDTelefoneRamal(item.contato.telefone2);
	                            }


	                        } else {
	                            $scope.object.codigo = item.codigo;

	                            //Update
	                            FaturamentoContato.update({
	                                id: $scope.object.codigo
	                            }, $scope.object).$promise.then(function(data) {
	                                notification.success("Contato atualizado com sucesso.");
	                                fnResetListaContato();
	                            }).catch(function(response) {
	                                $scope.$emit('crud-critical', 'update', response);
	                            });
	                        }

	                    }
	                    $scope.hide();
	                }
	            };

	            function getDDDTelefoneRamal(telefone) {
	                if (telefone) {
	                    if (telefone.numero && telefone.ddd) {
	                        return "(" + telefone.ddd + ") " + telefone.numero;
	                    }
	                }
	                return "";
	            }

	            $scope.hide = function() {
	                $mdDialog.hide();
	            };

	            $scope.cancel = function() {
	                $mdDialog.cancel();
	            };
	        } /* Fim na Dialog CONTATO */

	        $scope.excluirContato = function(item) {
	            // Chamada do serviço que criará um dialog de confirmação.
	            DialogConfirm.show(
	                undefined,
	                item,
	                'Remover contato: Confirmação',
	                'Deseja realmente remover este contato (esta operação não poderá ser desfeita)?',
	                'Confirmar',
	                'Cancelar',
	                confirmRemoverContato,
	                cancelRemoverContato);
	        };

	        function confirmRemoverContato(item, fnHide) {

	            if ($scope.screenMode === 'NEW') {

	                let index = $scope.listContato.indexOf(item);
	                $scope.listContato.splice(index, 1);
	                $scope.notification.success("Contato removido com sucesso.");
	                fnHide();

	            } else {
	                FaturamentoContato.remover({
	                    id: item.codigo
	                }).$promise.then(function(data) {

	                    $scope.notification.success("Contato removido com sucesso.");
	                    $scope.listContato = [];
	                    buscarListaContato($scope.object.codigo);
	                    fnHide();

	                }).catch(function(response) {
	                    $scope.$emit('crud-critical', 'dialog', response);
	                    $scope.notification.criticalError(response.status, response.statusText, response.data);
	                    fnHide();
	                });
	            }

	        };

	        function cancelRemoverContato(fnHide) {
	            fnHide();
	        };

	        // PARA O MODO NOVO
	        function buscarContatosDoContrato(codigoContrato) {
	            $scope.listContato = [];
	            let objFaturamento = $scope.object;

	            Contrato.buscarContato({
	                id: codigoContrato
	            }).$promise.then(function(data) {

	                angular.forEach(data, function(value, key) {
	                    let faturamentoContato = {};
	                    faturamentoContato.faturamento = objFaturamento;
	                    faturamentoContato.contato = value.contato;
	                    faturamentoContato.ordem = value.ordem;

	                    $scope.listContato.push(faturamentoContato);
	                });

	            }).catch(function(response) {
	                $scope.$emit('crud-critical', 'AbaContato', response);
	            });
	        }

	        function salvarContatos(faturamento) {
	            if ($scope.listContato && $scope.listContato.length > 0 && faturamento && faturamento.codigo) {

	                angular.forEach($scope.listContato, function(value, key) {
	                    value.faturamento = faturamento;
	                    value.contato.codigo = undefined;

	                    FaturamentoContato.save(value).$promise.then(function(data) {
	                        //salvo
	                    }).catch(function(response) {
	                        $scope.$emit('crud-critical', 'save', response);
	                        $scope.notification.criticalError(response.status, response.statusText, response.data);
	                    });
	                });
	            }
	        }

	        /* ABA INFORMAÇÕES COMPLEMENTARES */
	        $scope.copiaValor = 0;

	        $scope.searchTextServico = {};
	        $scope.selectedItemServico = undefined;

	        $scope.editarInformacoesCompl = function() {
	            $rootScope.loadingProgress = true;
	            $scope.copiaValor = angular.copy($scope.object.valor);
	            $scope.original = angular.copy($scope.object);
	            $scope.screenMode = 'EDIT';
	            $scope.modeInformacoes = true;
	            $scope.changeTitulo();
	            $rootScope.loadingProgress = false;

	            if ($scope.object.servicoTipo)
					$scope.selectedItemServico = $scope.object.servicoTipo.sigla + ' - ' + $scope.object.servicoTipo.nome;

				loadObj();
	        };

	        $scope.salvarInformacoesCompl = function(form) {
	            return new Promise((resolve, reject) => {
	                $scope.action = true;
	                $scope.verificarMudancaValor();
	                $rootScope.loadingProgress = true;
	                form.$setDirty();
	                form.$setUntouched();
	                form.$setPristine();
	                $scope.modeInformacoes = false;
	                $scope.etapasTabSelectedOnce = false;
	                $scope.buscarContatos()
	                    .then(function(data) {
	                        $scope.montarCampoDiscrimininacaoFinal(data);
	                    }).then(function(data) {
	                        //FIXME: refatorar
	                        if ($scope.original.percentualServico !== $scope.object.percentualServico) {
	                            $scope.buscarContatos().then(function(data) {
	                                $scope.montarCampoDiscrimininacaoCentral(data);
	                            }).then(function(data) {
	                                $rootScope.loadingProgress = false;
									if ($scope.object.tipoFornecimento === 'Só material')
										$scope.object.servicoTipo = undefined;
	                                $scope.salvarFaturamento().then(() => {
	                                    $scope.screenMode = 'VIEW';
	                                    $scope.changeTitulo();
	                                    $scope.action = false;
	                                    resolve(true);
	                                });
	                            });
	                        } else {
	                            $rootScope.loadingProgress = false;
	                            $scope.action = false;
	                            $scope.salvarFaturamento().then(() => {
	                                $scope.screenMode = 'VIEW';
	                                $scope.changeTitulo();
	                                resolve(true);
	                            });
	                        }
	                    });

	            });
	        };

	        function cancelamentoAbas(form) {
	            $rootScope.loadingProgress = true;
	            $scope.object = undefined;
	            $scope.screenMode = 'VIEW';
	            $scope.changeTitulo();
	            form.$setDirty();
	            form.$setUntouched();
	            form.$setPristine();
	            $scope.modeDadosPrincipais = false;
	            $scope.modeEdicaoPosAberta = false;
	            $scope.modeDiscriminacao = false;
	            $scope.modeTributos = false;
	            $scope.modeContrato = false;
	            $scope.modeInformacoes = false;
	            return loadObj();
	        }

	        $scope.cancelarInformacoesCompl = function(form) {
	            cancelamentoAbas(form);
	        };

	        $scope.cancelarContrato = function(form) {
	            cancelamentoAbas(form);
	        };

	        $scope.cancelarDadosPrincipais = function(form) {
	            cancelamentoAbas(form).then(() => {
	                //$scope.gerenciarClientesDadosPrincipais();
	                //document.querySelectorAll('input[type=search]')[1].value = '';
	            });
	        };

	        $scope.cancelarDiscriminacao = function(form) {
	            cancelamentoAbas(form);
	        };

	        $scope.cancelarTributos = function(form) {
	            cancelamentoAbas(form);
	        };

			$scope.habilitarTipoDocumentoFaturamento = function(faturamento) {
				if (faturamento.faturamentoEtapas && faturamento.faturamentoEtapas.length > 0)
					return false;

				return true;
			}

	        /* ABA TRIBUTOS */
	        $scope.editarTributos = function() {
	            $scope.modeTributos = true;

	            /*Remoção desta regra SGINV-1179*/

	            /*if ($scope.object.situacao !== 'Aberto' || ($scope.object.notasFiscais && $scope.object.notasFiscais.length > 0)  ) {
	            	$scope.notification.atencao('Este faturamento possui etapa associada. Não é permitido editar a aba "Tributos"');
	            	return;
	            }*/

	            //FIXME: NECESSÁRIO, POIS A DATA VEM COMO LONG E O MATERIAL SÓ ACEITA DATE.
	            $scope.object.dataVencimento1 = new Date($scope.object.dataVencimento1);
	            if (angular.isDefined($scope.object.dataVencimento2) && $scope.object.dataVencimento2 != undefined) {
	                $scope.object.dataVencimento2 = new Date($scope.object.dataVencimento2);
	            }

	            // USADO O PROCEDIMENTO ABAIXO APENAS PARA SUAVISAR A TRANSIÇÃO PARA O USUÁRIO //
	            $rootScope.loadingProgress = true;
	            $scope.original = angular.copy($scope.object);
	            $scope.screenMode = 'EDIT';
	            $scope.changeTitulo();
	            $rootScope.loadingProgress = false;
	            $scope.originalListaFaturamentoAliquota = angular.copy($scope.listaFaturamentoAliquota);

	            $scope.procurarCidadeUnidadeContrato();
	            $scope.cidUnidade = $scope.getCidadeISS();
	        };

	        $scope.atualizarDiscriminacaoFinal = function() {
	            return $scope.buscarContatos().then(function(data) {
	                $scope.montarCampoDiscrimininacaoFinal(data);
	            });
	        };

	        $scope.salvarTributos = function(form) {
	            $rootScope.loadingProgress = true;

	            if ($scope.original.issRetidoCliente !== $scope.object.issRetidoCliente) {
	                $scope.issRetidoCliente = $scope.object.issRetidoCliente;
	            }

	            $scope.atualizarDiscriminacaoFinal().then(function(data) {
	                $scope.salvarFaturamento().then(() => {
	                    $scope.modeTributos = false;
	                    $scope.action = false;
	                    $rootScope.loadingProgress = false;
	                    $scope.screenMode = 'VIEW';
	                    $scope.changeTitulo();
	                });
	            });
	        };

	        /* ABA DESCRIMINAÇÃO */
	        $scope.editarDiscriminacaoServicos = function() {
	            //FIXME: NECESSÁRIO, POIS A DATA VEM COMO LONG E O MATERIAL SÓ ACEITA DATE.
	            $scope.object.dataVencimento1 = new Date($scope.object.dataVencimento1);
	            if (angular.isDefined($scope.object.dataVencimento2) && $scope.object.dataVencimento2 != undefined) {
	                $scope.object.dataVencimento2 = new Date($scope.object.dataVencimento2);
	            }

	            // USADO O PROCEDIMENTO ABAIXO APENAS PARA SUAVIZAR A TRANSIÇÃO PARA O USUÁRIO //
	            $rootScope.loadingProgress = true;
	            $scope.original = angular.copy($scope.object);
	            $scope.screenMode = 'EDIT';
	            $scope.changeTitulo();
	            $scope.modeDiscriminacao = true;
	            $rootScope.loadingProgress = false;
	        };

	        $scope.salvarDiscriminacao = function(form) {
	            $rootScope.loadingProgress = true;
	            form.$setDirty();
	            form.$setUntouched();
	            form.$setPristine();
	            $scope.modeDiscriminacao = false;
	            $rootScope.loadingProgress = false;
	            $scope.salvarFaturamento();
	            $scope.screenMode = 'VIEW';
	            $scope.changeTitulo();
	        };
	        //====  FIM - EDIÇÃO POR ABA ====//



	        $scope.prepararFaturamentoAliquotas = function() {
	            return new Promise((resolve, reject) => {
	                $scope.object.faturamentoAliquotas = [];

	                if (angular.isDefined($scope.listaFaturamentoAliquota) &&
	                    angular.isDefined($scope.listaFaturamentoAliquota.length) &&
	                    $scope.listaFaturamentoAliquota.length > 0) {

	                    $scope.listaFaturamentoAliquota.forEach((value, index) => {
	                        let faturamentoAliquota = {};

	                        if (!CalculadorFaturamento.isCampoVazio(value.codigo)) {
	                            faturamentoAliquota.codigo = value.codigo;
	                        }

	                        faturamentoAliquota.codigofaturamento = $scope.objectId;
	                        faturamentoAliquota.aliquota = {};
	                        faturamentoAliquota.aliquota.codigo = value.aliquota.categoriaTributo.aliquotas[0].codigo;
	                        faturamentoAliquota.aliquota.percentual = value.aliquota.percentual;
	                        faturamentoAliquota.aliquota.codigoCategoriaTributo = value.aliquota.categoriaTributo.codigo;
	                        faturamentoAliquota.baseCalculo = value.baseCalculo;
	                        faturamentoAliquota.valor = value.valor;

	                        $scope.object.faturamentoAliquotas.push(faturamentoAliquota);
	                    });
	                }
	                resolve(true);
	            });
	        };

	        $scope.editar = function() {
	            $state.go('faturamento.editar', {
	                objectId: $scope.objectId
	            });
	        };

	        $scope.exibir = function(item) {
	            $state.go('faturamento.exibir', {
	                objectId: $scope.objectId
	            });
	        };

	        $scope.cancelarNovoFaturamento = function(ev) {
	            if ($scope.wizardStep1.$invalid) {
	                $state.go('faturamento.listar');
	            } else {
	                $mdDialog.show({
	                    controller: DialogCancelarNovoFaturamento,
	                    templateUrl: 'partials/faturamento/faturamento/partials/dialog_cancelar_novo_faturamento.html',
	                    parent: angular.element(document.body),
	                    targetEvent: ev,
	                    clickOutsideToClose: false,
	                    fullscreen: false
	                });
	            }
	        }

	        //==== DIALOG CANCELAR NOVO FATURAMENTO ====//
	        function DialogCancelarNovoFaturamento($scope, $mdDialog) {

	            $scope.hide = function() {
	                $mdDialog.hide();
	            };
	            $scope.cancel = function() {
	                $mdDialog.cancel();
	            };
	            $scope.confirmar = function() {
	                $state.go('faturamento.listar')
	                $mdDialog.hide();
	            }
	        }

	        //inicio - aba dados principais

	        /**
	         * Pesquisa de Clientes
	         */
	        $scope.searchTextCliente = undefined;
	        $scope.searchTextSubCliente = undefined;
	        $scope.selectedItemCliente = undefined;
	        $scope.selectedItemSubCliente = undefined;
	        $scope.resultsClient = [];
	        $scope.resultsSubClient = [];

	        $scope.getClientes = function(query) {
	            $rootScope.loadingProgress = true;
	            let resultCliente = AutocompleteService.getPessoas({
	                nome: query
	            }).$promise;

	            let clientesArray = [];
	            return resultCliente.then(function(clientes) {
	                $rootScope.loadingProgress = false;
	                angular.forEach(clientes, function(cliente) {
	                    clientesArray.push(cliente);
	                })
	                return clientesArray;
	            });
	        }

	        $scope.querySearchCliente = function(query) {
	            $rootScope.loadingProgress = true;
	            let obj = {};
	            obj.nomeFantasiaDocumento = query;
//	            obj.tipoPessoa = 'Pessoa Jurídica';
	            obj.situacao = 'Ativo';
	            obj.ordenacao = 'documento';
				delete obj.clientePrincipalTO;

	            if ($scope.object.centroFinanceiro && !$scope.object.centroFinanceiro.permiteClienteLivre) {
					obj.clientePrincipalTO = {};
					obj.clientePrincipalTO.codigo = -1;
	            }

	            let clientes = [];
	            let resultado = Pessoa.page(undefined, obj).$promise;

	            return resultado.then(function(data) {
	                $rootScope.loadingProgress = false;
	                angular.forEach(data.content, function(cliente) {
	                    clientes.push(cliente);
	                })
	                return clientes;
	            }).catch(function(response) {
	                $scope.$emit('crud-critical', 'search', response);
	                clientes = [];
	                return clientes;
	            });
	        };

	        $scope.changeOrdem = function() {
	            if ($scope.object.ordem === 'Centro de lucro/Cliente') {
	                delete $scope.object.centroFinanceiro;
	                delete $scope.object.cliente;
					delete $scope.object.subcliente;
					delete $scope.selectedItemSubCliente;
					delete $scope.selectedItemCliente;
	            }
	            else if ($scope.object.ordem === 'Cliente/Centro de lucro') {
					delete $scope.object.centroFinanceiro;
	                delete $scope.object.cliente;
					delete $scope.object.subcliente;
					delete $scope.selectedItemSubCliente;
					delete $scope.selectedItemCliente;
	            }
	        };

	        // Início AutoComplete Centro de Lucro
	        $scope.naoTemContrato = false;
	        $scope.searchTextCentroLucro = undefined;
	        $scope.selectedItemCentroLucro = undefined;

	        $scope.fnSelectedItemCentroLucro = function(item) {
	            $scope.action = true;
	            $scope.object.centroFinanceiro = item;

	            function apagarSelecaoCidades() {
	                $scope.object.cidadePrestacaoServico = undefined;
	                $scope.cidades = [];
				}

				if (!item) {
	                delete $scope.object.cliente;
					delete $scope.object.subcliente;
					delete $scope.selectedItemCliente;
	                apagarSelecaoCidades();
	            }
	            else if (!item.contratoTO || (item.contratoTO && !item.contratoTO.codigo)) {
	                $scope.notification.atencao('Este centro de lucro não possui contrato associado');
	                $scope.naoTemContrato = true;
	                apagarSelecaoCidades();
	                $scope.action = false;
	            }
	            else if (item.permiteClienteLivre) {
					$scope.mudarCentroLucro();
					$scope.action = false;
	            }
	            else  {
	                $scope.naoTemContrato = false;
	                Contrato.clienteContrato({id: item.contratoTO.codigo}).$promise.then(function(data) {
						$scope.object.cliente = data;
						delete $scope.object.subcliente;
	                    $scope.mudarCentroLucro();
	                    $scope.action = false;
	                }).catch(function(response) {
	                    $scope.$emit('crud-critical', 'search', response);
	                    apagarSelecaoCidades();
	                    $scope.action = false;
	                });

	                if (item.permiteClienteLivre)
	                	delete $scope.object.subcliente;
	            }

//	            if ($scope.object.ordem === 'Centro de lucro/Cliente') {
//	            	$scope.object.cliente
//	            }
	        };

	        $scope.querySearchCentroLucro = function(query) {
	            $scope.listaCentroLucro = [];

	            let objAtivo = {};
	            objAtivo.identificadorFinal = query;
	            objAtivo.nome = query;
				objAtivo.situacao = 'Ativo';

				//Para Encerrado com faturamento
				let objEncFat = angular.copy(objAtivo);
				objEncFat.situacao = 'Encerrado, mas com faturamento';

				let pCF = CentroFinanceiro.buscarCFs(objAtivo).$promise;
				let pCFEnc = CentroFinanceiro.buscarCFs(objEncFat).$promise;

	            return pCF.then(function(data) {
					$scope.listaCentroLucro.push(...data);

					return pCFEnc.then(function(data2) {
						$scope.listaCentroLucro.push(...data2);
						$scope.listaCentroLucro.sort((cf1, cf2) => { return cf1.codigo - cf2.codigo; });

						return $scope.listaCentroLucro;
					}).catch(function(response2) {
						$scope.$emit('crud-critical', 'search', response2);
						return $scope.listaCentroLucro;
					});

	            }).catch(function(response) {
	                $scope.$emit('crud-critical', 'search', response);
	                return $scope.listaCentroLucro;
				});

	        }; // Fim AutoComplete Centro de Lucro

	        $scope.querySearchSubCliente = function(query) {
	            $rootScope.loadingProgress = true;
	            let obj = {};
	            obj.nomeFantasiaDocumento = query;
	            //        obj.tipoPessoa = 'Pessoa Jurídica';
	            obj.situacao = 'Ativo';
	            obj.ordenacao = 'documento';
	            obj.clientePrincipalTO = {};
	            if (angular.isDefined($scope.object.cliente)) {
	                obj.clientePrincipalTO.codigo = $scope.object.cliente.codigo;
	            } else {
	                delete obj.nomeFantasiaDocumento;
	            }
	            if (angular.isDefined(obj.nomeFantasiaDocumento)) {
	                let resultado = Pessoa.buscarNomeFantasiaDocumento(undefined, obj).$promise;

	                return resultado.then(function(data) {
	                    $rootScope.loadingProgress = false;
	                    $scope.resultsSubClient = [];
	                    $scope.resultsSubClient = data;
	                    return $scope.resultsSubClient;
	                }).catch(function(response) {
	                    $scope.$emit('crud-critical', 'search', response);
	                    $scope.resultsSubClient = [];
	                    return $scope.resultsSubClient;
	                });
	            } else {
	                $rootScope.loadingProgress = false;
	                $scope.resultsSubClient = [];

	            }
	            return $scope.resultsSubClient;
	        };

	        $scope.selectedItemChange = function(item, tipo) {
				if (tipo === 'cliente_nao_do_cl') {
					if (item) {
	                    $scope.object.cliente = {};
	                    $scope.object.subcliente = {};
	                    $scope.object.cliente.codigo = item.codigo;
	                    $scope.object.cliente.nome = item.nome;
	                } else {
	                    document.querySelectorAll('input[type=search]')[1].value = '';
	                    $scope.resultsClient = [];
	                    delete $scope.selectedItemCliente;
	                    delete $scope.object.cliente;
	                    $scope.resultsSubClient = [];
	                    delete $scope.selectedItemSubCliente;
	                    delete $scope.object.subcliente;
	                }
				}
				else if (tipo === 'cliente') {
	                if (item) {
	                    $scope.object.cliente = {};
	                    $scope.object.subcliente = {};
	                    $scope.object.cliente.codigo = item.codigo;
	                    $scope.object.cliente.nome = item.nome;
	                    $scope.centros = [];
	                    $scope.buscarCentros(item, undefined);
	                }
	                else {
	                    document.querySelectorAll('input[type=search]')[1].value = '';
	                    $scope.resultsClient = [];
	                    delete $scope.object.centroFinanceiro;
	                    delete $scope.selectedItemCliente;
	                    delete $scope.object.cliente;
	                    $scope.resultsSubClient = [];
	                    $scope.centros = [];
	                    delete $scope.selectedItemSubCliente;
	                    delete $scope.object.subcliente;
	                }
	            }
	            else if (tipo === 'subcliente') {
	                if (item) {
	                    $scope.object.subcliente = {};
	                    $scope.object.subcliente.codigo = item.codigo;
	                    $scope.object.subcliente.nome = item.nome;
	                }
	                else {
	                    $scope.resultsSubClient = [];
	                    $scope.object.subcliente = {};
	                    $scope.searchTextSubCliente = undefined;
	                    delete $scope.selectedItemSubCliente;
	                    delete $scope.object.subcliente;
	                }
	            }
	        };

	        $scope.buscarCentros = function(cliente, contrato) {
	            new Promise((resolve, reject) => {
	                if (cliente || contrato) {
	                    $scope.action = true;
	                    let obj = {};
	                    obj.contratoTO = {};
	                    obj.contratoTO.cliente = cliente;
	                    if (contrato != null) {
	                        obj.contratoTO.codigo = contrato.codigo;
	                    }
	                    obj.situacao = 'Ativo';
	                    obj.permiteClienteLivre = false;
	                    let pCF = CentroFinanceiro.buscarCFs(obj).$promise;
	                    pCF.then(function(data) {
	                        $scope.centros = data;

	                        if ($scope.centros.length === 0) {
	                            $scope.notification.atencao('Este contrato não possui centro de lucro ativo associado.');
	                        }

	                        if (!($scope.screenMode === 'VIEW' || ($scope.object.situacao !== 'Aberto')) && $scope.centros.length === 1) {
	                            $scope.object.centroFinanceiro = $scope.centros[0];
	                            $scope.mudarCentroLucro();
	                        }
	                        $scope.action = false;
	                        resolve(true);
	                    }).catch(catchError);
	                } else {
	                    $scope.centros = [];
	                    reject(false);
	                }
	            });
	        };

	        $scope.mudarCentroLucro = function() {
	            if (angular.isDefined($scope.object.centroFinanceiro)) {
	                $rootScope.loadingProgress = true;

	                angular.forEach($scope.centros, function(value, key) {
	                    if (value.codigo === $scope.object.centroFinanceiro.codigo) {
	                        $scope.object.centroFinanceiro = angular.copy(value);
	                        return;
	                    }
	                });

	                let resultContrato = Contrato.get({
	                    id: $scope.object.centroFinanceiro.contratoTO.codigo
	                }).$promise;

	                resultContrato.then(function(object) {
	                    $scope.object.centroFinanceiro.contratoTO = object;
	                    $rootScope.loadingProgress = false;

	                    if ($scope.screenMode === 'NEW') {
	                        buscarContatosDoContrato(object.codigo);
	                    }

	                    Unidade.get({
	                            id: $scope.object.centroFinanceiro.unidade.codigo
	                        }).$promise
	                        .then(function(objectUnidade) {
	                            $scope.object.centroFinanceiro.unidade = objectUnidade;
	                            $scope.carregarBancos(objectUnidade.matriz.codigo);
	                            $scope.carregarCidadesFaturamento().then(() => {
	                                $scope.verificarCidade($scope.object.centroFinanceiro.contratoTO);
	                            });
	                        }).catch(function(response) {
	                            $scope.$emit('crud-critical', 'unidade load', response);
	                            $scope.bancoList = [];
	                            $scope.agenciaContaList = [];
	                        });
	                }).catch(function(response) {
	                    $scope.$emit('crud-critical', 'contrato load', response);
	                    $scope.bancoList = [];
	                    $scope.agenciaContaList = [];
	                });

	                $scope.verificarCidade = function(paramContrato) {

	                    let cidadesParaVerificar = [];

	                    let fitro = {};
	                    fitro.codigoContrato = paramContrato.codigo;

	                    ContratoCidadePrestacaoServicos.page(undefined, fitro)
	                        .$promise.then(function(data) {
	                            $scope.cidadesParaVerificar = data.content;

	                            if ($scope.screenMode === 'EDIT') {
	                                if ($scope.object.centroFinanceiro.codigo != $scope.original.centroFinanceiro.codigo) {
	                                    $scope.notification.atencao('O banco/agência deste faturamento não pertencem a mesma empresa ' + $scope.original.centroFinanceiro.unidade.codnome +
	                                        ' do CL selecionado. Será necessário fazer o ajuste em "Informações complementares" ou poderá haver incoerências no faturamento.');

	                                    $scope.centroDeLucroAlterado = true;

	                                    if ($scope.object.cidadePrestacaoServico != undefined) {
	                                        $scope.copiaCidadePrestacaoServico = angular.copy($scope.object.cidadePrestacaoServico);

	                                        let temMesmaCidade = false;

	                                        angular.forEach($scope.cidadesParaVerificar, function(objeto, key) {
	                                            if (objeto.cidade.codigo === $scope.object.cidadePrestacaoServico.codigo) {
	                                                temMesmaCidade = true;
	                                            }
	                                        });

	                                        if (!temMesmaCidade) {
	                                            $scope.object.cidadePrestacaoServico = undefined;
	                                            $scope.notification.atencao(
	                                                'Local de prestação de serviços diferente do contrato do centro de lucro. Selecione o local correto de prestação de serviços.');
	                                        }
	                                    }

	                                } else {
	                                    $scope.centroDeLucroAlterado = false;

	                                    if ($scope.object.cidadePrestacaoServico === undefined && $scope.copiaCidadePrestacaoServico != undefined) {
	                                        $scope.object.cidadePrestacaoServico = angular.copy($scope.copiaCidadePrestacaoServico);
	                                    }
	                                }
	                            }


	                        }).catch(function(response) {
	                            $scope.cidades = [];
	                            console.error('crud-critical', 'dialog', response);
	                            $scope.notification.criticalError(response.status, response.statusText, response.data);
	                        });
	                }
	            }
	        }

	        $scope.$on('changeCentroLucro', function() {
	            $scope.mudarCentroLucro();
	        });
	        //fim - aba aba dados principais


	        //inicio - aba contrato

	        $scope.cidades = [];

	        $scope.setarContratoCidadeFaturamento = function(novaCidade) {
	            $scope.object.cidadePrestacaoServico = novaCidade.cidade;
	            $scope.gerenciarCidadesDivergentes();
	        };


	        $scope.adicionarCidade = function(ev) {

	            $mdDialog.show({
	                controller: DialogControllerCidade,
	                locals: {
	                    contrato: $scope.object.centroFinanceiro.contratoTO,
	                    cidades: $scope.cidades,
	                    listaFaturamentoAliquota: $scope.listaFaturamentoAliquota,
	                    notification: $scope.notification,
	                    action: $scope.setarContratoCidadeFaturamento,
	                },
	                templateUrl: 'partials/faturamento/faturamento/partials/dialog_cidade.html',
	                parent: angular.element(document.body),
	                targetEvent: ev,
	                clickOutsideToClose: false,
	                fullscreen: false
	            });
	        };

	        function DialogControllerCidade($scope, $mdDialog, contrato, cidades, listaFaturamentoAliquota, notification, action) {
	            $scope.object = {};
	            delete $scope.object.uf;
	            $scope.contrato = contrato;
	            $scope.cidades = cidades;
	            $scope.cidadesDialog = [];
	            $scope.notification = notification;
	            $scope.listaFaturamentoAliquota = listaFaturamentoAliquota;
	            $scope.ufCidades = [];

	            $scope.carregarUFs = function() {
	                let filtro = {};
	                filtro.tributoTO = {};
	                filtro.tributoTO.codigo = 4;

	                //FIXME CRIAR METODO PARA OBTER O UF DAS CATEGORIAS COM CIDADE
	                CategoriaTributo.page({
	                        pageSize: 700
	                    }, filtro)
	                    .$promise.then(function(data) {
	                        $scope.categoriasComCidades = data.content;

	                        angular.forEach($scope.categoriasComCidades, function(value, key) {

	                            if (!CalculadorFaturamento.isCampoVazio(value) && !CalculadorFaturamento.isCampoVazio(value.cidade) && !CalculadorFaturamento.isCampoVazio(value.cidade
	                                    .uf)) {
	                                let resultado = $scope.ufCidades.indexOf(value.cidade.uf);

	                                if (!CalculadorFaturamento.isCampoVazio(value) && resultado === -1) {
	                                    $scope.ufCidades.push(value.cidade.uf);
	                                    $scope.ufCidades.sort();
	                                }
	                            }
	                        });

	                    }).catch(function(response) {
	                        console.error('crud-critical', 'dialog', response);
	                        notification.criticalError(response.status, response.statusText, response.data);
	                    });
	            };

	            $scope.$on('changeUF', function() {
	                let filtro = {};
	                filtro.cidade = {};
	                filtro.cidade.uf = $scope.object.uf;
	                $scope.cidadesDialog = [];
	                $scope.object.cidade = undefined;

	                Cidade.getCidadesISS({uf: filtro.cidade.uf})
	                	 .$promise.then(function(data) {
	                	 	$scope.cidadesDialog = data;
	                	 });
	            });

	            $scope.desabilitarBotaoSalvar = function() {
	                if (!CalculadorFaturamento.isCampoVazio($scope.object.uf) && !CalculadorFaturamento.isCampoVazio($scope.object.cidade))
	                    return false;
	                return true;
	            };

	            $scope.salvar = function() {
	                $scope.object.contratoCidadeFaturamento = {
	                    cidade: $scope.object.cidade,
	                    contrato: $scope.contrato
	                };

	                ContratoCidadePrestacaoServicos.save($scope.object.contratoCidadeFaturamento)
	                    .$promise.then(function(data) {
	                        $rootScope.loadingProgress = false;
	                        $scope.cidades.push(data);
	                        action(data);
	                        $scope.hide();
	                        $scope.notification.success("", "Cidade salva com sucesso");
	                    }).catch(function(response) {
	                        console.error('crud-critical', 'dialog', response);
	                        $rootScope.loadingProgress = false;
	                        $scope.notification.criticalError(response.status, response.statusText, response.data);
	                    });
	            };

	            $scope.cancelar = function() {
	                $mdDialog.cancel();
	            };

	            $scope.hide = function() {
	                $mdDialog.hide();
	            };

	            $scope.carregarUFs();
	        }

	        $scope.carregarCidadesFaturamento = function() {
	            return new Promise((resolve, reject) => {
	                if ($scope.cidades === undefined || $scope.cidades.length === 0) {
	                    $scope.action = true;
	                    $rootScope.loadingProgress = true;
	                    ContratoCidadePrestacaoServicos.buscarTodos(undefined, {
	                        codigoContrato: $scope.object.centroFinanceiro.contratoTO.codigo
	                    }).$promise.then(function(data) {
	                        $rootScope.loadingProgress = false;
	                        $scope.action = false;
	                        $scope.cidades = data.content;
	                        resolve(true);
	                    }).catch(function(response) {
	                        $scope.cidades = [];
	                        console.error('crud-critical', 'dialog', response);
	                        $rootScope.loadingProgress = false;
	                        $scope.action = false;
	                        $scope.notification.criticalError(response.status, response.statusText, response.data);
	                        reject(false);
	                    });
	                } else {
	                    resolve(true);
	                }
	            });
	        };

	        $scope.$on('changeCidade', function() {
	            $scope.gerenciarCidadesDivergentes();
	        });

	        /**
	         * Método para fazer as validações da cidade escolhida na combo "Local de prestação dos serviços" da aba de "Contrato".
	         * E popular a combo "Cidade ISS" na aba de tributos.
	         */
	        $scope.gerenciarCidadesDivergentes = function() {
	            $scope.cidadesDivergentes = false;
	            if ($scope.screenMode === 'NEW' && angular.isUndefined($scope.montouFaturamentoAnterior)) {
	                $scope.buscarFaturamentoAnterior();
	                $scope.procurarCidadeUnidadeContrato();
	            } else {
	                if ($scope.verificaCidadesDivergentes()) {
	                    $scope.cidadesDivergentes = true;

	                    if ($scope.screenMode === 'NEW') {
	                        $scope.notification.atencao("Atenção", "Cidade para faturamento diferente da cidade do ISS. Selecione a cidade correta do ISS.");
	                        $scope.procurarCidadeUnidadeContrato();
	                    } else {

	                        //cidade do tributo
	                        let varCidadeIss = $scope.getCidadeISS();

	                        if (varCidadeIss != undefined && $scope.cidadeUnidadeContrato != undefined) {

	                            //Se a cidade do tributo ISS for diferente da cidade do contrato
	                            // e a cidade selecionada no campo "Local de prestação dos serviços" for direfente da cidade do tributo ISS
	                            if (varCidadeIss.codigo != $scope.cidadeUnidadeContrato.codigo) {
	                                if ($scope.object.cidadePrestacaoServico.codigo != varCidadeIss.codigo) {
	                                    $scope.notification.atencao("Atenção", "Não é possível alterar a cidade de faturamento. Por favor, remova o tributo ISS.");
	                                    $scope.cidadesDivergentes = true;
	                                }
	                            } else {
	                                $scope.cidadesDivergentes = false;
	                            }
	                        } else {
	                            $scope.cidadesDivergentes = false;
	                        }
	                    }
	                } else {
	                    $scope.cidadesDivergentes = false;

	                    if ($scope.screenMode === 'NEW' && angular.isDefined($scope.montouFaturamentoAnterior)) {
	                        $scope.procurarCidadeUnidadeContrato();

	                        if ($scope.verificaCidadesDivergentes()) {
	                            $scope.notification.atencao("Atenção", "Cidade para faturamento diferente da cidade do ISS. Selecione a cidade correta do ISS.");
	                        }
	                    }

	                }
	            }
	        };

	        $scope.desabilitarBotaoSalvarFat = function() {
	            if (angular.isUndefined($scope.cidadesDivergentes)) {
	                return false;
	            } else {
	                return $scope.cidadesDivergentes;
	            }
	        };

	        /**
	        	 * Método para verificar se a cidade do tributo ISS é diferente da cidade do "Contrato" OU da cidade do "Local de prestação dos serviços".
	        	 * @return TRUE - caso tenha tributo ISS e a cidade do tributo seja diferente das duas cidades citadas acima.
	        	 * @return FALSE - caso não haja tributo ISS OU caso a cidade do tributo seja igual a uma das duas cidades citadas acima.

	        	 * Atualização 27/07/2018 (SGINV-1781)
	        	 * Atualização 10/08/2018 (SGINV-1788)
	        	   - Agora tambem é verificado se a cidade é da Unidade da Matriz do contrato. (no modo NOVO/EDIÇÃO)
	        	 */
	        $scope.verificaCidadesDivergentes = function() {

	            if (!CalculadorFaturamento.isCampoVazio($scope.listaFaturamentoAliquota) && $scope.listaFaturamentoAliquota.length > 0) {
	                for (let i = 0; i < $scope.listaFaturamentoAliquota.length; i++) {
	                    let categoriaTributo = $scope.listaFaturamentoAliquota[i].aliquota.categoriaTributo;

	                    if (categoriaTributo.tributoTO.codigo === 4) {

	                        if (angular.isDefined($scope.object.cidadePrestacaoServico) && $scope.object.cidadePrestacaoServico != undefined) {
	                            if (categoriaTributo.cidade.nome === $scope.object.cidadePrestacaoServico.nome) {
	                                return false;
	                            }
	                        }

	                        if (angular.isDefined($scope.cidadeDoContrato) && $scope.cidadeDoContrato != undefined) {
	                            if (categoriaTributo.cidade.nome === $scope.cidadeDoContrato.nome) {
	                                return false;
	                            }
	                        }

	                        if ($scope.object.servicoTipo && $scope.object.servicoTipo.tipoFaturamento && $scope.object.servicoTipo.tipoFaturamento === "Habilitado com cidade") {
	                            if ($scope.cidadeDaUnidadeMatrizDoContrato && $scope.cidadeDaUnidadeMatrizDoContrato.codigo === categoriaTributo.cidade.codigo) {
	                                return false;
	                            }
	                        }

	                        return true;
	                    }
	                }
	            }
	            return false;
	        };


	        //fim - aba contrato

	        //inicio - aba informações complementares
	        $scope.tipoCobrancaList = [];

	        $scope.carregarTipoCobranca = function() {
	            return new Promise((resolve, reject) => {
	                if ($scope.tipoCobrancaList.length === 0) {
	                    $scope.action = true;
	                    $rootScope.loadingProgress = true;
	                    TipoCobrancaFaturamento.page({
	                        sortField: 'descricao',
	                        pageSize: 500
	                    }, {}).$promise.then(function(data) {
	                        $scope.tipoCobrancaList = data.content;
	                        $scope.action = false;
	                        $rootScope.loadingProgress = false;
	                        resolve(true);
	                    }).catch(() => {
	                        reject(false);
	                    });
	                } else {
	                    resolve(true);
	                }
	            });
	        };

	        $scope.$on('changetipoCobranca', function() {
	            $scope.ajustarTipoCobrancaCombo(true);
	        });

	        // AJUSTAR O OBJETO, POIS A COMBO MODIFICA APENAS O CÓDIGO, E NÃO O OBJETO.
	        $scope.ajustarObjTipoCobranca = function() {
	            angular.forEach($scope.tipoCobrancaList, function(value, key) {
	                if (value.codigo === $scope.object.tipoCobrancaFaturamento.codigo) {
	                    delete $scope.object.tipoCobrancaFaturamento;
	                    $scope.object.tipoCobrancaFaturamento = angular.copy(value);
	                    return;
	                }
	            });
	        };

	        $scope.ajustarTipoCobrancaCombo = function(modificarValor) {
	            $scope.ajustarObjTipoCobranca();
	            if ($scope.object.tipoCobrancaFaturamento != undefined && $scope.object.tipoCobrancaFaturamento.tipoCobrancaVencimentos != undefined && $scope.object.tipoCobrancaFaturamento
	                .tipoCobrancaVencimentos.length === 1) {
	                $scope.vencimento1 = true;
	                $scope.vencimento2 = true;
	                $scope.manipularDataVcto(0, 'venc1', 'disable1', modificarValor);
	                delete $scope.object.dataVencimento2;
	            } else if ($scope.object.tipoCobrancaFaturamento != undefined && $scope.object.tipoCobrancaFaturamento.tipoCobrancaVencimentos != undefined && $scope.object.tipoCobrancaFaturamento
	                .tipoCobrancaVencimentos.length === 2) {
	                $scope.vencimento1 = true;
	                $scope.vencimento2 = true;
	                $scope.manipularDataVcto(0, 'venc1', 'disable1', modificarValor);
	                $scope.manipularDataVcto(1, 'venc2', 'disable2', modificarValor);
	            }
	        }

	        $scope.manipularDataVcto = function(dt, venc, disable, modificarValor) {
	            //data para saber o tipo de vcto. Ex. 60, sao 60 dias a partir data atual
	            let vctoRegra = $scope.object.tipoCobrancaFaturamento.tipoCobrancaVencimentos[dt];

	            let dataAtual = moment();

	            if ('disable1' === disable) {
	                $scope.vencimento1 = !vctoRegra.solicitaData; //solicita data ou nao. Está vindo como false, então negativar.;
	            } else {
	                $scope.vencimento2 = !vctoRegra.solicitaData; //solicita data ou nao. Está vindo como false, então negativar.;
	            }

	            if (modificarValor === true) {
	                if (vctoRegra.solicitaData === false) {
	                    if (angular.isDefined(vctoRegra.mesReferencia) && vctoRegra.mesReferencia !== undefined) {
	                        if (vctoRegra.mesReferencia === 'PROXIMO') {
	                            dataAtual = dataAtual.startOf("months").add(1, 'months');
	                            vctoRegra.diaVencimento--;
	                        } else if (vctoRegra.mesReferencia === 'ATUAL' && vctoRegra.diaVencimentoTipo !== 'DIA_CORRIDO') {
	                            dataAtual = dataAtual.startOf("months");
	                            vctoRegra.diaVencimento--;
	                        }
	                    }

	                    if (vctoRegra.diaVencimento === 99) {
	                        dataAtual = dataAtual.endOf("months");
	                    } else if (vctoRegra.diaVencimentoTipo === 'DIA_UTIL') {
	                        for (let x = 0; x < vctoRegra.diaVencimento; x++) {
	                            dataAtual = dataAtual.add(1, 'days');
	                            if (dataAtual.format('d') === "0") {
	                                dataAtual = dataAtual.add(1, 'days');
	                            } else if (dataAtual.format('d') === "6") {
	                                dataAtual = dataAtual.add(2, 'days');
	                            }
	                        }
	                    } else if (vctoRegra.diaVencimentoTipo === 'DIA_CORRIDO') {

	                        dataAtual = dataAtual.add(vctoRegra.diaVencimento, 'days');
	                    }

	                    if ('venc1' === venc) {
	                        $scope.object.dataVencimento1 = dataAtual.toDate();

	                    } else {
	                        $scope.object.dataVencimento2 = dataAtual.toDate();
	                    }
	                } else {
	                    if ('venc1' === venc) {
	                        delete $scope.object.dataVencimento1;
	                    } else {
	                        delete $scope.object.dataVencimento2;
	                    }
	                }
	            }
	        };

	        $scope.desabilitarVencimento1 = function() {
	            if ($scope.screenMode === 'VIEW') {
	                return true;
	            }
	            if ($scope.habilitarVenc1) {
	                return false;
	            }
	            return true;
	        };

	        $scope.desabilitarVencimento2 = function() {
	            if ($scope.screenMode === 'VIEW') {
	                return true;
	            }
	            if ($scope.habilitarVenc2) {
	                return false;
	            }
	            return true;
	        };

	        $scope.changeDivMatServ = () => {
				$scope.object.percentualServico = undefined;
				$scope.object.percentualMaterial = undefined;
	            if ($scope.object.tipoFornecimento === 'Só serviço') {
	                $scope.object.percentualServico = 100.00;
	                $scope.object.percentualMaterial = 0.00;
	            } else if ($scope.object.tipoFornecimento === 'Só material') {
					$scope.object.percentualServico = 0.00;
					$scope.object.percentualMaterial = 100.00;
					$scope.searchTextServico = {};
					delete $scope.selectedItemServico;
					delete $scope.object.servicoTipo;
				}
				atualizarValorServicoMaterial();
	        };

	        $scope.inputServicoPercentual = () => {
	        	$scope.object.percentualMaterial = 100 - $scope.object.percentualServico;
	        	atualizarValorServicoMaterial();
	        }

	        $scope.bancoList = [];
	        $scope.carregarBancos = function(codigoPessoa) {

	            if ($scope.bancoList.length === 0) {
	                $scope.action = true;
	                $rootScope.loadingProgress = true;
	                ContaCorrente.buscarChequesContaCorrenteMatriz({
	                    id: codigoPessoa
	                }).$promise.then(function(data) {
	                    $scope.bancoList = data;
	                    $scope.action = false;
	                    $rootScope.loadingProgress = false;
	                }).catch(function(response) {
	                    $scope.$emit('crud-critical', 'contrato load', response);
	                    $scope.bancoList = [];
	                    $scope.agenciaContaList = [];
	                    $scope.action = false;
	                    $rootScope.loadingProgress = false;
	                });
	            }
	        }

	        $scope.$on('changeBanco', function() {
	            if (!$scope.modeTributos && !$scope.modeDiscriminacao) {
	                let banco = $scope.object.contaCorrente.banco;
	                $scope.object.contaCorrente = {};
	                $scope.object.contaCorrente.banco = banco;
//	                delete $scope.object.tipoDocumentoPagamento;

	                let codigoUnidade = $scope.object.centroFinanceiro.unidade.matriz.codigo;

	                if ($scope.object.centroFinanceiro.unidade.tipoUnidade === 'Filial') {
	                    codigoUnidade = $scope.object.centroFinanceiro.unidade.matriz.codigo;
	                }

	                carregarContasCorrentes($scope.object.contaCorrente.banco.codigo, codigoUnidade);
	            }
	        });

	        $scope.agenciaContaList = [];

	        $scope.carregarContasCorrentes = function(codigoBanco, codigoPessoa) {
	            if ($scope.agenciaContaList.length === 0) {
	                carregarContasCorrentes(codigoBanco, codigoPessoa);
	            }
	        };

	        function carregarContasCorrentes(codigoBanco, codigoPessoa) {
	            $rootScope.loadingProgress = true;
	            ContaCorrente.page(undefined, {
	                banco: {
	                    codigo: codigoBanco
	                },
	                pessoa: {
	                    codigo: codigoPessoa
	                }
	            }).$promise.then(function(data) {
	                $scope.agenciaContaList = data.content;
	                $rootScope.loadingProgress = false;
	            }).catch(function(response) {
	                $scope.$emit('crud-critical', 'contrato load', response);
	                $scope.agenciaContaList = [];
	                $rootScope.loadingProgress = false;
	            });
	        }

	        $scope.montarLabelAgenciaConta = function(contaCorrente) {
	            return CalculadorFaturamento.montarCampoAgenciaConta(contaCorrente);
	        };

	        $scope.$watch('object.valor', function() {
	            CalculadorFaturamento.divideMaterialServico($scope.object, $scope.screenMode);
	        });

	        $scope.$watch('object.percentualServico', () => {
				CalculadorFaturamento.divideMaterialServico($scope.object, $scope.screenMode);
	            $scope.percentualMin = $scope.minPercentualServico();
				$scope.percentualMax = $scope.maxPercentualServico();
	        });

	        $scope.$watch('object.valorTributos', () => {
	            CalculadorFaturamento.divideMaterialServico($scope.object, $scope.screenMode);
	        });

	        $scope.minPercentualServico = () => {
	            if ($scope.object.tipoFornecimento === 'Só material') {
	                return $scope.object.percentualServico < 0;
	            }
				return $scope.object.percentualServico < 0.01;
	        };

	        $scope.maxPercentualServico = () => {
	            if ($scope.object.tipoFornecimento === 'Só serviço') {
	                return $scope.object.percentualServico > 100.00;
	            } else if ($scope.object.tipoFornecimento === 'Só material') {
	                return false;
	            }
				return $scope.object.percentualServico >= 99.99;
	        };

	        //GERENCIA O VALOR DO CAMPO
	        $scope.setValorTributo = () => {
	            let valor = 0;

	            if ($scope.object.issRetidoCliente) {
	                valor = $scope.totalValorTributo;
	            } else {
	                valor = $scope.subTotalValorTributo;
	            }

				$scope.object.valorTributos = valor;
				$scope.object.valorTributosAnterior = valor;

	            $scope.changePercentualRetencaoContratual();
	            $scope.changePercentualRetencaoAcessoria();
	        }

	        $scope.inputValorFaturamento = function() {
	            if ($scope.screenMode === 'NEW') {
	                $scope.recalcularDadosTributos();
	            }

	            $scope.atualizarValorRetencaoContratual();
	            $scope.atualizarValorRetencaoAcessoria();
	            atualizarValorServicoMaterial();
	        };

	        function atualizarValorServicoMaterial() {
	        	$scope.object.valorServico = ($scope.object.valor * $scope.object.percentualServico) / 100;
	        	$scope.object.valorMaterial = ($scope.object.valor * $scope.object.percentualMaterial) / 100;
	        }

	        $scope.verificarMudancaValor = function() {
	            if ($scope.copiaValor != $scope.object.valor) {
	                $scope.recalcularDadosTributos();
	            }
	        };

	        $scope.recalcularDadosTributos = function() {
	            angular.forEach($scope.listaFaturamentoAliquota, function(value, key) {
	                value.baseCalculo = angular.copy($scope.object.valor);

	                if (value.aliquota.categoriaTributo.tributoTO.codigo === 6) {
	                    value.baseCalculo = $scope.object.valor * ($scope.object.percentualServico / 100);
	                } else {
	                    value.baseCalculo = angular.copy($scope.object.valor);
	                }

	                value.valor = value.baseCalculo * ($scope.valorPercentualAliquota(value.aliquota.categoriaTributo) / 100);

	            });

	            $scope.gerenciarVariaveisDaTabelaTributos();
	        };

	        $scope.valorPercentualAliquota = function(argCategoriaTributo) {
	            if (angular.isDefined(argCategoriaTributo)) {
	                if (angular.isDefined(argCategoriaTributo.aliquotas)) {
	                    if (angular.isDefined(argCategoriaTributo.aliquotas[0])) {
	                        return argCategoriaTributo.aliquotas[0].percentual;
	                    }
	                }
	            }
	            return 0.00;
	        };

	        $scope.atualizarListaRetencao = function(chave) {
	            //FIXME: Testar depois.
	            //        angular.forEach($scope.object.faturamentoRetencoes, function(value, key) {
	            //            if (chave === 'contratual') {
	            //                value.percentual = $scope.object.percentualRetencaoContratual;
	            //            } else if (chave === 'acessoria') {
	            //                value.percentual = $scope.object.percentualRetencaoAcessoria;
	            //            }
	            //        });
	        }

	        $scope.changePercentualRetencaoContratual = function() {
	            $scope.atualizarValorRetencaoContratual();
	            $scope.object.valorLiquido = $scope.object.valor - $scope.object.valorTributos - $scope.object.valorRetencaoContratual - $scope.object.valorRetencaoAcessoria;

	            $scope.atualizarListaRetencao('contratual');

	            if (isNaN($scope.object.valorLiquido)) {
	                $scope.object.valorLiquido = 0;
	            }

	            if ($scope.object.valorLiquido >= 0) {
	                $scope.colorLiquido = 'black';
	                return $filter('currency')($scope.object.valorLiquido, '', 2);
	            } else {
	                $scope.colorLiquido = 'red';
	                return '(' + $filter('currency')(($scope.object.valorLiquido), '', 2) + ')';
	            }
	        };

	        $scope.changePercentualRetencaoAcessoria = function() {
	            $scope.atualizarValorRetencaoAcessoria();

	            $scope.object.valorLiquido = $scope.object.valor - $scope.object.valorTributos - $scope.object.valorRetencaoContratual - $scope.object.valorRetencaoAcessoria;

	            $scope.atualizarListaRetencao('acessoria');

	            if (isNaN($scope.object.valorLiquido)) {
	                $scope.object.valorLiquido = 0;
	            }

	            if ($scope.object.valorLiquido >= 0) {
	                $scope.colorLiquido = 'black';
	                return $filter('currency')($scope.object.valorLiquido, '', 2);
	            } else {
	                $scope.colorLiquido = 'red';
	                return '(' + $filter('currency')(($scope.object.valorLiquido), '', 2) + ')';
	            }
	        }

	        $scope.atualizarValorRetencaoContratual = function() {
	            $scope.object.valorRetencaoContratual = $scope.calcularValorRetencaoContratual();
	        };

	        $scope.atualizarValorRetencaoAcessoria = function() {
	            $scope.object.valorRetencaoAcessoria = $scope.calcularValorRetencaoAcessoria();
	        };

	        $scope.calcularValorRetencaoContratual = function() {
	            return ($scope.object.valor * $scope.object.percentualRetencaoContratual) / 100;
	        };

	        $scope.calcularValorRetencaoAcessoria = function() {
	            return ($scope.object.valor * $scope.object.percentualRetencaoAcessoria) / 100;
	        };

	        //fim - aba informações complementares


	        //inicio - aba tributos

	        $scope.listaFaturamentoAliquota = [];

	        //Listas
	        $scope.listSN = [{
	            nome: 'Não',
	            valor: false
	        }, {
	            nome: 'Sim',
	            valor: true
	        }];

	        $scope.stringCidadeIss = '';

	        //VARIAVEIS DA TABELA TRIBUTOS:
	        inicializarValoresPercentuaisTributosFaturamento($scope);

	        $scope.gerenciarVariaveisDaTabelaTributos = function() {
	            calcularValoresPercentuaisTributosFaturamento($scope);

	            $scope.mudarCidadeIss();

	            $scope.setValorTributo();

	            //Verificar divergencia entre a cidade do faturamento e a cidade do tributo ISS
	            if ($scope.verificaCidadesDivergentes()) {
	                $scope.cidadesDivergentes = true;
	            } else {
	                $scope.cidadesDivergentes = false;
	            }

	            //Organizar a lista de faturamento aliquota
	            $scope.organizarFaturamentoAliquota();
	        };


	        //Funções auxiliares
	        $scope.mudarCidadeIss = function() {
	            let cidadeIss = '';
	            for (let i = 0; i < $scope.listaFaturamentoAliquota.length; i++) {
	                let codigoTributo = $scope.listaFaturamentoAliquota[i].aliquota.categoriaTributo.tributoTO.codigo;

	                if (codigoTributo === 4) {
	                    cidadeIss = $scope.listaFaturamentoAliquota[i].aliquota.categoriaTributo.cidade.uf + '/' + $scope.listaFaturamentoAliquota[i].aliquota.categoriaTributo.cidade.nome;
	                    $scope.comboCidadeIssDesabilitado = true;
	                }
	            }
	            $scope.stringCidadeIss = cidadeIss;
	        };

	        $scope.$on('comboIssRetido', function() {
	            $scope.setValorTributo();
	            $scope.issRetidoCliente = $scope.object.issRetidoCliente;
	            $scope.atualizarDiscriminacaoFinal();
	        });

	        $scope.getTotalPercentualTributo = function() {
	            let total = 0;

	            angular.forEach($scope.listaFaturamentoAliquota, function(obj, key) {

	                let varPercentual = obj.aliquota.percentual;

	                if (angular.isDefined(varPercentual) && varPercentual !== undefined) {

						let codigoTributo = obj.aliquota.categoriaTributo.tributoTO.codigo;

	                	if (codigoTributo !== 4 || $scope.object.issRetidoCliente) {
	                		total += Number(varPercentual);
						}
	                }

	            });

	            return total;
	        };

	        $scope.temTributoSemIss = function() {
	            let tamanho = $scope.listaFaturamentoAliquota.length;

	            for (let i = 0; i < $scope.listaFaturamentoAliquota.length; i++) {
	                let codigoTributo = $scope.listaFaturamentoAliquota[i].aliquota.categoriaTributo.tributoTO.codigo;

	                if (codigoTributo === 4) {
	                    tamanho--;
	                }
	            }

	            if (tamanho > 0) {
	                return true;
	            } else {
	                return false;
	            }
	        };

	        $scope.temTributoIss = function() {
	            for (let i = 0; i < $scope.listaFaturamentoAliquota.length; i++) {
	                let codigoTributo = $scope.listaFaturamentoAliquota[i].aliquota.categoriaTributo.tributoTO.codigo;

	                if (codigoTributo === 4) {
	                    return true;
	                }
	            }

	            return false;
	        };

	        $scope.getCidadeISS = function() {

	            let varCidadeIss = undefined;

	            angular.forEach($scope.listaFaturamentoAliquota, function(obj, key) {

	                let codigoTributo = obj.aliquota.categoriaTributo.tributoTO.codigo;

	                if (codigoTributo === 4) {
	                    varCidadeIss = angular.copy(obj.aliquota.categoriaTributo.cidade);
	                }

	            });

	            return varCidadeIss;
	        };

	        //Médoto para ordenar a lista de faturamento aliquota no frontend.
	        $scope.organizarFaturamentoAliquota = function() {
	            $scope.listaFaturamentoAliquota.sort(function(a, b) {
	                if (a.aliquota.categoriaTributo.tributoTO.codigo > b.aliquota.categoriaTributo.tributoTO.codigo) {
	                    return -1;
	                }
	                if (a.aliquota.categoriaTributo.tributoTO.codigo < b.aliquota.categoriaTributo.tributoTO.codigo) {
	                    return 1;
	                }
	                return 0;
	            });
	        };

	        $scope.excluirFaturamentoAliquota = function(ev, faturamentoAliquota, isISS) {

	            //        if (isISS && !$scope.cidadesIguais) {
	            //            $scope.cidUnidade = undefined;
	            //        }

	            let index = $scope.listaFaturamentoAliquota.indexOf(faturamentoAliquota);
	            $scope.listaFaturamentoAliquota.splice(index, 1);
	            $scope.gerenciarVariaveisDaTabelaTributos();

	            if ($scope.temTributoIss() === false) {
	                $scope.object.issRetidoCliente = false;
	                $scope.comboCidadeIssDesabilitado = false;
	            }
	        };

	        $scope.desabilitarComboISSRetidoCliente = function() {
	            if ($scope.temTributoIss() === false)
	                return true;
	            return false;
	        };

	        $scope.novoFaturamentoAliquota = function(ev, isISS) {
	            $mdDialog.show({
	                controller: DialogControllerEditarFaturamentoAliquota,
	                locals: {
	                    isNew: true,
	                    faturamentoAliquota: undefined,
	                    listaFaturamentoAliquota: $scope.listaFaturamentoAliquota,
	                    isISS: isISS,
	                    Tributo: Tributo,
	                    CategoriaTributo: CategoriaTributo,
	                    faturamento: $scope.object,
	                    cidadeFaturamento: $scope.cidUnidade,
	                    variacaoTributo: $scope.variacaoTributo,
	                    notification: $scope.notification,
	                    loadingProgress: $rootScope.loadingProgress,
	                    gerenciarVarTabela: $scope.gerenciarVariaveisDaTabelaTributos
	                },
	                templateUrl: 'partials/faturamento/faturamento/partials/dialog_novo_tributo.html',
	                parent: angular.element(document.body),
	                targetEvent: ev,
	                clickOutsideToClose: false,
	                fullscreen: false
	            });
	        };

	        $scope.novoFaturamentoAliquotaISS = function(ev, isISS) {

	            if ($scope.cidadesIguais || (!$scope.cidadesIguais && $scope.cidUnidade != undefined)) {
	                $scope.novoFaturamentoAliquota(ev, isISS);
	            } else {
	                $scope.notification.atencao("Atenção", "Escolha uma cidade no campo 'Cidade ISS'");
	            }

	        };

	        // FUNÇÃO PARA REMOVER O FATURAMENTO.
	        $scope.removerFaturamento = function(ev) {
	            $mdDialog.show({
	                controller: DialogControllerRemoverFaturamento,
	                locals: {
	                    faturamento: $scope.object,
	                    resource: Faturamento,
	                    notification: $scope.notification,
	                    loadingProgress: $rootScope.loadingProgress
	                },
	                templateUrl: 'partials/faturamento/faturamento/partials/dialog_remocao_faturamento.html',
	                parent: angular.element(document.body),
	                targetEvent: ev,
	                clickOutsideToClose: false,
	                fullscreen: false
	            });
	        };

	        //==== DIALOG REMOVER FATURAMENTO ====//
	        function DialogControllerRemoverFaturamento($scope, $timeout, $mdDialog, $state, faturamento, resource, notification, loadingProgress) {
	            $scope.object = faturamento;
	            $scope.action = false;

	            $scope.confirm = function() {
	                loadingProgress = true;
	                $scope.action = true;
	                $scope.object.$delete({
	                        id: $scope.object.codigo
	                    })
	                    .then(function(result, status, headers, config) {
	                        $timeout(function() {
	                            let title = "";
	                            let message = 'Faturamento ' + $scope.object.numero + ' removido com sucesso';
	                            notification.success(title, message);
	                            $state.go('faturamento.listar');
	                            delete $scope.object;
	                            $scope.cancel();
	                            loadingProgress = false;
	                            $scope.action = false;
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


	        $scope.editarFaturamentoAliquota = function(ev, faturamentoAliquota) {
	            //Verifica se o tributo é ISS.
	            if (faturamentoAliquota.aliquota.categoriaTributo.tributoTO.codigo === 4) {
	                $scope.cidUnidade = angular.copy(faturamentoAliquota.aliquota.categoriaTributo.cidade);
	            }

	            $mdDialog.show({
	                controller: DialogControllerEditarFaturamentoAliquota,
	                locals: {
	                    isNew: false,
	                    faturamentoAliquota: faturamentoAliquota,
	                    listaFaturamentoAliquota: $scope.listaFaturamentoAliquota,
	                    isISS: false,
	                    Tributo: Tributo,
	                    CategoriaTributo: CategoriaTributo,
	                    faturamento: $scope.object,
	                    cidadeFaturamento: $scope.cidUnidade,
	                    variacaoTributo: $scope.variacaoTributo,
	                    notification: $scope.notification,
	                    gerenciarVarTabela: $scope.gerenciarVariaveisDaTabelaTributos
	                },
	                templateUrl: 'partials/faturamento/faturamento/partials/dialog_novo_tributo.html',
	                parent: angular.element(document.body),
	                targetEvent: ev,
	                clickOutsideToClose: false,
	                fullscreen: false
	            });
	        };


	        /**
	         * CONTROLLER COM SCOPE ISOLADO PARA O DIALOG DA ABA TRIBUTOS
	         */
	        function DialogControllerEditarFaturamentoAliquota($scope, $mdDialog, isNew, faturamentoAliquota, listaFaturamentoAliquota, isISS, Tributo, CategoriaTributo, faturamento,
	            cidadeFaturamento, variacaoTributo, notification, gerenciarVarTabela) {
	            $scope.isNew = isNew;
	            $scope.faturamentoAliquota = faturamentoAliquota;
	            $scope.listaDeTributos = [];
	            $scope.listaCategoriaTributo = [];
	            $scope.listaFaturamentoAliquota = listaFaturamentoAliquota;
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

	            $scope.$watch('copiaFaturamentoAliquota.baseCalculo', function() {
	                if ($scope.iniciandoDialog) {
	                    $scope.iniciandoDialog = false;
	                } else {
	                    if ($scope.variavelValido($scope.copiaFaturamentoAliquota.aliquota)) {
	                        $scope.copiaFaturamentoAliquota.aliquota.percentual = $scope.getPercentualAliquota($scope.copiaFaturamentoAliquota.aliquota.categoriaTributo);

	                        $scope.copiaFaturamentoAliquota.valor = $scope.copiaFaturamentoAliquota.baseCalculo * ($scope.getPercentualAliquota($scope.copiaFaturamentoAliquota.aliquota
	                            .categoriaTributo) / 100);
	                        $scope.copiaValor = angular.copy($scope.copiaFaturamentoAliquota.valor);

	                        $scope.categoriaTributoOriginal = angular.copy($scope.copiaFaturamentoAliquota.aliquota.categoriaTributo);
	                    }
	                }
	            });

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
	                                return $scope.getPercentualAliquota(a) - $scope.getPercentualAliquota(b);
	                            });

	                            $scope.listaCategoriaTributo = listaOrdenada;
	                        }

	                    }).catch(function(response) {
	                        console.error('crud-critical', 'dialog', response);
	                        notification.criticalError(response.status, response.statusText, response.data);
	                    });
	            }


	            //Caso o tributo não seja novo, verificar se o tributo é ISS. Se for, modifica a variavel "$scope.isISS"
	            if ($scope.faturamentoAliquota !== undefined) {
	                if ($scope.faturamentoAliquota.aliquota.categoriaTributo.tributoTO.codigo === 4) {
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
	                                    if (objTributo.codigo === codTributo) {
	                                        indexTributo = $scope.listaDeTributos.indexOf(objTributo);
	                                    }
	                                } else {
	                                    if (objTributo.codigo === codTributo && objTributo.codigo != $scope.faturamentoAliquota.aliquota.categoriaTributo.tributoTO.codigo) {
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

	            $scope.$on('comboCategoriaTributo', function() {
	                if ($scope.variavelValido($scope.copiaFaturamentoAliquota.aliquota)) {
	                    $scope.copiaFaturamentoAliquota.aliquota.percentual = $scope.getPercentualAliquota($scope.copiaFaturamentoAliquota.aliquota.categoriaTributo);
	                }

	                if (isNew || $scope.categoriaTributoOriginal.codigo != $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo.codigo) {

	                    //Calcular Base de cálculo e valor do tributo
	                    $scope.copiaFaturamentoAliquota.baseCalculo = angular.copy(faturamento.valor);

	                    if ($scope.objetoTributo.codigo === 6) {
	                        $scope.copiaFaturamentoAliquota.baseCalculo = $scope.faturamento.valor * ($scope.faturamento.percentualServico / 100);
	                    } else {
	                        $scope.copiaFaturamentoAliquota.baseCalculo = angular.copy($scope.faturamento.valor);
	                    }

	                    $scope.copiaFaturamentoAliquota.valor = $scope.copiaFaturamentoAliquota.baseCalculo * ($scope.getPercentualAliquota($scope.copiaFaturamentoAliquota.aliquota
	                        .categoriaTributo) / 100);
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
	                // FIXME: Verificar por que aliquota não vem codigo.
	                $scope.copiaFaturamentoAliquota.baseCalculo = $scope.copiaFaturamentoAliquota.baseCalculo;
	                $scope.copiaFaturamentoAliquota.valor = $scope.copiaFaturamentoAliquota.valor;
	                $scope.copiaFaturamentoAliquota.aliquota.codigo = $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo.aliquotas[0].codigo;
	                $scope.copiaFaturamentoAliquota.codigoTributo = $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo.tributoTO.codigo;
	                $scope.copiaFaturamentoAliquota.codigoCategoriaTributo = $scope.copiaFaturamentoAliquota.aliquota.categoriaTributo.codigo;
	                $scope.copiaFaturamentoAliquota.percentualTributo = $scope.copiaFaturamentoAliquota.aliquota.percentual;

	                if (isNew) {
	                    $scope.listaFaturamentoAliquota.push($scope.copiaFaturamentoAliquota);
	                } else {
	                    angular.merge($scope.faturamentoAliquota, $scope.copiaFaturamentoAliquota);
	                }
	                gerenciarVarTabela();
	                $mdDialog.hide();

	            };

	            /**
	             * Retorna percentual de uma aliquota relacionada ao "Categoria Tributo" passado pelo argumento
	             */
	            $scope.getPercentualAliquota = function(argCategoriaTributo) {

	                if (!$scope.desabilitarCampo(argCategoriaTributo)) {
	                    if (!$scope.desabilitarCampo(argCategoriaTributo.aliquotas)) {
	                        if (!$scope.desabilitarCampo(argCategoriaTributo.aliquotas[0])) {
	                            return argCategoriaTributo.aliquotas[0].percentual;
	                        }
	                    }
	                }
	                return 0.00;
	            };

	        } // fim Dialog

	        $scope.mudarCidUnidade = function(paramCidade) {
	            $scope.cidUnidade = paramCidade;

	            for (let i = 0; i < $scope.listaFaturamentoAliquota.length; i++) {
	                let codigoTributo = $scope.listaFaturamentoAliquota[i].aliquota.categoriaTributo.tributoTO.codigo;

	                if (codigoTributo === 4) {
	                    $scope.excluirFaturamentoAliquota(undefined, $scope.listaFaturamentoAliquota[i], true);
	                }
	            }
	        };

	        //fim - aba tributos


	        //inicio - aba etapas

	        $scope.object.faturamentoEtapas = [];

	        //adicionados parametros para novaEtapa, ediçao e exibicao
	        $scope.novaEtapa = function(ev, etapa, percentualTributos, motivoRetornoSalvo) {
				$mdDialog.show({
	                controller: DialogControllerEtapa,
	                locals: {
	                    mode: 'NEW',
	                    etapa: {},
	                    objetoFaturamento: $scope.object,
	                    percentualTributos: percentualTributos,
	                    listaEtapas: $scope.object.faturamentoEtapas,
	                    objectId: $scope.objectId,
	                    notification: $scope.notification,
	                    fnRemover: $scope.excluirEtapa,
	                    fnExibir: $scope.exibirEtapa,
	                    motivoRetornoSalvo: motivoRetornoSalvo,
	                    fn_listaContatos: $scope.buscarContatos,
	                    totalValorTributoEtapa: $scope.totalValorTributo,
	                    subTotalValorTributoEtapa: $scope.subTotalValorTributo,
	                    issRetencaoCliente: $scope.object.issRetidoCliente,
	                    subtotalPercentualTributo: $scope.subtotalPercentualTributo,
	                    totalPercentualTributo: $scope.totalPercentualTributo,
	                    listaFaturamentoAliquota: $scope.listaFaturamentoAliquota,
	                    issRetidoCliente: $scope.object.issRetidoCliente
	                },
	                templateUrl: 'partials/faturamento/faturamento/partials/dialog_etapa.html',
	                parent: angular.element(document.body),
	                targetEvent: ev,
	                clickOutsideToClose: false,
	                fullscreen: false
	            });
	        };

	        $scope.editarEtapa = function(ev, etapa, percentualTributos, motivoRetornoSalvo) {
	            $mdDialog.show({
	                controller: DialogControllerEtapa,
	                locals: {
	                    mode: 'EDIT',
	                    etapa: etapa,
	                    objetoFaturamento: $scope.object,
	                    percentualTributos: percentualTributos,
	                    listaEtapas: $scope.object.faturamentoEtapas,
	                    objectId: etapa.codigo,
	                    notification: $scope.notification,
	                    fnRemover: $scope.excluirEtapa,
	                    fnExibir: $scope.exibirEtapa,
	                    motivoRetornoSalvo: motivoRetornoSalvo,
	                    fn_listaContatos: $scope.buscarContatos,
	                    totalValorTributoEtapa: $scope.totalValorTributo,
	                    subTotalValorTributoEtapa: $scope.subTotalValorTributo,
	                    issRetencaoCliente: $scope.object.issRetidoCliente,
	                    subtotalPercentualTributo: $scope.subtotalPercentualTributo,
	                    totalPercentualTributo: $scope.totalPercentualTributo,
	                    listaFaturamentoAliquota: $scope.listaFaturamentoAliquota,
	                    issRetidoCliente: $scope.object.issRetidoCliente
	                },
	                templateUrl: 'partials/faturamento/faturamento/partials/dialog_etapa.html',
	                parent: angular.element(document.body),
	                targetEvent: ev,
	                clickOutsideToClose: false,
	                fullscreen: false
	            });
	        };

	        $scope.editarEmitirNota = function(ev, etapa) {
	            Etapasnaofaturadas.validarCL({
	                    id: etapa.codigo
	                }).$promise
	                .then(function(data) {
	                    if (data.validadoCL) {
	                    	if ($scope.object.tipoDocumentoFaturamento == 'Nota fiscal') {
								$state.go('etapasnaofaturadas.novo', {
									objectIdEtapa: etapa.codigo,
									acao: 'FAT',
									from: $scope.object.codigo
								});
	                        }
	                        else {
								$state.go('notadebito.novo', {
									objectIdEtapa: etapa.codigo,
									acao: 'FAT',
									from: $scope.object.codigo
								});
	                        }
	                    } else {
	                        $scope.notification.error('', `O centro de lucro ${data.centroFinanceiro.identificadorFinal} não está ativo. Criação de NF não permitida.`);
	                    }
	                }).catch(function(response) {
	                    console.error(response);
	                    $scope.$emit('crud-critical', 'load', response);
	                });
	        };

	        $scope.exibirEtapa = function(ev, etapa, percentualTributos, motivoRetornoSalvo) {
	            $mdDialog.show({
	                controller: DialogControllerEtapa,
	                locals: {
	                    mode: 'VIEW',
	                    etapa: etapa,
	                    objetoFaturamento: $scope.object,
	                    percentualTributos: percentualTributos,
	                    listaEtapas: $scope.object.faturamentoEtapas,
	                    objectId: etapa.codigo,
	                    notification: $scope.notification,
	                    fnRemover: $scope.excluirEtapa,
	                    fnExibir: $scope.exibirEtapa,
	                    motivoRetornoSalvo: motivoRetornoSalvo,
	                    fn_listaContatos: $scope.buscarContatos,
	                    totalValorTributoEtapa: $scope.totalValorTributo,
	                    subTotalValorTributoEtapa: $scope.subTotalValorTributo,
	                    issRetencaoCliente: $scope.object.issRetidoCliente,
	                    subtotalPercentualTributo: $scope.subtotalPercentualTributo,
	                    totalPercentualTributo: $scope.totalPercentualTributo,
	                    listaFaturamentoAliquota: $scope.listaFaturamentoAliquota,
	                    issRetidoCliente: $scope.object.issRetidoCliente
	                },
	                templateUrl: 'partials/faturamento/faturamento/partials/dialog_etapa.html',
	                parent: angular.element(document.body),
	                targetEvent: ev,
	                clickOutsideToClose: false,
	                fullscreen: false
	            });
	        };

	        //    $scope, $controller, $state, $mdDialog, $rootScope, FaturamentoEtapa, CalculadorFaturamento, isNew, etapa, objetoFaturamento, percentualTributos, listaEtapas
	        // adicionados a lista de contato do faturamento, valor total e sub total de tributos para a parte "final" da etapa
	        function DialogControllerEtapa($scope, $mdDialog, mode, etapa, objetoFaturamento, percentualTributos, listaEtapas, objectId, notification,
	        	fnRemover, fnExibir, motivoRetornoSalvo, fn_listaContatos, totalValorTributoEtapa,
	        	subTotalValorTributoEtapa, issRetencaoCliente, subtotalPercentualTributo, totalPercentualTributo,
	        	listaFaturamentoAliquota, issRetidoCliente
	        ) {

	            $scope.screenMode = mode;
	            $scope.etapa = etapa;
	            $scope.objetoFaturamento = objetoFaturamento;
	            $scope.listaEtapas = listaEtapas;
	            $scope.objectId = objectId;
	            $scope.notification = notification;
	            $scope.motivoRetornoSalvo = motivoRetornoSalvo;
	            $scope.fn_listaContatos = fn_listaContatos;
	            $scope.totalValorTributo = totalValorTributoEtapa;
	            $scope.subTotalValorTributo = subTotalValorTributoEtapa;
	            $scope.subtotalPercentualTributo = subtotalPercentualTributo;
	            $scope.totalPercentualTributo = totalPercentualTributo;
	            $scope.issRetencaoCliente = issRetencaoCliente;
	            $scope.listaFaturamentoAliquota = listaFaturamentoAliquota;
	            $scope.issRetidoCliente = issRetidoCliente;

	            if (issRetencaoCliente) {
	                $scope.percentualTributos = totalPercentualTributo;
	            } else {
	                $scope.percentualTributos = subtotalPercentualTributo;
	            }

	            calcularValoresPercentuaisTributosFaturamento($scope);

	            $scope.ultimaEtapa = function() {
	                if ($scope.etapa.codigo === $scope.listaEtapas[0].codigo) {
	                    return true;
	                } else {
	                    return false;
	                }
	            };

	            $scope.removerEtapaDialog = function() {
	                fnRemover(undefined, $scope.etapa, true)
	            };

	            $scope.cancelarEtapa = function(event) {
	                let chaveMotivo = 'cancelar-etapa';
	                let titulo = 'Motivo de retorno - Cancelamento de etapa';

	                // Função que será executada no confirmar no dialog de motivo de retorno.
	                $scope.confirmarMotivo = function(motivoRetornoDocumento, fn_Hide) {
	                    // MotivoRetornoDocumentoFaturamentoTO
	                    $scope.motivoRetornoDocumentoEtapa = {};


	                    $scope.motivoRetornoDocumentoEtapa.faturamentoEtapa = {
	                        codigo: $scope.etapa.codigo
	                    };
	                    $scope.motivoRetornoDocumentoEtapa.motivoRetornoDocumento = motivoRetornoDocumento;

	                    $rootScope.loadingProgress = true;

	                    MotivoRetornoEtapa.save($scope.motivoRetornoDocumentoEtapa)
	                        .$promise.then(function(data) {
	                            $rootScope.loadingProgress = false;
	                            $scope.notification.success("", "Etapa cancelada com sucesso");
	                            fn_Hide();
	                            fnExibir(event, $scope.etapa, $scope.percentualTributos, true);
	                        }).catch(function(response) {
	                            console.error('crud-critical', 'dialog', response);
	                            $rootScope.loadingProgress = false;
	                            $scope.notification.criticalError(response.status, response.statusText, response.data);
	                        });
	                };

	                // Função que será executada no cancelar no dialog de motivo de retorno.
	                $scope.cancelarMotivo = function(fn_Hide) {
	                    fn_Hide();
	                    fnExibir(event, $scope.etapa, $scope.percentualTributos);
	                };

	                // Passar para o serviço, as funções que serão chamadas ao confirmar ou cancelar o motivo de retorno.
	                MotivoRetornoDialog.show(event, chaveMotivo, titulo, $scope.confirmarMotivo, $scope.cancelarMotivo);
	            };
	        }


	        $scope.excluirEtapa = function(ev, etapa, popup) {
	            $mdDialog.show({
	                controller: DialogControllerRemoverEtapa,
	                locals: {
	                    mode: $scope.screenMode,
	                    etapa: etapa,
	                    objetoFaturamento: $scope.object,
	                    listaEtapas: $scope.object.faturamentoEtapas,
	                    objectId: etapa.codigo,
	                    notification: $scope.notification,
	                    exibirEtapa: $scope.exibirEtapa,
	                    popup: popup,
	                    ev: ev
	                },
	                templateUrl: 'partials/faturamento/faturamento/partials/dialog_remocao_etapa.html',
	                parent: angular.element(document.body),
	                targetEvent: ev,
	                clickOutsideToClose: false,
	                fullscreen: false
	            });
	        };

	        //==== DIALOG REMOVER FATURAMENTO ETAPA ====//
	        function DialogControllerRemoverEtapa($scope, $mdDialog, mode, etapa, objetoFaturamento, listaEtapas, objectId, notification, exibirEtapa, popup, ev) {
	            $scope.screenMode = mode;
	            $scope.etapa = etapa;
	            $scope.objetoFaturamento = objetoFaturamento;
	            $scope.listaEtapas = listaEtapas;
	            $scope.objectId = objectId;
	            $scope.notification = notification;

	            $scope.confirm = function() {
	                $rootScope.loadingProgress = true;

	                FaturamentoEtapa.delete({
	                        id: etapa.codigo
	                    })
	                    .$promise.then(function(object) {
	                        $rootScope.loadingProgress = false;
	                        let index = $scope.objetoFaturamento.faturamentoEtapas.indexOf(etapa);
	                        $scope.objetoFaturamento.faturamentoEtapas.splice(index, 1);
	                        $scope.notification.success("", "Etapa removida com sucesso");
	                    }).catch(function(response) {
	                        $rootScope.loadingProgress = false;
	                        console.error('crud-critical', 'dialog', response);
	                        notification.criticalError(response.status, response.statusText, response.data);
	                    });

	                $mdDialog.hide();
	            };

	            $scope.hide = function() {
	                $mdDialog.hide();
	                if (popup) {
	                    exibirEtapa(ev, etapa, undefined);
	                }
	            };
	            $scope.cancel = function() {
	                $mdDialog.cancel();
	                if (popup) {
	                    exibirEtapa(ev, etapa, undefined);
	                }
	            };
	        }

	        $scope.totalValorEtapa = function() {
	            let total = 0;
	            if (!CalculadorFaturamento.isCampoVazio($scope.object.faturamentoEtapas)) {
	                for (let i = 0; i < $scope.object.faturamentoEtapas.length; i++) {
	                    let varValor = $scope.object.faturamentoEtapas[i].valor;

	                    if (!CalculadorFaturamento.isCampoVazio(varValor)) {
	                        total += Number(varValor);
	                    }
	                }
	            }

	            return total;
	        };

	        $scope.totalValorServicoEtapa = function() {
	            let total = 0;

	            if (!CalculadorFaturamento.isCampoVazio($scope.object.faturamentoEtapas)) {
	                for (let i = 0; i < $scope.object.faturamentoEtapas.length; i++) {
	                    let varValor = $scope.object.faturamentoEtapas[i].valorServico;

	                    if (!CalculadorFaturamento.isCampoVazio(varValor)) {
	                        total += Number(varValor);
	                    }
	                }
	            }


	            return total;
	        };

	        $scope.totalValorLiquidoEtapa = function() {
	            let total = 0;

	            if (!CalculadorFaturamento.isCampoVazio($scope.object.faturamentoEtapas)) {
	                for (let i = 0; i < $scope.object.faturamentoEtapas.length; i++) {
	                    let varValor = $scope.object.faturamentoEtapas[i].valorLiquido;

	                    if (!CalculadorFaturamento.isCampoVazio(varValor)) {
	                        total += Number(varValor);
	                    }
	                }
	            }

	            return total;
	        };

	        $scope.totalGeralEtapa = function() {
	            let total = 0;
	            let valorTotalEtapa = $scope.totalValorEtapa();

	            if (!CalculadorFaturamento.isCampoVazio($scope.object.valor) && !CalculadorFaturamento.isCampoVazio(valorTotalEtapa) &&
	                !CalculadorFaturamento.isCampoVazio($scope.object.faturamentoEtapas) && $scope.object.faturamentoEtapas.length > 0) {
	                total = $scope.object.valor - valorTotalEtapa;
	            }

	            if (total >= 0) {
	                $scope.myColor = 'blue';
	                return '(' + $filter('currency')(total, '', 2) + ')';
	            } else {
	                $scope.myColor = 'red';
	                return '(' + $filter('currency')(-(total), '', 2) + ')';
	            }
	        };
	        //fim - aba etapas

	        //inicio - aba de notas de débito

	        $scope.totalAPagarND = 0;
	        $scope.totalBrutoND = 0;
	        $scope.totalTributosND = 0;
	        $scope.totalDescontosND = 0;
	        $scope.totalMultaJurosND = 0;
	        $scope.totalValorPagoND = 0;
	        $scope.totalValorPermutaND = 0;
	        $scope.totalValorRetencoesND = 0;

	        //É calculado os totais que são mostrados na tabela da aba de notas de débito.
	        $scope.calcularTotaisND = function(notaDebito) {
	            $scope.totalAPagarND += notaDebito.valorPagar;
	            $scope.totalBrutoND += notaDebito.valorBruto;
	            $scope.totalTributosND += notaDebito.tributos;
	            $scope.totalDescontosND += notaDebito.descontos;
	            $scope.totalMultaJurosND += notaDebito.multaJuros;
	            $scope.totalValorPagoND += notaDebito.valorPago;
	            $scope.totalValorPermutaND += notaDebito.valorPermuta;
	            $scope.totalValorRetencoesND += notaDebito.valorRetencoes;
	        };

	        $scope.exibirNotaDebito = function(ev, notaDebito) {
	            $state.go('notadebito.exibir_por_acao', {
	                objectId: notaDebito.codigo,
	                acao: 'FAT',
	                from: $scope.object.codigo
	            });
	        };

	        $scope.imprimirNotaDebito = function(ev, notaDebito) {
				let url = $state.href('notadebito.imprimir', {
					objectId: notaDebito.codigo
				});
				window.open(url, '_blank');
	        };

	        //fim - aba de notas de débito

	        //inicio - aba de notas fiscais

			$scope.totalAPagarNF = 0;
			$scope.totalBrutoNF = 0;
			$scope.totalTributosNF = 0;
			$scope.totalDescontosNF = 0;
			$scope.totalMultaJurosNF = 0;
			$scope.totalValorPagoNF = 0;
			$scope.totalValorPermutaNF = 0;
			$scope.totalValorRetencoesNF = 0;

			//É calculado os totais que são mostrados na tabela da aba de notas fiscais.
			$scope.calcularTotaisNF = function(notaFiscalExibicao) {
				$scope.totalAPagarNF += notaFiscalExibicao.valorPagar;
				$scope.totalBrutoNF += notaFiscalExibicao.valorBruto;
				$scope.totalTributosNF += notaFiscalExibicao.tributos;
				$scope.totalDescontosNF += notaFiscalExibicao.descontos;
				$scope.totalMultaJurosNF += notaFiscalExibicao.multaJuros;
				$scope.totalValorPagoNF += notaFiscalExibicao.valorPago;
				$scope.totalValorPermutaNF += notaFiscalExibicao.valorPermuta;
				$scope.totalValorRetencoesNF += notaFiscalExibicao.valorRetencoes;
			};

			$scope.exibirNotaFiscal = function(ev, notaFiscalExibicao) {
				$state.go('notafiscal.exibir_por_acao', {
					objectId: notaFiscalExibicao.codigoNota,
					acao: 'FAT',
					from: $scope.object.codigo
				});
			};

			$scope.imprimirNotaFiscal = function(ev, notaFiscalExibicao) {
				window.open($state.href('notafiscal.imprimir', {
					objectId: notaFiscalExibicao.codigoNota
				}, {
					absolute: true
				}));
			};

			//fim - aba de notas fiscais

	        //inicio - funções do carregamento anterior

	        $scope.buscarFaturamentoAnterior = function() {
	            $scope.validarCamposFaturamentoAnterior = function() {
	                if (!CalculadorFaturamento.isCampoVazio($scope.object.centroFinanceiro) && !CalculadorFaturamento.isCampoVazio($scope.object.centroFinanceiro.identificadorFinal) &&
	                    ((!CalculadorFaturamento.isCampoVazio($scope.object.cliente) && !CalculadorFaturamento.isCampoVazio($scope.object.cliente.codigo)) ||
	                        (!CalculadorFaturamento.isCampoVazio($scope.object.subcliente) && !CalculadorFaturamento.isCampoVazio($scope.object.subcliente.codigo))) &&
	                    !CalculadorFaturamento.isCampoVazio($scope.object.tipo) && !CalculadorFaturamento.isCampoVazio($scope.object.cidadePrestacaoServico) && !CalculadorFaturamento
	                    .isCampoVazio($scope.object.cidadePrestacaoServico.codigo)) {
	                    return true;
	                }
	                return false;
	            };

	            $scope.ordenaFaturamentos = function(faturamentos) {
	                faturamentos.sort(function(a, b) {
	                    if (a.codigo > b.codigo) {
	                        return -1;
	                    }
	                    if (a.codigo < b.codigo) {
	                        return 1;
	                    }
	                    return 0;
	                });
	                return faturamentos;
	            };

	            if ($scope.screenMode === 'NEW') {
	                //FIXME: Refazer pesquisa de faturamento anterior
	                if ($scope.validarCamposFaturamentoAnterior()) {

	                    let ultimosFaturamentos = [];
	                    let filtro = {};

	                    /*Se o subcliente for informado este é setado no parametro cliente da pesquisa.*/
	                    if (!CalculadorFaturamento.isCampoVazio($scope.object.subcliente) && !CalculadorFaturamento.isCampoVazio($scope.object.subcliente.codigo)) {
	                        filtro.cliente = $scope.object.subcliente;
	                    } else {
	                        filtro.cliente = $scope.object.cliente;
	                    }

	                    filtro.tipo = $scope.object.tipo;
	                    filtro.numCentroFinanceiro = $scope.object.centroFinanceiro.identificadorFinal;
	                    filtro.cidadePrestacaoServico = $scope.object.cidadePrestacaoServico.codigo;


	                    /*Verifica se existe pelo menos um faturamento para o mesmo cliente/subcliente, tipo, centro de lucro e cidade.*/
	                    Faturamento.page(undefined, filtro).$promise.then(function(data) {
	                        $rootScope.loadingProgress = false;
							ultimosFaturamentos = data.content;

	                        if (ultimosFaturamentos && ultimosFaturamentos.length > 0) {
	                            /*Coloca em ordem decrescente de código do faturamento a lista "ultimosFaturamentos"*/
	                            ultimosFaturamentos = $scope.ordenaFaturamentos(ultimosFaturamentos);

	                            /*A variável codigoFaturamentoAnterior é utilizada na função "montarFaturamentoAnterior" para carregar os dados do faturamento anterior.*/
	                            $scope.codigoFaturamentoAnterior = ultimosFaturamentos[0].codigo;
	                        } else {
	                            delete filtro.cidadePrestacaoServico;

	                            /*Verifica se existe pelo menos um faturamento para o mesmo cliente/subcliente, tipo e centro de lucro.*/
	                            Faturamento.page(undefined, filtro).$promise.then(function(data) {
	                                $rootScope.loadingProgress = false;
	                                ultimosFaturamentos = data.content;

	                                if (ultimosFaturamentos && ultimosFaturamentos.length > 0) {
	                                    /*Coloca em ordem decrescente de código do faturamento a lista "ultimosFaturamentos"*/
	                                    ultimosFaturamentos = $scope.ordenaFaturamentos(ultimosFaturamentos);

	                                    /*A variável codigoFaturamentoAnterior é utilizada na função "montarFaturamentoAnterior" para carregar os dados do faturamento anterior.*/
	                                    $scope.codigoFaturamentoAnterior = ultimosFaturamentos[0].codigo;
	                                }

	                                /*Retirada do filtro por cliente/subcliente e tipo: SGINV-829. */

	                                /*else {
	                                		delete filtro.numCentroFinanceiro;

	                                		*/
	                                /*Verifica se existe pelo menos um faturamento para o mesmo cliente/subcliente e tipo*/
	                                /*
	                                										Faturamento.page(undefined, filtro).$promise.then(function(data) {
	                                											$rootScope.loadingProgress = false;
	                                											ultimosFaturamentos = data.content;

	                                											if (!angular.isUndefined(ultimosFaturamentos) && ultimosFaturamentos.length > 0) {
	                                												*/
	                                /*Coloca em ordem decrescente de código do faturamento a lista "ultimosFaturamentos"*/
	                                /*
	                                												ultimosFaturamentos = $scope.ordenaFaturamentos(ultimosFaturamentos);

	                                												*/
	                                /*A variável codigoFaturamentoAnterior é utilizada na função "montarFaturamentoAnterior" para carregar os dados do faturamento anterior.*/
	                                /*
	                                												$scope.codigoFaturamentoAnterior = ultimosFaturamentos[0].codigo;
	                                											}
	                                										}).catch(function(response) {
	                                											$scope.$emit('crud-critical', 'load', response);
	                                										});
	                                									}*/
	                            }).catch(function(response) {
	                                $scope.$emit('crud-critical', 'load', response);
	                            });
	                        }

	                    }).catch(function(response) {
	                        $scope.$emit('crud-critical', 'load', response);
	                    });

	                } //fim do if verificar vazio

	            } //fim do if screenMode

	        };


	        // DISCRIMINAÇÃO WATCH
	        $scope.$watch('index', function(current, old) {

	            if ($scope.screenMode === 'NEW') {

	                /*Após os campos tipo, cliente, centro de lucro e cidade serem preenchidos a função "buscarFaturamentoAnterior" é disparada para tentar encontrar um faturamento anterior.

	                	 Condição do if:
	                		Se aba de informações complementares for a aba corrente e existir um faturamento anterior, os dados do deste faturamento anterior são carregados.

	                	 Condição do else if:
	                		Se não existir faturamento anterior e a aba corrente for a aba de discriminação de serviços e aba antiga for aba de informações complementares ou tributos,
	                	 os dados dos campos discriminação inicial e discriminação final são carregados.

	                	obs:
	                		A variável codigoFaturamentoAnterior é criada e preenchida pela função "buscarFaturamentoAnterior" e é utilizada na função "montarFaturamentoAnterior".
	                		A função "buscarFaturamentoAnterior" é disparada no change da combo cidade.*/

	                if (current === 2 && !CalculadorFaturamento.isCampoVazio($scope.codigoFaturamentoAnterior) && angular.isNumber($scope.codigoFaturamentoAnterior) && angular.isUndefined(
	                        $scope.montouFaturamentoAnterior)) {
	                    $scope.montarFaturamentoAnterior();
	                } else if (current === 4 && (old === 2 || old === 3)) {
	                    let listaContatos = [];

	                    $scope.buscarContatos().then(function(data) {
	                        listaContatos = data;
	                        if ($scope.screenMode !== 'VIEW') {
	                            $scope.montarCampoDiscrimininacaoInicial(listaContatos);
	                            $scope.montarCampoDiscrimininacaoCentral(listaContatos);
	                            $scope.montarCampoDiscrimininacaoFinal(listaContatos);
	                        }
	                    }).catch(function(response) {
	                        $rootScope.loadingProgress = false;
	                        $scope.$emit('crud-critical', 'load', response);
	                    });

	                } //fim do else if
	            } //fim do primeiro if
	            else if (current === 4) {
	                let listaContatos = [];

	                $scope.buscarContatos().then(function(data) {
	                    listaContatos = data;
	                    if ($scope.screenMode !== 'VIEW') {
	                        $scope.montarCampoDiscrimininacaoInicial(listaContatos);
	                        $scope.montarCampoDiscrimininacaoCentral(listaContatos);
	                        $scope.montarCampoDiscrimininacaoFinal(listaContatos);
	                    }
	                }).catch(function(response) {
	                    $rootScope.loadingProgress = false;
	                    $scope.$emit('crud-critical', 'load', response);
	                });
	            } //fim do else if do primeiro if
	        });

	        $scope.buscarContatos = function() {
	            return new Promise(function(resolve, reject) {

	                let obj = {};
	                obj.codigoContrato = $scope.object.centroFinanceiro.contratoTO.codigo;
	                obj.nomeRelacionamentoContato = "Faturamento";

	                Contato.page(undefined, obj).$promise.then(function(data) {
	                    $rootScope.loadingProgress = false;
	                    resolve(data.content);
	                }).catch(function(response) {
	                    $rootScope.loadingProgress = false;
	                    reject(response);
	                });

	            });
	        };

	        $scope.buscarContatosParaDiscriminacaoInicial = function() {
	            return new Promise(function(resolve, reject) {
	                let listaFatContato = [];

	                if ($scope.listContato && $scope.listContato.length > 0) {
	                    angular.forEach($scope.listContato, function(value) {
	                        if (value.contato && value.contato.relacionamentoContato) {
	                            if (value.contato.relacionamentoContato.nome === "Faturamento" || value.contato.relacionamentoContato.nome ===
	                                "Faturamento (gestor de contrato)") {
	                                listaFatContato.push(value.contato);
	                            }
	                        }
	                    });
	                }

	                resolve(listaFatContato);
	            });
	        };

	        $scope.montarFaturamentoAnterior = function() {
	            return new Promise((resolve, reject) => {
	                $scope.original = angular.copy($scope.object);

	                Faturamento.faturamentoanterior({
	                    id: $scope.codigoFaturamentoAnterior
	                }).$promise.then(function(data) {
	                    $scope.object = data;

	                    $scope.object.numero = $scope.original.numero;
	                    $scope.object.situacao = $scope.original.situacao;
	                    $scope.object.tipo = $scope.original.tipo;
	                    $scope.object.periodo = $scope.original.periodo;
	                    $scope.object.cliente = $scope.original.cliente;
	                    $scope.object.centroFinanceiro = $scope.original.centroFinanceiro;
						$scope.object.subcliente = $scope.original.subcliente;

	                    if ($scope.original.servicoTipo) {
	                        $scope.object.servicoTipo = $scope.original.servicoTipo;
	                    }

	                    if ($scope.object.servicoTipo) {
	                        $scope.object.servicoTipo.siglaNome = $scope.object.servicoTipo.sigla + ' - ' + $scope.object.servicoTipo.nome;
	                        if ($scope.searchTextServico !== null && $scope.searchTextServico !== undefined)
	                        	$scope.searchTextServico.search = $scope.object.servicoTipo.sigla + ' - ' + $scope.object.servicoTipo.nome;
	                        $scope.selectedItemServico = $scope.object.servicoTipo;
						}

	                    $scope.object.identificacao = $scope.original.identificacao;
	                    $scope.object.resumo = $scope.original.resumo;
	                    $scope.object.cidadePrestacaoServico = $scope.original.cidadePrestacaoServico;
	                    $scope.object.ordem = $scope.original.ordem;

	                    $scope.carregarTipoCobranca();
	                    $scope.ajustarTipoCobrancaCombo(false);

	                    $scope.carregarContasCorrentes($scope.object.contaCorrente.banco.codigo, $scope.object.centroFinanceiro.unidade.matriz.codigo);

	                    $scope.object.valorTributos = 0;
	                    $scope.carregarFaturamentoAliquotas($scope.codigoFaturamentoAnterior);

	                    //FIXME: NECESSÁRIO, POIS A DATA VEM COMO LONG E O MATERIAL SÓ ACEITA DATE.
	                    $scope.object.dataVencimento1 = new Date($scope.object.dataVencimento1);
	                    if (angular.isDefined($scope.object.dataVencimento2) && $scope.object.dataVencimento2 != undefined) {
	                        $scope.object.dataVencimento2 = new Date($scope.object.dataVencimento2);
	                    }

	                    CalculadorFaturamento.divideMaterialServico($scope.object, 'VIEW');
	                    $scope.object.codigo = undefined;
	                    $scope.montouFaturamentoAnterior = true;
	                    resolve(true);
	                }).catch(function(response) {
	                    $scope.$emit('crud-critical', 'load', response);
	                    reject(true);
	                });
	            });
	        };

	        function espacosEMBrancoIniciais(string, tamanho) {
	            let x = 1;
	            let espaco = " ";
	            for (x = 1; x < (tamanho - string.length); x++) {}
	            return espaco + string;
	        }

	        $scope.montarCampoDiscrimininacaoInicial = function(contatos) {

	            let numero_cl = "";
	            let objeto_faturamento = "";
	            let cidade_uf_iss = "";
	            let email_contatos_faturamento = "";
	            let gestor_faturamento = "";
	            let codt6 = "";

	            let discriminacaoParteInicial =
	                "C.L."
	                .concat("%numero_cl%")
	                .concat("        ") // 8 espaços
	                .concat("%email_contatos_faturamento%").concat("\n")
	                //    		.concat("REFERENTE A ")   // (SGINV-1948)
	                .concat("%objeto_faturamento%").concat("\n")
	                .concat("LOCAL DA PRESTACAO DOS SERVICOS: ")
	                .concat("%cidade_uf_iss%").concat("\n\n")
	                .concat("%codt6%")
	                .concat("%gestor_faturamento%")
	                .concat("\n");


	            if ($scope.object && $scope.object.centroFinanceiro && $scope.object.centroFinanceiro.identificadorFinal) {
	                numero_cl = $scope.object.centroFinanceiro.identificadorFinal.toUpperCase();
	            }

	            if ($scope.object && $scope.object.resumo) {
	                objeto_faturamento = $scope.object.resumo.toUpperCase();
	            }

	            if ($scope.object && $scope.object.cidadePrestacaoServico) {
	                let localDaPrestacao = $scope.object.cidadePrestacaoServico.nome + " - " + $scope.object.cidadePrestacaoServico.uf;
	                cidade_uf_iss = localDaPrestacao.toUpperCase();
	            }

	            if ($scope.reinfServico && $scope.reinfServico.codigoClassificacao) {
	                codt6 = "CODT6: ".concat($scope.reinfServico.codigoClassificacao);
	            }

	            // Buscar emails de contato e o gestor
	            if (contatos && contatos.length > 0) {
	                let quantidadeEmails = 0;

	                angular.forEach(contatos, function(contato, key) {
	                    if (contato && contato.relacionamentoContato && contato.relacionamentoContato.nome) {
	                        if (contato.relacionamentoContato.nome === "Faturamento" && contato.email) {
	                            if (quantidadeEmails > 0) {
	                                email_contatos_faturamento = email_contatos_faturamento.concat("\n").concat("                "); //16 espaços
	                            }

	                            email_contatos_faturamento = email_contatos_faturamento.concat(contato.email.toUpperCase());
	                            quantidadeEmails++;
	                        }
	                    }
	                });


	                // loop para o gestor (apenas um. o primeiro)
	                let continuarLoop = true;

	                angular.forEach(contatos, function(contato, key) {

	                    if (continuarLoop) {
	                        if (contato && contato.relacionamentoContato && contato.relacionamentoContato.nome) {
	                            if (contato.relacionamentoContato.nome === "Faturamento (gestor de contrato)") {

	                                gestor_faturamento = gestor_faturamento.concat("\n").concat("GESTOR: ").concat(contato.nome.toUpperCase());

	                                if (contato.email) {
	                                    gestor_faturamento = gestor_faturamento.concat("\n").concat("E-MAIL: ").concat(contato.email.toUpperCase());
	                                }

	                                continuarLoop = false;
	                            }
	                        }
	                    }
	                });
	            }

	            //REPLACEs
	            discriminacaoParteInicial = discriminacaoParteInicial.replace("%numero_cl%", numero_cl);
	            discriminacaoParteInicial = discriminacaoParteInicial.replace("%objeto_faturamento%", objeto_faturamento);
	            discriminacaoParteInicial = discriminacaoParteInicial.replace("%cidade_uf_iss%", cidade_uf_iss);
	            discriminacaoParteInicial = discriminacaoParteInicial.replace("%codt6%", codt6);
	            discriminacaoParteInicial = discriminacaoParteInicial.replace("%email_contatos_faturamento%", email_contatos_faturamento);
	            discriminacaoParteInicial = discriminacaoParteInicial.replace("%gestor_faturamento%", gestor_faturamento);

	            $scope.$apply(function() {
	                $scope.object.discriminacaoInicial = discriminacaoParteInicial;
	            });
	        };

	        //FIXME: verificar necessidade do parametro
	        $scope.montarCampoDiscrimininacaoCentral = function(contatos) {
	            let discriminacaoParteCentral = "";

	            discriminacaoParteCentral = "PERIODO: " + (!CalculadorFaturamento.isCampoVazio($scope.object.periodo) ? $scope.object.periodo : " ");

	            let servico = espacosEMBrancoIniciais($filter('currency')($scope.object.valorServico, ' R$ '), 21);
	            let material = espacosEMBrancoIniciais($filter('currency')($scope.object.valorMaterial, ' R$ '), 21);

	            discriminacaoParteCentral = discriminacaoParteCentral + "\nSERVICO (" + $scope.object.percentualServico + "%)..................................    -" + servico;

	            if ($scope.object.percentualMaterial > 0) {
	                discriminacaoParteCentral = discriminacaoParteCentral + "\nMATERIAL (" + $scope.object.percentualMaterial + "%)..................................    -" + material;
	            }

	            // SGINV-1948 (Parte central em branco)
	            //        $scope.object.discriminacaoCentral = discriminacaoParteCentral;
	            $scope.object.discriminacaoCentral = "";
	        };

	        //FIXME: REFATORAR
	        $scope.montarCampoDiscrimininacaoFinal = function(contatos) {
	            $scope.object.discriminacaoFinal = " ";
	            $scope.nome_contato_faturamento_gestor = " ";
	            $scope.email_contato_faturamento_gestor = " ";

	            if (!CalculadorFaturamento.isCampoVazio(contatos) && contatos.length > 0) {
	                for (let i = 0; i < contatos.length; i++) {
	                    let contato = contatos[i];

	                    //                if (!CalculadorFaturamento.isCampoVazio(contato) && !CalculadorFaturamento.isCampoVazio(contato.email) && !CalculadorFaturamento.isCampoVazio(contato.nomeRelacionamentoContato)) {
	                    if (!CalculadorFaturamento.isCampoVazio(contato) && !CalculadorFaturamento.isCampoVazio(contato.email) && !CalculadorFaturamento.isCampoVazio(contato
	                            .relacionamentoContato) &&
	                        !CalculadorFaturamento.isCampoVazio(contato.relacionamentoContato.nome)) {

	                        if (contato.relacionamentoContato.nome === "Faturamento (gestor de contrato)" && $scope.nome_contato_faturamento_gestor === " ") {
	                            $scope.nome_contato_faturamento_gestor = !CalculadorFaturamento.isCampoVazio(contato.nome) ? contato.nome.toUpperCase() : " ";
	                            $scope.email_contato_faturamento_gestor = contato.email.toUpperCase();
	                        }
	                    } //fim do isCampoVazio - contato
	                } //fim do for
	            }

	            let retencoes = [];

	            let discriminacaoParteFinal = "";

	            /*        if ($scope.nome_contato_faturamento_gestor !== "" && $scope.nome_contato_faturamento_gestor !== " ") {
	            			discriminacaoParteFinal = discriminacaoParteFinal + "GESTOR: " + "nome_contato_faturamento_gestor \n";
	            		}

	            		if ($scope.email_contato_faturamento_gestor !== "" && $scope.email_contato_faturamento_gestor !== " ") {
	            			discriminacaoParteFinal = discriminacaoParteFinal + "E-MAIL: " + "email_contato_faturamento_gestor \n";
	            		}*/


	            if ($scope.object.tipoDocumentoPagamento === "Depósito em conta/Transferência") {
	                discriminacaoParteFinal = discriminacaoParteFinal + "PAGTO: " + "nome_banco \n" + "AG. " + "numero_agencia";
	                discriminacaoParteFinal = discriminacaoParteFinal + " C/C " + "numero_conta\n";
	            } else if ($scope.object.tipoDocumentoPagamento === "Boleto") {
	                discriminacaoParteFinal = discriminacaoParteFinal + "PAGTO: " + "BOLETO BANCARIO (SERA DISPONIBILIZADO EM 2 DIAS NO E-MAIL CADASTRADO)\n";
	            }

				if ($scope.object.tipoFornecimento === 'Só material') {
					discriminacaoParteFinal = discriminacaoParteFinal + "VALOR DOS MATERIAIS.................................    -" + "valor_faturamento";
				} else {
					discriminacaoParteFinal = discriminacaoParteFinal + "VALOR DOS SERVICOS..................................    -" + "valor_faturamento";
				}

	            if ($scope.object.faturamentoRetencoes && $scope.object.faturamentoRetencoes.length > 0) {
	                angular.forEach($scope.object.faturamentoRetencoes, function(value, key) {
	                    if (value.tipo.chave === 'contratual') {
	                        let percContrat = $scope.object.percentualRetencaoContratual;
	                        let total = ($scope.object.valor * percContrat) / 100;
	                        if (angular.isDefined(total) && total > 0) {
	                            discriminacaoParteFinal = discriminacaoParteFinal + "\nRETENÇÃO CONTRATUAL DE " + $filter('currency')(percContrat, '') + "%" +
	                                "................................    -          " + $filter('currency')(total, 'R$ ');
	                        }
	                    } else if (value.tipo.chave === 'acessoria') {
	                        let percAcessoria = $scope.object.percentualRetencaoAcessoria;
	                        let totalAcessoria = ($scope.object.valor * percAcessoria) / 100;
	                        if (angular.isDefined(totalAcessoria) && totalAcessoria > 0) {
	                            discriminacaoParteFinal = discriminacaoParteFinal + "\nRETENÇÃO ACESSORIA DE " + $filter('currency')(percAcessoria, '') + "%" +
	                                "................................    -          " + $filter('currency')(totalAcessoria, 'R$ ');
	                        }
	                    }
	                });
	            }

				if ($scope.object.tipoFornecimento !== 'Só material') {
					discriminacaoParteFinal = discriminacaoParteFinal + "\nVALOR TOTAL DOS TRIBUTOS FEDERAIS, ESTADUAIS E MUNICIPAIS";
					discriminacaoParteFinal = discriminacaoParteFinal + "\nCONFORME DISPOSTO NA LEI 12.741/12";

					let labelValorTributo = 0;
					let totalPercentualTributo = 0;
					let totalPer = 0;

					if ($scope.object.issRetidoCliente) {
						labelValorTributo = $filter('currency')($scope.totalValorTributo, 'R$ ');
						totalPercentualTributo = $filter('currency')((($scope.totalValorTributo * 100) / $scope.object.valor), '');
						totalPer = $scope.totalValorTributo;
					} else {
						labelValorTributo = $filter('currency')($scope.subTotalValorTributo, 'R$ ');
						totalPercentualTributo = $filter('currency')((($scope.subTotalValorTributo * 100) / $scope.object.valor), '');
						totalPer = $scope.subTotalValorTributo;
					}

					if (totalPer > 0) {
						discriminacaoParteFinal = discriminacaoParteFinal + "\nCARGA TRIBUTARIA = " + "percentual_tributo" + "% VALOR DOS TRIBUTOS = " + "valor_tributo";
						discriminacaoParteFinal = discriminacaoParteFinal.replace("percentual_tributo", totalPercentualTributo);
						discriminacaoParteFinal = discriminacaoParteFinal.replace("valor_tributo", labelValorTributo);
					}
				}

	            discriminacaoParteFinal = discriminacaoParteFinal.replace("nome_contato_faturamento_gestor", $scope.nome_contato_faturamento_gestor);
	            discriminacaoParteFinal = discriminacaoParteFinal.replace("email_contato_faturamento_gestor", $scope.email_contato_faturamento_gestor);

	            let labelAgencia = $scope.object.contaCorrente.agencia;
	            if (!CalculadorFaturamento.isCampoVazio($scope.object.contaCorrente.digitoAgencia)) {
	                $scope.object.contaCorrente.digitoAgencia = $scope.object.contaCorrente.digitoAgencia.trim();

	                if (!CalculadorFaturamento.isCampoVazio($scope.object.contaCorrente.digitoAgencia))
	                    labelAgencia += '-' + $scope.object.contaCorrente.digitoAgencia;
	            }

	            let labelConta = $scope.object.contaCorrente.conta;
	            if (!CalculadorFaturamento.isCampoVazio($scope.object.contaCorrente.digito)) {
	                $scope.object.contaCorrente.digito = $scope.object.contaCorrente.digito.trim();

	                if (!CalculadorFaturamento.isCampoVazio($scope.object.contaCorrente.digito))
	                    labelConta += '-' + $scope.object.contaCorrente.digito;
	            }

	            discriminacaoParteFinal = discriminacaoParteFinal.replace("nome_banco", $scope.object.contaCorrente.banco.nome.toUpperCase());
	            discriminacaoParteFinal = discriminacaoParteFinal.replace("numero_agencia", labelAgencia.toUpperCase());
	            discriminacaoParteFinal = discriminacaoParteFinal.replace("numero_conta", labelConta.toUpperCase());


	            let labelValor = $filter('currency')($scope.object.valor, ' R$ ');
	            discriminacaoParteFinal = discriminacaoParteFinal.replace("valor_faturamento", espacosEMBrancoIniciais(labelValor, 21));


	            if (CalculadorFaturamento.isCampoVazio($scope.nome_contato_faturamento_gestor) || CalculadorFaturamento.isCampoVazio($scope.email_contato_faturamento_gestor)) {
	                let linhas = discriminacaoParteFinal.split("\n");
	                discriminacaoParteFinal = linhas[2] + " \n" + linhas[3] + " \n" + linhas[4] + " \n" + linhas[5] + " \n" + linhas[6] + " \n" + linhas[7];
	            }

				if ($scope.object.tipoFornecimento !== 'Só material') {
					discriminacaoParteFinal = complementarDiscriminacaoFinalComTributos($scope, discriminacaoParteFinal);
				}

	            $scope.object.discriminacaoFinal = discriminacaoParteFinal;
	        };

	        //fim - - funções do carregamento anterior

	        //inicio - funções dos botões do wizard
	        $scope.index = 0;

	        $scope.previousStep = () => {
	        	$scope.index--;
	        	if (!$scope.habilitarAbaTributos() && $scope.isLastStep())
	            	$scope.index--;
	        };

	        $scope.isFirstStep = () => {

	            if ($scope.index === 0) {
	                return true;
	            } else {
	                return false;
	            }
	        };

	        $scope.nextStep = () => {
	            $scope.index = $scope.index + 1;
	            if ($scope.index === 4) {
	                if (!$scope.object.servicoTipo && $scope.object.tipoFornecimento !== 'Só material') {
	                    $scope.notification.error('', 'O valor informado no campo "Código do serviço" é inválido.');
	                    $scope.index = 3;
	                }
	            }
	        };

	        $scope.isLastStep = () => {
	            if ($scope.index === 5 || ($scope.index === 4 && $scope.object.tipoFornecimento === 'Só material')) {
	                return true;
	            } else {
	                return false;
	            }
	        };

	        $scope.currentStepInvalid = () => {
	            if ($scope.index === 0) {
	                if (angular.isDefined($scope.wizardStep1)) {
	                    return $scope.wizardStep1.$invalid;
	                }
	            }
	            if ($scope.index === 1) {
	                if (angular.isDefined($scope.wizardStep2)) {
	                    return $scope.wizardStep2.$invalid;
	                }
	            }
	            if ($scope.index === 2) {
	                if (angular.isDefined($scope.wizardStep3)) {
	                    return $scope.wizardStep3.$invalid;
	                }
	            }
	            if ($scope.index === 3) {
	                if (angular.isDefined($scope.wizardStep4)) {
	                    return $scope.wizardStep4.$invalid;
	                }
	            }
	            if ($scope.index === 4) {
	                if (angular.isDefined($scope.wizardStep5)) {
	                    return $scope.wizardStep5.$invalid;
	                }
	            }
	            if ($scope.index === 5) {
	                if (angular.isDefined($scope.wizardStep6)) {
	                    return $scope.wizardStep6.$invalid;
	                }
	            }
	            return true;
	        };

	        $scope.formsIncomplete = function() {
	            if (angular.isDefined($scope.wizardStep1) && angular.isDefined($scope.wizardStep2) && angular.isDefined($scope.wizardStep3) && angular.isDefined($scope.wizardStep4) && angular
	                .isDefined($scope.wizardStep5) && angular.isDefined($scope.wizardStep6)) {
	                if ($scope.wizardStep1.$invalid === true && $scope.wizardStep2.$invalid === true && $scope.wizardStep3.$invalid === true && $scope.wizardStep4.$invalid === true && $scope
	                    .wizardStep5.$invalid === true && $scope.wizardStep6.$invalid === true) {
	                    return true;
	                }
	            }
	            return false;
	        };
	        //fim - funções dos botões do wizard

	        $scope.habilitarAbaTributos = function() {
	        	return $scope.object.tipoFornecimento !== 'Só material';
	        }

	        //FIXME: REFATORAR
	        $scope.novoMontarDiscriminacaoServico = function() {
	            let listaContatos = [];
	            $scope.action = true;
	            $rootScope.loadingProgress = true;

	            $q.all(
	                $scope.buscarContatosParaDiscriminacaoInicial().then(function(data) {
	                    $scope.montarCampoDiscrimininacaoInicial(data);
	                }),
	                //			$scope.buscarContatos().then(function(data) {
	                $scope.buscarContatosParaDiscriminacaoInicial().then(function(data) {
	                    listaContatos = data;
	                    $scope.montarCampoDiscrimininacaoCentral(listaContatos);
	                    $scope.montarCampoDiscrimininacaoFinal(listaContatos);
	                })

	            ).then(function() {
	                $rootScope.loadingProgress = false;
	                $scope.action = false;

	            }).catch(function(response) {
	                $rootScope.loadingProgress = false;
	                $scope.action = false;
	                $scope.$emit('crud-critical', 'load', response);
	            });
	        }

	        CalculadorFaturamento.divideMaterialServico($scope.object, $scope.screenMode);

	        let parametro = {
	            chave: "controlefiscal.limite.variacao.tributo"
	        };
	        Parametro.page(parametro).$promise.then(function(data) {
	            let variacao = data.valor;
	            $scope.variacaoTributo = Number(variacao);
	        }).catch(function(response) {
	            console.error('crud-critical', 'dialog', response);
	            $scope.notification.criticalError(response.status, response.statusText, response.data);
	        });

	    };

	})();

