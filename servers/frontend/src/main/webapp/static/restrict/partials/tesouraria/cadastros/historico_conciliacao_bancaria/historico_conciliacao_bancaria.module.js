(function() {
	'use strict';

	angular
		.module('app.historicoconciliacaobancaria', [])

		.constant('moduleUrl', 'partials/tesouraria/cadastros/historico_conciliacao_bancaria')

		.factory('HistoricoConciliacaoBancaria', historicoConciliacaoBancariaFactory)

		.factory('HistoricoConciliacaoContaContabil', historicoConciliacaoContaContabilFactory)

		.config(config);

		function historicoConciliacaoBancariaFactory($resource, $restServer) {
			return $resource($restServer + 'rest/tesouraria/historicoconciliacaobancaria/:id/:action', null, {
				remover: {
					method: 'GET',
					params: {'action':'remover'}
				},
				buscarHistoricoCCC:{
					method: 'GET',
                    params: {'action':'buscarhistoricoccc'},
                    isArray: true
				},
                buscarcontacontabil: {
                    method: 'POST',
                    params: {'action':'buscarcontacontabil'},
                    isArray: true
                }
			});
		};

		function historicoConciliacaoContaContabilFactory($resource, $restServer) {
			return $resource($restServer + 'rest/tesouraria/historicoconciliacontacontabil/:id/:action', null, {
				get: {
					method: 'GET'
				},
				create: {
					method: 'GET',
					params: {'action':'create'}
				},
				update: {
					method: 'PUT'
				},
				qbe: {
					method: 'POST',
					params: {'action':'busca'}
				},
				remover: {
					method: 'GET',
					params: {'action':'remover'}
				}
			});
		};

		function config(moduleUrl, $stateProvider) {
			$stateProvider
				.state('historico_conciliacao_bancaria', {
					abstract: true,
					url: '/tesouraria/historicoconciliacaobancaria',
					template: "<div ui-view></div>"
				})
				.state('historico_conciliacao_bancaria.list', {
					url: '/listar',
					templateUrl: moduleUrl + '/historico_conciliacao_bancaria.listar.html',
					controller: 'HistoricoConciliacaoBancariaListarCtrl',
					data: {
						titulo: 'Históricos de conciliação bancária',
						modulo: 'Tesouraria',
						subtitulo: 'Pesquisar'
					}
				})
				.state('historico_conciliacao_bancaria.new', {
					url: '/novo',
					templateUrl: moduleUrl + '/historico_conciliacao_bancaria.detail.html',
					controller: createController,
					data: {
						titulo: 'Históricos de conciliação bancária',
						modulo: 'Tesouraria',
						subtitulo: 'Novo'
					}
				})
				.state('historico_conciliacao_bancaria.view', {
					url: '/{objectId:int}',
					templateUrl: moduleUrl + '/historico_conciliacao_bancaria.detail.html',
					controller: viewController,
					data: {
						titulo: 'Histórico de conciliação bancária',
						modulo: 'Tesouraria',
						subtitulo: ''
					}
				})
				.state('historico_conciliacao_bancaria.edit', {
					url: '/{objectId:int}/editar',
					templateUrl: moduleUrl + '/historico_conciliacao_bancaria.detail.html',
					controller: editController,
					data: {
						titulo: 'Histórico de conciliação bancária',
						modulo: 'Tesouraria',
						subtitulo: 'Editar'
					}
				});
		};

})();
