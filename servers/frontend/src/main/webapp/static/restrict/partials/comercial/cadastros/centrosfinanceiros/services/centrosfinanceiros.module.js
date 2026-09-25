(function() {
	'use strict';

	angular
		.module('app.centrosfinanceiros', [])

		.constant('moduleUrl', 'partials/comercial/cadastros/centrosfinanceiros/html')

		.factory('CentroFinanceiro', centrosfinanceirosFactory)
		.factory('CentroFinanceiroEndereco', centrosfinanceirosEnderecoFactory)
		.factory('CentroFinanceiroFluxoEtapa', centrosfinanceirosFluxoEtapaFactory)
		.factory('CentroFinanceiroAliquota', centrosfinanceirosAliquotaFactory)

		.config(config);

		function centrosfinanceirosFactory($resource, $restServer) {
			return $resource(`${$restServer}rest/centrofinanceiro/:id/:action`, undefined, {
				cancelar: {
					method: 'POST',
					url: `${$restServer}rest/centrofinanceiro/cancelar`
				},
				copy: {
					method: 'POST',
					url: `${$restServer}rest/centrofinanceiro/copiar`
				},
				deleteList: {
					method: 'POST',
					url: `${$restServer}rest/centrofinanceiro/deletar/`
				},
				getGerenciaLogada: {
					 method: 'GET',
					 url: `${$restServer}rest/centrofinanceiro/gerencialogada`
				},
				buscarPorUnidade: {
					 method: 'POST',
					 url: `${$restServer}rest/centrofinanceiro/buscarPorUnidade`,
					 isArray: true
				},
				buscarPorUnidadeTelaRelatorio: {
					 method: 'POST',
					 url: `${$restServer}rest/centrofinanceiro/buscarPorUnidadeTelaRelatorio`,
					 isArray: true
				},
				buscarGerenciasPorUnidade: {
					method: 'GET',
					url: `${$restServer}rest/centrofinanceiro/buscargerenciasporunidade/:id`,
					isArray: true
				},
				buscarTodasGerenciasTelaRelatorio: {
					method: 'POST',
					url: `${$restServer}rest/centrofinanceiro/buscartodasgerenciastelarelatorio`,
					isArray: true
				},
				buscarGerenciasDaPesquisa: {
					method: 'POST',
					params: {action:'buscargerenciaspesquisa'},
					isArray: true
				},
				buscarGerenciasPorDiretoria: {
					method: 'POST',
					params: {action:'buscargerenciaspordiretoria'},
					isArray: true
				},
				buscarGerenciasPorContratos: {
					method: 'GET',
				 	params: {'action':'buscargerenciascontratos'},
				 	isArray: true
				},
				buscarCFs: {
					 method: 'POST',
					 url: `${$restServer}rest/centrofinanceiro/buscarCFs`,
					 isArray: true
				},
				buscarFaturamentoCFs: {
					method: 'POST',
					url: `${$restServer}rest/centrofinanceiro/buscarFaturamentoCFs`,
					isArray: true
			   },
				loadViewById: {
					 method: 'GET',
					 url: `${$restServer}rest/centrofinanceiro/loadViewById/:id`
				},
				inativarById: {
					 method: 'GET',
					 url: `${$restServer}rest/centrofinanceiro/inativarById/:id`
				},
				reativarById: {
					 method: 'GET',
					 url: `${$restServer}rest/centrofinanceiro/reativarById/:id`
				},
				removerById: {
					 method: 'GET',
					 url: `${$restServer}rest/centrofinanceiro/removerById/:id`
				},
				paralisarById: {
					 method: 'GET',
					 url: `${$restServer}rest/centrofinanceiro/paralisarById/:id`
				},
				encerrarById: {
					 method: 'GET',
					 url: `${$restServer}rest/centrofinanceiro/encerrarById/:id`
				},
				encerrarFatById: {
					method: 'GET',
					url: `${$restServer}rest/centrofinanceiro/encerrarFatById/:id`
			   	},
				cancelarById: {
					 method: 'GET',
					 url: `${$restServer}rest/centrofinanceiro/cancelarById/:id`
				},
				buscarGerenciasDaEdicao: {
					 method: 'POST',
					 url: `${$restServer}rest/centrofinanceiro/buscargerenciasedicao`,
					 isArray: true
				},
				buscarClPorCliente: {
					 method: 'GET',
					 url: `${$restServer}rest/centrofinanceiro/buscarclporcliente/:id`,
					 isArray: true
				},
				buscarTodasGerencias: {
				     method: 'GET',
                	 url: `${$restServer}rest/centrofinanceiro/buscartodasgerencias`,
                	 isArray: true
				},
				salvarAlterarFecho: {
					method: 'POST',
					params: {action:'salvarAlterarFecho'}
				},
				criarFecho: {
					method: 'POST',
					params: {action:'criarFecho'}
				},
				atualizarFecho: {
					method: 'POST',
					params: {action:'atualizarFecho'}
				},
				buscarGerenciasPorRegional: {
					 method: 'GET',
					 url: `${$restServer}rest/centrofinanceiro/buscarGerenciasPorRegional/:id`,
					 isArray: true
				},
				buscarCFsPorPerfilFaturamento: {
					 method: 'GET',
					 url: `${$restServer}rest/centrofinanceiro/buscarCFsPorPerfilFaturamento`,
					 isArray: true
				},
				findAllGerencias: {
				 	 method: 'GET',
					 url: `${$restServer}rest/centrofinanceiro/findAllGerencias`,
					 isArray: true
				},
				obterListaCentrosLucrosAtivos: {
					 method: 'GET',
					 params: {action:'obterlistacentroslucrosativos'},
					 isArray: true
				},
				buscarCFsSimples: {
					 method: 'POST',
					 params: {action:'buscarcfssimples'},
					 isArray: true
				}
			});
		}

		function config(moduleUrl, $stateProvider) {
			$stateProvider
				.state('centrosfinanceiros', {
					abstract: true,
					url: '/comercial/cadastros/centrosfinanceiros',
					template: '<div ui-view></div>'
				})
				.state('centrosfinanceiros.list', {
					url: '/listar',
					templateUrl: `${moduleUrl}/list.html`,
					controller: 'CentrosFinanceirosListarCtrl',
					controllerAs: '$ctrl'
				})
				.state('centrosfinanceiros.new', {
					url: '/novo',
					templateUrl: `${moduleUrl}/new.html`,
					controller: createController,
					controllerAs: '$ctrl'
				})
				.state('centrosfinanceiros.new_from', {
					url: '/novo/{from}/{objectId:int}',
					templateUrl: `${moduleUrl}/edit.html`,
					controller: createController,
					controllerAs: '$ctrl'
				})
				.state('centrosfinanceiros.view', {
					url: '/{objectId:int}',
					templateUrl: `${moduleUrl}/view.html`,
					controller: viewController,
					controllerAs: '$ctrl'
				})
				.state('centrosfinanceiros.view_from', {
					url: '/{objectId:int}?from',
					templateUrl: `${moduleUrl}/view.html`,
					controller: viewController,
					controllerAs: '$ctrl'
				})
				.state('centrosfinanceiros.edit', {
					url: '/{objectId:int}/editar',
					templateUrl: `${moduleUrl}/edit.html`,
					controller: editController,
					controllerAs: '$ctrl'
				})
				.state('centrosfinanceiros.edit_from', {
					url: '/{objectId:int}/editar/{from}/{objectIdFrom:int}',
					templateUrl: `${moduleUrl}/edit.html`,
					controller: editController,
					controllerAs: '$ctrl'
				})
				.state('centrosfinanceiros.imprimir_os_inicial', {
					url: '/{objectId:int}/imprimir',
					templateUrl: `${moduleUrl}/imprimir_os_inicial.html`,
					controller: viewController,
					controllerAs: '$ctrl',
					data: {
						titulo: 'Centros Financeiros',
						modulo: 'Comercial',
						subtitulo: 'IMPRIMIR_OS_INICIAL'
					}
				})
				.state('centrosfinanceiros.fecho_edit', {
					url: '/{objectId:int}/fecho',
					templateUrl: `${moduleUrl}/fecho.detail.html`,
					controller: editController,
					controllerAs: '$ctrl',
					data: {
						titulo: 'Centros Financeiros',
						modulo: 'Comercial',
						subtitulo: ''
					}
				})
				.state('centrosfinanceiros.fecho_new', {
					url: '/{objectId:int}/fecho/novo',
					templateUrl: `${moduleUrl}/fecho.detail.html`,
					controller: createController,
					controllerAs: '$ctrl',
					data: {
						titulo: 'Centros Financeiros',
						modulo: 'Comercial',
						subtitulo: ''
					}
				})
				;
		}

		function centrosfinanceirosEnderecoFactory($resource, $restServer) {
			return $resource(`${$restServer}rest/centrofinanceiroendereco/:id/:action`, undefined, {
				buscarListaCentroFinanceiroEndereco: {
					method: 'POST',
					params: {action:'buscarListaCentroFinanceiroEndereco'},
					isArray: true
				}
			});
		}

		function centrosfinanceirosFluxoEtapaFactory($resource, $restServer) {
			return $resource(`${$restServer}rest/centrofinanceirofluxoetapa/:id/:action`, undefined, {
				buscarTodosEtapaFluxo: {
					method: 'GET',
					params: {action:'buscartodosetapafluxo'},
					isArray: true
				},
				motivoAplicacaoByFluxoEtapaId: {
					method: 'GET',
					url: `${$restServer}rest/centrofinanceirofluxoetapa/motivoAplicacaoByFluxoEtapaId/:id`
				},
				assinarCentroFinanceiro: {
					 method: 'GET',
					 url: `${$restServer}rest/centrofinanceirofluxoetapa/assinarCentroFinanceiro/:codEtapaFluxo/:codCentroFinanceiro`
				},
                retornarAssinatura: {
                     method: 'POST',
                     url: `${$restServer}rest/centrofinanceirofluxoetapa/retornarAssinatura`
                },
                retornarAssinaturaSemMotivoRetorno: {
                     method: 'POST',
                     url: `${$restServer}rest/centrofinanceirofluxoetapa/retornarAssinaturaSemMotivoRetorno`
                }
			});
		};

		function centrosfinanceirosAliquotaFactory($resource, $restServer) {
			return $resource(`${$restServer}rest/centrofinanceiroaliquota/:id/:action`, undefined, {
				getListByCodCentroFinanceiro: {
					 method: 'GET',
					 url: `${$restServer}rest/centrofinanceiroaliquota/getListByCodCentroFinanceiro/:id`,
					 isArray: true
				}
			});
		};

})();
