(function () {
	'use strict';

	angular
		.module('app.movimentoautorizadoboletos')

		.controller('MovimentoAutorizadoBoletosListarCtrl', listController);

	function listController($scope, $controller, $state, EnumService, $mdDialog, $rootScope, $timeout, Unidade, ContaCorrente, Pessoa, Relatorios, UsuarioLogadoService, MovimentoAutorizadoBoletos, Banco, Boleto, String, BoletoService, CentroFinanceiro) {

	    $controller('GridController', {
                $scope: $scope,
                Resource: MovimentoAutorizadoBoletos
        });

	    $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;

        $scope.object = {};
		$scope.banco = {};
		$scope.banco.codigo = undefined;

        $scope.usuario = UsuarioLogadoService.getUsuarioLogado();

        $scope.exibeLinhaDigitavel = false;
        $scope.object.infoBoleto = "Código de barras";

        EnumService.enumValues({
                pacote: 'br.com.araujoabreu.sgi.administracao.enums',
                classe: 'null',
                enum: 'StatusCodigoBarraLd'
            })
            .$promise.then(function(data) {
                $scope.enumStatusCodBarr = [];
                $scope.enumStatusCodBarr.push('');
                angular.forEach(data, function(value, key) {
                    $scope.enumStatusCodBarr.push(value);
                });
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
        });


        $scope.$on('changeCodigo', function() {
            if ($scope.object.codigo === '') {
                $timeout(function() {
                    delete $scope.object.codigo;
                }, 200);
            }
        });

        function buscarEmpresas() {
            let filter = { tipoUnidade: 'MATRIZ' };
            Unidade.page(undefined, filter)
                .$promise.then(function(data) {
                    $scope.listEmpresas = [];
                    $scope.listEmpresas.push({codigo: undefined, codnome: ''});
                    angular.forEach(data.content, function(value, key) {
                        $scope.listEmpresas.push(value);
                    });

                    $scope.listEmpresas.sort(function(a, b){ return a.codigo-b.codigo; });

                }).catch(function(response) {
                    console.error(response);
                    $scope.$emit('crud-critical', 'search', response);
                });
        }

        buscarEmpresas();

        function carregarBancos() {
            Banco.todos().$promise.then(function(data) {
                    $scope.bancoList = [];

					$scope.bancoList.push({banco: {nome : ''}});

                    angular.forEach(data, function(value, key) {
                        $scope.bancoList.push(value);
					});
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'contrato load', response);
                    $scope.bancoList = [];
                    $scope.agenciaContaList = [];
            });
        }

        carregarBancos();

        function buscarTodasGerencias(){
            $scope.listGerencias = CentroFinanceiro.buscarTodasGerencias();
        }

        buscarTodasGerencias();

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
            if ($scope.object.infoBoleto === 'Linha digitável') {
               $scope.exibeLinhaDigitavel = true;
               $scope.object.codigobarra = '';
               } else {
                   $scope.exibeLinhaDigitavel = false;
                    $scope.object.linhadigitavel = '';
                }
        });

        function validaValorDeAte() {
            if (angular.isDefined($scope.object.valorDe) && $scope.object.valorDe != undefined && angular.isDefined($scope.object.valorAte) && $scope.object.valorAte != undefined) {
                $scope.object.valorDe = Number($scope.object.valorDe);
                $scope.object.valorAte = Number($scope.object.valorAte);

                if ($scope.object.valorDe > $scope.object.valorAte) {
                    return true;
                }
                else if ($scope.object.valorAte < $scope.object.valorDe) {
                    return true;
                }
                return false;
            }
        }

        let acoesAdicionais = [
            {nome : 'editar.objeto', label: 'Editar', icone : 'icon-pencil', tooltip: 'Editar dados de tesouraria'}
        ];

        $scope.mudarLinha = function(objeto) {
            if (objeto.linhaDigitavelInformada){
                objeto.codBarrasLD = objeto.linhaDigitavel;
            }
            else{
                objeto.codBarrasLD = objeto.codigoBarras;
            }
        };

        $scope.salvarCodigoBarrasLinhaDigitavelPorDigito = function(tipo, codigo, entity) {
            if (tipo) {
                if (codigo != undefined && codigo.length == 47) {
                    $scope.salvarLdCb(entity);
                }
            } else {
                if (codigo != undefined && codigo.length == 44) {
                    $scope.salvarLdCb(entity);
                }
            }
            if (codigo != undefined && codigo.length == 0) {
                entity.linhaDigitavel = undefined;
                entity.codigoBarras   = undefined;
            }
        };

        $scope.salvarLdCb = function (objeto) {
            if (objeto.linhaDigitavelInformada){
                objeto.linhaDigitavel = objeto.codBarrasLD;
            }
            else{
                objeto.codigoBarras = objeto.codBarrasLD;
            }

           let pUpdate = MovimentoAutorizadoBoletos.atualizarcodigobarras(objeto).$promise;

			pUpdate.then(function(data){
			    objeto.linhaDigitavel = data.linhaDigitavel;
                objeto.codigoBarras= data.codigoBarras;

                if (data.msgValidarValorEDataPagamento) {
                    $scope.notification.atencao("",data.msgValidarValorEDataPagamento);
                    objeto.dataPagamentoInvalido = true;
                }

				$scope.notification.success("","Pagamento " + data.numero + " atualizado com sucesso.");
			}).catch(function(response){
				$scope.$emit('crud-critical', 'save', response);
			});
        };

        let cellTemplateLdCb = '<input type="text" maxLength="{{row.entity.linhaDigitavelInformada ? 47 : 44}}" ng-model="row.entity.codBarrasLD" value="row.entity.codBarrasLD" ng-keyup="grid.appScope.salvarCodigoBarrasLinhaDigitavelPorDigito(row.entity.linhaDigitavelInformada, row.entity.codBarrasLD, row.entity)" ui-keypress="{13:\'grid.appScope.salvarLdCb(row.entity)\'}">';
        let cellTemplateExibirLd = '<input type="checkbox" ng-model="row.entity.linhaDigitavelInformada" ng-click="grid.appScope.mudarLinha(row.entity)" style="margin: 0px;">';
        let cellTemplateBanco = '<div class="over-flow-hidden">{{row.entity.dadosBancarios.banco.nome}}</div>';
        let cellTemplateCredor = '<div class="over-flow-hidden">{{row.entity.credor.nome}}</div>';

        /* CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */
        let columnDefs = [{
				name: 'Número',
				field: 'numero',
				enableSorting: false,
				sortFieldString: false,
				width: '10%',
				pinnedLeft: false
			}, {
				name: 'Pagamento',
				field: 'dataPagamento',
				width: '8%',
				cellClass: 'text-center',
				cellFilter: 'formatacaoData',
				cellTemplate: '<div style="{{row.entity.dataPagamentoInvalido ? \'color: red;\' : \'\'}}">{{row.entity.dataPagamento | date: \'dd/MM/yyyy\'}}</div>'
			}, {
				name: 'Banco',
				field: 'dadosBancarios.banco.nome',
				width: '10%',
				detalheContas: ['credor.codigo', 'dadosBancarios.codigo'],
				cellTemplate: cellTemplateBanco,
				tooltip: ['field']
			},  {
				name: 'Credor',
				field: 'credor.nome',
				detalhePessoa: ['credor.codigo', 'credor.tipoPessoa'],
				cellTemplate: cellTemplateCredor,
				tooltip: ['field']
			}, {
				name: 'A pagar',
				displayName: 'A pagar',
				field: 'valorPagar',
				width: '8%',
				cellFilter: 'formatacaoMoeda',
				cellClass: 'text-right',
				cellTemplate: '<div style="{{row.entity.dataPagamentoInvalido ? \'color: red;\' : \'\'}}">{{row.entity.valorPagar | currency: \'\':2}}</div>'
			}, {
               	name: 'LD',
               	field: 'linhaDigitavelInformada',
               	width: '5%',
               	cellClass: 'text-center',
               	type: 'boolean',
     			displayName: 'Indica se será informada uma linha digitável na próxima coluna',
               	cellTemplate: cellTemplateExibirLd,
                headerTooltip: true
            }, {
                name: 'Código de barras / Linha digitável',
                displayName: 'Código de barras / Linha digitável',
             	field: 'codBarrasLD',
             	cellTemplate: cellTemplateLdCb
            }, {
				name: 'Opções',
				width: '6%',
				cellClass: 'text-center',
				pinnedRight: false,
				enableSorting: false,
				acoesAdicionais: acoesAdicionais
		}];

		$scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'movimentoautorizado', 'e');

		$scope.setDefaultParams = function() {
			 $scope.paginationOptions.sortField = 'parcelaNota.codigo';
			 $scope.paginationOptions.sortFieldString = false;
			 $scope.paginationOptions.sortDir = 'asc';
		   //$scope.paginationOptions.pageSize = 5; //parametrizar para utilização do teste automatizado.
         };
         
		 $scope.setDefaultParams();

		 $scope.getPage = function(page) {
			 $scope.getPageGrid($scope.gridPesquisa, page);
		 };

        /* FIM DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */

        function filtrar() {
            $rootScope.loadingProgress = true;
            $scope.setDefaultParams();
            $scope.getPage(1);
        }

        $scope.buscar = function() {
            if ($scope.object.infoBoleto === 'Código de barras' && $scope.object.codigoBarras && $scope.object.codigoBarras.trim() !== '') {
                let codigo = String.replaceAll($scope.object.codigoBarras, '.', '');
                Boleto.codigobarras({tipoDocumentoPagamento:'Boleto', numero: codigo})
                .$promise.then(function(data) {
                    let dataVencimento = BoletoService.dataVencimentoCodigoBarras(codigo);
                    $scope.object.pagamentoDe  = dataVencimento;
                    $scope.object.pagamentoAte = dataVencimento;
                    let valor = BoletoService.valorBigDecimalDoTituloDoCodigoDeBarras(codigo);
                    $scope.object.valorDe  = valor;
                    $scope.object.valorAte = valor;
                    filtrar();
                }).catch(function(response) {
                    let mensagem = tratarMensagemDeErro(response.data.message);
					$scope.notification.error(mensagem[0], mensagem[1]);
                    return;
                });
            } else if ($scope.object.infoBoleto === 'Linha digitável' && $scope.object.linhaDigitavel && $scope.object.linhaDigitavel.trim() !== '') {
                let codigo = String.replaceAll($scope.object.linhaDigitavel, '.', '');
                Boleto.linhadigitavel({tipoDocumentoPagamento:'Boleto', numero: codigo})
                .$promise.then(function(data) {

                    let dataVencimento = BoletoService.dataVencimentoLinhaDigitavel(codigo);
                    $scope.object.pagamentoDe = dataVencimento;
                    $scope.object.pagamentoAte = dataVencimento;
                    let valor = BoletoService.valorBigDecimalDoTituloDaLinhaDigitavelFormatada(codigo);
                    $scope.object.valorDe  = valor;
                    $scope.object.valorAte = valor;
                    filtrar();
                }).catch(function(response) {
                	var mensagem = tratarMensagemDeErro(response.data.message);
                    $scope.notification.error(mensagem[0], mensagem[1]);
                    return;
                });
            } else {
                filtrar();
            }
        };

        $scope.$on('crud-success', function(event, action, data) {
            $rootScope.loadingProgress = false;
        });

        $scope.$watch('result', function() {
            $rootScope.loadingProgress = false;
        });

        $scope.listar = function() {
            $state.go('movimentoautorizadoboletos.list');
        };

        $scope.editar = function(item) {
            $state.go('movimentoautorizadoboletos.edit', {
                objectId: item.codigo
            });
        };

        $scope.reset = function() {
            $scope.result = undefined;
            $scope.clear();
            $scope.object.infoBoleto = "Código de barras";
        };


		/*
			Função para dividir o titulo e o corpo das mensagens de erro.
			Retorna uma lista com dois itens. 1 - Titulo / 2 - corpo da mensagem
		*/
        function tratarMensagemDeErro(mensagem) {
        	var listaSeparador = ['.', '!', ':'];
        	var listaResultado = ['', ''];
        	var indexTitulo = -1;

        	if (!mensagem) {
        		 mensagem = [];
        		 mensagem.push('');
        		 mensagem.push('Erro?');
        	     return mensagem;
        	}

        	angular.forEach(listaSeparador, function(value, key){

        		var indexTemp = mensagem.indexOf(value);

				if (indexTemp !== -1) {
					if (indexTitulo === -1) {
						indexTitulo = indexTemp;
					} else if (indexTemp < indexTitulo) {
						indexTitulo = indexTemp;
					}
				}
			});

			if (indexTitulo === -1) {
				listaResultado[1] = mensagem;
			} else {
				listaResultado[0] = mensagem.substring(0, indexTitulo + 1).trim();
				listaResultado[1] = mensagem.substring(indexTitulo + 1, mensagem.lenght).trim();
			}

			if (listaResultado[0].length > 0 && listaResultado[1].length === 0) {
				listaResultado[1] = listaResultado[0];
				listaResultado[0] = '';
			}

			listaResultado[0] = removerPontuacaoFinal(listaResultado[0]);
			listaResultado[1] = removerPontuacaoFinal(listaResultado[1]);

        	return listaResultado;
        }

        /*
        	Remove o a pontuação final de um texto.
        */
        function removerPontuacaoFinal(texto) {
        	var novoTexto = '';

        	if (texto && texto.length > 0) {
        		novoTexto = texto.trim();

        		var ultimoCaractere = novoTexto.charAt(novoTexto.length - 1);

        		if (ultimoCaractere === '.' || ultimoCaractere === '!' || ultimoCaractere === ':' ) {
        			novoTexto = novoTexto.substring(0, novoTexto.length - 1);
        		}
        	}

        	return novoTexto;
        }

	}; /* FIM DE PESQUISA E RESULTADO*/

})();
