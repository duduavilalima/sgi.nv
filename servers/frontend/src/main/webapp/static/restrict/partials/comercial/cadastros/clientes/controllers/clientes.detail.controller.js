angular.module('app.clientes').controller('ClientesDetailCtrl', detailController);

function detailController($scope, $state, $stateParams, $mdDialog, $rootScope, EnumService, PessoaFisica, PessoaJuridica,
    Clientes, AutocompleteService, DialogConfirm, Endereco, Cidade, PessoaContaContabil, Contato, UsuarioLogadoService) {

    const $ctrl = this;

    let init = () => {
        $ctrl.habilitar = true;
        $ctrl.bloquearCNPJ = false;
        $ctrl.object = undefined;
        $ctrl.insHab = true;
        $ctrl.preencherManualmente = false;

        if ($scope.screenMode === 'NEW') {
            newObj();
        } else {
            loadObj();
        }
    };

    let loadObj = () => {
        const pClientes = Clientes.get({id: $stateParams.objectId}).$promise;

        pClientes.then(function(data) {
            $ctrl.object = data;

            carregarDadosContabeis();
            carregarSubclientes();
            carregarContratos();
            carregarTelefones();
            carregarContatos();

            if ($ctrl.object.tipoPessoa === 'Pessoa Física') {
                $ctrl.tipoCliente = 'clientePF';
            } else {
                $ctrl.tipoCliente = 'clientePJ';
            }
            if ($ctrl.object.clientePrincipal) {
                $ctrl.object.subCliente = 'Sim';
            } else {
                $ctrl.object.subCliente = 'Não';
            }

            if ($scope.screenMode === 'EDIT') {
                $ctrl.bloquearCNPJ = true;
                initEdit();
            }

        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    /* Carregar Abas*/
    let carregarContatos = () => {
        Clientes.buscarContatos({
            id: $scope.objectId
        }).$promise.then(function(data) {
            $ctrl.listaContatos = angular.copy(data);
        }).catch(function(response) {
            $scope.$emit('crud-critical', 'contato', response);
        });
    };

    let carregarTelefones = () => {
        Clientes.buscarTelefones({
            id: $scope.objectId
        }).$promise.then(function(data) {
            $ctrl.listaTelefones = angular.copy(data);
        }).catch(function(response) {
            $scope.$emit('crud-critical', 'telefone', response);
        });
    };

    let carregarDadosContabeis = () => {
        Clientes.buscarDadosContabeis({
            id: $scope.objectId
        }).$promise.then(function(data) {
            $ctrl.listaDadosContabeis = angular.copy(data);
            if ($ctrl.listaDadosContabeis.length === 2) {
                $ctrl.desabilitarBotaoNovaCC = true;
            } else {
                $ctrl.desabilitarBotaoNovaCC = false;
            }
        }).catch(function(response) {
            $scope.$emit('crud-critical', 'dadosContabeis', response);
        });
    };

    let carregarSubclientes = () => {
        Clientes.buscarSubclientes({
            id: $scope.objectId
        }).$promise.then(function(data) {
            $ctrl.listaSubclientes = data;
        }).catch(function(response) {
            $scope.$emit('crud-critical', 'subcliente', response);
        });
    };

    let carregarContratos = () => {
        Clientes.buscarContratos({
            id: $scope.objectId
        }).$promise.then(function(data) {
            $ctrl.listaContratos = angular.copy(data);
        }).catch(function(response) {
            $scope.$emit('crud-critical', 'contrato', response);
        });
    };
    /* FIM  Carregar Abas*/

    $ctrl.editar = function() {
        $state.go('clientes.edit', {
            objectId: $ctrl.object.codigo
        });
    };

    $ctrl.retornar = function() {
        $state.go('clientes.list');
    };

    $ctrl.cancelarEdit = function() {
        if ($scope.screenMode === 'NEW') {
            $state.go('clientes.list');
        } else if ($scope.screenMode === 'EDIT') {
            $state.go('clientes.view', {
                objectId: $ctrl.object.codigo
            });
        }
    };

    $ctrl.salvar = function() {
        $ctrl.object.telefones = angular.copy($ctrl.listaTelefones);
        if ($scope.screenMode === 'NEW' && !$ctrl.object.codigo) {
            if ($ctrl.object.tipoPessoa === 'Pessoa Física') {
                PessoaFisica.save($ctrl.object)
                    .$promise.then(function(data) {
                        $scope.notification.success('Novo', 'Cliente inserido com sucesso.');
                        $state.go('clientes.view', {
                            objectId: data.codigo
                        });
                    }).catch(function(response) {
                        $scope.$emit('crud-critical', 'save', response);
                    });

            } else {

                PessoaJuridica.save($ctrl.object)
                    .$promise.then(function(data) {
                        $scope.notification.success('Novo', 'Cliente inserido com sucesso.');
                        $state.go('clientes.view', {
                            objectId: data.codigo
                        });
                    }).catch(function(response) {
                        $scope.$emit('crud-critical', 'save', response);
                    });
            }

        } else if ($scope.screenMode === 'EDIT' || $ctrl.object.codigo) {
            let entity;
            if ($ctrl.object.tipoPessoa === 'Pessoa Física') {
                entity = angular.copy($ctrl.object);
                PessoaFisica.update({
                    id: $ctrl.object.codigo
                }, entity).$promise.then(function(data) {
                    $scope.notification.success('Edição', 'Cliente atualizado com sucesso.');
                    $state.go('clientes.view', {
                        objectId: data.codigo
                    });
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'save', response);
                });
            } else {
                entity = angular.copy($ctrl.object);
                PessoaJuridica.update({
                    id: $ctrl.object.codigo
                }, entity).$promise.then(function(data) {
                    $scope.notification.success('Edição', 'Cliente atualizado com sucesso.');
                    $state.go('clientes.view', {
                        objectId: data.codigo
                    });
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'save', response);
                });
            }
        }
    };

    /*Inicio Dialog Remover*/
    $ctrl.remover = function(ev) {
        let tituloDialog = 'Cliente - Remover: Confirmação';
        let msgDialog = 'Deseja realmente remover este cliente (esta operação não poderá ser desfeita)?';
        let tituloBotaoConfirmar = 'CONFIRMAR';
        let tituloBotaoCancelar = 'CANCELAR';

        let funcaoCancelar = function(fnHide) {
            fnHide();
        };

        let funcaoConfirmar = function(object, fnHide) {
            let subcliente = $ctrl.object.subCliente;
            const pClientes = Clientes.removerCliente({
                id: object.codigo
            }).$promise;

            pClientes.then(function(data) {
                let title;
                let message;

                if (subcliente && subcliente === 'Sim') {
                    title = 'Remover Subliente';
                    message = `Subliente ${object.nome} removido com sucesso.`;
                } else {
                    title = 'Remover Cliente';
                    message = `Cliente ${object.nome} removido com sucesso.`;
                }
                $scope.notification.success(title, message);

                fnHide();
                $state.go('clientes.list');

            }).catch(function(response) {
                console.error('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
                fnHide();
            });
        };

        DialogConfirm.show(ev, $ctrl.object, tituloDialog, msgDialog, tituloBotaoConfirmar, tituloBotaoCancelar, funcaoConfirmar, funcaoCancelar);
    };
    /*FIM Dialog Remover*/

    /*DIALOG CONTATO TELEFONE*/
    $ctrl.contatoTelefone = function(item) {
        $mdDialog.show({
            controller: DialogContatoTelefone,
            locals: {
                item: item
            },
            templateUrl: 'partials/comercial/cadastros/clientes/html/dialogs/dialog_contato_telefone.html',
            parent: angular.element(document.body),
            clickOutsideToClose: false,
            fullscreen: false
        });
    };

    let DialogContatoTelefone = ($scope, $mdDialog, item) => {
        $scope.object = item;

        $scope.hide = function() {
            $mdDialog.hide();
        };
        $scope.cancel = function() {
            $mdDialog.cancel();
        };
    }; /* FIM DA DIALOG CONTATO TELEFONE */

    let newObj = () => {
        $ctrl.object = new Clientes();
        $ctrl.object.endereco = new Endereco();
        $ctrl.object.endereco.cidade = new Cidade();
        $ctrl.object.situacao = 'Ativo';
        $ctrl.object.subCliente = 'Não';
        $ctrl.object.faturamentoCobranca = false;
        $ctrl.object.canalPreferencial = 'Correspondência';
        $ctrl.preencherManualmente = false;
        $ctrl.listaContatos = [];
        $ctrl.listaTelefones = [];
        $ctrl.listaDadosContabeis = [];

        if ($stateParams.objectTipo === 0) {
            $ctrl.tipoCliente = 'clientePF';
            $ctrl.object.tipoPessoa = 'Pessoa Física';
            $ctrl.object.tipoPessoaFisica = 'Cliente';
        } else {
            $ctrl.tipoCliente = 'clientePJ';
            $ctrl.object.tipoPessoa = 'Pessoa Jurídica';
            $ctrl.object.tipoPessoaJuridica = 'Cliente';
            $ctrl.object.mei = false;
        }

        carregarClassificacaoCliente();
        initCampoCanalPreferencial();
        carregarTipoLogradouro();
    };

    let initEdit = () => {
        initAutocompletes();
        $ctrl.preencherManualmente = true;
        $ctrl.habilitarCamposIns();
        carregarClassificacaoCliente();
        initCampoCanalPreferencial();
        carregarTipoLogradouro();
    };

    /* Inicio funções Campos */
    $scope.selectedItemCidade = '';

    let initAutocompletes = () => {
        if ($ctrl.object.subCliente === 'Não') {
            $ctrl.habilitar = true;
        } else {
            $ctrl.habilitar = false;
        }
        if (!$ctrl.object.ufInscricaoEstadual || $ctrl.object.ufInscricaoEstadual === '') {
            $ctrl.insHab = true;
        } else {
            $ctrl.insHab = false;
            if ($ctrl.object.ufInscricaoEstadual === 'DF') {
                $ctrl.object.inscricaoMunicipal = 'ISENTO';
            }
        }
        if ($ctrl.object.cnae) {
            $scope.selectedItemRamoNegocio = $ctrl.object.cnae.descricao;
        }
        if ($ctrl.object.clientePrincipal) {
            $scope.selectedItemCliente = $ctrl.object.clientePrincipal.nome;
        }
        $scope.selectedItemCidade = $ctrl.object.endereco.cidade;
    };

    //AUTOCOMPLETE RAMO DE NEGÓCIO
    $ctrl.querySearchRamoNegocio = function(query) {
        let filtro = {nome: query};
        const pRamoNegocio = AutocompleteService.getRamoNegocio(filtro).$promise;

        return pRamoNegocio.then(function(data) {
            let listaRamoNegocio = [];
            listaRamoNegocio.push(...data);
            return listaRamoNegocio;
        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
            return [];
        });
    };

    $ctrl.selectedItemChange = function(item) {
        $ctrl.object.cnae = item;
    };
    //FIM AUTOCOMPLETE

    let carregarClassificacaoCliente = () => {
        Clientes.buscarClassificacaoCliente().$promise.then(function(data) {
            $ctrl.listClassificacao = angular.copy(data);
        }).catch(function(response) {
            $scope.$emit('crud-critical', 'classificacao', response);
        });
    };

    $ctrl.listUfInscricaoEstadual = [{
            sigla: '',
            nome: ''
        },
        {
            sigla: 'AC',
            nome: 'Acre'
        },
        {
            sigla: 'AL',
            nome: 'Alagoas'
        },
        {
            sigla: 'AM',
            nome: 'Amazonas'
        },
        {
            sigla: 'AP',
            nome: 'Amapá'
        },
        {
            sigla: 'BA',
            nome: 'Bahia'
        },
        {
            sigla: 'CE',
            nome: 'Ceará'
        },
        {
            sigla: 'DF',
            nome: 'Distrito Federal'
        },
        {
            sigla: 'ES',
            nome: 'Espírito Santo'
        },
        {
            sigla: 'GO',
            nome: 'Goiás'
        },
        {
            sigla: 'MA',
            nome: 'Maranhão'
        },
        {
            sigla: 'MG',
            nome: 'Minas Gerais'
        },
        {
            sigla: 'MS',
            nome: 'Mato Grosso do Sul'
        },
        {
            sigla: 'MT',
            nome: 'Mato Grosso'
        },
        {
            sigla: 'PA',
            nome: 'Pará'
        },
        {
            sigla: 'PB',
            nome: 'Paraíba'
        },
        {
            sigla: 'PE',
            nome: 'Pernambuco'
        },
        {
            sigla: 'PI',
            nome: 'Piauí'
        },
        {
            sigla: 'PR',
            nome: 'Paraná'
        },
        {
            sigla: 'RJ',
            nome: 'Rio de Janeiro'
        },
        {
            sigla: 'RN',
            nome: 'Rio Grande do Norte'
        },
        {
            sigla: 'RO',
            nome: 'Rondônia'
        },
        {
            sigla: 'RR',
            nome: 'Roraima'
        },
        {
            sigla: 'RS',
            nome: 'Rio Grande do Sul'
        },
        {
            sigla: 'SC',
            nome: 'Santa Catarina'
        },
        {
            sigla: 'SE',
            nome: 'Sergipe'
        },
        {
            sigla: 'SP',
            nome: 'São Paulo'
        },
        {
            sigla: 'TO',
            nome: 'Tocantins'
        }
    ];

    /*ON CHANGE UF INSCRIÇÃO ESTADUAL*/
    $ctrl.habilitarCamposIns = function() {
        if ($ctrl.object.ufInscricaoEstadual && $ctrl.object.ufInscricaoEstadual !== '') {
            $ctrl.insHab = false;
            if ($ctrl.object.ufInscricaoEstadual === 'DF') {
                $ctrl.object.inscricaoMunicipal = 'ISENTO';
            }
        } else {
            $ctrl.insHab = true;
        }
    };

    /* Criado p/ o campo SubCliente*/
    $ctrl.listSubCliente = ['Sim', 'Não'];

    $ctrl.listExcluir = [{
        nome: 'Não',
        valor: false
    }, {
        nome: 'Sim',
        valor: true
    }];

    /*ON CHANGE SUBCLIENTE*/
    $ctrl.desabilitarCampos = function() {
        if ($ctrl.object.subCliente === 'Não') {
            $ctrl.habilitar = true;
            delete $ctrl.object.clientePrincipal;
            delete $scope.selectedItemCliente;
            $ctrl.object.tipoPessoaJuridica = 'Cliente';
        } else {
            $ctrl.habilitar = false;
            $ctrl.object.tipoPessoaJuridica = 'Subcliente';
        }
    };

    //AUTOCOMPLETE CLIENTE
    $ctrl.querySearchCliente = function(query) {
        let filtro = {nome: query};
        const pCliente = AutocompleteService.getPessoaJuridicaCliente(filtro).$promise;

        return pCliente.then(function(data) {
            let listaCliente = [];
            listaCliente.push(...data);
            return listaCliente;
        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
            return [];
        });

    };

    $ctrl.selectedItemChangeC = function(item) {
        $ctrl.object.clientePrincipal = item;
    };
    //FIM AUTOCOMPLETE
    /* FIM Campos */

    /*ABA CONTATO*/
    let initCampoCanalPreferencial = () => {
        $ctrl.listPreferencial = [];

        const pCanalPreferencial = EnumService.enumValues({
            pacote: 'br.com.fasolti.corporativo.pessoa.domain',
            classe: 'PessoaJuridica',
            enum: 'CanalComunicacao'
        }).$promise;

        pCanalPreferencial.then(function(data) {
            $ctrl.listPreferencial.push(...data);
        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
        });
    };
    /*SUB-ABA PESSOAS PARA CONTATO*/
    /*Inicio da Dialog Cliente Contato*/
    $ctrl.novoContato = function(item) {
        $mdDialog.show({
            controller: DialogClienteContato,
            locals: {
                notification: $scope.notification,
                listaContatos: $ctrl.listaContatos,
                cliente: $ctrl.object,
                item: item

            },
            templateUrl: 'partials/comercial/cadastros/clientes/html/dialogs/dialog_cliente_pessoa_contato.html',
            parent: angular.element(document.body),
            clickOutsideToClose: false,
            fullscreen: false
        });
    };

    let DialogClienteContato = ($scope, $mdDialog, notification, item, listaContatos, cliente, Contato, Telefone) => {
        $scope.title = undefined;
        $scope.object = {};
        $scope.desabilitar1 = true;
        $scope.desabilitar2 = true;

        if (!item) {
            $scope.title = 'Novo';
        } else {
            $scope.title = 'Edição';
            $scope.object = angular.copy(item);

            //Desabilitar campos de telefones
            if ($scope.object.telefone1) {
                $scope.desabilitar1 = false;
            }
            if ($scope.object.telefone2) {
                $scope.desabilitar2 = false;
            }
        }

        /* CARREGAR COMBOS */
        let carregarRelacionamento = () => {
            Clientes.buscarRelacionamento().$promise.then(function(data) {
                $scope.listRelacionamento = angular.copy(data);
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'classificacao', response);
            });
        };

        let initCampoTipoTelefone = () => {
            const pTipoTelefone = EnumService.enumValues({
                pacote: 'br.com.fasolti.corporativo.pessoa.domain',
                classe: 'Telefone',
                enum: 'TipoTelefone'
            }).$promise;

            pTipoTelefone.then(function(data) {
                $scope.listTipoTelefone = [];
                $scope.listTipoTelefone.push('');
                $scope.listTipoTelefone.push(...data);
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });
        };

        let carregarOperadoras = () => {
            Telefone.buscarOperadora().$promise.then(function(data) {
                $scope.listOperadoras = angular.copy(data);
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'classificacao', response);
            });
        };

        carregarRelacionamento();
        initCampoTipoTelefone();
        carregarOperadoras();

        /*ON CHANGE*/
        $scope.desabilitarCamposTel1 = function() {
            if (!$scope.object.telefone1.tipo || $scope.object.telefone1.tipo === '') {
                $scope.desabilitar1 = true;
                $scope.object.telefone1 = undefined;
            } else {
                $scope.desabilitar1 = false;
            }
            removerCamposPorTipo($scope.object.telefone1);
        };

        $scope.desabilitarCamposTel2 = function() {
            if (!$scope.object.telefone2.tipo || $scope.object.telefone2.tipo === '') {
                $scope.desabilitar2 = true;
                $scope.object.telefone2 = undefined;
            } else {
                $scope.desabilitar2 = false;
            }
            removerCamposPorTipo($scope.object.telefone2);
        };

        let removerCamposPorTipo = (telefone) => {
            if (telefone) {
                telefone.numero = undefined;
                if (telefone.tipo && telefone.tipo === 'Celular') {
                    telefone.ramal = undefined;
                } else if (telefone.tipo !== 'Celular') {
                    telefone.operadora = undefined;
                } else {
                    telefone.ramal = undefined;
                    telefone.operadora = undefined;
                }
            }
        };

        $scope.confirm = function() {
            /*Verificar se foi adicionado telefone ou e-mail*/
            if (!$scope.object.telefone1 && !$scope.object.telefone2 && !$scope.object.email) {
                notification.atencao('Adicione um telefone ou e-mail.');
            } else {
                if (!item) {
                    $scope.object.pessoa = cliente;
                    // Salvar
                    Contato.save($scope.object)
                        .$promise.then(function(data) {

                            listaContatos.push($scope.object);
                            notification.success('Pessoa para contato inserida com sucesso.');
                            carregarContatos();

                        }).catch(function(response) {
                            $scope.$emit('crud-critical', 'save', response);
                        });

                } else {
                    $scope.object.codigo = item.codigo;

                    //Update
                    Contato.update({
                            id: $scope.object.codigo
                        }, $scope.object)
                        .$promise.then(function(data) {

                            notification.success('Pessoa para contato atualizada com sucesso.');
                            carregarContatos();

                        }).catch(function(response) {
                            $scope.$emit('crud-critical', 'update', response);
                        });
                }

                $scope.hide();
            }
        };

        $scope.hide = function() {
            $mdDialog.hide();
        };
        $scope.cancel = function() {
            $mdDialog.cancel();
        };
    }; /* Fim da Dialog Cliente Contato */

    /* Inicio da Dialog Excluir Contato */
    $ctrl.excluirContato = function(item) {
        // Chamada do serviço que criará um dialog de confirmação.
        DialogConfirm.show(
            undefined,
            item,
            'Remover Pessoa para contato: Confirmação',
            'Deseja realmente remover esta pessoa para contato (esta operação não poderá ser desfeita)?',
            'Confirmar',
            'Cancelar',
            confirmRemoverContato,
            cancelRemoverContato);
    };

    let confirmRemoverContato = (item, fnHide) => {

        Contato.remover({
                id: item.codigo
            })
            .$promise.then(function(data) {
                $scope.notification.success('Pessoa para contato removida com sucesso.');
                carregarContatos();
                fnHide();
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'dialog', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
                fnHide();
            });
    };

    let cancelRemoverContato = (fnHide) => {
        fnHide();
    };
    /* Fim da Dialog Excluir Contato */

    /*SUB-ABA TELEFONE*/
    $ctrl.changeSelectPrincipal = function(item) {
        $ctrl.listaTelefones.forEach(item => {
            item.principal = false;
        });
        item.principal = true;
    };

    /*Inicio da Dialog Cliente Telefone*/
    $ctrl.novoTelefone = function(item) {
        $mdDialog.show({
            controller: DialogClienteTelefone,
            locals: {
                listaTelefones: $ctrl.listaTelefones,
                item: item
            },
            templateUrl: 'partials/comercial/cadastros/clientes/html/dialogs/dialog_cliente_telefone.html',
            parent: angular.element(document.body),
            clickOutsideToClose: false,
            fullscreen: false
        });
    };

    let DialogClienteTelefone = ($scope, $mdDialog, listaTelefones, item, Telefone) => {
        $scope.title = undefined;
        $scope.object = new Telefone();

        if (!item) {
            $scope.title = 'Novo';
            $scope.object.principal = false;
        } else {
            $scope.title = 'Edição';
            $scope.object = item;
        }

        /* CARREGAR COMBOS */
        let initCampoTipoTelefone = () => {
            const pTipoTelefone = EnumService.enumValues({
                pacote: 'br.com.fasolti.corporativo.pessoa.domain',
                classe: 'Telefone',
                enum: 'TipoTelefone'
            }).$promise;

            pTipoTelefone.then(function(data) {
                $scope.listTipoTelefone = [];
                $scope.listTipoTelefone.push('');
                $scope.listTipoTelefone.push(...data);
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });
        };

        let carregarOperadoras = () => {
            Telefone.buscarOperadora().$promise.then(function(data) {
                $scope.listOperadoras = angular.copy(data);
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'classificacao', response);
            });
        };

        initCampoTipoTelefone();
        carregarOperadoras();

        $scope.confirm = function() {
            $scope.object.dddTelefoneRamal = `(${$scope.object.ddd}) ${$scope.object.numero}`;

            if (listaTelefones.length === 0) {
                $scope.object.principal = true;
            }
            listaTelefones.push($scope.object);
            $scope.hide();
        };

        $scope.hide = function() {
            $mdDialog.hide();
        };
        $scope.cancel = function() {
            $mdDialog.cancel();
        };
    }; /* Fim da Dialog Cliente Telefone */

    //EXCLUIR TELEFONE
    $ctrl.excluirTelefone = function(index) {
        $ctrl.listaTelefones.splice(index, 1);

        if ($ctrl.listaTelefones.length > 0) {
            let telefonePrincipal = $ctrl.listaTelefones.find((item) => { return item.principal === true; });
            
            if (!telefonePrincipal) {
                $ctrl.listaTelefones[0].principal = true;
            }
        }
    };

    /*SUB-ABA ENDEREÇO*/
    //AUTOCOMPLETE CIDADE
    $ctrl.querySearchCidade = function(query) {
        let filtro = query;
        const pCidade = Cidade.buscarListaPorNome(filtro).$promise;

        return pCidade.then(function(data) {
            let listaCidade = [];
            listaCidade.push(...data);
            return listaCidade;
        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
            return [];
        });
    };

    $ctrl.selectedItemCidadeChange = function(item) {
        $ctrl.object.endereco.cidade = item;
        $ctrl.object.endereco.uf = (item && item.uf) ? item.uf : undefined;
    };
    //FIM AUTOCOMPLETE

    let carregarTipoLogradouro = () => {
        Clientes.buscarTipoLogradouro().$promise.then(function(data) {
            $scope.listTipoLogradouro = angular.copy(data);
        }).catch(function(response) {
            $scope.$emit('crud-critical', 'tipoLogradouro', response);
        });
    };

    /*ABA DADOS CONTABEIS*/

    /*Inicio da Dialog Cliente Dados Contábeis*/
    $ctrl.novaContaContabil = function(item) {
        $mdDialog.show({
            controller: DialogClienteDadosContabeis,
            locals: {
                notification: $scope.notification,
                listaDadosContabeis: $ctrl.listaDadosContabeis,
                cliente: $ctrl.object,
                item: item
            },
            templateUrl: 'partials/comercial/cadastros/clientes/html/dialogs/dialog_associar_pessoa_conta_contabil.html',
            parent: angular.element(document.body),
            clickOutsideToClose: false,
            fullscreen: false
        });
    };

    let DialogClienteDadosContabeis = ($scope, $mdDialog, notification, listaDadosContabeis, cliente, AutocompleteService, Unidade, item, PessoaContaContabil) => {
        $scope.title = undefined;
        $scope.contaContabil = undefined;
        $scope.object = {};
        $scope.habilitarComboEmpresa = false;
        $scope.habilitarContaContabil = true;
        let aux1;

        if (!item) {
            $scope.title = 'Novo';
            $scope.habilitarComboEmpresa = true;

        } else {
            $scope.title = 'Edição';
            $scope.habilitarContaContabil = false;
        }

        /* CARREGAR COMBOS */
        let initCampoTipoDadosContabeis = () => {
            const pTipoDadosContabeis = EnumService.enumValues({
                pacote: 'br.com.araujoabreu.sgi.contabilidade.domain',
                classe: 'PessoaContaContabil',
                enum: 'TipoRelacionamento'
            }).$promise;

            pTipoDadosContabeis.then(function(data) {
                if (data) {
                    data.forEach(item => {
                        if (item === 'Cliente') {
                            $scope.object.tipoEspecial = item;
                        }
                    });
                }
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });
        };

        let initCampoEmpresa = () => {
            $scope.listEmpresas = [];

            const pUnidade = Unidade.page(undefined, {
                tipoUnidade: 'Matriz'
            }).$promise;

            pUnidade.then(function(data) {
                $scope.listEmpresas.push({
                    codigo: undefined,
                    codnome: ''
                });
                $scope.listEmpresas.push(...data.content);
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });
        };

        let checkListaContaContabil = () => {
            if (item) {
                let contaContabilAux;

                listaDadosContabeis.forEach(value => {
                    if (item.codigo === value.codigo) {
                        contaContabilAux = value.contaContabil;
                        $scope.object.unidade = item.unidade;
                        $scope.object.tipoEspecial = item.tipoEspecial;
                    }
                });

                if (contaContabilAux) {
                    aux1 = contaContabilAux;
                    $scope.contaContabil = contaContabilAux;
                }
            }
        };

        $scope.changeEmpresa = function() {
            $scope.habilitarContaContabil = false;

            if (!$scope.object.unidade.codigo) {
                delete $scope.object.unidade;
            }
            $scope.selectedItemContaContabil = undefined;
            $scope.object.contaContabil = undefined;
            $scope.searchTextPessoaContaContabil = '';
        };

        //AUTOCOMPLETE CONTA CONTÁBIL
        let autoCompleteContaContabil = () => {

            $scope.searchTextPessoaContaContabil = undefined;
            $scope.selectedItemPessoaContaContabil = aux1;

            $scope.querySearchContaContabil = function(query, queryTipoEspecial, queryUnidade) {
                let filtro = {};
                let aux = false;
                filtro.nome = query;
                filtro.unidade = queryUnidade;

                if (queryTipoEspecial === 'Faturamento Contratual' || queryTipoEspecial === 'Faturamento - Extra contratual') {
                    filtro.classificacao = 9;
                } else if (queryTipoEspecial === 'Faturamento - A receber') {
                    filtro.classificacao = 10;
                } else {
                    filtro.classificacao = 6;

                    if (queryTipoEspecial === 'Fundo fixo') {
                        filtro.tipoEspecial = 0;
                        aux = true;
                    } else if (queryTipoEspecial === 'Adiantamento') {
                        filtro.tipoEspecial = 1;
                        aux = true;
                    } else if (queryTipoEspecial === 'Reembolso') {
                        filtro.tipoEspecial = 2;
                        aux = true;
                    } else if (queryTipoEspecial === 'Empréstimo') {
                        filtro.tipoEspecial = 3;
                        aux = true;
                    } else if (queryTipoEspecial === 'Cliente') {
                        aux = false;
                    } else {
                        filtro.tipoEspecial = undefined;
                        aux = true;
                    }
                }

                let pContaContabil;

                if (aux) {
                    pContaContabil = AutocompleteService.getContaContabilPorEmpresaTipoEspecialClassificacao(filtro).$promise;
                } else {
                    pContaContabil = AutocompleteService.getContaContabilPorEmpresaClassificacao(filtro).$promise;
                }

                return pContaContabil.then(function(data) {
                    let listaContaContabil = [];
                    listaContaContabil.push(...data);
                    return listaContaContabil;
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'load', response);
                    return [];
                });

            };

            $scope.selectedItemChangePCC = function(item, pcc) {
                $scope.contaContabil = pcc;
            };
        }; //FIM AUTOCOMPLETE
        /* FIM Campos*/

        $scope.confirm = function() {
            if (!$scope.contaContabil) {
                notification.error('Conta contábil é um campo de preenchimento obrigatório.');

            } else {

                let jaTemNaLista = false;

                if (listaDadosContabeis.length > 0) {
                    listaDadosContabeis.forEach(value => {
                        if (!item && (value.unidade.codigo === $scope.object.unidade.codigo)) {
                            jaTemNaLista = true;
                            notification.atencao('Conta contábil já foi cadastrada para esta empresa.');
                        }
                        return;
                    });
                }

                if (!jaTemNaLista) {
                    $scope.object.pessoa = cliente;
                    $scope.object.contaContabil = $scope.contaContabil;
                    $scope.object.tipoEspecial = $scope.object.tipoEspecial;
                    $scope.object.unidade = $scope.object.unidade;
                    $scope.object.numeroContaContabil = `${$scope.contaContabil.numero} - ${$scope.contaContabil.descricao}`;

                    if (!item) {
                        // Salvar
                        PessoaContaContabil.save($scope.object)
                            .$promise.then(function(data) {

                                listaDadosContabeis.push($scope.object);
                                notification.success('Conta contábil inserida com sucesso.');
                                carregarDadosContabeis();

                            }).catch(function(response) {
                                $scope.$emit('crud-critical', 'save', response);
                            });

                        $scope.hide();

                    } else {
                        $scope.object.codigo = item.codigo;

                        //Update
                        PessoaContaContabil.update({
                                id: $scope.object.codigo
                            }, $scope.object)
                            .$promise.then(function(data) {
                                notification.success('Conta contábil atualizada com sucesso.');
                                carregarDadosContabeis();
                            }).catch(function(response) {
                                $scope.$emit('crud-critical', 'update', response);
                            });

                        $scope.hide();
                    }
                }

            }
        };

        initCampoTipoDadosContabeis();
        initCampoEmpresa();
        checkListaContaContabil();
        autoCompleteContaContabil();

        $scope.hide = function() {
            $mdDialog.hide();
        };
        $scope.cancel = function() {
            $mdDialog.cancel();
        };
    }; /* Fim da Dialog Cliente Dados Contábeis */

    /* Inicio da Dialog Excluir Conta Contábil */
    $ctrl.excluirDadosContabeis = function(item) {
        // Chamada do serviço que criará um dialog de confirmação.
        DialogConfirm.show(
            undefined,
            item,
            'Remover conta contábil: Confirmação',
            'Deseja realmente <b>remover</b> esta conta contábil (esta operação não poderá ser desfeita)?',
            'Confirmar',
            'Cancelar',
            confirmRemover,
            cancelRemover);
    };

    let confirmRemover = (item, fnHide) => {
        PessoaContaContabil.remover({ id: item.codigo }).$promise.then(function(data) {
            $scope.notification.success('Conta contábil removida com sucesso.');
            carregarDadosContabeis();
            fnHide();
        }).catch(function(response) {
            $scope.$emit('crud-critical', 'dialog', response);
            $scope.notification.criticalError(response.status, response.statusText, response.data);
            fnHide();
        });
    };

    let cancelRemover = (fnHide) => {
        fnHide();
    };
    /* Fim da Dialog Excluir Conta Contábil */

    /*--------------------------------------------------------------------------------------------------------------------*/
    /*VALIDAÇÃO DE DOCUMENTO DO CLIENTE*/
    $ctrl.consultarDocumento = function() {
        if ($ctrl.object.documento) {

            if ($ctrl.tipoCliente !== 'clientePF') {
                Clientes.consultarDocumentoPopup({}, $ctrl.object).$promise.then(function(data) {
                    if (data.value) {
                        DialogConfirm.show(
                            undefined,
                            undefined,
                            'Converter fornecedor: Confirmação',
                            'Este CNPJ pertence a um fornecedor. Deseja realmente cadastrar este CNPJ como cliente?',
                            'Confirmar',
                            'Cancelar',
                            confirmConverter,
                            cancelConverter);
                    } else {
                        $ctrl.object.homologado = false;
                        Clientes.consultarDocumento($ctrl.object)
                            .$promise.then(function(data) {
                                $ctrl.object = data;
                                initEdit();
                                $scope.consultaWebService = true;
                                $ctrl.object.homologado = true;
                                $ctrl.object.homologador = UsuarioLogadoService.usuario;
                                $ctrl.object.dataHomologacao = new Date();

                                if (!$ctrl.object.subCliente) {
									$ctrl.object.subCliente = $scope.listSubCliente[1];
								}
                            }).catch(function(response) {
                                $rootScope.loadingProgress = false;
                                if (response.data.message === 'Erro de conexão, login, ou dados.') {
                                    preenchimentoManual();
                                    $ctrl.object.homologado = false;
                                } else if (response.data.message === 'CNPJ de fornecedor.') {
                                    converterFornecedor();
                                } else {
                                    $ctrl.object.documento = undefined;
                                    $ctrl.object.homologado = false;
                                    $scope.notification.criticalError(response.status, response.statusText, response.data);
                                }
                            });
                    }
                }).catch(function(response) {
                    $rootScope.loadingProgress = false;
                    $scope.notification.criticalError(response.status, response.statusText, response.data);
                });
            } else if ($ctrl.tipoCliente === 'clientePF') {
                $ctrl.object.homologado = false;
                Clientes.consultarDocumento($ctrl.object)
                    .$promise.then(function(data) {
                        $ctrl.object = data;
                        initEdit();
                        $scope.consultaWebService = true;
                        $ctrl.object.homologado = true;
                        $ctrl.object.homologador = UsuarioLogadoService.usuario;
                        $ctrl.object.dataHomologacao = new Date();

                        $ctrl.object.faturamentoCobranca = false;
                        $ctrl.object.subCliente = 'Não';
                        $ctrl.desabilitarCampos();

                        if (!$ctrl.object.subCliente) {
							$ctrl.object.subCliente = $scope.listSubCliente[1];
						}
                    }).catch(function(response) {
                        $rootScope.loadingProgress = false;
                        if (response.data.message === 'Erro de conexão, login, ou dados.') {
                            $ctrl.object.homologado = false;
                            preenchimentoManual();
                        } else {
                            $ctrl.object.documento = undefined;
                            $ctrl.object.homologado = false;
                            $scope.notification.criticalError(response.status, response.statusText, response.data);
                        }
                    });
            }
        }
    };

    let preenchimentoManual = () => {
        $ctrl.object.homologado = false;
        $ctrl.object.homologador = undefined;
        $ctrl.object.dataHomologacao = undefined;
        // Chamada do serviço que criará um dialog de confirmação.
        DialogConfirm.show(
            undefined,
            undefined,
            'Erro comunicação com o site da Receita Federal',
            'Não foi possível realizar a comunicação com o site da Receita Federal para validação dos dados do fornecedor. Deseja preencher os dados manualmente ou tentar realizar o cadastro mais tarde?',
            'Preencher dados manualmente',
            'Tentar mais tarde',
            confirmManual,
            cancelManual);
    };

    let confirmManual = (item, fnHide) => {
        $ctrl.preencherManualmente = true;
        fnHide();
    };

    let cancelManual = (fnHide) => {
        $state.go('clientes.list');
        fnHide();
    };

    /* INICIO DA DIALOG CONVERTER FORNECEDOR PARA FORNECEDOR_CLIENTE */
    let converterFornecedor = () => {
        // Chamada do serviço que criará um dialog de confirmação.
        DialogConfirm.show(
            undefined,
            undefined,
            'Converter fornecedor: Confirmação',
            'Este CNPJ pertence a um fornecedor. Deseja realmente cadastrar este CNPJ como cliente?',
            'Confirmar',
            'Cancelar',
            confirmConverter,
            cancelConverter);
    };

    let confirmConverter = (item, fnHide) => {
        Clientes.converterFornecedor($ctrl.object)
            .$promise.then(function(data) {
                $ctrl.object = data;
                if ($ctrl.object.clientePrincipal) {
                    $ctrl.object.subCliente = 'Sim';
                } else {
                    $ctrl.object.subCliente = 'Não';
                }
                initAutocompletes();
                $ctrl.listaTelefones = angular.copy(data.telefones);
                $ctrl.preencherManualmente = true;

                fnHide();

            }).catch(function(response) {
                $scope.$emit('crud-critical', 'converter', response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
                fnHide();
            });
    };

    let cancelConverter = (fnHide) => {
        $ctrl.object.documento = undefined;
        $scope.notification.error('Este CNPJ já está cadastrado para um fornecedor.');
        fnHide();
    };
    /*FIMA DA DIALOG DE CONVERTER FORNECEDOR*/

    init();

}
