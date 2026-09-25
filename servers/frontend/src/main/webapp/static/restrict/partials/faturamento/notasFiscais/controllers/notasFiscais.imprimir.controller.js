(function () {
	'use strict';

	angular.module('app.notaFiscalFaturamento')
		.controller('NotaFiscalImpressaoCtrl', notaFiscalFaturamentoImprimirCtrl);

	function notaFiscalFaturamentoImprimirCtrl($scope, $rootScope, $state, CalculadorFaturamento, NotaFiscalFaturamento, MotivoRetornoNota, Endereco, PessoaJuridica, FaturamentoAliquota, CategoriaTributo, Contrato, $timeout, NotaTributoAliquotaEmissao) {
		$scope.object = {};
		$scope.prefeituraCidadeUF = "";
		var statusParaExibicao = ["Aberta", "Cancelada"];

		$scope.carregarNotaFiscal = function () {

			if (!CalculadorFaturamento.isCampoVazio($scope.objectId)) {
				var promiseNF = NotaFiscalFaturamento.buscarNfImpressao({
					id: $scope.objectId
				}).$promise;
				promiseNF.then(function (data) {

					$scope.object = data;

					$scope.exibindoStatusNF = statusParaExibicao.indexOf($scope.object.status) >= 0 ? $scope.object.status.toUpperCase() : null;
					$scope.valorLiquidoNF = $scope.object.notaSaidaParcelas.reduce(function (accum, elem) {
						return accum + elem.valorLiquido;
					}, 0.0);

					$scope.object.numero = data.numero.replace("NF/", "");

					$scope.object.empresa.endereco = {};
					$scope.object.empresa.endereco.codigoPessoa = $scope.object.empresa.codigo;

					$scope.object.faturamentoEtapaTO.faturamento.cliente.endereco = {};
					$scope.object.faturamentoEtapaTO.faturamento.cliente.endereco.codigoPessoa = $scope.object.faturamentoEtapaTO.faturamento.cliente.codigo;

					$scope.buscarEndereco($scope.object.empresa.endereco);
					$scope.buscarEndereco($scope.object.faturamentoEtapaTO.faturamento.cliente.endereco);
					$scope.buscarPessoaJuridica($scope.object.faturamentoEtapaTO.faturamento.cliente);
					carregarTributos($scope.object.faturamentoEtapaTO.faturamento.codigo, data.codigo);
					$scope.carregarMotivoRetorno(data);

					Contrato.cidadeUnidade(null, $scope.object.faturamentoEtapaTO.faturamento.centroFinanceiro.contratoTO)
						.$promise.then(function (data) {

							$scope.prefeituraCidadeUF = data.nome + "/" + data.uf;

						}).catch(function (response) {
							$rootScope.loadingProgress = false;
							$scope.$emit('crud-critical', 'search', response);
						});

				}).catch(function (response) {
					$scope.$emit('crud-critical', 'load', response);
				});
			}
		};

		$scope.carregarMotivoRetorno = function (resultado) {
			MotivoRetornoNota.page(null, {
					nota: {
						codigo: resultado.codigo
					}
				})
				.$promise.then(function (data) {
					$rootScope.loadingProgress = false;
					$scope.listMotivos = data.content;
				}).catch(function (response) {
					$rootScope.loadingProgress = false;
					$scope.$emit('crud-critical', 'search', response);
				});
		};

		function carregarTributos(codFaturamento, codNota) {
			$rootScope.loadingProgress = true;

			let params = {
				codigoNota: codNota
			};
			let pNotaTributoAliquotaEmissao = NotaTributoAliquotaEmissao.listarPorNota(params, null).$promise;

			let obj = {};
			obj.codigoFaturamento = codFaturamento;
			let pFaturamentoAliquota = FaturamentoAliquota.page({}, obj).$promise;

			let temNotaTributoAliquotaEmissao = false;

			pNotaTributoAliquotaEmissao.then(function (data1) {
				let listaNotaTributoAliquotaEmissao = data1;

				if (listaNotaTributoAliquotaEmissao && listaNotaTributoAliquotaEmissao.length > 0) {
					temNotaTributoAliquotaEmissao = true;
					gerenciarTributos(listaNotaTributoAliquotaEmissao)
				}

				return pFaturamentoAliquota;
			}).then(function (data2) {

				if (!temNotaTributoAliquotaEmissao) {
					gerenciarTributos(data2.content);
				}

			}).catch(function (response) {
				$rootScope.loadingProgress = false;
				$scope.$emit('crud-critical', 'load', response);
				$scope.object.faturamentoEtapas = [];
			});

		}

		function gerenciarTributos(tributos) {

			$scope.object.cofins = 0.00;
			$scope.object.cofinsPerc = 0.00;
			$scope.object.csll = 0.00;
			$scope.object.csllPerc = 0.00;
			$scope.object.pis = 0.00;
			$scope.object.pisPerc = 0.00;
			$scope.object.pcc = 0.00;
			$scope.object.pccPerc = 0.00;
			$scope.object.inss = 0.00;
			$scope.object.inssPerc = 0.00;
			$scope.object.irpj = 0.00;
			$scope.object.irpjPerc = 0.00;
			$scope.object.iss = 0.00;
			$scope.object.issPerc = 0.00;
			$scope.object.totalRetencoes = 0.00;
			$scope.object.totalRetencoesPerc = 0.00;

			$scope.object.faturamentoEtapaTO.faturamento.faturamentoAliquotas = [];
			$scope.object.faturamentoEtapaTO.faturamento.faturamentoAliquotas = tributos;

			if (angular.isDefined($scope.object.faturamentoEtapaTO.faturamento.faturamentoAliquotas)) {
				angular.forEach($scope.object.faturamentoEtapaTO.faturamento.faturamentoAliquotas, function (faturamentoAliquota, key) {
					var filtro = {};

					let objAliquota = null;
					let baseDeCalculo = 0;

					//faturamento aliquota
					if (faturamentoAliquota.aliquota) {
						objAliquota = faturamentoAliquota.aliquota;
						baseDeCalculo = $scope.object.valor;
					} else {
						//nota tributo aliquota emissao
						objAliquota = faturamentoAliquota.aliquota;
						baseDeCalculo = faturamentoAliquota.baseCalculo;
					}

					filtro.codigo = objAliquota.codigoCategoriaTributo;

					CategoriaTributo.page(null, filtro)
						.$promise.then(function (data) {

							objAliquota.categoriaTributo = data.content[0];

							if (angular.isDefined(objAliquota.categoriaTributo) && objAliquota.categoriaTributo.tributoTO.nome === 'PIS/COFINS/CSLL') {
								$scope.object.cofins = (baseDeCalculo * 3.00) / 100;
								$scope.object.cofinsPerc = 3.00;

								$scope.object.csll = (baseDeCalculo * 1.00) / 100;
								$scope.object.csllPerc = 1.00;

								$scope.object.pis = faturamentoAliquota.valor - ($scope.object.cofins + $scope.object.csll);
								$scope.object.pisPerc = 0.65;

								$scope.object.totalRetencoes = $scope.object.totalRetencoes + ($scope.object.cofins + $scope.object.csll + $scope.object.pis);
								$scope.object.totalRetencoesPerc = $scope.object.totalRetencoesPerc + ($scope.object.cofinsPerc + $scope.object.csllPerc + $scope.object.pisPerc);

								$scope.object.pcc = ($scope.object.cofins + $scope.object.csll + $scope.object.pis)
								$scope.object.pccPerc = ($scope.object.cofinsPerc + $scope.object.csllPerc + $scope.object.pisPerc);
							} else if (angular.isDefined(objAliquota.categoriaTributo) && objAliquota.categoriaTributo.tributoTO.nome === 'IRRF') {
								$scope.object.irpj = faturamentoAliquota.valor;
								$scope.object.totalRetencoes = $scope.object.totalRetencoes + $scope.object.irpj;

								$scope.object.irpjPerc = objAliquota.percentual;
								$scope.object.totalRetencoesPerc = $scope.object.totalRetencoesPerc + $scope.object.irpjPerc;
							} else if (angular.isDefined(objAliquota.categoriaTributo) && objAliquota.categoriaTributo.tributoTO.nome === 'INSS') {
								$scope.object.inss = faturamentoAliquota.valor;
								$scope.object.totalRetencoes = $scope.object.totalRetencoes + $scope.object.inss;

								$scope.object.inssPerc = objAliquota.percentual;
								$scope.object.totalRetencoesPerc = $scope.object.totalRetencoesPerc + $scope.object.inssPerc;
							} else if (angular.isDefined(objAliquota.categoriaTributo) && objAliquota.categoriaTributo.tributoTO.nome === 'ISS') {
								$scope.object.iss = faturamentoAliquota.valor;
								$scope.object.totalRetencoes = $scope.object.totalRetencoes + $scope.object.iss;

								$scope.object.issPerc = objAliquota.percentual;
								$scope.object.totalRetencoesPerc = $scope.object.totalRetencoesPerc + $scope.object.issPerc;
							}

						}).catch(function (response) {
							$scope.$emit('crud-critical', 'load', response);
						});

				});

			}

		}

		$scope.buscarEndereco = function (endereco) {
			var filtroEndereco = {};
			filtroEndereco.codigoPessoa = endereco.codigoPessoa;

			var enderecoCompleto = "";

			Endereco.qbePessoa(null, filtroEndereco)
				.$promise.then(function (data) {

					if (!CalculadorFaturamento.isCampoVazio(data.logradouroTipo))
						enderecoCompleto = enderecoCompleto + data.logradouroTipo;

					if (!CalculadorFaturamento.isCampoVazio(data.endereco))
						enderecoCompleto = enderecoCompleto + " " + data.endereco;

					if (!CalculadorFaturamento.isCampoVazio(data.numero))
						enderecoCompleto = enderecoCompleto + " " + data.numero;


					endereco.logradouro = enderecoCompleto;
					endereco.bairro = data.bairro;
					endereco.cidadeUF = data.nomeCidade;
					endereco.cep = data.cep;

				}).catch(function (response) {
					$scope.$emit('crud-critical', 'load', response);
				});
		};

		$scope.buscarPessoaJuridica = function (cliente) {
			var filtroCliente = {};
			filtroCliente.codigo = cliente.codigo;
			filtroCliente.ordenacao = 'documento';

			cliente.inscricaoEstadual = "";
			cliente.inscricaoMunicipal = "";

			PessoaJuridica.page(null, filtroCliente)
				.$promise.then(function (data) {

					if (!CalculadorFaturamento.isCampoVazio(data)) {
						if (!CalculadorFaturamento.isCampoVazio(data.content[0])) {
							if (!CalculadorFaturamento.isCampoVazio(data.content[0].inscricaoEstadual))
								cliente.inscricaoEstadual = data.content[0].inscricaoEstadual;

							if (!CalculadorFaturamento.isCampoVazio(data.content[0].inscricaoMunicipal))
								cliente.inscricaoMunicipal = data.content[0].inscricaoMunicipal;
						}
					}


				}).catch(function (response) {
					$scope.$emit('crud-critical', 'load', response);
				});
		};

		$scope.imprimir = function () {
			$("#controles").css("display", "none");

			$rootScope.loadingProgress = true;
			$('html, md-content').animate({
				scrollTop: 0
			}, 400);
			$timeout(function () {
				$rootScope.loadingProgress = false;
				window.print();
				$("#controles").css("display", "");
			}, 500);
		};

		$scope.voltar = function (ev) {
			if ($rootScope.$lastState.name !== '') {
				$state.go($rootScope.$lastState.name, $rootScope.$lastState.params);
			} else {
				$state.go('faturamento.listar');
			}
		};

		$scope.carregarNotaFiscal();
	}

})();
