(function () {
	"use strict";
	function PagerService() {
		// service definition
		let service = {};

		service.GetPager = GetPager;

		return service;

		// service implementation
		function GetPager(totalItems, currentPage, pageSize) {
			// default to first page
			currentPage = currentPage || 1;

			// default page size is 10
			pageSize = pageSize || 10;

			// calculate total pages
			let totalPages = Math.ceil(totalItems / pageSize);

			let startPage, endPage;
			if (totalPages <= 10) {
				// less than 10 total pages so show all
				startPage = 1;
				endPage = totalPages;
			} else {
				// more than 10 total pages so calculate start and end pages
				if (currentPage <= 6) {
					startPage = 1;
					endPage = 10;
				} else if (currentPage + 4 >= totalPages) {
					startPage = totalPages - 9;
					endPage = totalPages;
				} else {
					startPage = currentPage - 5;
					endPage = currentPage + 4;
				}
			}

			// calculate start and end item indexes
			let startIndex = (currentPage - 1) * pageSize;
			let endIndex = Math.min(startIndex + pageSize - 1, totalItems - 1);

			// create an array of pages to ng-repeat in the pager control
			let pages = _.range(startPage, endPage + 1);

			// return object with all pager properties required by the view
			return {
				totalItems: totalItems,
				currentPage: currentPage,
				pageSize: pageSize,
				totalPages: totalPages,
				startPage: startPage,
				endPage: endPage,
				startIndex: startIndex,
				endIndex: endIndex,
				pages: pages
			};
		}
	}

	function complementarDiscriminacaoFinalComTributos($scope, discriminacaoParteFinal) {
		let linha = "";
		let possuiPcc = $scope.percentualPcc && $scope.percentualPcc > 0;
		let possuiIss = $scope.issRetidoCliente && $scope.percentualIss && $scope.percentualIss > 0;
		let possuiInss = $scope.percentualInss && $scope.percentualInss > 0;
		let possuiIrrf = $scope.percentualIrrf && $scope.percentualIrrf > 0;
		let separadorPadrao = "    ";

		if (possuiPcc || possuiInss || possuiIss || possuiIrrf) {
			let separador = "";

			if (possuiPcc) {
				linha = linha + separador + "PCC:  " + $scope.percentualPcc + '%';
				separador = separadorPadrao;
			}

			if (possuiInss) {
				linha = linha + separador + "INSS:  " + $scope.percentualInss + '%';
				separador = separadorPadrao;
			}

			if (possuiIrrf) {
				linha = linha + separador + "IRRF:  " + $scope.percentualIrrf + '%';
				separador = separadorPadrao;
			}

			if (possuiIss) {
				linha = linha + separador + "ISS:  " + $scope.percentualIss + '%';
				separador = separadorPadrao;
			}
		}

		if (linha) {
			discriminacaoParteFinal = discriminacaoParteFinal + "\n" + linha;
		}

		return discriminacaoParteFinal;
	}

	function inicializarValoresPercentuaisTributosFaturamento($scope) {

		$scope.subtotalPercentualTributo = 0;
		$scope.subTotalValorTributo = 0;
		$scope.totalPercentualTributo = 0;
		$scope.totalValorTributo = 0;

		$scope.percentualIss = 0;
		$scope.percentualInss = 0;
		$scope.percentualPcc = 0;
		$scope.percentualIrrf = 0;

		$scope.valorIss = 0;
		$scope.valorInss = 0;
		$scope.valorPcc = 0;
		$scope.valorIrrf = 0;

		if (!('issRetidoCliente' in $scope)) {
			let obj = null;

			if ($scope.object) {
				obj = $scope.object.faturamento ? $scope.object.faturamento : $scope.object;
			}

			if (obj) {
				$scope.issRetidoCliente = obj.issRetidoCliente || false;
			}
		}
	}

	function calcularValoresPercentuaisTributosFaturamento($scope) {
		inicializarValoresPercentuaisTributosFaturamento($scope);
		if (!angular.isArray($scope.listaFaturamentoAliquota) || !$scope.listaFaturamentoAliquota.length) return;

		let total_1 = 0;
		let total_2 = 0;
		let total_3 = 0;
		let total_4 = 0;

		angular.forEach($scope.listaFaturamentoAliquota, function (obj, key) {
			let codigoTributo = obj.aliquota.categoriaTributo.tributoTO.codigo;

			let varPercentual = obj.aliquota.percentual;
			let varValor = obj.valor;

			if (codigoTributo !== 4) {

				if (angular.isDefined(varPercentual) && varPercentual !== null) {
					total_1 += Number(varPercentual);
				}

				if (angular.isDefined(varValor) && varValor !== null) {
					total_2 += Number(varValor);
				}

			} else {

				if (angular.isDefined(varPercentual) && varPercentual !== null) {
					total_3 += Number(varPercentual);
				}

				if (angular.isDefined(varValor) && varValor !== null) {
					total_4 += Number(varValor);
				}

			}

			switch (obj.aliquota.categoriaTributo.tributoTO.nome) {
				case 'PIS/COFINS/CSLL':
					$scope.percentualPcc = varPercentual;
					$scope.valorPcc = varValor;
					break;
				case 'INSS':
					$scope.percentualInss = varPercentual;
					$scope.valorInss = varValor;
					break;
				case 'ISS':
					$scope.percentualIss = varPercentual;
					$scope.valorIss = varValor;
					break;
				case 'IRRF':
				case 'IRPJ':
					$scope.percentualIrrf = varPercentual;
					$scope.valorIrrf = varValor;
					break;
			}
		});

		$scope.subtotalPercentualTributo = total_1;
		$scope.subTotalValorTributo = total_2;
		$scope.totalPercentualTributo = total_1 + total_3;
		$scope.totalValorTributo = total_2 + total_4;
	}

	angular.module('app.faturamento').controller('FaturamentoEtapaCtrl', faturamentoEtapaCtrlFn);

	function faturamentoEtapaCtrlFn($scope, $filter, $mdDialog, $rootScope, FaturamentoEtapa, CalculadorFaturamento, MotivoRetornoEtapa) {
		$scope.validacaoDataInicioEtapa = false;
		$scope.validacaoDataTerminoEtapa = false;

		$scope.listaExibirSimNao = [];
		$scope.listaExibirSimNao.push(false);
		$scope.listaExibirSimNao.push(true);

		let copiaDiscriminacaoInicial = "";

		let catchError = (response) => {
			console.log(response);
			console.error('crud-critical', 'dialog', response);
			$scope.action = false;
			$scope.notification.criticalError(response.status, response.statusText, response.data);
		};

		if ($scope.screenMode == 'VIEW' || $scope.screenMode == 'EDIT') {
			FaturamentoEtapa.getByCodigo({id: $scope.objectId}).$promise.then(function (object) {
				$scope.object = object;
				$scope.object.numero = CalculadorFaturamento.formataNumero($scope.etapa.numero, 3);
				$scope.object.inicio = new Date($scope.object.inicio);
				$scope.object.termino = new Date($scope.object.termino);

				carregaRetencoes($scope.objetoFaturamento);

				if ($scope.motivoRetornoSalvo) {
					$scope.etapa.situacao = $scope.object.situacao;
				}

				if ($scope.object.situacao === 'Cancelada') {
					$rootScope.loadingProgress = true;
					MotivoRetornoEtapa.page(null, {faturamentoEtapa: {codigo: $scope.objectId}}).$promise.then(function (data) {
							$rootScope.loadingProgress = false;
							$scope.listMotivos = data.content;
						}).catch(function (response) {
							$rootScope.loadingProgress = false;
							$scope.$emit('crud-critical', 'search', response);
						});
				}

				copiaDiscriminacaoInicial = angular.copy($scope.object.discriminacaoInicial);

			}).catch(catchError);
		} else if ($scope.screenMode == 'NEW') {
			$scope.object = new FaturamentoEtapa();
			$scope.object.situacao = "A faturar"; //"A_FATURAR";
			$scope.object.discriminacaoInicial = $scope.objetoFaturamento.discriminacaoInicial;
			$scope.object.discriminacaoCentral = $scope.objetoFaturamento.discriminacaoCentral;
			$scope.object.discriminacaoFinal = '';

			copiaDiscriminacaoInicial = angular.copy($scope.object.discriminacaoInicial);

			$scope.object.exibeRetencaoContratual = false;
			$scope.object.exibeRetencaoAcessoria = false;
			$scope.object.exibePermuta = false;

			carregaRetencoes($scope.objetoFaturamento);

			if (CalculadorFaturamento.isCampoVazio($scope.listaEtapas)) {
				$scope.listaEtapas = [];
			}

			FaturamentoEtapa.proximoNumeroEtapaByFatId({
					id: $scope.objectId
				})
				.$promise.then(function (data) {
					let proximoNumero = data.numero;
					$scope.object.numero = CalculadorFaturamento.formataNumero(proximoNumero, 3);
				}).catch(catchError);
		}

		function carregaRetencoes(faturamento) {
			if (angular.isDefined(faturamento.faturamentoRetencoes)) {
				angular.forEach(faturamento.faturamentoRetencoes, function (value, key) {
					if (value.tipo.chave === 'contratual') {
						$scope.object.percentualRetencaoContratual = value.percentual;
					} else if (value.tipo.chave === 'acessoria') {
						$scope.object.percentualRetencaoAcessoria = value.percentual;
					}
				});
			}
		}


		function ajustarParteFinal() {
			if (angular.isDefined($scope.object)) {
				$scope.issRetidoCliente = $scope.objetoFaturamento ? $scope.objetoFaturamento.issRetidoCliente : false;
				CalculadorFaturamento.dialogEtapa($scope.objetoFaturamento, $scope.object, $scope.percentualTributos);

				if (!CalculadorFaturamento.isCampoVazio($scope.object.valorLiquido) && $scope.object.valorLiquido < 0) {
					$scope.colorValorLiquido = 'red !important';
				} else {
					$scope.colorValorLiquido = ' ';
				}
				//calcular o valor dos serviços em relaçao ao valor digitado
				if (angular.isFunction($scope.fn_listaContatos)) {
					$scope.fn_listaContatos().then(function (data) {
						$scope.montarCampoDiscrimininacaoFinalEtapa(data);
					}).catch(catchError);
				}
			}
		}

		calcularValoresPercentuaisTributosFaturamento($scope);

		function ajustarParteInicial() {
			montarCampoDiscrimininacaoInicialEtapa();
		}

		$scope.$watch('object.inicio', function () {
			ajustarParteInicial();
		});

		$scope.$watch('object.termino', function () {
			ajustarParteInicial();
		});

		$scope.$watch('object.valor', function () {
			ajustarParteFinal();
			ajustarParteInicial();
		});

		$scope.$watch('object.valorPermuta', function () {
			ajustarParteFinal();
		});

		$scope.$watch('object.valorPermuta', function () {
			if (angular.isDefined($scope.object)) {
				CalculadorFaturamento.dialogEtapa($scope.objetoFaturamento, $scope.object, $scope.percentualTributos);

				if (!CalculadorFaturamento.isCampoVazio($scope.object.valorLiquido) && $scope.object.valorLiquido < 0) {
					$scope.colorValorLiquido = 'red !important';
				} else {
					$scope.colorValorLiquido = ' ';
				}
			}
		});

		$scope.$watch('object.valorDesconto', function () {
			if (angular.isDefined($scope.object)) {
				CalculadorFaturamento.dialogEtapa($scope.objetoFaturamento, $scope.object, $scope.percentualTributos);

				if (!CalculadorFaturamento.isCampoVazio($scope.object.valorLiquido) && $scope.object.valorLiquido < 0) {
					$scope.colorValorLiquido = 'red !important';
				} else {
					$scope.colorValorLiquido = ' ';
				}
			}
		});

		$scope.$watch('object.valorMulta', function () {
			if (angular.isDefined($scope.object)) {
				CalculadorFaturamento.dialogEtapa($scope.objetoFaturamento, $scope.object, $scope.percentualTributos);

				if (!CalculadorFaturamento.isCampoVazio($scope.object.valorLiquido) && $scope.object.valorLiquido < 0) {
					$scope.colorValorLiquido = 'red !important';
				} else {
					$scope.colorValorLiquido = ' ';
				}
			}
		});

		$scope.$watch('object.valorJuros', function () {
			if (angular.isDefined($scope.object)) {
				CalculadorFaturamento.dialogEtapa($scope.objetoFaturamento, $scope.object, $scope.percentualTributos);

				if (!CalculadorFaturamento.isCampoVazio($scope.object.valorLiquido) && $scope.object.valorLiquido < 0) {
					$scope.colorValorLiquido = 'red !important';
				} else {
					$scope.colorValorLiquido = ' ';
				}
			}
		});

		$scope.$watch('object.inicio', function () {
			if (angular.isDefined($scope.object)) {
				if ($scope.validacaoDataInicioEtapa == false && angular.isDefined($scope.object.inicio))
					$scope.validacaoDataInicioEtapa = true;
			}
		});

		$scope.$watch('object.termino', function () {
			if (angular.isDefined($scope.object)) {
				if ($scope.validacaoDataTerminoEtapa == false && angular.isDefined($scope.object.termino))
					$scope.validacaoDataTerminoEtapa = true;
			}
		});


		$scope.hide = function () {
			$mdDialog.hide();
		};


		$scope.cancel = function () {
			$mdDialog.cancel();
		};


		$scope.salvarEtapa = function () {
			$scope.action = true;
			if (CalculadorFaturamento.isCampoVazio($scope.object.faturamento)) {
				$scope.object.faturamento = {};
				$scope.object.faturamento.codigo = $scope.objetoFaturamento.codigo;
			}

			if ($scope.screenMode === 'NEW') {
				FaturamentoEtapa.save($scope.object).$promise.then(function (data) {
					$scope.listaEtapas.unshift(data);
					let conteudoEtapa = $scope.listaEtapas[0];
					conteudoEtapa.numero = CalculadorFaturamento.formataNumero(conteudoEtapa.numero, 3);
					CalculadorFaturamento.dialogEtapa($scope.objetoFaturamento, data, $scope.percentualTributos);

					let checkDescricao = () => FaturamentoEtapa.verificarDescricaoEtapaDeFaturamento({codigoEtapa: data.codigo, codigoFaturamento: conteudoEtapa.faturamento.codigo, descricao: conteudoEtapa.descricao}).$promise;
					let checkDescricaoResult = (data) => {
						if (data.descriptionExists) {
							let numero = CalculadorFaturamento.formataNumero(data.numeroEtapa, 3);
							$scope.notification.atencao(null, `A etapa ${numero} do faturamento "${conteudoEtapa.faturamento.numero}" possui a mesma descrição do item desta nova etapa.`);
						}
					};

					Promise.resolve()
					.then(checkDescricao)
					.then(checkDescricaoResult)
					.then(() => {
						$scope.notification.success("", "Etapa salva com sucesso");
						$scope.action = false;
						$mdDialog.hide();
					})
					.catch(catchError);
				}).catch(catchError);
			} else {
				FaturamentoEtapa.update({
					id: $scope.object.codigo
				}, $scope.object).$promise.then(function (data) {
					data.numero = $scope.etapa.numero;
					delete data.$promise;
					delete data.$resolved;
					CalculadorFaturamento.dialogEtapa($scope.objetoFaturamento, data, $scope.percentualTributos);
					data.inicio = new Date(data.inicio);
					data.termino = new Date(data.termino);
					angular.merge($scope.etapa, data);

					let checkDescricao = () => FaturamentoEtapa.verificarDescricaoEtapaDeFaturamento({codigoEtapa: data.codigo, codigoFaturamento: $scope.objetoFaturamento.codigo, descricao: data.descricao}).$promise;
					let checkDescricaoResult = (data) => {
						if (data.descriptionExists) {
							let numero = CalculadorFaturamento.formataNumero(data.numeroEtapa, 3);
							$scope.notification.atencao(null, `A etapa ${numero} do faturamento "${$scope.objetoFaturamento.numero}" possui a mesma descrição do item desta nova etapa.`);
						}
					};

					Promise.resolve()
					.then(checkDescricao)
					.then(checkDescricaoResult)
					.then(() => {
						$scope.notification.success("", "Etapa alterada com sucesso");
						$scope.action = false;
						$mdDialog.hide();
					})
					.catch(catchError);

				}).catch(catchError);
			}
		};

		$scope.habilitarEdicao = function () {
			$scope.screenMode = 'EDIT';
		};

		function carregarListaContatos() {
			if (angular.isFunction($scope.fn_listaContatos)) {
				$scope.fn_listaContatos().then(function (data) {
					$scope.montarCampoDiscrimininacaoFinalEtapa(data);
				}).catch(catchError);
			}
		}

		$scope.changeExibirRetencaoAcessorio = function () {
			carregarListaContatos();
		};

		$scope.changeExibirRetencaoContratual = function () {
			carregarListaContatos();
		};

		$scope.changeExibirPermuta = function () {
			carregarListaContatos();
		};


		//metodo especifico para a parte "final" de etapa ($scope = objetoFaturamento)
		$scope.montarCampoDiscrimininacaoFinalEtapa = function (contatos) {

			$scope.nome_contato_faturamento_gestor = " ";
			$scope.email_contato_faturamento_gestor = " ";

			if (!CalculadorFaturamento.isCampoVazio(contatos) && contatos.length > 0) {
				for (let i = 0; i < contatos.length; i++) {
					let contato = contatos[i];

					if (!CalculadorFaturamento.isCampoVazio(contato) && !CalculadorFaturamento.isCampoVazio(contato.email) && !CalculadorFaturamento.isCampoVazio(contato.nomeRelacionamentoContato)) {

						if (contato.nomeRelacionamentoContato === "Faturamento (gestor de contrato)" && $scope.nome_contato_faturamento_gestor === " ") {
							$scope.nome_contato_faturamento_gestor = !CalculadorFaturamento.isCampoVazio(contato.nome) ? contato.nome.toUpperCase() : " ";
							$scope.email_contato_faturamento_gestor = contato.email.toUpperCase();
						}
					} //fim do isCampoVazio - contato
				} //fim do for
			}

			let discriminacaoParteFinal = "";

			if ($scope.nome_contato_faturamento_gestor !== "" && $scope.nome_contato_faturamento_gestor !== " ") {
				discriminacaoParteFinal = discriminacaoParteFinal + "GESTOR: " + "nome_contato_faturamento_gestor \n";
			}

			if ($scope.email_contato_faturamento_gestor !== "" && $scope.email_contato_faturamento_gestor !== " ") {
				discriminacaoParteFinal = discriminacaoParteFinal + "E-MAIL: " + "email_contato_faturamento_gestor \n";
			}
			if ($scope.objetoFaturamento.tipoDocumentoPagamento === "Boleto") {
				discriminacaoParteFinal = discriminacaoParteFinal +
					"pagamento_boleto \n" +
					"VALOR DOS SERVICOS ..................................    -          " + "valor_faturamento";
			} else {
				discriminacaoParteFinal = discriminacaoParteFinal +
					"PAGTO: " + "nome_banco \n" + "AG. " + "numero_agencia" +
					" C/C " + "numero_conta \n" + "VALOR DOS SERVICOS ..................................    -          " + "valor_faturamento";
			}


			if ($scope.objetoFaturamento.faturamentoRetencoes.length > 0) {
				angular.forEach($scope.objetoFaturamento.faturamentoRetencoes, function (value, key) {
					//ver de onde vêm os tributos, percentual, se é do valor bruto, etc
					if (value.tipo.chave === 'contratual') {
						let percContrat = $scope.objetoFaturamento.percentualRetencaoContratual;
						let total = ($scope.object.valor * percContrat) / 100;
						if (angular.isDefined(total) && total > 0 && $scope.object.exibeRetencaoContratual) {
							discriminacaoParteFinal = discriminacaoParteFinal + "\nRETENCAO CONTRATUAL DE " + $filter('currency')(percContrat, '') + "%" + "................................    -          " + $filter('currency')(total, 'R$ ');
						}
					} else if (value.tipo.chave === 'acessoria' && $scope.object.exibeRetencaoAcessoria) {
						let percAcessoria = $scope.objetoFaturamento.percentualRetencaoAcessoria;
						let totalAcessoria = ($scope.object.valor * percAcessoria) / 100;
						if (angular.isDefined(totalAcessoria) && totalAcessoria > 0) {
							discriminacaoParteFinal = discriminacaoParteFinal + "\nRETENCAO ACESSORIA DE " + $filter('currency')(percAcessoria, '') + "%" + "................................    -          " + $filter('currency')(totalAcessoria, 'R$ ');
						}
					}
				});
			}

			if ($scope.object.valorPermuta > 0 && $scope.object.exibePermuta) {
				let valorPermuta = $filter('currency')($scope.object.valorPermuta, 'R$ ');
				discriminacaoParteFinal = discriminacaoParteFinal + "\nPERMUTA .............................................    -";
				let x = 0;
				for (x; x < 18 - valorPermuta.length; x++) {
					discriminacaoParteFinal = discriminacaoParteFinal + " ";
				}

				discriminacaoParteFinal = discriminacaoParteFinal + valorPermuta;
			}



			let labelValorTributo = 0;
			let totalPercentualTributo = 0;
			let totalValor = 0;

			if ($scope.issRetencaoCliente) {
				if ($scope.totalValorTributo > 0) {
					labelValorTributo = $scope.totalValorTributo;
					totalPercentualTributo = $scope.totalPercentualTributo;
					totalValor = (totalPercentualTributo * $scope.object.valor) / 100;
				}
			} else {
				if ($scope.subTotalValorTributo > 0) {
					labelValorTributo = $scope.subTotalValorTributo;
					totalPercentualTributo = $scope.subtotalPercentualTributo;
					totalValor = (totalPercentualTributo * $scope.object.valor) / 100;
				}
			}


			if (totalValor > 0 && totalPercentualTributo > 0) {
				discriminacaoParteFinal = discriminacaoParteFinal + "\nVALOR TOTAL DOS TRIBUTOS FEDERAIS, ESTADUAIS E MUNICIPAIS";
				discriminacaoParteFinal = discriminacaoParteFinal + "\nCONFORME DISPOSTO NA LEI 12.741/12";
				discriminacaoParteFinal = discriminacaoParteFinal + "\nCARGA TRIBUTARIA = " + "percentual_tributo" + "% VALOR DOS TRIBUTOS = " + "valor_tributo";
				discriminacaoParteFinal = discriminacaoParteFinal.replace("percentual_tributo", $filter('currency')(totalPercentualTributo, ''));
				discriminacaoParteFinal = discriminacaoParteFinal.replace("valor_tributo", $filter('currency')(totalValor, 'R$ '));
			}

			discriminacaoParteFinal = discriminacaoParteFinal.replace("nome_contato_faturamento_gestor", $scope.nome_contato_faturamento_gestor);
			discriminacaoParteFinal = discriminacaoParteFinal.replace("email_contato_faturamento_gestor", $scope.email_contato_faturamento_gestor);

			let labelAgencia = $scope.objetoFaturamento.contaCorrente.agencia;
			if (!CalculadorFaturamento.isCampoVazio($scope.objetoFaturamento.contaCorrente.digitoAgencia)) {
				$scope.objetoFaturamento.contaCorrente.digitoAgencia = $scope.objetoFaturamento.contaCorrente.digitoAgencia.trim();

				if (!CalculadorFaturamento.isCampoVazio($scope.objetoFaturamento.contaCorrente.digitoAgencia))
					labelAgencia += '-' + $scope.objetoFaturamento.contaCorrente.digitoAgencia;
			}

			let labelConta = $scope.objetoFaturamento.contaCorrente.conta;
			if (!CalculadorFaturamento.isCampoVazio($scope.objetoFaturamento.contaCorrente.digito)) {
				$scope.objetoFaturamento.contaCorrente.digito = $scope.objetoFaturamento.contaCorrente.digito.trim();

				if (!CalculadorFaturamento.isCampoVazio($scope.objetoFaturamento.contaCorrente.digito))
					labelConta += '-' + $scope.objetoFaturamento.contaCorrente.digito;
			}

			if ($scope.objetoFaturamento.tipoDocumentoPagamento === "Boleto") {
				let linhas = $scope.objetoFaturamento.discriminacaoFinal.split("\n");
				discriminacaoParteFinal = discriminacaoParteFinal.replace("pagamento_boleto", linhas[0]);

			} else {

				discriminacaoParteFinal = discriminacaoParteFinal.replace("nome_banco", $scope.objetoFaturamento.contaCorrente.banco.nome.toUpperCase());
				discriminacaoParteFinal = discriminacaoParteFinal.replace("numero_agencia", labelAgencia.toUpperCase());
				discriminacaoParteFinal = discriminacaoParteFinal.replace("numero_conta", labelConta.toUpperCase());

			}

			let labelValor = $filter('currency')($scope.object.valor, 'R$ ');
			if (angular.isUndefined(labelValor)) {
				labelValor = "0,00";
			}
			discriminacaoParteFinal = discriminacaoParteFinal.replace("valor_faturamento", labelValor);


			if (CalculadorFaturamento.isCampoVazio($scope.nome_contato_faturamento_gestor) || CalculadorFaturamento.isCampoVazio($scope.email_contato_faturamento_gestor)) {
				let linhas = discriminacaoParteFinal.split("\n");
				discriminacaoParteFinal = linhas[2] + " \n" + linhas[3] + " \n" + linhas[4] + " \n" + linhas[5] + " \n" + linhas[6] + " \n" + linhas[7];
			}

			discriminacaoParteFinal = complementarDiscriminacaoFinalComTributos($scope, discriminacaoParteFinal);
			$scope.object.discriminacaoFinal = discriminacaoParteFinal;
		};

		function montarCampoDiscrimininacaoInicialEtapa() {
			let textoDescricao = copiaDiscriminacaoInicial;

			if (textoDescricao && textoDescricao.length > 0) {

				//				let arrayLinhas = textoDescricao.trim().match(/[^\r\n]+/g);
				let arrayLinhas = textoDescricao.trim().split("\n");
				let numeroLinhas = arrayLinhas.length;

				let temPeriodo = false;
				let linhaPeriodo = null;

				let temPedido = false;
				let linhaPedido = null;

				let temItem = false;
				let linhaItem = null;

				let temCODT6 = false;
				let linhaCODT6 = null;

				let temBaseLegal = false;
				let linhaBaseLegal = null;

				let temGestor = false;
				let linhaGestor = null;

				angular.forEach(arrayLinhas, function (value, key) {
					let linha = value;

					let periodo = linha.replace(/\s/g, "").replace("Í", "I").indexOf("PERIODO");
					if (periodo === 0) {
						temPeriodo = true;
						linhaPeriodo = key;
					}

					let pedido = linha.replace(/\s/g, "").indexOf("PEDIDO");
					if (pedido === 0) {
						temPedido = true;
						linhaPedido = key;
					}

					let item = linha.replace(/\s/g, "").indexOf("ITEM");
					if (item === 0) {
						temItem = true;
						linhaItem = key;
					}

					let CODT6 = linha.replace(/\s/g, "").indexOf("CODT6");
					if (CODT6 === 0) {
						temCODT6 = true;
						linhaCODT6 = key;
					}

					let baseLegal = linha.replace(/\s/g, "").indexOf("BASELEGAL");
					if (baseLegal === 0) {
						temBaseLegal = true;
						linhaBaseLegal = key;
					}

					let gestor = linha.replace(/\s/g, "").indexOf("GESTOR");
					if (gestor === 0) {
						temGestor = true;
						linhaGestor = key;
					}
				});

				let novoTexto = "";

				let config_1 = false; // tem "PEDIDO" e "ITEM" (Valor fica acima de BASE LEGAL)
				let config_2 = false; // tem "PEDIDO" mas não tem "ITEM" (Valor fica abaixo de PEDIDO)
				let config_3 = false; // não tem "PEDIDO" e nem "ITEM". E nem GESTOR e CODT6 (Valor fica na ultima linha)
				let config_4 = false; // faturamento pertencer a um cliente do Grupo Oi e tem "CODT6" (Valor fica acima de CODT6)
				let config_5 = false; // não tem "PEDIDO" e nem "ITEM". Mas tem GESTOR ou CODT6 (Valor fica acima de GESTOR ou CODT6)
				let config_6 = false; // tem "PEDIDO" e "ITEM", mas não tem  "BASE LEGAL". (Valor fica na ultima linha)

				if (temPedido && temItem && temBaseLegal) {
					config_1 = true;
				}

				if (temPedido && !temItem) {
					config_2 = true;
				}

				if (!temPedido && !temItem && !temGestor && !temCODT6) {
					config_3 = true;
				}

				if (!temPedido && !temItem && (temGestor || temCODT6)) {
					config_5 = true;
				}

				if (temPedido && temItem && !temBaseLegal) {
					config_6 = true;
				}

				if (temCODT6 && isGrupoOi()) {
					config_4 = true;
					config_1 = false;
					config_2 = false;
					config_3 = false;
					config_5 = false;
					config_6 = false;
				}

				angular.forEach(arrayLinhas, function (value, key) {
					let linha = value;

					if (linhaPeriodo === key) {
						novoTexto = novoTexto.concat("PERIODO: ");

						if ($scope.object.inicio && $scope.object.termino) {

							// mesmo mês
							if ($scope.object.inicio.getMonth() === $scope.object.termino.getMonth()) {
								//primeiro dia do mes e ultimo dia do mes
								if ($scope.object.inicio.getDate() === 1 && $scope.object.termino.getDate() === lastday($scope.object.termino.getFullYear(), $scope.object.termino.getMonth())) {
									let dataFormatada = $filter('date')($scope.object.termino, 'MMMM/yyyy');
									dataFormatada = dataFormatada.toUpperCase();
									novoTexto = novoTexto.concat(dataFormatada);
								} else {
									let dataFormatadaInicio = $filter('date')($scope.object.inicio, 'dd/MM/yyyy');
									let dataFormatadaTermino = $filter('date')($scope.object.termino, 'dd/MM/yyyy');
									novoTexto = novoTexto.concat(dataFormatadaInicio);
									novoTexto = novoTexto.concat(" A ");
									novoTexto = novoTexto.concat(dataFormatadaTermino);
								}
							} else {
								let dataFormatadaInicio = $filter('date')($scope.object.inicio, 'dd/MM/yyyy');
								let dataFormatadaTermino = $filter('date')($scope.object.termino, 'dd/MM/yyyy');
								novoTexto = novoTexto.concat(dataFormatadaInicio);
								novoTexto = novoTexto.concat(" A ");
								novoTexto = novoTexto.concat(dataFormatadaTermino);
							}
						}
					} else {

						if (config_1) {
							if (linhaBaseLegal - 1 === key) {
								let posicaoPalavraRS = linha.replace(/\s/g, "").indexOf("R$");

								let labelValor = $filter('currency')($scope.object.valor, 'R$ ');
								if (!labelValor) {
									labelValor = "";
								}

								if (posicaoPalavraRS === 0) {
									novoTexto = novoTexto.concat(labelValor);
								} else {
									novoTexto = novoTexto.concat(linha);
									novoTexto = novoTexto.concat("\n");
									novoTexto = novoTexto.concat(labelValor);
								}
							} else {
								novoTexto = novoTexto.concat(linha);
							}

						} else if (config_2) {
							if (linhaPedido + 1 === key) {
								let posicaoPalavraRS = linha.replace(/\s/g, "").indexOf("R$");

								let labelValor = $filter('currency')($scope.object.valor, 'R$ ');
								if (!labelValor) {
                                    labelValor = "";
								}

								if (posicaoPalavraRS === 0) {
									novoTexto = novoTexto.concat(labelValor);
								} else {
									novoTexto = novoTexto.concat(labelValor);
									novoTexto = novoTexto.concat("\n");
									novoTexto = novoTexto.concat(linha);
								}
							} else {
								novoTexto = novoTexto.concat(linha);
							}

						} else if (config_3 || config_6) {
							if (numeroLinhas === (key + 1)) {
								let posicaoPalavraRS = linha.replace(/\s/g, "").indexOf("R$");

								let labelValor = $filter('currency')($scope.object.valor, 'R$ ');
								if (!labelValor) {
                                    labelValor = "";
								}

								if (posicaoPalavraRS === 0) {
									novoTexto = novoTexto.concat(labelValor);
								} else {
									novoTexto = novoTexto.concat(linha).concat("\n").concat(labelValor);
								}

							} else {
								novoTexto = novoTexto.concat(linha);
							}

						} else if (config_4) {
							if (linhaCODT6 - 1 === key) {
								let posicaoPalavraRS = linha.replace(/\s/g, "").indexOf("R$");

								let labelValor = $filter('currency')($scope.object.valor, 'R$ ');
								if (!labelValor) {
                                    labelValor = "";
                                }

								if (posicaoPalavraRS === 0) {
									novoTexto = novoTexto.concat(labelValor);
								} else {
									if (linha !== "") {
										novoTexto = novoTexto.concat(linha);
									}
									novoTexto = novoTexto.concat(labelValor);
									novoTexto = novoTexto.concat("\n");
								}
							} else {
								novoTexto = novoTexto.concat(linha);
							}
						} else if (config_5) {
							let linhaGestorOuCodt6;

							if (temCODT6) {
								linhaGestorOuCodt6 = linhaCODT6;
							} else {
								linhaGestorOuCodt6 = linhaGestor;
							}

							if (linhaGestorOuCodt6 - 1 === key) {
								let posicaoPalavraRS = linha.replace(/\s/g, "").indexOf("R$");

								let labelValor = $filter('currency')($scope.object.valor, 'R$ ');
								if (!labelValor || labelValor === 'R$ 0,00') {
									labelValor = "";
								}

								if (posicaoPalavraRS === 0) {
									novoTexto = novoTexto.concat(labelValor);
								} else {
									if (linha !== "") {
										novoTexto = novoTexto.concat(linha);
									}
									novoTexto = novoTexto.concat(labelValor);
									novoTexto = novoTexto.concat("\n");
								}
							} else {
								novoTexto = novoTexto.concat(linha);
							}
						} else {
							novoTexto = novoTexto.concat(linha);
						}
					}
					novoTexto = novoTexto.concat("\n");
				});
				$scope.object.discriminacaoInicial = novoTexto;
			}
		}

		// Função que retorna o ultimo dia de um mês
		// y -> inteiro que representa ano / m -> inteiro que representa mês (em java => janeiro = 0, fevereiro = 1, ...)
		let lastday = function (y, m) {
			return new Date(y, m + 1, 0).getDate();
		}

		function isGrupoOi() {
			let mySet = new Set();
			mySet.add("76.535.764/");
			mySet.add("04.014.081/");
			mySet.add("07.953.678/");
			mySet.add("33.000.118/");
			mySet.add("05.423.963/");

			let documento = $scope.objetoFaturamento.cliente.documento;
			let subDoc = documento.replace(/\s/g, "").substring(0, 11);

			if (mySet.has(subDoc)) {
				return true;
			} else {
				return false;
			}
		}

	}

})();
