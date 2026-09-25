angular.module('app.previsaoFaturamento')

    .controller('PrevisaoFaturamentoFiltro', ['$scope', '$state', '$stateParams', '$controller', '$mdDialog', '$rootScope', '$document', '$timeout', '$window', 'EnumService', 'Unidade', 'Gerencia', 'CentroFinanceiro', 'UsuarioLogadoService', function($scope, $state, $stateParams, $controller, $mdDialog, $rootScope, $document, $timeout, $window, EnumService, Unidade, Gerencia, CentroFinanceiro, UsuarioLogadoService) {

		init();

        function init() {
			$scope.modulo = $state.current.data.modulo;
			$scope.titulo = $state.current.data.titulo;
			$scope.subtitulo = $state.current.data.subtitulo;

			$scope.object = {};

			$scope.listaCompetencia = [];
			$scope.enumTipo = [];
			$scope.listaOrdem = [];
			$scope.empresaList = [];

			$scope.object.ordem = 0;

			$scope.listaCompetencia = criarListaCompetencia();
			buscarTipoFaturamento();
			$scope.listaOrdem = buscarOrdem();
			buscarEmpresas();

			$scope.gerenciaList = [];
			buscarGerencias();

			$scope.temPerfilAnalista = false;
			buscarPerfil();
        }

		// adiciona datas do mês seguinte à data atual, decrescendo até Janeiro de 2019.
        function criarListaCompetencia() {
        	let listaCompetencia = [];
        	listaCompetencia.push(null);

        	let numeroMes = moment().month();
        	numeroMes++;

        	let repetir = true;
        	while (repetir) {
				let data = moment().month(numeroMes--);
				listaCompetencia.push(data.toDate());

				let dataMoment = moment(data);
				let mes = dataMoment.month();
				let ano = dataMoment.year();

				if (mes === 0 && ano === 2019) {
					repetir = false;
				}
        	}

        	return listaCompetencia;
        }

        function buscarTipoFaturamento() {
			EnumService.enumValues({
				pacote: 'br.com.araujoabreu.sgi.faturamento.domain',
				classe: 'Faturamento',
				enum: 'TipoFaturamento'
			})
			.$promise.then(function(data) {
				$scope.enumTipo = [];
				$scope.enumTipo.push('');
				angular.forEach(data, function(value, key) {
					$scope.enumTipo.push(value);
				});
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});
        }

        function buscarOrdem() {
        	let listaOrdem = [
        		{valor: 0, nome: 'Gerência e CL'},
        		{valor: 1, nome: 'CL específico'}
        	];

        	return listaOrdem;
        }

	 	function buscarEmpresas() {
			let codigosUnidadesIgnoradas = [8]; // vide SGINV-1405

			Unidade.unidadesporempresaexceto(null, codigosUnidadesIgnoradas).$promise.then(function(data) {
					$scope.empresaList = [];
					$scope.empresaList.push({codigo: -1, codnome: ''});

					angular.forEach(data, function(value, key) {
						$scope.empresaList.push(value);
					});
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'search', response);
				});
		};

		function buscarGerencias() {
			let pGerencia = Gerencia.buscarTodasGerenciasHabFat().$promise;

			pGerencia.then(function(data) {
				$scope.gerenciaList = data;

			}).catch(function(response) {
				$scope.$emit('crud-critical', 'contrato load', response);
				$scope.gerenciaList = [];
			});
		}

		function buscarPerfil() {
			UsuarioLogadoService.possuiUmPerfilLista(['faturamento-analista']).$promise.then((result) => {
				$scope.temPerfilAnalista = result.possuiUmPerfilLista;
			});
		}


        $scope.$on('changeTipoFaturamento', function() {
			if ($scope.object.tipoFaturamento === '') {
				$timeout(function() {
					delete $scope.object.tipoFaturamento;
				}, 200);
			}
		});

		$scope.$on('changeCompetencia', function() {
			if ($scope.object.competencia === null) {
				$timeout(function() {
					delete $scope.object.competencia;
				}, 200);
			}
		});

		$scope.$on('changeEmpresa', function() {
			if ($scope.object.codigoUnidade == -1) {
				delete $scope.object.codigoUnidade;
			}
		});

		$scope.$on('changeGerencia', function() {
			if ($scope.object.gerencia.codigo == -1) {
				delete $scope.object.gerencia;
			}
		});

		$scope.fnSelectedItemCentroLucro = function(item) {
			$scope.action = true;
			$scope.object.centroFinanceiro = item;
		};

		$scope.querySearchCentroLucro = function(query){
			$scope.listaCentroLucro = [];

			var obj = {};
			obj.identificadorFinal = query;
			obj.nome = query;
			obj.situacao = 'Ativo';

			var pCF = CentroFinanceiro.buscarCFs(obj).$promise;

			return pCF.then(function(data) {
				$scope.listaCentroLucro = data;
				return $scope.listaCentroLucro;
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'search', response);
				return $scope.listaCentroLucro;
			});
		}; // Fim AutoComplete Centro de Lucro

		$scope.exibir = function() {
			$state.go('previsaoFaturamento.exibir',
			{
				param_competencia: $scope.object.competencia,
//				param_tipo_faturamento: $scope.object.tipoFaturamento, //apenas para o modo de edição
//				param_ordem: $scope.object.ordem,
//				param_cod_empresa: $scope.object.codigoUnidade,
				param_gerencia: $scope.object.gerencia,
				param_cl: $scope.object.centroFinanceiro,
				param_screen_mode: 'VIEW'
			});

		}

		$scope.editar = function() {
			$state.go('previsaoFaturamento.editar',
			{
				param_competencia: $scope.object.competencia,
				param_tipo_faturamento: $scope.object.tipoFaturamento,
//				param_ordem: $scope.object.ordem,
//				param_cod_empresa: $scope.object.codigoUnidade,
				param_gerencia: $scope.object.gerencia,
				param_cl: $scope.object.centroFinanceiro,
				param_screen_mode: 'EDIT'
			});
		}

		$scope.limpar = function() {
			delete $scope.object.gerencia;
			delete $scope.object.codigoUnidade;
			delete $scope.object.competencia;
			delete $scope.object.tipoFaturamento;
			delete $scope.searchTextCentroLucro;
			document.querySelectorAll('input[type=search]')[0].value = '';
			$scope.object.ordem = 0;
		}
    }])

    .controller('PrevisaoFaturamentoDetail', ['$scope', '$state', '$stateParams', '$controller', '$mdDialog', '$rootScope', '$document', '$timeout', 'EnumService', 'Unidade', 'Gerencia', 'CentroFinanceiro', 'UsuarioLogadoService', 'PrevisaoFaturamento', function($scope, $state, $stateParams, $controller, $mdDialog, $rootScope, $document, $timeout, EnumService, Unidade, Gerencia, CentroFinanceiro, UsuarioLogadoService, PrevisaoFaturamento) {

		init();

		function init() {
			// Para o caso de apetar F5. Pois nesses casos os parametros não são preenchidos
			if (!$stateParams.param_competencia && !$stateParams.param_gerencia) {
				$state.go('previsaoFaturamento.filtro', { });
			}

			$scope.modulo = $state.current.data.modulo;
			$scope.titulo = $state.current.data.titulo;
			$scope.subtitulo = $state.current.data.subtitulo;

			$scope.object = {};
			$scope.listaGerencia = [];
			$scope.listaPrevisao = [];
			$scope.temPerfilAnalista = false;

			buscarPerfil();

			$scope.screenMode = $stateParams.param_screen_mode;
			if ($scope.screenMode === 'VIEW') {
				exibicao();
			}
			if ($scope.screenMode === 'EDIT') {
				edicao();
			}
		}

		function exibicao() {
			$scope.listaGerencia = criarListaGerencia();

			$scope.object.competencia = $stateParams.param_competencia;

			let pesquisaTO = montarPesquisaTO();
			let pPrevisaoFaturamento = PrevisaoFaturamento.buscarPrevisoes(null, pesquisaTO).$promise;

			pPrevisaoFaturamento.then(function(data) {
				$scope.listaGerencia[0].listaPrevisao = data;
				$scope.listaGerencia[0].qtd = data.length;

				calcularValoresTotaisDaGerencia($scope.listaGerencia[0]);


				$scope.btnFab = false;
				if ($scope.listaGerencia[0].listaPrevisao.length > 10) {
					$scope.btnFab = true;
					$(document).ready(function() {
						$('md-content').bind('scroll', function() {
							if ($(this).scrollTop() + $(this).innerHeight() >= this.scrollHeight) {
								$scope.btnFab = false;
								$scope.btnBottom = true;
							} else {
								$scope.btnFab = true;
								$scope.btnBottom = false;
							}
						});
					});
				} else {
					$scope.btnFab = false;
				}


			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});
		}

		function edicao() {
			$scope.tipoFaturamento = $stateParams.param_tipo_faturamento;
			exibicao();
		}

		function montarPesquisaTO() {
			let pesquisaTO = {}; //centroFinanceiroPrevisaoPesquisaTO

			pesquisaTO.competencia = montarTextoCompetencia($stateParams.param_competencia);
			pesquisaTO.codigoGerencia = $stateParams.param_gerencia.codigo;

			if ($stateParams.param_cl) {
				pesquisaTO.codigoCL = $stateParams.param_cl.codigo;
			}

			/*if ($stateParams.param_tipo_faturamento) {
				pesquisaTO.tipoFaturamento = $stateParams.param_tipo_faturamento;
			}
			if ($stateParams.param_cod_empresa) {
				pesquisaTO.codigoUnidade = $stateParams.param_cod_empresa;
			}*/

			return pesquisaTO;
		}

		function montarTextoCompetencia(dataCompetencia) {
			let compData = dataCompetencia;
			let textoAno = "" + compData.getFullYear();
			let textoMes = "" + (compData.getMonth() + 1);

			if (textoMes.length === 1) {
				textoMes = "0".concat(textoMes);
			}

			return textoAno + textoMes;
		}


		function buscarPerfil() {
			UsuarioLogadoService.possuiUmPerfilLista(['faturamento-analista']).$promise.then((result) => {
				$scope.temPerfilAnalista = result.possuiUmPerfilLista;
			});
		}

		$scope.editar = function() {
			$state.go('previsaoFaturamento.editar',
			{
				param_competencia: $stateParams.param_competencia,
				param_tipo_faturamento: $stateParams.param_tipo_faturamento,
//				param_ordem: $scope.object.ordem,
//				param_cod_empresa: $scope.object.codigoUnidade,
				param_gerencia: $stateParams.param_gerencia,
				param_cl: $stateParams.param_cl,
				param_screen_mode: 'EDIT'
			});
		};

		$scope.imprimir = function() {
//			$state.go('previsaoFaturamento.filtro', { });
		};

		$scope.cancelar = function() {
			$state.go('previsaoFaturamento.filtro', { });
		};

		$scope.expandedResume = function(item, expand, lista) {
			angular.forEach(lista, function(value) {
				value.expanded = false;
			});
			item.expanded = expand;
		}

		function criarListaGerencia() {
			let lista = [];

			//Mostrar apenas uma gerencia, por enquanto!
			let gerencia = $stateParams.param_gerencia;

			let obj1 = {
				codigo: gerencia.codigo,
				nome: gerencia.nome,
				qtd: 0,
				expanded: false,
				listaPrevisao: null,
				totalOSInicial: 0,
				totalContratual: 0,
				totalExtra: 0,
				total: 0
			};

			lista.push(obj1);

			if (lista.length === 1) {
				lista[0].expanded = true;
			}

			return lista;
		}

		function calcularValoresTotaisDaGerencia(gerencia) {
			let totalOSInicial = 0;
			let totalContratual = 0;
			let totalExtra = 0;
			let total = 0;

			angular.forEach(gerencia.listaPrevisao, function(item, key) {
				totalOSInicial += item.valorOSInicial ? item.valorOSInicial : 0;
				totalContratual += item.valorContratual ? item.valorContratual : 0;
				totalExtra += item.valorExtra ? item.valorExtra : 0;
			});

			total = totalContratual + totalExtra;

			gerencia.totalOSInicial = totalOSInicial;
			gerencia.totalContratual = totalContratual;
			gerencia.totalExtra = totalExtra;
			gerencia.total = total;
		}

		$scope.salvar = function() {
			let listaPrevisoes = $scope.listaGerencia[0].listaPrevisao;
			let pPrevisaoFaturamento = PrevisaoFaturamento.updatePrevisoes(null, listaPrevisoes).$promise;

			pPrevisaoFaturamento.then(function(data) {
				exibir();
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
				$scope.editar();
			});
		}

		function exibir() {
			$state.go('previsaoFaturamento.exibir',
			{
				param_competencia: $stateParams.param_competencia,
				param_tipo_faturamento: $stateParams.param_tipo_faturamento,
//				param_ordem: $scope.object.ordem,
//				param_cod_empresa: $scope.object.codigoUnidade,
				param_gerencia: $stateParams.param_gerencia,
				param_cl: $stateParams.param_cl,
				param_screen_mode: 'VIEW'
			});
		}

		$scope.$on('atualizarTotais', function() {
			calcularValoresTotaisDaGerencia($scope.listaGerencia[0]);
		});

		$scope.competenciaAnterior = function(ev) {
            $mdDialog.show({
                controller: DialogComfirmarCompetenciaAnterior,
                locals: {
                    competencia: $stateParams.param_competencia
                },
                templateUrl: 'partials/faturamento/previsaoFaturamento/dialogs/cofirmar_competencia_anterior.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                fullscreen: false
            });
        }

		//==== DIALOG ====//
		function DialogComfirmarCompetenciaAnterior($scope, $mdDialog, $rootScope, competencia) {
			let copiaCompetencia = angular.copy(competencia);
			let competenciaMoment = moment(copiaCompetencia);

			$scope.competenciaAnterior = competenciaMoment.month(competenciaMoment.month() - 1).toDate();

			$scope.hide = function() {
				$mdDialog.hide();
			};

			$scope.cancel = function() {
				$mdDialog.cancel();
			};

			$scope.confirmar = function() {
				carregarCompetenciaAnterior().then(function(data) {
					$mdDialog.hide();

				}).catch(function(response) {
					$scope.$emit('crud-critical', 'load', response);
				});
			};

			$scope.cancel = function() {
				$mdDialog.cancel();
			};
		}

		function carregarCompetenciaAnterior() {
			let pesquisaTO = montarPesquisaTO();

			let copiaCompetencia = angular.copy($stateParams.param_competencia);
			let competenciaMoment = moment(copiaCompetencia);

			pesquisaTO.competencia = montarTextoCompetencia(competenciaMoment.month(competenciaMoment.month() - 1).toDate());

			let pPrevisaoFaturamento = PrevisaoFaturamento.buscarPrevisoes(null, pesquisaTO).$promise;

			return pPrevisaoFaturamento.then(function(data) {

				let map = new Map();

				angular.forEach(data, function(value) {
					map.set(value.centroFinanceiro.codigo, value);
				});

				angular.forEach($scope.listaGerencia[0].listaPrevisao, function(value) {
					let pAnterior = map.get(value.centroFinanceiro.codigo);

					value.valorContratual = angular.copy(pAnterior.valorContratual);
					value.valorExtra = angular.copy(pAnterior.valorExtra);
				});

				calcularValoresTotaisDaGerencia($scope.listaGerencia[0]);

			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});
		}

		$scope.top = function() {
			var tela = $('md-content');
			tela.prop("scrollTop", tela.prop("scrollHeight"));
		}

		$scope.bottom = function() {
			$( "md-content" ).scrollTop( 0 );
		}


	}])

;
