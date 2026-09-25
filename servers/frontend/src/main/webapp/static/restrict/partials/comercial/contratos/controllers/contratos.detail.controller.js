angular
    .module('app.contratos')

    .controller('ContratosDetailCtrl', detailController);

function detailController($scope, $state, $stateParams, $mdDialog, $rootScope, $window, EnumService, UsuarioLogadoService, DateTimeService, NumberService,
    Cidade, Contrato, Unidade, Diretoria, ContratoEndereco, Aditivo, Telefone, ContratoContato, ContratoAnexo, FormaContratacao,
    MotivoRetornoDialog, MotivoRetornoContrato, DialogConfirm, CentroFinanceiro) {

    const $ctrl = this;

    // Serve para carregar o progress
    $ctrl.load = true;

    const init = () => {
        $ctrl.modulo = $state.current.data.modulo;
        $ctrl.titulo = $state.current.data.titulo;
        $ctrl.object = {};

        if ($scope.screenMode === 'NEW') {
            $ctrl.load = false;
            newObj();

        } else {
            loadObj();

            if ($scope.screenMode === 'VIEW') {
                $ctrl.conjSituacaoInativo = new Set(["Inativo", "Encerrado", "Encerrado, mas com faturamento", "Paralisado", "Cancelado"]);
            }
        }
    };

    const loadObj = () => {
        return Contrato.get({id: $stateParams.objectId}).$promise.then((data) => {
            if ($scope.screenMode !== 'VIEW') {
                if (!(data.situacao === 'Ativo' || data.situacao === 'Em elaboração' ||  data.situacao === 'Em revisão')) {
                    $state.go('contratos.view', {objectId: data.codigo});
                }
            }
console.log('data: ', data)
            $ctrl.load = false;
            $ctrl.object = data;
            $ctrl.habilitarAdminContrato = false;
            carregarTipoPessoa();
            $ctrl.changeTipoPessoa();

            verificarPerfil();
            initCampoSituacao();
            initCampoProrrogacao();
            initCampoEmpresa();
            initCampoEstagioComercial();
            initCampoDiretoria();
            $ctrl.calcularRetencao();

            carregarAbaMotivoCancelamento();
            carregarAbaCentroLucro();
            carregarAbaContato();
            carregarAbaContratoEndereco();
            carregarAbaAditivos();
            carregarAbaAnexos();

            ajustarDatas($ctrl.object);
            dataFimVigenciaMaiorOuIgualDataCorrente();
            dataFimVigenciaMenorDataCorrente();


        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    const newObj = () => { // NEW

        $ctrl.object = new Contrato();
        $ctrl.object.cliente = {};
        $ctrl.object.cliente.tipoPessoa = 'Pessoa Jurídica';
        $ctrl.object.tipo = 'Cliente';
        $ctrl.object.favorecido = false;
        $ctrl.object.valorRetencao = 100;

        verificarPerfil();
        initCampoSituacao();
        initCampoProrrogacao();
        initCampoEmpresa();
        initCampoEstagioComercial();
        initCampoDiretoria();
        initCampoFormaContratacao();
        carregarCampoReajuste();
        carregarTipoPessoa();
        $ctrl.changeTipoPessoa();
    };

    /* Usado na tela p/ calcular o valor da retenção! */
    $ctrl.getValorRetencao = () => {
        let pertencualTotal = 100;
        if ($ctrl.object.valorTotal !== undefined && $ctrl.object.percentualRetencao !== undefined) {
            $ctrl.object.valorRetencao = ($ctrl.object.valorTotal * ($ctrl.object.percentualRetencao / pertencualTotal));
        }
    };

    $ctrl.calcularPrazoContrato = () => {
        if ((angular.isDefined($ctrl.object.dataFimVigencia)) && (angular.isDefined($ctrl.object.dataInicioVigencia))) {

            Contrato.calcularPrazoContrato($ctrl.object).$promise.then((data) => {
                $ctrl.object.prazoAtual = data.prazoAtual;

            }).catch((response) => {
                $rootScope.loadingProgress = false;
                $rootScope.$emit('crud-critical', 'load', response);
            });

        }
    };

    const verificarPerfil = () => {

        UsuarioLogadoService.possuiUmPerfilLista('admin-contrato').$promise.then((data) => {
            $ctrl.habilitarAdminContrato = data.possuiUmPerfilLista;
        }).catch((response) => {
            $rootScope.loadingProgress = false;
            $rootScope.$emit('crud-critical', 'load', response);
        });
    };

    const initCampoSituacao = () => {
        $ctrl.listSituacoes = [];

        let pStatusContrato = EnumService.enumValues({
            pacote: 'br.com.araujoabreu.sgi.administracao.domain',
            classe: 'Contrato',
            enum: 'SituacaoContrato'
        }).$promise;

        pStatusContrato.then((data) => {
            $ctrl.listSituacoes.push('');
            angular.forEach(data, (value, key) => {
                $ctrl.listSituacoes.push(value);
            });
        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    const initCampoProrrogacao = () => {
        $ctrl.listProrrogacao = [];

        let pProrrogacaoContrato = EnumService.enumValues({
            pacote: 'br.com.araujoabreu.sgi.administracao.domain',
            classe: 'Contrato',
            enum: 'Prorrogacao'
        }).$promise;

        pProrrogacaoContrato.then((data) => {
            angular.forEach(data, (value, key) => {
                $ctrl.listProrrogacao.push(value);
            });
        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    const initCampoEstagioComercial = () => {
        $ctrl.listComercial = [];

        let pContrato = EnumService.enumValues({
            pacote: 'br.com.araujoabreu.sgi.administracao.domain',
            classe: 'Contrato',
            enum: 'EstagioComercial'
        }).$promise;

        pContrato.then((data) => {
            angular.forEach(data, (value, key) => {
                $ctrl.listComercial.push(value);
            });
        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    const initCampoDiretoria = () => {
        $ctrl.listDiretoria = [];

        //fixme: pode refatorar no backend para melhorar a performace
        let pDiretoria = Diretoria.buscarDiretoria(undefined, {}).$promise;

        pDiretoria.then((data) => {

            angular.forEach(data, (value, key) => {
                $ctrl.listDiretoria.push(value);
            });
        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    const initCampoEmpresa = () => {
        $ctrl.listEmpresas = [];

        let codigosUnidadesIgnoradas = [8];
        let pUnidade = Unidade.unidadesporempresaexceto(undefined, codigosUnidadesIgnoradas).$promise;

        pUnidade.then((data) => {
            angular.forEach(data, (value, key) => {
                // TODO: Refatorar
                if (value.emitirNfFaturamento) {
                    $ctrl.listEmpresas.push(value);
                }
            });
        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    const carregarCampoReajuste = () => {
        EnumService.enumValues({
            pacote: 'br.com.araujoabreu.sgi.administracao.domain',
            classe: 'Contrato',
            enum: 'PeriodoReajuste'
        })
            .$promise.then((data) => {
                $ctrl.listReajuste = [];
                $ctrl.listReajuste.push('');
                angular.forEach(data, (value) => {
                    $ctrl.listReajuste.push(value);
                });
            }).catch((response) => {
                $scope.$emit('crud-critical', 'load', response);
            });
    };

    const carregarTipoPessoa = () => {
        $ctrl.listTipoPessoa = [];

        // Fix - Refatorar
        let pContrato = EnumService.enumValues({
            pacote: 'br.com.fasolti.corporativo.pessoa.domain',
            classe: 'Pessoa',
            enum: 'TipoPessoa'
        }).$promise;

        pContrato.then((data) => {
            angular.forEach(data, (value, key) => {
                // Para aparecer na tela somente a palavra "Juridica" ou "Fisica" utiliza-se no html o limitTo
                // Serve para filtrar por pessoa Juridica ou Fisica
                if (value === 'Pessoa Jurídica' || value === 'Pessoa Física') {
                    $ctrl.listTipoPessoa.push(value);
                }
            });
        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    $ctrl.changeTipoPessoa = () => {
        $ctrl.habilitarCnpjRazaoSocial = false;
        $ctrl.habilitarCpfNome = false;

        if ($ctrl.object.cliente.tipoPessoa === 'Pessoa Jurídica') {
            $ctrl.habilitarCnpjRazaoSocial = true;

        } else {
            $ctrl.habilitarCpfNome = true;
        }
    };

    const initCampoFormaContratacao = () => {
        $ctrl.listFormacontratual = [];

        //fixme: pode refatorar no backend
        let pFormaContratacao = FormaContratacao.page(undefined, {}).$promise;
        pFormaContratacao.then((data) => {

            angular.forEach(data.content, (value, key) => {
                $ctrl.listFormacontratual.push(value);
            });
        }).catch((response) => {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    $ctrl.retornar = () => {
        $state.go('contratos.list', {});
    };

    /* Inicio da Dialog Remover Contrato */
    $ctrl.remover = (event) => {


        // Chamada do serviço que criará um dialog de confirmação.
        DialogConfirm.show(
            undefined,
            $ctrl.object,
            'Remover contrato: Confirmação',
            `Deseja realmente <b>remover</b> este contrato (esta operação não poderá ser desfeita)?`,
            'Confirmar',
            'Cancelar',
            confirmRemoverContrato,
            cancel);
    };


    const confirmRemoverContrato = (object, fnHide) => { //removeContrato
        let codigo = $ctrl.object.codigo;
        $ctrl.object = undefined;

        let pRemover = Contrato.remove({
            id: codigo
        }).$promise;

        pRemover.then((data) => {

            let message = 'Contrato removido com sucesso.';
            $scope.notification.success(message);
            $state.go('contratos.list', {});

            $mdDialog.hide();

        }).catch((response) => {
            $scope.action = false;
            $rootScope.loadingProgress = false;
            console.error('crud-critical', 'dialog', response);
            $scope.notification.criticalError(response.status, response.statusText, response.data);
        });
    }; /*Fim da Dialog Remover Contrato*/

    /*VALIDAÇÃO DE DOCUMENTO DO CONTRATO*/
    $ctrl.consultarDocumento = () => {
        let auxTipoPessoa = $ctrl.object.cliente.tipoPessoa;

        if ($ctrl.object.cliente.documento != undefined) {

            Contrato.consultarDocumento({
                isFavorecido: false
            }, $ctrl.object)
                .$promise.then((data) => {

                    $ctrl.object = data;

                }).catch((response) => {
                    $ctrl.object.cliente = undefined;
                    $ctrl.object.cliente = {};
                    $ctrl.object.cliente.tipoPessoa = auxTipoPessoa;
                    $scope.notification.criticalError(response.status, response.statusText, response.data);
                });
        }
    };

    $ctrl.salvar = () => {

        if ($scope.screenMode === 'NEW') {
            $ctrl.object.situacao = 'Em elaboração';

            let pSave = $ctrl.object.$save();
            pSave.then((data) => {

                $scope.notification.success(undefined, "Contrato inserido com sucesso.");
                $state.go('contratos.view', {
                    objectId: data.codigo
                });

            }).catch((response) => {
                $scope.$emit('crud-critical', 'save', response);
            });

        } else if ($scope.screenMode === 'EDIT') {
            let entity = angular.copy($ctrl.object);

            Contrato.update({
                id: $ctrl.object.codigo
            }, entity).$promise.then((data) => {

                $scope.notification.success(undefined, "Contrato atualizado com sucesso.");
                $state.go('contratos.view', {
                    objectId: data.codigo
                });
            }).catch((response) => {
                $scope.$emit('crud-critical', 'save', response);
            });

        }
    };

    /*
     * ---------
     *  ABAS
     * ---------
     */
    const carregarAbaMotivoCancelamento = () => {
        $ctrl.listaMotivoRetornoDocContrato = [];
        $rootScope.loadingProgress = true;
        MotivoRetornoContrato.page(undefined, {
            contrato: {
                codigo: $scope.objectId
            }
        })
            .$promise.then((data) => {
                $rootScope.loadingProgress = false;
                $ctrl.listaMotivoRetornoDocContrato = data.content;
            }).catch((response) => {
                $rootScope.loadingProgress = false;
                $scope.$emit('crud-critical', 'search', response);
            });
    };

    const carregarAbaCentroLucro = () => {
        $ctrl.totalValorMensalCF = 0;
        $ctrl.totalValorTotalCF = 0;

        $ctrl.listCentroLucro = undefined;

        Contrato.buscarCentroLucro($ctrl.object).$promise.then((data) => {
            $ctrl.listCentroLucro = angular.copy(data);

//            let conjSituacao = new Set(['Ativo','Em elaboração','Em análise - Gerência','Em análise - Diretoria','Em análise - Gestão']);
            let conjSituacao = new Set(['Ativo']);

            $ctrl.listCentroLucro.filter(item => {
                return conjSituacao.has(item.situacao);
            }).forEach(item => {
                $ctrl.totalValorMensalCF += item.valorMensalCentroFinanceiro;
                $ctrl.totalValorTotalCF += item.valorTotalCentroFinanceiro;
            });

            $ctrl.totalValorMensalCF = NumberService.ajusteDecimal($ctrl.totalValorMensalCF);
            $ctrl.totalValorTotalCF = NumberService.ajusteDecimal($ctrl.totalValorTotalCF);

            configBotoesNavegacao($ctrl.listCentroLucro);

        }).catch((response) => {
            $scope.$emit('crud-critical', 'AbaCentroLucro', response);
        });
    };

    const carregarAbaContato = () => {
        Contrato.buscarContato({
            id: $scope.objectId
        }).$promise.then((data) => {
            $ctrl.listContato = angular.copy(data);

        }).catch((response) => {
            $scope.$emit('crud-critical', 'AbaContato', response);
        });
    };

    const carregarAbaContratoEndereco = () => {
        Contrato.buscarEnderecos({
            id: $scope.objectId
        }).$promise.then((data) => {
            $ctrl.listContratoEndereco = angular.copy(data);

        }).catch((response) => {
            $scope.$emit('crud-critical', 'AbaContratoEndereco', response);
        });
    };

    const carregarAbaAditivos = () => {
        Contrato.buscarAditivos({
            id: $scope.objectId
        }).$promise.then((data) => {
            $ctrl.listAditivos = angular.copy(data);
            $ctrl.listAditivos.forEach(item => {
                item.dataAssinatura = DateTimeService.ajustarData(item.dataAssinatura);
                item.dataInicioVigencia = DateTimeService.ajustarData(item.dataInicioVigencia);
                item.dataFimVigencia = DateTimeService.ajustarData(item.dataFimVigencia);
            });

        }).catch((response) => {
            $scope.$emit('crud-critical', 'AbaAditivos', response);
        });
    };

    const carregarAbaAnexos = () => {
        Contrato.buscarAnexos({
            id: $scope.objectId
        }).$promise.then((data) => {
            $ctrl.listAnexos = data;

        }).catch((response) => {
            $scope.$emit('crud-critical', 'contraAbaAnexos', response);
        });
    };

    $ctrl.editar = () => {
        $state.go('contratos.edit', {objectId: $ctrl.object.codigo});
    };

    //FIXME: Remover este método futuramento, se o problema do timezone for corrigido no sistema.
    const ajustarDatas = (objContrato) => {
        objContrato.dataAssinatura = DateTimeService.ajustarData(objContrato.dataAssinatura);
        objContrato.dataInicioVigencia = DateTimeService.ajustarData(objContrato.dataInicioVigencia);
        objContrato.dataFimVigencia = DateTimeService.ajustarData(objContrato.dataFimVigencia);
    };

    const dataFimVigenciaMaiorOuIgualDataCorrente = () => {
        $ctrl.habilitarParalisacaoDeContrato = false;

        if ($ctrl.object.dataFimVigencia !== undefined) {
            let dataAtual = new Date();
            let dataHoje = new Date(dataAtual.getFullYear(), dataAtual.getMonth(), dataAtual.getDate());
            let dataFimVigencia = new Date($ctrl.object.dataFimVigencia);

            if (dataFimVigencia >= dataHoje) {
                $ctrl.habilitarParalisacaoDeContrato = true;
            } else {
                $ctrl.habilitarParalisacaoDeContrato = false;
            }
        }
    };

    const dataFimVigenciaMenorDataCorrente = () => {
        $ctrl.habilitarEncerrarContrato = false;

        if ($ctrl.object.dataFimVigencia !== undefined) {
            let dataAtual = new Date();
            let dataHoje = new Date(dataAtual.getFullYear(), dataAtual.getMonth(), dataAtual.getDate());
            let dataFimVigencia = new Date($ctrl.object.dataFimVigencia);

            if (dataFimVigencia < dataHoje) {
                $ctrl.habilitarEncerrarContrato = true;
            } else {
                $ctrl.habilitarEncerrarContrato = false;
            }
        }
    };

    $ctrl.novoCentroLucro = () => {
        $state.go('centrosfinanceiros.new_from', {
            objectId: $ctrl.object.codigo,
            from: 'contrato'
        });
    };

    $ctrl.editarCentroLucro = (item) => {
        $state.go('centrosfinanceiros.edit_from', {
            objectId: item.codigo,
            from: 'contrato',
            objectIdFrom: $ctrl.object.codigo
        });
    };

    $ctrl.exibirCentroLucro = (item) => {
        window.open(`/sgi/nv/app/main.html#!/comercial/cadastros/centrosfinanceiros/${item.codigo}?from=contrato`, '_blank');
    };

    const configBotoesNavegacao = (listCentroLucro) => {
        $ctrl.btnFab = false;
        $ctrl.btnBottom = false;

        /*TODO: Função para mostrar o icone quando os botões do final da tela não estiverem aparecendo*/
        $scope.selectedMode = 'md-fling';
        $scope.selectedDirection = 'up';

        if (listCentroLucro.length > 10) {
            $ctrl.btnFab = true;

            $(document).ready(function() {
                $('md-content').bind('scroll', function() {
                    if ($(this).scrollTop() + $(this).innerHeight() >= this.scrollHeight) {
                        $ctrl.btnFab = false;
                        $ctrl.btnBottom = true;
                    } else {
                        $ctrl.btnFab = true;
                        $ctrl.btnBottom = false;
                    }
                });
            });
        } else {
            $ctrl.btnFab = false;
        }

        if (listCentroLucro > 0) {
            $('md-content').scrollTop();
        }
    };

    /*
     * ---------
     *  DIALOGS
     * ---------
     */

    /* Inicio da Dialog Reativar Contrato */
    $ctrl.reativar = (item) => {

        let total = 0;
        angular.forEach($ctrl.listCentroLucro, (data) => {
            if (data.situacao === 'Paralisado') {
                total++;
            }
        });

        // Chamada do serviço que criará um dialog de confirmação.
        DialogConfirm.show(
            undefined,
            item,
            'Encerrar contrato: Confirmação',
            `Deseja realmente <b>encerrar</b> este contrato e o(s) ${total} centro(s) de lucro associado(s) (esta operação não poderá ser desfeita)?`,
            'Confirmar',
            'Cancelar',
            confirmReativarContrato,
            cancel);
    };

    const confirmReativarContrato = (object, fnHide) => {

        let codigo = $ctrl.object.codigo;
        $ctrl.object = undefined;
        $ctrl.listCentroLucro = undefined;

        let pReativar = Contrato.reativarContrato({
            id: codigo
        }).$promise;

        pReativar.then((data) => {

            let message = 'Contrato ativado com sucesso.';
            $scope.notification.success(message);

            loadObj();

            $mdDialog.hide();

        }).catch((response) => {
            $scope.action = false;
            $rootScope.loadingProgress = false;
            console.error('crud-critical', 'dialog', response);
            $scope.notification.criticalError(response.status, response.statusText, response.data);
        });

    }; /*Fim da Dialog Reativar Contrato*/

    /* Inicio da Dialog Encerrar Contrato */
    $ctrl.encerrarContrato = (event) => {

        let total = 0;
        angular.forEach($ctrl.listCentroLucro, (data) => {
            if (data.situacao !== 'Encerrado' && data.situacao !== 'Cancelado') {
                total++;
            }
        });

        // Chamada do serviço que criará um dialog de confirmação.
        DialogConfirm.show(
            undefined,
            $ctrl.object,
            'Encerrar contrato: Confirmação',
            `Deseja realmente <b>encerrar</b> este contrato e o(s) ${total} centro(s) de lucro associado(s) (esta operação não poderá ser desfeita)?`,
            'Confirmar',
            'Cancelar',
            confirmEncerrarContrato,
            cancel);
    };

    const cancel = (fnHide) => {
        fnHide();
    };

    const confirmEncerrarContrato = (object, fnHide) => { //encerrarContrato

        let codigo = $ctrl.object.codigo;
        $ctrl.object = undefined;
        $ctrl.listCentroLucro = undefined;

        let pEncerrar = Contrato.encerrarContrato({
            id: codigo
        }).$promise;

        pEncerrar.then((data) => {

            let message = 'Contrato encerrado com sucesso.';
            $scope.notification.success(message);

            loadObj();
            $mdDialog.hide();

        }).catch((response) => {
            $scope.action = false;
            $rootScope.loadingProgress = false;
            console.error('crud-critical', 'dialog', response);
            $scope.notification.criticalError(response.status, response.statusText, response.data);
        });
    }; /*Fim da Dialog Encerrar Contrato*/

    /* Inicio da Dialog Motivo Retorno Cancelamento */
    $ctrl.cancelarContrato = () => {
        let chaveMotivo = 'contrato-cancelar';
        let titulo = 'Motivos de Retorno - Cancelar contrato';

        // Função que será executada no confirmar no dialog de motivo de retorno.
        $ctrl.confirmarMotivo = (motivoRetornoDocumento, fn_Hide) => {
            // MotivoRetornoDocumentoContratoTO
            $ctrl.motivoRetornoDocumentoContrato = {};
            $ctrl.motivoRetornoDocumentoContrato.contrato = {
                codigo: $ctrl.object.codigo
            };
            $ctrl.motivoRetornoDocumentoContrato.motivoRetornoDocumento = motivoRetornoDocumento;

            $rootScope.loadingProgress = true;

            MotivoRetornoContrato.save($ctrl.motivoRetornoDocumentoContrato)
                .$promise.then((data) => {
                    $rootScope.loadingProgress = false;
                    $ctrl.object.situacao = "Cancelado";
                    carregarAbaCentroLucro();
                    carregarAbaMotivoCancelamento();
                    $scope.notification.success(undefined, "Contrato cancelado com sucesso");
                    fn_Hide();
                }).catch((response) => {
                    console.error('crud-critical', 'dialog', response);
                    $rootScope.loadingProgress = false;
                    $scope.notification.criticalError(response.status, response.statusText, response.data);
                });
        };

        // Função que será executada no cancelar no dialog de motivo de retorno.
        $ctrl.cancelarMotivo = (fn_Hide) => {
            fn_Hide();
        };

        // Passar para o serviço, as funções que serão chamadas ao confirmar ou cancelar o motivo de retorno.
        MotivoRetornoDialog.show(event, chaveMotivo, titulo, $ctrl.confirmarMotivo, $ctrl.cancelarMotivo);
    };

    /* Inicio da Dialog Paralizar Contrato */
    $ctrl.paralizarContrato = (event) => {
        let total = 0;
        angular.forEach($ctrl.listCentroLucro, (data) => {
            if (data.situacao !== 'Encerrado' && data.situacao !== 'Cancelado' && data.situacao !== 'Paralisado') {
                total++;
            }
        });

        // Chamada do serviço que criará um dialog de confirmação.
        DialogConfirm.show(
            undefined,
            $ctrl.object,
            'Paralizar contrato: Confirmação',
            `Deseja realmente paralisar este contrato e o(s) ${total} centro(s) de lucro associado(s)?`,
            'Confirmar',
            'Cancelar',
            confirmParalizarContrato,
            cancelParalizarContrato);
    };

    const confirmParalizarContrato = (object, fnHide) => {

        let codigo = $ctrl.object.codigo;
        $ctrl.object = undefined;
        $ctrl.listCentroLucro = undefined;

        let pParalisar = Contrato.paralisarContrato({
            id: codigo
        }).$promise;

        pParalisar.then((data) => {

            let message = 'Contrato paralisado com sucesso.';
            $scope.notification.success(message);

            loadObj();
            $mdDialog.hide();

        }).catch(function (response) {
            $scope.action = false;
            $rootScope.loadingProgress = false;
            console.error('crud-critical', 'dialog', response);
            $scope.notification.criticalError(response.status, response.statusText, response.data);
        });
    }; /*Fim da Dialog Paralizar Contrato*/

    const cancelParalizarContrato = (fnHide) => {
        fnHide();
    };

    $ctrl.adicionarNovoEndereco = (item) => {

        $mdDialog.show({
            controller: DialogEndereco,
            locals: {
                notification: $scope.notification,
                contrato: $ctrl.object,
                listContratoEndereco: $ctrl.listContratoEndereco,
                ContratoEndereco: ContratoEndereco,
                Cidade: Cidade,
                item: item
            },
            templateUrl: 'partials/comercial/contratos/html/dialogs/dialog_endereco.html',
            parent: angular.element(document.body),
            clickOutsideToClose: false,
            fullscreen: false
        });
    };

    const DialogEndereco = ($scope, $mdDialog, notification, contrato, listContratoEndereco, ContratoEndereco, Cidade, item) => {
        $scope.title = undefined;

        let codContrato = contrato.id;
        $scope.habilitarComboTipoLogradouro = false;

        if (item === undefined) {
            $scope.title = "Novo";
            $scope.object = {};
            $scope.habilitarComboTipoLogradouro = true;

        } else {
            $scope.title = "Edição";
            $scope.object = angular.copy(item);
            $scope.selectedItemCliente = $scope.object.endereco.cidade;
            $scope.habilitarComboTipoLogradouro = false;
        }

        // Campo "Tipo" da Dialog Endereço
        const carregarCampoTipo = () => {
            EnumService.enumValues({
                pacote: 'br.com.araujoabreu.sgi.administracao.enums',
                classe: 'null',
                enum: 'TipoEnderecoContrato'
            })
                .$promise.then((data) => {
                    $scope.listTipo = [];
                    angular.forEach(data, (value) => {
                        $scope.listTipo.push(value);
                    });
                }).catch((response) => {
                    $scope.$emit('crud-critical', 'load', response);
                });
        };
        carregarCampoTipo();

        const carregarTipoLogradouro = () => {

            Contrato.buscarTipoLogradouro().$promise.then((data) => {
                $scope.listTipoLogradouro = angular.copy(data);

            }).catch((response) => {
                $scope.$emit('crud-critical', 'tipoLogradouro', response);
            });
        };
        carregarTipoLogradouro();

        //AUTOCOMPLETE CIDADE/UF
        $scope.querySearchCidade = (query) => {
            let nomeCidade = query;
            let listaCidade = [];

            let pCidade = Cidade.buscarListaPorNome(nomeCidade).$promise;

            return pCidade.then((data) => {
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

        $scope.selectedItemChange = (item) => {
            $scope.object.endereco.cidade = item;
            $scope.object.endereco.uf = item.uf;
        };
        //FIM AUTOCOMPLETE

        $scope.confirm = () => {
            $scope.object.contrato = contrato;

            if (listContratoEndereco.lenght === 0) {
                $scope.object.principal = true;
            } else {
                $scope.object.principal = false;
            }

            if ($scope.object.codigo == undefined) {

                ContratoEndereco.save($scope.object)
                    .$promise.then((data) => {

                        listContratoEndereco.push($scope.object);
                        notification.success("Endereço inserido com sucesso.");
                        carregarAbaContratoEndereco();


                    }).catch((response) => {
                        $scope.$emit('crud-critical', 'save', response);
                    });
            } else {

                $scope.object.codigo = item.codigo;

                ContratoEndereco.update({
                    id: $scope.object.codigo
                }, $scope.object)
                    .$promise.then((data) => {

                        notification.success("Endereço atualizado com sucesso.");
                        carregarAbaContratoEndereco();

                    }).catch((response) => {
                        $scope.$emit('crud-critical', 'update', response);
                    });
            }
            $scope.hide();
        };

        $scope.hide = () => {
            $mdDialog.hide();
        };

        $scope.cancel = () => {
            $mdDialog.cancel();
        };
    }; // Fim da Dialog Endereço

    /* Inicio da Dialog Excluir Endereco */
    $ctrl.excluirEndereco = (item) => {
        DialogConfirm.show(
            undefined,
            item,
            'Remover: Confirmação',
            'Deseja realmente <b>remover</b> o endereco (esta operação não poderá ser desfeita)?',
            'Confirmar',
            'Cancelar',
            confirmRemoverEndereco,
            cancelRemoverEndereco);
    };

    const confirmRemoverEndereco = (item, fnHide) => {

        // Remover da tela de exibição!
        ContratoEndereco.remover({
            id: item.codigo
        })
            .$promise.then(function (data) {

                $scope.notification.success("Endereço removido com sucesso.");
                carregarAbaContratoEndereco();
                fnHide();

            }).catch(function (response) {
                $scope.$emit('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
                fnHide();
            });
    };

    const cancelRemoverEndereco = (fnHide) => {
        fnHide();
    };
    /* Fim da Dialog Excluir Endereco */

    /* Inicio da Dialog Excluir Centro de Lucro (Centro Financeiro) */
    $ctrl.removerCentroLucro = (item) => {
        DialogConfirm.show(
            undefined,
            item,
            'Remover centro financeiro: Confirmação',
            'Deseja realmente <b>remover</b> este centro financeiro ?',
            'Confirmar',
            'Cancelar',
            confirmRemoveCentroFinanceiro,
            cancelRemoveCentroFinanceiro);
    };

    const confirmRemoveCentroFinanceiro = (item, fnHide) => {

        //TODO: Está funcionando, porém Centro Financeiro ainda não está terminado! - 18/07/2017
        // let pRemoverCf = Contrato.removeCentroFinanceiro({ id: item.codigo }).$promise;

        // Foi utilizado o 'CentroFinanceiro.removerById',  porque o 'Contrato.removeCentroFinanceiro' não estava funcionando.
        let pRemoverCf = CentroFinanceiro.removerById({ id: item.codigo }).$promise;

        pRemoverCf.then((data) => {

                $scope.notification.success("Centro financeiro removido com sucesso.");
                carregarAbaCentroLucro();
                fnHide();

            }).catch((response) => {
                $scope.$emit('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
                fnHide();
            });
    };

    const cancelRemoveCentroFinanceiro = (fnHide) => {
        fnHide();
    };
    /* Fim da Dialog Excluir Centro de Lucro (Centro Financeiro) */

    /* Inicio na Dialog ADITIVO */
    $ctrl.adicionarNovoAditivo = (item) => {

        $mdDialog.show({
            controller: DialogAditivo,
            locals: {
                notification: $scope.notification,
                contrato: $ctrl.object,
                listAditivos: $ctrl.listAditivos,
                Aditivo: Aditivo,
                item: item
            },
            templateUrl: 'partials/comercial/contratos/html/dialogs/dialog_aditivo.html',
            parent: angular.element(document.body),
            clickOutsideToClose: false,
            fullscreen: false
        });
    };

    const DialogAditivo = ($scope, $mdDialog, notification, contrato, item, Aditivo, listAditivos, $timeout) => {
        $scope.title = "Aditivo - Dados Gerais";
        $scope.object = {};

        let codContrato = contrato.id;

        /**
         * Obtem a diferença de duas datas em meses com arredondamente.
         * </br>
         * Exemplo 1: Se a data inicial for 01/09/2015 e a data final for 15/09/2015 o retorno será 0 (zero).
         * </br>
         * Exemplo 2: Se a data inicial for 01/09/2015 e a data final for 16/09/2015 o retorno será 1 (um).
         */
        $scope.calcularPrazo = (item) => {

            if ((item.dataInicioVigencia !== undefined) && (item.dataFimVigencia !== undefined)) {

                let dataInicioVigencia = new Date(item.dataInicioVigencia);
                let dataFimVigencia = new Date(item.dataFimVigencia);

                let dt1 = dataInicioVigencia.getTime();
                let dt2 = dataFimVigencia.getTime();

                let dias = (((dt2 - dt1) + 3600000));
                let meses = ((dias / 86400000) / 30);
                // Math.round -> Função que faz o arredondamente da conta
                let mesesRedondo = Math.round(meses);

                item.prazoAtual = mesesRedondo;
            }
        };

        if (item == undefined) { // Novo Contrato

            if (angular.isUndefined(listAditivos) || listAditivos == undefined || listAditivos.length == 0 || listAditivos.length == 1) {
                $scope.object.numero = 1;

            } else {
                $scope.object.numero = listAditivos.length;
            }

        } else {

            // É necessário usar esse timeout para carregar os componentes de data da dialog
            $timeout(() => {
                $scope.object = angular.copy(item);
                $scope.calcularPrazo($scope.object);
            }, 500);
        }

        // Campo "Reajuste" da Dialog Aditivo
        const carregarCampoReajuste = () => {
            EnumService.enumValues({
                pacote: 'br.com.araujoabreu.sgi.administracao.domain',
                classe: 'Contrato',
                enum: 'PeriodoReajuste'
            })
                .$promise.then((data) => {
                    $scope.listReajuste = [];
                    $scope.listReajuste.push('');
                    angular.forEach(data, (value) => {
                        $scope.listReajuste.push(value);
                    });
                }).catch((response) => {
                    $scope.$emit('crud-critical', 'load', response);
                });
        };
        carregarCampoReajuste();

        $scope.confirm = () => {
            $scope.object.contrato = contrato;

            if ($scope.object.codigo == undefined) {

                Aditivo.save($scope.object)
                    .$promise.then((data) => {

                        listAditivos.push($scope.object);
                        notification.success("Aditivo inserido com sucesso.");
                        loadObj();

                    }).catch((response) => {
                        $scope.$emit('crud-critical', 'save', response);
                    });
            } else {

                $scope.object.codigo = item.codigo;

                Aditivo.update({
                    id: $scope.object.codigo
                }, $scope.object)
                    .$promise.then((data) => {

                        notification.success("Aditivo atualizado com sucesso.");
                        loadObj();

                    }).catch((response) => {
                        $scope.$emit('crud-critical', 'update', response);
                    });
            }
            $scope.hide();
        };

        $scope.hide = () => {
            $mdDialog.hide();
        };

        $scope.cancel = () => {
            $mdDialog.cancel();
        };
    }; /* Fim na Dialog ADITIVO */

    /* Inicio da Dialog Excluir Aditivo */
    $ctrl.excluirAditivo = (item) => {
        DialogConfirm.show(
            undefined,
            item,
            'Remover aditivo: Confirmação',
            'Deseja realmente <b>remover</b> o aditivo (esta operação não poderá ser desfeita)?',
            'Confirmar',
            'Cancelar',
            confirmRemoverAditivo,
            cancelRemoverAditivo);
    };

    const confirmRemoverAditivo = (item, fnHide) => {

        Aditivo.remover({
            id: item.codigo
        })
            .$promise.then((data) => {

                $scope.notification.success("Aditivo removido com sucesso.");
                loadObj();
                fnHide();
            }).catch((response) => {
                $scope.$emit('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
                fnHide();
            });
    };

    const cancelRemoverAditivo = (fnHide) => {
        fnHide();
    };
    /* Fim da Dialog Excluir Aditivo */

    /* Inicio na Dialog ANEXO */
    $ctrl.adicionarNovoAnexo = (item) => {
        $mdDialog.show({
            controller: DialogAnexo,
            locals: {
                notification: $scope.notification,
                contrato: $ctrl.object,
                listAditivos: $ctrl.listAditivos,
                listAnexos: $ctrl.listAnexos,
                item: item
            },
            templateUrl: 'partials/comercial/contratos/html/dialogs/dialog_anexo.html',
            parent: angular.element(document.body),
            clickOutsideToClose: false,
            fullscreen: false
        });

    };

    const DialogAnexo = ($scope, $mdDialog, notification, contrato, listAnexos, listAditivos, item) => {
        $scope.title = undefined;
        $scope.object = {};
        let codContrato = contrato.id;
        $scope.habilitarAditivo = false;
        $scope.habilitarSalvar = false;
        $scope.habilitarBtnArquivo = undefined;
        $scope.habilitarBtnLimpar = undefined;
        $scope.habilitarBtnProcessar = undefined;

        if (item === undefined) {
            $scope.title = "Novo";
            $scope.habilitarBtnArquivo = false;
            $scope.habilitarBtnLimpar = true;
            $scope.habilitarBtnProcessar = true;


        } else {
            $scope.title = "Edição";
        	$scope.object = angular.copy(item);
        	$scope.object.descricao = $scope.object.anexo.descricao;

            $scope.habilitarBtnArquivo = false;
            $scope.habilitarBtnLimpar = true;
  			$scope.habilitarBtnProcessar = true;
  			$scope.habilitarSalvar = true;
        }

        // COMBO "Tipo" da Dialog Anexo
        const carregarCampoTipo = () => {
            EnumService.enumValues({
                pacote: 'br.com.araujoabreu.sgi.administracao.enums',
                classe: 'null',
                enum: 'TipoAnexo'
            })
                .$promise.then((data) => {
                    $scope.listTipo = [];
                    $scope.listTipo.push('');

                    angular.forEach(data, (value) => {
                        // Preencher com ou sem o tipo 'Aditivo' na COMBO "Tipo"
                        if (value === 'Aditivo') {
                            if (listAditivos !== undefined && listAditivos.length > 1) {
                                $scope.listTipo.push(value);
                            }
                        } else {
                            $scope.listTipo.push(value);
                        }
                    });

                    carregarComboAditivos();

                }).catch(function (response) {
                    $scope.$emit('crud-critical', 'load', response);
                    $scope.notification.criticalError(response.status, response.statusText, response.data);
                });
        };
        carregarCampoTipo();

        // Usado para o Modo de Edição - A combo vem selecionada!
        const carregarComboAditivos = () => {

            if ($scope.object.tipo === "Aditivo") {
                // Habilita a combo Aditivo
                $scope.habilitarAditivo = true;

                carregarAbaAditivos();

                // Faz uma cópia das listas
                $scope.listAditivos = listAditivos;

                // "splice": serve para tirar o 1º elemento da lista!
                // A regra de negócio não permite que o 1º aditivo entre na combo "Aditivo"
                $scope.listAditivos.splice(0, 1);
            }
        };

        // ONCHANGE - Habilitar a combo Aditivo
        $scope.selecionarAditivo = () => {

            if ($scope.object.tipo === "Aditivo") {
                carregarComboAditivos();

            } else {
                $scope.habilitarAditivo = false;
            }
        };

        const tipoArquivoValido = () => {
            let tipoArquivoValido = false;

            if ($scope.fileName && $scope.file) {

                //console.log('Arquivo: ', $scope.file); application/pdf image/tif

                $scope.habilitarBtnLimpar = false;

                if ($scope.file.type === "text/plain" || $scope.file.type === "image/jpeg" || $scope.file.type === "application/pdf" ||
                    $scope.file.type === "image/tif" || $scope.file.type === "image/png" || $scope.file.type === "application/msword" ||
                    $scope.file.type === "application/vnd.openxmlformats-officedocument.wordprocessingml.document" ||
                    $scope.file.type === "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" ||
                    $scope.file.type === "application/vnd.ms-excel" || $scope.file.type === "image/gif" || $scope.file.type === "image/bmp") {

                    tipoArquivoValido = true;
                }

                if (!tipoArquivoValido) {
                    notification.error("Erro", "Este arquivo não possui um tipo válido");
                }
            }
            return tipoArquivoValido;
        };

        $scope.processar = () => {
            let reader = new FileReader();

            tipoArquivoValido();

            if(!tipoArquivoValido()) {
            	return
            } else {
            	reader.onload = (event) => {
                	$scope.$apply(() => {
                    	//Montando o objeto
                        let anexo = {
                        	texto: reader.result,
                            nome: $scope.file.name,
                            contentType: $scope.file.type,
                            descricao: ""
                        };

                        contrato.anexo = anexo;
                        $scope.habilitarSalvar = true;
                    });
                };

                reader.readAsText($scope.file);
                notification.success("Arquivo processado com sucesso!");
            }
        };

        $scope.reset = () => {
            $scope.listGerencias = [];
            $scope.result = undefined;
            $scope.clear();
        };

        $scope.$on('limparTudo', () => {
        	$scope.habilitarSalvar = false;
            $scope.file = undefined;
            $scope.fileName = undefined;
            $scope.file = false;
        })

        // Salvar
        $scope.adicionarAnexo = () => { // object = ContratoAnexo

            if ($scope.object.codigo === undefined) { // Novo Anexo
                $scope.object.contrato = contrato;
                $scope.object.anexo = contrato.anexo;
                $scope.object.anexo.descricao = $scope.object.descricao;

                if (angular.isUndefined(listAnexos) || listAnexos === undefined || listAnexos.length === 0) {
                    $scope.object.ordem = 0;
                } else {
                    $scope.object.ordem = listAnexos.length;
                }

                ContratoAnexo.save($scope.object)
                    .$promise.then((data) => {

                        listAnexos.push($scope.object);
                        notification.success("Anexo inserido com sucesso.");
                        carregarAbaAnexos();

                    }).catch((response) => {
                        notification.criticalError(response.status, response.statusText, response.data);
                        console.error(response);
                    });
            } else { // Edição do Anexo

                $scope.object.codigo = item.codigo;

                ContratoAnexo.update({
                    id: $scope.object.codigo
                }, $scope.object)
                    .$promise.then((data) => {

                        notification.success("Anexo atualizado com sucesso.");
                        carregarAbaAnexos();

                    }).catch((response) => {
                        $scope.$emit('crud-critical', 'update', response);
                    });
            }
            $scope.hide();
        };

        $scope.hide = () => {
            $mdDialog.hide();
        };

        $scope.cancel = () => {
            $mdDialog.cancel();
        };
    }; /* Fim na Dialog ANEXO */

    /* Inicio da Dialog Excluir Anexo */
    // FIXME: ESTÁ EXCLUINDO APENAS NO BANCO E NÃO O ARQUIVO FÍSICO!
    $ctrl.excluirAnexo = (item) => {
        // Chamada do serviço que criará um dialog de confirmação.
        DialogConfirm.show(
            undefined,
            item,
            'Remover anexo: Confirmação',
            'Deseja realmente <b>remover</b> este anexo (esta operação não poderá ser desfeita)?',
            'Confirmar',
            'Cancelar',
            confirmRemoverAnexo,
            cancelRemoverAnexo);
    };

    const confirmRemoverAnexo = (item, fnHide) => {

        ContratoAnexo.remover({
            id: item.codigo
        })
            .$promise.then((data) => {

                $scope.notification.success("Anexo removido com sucesso.");
                carregarAbaAnexos();
                fnHide();

            }).catch((response) => {
                $scope.$emit('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
                fnHide();
            });
    };

    const cancelRemoverAnexo = (fnHide) => {
        fnHide();
    };
    /* Fim da Dialog Excluir Anexo */

    /* Inicio na Dialog CONTATO */
    $ctrl.adicionarNovoContato = (item) => {

        $mdDialog.show({
            controller: DialogContato,
            locals: {
                notification: $scope.notification,
                contrato: $ctrl.object,
                listContato: $ctrl.listContato,
                item: item
            },
            templateUrl: 'partials/comercial/contratos/html/dialogs/dialog_contrato_contato.html',
            parent: angular.element(document.body),
            clickOutsideToClose: false,
            fullscreen: false
        });
    };

    const DialogContato = ($scope, $mdDialog, notification, contrato, item, listContato) => {
        $scope.title = undefined;
        $scope.object = {};
        let codContrato = contrato.id;
        $scope.desabilitar1 = true;
        $scope.desabilitar2 = true;

        if (item === undefined) {
            $scope.title = "Novo";

        } else {
            $scope.title = "Edição";
            $scope.object = angular.copy(item);

            //Desabilitar campos de telefones
            if ($scope.object.contato.telefone1 != undefined) {
                $scope.desabilitar1 = false;
            }
            if ($scope.object.contato.telefone2 != undefined) {
                $scope.desabilitar2 = false;
            }
        }

        carregarRelacionamento = () => {
            ContratoContato.buscarRelacionamento().$promise.then((data) => {
                $scope.listRelacionamento = angular.copy(data);
            }).catch((response) => {
                $scope.$emit('crud-critical', 'classificacao', response);
            });
        };


        /* CARREGAR COMBOS */

        initCampoTipoTelefone = () => {
            let pTipoTelefone = EnumService.enumValues({
                pacote: 'br.com.fasolti.corporativo.pessoa.domain',
                classe: 'Telefone',
                enum: 'TipoTelefone'
            }).$promise;

            pTipoTelefone.then((data) => {
                $scope.listTipoTelefone = [];
                $scope.listTipoTelefone.push("");
                angular.forEach(data, (value) => {
                    $scope.listTipoTelefone.push(value);
                });

            }).catch((response) => {
                $scope.$emit('crud-critical', 'load', response);
            });
        };

        carregarOperadoras = () => {
            Telefone.buscarOperadora().$promise.then((data) => {
                $scope.listOperadoras = angular.copy(data);
            }).catch((response) => {
                $scope.$emit('crud-critical', 'classificacao', response);
            });
        };

        carregarRelacionamento();
        initCampoTipoTelefone();
        carregarOperadoras();

        /*ON CHANGE*/
        $scope.desabilitarCamposTel1 = () => {
            if ($scope.object.contato.telefone1.tipo == undefined || $scope.object.contato.telefone1.tipo == '') {
                $scope.desabilitar1 = true;
                $scope.object.contato.telefone1 = undefined;
            } else {
                $scope.desabilitar1 = false;
            }
        };

        $scope.desabilitarCamposTel2 = () => {
            if ($scope.object.contato.telefone2.tipo == undefined || $scope.object.contato.telefone2.tipo == '') {
                $scope.desabilitar2 = true;
                $scope.object.contato.telefone2 = undefined;
            } else {
                $scope.desabilitar2 = false;
            }
        };

        $scope.confirm = () => {
            /*Verificar se foi adicionado telefone ou e-mail*/
            if ($scope.object.contato.telefone1 === undefined && $scope.object.contato.telefone2 === undefined && $scope.object.contato.email === undefined) {
                notification.atencao("Adicione um telefone ou e-mail.");
            } else {
                if (item === undefined) {
                    $scope.object.contrato = contrato;

                    // Salvar
                    ContratoContato.save($scope.object)
                        .$promise.then((data) => {

                            listContato.push($scope.object);
                            notification.success("Contato inserido com sucesso.");
                            carregarAbaContato();
                        }).catch((response) => {
                            $scope.$emit('crud-critical', 'save', response);
                        });

                } else {
                    $scope.object.codigo = item.codigo;

                    //Update
                    ContratoContato.update({
                        id: $scope.object.codigo
                    }, $scope.object)
                        .$promise.then((data) => {

                            notification.success("Contato atualizado com sucesso.");
                            carregarAbaContato();
                        }).catch((response) => {
                            $scope.$emit('crud-critical', 'update', response);
                        });
                }
                $scope.hide();
            }
        };

        $scope.hide = () => {
            $mdDialog.hide();
        };

        $scope.cancel = () => {
            $mdDialog.cancel();
        };
    };
    /* Fim na Dialog CONTATO */

    /* Inicio da Dialog Excluir Contato */
    $ctrl.excluirContato = (item) => {

        // Chamada do serviço que criará um dialog de confirmação.
        DialogConfirm.show(
            undefined,
            item,
            'Remover contato: Confirmação',
            'Deseja realmente <b>remover</b> este contato (esta operação não poderá ser desfeita)?',
            'Confirmar',
            'Cancelar',
            confirmRemoverContato,
            cancelRemoverContato);
    };

    const confirmRemoverContato = (item, fnHide) => {

        ContratoContato.remover({
            id: item.codigo
        })
            .$promise.then((data) => {

                $scope.notification.success("Contato removido com sucesso.");
                carregarAbaContato();
                fnHide();

            }).catch((response) => {
                $scope.$emit('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
                fnHide();
            });
    };

    const cancelRemoverContato = (fnHide) => {
        fnHide();
    };
    /* Fim da Dialog Excluir Contato */

    $scope.editar = () => {
        $state.go('contratos.edit', {
            objectId: $scope.object.codigo
        });
    };

    $scope.cancelarEdit = () => {
        if ($scope.screenMode === 'NEW') {
            $state.go('contratos.list');
        } else if ($scope.screenMode === 'EDIT') {
            $state.go('contratos.view', {
                objectId: $ctrl.object.codigo
            });
        }
    };

    $ctrl.calcularRetencao = () => {
        if ($ctrl.object.percentualRetencao > 0) {
            $ctrl.object.retencao = ($ctrl.object.valorTotal * $ctrl.object.percentualRetencao) / 100;
        } else {
            $ctrl.object.retencao = 0;
        }
    };

    $ctrl.exibirResumoAditivo = (item, event) => {
        $mdDialog.show({
            controller: dialogResumoAditivo,
            locals: {
                item: item
            },
            templateUrl: 'partials/comercial/contratos/html/dialogs/dialog_detalhe_total.html',
            parent: angular.element(document.body),
            clickOutsideToClose: false,
            fullscreen: false
        });
    };

    const dialogResumoAditivo = ($scope, $mdDialog, item) => {

        $scope.item = item;

        $scope.hide = () => {
            $mdDialog.hide();
        };

        $scope.cancel = () => {
            $mdDialog.cancel();
        };

    };

    $ctrl.downloadAnexo = (item) => {
    	ContratoAnexo.verificaranexo({id: item.anexo.codigo})
			.$promise.then((data) => {
				if(data.codigo)
					$window.location = '/sgi/nv/rest/contratoanexo/' + item.anexo.codigo + '/baixaranexo';
				else
					$scope.notification.error("", "Arquivo não encontrado!");
			}).catch((response) => {
				$scope.$emit('crud-critical', 'dialog', response);
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});
	};

    $ctrl.top = function() {
        var tela = $('md-content');
        tela.prop("scrollTop", tela.prop("scrollHeight"));
    };

    $ctrl.bottom = function() {
        $("md-content").scrollTop(0);
    };

    $ctrl.recalcular = () => {
        const confirm = (object, fnHide) => {
            $ctrl.action = true;
            Contrato.atualizarTotais({id: $ctrl.object.codigo}).$promise.then(() => {
                loadObj().then(() => {
                    $scope.notification.success('Os valores do contrato foram recalculados com sucesso.');
                    $ctrl.action = false;
                    $mdDialog.hide();
                });

            }).catch((response) => {
                $scope.action = false;
                $rootScope.loadingProgress = false;
                console.error('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
            });
        };

        const cancel = (fnHide) => {
            $mdDialog.hide();
        }

        DialogConfirm.show(
            undefined,
            $ctrl.object,
            'Recalcular contrato: Confirmação',
            `Deseja <b>recalcular</b> o valor mensal e o valor total?`,
            'Confirmar',
            'Cancelar',
            confirm,
            cancel);
    }

    init();
}
