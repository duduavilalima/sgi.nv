angular
    .module('app.orcamento-empresarial')
    .controller('OrcamentoEmpresarialCtrl', orcamentoEmpresarialCtrlFn);

function orcamentoEmpresarialCtrlFn($scope, $state, $stateParams, OrcamentoEmpresarial, $rootScope, Rubricas, Unidade, Diretoria, Gerencia, CentroFinanceiro) {
    const $ctrl = this;

    $ctrl.action = false;
    $ctrl.tipo = undefined;
    $ctrl.listaPrevisao = undefined;

    $ctrl.totaisMes = {"1": 0, "2": 0, "3": 0, "4": 0, "5": 0, "6": 0, "7": 0, "8": 0, "9": 0, "10": 0, "11": 0, "12": 0};
    $ctrl.totaisGeral = 0;

    $ctrl.tiposList = [{
            codigo: 1,
            descricao: 'Matriz/Filial'
        },
        {
            codigo: 2,
            descricao: 'Diretoria'
        },
        {
            codigo: 3,
            descricao: 'Gerência'
        },
        {
            codigo: 4,
            descricao: 'Centro de Lucro'
        }
    ];

    let catchError = (response) => {
        $rootScope.loadingProgress = false;
        $ctrl.action = false;
        $scope.$emit('crud-critical', 'load', response);
        reject(false);
    };

    $ctrl.changeTipo = (tipo) => {
        switch (tipo) {
            case 1:
                obterListaUnidadesAtivas();
                $ctrl.object.unidade = undefined;
                $ctrl.object.diretoria = undefined;
                $ctrl.object.gerencia = undefined;
                $ctrl.object.centroFinanceiro = undefined;
                break;
            case 2:
                obterListaDiretorias();
                $ctrl.object.unidade = undefined;
                $ctrl.object.diretoria = undefined;
                $ctrl.object.gerencia = undefined;
                $ctrl.object.centroFinanceiro = undefined;
                break;
            case 3:
                obterListaGerencias();
                $ctrl.object.unidade = undefined;
                $ctrl.object.diretoria = undefined;
                $ctrl.object.gerencia = undefined;
                $ctrl.object.centroFinanceiro = undefined;
                break;
            case 4:
                obterListaCentroLucro();
                $ctrl.object.unidade = undefined;
                $ctrl.object.diretoria = undefined;
                $ctrl.object.gerencia = undefined;
                $ctrl.object.centroFinanceiro = undefined;
                break;
        }
    };

    let obterListaDespesasSimplesAtivas = () => {
        $ctrl.action = true;
        return new Promise((resolve, reject) => {
            $ctrl.listaPrevisao = [];
            if (!$ctrl.rubricasPaiList) {
                Rubricas.obterListaDespesasSimplesAtivas().$promise
                    .then(response => {
                        $ctrl.rubricasPaiList = response;
                        $ctrl.action = false;
                        $ctrl.rubricasPaiList.forEach(item => {
                            $ctrl.listaPrevisao.push({
                                despesaCustos:    item,
                                unidade:          undefined,
                                diretoria:        undefined,
                                gerencia:         undefined,
                                centroFinanceiro: undefined,
                                referenciaAno:    undefined,
                                _1: {valor: undefined, referenciaMes: 01},
                                _2: {valor: undefined, referenciaMes: 02},
                                _3: {valor: undefined, referenciaMes: 03},
                                _4: {valor: undefined, referenciaMes: 04},
                                _5: {valor: undefined, referenciaMes: 05},
                                _6: {valor: undefined, referenciaMes: 06},
                                _7: {valor: undefined, referenciaMes: 07},
                                _8: {valor: undefined, referenciaMes: 08},
                                _9: {valor: undefined, referenciaMes: 09},
                                _10:{valor: undefined, referenciaMes: 10},
                                _11:{valor: undefined, referenciaMes: 11},
                                _12:{valor: undefined, referenciaMes: 12},
                            });
                        });
                        resolve(true);
                    })
                    .catch(catchError);
            }
        });
    };

    $ctrl.atualizarTotais = (mes) => {
        let total = 0;
        $ctrl.listaPrevisao.forEach(item => {
            if (item.despesaCustos.operacao === 'SOMA') {
                total = total + (item[`_${mes}`].valor ? item[`_${mes}`].valor : 0);
            } else if (item.despesaCustos.operacao === 'SUBTRACAO') {
                total = total - (item[`_${mes}`].valor ? item[`_${mes}`].valor : 0);
            }
        });
        $ctrl.totaisMes[mes] = total;
        $ctrl.atualizarTotaisGeral();
    };

    $ctrl.atualizarTotaisGeral = () => {
        let total = 0;
        $ctrl.listaPrevisao.forEach(item => {
            // FIXME: Refatorar
            if (item.despesaCustos.operacao === 'SOMA') {
                total = total + (item._1.valor ? item._1.valor : 0);
                total = total + (item._2.valor ? item._2.valor : 0);
                total = total + (item._3.valor ? item._3.valor : 0);
                total = total + (item._4.valor ? item._4.valor : 0);
                total = total + (item._5.valor ? item._5.valor : 0);
                total = total + (item._6.valor ? item._6.valor : 0);
                total = total + (item._7.valor ? item._7.valor : 0);
                total = total + (item._8.valor ? item._8.valor : 0);
                total = total + (item._9.valor ? item._9.valor : 0);
                total = total + (item._10.valor ? item._10.valor : 0);
                total = total + (item._11.valor ? item._11.valor : 0);
                total = total + (item._12.valor ? item._12.valor : 0);
            } else if (item.despesaCustos.operacao === 'SUBTRACAO') {
                total = total - (item._1.valor ? item._1.valor : 0);
                total = total - (item._2.valor ? item._2.valor : 0);
                total = total - (item._3.valor ? item._3.valor : 0);
                total = total - (item._4.valor ? item._4.valor : 0);
                total = total - (item._5.valor ? item._5.valor : 0);
                total = total - (item._6.valor ? item._6.valor : 0);
                total = total - (item._7.valor ? item._7.valor : 0);
                total = total - (item._8.valor ? item._8.valor : 0);
                total = total - (item._9.valor ? item._9.valor : 0);
                total = total - (item._10.valor ? item._10.valor : 0);
                total = total - (item._11.valor ? item._11.valor : 0);
                total = total - (item._12.valor ? item._12.valor : 0);
            }
        });
        $ctrl.totaisGeral = total;
    };

    let obterListaUnidadesAtivas = () => {
        $ctrl.action = true;
        return new Promise((resolve, reject) => {
            if (!$ctrl.unidadesList) {
                Unidade.unidadesporempresa().$promise
                    .then(response => {
                        $ctrl.unidadesList = response;
                        $ctrl.action = false;
                        resolve(true);
                    })
                    .catch(catchError);
            }
        });
    };

    let obterListaDiretorias = () => {
        $ctrl.action = true;
        return new Promise((resolve, reject) => {
            if (!$ctrl.diretoriasList) {
                Diretoria.buscarDiretoria().$promise
                    .then(response => {
                        $ctrl.diretoriasList = response;
                        $ctrl.action = false;
                        resolve(true);
                    })
                    .catch(catchError);
            }
        });
    };

    let obterListaGerencias = () => {
        $ctrl.action = true;
        return new Promise((resolve, reject) => {
            if (!$ctrl.gerenciasList) {
                Gerencia.getGerenciaSimplesAtivas().$promise
                    .then(response => {
                        $ctrl.gerenciasList = response;
                        $ctrl.action = false;
                        resolve(true);
                    })
                    .catch(catchError);
            }
        });
    };

    let obterListaCentroLucro = () => {
        return new Promise((resolve, reject) => {
            if (!$ctrl.centroLucroList) {
                CentroFinanceiro.obterListaCentrosLucrosAtivos().$promise
                    .then(response => {
                        $ctrl.centroLucroList = response;
                        resolve(true);
                    })
                    .catch(catchError);
            }
        });
    };

    function loadObj() {
        let load = (resolve, reject) => {
            $ctrl.action = true;
            $ctrl.object = new OrcamentoEmpresarial();
            $ctrl.objectId = $stateParams.objectId;
            if (!$ctrl.objectId) {
                catchError();
            } else {
                OrcamentoEmpresarial.get({
                        id: $stateParams.objectId
                    }).$promise
                    .then(function(data) {
                        $ctrl.action = false;
                        $ctrl.object = data;
                        resolve(true);
                    });
            }
        };
        return Promise.resolve()
            .then(load)
            .catch(catchError);
    }

    function newObj() {
        $rootScope.loadingProgress = true;
        let newObj = function(data, status, headers, config) {
            $rootScope.loadingProgress = false;
            $ctrl.object = new OrcamentoEmpresarial();
            $ctrl.object = Object.assign(data, $ctrl.object);
            $scope.$apply();
        };
        return Promise.resolve()
            .then(obterListaDespesasSimplesAtivas)
            .then(() => OrcamentoEmpresarial.get().$promise)
            .then(newObj)
            .catch(catchError);
    }

    $ctrl.salvar = () => {
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
            $state.go('orcamento-empresarial.exibir', {
                objectId: response.codigo
            });
        }).catch(catchError);
    };

    $ctrl.editar = () => {
        $state.go('orcamento-empresarial.editar', {
            objectId: $stateParams.objectId
        });
    };

    $ctrl.cancelar = () => {
        if ($scope.screenMode === 'NEW' || $scope.screenMode === 'VIEW') {
            $state.go('orcamento-empresarial.listar');
        } else if ($scope.screenMode === 'EDIT') {
            $state.go('orcamento-empresarial.exibir', {
                objectId: $stateParams.objectId
            });
        }
    };

    $ctrl.retornar = () => {
        $state.go('orcamento-empresarial.listar');
    };

    $ctrl.gerar = () => {
        $ctrl.action = true;
        $rootScope.loadingProgress = true;
    };

    // FRAMEWORK NÃO LANÇA SUCESSO PARA CRIAÇÃO DE OBJETO NOVO //
    if ($scope.screenMode === 'NEW') {
        newObj();
    } else if ($scope.screenMode === 'EDIT' || $scope.screenMode === 'VIEW') {
        loadObj();
    }

}