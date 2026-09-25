(function() {
    'use strict';

    angular.module('app.notaDebito').controller('NotaDebitoFaturamentoCadastroCtrl', notaDebitoFaturamentoCadastroCtrlFn);

    function notaDebitoFaturamentoCadastroCtrlFn($scope, $state, $stateParams, $mdDialog, EnumService, $rootScope, $timeout, CalculadorFaturamento, NotaDebito, Tributo,
        CategoriaTributo, PessoaJuridica, Unidade, $filter, FaturamentoAliquota, MotivoRetornoDataCancelamentoDialog, MotivoRetornoDialog, MotivoRetornoNota, $q, Contrato, $location,
        FaturamentoRetencao, NotaTributoAliquota, NotaSaidaParcela, StringService, $window, NotaTributoAliquotaEmissao, Parametro, UsuarioLogadoService, Feriado, NotasDebitosService,
        DialogControllerEditarNotaTributoAliquota, MotivoRetornoDocumentoNotaDebito, FaturamentoEtapa) {

        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;
        $scope.action = false;
        $scope.exibirBotoes = false;
        $scope.Math = $window.Math;

		$scope.index = 0;

        const catchError = (response) => {
            $scope.action = false;
            $rootScope.loadingProgress = false;
            $scope.botaoNumerarDesabilitado = false;
            $scope.botaoConfirmarcadastroDesabilitado = false;
            $scope.$emit('crud-critical', 'search', response);
            reject(false);
        };

		$scope.buscarEmpresas = function(usarFuncao, funcao) {
			let p1 = new Promise(
				function(resolve, reject) {
					Unidade.unidadesfaturamento().$promise.then(function(data) {
						$scope.empresaList = [];
						$scope.empresaList.push({
							codigo: -1,
							codnome: ''
						});
						angular.forEach(data, function(value, key) {
							$scope.empresaList.push(value);
						});

						if (usarFuncao) {
							funcao();
						}
						resolve(true);
					}).catch(catchError);
				});
			return p1;
		};

		function definirEmpresa() {
			if ($scope.object.empresa == undefined) {
				$scope.object.empresa = $scope.empresaList[1];
			}
		}

        $scope.verificarPermissao = () => {
			// Exibir apenas quando for usuários do faturamento;
			UsuarioLogadoService.apenasFaturamento().$promise.then(response => {
				$scope.exibirBotoes = response.value;
			});
		};

		$scope.verificarPermissao();

		$scope.$on('changeTipoItens', function() {
			if ($scope.object.tipoItens) {
				acaoDataMap.get($scope.object.tipoItens.id).fn(acaoDataMap.get($scope.object.tipoItens.id).label, 1);
				if ($scope.object.tipoItens)
					$scope.object.tipoData = $scope.object.tipoItens.tipo;
			}
			$scope.$applyAsync();
		});

		$scope.selectedItemServico = undefined;
        $scope.resultsServico = [];
        $scope.bloquearEmissaoND = false;

		/*
		 * O botão 'Numerar' precisa ficar desabilitado para que o usuário não apertar no botão novamente
		 * enquanto ocorre as operações de numerar ou cancelar numeração, já que estas operações estão
		 * levando muito tempo para serem concluidas.
		 */
		$scope.botaoNumerarDesabilitado = false;

        $scope.exibirNotaDebito = function(notaDebito) {
			$state.go('notadebito.exibir', {
				objectId: notaDebito.codigo
			});
		};

        $scope.retornarTelaAnterior = function(notaDebito) {
			$state.go('notadebito.listar');
		};

		$scope.imprimir = function() {
			let url = $state.href('notadebito.imprimir', {
				objectId: $scope.object.codigo
			});
			window.open(url, '_blank');
		};

		$scope.removerNotaDebito = function(ev) {
			$mdDialog.show({
				controller: DialogRemoverNotaDebito,
				locals: {
					nota: $scope.object,
					notification: $scope.notification,
				},
				templateUrl: 'partials/faturamento/notasDebitos/html/partials/dialog_remover_nd.html',
				parent: angular.element(document.body),
				targetEvent: ev,
				clickOutsideToClose: false,
				fullscreen: false
			});
		};

		function DialogRemoverNotaDebito($scope, $mdDialog, $rootScope, nota, notification) {
			$scope.object = nota;
			$scope.notification = notification;
			$scope.action = false;

			$scope.hide = function() {
				$mdDialog.hide();
			};

			$scope.cancel = function() {
				$mdDialog.cancel();
			};

			$scope.confirm = function() {
				$rootScope.loadingProgress = true;
				$scope.action = true;

				NotaDebito.removerND($scope.object).$promise.then(function(data) {
					$scope.notification.success(undefined, "Nota de débito removida com sucesso");
					$mdDialog.hide();
					$scope.action = false;
					$rootScope.loadingProgress = false;
					if ($location.search().acao === 'FAT') {
						$state.go('faturamento.exibir', {
							objectId: $scope.object.faturamentoEtapaTO.faturamento.codigo
						});
					} else {
						$state.go('notadebito.listar');
					}
				}).catch(function(response) {
					$scope.action = false;
					$rootScope.loadingProgress = false;
					$scope.$emit('crud-critical', 'search', response);
					$scope.notification.criticalError(response.status, response.statusText, response.data);
				});
			};

			$scope.cancel = function() {
				$mdDialog.cancel();
			};
		}

		$scope.cancelarNotaDebito = function(event) {
			let chaveMotivo = 'faturamento-cancelar_nd';
			let titulo = 'Motivos de Retorno - Cancelamento de Nota de Débito';

			// Função que será executada no confirmar no dialog de motivo de retorno.
			$scope.confirmarMotivo = function(motivoRetornoDocumento, fn_Hide) {
				$scope.motivoRetornoDocumento = {};
				$scope.motivoRetornoDocumento.notaDebito = {
					codigo: $scope.object.codigo
				};
				$scope.motivoRetornoDocumento.dataCancelamento = motivoRetornoDocumento.dataCancelamento;
				$scope.motivoRetornoDocumento.motivoRetornoDocumento = motivoRetornoDocumento;
				$scope.motivoRetornoDocumento.motivoRetornoDocumento.aplicacao.chave = chaveMotivo;

				$rootScope.loadingProgress = true;

				MotivoRetornoDocumentoNotaDebito.retornarNotaDebito($scope.motivoRetornoDocumento)
					.$promise.then(function(data) {
						$rootScope.loadingProgress = false;
						$scope.notification.success(undefined, "Nota cancelada com sucesso.");
						fn_Hide();
						$scope.objectId = $scope.object.codigo;
						$scope.object.situacao = 'Cancelada';
						loadObj();
					}).catch(function(response) {
						$rootScope.loadingProgress = false;
						$scope.notification.criticalError(response.status, response.statusText, response.data);
					});
			};

			// Função que será executada no cancelar no dialog de motivo de retorno.
			$scope.cancelarMotivo = function(fn_Hide) {
				fn_Hide();
			};

			// Passar para o serviço, as funções que serão chamadas ao confirmar ou cancelar o motivo de retorno.
			MotivoRetornoDataCancelamentoDialog.show(event, chaveMotivo, titulo, $scope.confirmarMotivo, $scope.cancelarMotivo);
		};

		$scope.reverterCancelamento = function(event) {
			let chaveMotivo = 'faturamento-rever_canc_nd';
			let titulo = 'Motivos de Retorno - Reverter Cancelamento de Nota de Débito';

			// Função que será executada no confirmar no dialog de motivo de retorno.
			$scope.confirmarMotivo = function(motivoRetornoDocumento, fn_Hide) {
				$scope.motivoRetornoNota = {};
				$scope.motivoRetornoNota.notaDebito = {
					codigo: $scope.object.codigo
				};
				$scope.motivoRetornoNota.dataCancelamento = motivoRetornoDocumento.dataCancelamento;
				$scope.motivoRetornoNota.motivoRetornoDocumento = motivoRetornoDocumento;

				$rootScope.loadingProgress = true;

				MotivoRetornoDocumentoNotaDebito.retornarNotaDebito($scope.motivoRetornoNota)
					.$promise.then(function(data) {
						$rootScope.loadingProgress = false;
						$scope.notification.success(undefined, "Reversão do cancelamento da nota de débito realizado com sucesso.");
						fn_Hide();
						$scope.objectId = $scope.object.codigo;
						$scope.object.situacao = 'Em recebimento';
						loadObj();
					}).catch(function(response) {
						console.error('crud-critical', 'dialog', response);
						$rootScope.loadingProgress = false;
						$scope.notification.criticalError(response.status, response.statusText, response.data);
					});
			};

			// Função que será executada no cancelar no dialog de motivo de retorno.
			$scope.cancelarMotivo = function(fn_Hide) {
				fn_Hide();
			};

			// Passar para o serviço, as funções que serão chamadas ao confirmar ou cancelar o motivo de retorno.
			MotivoRetornoDialog.show(event, chaveMotivo, titulo, $scope.confirmarMotivo, $scope.cancelarMotivo);
		};

		let loadObj = () => {
			$scope.object = {};
			$rootScope.loadingProgress = true;
			if ($scope.objectId) {
				$scope.botaoNumerarDesabilitado = true;
				NotaDebito.buscarPorId({id: $scope.objectId }).$promise
					.then(function(data) {
						data.dataEmissao = ajustarTimeZoneData(data.dataEmissao);
						$scope.botaoNumerarDesabilitado = false;
						$scope.object = data;
						$scope.object.agenciaConta = CalculadorFaturamento.montarCampoAgenciaConta($scope.object.faturamentoEtapaTO.faturamento.contaCorrente);
						carregamentoMotivosRetorno($scope.object);
				}).catch(function(response) {
					console.error(response);
					$scope.botaoNumerarDesabilitado = false;
					$rootScope.loadingProgress = false;
					$scope.$emit('crud-critical', 'load', response);
				});
			}
		};

		/**
		 * Função para corrigir o time zone das datas.
		 * Quando uma variavel, por exemplo: "2018-11-01T02:00:00.000+0000", é convertido para data, automaticamente o javascript altera as horas de acordo com o
		 * fuzo horário local (do Brasil é GMT-0300 (-03) ).
		 * Essa função adiciona os minutos para a data não sofrer alteração.
		 */
		let ajustarTimeZoneData = (dataInicial) => {
			if (!dataInicial) {
				return;
			}
			let novaData;
			if (dataInicial instanceof Date) {
				novaData = angular.copy(dataInicial);
			} else {
				let localT = dataInicial ? dataInicial.search("T") : -1;
				if (localT > 0) {
					let apenasData = new Date(dataInicial);
					novaData = new Date(apenasData.getFullYear(), apenasData.getMonth(), apenasData.getDate());
				} else {
					novaData = new Date(dataInicial);

				}
			}

			let m = moment(novaData); // 2019-03-22T19:13:31.812+0000
			let resultadoFinal = m.add(novaData.getTimezoneOffset(), 'minutes').format("YYYY-MM-DDT12:00:00.000");

			return resultadoFinal;
		};

		$scope.inserirProtocolo = function(ev) {
			$mdDialog.show({
				controller: DialogControllerInserirProtocolo,
				locals: {
					fnSalvar: $scope.salvarProtocolo,
					nd: $scope.object
				},
				templateUrl: 'partials/faturamento/notasDebitos/html/partials/dialog_protocolo_nd.html',
				parent: angular.element(document.body),
				targetEvent: ev,
				clickOutsideToClose: false,
				fullscreen: false
			});
		};
		function DialogControllerInserirProtocolo($scope, $mdDialog, fnSalvar, nd) {
			function init() {
				$scope.copiaProtocoloEntrega = undefined;
				$scope.notaDebito = nd;
				$scope.copiaProtocoloEntrega = angular.copy($scope.notaDebito.protocoloEntrega);
			}

			init();

			$scope.cancel = function() {
				$mdDialog.cancel();
			};

			$scope.confirmar = function() {
				fnSalvar($scope.copiaProtocoloEntrega);
				$mdDialog.cancel();
			};

		} // fim dialog

		$scope.salvarProtocolo = function(copiaProtocoloEntrega) {
			$scope.actionProtocolo = true;
			$rootScope.loadingProgress = true;
			$scope.action = true;
			$scope.object.protocoloEntrega = copiaProtocoloEntrega;
			NotaDebito.salvarProtocolo($scope.object).$promise.then(function(data) {
				$scope.notification.success(undefined, `Protocolo da ${$scope.object.numero} atualizado com sucesso.`);
				$rootScope.loadingProgress = false;
				$scope.actionProtocolo = false;
				$scope.action = false;
				$scope.object.atualizador = data.atualizador;
				$scope.object.dataAtualizacao = data.dataAtualizacao;
			}).catch(function(response) {
				$rootScope.loadingProgress = false;
				$scope.actionProtocolo = false;
				$scope.action = false;
				$scope.$emit('crud-critical', 'search', response);
				console.error(response);
			});
		};

		//==== DIALOG CANCELAR NUMERAÇÃO DA NOTA FISCAL ====//
		function DialogCancelarNumeracaoNotaDebito($scope, $mdDialog, nota, func) {
			const original = angular.copy(nota);
			$scope.hide = function() {
				$mdDialog.hide();
			};

			$scope.cancel = function() {
				$mdDialog.cancel();
			};

			$scope.confirm = function() {
				$scope.action = true;
				func($mdDialog, original);
			};

			$scope.cancel = function() {
				$mdDialog.cancel();
			};
		};

		let functionCancelarNumeracaoNotaDebito = ($mdDialog, original) => {
			$rootScope.loadingProgress = true;
			$scope.action = true;
//			$scope.object.situacao = "Aberta";
			desabilitarBotaoNumerar();
			$scope.screenMode = 'VIEW';
			NotaDebito.cancelarNumeracaoND($scope.object)
				.$promise.then(function(data) {
					$scope.notification.success(undefined, "Cancelamento da numeração da nota de débito realizada com sucesso");
					$scope.object = data;
					loadObj();
					$mdDialog.hide();
					$scope.action = false;
					$rootScope.loadingProgress = false;
					$scope.botaoNumerarDesabilitado = false;
				}).catch(function(response) {
					$mdDialog.hide();
					$rootScope.loadingProgress = false;
					$scope.action = false;
					$scope.object = original;
					$scope.$emit('crud-critical', 'search', response);
					$scope.notification.criticalError(response.status, response.statusText, response.data);
				});
		};

		$scope.cancelarNumeracaoNotaDebito = function(ev) {
			$mdDialog.show({
				controller: DialogCancelarNumeracaoNotaDebito,
				locals: {
					nota: $scope.object,
					func: functionCancelarNumeracaoNotaDebito
				},
				templateUrl: 'partials/faturamento/notasDebitos/html/partials/dialog_cancelar_numeracao_nd.html',
				parent: angular.element(document.body),
				targetEvent: ev,
				clickOutsideToClose: false,
				fullscreen: false
			});
		};

		let carregamentoMotivosRetorno = function(resultado) {
			return $q(function(resolve, reject) {
				MotivoRetornoDocumentoNotaDebito.page(undefined, {
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

		$scope.numerarNotaDebito = (ev) => {
			let filtro = {};
			filtro.empresa = $scope.object.empresa;
			filtro.dataEmissao = $scope.object.dataEmissao;
			NotaDebito.qbeNumND(filtro).$promise.then(function(data) {
				$scope.copia = angular.copy($scope.object);
				$scope.copia.numero = data.numero;

				$mdDialog.show({
					controller: DialogNumerarNotaDebito,
					locals: {
						nota: $scope.copia,
						notification: $scope.notification,
						func: numeracaoNotaDebitoDialogFunction
					},
					templateUrl: 'partials/faturamento/notasDebitos/html/partials/dialog_numerar_nd.html',
					parent: angular.element(document.body),
					targetEvent: ev,
					clickOutsideToClose: false,
					fullscreen: false
				});

			}).catch(function(response) {
				$scope.$emit('crud-critical', 'search', response);
			});
		};

		//==== DIALOG NUMERAR NOTA DE DÉBITO	 ====//
		let DialogNumerarNotaDebito = ($scope, $mdDialog, $rootScope, nota, notification, func) => {
			const original = angular.copy(nota);
			// O timeout é usado para resolver um erro que ocorre no dialog
			// que impede o campo da data de emissão de carregar previamente preenchida.
			$timeout(function() {
				$scope.object = nota;
				$scope.notification = notification;
			}, 100);

			$scope.hide = function() {
				$mdDialog.hide();
			};

			$scope.cancel = function() {
				$mdDialog.cancel();
			};

			$scope.confirm = function() {
				$scope.action = true;
				func($mdDialog, $scope.object.dataEmissao, $scope.object.numero, original);
			};

			$scope.cancel = function() {
				$mdDialog.cancel();
			};
		};

		function desabilitarBotaoNumerar() {
			$scope.botaoNumerarDesabilitado = true;
		}

		let numeracaoNotaDebitoDialogFunction = ($mdDialog, dataEmissao, numero, original) => {
			$rootScope.loadingProgress = true;
			$scope.action = true;
			$scope.object.situacao = "Aberta";
			$scope.object.dataEmissao = ajustarTimeZoneData(dataEmissao);
			$scope.object.numero = numero;
			$scope.screenMode = 'VIEW';
			NotaDebito.numerarND($scope.object).$promise.then(function(data) {
				$rootScope.loadingProgress = false;
				$scope.notification.success(undefined, data.numero + " numerada com sucesso.");
				$scope.objectId = $scope.object.codigo;
				loadObj();
				$mdDialog.hide();
				$scope.action = false;
			}).catch(function(response) {
				$rootScope.loadingProgress = false;
				$scope.action = false;
				$mdDialog.hide();
				$scope.object = original;
				$scope.object.numero = "ND/000000000";
				$scope.$emit('crud-critical', 'search', response);
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
		};

		$scope.changeTitulo = function() {
			if ($scope.screenMode === 'VIEW') {
				$scope.subtitulo = 'Exibição';
			} else if ($scope.screenMode === 'EDIT') {
				$scope.subtitulo = 'Edição';
			}
		};

		/**
		 * Pesquisa do Código do Serviço
		 */
		$scope.querySearchServico = function(query) {
			$rootScope.loadingProgress = true;
			let obj = {};
			obj.nome = query;
			obj.situacao = 'Aberta';

			let servicos = [];
			let resultado = NotaDebito.qbeST(undefined, obj).$promise;

			return resultado.then(function(data) {
				$rootScope.loadingProgress = false;
				return data;
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'search', response);
				servicos = [];
				return servicos;
			});
		};

		$scope.selectedItemChange = function(item) {
			if (angular.isDefined(item)) {
				$scope.selectedNotaDebito = item;
			}
		};

		$scope.selectedItemChange = (item, tipo) => {
			if (tipo === 'servico') {
				if (item) {
					if ($scope.object.notaDebito === undefined) {
						$scope.object.notaDebito = {};
					}
					$scope.object.servicoTipo  = item;
					$scope.selectedItemServico = item;
				}
			}
		};

		//====  EDIÇÃO POR ABA - INICIO DAS FUNÇÕES ====//
		$scope.modeDadosPrincipais = false;
		$scope.modeDiscriminacao = false;

		/* ABA DADOS PRINCIPAIS */
		$scope.editarDadosPrincipais = function() {
			// USADO O PROCEDIMENTO ABAIXO APENAS PARA SUAVISAR A TRANSIÇÃO PARA O USUÁRIO //
			$rootScope.loadingProgress = true;
			$scope.action = true;
			$scope.screenMode = 'EDIT';
			$scope.modeDadosPrincipais = true;

			$scope.changeTitulo();

			$scope.original = angular.copy($scope.object);

			//FIXME: Erro ao preencher o campo data ao editar.
			setTimeout(() => {
				$scope.object.dataEmissao = ajustarTimeZoneData($scope.original.dataEmissao);
				$scope.object.dataVencimento = ajustarTimeZoneData($scope.original.dataVencimento);
				$scope.$apply();
			}, 300);

			$scope.buscarEmpresas(false, undefined).then(() => {
				$scope.gerenciarServicoTipoDadosPrincipais();
				$rootScope.loadingProgress = false;
				$scope.action = false;
			});
		};

		$scope.gerenciarServicoTipoDadosPrincipais = function() {
			$scope.selectedItemServico = $scope.object.servicoTipo;
		};

		$scope.salvarNotaDebito = function() {
			$rootScope.loadingProgress = true;
			$scope.action = true;
			let error = false;
			$scope.object.faturamentoEtapaTO.valorLiquido = $scope.object.faturamentoEtapaTO.valor - $scope.valorTributosView;
			let convertDoubleValorBrutoTotal = 0;

			angular.forEach($scope.object.notaDebito, function(value, index) {
				let convertDouble = parseFloat(value.valorLiquido);
				if (convertDouble < 0) {
					error = true;
					$scope.notification.error("", "O valor líquido não pode ser menor que R$ 0,00.");
					$scope.action = false;
					return;
				}
				let convertDoubleValorBruto = parseFloat(value.valorBruto);
				convertDoubleValorBrutoTotal = convertDoubleValorBrutoTotal + convertDoubleValorBruto;
				if (convertDoubleValorBruto <= 0) {
					error = true;
					$scope.notification.error("", "O valor bruto deve ser maior que R$ 0,00.");
					$scope.action = false;
					return;
				}
			});

			if (!error) {
				return $q(function(resolve, reject) {
					$scope.object.numero = 'ND/000000000';
					NotaDebito.salvarND($scope.object).$promise.then(function(data) {
						$rootScope.loadingProgress = false;
						$scope.object = data;
						$scope.notification.success(undefined, "Nota de débito salva com sucesso");
						$scope.action = false;
						$scope.modeDiscriminacao = false;
						if ($scope.screenMode === 'NEW') {
							if ($rootScope.$lastState.name !== '') {
								$state.go('notadebito.exibir_por_acao', {
									objectId: data.codigo,
									acao: 'FAT'
								});
							} else {
								$state.go('etapasnaofaturadas.listar');
							}
						} else {
							$scope.screenMode = 'VIEW';
							$scope.objectId = $scope.object.codigo;
							loadObj();
						}
						resolve({
							mensagem: 'Nota de débito salva com sucesso!',
							executou: true
						});
					}).catch(function(response) {
						$rootScope.loadingProgress = false;
						$scope.$emit('crud-critical', 'search', response);
						console.error(response);
						$scope.action = false;
						reject({
							mensagem: 'Nota de débito com error!',
							executou: false
						})
					});
				});
			}
		};

		$scope.openNewTargetFaturamento = function() {
			$window.open('/sgi/nv/app/main.html#!/faturamento/' + $scope.object.faturamentoEtapaTO.faturamento.codigo + '/exibir');
		};

		function DialogControllerPessoa($scope, $mdDialog, pessoa, tipo) {
			$scope.pessoa = pessoa;
			$scope.pessoa.tipoPessoaJuridica = tipo;
			$scope.fechar = function() {
				$mdDialog.cancel();
			};
		}

		$scope.exibirDetalheCliente = function(codigo, tipoPessoa, tipo, ev) {
			if (codigo == undefined) {
				return;
			}
			let useFullScreen = true;

			if (tipo == undefined) {
				tipo = 'ninformado';
			}

			$scope.pessoa = PessoaJuridica.popupFaturamento({
				id: codigo,
				tipo: tipoPessoa,
				idCl: $scope.object.faturamentoEtapaTO.faturamento.centroFinanceiro.codigo
			});

			$rootScope.loadingProgress = true;
			$scope.action = true;

			$scope.pessoa.$promise.then((dados) => {
				$rootScope.loadingProgress = false;
				$scope.action = false;
				$mdDialog.show({
					controller: DialogControllerPessoa,
					locals: {
						pessoa: dados,
						tipo: tipo
					},
					templateUrl: 'js/util/templates/detalhecliente.html',
					parent: angular.element(document.body),
					targetEvent: ev,
					clickOutsideToClose: true,
					fullscreen: useFullScreen,
					arialabel: 'Detalhe cliente'
				});
			});
		};

		$scope.salvarDadosPrincipais = function(form) {

			$rootScope.loadingProgress = true;
			$scope.action = true;

			let tempDataVencimento = converterData($scope.object.dataVencimento);
			$scope.object.dataVencimento = tempDataVencimento;

			let tempDataEmissao = converterData($scope.object.dataEmissao);
            $scope.object.dataEmissao = tempDataEmissao;

			let error = false;

			$scope.object.dataVencimento = $scope.object.dataVencimento;
            $scope.object.dataEmissao = $scope.object.dataEmissao;

			let codigoNotaDebito = $scope.object.codigo;

			$scope.salvarNotaDebito().then(function() {
				$rootScope.loadingProgress = false;

				$scope.modeDadosPrincipais = false;

				$scope.screenMode = 'VIEW';
				$scope.changeTitulo();

				$scope.action = false;
				$scope.actionProtocolo = false;

			}).catch(function(response) {
				$scope.screenMode = 'EDIT';
				$rootScope.loadingProgress = false;
				$scope.action = false;
				$scope.actionProtocolo = false;
				$scope.notification.error("", response.mensagem);
			});
		};

		function converterData(dataInicial) {
			return moment(dataInicial).hour(12).toDate();
		}

		$scope.cancelarDadosPrincipais = function(form) {
				$rootScope.loadingProgress = true;
				$scope.modeDadosPrincipais = false;
				$scope.object = angular.copy($scope.original);
				$scope.gerenciarServicoTipoDadosPrincipais();
				$scope.screenMode = 'VIEW';
				$scope.changeTitulo();
				form.$setDirty();
				form.$setUntouched();
				form.$setPristine();
				$rootScope.loadingProgress = false;
		};

		$scope.montarCampoDiscriminacaoServico = function(etapa) {
			let conteudo = "";

			if (!CalculadorFaturamento.isCampoVazio(etapa) && !CalculadorFaturamento.isCampoVazio(etapa.discriminacaoInicial)) {
				conteudo = etapa.discriminacaoInicial;
			}

			if (!CalculadorFaturamento.isCampoVazio(etapa) && !CalculadorFaturamento.isCampoVazio(etapa.discriminacaoCentral)) {
				conteudo = conteudo + " \n" + etapa.discriminacaoCentral;
			}

			if (!CalculadorFaturamento.isCampoVazio(etapa) && !CalculadorFaturamento.isCampoVazio(etapa.discriminacaoFinal)) {
				conteudo = conteudo + " \n" + etapa.discriminacaoFinal;
			}

			return conteudo;
		};

		if ($scope.screenMode === 'VIEW') {
			loadObj();
		} else if ($scope.screenMode === 'NEW') {
			$scope.objectIdEtapa = $stateParams.objectIdEtapa;

			NotaDebito.create({idEtapa : $scope.objectIdEtapa})
				.$promise.then((data) => {
					$scope.object = data;
					$scope.object.descricao = $scope.montarCampoDiscriminacaoServico($scope.object.faturamentoEtapaTO);
					$scope.object.agenciaConta = CalculadorFaturamento.montarCampoAgenciaConta($scope.object.faturamentoEtapaTO.faturamento.contaCorrente);
					if($scope.object.numero == "ND/000000000"){
						$scope.object.numero = "";
					}
					console.log($scope.object)
					$scope.object.situacao = "Aberta";
					$scope.object.valorBruto = $scope.object.faturamentoEtapaTO.valor;
					$scope.screenMode == 'VIEW';
				});

			$scope.buscarEmpresas(false, undefined);

		}
		$scope.previousStep = function() {
			$scope.index--;
		};

		$scope.isFirstStep = function() {

			if ($scope.index === 0) {
				return true;
			} else {
				return false;
			}
		};

		$scope.nextStep = function() {
			$scope.index = $scope.index + 1;
		};

		$scope.isLastStep = function() {
			if ($scope.index == 3) {
				return true;
			} else {
				return false;
			}
		};

		$scope.currentStepInvalid = function() {
			if ($scope.index === 0) {
				if (angular.isDefined($scope.wizardStep1)) {
					return $scope.wizardStep1.$invalid;
				}
			}
			if ($scope.index == 1) {
				if (angular.isDefined($scope.wizardStep2)) {
					return $scope.wizardStep2.$invalid;
				}
			}
			if ($scope.index == 2) {
				if (angular.isDefined($scope.wizardStep3)) {
					return $scope.wizardStep3.$invalid;
				}
			}
			if ($scope.index == 3) {
				if (angular.isDefined($scope.wizardStep4)) {
					return $scope.wizardStep4.$invalid;
				}
			}
			if ($scope.index == 4) {
				if (angular.isDefined($scope.wizardStep5)) {
					return $scope.wizardStep5.$invalid;
				}
			}
			return true;
		};

		$scope.formsIncomplete = function() {
				if (angular.isDefined($scope.wizardStep1) && angular.isDefined($scope.wizardStep2) && angular.isDefined($scope.wizardStep3) && angular.isDefined($scope.wizardStep4) && angular
					.isDefined($scope.wizardStep5) && angular.isDefined($scope.wizardStep6)) {
					if ($scope.wizardStep1.$invalid === true || $scope.wizardStep2.$invalid === true || $scope.wizardStep3.$invalid === true || $scope.wizardStep4.$invalid === true || $scope
						.wizardStep5.$invalid === true || $scope.wizardStep6.$invalid === true) {
						return true;
					}
				}
				if (angular.isDefined($scope.wizardStep1) && angular.isDefined($scope.wizardStep2) && angular.isDefined($scope.wizardStep3) && angular.isDefined($scope.wizardStep4) && angular
					.isDefined($scope.wizardStep5)) {
					if ($scope.wizardStep1.$invalid === true || $scope.wizardStep2.$invalid === true || $scope.wizardStep3.$invalid === true || $scope.wizardStep4.$invalid === true || $scope
						.wizardStep5.$invalid === true) {
						return true;
					}
				}
			return false;
		};

		/* ABA DESCRIMINAÇÃO */
		$scope.editarDiscriminacaoServicos = function() {
			// USADO O PROCEDIMENTO ABAIXO APENAS PARA SUAVIZAR A TRANSIÇÃO PARA O USUÁRIO //
			$rootScope.loadingProgress = true;
			$scope.original = angular.copy($scope.object);
			//FIXME: Erro ao preencher o campo data ao editar.
			$scope.object.dataEmissao = ajustarTimeZoneData($scope.original.dataEmissao);
			$scope.object.vencimento = ajustarTimeZoneData($scope.original.vencimento);
			$scope.changeTitulo();
			$scope.modeDiscriminacao = true;
			$rootScope.loadingProgress = false;
		};

		$scope.salvarDiscriminacao = function(form) {
			$rootScope.loadingProgress = true;
			form.$setDirty();
			form.$setUntouched();
			form.$setPristine();
			$rootScope.loadingProgress = false;
			$scope.salvarNotaDebito().then(function() {
				$scope.changeTitulo();
			});
		};

		$scope.cancelarDiscriminacao = function(form) {
			$rootScope.loadingProgress = true;
			$scope.object = angular.copy($scope.original);
			$scope.screenMode = 'VIEW';
			$scope.changeTitulo();
			form.$setDirty();
			form.$setUntouched();
			form.$setPristine();
			$scope.modeDiscriminacao = false;
			$rootScope.loadingProgress = false;
		};

		$scope.cancelarNovoNotaDebito = function(ev) {
			if (!$scope.wizardStep1.$invalid || !$scope.bloquearEmissaoND) {
				if ($rootScope.$lastState.params.acao === 'FAT') {
					let params = {
						...$rootScope.$lastState.params
					};
					params.objectId = params.from;
					$state.go('faturamento.exibir', params);
				} else if ($rootScope.$lastState.name !== '') {
					$state.go($rootScope.$lastState.name, $rootScope.$lastState.params);
				} else {
					$state.go('etapasnaofaturadas.listar');
				}
			} else {
				$mdDialog.show({
					controller: DialogCancelarNovoNotaDebito,
					templateUrl: 'partials/faturamento/notasDebitos/html/partials/dialog_cancelar_novo_nota_debito.html',
					parent: angular.element(document.body),
					targetEvent: ev,
					clickOutsideToClose: false,
					fullscreen: false
				});
			}
		};

		$scope.atualizarDiscriminacaoServico = function() {
			let copiaDiscriminacao = angular.copy($scope.object.descricao);

			setValorTributo();

			if (copiaDiscriminacao && copiaDiscriminacao.length > 0) {

				let arrayLinhas = copiaDiscriminacao.trim().split("\n");
				let linhaTextoValorDosTributos = undefined;

				angular.forEach(arrayLinhas, function(value, key) {
					let linha = value;
					let textoValorDosTributos = linha.replace(/\s/g, "").indexOf("VALORDOSTRIBUTOS");
					if (textoValorDosTributos > 0) {
						linhaTextoValorDosTributos = key;
					}
				});

				let novoTexto = "";

				if (linhaTextoValorDosTributos) {
					angular.forEach(arrayLinhas, function(value, key) {
						let linha = value;

						// penultima linha
						if (linhaTextoValorDosTributos === key) {

							let valorPercentual = 0;

							let indexVarCargaTributo = linha.indexOf("CARGA TRIBUTARIA");
							let inicioDalinhaCarga = linha.substring(0, indexVarCargaTributo).trim();

							if ($scope.object.issRetidoCliente) {
								valorPercentual = $scope.totalPercentualTributo;
							} else {
								valorPercentual = $scope.subtotalPercentualTributo;
							}

							let novaLinha = inicioDalinhaCarga.concat("CARGA TRIBUTARIA = ")
								.concat($filter('currency')(valorPercentual, '')).concat("%");

							novaLinha = novaLinha.concat(" ").concat("VALOR DOS TRIBUTOS = ")
								.concat($filter('currency')($scope.object.valorTributos, 'R$ '));

							novoTexto = novoTexto.concat(novaLinha).concat("\n");

							// ultima linha
						} else if (linhaTextoValorDosTributos + 1 === key) {

							let map = new Map();
							let listaCodTributo = [1, 6, 5, 4]; // a ordem que aparece os tributos

							angular.forEach($scope.listaNotaTributoAliquota, function(item, key2) {
								map.set(item.aliquota.categoriaTributo.tributoTO.codigo, item);
							});

							let novaLinha = "";

							angular.forEach(listaCodTributo, function(item, key2) {
								let tributoSelecionado = map.get(item);
								if (tributoSelecionado && tributoSelecionado.valor > 0) {
									let nomeTributo = tributoSelecionado.aliquota.categoriaTributo.tributoTO.nome;
									if (!$scope.object.issRetidoCliente && nomeTributo === 'ISS') {
										return;
									}
									let percentualTributo = tributoSelecionado.aliquota.percentual;
									if (nomeTributo === "PIS/COFINS/CSLL") {
										nomeTributo = "PCC";
									}
									novaLinha = novaLinha.concat(nomeTributo).concat(":").concat("  ").concat(percentualTributo).concat("%").concat("    ");
								}
							});

							novoTexto = novoTexto.concat(novaLinha.trim()).concat("\n");

						} else {
							novoTexto = novoTexto.concat(linha).concat("\n");
						}

					});

					$scope.object.descricao = novoTexto.trim();
				}
			}
		};
    }
})();
