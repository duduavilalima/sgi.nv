angular
    .module('app.rubricas')
    .controller('RubricasCtrl', rubricasCtrlFn);

function rubricasCtrlFn($scope, $state, $stateParams, Rubricas, $rootScope) {
    const $ctrl = this;

    $ctrl.action = false;
    $ctrl.rubricasPaiList = undefined;

    $ctrl.situacaoList = [{
            code: 'ATIVA',
            desc: 'Ativa'
        },
        {
            code: 'INATIVA',
            desc: 'Inativa'
        }
    ];

    $ctrl.tipoList = [{
            code: 'RECEBIMENTO',
            desc: 'Recebimento'
        },
        {
            code: 'CUSTO',
            desc: 'Custo'
        },
        {
            code: 'DESPESA_CENTRO_CUSTOS',
            desc: 'Despesa de centro de custos (não disponível para CL, somente gerências, diretorias e empresas)'
        },
        {
            code: 'RESULTADO',
            desc: 'Resultado (soma dos recebimentos e custos)'
        }
    ];

    $ctrl.operacaoList = [{
            code: 'SOMA',
            desc: 'Soma'
        },
        {
            code: 'SUBTRACAO',
            desc: 'Subtração'
        }
    ];

    let obterListaDespesasSimplesAtivas = () => {
        return new Promise((resolve, reject) => {
            Rubricas.obterListaDespesasSimplesAtivas().$promise
                .then(response => {
                    $ctrl.rubricasPaiList = response;
                    resolve(true);
                })
                .catch(catchError);
        });
    };

    let catchError = (response) => {
        $rootScope.loadingProgress = false;
        $ctrl.action = false;
        $scope.$emit('crud-critical', 'load', response);
        reject(false);
    };

    function loadObj() {
        let load = (resolve, reject) => {
            $ctrl.action = true;
            $ctrl.object = new Rubricas();
            $ctrl.objectId = $stateParams.objectId;
            if (!$ctrl.objectId) {
                catchError();
            } else {
                Rubricas.get({
                        id: $stateParams.objectId
                    }).$promise
                    .then(function(data) {
                        $ctrl.action = false;
                        $ctrl.object = data;
                        resolve(true);
                    });
            }
        };
        return Promise.resolve().then(obterListaDespesasSimplesAtivas).then(load).catch(catchError);
    }

    function newObj() {
        $rootScope.loadingProgress = true;
        let newObj = function(data, status, headers, config) {
            $rootScope.loadingProgress = false;
            $ctrl.object = new Rubricas();
            $ctrl.object = Object.assign(data, $ctrl.object);
            $scope.$apply();
        };
        return Promise.resolve()
            .then(obterListaDespesasSimplesAtivas)
            .then(() => Rubricas.get().$promise)
            .then(newObj)
            .catch(catchError);
    }

    // FRAMEWORK NÃO LANÇA SUCESSO PARA CRIAÇÃO DE OBJETO NOVO //
    if ($scope.screenMode === 'NEW') {
        newObj();
    } else if ($scope.screenMode === 'EDIT' || $scope.screenMode === 'VIEW') {
        loadObj();
    }

    $ctrl.salvar = () => {
        if (!$ctrl.object.pai || !$ctrl.object.pai.codigo) {
            delete $ctrl.object.pai;
        }
        $ctrl.action = true;
        $rootScope.loadingProgress = true;
        let promise = {};
        if ($ctrl.object.codigo) {
            promise = $ctrl.object.$update({
                id: $ctrl.object.codigo
            });
        } else {
            promise = $ctrl.object.$save();
        }
        promise.then((response) => {
            $rootScope.loadingProgress = false;
            $ctrl.action = false;
            $state.go('rubricas.exibir', {
                objectId: response.codigo
            });
        }).catch(catchError);
    };

    $ctrl.editar = () => {
        $state.go('rubricas.editar', {
            objectId: $stateParams.objectId
        });
    };

    $ctrl.cancelar = () => {
        if ($scope.screenMode === 'NEW' || $scope.screenMode === 'VIEW') {
            $state.go('rubricas.listar');
        } else if ($scope.screenMode === 'EDIT') {
            $state.go('rubricas.exibir', {
                objectId: $stateParams.objectId
            });
        }
    };

    $ctrl.retornar = () => {
        $state.go('rubricas.listar');
    };

}
