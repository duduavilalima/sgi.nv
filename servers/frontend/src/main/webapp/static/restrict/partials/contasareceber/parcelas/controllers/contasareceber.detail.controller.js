(function() {
    "use strict";

    angular.module('app.contasareceber').controller('ContasAReceberCadastroCtrl', contasAReceberCadastroCtrlFn);

    function contasAReceberCadastroCtrlFn(
        $scope,
        $state,
        $mdDialog,
        $rootScope,
        $timeout,
        $q,
        $window,
        $filter,
        EnumService,
        MotivoRetornoDialog,
        CalculadorFaturamento,
        NotaFiscalFaturamento,
        CategoriaTributo,
        Parcela,
        NotaTributoAliquota,
        ContaCorrente,
        ServicoAbaTributosCAR,
        MotivoRetornoParcela,
        ServicoDialogTributoCAR,
        NotaSaidaParcela,
        Parametro,
        Contrato,
        UsuarioLogadoService,
        MotivoDescontoDevolucao,
        MotivoAplicacao,
        NotaSaidaParcelaBaixaRetencao,
        PessoaJuridica,
        ParcelaService) {

        $scope.listSN = [{
            nome: 'Não',
            valor: false
        }, {
            nome: 'Sim',
            valor: true
        }];

        var verificarLote = (codParcela) => {
            return new Promise((resolve, reject) => {
                let promiseLote = Parcela.verificarLote({
                    id: codParcela
                }).$promise;
                $rootScope.loadingProgress = true;
                promiseLote.then(function(response) {
                    $rootScope.loadingProgress = false;
                    resolve(response);
                }).catch(function(response) {
                    $rootScope.loadingProgress = false;
                    $scope.$emit('crud-critical', 'search', response);
                    reject(false);
                });
            });
        };

        $scope.zerarCampoDesconto = function (){
        	if($scope.object.valorRetencaoRetida == null){
        		$scope.object.valorRetencaoRetida = 0;
        	}

        }

        $scope.zerarCampoJuros = function (){
			if($scope.object.valorJuros == null){
				$scope.object.valorJuros = 0;
			}

		}

        function inicializa() {
            $scope.modulo = $state.current.data.modulo;
            $scope.titulo = $state.current.data.titulo;
            $scope.tituloSingular = $state.current.data.tituloSingular;
            $scope.subtitulo = $state.current.data.subtitulo;

            $scope.index = 0;
            $scope.object = {};
            $scope.listaTributos = [];
            $scope.bancoList = [];
            $scope.baixasRetencaoList = undefined;
            $scope.agenciaContaList = [];

            $scope.valorTributos = 0;
            $scope.valorLiquido = 0;

            $scope.percentualRetencaoContratual = 0;
            $scope.object.valorRetencaoContratual = 0;

            $scope.percentualRetencaoAcessoria = 0;
            $scope.object.valorRetencaoAcessoria = 0;

            $scope.action = false;

            $scope.totalValorDesconto = 0;
            $scope.totalValorDevolucao = 0;
            $scope.subtotal = 0;

            UsuarioLogadoService.possuiUmPerfilLista(['faturamento-analista']).$promise.then((result) => {
                $scope.temPerfilAnalista = result.possuiUmPerfilLista;
            });

            UsuarioLogadoService.possuiUmPerfilLista(['faturamento-gestor']).$promise.then((result) => {
                $scope.temPerfilGestorFaturamento = result.possuiUmPerfilLista;
            });

            $scope.temPerfilFaturamentoCnsGerencia = false;

            UsuarioLogadoService.possuiUmPerfilLista(['faturamento-cns-gerencia', 'faturamento-cns-geral']).$promise.then((result) => {
                $scope.temPerfilFaturamentoCnsGerencia = result.possuiUmPerfilLista;
                if ($scope.temPerfilFaturamentoCnsGerencia && $scope.screenMode !== 'VIEW') {
                    $scope.screenMode = 'VIEW';
                    $state.go('contasareceber.exibir', {
                        objectId: $scope.objectId
                    });
                    $scope.action = false;
                    $scope.notification.error("", "Você não possui acesso aos dados desta parcela.");
                }
            });

            verificarLote($scope.objectId).then(response => {
                $scope.desabilitarCancelamentoRecebimento = response.selecao;
            });

            $scope.object = {};
            $scope.object.bancoSelecionado = null;
            $scope.valorTributoPIS = 0;

            $scope.listaMotivoDesconto = [];
            $scope.listaMotivoDevolucao = [];
        }

        inicializa();

        $scope.$on('comboIssRetido', function() {
            let iss = 0;
            $scope.listaTributos.forEach((value) => {
                if (value.categoriaTributo.tributoTO.nome === 'ISS') {
                    iss = value.valor;
                    return;
                }
            });
            if ($scope.object.notaSaida.issRetidoClienteRecebimento) {
                $scope.valorTributos = $scope.valorTributos + iss;
            } else {
                $scope.valorTributos = $scope.valorTributos - iss;
            }
            $scope.valorLiquido = $scope.calcularValorLiquidoParcela($scope.object, $scope.valorTributos);
        });

        //Listas
        $scope.listSN = [{
            nome: 'Não',
            valor: false
        }, {
            nome: 'Sim',
            valor: true
        }];

        $scope.openNewTargetNF = function() {
            $window.open('/sgi/nv/app/main.html#!/notafiscal/' + $scope.object.notaSaida.codigo + '/exibir');
        };

        $scope.isValorLiquidoDiferente = function() {
            return Math.round($scope.object.valorLiquido * 100) !== Math.round($scope.valorLiquido * 100);
        };

        function loadObj() {
            return new Promise((resolve, reject) => {
                if (!CalculadorFaturamento.isCampoVazio($scope.objectId)) {
                    // FIXME: Refatorar todo esse código.
                    $scope.action = true;
                    Parcela.get({
                        id: $scope.objectId
                    }).$promise.then(function(data) {
                        let object = data;

                        object.ordem = CalculadorFaturamento.formataNumero(object.ordem, 2);

                        $scope.carregaTributos(object.notaSaida, object.ordem).then((result) => {
                            let listaRetencoes = [];
                            if (!CalculadorFaturamento.isCampoVazio(object.parcelaRetencoes)) {
                                listaRetencoes = object.parcelaRetencoes;
                            }

                            object = calcularSubTotaisTabelaDescontoDevolucao(object);

                            if (angular.isDefined(result.arrayTributos)) {
                                $scope.listaTributos = result.arrayTributos;
                                $scope.valorTributos = $scope.calcularValorTributos(object, $scope.listaTributos);
                            } else {
                                $scope.listaTributos = [];
                                $scope.valorTributos = 0;
                            }

                            calcularSutTotal(object, $scope.listaTributos);

                            $scope.valorLiquido = $scope.calcularValorLiquidoParcela(object, $scope.valorTributos);

                            if (object.contaCorrenteRecebimento)
                                object.contaCorrente = object.contaCorrenteRecebimento;
							else
								object.contaCorrente = object.contaCorrentePrevista;

                            if (object.contaCorrenteRecebimento && object.contaCorrenteRecebimento.banco)
                                object.bancoSelecionado = object.contaCorrenteRecebimento.banco;
							else
								object.bancoSelecionado = object.contaCorrentePrevista.banco;

                            if (object.tipoDocumentoRecebimento)
                                object.tipoDocumento = object.tipoDocumentoRecebimento;
							else
								object.tipoDocumento = object.tipoDocumentoPrevisto;

                            if (object.notaSaida.empresa && object.notaSaida.empresa.matriz && object.notaSaida.empresa.matriz.codigo) {
                                $scope.carregarBancos(object.notaSaida.empresa.matriz.codigo);
                                $scope.carregarContasCorrentes(object.contaCorrente.banco.codigo, object.notaSaida.empresa.matriz.codigo);
                            } else {
                                $scope.carregarBancos(object.notaSaida.empresa.codigo);
                                $scope.carregarContasCorrentes(object.contaCorrente.banco.codigo, object.notaSaida.empresa.codigo);
                            }

                            $scope.montarLabelCentroLucro(object.notaSaida.faturamentoEtapaTO.faturamento.centroFinanceiro);
                            object.dataVencimento = new Date(object.dataVencimento);
                            object.dataPagamento = new Date(object.dataPagamento);

                            if (object.dataContabilizacao) {
                                object.dataContabilizacao = new Date(object.dataContabilizacao);
                            }

                            $scope.original = angular.copy(object);
                            $scope.object = angular.copy(object);

                            $scope.carregarDadosCancelamento();

                            $scope.action = false;

                            carregarMotivos($scope.object);

//                            $scope.object.tipoDocumento = $scope.object.tipoDocumentoPrevisto;
                            carregarTipoDocumento();

                            return carregarBaixasRetencaoDaNotaSaidaParcela();

                        });
                    }).catch(function(response) {
                        $scope.$emit('crud-critical', 'load', response);
                        reject({
                            mensagem: 'Não foi possível realizar a pesquisa da parcela!',
                            executou: false
                        });
                    });
                }
            });
        } //fim da função loadObj

        const carregarBaixasRetencaoDaNotaSaidaParcela = () => {
            $scope.totalValorBaixa = 0;
            $scope.totalJurosBaixa = 0;
            $scope.totalRecebido = 0;
            return NotaSaidaParcelaBaixaRetencao.obterBaixasRetencaoDaNotaSaidaParcela({
                    id: $scope.object.id
                }).$promise
                .then(response => {
                    $scope.baixasRetencaoList = response;
                    $scope.baixasRetencaoList.forEach(item => {
                        $scope.totalValorBaixa = item.valorBaixa + $scope.totalValorBaixa;
                        $scope.totalJurosBaixa = item.valorJuros + $scope.totalJurosBaixa;
                        $scope.totalRecebido = (item.valorBaixa + item.valorJuros) + $scope.totalRecebido;
                    });

                    $scope.baixasRetencaoList.sort((a, b) => {
                        if (a.dataCriacao < b.dataCriacao) {
                            return -1;
                        } else if (a.dataCriacao === b.dataCriacao) {
                            return 0;
                        } else {
                            return 1;
                        }
                    });

                })
                .catch(response => {
                    $scope.$emit('crud-critical', 'load', response);
                });
        };

        function calcularSubTotaisTabelaDescontoDevolucao(object) {
            $scope.totalValorDevolucao = 0;
            $scope.totalValorDesconto = 0;
            $scope.subtotalValorDesconto = object.listaNotaSaidaParcelaMotivoDescDev.filter((item) => {
                if ('DESCONTO' === item.tipo) {
                    $scope.totalValorDesconto = item.valor + $scope.totalValorDesconto;
                }
            });
            object.valorDesconto = $scope.totalValorDesconto;

            $scope.subtotalValorDesconto = object.listaNotaSaidaParcelaMotivoDescDev.filter((item) => {
                if ('DEVOLUCAO' === item.tipo) {
                    $scope.totalValorDevolucao = item.valor + $scope.totalValorDevolucao;
                }
            });
            object.valorDevolucao = $scope.totalValorDevolucao;

            return object;
        }

        function calcularSutTotal(object, listaTributos) {
            let PIS_COFINS_CSLL = listaTributos.find((element) => element.categoriaTributo.tributoTO.nome === 'PIS/COFINS/CSLL');
            $scope.subtotal = (object.valorBruto - ($scope.valorTributos - (PIS_COFINS_CSLL ? PIS_COFINS_CSLL.valor : 0)));
        }

        $scope.changeTitulo = function() {
            if ($scope.screenMode === 'VIEW') {
                $scope.subtitulo = 'Exibição';
            } else if ($scope.screenMode === 'EDIT') {
                $scope.subtitulo = 'Edição';
            } else if ($scope.screenMode === 'RECEBER') {
                $scope.subtitulo = 'Receber';
            }
        };


        $scope.montarLabelCentroLucro = function(centroFinanceiro) {
            if (!CalculadorFaturamento.isCampoVazio(centroFinanceiro) && !CalculadorFaturamento.isCampoVazio(centroFinanceiro.identificadorFinal) && !CalculadorFaturamento.isCampoVazio(
                    centroFinanceiro.nome)) {
                $scope.object.centroLucro = centroFinanceiro.identificadorFinal + " - " + centroFinanceiro.nome
            }
        };

        $scope.carregaTributos = function(nota, ordem) {
            return new Promise((resolve, reject) => {
                let arrayTributos = [];

                let obj = {};
                obj.nota = {};
                obj.nota.codigo = nota.codigo;

                let params = {};
                $rootScope.loadingProgress = true;

                NotaTributoAliquota.page(params, obj).$promise
                    .then(function(data) {
                        $rootScope.loadingProgress = false;
                        arrayTributos = filtrarTributoPorParcela(ordem, data.content);
                        $scope.buscarCidades(nota, arrayTributos);
                        initAbaTributos(arrayTributos);
                        resolve({
                            mensagem: 'Pesquisa dos tributos foi atualizada com sucesso!',
                            arrayTributos: arrayTributos
                        });
                    }).catch(function(response) {
                        $scope.$emit('crud-critical', 'load', response);
                        reject({
                            mensagem: 'Não foi possível realizar a pesquisa de tributos!',
                            arrayTributos: arrayTributos
                        });
                    });
            });
        };

        function filtrarTributoPorParcela(parcela, listaTributos) {
            let arrayTributos = [];

            angular.forEach(listaTributos, function(obj, key) {
                if (obj.ordem === Number(parcela)) {
                    arrayTributos.push(obj);
                }
            });

            return arrayTributos;
        }

        $scope.percentualTotalTributos = function(listaTributos) {
            let percentualTotal = 0;

            angular.forEach(listaTributos, function(obj, key) {

                if (!CalculadorFaturamento.isCampoVazio(obj) && !CalculadorFaturamento.isCampoVazio(obj.aliquota) && !CalculadorFaturamento.isCampoVazio(obj.aliquota.percentual))
                    percentualTotal += Number(obj.aliquota.percentual);

            });

            return percentualTotal;
        };

        $scope.calcularValorTributos = function(parcela, listaTributos) {
            let varValorTributos = 0;

            if (!CalculadorFaturamento.isCampoVazio(parcela) && !CalculadorFaturamento.isCampoVazio(parcela.notaSaida) &&
                !CalculadorFaturamento.isCampoVazio(parcela.notaSaida.faturamentoEtapaTO) &&
                !CalculadorFaturamento.isCampoVazio(parcela.notaSaida.faturamentoEtapaTO.faturamento)) {

                if (parcela.notaSaida.issRetidoClienteRecebimento) {
                    varValorTributos = ServicoAbaTributosCAR.getTotalValorTributos(listaTributos);
                } else {
                    varValorTributos = ServicoAbaTributosCAR.getSubtotalValorTributos(listaTributos);
                }
            }

            $scope.valorTributoPIS = ServicoAbaTributosCAR.getValorTributoPIS(listaTributos);

            $scope.valorTributos = varValorTributos;

            calcularSutTotal($scope.object, listaTributos);

            return varValorTributos;
        };

        $scope.calcularValorRetencoesParcela = function(parcela, listaRetencoes) {

            return $q(function(resolve, reject) {

                let result = {
                    percentualContratual: 0,
                    valorContratual: 0,
                    percentualAcessoria: 0,
                    valorAcessoria: 0
                };

                if (!CalculadorFaturamento.isCampoVazio(parcela) &&
                    !CalculadorFaturamento.isCampoVazio(parcela.valorBruto) &&
                    !CalculadorFaturamento.isCampoVazio(listaRetencoes)) {

                    angular.forEach(listaRetencoes, function(value, key) {
                        if (parcela.situacao == 'Finalizada (recebida)') {
                            if (value.tipo === 'Contratual') {
                                result.percentualContratual = (value.valorRetido * 100) / parcela.valorBruto;
                                result.valorContratual = value.valorRetido;
                            } else if (value.tipo === 'Acessória') {
                                result.percentualAcessoria = (value.valorRetido * 100) / parcela.valorBruto;
                                result.valorAcessoria = value.valorRetido;
                            }
                        } else {
                            if (value.tipo === 'Contratual') {
                                result.percentualContratual = (value.valorAReter * 100) / parcela.valorBruto;
                                result.valorContratual = value.valorAReter;
                            } else if (value.tipo === 'Acessória') {
                                result.percentualAcessoria = (value.valorAReter * 100) / parcela.valorBruto;
                                result.valorAcessoria = value.valorAReter;
                            }
                        }
                    });

                    resolve({
                        mensagem: 'Cálculo das retenções realizado com sucesso!',
                        result: result
                    });
                } else {
                    reject({
                        mensagem: 'Não foi possível realizar os cálculos das retenções!',
                        result: result
                    });
                }

            });

        };

        $scope.calcularValorLiquidoParcela = function(parcela, tributos) {
            if (!CalculadorFaturamento.isCampoVazio(parcela) &&
                !CalculadorFaturamento.isCampoVazio(parcela.valorBruto)) {
                let liquido = parcela.valorBruto -
                    tributos -
                    $scope.object.valorRetencaoRetida -
                    parcela.valorDesconto + parcela.valorMulta + parcela.valorJuros + parcela.valorDevolucao;
                if (CalculadorFaturamento.isCampoVazioNumerico(liquido)) {
                    liquido = 0;
                }

                return liquido;
            }

            return 0;
        };

        $scope.carregarBancos = function(codigoPessoa) {
            // CARREGAR BANCOS
            ContaCorrente.page(null, {
                    pessoa: {
                        codigo: codigoPessoa
                    }
                }).$promise
                .then(function(data) {
                    $scope.bancoList = data.content;
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'carregar bancos load', response);
                    $scope.bancoList = [];
                    $scope.agenciaContaList = [];
                });
        };

        $scope.$on('changeBanco', function() {
            if ($scope.object.notaSaida) {
                if ($scope.object.notaSaida.empresa && $scope.object.notaSaida.empresa.matriz && $scope.object.notaSaida.empresa.matriz.codigo) {
                    $scope.carregarContasCorrentes($scope.object.bancoSelecionado.codigo, $scope.object.notaSaida.empresa.matriz.codigo);
                } else {
                    $scope.carregarContasCorrentes($scope.object.bancoSelecionado.codigo, $scope.object.notaSaida.empresa.codigo);
                }
            }
            if ($scope.object.tipoDocumentoPrevisto === 'Boleto')
            	delete $scope.object.tipoDocumento;
            $scope.object.contaCorrente = null;
        });

        $scope.carregarContasCorrentes = function(codigoBanco, codigoPessoa) {
            // CARREGAR CONTAS CORRENTES
            ContaCorrente.page(null, {
                    banco: {
                        codigo: codigoBanco
                    },
                    pessoa: {
                        codigo: codigoPessoa
                    }
                })
                .$promise.then(function(data) {
                    $scope.agenciaContaList = data.content;
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'contrato load', response);
                    $scope.agenciaContaList = [];
                });
        };

        $scope.$on('changeAgenciaRecebimento', function() {
            carregarTipoDocumento();
             ajustarTipoDocumento();
        });

        $scope.$on('changeAgenciaEdicao', function() {
        	carregarTipoDocumento();
            ajustarTipoDocumento();
        });

        function ajustarTipoDocumento() {
            if ($scope.tipoBoleto && $scope.tipoDeposito) {
                atribuirTipoDocumento($scope.tipoBoleto, $scope.tipoDeposito);

            } else {
                EnumService.enumValues({
                        pacote: 'br.com.araujoabreu.sgi.contasApagar.domain',
                        classe: 'Pagamento',
                        enum: 'TipoDocumentoPagamento'
                    })
                    .$promise.then(function(data) {
                        $scope.tipoBoleto = data[0];
                        $scope.tipoDeposito = data[1];

                        atribuirTipoDocumento($scope.tipoBoleto, $scope.tipoDeposito);

                    }).catch(function(response) {
                        $scope.$emit('crud-critical', 'load', response);
                    });
            }
        }

        function atribuirTipoDocumento(boleto, deposito) {
            if ($scope.object.contaCorrente && !$scope.object.contaCorrente.boletoFaturamento) {
                $scope.object.tipoDocumento = deposito;
            }
        }

        function carregarTipoDocumento() {
            let contaCorrente = $scope.object.contaCorrente;

            if (!CalculadorFaturamento.isCampoVazio(contaCorrente) && !CalculadorFaturamento.isCampoVazio(contaCorrente.boletoFaturamento) && !contaCorrente.boletoFaturamento) {
                EnumService.enumValues({
                        pacote: 'br.com.araujoabreu.sgi.contasApagar.domain',
                        classe: 'Pagamento',
                        enum: 'TipoDocumentoPagamento'
                    })
                    .$promise.then(function(data) {
                        $scope.enumTipoDocumentoPagamento = [];
                        $scope.enumTipoDocumentoPagamento.push(data[1]);
                        $scope.object.tipoDocumento = data[1];
                    }).catch(function(response) {
                        $scope.$emit('crud-critical', 'load', response);
                    });
            }
            else {
                EnumService.enumValues({
                        pacote: 'br.com.araujoabreu.sgi.contasApagar.domain',
                        classe: 'Pagamento',
                        enum: 'TipoDocumentoPagamento'
                    })
                    .$promise.then(function(data) {
                        $scope.enumTipoDocumentoPagamento = [];
                        $scope.enumTipoDocumentoPagamento.push(data[0]);
                        $scope.enumTipoDocumentoPagamento.push(data[1]);
                    }).catch(function(response) {
                        $scope.$emit('crud-critical', 'load', response);
                    });
            }
        }

        $scope.montarLabelAgenciaConta = function(contaCorrente) {
            let conteudo = CalculadorFaturamento.montarCampoAgenciaConta(contaCorrente);
            if (CalculadorFaturamento.isCampoVazio(conteudo) || conteudo.trim() === '/') {
                return ' ';
            } else {
                return conteudo;
            }
        };

        $scope.verificarVencimentoEdicao = function() {
            if (!$scope.desabilitarMovitoVencimento) {
                $scope.object.motivoDataVencimento = null;
            }

            let dataParaVarificar = moment($scope.object.dataVencimento).add(1, 'days').toDate();

            Parcela.confirmarOuAjustarDataAgendamento(null, {
                    dataVencimento: dataParaVarificar
                })
                .$promise
                .then(data => {
                    $scope.object.dataAgendamento = data.dataVencimento;
                })
                .catch(err => {
                    console.error(err);
                });
        };

        $scope.desabilitarMovitoVencimento = function() {
            if (!CalculadorFaturamento.isCampoVazio($scope.original) && !CalculadorFaturamento.isCampoVazio($scope.original.dataVencimento) && $scope.screenMode == 'EDIT') {

                if (new Date($scope.original.dataVencimento) > new Date($scope.object.dataVencimento) || new Date($scope.original.dataVencimento) < new Date($scope.object.dataVencimento)) {
                    return false;
                }
                return true;
            }
            return true;
        };

        $scope.desabilitarMovitoPagamento = function() {
            if (!CalculadorFaturamento.isCampoVazio($scope.original) && !CalculadorFaturamento.isCampoVazio($scope.original.dataPagamento) && ($scope.screenMode == 'EDIT' || $scope
                    .screenMode == "RECEBER")) {

                if (new Date($scope.original.dataPagamento) > new Date($scope.object.dataPagamento) || new Date($scope.original.dataPagamento) < new Date($scope.object.dataPagamento)) {
                    return false;
                }
                return true;
            }
            return true;
        };

        function carregarMotivos(notaSaidaParcela) {
            if ($scope.screenMode === 'RECEBER') {
                carregarMotivosDescontos(notaSaidaParcela);
                carregarMotivosDevolucao(notaSaidaParcela);
            }
        }

        function carregarMotivosDescontos(notaSaidaParcela) {
            let codNota = notaSaidaParcela.notaSaida.codigo;

            $scope.listaMotivoDesconto = [];

            var pMotivoDescontoDevolucao = MotivoDescontoDevolucao.getListaDescontoByCodNota({
                id: codNota
            }).$promise;

            pMotivoDescontoDevolucao.then(function(data) {

                $scope.listaMotivoDesconto.push({
                    codigo: -1,
                    descricao: ''
                });

                angular.forEach(data, function(value, key) {
                    $scope.listaMotivoDesconto.push(value);
                });

                $scope.listaMotivoDesconto.sort((a, b) => {
                    if (a.descricao > b.descricao) {
                        return 1;
                    }
                    if (a.descricao < b.descricao) {
                        return -1;
                    }
                    return 0;
                });

            }).catch(function(response) {
                $mdDialog.hide();
                $scope.notification.error("", response);
            });
        }

        $scope.$on('changeMotivoDesconto', function() {
            if ($scope.object && $scope.object.descontoMotivo && $scope.object.descontoMotivo.codigo === -1) {
                delete $scope.object.descontoMotivo;
            }
        });

        function carregarMotivosDevolucao(notaSaidaParcela) {
            let codNota = notaSaidaParcela.notaSaida.codigo;

            $scope.listaMotivoDesconto = [];

            var pMotivoDescontoDevolucao = MotivoDescontoDevolucao.getListaDevolucaoByCodNota({
                id: codNota
            }).$promise;

            pMotivoDescontoDevolucao.then(function(data) {

                $scope.listaMotivoDevolucao.push({
                    codigo: -1,
                    descricao: ''
                });

                angular.forEach(data, function(value, key) {
                    $scope.listaMotivoDevolucao.push(value);
                });

                $scope.listaMotivoDevolucao.sort((a, b) => {
                    if (a.descricao > b.descricao) {
                        return 1;
                    }
                    if (a.descricao < b.descricao) {
                        return -1;
                    }
                    return 0;
                });

            }).catch(function(response) {
                $mdDialog.hide();
                $scope.notification.error("", response);
            });
        }

        $scope.$on('changeMotivoDevolucao', function() {
            if ($scope.object && $scope.object.devolucaoMotivo && $scope.object.devolucaoMotivo.codigo === -1) {
                delete $scope.object.devolucaoMotivo;
            }
        });


        /*
         * INÍCIO DOS $watch
         */

        $scope.$watch('object.valorRetencaoRetida', function() {
            $scope.valorLiquido = $scope.calcularValorLiquidoParcela($scope.object, $scope.valorTributos);
        });

        $scope.$watch('object.valorDevolucao', function() {
            $scope.valorLiquido = $scope.calcularValorLiquidoParcela($scope.object, $scope.valorTributos);
        });

        $scope.$watch('object.valorPermuta', function() {
            $scope.valorLiquido = $scope.calcularValorLiquidoParcela($scope.object, $scope.valorTributos);
        });

        $scope.$watch('object.valorDesconto', function() {
            $scope.valorLiquido = $scope.calcularValorLiquidoParcela($scope.object, $scope.valorTributos);
        });

        $scope.$watch('object.valorMulta', function() {
            $scope.valorLiquido = $scope.calcularValorLiquidoParcela($scope.object, $scope.valorTributos);
        });

        $scope.$watch('object.valorJuros', function() {
            $scope.valorLiquido = $scope.calcularValorLiquidoParcela($scope.object, $scope.valorTributos);
        });

        $scope.$watch('object.selected.valor', function() {
            $scope.valorLiquido = $scope.calcularValorLiquidoParcela($scope.object, $scope.valorTributos);
        });

        /*
         * FIM DOS $watch
         */





        /*
         * INÍCIO DAS FUNÇÕES DOS BOTÕES
         */

        $scope.editarParcela = function() {
            $state.go('contasareceber.editar', {
                objectId: $scope.object.codigo
            });
        };

        $scope.receberParcela = function() {
            $state.go('contasareceber.receber', {
                objectId: $scope.object.codigo
            });
        };

        $scope.reParcelar = function() {
            $state.go('notafiscal.exibir_por_acao', {
                objectId: $scope.object.notaSaida.codigo,
                acao: $scope.object.codigo,
                from: 'contasareceber.exibir'
            });
        };

        $scope.salvarParcela = function() {
            let time = new Date();

            $rootScope.loadingProgress = true;
            $scope.action = true;

            $scope.object.contaCorrentePrevista.banco = $scope.object.contaCorrente.banco;
            $scope.object.contaCorrentePrevista = $scope.object.contaCorrente;
            $scope.object.tipoDocumentoPrevisto = $scope.object.tipoDocumento;

            $scope.object.action = 'editar';

            if ($scope.object.codigo == null) {
                Parcela.save($scope.object).$promise.then(function(data) {
                    $rootScope.loadingProgress = false;
                    $state.go('contasareceber.exibir', {
                        objectId: $scope.object.codigo
                    });
                    $scope.action = false;
                    $scope.notification.success("Parcela", "Parcela salva com sucesso");
                }).catch(function(response) {
                    $rootScope.loadingProgress = false;
                    $scope.action = false;
                    $scope.$emit('crud-critical', 'search', response);
                    console.error(response);
                });
            } else {
				$scope.object.contaCorrenteRecebimento = $scope.object.contaCorrente;
				$scope.object.contaCorrenteRecebimento.banco = $scope.object.contaCorrente.banco;
				$scope.object.tipoDocumentoRecebimento = $scope.object.tipoDocumento;
                Parcela.updateParcela({
                    id: $scope.object.codigo
                }, $scope.object).$promise.then(function(data) {
                    $rootScope.loadingProgress = false;
                    $state.go('contasareceber.exibir', {
                        objectId: $scope.object.codigo
                    });
                    $scope.action = false;
                    $scope.notification.success("Parcela", "Parcela salva com sucesso");
                }).catch(function(response) {
                    $rootScope.loadingProgress = false;
                    $scope.action = false;
                    $scope.$emit('crud-critical', 'search', response);
                    console.error(response);
                });
            }
        };

        $scope.salvarRecebimentoParcela = function() {
            $rootScope.loadingProgress = true;
            $scope.action = true;

            $scope.object.contaCorrenteRecebimento.banco = $scope.object.contaCorrente.banco;
            $scope.object.contaCorrenteRecebimento = $scope.object.contaCorrente;
            $scope.object.tipoDocumentoRecebimento = $scope.object.tipoDocumento;

            angular.forEach($scope.object.parcelaRetencoes, function(value, key) {
                if (value.tipo === 'Contratual') {
                    value.valorAReter = $scope.object.valorRetencaoContratual;
                } else if (value.tipo === 'Acessória') {
                    value.valorAReter = $scope.object.valorRetencaoAcessoria;
                }
            });

            $scope.object.action = 'receber';
            $scope.object.listaTributos = $scope.listaTributos;
            $scope.object.valorLiquido = $scope.valorLiquido;

            Parcela.updateParcelaRecebimento({
                id: $scope.object.codigo
            }, $scope.object).$promise.then(function(data) {
                $rootScope.loadingProgress = false;

                verificarLote($scope.object.codigo).then(response => {
                    if (response.selecao) {
                        const mensagem =
                            "Existe um lote contábil diário para esta empresa/contabilização. Favor avisar à Contabilidade que um novo recebimento para esta data foi realizado e que o lote deverá ser reprocessado.";
                        $scope.notification.atencao("", mensagem);
                    }
                    $state.go('contasareceber.exibir', {
                        objectId: $scope.object.codigo
                    });
                    $scope.action = false;
                    $scope.notification.success("Parcela", "Parcela recebida com sucesso");
                }).catch(error => {
                    $scope.action = false;
                });
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'search', response);
                $rootScope.loadingProgress = false;
                $scope.action = false;
                console.error(response);
            });
        };

        $scope.cancelarEditar = function() {
            $state.go('contasareceber.exibir', {
                objectId: $scope.object.codigo
            });
        };

        $scope.cancelarRecebimento = function() {
            if ($rootScope.$lastState.name) {
                $state.go($rootScope.$lastState.name, $rootScope.$lastState.params);
            } else {
                $state.go('contasareceber.listar');
            }
        };

        $scope.retornar = function() {
            $state.go('contasareceber.listar');
        };

        $scope.reagendar = function(ev) {
            $mdDialog.show({
                controller: DialogControllerReagendamento,
                locals: {
                    objNotaSaidaParcela: $scope.object,
                    notification: $scope.notification,
                    parametros: {titulo: 'Parcela - Reagendamento', edicao: true}
                },
                templateUrl: 'partials/contasareceber/parcelas/html/partials/dialogs/reagendamento.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                fullscreen: false
            });

        };

         $scope.reagendarLeitura = function(ev) {
                    $mdDialog.show({
                        controller: DialogControllerReagendamento,
                        locals: {
                            objNotaSaidaParcela: $scope.object,
                            notification: $scope.notification,
                            parametros: {titulo: 'Agendamento - Dados gerais', edicao: false}
                        },
                        templateUrl: 'partials/contasareceber/parcelas/html/partials/dialogs/reagendamento.html',
                        parent: angular.element(document.body),
                        targetEvent: ev,
                        clickOutsideToClose: false,
                        fullscreen: false
                    });

                };

        function DialogControllerReagendamento($scope, $mdDialog, objNotaSaidaParcela, notification, parametros) {

            // O timeout é usado para resolver um erro que ocorre no dialog
            // que impede o campo da data de emissão de carregar previamente preenchida.
            $timeout(function() {
                $scope.notification = notification;

                $scope.object = angular.copy(objNotaSaidaParcela);
                let dataAgendamento = $scope.object.dataAgendamento;

                if (dataAgendamento && typeof dataAgendamento === 'string') {
                    $scope.object.dataAgendamento = moment(dataAgendamento);
                }

            }, 100);

            $scope.edicao = parametros.edicao;
            $scope.titulo = parametros.titulo;

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };

            $scope.salvar = function() {
                $rootScope.loadingProgress = true;

                Parcela.updateAgendamento({
                    id: $scope.object.codigo
                }, $scope.object).$promise.then(function(data) {
                    loadObj();
                    $rootScope.loadingProgress = false;
                    $scope.notification.success(null, "Reagendamento da parcela realizado.");
                }).catch(function(response) {
                    $rootScope.loadingProgress = false;
                    $scope.$emit('crud-critical', 'search', response);
                });

                $mdDialog.cancel();
            };
        }

        $scope.imprimir = function(ev) {
            let url = $state.href('contasareceber.imprimir', {
                objectId: $scope.object.codigo
            });
            window.open(url, '_blank');
        };

        // FUNÇÃO PARA COBRANÇA CRÔNICA.
        $scope.cobranca = function(ev) {
            $mdDialog.show({
                controller: DialogControllerCobrancaCronica,
                locals: {
                    objNotaSaidaParcela: $scope.object,
                    notification: $scope.notification
                },
                templateUrl: 'partials/contasareceber/parcelas/html/partials/dialogs/cobranca_cronica.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        //==== DIALOG COBRANÇA CRÔNICA ====//
        function DialogControllerCobrancaCronica($scope, $mdDialog, objNotaSaidaParcela, notification) {
            $scope.object = objNotaSaidaParcela;
            $scope.notification = notification;

            $scope.confirm = function() {
                $rootScope.loadingProgress = true;

                Parcela.marcarCobrancaCronica({
                    id: $scope.object.codigo
                }, $scope.object).$promise.then(function(data) {
                    loadObj();
                    $rootScope.loadingProgress = false;
                    $scope.notification.success(null, "Parcela marcada como problema crônico de cobrança.");
                }).catch(function(response) {
                    $rootScope.loadingProgress = false;
                    $scope.$emit('crud-critical', 'search', response);
                });

                $mdDialog.cancel();
            };

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
        }

        // FUNÇÃO PARA CANCELAR COBRANÇA CRÔNICA.
        $scope.cancelarcobranca = function(ev) {
            $mdDialog.show({
                controller: DialogControllerCancelarCobrancaCronica,
                locals: {
                    objNotaSaidaParcela: $scope.object,
                    notification: $scope.notification
                },
                templateUrl: 'partials/contasareceber/parcelas/html/partials/dialogs/cancelar_cobranca_cronica.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        //==== DIALOG CANCELAR COBRANÇA CRÔNICA ====//
        function DialogControllerCancelarCobrancaCronica($scope, $mdDialog, objNotaSaidaParcela, notification) {
            $scope.object = objNotaSaidaParcela;
            $scope.notification = notification;

            $scope.confirm = function() {
                Parcela.desmarcarCobrancaCronica({
                    id: $scope.object.codigo
                }, $scope.object).$promise.then(function(data) {
                    loadObj();
                    $rootScope.loadingProgress = false;
                    $scope.notification.success(null, "Parcela desmarcada como problema crônico de cobrança.");
                }).catch(function(response) {
                    $rootScope.loadingProgress = false;
                    $scope.$emit('crud-critical', 'search', response);
                });

                $mdDialog.cancel();
            };

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
        }

        /*
         * FIM DAS FUNÇÕES DOS BOTÕES
         */


        /*
         * INÍCIO ABA TRIBUTOS
         */

        //Atributo para verificar se há tributos na lista, que não sejam ISS.
        $scope.listaTemTributo = false;
        $scope.listaTemTributoComIss = false;
        $scope.totalPercentualTributos = 0;
        $scope.subtotalPercentualTributos = 0;
        $scope.totalValorTributos = 0;
        $scope.subtotalValorTributos = 0;

        // variavel do tipo string. No formato "UF/nome". Exemplo: "RJ/RIO DE JANEIRO".
        $scope.cidadeIssStr = null;

        // variavel do tipo Cidade.
        $scope.cidadeIssObj = null;

        // let tipo boolean. True: se tiver duas opções de cidade para o usuário escolher no campo "Cidade ISS".
        //                   False: Se tiver apenas uma ou nenhuma opção para o campo "Cidade ISS".
        $scope.opcoesCidades = false;

        //Lista para a opções do campo "Cidade ISS".
        $scope.listaCidadesISS = [];

        $scope.cidadePrestacaoServico = null;
        $scope.cidadeContrato = null;
        $scope.cidadeTributoIss = null;

        $scope.variacaoTributo = 0;

        initVariacaoTributo();

        function initAbaTributos(listaTributos) {
            $scope.listaTemTributo = ServicoAbaTributosCAR.verificarListaTemTributos(listaTributos);
            $scope.listaTemTributoComIss = ServicoAbaTributosCAR.verificarListaTemTributosComIss(listaTributos);
            $scope.totalPercentualTributos = $scope.percentualTotalTributos(listaTributos);
            $scope.subtotalPercentualTributos = ServicoAbaTributosCAR.getSubtotalPercentualTributos(listaTributos);
            $scope.totalValorTributos = ServicoAbaTributosCAR.getTotalValorTributos(listaTributos);
            $scope.subtotalValorTributos = ServicoAbaTributosCAR.getSubtotalValorTributos(listaTributos);

            $scope.valorTributos = $scope.calcularValorTributos($scope.object, listaTributos);
            $scope.valorLiquido = $scope.calcularValorLiquidoParcela($scope.object, $scope.valorTributos);
        }

        function initVariacaoTributo() {
            let parametro = {
                chave: "controlefiscal.limite.variacao.tributo"
            };

            Parametro.page(parametro).$promise.then(function(data) {

                let variacao = data.valor;
                $scope.variacaoTributo = Number(variacao);
            }).catch(function(response) {
                $scope.variacaoTributo = 0;

                console.error('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
            });
        }

        $scope.adicionarTributo = function(event) {
            ServicoDialogTributoCAR.adicionarTributo(event, $scope.listaTributos, $scope.object, $scope.variacaoTributo, initAbaTributos);
        };

        $scope.adicionarTributoIss = function(event) {
            if (!CalculadorFaturamento.isCampoVazio($scope.cidadeIssObj)) {
                ServicoDialogTributoCAR.adicionarTributoIss(event, $scope.cidadeIssObj, $scope.listaTributos, $scope.object, $scope.variacaoTributo, initAbaTributos);
            } else {
                $scope.notification.atencao("Atenção", "Escolha uma cidade no campo 'Cidade ISS'");
            }
        };

        $scope.editarTributo = function(event, tributo) {
            ServicoDialogTributoCAR.editarTributo(event, tributo, $scope.listaTributos, $scope.object, $scope.variacaoTributo, initAbaTributos);
        };

        $scope.editarTributoIss = function(event, tributo) {
            ServicoDialogTributoCAR.editarTributoIss(event, tributo, $scope.listaTributos, $scope.object, $scope.variacaoTributo, initAbaTributos);
        };

        $scope.excluirTributo = function(event, tributo) {
            ServicoDialogTributoCAR.excluirTributo(tributo, $scope.listaTributos);
            initAbaTributos($scope.listaTributos);

            //tributo ISS
            if (tributo.categoriaTributo.tributoTO.codigo == 4) {
                $scope.cidadeTributoIss = null;
                $scope.cidadeIssStr = null;
                $scope.listaCidadesISS = [];
                $scope.escolherCidadeIss();
            }

        };

        $scope.quantidadeDesconto = function(tipo) {
            let qtd = 0;
            $scope.object.listaNotaSaidaParcelaMotivoDescDev.filter(value => {
                if (value.tipo === tipo) {
                    qtd = qtd + 1;
                }
            });
            return qtd;
        }

        function DialogAdicionarDescontoDevolucao($scope, $mdDialog, tipo, mode, obj, fnDescontoDevolucao, listaMotivo, listaSelecionada) {
            $scope.object = obj;
            let objOld = angular.copy(obj);
            let mode_title = mode === 'edit' ? 'Edição' : 'Novo'
            if (tipo === 'DESCONTO') {
                $scope.title = `Desconto - ${mode_title}`;
                $scope.lblCombo = 'Motivo do desconto';
            } else {
                $scope.title = `Devolução - ${mode_title}`;
                $scope.lblCombo = 'Motivo da devolução';
            }

            let listaCodigoJaSelecionado = [];

            listaSelecionada.filter(value => {
                if (tipo === value.tipo) {
                    listaCodigoJaSelecionado.push(value.codigoMotivoDescontoDevolucao);
                }
            });

            $scope.listaMotivos = listaMotivo.filter(value => {
                return listaCodigoJaSelecionado.indexOf(value.codigo) < 0;
            });

            if (mode === 'edit') {
                $scope.listaMotivos.push(obj.motivoDescontoDevolucao);
                $scope.listaMotivos.sort((a, b) => {
                    if (a.descricao > b.descricao) {
                        return 1;
                    }
                    if (a.descricao < b.descricao) {
                        return -1;
                    }
                    return 0;
                });
            }

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
            $scope.confirmar = function() {
                $scope.listaMotivos.filter(value => {
                    if (value.codigo === $scope.object.codigoMotivoDescontoDevolucao) {
                        $scope.object.motivoDescontoDevolucao = value;
                        return;
                    }
                });
                fnDescontoDevolucao($scope.object, objOld, $scope.hide);
            }
        }

        function fnAdicionarDescontoDevolucao(notaSaidaParcelaMotivoDescDev, objOld, fnHide) {
            $scope.object.listaNotaSaidaParcelaMotivoDescDev.push(notaSaidaParcelaMotivoDescDev);
            $scope.object = calcularSubTotaisTabelaDescontoDevolucao($scope.object);
            fnHide();
        }

        function fnEditarDescontoDevolucao(notaSaidaParcelaMotivoDescDev, objOld, fnHide) {
            angular.forEach($scope.object.listaNotaSaidaParcelaMotivoDescDev, function(item, key) {
                if (item.tipo === objOld.tipo &&
                    item.valor === objOld.valor &&
                    item.codigoMotivoDescontoDevolucao === objOld.codigoMotivoDescontoDevolucao) {
                    item.codigoMotivoDescontoDevolucao = notaSaidaParcelaMotivoDescDev.codigoMotivoDescontoDevolucao;
                    item.motivoDescontoDevolucao = notaSaidaParcelaMotivoDescDev.motivoDescontoDevolucao;
                    item.valor = notaSaidaParcelaMotivoDescDev.valor;
                    return;
                }
            });
            $scope.object = calcularSubTotaisTabelaDescontoDevolucao($scope.object);
            fnHide();
        }


        $scope.editarDescontoDevolucao = function(event, item, tipo) {
            $mdDialog.show({
                controller: DialogAdicionarDescontoDevolucao,
                locals: {
                    tipo: tipo,
                    mode: 'edit',
                    obj: angular.copy(item),
                    fnDescontoDevolucao: fnEditarDescontoDevolucao,
                    listaMotivo: tipo === 'DESCONTO' ? $scope.listaMotivoDesconto : $scope.listaMotivoDevolucao,
                    listaSelecionada: $scope.object.listaNotaSaidaParcelaMotivoDescDev
                },
                templateUrl: 'partials/contasareceber/parcelas/html/partials/dialogs/desconto_devolucao_dialog.html',
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        $scope.adicionarDescontoDevolucao = function(event, tipo) {
            let obj = {
                codigo: null,
                codigoNotaSaidaParcela: $scope.object.codigo,
                tipo: tipo,
                codigoMotivoDescontoDevolucao: -1
            };
            $mdDialog.show({
                controller: DialogAdicionarDescontoDevolucao,
                locals: {
                    tipo: tipo,
                    mode: 'new',
                    obj: obj,
                    fnDescontoDevolucao: fnAdicionarDescontoDevolucao,
                    codigoNotaSaidaParcela: $scope.object.codigo,
                    listaMotivo: tipo === 'DESCONTO' ? $scope.listaMotivoDesconto : $scope.listaMotivoDevolucao,
                    listaSelecionada: $scope.object.listaNotaSaidaParcelaMotivoDescDev
                },
                templateUrl: 'partials/contasareceber/parcelas/html/partials/dialogs/desconto_devolucao_dialog.html',
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        $scope.excluirDescontoDevolucao = function(event, item, tipo) {
            $scope.object.listaNotaSaidaParcelaMotivoDescDev.splice($scope.object.listaNotaSaidaParcelaMotivoDescDev.indexOf(item), 1);
            $scope.object = calcularSubTotaisTabelaDescontoDevolucao($scope.object);
        };

        /**
         * Busca as cidades para gerenciar o campo "Cidade ISS"
         *
         * Parâmetros:
         * Faturamento - Faturamento da parcela
         *
         * CIDADES:
         * Cidade 1 - Cidade da prestação de serviço
         * Cidade 2 - Cidade do tributo ISS (se houver tributo ISS)
         * Cidade 3 - Cidade do contrato
         */
        $scope.buscarCidades = function(notaSaida, arrayTributos) {

            $scope.cidadePrestacaoServico = angular.copy(notaSaida.faturamentoEtapaTO.faturamento.cidadePrestacaoServico);

            let promessa2 = Contrato.cidadeUnidade(notaSaida.faturamentoEtapaTO.faturamento.centroFinanceiro.contratoTO).$promise;

            let notaTributoAliquotas = angular.copy(arrayTributos);
            let promises = [];

            angular.forEach(notaTributoAliquotas, function(value, key) {
                promises.push(CategoriaTributo.get({
                    id: value.aliquota.codigoCategoriaTributo
                }).$promise);
            });

            $q.all(promises).then(function(lista) {

                angular.forEach(lista, function(value, key) {
                    if (value.tributoTO.codigo == 4) {
                        $scope.cidadeTributoIss = value.cidade;
                    }
                });

                return promessa2;
            }).then(function(data) {
                $scope.cidadeContrato = data;
                $scope.escolherCidadeIss();

            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });

        };

        $scope.escolherCidadeIss = function() {

            if (!CalculadorFaturamento.isCampoVazio($scope.cidadeTributoIss)) {
                $scope.cidadeIssObj = angular.copy($scope.cidadeTributoIss);
                $scope.cidadeIssStr = $scope.cidadeTributoIss.nome + "/" + $scope.cidadeTributoIss.uf;
                $scope.opcoesCidades = false;

            } else if ($scope.cidadePrestacaoServico.codigo == $scope.cidadeContrato.codigo) {
                $scope.cidadeIssObj = angular.copy($scope.cidadeContrato);
                $scope.cidadeIssStr = $scope.cidadeContrato.nome + "/" + $scope.cidadeContrato.uf;
                $scope.opcoesCidades = false;

            } else {
                $scope.cidadeIssObj = null;
                $scope.cidadeIssStr = null;
                $scope.opcoesCidades = true;

                $scope.listaCidadesISS.push({
                    objeto: null,
                    texto: ''
                });

                $scope.listaCidadesISS.push({
                    objeto: $scope.cidadeContrato,
                    texto: '' + $scope.cidadeContrato.nome + '/' + $scope.cidadeContrato.uf + ''
                });

                $scope.listaCidadesISS.push({
                    objeto: $scope.cidadePrestacaoServico,
                    texto: '' + $scope.cidadePrestacaoServico.nome + '/' + $scope.cidadePrestacaoServico.uf + ''
                });

            }

        };

        $scope.selecionarCidade = function(paramCidade) {
            $scope.cidadeIssObj = paramCidade;
        };

        /*
         * FIM ABA TRIBUTOS
         */

        //==== FUNÇÃO QUE CANCELA A NOTA ====//
        $scope.acaoCancelarNotaFiscal = function(nota, fn_hide) {
            $rootScope.loadingProgress = true;
            NotaFiscalFaturamento.cancelarNotaFiscal(nota)
                .$promise.then(function(data) {
                    $rootScope.loadingProgress = false;
                    $scope.notification.success("Nota fiscal", "Nota fiscal cancelada com sucesso");
                    fn_hide();
                }).catch(function(response) {
                    $rootScope.loadingProgress = false;
                    console.error(response);
                    $scope.$emit('crud-critical', 'search', response);
                });
        }


        // ==== ACAO CANCELAR NOTA FISCAL ====//
        $scope.cancelarNotaFiscal = function(ev, notaSaida, num_parcelas) {
            $mdDialog.show({
                controller: DialogCancelarNotaFiscal,
                locals: {
                    nota: notaSaida,
                    fn_cancelar_nota: $scope.acaoCancelarNotaFiscal,
                    num_parcelas: num_parcelas
                },
                templateUrl: 'partials/dialogs/confirm_cancel_or_delete.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        //==== DIALOG CANCELAR NOTA FISCAL ====//
        function DialogCancelarNotaFiscal($scope, $mdDialog, nota, fn_cancelar_nota, num_parcelas) {
            $scope.name_ok_btn = 'CONFIRMAR';
            $scope.name_cancel_btn = 'CANCELAR';

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
            $scope.confirmar = function() {
                fn_cancelar_nota(nota, $scope.hide);
            };

            $scope.title = 'Nota Fiscal - Cancelar: Confirmação';

            if (num_parcelas == 1) {
                $scope.msg = 'A única parcela da Nota Fiscal está cancelada. Deseja cancelar também a NF (esta operação não poderá ser desfeita)?';
            } else {
                $scope.msg = 'Todas as parcelas da Nota Fiscal estão canceladas. Deseja cancelar também a NF (esta operação não poderá ser desfeita)?';
            }
        }

        $scope.cancelarParcela = function(event) {
            let chaveMotivo = 'faturamento-canc_parcela';
            let titulo = 'Motivo - Cancelamento da Parcela';

            // Função que será executada no confirmar no dialog de motivo de retorno.
            $scope.confirmarMotivo = function(motivoRetornoDocumento, fn_Hide) {
                // MotivoRetornoDocumentoFaturamentoTO
                $scope.motivoRetornoDocumentoParcela = {};
                $scope.motivoRetornoDocumentoParcela.notaSaidaParcela = {
                    codigo: $scope.object.codigo
                };
                $scope.motivoRetornoDocumentoParcela.motivoRetornoDocumento = motivoRetornoDocumento;

                $rootScope.loadingProgress = true;

                MotivoRetornoParcela.save($scope.motivoRetornoDocumentoParcela)
                    .$promise.then(function(data) {
                        loadObj();
                        $scope.notification.success("Cancelada", "Parcela cancelada com sucesso");
                        fn_Hide();

                        let filter = {};
                        filter.notaSaida = {};
                        filter.notaSaida.codigo = $scope.object.notaSaida.codigo;
                        NotaSaidaParcela.page(null, filter)
                            .$promise.then(function(data) {
                                let totalCancelado = 0;
                                let totalList = data.content.length;
                                angular.forEach(data.content, function(parcela, key) {
                                    if (parcela.situacao === 'Cancelada') {
                                        totalCancelado++;
                                    }
                                });
                                if (totalCancelado == totalList) {
                                    $scope.cancelarNotaFiscal(event, $scope.object.notaSaida, totalList);
                                }
                                $rootScope.loadingProgress = false;
                            }).catch(function(response) {
                                console.error(response);
                                $scope.$emit('crud-critical', 'search', response);
                            });
                        $rootScope.loadingProgress = false;
                    }).catch(function(response) {
                        console.error('crud-critical', 'dialog', response);
                        $rootScope.loadingProgress = false;
                        $scope.notification.criticalError(response.status, response.statusText, response.data);
                    });
            };

            // Função que será executada no cancelar no dialog de motivo de retorno.
            $scope.cancelarMotivo = function(fn_Hide) {
                fn_Hide();
            };

            // Passar para o serviço, as funções que serão chamadas ao confirmar ou cancelar o motivo de retorno.
            MotivoRetornoDialog.show(event, chaveMotivo, titulo, $scope.confirmarMotivo, $scope.cancelarMotivo);
        };

        $scope.cancelarRecebimentoMotivoRetorno = function(event) {
            if ($scope.baixasRetencaoList && $scope.baixasRetencaoList.length > 0 && $scope.object.valorRetencaoRetida) {
                confirmarCancelamentoRecebimento(event);
            } else {
                motivoCancelamentoRecebimento();
            }
        };

        let verificarLoteLoteContabilDiario = () => {
            return NotaSaidaParcela.verificarLoteLoteContabilDiario({
                id: $scope.object.codigo
            }).$promise.then(function(data) {
                if (data.value) {
                    let dataFormatada = $filter('date')($scope.object.dataContabilizacao, 'dd/MM/yyyy');
                    const mensagem =
                        `Existe um lote contábil diário para esta empresa/contabilização (${dataFormatada}). Favor avisar à Contabilidade que um recebimento para esta data foi cancelado e que o lote deverá ser reprocessado.`;
                    $scope.notification.atencao("", mensagem);
                }
                $rootScope.loadingProgress = false;
                $scope.action = false;
            }).catch(function(response) {
                $rootScope.loadingProgress = false;
                $scope.action = false;
                $scope.$emit('crud-critical', 'search', response);
            });
        };

        let motivoCancelamentoRecebimento = ($event) => {
            let chaveMotivo = 'faturamento-canc_receb';
            let titulo = 'Motivos de Retorno - Cancelamento de Recebimento de Parcela';

            // Função que será executada no confirmar no dialog de motivo de retorno.
            let confirmarMotivo = (motivoRetornoDocumento, fn_Hide) => {
                $scope.motivoRetornoDocumentoParcela = {};
                $scope.motivoRetornoDocumentoParcela.notaSaidaParcela = {
                    codigo: $scope.object.codigo
                };

                $scope.motivoRetornoDocumentoParcela.motivoRetornoDocumento = motivoRetornoDocumento;

                $scope.action = true;
                $rootScope.loadingProgress = true;

                verificarLoteLoteContabilDiario().then(() => {
                    MotivoRetornoParcela.recebimento($scope.motivoRetornoDocumentoParcela)
                        .$promise.then(function(data) {
                            Parcela.cancelarParcelaRecebimento({
                                id: $scope.object.codigo
                            }).$promise.then(function(data) {
                                loadObj();
                                $scope.notification.success("Cancelado", "Recebimento cancelado com sucesso");
                                fn_Hide();
                                $rootScope.loadingProgress = false;
                                $scope.action = false;
                            }).catch(function(response) {
                                fn_Hide();
                                console.error('crud-critical', 'dialog', response);
                                $rootScope.loadingProgress = false;
                                $scope.action = false;
                            });

                        }).catch(function(response) {
                            console.error('crud-critical', 'dialog', response);
                            $rootScope.loadingProgress = false;
                            $scope.notification.criticalError(response.status, response.statusText, response.data);
                        });
                });

            };

            // Função que será executada no cancelar no dialog de motivo de retorno.
            let cancelarMotivo = fn_Hide => {
                fn_Hide();
            };

            // Passar para o serviço, as funções que serão chamadas ao confirmar ou cancelar o motivo de retorno.
            MotivoRetornoDialog.show($event, chaveMotivo, titulo, confirmarMotivo, cancelarMotivo);
        };

        /*
         *  INÍCIO ABA CANCELAMENTO
         */

        $scope.listMotivos = null;

        $scope.carregarDadosCancelamento = function() {
            $rootScope.loadingProgress = true;
            MotivoRetornoParcela.page(null, {
                    notaSaidaParcela: {
                        codigo: $scope.objectId
                    }
                })
                .$promise.then(function(data) {
                    $rootScope.loadingProgress = false;
                    $scope.listMotivos = data.content;

                    $scope.listMotivos.forEach((item) => {
                        buscarMotivoAplicacao(item.motivoRetornoDocumento);
                    });

                }).catch(function(response) {
                    $rootScope.loadingProgress = false;
                    $scope.$emit('crud-critical', 'search', response);
                });
        };

        var buscarMotivoAplicacao = (motivoRetornoDocumento) => {

            let pMotivoAplicacao = MotivoAplicacao.findByCodMotivoRetornoDoc({
                id: motivoRetornoDocumento.codigo
            }).$promise;

            pMotivoAplicacao.then(function(data) {
                motivoRetornoDocumento.aplicacao = data;
            }).catch(function(response) {
                $rootScope.loadingProgress = false;
                $scope.$emit('crud-critical', 'search', response);
            });
        };

        /*
         *  FIM ABA CANCELAMENTO
         */

        loadObj();

        function DialogControllerBaixas($scope, $mdDialog, notaSaidaParcela, fnCriarBaixaRetencao, bancoList, fnMontarLabelAgenciaConta) {
            const init = () => {
                $scope.object = {};
                $scope.object.bancoSelecionado = notaSaidaParcela.bancoSelecionado;
                $scope.object.valorBaixa = 0;
                $scope.notaSaidaParcela = notaSaidaParcela;
                $scope.action = false;
                $scope.bancoList = bancoList;
                $scope.montarLabelAgenciaConta = fnMontarLabelAgenciaConta;
                console.log("$scope.object antes: ", $scope.object);
                changeBanco().then(() => {
                    $scope.object.contaCorrente = notaSaidaParcela.contaCorrenteRecebimento;
                    $scope.object.contaCorrente.codigo = notaSaidaParcela.contaCorrenteRecebimento.codigo;
                    $scope.object.bancoSelecionado = notaSaidaParcela.bancoSelecionado;
                });
                console.log("notaSaidaParcela: ", notaSaidaParcela);
                console.log("$scope.object: ", $scope.object);
            };

            // FIXME: Refatorar
            $scope.carregarContasCorrentes = function(codigoBanco, codigoPessoa) {
                return ContaCorrente.page(null, {
                        banco: {
                            codigo: codigoBanco
                        },
                        pessoa: {
                            codigo: codigoPessoa
                        }
                    })
                    .$promise.then(function(data) {
                        $scope.agenciaContaList = data.content;
                    }).catch(function(response) {
                        $scope.$emit('crud-critical', 'contrato load', response);
                        $scope.agenciaContaList = [];
                    });
            };

            const changeBanco = () => {
                return new Promise((resolve, reject) => {
                    $scope.object.contaCorrente = undefined;
                    if ($scope.notaSaidaParcela.notaSaida && $scope.object.bancoSelecionado) {
                        if ($scope.notaSaidaParcela.notaSaida.empresa &&
                            $scope.notaSaidaParcela.notaSaida.empresa.matriz &&
                            $scope.notaSaidaParcela.notaSaida.empresa.matriz.codigo) {
                            $scope.carregarContasCorrentes($scope.object.bancoSelecionado.codigo, $scope.notaSaidaParcela.notaSaida.empresa.matriz.codigo)
                                .then(() => resolve(true));
                        } else {
                            $scope.carregarContasCorrentes($scope.notaSaidaParcela.bancoSelecionado.codigo, $scope.notaSaidaParcela.notaSaida.empresa.codigo)
                                .then(() => resolve(true));
                        }
                    }
                    resolve(true);
                });
            };

            $scope.$on('changeBanco', changeBanco);

            $scope.confirm = () => {
                $scope.action = true;
                fnCriarBaixaRetencao($scope.object)
                    .then(() => {
                        $scope.action = false;
                        $mdDialog.hide();
                    })
                    .catch(() => $scope.action = false);
            };

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };

            init();
        }

        let verificarLoteBaixas = (baixa) => {
            $rootScope.loadingProgress = true;
            $scope.action = true;
            return NotaSaidaParcelaBaixaRetencao.verificarLoteBaixas({
                id: baixa.codigo
            }).$promise.then(function(data) {
                if (data.value) {
                    const mensagem =
                        "Existe um lote contábil diário para esta empresa/contabilização. Favor avisar à Contabilidade que uma nova baixa de retenção para esta data foi realizado e que o lote deverá ser reprocessado.";
                    $scope.notification.atencao("", mensagem);
                }
                $rootScope.loadingProgress = false;
                $scope.action = false;
            }).catch(function(response) {
                $rootScope.loadingProgress = false;
                $scope.action = false;
                $scope.$emit('crud-critical', 'search', response);
            });
        };

        const fnCriarBaixaRetencao = (baixaRetencao) => {
            baixaRetencao.notaSaidaParcela = {};
            baixaRetencao.notaSaidaParcela.codigo = $scope.object.codigo;
            return NotaSaidaParcelaBaixaRetencao.save(baixaRetencao).$promise.then(response => {
                verificarLoteBaixas(response);
                carregarBaixasRetencaoDaNotaSaidaParcela().then(() => {
                    Parcela.get({
                        id: $scope.objectId
                    }).$promise.then(response => {
                        $scope.object.saldoRetencao = response.saldoRetencao;
                    });
                    $rootScope.loadingProgress = false;
                    $scope.action = false;
                    $scope.notification.success(null, "Baixa de retenção adicionada com sucesso.");
                });
            }).catch(response => {
                $rootScope.loadingProgress = false;
                $scope.action = false;
                $scope.$emit('crud-critical', 'search', response);
            });
        };

        $scope.adicionarBaixaRetencao = ($event) => {
            $mdDialog.show({
                controller: DialogControllerBaixas,
                locals: {
                    notaSaidaParcela: $scope.object,
                    fnCriarBaixaRetencao: fnCriarBaixaRetencao,
                    bancoList: $scope.bancoList,
                    fnMontarLabelAgenciaConta: $scope.montarLabelAgenciaConta
                },
                templateUrl: 'partials/contasareceber/parcelas/html/partials/dialogs/dialog_baixa_retencao.html',
                parent: angular.element(document.body),
                targetEvent: $event,
                clickOutsideToClose: false,
                fullscreen: false
            });
        };


        function DialogControllerBaixasRemover($scope, $mdDialog, baixa, fnRemoverBaixaRetencao) {
            $scope.object = baixa;
            $scope.confirm = () => {
                $scope.action = true;
                fnRemoverBaixaRetencao($scope.object)
                    .then(() => {
                        $scope.action = false;
                        $mdDialog.hide();
                    })
                    .catch(() => $scope.action = false);
            };

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
        }

        const fnRemoverBaixaRetencao = (baixaRetencao) => {
            baixaRetencao.notaSaidaParcela = {};
            baixaRetencao.notaSaidaParcela.codigo = $scope.object.codigo;
            return NotaSaidaParcelaBaixaRetencao.removerBaixaRetencao({
                id: baixaRetencao.codigo
            }).$promise.then(response => {
                carregarBaixasRetencaoDaNotaSaidaParcela()
                    .then(() => {
                        Parcela.get({
                            id: $scope.objectId
                        }).$promise.then(response => {
                            $scope.object.saldoRetencao = response.saldoRetencao;
                        });
                        $rootScope.loadingProgress = false;
                        $scope.action = false;
                        $scope.notification.success(null, "Baixa de retenção removida com sucesso.");
                    });
            }).catch(response => {
                $rootScope.loadingProgress = false;
                $scope.action = false;
                $scope.$emit('crud-critical', 'search', response);
            });
        };

        $scope.excluirBaixaRetencao = ($event, item) => {
            $mdDialog.show({
                controller: DialogControllerBaixasRemover,
                locals: {
                    baixa: item,
                    fnRemoverBaixaRetencao: fnRemoverBaixaRetencao
                },
                templateUrl: 'partials/contasareceber/parcelas/html/partials/dialogs/dialog_confirm_remover_baixa.html',
                parent: angular.element(document.body),
                targetEvent: $event,
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        const fnConfirmarCancelamentoRecebimento = ($event) => {
            return new Promise((resolve, reject) => {
                motivoCancelamentoRecebimento($event);
                resolve(true);
            });
        };

        function DialogControllerConfirmarCancelamentoRecebimento($scope, $mdDialog, fnConfirmarCancelamentoRecebimento, $event) {
            $scope.confirm = () => {
                $scope.action = true;
                fnConfirmarCancelamentoRecebimento($event)
                    .then(() => {
                        $scope.action = false;
                        $mdDialog.hide();
                    })
                    .catch(() => $scope.action = false);
            };

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
        }

        let confirmarCancelamentoRecebimento = (event) => {
            $mdDialog.show({
                controller: DialogControllerConfirmarCancelamentoRecebimento,
                locals: {
                    fnConfirmarCancelamentoRecebimento: fnConfirmarCancelamentoRecebimento,
                    $event: event
                },
                templateUrl: 'partials/contasareceber/parcelas/html/partials/dialogs/dialog_confirm_cancelamento_recebimento.html',
                parent: angular.element(document.body),
                targetEvent: event,
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        $scope.exibirDetalheCliente = function(codigo, tipoPessoa, tipo, ev) {
            if (codigo == null) {
                return;
            }
            let useFullScreen = true;

            if (tipo == null) {
                tipo = 'ninformado';
            }

            let pPessoa = PessoaJuridica.popupFaturamento({
                id: codigo,
                tipo: tipoPessoa,
                idCl: $scope.object.notaSaida.faturamentoEtapaTO.faturamento.centroFinanceiro.codigo
            });

            pPessoa.$promise.then((dados) => {

                buscarClientePrincipalParaDialog(dados);

                $mdDialog.show({
                    controller: DialogControllerPessoa,
                    locals: {
                        pessoa: dados,
                        tipo: tipo
                    },
                    templateUrl: 'partials/contasareceber/parcelas/html/partials/dialogs/dialog_detalhe_cliente.html',
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

        $scope.liberarEdicaoTipoDocumento = () => {
        	if ($scope.object.contaCorrente) {
        		if ($scope.object.contaCorrente.boletoFaturamento)
        			return $scope.object.contaCorrente.boletoFaturamento;
        		else
        			return false;
        	}
			else
				return false;
        }

        let buscarClientePrincipalParaDialog = (objetoCliente) => {

            let clientePrincipal = objetoCliente.clientePrincipalTO;

            if (clientePrincipal && clientePrincipal.codigo) {
                let pPessoa = PessoaJuridica.popupFaturamento({
                    id: clientePrincipal.codigo,
                    tipo: clientePrincipal.tipoPessoa,
                    idCl: $scope.object.notaSaida.faturamentoEtapaTO.faturamento.centroFinanceiro.codigo
                });

                pPessoa.$promise.then((dados) => {
                    objetoCliente.clientePrincipalTO = dados;

                }).catch(response => {
                    $scope.$emit('crud-critical', 'search', response);
                });
            }
        };

        $scope.observacoes = (ev) => {
            ParcelaService.dialogObservacao(ev, $scope.object, $scope.notification, 'Observações (controle do recebimento) - Edição');
        };
    }
})();
