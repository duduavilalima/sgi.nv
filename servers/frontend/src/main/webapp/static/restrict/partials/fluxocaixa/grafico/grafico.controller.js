(function(){

	'use strict';

	angular
		.module('app.grafico')

		.controller('GraficoCtrl', detailGrafico);

	function detailGrafico($scope, $state, $rootScope, $controller,  $mdDialog, $window, $timeout, $filter, Unidade, Diretoria, CentroFinanceiro, EnumService, Regional) {

		  	function dialogReceitas($scope, $state, $mdDialog, title, list) {

		  		$scope.title = title;
		  		$scope.listReceitas = list;

		  		$scope.listReceitas = [
					{
						id: 1,
						cl: "4247",
						clienteSub: "GLOBOBOSAT",
						valor: "5000,00",
						emissao: "26/02/2017",
						recebimento: "29/03/2017"
					},
					{
						id: 2,
						cl: "4347",
						clienteSub: "Globo",
						valor: "6000,00",
						emissao: "26/02/2017",
						recebimento: "29/03/2017"
					}
				];

				$scope.hide = function() {
					$mdDialog.hide();
				};
				$scope.cancel = function() {
					$mdDialog.cancel();
				};
				$scope.confirmar = function() {
					$mdDialog.hide();
				};
			}
			$scope.mostraReceitas = function(ev) {
				$mdDialog.show({
					controller: dialogReceitas,
					locals: {
						title: 'Receitas',
						list: $scope.listReceitas
					},
					templateUrl: 'partials/fluxocaixa/grafico/dialog/dialog_receitas.html',
					parent: angular.element(document.body),
					targetEvent: ev,
					clickOutsideToClose: false,
					fullscreen: false
				});
			};

			function dialogDespesas($scope, $state, $mdDialog, title, list) {

				$scope.title = title;
				$scope.listDespesas = list;
				$scope.pagamento = new Date();

				$scope.listDespesas = [
					{
						id: 1,
						numero: "NF/2000",
						credor: "GLOBOBOSAT",
						aPagar: "4500,00"
					},
					{
						id: 2,
						numero: "NF/4800",
						credor: "SODEXO",
						aPagar: "2500,00"
					}
				];

				$scope.hide = function() {
					$mdDialog.hide();
				};
				$scope.cancel = function() {
					$mdDialog.cancel();
				};
				$scope.confirmar = function() {
					$mdDialog.hide();
				};
			}
			$scope.mostraDespesas = function(ev) {
				$mdDialog.show({
					controller: dialogDespesas,
					locals: {
						title: 'Despesas',
						list: $scope.listDespesas
					},
					templateUrl: 'partials/fluxocaixa/grafico/dialog/dialog_despesas.html',
					parent: angular.element(document.body),
					targetEvent: ev,
					clickOutsideToClose: false,
					fullscreen: false
				});
			};

			$scope.grafico1 = false;
			$scope.grafico2 = false;
			$scope.grafico3 = false;
			$scope.mostraDiv = false;

			$scope.gerarGraficoDonut = function(data) {
				console.log(data);
				for(var i = 0; i < 5; i++){
					if(data.x === i){
						$scope.tipoDoGrafico = ++i + 'ª semana';
						break;
					}
				}
				$scope.mostraDiv = true;
				$timeout(function(){
					$scope.grafico1 = true;
					$scope.grafico2 = true;
					$scope.grafico3 = true;
				}, 3000);
			};

		function init(){

			$scope.modulo = $state.current.data.modulo;
			$scope.titulo = $state.current.data.titulo;
			$scope.subtitulo = "Pesquisar";

			initCampoEmpresa($scope.object);
			carregarTipoGrafico();
			carregarTipoPeriodo();
			carregarNiveisTributos();
			carregarNiveisPrestContas();
			carregarNiveisFornecedores();
			carregarNivelPrecisaoReceita();

			$scope.listSemanas = [
				{
					id: 1, semana: "1ª semana"
				},{
					id: 2, semana: "2º semana"
				},{
					id: 3, semana: "3º semana"
				}
			]

			$scope.graficoGeral = false;
			$scope.mostraGrafico = false;

			$scope.gerarGrafico = function(){
				$scope.mostraGrafico = true;
				$scope.graficoGeral = true;
				$scope.mudaTipoGrafico();
			};

			$scope.graficoBarra = false;
			$scope.graficoLinha = false;

			$scope.mudaTipoGrafico = function(){
				if($scope.object.grafico === 'Linha'){
					$scope.graficoLinha = true;
					$scope.graficoBarra = false;
					$scope.graficoGeral = false;

					$scope.graficoGeral = true;

				} else if($scope.object.grafico === 'Barra/linhas') {
					$scope.graficoLinha = false;
					$scope.graficoBarra = true;
					$scope.graficoGeral = false;

					$scope.graficoGeral = true;

				}
			};

		} // fim do método init

		$scope.reset = function(){
			delete $scope.object;
			$scope.result = null;
			$scope.listGerencias = [];
			$scope.mostraGrafico = false;
			$scope.graficoLinha = false;
		}

		function carregarNivelPrecisaoReceita() {
			 EnumService.enumValues({ pacote: 'br.com.fasolti.sgi.types', classe: 'null', enum: 'NivelPrecisaoReceita' })
			 .$promise.then(function(data) {

			 $scope.listNiveisReceitas = [];

				angular.forEach(data, function(value) {
				 $scope.listNiveisReceitas.push(value);

				});
			 }).catch(function(response) {
				 $scope.$emit('crud-critical', 'load', response);
			 });
		};

		function carregarNiveisFornecedores() {
			 EnumService.enumValues({ pacote: 'br.com.fasolti.sgi.types', classe: 'null', enum: 'NivelPrecisaoFornecedor' })
			 .$promise.then(function(data) {

			 $scope.listNiveisFornecedores = [];

				angular.forEach(data, function(value) {
				 $scope.listNiveisFornecedores.push(value);

				});
			 }).catch(function(response) {
				 $scope.$emit('crud-critical', 'load', response);
			 });
		};

		function carregarNiveisPrestContas() {
			 EnumService.enumValues({ pacote: 'br.com.fasolti.sgi.types', classe: 'null', enum: 'NivelPrecisaoPretacaoConta' })
			 .$promise.then(function(data) {

			 $scope.listNiveisPrestContas = [];

				angular.forEach(data, function(value) {
				 $scope.listNiveisPrestContas.push(value);

				});
			 }).catch(function(response) {
				 $scope.$emit('crud-critical', 'load', response);
			 });
		};

		function carregarTipoGrafico() {
			 EnumService.enumValues({ pacote: 'br.com.fasolti.sgi.types', classe: 'null', enum: 'TipoGrafico' })
			 .$promise.then(function(data) {

			 $scope.listTipoGrafico = [];
			 $scope.listTipoGrafico.push('');

				angular.forEach(data, function(value) {
				 $scope.listTipoGrafico.push(value);

				});
			 }).catch(function(response) {
				 $scope.$emit('crud-critical', 'load', response);
			 });
		};

		function carregarTipoPeriodo() {
			 EnumService.enumValues({ pacote: 'br.com.fasolti.sgi.types', classe: 'null', enum: 'TipoPeriodo' })
			 .$promise.then(function(data) {

			 $scope.listTipoPeriodo = [];

				angular.forEach(data, function(value) {
				 $scope.listTipoPeriodo.push(value);

				});
			 }).catch(function(response) {
				 $scope.$emit('crud-critical', 'load', response);
			 });
		};

		function carregarNiveisTributos() {
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

		function initCampoEmpresa(object) {
			$scope.listEmpresas = [];

			var pUnidade = Unidade.page(null, {tipoUnidade: "Matriz"}).$promise;

			pUnidade.then(function(data){
				$scope.listEmpresas.push({codigo: null, codnome: ""});
				angular.forEach(data.content, function(value, key) {
					$scope.listEmpresas.push(value);
				});

			}).catch(function(response) {
				 $scope.$emit('crud-critical', 'load', response);
			});
		}

		$scope.carregarGerencia = function(){
			$scope.listGerencias = [];

			var pGerencia = CentroFinanceiro.buscarGerenciasPorDiretoria($scope.object.diretoria).$promise;

			pGerencia.then(function(data){
				$scope.listGerencias.push({codigo: null, nome: ""});
				angular.forEach(data, function(value, key) {
					$scope.listGerencias.push(value);
				});
			}).catch(function(response) {
				 $scope.$emit('crud-critical', 'load', response);
			})
		}

		function carregarRegional(){
			$scope.listRegionais = [];

			if($scope.object.unidade.codigo !== null && $scope.object.unidade.codigo !== ''){

				var pRegional = Regional.buscarPorCodigoUnidade({id: $scope.object.unidade.codigo}).$promise;

				pRegional.then(function(data){
					$scope.listRegionais.push({codigo: null, nome: ""});
					angular.forEach(data, function(value, key) {
						$scope.listRegionais.push(value);
					});
				}).catch(function(response) {
					 $scope.$emit('crud-critical', 'load', response);
				})

			}
		}

		function initCampoDiretoria() {
			$scope.listDiretoria = [];

			if($scope.object.unidade.codigo !== null && $scope.object.unidade.codigo !== ''){

				var pDiretoria = Diretoria.buscarDiretoriaPorEmpresa({id: $scope.object.unidade.codigo}).$promise;

				pDiretoria.then(function(data){

					$scope.listDiretoria.push({codigo: null, descricao: ''});
					angular.forEach(data, function(value, key) {
						$scope.listDiretoria.push(value);
					});

				}).catch(function(response) {
					 $scope.$emit('crud-critical', 'load', response);
				});
			}
		}

		$scope.carregarElementos = function(){
			carregarRegional();
			initCampoDiretoria();
		}

		$scope.carregarCentroLucro = function(){
			$scope.listCentroLucro = [];

			if($scope.object.gerencia.codigo !== null && $scope.object.gerencia.codigo !== ''){

				var pCentroLucro  = CentroFinanceiro.buscarGerenciasPorContratos({id: $scope.object.gerencia.codigo}).$promise;

				pCentroLucro .then(function(data){
					$scope.listCentroLucro.push({codigo: null, nome: ""});

					angular.forEach(data, function(value, key) {
						$scope.listCentroLucro.push(value);
					});
				}).catch(function(response) {
					 $scope.$emit('crud-critical', 'load', response);
				})
			}
		}

		$scope.verificarPeriodoDe = function(){

			var dataSelecionada = new Date($scope.object.periodoDe);
			var proximoDiaPeriodoAte = new Date();
			var diaDaSemana = dataSelecionada.getDay();
		    proximoDiaPeriodoAte.setDate(dataSelecionada.getDate() + 33);
 			var dataFormatada = $filter('date')(proximoDiaPeriodoAte, 'dd/MM/yyyy');

			if((diaDaSemana !== 1) ){

				 $scope.notification.atencao('A data selecionada deve ser uma segunda-feira');
				 $scope.object.periodoAte = null;
				 delete $scope.object.periodoDe;

			}else {
				$scope.object.periodoAte = proximoDiaPeriodoAte;
			}
		}

		init();

	};
})();
