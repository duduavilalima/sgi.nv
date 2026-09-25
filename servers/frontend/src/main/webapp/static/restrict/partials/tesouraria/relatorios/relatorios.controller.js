(function(){

	'use strict'

	angular
		.module('app.relatorio')

		.controller('RelatoriosCtrl', listRelatorios)

		function listRelatorios($scope, $controller, $state, AutocompleteService, Relatorios, $mdDialog, $rootScope, $timeout, $window, $filter, EnumService, Unidade, Pessoa, UsuarioLogadoService, CentroFinanceiro, Parametro){

			$scope.object = {};
			$scope.relatorio = {};

			$scope.modulo = $state.current.data.modulo;
			$scope.titulo = $state.current.data.titulo;

			$scope.object.gerencia = {};
			$scope.gerenciaList = [];
			$scope.tselected = [];
			$scope.toptions = [];
			$scope.object.gerencia = {};
			$scope.object.gerencia.codigo = -1;

			/*UsuarioLogadoService.possuiUmPerfilLista()
				.$promisse.then(function(data){
					console.log(data);
				}).catch(function(response){
					$scope.$emit('crud-critical', 'search', response);
				});*/

			$scope.listRelatorios = [
				{
					chave:'Relacao_Documentos_Lancados',
					nome: "Relação de documentos lançados"
				},
				{
					chave:'Rateio_notas_fiscais',
					nome: "Rateio de Notas Fiscais"
				}
			];

			$scope.listAprovaGerente = [
				{
					codigo:"",
					tipo: "Indiferente"
				},
				{
					codigo:1,
					tipo: "Aprovadas pelo gerente"
				},
				{
					codigo:0,
					tipo: "Não aprovadas pelo gerente"
				}
			];

			$scope.listEmpresas = [];

			$scope.gerenciaList = CentroFinanceiro.buscarTodasGerencias();

			$scope.changeGerencia = function() {
				delete $scope.toptions;
				delete $scope.tselected;
				changePickListCcCl();
			};


			function changePickListCcCl() {
				// Pesquisa de Centros de Lucro
				var centroFinanceiroTO = {};

				if ($scope.object.gerencia != null) {
					centroFinanceiroTO = $scope.object.gerencia;
				}

				Relatorios.buscarCentros(centroFinanceiroTO)
					.$promise.then(function(data) {
						$scope.centroFinanceiroList = data;
						$scope.toptions = [];
						for (var i = 0; i < $scope.centroFinanceiroList.length; i++) {
							var objeto = {
								name: $scope.centroFinanceiroList[i].identificadorFinal + ' - ' + $scope.centroFinanceiroList[i].nome,
								value: $scope.centroFinanceiroList[i],
								index: i
							};
							$scope.toptions.push(objeto);
						}

						$scope.tselected = [];
					}).catch(function(response) {
						$scope.$emit('crud-critical', 'search', response);
					});
			};

			Unidade.page(null, {tipoUnidade: "Matriz"})
				.$promise.then(function(data){
					$scope.listEmpresas = data.content;
				}).catch(function(response){
					$scope.$emit('crud-critical', 'search', response);
				})


			/*Autocomplete fornecedores*/
            $scope.searchTextFornecedor = null;
            $scope.selectedItemFornecedor = null;
            $scope.resultsFornecedor = [];

            $scope.querySearchCliente = function(query) {
                $rootScope.loadingProgress = true;
                var resultsFornecedor = AutocompleteService.getPessoaJuridica({
                    nome: query
                }).$promise;

                var FornecedoresArray = [];
                return resultsFornecedor.then(function(fornecedor) {
                    $rootScope.loadingProgress = false;
                    angular.forEach(fornecedor, function(fornecedor) {
                        FornecedoresArray.push(fornecedor);
                    })
                    return FornecedoresArray;
                });
            }

            $scope.selectedItemChange = function(item, tipo) {

            };


		// base relatório

		$scope.listTipoNotaRelatorio = [
			{
				codigo:0,
				tipo: "Todas",
				categoria:"todas"
			},
			{
				codigo: 1,
				tipo: "Somente NF (todas)",
				categoria:"todas"
			},
			{
				codigo: 2,
				tipo: "Somente NP (todas)",
				categoria:"todas"
			},
			{
				codigo: 3,
				tipo: "Somente LT (todos)",
				categoria:"todas"
			},
			{
				codigo: 4,
				tipo: "NF e NP",
				categoria:"todas"
			},
			{
				codigo: 5,
				tipo: "NF - Fornecimento/OC",
				categoria:"nf"
			},
			{
				codigo: 6,
				tipo: "NF - Prestação de Contas",
				categoria:"nf"
			},
			{
				codigo: 7,
				tipo: "NF - Transporte",
				categoria:"nf"
			},
			{
				codigo: 8,
				tipo: "NP - Prestação de contas",
				categoria:"np"
			},
			{
				codigo: 9,
				tipo: "NP - Não prestação de contas",
				categoria:"np"
			}
		];

		var listSituacaoRelatorio = [
			{
				codigo:0,
				tipo: "Aberta",
				categoria:"todas"
			},
			{
				codigo:1,
				tipo: "Ajuste Fiscal",
				categoria:"2"
			},
			{
				codigo: 2,
				tipo: "Em Análise",
				categoria:"2"
			},
			{
				codigo: 3,
				tipo: "Reaberta",
				categoria:"todas"
			},
			{
				codigo: 4,
				tipo: "Em Autorização",
				categoria:"2"
			},
			{
				codigo: 5,
				tipo: "Em Cancelamento",
				categoria:"todas"
			},
			{
				codigo: 6,
				tipo: "Cancelada",
				categoria:"todas"
			},
			{
				codigo: 7,
				tipo: "Autorizada",
				categoria:"todas"
			},
			{
				codigo: 8,
				tipo: "Em Pagamento",
				categoria:"3"
			},
			{
				codigo: 9,
				tipo: "Relacionada",
				categoria:"3"
			},
			{
				codigo: 10,
				tipo: "Finalizada",
				categoria: "4"
			},
			{
				codigo: 11,
				tipo: "Em Aprovação",
				categoria:"todas"
			},
			{
				codigo: 12,
				tipo: "Aguardando Agrupamento",
				categoria:"todas"
			},
			{
				codigo: 13,
				tipo: "Agrupado",
				categoria:"todas"
			},
			{
				codigo: 14,
				tipo: "Aguardando homologação (Fundo Fixo)",
				categoria:"todas"
			},
			{
				codigo: 15,
				tipo: "Aguardando liberação da RN",
				categoria:"todas"
			},
			{
				codigo: 16,
				tipo: "Não Finalizado",
				categoria:"todas"
			},
			{
				codigo: 17,
				tipo: "Aberta/Reaberta",
				categoria:"todas"
			},
			{
				codigo: 18,
				tipo: "Em cancelamento/cancelada",
				categoria: "4"
			}
		];


		function carregaLista(){
			$scope.listSituacaoPagamentoRelatorio = [];
			$scope.listSituacaoPagamentoRelatorio.push(listSituacaoRelatorio[0],
			listSituacaoRelatorio[3], listSituacaoRelatorio[17], listSituacaoRelatorio[11],
			listSituacaoRelatorio[15], listSituacaoRelatorio[1], listSituacaoRelatorio[2],
			listSituacaoRelatorio[4], listSituacaoRelatorio[8], listSituacaoRelatorio[9],
			listSituacaoRelatorio[10], listSituacaoRelatorio[18]);
		};

		carregaLista();

		// fim - base relatório


		//FUNÇÃO DE LIMPAR

		$scope.reset = function(form) {
                $scope.object = {};
                $scope.gerencia = [];
                delete $scope.gerenciaList;
                delete $scope.toptions;
                delete $scope.tselected;
                delete $scope.object.aprovGerente;
                delete $scope.object.tipoDocNotaRelatorio;
                delete $scope.object.emissaoDe;
                delete $scope.object.emissaoAte;
                delete $scope.object.gerencia;
                delete $scope.object.movimentoDe;
                delete $scope.object.movimentoAte;
                delete $scope.object.situacao;
                delete $scope.object.pagamentoDe;
                delete $scope.object.pagamentoAte;
                delete $scope.object.numero;
                delete $scope.object.unidade;
                delete $scope.selectedItemFornecedor;

                $scope.form.$setPristine();
                $scope.form.$setUntouched();
            };

		// FUNÇÃO PARA GERAR RELATÓRIO
            $scope.gerarRelatorio = function() {

				if((angular.isUndefined($scope.object.relatorios)) || $scope.object.relatorios == null || $scope.object.relatorios == ''){
					$scope.notification.error("", "Relatorio é um campo de preenchimento obrigatório.");
					return;
				}

                if ((angular.isUndefined($scope.object.emissaoDe) || $scope.object.emissaoDe == null || $scope.object.emissaoDe == '')
                        && (angular.isUndefined($scope.object.emissaoAte) || $scope.object.emissaoAte == null || $scope.object.emissaoAte == '')
                        && (angular.isUndefined($scope.object.movimentoDe) || $scope.object.movimentoDe == null || $scope.object.movimentoDe == '')
                        && (angular.isUndefined($scope.object.movimentoAte) || $scope.object.movimentoAte == null || $scope.object.movimentoAte == '')
                        && (angular.isUndefined($scope.object.pagamentoDe) || $scope.object.pagamentoDe == null || $scope.object.pagamentoDe == '')
                        && (angular.isUndefined($scope.object.pagamentoAte) || $scope.object.pagamentoAte == null || $scope.object.pagamentoAte == '')) {

                    $scope.notification.error("", "É obrigatório informar, pelo menos, um período de datas (emissão, movimento ou pagamento).");
                    return;
                }

				var url;

                Parametro.page({
					chave: 'layout.relatorios.temp.url'
				}).$promise.then(function(parametro) {
					var caminhoBase = '/relatorio/frameset?__locale=pt_BR&__format=pdf&__showtitle=false&__report=report/sgi/financeiro/tesouraria/';

					if (!parametro || !parametro.valor) {
						// Se a chave não foi encontrada, ou se o valor for vazio/nulo, consideramos que estamos em ambiente de
						// desenvolvimento, e geramos a página com base no ambiente local (localhost), assim como era feito
						// anteriormente.
						url = window.location.hostname + ":8081/sgi" + caminhoBase;

					} else {
						// Se a chave foi encontrada, consideramos que o valor do parâmetro é a url base (no geral, hostname +
						// caminho de contexto). O resto da URL será gerado dinamicamente, de acordo com as opções escolhidas
						// pelo usuário.
						url = parametro.valor + caminhoBase;
					}

					montarURLRelatorio(url);

				}).catch(function(response) {
					console.error('crud-critical', 'dialog', response);
					$scope.action = false;
					$scope.notification.criticalError(response.status, response.statusText, response.data);
				});
            }

			function montarURLRelatorio(url) {
				if (angular.isDefined($scope.object.relatorios) && $scope.object.relatorios != null && $scope.object.relatorios.nome != null && $scope.object.relatorios.chave != null) {
					url = url + $scope.object.relatorios.chave + ".rptdesign&nomRelat=" + $scope.object.relatorios.nome;
				}

				if (angular.isDefined($scope.selectedItemFornecedor) && $scope.selectedItemFornecedor != null) {
					url = url + "&fornecedor=" + $scope.selectedItemFornecedor.codigo;
				}


				if ($scope.object.emissaoDe != null && $scope.object.emissaoDe != 'Invalid Date' && angular.isDate($scope.object.emissaoDe)) {
					url = url + "&dtEmiIni=" + $filter('date')($scope.object.emissaoDe, 'yyyy-MM-dd');
				}

				if ($scope.object.emissaoAte != null && $scope.object.emissaoAte != 'Invalid Date' && angular.isDate($scope.object.emissaoAte)) {
					url = url + "&dtEmiFim=" + $filter('date')($scope.object.emissaoAte, 'yyyy-MM-dd');
				}

				if ($scope.object.movimentoDe != null && $scope.object.movimentoDe != 'Invalid Date' && angular.isDate($scope.object.movimentoDe)) {
					url = url + "&dtMovIni=" + $filter('date')($scope.object.movimentoDe, 'yyyy-MM-dd');
				}

				if ($scope.object.movimentoAte != null && $scope.object.movimentoAte != 'Invalid Date' && angular.isDate($scope.object.movimentoAte)) {
					url = url + "&dtMovFim=" + $filter('date')($scope.object.movimentoAte, 'yyyy-MM-dd');
				}

				if ($scope.object.pagamentoDe != null && $scope.object.pagamentoDe != 'Invalid Date' && angular.isDate($scope.object.pagamentoDe)) {
					url = url + "&dtPagIni=" + $filter('date')($scope.object.pagamentoDe, 'yyyy-MM-dd');
				}

				if ($scope.object.pagamentoAte != null && $scope.object.pagamentoAte != 'Invalid Date' && angular.isDate($scope.object.pagamentoAte)) {
					url = url + "&dtPagFim=" + $filter('date')($scope.object.pagamentoAte, 'yyyy-MM-dd');
				}

				if($scope.object.numero != null && $scope.object.numero != ""){
					url = url + "&num=" + $scope.object.numero;
				}


				if($scope.object.aprovGerente != null){
					url = url + "&tnf=" + $scope.object.aprovGerente.codigo;
				}

				if (angular.isDefined($scope.object.unidade) && $scope.object.unidade != null && $scope.object.unidade.codigo != null) {
					url = url + "&codigoEmpresa=" + $scope.object.unidade.codigo;
					url = url + "&nomeEmpresa=" + $scope.object.unidade.codnome;
					if ($scope.object.unidade.tipoUnidade == 'Matriz'){
						url = url + "&isMatriz=true";
					} else {
						url = url + "&isMatriz=false";
					}
				}

				if ($scope.object.situacao != null && $scope.object.situacao != "" && angular.isDefined($scope.object.situacao)) {
					if(listSituacaoRelatorio[17].codigo == $scope.object.situacao.codigo){
						url = url + "&situacao=" + listSituacaoRelatorio[0].codigo + "," + listSituacaoRelatorio[3].codigo;
					} else if(listSituacaoRelatorio[18].codigo == $scope.object.situacao.codigo){
						url = url + "&situacao=" + listSituacaoRelatorio[6].codigo + "," + listSituacaoRelatorio[5].codigo;
					} else {
						url = url + "&situacao=" + $scope.object.situacao.codigo;
					}

					url = url + "&nomeSituacao=" + $scope.object.situacao.tipo;
				}

				if($scope.object.tipoDocNotaRelatorio != null && $scope.object.tipoDocNotaRelatorio != "Todas" && angular.isDefined($scope.object.tipoDocNotaRelatorio)){
					url = url + "&tipoNota=" + $scope.object.tipoDocNotaRelatorio.codigo;
				}

				if (angular.isDefined($scope.tselected) && $scope.tselected != null && $scope.tselected.length > 0) {
					var ccCl = '';
					var i = 0;
					angular.forEach($scope.tselected, function(value, key) {
						i++;

						if(key != 0) {
							ccCl = ccCl + (',');
						}

						ccCl = ccCl + value.value.codigo.toString();
					});

					url = url + '&cl=' + ccCl; // + $scope.object.centroFinanceiroList; //+ "&nomCcCl=" + $scope.object.codigoCcCl.nome;
				}

				if (angular.isDefined($scope.object.gerencia) && $scope.object.gerencia != null && angular.isDefined($scope.object.gerencia.codigo) && $scope.object.gerencia.codigo != null && angular.isDefined($scope.object.gerencia.nome) && $scope.object.gerencia.nome != null) {
					url = url + "&codGer=" + $scope.object.gerencia.codigo; // + "&nomGer=" + $scope.object.gerencia.nome;
					url = url + "&gerencia=" + $scope.object.gerencia.nome;
				}

				url = url.replace(" ", "_").replace(',"', '"') + "&tot=1";
				console.log(url);

				$window.open(url);
			}

            $scope.renderedGerencia = false;

            function renderedGerenciaPorPerfil() {
                $rootScope.loadingProgress = true;
                UsuarioLogadoService.possuiUmPerfilLista('visualizacao-pagamentos').$promise.then(function(data) {
                    $scope.renderedGerencia = data.possuiUmPerfilLista;
               }).catch(function(response) {
                   $rootScope.loadingProgress = false;
                   $rootScope.$emit('crud-critical', 'load', response);
               });
            };

            renderedGerenciaPorPerfil();
		}
})();
