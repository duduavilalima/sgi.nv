angular.module('app.usuario')

    .controller('UsuarioListarCtrl', ['$scope', '$controller', '$state', '$mdDialog', '$rootScope', '$timeout', '$filter', 'Usuario', 'UsuarioLogadoService', 'EnumService', function($scope, $controller, $state, $mdDialog, $rootScope, $timeout, $filter, Usuario, UsuarioLogadoService, EnumService) {
        $controller('SearchController', {
            $scope: $scope,
            Resource: Usuario
        });
        $controller('GridController', {
            $scope: $scope,
            Resource: Usuario
        });

        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;

        $scope.usuario = UsuarioLogadoService.getUsuarioLogado();

        var perfisAcesso = ['admin-usuario', 'admin'];
        this.possuiTodosPerfis = UsuarioLogadoService.possuiTodosPerfisLista(perfisAcesso);
        this.possuiUmPerfil = UsuarioLogadoService.possuiUmPerfilLista(perfisAcesso);

        /* CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */
        var columnDefs = [{
                name: 'login',
                field: 'login',
                width: '20%',
                enableSorting: false,
                sortFieldString: true,
                funcao: ['exibir', 'row.entity.codigo']
            },
            {
                name: 'últimoAcesso',
                field: 'dataUltimoAcesso',
                cellFilter: 'formatacaoData',
                enableSorting: false,
                sortFieldString: false,
                tooltip: 'field'
            },
            {
                //				name:'Opções', acoes: ['exibir', 'editar', 'imprimir', 'excluir'], width: '5%', pinnedRight: false, enableSorting: false
                name: 'Opções',
                acoes: ['exibir'],
                width: '5%',
                pinnedRight: false,
                enableSorting: false
            }
        ];

        $scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'usuário', 'e');

        $scope.setDefaultParams = function() {
            $scope.paginationOptions.sortField = 'login';
            $scope.paginationOptions.sortFieldString = true;
            $scope.paginationOptions.sortDir = 'asc';
            //        $scope.paginationOptions.pageSize = 5;
        }
        $scope.setDefaultParams();

        $scope.getPage = function(page) {
            $scope.getPageGrid($scope.gridPesquisa, page);
        };

        $scope.getSortedPage = function(col) {
            $scope.paginationOptions.sortField = col.name;
            $scope.paginationOptions.sortFieldString = col.sortFieldString;
            $scope.paginationOptions.sortDir = col.sort.direction;
            $scope.getPageGrid($scope.gridPesquisa, $scope.paginationOptions.pageNumber);
        };

        /* FIM DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */
        $scope.buscar = function() {
            $rootScope.loadingProgress = true;
            $scope.setDefaultParams();
            this.getPage(1);
        };

        $scope.$on('crud-success', function(event, action, data) {});

        $scope.$watch('result', function() {
            $rootScope.loadingProgress = false;
        });

        $scope.listar = function() {
            $state.go('usuario.listar');
        };

        $scope.exibir = function(codigo) {
            console.log('exibir' + codigo);
            $state.go('usuario.exibir', {
                objectId: codigo
            });
        };

        $scope.editar = function(codigo) {
            console.log('editar' + codigo);
            $state.go('usuario.editar', {
                objectId: codigo
            });
        };

        $scope.excluir = function(codigo) {
            console.log('excluir' + codigo);
            $state.go('usuario.exibir', {
                objectId: codigo
            });
        };

        $scope.imprimir = function(codigo) {
            console.log('imprimir');
            $state.go('usuario.exibir', {
                objectId: codigo
            });
        };

        $scope.reset = function() {
            $scope.result = null;
            $scope.clear();
        };

    }])

    .controller('UsuarioCadastroCtrl', ['$scope', '$controller', '$state', '$mdDialog', '$rootScope', '$timeout', '$filter', 'Usuario', 'UsuarioLogadoService', 'EnumService', function($scope, $controller, $state, $mdDialog, $rootScope, $timeout, $filter, Usuario, UsuarioLogadoService, EnumService) {
        /*$controller('CrudController', {
            $scope: $scope,
            Resource: Usuario
        });*/

        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;

        $scope.usuario = UsuarioLogadoService.getUsuarioLogado();

        $scope.screenMode = $state.current.data.screenMode;

        if ($scope.screenMode === 'NEW') {
            //		TRATAR NOVO
        } else if ($scope.screenMode === 'EDIT' || $scope.screenMode === 'VIEW') {
            //      TRATAR EDIÇÃO / EXIBIÇÃO
        };

    }])
