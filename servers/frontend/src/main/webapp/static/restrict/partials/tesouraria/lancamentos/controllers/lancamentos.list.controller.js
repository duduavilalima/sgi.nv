(function () {
	'use strict';

	angular
		.module('app.lancamentos')

		.controller('LancamentosListarCtrl', listController);

	function listController($scope, $state, $rootScope, $controller, $mdDialog, EnumService, Unidade, ContaCorrente, ContaContabil, Lancamentos, DialogConfirm, AutocompleteService) {
		$controller('GridController', {
			$scope: $scope,
			Resource: Lancamentos
		});

		function init() {
			$scope.modulo = $state.current.data.modulo;
			$scope.titulo = $state.current.data.titulo;
			$scope.subtitulo = "Pesquisar";

			$scope.object = {};
			$scope.object.numero = null;
			$scope.object.status = null;
			$scope.object.pagamentoDe = null;
			$scope.object.pagamentoAte = null;
			$scope.object.unidade = null;
			$scope.object.banco = null;
			$scope.object.contaCorrente = null;
			$scope.object.contaContabil = {};
			$scope.object.contaContabil.numero = null;
			$scope.object.contaContabil.descricao = null;
			$scope.object.valorDe = null;
			$scope.object.valorAte = null;

			initCampoSituacao($scope.object);
			initCampoEmpresa($scope.object);

			initOnChanges();
		}

		function initCampoSituacao(object) {
			object.status = null;
			$scope.listSituacoes = [];

			var pStatusLancamento = EnumService.enumValues({
										pacote: 'br.com.araujoabreu.sgi.tesouraria.domain',
										classe: 'Lancamento',
										enum: 'StatusLancamento'
									}).$promise;

			pStatusLancamento.then(function(data) {
				$scope.listSituacoes.push('');
				angular.forEach(data, function(value, key) {
					$scope.listSituacoes.push(value);
				});

			}).catch(function(response) {
				 $scope.$emit('crud-critical', 'load', response);
			});
		}

		function initCampoEmpresa(object) {
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

		function initCampoBanco(object) {
			$scope.listBancos = [];

			if(object.unidade !== null && object.unidade.codigo !== null) {
				var objContaCorrente = {};
				var pContaCorrente = ContaCorrente.obterContas(null, { pessoa: {codigo: object.unidade.codigo} }).$promise;

				pContaCorrente.then(function(data) {
					$scope.listBancos.push({codigo: null, nome : ''});

					angular.forEach(data, function(value, key) {
						$scope.listBancos.push(value.banco);
					})

				}).catch(function(response) {
					$scope.$emit('crud-critical', 'load', response);
				});
			}
		}

		function initCampoAgencia(object) {
			$scope.listAgencias = [];

			if(object.unidade !== null && object.unidade.codigo !== null) {
				var objContaCorrente = {};
				var pContaCorrente = ContaCorrente.obterContas(null, { pessoa: {codigo: object.unidade.codigo} }).$promise;

				pContaCorrente.then(function(data) {
					$scope.listAgencias.push({codigo: null});

					angular.forEach(data, function(value, key) {
						if(object.banco !== null && object.banco.codigo === value.banco.codigo) {
							$scope.listAgencias.push(value);
						}
					});
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'load', response);
				});

			}
		}

		/** ONCHANGES */
		function initOnChanges() {
			$scope.changeSituacao = function() {
				if ($scope.object.status === '') {
					delete $scope.object.status;
				}
			};

			$scope.changeEmpresa = function() {
				if ($scope.object.unidade.codigo === null) {
					delete $scope.object.unidade;
					$scope.listBancos = [];
				} else {
					initCampoBanco($scope.object);
				}
				$scope.object.banco = null;
				$scope.object.contaCorrente = null;
				$scope.listAgencias = [];

				$scope.selectedItemContaContabil = null;
				$scope.object.contaContabil = {};
				$scope.searchTextContaContabil = "";
			};

			$scope.changeBanco = function() {
				if ($scope.object.banco.codigo === null) {
					$scope.object.banco = null;
					$scope.object.contaCorrente = null;
					$scope.listAgencias = [];
				} else {
					initCampoAgencia($scope.object);
				}
			};

			$scope.changeAgencia = function() {
				if ($scope.object.contaCorrente.codigo === null) {
					delete $scope.object.contaCorrente;
				}
			};
		}

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

		$scope.buscar = function() {
		    $scope.object.contaContabil.numero = $scope.object.contaContabil.descricao;
			$rootScope.loadingProgress = true;
			$scope.setDefaultParams();
			$scope.getPage(1);
		};

		$scope.$on('crud-success', function(event, action, data) {
			$rootScope.loadingProgress = false;
		});

		$scope.$watch('result', function() {
			$rootScope.loadingProgress = false;
		});


		$scope.reset = function() {
			$scope.listBancos = [];
			$scope.listAgencias = [];

			$scope.result = null;
			$scope.clear();
			$scope.object = {};
			$scope.object.contaContabil = {};
			$scope.object.contaContabil.numero = null;
			$scope.object.contaContabil.descricao = null;

//			document.querySelectorAll('input[type=search]')[0].value = '';
		};


		/** INICIO DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA  */
		$scope.getAgenciaConta = function(contaCorrente) {
			return contaCorrente.agencia + " / " + contaCorrente.conta + "-" + contaCorrente.digito;
		}
		var cellTemplateAgenciaConta = '<span>{{grid.appScope.getAgenciaConta(row.entity.contaCorrente)}}</span>';

		$scope.getContaContabil = function(contaContabil) {
			return contaContabil.numero + " - " + contaContabil.descricao;
		}
		var cellTemplateContaContabil = '<span>{{grid.appScope.getContaContabil(row.entity.contaContabil)}}</span>';

		$scope.getStatusLancamento = function(status) {
			if (status === 'Aberto') {
				return "Aberto";
			}
			if (status === 'Enviado para Contabilidade') {
				return "Enviado";
			}
			if (status === 'Cancelado') {
				return "Cancelado";
			}

		}
		var cellTemplateStatusLancamento = '<span>{{grid.appScope.getStatusLancamento(row.entity.status)}}</span>';

		var acoesAdicionais = [
		    {
        		nome : 'exibir.objeto', label: 'Exibir', icone : 'icon-eye', tooltip: 'Exibir os dados deste lançamento'
        	}
        	,
			{
				nome : 'editar.objeto', label: 'Editar', icone : 'icon-pencil', tooltip: 'Editar os dados deste lançamento'
			}
			,
			{
				nome : 'remover.objeto', label: 'Remover', icone : 'icon-close', tooltip: 'Remover este lançamento (esta operação não poderá ser desfeita)'
			}
		];

		var columnDefs = [
			{
				name: 'Número',
				field: 'numero',
				width: '8%',
				enableSorting: false,
				sortFieldString: false,
				pinnedLeft: false,
				funcao: ['exibir', 'row.entity']
			},
			{
				name: 'Situação',
				field: 'status',
				cellTemplate: cellTemplateStatusLancamento,
				width: '7%',
				cellClass: 'text-left'
			},
			{
				name: 'Banco',
				field: 'contaCorrente.banco.nome',
				width: '10%'
			},
			{
				name: 'Agência / Conta',
 				field: 'contaCorrente',
 				cellTemplate: cellTemplateAgenciaConta,
 				cellClass: 'text-center',
				width: '15%'
			},
			{
				name: 'Conta contábil',
				displayName: 'Conta contábil',
  				field: 'contaContabil',
  				cellTemplate: cellTemplateContaContabil
			},
			{
				name: 'Pagamento',
   				field: 'data',
				width: '9%',
				cellFilter: 'formatacaoData',
				cellClass: 'text-center'
			},
			{
				name: 'Valor',
				field: 'valor',
				width: '9%',
				cellFilter: 'formatacaoMoeda',
				cellClass: 'text-right'
			},
			{
				name: 'D/C',
				field: 'debitoCredito',
				width: '4%',
				cellClass: 'text-center'
			},
			{
				name: 'Opções',
				width: '7%',
				pinnedRight: false,
				enableSorting: false,
				acoes: [],
				acoesAdicionais: acoesAdicionais
			}
		];

		$scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'lançamento', 'e');

		$scope.setDefaultParams = function() {
			$scope.paginationOptions.sortField = 'lancamento.codigo';
			$scope.paginationOptions.sortFieldString = false;
			$scope.paginationOptions.sortDir = 'asc';
		}
		$scope.setDefaultParams();

		$scope.getPage = function(page) {
			$scope.getPageGrid($scope.gridPesquisa, page);
		};


		$scope.exibirAcao = function(acao, row) {
			if (acao === 'editar') {
				if (row.entity.status === 'Aberto') {
					return true;
				}
			}
			if (acao === 'remover') {
				if (row.entity.status === 'Aberto') {
					return true;
				}
			}
			if (acao === 'exibir') {
				return true;
			}
				return false;
	 	}


		$scope.exibir = function(item) {
			$state.go('lancamentos.view', { objectId: item.codigo });
		};

		$scope.editar = function(item) {
			$state.go('lancamentos.edit', { objectId: item.codigo });
		};

		$scope.remover = function(item) {
			$scope.showConfirm(item);
		};

		$scope.showConfirm = function(objLancamento) {
			var tituloDialog = "Lançamento - Remover: Confirmação";
			var msgDialog = "Deseja realmente remover este lançamento (esta operação não poderá ser desfeita)?";
			var tituloBotaoConfirmar = "CONFIRMAR";
			var tituloBotaoCancelar = "CANCELAR";

			var funcaoCancelar = function(fnHide) {
				fnHide();
			};

			var funcaoConfirmar = function(object, fnHide) {
				var pLancamento = Lancamentos.delete({id: object.codigo}).$promise;

				pLancamento.then(function(data){

					var title = 'Remover Lançamento';
					var message = 'Lançamento ' + object.numero + ' removido com sucesso.';
					$scope.notification.success(title, message);

					var index = null;
					angular.forEach($scope.result, function(value, key) {
						if (value.codigo === object.codigo) {
							index = $scope.result.indexOf(value);
							return;
						}
					});

					if (index != null) {
						$scope.result.splice(index, 1);
						index = null;
					}

					fnHide();

				}).catch(function(response){
					console.error('crud-critical', 'dialog', response);
					$scope.notification.criticalError(response.status, response.statusText, response.data);
				});
			};

			DialogConfirm.show(null, objLancamento, tituloDialog, msgDialog, tituloBotaoConfirmar, tituloBotaoCancelar, funcaoConfirmar, funcaoCancelar);
		};

		init();

		/** FIM DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA  */

	};
})();
