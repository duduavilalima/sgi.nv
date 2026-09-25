angular
    .module('app.obras')

    .controller('ObrasDetailCtrl', detailController);

function detailController($scope, $state, $stateParams, $mdDialog, $rootScope, Pessoa, PessoaJuridica, Lancamentos, Obras, Unidade, ContaCorrente, ContaContabil, Endereco, AutocompleteService, DialogConfirm, EnumService, Cidade) {
    const $ctrl = this;

    const init=() => {
        $ctrl.modulo = $state.current.data.modulo;
        $ctrl.titulo = $state.current.data.titulo;
        $ctrl.object = {};
        $ctrl.searchTextCidade = "";

        if ($scope.screenMode === 'NEW') {
            newObj();
        } else {
            loadObj();
        }
    };

    const loadObj=() => {

        let pObras = Obras.get({ id: $stateParams.objectId }).$promise;

        pObras.then((data) => {
            $ctrl.object = data;
            $ctrl.calcularPrazo();
            if ($scope.screenMode === 'EDIT') {
                initEdit(data);
                $ctrl.selectedItemCidade = data.endereco.cidade;
                $ctrl.searchTextCidade = data.endereco.cidade.nome;
                $ctrl.selectedItemCidadeChange(data.endereco.cidade);
            }
            
        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    const newObj=() => {
        $ctrl.object = new Obras();
        $ctrl.object.clientePrincipal = {};
        $ctrl.object.clientePrincipal.pessoaJuridicaTO = {};
        $ctrl.object.clientePrincipal.percFaturamento = 100;
        if ($ctrl.object.clientePrincipal.percFaturamento) {
            $ctrl.object.clienteSecundario = {};
            $ctrl.object.clienteSecundario.pessoa = {};
        }
        $ctrl.object.nome = undefined;
        $ctrl.object.situacao = "Ativo";
        $ctrl.object.inicioVigencia = undefined;
        $ctrl.object.fimVigencia = undefined;
        $ctrl.object.numSalas = undefined;
        $ctrl.object.numLojas = undefined;
        $ctrl.object.prazo = undefined;
        $ctrl.object.tipoObra = undefined;
        $ctrl.object.m2total = undefined;
        $ctrl.object.endereco = {};
        $ctrl.object.endereco.cidade = {};

        $ctrl.listSetores = [];
        $ctrl.listSituacao = [];
        $ctrl.listTipos = [];
        $ctrl.listTipoLogradouro = [];
        initCampoSituacao();
        initCampoSetor();
        initCampoTipo();
        initTipoLogradouro();
        initOnChanges();
        initCampoUF();
         
        
        // console.log($ctrl);

        // console.log($scope);

        // console.log(object);

    };

    const initEdit=(object) => {

        $ctrl.listSetores = [];
        $ctrl.listSituacao = [];
        $ctrl.listTipos = [];
        $ctrl.listTipoLogradouro = [];
        initCampoSituacao();
        initCampoSetor();
        initCampoTipo();
        initTipoLogradouro();
        initCampoUF();
    };

    const initCampoSituacao=() => {
        let pStatusObra = EnumService.enumValues({
            pacote: 'br.com.araujoabreu.sgi.enumerators',
            classe: 'null',
            enum: 'Situacao'
        }).$promise;

        pStatusObra.then((data) => {
            $ctrl.listSituacao.push('');
            angular.forEach(data, (value, key) => {
                $ctrl.listSituacao.push(value);
            });

            initOnChanges();

        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    const initCampoSetor=() => {

        let pSetores = EnumService.enumValues({
            pacote: 'br.com.araujoabreu.sgi.enumerators',
            classe: 'null',
            enum: 'Setor'
        }).$promise;

        pSetores.then( (data) => {
            $ctrl.listSetores.push('');
            angular.forEach(data, (value, key) => {
                $ctrl.listSetores.push(value);
            });

        }).catch( (response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    //TIPOOBRAS
    const initCampoTipo=() => {
        Obras.buscarTipoObra().$promise.then((data) => {
            $ctrl.listTipos.push('');
            data.sort( (a, b) => { return a.nome - b.nome });
            angular.forEach(data,  (value) => {
                $ctrl.listTipos.push(value);
            });
        }).catch( (response) => {
            $scope.$emit('crud-critical', 'load', response);
            $ctrl.listTipos = [];
        });
    };
    //FIM TIPOOBRAS

    const initTipoLogradouro=() => {
        Endereco.buscarTipoLogradouro().$promise.then( (data) => {
            $ctrl.listTipoLogradouro.push('');
            data.sort( (a, b) => { return a.nome - b.nome });
            angular.forEach(data, (value) => {
                $ctrl.listTipoLogradouro.push(value);
            });
        }).catch( (response) => {
            $scope.$emit('crud-critical', 'load', response);
            $ctrl.listTipos = [];
        });
    };

    const initCampoUF=() => {
        $ctrl.listEstados = ['', 'AC', 'AL', 'AM', 'AP', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MG', 'MS', 'MT', 'PA', 'PB', 'PE', 'PI', 'PR', 'RJ', 'RN', 'RO', 'RR', 'RS', 'SC', 'SE', 'SP', 'TO'];
    };

    /** ONCHANGES */
    const initOnChanges=() => {

        $ctrl.changeSituacao = () => {
            if ($ctrl.object.situacao === '') {
                delete $ctrl.object.situacao;
            }
        };

        $ctrl.changeTipoObra = () => {
            if ('' === $ctrl.object.tipoObra) {
                delete $ctrl.object.tipoObra;
            }
        };

        $ctrl.changeUf = () => {
            if ('' === $ctrl.object.uf) {
                delete $ctrl.object.uf;
            }
        };

        $ctrl.changeSetor = () => {
            if ('' === $ctrl.object.setor) {
                delete $ctrl.object.setor;
            }
        };

        $ctrl.changeSituacao = () => {
            if ('' === $ctrl.object.situacao) {
                delete $ctrl.object.situacao;
            }
        };

    };

    $ctrl.editar = () => {
        $state.go('obras.edit', { objectId: $ctrl.object.codigo });
    };

    $ctrl.retornar = () => {
        $state.go('obras.list');
    };

    $ctrl.cancelarEdit = () => {
        if ($scope.screenMode === 'NEW') {
            $state.go('obras.list');
        } else if ($scope.screenMode === 'EDIT') {
            $state.go('obras.view', { objectId: $ctrl.object.codigo });
        }
    };

    $ctrl.selectedItemChange = (item) => {
        $ctrl.object.contaContabil = item;
    };

    $ctrl.salvar = () => {
        if ($ctrl.object.clientePrincipal.percFaturamento < 50) {
            $scope.notification.error("", "Percentual de faturamento do cliente principal deve ser maior ou igual a 50.");
            return;
        }
        if ($ctrl.object.clientePrincipal.percFaturamento < 100) {
            if (angular.isUndefined($ctrl.object.clienteSecundario.pessoa.codigo)) {
                $scope.notification.error("", "Cliente secundário é obrigatório");
                return;
            }
        }
        if ($scope.screenMode === 'NEW') {

            let pSave = $ctrl.object.$save();
            pSave.then((data) => {
                $scope.notification.success("Novo", "Obra inserida com sucesso.");
                $state.go('obras.view', { objectId: data.codigo });
            }).catch((response) => {
                $scope.$emit('crud-critical', 'save', response);
            });

        } else if ($scope.screenMode === 'EDIT') {
            let entity = angular.copy($ctrl.object);
            if ($ctrl.object.clientePrincipal.percFaturamento == '100') {
                delete $ctrl.object.clienteSecundario;

            }
            Obras.update({ id: $ctrl.object.codigo }, entity).$promise.then((data) => {
                $scope.notification.success("Edição", "Obra atualizada com sucesso.");
                $state.go('obras.view', { objectId: data.codigo });
            }).catch( (response) => {
                $scope.$emit('crud-critical', 'save', response);
            });

        }
    };

    /** REMOVER OBRA */
    $ctrl.remover = (ev) => {
        let tituloDialog = "Obra - Remover: Confirmação";
        let msgDialog = "Deseja realmente remover esta obra (esta operação não poderá ser desfeita)?";
        let tituloBotaoConfirmar = "CONFIRMAR";
        let tituloBotaoCancelar = "CANCELAR";

        let funcaoCancelar = (fnHide) => {
            fnHide();
        };

        let funcaoConfirmar = (object, fnHide) => {
            let pObras = Obras.delete({ id: object.codigo }).$promise;

            pObras.then((data) => {

                let title = 'Remover Obra';
                let message = 'Obra ' + object.nome + ' removida com sucesso.';
                $scope.notification.success(title, message);

                fnHide();
                $state.go('obras.list');

            }).catch( (response) => {
                console.error('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
            });
        };

        DialogConfirm.show(ev, $ctrl.object, tituloDialog, msgDialog, tituloBotaoConfirmar, tituloBotaoCancelar, funcaoConfirmar, funcaoCancelar);
    };

    /** CANCELAR OBRA */
    $ctrl.cancelar = (ev) => {
        let tituloDialog = "Obras - Cancelar: Confirmação";
        let msgDialog = "Deseja realmente cancelar esta obra (esta operação não poderá ser desfeita)?";
        let tituloBotaoConfirmar = "CONFIRMAR";
        let tituloBotaoCancelar = "CANCELAR";

        const funcaoCancelar = (fnHide) => {
            fnHide();
        };

        const funcaoConfirmar = (object, fnHide) => {
            let pObras = Obras.cancelar(object.codigo).$promise;

            pObras.then((data) => {

                let title = 'Cancelar Obra';
                let message = 'Obra ' + object.numero + ' cancelada com sucesso.';
                $scope.notification.success(title, message);

                loadObj();
                fnHide();

            }).catch((response) => {
                console.error('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
            });
        };

        DialogConfirm.show(ev, $ctrl.object, tituloDialog, msgDialog, tituloBotaoConfirmar, tituloBotaoCancelar, funcaoConfirmar, funcaoCancelar);
    };

    $ctrl.calculaPercFaturamentoSec = () => {
        if ($ctrl.object.clienteSecundario === undefined) {
            $ctrl.object.clienteSecundario = {};
            $ctrl.object.clienteSecundario.pessoa = {};
        }
        $ctrl.object.clienteSecundario.percFaturamento = 100 - $ctrl.object.clientePrincipal.percFaturamento;
    };

    $ctrl.calcularPrazo = () => {
        if ($ctrl.object.fimVigencia != undefined && $ctrl.object.inicioVigencia != undefined) {
            let dt = (new Date($ctrl.object.fimVigencia).getTime() - new Date($ctrl.object.inicioVigencia).getTime() + 3600000);
            let meses = (dt / 86400000) / 30;
            meses = Math.ceil(meses * 1000) / 1000;
            meses = Math.round(meses);
            $ctrl.object.prazo = parseInt(meses);
        }
    };

    $ctrl.obterCamposPorDocumento = (documento, tipoCliente) => {
        if (documento) {
            documento = documento.trim();
        } else {
            if (tipoCliente == '1' && $ctrl.object.clientePrincipal && $ctrl.object.clientePrincipal.pessoa) {
                delete $ctrl.object.clientePrincipal.pessoa;
            }

            if (tipoCliente == '2' && $ctrl.object.clienteSecundario && $ctrl.object.clienteSecundario.pessoa) {
                delete $ctrl.object.clienteSecundario.pessoa;
            }

            return;
        }

        if (documento && documento.match(/^[0-9]+$/) === undefined) {
            documento = undefined;
        } else {
            $rootScope.loadingProgress = true;
            let resultado = PessoaJuridica.getPorDocumentoNaoFormatado({ documento: documento }).$promise;
            return resultado.then((data) => {
                $rootScope.loadingProgress = false;
                if (tipoCliente == '1') {
                    $ctrl.object.clientePrincipal.pessoa = data;
                }
                if (tipoCliente == '2') {
                    $ctrl.object.clienteSecundario.pessoa = data;
                }
            }).catch((response) => {
                $scope.$emit('crud-critical', 'search', response);
                return resultado;
            });
        }
    };

    //AUTOCOMPLETE CIDADE
    $ctrl.querySearchCidade = (query) => {
        let nomeCidade = query;
        let listaCidade = [];

        let pCidade = Cidade.buscarListaPorNome(nomeCidade).$promise;

        return pCidade.then( (data) => {
            angular.forEach(data, (value, key) => {
                listaCidade.push(value);
            });
            return listaCidade;

        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
            listaCidade = [];
            return listaCidade;
        });
    };

    $ctrl.selectedItemCidadeChange = (item) => {
        $ctrl.object.endereco.cidade = item;

        $ctrl.object.endereco.cidade.uf = item.uf;

        $ctrl.object.endereco.uf = item.uf;
    };
    //FIM AUTOCOMPLETE

    init();

}