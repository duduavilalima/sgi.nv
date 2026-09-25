angular.module('app.menu', ['usuariologadoservice'])

.controller('MenuCtrl', ['$scope', '$controller', '$state', 'UsuarioLogadoService', 'MenuFactory', function($scope, $controller, $state, UsuarioLogadoService, MenuFactory) {

	$scope.menu = null;

	// FIXME: Passar para serviço
	const decodeMenu = (data) => {
		return JSON.parse(window.atob(data));
	};

	// FIXME: Passar para serviço
	const encodeMenu = (data) => {
		return window.btoa(JSON.stringify(data));
	};

	// FIXME: Passar para serviço
	if (localStorage.getItem("menu")) {
		$scope.menu = decodeMenu(localStorage.getItem("menu"));
	} else {
		MenuFactory.get().$promise.then(function(data) {
			$scope.menu = data;
			localStorage.setItem("menu", encodeMenu(data));
		}, function (error) {
			console.error(error);
		});
	}

	var perfisFaturamento = ['faturamento-analista'];
	this.possuiTodosPerfisFaturamento = UsuarioLogadoService.possuiTodosPerfisLista(perfisFaturamento);
	this.possuiUmPerfilFaturamento = UsuarioLogadoService.possuiUmPerfilLista(perfisFaturamento);

	var perfisControladorFiscal = ['controlador-fiscal'];
	this.possuiTodosPerfisControladorFiscal = UsuarioLogadoService.possuiTodosPerfisLista(perfisControladorFiscal);
	this.possuiUmPerfilControladorFiscal = UsuarioLogadoService.possuiUmPerfilLista(perfisControladorFiscal);

	var perfisAdminUsuario = ['admin-usuario'];
	this.possuiTodosPerfisAdminUsuario = UsuarioLogadoService.possuiTodosPerfisLista(perfisAdminUsuario);
	this.possuiUmPerfilAdminUsuario = UsuarioLogadoService.possuiUmPerfilLista(perfisAdminUsuario);

	var perfisOperadorTesouraria = ['operador-tesouraria'];
	this.possuiTodosPerfisOperadorTesouraria = UsuarioLogadoService.possuiTodosPerfisLista(perfisOperadorTesouraria);
	this.possuiUmPerfilOperadorTesouraria = UsuarioLogadoService.possuiUmPerfilLista(perfisOperadorTesouraria);

	var perfisFinanceiroDiretor = ['financeiro-diretor'];
	this.possuiTodosPerfisFinanceiroDiretor = UsuarioLogadoService.possuiTodosPerfisLista(perfisFinanceiroDiretor);
	this.possuiUmPerfilFinanceiroDiretor = UsuarioLogadoService.possuiUmPerfilLista(perfisFinanceiroDiretor);

	var perfisTesouraria = ['operador-tesouraria'];
	this.possuiTodosPerfisTesouraria = UsuarioLogadoService.possuiTodosPerfisLista(perfisTesouraria);
	this.possuiUmPerfilTesouraria = UsuarioLogadoService.possuiUmPerfilLista(perfisTesouraria);

	var perfisAdmnistracao = ['admin'];
    this.possuiTodosPerfisAdministracao = UsuarioLogadoService.possuiTodosPerfisLista(perfisAdmnistracao);
    this.possuiUmPerfilAdministracao = UsuarioLogadoService.possuiUmPerfilLista(perfisAdmnistracao);
}])

.factory('MenuFactory', menuFactory);

function menuFactory($resource, $restServer) {
	return $resource($restServer + 'rest/menu/:id/:action', null, {
		get: {
			method: 'GET',
			isArray: true
		}
	});
}
