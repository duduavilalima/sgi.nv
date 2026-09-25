angular
    .module('app.categoriacontrato')
    .controller('categoriacontratoDetailCtrl', detailController);

function detailController($scope, $state, $stateParams, DialogConfirm, EnumService, CategoriaContrato) {
    const $ctrl = this;

    init = () => {
        if ($scope.screenMode === 'NEW') {
            newObj();
        } else {
            loadObj();
        }
    };

    loadObj = () => {
        const pCategoriaContrato = CategoriaContrato.get({id: $stateParams.objectId }).$promise;

        pCategoriaContrato.then(function(data) {
            $ctrl.object = data;
            carregarTipoCategoriaContrato();
        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    newObj = () => {
        $ctrl.object = new CategoriaContrato();
        $state.go('categoriacontrato.new');
        carregarTipoCategoriaContrato();
    };

    // Carrega a combo de pesquisa Situação
    carregarTipoCategoriaContrato = () => {
        EnumService.enumValues({ pacote: 'br.com.araujoabreu.sgi.administracao.domain', classe: 'CategoriaContrato', enum: 'TipoCategoriaContrato' })
            .$promise.then(function(data) {
                $ctrl.listtipo = [];
                $ctrl.listtipo.push('');
                $ctrl.listtipo.push(...data);
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });
    };

    /* Inicio da Dialog Remover Categoria Contrato */
    $ctrl.removerCategoriaContrato = function(item) {
        // Chamada do serviço que criará um dialog de confirmação.
        DialogConfirm.show(
            undefined,
            item,
            'Remover categoria de contrato: Confirmação',
            'Deseja realmente remover esta categoria de contrato (esta operação não poderá ser desfeita)?',
            'Confirmar',
            'Cancelar',
            confirmCategoriaContrato,
            cancelCategoriaContrato);
    };

    confirmCategoriaContrato = (object, fnHide) => {
        let pDelete = $ctrl.object.$delete({ id: $ctrl.object.codigo });

        pDelete.then(function(data) {
            fnHide();
            $scope.notification.success('Categoria de Contrato removida.');
            $state.go('categoriacontrato.list', { });
        }).catch(function(response){
            $scope.$emit('crud-critical', 'delete', response);
        });
        // Fecha a Dialog
        fnHide();
    };

    cancelCategoriaContrato = (fnHide) => {
        fnHide();
    };
    /*Fim da Dialog Remover Categoria Contrato*/

    $ctrl.salvar = function() {
        if ($scope.screenMode === 'NEW') {

            const pSave = $ctrl.object.$save();

            pSave.then(function(data){
                $scope.notification.success(undefined, 'Categoria de Contrato inserida com sucesso.');
                $state.go('categoriacontrato.view', { objectId: data.codigo });
            }).catch(function(response){
                $scope.$emit('crud-critical', 'save', response);
            });

        } else if ($scope.screenMode === 'EDIT') {

            const pUpdate = $ctrl.object.$update({ id: $ctrl.object.codigo });

            pUpdate.then(function(data){
                $scope.notification.success(undefined, 'Categoria de Contrato atualizado com sucesso.');
                $state.go('categoriacontrato.view', { objectId: data.codigo });
            }).catch(function(response){
                $scope.$emit('crud-critical', 'save', response);
            });
        }
    };

    $ctrl.editar = function() {
        $state.go('categoriacontrato.edit', { objectId: $ctrl.object.codigo });
    };

    $ctrl.retornar = function() {
        $state.go('categoriacontrato.list', {
        });
    };

    $ctrl.cancelarEdit = function() {
        if ($scope.screenMode === 'NEW') {
            $state.go('categoriacontrato.list');
        } else if ($scope.screenMode === 'EDIT') {
            $state.go('categoriacontrato.view', { objectId: $ctrl.object.codigo });
        }
    };

    init();
}