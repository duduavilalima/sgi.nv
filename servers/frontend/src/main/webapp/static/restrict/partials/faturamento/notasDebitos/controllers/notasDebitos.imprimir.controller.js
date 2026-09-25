(function () {
	'use strict';

	angular.module('app.notaDebito')
		.controller('NotaDebitoImpressaoCtrl', notaDebitoImprimirCtrl);

	function notaDebitoImprimirCtrl($scope, $rootScope, $state, CalculadorFaturamento, NotaDebito, MotivoRetornoNota, Endereco, PessoaJuridica, FaturamentoAliquota, CategoriaTributo, Contrato, $timeout, NotaTributoAliquotaEmissao) {
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

					$scope.object.numero = data.numero.replace("ND/00000", "Nº ");

				}).catch(function (response) {
					$scope.$emit('crud-critical', 'load', response);
				});
			}
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

		$scope.carregarNotaDebito();
	}

})();
