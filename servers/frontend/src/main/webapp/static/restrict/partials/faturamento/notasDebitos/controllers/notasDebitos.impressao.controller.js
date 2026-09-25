(function () {
	'use strict';

	angular
		.module('app.notaDebito')
		.controller('NotaDebitoImpressaoCtrl', notaDebitoController);

	function notaDebitoController(
		$scope,
		$rootScope,
		$state,
		CalculadorFaturamento,
		NotaDebito,
		MotivoRetornoNotaDebito,
		Endereco,
		PessoaJuridica,
		NotaTributoAliquota,
		CategoriaTributo,
		Contrato,
		$timeout,
		NotaTributoAliquotaEmissao) {

		$scope.object = {};
		$scope.prefeituraCidadeUF = "";
		var statusParaExibicao = ["Aberta", "Cancelada"];

		$scope.carregarNotaDebito = function () {

			if (!CalculadorFaturamento.isCampoVazio($scope.objectId)) {

				var promiseND = NotaDebito.buscarNdImpressao({
					id: $scope.objectId
				}).$promise;
				promiseND.then(function (data) {

					$scope.object = data;

					$scope.exibindoStatusND = statusParaExibicao.indexOf($scope.object.situacao) >= 0 ? $scope.object.situacao.toUpperCase() : null;
					$scope.valorLiquidoND = $scope.object.valorLiquido;

					$scope.object.numero = data.numero.replace("ND/", "");

					$scope.object.empresa.endereco = {};
					$scope.object.empresa.endereco.codigoPessoa = $scope.object.empresa.codigo;

					$scope.object.faturamentoEtapaTO.faturamento.cliente.endereco = {};
					$scope.object.faturamentoEtapaTO.faturamento.cliente.endereco.codigoPessoa = $scope.object.faturamentoEtapaTO.faturamento.cliente.codigo;

					$scope.buscarEndereco($scope.object.empresa.endereco);
					$scope.buscarEndereco($scope.object.faturamentoEtapaTO.faturamento.cliente.endereco);
					$scope.buscarPessoaJuridica($scope.object.faturamentoEtapaTO.faturamento.cliente);
					carregarTributos(data.codigo);
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
			MotivoRetornoNotaDebito.page(null, {
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
			$("#papel").removeClass("page_layout_print");
			$("#nota_fiscal").removeClass("m-25");
			$("#papel").addClass("page_layout_print_view");
			$rootScope.loadingProgress = true;
			$('html, md-content').animate({
				scrollTop: 0
			}, 400);
			$timeout(function () {
				$rootScope.loadingProgress = false;
				window.print();
				$("#controles").css("display", "");
				$("#papel").removeClass("page_layout_print_view");
				$("#papel").addClass("page_layout_print");
				$("#nota_fiscal").addClass("m-25");
			}, 500);
		};

		$scope.voltar = function (ev) {
			if ($rootScope.$lastState.name !== '') {
				$state.go($rootScope.$lastState.name, $rootScope.$lastState.params);
			} else {
				$state.go('faturamento.listar');
			}
		};

		$scope.carregarNotaDebito();
	}

})();
