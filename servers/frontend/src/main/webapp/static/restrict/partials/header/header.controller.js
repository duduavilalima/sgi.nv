angular.module('SGIApp')

    .controller('ToolbarController', ['$scope', '$state', '$rootScope', '$mdSidenav', 'msNavFoldService', 'UsuarioLogadoService', function($scope, $state, $rootScope, $mdSidenav, msNavFoldService,
        UsuarioLogadoService) {
        var vm = this;

        // Data
        $rootScope.global = {
            search: ''
        };

        $scope.linkHome = function() {
            $state.go('home');
        };

        $scope.linkSGI = function() {
            window.open('https://sgi.araujoabreu.com.br/sgi/home.seam', '_blank');
        };

        // BOTÃO SGI 1.0
        $scope.apenasFaturamento = {};

        UsuarioLogadoService.apenasFaturamento().$promise.then(function(data) {
            if (data.value) {
                $scope.apenasFaturamento = true;
            } else {
                $scope.apenasFaturamento = false;
            }
        }).catch(function(response) {
            console.error(response);
        });

        $scope.logout = function() {
            localStorage.removeItem("menu");
            var urlLogout = '/sgi/nv/logout';
            // var urlHome = '../app/main.html';
            // function goHome(response) { $window.location.href = urlHome; }

            /* NOTE SGINV-222:
             * O redirecionamento via simples `$window.location.href` funciona apenas quando a aplicação
             * é implantada no ambiente de produção (docker). Para os deploys locais nas máquinas de desenvolvimento,
             * um POST é necessário; do contrário, o CAS retornará status 404 com redirecionamento (via HTML) para a
             * página inicial da aplicação `/sgi/nv`. Até que uma melhor solução seja encontrada (alguma que utilize
             * redirecionamento simples via `$window.location.href`, este método de chamada AJAX (GET com fallback
             * em POST) será utilizado.
             * É normal um erro ser mostrado no `console.log` do cliente, se ocorrer, mas não é um impedimento ao
             * processo de logout. Ademais, o cliente não verá o erro (exceto quando ele deliberadamente abrir as
             * ferramentas de desenvolvedor).
             */
            window.location.href = urlLogout;
            //    	$http.get(urlLogout)
            //    	.then(function (success) {
            //			localStorage.setItem("last_address_logout", window.location.href);
            //    		goHome(success);
            //    	}, function (error) {
            //			// env:dev
            //			localStorage.setItem("last_address_logout", window.location.href);
            //    		$http.post(urlLogout).then(goHome, goHome);
            //    	});
        };

        vm.usuario = UsuarioLogadoService.usuario;

        // Methods
        vm.toggleSidenav = toggleSidenav;
        vm.toggleNavigationSidenavFold = toggleNavigationSidenavFold;

        //////////

        /**
         * Toggle sidenav
         *
         * @param sidenavId
         */
        function toggleSidenav(sidenavId) {
            $mdSidenav(sidenavId).toggle();
        }

        /**
         * Toggle navigation sidenav fold
         */
        function toggleNavigationSidenavFold(event) {
            event.preventDefault();

            msNavFoldService.toggleFold();
        }
    }]);