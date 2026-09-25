angular
    .module('app.orcamento-empresarial')
    .controller('OrcamentoEmpresarialImportacaoCtrl', orcamentoEmpresarialImportacaoCtrlFn);

function orcamentoEmpresarialImportacaoCtrlFn($scope, $rootScope, Unidade, Diretoria, Gerencia, CentroFinanceiro, FileUploadCustos) {
    const $ctrl = this;

    $ctrl.action = false;
    $ctrl.tipo = undefined;
    $ctrl.listaPrevisao = undefined;
    $ctrl.object = {};

    $ctrl.files = undefined;

    $scope.carregarArquivos = function(files) {
        $ctrl.files = files;
    };


    $ctrl.searchTextCentroLucro = undefined;
    $ctrl.selectedItemCentroLucro = undefined;

    $ctrl.tiposList = [
        // TODO: A princípio, só haverá tipo gerência
        // {
        //     codigo: 1,
        //     descricao: 'Matriz/Filial'
        // },
        // {
        //     codigo: 2,
        //     descricao: 'Diretoria'
        // },
        {
            codigo: 3,
            descricao: 'Gerência'
        },
        // {
        //     codigo: 4,
        //     descricao: 'Centro de Lucro'
        // }
    ];

    let catchError = (response) => {
        console.error(response);
        $rootScope.loadingProgress = false;
        $ctrl.action = false;
        $scope.$emit('crud-critical', 'load', response);
        reject(false);
    };

    let catchErrorUpload = (response) => {
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
                $ctrl.searchTextCentroLucro = undefined;
                $ctrl.selectedItemCentroLucro = undefined;
                break;
            case 2:
                obterListaDiretorias();
                $ctrl.object.unidade = undefined;
                $ctrl.object.diretoria = undefined;
                $ctrl.object.gerencia = undefined;
                $ctrl.object.centroFinanceiro = undefined;
                $ctrl.searchTextCentroLucro = undefined;
                $ctrl.selectedItemCentroLucro = undefined;
                break;
            case 3:
                obterListaGerencias();
                $ctrl.object.unidade = undefined;
                $ctrl.object.diretoria = undefined;
                $ctrl.object.gerencia = undefined;
                $ctrl.object.centroFinanceiro = undefined;
                $ctrl.searchTextCentroLucro = undefined;
                $ctrl.selectedItemCentroLucro = undefined;
                break;
            case 4:
                $ctrl.object.unidade = undefined;
                $ctrl.object.diretoria = undefined;
                $ctrl.object.gerencia = undefined;
                $ctrl.object.centroFinanceiro = undefined;
                break;
        }
    };

    let obterListaUnidadesAtivas = () => {
        $ctrl.action = true;
        $rootScope.loadingProgress = true;
        return new Promise((resolve, reject) => {
            if (!$ctrl.unidadesList) {
                Unidade.unidadesporempresa().$promise
                    .then(response => {
                        $ctrl.unidadesList = response;
                        $ctrl.action = false;
                        $rootScope.loadingProgress = false;
                        resolve(true);
                    })
                    .catch(catchError);
            } else {
                $ctrl.action = false;
                $rootScope.loadingProgress = false;
            }
        });
    };

    let obterListaDiretorias = () => {
        $ctrl.action = true;
        $rootScope.loadingProgress = true;
        return new Promise((resolve, reject) => {
            if (!$ctrl.diretoriasList) {
                Diretoria.buscarDiretoria().$promise
                    .then(response => {
                        $ctrl.diretoriasList = response;
                        $ctrl.action = false;
                        $rootScope.loadingProgress = false;
                        resolve(true);
                    })
                    .catch(catchError);
            } else {
                $ctrl.action = false;
                $rootScope.loadingProgress = false;
            }
        });
    };

    let obterListaGerencias = () => {
        $ctrl.action = true;
        $rootScope.loadingProgress = true;
        return new Promise((resolve, reject) => {
            if (!$ctrl.gerenciasList) {
                Gerencia.getGerenciaSimplesAtivas().$promise
                    .then(response => {
                        $ctrl.gerenciasList = response;
                        $ctrl.action = false;
                        $rootScope.loadingProgress = false;
                        resolve(true);
                    })
                    .catch(catchError);
            } else {
                $ctrl.action = false;
                $rootScope.loadingProgress = false;
            }
        });
    };

    $ctrl.querySearchCentroLucro = function(query) {
        $ctrl.centroLucroList = [];
        let obj = {};
        obj.identificadorFinal = query;
        obj.nome = query;
        obj.situacao = 'Ativo';

        let pCF = CentroFinanceiro.buscarCFsSimples(obj).$promise;

        return pCF.then(function(data) {
            $ctrl.centroLucroList = data;
            return $ctrl.centroLucroList;
        }).catch(catchError);
    };

    $ctrl.fnSelectedItemCentroLucro = function(item) {
        $ctrl.object.centroFinanceiro = item;
        $ctrl.selectedItemCentroLucro = item;
        if (!item) {
            delete $ctrl.object.centroFinanceiro;
            $ctrl.selectedItemCentroLucro = undefined;
        }
    };

    function validar() {
        return new Promise((resolve, reject) => {
            if (!$ctrl.tipo) {
                $scope.notification.error('Por favor, selecione um tipo');
                reject(false);
            }
            switch ($ctrl.tipo) {
                case 1:
                    if (!$ctrl.object.unidade || !$ctrl.object.unidade.codigo) {
                        $scope.notification.error('Por favor, selecione uma matriz/filial');
                        reject(false);
                    }
                    break;
                case 2:
                    if (!$ctrl.object.diretoria || !$ctrl.object.diretoria.codigo) {
                        $scope.notification.error('Por favor, selecione uma diretoria');
                        reject(false);
                    }
                    break;
                case 3:
                    if (!$ctrl.object.gerencia || !$ctrl.object.gerencia.codigo) {
                        $scope.notification.error('Por favor, selecione uma gerência');
                        reject(false);
                    }
                    break;
                case 4:
                    if (!$ctrl.object.centroFinanceiro) {
                        $scope.notification.error('Por favor, selecione um centro de lucro');
                        reject(false);
                    }
                    break;
                default:
                    error = undefined;
                    $scope.notification.error('Por favor, selecione um tipo de matriz/filial, diretoria, gerência ou centro de lucro');
                    reject(false);
                    break;
            }
            if (!$ctrl.object.referenciaAno) {
                $scope.notification.error('Por favor, selecione o ano de referência');
                reject(false);
            }
            if (!$ctrl.files) {
                $scope.notification.error('Por favor, selecione um arquivo');
                reject(false);
            }
            resolve(true);
        });
    }

    function onFileSelect(fileList) {
        return new Promise((resolve, reject) => {
            let fileReader = new FileReader();
            if (fileReader && fileList && fileList.length) {
                fileReader.readAsArrayBuffer(new Blob([fileList[0]]));
                fileReader.onload = function() {
                    let bytes = new Uint8Array(fileReader.result);
                    let binary = '';
                    let len = bytes.byteLength;
                    for (var i = 0; i < len; i++) {
                        binary += String.fromCharCode( bytes[ i ] );
                    }
                    resolve(window.btoa(binary));
                };
            }
        });
    }

    $ctrl.importar = () => {
        validar().then((value) => {
            $ctrl.action = true;
            $rootScope.loadingProgress = true;
            Promise.resolve()
                .then(() => onFileSelect($ctrl.files))
                .then((data) => {
                    let importacaoOrcamentoEmpresarialTO = {
                        tipoPlanilha: $ctrl.tipo,
                        unidade: $ctrl.object.unidade,
                        diretoria: $ctrl.object.diretoria,
                        gerencia: $ctrl.object.gerencia,
                        centroFinanceiro: $ctrl.object.centroFinanceiro,
                        referenciaAno: $ctrl.object.referenciaAno,
                        file: data
                    };
                    FileUploadCustos.importacaoOrcamentoEmpresaria({
                            file: $ctrl.files[0].name.split(".")[0],
                            sufixo: $ctrl.files[0].name.split(".")[1]
                        }, importacaoOrcamentoEmpresarialTO).$promise
                        .then(response => {
                            console.log(response);
                            $rootScope.loadingProgress = false;
                            $ctrl.action = false;
                            $scope.notification.success("Importação realizada com sucesso");
                        })
                        .catch(catchError);
                });
        }).catch((error) => console.error(error));
    };
}