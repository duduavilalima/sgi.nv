(function() {
    'use strict';

    angular.module('app.notasDebitoCR').controller('notasDebitoCRCtrl', notasDebitoCRCtrlFn);

    function notasDebitoCRCtrlFn(
    	$scope,
    	$state,
    	$stateParams,
    	$mdDialog,
    	$window,
    	$rootScope,
    	$timeout,
    	EnumService,
    	CalculadorFaturamento,
    	NotaDebito,
    	Tributo,
        CategoriaTributo,
        Parcela,
        ContaCorrente,
        MotivoRetornoDataCancelamentoDialog,
        MotivoRetornoDocumentoNotaDebito,
        MotivoRetornoDialog,
        MotivoRetornoNota,
        $q,
        Contrato,
        $location,
        NotaTributoAliquota,
        UsuarioLogadoService,
        DialogControllerEditarNotaTributoAliquota) {

        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;
        $scope.action = false;
        $scope.exibirBotoes = false;
        $scope.Math = $window.Math;

        const catchError = (response) => {
            $scope.action = false;
            $rootScope.loadingProgress = false;
            $scope.botaoNumerarDesabilitado = false;
            $scope.botaoConfirmarcadastroDesabilitado = false;
            $scope.$emit('crud-critical', 'search', response);
            reject(false);
        };

        function inicializar() {
			$scope.modulo = $state.current.data.modulo;
			$scope.titulo = $state.current.data.titulo;
			$scope.subtitulo = $state.current.data.subtitulo;

			$scope.index = 0;
			$scope.object = {};
			$scope.listaTributos = [];
			$scope.bancoList = [];
			$scope.baixasRetencaoList = undefined;
			$scope.agenciaContaList = [];

			$scope.valorTributos = 0;
			$scope.valorLiquido = 0;

			$scope.percentualRetencaoContratual = 0;
			$scope.object.valorRetencaoContratual = 0;

			$scope.percentualRetencaoAcessoria = 0;
			$scope.object.valorRetencaoAcessoria = 0;

			$scope.action = false;

			$scope.totalValorDesconto = 0;
			$scope.totalValorDevolucao = 0;
			$scope.subtotal = 0;

			UsuarioLogadoService.possuiUmPerfilLista(['faturamento-analista']).$promise.then((result) => {
				$scope.temPerfilAnalista = result.possuiUmPerfilLista;
			});

			UsuarioLogadoService.possuiUmPerfilLista(['faturamento-gestor']).$promise.then((result) => {
				$scope.temPerfilGestorFaturamento = result.possuiUmPerfilLista;
			});

			$scope.temPerfilFaturamentoCnsGerencia = false;

			UsuarioLogadoService.possuiUmPerfilLista(['faturamento-cns-gerencia', 'faturamento-cns-geral']).$promise.then((result) => {
				$scope.temPerfilFaturamentoCnsGerencia = result.possuiUmPerfilLista;
				if ($scope.temPerfilFaturamentoCnsGerencia && $scope.screenMode !== 'VIEW') {
					$scope.screenMode = 'VIEW';
					$state.go('car_notadebito.exibir', {
						objectId: $scope.objectId
					});
					$scope.action = false;
					$scope.notification.error("", "Você não possui acesso aos dados desta parcela.");
				}
			});

			$scope.object = {};
			$scope.object.bancoSelecionado = null;
		}

        inicializar();

		$scope.$on('changeTipoItens', function() {
			if ($scope.object.tipoItens) {
				acaoDataMap.get($scope.object.tipoItens.id).fn(acaoDataMap.get($scope.object.tipoItens.id).label, 1);
				if ($scope.object.tipoItens)
					$scope.object.tipoData = $scope.object.tipoItens.tipo;
			}
			$scope.$applyAsync();
		});

        $scope.exibirNotaDebito = function(notaDebito) {
			$state.go('car_notadebito.exibir', {
				objectId: notaDebito.codigo
			});
		};

		$scope.carregarMotivosRetorno = function(resultado) {
			 	return $q(function(resolve, reject) {
			 		MotivoRetornoDocumentoNotaDebito.page({
			 			notaDebito: {
			 				codigo: resultado.codigo
			 			}
			 		})
			 		.$promise.then(function(data) {
			 			data.content.filter((content)=>{
			 				content.motivoRetornoDocumento.observacao = content.motivoRetornoDocumento.observacao.replace(/\u000A/gi, " ");
			 				return content;
			 			})
			 			$rootScope.loadingProgress = false;
			 			$scope.listMotivos = data.content;
			 			resolve();
			 		}).catch(catchError);
			 	});
			 };

		function loadObj() {
			$rootScope.loadingProgress = true;
			if ($scope.objectId) {
				$timeout(function() {
					NotaDebito.buscarPorId({id: $scope.objectId }).$promise
					.then(function(data) {
						$scope.object = data;
						$scope.object.bancoSelecionado = $scope.object.contaCorrenteRecebimento.banco;
						$scope.object.contaCorrente = $scope.object.contaCorrenteRecebimento;
						$scope.valorTributos = $scope.object.valorTributoRecebido;
						$scope.carregarBancos($scope.object.empresa.matriz.codigo);
						$scope.carregarContasCorrentes($scope.object.bancoSelecionado.codigo, $scope.object.empresa.matriz.codigo);
						$scope.valorLiquido = $scope.calcularValorLiquidoParcela($scope.object, $scope.valorTributos);

						$scope.carregarMotivosRetorno($scope.object);
					}).catch(function(response) {
						console.error(response);
						if (response && response.data && response.data.localizedMessage) {
							if (response.data.localizedMessage === 'Não há etapa para essa nota de débito') {
								$scope.retornarTelaAnterior();
							}
						}
						$rootScope.loadingProgress = false;
						$scope.$emit('crud-critical', 'load', response);
					});
				}, 100);
			}
			$rootScope.loadingProgress = false;
		};

		loadObj();

		$scope.calcularValorLiquidoParcela = function(parcela, tributos) {
			let liquido = parcela.valorBruto - tributos + parcela.valorJuros;
			return liquido
		};

		$scope.carregarBancos = function(codigoPessoa) {
			ContaCorrente.page(null, {
				pessoa: {
					codigo: codigoPessoa
				}
			}).$promise
			.then(function(data) {
				$scope.bancoList = data.content;
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'carregar bancos load', response);
				$scope.bancoList = [];
				$scope.agenciaContaList = [];
			});
		};

		$scope.$on('changeBanco', function() {
			delete $scope.object.tipoDocumento;
			$timeout(function() {
				$scope.carregarContasCorrentes($scope.object.bancoSelecionado.codigo, $scope.object.empresa.matriz.codigo);
			}, 300)
	});

		$scope.carregarContasCorrentes = function(codigoBanco, codigoPessoa) {
			ContaCorrente.page(null, {
				banco: {
					codigo: codigoBanco
				},
				pessoa: {
					codigo: codigoPessoa
				}
			})
			.$promise.then(function(data) {
				$scope.agenciaContaList = data.content;
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'contrato load', response);
				$scope.agenciaContaList = [];
			});
		};

		$scope.$on('changeAgenciaRecebimento', function() {
			carregarTipoDocumento();
		});

		$scope.$on('changeAgenciaEdicao', function() {
			ajustarTipoDocumento();
		});

		function ajustarTipoDocumento() {
			if ($scope.tipoBoleto && $scope.tipoDeposito) {
				atribuirTipoDocumento($scope.tipoBoleto, $scope.tipoDeposito);

			} else {
				EnumService.enumValues({
					pacote: 'br.com.araujoabreu.sgi.contasApagar.domain',
					classe: 'Pagamento',
					enum: 'TipoDocumentoPagamento'
				})
				.$promise.then(function(data) {
					$scope.tipoBoleto = data[0];
					$scope.tipoDeposito = data[1];

					atribuirTipoDocumento($scope.tipoBoleto, $scope.tipoDeposito);

				}).catch(function(response) {
					$scope.$emit('crud-critical', 'load', response);
				});
			}
		}

		function atribuirTipoDocumento(boleto, deposito) {
			if ($scope.object.contaCorrente && $scope.object.contaCorrente.boletoFaturamento) {
				$scope.object.tipoDocumento = boleto;
			} else {
				$scope.object.tipoDocumento = deposito;
			}
		}

		function carregarTipoDocumento() {
			let contaCorrente = $scope.object.contaCorrente;

			if (!CalculadorFaturamento.isCampoVazio(contaCorrente) && !CalculadorFaturamento.isCampoVazio(contaCorrente.boletoFaturamento) && !contaCorrente.boletoFaturamento) {
				EnumService.enumValues({
					pacote: 'br.com.araujoabreu.sgi.contasApagar.domain',
					classe: 'Pagamento',
					enum: 'TipoDocumentoPagamento'
				})
				.$promise.then(function(data) {
					$scope.enumTipoDocumentoPagamento = [];
					$scope.enumTipoDocumentoPagamento.push(data[1]);
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'load', response);
				});
			} else {
				EnumService.enumValues({
					pacote: 'br.com.araujoabreu.sgi.contasApagar.domain',
					classe: 'Pagamento',
					enum: 'TipoDocumentoPagamento'
				})
				.$promise.then(function(data) {
					$scope.enumTipoDocumentoPagamento = [];
					$scope.enumTipoDocumentoPagamento.push(data[0]);
					$scope.enumTipoDocumentoPagamento.push(data[1]);
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'load', response);
				});
			}
		}

		$scope.montarLabelAgenciaConta = function(contaCorrente) {
			let conteudo = CalculadorFaturamento.montarCampoAgenciaConta(contaCorrente);
			if (CalculadorFaturamento.isCampoVazio(conteudo) || conteudo.trim() === '/') {
				return ' ';
			} else {
				return conteudo;
			}
		};

		$scope.$watch('object.valorJuros', function() {
			$scope.valorLiquido = $scope.calcularValorLiquidoParcela($scope.object, $scope.valorTributos);
		});

		$scope.retornar = function() {
			$state.go('car_notadebito.listar');
		};

		$scope.receber = function() {
			$state.go('car_notadebito.receber', {
				objectId: $scope.object.codigo
			});
		};

		$scope.salvarRecebimentoNotaDebito = function() {

			$rootScope.loadingProgress = true;

			$scope.object.contaCorrenteRecebimento.banco = $scope.object.contaCorrente.banco;
			$scope.object.contaCorrenteRecebimento = $scope.object.contaCorrente;
			$scope.object.valorLiquidoRecebido = $scope.valorLiquido;

			NotaDebito.receberNotaDebito($scope.object).$promise.then(function(data) {
				$rootScope.loadingProgress = false;
				$state.go('car_notadebito.exibir',{
					objectId: $scope.object.codigo
				})
				$scope.notification.success("Nota de Débito", "Nota recebida com sucesso");
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'search', response);
				$rootScope.loadingProgress = false;
				$scope.action = false;
				console.error(response);
			});
		};

		let confirmarCancelamentoRecebimento = (event) => {
			$mdDialog.show({
				controller: DialogControllerConfirmarCancelamentoRecebimento,
				locals: {
					fnConfirmarCancelamentoRecebimento: fnConfirmarCancelamentoRecebimento,
					$event: event
				},
				templateUrl: 'partials/contasareceber/notasdebito/html/partials/dialogs/dialog_confirm_cancelamento_recebimento.html',
				parent: angular.element(document.body),
				targetEvent: event,
				clickOutsideToClose: false,
				fullscreen: false
			});
		};

		$scope.cancelarRecebimento = function() {
			if ($rootScope.$lastState.name) {
				$state.go($rootScope.$lastState.name, $rootScope.$lastState.params);
			} else {
				$state.go('car_notadebito.listar');
			}
		};

		$scope.motivoCancelamentoRecebimento = function(event) {
			let chaveMotivo = 'faturamento-canc_receb_nd';
			let titulo = 'Motivos de Retorno - Cancelamento de Nota de Débito';

			$scope.confirmarMotivo = function(motivoRetornoDocumento, fn_Hide) {
				$scope.motivoRetornoDocumento = {};
				$scope.motivoRetornoDocumento.notaDebito = {
					codigo: $scope.object.codigo
				};
				$scope.motivoRetornoDocumento.dataCancelamento = motivoRetornoDocumento.dataCancelamento;
				$scope.motivoRetornoDocumento.motivoRetornoDocumento = motivoRetornoDocumento;
				$scope.motivoRetornoDocumento.motivoRetornoDocumento.aplicacao.chave = 'faturamento-canc_receb_nd';

				$rootScope.loadingProgress = true;

				MotivoRetornoDocumentoNotaDebito.retornarNotaDebito($scope.motivoRetornoDocumento)
					.$promise.then(function(data) {
						$rootScope.loadingProgress = false;
						$scope.notification.success("", "Recebimento cancelado com sucesso.");
						fn_Hide();
						$scope.objectId = $scope.object.codigo;
						$scope.object.situacao = 'Em recebimento';
					}).catch(function(response) {
						$rootScope.loadingProgress = false;
						$scope.notification.criticalError(response.status, response.statusText, response.data);
					});
			};

			$scope.cancelarMotivo = function(fn_Hide) {
				fn_Hide();
			};

			MotivoRetornoDataCancelamentoDialog.show(event, chaveMotivo, titulo, $scope.confirmarMotivo, $scope.cancelarMotivo);
        };

		$scope.cancelarRecebimentoMotivoRetorno = function(event) {
			if ($scope.baixasRetencaoList && $scope.baixasRetencaoList.length > 0 && $scope.object.valorRetencaoRetida) {
				confirmarCancelamentoRecebimento(event);
			} else {
				$scope.motivoCancelamentoRecebimento();
			}
		};
    }

})();
