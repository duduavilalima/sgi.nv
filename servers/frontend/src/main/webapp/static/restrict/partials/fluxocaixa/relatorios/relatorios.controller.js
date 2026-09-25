(function(){

	'use strict'

	angular

	.module('app.fluxocaixa.relatorios')

	.controller('FluxoCaixaRelatoriosCtrl', listRelatorios)

	function listRelatorios($scope, $state, $filter, Unidade, Diretoria, Gerencia, Relatorios, Regional, EnumService) {

		init();

		function init() {
			$scope.object = {};
			$scope.object.nivelReceitas = "A receber";
			$scope.object.nivelFornecedor = "Notas fiscais aprovadas pelo contas a pagar";
			$scope.object.nivelPrestacaoContas = "Em pagamento";
			$scope.object.nivelTributos = "Tributos agrupados/liberados";

			$scope.modulo = $state.current.data.modulo;
			$scope.titulo = $state.current.data.titulo;

			$scope.listEmpresas = [];
			$scope.listDiretorias = [];
			$scope.listGerencias = [];
			$scope.listCl = [];
			$scope.listRegionais = [];

			initCampoEmpresa();
			initCampoNivelPrecisaoReceita();
			initCampoNiveisFornecedores();
			initCampoNiveisPrestContas();
			initCampoNiveisTributos();
		}

		/*
		 *  --------
		 *   BOTÕES
		 *  --------
		 */

		$scope.reset = function() {
			delete $scope.object.pagamentoDe;
			delete $scope.object.pagamentoAte;
			delete $scope.object.saldoInicial;

			delete $scope.object.empresa;
			delete $scope.object.diretoria;
			delete $scope.object.regional;

			delete $scope.object.gerencia;
			delete $scope.object.cl;

			delete $scope.object.nivelReceitas;
			delete $scope.object.nivelFornecedor;
			delete $scope.object.nivelPrestacaoContas;
			delete $scope.object.nivelTributos;

			$scope.listDiretorias = [];
			$scope.listGerencias = [];
			$scope.listCl = [];
			$scope.object = {};
		};

		$scope.gerarRelatorio = function() {
			//todo
		}

		/*
		 *  --------
		 *   CAMPOS:
		 *  --------
		 */

		function initCampoEmpresa() {

			var pUnidade = Unidade.page(null, {tipoUnidade: "Matriz"}).$promise;

            pUnidade.then(function(data) {
            	$scope.listEmpresas = data.content;
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'search', response);
			});
		}

		function initCampoNivelPrecisaoReceita() {
			 EnumService.enumValues({ pacote: 'br.com.fasolti.sgi.types', classe: 'null', enum: 'NivelPrecisaoReceita' })
			 .$promise.then(function(data) {

			 $scope.listaNivelReceitas = [];

				angular.forEach(data, function(value) {
				 $scope.listaNivelReceitas.push(value);

				});
			 }).catch(function(response) {
				 $scope.$emit('crud-critical', 'load', response);
			 });
		};

		function initCampoNiveisFornecedores() {
			 EnumService.enumValues({ pacote: 'br.com.fasolti.sgi.types', classe: 'null', enum: 'NivelPrecisaoFornecedor' })
			 .$promise.then(function(data) {

			 $scope.listaNivelFornecedores = [];

				angular.forEach(data, function(value) {
				 $scope.listaNivelFornecedores.push(value);

				});
			 }).catch(function(response) {
				 $scope.$emit('crud-critical', 'load', response);
			 });
		};

		function initCampoNiveisPrestContas() {
             EnumService.enumValues({ pacote: 'br.com.fasolti.sgi.types', classe: 'null', enum: 'NivelPrecisaoPretacaoConta' })
             .$promise.then(function(data) {

             $scope.listaNivelPrestacaoContas = [];

                angular.forEach(data, function(value) {
                 $scope.listaNivelPrestacaoContas.push(value);

                });
             }).catch(function(response) {
                 $scope.$emit('crud-critical', 'load', response);
             });
        };

		function initCampoNiveisTributos() {
			 EnumService.enumValues({ pacote: 'br.com.fasolti.sgi.types', classe: 'null', enum: 'NivelPrecisaoTributo' })
			 .$promise.then(function(data) {

			 $scope.listNiveisTributos = [];

				angular.forEach(data, function(value) {
				 $scope.listNiveisTributos.push(value);

				});
			 }).catch(function(response) {
				 $scope.$emit('crud-critical', 'load', response);
			 });
		};

		/*
		*  ---------
		*   CHANGES:
		*  ---------
		*/

		$scope.changeEmpresa = function() {
		 	buscarDiretoria($scope.object.empresa);
		 	buscarGerencia($scope.object.empresa);
		 	buscarRegional($scope.object.empresa);
		};

		$scope.changeGerencia = function() {
			$scope.listCl = [];
			delete $scope.object.cl;

			if ($scope.object.gerencia) {
				var pRelatorios = Relatorios.buscarCentros($scope.object.gerencia).$promise;
				pRelatorios.then(function(data) {
					$scope.listCl = data;
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'search', response);
					$scope.listCl = [];
					delete $scope.object.cl;
				});
			}
		};

		$scope.verificarPagamentoDe = function() {

			var dataSelecionada = new Date($scope.object.pagamentoDe);
			var proximoDiaPeriodoAte = angular.copy(dataSelecionada);
			var diaDaSemana = dataSelecionada.getDay();
			var diaDoMes = dataSelecionada.getDate();
			proximoDiaPeriodoAte.setDate(1);
			proximoDiaPeriodoAte.setDate(diaDoMes + 32);
			var dataFormatada = $filter('date')(proximoDiaPeriodoAte, 'dd/MM/yyyy');

			if((diaDaSemana !== 1) ){

				 $scope.notification.atencao('A data selecionada deve ser uma segunda-feira');
				 $scope.object.pagamentoAte = null;
				 delete $scope.object.pagamentoDe;

			} else {
				$scope.object.pagamentoAte = proximoDiaPeriodoAte;
			}
		}

		/*
		*  ---------
		*   FUNÇÕES:
		*  ---------
		*/

		function buscarGerencia(unidade) {
		 	$scope.listGerencias = [];
			$scope.listCl = [];
			delete $scope.object.gerencia;
			delete $scope.object.cl;

		 	if (unidade && unidade.codigo) {
		 		var filtro = {codigo: unidade.codigo}
				var pGerencia = Gerencia.buscarByUnidade(filtro).$promise;

				pGerencia.then(function(data) {
					$scope.listGerencias = data;
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'search', response);

					$scope.listGerencias = [];
					$scope.listCl = [];
					delete $scope.object.gerencia;
					delete $scope.object.cl;
				});
		 	}
		}

		function buscarDiretoria(unidade) {
		 	$scope.listDiretorias = [];
			delete $scope.object.diretoria;

		 	if (unidade && unidade.codigo) {

				var pDiretoria = Diretoria.buscarDiretoriaPorEmpresa({id: unidade.codigo}).$promise;

				pDiretoria.then(function(data) {
					if (data) {
						var pDiretoriaContrato = Diretoria.buscarDiretoriasDeContratos().$promise;

						pDiretoriaContrato.then(function(data2) {

							angular.forEach(data, function(value1) {
								angular.forEach(data2, function(value2) {
									if (value1.codigo === value2.codigo) {
										$scope.listDiretorias.push(value1);
									}
								});
							});

						}).catch(function(response) {
							$scope.$emit('crud-critical', 'search', response);
							$scope.listDiretorias = [];
							delete $scope.object.diretoria;
						});
					}

				}).catch(function(response) {
					$scope.$emit('crud-critical', 'search', response);
					$scope.listDiretorias = [];
					delete $scope.object.diretoria;
				});
			}
		}

		function buscarRegional(unidade) {
		 	$scope.listRegionais = [];
			delete $scope.object.regional;

			if (unidade && unidade.codigo) {

				var pRegional = Regional.buscarPorCodigoUnidade({id: unidade.codigo}).$promise;

				pRegional.then(function(data){
					$scope.listRegionais = data
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'load', response);
					$scope.listRegionais = [];
					delete $scope.object.regional;
				})
			}
		}

	}

})();
