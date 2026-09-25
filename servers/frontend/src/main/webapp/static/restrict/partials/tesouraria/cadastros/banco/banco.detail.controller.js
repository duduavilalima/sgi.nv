(function() {

    'use strict'

    angular
        .module('app.banco')
        .controller('BancoDetailCtrl', detailController);

    function detailController($scope, $state, DialogConfirm, $rootScope, Banco, $stateParams) {

        $scope.object = {};

        function init() {
            $scope.modulo = $state.current.data.modulo;
            $scope.titulo = $state.current.data.titulo;
            $scope.subtitulo = $state.current.data.subtitulo;

            $scope.action = false;

            if ($scope.screenMode === 'NEW') {
                newObj();
            } else {
                loadObj();
            }
        }

        function newObj() {
            $scope.object = new Banco();
            $rootScope.loadingProgress = true;
            $scope.action = true;
            Banco.get().$promise
                .then(function(data, status, headers, config) {
                    $scope.action = false;
                    $rootScope.loadingProgress = false;
                    $scope.object.situacao = 'Ativo';
                }, function(response) {
                    $scope.action = false;
                    $rootScope.loadingProgress = false;
                    $scope.$emit('crud-critical', 'load', response);
                });
        }

        function loadObj() {
            $rootScope.loadingProgress = true;
            $scope.action = true;

            Banco.get({
                id: $stateParams.objectId
            }).$promise.then(function(data) {
                $scope.object = angular.copy(data);
                $rootScope.loadingProgress = false;
                $scope.action = false;
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
                $rootScope.loadingProgress = false;
                $scope.action = false;
            });
        }

        $scope.remover = function(event) {
            DialogConfirm.show(
                event,
                $scope.object,
                'Remover banco: Confirmação',
                'Deseja realmente remover esta banco (esta operação não poderá ser desfeita)?',
                'Confirmar',
                'Cancelar',
                confirmRemover,
                cancelRemover);
        };

        function confirmRemover(object, fnHide) {
            object.$delete({
                id: object.codigo
            }).then(function(data) {
                fnHide();
                $state.go('banco.list');
                $scope.notification.success("Banco removido com sucesso.");
            }).catch(function(response) {
                fnHide();
                console.error('crud-critical', 'dialog', response);
                $scope.$emit('crud-critical', 'dialog', response);
            });
        }

        function cancelRemover(fnHide) {
            fnHide();
        }

        $scope.salvar = function() {
            $scope.action = true;
            $rootScope.loadingProgress = true;
            if ($scope.screenMode === 'NEW') {
                $scope.object.$save().then(function(data) {
                    $scope.notification.success("Novo", "Banco inserido com sucesso.");
                    $state.go('banco.view', {
                        objectId: data.codigo
                    });
                    $scope.action = false;
                    $rootScope.loadingProgress = false;
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'save', response);
                    $scope.action = false;
                    $rootScope.loadingProgress = false;
                });
            } else if ($scope.screenMode === 'EDIT') {
                $scope.object.$update({
                    id: $scope.objectId
                }).then(function(data) {
                    $scope.notification.success("Edição", "Banco atualizado com sucesso.");
                    $state.go('banco.view', {
                        objectId: data.codigo
                    });
                    $scope.action = false;
                    $rootScope.loadingProgress = false;
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'save', response);
                    $scope.action = false;
                    $rootScope.loadingProgress = false;
                });
            }
        };

        //Função também serve para o botão retornar
        $scope.cancelar = function() {
            if ($scope.screenMode !== 'EDIT') {
                $state.go('banco.list');
            } else {
                $state.go('banco.view', {
                    objectId: $scope.object.codigo
                });
            }
        };

        $scope.editar = function() {
            $state.go('banco.edit', {
                objectId: $scope.object.codigo
            });
        };

        init();
    }
})();