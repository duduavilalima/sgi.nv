(function(){

	'use strict'

	angular.module('app.importarconciliacao', [])

	.constant('moduleUrl', 'partials/tesouraria/importar-conciliacao')

	.factory('ConciliacaoBancaria', ConciliacaoBancariaFactory)

	.factory('ConciliacaoLancamento', ConciliacaoLancamentoFactory)

	.config(config);

	function ConciliacaoBancariaFactory($resource, $restServer) {
		return $resource($restServer + 'rest/tesouraria/conciliacaobancaria/:id/:action', null, {
			validarunicidade: {
				method: 'POST',
				params: {'action':'validarunicidade'}
			},
			pesquisarelacaobancaria: {
				method: 'POST',
				params: {'action':'pesquisarelacaobancaria'},
				isArray: true
			},
			salvardados: {
				method: 'POST',
				params: {'action':'salvardados'}
			}
		});
	};

	function ConciliacaoLancamentoFactory($resource, $restServer) {
		return $resource($restServer + 'rest/tesouraria/conciliacaolancamento/:id/:action', null, {
			getlancamentosassociados: {
				method: 'POST',
				params: {'action':'getlancamentosassociados'},
				isArray: true
			},
			getchequeassociado: {
				method: 'PUT',
				params: {'action':'getchequeassociado'}
			}
		});
	};

	function config(moduleUrl, $stateProvider) {
		$stateProvider
			.state('importarconciliacao', {
				abstract: true,
				url: '/tesouraria/importarconciliacao',
				template: "<div ui-view></div>"
			})
			.state('importarconciliacao.importar', {
				url: '/importar',
				templateUrl: moduleUrl + '/importar-conciliacao.detail.html',
				controller: 'ImportarConciliacao',
				data: {
					titulo: 'Importar Conciliação Bancária',
					modulo: 'Tesouraria',
					subtitulo: ''
				}
			});
		}
})();
