(function () {
	"use strict";

	angular.module('app.contasareceber').controller('ContasAReceberImpressaoCtrl', contasAReceberImpressaoCtrlFn);

	function contasAReceberImpressaoCtrlFn($scope, $rootScope, $state, $timeout, $mdDialog, $q, CalculadorFaturamento, Parcela, NotaTributoAliquota, MotivoRetornoParcela, Contrato, CategoriaTributo, ServicoAbaTributosCAR, ParcelaService) {
		function inicializa() {
			$scope.modulo = $state.current.data.modulo;
			$scope.titulo = $state.current.data.titulo;
			$scope.subtitulo = $state.current.data.subtitulo;

			$scope.listaTributos = [];

			$scope.Number = Number;

			$scope.object = {};

			$scope.valorIRRF = 0;
			$scope.valorPIS = 0;
			$scope.valorCOFINS = 0;
			$scope.valorCSLL = 0;
			$scope.valorISS = 0;
			$scope.valorINSS = 0;

			$scope.tipoDocumento = 'CNPJ';
			$scope.finalDoDocumento = '';
		}

		inicializa();

		function loadObj() {
			return $q(function (resolve, reject) {

				if (!CalculadorFaturamento.isCampoVazio($scope.objectId)) {

					$scope.action = true;

					Parcela.get({id: $scope.objectId}).$promise.then(function (data) {

						$scope.object = data;
						
						$scope.object.ordem = CalculadorFaturamento.formataNumero($scope.object.ordem, 2);

						let promises = {};

						//Se for a primeira parcela coloca a função carregaTributos no objeto PROMISES para se executada
						if ($scope.object.ordem == '01') {
							promises.tributo = carregaTributos($scope.object.notaSaida);
						}

						let listaRetencoes = [];

						if (!CalculadorFaturamento.isCampoVazio($scope.object.parcelaRetencoes)) {
							listaRetencoes = $scope.object.parcelaRetencoes;
						}

						promises.retencao = calcularValorRetencoesParcela($scope.object, listaRetencoes);

						$q.all(promises).then(function (values) {
							if (angular.isDefined(values.tributo)) {
								$scope.listaTributos = values.tributo.arrayTributos;
								$scope.valorTributos = calcularValorTributos($scope.object, $scope.listaTributos);

							} else {
								$scope.listaTributos = [];
								$scope.valorTributos = 0.00;
							}

							if (angular.isDefined(values.retencao)) {
								$scope.percentualRetencaoContratual = values.retencao.result.percentualContratual;
								$scope.object.valorRetencaoContratual = values.retencao.result.valorContratual;
								$scope.percentualRetencaoAcessoria = values.retencao.result.percentualAcessoria;
								$scope.object.valorRetencaoAcessoria = values.retencao.result.valorAcessoria;
							}
						}).catch(function (erro) {
							console.log('erro no all promises');
							console.log(erro);
						});

						if ($scope.object.situacao == 'Finalizada (recebida)' || $scope.screenMode == 'RECEBER') {
							$scope.object.contaCorrente = $scope.object.contaCorrenteRecebimento;
							$scope.object.bancoSelecionado = $scope.object.contaCorrenteRecebimento.banco;
							$scope.object.tipoDocumento = $scope.object.tipoDocumentoRecebimento;
						} else {
							$scope.object.contaCorrente = $scope.object.contaCorrentePrevista;
							$scope.object.bancoSelecionado = $scope.object.contaCorrentePrevista.banco;
							$scope.object.tipoDocumento = $scope.object.tipoDocumentoPrevisto;
						}

						montarLabelCentroLucro($scope.object.notaSaida.faturamentoEtapaTO.faturamento.centroFinanceiro);
						$scope.object.dataVencimento = new Date($scope.object.dataVencimento);
						$scope.object.dataPagamento = new Date($scope.object.dataPagamento);

						if ($scope.object.dataContabilizacao) {
							$scope.object.dataContabilizacao = new Date($scope.object.dataContabilizacao);
						}

						$scope.original = angular.copy($scope.object);

						carregarDadosCancelamento();

						$scope.action = false;

						resolve({
							mensagem: 'Pesquisa da parcela realizada com sucesso!',
							executou: true
						});

						if (!$scope.original.observacoesImpressao) {
							$scope.observacoes(null);
						}

						definirDocumento($scope.object);
					}).catch(function (response) {
						reject({
							mensagem: 'Não foi possível realizar a pesquisa da parcela!',
							executou: false
						});
						$scope.$emit('crud-critical', 'load', response);
					});
				}

			});
		}; //fim da função loadObj

		function carregaTributos(nota) {

			return $q(function (resolve, reject) {

				let arrayTributos = [];

				let obj = {};
				obj.nota = {};
				obj.nota.codigo = nota.codigo;

				let params = {};

				$rootScope.loadingProgress = true;

				NotaTributoAliquota.page(params, obj)
					.$promise.then(function (data) {
						$rootScope.loadingProgress = false;
						arrayTributos = data.content;

						buscarCidades(nota, arrayTributos);

						resolve({
							mensagem: 'Pesquisa dos tributos foi atualizada com sucesso!',
							arrayTributos: arrayTributos
						});

					}).catch(function (response) {
						$scope.$emit('crud-critical', 'load', response);
						reject({
							mensagem: 'Não foi possível realizar a pesquisa de tributos!',
							arrayTributos: arrayTributos
						});
					});

			});
		}

		function calcularValorRetencoesParcela(parcela, listaRetencoes) {

			return $q(function (resolve, reject) {

				let result = {
					percentualContratual: 0,
					valorContratual: 0,
					percentualAcessoria: 0,
					valorAcessoria: 0
				};

				if (!CalculadorFaturamento.isCampoVazio(parcela) &&
					!CalculadorFaturamento.isCampoVazio(parcela.valorBruto) &&
					!CalculadorFaturamento.isCampoVazio(listaRetencoes)) {

					angular.forEach(listaRetencoes, function (value, key) {
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

		}

		function montarLabelCentroLucro(centroFinanceiro) {
			if (!CalculadorFaturamento.isCampoVazio(centroFinanceiro) && !CalculadorFaturamento.isCampoVazio(centroFinanceiro.identificadorFinal) && !CalculadorFaturamento.isCampoVazio(centroFinanceiro.nome)) {
				$scope.object.centroLucro = centroFinanceiro.identificadorFinal + " - " + centroFinanceiro.nome;
			}
		}

		function carregarDadosCancelamento() {
			$rootScope.loadingProgress = true;
			MotivoRetornoParcela.page(null, {
					notaSaidaParcela: {
						codigo: $scope.objectId
					}
				})
				.$promise.then(function (data) {
					$rootScope.loadingProgress = false;
					$scope.listMotivos = data.content;
				}).catch(function (response) {
					$rootScope.loadingProgress = false;
					$scope.$emit('crud-critical', 'search', response);
				});
		}

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
		function buscarCidades(notaSaida, arrayTributos) {

			$scope.cidadePrestacaoServico = angular.copy(notaSaida.faturamentoEtapaTO.faturamento.cidadePrestacaoServico);

			let promessa2 = Contrato.cidadeUnidade(notaSaida.faturamentoEtapaTO.faturamento.centroFinanceiro.contratoTO).$promise;

			let notaTributoAliquotas = angular.copy(arrayTributos);
			let promises = [];

			angular.forEach(notaTributoAliquotas, function (value, key) {
				promises.push(CategoriaTributo.get({
					id: value.aliquota.codigoCategoriaTributo
				}).$promise);
			});

			$q.all(promises).then(function (lista) {

				angular.forEach(lista, function (value, key) {
					if (value.tributoTO.codigo == 4) {
						$scope.cidadeTributoIss = value.cidade;
					}
				});

				return promessa2;
			}).then(function (data) {
				$scope.cidadeContrato = data;
			}).catch(function (response) {
				$scope.$emit('crud-critical', 'load', response);
			});

		}

		function calcularValorTributos(parcela, listaTributos) {
			let varValorTributos = 0;

			if (!CalculadorFaturamento.isCampoVazio(parcela) && !CalculadorFaturamento.isCampoVazio(parcela.notaSaida) &&
				!CalculadorFaturamento.isCampoVazio(parcela.notaSaida.faturamentoEtapaTO) &&
				!CalculadorFaturamento.isCampoVazio(parcela.notaSaida.faturamentoEtapaTO.faturamento)) {

				if (parcela.notaSaida.issRetidoCliente) {
					varValorTributos = ServicoAbaTributosCAR.getTotalValorTributos(listaTributos);
				} else {
					varValorTributos = ServicoAbaTributosCAR.getSubtotalValorTributos(listaTributos);
				}
			}

			$scope.valorIRRF = ServicoAbaTributosCAR.getValorTributoIRRF(listaTributos);
			$scope.valorISS = ServicoAbaTributosCAR.getValorTributoISS(listaTributos);
			$scope.valorINSS = ServicoAbaTributosCAR.getValorTributoINSS(listaTributos);

			buscarValorPisConfisCsll(listaTributos);

			return varValorTributos;
		}

		function buscarValorPisConfisCsll(listaTributos) {

			let objNotaTributoAliquota = null;

			angular.forEach(listaTributos, function (item, key) {
				let codigoTributo = item.categoriaTributo.tributoTO.codigo;
				if (codigoTributo === 1) {
					objNotaTributoAliquota = angular.copy(item);
				}
			});

			if (objNotaTributoAliquota) {
				let percentual = objNotaTributoAliquota.percentual;

				switch (percentual) {

					case 0.65:
						// pis
						$scope.valorPIS = ServicoAbaTributosCAR.getValorTributoPIS(listaTributos);
						$scope.valorCOFINS = 0;
						$scope.valorCSLL = 0;
						break;
					case 3:
						// cofins
						$scope.valorPIS = 0;
						$scope.valorCOFINS = ServicoAbaTributosCAR.getValorTributoPIS(listaTributos);
						$scope.valorCSLL = 0;
						break;
					case 1:
						// csll
						$scope.valorPIS = 0;
						$scope.valorCOFINS = 0;
						$scope.valorCSLL = ServicoAbaTributosCAR.getValorTributoPIS(listaTributos);
						break;
					case 3.65:
						// PIS + CONFINS
						$scope.valorPIS = (objNotaTributoAliquota.baseCalculo * 0.65) / 100;
						$scope.valorCOFINS = (objNotaTributoAliquota.baseCalculo * 3) / 100;
						$scope.valorCSLL = 0;
						break;
					case 1.65:
						// PIS + CSLL
						$scope.valorPIS = (objNotaTributoAliquota.baseCalculo * 0.65) / 100;
						$scope.valorCOFINS = 0;
						$scope.valorCSLL = (objNotaTributoAliquota.baseCalculo * 1) / 100;
						break;
					case 4:
						// CONFINS + CSLL
						$scope.valorPIS = 0;
						$scope.valorCOFINS = (objNotaTributoAliquota.baseCalculo * 3) / 100;
						$scope.valorCSLL = (objNotaTributoAliquota.baseCalculo * 1) / 100;
						break;
					case 4.65:
						// os três tributos
						$scope.valorPIS = (objNotaTributoAliquota.baseCalculo * 0.65) / 100;
						$scope.valorCOFINS = (objNotaTributoAliquota.baseCalculo * 3) / 100;
						$scope.valorCSLL = (objNotaTributoAliquota.baseCalculo * 1) / 100;
						break;
					default:
						$scope.valorPIS = 0;
						$scope.valorCOFINS = 0;
						$scope.valorCSLL = 0;
						break;
				}
			}
		}

		$scope.imprimir = function () {
			$("#papel").removeClass("page_layout_print");
			$("#papel").addClass("page_layout_print_view");
			$rootScope.loadingProgress = true;
			$('html, md-content').animate({
				scrollTop: 0
			}, 400);
			$timeout(function () {
				$rootScope.loadingProgress = false;
				window.print();
				$("#papel").removeClass("page_layout_print_view");
				$("#papel").addClass("page_layout_print");	
			}, 500);
		};

		$scope.observacoes = function (ev) {
			ParcelaService.dialogObservacao(ev, $scope.object, $scope.notification);
		};

		let definirDocumento = (object) => {
			let cliente = object.notaSaida.faturamentoEtapaTO.faturamento.cliente;
			let documento = cliente.documento;

			if (cliente.tipoPessoa === 'Pessoa Jurídica') {
				$scope.tipoDocumento = 'CNPJ';
				$scope.finalDoDocumento = documento.substring(documento.length - 7, documento.length);
			} else {
				$scope.tipoDocumento = 'CPF';
				$scope.finalDoDocumento = documento.substring(documento.length - 6, documento.length);
			}
		};

		loadObj();
	}
	
})();
