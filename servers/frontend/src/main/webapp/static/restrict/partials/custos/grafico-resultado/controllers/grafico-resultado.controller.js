(function(){

	'use strict';

	angular
		.module('app.grafico-resultado')

		.controller('GraficoResultadoCtrl', detailGrafico);

	function detailGrafico($scope, $state, $mdDialog, $window, $timeout) {

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

    			$scope.listMeses = [
    				{
    					id:0,
    					mes: 'Janeiro'
    				},{
						id:1,
						mes: 'Fevereiro'
					},{
						id:2,
						mes: 'Março'
					},{
						id:3,
						mes: 'Abril'
					},{
						id:4,
						mes: 'Maio'
					},{
						id:5,
						mes: 'Junho'
					},{
						id:6,
						mes: 'Julho'
					},{
						id:7,
						mes: 'Agosto'
					},{
						id:8,
						mes: 'Setembro'
					},{
						id:9,
						mes: 'Outubro'
					},{
						id:10,
						mes: 'Novembro'
					},{
						id:11,
						mes: 'Dezembro'
					}

    			];

    			$scope.tickValues = [0,500,1000,5000,10000,50000,100000,500000,1000000000,5000000000];

    			$scope.grafico1 = false;
    			$scope.grafico2 = false;
    			$scope.grafico3 = false;
    			$scope.mostraDiv = false;

    			$scope.gerarGraficoDonut = function(data) {
    				for(var i = 0; i < $scope.listMeses.length + 1; i++){
						if(data.x === i){
							$scope.mes = $scope.listMeses[i].mes;
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
    			$scope.object = {};

    			$scope.modulo = $state.current.data.modulo;
    			$scope.titulo = $state.current.data.titulo;

    			$scope.listNiveisReceitas = [
    				{
    					id: 1,
    					nivel: "A faturar"
    				},
    				{
    					id: 2,
    					nivel: "A receber"
    				}
    			];

    			$scope.listSemanas = [
    				{
    					id: 1,
    					semana: "1ª semana"
    				},
    				{
    					id: 2,
    					semana: "2º semana"
    				},
    				{
    					id: 3,
    					semana: "3º semana"
    				}
    			]

    			$scope.listNiveisFornecedores = [
    				{
    					id: 1,
    					nivel: "OC aprovadas"
    				},
    				{
    					id: 2,
    					nivel: "NF cadastradas"
    				},
    				{
    					id: 3,
    					nivel: "NF aprovadas pelo gerente"
    				},
    				{
    					id: 4,
    					nivel: "NF analisadas pelo CAP"
    				}
    			];

    			$scope.listAnos = [
    				{
    					id: 1,
    					ano: "2017"
    				}
    			];

    			$scope.listTipoGrafico = [
    				{
    					id: 1,
    					tipo: "Mensal"
    				},
    				{
    					id: 2,
    					tipo: "Anual"
    				}
    			];

    			$scope.listNiveisPrestContas = [
    				{
    					id: 1,
    					nivel: "Aprovada pelo gerente"
    				},
    				{
    					id: 2,
    					nivel: "Homologada"
    				},
    				{
    					id: 3,
    					nivel: "Em pagamento"
    				}
    			];

    			$scope.listNiveisTributos = [
    				{
    					id:1,
    					nivel: "À agrupar"
    				},
    				{
    					id:2,
    					nivel: "Agrupados/liberados"
    				}
    			];

    			$scope.listInclui = [
					{
						id:1,
						tipo: "Sim"
					},
					{
						id:2,
						tipo: "Não"
					}
				];

    			$scope.graficoGeral = false;
    			$scope.mostraGrafico = false;

    			$scope.gerarGrafico = function(){
    				$timeout(function(){
    					$scope.mostraGrafico = true;
    					$scope.graficoGeral = true;
    					$scope.mudaTipoGrafico();
    				}, 2000);
    			};

    			$scope.graficoBarra = false;
    			$scope.graficoLinha = false;

    			$scope.graficos = false;
    			$scope.novaData = new Date();
    			$scope.mesExibir = $scope.novaData.getMonth() - 1;
				$scope.mesAnterior = $scope.listMeses[$scope.mesExibir].mes;

    			$scope.mudaTipoGrafico = function(){
    				if($scope.object.grafico.tipo === 'Anual'){
    					$scope.graficoLinha = true;
    					$scope.graficoBarra = false;
    					$scope.graficoGeral = false;
    					$timeout(function(){
    						$scope.graficoGeral = true;
    						$scope.graficos = true;
    					}, 2000);
    				} else if($scope.object.grafico.tipo === 'Mensal') {
    					$scope.graficoLinha = false;
    					$scope.graficoBarra = true;
    					$scope.graficoGeral = false;
    					$timeout(function(){
    						$scope.graficoGeral = true;
    					}, 2000);
    				}
    			};

    			$scope.reset = function(){
    				delete $scope.object;
    				$scope.mostraGrafico = false;
    				$scope.graficoLinha = false;
    				$scope.mostraDiv = false;
    			}

			$scope.object.ano = $scope.listAnos[0];
			$scope.object.inclui = $scope.listInclui[1];

			$scope.object.nivelReceitas = $scope.listNiveisReceitas[1];
			$scope.object.nivelFornecedores = $scope.listNiveisFornecedores[3];
			$scope.object.nivelPrestContas = $scope.listNiveisPrestContas[2];
			$scope.object.nivelTributos = $scope.listNiveisTributos[1];

    		}
    		init();

    	};
})();
