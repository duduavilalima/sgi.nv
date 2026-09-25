(function() {
	'use strict';

	angular
		.module('app.conta_corrente', [])

		.constant('htmlUrl', 'partials/tesouraria/cadastros/conta_corrente/html')

		.filter('zerosAEsquerda', function () {
			return function (n, len) {
				var num = parseInt(n, 10);
				len = parseInt(len, 10);
				if (isNaN(num) || isNaN(len)) {
					return n;
				}
				num = ''+num;
				while (num.length < len) {
					num = '0'+num;
				}
				return num;
			};
		})

		.factory('ContaCorrente', contaCorrenteFactory)

		.config(config);

		function contaCorrenteFactory($resource, $restServer) {
			return $resource($restServer + 'rest/contacorrente/:id/:action', null, {
				  buscarChequesContaCorrente: {
					  method: 'GET',
					  params: {'action':'chequescontacorrente'},
					  isArray: true
				  },
                  buscarChequesContaCorrenteMatriz: {
                      method: 'GET',
                      params: {'action':'chequescontacorrentematriz'},
                      isArray: true
                  },
                  obterSaldos: {
                      method: 'POST',
                      params: {'action':'saldos'}
                  },
				  obterContas: {
					 method: 'POST',
					 params: {'action':'contascorrentes'},
					 isArray: true
				  }
				});
		};

		function config(htmlUrl, $stateProvider) {
			$stateProvider
				.state('conta_corrente', {
					abstract: true,
					url: '/tesouraria/contacorrente',
					template: "<div ui-view></div>"
				})
				.state('conta_corrente.list', {
					url: '/listar',
					templateUrl: htmlUrl + '/conta_corrente.listar.html',
					controller: 'ContaCorrenteListarCtrl',
					data: {
						titulo: 'Contas correntes',
						modulo: 'Tesouraria',
						subtitulo: 'Pesquisar'
					}
				})
				.state('conta_corrente.new', {
					url: '/novo',
					templateUrl: htmlUrl + '/conta_corrente.detail.html',
					controller: createController,
					data: {
						titulo: 'Conta corrente',
						modulo: 'Tesouraria',
						subtitulo: 'Novo'
					}
				})
				.state('conta_corrente.view', {
					url: '/{objectId:int}',
					templateUrl: htmlUrl + '/conta_corrente.detail.html',
					controller: viewController,
					data: {
						titulo: 'Conta corrente',
						modulo: 'Tesouraria',
						subtitulo: ''
					}
				})
				.state('conta_corrente.edit', {
					url: '/{objectId:int}/editar',
					templateUrl: htmlUrl + '/conta_corrente.detail.html',
					controller: editController,
					data: {
						titulo: 'Conta corrente',
						modulo: 'Tesouraria',
						subtitulo: 'Editar'
					}
				});
		};

})();
