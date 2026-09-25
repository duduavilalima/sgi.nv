(function () {
	'use strict';

	angular
		.module('app.movimentoautorizadoboletos')

		.controller('MovimentoAutorizadoBoletosDetailCtrl', detailController);

	function detailController($scope, $state, $rootScope, $stateParams, $mdDialog, EnumService, UsuarioLogadoService, DadosBancarios, MovimentoAutorizadoBoletos, DialogConfirm) {

	    $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;

        $scope.object = null;

		$scope.usuario = UsuarioLogadoService.getUsuarioLogado();

        function loadObj() {
            $rootScope.loadingProgress = true;

            var qParcelaNota = MovimentoAutorizadoBoletos.get({id: $stateParams.objectId }).$promise;

            qParcelaNota.then(function(data) {
                $scope.object = data;
                $scope.object.infoBoleto = "Linha digitável";
				$scope.exibeLinhaDigitavel = true;
				$scope.hideBancos = true;

                verificarPerfilHomologador();

				statusCamposCodBarrLD();

				disableCampoComIconeEditar();

				if(!isTributo() && $scope.object.dadosBancarios != null){
					$scope.hideBancos = false;
					carregarDadosBancarios();
				}

				$rootScope.loadingProgress = false;
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
                $rootScope.loadingProgress = false;
            });

        }

        $scope.cancelar = function() {
            $state.go('movimentoautorizadoboletos.list');
        };

        $scope.salvar = function() {
			$scope.action = true;
			var pUpdate = $scope.object.$update({ id: $scope.object.codigo });

			pUpdate.then(function(data){
				$scope.notification.success("Edição", "Dados do pagamento atualizado com sucesso.");
				$state.go('movimentoautorizadoboletos.list');
				$scope.action = false;
			}).catch(function(response){
			    $scope.action = false;
				$scope.$emit('crud-critical', 'save', response);
			});

        }

		function carregarDadosBancarios() {
			var filtro = {};
			filtro.pessoa = $scope.object.credor;

			DadosBancarios.page(filtro).$promise.then(function(data) {

				$scope.listBancos = data.content;

				angular.forEach($scope.listBancos, function(value, key){

					if (value.codigo === $scope.object.dadosBancarios.codigo ) {
						value.selecao = true;
					} else {
						value.selecao = false;
					}
				});

			}).catch(function(response) {
				$scope.$emit('crud-critical', 'contrato load', response);
				$scope.listBancos = [];
			});
	 	};

		$scope.changeSelectBanco = function(item) {
			angular.forEach($scope.listBancos, function(value) {
				value.selecao = false;
			});
			$scope.object.dadosBancarios = item;
			item.selecao = true;
		};

        EnumService.enumValues({
                pacote: 'br.com.araujoabreu.sgi.administracao.enums',
                classe: 'null',
                enum: 'TipoInformacaoNumeroBoleto'
            })
            .$promise.then(function(data) {
                $scope.enumTipoNumBoleto = [];
                angular.forEach(data, function(value, key) {
                    $scope.enumTipoNumBoleto.push(value);
                });
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
        });


        $scope.$on('changeInfoBoleto', function() {
            exibirLDCodBarr();
        });

        function exibirLDCodBarr() {
            if ($scope.object.infoBoleto === 'Linha digitável') {
               $scope.exibeLinhaDigitavel = true;

            } else {
                   $scope.exibeLinhaDigitavel = false;
            }
        }

		EnumService.enumValues({
				pacote: 'br.com.araujoabreu.sgi.contasApagar.domain',
				classe: 'Pagamento',
				enum: 'TipoDocumentoPagamento'
			})
			.$promise.then(function(data) {
				 $scope.listTipoDoc = [];

				 angular.forEach(data, function(value) {
					 $scope.listTipoDoc.push(value);
				 });
			}).catch(function(response) {
			 $scope.$emit('crud-critical', 'load', response);
		});

		 $scope.$on('changeTipoDocPag', function() {
				statusCamposCodBarrLD();
				$scope.object.codigoBarras = '';
                $scope.object.linhaDigitavel = '';
		 });

        function statusCamposCodBarrLD() {
			switch($scope.object.tipoDocumentoPagamento){
				case 'Guia':
					$scope.tipoGuia = true;
					$scope.tipoBoleto = false;
					$scope.tipoConc = false;
					break;

				case 'Concessionária':
					$scope.tipoGuia = false;
					$scope.tipoBoleto = false;
					$scope.tipoConc = true;
					break;

				default:
					$scope.tipoGuia = false;
					$scope.tipoBoleto = true;
                    $scope.tipoConc = false;

			}

			if ($scope.object.tipoDocumentoPagamento === 'Boleto'
					|| $scope.object.tipoDocumentoPagamento === 'Concessionária' || ($scope.object.tipoDocumentoPagamento === 'Guia' && isTributo())) {

            			   $scope.disableInfoBoleto = false;
            			   $scope.disableLDCodBarr = false;
            			   exibirLDCodBarr();
            } else {
                	$scope.disableInfoBoleto = true;
                	$scope.disableLDCodBarr = true;
					$scope.object.codigoBarras = '';
				 	$scope.object.linhaDigitavel = '';
            }
        }

        function isTributo() {
			return ($scope.object.tipo != null  && $scope.object.tipo === 'TA') || $scope.object.tipo === 'TR';
        }

		function disableCampoComIconeEditar(){

			var dataPgt = $scope.object.dataPagamento;
			var dataVen = $scope.object.dataVencimento

			if(isTributo()){

				if(dataPgt != null && dataVen!= null){

					$scope.disableField = (moment(dataPgt).endOf("day").isBefore(moment(dataVen).endOf("day")));
				}

			}else{

				$scope.disableField = false;
			}
		}

		$scope.$on('changeValorPagar', function() {
			var multa = $scope.object.valorMulta;
			var juros =	$scope.object.valorJuros;
			var descontos =	$scope.object.valorDesconto;
			var valueLiquido = $scope.object.valorLiquido;

			var valuePagar = valueLiquido + juros + multa - descontos;

			if(valuePagar <= 0){

				$scope.object.valorPagar = 0;
			}else{

				$scope.object.valorPagar = valuePagar;
			}

        });

		/* Inicio da Dialog Conta Contabil */
		$scope.contaContabil = function(tipoContaContabil) {

			$mdDialog.show({
				controller: DialogContaContabil,
				locals: {
					movAutBol: $scope.object,
					tipoContaContabil: tipoContaContabil,
					notification: $scope.notification
				},
				templateUrl: 'partials/tesouraria/movimento_autorizado_boletos/html/partials/dialog_conta_contabil.html',
				parent: angular.element(document.body),
				clickOutsideToClose: false,
				fullscreen: false
			});
		};

		function DialogContaContabil($scope, $timeout, $mdDialog, $state, movAutBol, tipoContaContabil, notification, AutocompleteService ) {

			$scope.title = tipoContaContabil.toLowerCase();
			$scope.contaContabil = null;
			var aux = null;

			function checkListaContaContabil() {

				if ( !angular.isUndefined(movAutBol.listaPgtoCC) && movAutBol.listaPgtoCC != null ) {

					var contaContabilPorTipo = null;

					angular.forEach(movAutBol.listaPgtoCC, function(value, key) {
						if ( value.tipo === tipoContaContabil ) {
							contaContabilPorTipo = value.contaContabil;
						}
					})

					if (contaContabilPorTipo != null) {
						aux = contaContabilPorTipo;
						$scope.contaContabil = contaContabilPorTipo;
					}
				}
			}

			checkListaContaContabil();

			// INICIO DO AUTOCOMPLETE DE CONTA CONTÁBIL
			function autoCompleteContaContabil(){

				$scope.searchTextContaContabil = null;
				$scope.selectedItemContaContabil = aux;
				$scope.resultsContaContabil = [];

				$scope.fnSelectedItemContaContabil = function(item) {
					$scope.contaContabil = item;
				};

				$scope.querySearchContaContabil = function(query) {
					$rootScope.loadingProgress = true;

                    var filtro = {};
                    filtro.nome = query;

					var resultContaContabilPromise = AutocompleteService.getContaContabil(filtro).$promise;

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

			$scope.confirm = function() {

				if ($scope.contaContabil == null) {
					notification.error("Conta contábil é um campo de preenchimento obrigatório.");

				} else {

					var jaTemNaLista = false;

					angular.forEach(movAutBol.listaPgtoCC, function(value, key) {
						if(value.tipo === tipoContaContabil) {
							value.contaContabil = $scope.contaContabil;
							jaTemNaLista = true;
							return;
						}
					});

					if (!jaTemNaLista) {
						var objPagamento = {codigo: movAutBol.codigo };
						var objPagamentoContaContabil = {};

						objPagamentoContaContabil.codigo = null;
						objPagamentoContaContabil.tipo = tipoContaContabil;
						objPagamentoContaContabil.pagamento = objPagamento;
						objPagamentoContaContabil.contaContabil = $scope.contaContabil;

						movAutBol.listaPgtoCC.push(objPagamentoContaContabil);
					}

					$scope.hide();
				}

			};

			$scope.hide = function() {
				$mdDialog.hide();
			};
			$scope.cancel = function() {
				$mdDialog.cancel();
			};
		}/* Fim da Dialog  Conta Contabil */

		function verificarPerfilHomologador() {
			UsuarioLogadoService.possuiUmPerfilLista('homologacao-manual').$promise.then(function(data) {

				$scope.habilitarHomologBanco = data.possuiUmPerfilLista;

		   }).catch(function(response) {
			   $rootScope.loadingProgress = false;
			   $rootScope.$emit('crud-critical', 'load', response);
		   });
        };

		/* Inicio da Dialog Homologação */
		$scope.homologarDadosBancarios = function(item) {
			// Chamada do serviço que criará um dialog de confirmação.
			DialogConfirm.show(
				null,
				item,
				'Homologar manualmente dados bancários: Confirmação',
				'Deseja homologar manualmente estes dados bancários?',
				'Confirmar',
				'Cancelar',
				confirmHomologar,
				cancelHomologar);
		};

		function confirmHomologar(object, fnHide) {

			DadosBancarios.homologarDadosBancariosManual(object)
				.$promise.then(function(data) {

				    carregarDadosBancarios();

					// Esta função veio do dialog.
					fnHide();

					//$scope.notification.success("Sucesso");
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'dialog', response);
			});
		};
		function cancelHomologar(fnHide) {
			fnHide();
		};
		/*Fim da Dialog Homologação*/

        loadObj();

	};/* FIM DE EDITAR */

})();
