angular.module('usuariologadoservice', ['services'])

.service('UsuarioLogadoService', ['Usuario', function(Usuario) {
	var vm = this;

	vm.usuario = Usuario.usuarioLogado();

    vm.getUsuarioLogado = function() {
        return vm.usuario;
    };

    vm.possuiTodosPerfisLista = function(listaPerfis) {
        var params = {};
        params.perfis = listaPerfis;
        return Usuario.possuiTodosPerfisLista(params, null);
    };


    vm.possuiUmPerfilLista = function(listaPerfis) {
        var params = {};
        params.perfis = listaPerfis;
        return Usuario.possuiUmPerfilLista(params, null);
    };

    vm.apenasFaturamento = function() {
        return Usuario.apenasFaturamento();
    };
}]);
