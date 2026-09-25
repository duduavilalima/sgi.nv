(function(){

	'use strict'

	angular
		.module('app.conta_corrente')
		.controller('ContaCorrenteDetailCtrl', detailController);

function detailController($scope, $state, $mdDialog, $rootScope, $timeout, $window, Unidade, ContaCorrente, EnumService, AutocompleteService, $stateParams, DialogConfirm, MotivoRetornoDialog, MotivoRetornoChequeContaCorrente, $q, PessoaJuridica, DadosBancarios, Banco) {

    function init() {
        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;

		$scope.action = false;

        $scope.object = null;
        $scope.filtro = {};
        $scope.filtroSaldo = {};
        $scope.filtroSaldo.contaCorrente = {};
        $scope.filtroSaldo.data = new Date();
        $scope.filtroSaldo.tipoDebitosCreditos = null;

        $scope.saldos = {};
        $scope.saldos.listaDeLancamentos = null;

        $scope.listaTipoDebito = [];
        $scope.listaTipoCredito = [];

        if ($scope.screenMode === 'NEW') {
            newObj();
        } else {
            loadObj();
        }
    }

	$scope.$on('changeEmpresa', function() {
		if ($scope.object.pessoa.codigo === null) {
			delete $scope.object.pessoa;
			$scope.bancoList = [];
		} else {
			carregarBancos($scope.object);
		}
		delete $scope.object.banco;
		$scope.object.contaCorrente = null;
		$scope.listAgencias = [];
	});

    function newObj() {
        $scope.object = new ContaCorrente();

        ContaCorrente.get().$promise
            .then(function(data, status, headers, config) {
                carregaTiposContasCorrentes();
                buscarEmpresas();
                $scope.object.homologado = false;
				$scope.object.principal = false;
                carregarBancos($scope.object);
            }, function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });
    };

    function loadObj() {
        $rootScope.loadingProgress = true;
        $scope.action = true;

        ContaCorrente.get({id: $stateParams.objectId }).$promise.then(function(data) {
            $scope.object = angular.copy(data);

            if ($scope.object.digitoAgencia) {
            	$scope.object.digitoAgencia = $scope.object.digitoAgencia.trim();
            }

            if($scope.screenMode === 'EDIT') {
                $scope.object.pessoa = data.unidade;
				$scope.selectedItemContaContabil = data.contaContabil;

				const pBuscarEmpresas = () => buscarEmpresas();
				const pCarregarBancos = () => carregarBancos($scope.object);

				carregaTiposContasCorrentes()
				.then(pBuscarEmpresas)
				.then(pCarregarBancos)
				.then(() => {
					$scope.object.banco = data.banco;
				})
				.catch(function(response2) {
					$scope.$emit('crud-critical', 'load', response2);
					$rootScope.loadingProgress = false;
					$scope.action = false;
				});

            } else {
                $scope.object.pessoa = data.unidade;
                $scope.selectedItemContaContabil = data.contaContabil;

                $scope.desabilitarBotaoCancelar = true;

                //removido no issue SGINV-551
//                $scope.listaCombo = data.listaFaixaDeCheques;
//                $scope.filtro.faixaCheques = $scope.listaCombo[0];

				$scope.listaFiltrada = [];
                carregarCheques();

                carregarSaldos().then(function(data) {
					delete $scope.filtroSaldo.data;
				}).catch(function(response) {
					$scope.saldos.listaDeLancamentos = [];
					$scope.$emit('crud-critical', 'cheque', response);
				});
            }

            $rootScope.loadingProgress = false;
            $scope.action = false;
        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
            $rootScope.loadingProgress = false;
            $scope.action = false;
        });
    };

	//Função também serve para o botão retornar
    $scope.cancelar = function(){
        if($scope.screenMode !== 'EDIT'){
            $state.go('conta_corrente.list');
        } else {
            $state.go('conta_corrente.view', { objectId: $scope.object.codigo});
        }
    };

    $scope.editar = function(){
        $state.go('conta_corrente.edit', { objectId: $scope.object.codigo});
    };


    function buscarEmpresas() {
		return new Promise((resolve, reject) => {
			let filter = { tipoUnidade: 'MATRIZ' };
			Unidade.page(null, filter).$promise.then(function(data) {
				$scope.listEmpresas = [];
				angular.forEach(data.content, function(value, key) {
					$scope.listEmpresas.push(value);
				});
				resolve(data);
			}).catch(function(response) {
				console.error(response);
				$scope.$emit('crud-critical', 'search', response);
				reject(response);
			});

		});
    }

    function carregaTiposContasCorrentes(){
		return new Promise ((resolve, reject) => {
			EnumService.enumValues({
				pacote: 'br.com.araujoabreu.sgi.tesouraria.domain',
				classe: 'ContaCorrente',
				enum: 'TipoContaCorrente'
			})
			.$promise.then(function(data) {
				$scope.tipoContasList = [];
				angular.forEach(data, function(value, key) {
					$scope.tipoContasList.push(value);
				});
				resolve(data);
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
				reject(response);
			});
		});
    }

    // INICIO DO AUTOCOMPLETE DE CONTA CONTÁBIL

    $scope.searchTextContaContabil = null;
    $scope.selectedItemContaContabil = null;
    $scope.resultsContaContabil = [];

    $scope.fnSelectedItemContaContabil = function(item) {
        $scope.object.contaContabil = item;
    };

    $scope.querySearchContaContabil = function(query) {
        $rootScope.loadingProgress = true;

        var filtro = {};
        filtro.unidade = {};
        filtro.unidade.codigo = $scope.object.pessoa.codigo;
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
    };


    function carregarBancos(object) {
		return new Promise((resolve, reject) => {
			$scope.bancoList = [];

			if (object.pessoa && object.pessoa.codigo !== undefined) {
					var pContaCorrente = Banco.todosordenado().$promise;

					pContaCorrente.then(function(data) {
						let mapBanco = new Map();

						angular.forEach(data, function(value, key) {
							mapBanco.set(value.codigo, value);
						});

						$scope.bancoList = [];

						angular.forEach(mapBanco, function(value, key) {
							$scope.bancoList.push(value);
						});

						resolve(data);

					}).catch(function(response) {
						$scope.$emit('crud-critical', 'contrato load', response);
						$scope.bancoList = [];
						$scope.object.agencia = "";
						$scope.object.digitoAgencia = "";
						$scope.object.conta = "";
						$scope.object.digito = "";
						reject(response);
				});
			}
		});
	}


    function carregarCheques() {
		ContaCorrente.buscarChequesContaCorrente({id: $scope.objectId}).$promise.then(function(data) {
				$scope.listaOriginal = angular.copy(data);
//				$scope.listaFiltrada = angular.copy(data);
//				$scope.filtrar();
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'cheque', response);
		});
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

    $scope.filtrarSaldos = function() {
        $scope.saldos.listaDeLancamentos = null;
        carregarSaldos();
    }

    $scope.limparPesquisaSaldos = function() {
        delete $scope.filtroSaldo.data;
        delete $scope.filtroSaldo.tipoDebitosCreditos;
    }

	function carregarSaldos() {
		return $q(function(resolve, reject){
			if ($scope.filtroSaldo.data == null || $scope.filtroSaldo.data == '') {
				$scope.filtroSaldo.data = new Date();
			}
			$scope.filtroSaldo.contaCorrente.codigo = $scope.object.codigo;

			ContaCorrente.obterSaldos(null, $scope.filtroSaldo).$promise.then(function(data) {
					$scope.saldos = data;
					resolve({mensagem: 'Saldos carregados com sucesso.'});
					criarTipos($scope.saldos.listaDeLancamentos);
				}).catch(function(response) {
					$scope.saldos.listaDeLancamentos = [];
					$scope.$emit('crud-critical', 'cheque', response);
					reject({mensagem: 'Não foi possível carrega os saldos!'});
				});

		})
    };


	$scope.filtrar = function() {

		if ( (!$scope.filtro.faixaDe && $scope.filtro.faixaDe !== 0 ) || (!$scope.filtro.faixaAte && $scope.filtro.faixaAte !== 0 )) {
			if (!$scope.filtro.faixaDe && $scope.filtro.faixaDe !== 0) {
				$scope.notification.error("","\"Faixa de cheques até\" é um campo obrigatório.");
			}
			if (!$scope.filtro.faixaAte && $scope.filtro.faixaAte !== 0) {
				$scope.notification.error("","\"Faixa de cheques de\" é um campo obrigatório.");
			}
			$scope.chequeAction = false;
			return;
		}

		if (($scope.filtro.faixaDe || $scope.filtro.faixaDe === 0) && ($scope.filtro.faixaAte || $scope.filtro.faixaAte === 0)) {
			if ($scope.filtro.faixaDe > $scope.filtro.faixaAte) {
				$scope.notification.error("","O valor informado no campo \"Faixa de cheques de\" deve ser menor que o valor do campo \"Faixa de cheques até\"");
				$scope.chequeAction = false;
				return;
			}

			if ($scope.filtro.faixaAte - $scope.filtro.faixaDe > 200) {
				$scope.notification.error("","O limite da faixa de cheques deve ser de no máximo 200 posições");
				$scope.chequeAction = false;
				return;
			}
		}

		if (($scope.filtro.faixaDe || $scope.filtro.faixaDe === 0) && $scope.filtro.faixaDe < $scope.object.chequeInicial) {
			$scope.notification.error("","O valor informado no campo \"Faixa de cheques de\" deve ser maior ou igual ao valor do campo \"Cheque inicial\"");
			$scope.chequeAction = false;
			return;
		}

		if (($scope.filtro.faixaAte || $scope.filtro.faixaAte === 0) && $scope.filtro.faixaAte > $scope.object.chequeAnterior) {
			$scope.notification.error("","O valor informado no campo \"Faixa de cheques até\" deve ser menor ou igual ao valor do campo \"Último cheque\"");
			$scope.chequeAction = false;
			return;
		}

	    $scope.chequeAction = true;
	    $scope.listaFiltrada = null;

	    $timeout(function() {
	    		$scope.listaFiltrada = [];

        		var arrayStage0 = [];

        		if( $scope.filtro.faixaDe || $scope.filtro.faixaAte ){
        			angular.forEach($scope.listaOriginal, function(value, key){
        				if ( ($scope.filtro.faixaDe && $scope.filtro.faixaAte) && (value.chequeItem >= $scope.filtro.faixaDe && value.chequeItem <= $scope.filtro.faixaAte) ) {
        					arrayStage0.push(value);
        				}
        			});
        		} else {
        			arrayStage0 = angular.copy($scope.listaOriginal);
        		}

        		var arrayStage01 = [];

        		if(angular.isDefined($scope.filtro.compensados) && $scope.filtro.compensados !== null){

        			angular.forEach(arrayStage0, function(value, key){
        				if($scope.filtro.compensados === true){
        					if(value.dataCompensacao !== null && value.dataCompensacao !== "" && value.dataCompensacao !== "—"){
        						arrayStage01.push(value);
        					}
        				} else if($scope.filtro.compensados === false){
        					if(value.dataCompensacao === null || value.dataCompensacao === "" || value.dataCompensacao === "—"){
        						arrayStage01.push(value);
        					}
        				} else if($scope.filtro.compensados === null || $scope.filtro.compensados === "" || angular.isUndefined($scope.filtro.compensados)){
        					arrayStage01 = angular.copy($scope.listaOriginal);
        				}
        			});
        		} else {
        		    arrayStage01 = angular.copy(arrayStage0);
        		}

        		var arrayStage02 = [];

        	 	if((angular.isDefined($scope.filtro.emissaoDe) && $scope.filtro.emissaoDe !== null && $scope.filtro.emissaoDe !== '') ||
        	 	   (angular.isDefined($scope.filtro.emissaoAte) && $scope.filtro.emissaoAte !== null && $scope.filtro.emissaoAte !== '')){

        			angular.forEach(arrayStage01, function(value, key){
        				if (angular.isDefined($scope.filtro.emissaoDe) && $scope.filtro.emissaoDe != null && $scope.filtro.emissaoDe !== ''
        				&&  angular.isDefined($scope.filtro.emissaoAte) && $scope.filtro.emissaoAte != null && $scope.filtro.emissaoAte !== '') {
        				   if(moment(value.dataPagamento).isSameOrAfter(new Date($scope.filtro.emissaoDe)) && moment(value.dataPagamento).isSameOrBefore(new Date($scope.filtro.emissaoAte))){
        						arrayStage02.push(value);
        					}
        				} else {
        					 if(moment(value.dataPagamento).isSameOrAfter(new Date($scope.filtro.emissaoDe))){
        						arrayStage02.push(value);
        					} else if(moment(value.dataPagamento).isSameOrBefore(new Date($scope.filtro.emissaoAte))){
        						arrayStage02.push(value);
        					}
        				}
        			});
        		} else {
        		    arrayStage02 = angular.copy(arrayStage01);
        		}

				if (!$scope.filtro.emissaoDe && !$scope.filtro.emissaoAte && ($scope.filtro.compensados !== true && $scope.filtro.compensados !== false)) {
//				console.log('entrou');
				    insereChequesEmbranco(arrayStage02);
				}

				angular.forEach(arrayStage02, function(value, key) {
					$scope.listaFiltrada.push(angular.copy(value));
			  	});

                $scope.changeSelecaoCheque();

				$scope.chequeAction = false;
	    }, 500);
	};

	$scope.exibirDetalheContas = function(item, ev) {
		var useFullScreen = true;

		var codigoConta = null;

		if (item.banco != null && item.banco.codigo != null) {
			codigoConta = item.banco.codigo;
		} else if (item.contaCorrente != null && item.contaCorrente.banco != null && item.contaCorrente.banco.codigo != null) {
			codigoConta = item.contaCorrente.banco.codigo;
		}

		if (codigoConta != null) {
//		console.log(item);
			$scope.listaDadosBancarios = DadosBancarios.popup({idPessoa: item.credor.codigo});

			$mdDialog.show({
				controller: DialogControllerContas,
				locals: {listaDadosBancarios: $scope.listaDadosBancarios, contaSelecionada: codigoConta, statusRB:'Finalizada'},
				templateUrl: 'js/util/templates/detalhecontas.html',
				parent: angular.element(document.body),
				targetEvent: ev,
				clickOutsideToClose:true,
				fullscreen: useFullScreen,
				arialabel: 'Detalhe pessoa'
			});
		}
	};

	function DialogControllerContas($scope, $mdDialog, listaDadosBancarios, contaSelecionada, statusRB) {
	//criada esta variavel abaixo para exibir somente o dado bancario que está homologado, se e somente se a RB = Finalizada.
		$scope.exibeSimples = false;
		listaDadosBancarios.$promise.then(function(data) {
			$scope.listaDadosBancarios = data;

			angular.forEach(data, function(value) {
				if (value.banco.codigo === contaSelecionada) {
					$scope.contaSelecionada = value.codigo;
				}
			});
		});

		//criada esta verificação para caso a RB esteja Finalizada, exibe os dados bancários somente da conta homologada, uma tabela mais simples. (variavel "exibeSimples")
		if (statusRB === 'Finalizada') {
			$scope.exibeSimples = true;
		}

		$scope.fechar = function() {
			$mdDialog.cancel();
		};
	};

	$scope.criarCheque = function(value, numeroCheque){
		var chequeTemporario = {};

		chequeTemporario.banco = '';
		chequeTemporario.chequeItem = numeroCheque;
		chequeTemporario.codigo = null;
		chequeTemporario.credor = {};
		chequeTemporario.credor.codigo = null;
		chequeTemporario.credor.codigoEndereco = null;
		chequeTemporario.credor.documento = '';
		chequeTemporario.credor.inscricaoEstadual = '';
		chequeTemporario.credor.inscricaoMunicipal = '';
		chequeTemporario.credor.nome = '';
		chequeTemporario.credor.situacao = '';
		chequeTemporario.credor.tipoPessoa = '';
		chequeTemporario.credor.ufInscricaoEstadual = null;
		chequeTemporario.criacao = null;
		chequeTemporario.dataCompensacao = null;
		chequeTemporario.dataPagamento = null;
		chequeTemporario.docPagamento = '';
		chequeTemporario.numero = '';
		chequeTemporario.qtdPagamentos = null;
		chequeTemporario.status = '';
		chequeTemporario.total = null;

		return chequeTemporario;
	};

	function temNaListaDeItens(lista, cheque) {
	    var tem = false;
	    angular.forEach(lista, function(value, key) {
	    	if (value.chequeItem != null && value.chequeItem == cheque) {
	    	    tem = true;
	    	    return;
	    	}
	    });
	    return tem;
	}

	function insereChequesEmbranco(arrayStage02) {
		var listaTemporaria = angular.copy(arrayStage02);

		var conjCheque = new Set();
		angular.forEach(arrayStage02, function(value, key) {
			if (value.chequeItem != null) {
				conjCheque.add(value.chequeItem);
			}
		});

		if (($scope.filtro.faixaDe || $scope.filtro.faixaDe === 0) && ($scope.filtro.faixaAte || $scope.filtro.faixaAte === 0)) {

			for (var i = $scope.filtro.faixaDe; i <= $scope.filtro.faixaAte; i++) {

				if (!conjCheque.has(i)) {
					var cheque = $scope.criarCheque({}, i);
					arrayStage02.push(cheque);
					conjCheque.add(i);
				}
			}
		}
	}


	$scope.limparPesquisa = function(){
	 	$scope.filtro = {};
	 	$scope.listaFiltrada = [];
//		$scope.listaFiltrada = angular.copy($scope.listaOriginal);
	};

    $scope.salvarContaCorrente = function(){
		$scope.action = true;
        if($scope.screenMode === 'NEW') {
            $scope.object.$save().then(function(data){
                    $scope.notification.success("Novo", "Conta corrente inserida com sucesso.");
                    $state.go('conta_corrente.view', {objectId: data.codigo});
                    $scope.action = false;
                }).catch(function(response){
                    $scope.$emit('crud-critical', 'save', response);
                    $scope.action = false;
                });
        } else if ($scope.screenMode === 'EDIT') {
            $scope.object.$update({id: $scope.objectId}).then(function(data) {
                $scope.notification.success("Edição", "Conta corrente atualizada com sucesso.");
                $state.go('conta_corrente.view', {objectId: data.codigo});
                $scope.action = false;
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'save', response);
                $scope.action = false;
            });
        }
    };

    $scope.remover = function(item) {
        $scope.showConfirm(item);
    };

    $scope.remover = function(event) {
        // Chamada do serviço que criará um dialog de confirmação.
        DialogConfirm.show(
            event,
            $scope.object,
            'Remover conta corrente: Confirmação',
            'Deseja realmente remover esta conta corrente (esta operação não poderá ser desfeita)?',
            'Confirmar',
            'Cancelar',
            confirmRemover,
            cancelRemover);
    };

    // Função adicionada na chamada do dialog para confirmação.
    function confirmRemover(object, fnHide) {
        object.$delete({id: object.codigo}).then(function(data){
            // Esta função veio do dialog.
            fnHide();
            $state.go('conta_corrente.list');
            $scope.notification.success("Conta corrente removida com sucesso.");
        }).catch(function(response){
            fnHide();
            console.error('crud-critical', 'dialog', response);
            $scope.$emit('crud-critical', 'dialog', response);
        });
    };

    // Função adicionada na chamada do dialog para cancelamento do mesmo.
    function cancelRemover(fnHide) {
        // Esta função veio do dialog.
        // Aqui você pode fazer qualquer ação ao cancelar um dialog de confirmação.
        fnHide();
    };

    $scope.changeSelecaoCheque = function() {
		$scope.listaChequeParaCancelar = [];

		angular.forEach($scope.listaFiltrada, function(value, key) {
			 if (value.cancelar === true) {
				 $scope.listaFiltrada.indexOf(value);
				 $scope.listaChequeParaCancelar.push(value);
			 }

			 if($scope.listaChequeParaCancelar.length > 0){
				$scope.desabilitarBotaoCancelar = false;
			  } else {
				$scope.desabilitarBotaoCancelar = true;
			  }
		  });


	};

	$scope.cancelarCheque = function(event) {
	    $scope.action = true;
		var chaveMotivo = 'cancelar-cheque';
		var titulo = 'Motivos de cancelamento - Conta Corrente';

		var list = $scope.listaChequeParaCancelar;
		// Função que será executada no confirmar no dialog de motivo de retorno.
		$scope.confirmarMotivo = function(motivoRetornoDocumento, fn_Hide) {
			$scope.action = true;
			angular.forEach(list, function(value, key){
				$scope.motivoRetornoDocumentoChequeContaCorrente = {};
				$scope.motivoRetornoDocumentoChequeContaCorrente.chequeContaCorrenteTO = {
					codigo: value.codigo,
					cheque: value.chequeItem,
					codigoContaCorrente: $scope.object.codigo
				};

				$scope.motivoRetornoDocumentoChequeContaCorrente.motivoRetornoDocumento = motivoRetornoDocumento;

				$rootScope.loadingProgress = true;

				MotivoRetornoChequeContaCorrente.save($scope.motivoRetornoDocumentoChequeContaCorrente).$promise.then(function(data) {
						$rootScope.loadingProgress = false;
						loadObj();
						$scope.notification.success("Cancelado", "Cheque cancelado com sucesso.");
						fn_Hide();
						$scope.action = false;
					}).catch(function(response) {
						console.error('crud-critical', 'dialog', response);
						$rootScope.loadingProgress = false;
						$scope.notification.criticalError(response.status, response.statusText, response.data);
						$scope.action = false;
					});
			});
		};

		// Função que será executada no cancelar no dialog de motivo de retorno.
		$scope.cancelarMotivo = function(fn_Hide) {
			$scope.action = false;
			fn_Hide();
		};

		// Passar para o serviço, as funções que serão chamadas ao confirmar ou cancelar o motivo de retorno.
		MotivoRetornoDialog.show(event, chaveMotivo, titulo, $scope.confirmarMotivo, $scope.cancelarMotivo);
	};

	$scope.imprimirConciliacaoBancaria = function(ev) {
		if ($scope.saldos && $scope.saldos.codigo) {

			//var "producao" = false para testes e desenv. Em produçao, nao esquecer de setar para "true"
			var producao = true;
			var url = "../../sgi/relatorio/frameset?__locale=pt_BR&__format=pdf&__showtitle=false&__report=report/sgi/tesouraria/";

			if (producao == false) {
				url = 'http://localhost' + ":8081/sgi/relatorio/frameset?__locale=pt_BR&__format=pdf&__showtitle=false&__report=report/sgi/tesouraria/";
			}

			url = url.concat("conciliacao_bancaria.rptdesign");
			url = url.concat("&codigoConciliacaoBancaria=" + $scope.saldos.codigo);

			$window.open(url);
		}
	};


	/*
	* ----------------------------------------
	* FUNÇÕES PARA A NOVA TABELA DE RESULTADO:
	* ----------------------------------------
	*/

	/**
	* Função cria os tipos de Conciliação lançamentos que serão agrupados na tabela.
	*/
	function criarTipos(listaCL) {

		$scope.listaTipoDebito = [];
		$scope.listaTipoCredito = [];

		angular.forEach(listaCL, function(value, key) {
			var tipoValor = value.valor;
			var tipoNome = removerNumeros(value.historico);

			if (value.debitoCredito == "débito") {

				if ($scope.listaTipoDebito.length <= 0) {
					var tipoItem = { nome: tipoNome, qtd: 1, valor: tipoValor, expanded: false, listaConciliacaoLancamento: [value] };
					$scope.listaTipoDebito.push(tipoItem);

				} else {

					var temTipo = false;
					var indexTipo = null;

					angular.forEach($scope.listaTipoDebito, function(valueTipo, keyTipo) {
						if (valueTipo.nome === tipoNome ) {
							temTipo = true;
							indexTipo = keyTipo;
						}
					});

					if (temTipo) {
						$scope.listaTipoDebito[indexTipo].qtd++;
						$scope.listaTipoDebito[indexTipo].valor += tipoValor;
						$scope.listaTipoDebito[indexTipo].listaConciliacaoLancamento.push(value);

					} else {
						var tipoItem = { nome: tipoNome, qtd: 1, valor: tipoValor, expanded: false, listaConciliacaoLancamento: [value] };
						$scope.listaTipoDebito.push(tipoItem);
					}
				}

			} else {

				if ($scope.listaTipoCredito.length <= 0) {
					var tipoItem = { nome: tipoNome, qtd: 1, valor: tipoValor, expanded: false, listaConciliacaoLancamento: [value] };
					$scope.listaTipoCredito.push(tipoItem);

				} else {

					var temTipo = false;
					var indexTipo = null;

					angular.forEach($scope.listaTipoCredito, function(valueTipo, keyTipo) {
						if (valueTipo.nome === tipoNome ) {
							temTipo = true;
							indexTipo = keyTipo;
						}
					});

					if (temTipo) {
						$scope.listaTipoCredito[indexTipo].qtd++;
						$scope.listaTipoCredito[indexTipo].valor += tipoValor;
						$scope.listaTipoCredito[indexTipo].listaConciliacaoLancamento.push(value);

					} else {
						var tipoItem = { nome: tipoNome, qtd: 1, valor: tipoValor, expanded: false, listaConciliacaoLancamento: [value] };
						$scope.listaTipoCredito.push(tipoItem);
					}
				}

			}

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

	$scope.exibirDetalhePessoa = function(credor, ev) {
		var useFullScreen = true;

		if(angular.isDefined(credor.codigo) && credor.codigo != null){
			$scope.pessoa = PessoaJuridica.popup({id: credor.codigo, tipo: credor.tipoPessoa});
		}
		else{
			$scope.pessoa = {};
		}

		$mdDialog.show({
			controller: DialogControllerPessoa,
			locals: {pessoa: $scope.pessoa, titulo: 'Credor / Favorecido'},
			templateUrl: 'partials/tesouraria/cadastros/conta_corrente/html/dialogs/detalhepessoa.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose:true,
			fullscreen: useFullScreen,
			arialabel: 'Credor / Favorecido'
		})
	};

	function DialogControllerPessoa($scope, $mdDialog, pessoa, titulo) {
		$scope.pessoa = pessoa;
		$scope.titulo = titulo;

		$scope.fechar = function() {
			$mdDialog.cancel();
		};
	};


  init();
};
})();
