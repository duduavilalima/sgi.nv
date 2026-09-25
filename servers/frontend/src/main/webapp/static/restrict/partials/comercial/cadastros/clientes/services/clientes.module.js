(function() {
	'use strict';

	angular
		.module('app.clientes', ['ngMask'])
		.constant('moduleUrl', 'partials/comercial/cadastros/clientes/html')
        .factory('Clientes', clientesFactory)
        .factory('Contato', contatoFactory)
        .factory('PessoaContaContabil', pessoaContaContabilFactory)
        .factory('PessoaFisica', pessoaFisicaFactory)
        .factory('PessoaJuridica', pessoaJuridicaFactory)
		.config(config);

		function clientesFactory($resource, $restServer) {
			return $resource(`${$restServer}rest/clientes/:id/:action`, undefined, {
				cancelar:{
					method:'POST',
					params: {action:'cancelar'}
				},
				removerCliente: {
					method: 'GET',
					params: {action:'remover'}
				},
				buscarContratos: {
					method: 'GET',
					params: {action:'buscarcontratos'},
					isArray: true
				},
				buscarSubclientes: {
					method: 'GET',
					params: {action:'buscarsubclientes'},
					isArray: true
				},
				buscarDadosContabeis: {
					method: 'GET',
					params: {action:'buscardadoscontabeis'},
					isArray: true
				},
				buscarTelefones: {
					method: 'GET',
					params: {action:'buscartelefones'},
					isArray: true
				},
				buscarContatos: {
					method: 'GET',
					params: {action:'buscarcontatos'},
					isArray: true
				},
				buscarClassificacaoCliente: {
					method: 'GET',
					params: {action:'buscarclassificacao'},
					isArray: true
				},
				buscarRelacionamento: {
					method: 'GET',
					params: {action:'buscarrelacionamento'},
					isArray: true
				},
				buscarTipoLogradouro: {
					method: 'GET',
					params: {action:'buscartipologradouro'},
					isArray: true
				},
				consultarDocumento: {
					method: 'POST',
					params: {action:'consultardocumento'},
				},
				consultarDocumentoPopup: {
					method: 'POST',
					params: {action:'consultardocumentopopup'},
				},
				converterFornecedor: {
					method: 'POST',
					params: {action:'buscarfornecedor'},
				}

			});
		}

		function contatoFactory($resource, $restServer) {
			return $resource(`${$restServer}rest/contato/:id/:action`, undefined, {
				remover: {
					method: 'GET',
					params: {action:'remover'}
				}
			});
		}

		function pessoaContaContabilFactory($resource, $restServer) {
			return $resource(`${$restServer}rest/pessoacontacontabil/:id/:action`, undefined, {
				remover: {
					method: 'GET',
					params: {action:'remover'}
				}
			});
		}

		function pessoaFisicaFactory($resource, $restServer) {
			return $resource(`${$restServer}rest/pessoafisica/:id/:action`, undefined, {
				remover: {
					method: 'GET',
					params: {action:'remover'}
				},
				buscarListaPessoaFisica: {
					method: 'POST',
					params: {action:'buscarListaPessoaFisica'},
					isArray: true
				},
				buscarNomeByLogin: {
					method: 'POST',
					params: {action:'buscarNomeByLogin'}
				}
			});
		}

		function pessoaJuridicaFactory($resource, $restServer) {
			return $resource(`${$restServer}rest/pessoajuridica/:id/:action/:documento`, undefined, {
				remover: {
					method: 'GET',
					params: {action:'remover'}
				},
				popup: {
					method: 'GET',
					url: `${$restServer}rest/pessoajuridica/popup/:id/:tipo`
				},
				popupFaturamento: {
                    method: 'GET',
                    url: `${$restServer}rest/pessoajuridica/popup/faturamento/:id/:tipo/:idCl`
                },
				emailhomologador: {
					method: 'GET',
					url: `${$restServer}rest/pessoajuridica/emailhomologador/:id`
				},
				getPorDocumentoNaoFormatado: {
					method: 'GET',
					params: {action:'cnpj'}
				},
				buscarListaPorNomeFantasia: {
					method: 'POST',
					url: `${$restServer}rest/pessoajuridica/buscarListaPorNomeFantasia`,
					isArray: true
				}
			});
		}

		function config(moduleUrl, $stateProvider) {
			$stateProvider
			    .state('clientes', {
            		abstract: true,
            		url: '/comercial/cadastros/clientes',
            		template: '<div ui-view></div>'
            	})
				.state('clientes.list', {
					url: '/listar',
					templateUrl: `${moduleUrl}/list.html`,
					controller: 'ClientesListarCtrl',
					controllerAs: '$ctrl'
				})
				.state('clientes.new', {
					url: '/{objectTipo:int}/novo',
					templateUrl: `${moduleUrl}/new.html`,
					controller: createController,
					controllerAs: '$ctrl'
				})
				.state('clientes.view', {
					url: '/{objectId:int}',
					templateUrl: `${moduleUrl}/view.html`,
					controller: viewController,
					controllerAs: '$ctrl'
				})
				.state('clientes.edit', {
					url: '/{objectId:int}/editar',
					templateUrl: `${moduleUrl}/edit.html`,
					controller: editController,
					controllerAs: '$ctrl'
				});
		}

})();
