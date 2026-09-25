angular
.module('app.centrosfinanceiros').controller('CentrosFinanceirosDetailCtrl', detailController);

function detailController($scope, $state, $stateParams, $mdDialog, $rootScope, CentroFinanceiro, CentroFinanceiroEndereco, CentroFinanceiroFluxoEtapa, CentroFinanceiroAliquota, Gerencia, Telefone, DialogConfirm,
							Fecho, FechoMaoDeObra, FechoMaoDeObraAdicional, FechoDespesaGeral, FechoService, FechoMaoDeObraService, FechoDespesaGeralService, CategoriaTributo, CentroFinanceiroAliquotaService,
							UsuarioLogadoService, Unidade, Colaborador, PessoaFisica, Pessoa, PessoaJuridica, Endereco, Cidade, EnderecoService, MotivoRetorno, MotivoRetornoDocumento, MotivoAplicacao,
							MotivoRetornoCentroFinanceiro, MotivoDocumento, Contrato, CentroFinanceiroService, ContratoContato, Parametro, MotivoRetornoDialog, FundoFixo, CategoriaMaoDeObraAdicional) {

	const $ctrl = this;
	var isTelaImpressao;
	let dataPrevisaoReajuste;

	$ctrl.listGarantias = [
		{ name: 'Não exigidas', value: 'Não exigidas' },
		{ name: 'Fiança bancária', value: 'Fiança bancária' },
		{ name: 'Caução', value: 'Caução' },
		{ name: 'Seguro-garantia', value: 'Seguro-garantia' }
	];


	init = () => {
		$ctrl.object = undefined;
		isTelaImpressao = $state.current.data && $state.current.data.subtitulo === 'IMPRIMIR_OS_INICIAL' ? true : false;

		$scope.selectedItemEngResponsavel = '';
		$scope.selectedItemContatoAA = '';

		if ($scope.screenMode === 'NEW') {
			newObj();
		} else {
			loadObj();
		}
	};

	loadObj = () => {
		const pCentroFinanceiro = CentroFinanceiro.get({id: $stateParams.objectId }).$promise;

		pCentroFinanceiro.then(function(data) {

			dataPrevisaoReajuste = angular.copy(data.dataPrevisaoReajuste);
			$ctrl.object = data;
			$ctrl.object.isAssociadoContrato = data.contratoTO !== undefined;
			if (!$ctrl.object.isAssociadoContrato)
				delete $ctrl.object.permiteClienteLivre

			if ($scope.screenMode === 'VIEW') {
				initView($ctrl.object);
			} else {
				initEdit($ctrl.object);
			}

			// FIXME PICK DATA
			setTimeout(function(){
				$ctrl.object.dataPrevisaoReajuste = angular.copy(dataPrevisaoReajuste);
				$scope.$apply();
			}, 500);

		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});
	};

	/*
	 * ========================================
	 *          -:: NOVO / EDIÇÃO ::-
	 * ========================================
	 */
	newObj = () => {
		$ctrl.object = new CentroFinanceiro();
		$ctrl.object.cumulativo = true;
		$ctrl.object.identificador1 = undefined;
		$ctrl.object.identificador2 = undefined;
		$ctrl.object.totalHH = undefined;
		$ctrl.object.formaPagamento = undefined;
		$ctrl.object.criterioMedicao = undefined;
		$ctrl.object.permiteClienteLivre = false;

		if ($stateParams && $stateParams.objectId && $stateParams.from === 'contrato') {
			$ctrl.object.situacao = 'Em elaboração';
			$ctrl.object.isAssociadoContrato = true;
			buscarContratoParaNovoCf($stateParams.objectId);
		} else {
			delete $ctrl.object.permiteClienteLivre
			$ctrl.object.isAssociadoContrato = false;
			$ctrl.object.situacao = 'Ativo';
		}

		initCampoGerencia({codigo: undefined}).then(() => {
			initCampoMatrizFilial().then(() => {
				$ctrl.object.unidade = $ctrl.listaUnidade[0];
				$ctrl.object.listaEnderecos = [];
				$ctrl.listaEnderecoParaRemover = [];
			});
		});
	};

	initEdit = (objCentroFinanceiro) => {
		let centroFinanceiroTO = { codigo: undefined };
		initCampoGerencia(centroFinanceiroTO).then(() => {
			let finalDoIdentificador = objCentroFinanceiro.identificador.substring(7, objCentroFinanceiro.identificador.length);
			objCentroFinanceiro.identificador2 = Number(finalDoIdentificador);

			carregarIdentificadorView(objCentroFinanceiro);
			let temContrato = verificarContrato(objCentroFinanceiro);

			renderizarBotoes(temContrato);
			desabilitarBotoes(temContrato);

			if (objCentroFinanceiro.contatoAA && objCentroFinanceiro.contatoAA.codigo) {
				$scope.selectedItemContatoAA = objCentroFinanceiro.contatoAA;
				$scope.searchTextContatoAA = objCentroFinanceiro.contatoAA.nome;
				carregarTelContatoView(objCentroFinanceiro);
			}

			if (objCentroFinanceiro.engenheiroResponsavel && objCentroFinanceiro.engenheiroResponsavel.codigo) {
				$scope.selectedItemEngResponsavel = objCentroFinanceiro.engenheiroResponsavel;
				$scope.searchTextEngResponsavel = objCentroFinanceiro.engenheiroResponsavel.nome;
			}

			initCampoMatrizFilial().then(() => carregarAbaEndereco(objCentroFinanceiro));
			$scope.searchTextEngResponsavel = '';

			$ctrl.listaEnderecoParaRemover = [];
		});
	};

	// FIXME PICK DATA
	$ctrl.blurDataPrevisaoReajuste = (novaDataPrevisaoReajuste) => {
		if (novaDataPrevisaoReajuste == dataPrevisaoReajuste) {
			setTimeout(function(){
				$ctrl.object.dataPrevisaoReajuste = angular.copy(dataPrevisaoReajuste);
			}, 1);
		}
	};

	/*
	* --------------
	*  :: CAMPOS ::
	* --------------
	*/

	initCampoGerencia = (objCentroFinanceiroTO) => {
		$ctrl.listaGerencia = [];

		const pCentroFinanceiro = () => CentroFinanceiro.buscarGerenciasDaEdicao(objCentroFinanceiroTO).$promise;
		let pCentroFinanceiroFN = data => { $ctrl.listaGerencia.push(...data); };

		let catchError = response => {
			$scope.$emit('crud-critical', 'load', response);
			$ctrl.listaGerencia = [];
		};

		return Promise.resolve().then(pCentroFinanceiro).then(pCentroFinanceiroFN).catch(catchError);
	};

	initCampoMatrizFilial = () => {
		$ctrl.listaUnidade = [];

		const pUnidade = () => Unidade.unidadesporempresa().$promise;

		let pUnidadeFN = data => {
			data.filter((item) => { return item.situacao === 'Ativo'; })
				.forEach((item) => { $ctrl.listaUnidade.push(item); });
		};

		let catchError = response => {
			$scope.$emit('crud-critical', 'load', response);
		};

		return Promise.resolve().then(pUnidade).then(pUnidadeFN).catch(catchError);
	};

	$ctrl.querySearchEngResponsavel = (query) => {
		let objColaborador = { situacao: 'Ativo', nome: query };
		const pColaborador = Colaborador.buscarListaColaborador(objColaborador).$promise;

		return pColaborador.then(function(data) {
			let listaColaborador = [];
			listaColaborador.push(...data);
			return listaColaborador;
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
			return [];
		});
	};

	$ctrl.selectedItemChangeEngResponsavel = (item) => {
		$ctrl.object.engenheiroResponsavel = item;
	};

	$ctrl.querySearchContatoAA = (query) => {
		let objPessoaFisica = { nome: query };
		const pPessoaFisica = PessoaFisica.buscarListaPessoaFisica({}, objPessoaFisica).$promise;

		return pPessoaFisica.then(function(data) {
			let listaPessoaFisica = [];
			listaPessoaFisica.push(...data);
			return listaPessoaFisica;
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
			return [];
		});
	};

	$ctrl.selectedItemChangeContatoAA = (item) => {
		$ctrl.object.contatoAA = item;
		carregarTelContatoView($ctrl.object);
	};

	$ctrl.changeGerencia = () => {
		if ($ctrl.object && $ctrl.object.pai) {
			let parte1 = $ctrl.object.pai.diretoria.numero;
			let parte2 = $ctrl.object.pai.numero;
			$ctrl.object.identificador1 = `${parte1}.${parte2}.`;
		}
	};

	enderecosValidados = () => {

		if (!$ctrl.object.listaEnderecos || $ctrl.object.listaEnderecos.length <= 0) {
			$scope.notification.error('', 'É necessário informar, pelo menos, um endereço para o centro financeiro.');
			return  false;
		}

		let temEnderecoPrincipal = $ctrl.object.listaEnderecos.some(cfEndereco => { return cfEndereco.endereco.principal; });

		if (!temEnderecoPrincipal) {
			$scope.notification.error('', 'Na aba endereços, deve haver pelo menos um endereço marcado como principal.');
			return false;
		}

		return true;
	};

	criarIdentificador = () => {
		let identificador = $ctrl.object.identificador1.trim() + $ctrl.object.identificador2;
		return identificador;
	};


	/*
	* --------
	*  BOTÕES
	* --------
	*/

	$ctrl.salvar = () => {
		if (enderecosValidados()) {
			$ctrl.object.identificador = criarIdentificador();

			if ($scope.screenMode === 'NEW') {
				salvarNovoCentroFinanceiro($ctrl.object);

			} else {

				//buscar pai do centro financeiro:
				let codigoPai = $ctrl.object.pai.codigo;
				const pCentroFinanceiro = CentroFinanceiro.get({ id: codigoPai }).$promise;

				pCentroFinanceiro.then(function(data) {
					let objPai = data;
					$ctrl.object.pai = objPai;
					atualizarCentroFinanceiro($ctrl.object);

				}).catch(function(response) {
					$scope.$emit('crud-critical', 'load', response);
				});
			}
		}
	};

	$ctrl.cancelar = () => {
		if ($scope.screenMode === 'NEW') {
			if ($stateParams.from === 'contrato') {
				$state.go('contratos.view', { objectId: $stateParams.objectId });
			} else {
				$state.go('centrosfinanceiros.list');
			}
		} else {
			if ($stateParams.from === 'contrato') {
				$state.go('contratos.view', { objectId: $stateParams.objectIdFrom });
			} else {
				$state.go('centrosfinanceiros.view', { objectId: $ctrl.object.codigo });
			}
		}
	};

	salvarNovoCentroFinanceiro = (centroFinanceiro) => {
		$rootScope.loadingProgress = true;

		let tempListaCfEndereco = angular.copy(centroFinanceiro.listaEnderecos);


		centroFinanceiro.proximoNumOC = 1;
		centroFinanceiro.tipo = 'CENTRO_LUCRO';

		if ($stateParams.from === 'contrato') {
			centroFinanceiro.contratoTO = {};
			centroFinanceiro.contratoTO.codigo = $stateParams.objectId;
		}

		const sCentroFinanceiro = centroFinanceiro.$save();

		sCentroFinanceiro.then(function(dataCentroFinanceiro) {

			let contador = 0;

			//Inicio loop
			tempListaCfEndereco.forEach(cfEndereco => {
				cfEndereco.centroFinanceiro = dataCentroFinanceiro;
				let sCentroFinanceiroEndereco = cfEndereco.$save();

				sCentroFinanceiroEndereco.then(function (dataCentroFinanceiroEndereco) {

					contador++;
					if (contador === tempListaCfEndereco.length) {
						$rootScope.loadingProgress = false;
						$state.go('centrosfinanceiros.view', { objectId: dataCentroFinanceiro.codigo });
						$scope.notification.success(undefined, 'Centro financeiro salvo com sucesso');

						if ($stateParams.from === 'contrato') {
							$state.go('contratos.view', { objectId: $stateParams.objectId });
						}
					}

				}).catch(function(responseCentroFinanceiroEndereco) {
					$rootScope.loadingProgress = false;
					$scope.$emit('crud-critical', 'save', responseCentroFinanceiroEndereco);
				});

			}); //Fim loop

		}).catch(function(responseCentroFinanceiro) {
			$rootScope.loadingProgress = false;
			$scope.$emit('crud-critical', 'save', responseCentroFinanceiro);
		});
	};

	atualizarCentroFinanceiro = (objCentroFinanceiro) => {

		$rootScope.loadingProgress = true;

		let tempListaCfEndereco = angular.copy(objCentroFinanceiro.listaEnderecos);

		if (!objCentroFinanceiro.contratoTO || !objCentroFinanceiro.contratoTO.codigo) {
			objCentroFinanceiro.contratoTO = undefined;
			delete objCentroFinanceiro.contratoTO;
		}

		if (!objCentroFinanceiro.engenheiroResponsavel || !objCentroFinanceiro.engenheiroResponsavel.codigo) {
			objCentroFinanceiro.engenheiroResponsavel = undefined;
			delete objCentroFinanceiro.engenheiroResponsavel;
		}

		const sCentroFinanceiro = CentroFinanceiro.update({id: objCentroFinanceiro.codigo}, objCentroFinanceiro).$promise;

		sCentroFinanceiro.then(function(dataCentroFinanceiro) {
			atualizarEnderecos(tempListaCfEndereco, dataCentroFinanceiro);

		}).catch(function(responseCentroFinanceiro) {
			$scope.$emit('crud-critical', 'save', responseCentroFinanceiro);
		});

	};

	//Atualizar OU criar CentroFinanceiroEndereco e Endereco.
	atualizarEnderecos = (listaCfEndereco, centroFinanceiro) => {
		let contador = 0;

		listaCfEndereco.forEach(cfEndereco => {
			cfEndereco.centroFinanceiro = centroFinanceiro;
			let sCentroFinanceiroEndereco;

			// atualizar endereço
			if (cfEndereco.codigo) {
				sCentroFinanceiroEndereco = cfEndereco.$update({ id: cfEndereco.codigo });

			// criar novo endereço
			} else {
				sCentroFinanceiroEndereco = cfEndereco.$save();
			}

			sCentroFinanceiroEndereco.then(function (dataCentroFinanceiroEndereco) {

				contador++;
				if (contador === listaCfEndereco.length) {
					removerEnderecos();
				}

			}).catch(function(responseCentroFinanceiroEndereco) {
				$rootScope.loadingProgress = false;
				$scope.$emit('crud-critical', 'save', responseCentroFinanceiroEndereco);
			});

		});
	};

	removerEnderecos = () => {
		if ($ctrl.listaEnderecoParaRemover && $ctrl.listaEnderecoParaRemover.length > 0) {

			let listaComIDs = [];

			$ctrl.listaEnderecoParaRemover
				.filter(cfEndereco => { return cfEndereco.codigo; } )
				.forEach(cfEndereco => { listaComIDs.push(cfEndereco); });

			if (listaComIDs.length > 0) {

				let contador = 0;

				listaComIDs.forEach(cfEndereco => {

					const pCentroFinanceiroEndereco = cfEndereco.$delete({ id: cfEndereco.codigo });

					pCentroFinanceiroEndereco.then(function (dataCentroFinanceiroEndereco) {

						let objEndereco = new Endereco();
						objEndereco.codigo = dataCentroFinanceiroEndereco.endereco.codigo;

						const pEndereco = objEndereco.$delete({ id: objEndereco.codigo });

						pEndereco.then(function (dataEndereco) {

							contador++;
							if (contador === listaComIDs.length) {
								finalizarEdicaoComSucesso();
							}

						}).catch(function(responseEndereco) {
							$rootScope.loadingProgress = false;
							$scope.$emit('crud-critical', 'save', responseEndereco);
						});

					}).catch(function(responseCentroFinanceiroEndereco) {
						$rootScope.loadingProgress = false;
						$scope.$emit('crud-critical', 'save', responseCentroFinanceiroEndereco);
					});
				});

			} else {
				finalizarEdicaoComSucesso();
			}

		} else {
			finalizarEdicaoComSucesso();
		}
	};

	finalizarEdicaoComSucesso = () => {
		$scope.notification.success(undefined, 'Centro Financeiro atualizado com sucesso.');
		if ($stateParams.from === 'contrato') {
			$state.go('contratos.view', { objectId: $stateParams.objectIdFrom });
		} else {
			$state.go('centrosfinanceiros.view', { objectId: $ctrl.object.codigo });
		}
		$rootScope.loadingProgress = false;
	};


	/*
	* ====================================
	*           -:: EXIBIÇÃO ::-
	* ====================================
	*/

	initView = (object) => {

		carregarIdentificadorView(object);
		carregarTelContatoView(object);

		let temContrato = verificarContrato(object);

		carregarAbaEndereco(object);

		renderizarBotoes(temContrato);
		desabilitarBotoes(temContrato);

		//Apenas quando tiver contrato
		if (temContrato) {
			carregarAbaAssinaturas(object);
			carregarAbaReajuste(object);
			carregarAbaFecho(object);
		}
	};

	//Inicia o campo Identificador para o modo VIEW
	carregarIdentificadorView = (object) => {
		object.varIdentificador = '';

		let parte3 = object.identificador.trim().substring(object.identificador.length - 4);

		if (object.pai && object.pai.codigo) {

			const pGerencia = Gerencia.getGerenciaSimplesById({id: object.pai.codigo }).$promise;
			pGerencia.then(function(data) {
				let gerencia = data;
				let parte1 = gerencia.diretoria.numero;
				let parte2 = gerencia.numero;
				object.varIdentificador = `${parte1}.${parte2}.${parte3}`;

			}).catch(function(response) {
				object.varIdentificador = '';
				$scope.$emit('crud-critical', 'load', response);
			});

		} else {
			object.varIdentificador = object.varIdentificador.concat(parte3);
		}
	};

	// Inicia o campo Tel. contato para o modo VIEW
	// Tambem usado no modo NEW/EDIT
	carregarTelContatoView = (object) => {
		object.telefone = '';

		if (object && object.contatoAA && object.contatoAA.codigo) {

			let param = {};
			let filtro = { pessoa: object.contatoAA };

			const pTelefone = Telefone.buscarListaTelefone(filtro).$promise;
			pTelefone.then(function(data) {

				data.forEach(tel => {
					if (tel.principal) {
						let telView = '';

						if (tel.numero) {
							if (tel.tipo) {
								telView = `${tel.tipo}`;
							}
							if (tel.ddd) {
								telView = `${telView} - (${tel.ddd}) ${tel.numero}`;
							}
							if (tel.ramal) {
								telView = `${telView} r. ${tel.ramal}`;
							}

							object.telefone = telView;
						}
					}
				});

			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});
		}
	};

	verificarContrato = (object) => {
		object.isAssociadoContrato = false;

		if (object.contratoTO && object.contratoTO.codigo) {
			object.isAssociadoContrato = true;
		}

		return object.isAssociadoContrato;
	};

	/*
	* --------
	*  BOTÕES
	* --------
	*/

	$ctrl.retornar = () => {
		if ($stateParams && $stateParams.from === 'contrato') {
			$state.go('contratos.view', {objectId: $ctrl.object.contratoTO.codigo});
		} else {
			$state.go('centrosfinanceiros.list');
		}
	};

	$ctrl.editar = () => {
		$state.go('centrosfinanceiros.edit', { objectId: $ctrl.object.codigo });
	};

	$ctrl.imprimirOS = () => {
		$state.go('centrosfinanceiros.imprimir_os_inicial', { objectId: $ctrl.object.codigo });
	};

	$ctrl.inativar = () => {

		let funcaoCancelar = (fnHide) => {
			fnHide();
		};

		let funcaoConfirmar = (object, fnHide) => {

			const pCentroFinanceiro = CentroFinanceiro.inativarById({id: object.codigo}).$promise;
			pCentroFinanceiro.then(function(data) {
				loadObj();
				$scope.notification.success('', 'Centro Financeiro inativado com sucesso.');
				verificarPrestacaoConta(object.codigo);
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});

			fnHide();
		};

		DialogConfirm.show(
			undefined,
			$ctrl.object,
			'Inatilet centro financeiro: Confirmação',
			'Deseja realmente inatilet este centro financeiro?',
			'Confirmar',
			'Cancelar',
			funcaoConfirmar,
			funcaoCancelar);
	};

	$ctrl.reativar = () => {

		let funcaoCancelar = (fnHide) => {
			fnHide();
		};

		let funcaoConfirmar = (object, fnHide) => {

			const pCentroFinanceiro = CentroFinanceiro.reativarById({id: object.codigo}).$promise;
			pCentroFinanceiro.then(function(data) {
				loadObj();
				$scope.notification.success('', 'Centro Financeiro reativado com sucesso.');
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});

			fnHide();
		};

		if ($ctrl.object.situacao === 'Paralisado' || $ctrl.object.situacao === 'Inativo' || $ctrl.object.situacao === 'Encerrado' || $ctrl.object.situacao === 'Encerrado, mas com faturamento') {
			DialogConfirm.show(
				undefined,
				$ctrl.object,
				'Reativar Centro Financeiro: Confirmação',
				'Deseja realmente reativar este centro financeiro?',
				'Confirmar',
				'Cancelar',
				funcaoConfirmar,
				funcaoCancelar);

		} else {
			$scope.notification.atencao('', 'Operação não permitida. Este centro financeiro já foi atualizado por outro usuário.');
		}
	};

	$ctrl.remover = () => {

		let funcaoCancelar = function(fnHide) {
			fnHide();
		};

		let funcaoConfirmar = function(object, fnHide) {

			const pCentroFinanceiro = CentroFinanceiro.removerById({id: object.codigo}).$promise;
			pCentroFinanceiro.then(function(data) {
				$scope.notification.success('', 'Centro Financeiro removido com sucesso.');
				fnHide();
				$ctrl.retornar();
			}).catch(function(response) {
				fnHide();
				$scope.notification.error('', 'Este Centro Financeiro não pode ser removido.');
			});
		};

		DialogConfirm.show(
			undefined,
			$ctrl.object,
			'Remover Centro Financeiro: Confirmação',
			'Deseja realmente remover este centro financeiro (esta operação não poderá ser desfeita)?',
			'Confirmar',
			'Cancelar',
			funcaoConfirmar,
			funcaoCancelar);
	};

	$ctrl.paralisar = () => {

		let funcaoCancelar = (fnHide) => {
			fnHide();
		};

		let funcaoConfirmar = (object, fnHide) => {

			const pCentroFinanceiro = CentroFinanceiro.paralisarById({id: object.codigo}).$promise;
			pCentroFinanceiro.then(function(data) {
				fnHide();
				loadObj();
				$scope.notification.success('', 'Centro Financeiro paralisado com sucesso.');
				verificarPrestacaoConta(object.codigo);
			}).catch(function(response) {
				fnHide();
				$scope.notification.error('', 'Operação não permitida. Este centro financeiro já foi atualizado por outro usuário.');
			});
		};

		DialogConfirm.show(
			undefined,
			$ctrl.object,
			'Paralisar Centro Financeiro: Confirmação',
			'Deseja realmente paralisar este centro financeiro?',
			'Confirmar',
			'Cancelar',
			funcaoConfirmar,
			funcaoCancelar);
	};

	$ctrl.encerrar = () => {
		showDialogEncerrar();
	};

	let confirmarEncerramento = () => {
		CentroFinanceiro.encerrarById({id: $ctrl.object.codigo}).$promise.then(function(data) {
			loadObj();
			$scope.notification.success('', 'Centro Financeiro encerrado com sucesso.');
			verificarPrestacaoConta($ctrl.object.codigo);
		}).catch(function(response) {
			$scope.notification.error('', 'Operação não permitida. Este centro financeiro já foi atualizado por outro usuário.');
		});
	};

	let confirmarEncerramentoFat = () => {
		CentroFinanceiro.encerrarFatById({id: $ctrl.object.codigo}).$promise.then(function(data) {
			loadObj();
			$scope.notification.success('', 'Centro Financeiro encerrado com sucesso.');
			verificarPrestacaoConta($ctrl.object.codigo);
		}).catch(function(response) {
			$scope.notification.error('', 'Operação não permitida. Este centro financeiro já foi atualizado por outro usuário.');
		});
	};

	let showDialogEncerrar = () => {
		$mdDialog.show({
			controller: DialogEncerrar,
			locals: {
				fnConfirmarEncerramento: confirmarEncerramento,
				fnConfirmarEncerramentoFat: confirmarEncerramentoFat,
				showBtnFat: 'Encerrado, mas com faturamento' !== $ctrl.object.situacao
			},
			templateUrl: 'partials/comercial/cadastros/centrosfinanceiros/html/dialogs/dialog_encerramento.html',
			parent: angular.element(document.body),
			clickOutsideToClose: false,
			fullscreen: false
		});
	};

	let DialogEncerrar = ($scope, $mdDialog, fnConfirmarEncerramento, fnConfirmarEncerramentoFat, showBtnFat) => {

		init = () => {
			$scope.showBtnFat = showBtnFat;
		};
		init();

		$scope.hide = () => {
			$mdDialog.hide();
		};

		$scope.cancel = () => {
			$mdDialog.cancel();
		};

		$scope.confirmFat = () => {
			fnConfirmarEncerramentoFat();
			$mdDialog.cancel();
		};

		$scope.confirm = () => {
			fnConfirmarEncerramento();
			$mdDialog.cancel();
		};
	};

	renderizarBotoes = (temContrato) => {
		renderizarBotaoInativar(temContrato);
		renderizarBotaoReativar(temContrato);
		renderizarBotaoAlterarFecho(temContrato);
		renderizarBotaoRemover(temContrato);
		renderizarBotaoParalisar(temContrato);
		renderizarBotaoCancelar(temContrato);
		renderizarBotaoEncerrar(temContrato);
		renderizarBotaoNovoCL(temContrato);
		renderizarBotaoImprimirOS(temContrato);
		renderizarBotaoEditar();
	};

	desabilitarBotoes = (temContrato) => {
		desabilitarBotaoEditar(temContrato);
	};

	renderizarBotaoInativar = (temContrato) => {
		$ctrl.showBtnInativar = false;

		if (!temContrato) {
			if ($ctrl.object.situacao === 'Ativo') {
				let listaPerfil = ['admin-contrato','analista-cf'];

				const pUsuario = UsuarioLogadoService.possuiTodosPerfisLista(listaPerfil).$promise;
				pUsuario.then(function(data) {
					$ctrl.showBtnInativar = data.possuiTodosPerfisLista;
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'load', response);
				});
			}
		}
	};

	renderizarBotaoReativar = (temContrato) => {
		$ctrl.showBtnReativar = false;

		if (temContrato && $ctrl.object.situacao === 'Paralisado' || $ctrl.object.situacao === 'Encerrado' || $ctrl.object.situacao === 'Encerrado, mas com faturamento') {
			let listaPerfil = ['admin-contrato'];

			const pUsuario = UsuarioLogadoService.possuiTodosPerfisLista(listaPerfil).$promise;
			pUsuario.then(function(data) {
				$ctrl.showBtnReativar = data.possuiTodosPerfisLista;
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});

		} else if ($ctrl.object.situacao === 'Inativo') {
			let listaPerfil = ['admin-contrato','analista-cf'];
			const pUsuario = UsuarioLogadoService.possuiTodosPerfisLista(listaPerfil).$promise;

			pUsuario.then(function(data) {
				$ctrl.showBtnReativar = data.possuiTodosPerfisLista;
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});
		}
	};

	criarBotaoFecho = (returnFecho, label) => {
		$ctrl.labelButtonFecho = label;
		if (returnFecho && returnFecho.codigo) {
			$ctrl.showBtnAlterarFecho = true;
		} else {
			$ctrl.showBtnCriarFecho = true;
		}
	}

	renderizarBotaoAlterarFecho = (temContrato) => {
		$ctrl.showBtnAlterarFecho = false;
		$ctrl.showBtnCriarFecho = false;

		if (temContrato) {
			let listaPerfil = ['admin-contrato'];
			const pUsuario = UsuarioLogadoService.possuiTodosPerfisLista(listaPerfil).$promise;

			pUsuario.then(function(data) {

				if (data.possuiTodosPerfisLista) {
					const pFecho = Fecho.fechoByCodCentroFinanceiro({id: $ctrl.object.codigo}).$promise;
					pFecho.then(function(returnFecho) {

						if ($ctrl.object.situacao === 'Em elaboração' || $ctrl.object.situacao === 'Ativo') {
							if (returnFecho && returnFecho.codigo) {
								$ctrl.showBtnAlterarFecho = true;
							} else {
								$ctrl.showBtnCriarFecho = true;
							}
						}

					}).catch(function(response2) {
						$scope.$emit('crud-critical', 'load', response2);
					});
				}

			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});
		}
	};

	renderizarBotaoRemover = (temContrato) => {
		$ctrl.showBtnRemover = false;

		if ($ctrl.object.situacao === 'Em elaboração' || $ctrl.object.situacao === 'Em revisão' ) {
			$ctrl.showBtnRemover = true;
		} else {
			UsuarioLogadoService.possuiTodosPerfisLista(['admin-contrato']).$promise
			.then(function(data) {
				$ctrl.showBtnRemover = data.possuiTodosPerfisLista;
				return UsuarioLogadoService.possuiTodosPerfisLista(['analista-cf']).$promise;
			})
			.then((data) => {
				$ctrl.showBtnRemover = data.possuiTodosPerfisLista;
			})
			.catch(function(response) {
				$ctrl.$emit('crud-critical', 'load', response);
			});
		}
	};

	renderizarBotaoParalisar = (temContrato) => {
		$ctrl.showBtnParalisar = false;

		if (temContrato && $ctrl.object.situacao === 'Ativo') {

			let listaPerfil = ['admin-contrato'];

			const pUsuario = UsuarioLogadoService.possuiTodosPerfisLista(listaPerfil).$promise;
			pUsuario.then(function(data) {
				$ctrl.showBtnParalisar = data.possuiTodosPerfisLista;
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});
		}
	};

	renderizarBotaoCancelar = (temContrato) => {
		$ctrl.showBtnCancelar = false;

		if (temContrato && $ctrl.object.situacao !== 'Encerrado' && $ctrl.object.situacao !== 'Cancelado') {

			let listaPerfil = ['admin-contrato'];

			const pUsuario = UsuarioLogadoService.possuiTodosPerfisLista(listaPerfil).$promise;
			pUsuario.then(function(data) {
				$ctrl.showBtnCancelar = data.possuiTodosPerfisLista;
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});
		}
	};

	renderizarBotaoEncerrar = (temContrato) => {
		$ctrl.showBtnEncerrar = false;

		if (temContrato && ($ctrl.object.situacao === 'Ativo' || $ctrl.object.situacao === 'Paralisado' || $ctrl.object.situacao === 'Encerrado, mas com faturamento') ) {

			let listaPerfil = ['admin-contrato'];

			const pUsuario = UsuarioLogadoService.possuiTodosPerfisLista(listaPerfil).$promise;
			pUsuario.then(function(data) {
				$ctrl.showBtnEncerrar = data.possuiTodosPerfisLista;
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});
		}
	};

	//TODO: Não renderizar o botão até a sua funcionalidade ser implementado.
	renderizarBotaoNovoCL = (temContrato) => {
		$ctrl.showBtnNovoCL = false;

		//TODO: Código OK. Aguardando a implementação do botão 'Novo CL' para renderizar o botão.
		/*if (temContrato) {

			if ($ctrl.object.situacao !== 'Paralisado' && $ctrl.object.situacao !== 'Inativo'
				&& $ctrl.object.situacao !== 'Encerrado' && $ctrl.object.situacao !== 'Cancelado'
					&& $ctrl.object.situacao !== 'Reativado' && $ctrl.object.situacao !== 'Criado') {

				let listaPerfil = ['admin-contrato'];
				let pUsuario = UsuarioLogadoService.possuiTodosPerfisLista(listaPerfil).$promise;
				pUsuario.then(function(data) {
					$scope.showBtnNovoCL = data.possuiTodosPerfisLista;
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'load', response);
				});
			}
		}*/
	};

	renderizarBotaoImprimirOS = (temContrato) => {
		$ctrl.showBtnImprimirOS = false;
		if (temContrato) {
			$ctrl.showBtnImprimirOS = true;
		}
	};

	renderizarBotaoEditar = () => {
		$ctrl.showBtnEditar = false;

		let listaPerfil = ['admin-contrato'];
		const pUsuario = UsuarioLogadoService.possuiTodosPerfisLista(listaPerfil).$promise;
		pUsuario.then(function(data) {
			$ctrl.showBtnEditar = data.possuiTodosPerfisLista;
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});
	};

	desabilitarBotaoEditar = (temContrato) => {
		$ctrl.btnEditarDesabilitado = true;

		if (temContrato) {
			let listaPerfil = ['admin-contrato'];
			const pUsuario = UsuarioLogadoService.possuiTodosPerfisLista(listaPerfil).$promise;
			pUsuario.then(function(data) {
				if (data.possuiTodosPerfisLista) {
					if ($ctrl.object.situacao === 'Ativo' || $ctrl.object.situacao === 'Em elaboração' || $ctrl.object.situacao === 'Em revisão') {
						$ctrl.btnEditarDesabilitado = false;
					}
				}
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});

		} else {
			let listaPerfil = ['mantenedor-compras'];
			const pUsuario = UsuarioLogadoService.possuiTodosPerfisLista(listaPerfil).$promise;
			pUsuario.then(function(data) {
				$ctrl.btnEditarDesabilitado = !data.possuiTodosPerfisLista;
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});
		}
	};

	// DIALOG
	$ctrl.chamarDialogMotivoRetorno = () => {
		cancelarCentroFinanceiro();
	};

	cancelarCentroFinanceiro = () => {
		$mdDialog.show({
			controller: DialogMotivoRetorno,
			locals: {
				notification: $scope.notification,
				MotivoRetorno: MotivoRetorno,
				CentroFinanceiro: CentroFinanceiro,
				MotivoRetornoDocumento: MotivoRetornoDocumento,
				MotivoAplicacao: MotivoAplicacao,
				MotivoRetornoCentroFinanceiro: MotivoRetornoCentroFinanceiro,
				MotivoDocumento: MotivoDocumento,
				objCentroFinanceiro: $ctrl.object,
				funcaoLoadObj: loadObj,
				funcaoVerificarPrestacaoConta: verificarPrestacaoConta
			},
			templateUrl: 'partials/comercial/cadastros/centrosfinanceiros/html/dialogs/dialog_motivo_retorno.html',
			parent: angular.element(document.body),
			clickOutsideToClose: false,
			fullscreen: false
		});
	};

	DialogMotivoRetorno = ($scope, $mdDialog, notification, MotivoRetorno, CentroFinanceiro, MotivoRetornoDocumento, MotivoAplicacao, MotivoRetornoCentroFinanceiro, MotivoDocumento, objCentroFinanceiro, funcaoLoadObj, funcaoVerificarPrestacaoConta) => {

		init();

		function init() {
			$scope.title = 'Motivos de Retorno - Cancelar centro financeiro';
			$scope.chaveCancelar = 'cf-cancelar';

			$scope.motivoRetornoDocumento = new MotivoRetornoDocumento();
			$scope.motivo = undefined;
			$scope.listaMotivo = [];
			$scope.listaMotivoEscolhido = [];

			buscarListaMotivoRetorno();
		}

		function buscarListaMotivoRetorno() {
			const pMotivoRetorno = MotivoRetorno.buscarMotivoRetornoByChave({chave: $scope.chaveCancelar}).$promise;

			pMotivoRetorno.then(function(data) {
				if (data && data.length > 0) {
					$scope.listaMotivo.push(...data);
				}
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});
		}

		$scope.selecionarMotivo = () => {
			$scope.listaMotivoEscolhido.push($scope.motivo);

			let index = $scope.listaMotivo.indexOf($scope.motivo);
			$scope.listaMotivo.splice(index, 1);

			$scope.motivo = undefined;
		};

		$scope.removerMotivo = (motivo) => {
			let index = $scope.listaMotivoEscolhido.indexOf(motivo);
			$scope.listaMotivoEscolhido.splice(index, 1);
			$scope.listaMotivo.push(motivo);
		};

		$scope.confirm = () => {

			// Atualizar centro financeiro com situação cancelado
			const pCentroFinanceiro = CentroFinanceiro.cancelarById({id: objCentroFinanceiro.codigo}).$promise;
			pCentroFinanceiro.then(function(data1) {

				// buscar MotivoAplicacao, pois ele é um dos atributos do objeto MotivoRetornoDocumento
				const pMotivoAplicacao = MotivoAplicacao.buscarByChave({chave: $scope.chaveCancelar}).$promise;
				pMotivoAplicacao.then(function(data2) {

					$scope.motivoRetornoDocumento.aplicacao = data2;

					// salvar objeto MotivoRetornoDocumento
					let sMotivoRetornoDocumento = $scope.motivoRetornoDocumento.$save();
					sMotivoRetornoDocumento.then(function (data3) {

						// criar objeto para salvar na tabela de relacionamento motivo_retorno_documento_cf
						let objMotivoRetornoCentroFinanceiro = new MotivoRetornoCentroFinanceiro();
						objMotivoRetornoCentroFinanceiro.centroFinanceiro = objCentroFinanceiro;
						objMotivoRetornoCentroFinanceiro.motivoRetornoDocumento = data3;

						//se tiver escolhido MotivoRetorno(s) adiciona eles na lista do MotivoRetornoDocumento
						if ($scope.listaMotivoEscolhido && $scope.listaMotivoEscolhido.length > 0) {
							objMotivoRetornoCentroFinanceiro.motivoRetornoDocumento.listaMotivoRetorno = [];

							$scope.listaMotivoEscolhido.forEach(item => {
								let objMotivoDocumento = new MotivoDocumento();
								objMotivoDocumento.motivoRetornoDocumento = {codigo: data3.codigo};
								objMotivoDocumento.motivoRetorno = item;

								objMotivoRetornoCentroFinanceiro.motivoRetornoDocumento.listaMotivoRetorno.push(objMotivoDocumento);
							});
						}

						// salvar objeto MotivoRetornoCentroFinanceiro (tambem salva o(s) MotivoDocumento(s), caso tenha sido escolhido(s) MotivoRetorno(s))
						let sMotivoRetornoCentroFinanceiro = objMotivoRetornoCentroFinanceiro.$save();
						sMotivoRetornoCentroFinanceiro.then(function (data4) {

							funcaoLoadObj();
							notification.success('', 'Centro Financeiro cancelado com sucesso.');
							funcaoVerificarPrestacaoConta(objCentroFinanceiro.codigo);
							$mdDialog.hide();

						}).catch(function(response4) {
							$mdDialog.hide();
							notification.error('', 'Erro ao cancelar centro financeiro.');
						});

					}).catch(function(response3) {
						$mdDialog.hide();
						notification.error('', 'Erro ao cancelar centro financeiro.');
					});

				}).catch(function(response2) {
					$mdDialog.hide();
					notification.error('', 'Erro ao cancelar centro financeiro.');
				});

			}).catch(function(response1) {
				$mdDialog.hide();
				notification.error('', 'Erro ao cancelar centro financeiro.');
			});
		};

		$scope.hide = function() {
			$mdDialog.hide();
		};

		$scope.cancel = function() {
			$mdDialog.cancel();
		};
	};

	// Verifica se existem contas de prestação de contas ativas (tabela 'fundo_fixo') para o este CL (campo 'codigo_centro_financeiro').
	verificarPrestacaoConta = (codigoCentroFinanceiro) => {

		const pFundoFixo = FundoFixo.buscarQtdFundoFixoPorCfId({id: codigoCentroFinanceiro}).$promise;

		pFundoFixo.then(function(data) {

			let quantidadeContas = data.qtd;

			if (quantidadeContas > 0) {
				$scope.notification.atencao('', 'Este centro financeiro possui conta(s) em prestação de contas ativa(s). Informe ao Gestor da Prestação de Contas para ajustar esta(s) conta(s) para um novo centro financeiro.');
			}

		}).catch(function(response) {
			$mdDialog.hide();
			$scope.notification.error('', 'Erro ao verificar prestação de conta.');
		});
	};

	/*
	* ==================
	*     :: ABAs ::
	* ==================
	*/

	/*
	* ----------------
	*  ABA CONTRATO:
	* ----------------
	*/

	const buscarContratoParaNovoCf = (codigoContrato) => {
		const pContrato = Contrato.get({id: codigoContrato}).$promise;
		pContrato.then(function(data) {
			$ctrl.object.contratoTO = data;

		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});
	};


	/*
	* ----------------
	*  ABA ENDEREÇOS:
	* ----------------
	*/

	carregarAbaEndereco = (object) => {
		object.listaEnderecos = [];

		const pCentroFinanceiroEndereco = () => CentroFinanceiroEndereco.buscarListaCentroFinanceiroEndereco({centroFinanceiro: {codigo: object.codigo}}).$promise;

		let pCentroFinanceiroEnderecoFN = data => {
			object.listaEnderecos.push(...data);
		};

		let catchError = response => {
			$scope.$emit('crud-critical', 'load', response);
			object.listaEnderecos = [];
		};

		return Promise.resolve().then(pCentroFinanceiroEndereco).then(pCentroFinanceiroEnderecoFN).catch(catchError);
	};

	// DIALOG
	$ctrl.chamarDialogNovoEndereco = () => {
		adicionarNovoEndereco();
	};

	adicionarNovoEndereco = () => {
		let objCentroFinanceiroEndereco = new CentroFinanceiroEndereco();

		objCentroFinanceiroEndereco.centroFinanceiro = {}; //nao colocar o centro financeiro por enquanto, para evitar o bug da estrutura recursiva
		objCentroFinanceiroEndereco.endereco = undefined;
		objCentroFinanceiroEndereco.tipo = undefined;

		$mdDialog.show({
			controller: DialogEndereco,
			locals: {
				notification: $scope.notification,
				Endereco: Endereco,
				Cidade: Cidade,
				EnderecoService: EnderecoService,
				fnAdicionarEndereco: adicionarEndereco,
				cfEndereco: objCentroFinanceiroEndereco
			},
			templateUrl: 'partials/comercial/cadastros/centrosfinanceiros/html/dialogs/dialog_endereco.html',
			parent: angular.element(document.body),
			clickOutsideToClose: false,
			fullscreen: false
		});
	};

	$ctrl.editarEndereco = (ev, objCfEndereco) => {
		$mdDialog.show({
			controller: DialogEndereco,
			locals: {
				Endereco: Endereco,
				Cidade: Cidade,
				fnAdicionarEndereco: adicionarEndereco,
				cfEndereco: objCfEndereco
			},
			templateUrl: 'partials/comercial/cadastros/centrosfinanceiros/html/dialogs/dialog_endereco.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose: false,
			fullscreen: false
		});
	};

	DialogEndereco = ($scope, $mdDialog, Endereco, Cidade, EnderecoService, fnAdicionarEndereco, cfEndereco) => {
		let novoEndereco = true;

		init = () => {
			$scope.title = 'Endereço';
			$scope.selectedItemCidade = undefined;

			if (cfEndereco.endereco) {
				novoEndereco = false;
			}

			if (novoEndereco) {
				$scope.object = cfEndereco;
				$scope.object.tipo = 'Outros';
				$scope.object.principal = false;
				$scope.object.endereco = new Endereco();
				$scope.object.endereco.tipoEndereco = 'Outros';
				$scope.object.endereco.endereco = undefined;

			} else {
				$scope.object = angular.copy(cfEndereco);
				$scope.selectedItemCidade = $scope.object.endereco.cidade;
			}

			initCampoTipoLogradouro();
		};

		initCampoTipoLogradouro = () => {

			$scope.listaTipo = [];

			const pEndereco = Endereco.buscarTipoLogradouro().$promise;
			pEndereco.then(function(data) {
				$scope.listaTipo.push({codigo: undefined, nome: ''});
				$scope.listaTipo.push(...data);
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});
		};

		init();

		$scope.changeTipoLogr = () => {
			if (!$scope.object.endereco.tipoLogradouro || !$scope.object.endereco.tipoLogradouro.codigo) {
				delete $scope.object.endereco.tipoLogradouro;
			}
		};

		$scope.querySearchCidade = (query) => {
			let nomeCidade = query;

			const pCidade = Cidade.buscarListaPorNome(nomeCidade).$promise;

			return pCidade.then(function(data) {
				let listaCidade = [];
				listaCidade.push(...data);
				return listaCidade;

				}).catch(function(response) {
					$scope.$emit('crud-critical', 'load', response);
					return [];
				});
		};

		$scope.selectedItemChange = (item) => {
			$scope.object.endereco.cidade = item;
		};

		$scope.confirm = () => {
			$scope.object.endereco.uf = $scope.object.endereco.cidade.uf;

			if (novoEndereco) {
				fnAdicionarEndereco($scope.object);
			} else {
				$scope.object.endereco.enderecoCompleto = EnderecoService.montarEnderecoCompleto($scope.object.endereco);
				angular.merge(cfEndereco.endereco, $scope.object.endereco);
			}

			$mdDialog.hide();
		};

		$scope.hide = () => {
			$mdDialog.hide();
		};

		$scope.cancel = () => {
			$mdDialog.cancel();
		};
	};

	adicionarEndereco = (objCfEndereco) => {
		if (objCfEndereco && objCfEndereco.endereco) {
			objCfEndereco.endereco.enderecoCompleto = EnderecoService.montarEnderecoCompleto(objCfEndereco.endereco);
			$ctrl.object.listaEnderecos.push(objCfEndereco);
		}
	};

	$ctrl.excluirEndereco = (objCfEndereco) => {
		$ctrl.listaEnderecoParaRemover.push(objCfEndereco);
		let index = $ctrl.object.listaEnderecos.indexOf(objCfEndereco);
		$ctrl.object.listaEnderecos.splice(index, 1);
	};

	//CHECKBOX
	$ctrl.exists = (cfEndereco) => {
		return cfEndereco && cfEndereco.endereco && cfEndereco.endereco.principal;
	};

	//CHECKBOX
	$ctrl.selecionar = (cfEndereco) => {
		let index = $ctrl.object.listaEnderecos.indexOf(cfEndereco);

		if ($ctrl.object && $ctrl.object.listaEnderecos) {

			if ($ctrl.object.listaEnderecos[index].endereco.principal === true) {
				$ctrl.object.listaEnderecos[index].endereco.principal = false;

			} else {
				$ctrl.object.listaEnderecos.forEach(item => { item.endereco.principal = false; });
				$ctrl.object.listaEnderecos[index].endereco.principal = true;
			}
		}
	};

	/*
	* -----------
	*  ABA FECHO:
	* -----------
	*/

	carregarAbaFecho = (object) => {

		object.fecho = { dataReajuste: undefined, percentualImposto: undefined, prazo: undefined, custoMensal: 0, bdi: 0, tributos: 0, valorMensal: 0, valorTotal: 0 };

		const pFecho = Fecho.fechoByCodCentroFinanceiro({id: object.codigo}).$promise;
		pFecho.then(function(data) {

			if (data && data.dataReajuste) {
				object.fecho.dataReajuste = data.dataReajuste;
			} else {
				object.fecho.dataReajuste = new Date();
			}

			object.fecho.percentualImposto = data.percentualImposto;

			if (object.contratoTO.prazoAtual === 0) {
				object.fecho.prazo = 1;
			} else {
				object.fecho.prazo = object.contratoTO.prazoAtual;
			}

			if (data && data.codigo) {
				object.fecho.codigo = data.codigo;
				carregarFechoMaoDeObra(data);
			} else {
				if (isTelaImpressao) {
					carregarDadosParaImpressao();
				}
			}

		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});

	};

	calcularValoresDoFecho = () => {
		$ctrl.object.fecho.valorMensal = $ctrl.object.fecho.custoMensal + $ctrl.object.fecho.bdi + $ctrl.object.fecho.tributos;

		if ($ctrl.object.fecho.prazo) {
			$ctrl.object.fecho.valorTotal = $ctrl.object.fecho.valorMensal * $ctrl.object.fecho.prazo;
		} else {
			$ctrl.object.fecho.valorTotal = $ctrl.object.fecho.valorMensal;
		}

		if (isTelaImpressao) {
			carregarDadosParaImpressao();
		}
	};

	$ctrl.alterarFecho = () => {
		if ($ctrl.object.situacao === 'Ativo') {
			let funcaoCancelar = function(fnHide) {
				fnHide();
			};

			let funcaoConfirmar = function(object, fnHide) {
				$state.go('centrosfinanceiros.fecho_edit', { objectId: $ctrl.object.codigo });
				fnHide();
			};

			let objeto = {};

			let msg = '';
		    let title = '';

			if ($ctrl.labelButtonFecho === 'Alterar fecho') {
				msg = 'alterar';
				title = 'Alterar';
			} else {
				msg = 'criar';
				title = 'Criar';
			}

			//FIXME: Verificar se ainda vai ser exibido este dialog
			DialogConfirm.show(
				undefined,
				objeto,
				`${title} fecho: Confirmação`,
				`Deseja realmente <b>${msg}</b> este fecho <b>(esta alteração requer novas assinaturas)</b>?`,
				'Confirmar',
				'Cancelar',
				funcaoConfirmar,
				funcaoCancelar);

		} else {
			// 'Em elaboração'
			$state.go('centrosfinanceiros.fecho_edit', { objectId: $ctrl.object.codigo });
		}
	};

	$ctrl.criarFecho = () => {
		let funcaoCancelar = function(fnHide) {
			fnHide();
		};

		let funcaoConfirmar = function(object, fnHide) {
			$state.go('centrosfinanceiros.fecho_new', { objectId: $ctrl.object.codigo });
			fnHide();
		};

		let msg = '';
		let title = '';

		if ($ctrl.labelButtonFecho === 'Alterar fecho') {
			msg = 'alterar';
			title = 'Alterar';
		} else {
			msg = 'criar';
			title = 'Criar';
		}

		DialogConfirm.show(
			undefined,
			{},
			`${title} fecho: Confirmação`,
			`Deseja realmente <b>${msg}</b> este fecho <b>(esta alteração requer novas assinaturas)</b>?`,
			'Confirmar',
			'Cancelar',
			funcaoConfirmar,
			funcaoCancelar);
	};

	/*
	*  -----------------------------
	*   ABA FECHO / ABA MÃO DE OBRA
	*  -----------------------------
	*/

	carregarFechoMaoDeObra = (fecho) => {
		$ctrl.subTotalMaoDeObra = 0;
		$ctrl.totalEncargosSociais = 0;
		$scope.totalFechoMaoDeObra = 0;
		$ctrl.percentualEncargosSociais = fecho.percentualEncargosSociais;

		const pMaoDeObra = FechoMaoDeObra.maoDeObraByCodFecho({id: fecho.codigo}).$promise;
		pMaoDeObra.then(function(data) {
			$ctrl.listaFechoMaoDeObra = data;

			$ctrl.listaFechoMaoDeObra.forEach(maoDeObra => {
				maoDeObra.totalAdicionais = FechoMaoDeObraService.getTotalAdicionais(maoDeObra.listaMaoDeObraAdicional);
				maoDeObra.valorTotal = FechoMaoDeObraService.getValorTotal(maoDeObra);
				$ctrl.subTotalMaoDeObra += maoDeObra.valorTotal;

				//caso o FechoMaoDeObra nao tenha MaoDeObraAdicional é preciso adicionar eles para serem exibidos no dialog 'Adicionais de mão de obra'
				if (maoDeObra.listaMaoDeObraAdicional.length === 0) {
					adicionarListaMaoDeObraAdicional(maoDeObra);
				}
			});

			$ctrl.totalEncargosSociais = FechoMaoDeObraService.getTotalEncargosSociais($ctrl.percentualEncargosSociais, $ctrl.subTotalMaoDeObra);
			$scope.totalFechoMaoDeObra = FechoMaoDeObraService.getTotal($ctrl.totalEncargosSociais, $ctrl.subTotalMaoDeObra);

			if (isTelaImpressao) {
				$scope.subTotalMaoDeObraContratacaoDireta = FechoMaoDeObraService.getSubTotalMaoDeObraContratacao($ctrl.listaFechoMaoDeObra, 'Direta');
				$scope.subTotalMaoDeObraContratacaoDiretaPlantao = FechoMaoDeObraService.getSubTotalMaoDeObraContratacao($ctrl.listaFechoMaoDeObra, 'Direta (Plantão)');
				$scope.subTotalMaoDeObraContratacaoIndireta = FechoMaoDeObraService.getSubTotalMaoDeObraContratacao($ctrl.listaFechoMaoDeObra, 'Indireta');
				$scope.subTotalMaoDeObraContratacaoIndiretaPlantao = FechoMaoDeObraService.getSubTotalMaoDeObraContratacao($ctrl.listaFechoMaoDeObra, 'Indireta (Plantão)');
			}

			carregarFechoGastosGerais(fecho);

		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});
	};

	$ctrl.chamarDialogMaoObraAdicional = (objMaoDeObra) => {
		chamarDialogMaoObraAdicional(objMaoDeObra);
	};

	chamarDialogMaoObraAdicional = (objMaoDeObra) => {

		$mdDialog.show({
			controller: DialogMaoDeObraAdicional,
			locals: {
				objMaoDeObra: objMaoDeObra
			},
			templateUrl: 'partials/comercial/cadastros/centrosfinanceiros/html/dialogs/dialog_mao_de_obra_adicional.html',
			parent: angular.element(document.body),
			clickOutsideToClose: false,
			fullscreen: false
		});
	};

	DialogMaoDeObraAdicional = ($scope, $mdDialog, objMaoDeObra) => {
		$scope.title = 'Adicionais de mão de obra';

		$scope.listaAdicional = objMaoDeObra.listaMaoDeObraAdicional;
		$scope.total = calcularTotal($scope.listaAdicional);

		function calcularTotal(lista) {
			let total = 0;

			lista.forEach(value => {
				total += value.valorUnitario;
			});

			return total;
		}

		$scope.cancel = function() {
			$mdDialog.cancel();
		};
	};

	adicionarListaMaoDeObraAdicional = (fechoMaoDeObra) => {

		const pCategoria = CategoriaMaoDeObraAdicional.buscarTodasCategorias().$promise;
		pCategoria.then(function(data) {
			let listaCategoriaAdicional = data;

			listaCategoriaAdicional.forEach(adicional => {
				let fechoMaoDeObraAdicional = new FechoMaoDeObraAdicional();
				fechoMaoDeObraAdicional.categoriaMaoDeObraAdicional = adicional;
				fechoMaoDeObraAdicional.valorUnitario = 0;

				fechoMaoDeObra.listaMaoDeObraAdicional.push(fechoMaoDeObraAdicional);
			});

		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});
	};

	/*
	*  -------------------------------
	*   ABA FECHO / ABA GASTOS GERAIS
	*  -------------------------------
	*/

	carregarFechoGastosGerais = (fecho) => {

		$ctrl.listaFechoGastosGerais = [];
		$ctrl.totalFechoGastosGerais = 0;

		$ctrl.object.fecho.custoMensal = 0;

		const pDespesa = FechoDespesaGeral.despesaByCodFecho({id: fecho.codigo}).$promise;
		pDespesa.then(function(data) {

			data.forEach(despesa => {
				despesa.descricaoCategoriaDespesaGeral = '';
				despesa.descricaoCategoriaDespesaGeral = FechoDespesaGeralService.getDescricaoCategoriaDespesaGeral(despesa);
			});

			$ctrl.listaFechoGastosGerais = FechoDespesaGeralService.filtrarListaPorTipo(data, 'Gasto geral');
			$ctrl.totalFechoGastosGerais = FechoDespesaGeralService.getTotalPorTipo(data, 'Gasto geral');
			$scope.totalFechoMaoDeObra = fecho.valorMaoObra == null ? $scope.totalFechoMaoDeObra : fecho.valorMaoObra;
//			$ctrl.object.fecho.custoMensal = FechoService.getCustoMensal($scope.totalFechoMaoDeObra, data);
			$ctrl.object.fecho.custoMensal = fecho.valorGastosGerais + fecho.valorMaoObra + fecho.valorMaterial + fecho.valorServicoTerceiro;

			carregarFechoMateriais(data);
			carregarFechoServicoTercerizado(data);
			carregarFechoBdi(fecho);

		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});
	};

	/*
	*  -------------------------------
	*   ABA FECHO / MATERIAIS
	*  -------------------------------
	*/

	carregarFechoMateriais = (listaFechoDespesaGeral) => {
		$ctrl.listaFechoMateriais = [];
		$ctrl.totalFechoMateriais = 0;

		$ctrl.listaFechoMateriais = FechoDespesaGeralService.filtrarListaPorTipo(listaFechoDespesaGeral, 'Material');
		$ctrl.totalFechoMateriais = FechoDespesaGeralService.getTotalPorTipo(listaFechoDespesaGeral, 'Material');
	};

	/*
	*  -------------------------------
	*   ABA FECHO / SERVIÇOS TERCEIROS
	*  -------------------------------
	*/

	carregarFechoServicoTercerizado = (listaFechoDespesaGeral) => {
		$ctrl.listaFechoServicosTercerizados = [];
		$ctrl.totalFechoServicosTercerizados = 0;

		$ctrl.listaFechoServicosTercerizados = FechoDespesaGeralService.filtrarListaPorTipo(listaFechoDespesaGeral, 'Serviço de terceiro');
		$ctrl.totalFechoServicosTercerizados = FechoDespesaGeralService.getTotalPorTipo(listaFechoDespesaGeral, 'Serviço de terceiro');
	};

	/*
	*  ----------------------
	*   ABA FECHO / ABA BDI
	*  ----------------------
	*/

	carregarFechoBdi = (fecho) => {
		$ctrl.listaFechoBdi = FechoService.getListaBdi(fecho.percentualBdiDespesas, fecho.percentualBdiBeneficios);
		$ctrl.totalPercentualBdi = 0;

		$ctrl.listaFechoBdi.forEach(value => {
			value.custoMensal = angular.copy($ctrl.object.fecho.custoMensal);
			value.listaBdi = $ctrl.listaFechoBdi;
			value.valorTotal = FechoService.getTotalBdi(value);

			$ctrl.totalPercentualBdi += value.valor ? value.valor : 0;
			$ctrl.object.fecho.bdi += value.valorTotal;
		});

		carregarFechoTributos($ctrl.object.codigo);
	};

	/*
	*  --------------------------
	*   ABA FECHO / ABA TRIBUTOS
	*  --------------------------
	*/

	carregarFechoTributos = (codCentroFinanceiro) => {
		$ctrl.listaCentroFinanceiroAliquota = [];
		$ctrl.subTotalPercentualTributo = 0;
		$ctrl.totalPercentualTributo = 0;

		const pCentroFinanceiroAliquota = CentroFinanceiroAliquota.getListByCodCentroFinanceiro({id: codCentroFinanceiro}).$promise;
		pCentroFinanceiroAliquota.then(function(data) {
			$ctrl.listaCentroFinanceiroAliquota = data;

			if ($ctrl.listaCentroFinanceiroAliquota && $ctrl.listaCentroFinanceiroAliquota.length > 0) {
				let contador = 0;

				$ctrl.listaCentroFinanceiroAliquota.forEach(centroFinanceiroAliquota => {
					centroFinanceiroAliquota.aliquota.categoriaTributo = {};

					const pCategoriaTributo = CategoriaTributo.getCategoriaTributoByCod({id: centroFinanceiroAliquota.aliquota.codigoCategoriaTributo}).$promise;
					pCategoriaTributo.then(function(data2) {
						centroFinanceiroAliquota.aliquota.categoriaTributo = data2;

						centroFinanceiroAliquota.percentualTributo = 0;
						centroFinanceiroAliquota.percentualTributo = CentroFinanceiroAliquotaService.getPercentualTributo(centroFinanceiroAliquota);
						$ctrl.totalPercentualTributo += centroFinanceiroAliquota.percentualTributo;

						if (centroFinanceiroAliquota.aliquota.categoriaTributo.tributoTO.nome !== 'ISS') {
							$ctrl.subTotalPercentualTributo += centroFinanceiroAliquota.percentualTributo;
						}

						contador++;
						if (contador === $ctrl.listaCentroFinanceiroAliquota.length) {
							calcularTotalCFAliquota();
						}

					}).catch(function(response2) {
						$scope.$emit('crud-critical', 'load', response2);
					});
				});

			} else {
				calcularValoresDoFecho();
			}

		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});
	};

	calcularTotalCFAliquota = () => {
		$ctrl.subTotalValorTributo = 0;

		let percentualTotalTributos = $ctrl.listaCentroFinanceiroAliquota.reduce((acc, cfa) => acc + cfa.percentualTributo, 0);
		let baseDeTributos = $ctrl.object.fecho.custoMensal + $ctrl.object.fecho.bdi;

		$ctrl.listaCentroFinanceiroAliquota.forEach(centroFinanceiroAliquota => {
			centroFinanceiroAliquota.totalTributos = 0;

			centroFinanceiroAliquota.totalTributos = CentroFinanceiroAliquotaService.getValorTotal(baseDeTributos, centroFinanceiroAliquota, percentualTotalTributos);
			$ctrl.object.fecho.tributos += Math.round((centroFinanceiroAliquota.totalTributos + Number.EPSILON) * 100) / 100; //para arredondar para duas casas decimais.

			if (centroFinanceiroAliquota.aliquota.categoriaTributo.tributoTO.nome !== 'ISS') {
				$ctrl.subTotalValorTributo += centroFinanceiroAliquota.totalTributos;
			}
		});

		calcularValoresDoFecho();
	};

	/*
	* --------------
	*  ABA REAJUSTE:
	* --------------
	*/

	carregarAbaReajuste = (object) => {
		object.listaFechos = [];

		const pFecho = Fecho.listaFechoByCodCentroFinanceiro({id: object.codigo}).$promise;
		pFecho.then(function(data) {
			object.listaFechos.push(...data);
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});
	};

	//DIALOGs

	$ctrl.novoReajuste = () => {
		chamarDialogNovoReajuste();
	};

	$scope.removerReajuste = () => {
		showRemoverReajusteConfirm($ctrl.object.codigo);
	};

	chamarDialogNovoReajuste = () => {

		$mdDialog.show({
			controller: DialogNovoReajuste,
			locals: {
				notification: $scope.notification,
				centroFinanceiro: $ctrl.object,
				fnAtualizarAbaFecho: atualizarAbaFecho
			},
			templateUrl: 'partials/comercial/cadastros/centrosfinanceiros/html/dialogs/dialog_novo_reajuste.html',
			parent: angular.element(document.body),
			clickOutsideToClose: false,
			fullscreen: false
		});
	};

	DialogNovoReajuste = ($scope, $mdDialog, $rootScope, notification, centroFinanceiro, fnAtualizarAbaFecho) => {
		$scope.title = 'Reajuste - Centro Financeiro';

		$scope.object = {}; // Fecho
		$scope.object.centroFinanceiro = centroFinanceiro;

		$scope.escopo = 'Total';
		$scope.escopos = ['Total', 'Parcial'];

		$scope.$on('changeEscopo', function() {
			$scope.reajuste = undefined;
			$scope.object.percentualReajusteMaoDeObra = undefined;
			$scope.object.percentualReajusteGastosGerais = undefined;
			$scope.object.percentualReajusteMateriais = undefined;
			$scope.object.percentualReajusteServicos = undefined;
		});

		$scope.$on('changeReajuste', function() {
			$scope.object.percentualReajusteMaoDeObra = $scope.reajuste;
			$scope.object.percentualReajusteGastosGerais = $scope.reajuste;
			$scope.object.percentualReajusteMateriais = $scope.reajuste;
			$scope.object.percentualReajusteServicos = $scope.reajuste;
		});

		function reajusteValido() {
			if(!$scope.object.percentualReajusteMaoDeObra &&
				!$scope.object.percentualReajusteGastosGerais &&
				!$scope.object.percentualReajusteMateriais &&
				!$scope.object.percentualReajusteServicos) {
				return false;
			} else {
				return true;
			}
		}

		$scope.confirm = function() {
			if (reajusteValido()) {
				$rootScope.loadingProgress = true;
				const pFecho = Fecho.addReajuste($scope.object).$promise;

				pFecho.then(function(data) {
					fnAtualizarAbaFecho();
					$rootScope.loadingProgress = false;
					notification.success('', 'Reajuste aplicado com sucesso.');
					$scope.hide();
				}).catch(function(response) {
					$rootScope.loadingProgress = false;
					notification.error('', response.data.message);
					$scope.$emit('crud-critical', 'load', response);
				});

			} else {
				notification.error('','Pelo menos, um dos percentuais de reajuste deve ser preenchido.');
			}
		};

		$scope.hide = function() {
			$mdDialog.hide();
		};

		$scope.cancel = function() {
			$mdDialog.cancel();
		};
	};

	showRemoverReajusteConfirm = (codCentroFinanceiro) => {
		let tituloDialog = 'Exclusão do reajuste';
		let msgDialog = 'Confirma a exclusão do reajuste (esta operação não poderá ser desfeita)?';
		let tituloBotaoConfirmar = 'CONFIRMAR';
		let tituloBotaoCancelar = 'CANCELAR';

		let funcaoCancelar = function(fnHide) {
			fnHide();
		};

		let funcaoConfirmar = function(object, fnHide) {
			const pFecho = Fecho.removerReajusteByCodCentroFinanceiro({id: object}).$promise;

			pFecho.then(function(data) {
				atualizarAbaFecho();
				$scope.notification.success('', 'Reajuste removido com sucesso.');
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});

			fnHide();
		};

		DialogConfirm.show(undefined, codCentroFinanceiro, tituloDialog, msgDialog, tituloBotaoConfirmar, tituloBotaoCancelar, funcaoConfirmar, funcaoCancelar);
	};

	atualizarAbaFecho = () => {
		carregarAbaFecho($ctrl.object);
		carregarAbaReajuste($ctrl.object);
	};

	/*
	* -----------------
	*  ABA ASSINATURAS:
	* -----------------
	*/

	carregarAbaAssinaturas = (object) => {

		object.listaAssinaturas = [];

		let param = {};
		let filtro = {centroFinanceiro: {codigo: object.codigo}};

		const pCentroFinanceiroFluxoEtapa = CentroFinanceiroFluxoEtapa.page(param, filtro).$promise;
		pCentroFinanceiroFluxoEtapa.then(function(data) {

			const pEtapaFluxo = CentroFinanceiroFluxoEtapa.buscarTodosEtapaFluxo().$promise;
			pEtapaFluxo.then(function(data2) {

				let fluxoSemGestao = data2.filter((value) => { return value.statusAtual !== 16; }); //remove a gestao;

				fluxoSemGestao.forEach(etapaFluxo => {
					let temEtapaFluxo = false;

					data.content.forEach(assinatura => {
						if (etapaFluxo.codigo === assinatura.etapaFluxo.codigo) {
							object.listaAssinaturas.push(assinatura);
							temEtapaFluxo = true;
						}
					});

					if (!temEtapaFluxo) {
						let novaAssinatura = {etapaFluxo: etapaFluxo};
						object.listaAssinaturas.push(novaAssinatura);
					}
				});

				buscarMotivoAplicacaoPataEtapaFluxo(object.listaAssinaturas);

			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});

		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});
	};

	$ctrl.renderizarAssinatura = (assinatura) => {
		if ($ctrl.object && assinatura) {
			let situacaoOrdinalCf = CentroFinanceiroService.getOrdinalDaSituacao($ctrl.object.situacao);

			if (situacaoOrdinalCf === assinatura.etapaFluxo.statusAtual || (assinatura.etapaFluxo.statusBase && situacaoOrdinalCf === assinatura.etapaFluxo.statusBase)) {
				let usuarioLogado = UsuarioLogadoService.getUsuarioLogado();

				// 1
				if (assinatura.etapaFluxo.colaborador && assinatura.etapaFluxo.colaborador.codigo) {
					if (assinatura.etapaFluxo.colaborador.codigo === usuarioLogado.codigo) {
						return true;
					}
				}

				// 2
				if (assinatura.etapaFluxo.statusBase && assinatura.dataRealizacao) {
					return false;
				}

				// 3
				let achouPerfil = false;
				usuarioLogado.perfis.forEach(perfil => {
					if (perfil.codigo === assinatura.etapaFluxo.perfil.codigo) {
						achouPerfil = true;
					}
				}); //FIXME: REFATORAR USANDO O 'SOME' DO ARRAY

				if (achouPerfil) {
					return true;
				}
			}
		}

		return false;
	};

	$ctrl.renderizarRetornoAssinatura = (assinatura) => {
		if ($ctrl.renderizarAssinatura(assinatura) && assinatura.etapaFluxo.ordem > 1) {
			return true;
		} else {
			return false;
		}
	};

	$ctrl.assinaturaRetornoObrigatorio = (assinatura) => {
		if (assinatura.etapaFluxo) {
			if (assinatura.etapaFluxo.motivoAplicacao && assinatura.etapaFluxo.motivoAplicacao.codigo) {
				return true;
			}
		}

		return false;
	};

	buscarMotivoAplicacaoPataEtapaFluxo = (listaAssinaturas) => {
		if (listaAssinaturas && listaAssinaturas.length > 0) {
			listaAssinaturas.forEach(assinatura => {

				const pMotivoAplicacao = CentroFinanceiroFluxoEtapa.motivoAplicacaoByFluxoEtapaId({id: assinatura.etapaFluxo.codigo}).$promise;

				pMotivoAplicacao.then(function(data) {
					assinatura.etapaFluxo.motivoAplicacao = data;
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'load', response);
				});
			});
		}
	};

	$ctrl.assinar = (assinatura) => {

		$rootScope.loadingProgress = true;

		const pCentroFinanceiro = CentroFinanceiroFluxoEtapa.assinarCentroFinanceiro({codEtapaFluxo: assinatura.etapaFluxo.codigo, codCentroFinanceiro: $ctrl.object.codigo}).$promise;
		pCentroFinanceiro.then(function(data) {
			$rootScope.loadingProgress = false;
			loadObj();
			if (data && data.warnings && data.warnings.length > 0) {
				$scope.notification.atencao('', data.warnings[0]);
			}
			$scope.notification.success('', 'Assinatura do centro financeiro realizada com sucesso.');
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});
	};

	$ctrl.retornarAssinatura = (event, assinatura) => {
		let chaveMotivo = 'cf-retornar';
		let titulo = 'Motivos de Retorno - Assinatura do centro financeiro';

		// Função que será executada no confirmar no dialog de motivo de retorno.
		$scope.confirmarMotivo = function(motivoRetornoDocumento, fn_Hide) {
			let retornoAssinaturaTO = {};
			retornoAssinaturaTO.motivoRetornoDocumento = motivoRetornoDocumento;
			retornoAssinaturaTO.codEtapaFluxo = assinatura.etapaFluxo.codigo;
			retornoAssinaturaTO.codCentroFinanceiro = $ctrl.object.codigo;
			retornoAssinaturaTO.chave = chaveMotivo;

			const pFluxoEtapa = CentroFinanceiroFluxoEtapa.retornarAssinatura(retornoAssinaturaTO).$promise;

			pFluxoEtapa.then(function(data) {
				loadObj();
				fn_Hide();

			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
				fn_Hide();
			});

		};

		// Função que será executada no cancelar no dialog de motivo de retorno.
		$scope.cancelarMotivo = function(fn_Hide) {
			fn_Hide();
		};

		// Passar para o serviço, as funções que serão chamadas ao confirmar ou cancelar o motivo de retorno.
		MotivoRetornoDialog.show(event, chaveMotivo, titulo, $scope.confirmarMotivo, $scope.cancelarMotivo);
	};

	$scope.confirmarRetornoAssinatura = (event, assinatura) => {
		let retornoAssinaturaTO = {};
		retornoAssinaturaTO.codEtapaFluxo = assinatura.etapaFluxo.codigo;
		retornoAssinaturaTO.codCentroFinanceiro = $ctrl.object.codigo;

		let funcaoCancelar = function(fnHide) {
			fnHide();
		};

		let funcaoConfirmar = function(object, fnHide) {
			const pFluxoEtapa = CentroFinanceiroFluxoEtapa.retornarAssinaturaSemMotivoRetorno(object).$promise;

			pFluxoEtapa.then(function(data) {
				loadObj();
				fnHide();

			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
				fnHide();
			});

		};

		DialogConfirm.show(
			undefined,
			retornoAssinaturaTO,
			'Retornar: Confirmação',
			'Deseja realmente retornar esta assinatura para a etapa anterior?',
			'Confirmar',
			'Cancelar',
			funcaoConfirmar,
			funcaoCancelar);
	};


	/**
		 ----------------------
		IMPRESSÃO OS INICIAL
		----------------------
	*/

	carregarDadosParaImpressao = () => {

		$ctrl.dadosImpressao = {
			produto: '',
			endereco: '',
			bairro: '',
			cep: '',
			municipio: '',
			uf: '',
			contrato: {},
			inscricaoEstadualUnidadeFederal: '',
			inscricaoMunicipal: '',
			contato: '',
			labelTipoContato: '',
			meioContato: '',
			labelBeneficio: '',
			valorBeneficio: '',
			planoSaude: 0,
			listaValores: []
		};

		carregarProdutoParaImpressao();
		carregarEnderecoParaImpressao();
		buscarMunicipioParaImpressao();
		buscarUfParaImpressao();
		buscarContratoParaImpressao();
		buscarContatoParaImpressao();
		buscarLabelBeneficio();
		buscarPlanoSaude();
		buscarValoresParaImpressao();
	};

	carregarProdutoParaImpressao = () => {
		$ctrl.object.gerencia = undefined;

		if ($ctrl.object && $ctrl.object.pai) {
			$ctrl.object.gerencia = $ctrl.object.pai;
		} else {
			$ctrl.object.gerencia = {codigo: $ctrl.object.codigo};
		}

		const pGerencia = Gerencia.getGerenciaSimplesById({id: $ctrl.object.gerencia.codigo }).$promise;
		pGerencia.then(function(data) {

			let objGerencia = data;
			$ctrl.dadosImpressao.produto = objGerencia.diretoria.descricao;

		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});
	};

	carregarEnderecoParaImpressao = () => {

		$ctrl.dadosImpressao.endereco = '';

		if ($ctrl.object && $ctrl.object.listaEnderecos) {
			if ($ctrl.object.listaEnderecos.length === 1) {
				$ctrl.dadosImpressao.endereco = $ctrl.object.listaEnderecos[0].endereco.enderecoCompleto;
				$ctrl.dadosImpressao.bairro = $ctrl.object.listaEnderecos[0].endereco.bairro;
				$ctrl.dadosImpressao.cep = $ctrl.object.listaEnderecos[0].endereco.cep;
				$ctrl.dadosImpressao.uf = $ctrl.object.listaEnderecos[0].endereco.uf;

			} else if ($ctrl.object.listaEnderecos.length > 1) {
				$ctrl.dadosImpressao.endereco = 'Diversos';
				$ctrl.dadosImpressao.bairro = 'Diversos';
				$ctrl.dadosImpressao.cep = 'Diversos';
				$ctrl.dadosImpressao.uf = 'Diversos';
			} else {
				buscarEnderecoRelatorio();
			}

		} else {
			buscarEnderecoRelatorio();
		}
	};

	buscarEnderecoRelatorio = () => {
		if ($ctrl.object && $ctrl.object.contratoTO && $ctrl.object.contratoTO.codigo) {

			const pContrato = Contrato.buscarEnderecos({id: $ctrl.object.contratoTO.codigo}).$promise;
			pContrato.then(function(data) {
				let listaContratoEndereco = data;

				if (listaContratoEndereco && listaContratoEndereco.length > 0) {
					listaContratoEndereco.forEach(item => {

						if (item.tipo === 'Prestação de serviço') {
							$ctrl.dadosImpressao.endereco = item.endereco.enderecoCompleto;
							$ctrl.dadosImpressao.bairro = item.endereco.bairro;
							$ctrl.dadosImpressao.cep = item.endereco.cep;

							if (!$ctrl.object.listaEnderecos || $ctrl.object.listaEnderecos.length <= 0) {
								$ctrl.dadosImpressao.municipio = item.endereco.cidade.nome;
							}

							$ctrl.dadosImpressao.uf = item.endereco.uf;

							return;
						}
					});
				}

			}).catch(function(response) {
				$scope.$emit('crud-critical', 'AbaContratoEndereco', response);
			});
		}
	};

	buscarMunicipioParaImpressao = () => {

		let descricaoCidade = '';

		if ($ctrl.object.listaEnderecos && $ctrl.object.listaEnderecos.length > 0) {
			descricaoCidade = CentroFinanceiroService.getDescricaoCidade($ctrl.object.listaEnderecos);
			$ctrl.dadosImpressao.municipio = descricaoCidade;
		}
	};

	buscarUfParaImpressao = () => {

		let cfEndereco;

		if ($ctrl.object.listaEnderecos && $ctrl.object.listaEnderecos.length > 0) {
			cfEndereco = CentroFinanceiroService.getEndereco($ctrl.object.listaEnderecos);
		}

		if (!$ctrl.dadosImpressao.municipio && $ctrl.dadosImpressao.municipio !== 'Diversos') {
			if (cfEndereco && cfEndereco.endereco && cfEndereco.endereco.uf) {
				$ctrl.dadosImpressao.uf = cfEndereco.endereco.uf;
			}
		}

		if ($ctrl.dadosImpressao.municipio && $ctrl.dadosImpressao.municipio === 'Diversos') {
			$ctrl.dadosImpressao.uf = 'Diversos';
		}
	};

	buscarContratoParaImpressao = () => {

		const pContrato = Contrato.get({id: $ctrl.object.contratoTO.codigo}).$promise;
		pContrato.then(function(data) {

			$ctrl.dadosImpressao.contrato = data;
			buscarInscricoesParaImpressao(data.cliente.codigo);

			const pPessoa = Pessoa.get({id: data.cliente.codigo}).$promise;
			pPessoa.then(function(data2) {

				const pEndereco = Endereco.get({id: data2.codigoEndereco}).$promise;
				pEndereco.then(function(data3) {
					$ctrl.dadosImpressao.contrato.cliente.endereco = {};
					$ctrl.dadosImpressao.contrato.cliente.endereco = data3;

				}).catch(function(response3) {
					$scope.$emit('crud-critical', 'load', response3);
				});

			}).catch(function(response2) {
				$scope.$emit('crud-critical', 'load', response2);
			});

		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});
	};

	buscarInscricoesParaImpressao = (codCliente) => {

		const pPessoaJuridica = PessoaJuridica.get({id: codCliente}).$promise;
		pPessoaJuridica.then(function(data) {
			$ctrl.dadosImpressao.inscricaoMunicipal = data.inscricaoMunicipal;

			if (data.inscricaoEstadual && data.ufInscricaoEstadual) {
				$ctrl.dadosImpressao.inscricaoEstadualUnidadeFederal = data.inscricaoEstadual + ' / ' + data.ufInscricaoEstadual; //FIXME: REFATORAR USANDO template_strings
			}

		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});
	};

	buscarContatoParaImpressao = () => {
		$ctrl.dadosImpressao.labelTipoContato = 'Telefones';

		const pContratoContato = ContratoContato.listaByCodContrato({id: $ctrl.object.contratoTO.codigo}).$promise;
		pContratoContato.then(function(data) {

			let objContato;

			if (data && data.length > 0) {
				data.forEach(value => {
					if (value.ordem === 0) {
						objContato = value.contato;
						return;
					}
				}); //FIXME: REFATORAR
			}

			if (objContato) {
				if (objContato.nome) {
					$ctrl.dadosImpressao.contato = objContato.nome;
				}
				buscarTipoContato(objContato);
			}

		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});
	};

	buscarTipoContato = (objContato) => {
		let objTelefone = {contato: objContato};

		const pTelefone = Telefone.buscarListaTelefone(objTelefone).$promise;
		pTelefone.then(function(data) {

			if (data && data.length > 0) {
				if (data.length === 1) {
					$ctrl.dadosImpressao.labelTipoContato = 'Telefone';
				}

				criarStringTelefonesContato(data);

			} else {
				$ctrl.dadosImpressao.labelTipoContato = 'E-mail';
				$ctrl.dadosImpressao.meioContato = objContato.email;
			}

		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});
	};

	criarStringTelefonesContato = (listaTelefone) => {
		let telefone1;
		let telefone2;

		let telefonesContato = '';

		if (listaTelefone && listaTelefone.length > 0) {
			telefone1 = listaTelefone[0];

			if (listaTelefone.length > 1) {
				telefone2 = listaTelefone[1];
			}

			telefonesContato = telefonesContato.concat(telefone1.dddTelefoneRamal); //FIXME: REFATORAR

			if (telefone2 && telefone2.codigo) {
				telefonesContato = telefonesContato.concat(' / ');
				telefonesContato = telefonesContato.concat(telefone2.dddTelefoneRamal); //FIXME: REFATORAR
			}
		}

		$ctrl.dadosImpressao.meioContato = telefonesContato;
	};

	buscarLabelBeneficio = () => {

		$ctrl.dadosImpressao.labelBeneficio = 'Benefício (Alimentação)';

		const pParametro = Parametro.page({chave: 'comercial.gastosgerais.ticket_refeicao'}).$promise;
		pParametro.then(function(data) {
			let codigoTicketRefeicao = data.valor;
			let valorBeneficio = getDespesaValorParametro(codigoTicketRefeicao);

			if (valorBeneficio && valorBeneficio > 0) {
				$ctrl.dadosImpressao.labelBeneficio = 'Benefício (Refeição)';
			}
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});
	};

	getDespesaValorParametro = (codigoDespesa) => {
		let valorbeneficio = 0;
		let valorUnitario = 0;

		if ($ctrl.listaFechoGastosGerais && $ctrl.listaFechoGastosGerais.length > 0) {

			$ctrl.listaFechoGastosGerais.forEach(item => {
				if (item.categoriaDespesaGeral.codigo === Number(codigoDespesa)) {
					valorbeneficio = item.quantidade * item.valorUnitario;
					valorUnitario = item.valorUnitario;
					return;
				}
			});
		}

		$ctrl.dadosImpressao.valorBeneficio = valorUnitario;
		return valorbeneficio;
	};

	buscarPlanoSaude = () => {

		const pParametro = Parametro.page({chave: 'comercial.gastosgerais.plano_saude'}).$promise;

		pParametro.then(function(data) {
			let codigoPlanoSaude = data.valor;
			let valorUnitario = getDespesaValorUnitarioParametro(codigoPlanoSaude);
			$ctrl.dadosImpressao.planoSaude = valorUnitario;
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
		});
	};

	getDespesaValorUnitarioParametro = (codigoDespesa) => {
		let valorUnitario = 0;

		if ($ctrl.listaFechoGastosGerais && $ctrl.listaFechoGastosGerais.length > 0) {
			$ctrl.listaFechoGastosGerais.forEach(item => {
				if (item.categoriaDespesaGeral.codigo === Number(codigoDespesa)) {
					valorUnitario = item.valorUnitario;
					return;
				}
			});
		}

		return valorUnitario;
	};

	buscarValoresParaImpressao = () => {
		$ctrl.dadosImpressao.listaValores = [];
		let listaValores = [];
		let valores = [];

		let um = 1;
		let cem = 100;
		let prazo = $ctrl.object.contratoTO.prazoAtual;
		let percentualImpostoAux = $ctrl.object.fecho.percentualImposto;

		// CALCULO PARA RECEITA BRUTA [INICIO]
		let custoMensal = $ctrl.object.fecho.custoMensal;
		let valorBdi = $ctrl.object.fecho.bdi;

		let baseDeTributos = custoMensal + valorBdi;
		let percImposto = $ctrl.object.fecho.percentualImposto / 100;
		let valorTributos = baseDeTributos * (percImposto) / (1 - percImposto);

		let valorMensal = custoMensal + valorBdi + valorTributos;

		let receitaBruta = valorMensal * $ctrl.object.fecho.prazo;
		// CALCULO PARA RECEITA BRUTA [FIM]

		let totalImpostos = ( receitaBruta * (percentualImpostoAux ? percentualImpostoAux : 0) ) / cem;
		let receitaLiquida = receitaBruta - totalImpostos;
		let encargosSociais = $ctrl.percentualEncargosSociais ? $ctrl.percentualEncargosSociais : 0;
		let totalMaterial = $ctrl.totalFechoMateriais * prazo;

		let totalMaoDeObraDireta = $scope.subTotalMaoDeObraContratacaoDireta * (um + (encargosSociais / cem)) * prazo;
		totalMaoDeObraDireta = totalMaoDeObraDireta + ( $scope.subTotalMaoDeObraContratacaoDiretaPlantao * (um + (encargosSociais / cem)) * prazo );

		let totalMaoDeObraIndireta = $scope.subTotalMaoDeObraContratacaoIndireta * (um + (encargosSociais / cem)) * prazo;
		totalMaoDeObraIndireta = totalMaoDeObraIndireta + ( $scope.subTotalMaoDeObraContratacaoIndiretaPlantao * (um + (encargosSociais / cem)) * prazo );

		let totalServicosTerceiros = $ctrl.totalFechoServicosTercerizados * prazo;
		let totalGastosGerais = $ctrl.totalFechoGastosGerais * prazo;

		let custoServicos = totalMaoDeObraDireta + totalMaoDeObraIndireta + totalServicosTerceiros + totalGastosGerais + totalMaterial;

		let margemContribuicao = receitaLiquida - custoServicos;

		let percentualReceitaLiquida = 100;

		let percentualImpostos = (percentualImpostoAux ? percentualImpostoAux : 0);
		let percentualReceitaBruta = 0;
		let percentualCustoServicos = 0;
		let percentualTotalMaterial = 0;
		let percentualMaoDeObraDireta = 0;
		let percentualMaoDeObraIndireta = 0;
		let percentualServicosTerceiros = 0;
		let percentualGastosGerais = 0;
		let percentualMargemContribuicao = 0;

		if (receitaLiquida > 0) {
			percentualReceitaBruta = cem + ( (totalImpostos / receitaLiquida) * cem ) ;
			percentualImpostos = (totalImpostos / receitaLiquida) * cem;
			percentualCustoServicos = (custoServicos / receitaLiquida) * cem;
			percentualTotalMaterial = (totalMaterial / receitaLiquida) * cem;
			percentualMaoDeObraDireta = (totalMaoDeObraDireta / receitaLiquida) * cem;
			percentualMaoDeObraIndireta = (totalMaoDeObraIndireta / receitaLiquida) * cem;
			percentualServicosTerceiros = (totalServicosTerceiros / receitaLiquida) * cem;
			percentualGastosGerais = (totalGastosGerais / receitaLiquida) * cem;
			percentualMargemContribuicao = (margemContribuicao / receitaLiquida) * cem;
		}

		valores = [];
		valores.push('Receita bruta');
		valores.push(receitaBruta);
		valores.push(percentualReceitaBruta);
		listaValores.push(valores);

		valores = [];
		valores.push('(-) Impostos');
		valores.push(totalImpostos);
		valores.push(percentualImpostos);
		listaValores.push(valores);

		valores = [];
		valores.push('Receita líquida');
		valores.push(receitaLiquida);
		valores.push(percentualReceitaBruta == 0 ? 0 : percentualReceitaLiquida);
		listaValores.push(valores);

		valores = [];
		valores.push('(-) Custo dos serviços');
		valores.push(custoServicos ? custoServicos : 0);
		valores.push(percentualCustoServicos);
		listaValores.push(valores);

		valores = [];
		valores.push('Material');
		valores.push(totalMaterial ? totalMaterial : 0);
		valores.push(percentualTotalMaterial);
		listaValores.push(valores);

		valores = [];
		valores.push('Mão de obra direta');
		valores.push(totalMaoDeObraDireta ? totalMaoDeObraDireta : 0);
		valores.push(percentualMaoDeObraDireta);
		listaValores.push(valores);

		valores = [];
		valores.push('Mão de obra indireta');
		valores.push(totalMaoDeObraIndireta ? totalMaoDeObraIndireta : 0);
		valores.push(percentualMaoDeObraIndireta);
		listaValores.push(valores);

		valores = [];
		valores.push('Serviços de terceiros');
		valores.push(totalServicosTerceiros ? totalServicosTerceiros : 0);
		valores.push(percentualServicosTerceiros);
		listaValores.push(valores);

		valores = [];
		valores.push('Gastos gerais');
		valores.push(totalGastosGerais ? totalGastosGerais : 0);
		valores.push(percentualGastosGerais);
		listaValores.push(valores);

		valores = [];
		valores.push('.');
		valores.push(undefined);
		valores.push(undefined);
		listaValores.push(valores);

		valores = [];
		valores.push('.');
		valores.push(undefined);
		valores.push(undefined);
		listaValores.push(valores);

		valores = [];
		valores.push('(=) Margem de contribuição');
		valores.push(margemContribuicao ? margemContribuicao : 0);
		valores.push(percentualMargemContribuicao);
		listaValores.push(valores);

		$ctrl.dadosImpressao.listaValores = listaValores;
	};

	// BOTÕES:

	$ctrl.imprimir = () => {

		let cssLinks = `<link rel='stylesheet' href='../lib/angular-ui-grid/ui-grid.min.css' />
						<link rel='stylesheet' href='../lib/fontawesome/css/font-awesome.min.css' />
						<link rel='stylesheet' href='../lib/angular-material/angular-material.min.css' />
						<link rel='stylesheet' href='../lib/angular-datatables/dist/css/angular-datatables.min.css' />
						<link rel='stylesheet' href='../lib/datatables/media/css/jquery.dataTables.min.css' />
						<link rel='stylesheet' href='../lib/perfect-scrollbar/css/perfect-scrollbar.min.css' />
						<link rel='stylesheet' href='../lib/textAngular/dist/textAngular.css' />
						<link rel='stylesheet' href='../lib/angular-material-data-table/dist/md-data-table.css' />
						<link rel='stylesheet' href='../lib/fuse/assets/icons/style.css' />
						<link rel='stylesheet' href='partials/comercial/cadastros/centrosfinanceiros/html/partials/impressao.css' />
						<link rel='stylesheet' href='../lib/style/main.css' />`;

		let printContents = document.getElementById('os_inicial_impressao').innerHTML;
		let popupWin = window.open('', '_blank', 'width=1000,height=900');
		popupWin.document.open();
		popupWin.document.write(`<html><head> ${cssLinks} </head><body onload='window.print()'> ${printContents} </body></html>`);
		popupWin.document.close();

	};

	$ctrl.voltar = (ev) => {
		$state.go('centrosfinanceiros.view', { objectId: $stateParams.objectId });
	};

	init();

}

