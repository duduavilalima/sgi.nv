(function() {
    'use strict';

    angular
        .module('app.notasDebitoCR', ['utils', 'services', 'enumservice', 'autocompleteservice'])
        .constant('moduleUrl', 'partials/contasareceber/notasdebito/html')
        .config(configContasAReceber);

    function configContasAReceber(moduleUrl, $stateProvider) {
        $stateProvider
            .state('car_notadebito', {
                abstract: true,
                url: '/contasareceber/notasdebito',
                template: "<div ui-view></div>",
                data: {
                    modulo: 'Contas a receber',
                    titulo: 'Notas de débito'
                }
            })
            .state('car_notadebito.listar', {
                url: '/listar',
                templateUrl: moduleUrl + '/notasDebitoCR.list.html',
                data: {
                    titulo: 'Notas de débito',
                    subtitulo: 'Pesquisar',
                    modulo: 'Contas a receber'
                },
            })
            .state('car_notadebito.exibir', {
				url: '/{objectId:int}/exibir',
				templateUrl: moduleUrl + '/partials/notasDebitoCR_exibicao.html',
				controller: viewController,
				data: {
					titulo: 'Notas de débito',
					subtitulo: 'Exibição',
					modulo: 'Contas a receber'
				},
			})
			.state('car_notadebito.editar', {
				url: '/{objectId:int}/editar',
				templateUrl: moduleUrl + '/partials/parcela_edicao.html',
				controller: editController,
				data: {
					titulo: 'Notas de débito',
					subtitulo: 'Edição',
					modulo: 'Contas a receber'
				},
			})
			.state('car_notadebito.receber', {
				url: '/{objectId:int}/receber',
				templateUrl: moduleUrl + '/partials/parcela_edicao_abas.html',
				controller: receberController,
				data: {
					titulo: 'Notas de débito',
					subtitulo: 'Receber',
					modulo: 'Contas a receber'
				},
			})
			.state('car_notadebito.imprimir', {
				url: '/{objectId:int}/imprimir',
				templateUrl: 'partials/contasareceber/notasdebito/html/notasDebitoCR.imprimir.html',
				controller: viewController,
				data: {
					subtitulo: 'Imprimir'
				},
			});
    }
})();
