(function() {
    'use strict';
    angular
        .module('app.notasDebitoCR')
        .factory('ParcelaService', function($rootScope, Parcela, $mdDialog) {
            const catchError = (response) => {
                console.error(response);
                $rootScope.$emit('crud-critical', 'load', response);
            };

            let service = {};

            function DialogControllerObservacao($scope, $mdDialog, objNotaSaidaParcela, notification, title) {
                $scope.notification = notification;
                $scope.object = {};
                $scope.object.observacoes = objNotaSaidaParcela.observacoesImpressao;
                $scope.action = false;
                $scope.title = title ? title : "Impressão da parcela - Observações";

                $scope.hide = function () {
                    $scope.action = false;
                    $mdDialog.hide();
                };
                $scope.cancel = function () {
                    $scope.action = false;
                    $mdDialog.cancel();
                };

                $scope.salvar = function () {
                    $scope.action = true;
                    objNotaSaidaParcela.observacoesImpressao = $scope.object.observacoes;
                    Parcela.salvarObservacao(objNotaSaidaParcela).$promise
                    .then(() => {
                        $scope.action = false;
                        $mdDialog.cancel();
                    })
                    .catch(catchError);
                };
            }

            service.dialogObservacao = (ev, object, notification, title) => {
                $mdDialog.show({
                    controller: DialogControllerObservacao,
                    locals: {
                        objNotaSaidaParcela: object,
                        notification: notification,
                        Parcela: Parcela,
                        title: title
                    },
                    templateUrl: 'partials/contasareceber/notasdebito/html/partials/dialogs/imprimir_observacoes.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    fullscreen: false
                });
            };

            return service;
        });

})();
