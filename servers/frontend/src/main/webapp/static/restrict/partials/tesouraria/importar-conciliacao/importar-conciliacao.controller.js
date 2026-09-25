(function(){

	'use strict';

	angular
		.module('app.importarconciliacao')

		.controller('ImportarConciliacao', detailImportarConciliacao);

	function detailImportarConciliacao($scope, $state, $mdDialog, $window, $timeout, $rootScope, Unidade, ContaCorrente, ConciliacaoBancaria, ConciliacaoLancamento, Banco, CentroFinanceiro, EnumService, Lancamentos, RelacaoBancaria, HistoricoConciliacaoBancaria, Parametro) {

		function init(){
			$scope.object = {};
			$scope.object.unidade = null
			$scope.object.contaCorrente = null;
			$scope.object.conciliacao = null;

			$scope.modulo = $state.current.data.modulo;
			$scope.titulo = $state.current.data.titulo;

			$scope.listEmpresas = [];
			$scope.listaContaCorrente = [];
			$scope.listaConciliacaoLancamento = [];
			$scope.fileName = null;
			$scope.file = null;
			$scope.desabilitarBotaoProcessar = true;
			$scope.mostraBotaoLimpar = false;

			initUnidade();
			initTipoArquivoAnexo();

			$scope.conciliacaoBancaria = null;
			$scope.codConciliacaoBancaria = null;

			$scope.totalCredito = 0;
			$scope.totalDebito = 0;

			$scope.saldoInicial = 0;
			$scope.saldoFinal = 0;
			$scope.situacaoInicial = "";
			$scope.situacaoFinal = "";

			$scope.mostrarResultado = false;

			$scope.listaTipoDebito = [];
			$scope.listaTipoCredito = [];

			$scope.codigoContaCorrenteConciliacao = null;
		};

		init();

		/**
		* ------------------------
		* INICIALIZAÇÃO DOS CAMPOS:
		* ------------------------
		*/

		function initUnidade() {
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

		function initCampoContaCorrente(objUnidade) {

			if(!angular.isUndefined(objUnidade) && objUnidade !== null && objUnidade.codigo !== null) {

				var objContaCorrente = {codigo: null};
				$scope.listaContaCorrente.push(objContaCorrente);

				var pContaCorrente = ContaCorrente.page(null, { pessoa: {codigo: objUnidade.codigo} , conciliacao: true }).$promise;

				pContaCorrente.then(function(data) {
					angular.forEach(data.content, function(value, key) {
						$scope.listaContaCorrente.push(value);
					});
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'load', response);
				});
			}
		}

		function initTipoArquivoAnexo() {
			var parametro = {
				chave: "tesouraria.conciliacao.anexo.tipos"
			};

			Parametro.page(parametro).$promise.then(function(data) {

				var tipoAnexo = data.valor;
				$scope.tipoArquivoAnexo = tipoAnexo;
			}).catch(function(response) {
				console.error('crud-critical', 'dialog', response);
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
		}


		/**
		* ----------
		* ONCHANGES:
		* ----------
		*/

		$scope.changeEmpresa = function() {
			$scope.object.contaCorrente = null;
			$scope.listaContaCorrente = [];

			if ($scope.object.unidade.codigo === null) {
				delete $scope.object.unidade;
			} else {
				initCampoContaCorrente($scope.object.unidade);
			}
		};

		/**
		* ----------------------------
		* CRIAR TEXTOS PARA EXIBIÇÃO:
		* ----------------------------
		*/

		$scope.montarTextoContaCorrente = function (objContaCorrente) {
			var texto = "";

			if (objContaCorrente && objContaCorrente.codigo != null) {
				texto = texto.concat(objContaCorrente.banco.nome);
				texto = texto.concat(" / ");
				texto = texto.concat(objContaCorrente.agencia);

				if (!angular.isUndefined(objContaCorrente.digitoAgencia) && objContaCorrente.digitoAgencia != null && objContaCorrente.digitoAgencia.trim() !== "" ) {
					texto = texto.concat("-");
					texto = texto.concat(objContaCorrente.digitoAgencia);
				}

				texto = texto.concat(" / ");
				texto = texto.concat(objContaCorrente.conta);

				if (!angular.isUndefined(objContaCorrente.digito) && objContaCorrente.digito != null && objContaCorrente.digito.trim() !== "") {
					texto = texto.concat("-");
					texto = texto.concat(objContaCorrente.digito);
				}
			}

			return texto;
		};

		$scope.montarNumeroRegistro = function (numero) {
			var stringNumero = "" + numero;
			if(stringNumero) {
				var tamanhoString = stringNumero.length;

				if (tamanhoString < 5) {
					var numeroRegistro = "";
					var zeros = 5 - tamanhoString;
					for(var i = 0; i < zeros; i++) {
						numeroRegistro = numeroRegistro.concat("0");
					}
					numeroRegistro = numeroRegistro.concat(numero);
					return numeroRegistro;
				} else {
					return numero;
				}
			} else {
				return numero;
			}
		};

		$scope.montarNaturezaToolTip = function (natureza) {
			var toolTip = "";

			if (natureza === 'CDS') {
				toolTip = toolTip.concat("Lançamento composto de recursos disponíveis e bloqueados");
			}
			if (natureza === 'DPV') {
				toolTip = toolTip.concat("Lançamento ocorrido em recurso 'disponível'");
			}
			if (natureza === 'SCR') {
				toolTip = toolTip.concat("Lançamento ocorrido em recurso 'disponível', porém não liberado para uso");
			}
			if (natureza === 'SSR') {
				toolTip = toolTip.concat("Lançamento ocorrido em recurso à compensar");
			}

			return toolTip;
		};


		/**
		* --------
		* WATCHES:
		* --------
		*/

		$scope.$watch('fileName', function(){
			habilitarProcessamento();
	  	});
	  	$scope.$watch('object.contaCorrente', function(){
	  		if ($scope.object.contaCorrente && $scope.object.contaCorrente.codigo == null) {
	  			delete $scope.object.contaCorrente;
	  		}

			habilitarProcessamento();
		});
		$scope.$watch('object.conciliacao', function(){
			habilitarProcessamento();
		});

	  	function habilitarProcessamento() {
	  		if ($scope.fileName && $scope.file && $scope.object && $scope.object.contaCorrente && $scope.object.conciliacao) {
				$scope.desabilitarBotaoProcessar = false;
				$scope.mostraBotaoLimpar = true;
			} else {
				$scope.desabilitarBotaoProcessar = true;
				$scope.mostraBotaoLimpar = false;
			}
	  	}

	  	/**
	  	* -----------------------
	  	* FUNÇÕES PARA OS BOTÕES:
	  	* -----------------------
	  	*/

	  	$scope.limpar = function() {
	  		$scope.object = {};
	  		$scope.fileName = null;
			$scope.file = null;

			$scope.listaContaCorrente = [];
			$scope.listaConciliacaoLancamento = [];

			$scope.desabilitarBotaoProcessar = true;
			$scope.mostraBotaoLimpar = false;

			$scope.codConciliacaoBancaria = null;
			$scope.mostrarResultado = false;

			$scope.listaTipoDebito = [];
			$scope.listaTipoCredito = [];
			$scope.codigoContaCorrenteConciliacao = null;
	  	};

	  	$scope.processar = function() {

			if (tipoArquivoValido()) {

				$scope.listaConciliacaoLancamento = [];
				$scope.conciliacaoBancaria = null;
				$scope.codConciliacaoBancaria = null;
				$scope.totalCredito = 0;
				$scope.totalDebito = 0;
				$scope.saldoInicial = 0;
				$scope.saldoFinal = 0;
				$scope.situacaoInicial = "";
				$scope.situacaoFinal = "";
				$scope.mostrarResultado = false;

				$scope.listaTipoDebito = [];
				$scope.listaTipoCredito = [];

				$rootScope.loadingProgress = true;

				$timeout(function(){
					validarUnicidade();
				}, 500);

			}
		};

		$scope.imprimir = function(ev) {
			if ($scope.codConciliacaoBancaria) {

				//var "producao" = false para testes e desenv. Em produçao, nao esquecer de setar para "true"
				var producao = true;
				var url = "../../relatorio/frameset?__locale=pt_BR&__format=pdf&__showtitle=false&__report=report/sgi/tesouraria/";

				if (producao == false) {
					url = 'http://localhost' + ":8081/sgi/relatorio/frameset?__locale=pt_BR&__format=pdf&__showtitle=false&__report=report/sgi/tesouraria/";
				}

				url = url.concat("conciliacao_bancaria.rptdesign");
				url = url.concat("&codigoConciliacaoBancaria=" + $scope.codConciliacaoBancaria);

				$window.open(url);
			}
        };

		$scope.salvar = function() {
			if(isValoresValidos()) {
				salvarConciliacaoBancaria();
			}
		};

		$scope.cancelar = function() {
			$scope.limpar();
		};

		$scope.adicionarAssociacao = function(evento, conciliacaoLancamentoTO) {
			adicionarRelacaoBancaria(evento, conciliacaoLancamentoTO);
		};

		$scope.adicionarAssociacaoLancamento = function(evento, conciliacaoLancamentoTO) {
			adicionarLancamentoExistente(evento, conciliacaoLancamentoTO);
		};

		$scope.criarAssociacaoLancamento = function(evento, conciliacaoLancamentoTO, codHistorico) {
			adicionarLancamento(evento, conciliacaoLancamentoTO, codHistorico);
		};

		$scope.isTipoLancamento = function(conciliacaoLancamentoTO){
			var initHistorico = conciliacaoLancamentoTO.historico.substring(0, 3);

			if(initHistorico === "TAR") {
				return true;
			}
			return false;
		};

		$scope.isTipoRB = function(conciliacaoLancamentoTO){
			var initHistorico = conciliacaoLancamentoTO.historico.substring(0, 3);

			if(initHistorico === "PAG" || initHistorico === "SIS" || initHistorico === "CH ") {
				return true;
			}
			return false;
		};

		$scope.editarAssociacao = function(evento, conciliacaoLancamentoTO) {
			editarLancamento(evento, conciliacaoLancamentoTO);
		};

		$scope.removerAssociacao = function(evento, conciliacaoLancamentoTO) {
			removerRelacaoBancaria(evento, conciliacaoLancamentoTO);
		};

		$scope.removerAssociacaoLancamento = function(evento, conciliacaoLancamentoTO) {
			removerLancamento(evento, conciliacaoLancamentoTO);
		};

		/* ---------------
		 * 	SALVAR DADOS:
		 * ---------------
		 */

		 function salvarConciliacaoBancaria() {
		 	if ($scope.conciliacaoBancaria) {

		 		angular.forEach($scope.listaConciliacaoLancamento, function(value, key) {
		 			if (value.situacao === "Não pendente" ) {
		 				$scope.conciliacaoBancaria.situacao = 1;
		 				return;
		 			}
		 		});

		 		var dadosConciliacaoBancaria = {};
		 		dadosConciliacaoBancaria.conciliacaoBancaria = $scope.conciliacaoBancaria;
		 		dadosConciliacaoBancaria.listaConciliacaoLancamento = $scope.listaConciliacaoLancamento;

		 		var pSave = ConciliacaoBancaria.salvardados(dadosConciliacaoBancaria).$promise;

		 		pSave.then(function(data){
		 			$scope.notification.success(null, "Conciliação Bancária salva com sucesso.");
		 			$scope.codConciliacaoBancaria = data.codigo;
		 		}).catch(function(response){
		 			if (response && response.data && response.data.message) {
		 				$scope.notification.error("", response.data.message);
		 			} else {
						$scope.$emit('crud-critical', 'save', response);
		 			}
				});
		 	}
		 }

	  	/**
	  	* -------------
	  	*  VALIDAÇÕES:
	  	* -------------
	  	*/

	  	function tipoArquivoValido () {
	  		var tipoArquivoValido = false;

			if ($scope.fileName && $scope.file) {

				if ($scope.file.type === "text/plain" || $scope.file.type === "") {
					tipoArquivoValido = true;

				}

				if(!tipoArquivoValido) {
					$scope.notification.error(null, "Este arquivo não é do tipo txt ou texto.");
				}
			}

	  		return tipoArquivoValido;
	  	}

	  	function validarUnicidade() {

	  		var conciliacaoBancaria = criarConciliacaoBancaria();
			var pConciliacaoBancaria = ConciliacaoBancaria.validarunicidade(conciliacaoBancaria).$promise;

			pConciliacaoBancaria.then(function(data) {

				var reader = new FileReader();

				reader.onload = function (event) {

					$scope.$apply(function() {
						conciliacaoBancaria.anexo.texto = reader.result;

						var arquivoValido = validarArquivo(conciliacaoBancaria);

						if (arquivoValido) {
							$scope.mostrarResultado = true;
							processarArquivo(conciliacaoBancaria);

							$scope.codigoContaCorrenteConciliacao = $scope.object.contaCorrente.codigo;

						} else {
							$scope.mostrarResultado = false;
							$rootScope.loadingProgress = false;
						}

					});
				}

				reader.readAsText($scope.file);

			}).catch(function(response){
				$scope.mostrarResultado = false;
				$rootScope.loadingProgress = false;
				$scope.notification.error("", "Já existe uma conciliação bancária para a conta corrente nesta data. Não é possível realizar uma nova conciliação bancária");
				return false;
			});

	  	}

	  	function validarArquivo(conciliacaoBancaria) {
	  		var arquivoValido = true;

	  		if (conciliacaoBancaria.anexo.texto) {
	  			var separador = /[^\r\n]+/g;
	  			var listaLinhas = conciliacaoBancaria.anexo.texto.match(separador);

	  			var index = 0;
	  			var indexPenultimaLinha = listaLinhas.length - 1;
	  			var indexUltimaLinha = listaLinhas.length;

	  			angular.forEach(listaLinhas, function(value, key) {
	  				index++;
	  				var colunaOito = value.substring(7, 8);

	  				var validacao_1 = validarTamanhoDeCaracteresDaLinha(value);
	  				var validacao_2 = validarHeaderDoArquivo(index, colunaOito);
	  				var validacao_3 = validarHeaderDoLote(index, colunaOito);
	  				var validacao_4 = validarDetalhe(index, indexPenultimaLinha, colunaOito);
	  				var validacao_5 = validarTrailerDoLote(index, indexPenultimaLinha, colunaOito);
	  				var validacao_6 = validarTrailerDoArquivo(index, indexUltimaLinha, colunaOito);
	  				var validacao_7 = validarContaCorrente(conciliacaoBancaria, value, index);

					if (index === indexPenultimaLinha) {
	  					var validacao_8 = validarData(conciliacaoBancaria, value, index);
					}

	  				if (!validacao_1 || !validacao_2 || !validacao_3 || !validacao_4 || !validacao_5
	  					|| !validacao_6 || !validacao_7 || (angular.isDefined(validacao_8) && !validacao_8)) {
						arquivoValido = false;
						return;
					}
				});
	  		}

	  		return arquivoValido;
	  	}

	  	function validarTamanhoDeCaracteresDaLinha(linha) {
	  		var tamanho = linha.length;

	  		if (tamanho === 240) {
	  			return true;
	  		} else {
	  			$scope.notification.error("" ,"As linhas do arquivo não possuem 240 caracteres (conforme layout padrão).");
	  			return false;
	  		}
	  	}

	  	function validarHeaderDoArquivo(index, colunaOito) {
	  		if (index === 1) {
				if (colunaOito === "0") {
					return true;
				} else {
					$scope.notification.error("" ,"Não foi possível encontrar o header do arquivo (conforme layout padrão).");
					return false;
				}
	  		} else {
	  			return true;
	  		}
	  	}

	  	function validarHeaderDoLote(index, colunaOito) {
	  		if (index === 2) {
				if (colunaOito === "1") {
					return true;
				} else {
					$scope.notification.error("", "Não foi possível encontrar o header do lote (conforme layout padrão).");
					return false;
				}
			} else {
				return true;
			}
	  	}

	  	function validarDetalhe(index, penultimaLinha, colunaOito) {
	  		if (index > 2 && index < penultimaLinha) {
				if (colunaOito === "3") {
					return true;
				} else {
					$scope.notification.error("", "A " + index + "ª linha do arquivo é inválida (conforme layout padrão).");
					return false;
				}
			} else {
				return true;
			}
	  	}

		function validarTrailerDoLote(index, penultimaLinha, colunaOito) {
	  		if (index === penultimaLinha) {
				if (colunaOito === "5") {
					return true;
				} else {
					$scope.notification.error("", "Não foi possível encontrar o trailer do lote (conforme layout padrão).");
					return false;
				}
			} else {
				return true;
			}
	  	}

		function validarTrailerDoArquivo(index, ultimaLinha, colunaOito) {
	  		if (index === ultimaLinha) {
				if (colunaOito === "9") {
					return true;
				} else {
					$scope.notification.error("", "Não foi possível encontrar o trailer do arquivo (conforme layout padrão).");
					return false;
				}
			} else {
				return true;
			}
	  	}

	  	function validarContaCorrente(conciliacaoBancaria, linha, index) {
	  		if (index === 1) {
	  			var codigoBanco = linha.substring(0, 3);

				var agencia = linha.substring(52, 57);
				var digitoAgencia = linha.substring(57, 58);

				var conta = linha.substring(58, 70);
				var digitoConta = linha.substring(71, 72);

				var contaCorrente = conciliacaoBancaria.contaCorrente;
				var banco = contaCorrente.banco;

				var contaValida = true;

				//Verifica conta, agencia e o banco
				if (agencia !== contaCorrente.agencia || conta !== contaCorrente.conta || codigoBanco !== (banco.codigo + "")) {
					contaValida = false;
				}

				//se houver o digito da agencia, faz a verificacao
				if (contaCorrente.digitoAgencia && contaCorrente.digitoAgencia.trim() !== "" && contaCorrente.digitoAgencia !== digitoAgencia) {
					contaValida = false;
				}

				//se houver digito da conta, faz a verificacao
				if (contaCorrente.digito && contaCorrente.digito.trim() !== "" && contaCorrente.digito !== digitoConta) {
					contaValida = false;
				}

				if (!contaValida) {
					$scope.notification.error("", "O arquivo não corresponde à conta corrente selecionada.");
				}

				return contaValida;
	  		} else {
	  			return true;
	  		}
	  	}

		function validarData(conciliacaoBancaria, linha, index) {
			if (conciliacaoBancaria.anexo.texto) {
				var separador = /[^\r\n]+/g;
				var listaLinhas = conciliacaoBancaria.anexo.texto.match(separador);
				var data = linha.substring(142, 150);

				var dia = data.substring(0, 2);
				var mes = data.substring(2, 4);
				var ano = data.substring(4, 8);

				var dataConciliacao = conciliacaoBancaria.data;

				if (Number(dia) === dataConciliacao.getDate() && Number(mes) === (dataConciliacao.getMonth() + 1) && Number(ano) === dataConciliacao.getFullYear()) {
					return true;
				} else {
					$scope.notification.error("", "A data do arquivo não corresponde a data do(s) lançamento(s) informada.");
					return false;
				}
			}
	  	}

	  	//Verifica se o valor dos registros é igual ao valor dos pagamentos adicionados
	  	function isValoresValidos() {
	  		var validado = true;

	  		angular.forEach($scope.listaConciliacaoLancamento, function(value, key) {
	  			if(value.objLancamento) {
	  				if(value.objLancamento.valor != value.valor) {

	  					validado = false;
	  					var numRe = $scope.montarNumeroRegistro(value.registro);

	  					if(value.objLancamento.valor > value.valor) {
	  						$scope.notification.error("", "O valor dos pagamentos selecionados para o lançamento " + numRe + " é maior que o valor do lançamento.");
	  					} else {
	  						$scope.notification.error("", "O valor dos pagamentos selecionados para o lançamento " + numRe + " é menor que o valor do lançamento.");
	  					}
	  				}
	  			}

	  			if(value.objPagamento) {
	  				if(value.totalPagamentosSelecionados != value.valor) {

						validado = false;
						var numRe = $scope.montarNumeroRegistro(value.registro);

						if(value.totalPagamentosSelecionados > value.valor) {
							$scope.notification.error("", "O valor dos pagamentos selecionados para o lançamento " + numRe + " é maior que o valor do lançamento.");
						} else {
							$scope.notification.error("", "O valor dos pagamentos selecionados para o lançamento " + numRe + " é menor que o valor do lançamento.");
						}
					}
	  			}
			});

			return validado;
	  	}


	  	/**
		* -------------------
		*  PROCESSAR ARQUIVO:
		* -------------------
		*/

		function processarArquivo(conciliacaoBancaria) {
			$scope.listaConciliacaoLancamento = [];

			if (conciliacaoBancaria.anexo.texto) {
				var separador = /[^\r\n]+/g;
				var listaLinhas = conciliacaoBancaria.anexo.texto.match(separador);

				var index = 0;
				var indexPenultimaLinha = listaLinhas.length - 1;

				angular.forEach(listaLinhas, function(value, key) {
					index++;

					if (index === 2) {
						var saldoInicialString = value.substring(150, 166);
						saldoInicialString = saldoInicialString.concat(".");
						saldoInicialString = saldoInicialString.concat(value.substring(166, 168));

						$scope.saldoInicial = Number(saldoInicialString);
						$scope.situacaoInicial = value.substring(168, 169);
					}

					if (index === indexPenultimaLinha) {
						var saldoFinalString = value.substring(150, 166);
						saldoFinalString = saldoFinalString.concat(".");
						saldoFinalString = saldoFinalString.concat(value.substring(166, 168));

						$scope.saldoFinal = Number(saldoFinalString);
						$scope.situacaoFinal = value.substring(168, 169);
					}

					if (index > 2 && index < indexPenultimaLinha) {
						var conciliacaoLancamento = criarConciliacaoLancamento(value);
						conciliacaoLancamento.conciliacaoBancaria = conciliacaoBancaria;

						if (conciliacaoLancamento.debitoCredito === "débito") {
							$scope.totalDebito += conciliacaoLancamento.valor;
						} else {
							$scope.totalCredito += conciliacaoLancamento.valor;
						}

						$scope.listaConciliacaoLancamento.push(conciliacaoLancamento);
					}
				});

				conciliacaoBancaria.totalDebito = $scope.totalDebito;
				conciliacaoBancaria.totalCredito = $scope.totalCredito;

				if ($scope.situacaoInicial === 'C') {
					conciliacaoBancaria.saldoInicial = $scope.saldoInicial;
				} else {
					conciliacaoBancaria.saldoInicial = -Math.abs($scope.saldoInicial);
				}

				conciliacaoBancaria.saldoFinal = (conciliacaoBancaria.saldoInicial + $scope.totalCredito) - $scope.totalDebito;
				$scope.conciliacaoBancaria = conciliacaoBancaria;
				$rootScope.loadingProgress = false;
				$scope.mostrarResultado = true;

				criarTipos($scope.listaConciliacaoLancamento);
				verificarCheques($scope.listaConciliacaoLancamento);

			}
		}


	  	/**
	  	* --------------
	  	* CRIAR OBJETOS:
	  	* --------------
	  	*/

	  	function criarConciliacaoBancaria () {
	  		var conciliacaoBancaria = new ConciliacaoBancaria();

			conciliacaoBancaria.codigo = null;
			conciliacaoBancaria.contaCorrente = $scope.object.contaCorrente;
			conciliacaoBancaria.anexo = criarAnexo();
			conciliacaoBancaria.data = $scope.object.conciliacao;
			conciliacaoBancaria.situacao = 0;
			conciliacaoBancaria.totalDebito = 0;
			conciliacaoBancaria.totalCredito = 0;
			conciliacaoBancaria.saldoFinal = 0;
			conciliacaoBancaria.saldoInicial = 0;
			conciliacaoBancaria.criador = "";
			conciliacaoBancaria.dataCriacao = Date.now();
			conciliacaoBancaria.atualizador = null;
			conciliacaoBancaria.dataAtualizacao = null;

			return conciliacaoBancaria;
	  	}

	  	function criarAnexo () {
	  		var anexo = {};

	  		anexo.codigo = null;
	  		anexo.nome = $scope.fileName;
	  		anexo.descricao = "";
	  		anexo.criador = "";
	  		anexo.contentType = $scope.file.type;
	  		anexo.dataCriacao = Date.now();
	  		anexo.texto = null;

	  		return anexo;
		}

		function criarConciliacaoLancamento(linha) {
			var numeroRegistro = linha.substring(8, 13);
			var natureza = linha.substring(108, 111);
			var dataLancamento = linha.substring(142, 150);

			var valor = linha.substring(150, 166);
			valor = valor.concat(".");
			valor = valor.concat(linha.substring(166, 168));

			var tipo = linha.substring(168, 169);
			var historico = linha.substring(176, 201);

			var conciliacaoLancamento = new ConciliacaoLancamento();
			conciliacaoLancamento.situacao = "Pendente";
			conciliacaoLancamento.registro = Number(numeroRegistro);
			conciliacaoLancamento.natureza = natureza;
			conciliacaoLancamento.historico = historico;
			conciliacaoLancamento.valor = Number(valor);
			conciliacaoLancamento.cheque = null;
			conciliacaoLancamento.tipoCheque = false;

			if (tipo === "D") {
				conciliacaoLancamento.debitoCredito = "débito";
			} else {
				conciliacaoLancamento.debitoCredito = "crédito";
			}

			var dia = dataLancamento.substring(0, 2);
			var mes = dataLancamento.substring(2, 4);
			var ano = dataLancamento.substring(4, 8);

			conciliacaoLancamento.lancamento = new Date(Number(ano), Number(mes) - 1, Number(dia));

			return conciliacaoLancamento;
		}


		/* ----------
		 * 	DIALOGS:
		 * ----------
		 */

		 /*
		   INICIO DIALOG LANCAMENTO
		 */
		 function editarLancamento(evento, conciliacaoLancamentoTO){

			$mdDialog.show({
				controller: DialogLancamento,
				locals: {
					index: conciliacaoLancamentoTO.registro,
					conciliacaoLancamento: conciliacaoLancamentoTO,
					tipo: 'edicao',
					notification: $scope.notification,
					fn_confirmAddLancamento: confirmAddLancamento,
					codHistorico: null
				},
				templateUrl: 'partials/tesouraria/importar-conciliacao/dialogs/dialog_lancamento.html',
				parent: angular.element(document.body),
				clickOutsideToClose: false,
				fullscreen: false
			});
		 }

		 function removerLancamento(evento, conciliacaoLancamentoTO){
		 	if(conciliacaoLancamentoTO != null){
		 		conciliacaoLancamentoTO.situacao = "Pendente";
		 		delete conciliacaoLancamentoTO.objLancamento;
		 	}
		 }

		 function adicionarLancamento(evento, conciliacaoLancamentoTO, codHistorico) {

			$mdDialog.show({
				controller: DialogLancamento,
				locals: {
				    index: conciliacaoLancamentoTO.registro,
					conciliacaoLancamento: conciliacaoLancamentoTO,
					tipo: 'novo',
					notification: $scope.notification,
					fn_confirmAddLancamento: confirmAddLancamento,
					codHistorico: codHistorico,
					HistoricoConciliacaoBancaria: HistoricoConciliacaoBancaria
				},
				templateUrl: 'partials/tesouraria/importar-conciliacao/dialogs/dialog_lancamento.html',
				parent: angular.element(document.body),
				clickOutsideToClose: false,
				fullscreen: false
			});
         };

         function confirmAddLancamento(lancamento, numeroRegistro, fn_hide) {

         	angular.forEach($scope.listaConciliacaoLancamento, function(value, key) {

				if (value.registro === numeroRegistro) {
					var obj = value;
					obj.objLancamento = lancamento;
					obj.situacao = "Não pendente";
					return;
				}
			});

			fn_hide();
         }

         function removerLancamentoPreAssociados(listaLancamentos) {
         	var listaFinal = [];
         	var listaLancamentoPreAssociado = [];

         	if (listaLancamentos && listaLancamentos.length > 0) {
         		angular.forEach($scope.listaConciliacaoLancamento, function(value, key) {
					if (value && value.objLancamento && value.objLancamento.codigo) {
						listaLancamentoPreAssociado.push(value.objLancamento);
					}
				});

				if (listaLancamentoPreAssociado && listaLancamentoPreAssociado.length > 0) {

					angular.forEach(listaLancamentos, function(value1, key) {
						var isPreAssociado = false;

						angular.forEach(listaLancamentoPreAssociado, function(value2, key) {
							if (value1.codigo === value2.codigo) {
								isPreAssociado = true;
							}
						});

						if (!isPreAssociado) {
							listaFinal.push(value1);
						}
					});

					return listaFinal;

				} else {
					listaFinal = listaLancamentos;
					return listaFinal;
				}

         	} else {
         		listaFinal = listaLancamentos;
         		return listaFinal;
         	}

         	return listaFinal;
         }

		 function DialogLancamento($scope, $rootScope, $timeout, $mdDialog, $state, index, conciliacaoLancamento, tipo, notification, AutocompleteService, fn_confirmAddLancamento, codHistorico, HistoricoConciliacaoBancaria) {
			 $scope.title = null;
			 $scope.selectedItemContaContabil = null;

			 if(tipo === 'novo'){
			 	$scope.title = "Criar lançamento";
			 	initNovo();
			 }else{
			 	$scope.title = "Editar lançamento";
			 }

		 	 $scope.object = new Lancamentos();
		 	 $scope.object.numero = " ";
		 	 $scope.object.situacao = "Aberto";
		 	 $scope.object.data = conciliacaoLancamento.lancamento ;
		 	 $scope.object.unidade = conciliacaoLancamento.conciliacaoBancaria.contaCorrente.unidade;
		 	 $scope.object.banco = conciliacaoLancamento.conciliacaoBancaria.contaCorrente.banco;
			 $scope.object.contaCorrente = conciliacaoLancamento.conciliacaoBancaria.contaCorrente;
 			 $scope.object.valor = conciliacaoLancamento.valor;
			 $scope.object.debitoCredito = "";
			 var aux = null;

			 $scope.agenciaConta = criarStringAgenciaConta();
			 function criarStringAgenciaConta() {
			 	var stringAgenciaConta = "";

			 	stringAgenciaConta = stringAgenciaConta.concat($scope.object.contaCorrente.agencia);

			 	if (($scope.object.contaCorrente.digitoAgencia && $scope.object.contaCorrente.digitoAgencia.trim()) || $scope.object.contaCorrente.digitoAgencia === 0) {
			 		stringAgenciaConta = stringAgenciaConta.concat("-");
			 		stringAgenciaConta = stringAgenciaConta.concat($scope.object.contaCorrente.digitoAgencia);
			 	}

			 	stringAgenciaConta = stringAgenciaConta.concat(" / ");
			 	stringAgenciaConta = stringAgenciaConta.concat($scope.object.contaCorrente.conta);

				if (($scope.object.contaCorrente.digito && $scope.object.contaCorrente.digito.trim()) || $scope.object.contaCorrente.digito === 0) {
					stringAgenciaConta = stringAgenciaConta.concat("-");
					stringAgenciaConta = stringAgenciaConta.concat($scope.object.contaCorrente.digito);
				}

				return stringAgenciaConta;
			 }

			 // Para salvar no Banco
 			 if(conciliacaoLancamento.debitoCredito ===  "débito" ){
 			 	$scope.object.debitoCredito = "D";

 			 } else {
 			 	$scope.object.debitoCredito = "C";
 			 }

			 // Para pegar a Conta Contabil
 			 if(conciliacaoLancamento.objLancamento != null){
				aux = conciliacaoLancamento.objLancamento.contaContabil;
				$scope.object.contaContabil = conciliacaoLancamento.objLancamento.contaContabil;

 			 }else{
		 	 	$scope.object.contaContabil = null;
 			 }

			// INICIO DO AUTOCOMPLETE DE CONTA CONTÁBIL
			function autoCompleteContaContabil(){

				$scope.searchTextContaContabil = null;
				$scope.selectedItemContaContabil = aux;
				$scope.resultsContaContabil = [];

				$scope.fnSelectedItemContaContabil = function(item) {
					$scope.object.contaContabil = item;
				};

				$scope.querySearchContaContabil = function(query) {
					$rootScope.loadingProgress = true;

					var filtro = {};
					filtro.unidade = {};
					filtro.unidade.codigo = conciliacaoLancamento.conciliacaoBancaria.contaCorrente.unidade.codigo;
					filtro.nome = query;

					var resultContaContabilPromise = AutocompleteService.getContaContabilPorEmpresa(filtro).$promise;

					var contaContabilArray = [];

					return resultContaContabilPromise.then(function(contaContabil) {

						$rootScope.loadingProgress = false;

						angular.forEach(contaContabil, function(contaContabil) {
							contaContabilArray.push(contaContabil);
						})

						return contaContabilArray;
					});
				}
			} // FIM DO AUTOCOMPLETE DE CONTA CONTÁBIL

			autoCompleteContaContabil();

			// FUNÇÃO PARA UM NOVO LANÇAMENTO. PREENCHE O CAMPO CONTA CONTABIL AO INICIAR O DIALOG.
			function initNovo() {

				if (codHistorico) {

					var contaContabilDoHistorico = null;
					var pHistorico = HistoricoConciliacaoBancaria.buscarcontacontabil(codHistorico).$promise;

					pHistorico.then(function(data) {
						angular.forEach(data, function(value, key) {
							if (!contaContabilDoHistorico && value.empresa.codigo === $scope.object.unidade.codigo) {
								contaContabilDoHistorico = angular.copy(value);
							}
						});

						if (contaContabilDoHistorico) {
							$scope.selectedItemContaContabil = contaContabilDoHistorico;
						}

					}).catch(function(response) {
						$scope.$emit('crud-critical', 'load', response);
					});
				}
			}

			$scope.confirm = function() {

				if ($scope.object.contaContabil != null) {

					fn_confirmAddLancamento($scope.object, index, $scope.hide);

				} else {

					notification.error("Conta contábil é um campo de preenchimento obrigatório.");
				}
			};

			$scope.hide = function() {
            	$mdDialog.hide();
            };

			$scope.cancel = function() {
				$mdDialog.cancel();
			};
		 } // FIM DIALOG LANCAMENTO

		 function adicionarLancamentoExistente(evento, conciliacaoLancamentoTO) {
		 	$mdDialog.show({
				controller: DialogAdicionarLancamento,
				locals: {
					numeroRegistro: conciliacaoLancamentoTO.registro,
					conciliacaoLancamento: conciliacaoLancamentoTO,
					notification: $scope.notification,
					fn_confirmAddLancamento: confirmAddLancamento,
					fn_removerLancamentoPreAssociados: removerLancamentoPreAssociados
				},
				templateUrl: 'partials/tesouraria/importar-conciliacao/dialogs/dialog_adicionar_lancamento.html',
				parent: angular.element(document.body),
				clickOutsideToClose: false,
				fullscreen: false
			});

		 }

		 function DialogAdicionarLancamento($scope, $rootScope, $timeout, $mdDialog, $state, AutocompleteService, numeroRegistro, conciliacaoLancamento, notification, fn_confirmAddLancamento, fn_removerLancamentoPreAssociados) {
			init();

			function init() {
				$scope.title = "Conciliação Bancária - Adicionar lançamento";

				$scope.object = {};
				$scope.object.status = "Aberto";
				$scope.object.unidade = conciliacaoLancamento.conciliacaoBancaria.contaCorrente.unidade;
				$scope.object.valorDe = null;
				$scope.object.valorAte = null;

				$scope.listBancos = [];
				$scope.listAgencias = [];
				$scope.listaLancamentos = [];

				$scope.lancamentoSelecionado = null;
				$scope.pesquisaRealizada = false;

				initCampoBanco();
			}

			//INICIALIAR CAMPOS

			function initCampoBanco() {

				if($scope.object.unidade && $scope.object.unidade.codigo) {
					var objContaCorrente = {};
					var pContaCorrente = ContaCorrente.page(null, { pessoa: {codigo: $scope.object.unidade.codigo} }).$promise;

					pContaCorrente.then(function(data) {
						$scope.listBancos.push({codigo: null, nome : ''});

						angular.forEach(data.content, function(value, key) {
							$scope.listBancos.push(value.banco);
						})

					}).catch(function(response) {
						$scope.$emit('crud-critical', 'load', response);
					});
				}
			}

			function initCampoAgencia(object) {
				$scope.listAgencias = [];

				if($scope.object.unidade && $scope.object.unidade.codigo) {
					var objContaCorrente = {};
					var pContaCorrente = ContaCorrente.page(null, { pessoa: {codigo: $scope.object.unidade.codigo} }).$promise;

					pContaCorrente.then(function(data) {
						$scope.listAgencias.push({codigo: null});

						angular.forEach(data.content, function(value, key) {
							if($scope.object.banco && $scope.object.banco.codigo === value.banco.codigo) {
								$scope.listAgencias.push(value);
							}
						});
					}).catch(function(response) {
						$scope.$emit('crud-critical', 'load', response);
					});
				}
			}

			// ONCHANGES

			$scope.changeBanco = function() {
				if (!$scope.object.banco.codigo) {
					$scope.object.banco = null;
					$scope.object.contaCorrente = null;
					$scope.listAgencias = [];
				} else {
					initCampoAgencia($scope.object);
				}
			};

			//AUTOCOMPLETE

			$scope.querySearchContaContabil = function(query) {
				var listaContaContabil = [];
				var filtro = {};
				filtro.unidade = {};
				filtro.unidade.codigo = $scope.object.unidade.codigo;
				filtro.nome = query;

				var pContaContabil = AutocompleteService.getContaContabilPorEmpresa(filtro).$promise;

				return pContaContabil.then(function(data) {

					angular.forEach(data, function(value, key) {
						listaContaContabil.push(value);
					});

					return listaContaContabil;
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'load', response);
					listaContaContabil = [];
					return listaContaContabil;
				});

			};

			$scope.selectedItemChange = function(item) {
				$scope.object.contaContabil = item;
			};

			// FUNÇÕES AUXILIARES

			$scope.getAgenciaConta = function(contaCorrente) {
				var nomeConta = "";
				nomeConta = nomeConta.concat(contaCorrente.agencia);
				nomeConta = nomeConta.concat(" / ");
				nomeConta = nomeConta.concat(contaCorrente.conta);

				if (contaCorrente.digito && contaCorrente.digito.trim() !== "") {
					nomeConta = nomeConta.concat("-");
					nomeConta = nomeConta.concat(contaCorrente.digito);
				}

				return nomeConta;
			}

			function removerLancamentosAssociados(listaLancamento, listaLancamentoAssociado) {
				var listaFinal = [];

				if (!listaLancamento) {
					return listaFinal;
				}

				if ((listaLancamento && !listaLancamentoAssociado) ||
				   		(listaLancamento && listaLancamentoAssociado && listaLancamentoAssociado.length <= 0)) {

					listaFinal = listaLancamento;
					return listaFinal;
				}

				if (listaLancamento && listaLancamento.length > 0 &&
						listaLancamentoAssociado && listaLancamentoAssociado.length > 0) {

					angular.forEach(listaLancamento, function(value1, key) {
						var isAssociado = false;

						angular.forEach(listaLancamentoAssociado, function(value2, key) {
							if (value1.codigo === value2.codigo) {
								isAssociado = true;
							}
						});

						if (!isAssociado) {
							listaFinal.push(value1);
						}
					});

					return listaFinal;
				}

				return listaFinal;
			}

			//CHECKBOX
			$scope.exists = function (item) {
				var resultado = false;

				if ($scope.lancamentoSelecionado && $scope.lancamentoSelecionado.codigo === item.codigo) {
					resultado = true;
				}

				return resultado;
			};

			$scope.selecionar = function (item) {
				$scope.lancamentoSelecionado = item;
			};

			//BOTÕES

			$scope.hide = function() {
				$mdDialog.hide();
			};
			$scope.cancel = function() {
				$mdDialog.cancel();
			};

			$scope.pesquisar = function() {
				$scope.pesquisaRealizada = false;
				$scope.lancamentoSelecionado = null;
				$scope.listaLancamentos = [];

				var pPesquisa = Lancamentos.page($scope.object).$promise;

				pPesquisa.then(function(data) {
					$scope.pesquisaRealizada = true;

					if (data.content && data.content.length > 0) {
						var pConciliacaoLancamento = ConciliacaoLancamento.getlancamentosassociados().$promise;

						pConciliacaoLancamento.then(function(data2) {
							var listaLancamentoUpdate = removerLancamentosAssociados(data.content, data2);
							$scope.listaLancamentos = fn_removerLancamentoPreAssociados(listaLancamentoUpdate)

						}).catch(function(response) {
							$scope.$emit('crud-critical', 'load', response);
						});
					}

				}).catch(function(response) {
					$scope.$emit('crud-critical', 'load', response);
				});
			};

			$scope.limpar = function() {
				$scope.selectedItemContaContabil = null;
				$scope.searchTextContaContabil = null;

				init();
			};

			$scope.adicionar = function() {
				if ($scope.lancamentoSelecionado) {
					fn_confirmAddLancamento($scope.lancamentoSelecionado, numeroRegistro, $scope.hide);
				}
			};
		 } // FIM DIALOG LANCAMENTO


		 /*
		  RELAÇÃO BANCARIA
		 */

		 function adicionarRelacaoBancaria(evento, conciliacaoLancamentoTO) {
		 	$mdDialog.show({
				controller: DialogControllerAdicionarRelacaoBancaria,
				locals: {
					conciliacaoLancamento: conciliacaoLancamentoTO,
					numeroRegistro: conciliacaoLancamentoTO.registro,
					fn_confirmAddPagamento: confirmAddPagamento,
					fn_removerItemRelacaoPreAssociado: removerItemRelacaoPreAssociado,
					codigoContaCorrenteConciliacao: $scope.codigoContaCorrenteConciliacao
				},
				templateUrl: 'partials/tesouraria/importar-conciliacao/dialogs/dialog_relacao_bancaria.html',
				parent: angular.element(document.body),
				targetEvent: evento,
				clickOutsideToClose: false,
				fullscreen: false
			});
		 }

		 function DialogControllerAdicionarRelacaoBancaria($scope, $mdDialog, $controller, $rootScope, conciliacaoLancamento, numeroRegistro, fn_confirmAddPagamento, fn_removerItemRelacaoPreAssociado, codigoContaCorrenteConciliacao) {
		 	initDialog();

		 	function initDialog() {
		 		$scope.object = {};
		 		$scope.object.empresa = conciliacaoLancamento.conciliacaoBancaria.contaCorrente.unidade;
		 		$scope.object.codigoContaCorrenteConciliacao = codigoContaCorrenteConciliacao;

		 		$scope.isCheque = conciliacaoLancamento.tipoCheque;

				$scope.object.valorDe = conciliacaoLancamento.valor;
				$scope.object.valorAte = conciliacaoLancamento.valor;

		 		$scope.title = "Conciliação Bancária - Adicionar pagamento";
		 		$scope.listaRB = [];
		 		$scope.listaBancos = [];
		 		$scope.listaGerencias = [];
		 		$scope.listTipoDoc = [];

		 		carregarBancos();
		 		carregarGerencias();
		 		carregarTipoDocumento();

		 		$scope.itensSelecionados = [];
		 		$scope.pesquisaRealizada = false;
		 		$scope.totalPagamentosSelecionados = 0;
		 	}

		 	//INICIALIZAR CAMPOS

		 	function carregarBancos() {
				Banco.todos().$promise.then(function(data) {
						$scope.listaBancos = [];

						angular.forEach(data, function(value, key) {
							$scope.listaBancos.push(value);
						})

					}).catch(function(response) {
						$scope.$emit('crud-critical', 'contrato load', response);
						$scope.listaBancos = [];
				});
			};

			function carregarGerencias() {
				$scope.listaGerencias = CentroFinanceiro.buscarTodasGerencias();
			};

			function carregarTipoDocumento() {
				 EnumService.enumValues({ pacote: 'br.com.araujoabreu.sgi.administracao.domain', classe: 'null', enum: 'TipoDocumentoPagamentoPesquisa' })
					 .$promise.then(function(data) {
						 $scope.listTipoDoc = [];
						 $scope.listTipoDoc.push("");
						 $scope.listTipoDoc.push("Boleto");

						 angular.forEach(data, function(value, key) {
						 	if (key > 2) {
								$scope.listTipoDoc.push(value);
						 	}
						 });
					 }).catch(function(response) {
						 $scope.$emit('crud-critical', 'load', response);
					 });
			};


			//FUNÇÕES PARA OS CHECKBOXS

			$scope.selecionar = function (item, list) {
				var idx = list.indexOf(item);
				if (idx > -1) {
				  list.splice(idx, 1);
				}
				else {
				  list.push(item);
				}
				atualizarTotalPagamentoSelecionado();
			};

			$scope.exists = function (item, list) {
				return list.indexOf(item) > -1;
		  	};

		  	$scope.isChecked = function() {
				return $scope.itensSelecionados.length === $scope.listaRB.length;
		  	};

		  	$scope.selecionarTodos = function() {
				if ($scope.itensSelecionados.length === $scope.listaRB.length) {
			  		$scope.itensSelecionados = [];
				} else if ($scope.itensSelecionados.length === 0 || $scope.itensSelecionados.length > 0) {
			 		$scope.itensSelecionados = $scope.listaRB.slice(0);
				}
				atualizarTotalPagamentoSelecionado();
		  	};

		  	$scope.isIndeterminate = function() {
            	return ($scope.itensSelecionados.length !== 0 && $scope.itensSelecionados.length !== $scope.listaRB.length);
            };

            function atualizarTotalPagamentoSelecionado() {
            	$scope.totalPagamentosSelecionados = 0;
            	angular.forEach($scope.itensSelecionados, function(item, key) {
					$scope.totalPagamentosSelecionados += item.total;
            	});
            }

			//FUNÇÕES PARA OS BOTÕES

			$scope.limpar = function() {
				$scope.object = {};
				$scope.object.empresa = conciliacaoLancamento.conciliacaoBancaria.contaCorrente.unidade;

				if ($scope.isCheque) {
					$scope.object.valorDe = conciliacaoLancamento.valor;
					$scope.object.valorAte = conciliacaoLancamento.valor;
				}

				$scope.listaRB = [];
				$scope.itensSelecionados = [];
				$scope.pesquisaRealizada = false;
				$scope.totalPagamentosSelecionados = 0;
			};

			$scope.pesquisar = function() {
				$scope.totalPagamentosSelecionados = 0;
				$scope.pesquisaRealizada = false;
				$scope.listaRB = [];
				$scope.itensSelecionados = [];

				var pRelacaoBancaria = ConciliacaoBancaria.pesquisarelacaobancaria($scope.object).$promise;

				pRelacaoBancaria.then(function(data) {
					$scope.pesquisaRealizada = true;
					$scope.listaRB = fn_removerItemRelacaoPreAssociado(data);
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'load', response);
				});
			};

		 	$scope.hide = function() {
				$mdDialog.hide();
			};
			$scope.cancel = function() {
				$mdDialog.cancel();
			};
			$scope.adicionar = function() {
				fn_confirmAddPagamento($scope.itensSelecionados, numeroRegistro, $scope.totalPagamentosSelecionados);
				$mdDialog.hide();
			};
		 }

		 function confirmAddPagamento(listaPagamento, numeroRegistro, totalPagamentosSelecionados) {
		 	angular.forEach($scope.listaConciliacaoLancamento, function(value, key) {

		 		if (value.registro === numeroRegistro) {
		 			var obj = value;
		 			obj.objPagamento = listaPagamento;
		 			obj.situacao = "Não pendente";
		 			obj.totalPagamentosSelecionados = totalPagamentosSelecionados;
		 			return;
		 		}
			});
		 }

		 function removerRelacaoBancaria(evento, conciliacaoLancamentoTO){
			if(conciliacaoLancamentoTO){
				conciliacaoLancamentoTO.situacao = "Pendente";
				delete conciliacaoLancamentoTO.objPagamento;

				if (conciliacaoLancamentoTO.cheque) {
					delete conciliacaoLancamentoTO.cheque;
				}

				if (conciliacaoLancamentoTO.totalPagamentosSelecionados) {
					delete conciliacaoLancamentoTO.totalPagamentosSelecionados;
				}
			}
		 }

		 function removerItemRelacaoPreAssociado(listaItemRelacao) {
		 	var listaFinal = [];
			var listaIRPreAssociado = [];

			if (listaItemRelacao && listaItemRelacao.length > 0) {

				angular.forEach($scope.listaConciliacaoLancamento, function(conciliacaoLancamento, key1) {

					if (conciliacaoLancamento && conciliacaoLancamento.objPagamento) {

						angular.forEach(conciliacaoLancamento.objPagamento, function(itemRelacao, key2) {

							if (itemRelacao && itemRelacao.codigo) {
								listaIRPreAssociado.push(itemRelacao);
							}
						});
					}
				});

				if (listaIRPreAssociado && listaIRPreAssociado.length > 0) {

					angular.forEach(listaItemRelacao, function(value1, key) {
						var isPreAssociado = false;

						angular.forEach(listaIRPreAssociado, function(value2, key) {
							if (value1.codigo === value2.codigo) {
								isPreAssociado = true;
							}
						});

						if (!isPreAssociado) {
							listaFinal.push(value1);
						}
					});

					return listaFinal;

				} else {
					listaFinal = listaItemRelacao;
					return listaFinal;
				}

			} else {
				listaFinal = listaItemRelacao;
				return listaFinal;
			}

		 	return listaFinal;
		 }


		/*
		* ----------------------------------------
		* FUNÇÕES PARA A NOVA TABELA DE RESULTADO:
		* ----------------------------------------
		*/


		/*
		  Função cria os tipos de Conciliação lançamentos (CL) que serão agrupados na tabela.

		  Vai ser gerado duas listas. Uma lista de débitos ($scope.listaTipoDebito) e outra de créditos ($scope.listaTipoCredito).
		  Cada item dessas listas vai representar um histórico que vai agrupar os CLs.
		  Esses itens serão criados se houver relação entre o nome do histórico do CL e o nome dos itens da tabela 'historico_conciliacao'.

		  Se o nome do histórico do CL não estiver na tabela 'historico_conciliacao' ele será agrupado no histórico 'OUTROS'.
		  Cada item das duas listas terá uma sub-lista contendo os CLs.

		  O Histórico conciliação tem cinco tipos de ação:
		  0 - 'Nenhuma ação'
		  1 - 'Lançamento de tesouraria'
		  2 - 'Pagamento'
		  3 - 'Pagamento (cheque)'
		  4 - 'NF/Saída (faturamento)'
		  5 - 'Outros recebimentos'

		  Se houver CLs com tipo 4 ou 5 na lista de DÉBITOS eles serão agrupados no histórico 'OUTROS'.
		  Se houver CLs com tipo 1, 2, 3 e 4 na lista de CRÉDITOS eles tambem serão agrupados no histórico 'OUTROS'.
		*/
		function criarTipos(listaCL) {

			//pegar históricos das conciliações do bando de dados
			$scope.listaHistorico = [];
			var pHistorico = HistoricoConciliacaoBancaria.page(null, {}).$promise;

			pHistorico.then(function(data) {

				angular.forEach(data.content, function(valueHistorico, keyHistorico) {
					$scope.listaHistorico.push(valueHistorico);
				});

				angular.forEach(listaCL, function(value, key) {
					var tipoValor = value.valor;
					var tipoNome = removerNumeros(value.historico);
					tipoNome = decomporNome(tipoNome, $scope.listaHistorico);

					if (value.debitoCredito == "débito") {

						var inserirClEmOutros = true;

						if ($scope.listaTipoDebito.length <= 0) {

							angular.forEach($scope.listaHistorico, function(historicoValue, historicoKey) {

								var numTipoAcao = getNumTipo(historicoValue.tipoAcao);

								if (nomesIguais(historicoValue.descricao, tipoNome) && (numTipoAcao === 0 || numTipoAcao === 1 || numTipoAcao === 2 || numTipoAcao === 3)) {
									adicionarItemNaLista($scope.listaTipoDebito, value, tipoNome, tipoValor, historicoValue.tipoAcao, historicoValue.codigo);
									inserirClEmOutros = false;
								}
							});

							if ($scope.listaTipoDebito.length <= 0) {
								adicionarItemNaLista($scope.listaTipoDebito, value, "OUTROS", tipoValor, "Nenhuma ação", null);
								inserirClEmOutros = false;
							}

						} else {

							var temTipo = false;
							var indexTipo = null;

							angular.forEach($scope.listaTipoDebito, function(valueTipo, keyTipo) {
								if (nomesIguais(valueTipo.nome, tipoNome)) {
									temTipo = true;
									indexTipo = keyTipo;
								}
							});

							if (temTipo) {
								$scope.listaTipoDebito[indexTipo].qtd++;
								$scope.listaTipoDebito[indexTipo].valor += tipoValor;
								$scope.listaTipoDebito[indexTipo].listaConciliacaoLancamento.push(value);
								inserirClEmOutros = false;

							} else {
								angular.forEach($scope.listaHistorico, function(historicoValue, historicoKey) {

									var numTipoAcao = getNumTipo(historicoValue.tipoAcao);

									if (nomesIguais(historicoValue.descricao, tipoNome) && (numTipoAcao === 0 || numTipoAcao === 1 || numTipoAcao === 2 || numTipoAcao === 3)) {
										adicionarItemNaLista($scope.listaTipoDebito, value, tipoNome, tipoValor, historicoValue.tipoAcao, historicoValue.codigo);
										inserirClEmOutros = false;
									}
								});
							}
						}

						if (inserirClEmOutros) {
							adicionarItemOutros($scope.listaTipoDebito, value, tipoValor);
						}

					} else {

						var inserirClEmOutros = true;

						if ($scope.listaTipoCredito.length <= 0) {

							angular.forEach($scope.listaHistorico, function(historicoValue, historicoKey) {

								var numTipoAcao = getNumTipo(historicoValue.tipoAcao);

								if (nomesIguais(historicoValue.descricao, tipoNome) && (numTipoAcao === 0 || numTipoAcao === 5)) {
									adicionarItemNaLista($scope.listaTipoCredito, value, tipoNome, tipoValor, historicoValue.tipoAcao, historicoValue.codigo);
									inserirClEmOutros = false;
								}
							});

							if ($scope.listaTipoCredito.length <= 0) {
								adicionarItemNaLista($scope.listaTipoCredito, value, "OUTROS", tipoValor, "Nenhuma ação", null);
								inserirClEmOutros = false;
							}

						} else {

							var temTipo = false;
							var indexTipo = null;

							angular.forEach($scope.listaTipoCredito, function(valueTipo, keyTipo) {
								if (nomesIguais(valueTipo.nome, tipoNome)) {
									temTipo = true;
									indexTipo = keyTipo;
								}
							});

							if (temTipo) {
								$scope.listaTipoCredito[indexTipo].qtd++;
								$scope.listaTipoCredito[indexTipo].valor += tipoValor;
								$scope.listaTipoCredito[indexTipo].listaConciliacaoLancamento.push(value);
								inserirClEmOutros = false;

							} else {
								angular.forEach($scope.listaHistorico, function(historicoValue, historicoKey) {

									var numTipoAcao = getNumTipo(historicoValue.tipoAcao);

									if (nomesIguais(historicoValue.descricao, tipoNome) && (numTipoAcao === 0 || numTipoAcao === 5)) {
										adicionarItemNaLista($scope.listaTipoCredito, value, tipoNome, tipoValor, historicoValue.tipoAcao, historicoValue.codigo);
										inserirClEmOutros = false;
									}
								});
							}
						}

						if (inserirClEmOutros) {
							adicionarItemOutros($scope.listaTipoCredito, value, tipoValor);
						}
					}
				});

				reorganizarLista($scope.listaTipoDebito);
				reorganizarLista($scope.listaTipoCredito);

			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});

		}

		/**
		* Remove os números e todos os caracteres que estão a sua direita
		*/
		function removerNumeros(nome) {
			var novoNome = "";

			if (nome) {

				var indexPrimeiroNumero = null;
				var temNumero = false;

				for(var i = 0; i < nome.length; i++) {

					var letra = nome.charAt(i);

					if (letra == "0" || letra == "1" || letra == "2" ||
						letra == "3" || letra == "4" || letra == "5" ||
						letra == "6" || letra == "7" || letra == "8" || letra == "9") {

						temNumero = true;
						indexPrimeiroNumero = i;
						break;
					}
				}

				if (temNumero) {
					novoNome = nome.substring(0, indexPrimeiroNumero);
				} else {
					novoNome = nome;
				}

			}

			return novoNome;
		}

		//Se uma lista tiver tipo "OUTROS" a função coloca esse item no final da lista.
		function reorganizarLista(lista) {
			var temOutros = false;
			var indexTipo = null;

			angular.forEach(lista, function(valueTipo, keyTipo) {
				if (valueTipo.nome === "OUTROS") {
					temOutros = true;
					indexTipo = keyTipo;
				}
			});

			if (temOutros) {
				var itemOutros = angular.copy(lista[indexTipo]);
				lista.splice(indexTipo, 1);
				lista.push(itemOutros);
			}
		}

		//Adiciona um novo tipo histórico a lista.
		function adicionarItemNaLista(lista, conciliacaoLancamento, tipoNome, tipoValor, tipoAcao, codHistorico) {
			var tipoItem = { nome: tipoNome, qtd: 1, valor: tipoValor, expanded: false, listaConciliacaoLancamento: [conciliacaoLancamento], tipoAcao: tipoAcao, codHistorico: codHistorico };
			lista.push(tipoItem);
		}

		//Verifica e adicionar o histórico "OUTROS" na lista
		function adicionarItemOutros(lista, conciliacaoLancamento, tipoValor) {
			var temTipoOutros = false;
			var indexTipo = null;

			angular.forEach(lista, function(valueTipo, keyTipo) {
				if (nomesIguais(valueTipo.nome, "OUTROS")) {
					temTipoOutros = true;
					indexTipo = keyTipo;
				}
			});

			if (temTipoOutros) {
				lista[indexTipo].qtd++;
				lista[indexTipo].valor += tipoValor;
				lista[indexTipo].listaConciliacaoLancamento.push(conciliacaoLancamento);
			} else {
				adicionarItemNaLista(lista, conciliacaoLancamento, "OUTROS", tipoValor, "Nenhuma ação");
			}
		}

		//Remover Acentos
		function removerAcentos( newStringComAcento ) {
          var string = newStringComAcento;
        	var mapaAcentosHex 	= {
        		a : /[\xE0-\xE6]/g,
        		e : /[\xE8-\xEB]/g,
        		i : /[\xEC-\xEF]/g,
        		o : /[\xF2-\xF6]/g,
        		u : /[\xF9-\xFC]/g,
        		c : /\xE7/g,
        		n : /\xF1/g
        	};

        	for ( var letra in mapaAcentosHex ) {
        		var expressaoRegular = mapaAcentosHex[letra];
        		string = string.replace( expressaoRegular, letra );
        	}

        	return string;
        }

		//Verifica se duas strings são iguais, removendo acentos e espaços em branco.
        function nomesIguais(nome1, nome2) {
        	if (removerAcentos(nome1.trim().toLowerCase()) === removerAcentos(nome2.trim().toLowerCase())) {
        		return true;
        	} else {
        		return false;
        	}
        }

		/*
		*  Essa função verifica se os nomes da lista do histórico (tabela historico_conciliacao) estão nos registros do arquivo. Comparando os nomes do historico com o nome de um registro.
		*  Se os nomes forem diferentes vai ser decomposto o nome do registro do arquivo para achar um nome similar com o nome do histórico.
		*
		*  Por exemplo: "SISPAG FORNECEDORES TED"
		*  Esse nome vai ser decomposto da seguinte forma: "SISPAG FORNECEDORES TED" | "SISPAG FORNECEDORES" | "SISPAG"
		*
		*  Se achar um nome similar será retornado o nome decomposto. Se não, será retornado o mesmo valor do parametro "nomeDoArquivo".
		*/
        function decomporNome(nomeDoArquivo, listaHistorico) {

        	var novoTipoNome = nomeDoArquivo;
        	var temNomeIgual = false;

        	angular.forEach(listaHistorico, function(historicoValue, historicoKey) {
				if (nomesIguais(historicoValue.descricao, nomeDoArquivo)) {
					temNomeIgual = true;
					novoTipoNome = nomeDoArquivo;
				}
			});

			if (!temNomeIgual) {
				var arrayNome = nomeDoArquivo.trim().split(" ");
				var nomeDecomposto = "";
				var nomeIgualDecomposto = false;

				for (var j = 1; j <= arrayNome.length; j++) { //for 1

					for (var i = 0; i <= (arrayNome.length - j); i++) { //for 2
						nomeDecomposto = nomeDecomposto.concat(arrayNome[i]);
						nomeDecomposto = nomeDecomposto.concat(" ");
					} //for 2

					nomeDecomposto = nomeDecomposto.trim();

					angular.forEach(listaHistorico, function(historicoValue, historicoKey) {
						if (nomesIguais(historicoValue.descricao, nomeDecomposto)) {
							nomeIgualDecomposto = true;
							novoTipoNome = nomeDecomposto;
						}
					});

					if (nomeIgualDecomposto) {
						break;
					} else {
						nomeDecomposto = "";
					}

				} //for 1
			}

			return novoTipoNome;
        }

		// Retorna o numero do Enum do HistoricoConciliacao.TipoAcao
		// do nome passado pelo parametro.
        function getNumTipo(nome) {

        	var numero = -1;

        	switch(nome) {
        		case "Nenhuma ação":
        			numero = 0;
        			break;
        		case "Lançamento de tesouraria":
					numero = 1;
					break;
				case "Pagamento":
					numero = 2;
					break;
				case "Pagamento (cheque)":
					numero = 3;
					break;
				case "NF/Saída (faturamento)":
					numero = 4;
					break;
				case "Outros recebimentos":
					numero = 5;
					break;
        	}

        	return numero;
        }


		/*
		* ------------------------------------------
		* FUNÇÃO PARA VERIFICAR CHEQUES ASSOCIADOS
		* ------------------------------------------
		*/

		function verificarCheques(listaCL) {

			angular.forEach(listaCL, function(value, key) {

				var historico = value.historico;
				var pagCheque = "PAGAMENTO CHEQUE";
				var chCom = "CH COMPENSADO";

				if (historico.includes(pagCheque) || historico.includes(chCom)) {
					var numeroCheque = "";

					if (historico.includes(pagCheque)) {
						numeroCheque = historico.replace(pagCheque, "").trim();
					}

					if (historico.includes(chCom)) {
						numeroCheque = historico.replace(chCom, "").trim();
					}

					value.tipoCheque = true;

					var pChequeAssociado = ConciliacaoLancamento.getchequeassociado(numeroCheque).$promise;
					pChequeAssociado.then(function(data){

						//O Cheque só é associado se tiver o número do pagamento.
						if(data && data.codigo && data.relacaoBancariaPagamentosVO && data.relacaoBancariaPagamentosVO.numero) {
							value.cheque = data
							value.objPagamento = [];
							value.objPagamento.push(data.relacaoBancariaPagamentosVO);
						}
					}).catch(function(response){
						$scope.$emit('crud-critical', 'load', response);
					});
				}
			});
		}

	};
})();
