(function() {
    'use strict';

    angular.module('app.notaFiscalFaturamento').controller('NotaFiscalFaturamentoCadastroCtrl', notaFiscalFaturamentoCadastroCtrlFn);

    function notaFiscalFaturamentoCadastroCtrlFn($scope, $state, $stateParams, $mdDialog, EnumService, $rootScope, $timeout, CalculadorFaturamento, NotaFiscalFaturamento, Tributo,
        CategoriaTributo, PessoaJuridica, Unidade, $filter, FaturamentoAliquota, MotivoRetornoDataCancelamentoDialog, MotivoRetornoDialog, MotivoRetornoNota, $q, Contrato, $location,
        FaturamentoRetencao, NotaTributoAliquota, NotaSaidaParcela, StringService, $window, NotaTributoAliquotaEmissao, Parametro, UsuarioLogadoService, Feriado, NotasFiscaisService,
        DialogControllerEditarNotaTributoAliquota) {

        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;
        $scope.action = false;
        $scope.exibirBotoes = false;
        $scope.Math = $window.Math;

        $scope.parcelaSelecionada = undefined;

        const catchError = (response) => {
            $scope.action = false;
            $rootScope.loadingProgress = false;
            $scope.botaoNumerarDesabilitado = false;
            $scope.botaoConfirmarcadastroDesabilitado = false;
            $scope.$emit('crud-critical', 'search', response);
            reject(false);
        };

        $scope.verificarPermissao = () => {
            // Exibir apenas quando for usuários do faturamento;
            UsuarioLogadoService.apenasFaturamento().$promise.then(response => {
                $scope.exibirBotoes = response.value;
            });
        };

        $scope.verificarPermissao();

        /**
         * usarFuncao - true ou false | Se for true, usa a função passada no segundo parametro
         * funcao - função para ser executada
         */
        $scope.buscarEmpresas = function(usarFuncao, funcao) {
            let p1 = new Promise(
                function(resolve, reject) {
                    Unidade.unidadesfaturamento().$promise.then(function(data) {
                        $scope.empresaList = [];
                        $scope.empresaList.push({
                            codigo: -1,
                            codnome: ''
                        });
                        angular.forEach(data, function(value, key) {
                            $scope.empresaList.push(value);
                        });

                        if (usarFuncao) {
                            funcao();
                        }
                        resolve(true);
                    }).catch(catchError);
                });
            return p1;
        };

        $scope.$on('changeEmpresa', function() {
            if ($scope.object.notaSaida.empresa.codigo == -1) {
                delete $scope.object.notaSaida.empresa;
            }
        });

        $scope.index = 0;
        $scope.object = {};
        $scope.object.natureza = "Venda à vista";
        $scope.parcelaSelecionada = {};
        $scope.bloquearEmissaoNF = false;
        $scope.object.notaSaidaParcelas = [];
        $scope.objectIdEtapa = $stateParams.objectIdEtapa;

        $scope.selectedItemServico = undefined;
        $scope.resultsServico = [];

        $scope.percentualRetencaoContratual = 0;
        $scope.valorRetencaoContratual = 0;
        $scope.percentualRetencaoAcessoria = 0;
        $scope.valorRetencaoAcessoria = 0;
        $scope.valorRetencoes = 0;
        $scope.totalParcelas = 0;
        $scope.permuta = 0;
        $scope.juros = 0;
        $scope.valorLiquido = 0;
        $scope.valorTributos = 0;
        $scope.valorDevolucao = 0;
        $scope.parcelasValorTributos = 0;
        $scope.valorTributosView = 0;
        $scope.object.status = "Aberta";
        $scope.labelVcto = 'Vencimento';
        $scope.situacaoLoteEnvio = undefined;
        $scope.object.empresa = undefined;

        $scope.temNotaCarioca = false;

        $scope.vindoDoContasReceber = false;
        $scope.codigoParaVoltarContasReceber = undefined;

        $scope.todasParcelasRecebidas = false;

        if ($location.search().acao && $location.search().acao !== 'FAT' && $location.search().acao !== 'parcelar') {
            $scope.codigoParaVoltarContasReceber = $location.search().acao;
        }

        /*
         * O botão 'Numerar' precisa ficar desabilitado para que o usuário não apertar no botão novamente
         * enquanto ocorre as operações de numerar ou cancelar numeração, já que estas operações estão
         * levando muito tempo para serem concluidas.
         */
        $scope.botaoNumerarDesabilitado = false;


        //inicio - carregamento da tela no screenMode VIEW
        $scope.carregamentoCompletoNotaFiscal = (resultado) => {
            if (!CalculadorFaturamento.isCampoVazio(resultado)) {
                if (!CalculadorFaturamento.isCampoVazio(resultado.codigo)) {
                    $scope.object.codigo = resultado.codigo;
                }

                if (!CalculadorFaturamento.isCampoVazio(resultado.empresa)) {
                    $scope.object.empresa = resultado.empresa;
                }

                if (!CalculadorFaturamento.isCampoVazio(resultado.numero)) {
                    $scope.object.numero = resultado.numero;
                }

                if (!CalculadorFaturamento.isCampoVazio(resultado.status)) {
                    $scope.object.status = resultado.status;
                }

                if (!CalculadorFaturamento.isCampoVazio(resultado.dataEmissao)) {
                    let dataAjustada = ajustarTimeZoneData(resultado.dataEmissao);
                    resultado.dataEmissao = dataAjustada;
                    $scope.object.dataEmissao = dataAjustada;
                }

                if (!CalculadorFaturamento.isCampoVazio(resultado.servicoTipo)) {
                    $scope.object.servicoTipo = resultado.servicoTipo;
                    $scope.gerenciarServicoTipoDadosPrincipais();
                }

                if (!CalculadorFaturamento.isCampoVazio(resultado.natureza)) {
                    $scope.object.natureza = resultado.natureza;
                }

                if (!CalculadorFaturamento.isCampoVazio(resultado.formaPagamento)) {
                    $scope.object.formaPagamento = resultado.formaPagamento;
                }

                if (!CalculadorFaturamento.isCampoVazio(resultado.vencimento)) {
                    $scope.object.vencimento = new Date(resultado.vencimento);
                }

                if (!CalculadorFaturamento.isCampoVazio(resultado.notaSaidaParcelas)) {
                    $scope.object.notaSaidaParcelas = resultado.notaSaidaParcelas;
                    $scope.object.notaSaidaParcelas.sort((a, b) => {
                        return a.ordem > b.ordem ? 1 : -1;
                    });
                }

                return $q.all(
                    $scope.carregamentoBasicoNotaFiscal(resultado),
                    carregamentoMotivosRetorno(resultado)
                );
            }
            return $q(function(resolve, reject) {
                resolve();
            });
        };

        let carregamentoMotivosRetorno = function(resultado) {
            return $q(function(resolve, reject) {
                MotivoRetornoNota.page(undefined, {
                        nota: {
                            codigo: resultado.codigo
                        }
                    })
                    .$promise.then(function(data) {
                        $rootScope.loadingProgress = false;
                        $scope.listMotivos = data.content;
                        resolve();
                    }).catch(catchError);
            });
        };

        function carregarFaturamentoRetencao(codigoFaturamento) {
            return new Promise(function(resolve, reject) {
                $rootScope.loadingProgress = true;
                FaturamentoRetencao.listarPorFaturamento({
                        id: codigoFaturamento
                    }, undefined)
                    .$promise.then(function(dados) {
                        $scope.object.faturamentoEtapaTO.faturamento.faturamentoRetencoes = dados;
                        resolve(true);
                    })
                    .catch(() => {
                        $scope.object.faturamentoEtapaTO.faturamento.faturamentoRetencoes = [];
                    })
                    .catch(catchError);
            });
        }

        let loadObj = () => {
            $scope.object = {};
            $scope.parcelaSelecionada = {};
            $scope.situacaoLoteEnvio = undefined;
            $scope.action = true;
            $rootScope.loadingProgress = true;

            if ($scope.objectId) {
                $scope.object.loteEnvio = {};
                $scope.object.loteCancelamento = {};
                $scope.botaoNumerarDesabilitado = true;

                // FIXME: Refatorar esse código, pois há o mesmo abaixo.
                if ($scope.screenMode === 'VIEW' && $location.search().from === 'contasareceber.exibir') {
                    $scope.object.formaPagamento = 'Parcelada';
                }

                NotaFiscalFaturamento.qbeNF({
                        codigo: $scope.objectId
                    }).$promise
                    .then(function(data) {
                        $scope.botaoNumerarDesabilitado = false;
                        $scope.botaoConfirmarcadastroDesabilitado = false;

                        if (data.tipoFornecimento === 'Só material') {
                        	$scope.selectedItemServico = undefined
                        } else {
                        	$scope.selectedItemServico = data.servicoTipo;
                        }

                        data.dataEmissao = ajustarTimeZoneData(data.dataEmissao);

                        if (!CalculadorFaturamento.isCampoVazio(data.vencimento)) {
                            data.vencimento = new Date(data.vencimento);
                        }

                        $scope.object = data;

                        if (data.faturamentoEtapaTO) {
                            const faturamentoRetencao = () => carregarFaturamentoRetencao(data.faturamentoEtapaTO.faturamento.codigo);

                            const listaNotaTributoAliquota = () => carregarListaNotaTributoAliquota(data.codigo, data.status);

                            const cidadeUnidade = () => Contrato.cidadeUnidade(undefined, data.faturamentoEtapaTO.faturamento.centroFinanceiro.contratoTO).$promise;

                            const ajusteModoNew = () => {
                                if ($scope.screenMode === 'NEW') {
                                    $scope.issRetidoCliente = data.faturamentoEtapaTO.faturamento.issRetidoCliente;
                                }
                                data.faturamentoEtapaTO.valorLiquido = 0;
                            };

                            const carregamentoMotivosRetornoNF = () => {
                                $scope.object = angular.copy(data);
                                return $scope.carregamentoCompletoNotaFiscal($scope.object);
                            };

                            const carregamentoMotivosRetornoResult = () => {
                                $scope.object.empresa = angular.copy(data.empresa);
                                $scope.object.dataEmissao = ajustarTimeZoneData(data.dataEmissao);
                                if (!CalculadorFaturamento.isCampoVazio($scope.object.vencimento)) {
                                    $scope.object.vencimento = new Date(data.vencimento);
                                }
                            };

                            const ajusteLoteNf = (data2) => {
                                $scope.contratoCidade = data2.nome + "/" + data2.uf;

                                angular.forEach(data.lotes, function(loteNota) {
                                    if (loteNota.tipo === "Enviada") {
                                        $scope.object.loteEnvio = loteNota.loteInterface;
                                        $scope.object.loteEnvio.numero = "LT/" + loteNota.loteInterface.codigo;
                                        $scope.object.numeroLote = $scope.object.loteEnvio ? $scope.object.loteEnvio.numero : '';
                                        gerenciarSituacaoLoteEnvio($scope.object.loteEnvio);
                                    } else if (loteNota.tipo === "Cancelada") {
                                        $scope.object.loteCancelamento = loteNota.loteInterface;
                                        $scope.object.loteCancelamento.numero = "LT/" + loteNota.loteInterface.codigo;
                                        $scope.object.numeroLote = $scope.object.loteCancelamento.numero;
                                    }
                                });

                                adicionarSiglaNome(data);

                                $scope.totalValorParcelas();
                                $scope.totalValorPermuta();
                                $scope.totalValorRetencoes();
                                $scope.totalValorDescontos();
                                $scope.totalValorMultas();
                                $scope.totalValorTributos();
                                $scope.totalValorJuros();
                                $scope.totalValorLiquido();


                                $scope.todasParcelasRecebidas = !data.notaSaidaParcelas.some(elem => elem.situacao === 'Finalizada (recebida)');

                                if ($scope.screenMode === 'VIEW' && ($rootScope.$lastState.name === 'contasareceber.exibir' || $location.search().from === 'contasareceber.exibir')) {
                                    // $scope.editarDadosPrincipais();
                                    $scope.vindoDoContasReceber = true;
                                    $scope.index = 5;
                                    $scope.object.formaPagamento = 'Parcelada';
                                    $scope.editarParcelas();
                                } else if ($location.search().acao === 'parcelar') {
                                    $scope.index = 5;
                                }

                                $scope.object.notaSaidaParcelas.forEach((parcela) => {
                                    parcela.dataPagamento = moment(parcela.dataPagamento).toDate();
                                    parcela.dataVencimento = moment(parcela.dataVencimento).toDate();
                                });

                                $scope.cancelarParcela();
                            };

                            carregarFaturamentoAliquotas(data.faturamentoEtapaTO.faturamento.codigo)
                                .then(faturamentoRetencao)
                                .then(listaNotaTributoAliquota)
                                .then(cidadeUnidade)
                                .then(ajusteLoteNf)
                                .then(ajusteModoNew)
                                .then(carregamentoMotivosRetorno(data))
                                .then(carregamentoMotivosRetornoNF)
                                .then(carregamentoMotivosRetornoResult)
                                .then(() => {
                                    $rootScope.loadingProgress = false;
                                    $scope.action = false;
                                    $scope.$apply();
                                })
                                .catch(catchError);
                        }
                    }).catch(function(response) {
                        console.error(response);
                        if (response && response.data && response.data.localizedMessage) {
                            if (response.data.localizedMessage === 'Não há etapa para essa nota fiscal') {
                                $scope.retornarTelaAnterior();
                            }
                        }
                        $scope.botaoNumerarDesabilitado = false;
                        $scope.botaoConfirmarcadastroDesabilitado = false;
                        $rootScope.loadingProgress = false;
                        $scope.$emit('crud-critical', 'load', response);
                    });
            }
        };

        function gerenciarSituacaoLoteEnvio(loteEnvio) {
            $scope.situacaoLoteEnvio = undefined;

            if (loteEnvio && loteEnvio.situacao) {
                if (loteEnvio.situacao === "Importado na contabilidade") {
                    $scope.situacaoLoteEnvio = "Importado na Prefeitura";
                } else {
                    $scope.situacaoLoteEnvio = loteEnvio.situacao;
                }
            }

        }

        //fim - carregamento da tela no screenMode VIEW


        //inicio - carregamento da tela no screenMode NEW
        $scope.carregamentoBasicoNotaFiscal = function(resultado) {
            return $q(function(resolve, reject) {

                if (!CalculadorFaturamento.isCampoVazio(resultado)) {
                    $scope.parcelaSelecionada = {};

                    $scope.object.status = resultado.status;

                    $scope.object.numero = resultado.numero;

                    $scope.object.empresa = resultado.empresa.matriz;

                    $scope.object.criador = resultado.criador;
                    $scope.object.dataCriacao = resultado.dataCriacao;

                    $scope.object.atualizador = resultado.atualizador;
                    $scope.object.dataAtualizacao = resultado.dataAtualizacao;

                    $scope.object.responsavelNumeracao = resultado.responsavelNumeracao;
                    $scope.object.dataNumeracao = resultado.dataNumeracao;

                    $scope.object.responsavelCancelarNumeracao = resultado.responsavelCancelarNumeracao;
                    $scope.object.dataCancelamentoNumeracao = resultado.dataCancelamentoNumeracao;

                    if (resultado.responsavelCadastroPrefeitura && resultado.responsavelCadastroPrefeitura.nome != undefined) {
                        $scope.object.responsavelCadastroPrefeitura = resultado.responsavelCadastroPrefeitura;
                        $scope.object.dataCadastroPrefeitura = resultado.dataCadastroPrefeitura;
                    } else {
                        $scope.object.dataCadastroPrefeitura = undefined;
                        $scope.object.responsavelCadastroPrefeitura = undefined;
                    }

                    $scope.object.responsavelCancelamento = resultado.responsavelCancelamento;
                    $scope.object.dataCancelamento = resultado.dataCancelamento;

                    $scope.object.responsavelReversaoCancelamento = resultado.responsavelReversaoCancelamento;
                    $scope.object.dataReversaoCancelamento = resultado.dataReversaoCancelamento;

                    if (!CalculadorFaturamento.isCampoVazio(resultado.protocoloEntrega)) {
                        $scope.object.protocoloEntrega = resultado.protocoloEntrega;
                    }

                    $scope.object.faturamentoEtapaTO = resultado.faturamentoEtapaTO;

                    if ($scope.object.faturamentoEtapaTO) {
                        $scope.object.faturamentoEtapaTO.numero = CalculadorFaturamento.formataNumero($scope.object.faturamentoEtapaTO.numero, 3);

                        $scope.carregarFaturamentoAliquotas($scope.object.faturamentoEtapaTO.faturamento.codigo).then(function() {

                            if ($scope.screenMode === 'NEW') {
                                $scope.object.infoComplementarContribuinte = $scope.montarCampoDiscriminacaoServico($scope.object.faturamentoEtapaTO);

								if ($scope.object.tipoFornecimento === 'Só material') {
									$scope.selectedItemServico = undefined
								} else {
									$scope.selectedItemServico = $scope.object.faturamentoEtapaTO.faturamento.servicoTipo.sigla + ' - ' + $scope.object.faturamentoEtapaTO.faturamento.servicoTipo.nome;
								}

                                $scope.object.servicoTP = {};
                                $scope.object.servicoTP.codigo = $scope.object.faturamentoEtapaTO.faturamento.servicoTipo.codigo;
                                $scope.object.servicoTP.nome = $scope.object.faturamentoEtapaTO.faturamento.servicoTipo.nome;
                                $scope.object.servicoTP.sigla = $scope.object.faturamentoEtapaTO.faturamento.servicoTipo.sigla;
                                $scope.object.dataEmissao = new Date();
                                $scope.object.issRetidoCliente = $scope.object.faturamentoEtapaTO.faturamento.issRetidoCliente;
                            } else {
                                $scope.object.infoComplementarContribuinte = resultado.infoComplementarContribuinte;
                            }

                            $scope.montarLabelCentroLucro($scope.object.faturamentoEtapaTO.faturamento.centroFinanceiro);

                            $scope.montarLabelAgenciaConta($scope.object.faturamentoEtapaTO.faturamento.contaCorrente);


                            $scope.montarCidadeUF($scope.object.faturamentoEtapaTO.faturamento);

                            $scope.calcularValorRetencoesEtapa($scope.object.faturamentoEtapaTO, $scope.object.faturamentoEtapaTO.faturamento);

                            if (!$scope.object.servicoTipo &&
                                resultado.faturamentoEtapaTO && resultado.faturamentoEtapaTO.faturamento &&
                                resultado.faturamentoEtapaTO.faturamento.servicoTipo) {
                                    $scope.object.servico = $scope.object.servicoTP;
                                    $scope.object.servicoTipo = resultado.faturamentoEtapaTO.faturamento.servicoTipo;
                                    adicionarSiglaNome($scope.object);
                                    $scope.gerenciarServicoTipoDadosPrincipais();
                            }

                            if (!$scope.object.dataEmissao || $scope.screenMode === 'NEW') {
                                $scope.object.dataEmissao = new Date();
                            }

                            resolve({
                                mensagem: 'Executou a função carregamentoBasicoNotaFiscal com sucesso!',
                                executou: true
                            });

                        }).catch(function(response) {
                            $scope.$emit('crud-critical', 'load', response);
                            reject({
                                mensagem: 'Não foi possível executar a função carregamentoBasicoNotaFiscal!',
                                executou: false
                            });
                        });
                    }
                } //fim do if que verificar se a variavel resultado está undefined ou vazia
                else {
                    reject({
                        mensagem: 'Não foi possível executar a função carregamentoBasicoNotaFiscal!',
                        executou: false
                    });
                }
            });
        };

        $scope.changeTitulo = function() {
            if ($scope.screenMode === 'VIEW') {
                $scope.subtitulo = 'Exibição';
            } else if ($scope.screenMode === 'EDIT') {
                $scope.subtitulo = 'Edição';
            }
        };

        $scope.carregamentoParcelas = function(listaNotaTributoAliquota) {
            if ($scope.screenMode === 'VIEW' || $scope.screenMode === 'EDIT') {
                if (!CalculadorFaturamento.isCampoVazio($scope.object) && !CalculadorFaturamento.isCampoVazio($scope.object.notaSaidaParcelas) &&
                    !CalculadorFaturamento.isCampoVazio($scope.object.notaSaidaParcelas.length) && $scope.object.notaSaidaParcelas.length > 0) {
                    for (let i = 0; i < $scope.object.notaSaidaParcelas.length; i++) {
                        let obj = $scope.object.notaSaidaParcelas[i];

                        if (!CalculadorFaturamento.isCampoVazio(obj) && !CalculadorFaturamento.isCampoVazio(obj.valorBruto)) {
                            // FIXME: Já vem do backend o valor do tributo, por que calcular de novo?

                            if (obj.ordem === 1) {
                                //obj.valorTributo = calcularValorTributosComISSRetidoOuNao(listaNotaTributoAliquota);
                                obj.valorTributo = angular.copy(obj.valorTributo);
                                obj.valorTributos = angular.copy(obj.valorTributo);
                                obj.minDate = new Date($scope.object.dataEmissao);
                            } else {
                                obj.valorTributos = obj.valorTributo;
                                if (angular.isDefined($scope.object.notaSaidaParcelas[i - 1])) {
                                    let novaData = $scope.adicionarDiasData($scope.object.notaSaidaParcelas[i - 1].dataVencimento, 1)
                                    obj.minDate = new Date(novaData);
                                }
                            }

                            obj.valorRetencoes = $scope.calcularValorRetencoesParcela($scope.object.faturamentoEtapaTO.faturamento, obj);
                            obj.valorLiquido = calcularValorLiquidoDaParcela(obj);

                            obj.ordem = CalculadorFaturamento.formataNumero(obj.ordem, 3);
                            let descontos = obj.valorTributos + obj.valorDesconto + obj.valorRetencoes + obj.valorPermuta;
                            let proventos = obj.valor + obj.valorMulta + obj.valorJuros;

                        }
                    }
                }
            }
        };

        function calcularValorTributosComISSRetidoOuNao(listaNotaTributoAliquota) {
            let total = 0;

            angular.forEach(listaNotaTributoAliquota, function(obj, key) {
                if (obj.aliquota.categoriaTributo.tributoTO.codigo !== 4 || $scope.object.issRetidoCliente) {
                    total += obj.valor;
                }
            });

            return total;
        }

        function calcularValorLiquidoDaParcela(parcela) {
            let total = 0;

            total += parcela.valorBruto;
            total -= parcela.valorTributos;
            total -= parcela.valorRetencoes;
            total -= parcela.valorPermuta;
            total -= parcela.valorDesconto;
            total += parcela.valorMulta;
            total += parcela.valorJuros;

            return total;
        }

        $scope.imprimir = function() {
            let url = $state.href('notafiscal.imprimir', {
                objectId: $scope.object.codigo,
                tipoimpressao: 'nf'
            });
            window.open(url, '_blank');
        };

        $scope.imprimirRps = function() {
            let url = $state.href('notafiscal.imprimir', {
                objectId: $scope.object.codigo,
                tipoimpressao: 'rps'
            });
            window.open(url, '_blank');
        };

        //====  EDIÇÃO POR ABA - INICIO DAS FUNÇÕES ====//
        $scope.modeDadosPrincipais = false;
        $scope.modeContrato = false;
        $scope.modeInformacoesComplementares = false;
        $scope.modeTributo = false;
        $scope.modeDiscriminacao = false;
        $scope.modeFormaPagamentoParcelada = false;

        /* ABA DADOS PRINCIPAIS */
        $scope.editarDadosPrincipais = function() {
            // USADO O PROCEDIMENTO ABAIXO APENAS PARA SUAVISAR A TRANSIÇÃO PARA O USUÁRIO //
            $rootScope.loadingProgress = true;
            $scope.action = true;
            $scope.screenMode = 'EDIT';
			console.log($scope.object);
            $scope.changeTitulo();

            if ($scope.object && $scope.object.formaPagamento === "Parcelada") {
                $scope.modeDadosPrincipais = false;
                $scope.modeFormaPagamentoParcelada = true;
            } else {
                $scope.modeDadosPrincipais = true;
                $scope.modeFormaPagamentoParcelada = false;
            }

            $scope.original = angular.copy($scope.object);
            // adicionarSiglaNome($scope.object);

            //FIXME: Erro ao preencher o campo data ao editar.
            setTimeout(() => {
                $scope.object.dataEmissao = ajustarTimeZoneData($scope.original.dataEmissao);
                $scope.object.vencimento = ajustarTimeZoneData($scope.original.vencimento);
                $scope.$apply();
            }, 300);

            $scope.buscarEmpresas(false, undefined).then(() => {
                $scope.gerenciarServicoTipoDadosPrincipais();
                $rootScope.loadingProgress = false;
                $scope.action = false;
            });
        };

        $scope.salvarDadosPrincipais = function(form) {
            if (!qtdParcelasValidas()) {
                return;
            }

            $rootScope.loadingProgress = true;
            $scope.action = true;

            let tempDataVencimento = converterData($scope.object.vencimento);
            $scope.object.vencimento = tempDataVencimento;

            let error = false;

            angular.forEach($scope.object.notaSaidaParcelas, function(parcela, index) {
                let convertDouble = parseFloat(parcela.valorLiquido);
                if (convertDouble < 0) {
                    error = true;
                    $scope.notification.error("", "O valor líquido não pode ser menor que R$ 0,00.");
                    $scope.action = false;
                    return;
                }
                let convertDoubleValorBruto = parseFloat(parcela.valorBruto);
                if (convertDoubleValorBruto <= 0) {
                    error = true;
                    $scope.notification.error("", "O valor bruto deve ser maior que R$ 0,00.");
                    $scope.action = false;
                    return;
                }
            });

            if ($scope.object.formaPagamento === "Parcela única" && $scope.object.notaSaidaParcelas.length == 1) {
                $scope.object.notaSaidaParcelas[0].dataVencimento = $scope.object.vencimento;
                $scope.object.notaSaidaParcelas[0].dataPagamento = $scope.object.vencimento;
            }

            let codigoNotaSaida = $scope.object.codigo;

		   NotaFiscalFaturamento.verificarContaContabilCliente({id: $scope.object.id}).$promise.then(function(data){
				console.log(data);
				if (data && data.warnings && data.warnings.length > 0) {
					$scope.notification.atencao('', data.warnings[0]);
				}
			});

            $scope.salvarNotaFiscal().then(function() {
                $rootScope.loadingProgress = false;

                $scope.modeDadosPrincipais = false;
                $scope.modeFormaPagamentoParcelada = false;

                $scope.screenMode = 'VIEW';
                $scope.changeTitulo();

                $scope.action = false;
                $scope.actionProtocolo = false;

                if ($scope.vindoDoContasReceber) {
                    NotaSaidaParcela.listarPorCodNotaSaida({
                        codigoNota: codigoNotaSaida
                    }).$promise.then(function(dataLista) {
                        if (dataLista && dataLista.length > 0) {
                            let nsp = dataLista[0];
                            $state.go("contasareceber.exibir", {
                                objectId: nsp.codigo
                            });
                        }
                    }).catch(function(responseLista) {
                        $scope.$emit('crud-critical', 'load', responseLista);
                    });
                }

            }).catch(function(response) {
                $scope.screenMode = 'EDIT';
                $rootScope.loadingProgress = false;
                $scope.action = false;
                $scope.actionProtocolo = false;
                $scope.notification.error("", response.mensagem);
            });
        };

        $scope.cancelarDadosPrincipais = function(form) {
            if ($scope.vindoDoContasReceber) {
                $scope.retornarParaContasReceber();
            } else {
                $rootScope.loadingProgress = true;
                $scope.modeDadosPrincipais = false;
                $scope.modeFormaPagamentoParcelada = false;
                $scope.object = angular.copy($scope.original);
                $scope.gerenciarServicoTipoDadosPrincipais();
                $scope.screenMode = 'VIEW';
                $scope.changeTitulo();
                form.$setDirty();
                form.$setUntouched();
                form.$setPristine();
                $rootScope.loadingProgress = false;
            }
        };

        /* ABA DESCRIMINAÇÃO */
        $scope.editarDiscriminacaoServicos = function() {
            // USADO O PROCEDIMENTO ABAIXO APENAS PARA SUAVIZAR A TRANSIÇÃO PARA O USUÁRIO //
            $rootScope.loadingProgress = true;
            $scope.original = angular.copy($scope.object);
            //FIXME: Erro ao preencher o campo data ao editar.
            //$scope.object.dataEmissao = ajustarTimeZoneData($scope.original.dataEmissao);
            //$scope.object.vencimento = ajustarTimeZoneData($scope.original.vencimento);
            $scope.changeTitulo();
            $scope.modeDiscriminacao = true;
            $rootScope.loadingProgress = false;
        };

        $scope.salvarDiscriminacao = function(form) {
            $rootScope.loadingProgress = true;
            form.$setDirty();
            form.$setUntouched();
            form.$setPristine();
            $rootScope.loadingProgress = false;
            $scope.salvarNotaFiscal().then(function() {
                $scope.changeTitulo();
            });
        };

        $scope.cancelarDiscriminacao = function(form) {
            $rootScope.loadingProgress = true;
            $scope.object = angular.copy($scope.original);
            $scope.screenMode = 'VIEW';
            $scope.changeTitulo();
            form.$setDirty();
            form.$setUntouched();
            form.$setPristine();
            $scope.modeDiscriminacao = false;
            $rootScope.loadingProgress = false;
        };

        /* ABA PARCELAS */
        $scope.editarParcelas = function() {
            // USADO O PROCEDIMENTO ABAIXO APENAS PARA SUAVIZAR A TRANSIÇÃO PARA O USUÁRIO //
            $rootScope.loadingProgress = true;
            $scope.original = angular.copy($scope.object);
            $scope.screenMode = 'EDIT';
            $scope.changeTitulo();
            $scope.modeParcelas = true;
            $rootScope.loadingProgress = false;

            //FIXME: Erro ao preencher o campo data ao editar.
            const dataEmissao = $scope.object.dataEmissao;
            const dataVencimento = $scope.object.vencimento;
            setTimeout(() => {
                $scope.object.dataEmissao = ajustarTimeZoneData(dataEmissao);
                $scope.object.vencimento = ajustarTimeZoneData(dataVencimento);
                $scope.$apply();
            }, 200);
        };

        $scope.salvarParcelas = function(form) {
            $rootScope.loadingProgress = true;

            let error = false;

            angular.forEach($scope.object.notaSaidaParcelas, function(value, index) {
                let convertDouble = parseFloat(value.valorLiquido);
                if (convertDouble < 0) {
                    error = true;
                    $scope.notification.error("", "O valor líquido não pode ser menor que R$ 0,00.");
                    return;
                }
                let convertDoubleValorBruto = parseFloat(value.valorBruto);
                if (convertDoubleValorBruto <= 0) {
                    error = true;
                    $scope.notification.error("", "O valor bruto deve ser maior que R$ 0,00.");
                    return;
                }
            });

            error = !qtdParcelasValidas();

            if (error) {
                return;
            }

            $scope.salvarNotaFiscal().then(function() {

                form.$setDirty();
                form.$setUntouched();
                form.$setPristine();
                $scope.modeParcelas = false;
                $rootScope.loadingProgress = false;
                $scope.changeTitulo();

                if ($scope.vindoDoContasReceber) {
                    $scope.retornarParaContasReceber();
                }

            }).catch(function(response) {
                $scope.screenMode = 'EDIT';
                $scope.modeParcelas = true;
                $rootScope.loadingProgress = false;
                $scope.notification.error("", response.mensagem);
            });
        };

        $scope.cancelarParcelas = function(form) {
            $rootScope.loadingProgress = true;
            $scope.object = angular.copy($scope.original);
            $scope.screenMode = 'VIEW';
            $scope.changeTitulo();
            form.$setDirty();
            form.$setUntouched();
            form.$setPristine();
            $scope.modeParcelas = false;
            $rootScope.loadingProgress = false;
            if ($scope.vindoDoContasReceber) {
                $scope.retornarTelaAnterior();
            }
        };

        function converterData(dataInicial) {
            return moment(dataInicial).hour(12).toDate();
        }

        /**
         * Função para corrigir o time zone das datas.
         * Quando uma variavel, por exemplo: "2018-11-01T02:00:00.000+0000", é convertido para data, automaticamente o javascript altera as horas de acordo com o
         * fuzo horário local (do Brasil é GMT-0300 (-03) ).
         * Essa função adiciona os minutos para a data não sofrer alteração.
         */
        let ajustarTimeZoneData = (dataInicial) => {
            if (!dataInicial) {
                return;
            }
            let novaData;
            if (dataInicial instanceof Date) {
                novaData = angular.copy(dataInicial);
            } else {
                let localT = dataInicial ? dataInicial.search("T") : -1;
                if (localT > 0) {
                    let apenasData = new Date(dataInicial);
                    novaData = new Date(apenasData.getFullYear(), apenasData.getMonth(), apenasData.getDate());
                } else {
                    novaData = new Date(dataInicial);

                }
            }

            let m = moment(novaData); // 2019-03-22T19:13:31.812+0000
            let resultadoFinal = m.add(novaData.getTimezoneOffset(), 'minutes').format("YYYY-MM-DDT12:00:00.000");

            return resultadoFinal;
        };

        const qtdParcelasValidas = () => {
            let resultado = true;

            if ($scope.object && $scope.object.formaPagamento === "Parcelada" && $scope.object.notaSaidaParcelas.length === 1) {
                if ($location.search().from === 'contasareceber.exibir') {
                    $scope.object.formaPagamento = "Parcela única";
                    $scope.object.vencimento = $scope.object.notaSaidaParcelas[0].dataVencimento;
                } else {
                    $scope.notification.error("", "Esta NF é parcelada. Para apenas uma única parcela, modifique a NF na aba \"DADOS PRINCIPAIS\"");
                    resultado = false;
                }
            }

            return resultado;
        };

        //==== EDIÇÃO POR ABA - FIM DAS FUNÇÕES ====//


        //inicio - aba dados principais

        /**
         * Pesquisa do Código do Serviço
         */
        $scope.querySearchServico = function(query) {
            $rootScope.loadingProgress = true;
            let obj = {};
            obj.nome = query;
            obj.situacao = 'Ativo';

            let servicos = [];
            let resultado = NotaFiscalFaturamento.qbeST(undefined, obj).$promise;

            return resultado.then(function(data) {
                $rootScope.loadingProgress = false;
                return data;
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'search', response);
                servicos = [];
                return servicos;
            });
        };

        $scope.selectedItemChange = (item, tipo) => {
            if (tipo === 'servico') {
                if (item) {
                    if ($scope.object.notaSaida === undefined) {
                        $scope.object.notaSaida = {};
                    }
                    $scope.object.notaSaida.servicoTipoCodigo = item.codigo;
                    $scope.object.servicoTipo                 = item;
                    $scope.selectedItemServico                = item;
                } else {
                    if (document.querySelectorAll('input[type=search]')[1]) {
                        document.querySelectorAll('input[type=search]')[1].value = '';
                    }
                    $scope.resultsServico = [];
                    delete $scope.object.centroFinanceiro;
                    delete $scope.selectedItemServico;
                    delete $scope.object.servicoTipo;
                }
            }
        };

        $scope.gerenciarServicoTipoDadosPrincipais = function() {
        	if ($scope.object.tipoFornecimento === 'Só material') {
        		$scope.selectedItemServico = undefined
        	} else {
        		$scope.selectedItemServico = $scope.object.servicoTipo;
        	}

        };

        $scope.carregarComboFormaPagamento = function() {
            $scope.formaPagamentoList = EnumService.enumValues({
                pacote: 'br.com.araujoabreu.sgi.compras.domain',
                classe: 'Nota',
                enum: 'FormaPagamento'
            });
        };

        $scope.carregarComboNatureza = function() {
            $scope.naturezaList = EnumService.enumValues({
                pacote: 'br.com.araujoabreu.sgi.compras.domain',
                classe: 'NotaFiscal',
                enum: 'NaturezaNotaFiscal'
            });
        };

        function parcelamentoNotaFiscalConfirm(qtdParcelas, dataPrimeiroVencimento, fn_hide) {
            let totalParcelas = Number(0);

            for (let x = 0; x < qtdParcelas; x++) {
                let valorPcl = Number(parseFloat($scope.object.faturamentoEtapaTO.valor / qtdParcelas).toFixed(2));
                totalParcelas = totalParcelas + valorPcl;
                criarParcela(dataPrimeiroVencimento, valorPcl);
                dataPrimeiroVencimento = moment(dataPrimeiroVencimento).add(1, 'months').toDate();
            }

            let diferencaValor = Number($scope.object.faturamentoEtapaTO.valor) - Number(totalParcelas);
            let valorAtualizadoPrimeiraParcela = Number($scope.object.notaSaidaParcelas[0].valorBruto) + diferencaValor;

            if (valorAtualizadoPrimeiraParcela > 0) {
                $scope.object.notaSaidaParcelas[$scope.object.notaSaidaParcelas.length - 1].valorBruto = valorAtualizadoPrimeiraParcela;
                $scope.object.notaSaidaParcelas[$scope.object.notaSaidaParcelas.length - 1].valorLiquido = valorAtualizadoPrimeiraParcela;
            } else {
                $scope.object.notaSaidaParcelas[0].valorBruto = valorAtualizadoPrimeiraParcela;
                $scope.object.notaSaidaParcelas[0].valorLiquido = valorAtualizadoPrimeiraParcela - $scope.object.notaSaidaParcelas[0].valorTributo;
            }

            $scope.labelVcto = "Vencimento";
            $scope.modeDadosPrincipais = false;
            $scope.modeFormaPagamentoParcelada = true;
            $scope.totalValorParcelas(undefined);
            $scope.totalValorLiquido(false);
            $scope.totalValorDevolucao();
            $scope.totalValorTributos();
            delete $scope.object.vencimento;
            fn_hide();
        }

        function parcelamentoNotaFiscalCancelar(fn_hide) {
            let vencimentoParcela = undefined;

            if (!CalculadorFaturamento.isCampoVazio($scope.object.vencimento)) {
                vencimentoParcela = new Date($scope.object.vencimento);
                delete $scope.object.vencimento;
            }

            $scope.labelVcto = "Vencimento";
            $scope.modeDadosPrincipais = false;
            $scope.modeFormaPagamentoParcelada = true;

            fn_hide();
        }

        function DialogControllerParcelamento($scope, $mdDialog, notaFiscal, fn, fn_cancelar) {
            function init() {
                $scope.object = notaFiscal;
                $scope.qtdParcelas;
                if (notaFiscal.vencimento !== undefined) {
                    $scope.dataPrimeiroVencimento = new Date(notaFiscal.vencimento);
                }
            }

            $scope.cancel = function() {
                fn_cancelar($scope.hide);
            };

            $scope.hide = function() {
                $mdDialog.hide();
            };

            $scope.confirmar = function() {
                $scope.object.notaSaidaParcelas = [];
                fn($scope.qtdParcelas, $scope.dataPrimeiroVencimento, $scope.hide);
            };

            init();
        }

        $scope.$on('changeFormaPagamento', function() {
            $scope.labelVcto = "Vencimento";
            if ($scope.object.formaPagamento === 'Parcelada') {
                $mdDialog.show({
                    controller: DialogControllerParcelamento,
                    locals: {
                        notaFiscal: $scope.object,
                        fn: parcelamentoNotaFiscalConfirm,
                        fn_cancelar: parcelamentoNotaFiscalCancelar
                    },
                    templateUrl: 'partials/faturamento/notasFiscais/html/partials/dialog_parcelamento.html',
                    parent: angular.element(document.body),
                    targetEvent: undefined,
                    clickOutsideToClose: false,
                    fullscreen: false
                });
            } else {
                if ($scope.object.notaSaidaParcelas.length != 1) {
                    $scope.object.notaSaidaParcelas = [];
                    criarParcela(undefined, $scope.object.faturamentoEtapaTO.valor);
                }

                $scope.labelVcto = "Vencimento *";
                $scope.modeDadosPrincipais = true;
                $scope.modeFormaPagamentoParcelada = false;
            }
        });

        $scope.$watch('parcelaSelecionada.valorBruto', function() {
            $scope.parcelaSelecionada.valorLiquido = $scope.calcularValorLiquidoParcela($scope.parcelaSelecionada, $scope.object);
            $scope.totalValorLiquido();
            $scope.totalValorParcelas();
        });

        $scope.$watch('parcelaSelecionada.valorTributo', function() {
            $scope.parcelaSelecionada.valorLiquido = $scope.calcularValorLiquidoParcela($scope.parcelaSelecionada, $scope.object);
            $scope.totalValorTributos();
            $scope.totalValorParcelas();
            $scope.totalValorLiquido();
        });

        $scope.$watch('parcelaSelecionada.valorPermuta', function() {
            $scope.parcelaSelecionada.valorLiquido = $scope.calcularValorLiquidoParcela($scope.parcelaSelecionada, $scope.object);
            $scope.totalValorPermuta();
            $scope.totalValorLiquido();
        });

        $scope.$watch('parcelaSelecionada.valorDesconto', function() {
            $scope.parcelaSelecionada.valorLiquido = $scope.calcularValorLiquidoParcela($scope.parcelaSelecionada, $scope.object);
            $scope.totalValorDescontos();
            $scope.totalValorLiquido();
        });

        $scope.$watch('parcelaSelecionada.valorMulta', function() {
            $scope.parcelaSelecionada.valorLiquido = $scope.calcularValorLiquidoParcela($scope.parcelaSelecionada, $scope.object);
            $scope.totalValorMultas();
            $scope.totalValorLiquido();
        });

        $scope.$watch('parcelaSelecionada.valorJuros', function() {
            $scope.parcelaSelecionada.valorLiquido = $scope.calcularValorLiquidoParcela($scope.parcelaSelecionada, $scope.object);
            $scope.totalValorJuros();
            $scope.totalValorLiquido();
        });


        $scope.calcularValorLiquidoParcela = function(parcelaEdicao, notaFiscal) {
            if (!CalculadorFaturamento.isCampoVazio(parcelaEdicao)) {
                let varValorBruto = parcelaEdicao.valorBruto;
                if (!varValorBruto) {
                    varValorBruto = 0;
                }

                let varTributos = 0;
                if (parcelaEdicao.ordem === '1' || parcelaEdicao.ordem === '001') {
                    varTributos = $scope.calcularValorTributos(notaFiscal);
                }

                let varRetencoes = 0.00;
                if (!CalculadorFaturamento.isCampoVazio(notaFiscal) && !CalculadorFaturamento.isCampoVazio(notaFiscal.faturamentoEtapaTO) &&
                    !CalculadorFaturamento.isCampoVazio(notaFiscal.faturamentoEtapaTO.faturamento)) {
                    parcelaEdicao.valorRetencoes = $scope.calcularValorRetencoesParcela(notaFiscal.faturamentoEtapaTO.faturamento, parcelaEdicao);
                    varRetencoes = parcelaEdicao.valorRetencoes;
                }


                let varPermuta = 0.00;
                if (!CalculadorFaturamento.isCampoVazioNumerico(parcelaEdicao.valorPermuta)) {
                    varPermuta = parcelaEdicao.valorPermuta;
                }

                let varDesconto = 0.00;
                if (!CalculadorFaturamento.isCampoVazioNumerico(parcelaEdicao.valorDesconto)) {
                    varDesconto = parcelaEdicao.valorDesconto;
                }

                let varMulta = 0.00;
                if (!CalculadorFaturamento.isCampoVazioNumerico(parcelaEdicao.valorMulta)) {
                    varMulta = parcelaEdicao.valorMulta;
                }

                let varJuros = 0.00;
                if (!CalculadorFaturamento.isCampoVazioNumerico(parcelaEdicao.valorJuros)) {
                    varJuros = parcelaEdicao.valorJuros;
                }

                let varLiquido = varValorBruto - varTributos - varRetencoes - varPermuta - varDesconto + varMulta + varJuros;

                if (CalculadorFaturamento.isCampoVazioNumerico(varLiquido)) {
                    varLiquido = 0.00;
                }

                return varLiquido.toFixed(2);
            }

            return 0;
        };

        $scope.validarCidadeFaturamento = function(faturamento) {
            if (faturamento.cidadePrestacaoServico == undefined || faturamento.cidadePrestacaoServico.codigo == undefined)
                return true;
            return false;
        };


        $scope.inserirProtocolo = function(ev) {
            $mdDialog.show({
                controller: DialogControllerInserirProtocolo,
                locals: {
                    fnSalvar: $scope.salvarProtocolo,
                    nf: $scope.object
                },
                templateUrl: 'partials/faturamento/notasFiscais/html/partials/dialog_protocolo_nf.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        function DialogControllerInserirProtocolo($scope, $mdDialog, fnSalvar, nf) {

            function init() {
                $scope.copiaProtocoloEntrega = undefined;
                $scope.notaFiscal = nf;
                $scope.copiaProtocoloEntrega = angular.copy($scope.notaFiscal.protocoloEntrega);
            }

            init();

            $scope.cancel = function() {
                $mdDialog.cancel();
            };

            $scope.confirmar = function() {
                fnSalvar($scope.copiaProtocoloEntrega);
                $mdDialog.cancel();
            };

        } // fim dialog

        $scope.salvarProtocolo = function(copiaProtocoloEntrega) {
            $scope.actionProtocolo = true;
            $rootScope.loadingProgress = true;
            $scope.action = true;
            $scope.object.protocoloEntrega = copiaProtocoloEntrega;
            NotaFiscalFaturamento.salvarProtocolo($scope.object).$promise.then(function(data) {
                $scope.object.dataAtualizacao = data.dataAtualizacao;
                $scope.object.atualizador = data.atualizador;
                $scope.notification.success(undefined, `Protocolo da ${$scope.object.numero} atualizado com sucesso.`);
                $rootScope.loadingProgress = false;
                $scope.actionProtocolo = false;
                $scope.action = false;
            }).catch(function(response) {
                $rootScope.loadingProgress = false;
                $scope.actionProtocolo = false;
                $scope.action = false;
                $scope.$emit('crud-critical', 'search', response);
                console.error(response);
            });
        };


        //fim - aba dados principais



        //inicio - aba faturamento

        $scope.montarLabelCentroLucro = function(centroFinanceiro) {
            if (!CalculadorFaturamento.isCampoVazio(centroFinanceiro) && !CalculadorFaturamento.isCampoVazio(centroFinanceiro.identificadorFinal) && !CalculadorFaturamento.isCampoVazio(
                    centroFinanceiro.nome)) {
                $scope.object.centroLucro = centroFinanceiro.identificadorFinal + " - " + centroFinanceiro.nome
            }
        };

        $scope.montarLabelAgenciaConta = function(contaCorrente) {
            $scope.object.agenciaConta = CalculadorFaturamento.montarCampoAgenciaConta(contaCorrente);
        };

        $scope.montarCidadeUF = function(faturamento) {
            if (!CalculadorFaturamento.isCampoVazio(faturamento.cidadePrestacaoServico) &&
                !CalculadorFaturamento.isCampoVazio(faturamento.cidadePrestacaoServico.nome) && !CalculadorFaturamento.isCampoVazio(faturamento.cidadePrestacaoServico.uf)) {
                $scope.object.cidadeUF = faturamento.cidadePrestacaoServico.nome + "/" + faturamento.cidadePrestacaoServico.uf;
            }
        };

        $scope.montarIssRetidoClientePorFaturamento = function(faturamento) {
            $scope.montarIssRetidoCliente(faturamento.issRetidoCliente);
        };

        $scope.montarCampoDiscriminacaoServico = function(etapa) {
            let conteudo = "";

            if (!CalculadorFaturamento.isCampoVazio(etapa) && !CalculadorFaturamento.isCampoVazio(etapa.discriminacaoInicial)) {
                conteudo = etapa.discriminacaoInicial;
            }

            if (!CalculadorFaturamento.isCampoVazio(etapa) && !CalculadorFaturamento.isCampoVazio(etapa.discriminacaoCentral)) {
                conteudo = conteudo + " \n" + etapa.discriminacaoCentral;
            }

            if (!CalculadorFaturamento.isCampoVazio(etapa) && !CalculadorFaturamento.isCampoVazio(etapa.discriminacaoFinal)) {
                conteudo = conteudo + " \n" + etapa.discriminacaoFinal;
            }

            return conteudo;
        };
        //fim - aba faturamento


        //inicio - aba etapas
        // CÓDIGO ABAIXO USADO NO CARREGAMENTO DOS FATURAMENTOS ALIQUOTAS (TRIBUTOS)
        $scope.carregarFaturamentoAliquotas = function(codFaturamento) {
            $rootScope.loadingProgress = true;

            return $q(function(resolve, reject) {

                let obj = {};
                obj.codigoFaturamento = codFaturamento;

                let params = {};

                FaturamentoAliquota.page(params, obj)
                    .$promise.then(function(data) {

                        $scope.object.faturamentoEtapaTO.faturamento.faturamentoAliquotas = [];
                        $scope.object.faturamentoEtapaTO.faturamento.faturamentoAliquotas = data.content;

                        //FIXME: Obtendo a cidade do ISS. Ajustar e colocar em uma função para ser usando em mais de um lugar.
                        angular.forEach(data.content, function(value, key) {
                            CategoriaTributo.get({
                                    id: value.aliquota.codigoCategoriaTributo
                                })
                                .$promise.then(function(data2) {
                                    if (data2.tributoTO.codigo == 4) {
                                        $scope.cidadeIss = data2.cidade.nome + '/' + data2.cidade.uf;
                                        $scope.temNotaCarioca = data2.cidade.interfacePrefeitura;
                                    }
                                }).catch(function(response) {
                                    $rootScope.loadingProgress = false;
                                    console.error(response);
                                    $scope.$emit('crud-critical', 'load', response);
                                });
                        });

                        let percentualTributo = CalculadorFaturamento.percentualTotalTributo($scope.object.faturamentoEtapaTO.faturamento.faturamentoAliquotas, $scope.object
                            .faturamentoEtapaTO.faturamento.issRetidoCliente);
                        CalculadorFaturamento.divideMaterialServico($scope.object.faturamentoEtapaTO.faturamento, 'VIEW');
                        CalculadorFaturamento.divideMaterialServicoEtapa($scope.object.faturamentoEtapaTO.faturamento, $scope.object.faturamentoEtapaTO, percentualTributo);

                        $scope.atualizarValorTributos($scope.object);
                        $scope.atualizarValorLiquido($scope.object);
                        resolve({
                            mensagem: 'Pesquisa dos tributos foi atualizada com sucesso!',
                            executou: true
                        });

                    }).catch(function(response) {
                        $scope.$emit('crud-critical', 'load', response);
                        $scope.object.faturamentoEtapas = [];
                        reject({
                            mensagem: 'Não foi possível realizar a pesquisa de tributos!',
                            executou: false
                        });
                    });

            });
        };
        //fim - aba etapas


        //inicio - aba parcelas

        // gets the template to ng-include for a table row / item
        $scope.getTemplate = function(parcela) {
            if ($scope.parcelaSelecionada && parcela.ordem === $scope.parcelaSelecionada.ordem)
                return 'edit';
            else
                return 'display';
        };

        // $scope.$watch('object.dataEmissao', function() {
        //     if (!CalculadorFaturamento.isCampoVazio($scope.object) && !CalculadorFaturamento.isCampoVazio($scope.object.dataEmissao)) {
        //         if (!CalculadorFaturamento.isCampoVazio($scope.object.notaSaidaParcelas) && !CalculadorFaturamento.isCampoVazio($scope.object.notaSaidaParcelas.length)) {
        //             if ($scope.object.notaSaidaParcelas.length > 0) {
        //                 $scope.object.notaSaidaParcelas[0].minDate = new Date($scope.object.dataEmissao);
        //             }
        //         }

        //         if ($scope.object && $scope.object.faturamentoEtapaTO && $scope.object.faturamentoEtapaTO.faturamento &&
        //             $scope.object.faturamentoEtapaTO.faturamento.tipoCobrancaFaturamento && $scope.object.faturamentoEtapaTO.faturamento.tipoCobrancaFaturamento.codigo &&
        //             $scope.object.faturamentoEtapaTO.faturamento.tipoCobrancaFaturamento.tipoCobrancaVencimentos && $scope.object.faturamentoEtapaTO.faturamento.tipoCobrancaFaturamento.tipoCobrancaVencimentos.length > 0) {
        //             if ($scope.screenMode !== 'VIEW') {
        //                 ajustarDataVencimento();
        //             }
        //         }
        //     }
        // });


        let ajustarDataVencimento = () => {
            let vctoRegra = $scope.object.faturamentoEtapaTO.faturamento.tipoCobrancaFaturamento.tipoCobrancaVencimentos[0];
            let dataAtual = moment($scope.object.dataEmissao);

            if (!$scope.object.dataEmissao) {
                dataAtual = moment(new Date());
            }

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
                $scope.object.vencimento = moment(dataAtual).toDate();
            } else {
                $scope.object.vencimento = undefined;
            }
        };

        function criarParcela(dataVencimento, valorParcela) {
            let novaParcela = {};
            novaParcela.valorBruto = 0.00;
            novaParcela.valorTributo = 0.00;
            novaParcela.valorDesconto = 0.00;
            novaParcela.valorPermuta = 0.00;
            novaParcela.valorMulta = 0.00;
            novaParcela.valorJuros = 0.00;
            novaParcela.valorLiquido = 0.00;
            novaParcela.valorDevolucao = 0.00;

            if ('Em recebimento' === $scope.object.status) {
                novaParcela.situacao = 'Em recebimento';
            } else {
                novaParcela.situacao = 'Aberta (NF não processada)';
            }

            novaParcela.ordem = CalculadorFaturamento.formataNumero($scope.object.notaSaidaParcelas.length + 1, 3);

            novaParcela.valorRetencoes = $scope.calcularValorRetencoesParcela($scope.object.faturamentoEtapaTO.faturamento, novaParcela);

            if (dataVencimento !== undefined || $scope.object.notaSaidaParcelas.length === 0) {
                novaParcela.dataVencimento = new Date(dataVencimento);
                novaParcela.dataPagamento = new Date(dataVencimento);
            } else {
                novaParcela.dataVencimento = moment($scope.object.notaSaidaParcelas[$scope.object.notaSaidaParcelas.length - 1].dataVencimento).add(1, 'months').toDate();
                novaParcela.dataPagamento = moment($scope.object.notaSaidaParcelas[$scope.object.notaSaidaParcelas.length - 1].dataVencimento).add(1, 'months').toDate();
            }

            if (!CalculadorFaturamento.isCampoVazio(valorParcela)) {
                novaParcela.valor = valorParcela;
                novaParcela.valorBruto = valorParcela;
            }

            if ($scope.object.notaSaidaParcelas.length > 0) {
                let novaData = $scope.adicionarDiasData($scope.object.notaSaidaParcelas[$scope.object.notaSaidaParcelas.length - 1].dataVencimento, 1);
                novaParcela.minDate = new Date(novaData);
                novaParcela.valorTributo = 0.00;
                novaParcela.valorLiquido = novaParcela.valorBruto;
            } else {
                if ($scope.object.issRetidoCliente) {
                    novaParcela.valorTributo = $scope.getTotalValorTributos();
                } else {
                    novaParcela.valorTributo = $scope.getSubtotalValorTributos();
                }
                if (!CalculadorFaturamento.isCampoVazio($scope.object.dataEmissao)) {
                    novaParcela.minDate = new Date($scope.object.dataEmissao);
                }
                novaParcela.valorLiquido = novaParcela.valorBruto - novaParcela.valorTributo;
            }

            $scope.object.notaSaidaParcelas.push(novaParcela);
        }

        $scope.adicionarParcela = function(parcela) {
            if (!$scope.desabilitarBotaoAdicionar(parcela)) {
                criarParcela(new Date(), 0);
                $scope.parcelaSelecionada = angular.copy($scope.object.notaSaidaParcelas[$scope.object.notaSaidaParcelas.length - 1]);
            }
        };

        /*Atualizar a data minima da próxima parcela ao remover uma parcela*/
        function atualizaMinDateParcelaAoRemover(indexARemover, valorTributo) {
            return new Promise(function(resolve, reject) {
                let novoMinDate = '';
                let novoTributo = '';
                let atualizar = false;
                let posicaoProximaParcela = 0;

                if (indexARemover === 0) {
                    if (!CalculadorFaturamento.isCampoVazio($scope.object.dataEmissao)) {
                        novoMinDate = new Date($scope.object.dataEmissao);
                    }
                    novoTributo = valorTributo;
                    posicaoProximaParcela = indexARemover + 1;
                    atualizar = true;
                } else if (indexARemover > 0 && indexARemover < $scope.object.notaSaidaParcelas.length - 1) {
                    novoMinDate = $scope.object.notaSaidaParcelas[indexARemover].minDate;
                    posicaoProximaParcela = indexARemover + 1;
                    atualizar = true;
                }

                if (atualizar && posicaoProximaParcela < $scope.object.notaSaidaParcelas.length) {
                    $scope.object.notaSaidaParcelas[indexARemover + 1].minDate = novoMinDate;

                    let contador = indexARemover + 1;

                    for (let i = 0; i < $scope.object.notaSaidaParcelas.length; i++) {
                        if (i == contador) {
                            let parcelaTemp = $scope.object.notaSaidaParcelas[i];
                            parcelaTemp.ordem = CalculadorFaturamento.formataNumero(parcelaTemp.ordem, 3);
                            contador++;
                        }
                    }

                    if (!CalculadorFaturamento.isCampoVazio(novoTributo)) {
                        $scope.object.notaSaidaParcelas[indexARemover + 1].valorTributos = novoTributo;
                    }
                }
                resolve(true);
            });
        }

        /*Atualizar a data minima da próxima parcela ao salvar ou alterar uma parcela*/
        function atualizaMinDateParcela(indexAtual) {
            let novoMinDate;
            let posicaoProximaParcela = 0;

            novoMinDate = $scope.object.notaSaidaParcelas[indexAtual].dataVencimento;
            posicaoProximaParcela = indexAtual + 1;

            if (posicaoProximaParcela < $scope.object.notaSaidaParcelas.length) {
                $scope.object.notaSaidaParcelas[indexAtual + 1].minDate = novoMinDate;
            }
        }

        $scope.isParcelaPreenchida = function(parcela) {
            if (parcela !== undefined &&
                !CalculadorFaturamento.isCampoVazio(parcela.ordem) &&
                !CalculadorFaturamento.isCampoVazio(parcela.dataVencimento)) {
                return true;
            }
            return false;
        };

        $scope.editarParcela = function(parcela) {
            $scope.editarParcelaAction = true;
            $scope.parcelaSelecionada = angular.copy(parcela);
            setTimeout(() => {
                $scope.parcelaSelecionada.dataVencimento = moment(parcela.dataVencimento).toDate();
                $scope.parcelaSelecionada.dataPagamento = moment(parcela.dataPagamento).toDate();
                $scope.$apply();
            }, 200);
            $scope.totalValorTributos();
        };

        $scope.salvarParcela = function(index) {

            atualizaMinDateParcela(index);

            if (!CalculadorFaturamento.isCampoVazio($scope.parcelaSelecionada.situacao) && $scope.parcelaSelecionada.situacao == "Aberta (NF não processada)") {
                $scope.parcelaSelecionada.dataPagamento = $scope.parcelaSelecionada.dataVencimento;
            }

            if ($scope.parcelaSelecionada.valorBruto == undefined) {
                $scope.notification.atencao("", "O valor bruto deve ser maior que R$ 0,00");
                return;
            }

            if ($scope.parcelaSelecionada.valorBruto <= 0) {
                $scope.notification.atencao("", "O valor bruto deve ser maior que R$ 0,00");
                return;
            }

            let parcelaEdicao = angular.copy($scope.parcelaSelecionada);

            if (angular.isUndefined($scope.parcelaSelecionada.valor)) {
                parcelaEdicao.valor = 0;
            }

            if (CalculadorFaturamento.isCampoVazioNumerico(parcelaEdicao.valorPermuta)) {
                parcelaEdicao.valorPermuta = 0;
            }

            if (CalculadorFaturamento.isCampoVazioNumerico(parcelaEdicao.valorDesconto)) {
                parcelaEdicao.valorDesconto = 0;
            }

            if (CalculadorFaturamento.isCampoVazioNumerico(parcelaEdicao.valorDevolucao)) {
                parcelaEdicao.valorDevolucao = 0;
            }

            if (CalculadorFaturamento.isCampoVazioNumerico(parcelaEdicao.valorMulta)) {
                parcelaEdicao.valorMulta = 0;
            }

            if (CalculadorFaturamento.isCampoVazioNumerico(parcelaEdicao.valorJuros)) {
                parcelaEdicao.valorJuros = 0;
            }

            if (CalculadorFaturamento.isCampoVazio(parcelaEdicao.dataVencimento)) {
                parcelaEdicao.dataVencimento = $scope.parcelaSelecionada.dataVencimento;
            }

            if (CalculadorFaturamento.isCampoVazio(parcelaEdicao.dataPagamento)) {
                parcelaEdicao.dataPagamento = $scope.parcelaSelecionada.dataPagamento;
            }

            if (CalculadorFaturamento.isCampoVazio(parcelaEdicao.valorRetencaoContratual)) {
                parcelaEdicao.valorRetencaoContratual = 0;
            }

            let descontos = parcelaEdicao.valorTributo - parcelaEdicao.valorRetencaoContratual - parcelaEdicao.valorPermuta - parcelaEdicao.valorDesconto;
            let devolucao = parcelaEdicao.valorDevolucao + parcelaEdicao.valorJuros;
            let valorLiquido = parcelaEdicao.valorBruto - descontos + devolucao;

            parcelaEdicao.valorLiquido = valorLiquido;

            if (parcelaEdicao.valorLiquido <= 0) {
                const error = true;
                $scope.notification.atencao("", "O valor líquido não pode ser menor que R$ 0,00.");
                return;
            }

            // parcelaEdicao
            Feriado.getDataPagamentoDataAgendamento({
                dataVencimento: parcelaEdicao.dataVencimento.toLocaleDateString()
            }).$promise.then(function(datasParcela) {
                parcelaEdicao.dataPagamento = datasParcela.dataPagamento;
            }).catch(function(responseLista) {
                $scope.$emit('crud-critical', 'load', responseLista);
            });

            if (index == 0) {
                let dateEmissao = new Date($scope.object.dataEmissao);
                dateEmissao = new Date(dateEmissao.getFullYear(), dateEmissao.getMonth(), dateEmissao.getDate(), 0, 0, 0);
                let dateVencimento = new Date(parcelaEdicao.dataVencimento);

                if (dateEmissao <= dateVencimento) {
                    //                $scope.object.notaSaidaParcelas[index] = angular.copy($scope.parcelaSelecionada);
                    $scope.object.notaSaidaParcelas[index] = parcelaEdicao;
                    $scope.cancelarParcela();
                } else {
                    $scope.notification.atencao(undefined, "O vencimento da primeira parcela deve ser maior ou igual a data de emissão.");
                }
            } else if (index > 0 && index < $scope.object.notaSaidaParcelas.length) {
                let parcelaAnterior = $scope.object.notaSaidaParcelas[index - 1];

                let parcelaAnteriorDateVencimento = new Date(parcelaAnterior.dataVencimento);

                parcelaAnteriorDateVencimento = new Date(parcelaAnteriorDateVencimento);

                let parcelaEdicaoDateVencimento = new Date(parcelaEdicao.dataVencimento);

                if (parcelaEdicao.dataVencimento === undefined || parcelaEdicao.dataVencimento === '') {
                    parcelaEdicaoDateVencimento = new Date(moment(parcelaAnteriorDateVencimento).add(1, 'months').toDate());
                }

                if (parcelaAnteriorDateVencimento < parcelaEdicaoDateVencimento) {
                    parcelaEdicao.dataVencimento = parcelaEdicaoDateVencimento;
                    parcelaEdicao.dataPagamento = parcelaEdicaoDateVencimento;
                    $scope.object.notaSaidaParcelas[index] = parcelaEdicao;
                    $scope.cancelarParcela();
                } else {
                    let dataAnterior = new Date(parcelaAnterior.dataVencimento);
                    let dataFormatada = $filter('date')(dataAnterior, 'dd/MM/yyyy');
                    $scope.notification.atencao(undefined, "O vencimento da parcela " + $scope.parcelaSelecionada.ordem + " tem que ser posterior a " + dataFormatada + ".");
                }
            }

            $scope.editarParcelaAction = false;
        };

        $scope.cancelarParcela = function() {
            $scope.parcelaSelecionada = {};
            $scope.totalValorParcelas();
            $scope.totalValorPermuta();
            $scope.totalValorRetencoes();
            $scope.totalValorDescontos();
            $scope.totalValorMultas();
            $scope.totalValorTributos();
            $scope.totalValorJuros();
            $scope.totalValorLiquido();
        };

        $scope.cancelarRemoverParcela = function(index) {
            let parcela = $scope.object.notaSaidaParcelas[index];

            if (!$scope.isParcelaPreenchida(parcela)) {
                atualizaMinDateParcelaAoRemover(index, $scope.object.faturamentoEtapaTO.valorTributos).then(value => {
                    $scope.object.notaSaidaParcelas.splice(index, 1);
                    if ($scope.object.notaSaidaParcelas.length === 0) {
                        criarParcela(new Date(), 0);
                    }
                });
            } else {
                $scope.parcelaSelecionada = {};
            }

            $scope.totalValorParcelas();
            $scope.totalValorPermuta();
            $scope.totalValorRetencoes();
            $scope.totalValorDescontos();
            $scope.totalValorMultas();
            $scope.totalValorTributos();
            $scope.totalValorJuros();
            $scope.totalValorLiquido();
        };

        $scope.desabilitarBotaoAdicionar = function(parcela) {
            if (!CalculadorFaturamento.isCampoVazio(parcela.ordem) &&
                !CalculadorFaturamento.isCampoVazio(parcela.dataVencimento) &&
                !CalculadorFaturamento.isCampoVazio(parcela.valorBruto)) {
                return false;
            }
            return true;
        };

        $scope.totalValorGeralParcelas = function() {
            let total = 0;
            let valorParcelas = 0;
            let valorNotaFiscal = 0;

            if ($scope.object.faturamentoEtapaTO && !CalculadorFaturamento.isCampoVazio($scope.object.faturamentoEtapaTO.valor)) {
                valorNotaFiscal = $scope.object.faturamentoEtapaTO.valor;
                angular.forEach($scope.object.notaSaidaParcelas, function(obj, key) {

                    if (!CalculadorFaturamento.isCampoVazio(obj) && !CalculadorFaturamento.isCampoVazio(obj.valorBruto)) {
                        valorParcelas += Number(obj.valorBruto);
                    }
                });
            }

            total = Number(Number((valorNotaFiscal).toFixed(2)) - Number((valorParcelas).toFixed(2)));

            if (total > 0) {
                $scope.myColor = 'blue';
                return "(" + $filter('currency')(total, '', 2) + ")";
            } else if (total == 0) {
                $scope.myColor = '';
                return '';
            } else {
                $scope.myColor = 'red';
                return "(" + $filter('currency')(-(total), '', 2) + ")";
            }
        };

        $scope.totalValorParcelas = function() {
            let total = 0;
            $scope.totalParcelas = 0;
            angular.forEach($scope.object.notaSaidaParcelas, function(obj, key) {
                if (!CalculadorFaturamento.isCampoVazio(obj) &&
                    !CalculadorFaturamento.isCampoVazio(obj.valorBruto) &&
                    $scope.parcelaSelecionada.ordem !== obj.ordem) {
                    total += Number(obj.valorBruto);
                }
            });

            if (!$scope.parcelaSelecionada.valorBruto) {
                $scope.parcelaSelecionada.valorBruto = 0;
            }

            if ($scope.parcelaSelecionada.ordem) {
                total += $scope.parcelaSelecionada.valorBruto;
            }

            $scope.totalParcelas = Number(total);
        };

        $scope.totalValorTributos = function() {
            let total = 0;
            $scope.parcelasValorTributos = 0;

            angular.forEach($scope.object.notaSaidaParcelas, function(obj, key) {
                if (!CalculadorFaturamento.isCampoVazio(obj) &&
                    !CalculadorFaturamento.isCampoVazio(obj.valorTributo) &&
                    $scope.parcelaSelecionada.ordem !== obj.ordem) {
                    total += Number(obj.valorTributo);
                }
            });

            if (!$scope.parcelaSelecionada.valorTributo) {
                $scope.parcelaSelecionada.valorTributo = 0;
            }

            if ($scope.parcelaSelecionada.ordem) {
                total += $scope.parcelaSelecionada.valorTributo;
            }

            $scope.parcelasValorTributos = Number(total);
        };

        $scope.totalValorRetencoes = function() {
            let total = 0;
            $scope.valorRetencoes = 0;

            angular.forEach($scope.object.notaSaidaParcelas, function(obj, key) {
                if (!CalculadorFaturamento.isCampoVazio(obj) &&
                    !CalculadorFaturamento.isCampoVazio(obj.valorRetencoes) &&
                    $scope.parcelaSelecionada.ordem !== obj.ordem) {
                    total += Number(obj.valorRetencoes);
                }
            });

            if (!$scope.parcelaSelecionada.valorRetencoes) {
                $scope.parcelaSelecionada.valorRetencoes = 0;
            }

            if ($scope.parcelaSelecionada.ordem) {
                total += $scope.parcelaSelecionada.valorRetencoes;
            }

            $scope.valorRetencoes = Number(total);
        };

        $scope.totalValorPermuta = function() {
            let total = 0;
            $scope.permuta = 0;

            angular.forEach($scope.object.notaSaidaParcelas, function(obj, key) {
                if (!CalculadorFaturamento.isCampoVazio(obj) &&
                    !CalculadorFaturamento.isCampoVazio(obj.valorPermuta) &&
                    $scope.parcelaSelecionada.ordem !== obj.ordem) {
                    total += Number(obj.valorPermuta);
                }
            });

            if (!$scope.parcelaSelecionada.valorPermuta) {
                $scope.parcelaSelecionada.valorPermuta = 0;
            }

            if ($scope.parcelaSelecionada.ordem) {
                total += $scope.parcelaSelecionada.valorPermuta;
            }
            $scope.permuta = Number(total);
        };


        $scope.totalValorDescontos = function() {
            let total = 0;
            $scope.valorDesconto = 0;

            angular.forEach($scope.object.notaSaidaParcelas, function(obj, key) {
                if (!CalculadorFaturamento.isCampoVazio(obj) &&
                    !CalculadorFaturamento.isCampoVazio(obj.valorDesconto) &&
                    $scope.parcelaSelecionada.ordem !== obj.ordem) {
                    total += Number(obj.valorDesconto);
                }
            });

            if (!$scope.parcelaSelecionada.valorDesconto) {
                $scope.parcelaSelecionada.valorDesconto = 0;
            }

            if ($scope.parcelaSelecionada.ordem) {
                total += $scope.parcelaSelecionada.valorDesconto;
            }

            $scope.valorDesconto = Number(total);
        };

        $scope.totalValorMultas = function() {
            let total = 0;
            $scope.multa = 0;

            angular.forEach($scope.object.notaSaidaParcelas, function(obj, key) {
                if (!CalculadorFaturamento.isCampoVazio(obj) &&
                    !CalculadorFaturamento.isCampoVazio(obj.valorMulta) &&
                    $scope.parcelaSelecionada.ordem !== obj.ordem) {
                    total += Number(obj.valorMulta);
                }
            });

            if (!$scope.parcelaSelecionada.valorMulta) {
                $scope.parcelaSelecionada.valorMulta = 0;
            }

            if ($scope.parcelaSelecionada.ordem) {
                total += $scope.parcelaSelecionada.valorMulta;
            }

            $scope.multa = Number(total);
        };

        $scope.totalValorJuros = function() {
            let total = 0;
            $scope.juros = 0;

            angular.forEach($scope.object.notaSaidaParcelas, function(obj, key) {
                if (!CalculadorFaturamento.isCampoVazio(obj) &&
                    !CalculadorFaturamento.isCampoVazio(obj.valorJuros) &&
                    $scope.parcelaSelecionada.ordem !== obj.ordem) {
                    total += Number(obj.valorJuros);
                }
            });

            if (!$scope.parcelaSelecionada.valorJuros) {
                $scope.parcelaSelecionada.valorJuros = 0;
            }

            if ($scope.parcelaSelecionada.ordem) {
                total += $scope.parcelaSelecionada.valorJuros;
            }

            $scope.juros = Number(total);
        };

        $scope.totalValorDevolucao = function() {
            let total = 0;
            $scope.valorDevolucao = 0;

            angular.forEach($scope.object.notaSaidaParcelas, function(obj, key) {
                if (!CalculadorFaturamento.isCampoVazio(obj) &&
                    !CalculadorFaturamento.isCampoVazio(obj.valorDevolucao) &&
                    $scope.parcelaSelecionada.ordem !== obj.ordem) {
                    total += Number(obj.valorDevolucao);
                }
            });

            if (!$scope.parcelaSelecionada.valorDevolucao) {
                $scope.parcelaSelecionada.valorDevolucao = 0;
            }

            if ($scope.parcelaSelecionada.ordem) {
                total += $scope.parcelaSelecionada.valorDevolucao;
            }

            $scope.valorDevolucao = Number(total);
        };

        $scope.totalValorDesconto = function() {
            let total = 0;
            $scope.valorDesconto = 0;

            angular.forEach($scope.object.notaSaidaParcelas, function(obj, key) {
                if (!CalculadorFaturamento.isCampoVazio(obj) &&
                    !CalculadorFaturamento.isCampoVazio(obj.valorJuros) &&
                    $scope.parcelaSelecionada.ordem !== obj.ordem) {
                    total += Number(obj.valorDesconto);
                }
            });

            if (!$scope.parcelaSelecionada.valorDesconto) {
                $scope.parcelaSelecionada.valorDesconto = 0;
            }

            if ($scope.parcelaSelecionada.ordem) {
                total += $scope.parcelaSelecionada.valorDesconto;
            }

            $scope.valorDesconto = Number(total);
        };


        $scope.totalValorLiquido = function() {
            let total = 0;
            $scope.valorLiquido = 0;

            angular.forEach($scope.object.notaSaidaParcelas, function(obj, key) {
                if (!CalculadorFaturamento.isCampoVazio(obj) &&
                    !CalculadorFaturamento.isCampoVazio(obj.valorLiquido) &&
                    $scope.parcelaSelecionada.ordem !== obj.ordem) {
                    if (obj.valorLiquido) {
                        total += Number(obj.valorLiquido);
                    }
                }
            });
            if (!$scope.parcelaSelecionada.valorLiquido) {
                $scope.parcelaSelecionada.valorLiquido = 0;
            }

            if ($scope.parcelaSelecionada.ordem) {
                total += Number($scope.parcelaSelecionada.valorLiquido);
            }

            $scope.valorLiquido = Number(total);
        };

        //==== DIALOG REMOVER PARCELA ====//
        function fnConfirmRemoverParcela(fnHider, index) {
            atualizaMinDateParcelaAoRemover(index, $scope.valorTributos)
                .then(value => {
                    $scope.object.notaSaidaParcelas.splice(index, 1);

                    if ($scope.object.notaSaidaParcelas.length === 0) {
                        criarParcela(new Date(), 0);
                    }

                    $scope.totalValorParcelas();
                    $scope.totalValorPermuta();
                    $scope.totalValorRetencoes();
                    $scope.totalValorDescontos();
                    $scope.totalValorMultas();
                    $scope.totalValorTributos();
                    $scope.totalValorJuros();
                    $scope.totalValorLiquido();

                    fnHider();
                });
        };

        function DialogControllerRemoverParcela($scope, $mdDialog, index, fn) {
            $scope.index = index;

            $scope.confirm = function() {
                fn($mdDialog.hide, index);
            };

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
        };

        $scope.removerParcela = function(ev, index) {
            let parcela = $scope.object.notaSaidaParcelas[index];

            if ($scope.isParcelaPreenchida(parcela)) {
                $mdDialog.show({
                    controller: DialogControllerRemoverParcela,
                    locals: {
                        index: index,
                        fn: fnConfirmRemoverParcela
                    },
                    templateUrl: 'partials/faturamento/notasFiscais/html/partials/dialog_remocao_parcela.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    fullscreen: false
                });
            } else {
                atualizaMinDateParcelaAoRemover(index, $scope.object.faturamentoEtapaTO.valorTributos)
                    .then(value => {
                        $scope.object.notaSaidaParcelas.splice(index, 1);
                        if ($scope.object.notaSaidaParcelas.length === 0) {
                            criarParcela(new Date(), 0);
                        }
                        $scope.totalValorParcelas();
                        $scope.totalValorPermuta();
                        $scope.totalValorRetencoes();
                        $scope.totalValorDescontos();
                        $scope.totalValorMultas();
                        $scope.totalValorTributos();
                        $scope.totalValorJuros();
                        $scope.totalValorLiquido();
                    });
            }
        };

        $scope.validarParcelas = function() {
            let parcelasValidas = true;

            angular.forEach($scope.object.notaSaidaParcelas, function(obj, key) {
                if (!CalculadorFaturamento.isCampoVazio(obj) && (CalculadorFaturamento.isCampoVazio(obj.valor) || CalculadorFaturamento.isCampoVazio(obj.dataVencimento))) {
                    parcelasValidas = false;
                }
            });

            return parcelasValidas;
        };

        $scope.adicionarDiasData = function(data, dias) {
            let dataVenc = new Date(data);
            let novaDataVenc = new Date(dataVenc.getTime() + (dias * 24 * 60 * 60 * 1000));
            return novaDataVenc;
        }

        $scope.montarParcelasTipoCobranca = function(faturamento) {
            if (!CalculadorFaturamento.isCampoVazio(faturamento.dataVencimento1) && !CalculadorFaturamento.isCampoVazio(faturamento.dataVencimento2)) {
                criarParcela(faturamento.dataVencimento1, undefined);
                criarParcela(faturamento.dataVencimento2, undefined);
                $scope.object.formaPagamento = "Parcelada";
            } else if (!CalculadorFaturamento.isCampoVazio(faturamento.dataVencimento1) || !CalculadorFaturamento.isCampoVazio(faturamento.dataVencimento2)) {
                $scope.object.formaPagamento = "Parcela única";
                $scope.object.vencimento = new Date(faturamento.dataVencimento1);
            }
        };
        //fim - aba parcelas

        //inicio - funções dos botões do wizard

        $scope.previousStep = function() {
            $scope.index--;
        };

        $scope.isFirstStep = function() {

            if ($scope.index === 0) {
                return true;
            } else {
                return false;
            }
        };

        $scope.nextStep = function() {
            $scope.index = $scope.index + 1;
        };

        $scope.isLastStep = function() {
            if (angular.isDefined($scope.object.formaPagamento) && $scope.object.formaPagamento === 'Parcelada') {
                if ($scope.index == 5) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if ($scope.index == 4) {
                    return true;
                } else {
                    return false;
                }
            }

        };

        $scope.currentStepInvalid = function() {
            if ($scope.index === 0) {
                if (angular.isDefined($scope.wizardStep1)) {
                    return $scope.wizardStep1.$invalid;
                }
            }
            if ($scope.index == 1) {
                if (angular.isDefined($scope.wizardStep2)) {
                    return $scope.wizardStep2.$invalid;
                }
            }
            if ($scope.index == 2) {
                if (angular.isDefined($scope.wizardStep3)) {
                    return $scope.wizardStep3.$invalid;
                }
            }
            if ($scope.index == 3) {
                if (angular.isDefined($scope.wizardStep4)) {
                    return $scope.wizardStep4.$invalid;
                }
            }
            if ($scope.index == 4) {
                if (angular.isDefined($scope.wizardStep5)) {
                    return $scope.wizardStep5.$invalid;
                }
            }
            if ($scope.index == 5) {
                if (angular.isDefined($scope.wizardStep6)) {
                    return $scope.wizardStep6.$invalid;
                }
            }
            return true;
        };

        $scope.formsIncomplete = function() {
            if ($scope.object.formaPagamento === 'Parcelada') {
                if (!$scope.validarParcelas())
                    return true;

                if (angular.isDefined($scope.wizardStep1) && angular.isDefined($scope.wizardStep2) && angular.isDefined($scope.wizardStep3) && angular.isDefined($scope.wizardStep4) && angular
                    .isDefined($scope.wizardStep5) && angular.isDefined($scope.wizardStep6)) {
                    if ($scope.wizardStep1.$invalid === true || $scope.wizardStep2.$invalid === true || $scope.wizardStep3.$invalid === true || $scope.wizardStep4.$invalid === true || $scope
                        .wizardStep5.$invalid === true || $scope.wizardStep6.$invalid === true) {
                        return true;
                    }
                }
            } else {
                if (angular.isDefined($scope.wizardStep1) && angular.isDefined($scope.wizardStep2) && angular.isDefined($scope.wizardStep3) && angular.isDefined($scope.wizardStep4) && angular
                    .isDefined($scope.wizardStep5)) {
                    if ($scope.wizardStep1.$invalid === true || $scope.wizardStep2.$invalid === true || $scope.wizardStep3.$invalid === true || $scope.wizardStep4.$invalid === true || $scope
                        .wizardStep5.$invalid === true) {
                        return true;
                    }
                }
            }
            return false;
        };
        //fim - funções dos botões do wizard


        //inicio - funções da nota fiscal
        $scope.exibirNotaFiscal = function(notaFiscal) {
            $state.go('notafiscal.exibir', {
                objectId: notaFiscal.codigo
            });
        };

        let numeracaoNotaFiscalDialogFunction = ($mdDialog, dataEmissao, numero, original) => {
            $rootScope.loadingProgress = true;
            $scope.action = true;
            $scope.object.status = "A processar";
            $scope.object.dataEmissao = ajustarTimeZoneData(dataEmissao);
            $scope.object.numero = numero;
            $scope.screenMode = 'VIEW';
            NotaFiscalFaturamento.numerarNF($scope.object).$promise.then(function(data) {
                $rootScope.loadingProgress = false;
                $scope.notification.success(undefined, data.numero + " numerada com sucesso.");
                $scope.objectId = $scope.object.codigo;
                verificarNfLote($scope.objectId);
                loadObj();
                $mdDialog.hide();
                $scope.action = false;
            }).catch(function(response) {
                $rootScope.loadingProgress = false;
                $scope.action = false;
                $mdDialog.hide();
                $scope.object = original;
                $scope.object.numero = "NF/000000000";
                $scope.$emit('crud-critical', 'search', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
            });
        };

        $scope.numerarNotaFiscal = (ev) => {
            let filtro = {};
            filtro.empresa = $scope.object.empresa;
            filtro.dataEmissao = $scope.object.dataEmissao;
            filtro.tipoFornecimento = $scope.object.tipoFornecimento;
            NotaFiscalFaturamento.qbeNumNF(filtro).$promise.then(function(data) {
                $scope.copia = angular.copy($scope.object);
                $scope.copia.numero = data.numero;

                $mdDialog.show({
                    controller: DialogNumerarNotaFiscal,
                    locals: {
                        nota: $scope.copia,
                        notification: $scope.notification,
                        func: numeracaoNotaFiscalDialogFunction
                    },
                    templateUrl: 'partials/faturamento/notasFiscais/html/partials/dialog_numerar_nf.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    fullscreen: false
                });

            }).catch(function(response) {
                $scope.$emit('crud-critical', 'search', response);
            });
        };

        //==== DIALOG NUMERAR NOTA FISCAL ====//
        let DialogNumerarNotaFiscal = ($scope, $mdDialog, $rootScope, nota, notification, func) => {
            const original = angular.copy(nota);
            // O timeout é usado para resolver um erro que ocorre no dialog
            // que impede o campo da data de emissão de carregar previamente preenchida.
            $timeout(function() {
                $scope.object = nota;
                $scope.notification = notification;
            }, 100);

            $scope.hide = function() {
                $mdDialog.hide();
            };

            $scope.cancel = function() {
                $mdDialog.cancel();
            };

            $scope.confirm = function() {
                $scope.action = true;
                func($mdDialog, $scope.object.dataEmissao, $scope.object.numero, original);
            };

            $scope.cancel = function() {
                $mdDialog.cancel();
            };
        };

        //==== DIALOG CANCELAR NUMERAÇÃO DA NOTA FISCAL ====//
        function DialogCancelarNumeracaoNotaFiscal($scope, $mdDialog, nota, func) {
            const original = angular.copy(nota);
            $scope.hide = function() {
                $mdDialog.hide();
            };

            $scope.cancel = function() {
                $mdDialog.cancel();
            };

            $scope.confirm = function() {
                $scope.action = true;
                func($mdDialog, original);
            };

            $scope.cancel = function() {
                $mdDialog.cancel();
            };
        };

        let functionCancelarNumeracaoNotaFiscal = ($mdDialog, original) => {
            $rootScope.loadingProgress = true;
            $scope.action = true;
            $scope.object.status = "Aberta";
            desabilitarBotaoNumerar();
            $scope.screenMode = 'VIEW';
            NotaFiscalFaturamento.cancelarNumeracaoNF($scope.object)
                .$promise.then(function(data) {
                    $scope.notification.success(undefined, "Cancelamento da numeração da nota fiscal realizada com sucesso");
                    $scope.object = data;
                    loadObj();
                    $mdDialog.hide();
                    $scope.action = false;
                    $rootScope.loadingProgress = false;
                    $scope.botaoNumerarDesabilitado = false;
                }).catch(function(response) {
                    $mdDialog.hide();
                    $rootScope.loadingProgress = false;
                    $scope.action = false;
                    $scope.object = original;
                    $scope.$emit('crud-critical', 'search', response);
                    $scope.notification.criticalError(response.status, response.statusText, response.data);
                });
        };

        $scope.cancelarNumeracaoNotaFiscal = function(ev) {

            $mdDialog.show({
                controller: DialogCancelarNumeracaoNotaFiscal,
                locals: {
                    nota: $scope.object,
                    func: functionCancelarNumeracaoNotaFiscal
                },
                templateUrl: 'partials/faturamento/notasFiscais/html/partials/dialog_cancelar_numeracao_nf.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        $scope.confirmarCadastroNotaFiscal = function(ev) {
            $mdDialog.show({
                controller: DialogConfirmarCadastroNotaFiscal,
                locals: {
                    nota: $scope.object,
                    notification: $scope.notification,
                },
                templateUrl: 'partials/faturamento/notasFiscais/html/partials/dialog_confirmar_cadastro_nf.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                fullscreen: false
            });

        };

        $scope.removerNotaFiscal = function(ev) {

            $mdDialog.show({
                controller: DialogRemoverNotaFiscal,
                locals: {
                    nota: $scope.object,
                    notification: $scope.notification,
                },
                templateUrl: 'partials/faturamento/notasFiscais/html/partials/dialog_remover_nf.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        $scope.cancelarNotaFiscal = function(event) {
            let chaveMotivo = 'faturamento-canc_nf';
            let titulo = 'Motivos de Retorno - Cancelamento de Nota Fiscal';

            // Função que será executada no confirmar no dialog de motivo de retorno.
            $scope.confirmarMotivo = function(motivoRetornoDocumento, fn_Hide) {
                // MotivoRetornoNotaTO
                $scope.motivoRetornoNota = {};
                $scope.motivoRetornoNota.nota = {
                    codigo: $scope.object.codigo
                };
                $scope.motivoRetornoNota.dataCancelamento = motivoRetornoDocumento.dataCancelamento;
                $scope.motivoRetornoNota.motivoRetornoDocumento = motivoRetornoDocumento;
                $scope.motivoRetornoNota.motivoRetornoDocumento.aplicacao.chave = 'faturamento-canc_nf';

                $rootScope.loadingProgress = true;

                MotivoRetornoNota.saveCancelamentoNotaFiscal($scope.motivoRetornoNota)
                    .$promise.then(function(data) {
                        $rootScope.loadingProgress = false;
                        $scope.notification.success(undefined, "Nota cancelada com sucesso.");
                        fn_Hide();
                        $scope.objectId = $scope.object.codigo;
                        $scope.object.status = 'Cancelada';
                        loadObj();
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
            MotivoRetornoDataCancelamentoDialog.show(event, chaveMotivo, titulo, $scope.confirmarMotivo, $scope.cancelarMotivo);
        };

        $scope.reverterCancelamento = function(event) {
            let chaveMotivo = 'fatura-revert_canc_nf';
            let titulo = 'Motivos de Retorno - Reverter Cancelamento de Nota Fiscal';

            // Função que será executada no confirmar no dialog de motivo de retorno.
            $scope.confirmarMotivo = function(motivoRetornoDocumento, fn_Hide) {
                // MotivoRetornoNotaTO
                $scope.motivoRetornoNota = {};
                $scope.motivoRetornoNota.nota = {
                    codigo: $scope.object.codigo
                };
                $scope.motivoRetornoNota.dataCancelamento = motivoRetornoDocumento.dataCancelamento;
                $scope.motivoRetornoNota.motivoRetornoDocumento = motivoRetornoDocumento;

                $rootScope.loadingProgress = true;

                MotivoRetornoNota.save($scope.motivoRetornoNota)
                    .$promise.then(function(data) {
                        $rootScope.loadingProgress = false;
                        $scope.notification.success(undefined, "Reversão do cancelamento da nota fiscal realizado com sucesso.");
                        fn_Hide();
                        $scope.objectId = $scope.object.codigo;
                        //                        $scope.object.status = 'Cancelada';
                        loadObj();
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

        $scope.removerNfNumerada = function(event) {
            let chaveMotivo = 'faturamento-remover_nf';
            let titulo = 'Motivos de Retorno - Remover Nota Fiscal Numerada';

            // Função que será executada no confirmar no dialog de motivo de retorno.
            $scope.confirmarMotivo = function(motivoRetornoDocumento, fn_Hide) {
                // MotivoRetornoNotaTO
                $scope.motivoRetornoNota = {};
                $scope.motivoRetornoNota.nota = {
                    codigo: $scope.object.codigo
                };
                $scope.motivoRetornoNota.dataCancelamento = motivoRetornoDocumento.dataCancelamento;
                $scope.motivoRetornoNota.motivoRetornoDocumento = motivoRetornoDocumento;

                $rootScope.loadingProgress = true;

                MotivoRetornoNota.save($scope.motivoRetornoNota)
                    .$promise.then(function(data) {
                        $rootScope.loadingProgress = false;
                        $scope.notification.success(undefined, "Nota fiscal removida com sucesso.");
                        fn_Hide();
                        $scope.retornarTelaAnterior();
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

        $scope.retornarParaContasReceber = function(event) {
            if ($scope.codigoParaVoltarContasReceber) {
                $state.go("contasareceber.exibir", {
                    objectId: $scope.codigoParaVoltarContasReceber
                });
            }
        };

        $scope.salvarNotaFiscal = function() {
            $rootScope.loadingProgress = true;
            $scope.action = true;
            let error = false;

            $scope.object.faturamentoEtapaTO.valorLiquido = $scope.object.faturamentoEtapaTO.valor - $scope.valorTributosView;

            let convertDoubleValorBrutoTotal = 0;

            angular.forEach($scope.object.notaSaidaParcelas, function(value, index) {
                let convertDouble = parseFloat(value.valorLiquido);
                if (convertDouble < 0) {
                    error = true;
                    $scope.notification.error("", "O valor líquido não pode ser menor que R$ 0,00.");
                    $scope.action = false;
                    return;
                }
                let convertDoubleValorBruto = parseFloat(value.valorBruto);
                convertDoubleValorBrutoTotal = convertDoubleValorBrutoTotal + convertDoubleValorBruto;
                if (convertDoubleValorBruto <= 0) {
                    error = true;
                    $scope.notification.error("", "O valor bruto deve ser maior que R$ 0,00.");
                    $scope.action = false;
                    return;
                }
            });

            if ($scope.object.notaSaidaParcelas.length > 0 && Number((convertDoubleValorBrutoTotal).toFixed(2)) < Number(($scope.object.faturamentoEtapaTO.valor).toFixed(2))) {
                $scope.notification.error("", "O valor total das parcelas deve ser igual ao valor da etapa.");
                $scope.action = false;
                return;
            }

            if (!error) {
                return $q(function(resolve, reject) {


                    if ($scope.screenMode === 'NEW') {
                        $scope.object.servicoTipo = $scope.object.servicoTP;
                    }

                    if ($scope.listaNotaTributoAliquota && $scope.listaNotaTributoAliquota.length > 0) {
                        $scope.object.listaNotaTributoAliquota = [];

                        for (let i = 0; i < $scope.listaNotaTributoAliquota.length; i++) {
                            let objNotaTributoAliquota = $scope.listaNotaTributoAliquota[i];

                            let notaTributoAliquota = {};

                            if (!CalculadorFaturamento.isCampoVazio(objNotaTributoAliquota.codigo)) {
                                notaTributoAliquota.codigo = objNotaTributoAliquota.codigo;
                            }

                            notaTributoAliquota.nota = $scope.object;
                            notaTributoAliquota.aliquota = {};
                            notaTributoAliquota.aliquota.codigo = objNotaTributoAliquota.aliquota.codigo;
                            notaTributoAliquota.aliquota.codigoTributo = objNotaTributoAliquota.aliquota.codigoTributo;
                            notaTributoAliquota.percentual = objNotaTributoAliquota.aliquota.percentual;
                            notaTributoAliquota.aliquota.percentual = objNotaTributoAliquota.aliquota.percentual;
                            notaTributoAliquota.aliquota.codigoCategoriaTributo = objNotaTributoAliquota.aliquota.categoriaTributo.codigo;
                            notaTributoAliquota.baseCalculo = objNotaTributoAliquota.baseCalculo;
                            notaTributoAliquota.valor = objNotaTributoAliquota.valor;
                            notaTributoAliquota.individual = objNotaTributoAliquota.individual;

                            delete notaTributoAliquota.nota;

                            $scope.object.listaNotaTributoAliquota.push(notaTributoAliquota);
                        }

                    }

                    NotaFiscalFaturamento.salvarNF($scope.object).$promise.then(function(data) {
                        $rootScope.loadingProgress = false;
                        $scope.object = data;
                        $scope.notification.success(undefined, "Nota fiscal salva com sucesso");
                        $scope.action = false;
                        $scope.modeDiscriminacao = false;
                        if ($scope.screenMode === 'NEW') {
                            verificarNfLote(data.codigo);
                            if ($rootScope.$lastState.name !== '') {
                                $state.go('notafiscal.exibir_por_acao', {
                                    objectId: data.codigo,
                                    acao: 'FAT'
                                });
                            } else {
                                $state.go('etapasnaofaturadas.listar');
                            }
                        } else {
                            $scope.screenMode = 'VIEW';
                            $scope.objectId = $scope.object.codigo;
                            loadObj();
                        }
                        resolve({
                            mensagem: 'Nota fiscal salva com sucesso!',
                            executou: true
                        });
						NotaFiscalFaturamento.verificarContaContabilCliente({id: $scope.object.id}).$promise.then(function(data){
//							console.log(data);
							if (data && data.warnings && data.warnings.length > 0) {
								$scope.notification.atencao('', data.warnings[0]);
							}
						});
                    }).catch(function(response) {
                        $rootScope.loadingProgress = false;
                        $scope.$emit('crud-critical', 'search', response);
                        console.error(response);
                        $scope.action = false;
                        reject({
                            mensagem: 'Nota fiscal com error!',
                            executou: false
                        })
                    });
                });
            }
        };

        $scope.cancelarNovoNotaFiscal = function(ev) {
            if (!$scope.wizardStep1.$invalid || !$scope.bloquearEmissaoNF) {
                if ($rootScope.$lastState.params.acao === 'FAT') {
                    let params = {
                        ...$rootScope.$lastState.params
                    };
                    params.objectId = params.from;
                    $state.go('faturamento.exibir', params);
                } else if ($rootScope.$lastState.name !== '') {
                    $state.go($rootScope.$lastState.name, $rootScope.$lastState.params);
                } else {
                    $state.go('etapasnaofaturadas.listar');
                }
            } else {
                $mdDialog.show({
                    controller: DialogCancelarNovoNotaFiscal,
                    templateUrl: 'partials/faturamento/notasFiscais/html/partials/dialog_cancelar_novo_nota_fiscal.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    fullscreen: false
                });
            }
        };

        $scope.retornarTelaAnterior = function() {
            if ($location.search().acao === 'parcelar') {
                $state.go('contasareceber.listar');
            } else if ($location.search().acao === 'FAT') {
                $state.go('faturamento.exibir', {
                    objectId: $scope.object.faturamentoEtapaTO.faturamento.codigo
                });
            } else if ($location.search().from) {
                $state.go($location.search().from, {
                    objectId: $location.search().acao
                });
            } else if ($rootScope.$lastState.name !== '') {
                $state.go($rootScope.$lastState.name, $rootScope.$lastState.params);
            } else {
                $state.go('notafiscal.listar');
            }
        };

        function verificarRetencao(listaTributos) {
            let listaTributosParaRetencao = [];

            angular.forEach(listaTributos, function(notaTributoAliquota, index) {
                if (tributoEstaRetido(notaTributoAliquota)) {
                    listaTributosParaRetencao.push(notaTributoAliquota);
                }
            });

            if (listaTributosParaRetencao && listaTributosParaRetencao.length > 0) {
                gerenciarRetencaoTributo(undefined, listaTributosParaRetencao, $scope.gerenciarVariaveisDaTabelaTributos, $scope.atualizarDiscriminacaoServico, undefined, undefined);
            }
        };

        // indica se o tributo (nota tributo aliquota) esta retido ou não
        function tributoEstaRetido(notaTributoAliquota) {
            if (notaTributoAliquota.aliquota.categoriaTributo.minimoGuia == 0) {
                return false;
            } else if (notaTributoAliquota.valor < notaTributoAliquota.aliquota.categoriaTributo.minimoGuia) {
                return true;
            } else {
                return false;
            }
        }

        function gerenciarRetencaoTributo(ev, listaTributosParaRetencao, fnGerenciarVariaveisDaTabelaTributos, fnAtualizarDiscriminacaoServico, fnAtualizarAbaParcela, formaPagamento) {
            $mdDialog.show({
                controller: DialogControllerRetencaoTributo,
                locals: {
                    listaTributosParaRetencao: listaTributosParaRetencao,
                    fnGerenciarVariaveisDaTabelaTributos: fnGerenciarVariaveisDaTabelaTributos,
                    fnAtualizarDiscriminacaoServico: fnAtualizarDiscriminacaoServico,
                    fnAtualizarAbaParcela: fnAtualizarAbaParcela,
                    formaPagamento: formaPagamento
                },
                templateUrl: 'partials/faturamento/notasFiscais/html/partials/dialog_tributo_retencao.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                fullscreen: false
            });
        }

        function DialogControllerRetencaoTributo($scope, $mdDialog, listaTributosParaRetencao, fnGerenciarVariaveisDaTabelaTributos, fnAtualizarDiscriminacaoServico, fnAtualizarAbaParcela, formaPagamento) {
            init();

            function init() {
                $scope.zero = 0;

                $scope.listaTributos = listaTributosParaRetencao;

                angular.forEach($scope.listaTributos, function(notaTributoAliquota, index) {

                    if (notaTributoAliquota.valor == 0) {
                        notaTributoAliquota.zerar = true;

                        let valorOriginal = notaTributoAliquota.baseCalculo * (notaTributoAliquota.aliquota.percentual / 100);
                        notaTributoAliquota.valorOriginal = valorOriginal;

                    } else {
                        notaTributoAliquota.zerar = false;
                        notaTributoAliquota.valorOriginal = angular.copy(notaTributoAliquota.valor);
                    }
                });
            }

            $scope.checkZerar = function(item) {
                if (item.zerar) {
                    item.zerar = false;
                } else {
                    item.zerar = true;
                }
            };

            $scope.cancel = function() {
                $mdDialog.cancel();
            };

            $scope.sim = function() {
                angular.forEach($scope.listaTributos, function(notaTributoAliquota, index) {
                    if (notaTributoAliquota.zerar) {
                        notaTributoAliquota.valor = 0;
                    } else {
                        let valorOriginal = notaTributoAliquota.baseCalculo * (notaTributoAliquota.aliquota.percentual / 100);
                        notaTributoAliquota.valor = valorOriginal;
                    }
                });
                fnGerenciarVariaveisDaTabelaTributos();
                fnAtualizarDiscriminacaoServico();

                if (formaPagamento && formaPagamento === 'Parcelada') {
                    fnAtualizarAbaParcela();
                }

                $mdDialog.cancel();
            };

            $scope.nao = function() {
                $mdDialog.cancel();
            };
        }

        $scope.atualizarDiscriminacaoServico = function() {
            let copiaDiscriminacao = angular.copy($scope.object.infoComplementarContribuinte);

            setValorTributo();

            if (copiaDiscriminacao && copiaDiscriminacao.length > 0) {

                let arrayLinhas = copiaDiscriminacao.trim().split("\n");
                let linhaTextoValorDosTributos = undefined;

                angular.forEach(arrayLinhas, function(value, key) {
                    let linha = value;
                    let textoValorDosTributos = linha.replace(/\s/g, "").indexOf("VALORDOSTRIBUTOS");
                    if (textoValorDosTributos > 0) {
                        linhaTextoValorDosTributos = key;
                    }
                });

                let novoTexto = "";

                if (linhaTextoValorDosTributos) {
                    angular.forEach(arrayLinhas, function(value, key) {
                        let linha = value;

                        // penultima linha
                        if (linhaTextoValorDosTributos === key) {

                            let valorPercentual = 0;

                            let indexVarCargaTributo = linha.indexOf("CARGA TRIBUTARIA");
                            let inicioDalinhaCarga = linha.substring(0, indexVarCargaTributo).trim();

                            if ($scope.object.issRetidoCliente) {
                                valorPercentual = $scope.totalPercentualTributo;
                            } else {
                                valorPercentual = $scope.subtotalPercentualTributo;
                            }

                            let novaLinha = inicioDalinhaCarga.concat("CARGA TRIBUTARIA = ")
                                .concat($filter('currency')(valorPercentual, '')).concat("%");

                            novaLinha = novaLinha.concat(" ").concat("VALOR DOS TRIBUTOS = ")
                                .concat($filter('currency')($scope.object.valorTributos, 'R$ '));

                            novoTexto = novoTexto.concat(novaLinha).concat("\n");

                            // ultima linha
                        } else if (linhaTextoValorDosTributos + 1 === key) {

                            let map = new Map();
                            let listaCodTributo = [1, 6, 5, 4]; // a ordem que aparece os tributos

                            angular.forEach($scope.listaNotaTributoAliquota, function(item, key2) {
                                map.set(item.aliquota.categoriaTributo.tributoTO.codigo, item);
                            });

                            let novaLinha = "";

                            angular.forEach(listaCodTributo, function(item, key2) {
                                let tributoSelecionado = map.get(item);
                                if (tributoSelecionado && tributoSelecionado.valor > 0) {
                                    let nomeTributo = tributoSelecionado.aliquota.categoriaTributo.tributoTO.nome;
                                    if (!$scope.object.issRetidoCliente && nomeTributo === 'ISS') {
                                        return;
                                    }
                                    let percentualTributo = tributoSelecionado.aliquota.percentual;
                                    if (nomeTributo === "PIS/COFINS/CSLL") {
                                        nomeTributo = "PCC";
                                    }
                                    novaLinha = novaLinha.concat(nomeTributo).concat(":").concat("  ").concat(percentualTributo).concat("%").concat("    ");
                                }
                            });

                            novoTexto = novoTexto.concat(novaLinha.trim()).concat("\n");

                        } else {
                            novoTexto = novoTexto.concat(linha).concat("\n");
                        }

                    });

                    $scope.object.infoComplementarContribuinte = novoTexto.trim();
                }
            }
        };

        let verificarNfLote = (codNotaFiscal) => {
            let promiseNF = NotaFiscalFaturamento.verificarNfLote({
                id: codNotaFiscal
            }).$promise;

            $rootScope.loadingProgress = true;

            promiseNF.then(function(data) {
                if (data.selecao) {
                    const mensagem =
                        "Existe um lote contábil mensal para esta empresa/emissão. Favor avisar à Contabilidade que uma nova NF foi criada e que o lote deverá ser reprocessado.";
                    $scope.notification.atencao("", mensagem);
                }

                $rootScope.loadingProgress = false;

            }).catch(function(response) {
                $rootScope.loadingProgress = false;
                $scope.$emit('crud-critical', 'search', response);
            });
        };

        //fim - funções da nota fiscal

        //==== DIALOG CANCELAR NOVO NOTA FISCAL ====//
        function DialogCancelarNovoNotaFiscal($scope, $mdDialog, $rootScope) {

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
            $scope.confirmar = function() {
                $state.go('etapasnaofaturadas.listar');
                $mdDialog.hide();
            }
        }

        function desabilitarBotaoNumerar() {
            $scope.botaoNumerarDesabilitado = true;
        }

        //==== DIALOG CONFIRMAR CADASTRO DA NOTA FISCAL ====//
        function DialogConfirmarCadastroNotaFiscal($scope, $mdDialog, $rootScope, nota, notification) {
            $scope.object = angular.copy(nota);
            $scope.notification = notification;

            $scope.hide = function() {
                $mdDialog.hide();
            };

            $scope.cancel = function() {
                $mdDialog.cancel();
            };

            $scope.confirm = function() {
                $rootScope.loadingProgress = true;
                NotaFiscalFaturamento.confirmarCadastroNF($scope.object)
                    .$promise.then(function(data) {
                        $rootScope.loadingProgress = false;
                        notification.success("Nota fiscal", "Confirmação do cadastro na prefeitura da nota fiscal realizada com sucesso");
                        $scope.objectId = $scope.object.codigo;
                        loadObj();
                    }).catch(function(response) {
                        $rootScope.loadingProgress = false;
                        $scope.$emit('crud-critical', 'search', response);
                        notification.criticalError(response.status, response.statusText, response.data);
                    });

                $mdDialog.hide();
            };

            $scope.cancel = function() {
                $mdDialog.cancel();
            };
        }

        //==== DIALOG CONFIRMAR CADASTRO DA NOTA FISCAL ====//
        function DialogRemoverNotaFiscal($scope, $mdDialog, $rootScope, nota, notification) {
            $scope.object = nota;
            $scope.notification = notification;
            $scope.action = false;

            $scope.hide = function() {
                $mdDialog.hide();
            };

            $scope.cancel = function() {
                $mdDialog.cancel();
            };

            $scope.confirm = function() {
                $rootScope.loadingProgress = true;
                $scope.action = true;

                NotaFiscalFaturamento.removerNF($scope.object).$promise.then(function(data) {
                    $scope.notification.success(undefined, "Nota fiscal removida com sucesso");
                    $mdDialog.hide();
                    $scope.action = false;
                    $rootScope.loadingProgress = false;
                    if ($location.search().acao === 'FAT') {
                        $state.go('faturamento.exibir', {
                            objectId: $scope.object.faturamentoEtapaTO.faturamento.codigo
                        });
                    } else {
                        $state.go('notafiscal.listar');
                    }
                }).catch(function(response) {
                    $scope.action = false;
                    $rootScope.loadingProgress = false;
                    $scope.$emit('crud-critical', 'search', response);
                    $scope.notification.criticalError(response.status, response.statusText, response.data);
                });
            };

            $scope.cancel = function() {
                $mdDialog.cancel();
            };
        }

        $scope.atualizarValorLiquido = function(notaFiscal) {
            $scope.valorLiquidoEtapa = $scope.calcularValorLiquidoEtapa(notaFiscal);
            $scope.object.faturamentoEtapaTO.valorLiquido = Math.round($scope.valorLiquido * 100) / 100;
        };

        $scope.atualizarValorTributos = function(notaFiscal) {
            $scope.valorTributos = $scope.calcularValorTributos(notaFiscal);
        };

        //calcula os valores das retenções da ABA DE PARCELAS - SOMENTE DA ABA DE PARCELAS
        $scope.calcularValorRetencoesParcela = function(faturamento, parcela) {
            let somaPercentualRetencao = 0;
            let valorRetencoes = 0;

            if (!CalculadorFaturamento.isCampoVazio(faturamento)) {
                if (!CalculadorFaturamento.isCampoVazio(parcela)) {
                    if (angular.isDefined(faturamento.faturamentoRetencoes)) {

                        if (angular.isUndefined(parcela.parcelaRetencoes))
                            parcela.parcelaRetencoes = [];

                        if (faturamento.faturamentoRetencoes != undefined && (parcela.parcelaRetencoes.length == 0 || parcela.parcelaRetencoes.length != faturamento.faturamentoRetencoes.length)) {

                            angular.forEach(faturamento.faturamentoRetencoes, function(value, key) {
                                //parcelaRetencao.percentual usado somente para facilitar o cálculo em tempo de execução, não é persistido e nem mapeado

                                let parcelaRetencao = {};
                                parcelaRetencao.tipo = value.tipo.nome;
                                parcelaRetencao.percentual = value.percentual;

                                let valorRetencao = 0;
                                valorRetencao = (parcela.valor * value.percentual) / 100;

                                if (CalculadorFaturamento.isCampoVazioNumerico(valorRetencao))
                                    valorRetencao = 0;

                                parcelaRetencao.valorAReter = valorRetencao;
                                parcelaRetencao.valorRetido = 0;

                                if (parcelaRetencao.tipo === "Acessoria") {
                                    parcelaRetencao.tipo = "Acessória";
                                }

                                parcela.parcelaRetencoes.push(parcelaRetencao);
                                somaPercentualRetencao = somaPercentualRetencao + value.percentual;
                            });

                        } else {

                            angular.forEach(parcela.parcelaRetencoes, function(parcelaRetencao, key) {
                                //value.percentual usado somente para facilitar o cálculo em tempo de execução, não é persistido e nem mapeado

                                if (CalculadorFaturamento.isCampoVazio(parcelaRetencao.percentual)) {
                                    angular.forEach(faturamento.faturamentoRetencoes, function(faturamentoRetencao, key) {
                                        if (faturamentoRetencao.tipo.chave.toUpperCase() === StringService.replaceAccents(parcelaRetencao.tipo).toUpperCase()) {
                                            parcelaRetencao.percentual = faturamentoRetencao.percentual;
                                        }
                                    });
                                }

                                let valorRetencao = 0;
                                valorRetencao = (parcela.valor * parcelaRetencao.percentual) / 100;

                                if (CalculadorFaturamento.isCampoVazioNumerico(valorRetencao))
                                    valorRetencao = 0;

                                parcelaRetencao.valorAReter = valorRetencao;
                                somaPercentualRetencao = somaPercentualRetencao + parcelaRetencao.percentual;
                            });
                        }
                    }

                    valorRetencoes = (parcela.valor * somaPercentualRetencao) / 100;

                    if (CalculadorFaturamento.isCampoVazioNumerico(valorRetencoes))
                        valorRetencoes = 0;

                    return valorRetencoes;
                }
            }

            return 0.00;
        };

        //calcula os valores das retenções da ABA DE ETAPAS - SOMENTE DA ABA DE ETAPAS
        $scope.calcularValorRetencoesEtapa = function(etapa, faturamento) {

            if (angular.isDefined(faturamento) && angular.isDefined(faturamento.faturamentoRetencoes)) {
                angular.forEach(faturamento.faturamentoRetencoes, function(value, key) {
                    if (value.tipo.chave === 'contratual') {
                        $scope.percentualRetencaoContratual = value.percentual;
                        $scope.valorRetencaoContratual = (etapa.valor * $scope.percentualRetencaoContratual) / 100;
                    } else if (value.tipo.chave === 'acessoria') {
                        $scope.percentualRetencaoAcessoria = value.percentual;
                        $scope.valorRetencaoAcessoria = (etapa.valor * $scope.percentualRetencaoAcessoria) / 100;
                    }
                });
            }

        };

        //calcula o valor líquido da ABA DE ETAPAS - SOMENTE DA ABA DE ETAPAS
        $scope.calcularValorLiquidoEtapa = function(notaFiscal) {
            if (!CalculadorFaturamento.isCampoVazio(notaFiscal)) {
                if (!CalculadorFaturamento.isCampoVazio(notaFiscal.faturamentoEtapaTO)) {

                    let varValor = notaFiscal.faturamentoEtapaTO.valor;
                    //                    let varTributos = $scope.calcularValorTributos(notaFiscal);
                    let varTributos = 0;
                    let varPermuta = notaFiscal.faturamentoEtapaTO.valorPermuta;
                    let varDesconto = notaFiscal.faturamentoEtapaTO.valorDesconto;

                    let varMulta = notaFiscal.faturamentoEtapaTO.valorMulta;
                    let varJuros = notaFiscal.faturamentoEtapaTO.valorJuros;

                    $scope.calcularValorRetencoesEtapa(notaFiscal.faturamentoEtapaTO, notaFiscal.faturamentoEtapaTO.faturamento);
                    let varRetencoes = $scope.valorRetencaoContratual + $scope.valorRetencaoAcessoria;

                    let varLiquido = varValor - varTributos - varRetencoes - varPermuta - varDesconto + varMulta + varJuros;


                    return varLiquido;
                }
            }

            return 0;
        };

        $scope.calcularPercentualTributo = function(notaFiscal) {
            if (!CalculadorFaturamento.isCampoVazio(notaFiscal) &&
                !CalculadorFaturamento.isCampoVazio(notaFiscal.faturamentoEtapaTO) &&
                !CalculadorFaturamento.isCampoVazio(notaFiscal.faturamentoEtapaTO.faturamento) &&
                !CalculadorFaturamento.isCampoVazio(notaFiscal.faturamentoEtapaTO.faturamento.faturamentoAliquotas)
            ) {
                return CalculadorFaturamento.percentualTotalTributo(notaFiscal.faturamentoEtapaTO.faturamento.faturamentoAliquotas, notaFiscal.faturamentoEtapaTO.faturamento.issRetidoCliente);
            }
            return 0;
        };

        $scope.calcularValorTributos = function(notaFiscal) {
            let varPercentualTributo = $scope.calcularPercentualTributo(notaFiscal);
            let varValor = notaFiscal.faturamentoEtapaTO.valor;
            let varValorTributos = (varValor * varPercentualTributo) / 100;
            return varValorTributos;
        };

        $scope.getListaTributos = function() {
            return $scope.listaNotaTributoAliquota;
        };

        $scope.getSubtotalPercentualTributos = function() {
            if ($scope.notaTributoAliquota && typeof $scope.notaTributoAliquota.subtotalPercentual === 'number') {
                return $scope.notaTributoAliquota.subtotalPercentual;
            }
            return $scope.subtotalPercentualTributo;
        };

        $scope.getSubtotalValorTributos = function() {
            if ($scope.notaTributoAliquota && typeof $scope.notaTributoAliquota.subtotalValor === 'number') {
                return $scope.notaTributoAliquota.subtotalValor;
            }
            return $scope.subTotalValorTributo;
        };

        $scope.getTotalPercentualTributos = function() {
            if ($scope.notaTributoAliquota && typeof $scope.notaTributoAliquota.totalPercentual === 'number') {
                return $scope.notaTributoAliquota.totalPercentual;
            }
            return $scope.totalPercentualTributo;
        };

        $scope.getTotalValorTributos = function() {
            //            if ($scope.notaTributoAliquota && typeof $scope.notaTributoAliquota.totalValor === 'number') {
            //                return $scope.notaTributoAliquota.totalValor;
            //            }
            return $scope.totalValorTributo;
        };

        // Adiciona o atributo siglaNome no atributo servicoTipo do objeto carregado.
        // Usado para preencher o campo "Código do serviço".
        // Usado no carregamento do objeto
        function adicionarSiglaNome(object) {
            if (object && object.servicoTipo) {
                $scope.object.servicoTipo.siglaNome = object.servicoTipo.sigla + " - " + object.servicoTipo.nome;
            }
        }

        // ABA TRIBUTOS
        //VARIAVEIS DA TABELA TRIBUTOS:
        $scope.subtotalPercentualTributo = 0;
        $scope.subTotalValorTributo = 0;
        $scope.totalPercentualTributo = 0;
        $scope.totalValorTributo = 0;
        $scope.listaFaturamentoAliquota = [];
        $scope.listaNotaTributoAliquota = [];
        $scope.stringCidadeIss = '';

        // CÓDIGO ABAIXO USADO NO CARREGAMENTO DOS FATURAMENTOS ALIQUOTAS (TRIBUTOS)
        function carregarFaturamentoAliquotas(codFaturamento) {
            return new Promise(
                function(resolve, reject) {
                    $rootScope.loadingProgress = true;
                    let obj = {};
                    obj.codigoFaturamento = codFaturamento;

                    let params = {};

                    FaturamentoAliquota.page(params, obj).$promise.then(function(data) {
                        $rootScope.loadingProgress = false;
                        $scope.listaFaturamentoAliquota = [];
                        $scope.object.faturamentoAliquotas = [];

                        $scope.object.faturamentoAliquotas = data.content;

                        if (angular.isDefined($scope.object.faturamentoAliquotas)) {
                            angular.forEach($scope.object.faturamentoAliquotas, function(value, key) {
                                let filtro = {};

                                filtro.codigo = value.aliquota.codigoCategoriaTributo;
                                $rootScope.loadingProgress = true;

                                CategoriaTributo.page(undefined, filtro).$promise.then(function(data) {
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


                                        if (tributo.aliquota && tributo.aliquota.categoriaTributo && value.aliquota.categoriaTributo.tributoTO.id === 4) {
                                            tributo.valor = NotasFiscaisService.calculaValorTributo(
                                                value.baseCalculo,
                                                tributo.aliquota.categoriaTributo,
                                                tributo.aliquota.categoriaTributo.cidade.id);
                                        }

                                        $scope.listaFaturamentoAliquota.push(tributo);

                                        if ($scope.screenMode === 'NEW' && $scope.listaFaturamentoAliquota.length === $scope.object.faturamentoAliquotas.length) {
                                            if (angular.isDefined($scope.montouFaturamentoAnterior)) {
                                                // $scope.cidUnidade = $scope.getCidadeISS();
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

                                                if (verificaCidadesDivergentes()) {
                                                    $scope.cidadesDivergentes = true;
                                                    $scope.notification.atencao(undefined,
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

                    }).catch(function(response) {
                        $scope.$emit('crud-critical', 'load', response);
                        $scope.object.faturamentoEtapas = [];
                        reject(false);
                    });

                    resolve(true);
                });
        }

        function carregarListaNotaTributoAliquota(codNotaFiscal, situacaoDaNota) {
            return new Promise(function(resolve, reject) {
                $rootScope.loadingProgress = true;
                $scope.listaNotaTributoAliquota = [];
                let params = {
                    codigoNota: codNotaFiscal
                };
                $scope.notaTributoAliquota = {
                    subtotalPercentual: 0,
                    subtotalValor: 0,
                    totalPercentual: 0,
                    totalValor: 0,
                    individual: 0,
                };

                let pNotaTributoAliquota = undefined;

                if (situacaoDaNota === 'Aberta' || situacaoDaNota === 'A processar' || situacaoDaNota === 'Em processamento') {
                    pNotaTributoAliquota = NotaTributoAliquota.listarPorNota(params, undefined).$promise;
                } else if (situacaoDaNota === 'Em recebimento' || situacaoDaNota === 'Recebida' || situacaoDaNota === 'Cancelada' || situacaoDaNota === 'Cancelada Faturamento') {
                    pNotaTributoAliquota = NotaTributoAliquotaEmissao.listarPorNota(params, undefined).$promise;
                }

                pNotaTributoAliquota.then(function(data) {
                    $rootScope.loadingProgress = false;

                    let tamanhoTributos = data.length;

                    $scope.stringCidadeIss = '';

                    if (tamanhoTributos > 0) {

                        let contador = 0;

                        angular.forEach(data, function(value, key) {
                            let filtro = {};
                            let aliquota = value.aliquota;

                            filtro.codigo = aliquota.codigoCategoriaTributo;
                            $rootScope.loadingProgress = true;

                            CategoriaTributo.page(undefined, filtro).$promise.then(function(data) {
                                $rootScope.loadingProgress = false;
                                let categoriaTributo = data.content[0];
                                let tributo = {};

                                if ($scope.screenMode != 'NEW') {
                                    tributo.codigo = value.codigo;
                                    tributo.codigoFaturamento = value.codigofaturamento;
                                }

                                tributo.aliquota = {
                                    codigo: aliquota.codigo,
                                    percentual: aliquota.percentual,
                                    categoriaTributo: categoriaTributo,
                                    codigoTributo: categoriaTributo.tributoTO.codigo
                                };
                                tributo.baseCalculo = value.baseCalculo;
                                tributo.valor = value.valor;
                                tributo.individual = value.individual;
                                tributo.nome = categoriaTributo.tributoTO.nome;
                                $scope.notaTributoAliquota.totalPercentual += aliquota.percentual;
                                $scope.notaTributoAliquota.totalValor += value.valor;

                                if (tributo.nome !== 'ISS') {
                                    $scope.notaTributoAliquota.subtotalPercentual += aliquota.percentual;
                                    $scope.notaTributoAliquota.subtotalValor += value.valor;
                                }

                                $scope.listaNotaTributoAliquota.push(tributo);

                                //Organizar a lista de nota tributo aliquota
                                organizarNotaTributoAliquota();

                                $scope.gerenciarVariaveisDaTabelaTributos();

                                contador++;

                                if (contador === tamanhoTributos) {
                                    $scope.carregamentoParcelas($scope.listaNotaTributoAliquota);
                                    mudarCidadeIss($scope.listaNotaTributoAliquota);
                                }
                            }).catch(function(response) {
                                $rootScope.loadingProgress = false;
                                $scope.$emit('crud-critical', 'load', response);
                            });

                        });

                    } else {
                        $scope.carregamentoParcelas(undefined);
                    }

                    resolve(true);
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'load', response);
                    $scope.object.faturamentoEtapas = [];
                    reject(true);
                });
            });
        }

        $scope.temTributoSemIss = function() {

            if ($scope.listaNotaTributoAliquota != undefined) {

                let tamanho = $scope.listaNotaTributoAliquota.length;

                for (let i = 0; i < tamanho; i++) {
                    let codigoTributo = $scope.listaNotaTributoAliquota[i].aliquota.categoriaTributo.tributoTO.codigo;

                    if (codigoTributo === 4) {
                        tamanho--;
                    }
                }

                if (tamanho > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        $scope.temTributoIss = function() {

            if ($scope.listaNotaTributoAliquota != undefined) {

                let tamanho = $scope.listaNotaTributoAliquota.length;
                for (let i = 0; i < tamanho; i++) {
                    let codigoTributo = $scope.listaNotaTributoAliquota[i].aliquota.categoriaTributo.tributoTO.codigo;

                    if (codigoTributo === 4) {
                        return true;
                    }
                }

                return false;
            }
        };


        $scope.gerenciarVariaveisDaTabelaTributos = function() {
            let total_1 = 0;
            let total_2 = 0;
            let total_3 = 0;
            let total_4 = 0;

            angular.forEach($scope.listaNotaTributoAliquota, function(obj, key) {
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

            });

            $scope.subtotalPercentualTributo = total_1;
            $scope.subTotalValorTributo = total_2;

            $scope.totalPercentualTributo = total_1 + total_3;
            $scope.totalValorTributo = total_2 + total_4;

            setValorTributo();

            organizarNotaTributoAliquota();
        };

        //Funções auxiliares
        function mudarCidadeIss(paramListaNotaTributoAliquota) {

            if (!paramListaNotaTributoAliquota || paramListaNotaTributoAliquota.length == 0) {
                $scope.stringCidadeIss = '';
            } else {
                let ntaTributoISS = paramListaNotaTributoAliquota.filter(nta => nta.aliquota.categoriaTributo.tributoTO.nome === 'ISS')[0];

                if (!!ntaTributoISS && !!ntaTributoISS.aliquota.categoriaTributo.cidade) {
                    let cidadeTributoISS = ntaTributoISS.aliquota.categoriaTributo.cidade;
                    $scope.stringCidadeIss = '' + cidadeTributoISS.uf + '/' + cidadeTributoISS.nome + '';
                } else {
                    $scope.stringCidadeIss = '';
                }
            }
        }

        $scope.getTotalPercentualTributo = function() {
            let total = 0;

            angular.forEach($scope.listaNotaTributoAliquota, function(obj, key) {

                let varPercentual = obj.aliquota.percentual;

                if (angular.isDefined(varPercentual) && varPercentual !== undefined) {
                    total += Number(varPercentual);
                }

            });

            return total;
        };

        function setValorTributo() {
            let valor = 0;

            if ($scope.object.issRetidoCliente) {
                valor = $scope.totalValorTributo;
            } else {
                valor = $scope.subTotalValorTributo;
            }
            $scope.object.valorTributos = valor;
            $scope.valorTributosView = valor;
        };

        /**
         * Método para verificar se a cidade do tributo ISS é diferente da cidade do "Contrato" OU da cidade do "Local de prestação dos serviços".
         * @return TRUE - caso tenha tributo ISS e a cidade do tributo seja diferente das duas cidades citadas acima.
         * @return FALSE - caso não haja tributo ISS OU caso a cidade do tributo seja igual a uma das duas cidades citadas acima.
         */
        function verificaCidadesDivergentes() {

            if (!CalculadorFaturamento.isCampoVazio($scope.listaFaturamentoAliquota) && $scope.listaFaturamentoAliquota.length > 0) {
                for (let i = 0; i < $scope.listaFaturamentoAliquota.length; i++) {
                    let categoriaTributo = $scope.listaFaturamentoAliquota[i].aliquota.categoriaTributo;

                    if (categoriaTributo.tributoTO.codigo === 4) {

                        if (angular.isDefined($scope.object.cidadePrestacaoServico) && $scope.object.cidadePrestacaoServico != undefined) {
                            if (categoriaTributo.cidade.nome == $scope.object.cidadePrestacaoServico.nome) {
                                return false;
                            }
                        }

                        if (angular.isDefined($scope.cidadeDoContrato) && $scope.cidadeDoContrato != undefined) {
                            if (categoriaTributo.cidade.nome == $scope.cidadeDoContrato.nome) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
            return false;
        };

        //Médoto para ordenar a lista de faturamento aliquota no frontend.
        function organizarNotaTributoAliquota() {
            $scope.listaNotaTributoAliquota.sort(function(a, b) {
                if (a.aliquota.categoriaTributo.tributoTO.codigo > b.aliquota.categoriaTributo.tributoTO.codigo) {
                    return -1;
                }
                if (a.aliquota.categoriaTributo.tributoTO.codigo < b.aliquota.categoriaTributo.tributoTO.codigo) {
                    return 1;
                }
                return 0;
            });
        }

        $scope.carregarComboFormaPagamento();
        $scope.carregarComboNatureza();
        $scope.changeTitulo();

        $scope.openNewTargetFaturamento = function() {
            $window.open('/sgi/nv/app/main.html#!/faturamento/' + $scope.object.faturamentoEtapaTO.faturamento.codigo + '/exibir');
        };

        function definirEmpresa() {
            if ($scope.object.empresa == undefined) {
                $scope.object.empresa = $scope.empresaList[1];
            }
        }

        function montarNotaTributoAliquota(faturamentoAliquota, etapa) {
            let objNotaTributoAliquota = {};

            //TODO: O valor para o cálculo do tributo tem que vim da etapa e não do faturamento.
            objNotaTributoAliquota.nota = $scope.object;
            objNotaTributoAliquota.percentual = faturamentoAliquota.aliquota.percentual;
            objNotaTributoAliquota.aliquota = faturamentoAliquota.aliquota;
            objNotaTributoAliquota.baseCalculo = etapa.valor;
            objNotaTributoAliquota.valor = (etapa.valor * faturamentoAliquota.aliquota.percentual) / 100;

            if (faturamentoAliquota.aliquota && faturamentoAliquota.aliquota.categoriaTributo && faturamentoAliquota.aliquota.categoriaTributo.tributoTO.id === 4) {
                    objNotaTributoAliquota.valor = NotasFiscaisService.calculaValorTributo(
                        objNotaTributoAliquota.baseCalculo,
                        faturamentoAliquota.aliquota.categoriaTributo,
                        objNotaTributoAliquota.aliquota.categoriaTributo.cidade.id);
            }

            return objNotaTributoAliquota;
        }

        // Cria uma lista de nota tributo aliquota, para ser apresentado na aba tributo.
        // Apenas para exibição no modo NEW.
        function carregarTributosParaModoNovo(etapa) {
            return new Promise(
                function(resolve, reject) {
                    let params = {};
                    let obj = {};

                    obj.codigoFaturamento = etapa.faturamento.codigo;

                    $scope.totalPercentualTributo = 0;

                    FaturamentoAliquota.page(params, obj).$promise.then(function(data) {

                        let tempListaFaturamentoAliquota = data.content;

                        let contagemTributos = 0;

                        angular.forEach(tempListaFaturamentoAliquota, function(value, key) {
                            CategoriaTributo.get({id: value.aliquota.codigoCategoriaTributo}).$promise.then(function(data2) {
                                    value.aliquota.categoriaTributo = data2;
                                    let novoNotaTributoAliquota = montarNotaTributoAliquota(value, etapa);

                                    $scope.listaNotaTributoAliquota.push(novoNotaTributoAliquota);

                                    organizarNotaTributoAliquota();
                                    $scope.gerenciarVariaveisDaTabelaTributos();

                                    contagemTributos++;

                                    if (tempListaFaturamentoAliquota.length === contagemTributos) {
                                        verificarRetencao($scope.listaNotaTributoAliquota);
                                    }
                                }).catch(function(response2) {
                                    $rootScope.loadingProgress = false;
                                    console.error(response2);
                                    $scope.$emit('crud-critical', 'load', response2);
                                });
                        });
                        resolve(true);
                    }).catch(function(response) {
                        console.error(response);
                        $scope.$emit('crud-critical', 'load', response);
                        reject(false);
                    });
                });
        }

        function DialogControllerPessoa($scope, $mdDialog, pessoa, tipo) {
            $scope.pessoa = pessoa;
            $scope.pessoa.tipoPessoaJuridica = tipo;
            $scope.fechar = function() {
                $mdDialog.cancel();
            };
        }

        $scope.exibirDetalheCliente = function(codigo, tipoPessoa, tipo, ev) {
            if (codigo == undefined) {
                return;
            }
            let useFullScreen = true;

            if (tipo == undefined) {
                tipo = 'ninformado';
            }

            $scope.pessoa = PessoaJuridica.popupFaturamento({
                id: codigo,
                tipo: tipoPessoa,
                idCl: $scope.object.faturamentoEtapaTO.faturamento.centroFinanceiro.codigo
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

        $scope.alterarValor = function(ev) {
            $scope.copia = angular.copy($scope.object);

            $mdDialog.show({
                controller: DialogAlterarValor,
                locals: {
                    nota: $scope.copia,
                    notification: $scope.notification
                },
                templateUrl: 'partials/faturamento/notasFiscais/html/partials/dialog_alterar_valor.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        function DialogAlterarValor($scope, $mdDialog, $rootScope, nota, notification) {

            $scope.object = nota;
            $scope.notification = notification;

            $scope.hide = function() {
                $mdDialog.hide();
            };

            $scope.cancel = function() {
                $mdDialog.cancel();
            };

            $scope.cancel = function() {
                $mdDialog.cancel();
            };

            $scope.confirm = function() {
                $rootScope.loadingProgress = true;
                $scope.action = true;

                NotaFiscalFaturamento.alterarValorBruto($scope.object).$promise.then(function(data) {
                    $rootScope.loadingProgress = false;
                    $scope.notification.success(undefined, "Valor da nota fiscal ajustado com sucesso.");
                    $scope.screenMode = 'VIEW';
                    $scope.objectId = $scope.object.codigo;
                    loadObj();
                    $mdDialog.hide();
                    $scope.action = false;
                }).catch(function(response) {
                    $rootScope.loadingProgress = false;
                    $scope.action = false;
                    $scope.$emit('crud-critical', 'search', response);
                    $scope.notification.criticalError(response.status, response.statusText, response.data);
                });
            };
        }

        /* ABA TRIBUTOS */
        //Listas
        $scope.listSN = [{
            nome: 'Não',
            valor: false
        }, {
            nome: 'Sim',
            valor: true
        }];

        $scope.variacaoTributo = 0;

        initVariacaoTributo();

        $scope.$on('comboIssRetido', function() {
            setValorTributo();
            $scope.issRetidoCliente = $scope.object.issRetidoCliente;
            $scope.atualizarDiscriminacaoServico();
        });

        $scope.mudarCidUnidade = function(paramCidade) {
            $scope.cidUnidade = paramCidade;

            for (let i = 0; i < $scope.listaNotaTributoAliquota.length; i++) {
                let codigoTributo = $scope.listaNotaTributoAliquota[i].aliquota.categoriaTributo.tributoTO.codigo;

                if (codigoTributo === 4) {
                    $scope.excluirNotaTributoAliquota(undefined, $scope.listaNotaTributoAliquota[i], true);
                }
            }
        };

        $scope.desabilitarComboISSRetidoCliente = function() {
            if ($scope.temTributoIss() === false)
                return true;
            return false;
        };

        $scope.getCidadeISS = function() {

            let varCidadeIss = undefined;

            angular.forEach($scope.listaNotaTributoAliquota, function(obj, key) {

                let codigoTributo = obj.aliquota.categoriaTributo.tributoTO.codigo;

                if (codigoTributo === 4) {
                    varCidadeIss = angular.copy(obj.aliquota.categoriaTributo.cidade);
                }

            });

            return varCidadeIss;
        };

        let procurarCidadeUnidadeContrato = function() {
            let objCidadeRj = undefined;

            // Variavel para indicar que o faturamento tem serviço na cidade do Rio de Janeiro/RJ.
            let temServicoRj = false;

            if ($scope.object.servicoTipo && $scope.object.servicoTipo.sigla && $scope.object.servicoTipo.sigla.length >= 2) {
                let numeroInicialServico = $scope.object.servicoTipo.sigla.substring(0, 2);

                if (numeroInicialServico === '14') {
                    temServicoRj = true;
                    objCidadeRj = {
                        cidadeUf: 'RIO DE JANEIRO/RJ',
                        codigo: 3304557,
                        id: 3304557,
                        nome: 'RIO DE JANEIRO',
                        uf: 'RJ'
                    };
                }
            }

            let centroFinanceiro = $scope.object.faturamentoEtapaTO.faturamento.centroFinanceiro;
            let contratoTO = centroFinanceiro.contratoTO;

            //BUSCA A CIDADE DA UNIDADE DO CONTRATO
            return $q.all(
                Contrato.cidadeUnidade(contratoTO).$promise.then(function(objCidade) {
                    $scope.cidadeUnidadeContrato = objCidade;
                    $scope.cidadeDoContrato = angular.copy(objCidade);

                    let idsDasCidades = new Set();
                    idsDasCidades.add($scope.cidadeUnidadeContrato.codigo);

                    if ($scope.object.faturamentoEtapaTO.faturamento.cidadePrestacaoServico != undefined) {
                        idsDasCidades.add($scope.object.faturamentoEtapaTO.faturamento.cidadePrestacaoServico.codigo);
                    }

                    if (temServicoRj) {
                        idsDasCidades.add(objCidadeRj.codigo);
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

                        if ($scope.object.faturamentoEtapaTO.faturamento.cidadePrestacaoServico != undefined && !cidadesNaLista.has($scope.object.faturamentoEtapaTO.faturamento
                                .cidadePrestacaoServico.codigo)) {
                            let cidade = {
                                objeto: $scope.object.faturamentoEtapaTO.faturamento.cidadePrestacaoServico,
                                texto: '' + $scope.object.faturamentoEtapaTO.faturamento.cidadePrestacaoServico.uf + '/' + $scope.object.faturamentoEtapaTO.faturamento
                                    .cidadePrestacaoServico.nome + ''
                            };

                            listaCidadesISS.push(cidade);

                            cidadesNaLista.add($scope.object.faturamentoEtapaTO.faturamento.cidadePrestacaoServico.codigo);

                            $scope.cidUnidade = cidade.objeto;
                        }

                        let ntaTributoISS = $scope.listaNotaTributoAliquota.filter(nta => nta.aliquota.categoriaTributo.tributoTO.nome === 'ISS')[0];

                        if (!!ntaTributoISS && !!ntaTributoISS.aliquota.categoriaTributo.cidade) {
                            let cidadeTributoISS = ntaTributoISS.aliquota.categoriaTributo.cidade;

                            if (cidadesNaLista.has(cidadeTributoISS.codigo)) {
                                let objetoCidade = listaCidadesISS.filter(item => !!item.objeto && item.objeto.codigo === cidadeTributoISS.codigo)[0];
                                $scope.cidUnidade = objetoCidade.objeto;

                            } else {
                                let objetoCidade = {
                                    objeto: cidadeTributoISS,
                                    texto: '' + cidadeTributoISS.uf + '/' + cidadeTributoISS.nome + ''
                                };

                                listaCidadesISS.push(objetoCidade);
                                cidadesNaLista.add(cidadeTributoISS.codigo);

                                $scope.cidUnidade = cidadeTributoISS;
                            }
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

                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'load', response);
                }),

                Contrato.cidadeUnidadeMatriz(contratoTO).$promise.then(function(objCidade) {
                    $scope.cidadeDaUnidadeMatrizDoContrato = angular.copy(objCidade);
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'load', response);
                })
            );
        };

        $scope.editarTributos = function() {
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
            $scope.modeTributos = true;
            $rootScope.loadingProgress = false;
            $scope.originalListaNotaTributoAliquota = angular.copy($scope.listaNotaTributoAliquota);

            procurarCidadeUnidadeContrato();
            $scope.cidUnidade = $scope.getCidadeISS();
        };

        $scope.salvarTributos = function(form) {
            $rootScope.loadingProgress = true;
            $timeout(function() {
                form.$setDirty();
                form.$setUntouched();
                form.$setPristine();
                $rootScope.loadingProgress = false;

                if ($scope.original.issRetidoCliente !== $scope.object.issRetidoCliente) {
                    $scope.issRetidoCliente = $scope.object.issRetidoCliente;
                }

                $scope.object.dataEmissao = $scope.original.dataEmissao;
                $scope.object.dataVencimento = $scope.original.vencimento;
                $scope.object.vencimento = $scope.original.vencimento;
                $scope.object.editTributos = $scope.modeTributos;
                $scope.atualizarDiscriminacaoServico();
                $scope.salvarNotaFiscal();
                $scope.modeTributos = false;
                $scope.screenMode = 'VIEW';
                $scope.changeTitulo();

            }, 300);
        };

        $scope.cancelarTributos = function(form) {
            $rootScope.loadingProgress = true;
            $timeout(function() {
                $scope.object = angular.copy($scope.original);
                $scope.screenMode = 'VIEW';
                $scope.changeTitulo();
                form.$setDirty();
                form.$setUntouched();
                form.$setPristine();
                $scope.modeTributos = false;
                $rootScope.loadingProgress = false;
                $scope.listaNotaTributoAliquota = angular.copy($scope.originalListaNotaTributoAliquota);
                $scope.originalListaNotaTributoAliquota = undefined;
                $scope.gerenciarVariaveisDaTabelaTributos();
            }, 300);
        };

        $scope.excluirNotaTributoAliquota = function(ev, notaTributoAliquota, isISS) {
            let index = $scope.listaNotaTributoAliquota.indexOf(notaTributoAliquota);
            $scope.listaNotaTributoAliquota.splice(index, 1);
            $scope.gerenciarVariaveisDaTabelaTributos();

            if ($scope.temTributoIss() === false) {
                $scope.object.issRetidoCliente = false;
                $scope.comboCidadeIssDesabilitado = false;
            }
        };

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

        $scope.novoNotaTributoAliquota = function(ev, isISS) {
            $mdDialog.show({
                controller: DialogControllerEditarNotaTributoAliquota,
                locals: {
                    isNew: true,
                    notaTributoAliquota: undefined,
                    listaNotaTributoAliquota: $scope.listaNotaTributoAliquota,
                    isISS: isISS,
                    Tributo: Tributo,
                    CategoriaTributo: CategoriaTributo,
                    faturamento: $scope.object.faturamentoEtapaTO,
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

        $scope.novoNotaTributoAliquotaISS = function(ev, isISS) {

            if ($scope.cidadesIguais || (!$scope.cidadesIguais && $scope.cidUnidade != undefined)) {
                $scope.novoNotaTributoAliquota(ev, isISS);
            } else {
                $scope.notification.atencao("Atenção", "Escolha uma cidade no campo 'Cidade ISS'");
            }

        };

        $scope.editarNotaTributoAliquota = function(ev, notaTributoAliquota) {

            //Verifica se o tributo é ISS.
            if (notaTributoAliquota.aliquota.categoriaTributo.tributoTO.codigo == 4) {
                $scope.cidUnidade = angular.copy(notaTributoAliquota.aliquota.categoriaTributo.cidade);
            }

            $mdDialog.show({
                controller: DialogControllerEditarNotaTributoAliquota,
                locals: {
                    isNew: false,
                    notaTributoAliquota: notaTributoAliquota,
                    NotasFiscaisService: NotasFiscaisService,
                    listaNotaTributoAliquota: $scope.listaNotaTributoAliquota,
                    isISS: false,
                    Tributo: Tributo,
                    CategoriaTributo: CategoriaTributo,
                    faturamento: $scope.object.faturamentoEtapaTO,
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

        let getPercentualAliquota = (argCategoriaTributo) => {
            if (argCategoriaTributo && argCategoriaTributo.aliquotas && argCategoriaTributo.aliquotas[0] &&
                argCategoriaTributo.aliquotas[0].percentual) {
                return argCategoriaTributo.aliquotas[0].percentual;
            }
            return 0.00;
        };

        $scope.permitirZerarNotaTributoAliquota = function(paramNotaTributoAliquota) {
            let resultado = false;

            if (paramNotaTributoAliquota) {
                if (paramNotaTributoAliquota.valor > 0) {

                    if (paramNotaTributoAliquota.valor < paramNotaTributoAliquota.aliquota.categoriaTributo.minimoGuia) {
                        resultado = true;
                    }

                } else {
                    let valorOriginal = paramNotaTributoAliquota.baseCalculo * (getPercentualAliquota(paramNotaTributoAliquota.aliquota.categoriaTributo) / 100);

                    if (valorOriginal < paramNotaTributoAliquota.aliquota.categoriaTributo.minimoGuia) {
                        resultado = true;
                    }
                }
            }

            return resultado;
        };

        $scope.zerarTributo = function(ev, paramNotaTributoAliquota) {
            let listaTributosParaRetencao = [];
            listaTributosParaRetencao.push(paramNotaTributoAliquota);
            gerenciarRetencaoTributo(ev, listaTributosParaRetencao, $scope.gerenciarVariaveisDaTabelaTributos, $scope.atualizarDiscriminacaoServico, $scope.atualizarAbaParcela, $scope.object.formaPagamento);
        };

        // Para o caso de zerar um tributo na aba de tributos.
        $scope.atualizarAbaParcela = () => {
            if ($scope.object.notaSaidaParcelas && $scope.object.notaSaidaParcelas.length > 0) {
                let primeiraParcela = $scope.object.notaSaidaParcelas[0];

                if ($scope.object.issRetidoCliente) {
                    primeiraParcela.valorTributo = $scope.getTotalValorTributos();
                } else {
                    primeiraParcela.valorTributo = $scope.getSubtotalValorTributos();
                }

                primeiraParcela.valorLiquido = primeiraParcela.valorBruto - primeiraParcela.valorTributo;
                $scope.totalValorLiquido();
                $scope.totalValorTributos();
            }
        };

        //fim - carregamento da tela no screenMode NEW

        $scope.$on('changeEmissao', function() {
            if (!CalculadorFaturamento.isCampoVazio($scope.object) && !CalculadorFaturamento.isCampoVazio($scope.object.dataEmissao)) {
                if (!CalculadorFaturamento.isCampoVazio($scope.object.notaSaidaParcelas) && !CalculadorFaturamento.isCampoVazio($scope.object.notaSaidaParcelas.length)) {
                    if ($scope.object.notaSaidaParcelas.length > 0) {
                        $scope.object.notaSaidaParcelas[0].minDate = new Date($scope.object.dataEmissao);
                    }
                }

                if ($scope.object && $scope.object.faturamentoEtapaTO && $scope.object.faturamentoEtapaTO.faturamento &&
                    $scope.object.faturamentoEtapaTO.faturamento.tipoCobrancaFaturamento && $scope.object.faturamentoEtapaTO.faturamento.tipoCobrancaFaturamento.codigo &&
                    $scope.object.faturamentoEtapaTO.faturamento.tipoCobrancaFaturamento.tipoCobrancaVencimentos && $scope.object.faturamentoEtapaTO.faturamento.tipoCobrancaFaturamento
                    .tipoCobrancaVencimentos.length > 0) {
                    if ($scope.screenMode !== 'VIEW') {
                        ajustarDataVencimento();
                    }
                }
            }
        });

        $scope.associarNfSubstituta = function(ev) {
            $mdDialog.show({
                controller: function($scope, $mdDialog, $rootScope, nota, notification, NotaFiscalFaturamento, loadObjFN) {
                    $scope.numero;
                    $scope.selectedNota;
                    $scope.searchTextNumero;
                    if (nota.notaAssociadaCancelamento) {
                        $scope.numero = nota.notaAssociadaCancelamento.numero;
                        $scope.searchTextNumero = nota.notaAssociadaCancelamento.numero;
                    }
                    $scope.object = nota;
                    $scope.notification = notification;

                    $scope.selectedItemChange = function(item) {
                        if (angular.isDefined(item)) {
                            $scope.selectedNota = item;
                        }
                    };

                    $scope.querySearchNumero = function(query) {
                        $rootScope.loadingProgress = true;
                        let obj = {};
                        obj.numero = query.replace('NF/', '');
                        obj.codigoFaturamento = nota.faturamentoEtapaTO.faturamento.codigo;

                        let resultado = NotaFiscalFaturamento.pesquisaPorNumeroAutoComplete(undefined, obj).$promise;

                        return resultado.then(function(data) {
                            $rootScope.loadingProgress = false;
                            return data;
                        }).catch(function(response) {
                            $scope.$emit('crud-critical', 'search', response);
                            return [];
                        });
                    };

                    $scope.hide = function() {
                        $mdDialog.hide();
                    };

                    $scope.cancel = function() {
                        $mdDialog.cancel();
                    };

                    $scope.confirm = function() {
                        $rootScope.loadingProgress = true;
                        $scope.action = true;

                        let nf = {};
                        nf.codigo = nota.codigo;
                        nf.codigoNotaAssociadaCancelamento = $scope.selectedNota === undefined ? nota.notaAssociadaCancelamento.codigo : $scope.selectedNota.codigo;

                        NotaFiscalFaturamento.atualizarAssociacaoNfSubstituta(undefined, nf).$promise.then(function(data) {
                            $rootScope.loadingProgress = false;
                            $scope.notification.success(undefined, "Associar NF substituta com sucesso.");
                            loadObjFN();
                            $mdDialog.hide();
                            $scope.action = false;
                        }).catch(function(response) {
                            $rootScope.loadingProgress = false;
                            $scope.action = false;
                            $scope.$emit('crud-critical', 'search', response);
                            $scope.notification.criticalError(response.status, response.statusText, response.data);
                        });
                    };
                },
                locals: {
                    nota: angular.copy($scope.object),
                    notification: $scope.notification,
                    NotaFiscalFaturamento: NotaFiscalFaturamento,
                    loadObjFN: loadObj
                },
                templateUrl: 'partials/faturamento/notasFiscais/html/partials/dialog_associar_nf_substituta.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        $scope.abasParcela = function() {
            $scope.index = 5;
        };

        if ($scope.screenMode === 'VIEW') {
            loadObj();
        } else if ($scope.screenMode === 'NEW') {
            if (!CalculadorFaturamento.isCampoVazio($scope.objectIdEtapa)) {
                $rootScope.loadingProgress = true;
                let filtro = {};
                filtro.faturamentoEtapaTO = {};
                filtro.faturamentoEtapaTO.codigo = $scope.objectIdEtapa;
                NotaFiscalFaturamento.create(filtro).$promise.then(function(data) {
                    $scope.carregamentoBasicoNotaFiscal(data).then(function() {
                    $scope.object.tipoFornecimento = data.tipoFornecimento
                    if ($scope.object.tipoFornecimento === 'Só material') {
                    	$scope.object.servicoTipo = null
                    }
                        // Montando a aba de Tributos
                        carregarFaturamentoAliquotas($scope.object.faturamentoEtapaTO.faturamento.codigo)
                            .then(() => carregarTributosParaModoNovo($scope.object.faturamentoEtapaTO))
                            .then(() => Contrato.cidadeUnidade(undefined, $scope.object.faturamentoEtapaTO.faturamento.centroFinanceiro.contratoTO).$promise)
                            .then(function(data2) {
                                $scope.contratoCidade = data2.nome + "/" + data2.uf;
                                return procurarCidadeUnidadeContrato();
                            }).then(() => {
                                $scope.cidUnidade = $scope.getCidadeISS();
                                $scope.object.empresa = angular.copy(data.empresa);
                                $scope.buscarEmpresas(true, definirEmpresa).then(() => {
                                    $scope.montarParcelasTipoCobranca($scope.object.faturamentoEtapaTO.faturamento);
                                    $scope.bloquearEmissaoNF = $scope.validarCidadeFaturamento($scope.object.faturamentoEtapaTO.faturamento);
                                    if ($scope.bloquearEmissaoNF) {
                                        $scope.notification.atencao(undefined, "Este faturamento não possui cidade para faturamento. Não é possível emitir a nota fiscal.");
                                    }
                                    // Carregar sempre como "Parcela única" SGINV-2052.
                                    $scope.object.formaPagamento = "Parcela única";
                                    ajustarDataVencimento();
                                    $scope.atualizarDiscriminacaoServico();
                                });
                            });
                    });
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'load', response);
                    $rootScope.loadingProgress = false;
                });

            } else {
                $scope.buscarEmpresas(false, undefined);
            }
        } else {
            $scope.buscarEmpresas(false, undefined);
        }
    }
})();
