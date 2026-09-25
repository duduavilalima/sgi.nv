(function () {
    'use strict';

    angular
        .module('app.funcionalidades')

        .controller('FuncionalidadesDetailCtrl', detailController);

    function detailController($scope, $state, $stateParams, Funcionalidades) {

        const init = () => {
            $scope.modulo = $state.current.data.modulo;
            $scope.titulo = $state.current.data.titulo;
            $scope.object = undefined;
            $scope.searchTextCidade = "";

            if ($scope.screenMode === 'NEW') {
                newObj();
            } else {
                loadObj();
            }
        };

        const catchError = (response) => {
            console.error(response);
            $scope.$emit('crud-critical', 'load', response);
        };

        const loadObj = () => {
            const thenFN = data => {
                $scope.object = data;
                $scope.calcularPrazo();
                if ($scope.screenMode === 'EDIT') { }
            };

            const promise = Funcionalidades.get({ id: $stateParams.objectId }).$promise;

            return Promise.resolve()
                .then(promise)
                .then(thenFN)
                .catch(catchError);
        };

        const newObj = () => {
            $scope.object = new Funcionalidades();
            $scope.object.funcionalidades = {};
        };

        init();
    }
})();