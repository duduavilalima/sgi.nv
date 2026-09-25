(function() {
	'use strict';

	angular
		.module('app.contratos', [])

		.constant('moduleUrl', 'partials/comercial/contratos')

		.factory('MotivoRetornoContrato', motivoRetornoContratoFactory)

		.factory('Contrato', contratosFactory)

		.factory('ContratoContato', contratoContatoFactory)

		.factory('ContratoEndereco', contratoEnderecoFactory)

		.factory('ContratoAnexo', contratoAnexoFactory)

		.config(config);

		/* MOTIVO RETORNO CONTRATO */
		 function motivoRetornoContratoFactory($resource, $restServer) {
			return $resource(`${$restServer}rest/motivo/retorno/contrato/:id/:action`, null, {

			});
		}

		/* CONTRATO */
		function contratosFactory($resource, $restServer) {
			return $resource(`${$restServer}rest/contrato/:prefix/:id/:action`, null, {
				cidadeUnidade: {
					 method: 'POST',
					 url: `${$restServer}rest/contrato/busca/cidadeunidade`,
					 cache: true
				 },
				cidadeUnidadeMatriz: {
					 method: 'POST',
					 url: `${$restServer}rest/contrato/busca/cidadeunidadematriz`,
					 cache: true
				 },
				clienteContrato: {
					method: 'GET',
					params: {'action' : 'cliente'},
					cache: true
				},
				reativarContrato: {
					method: 'GET',
					params: {'action' : 'reativarcontrato'},
					cache: true
				},
				encerrarContrato: {
					method: 'GET',
					params: {'action' : 'encerrarcontrato'},
					cache: true
				},
				paralisarContrato: {
					method: 'GET',
					params: {'action' : 'paralisarcontrato'},
					cache: true
				},
				buscarEnderecos: {
					method: 'GET',
					params: {'action':'buscarenderecos'},
					isArray: true
				},
				buscarCentroLucro: {
					method: 'POST',
					params: {'action':'buscarcentrolucro'},
					isArray: true
				},
				buscarAditivos: {
					method: 'GET',
					params: {'action':'buscaraditivos'},
					isArray: true
				},
				buscarAnexos: {
					method: 'GET',
					params: {'action':'buscaranexos'},
					isArray: true
				},
				buscarContato: {
					method: 'GET',
					params: {'action':'buscarcontato'},
					isArray: true
				},
				buscarTipoLogradouro: {
					method: 'GET',
					params: {'action':'buscartipologradouro'},
					isArray: true
				},
				consultarDocumento:{
					method: 'POST',
                	params: {'action':'consultardocumento'}
				},
				totalCentrosFinanceiros:{
					method: 'POST',
                	params: {'action':'totalcentrosfinanceiros'}
				},
				//FIXME: Verificar se está sendo utilizado, pois já existe um rest para remoção do centro financeiro no 'centrosfinanceiros.modulo.js'
				removeCentroFinanceiro: {
					method: 'GET',
					params: {'action':'removecentrofinanceiro'}
				},
				calcularPrazoContrato:{
					method: 'POST',
					params: {'action':'calcularprazocontrato'}
				},
				atualizarTotais: {
					method: 'GET',
					params: {'action':'atualizatotais'}
				},
			});
		}

		/*
        	CONTRATO CONTATO
        */
		function contratoContatoFactory($resource, $restServer) {
			return $resource(`${$restServer}rest/contratocontato/:id/:action`, undefined, {
				remover: {
					method: 'GET',
					params: {'action':'remover'}
				},
				buscarRelacionamento: {
					method: 'GET',
					params: {'action':'buscarrelacionamento'},
					isArray: true
				},
				listaByCodContrato: {
					method: 'GET',
					url: `${$restServer}rest/contratocontato/listaByCodContrato/:id`,
					isArray: true
				}
			});
		}

		/*
			CONTRATO ENDEREÇO
		*/
		function contratoEnderecoFactory($resource, $restServer) {
			return $resource(`${$restServer}rest/contratoendereco/:id/:action`, undefined, {
				remover: {
					method: 'GET',
					params: {'action':'remover'}
				}
			});
		}

		/*
        	CONTRATO ANEXO
        */
		function contratoAnexoFactory($resource, $restServer) {
			return $resource(`${$restServer}rest/contratoanexo/:id/:action`, undefined, {
				remover: {
					method: 'GET',
					params: {'action':'remover'}
				},
				baixaranexo: {
					method: 'GET',
					params: {'action':'baixaranexo'}
				},
				verificaranexo: {
					method: 'GET',
					params: {'action':'verificaranexo'}
				}

			});
		}

		function config(moduleUrl, $stateProvider) {
			$stateProvider
				.state('contratos', {
					abstract: true,
					url: '/comercial/contratos',
					template: "<div ui-view></div>"
				})
				.state('contratos.list', {
					url: '/listar',
					templateUrl: `${moduleUrl}/html/list.html`,
					controller: 'ContratosListarCtrl',
					controllerAs: '$ctrl',
					data: {
						titulo: 'Contratos',
						modulo: 'Comercial',
						subtitulo: ''
					}
				})
				.state('contratos.novo', {
					url: '/novo',
					templateUrl: `${moduleUrl}/html/new.html`,
					controller: createController,
					data: {
						titulo: 'Contratos',
						modulo: 'Comercial',
						subtitulo: 'Novo'
					}
				})
				.state('contratos.view', {
					url: '/{objectId:int}',
					templateUrl: `${moduleUrl}/html/view.html`,
					controller: viewController,
					data: {
						titulo: 'Contratos',
						modulo: 'Comercial',
						subtitulo: ''
					}
				})
				.state('contratos.edit', {
					url: '/{objectId:int}/editar',
					templateUrl: `${moduleUrl}/html/edit.html`,
					controller: editController,
					data: {
						titulo: 'Contratos',
						modulo: 'Comercial',
						subtitulo: 'Editar'
					}
				});
		}

})();
