angular
    .module('app.formacontratacao')

    .controller('formacontratacaoDetailCtrl', detailController);


function detailController($scope, $state, $stateParams, FormaContratacao, EnumService, DialogConfirm) {
    const $ctrl = this;
    const init = () => {
        $ctrl.modulo = $state.current.data.modulo;
        $ctrl.titulo = $state.current.data.titulo;

        $ctrl.object = undefined;

        if ($scope.screenMode === 'NEW') {
            newObj();
        } else {
            loadObj();
        }
    };

    const loadObj=() => {
        let pFormaContratacao = FormaContratacao.get({
            id: $stateParams.objectId
        }).$promise;

        pFormaContratacao.then((data) => {
            $ctrl.object = data;

            if ($scope.screenMode === 'EDIT') {
                initCampoTipo();

            }

        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    const newObj=() => {
        $ctrl.listTipo = [];

        $ctrl.object = new FormaContratacao();
        $state.go('formacontratacao.new');
        initCampoTipo();
    };

    const initCampoTipo=() => {
        let pTiposContratacoes = EnumService.enumValues({
            pacote: 'br.com.araujoabreu.sgi.administracao.domain',
            classe: 'FormaContratacao',
            enum: 'TipoContratacao'
        }).$promise;

        pTiposContratacoes.then((data) => {
            $ctrl.listTipos = [];
            $ctrl.listTipos.push('');
            angular.forEach(data, (value) => {
                $ctrl.listTipos.push(value);
            });

        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    $ctrl.editar = () => {
        $state.go('formacontratacao.edit', {
            objectId: $ctrl.object.codigo
        });
    };

    $ctrl.salvar = () => {
        if ($scope.screenMode === 'NEW') {
            $ctrl.object.codigo = undefined;
            let pSave = $ctrl.object.$save();

            pSave.then((data) => {
                $scope.notification.success("Novo", "Forma de Contratação inserida com sucesso.");
                $state.go('formacontratacao.view', {
                    objectId: data.codigo
                });
            }).catch((response) => {
                $scope.$emit('crud-critical', 'save', response);
            });

        } else if ($scope.screenMode === 'EDIT') {
            let pUpdate = $ctrl.object.$update({
                id: $ctrl.object.codigo
            });

            pUpdate.then((data) => {
                $scope.notification.success("Edição", "Forma de Contratação atualizada com sucesso.");
                $state.go('formacontratacao.view', {
                    objectId: data.codigo
                });
            }).catch((response) => {
                $scope.$emit('crud-critical', 'save', response);
            });

        }
    };

    $ctrl.cancelar = () => {
        if ($scope.screenMode === 'NEW') {
            $state.go('formacontratacao.list');
        } else if ($scope.screenMode === 'EDIT') {
            $state.go('formacontratacao.view', {
                objectId: $ctrl.object.codigo
            });
        }
    };

    $ctrl.retornar = () => {
        $state.go('formacontratacao.list');
    };

    $ctrl.remover = (ev) => {
        let tituloDialog = "Forma de Contratação - Remover: Confirmação";
        let msgDialog = "Deseja realmente remover esta forma de contratação (esta operação não poderá ser desfeita)?";
        let tituloBotaoConfirmar = "CONFIRMAR";
        let tituloBotaoCancelar = "CANCELAR";

        let funcaoCancelar = (fnHide) => {
            fnHide();
        };

        let funcaoConfirmar = (object, fnHide) => {
            let pFormaContratacao = FormaContratacao.delete({
                id: object.codigo
            }).$promise;

            pFormaContratacao.then((data) => {

                let title = 'Remover Forma de Contratação';
                let message = 'Forma de Contratação ' + object.nome + ' removido com sucesso.';
                $scope.notification.success(title, message);

                fnHide();
                $state.go('formacontratacao.list');

            }).catch((response) => {
                console.error('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
            });
        };

        DialogConfirm.show(ev, $ctrl.object, tituloDialog, msgDialog, tituloBotaoConfirmar, tituloBotaoCancelar, funcaoConfirmar, funcaoCancelar);
    };

    init();

}