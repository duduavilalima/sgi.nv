angular
    .module('app.centrosfinanceiros')
    .controller('FechoDetailCtrl', detailController);

function detailController($scope, $state, $stateParams, $mdDialog, CentroFinanceiro, CentroFinanceiroAliquota, Gerencia, Telefone,
    Fecho, FechoMaoDeObra, FechoMaoDeObraAdicional, FechoDespesaGeral, FechoService, FechoMaoDeObraService, FechoDespesaGeralService,
    CategoriaTributo, CentroFinanceiroAliquotaService, Cidade, UnidadeMedida, CategoriaDespesaGeral, EnumService, CategoriaMaoDeObra, CategoriaMaoDeObraAdicional, Tributo, Aliquota) {

    const $ctrl = this;

    init = () => {
        $ctrl.object = undefined;
        $scope.selectedItemEngResponsavel = undefined;
        $scope.selectedItemContatoAA = undefined;

        loadObj();
    };

    loadObj = () => {
        let pCentroFinanceiro = CentroFinanceiro.get({
            id: $stateParams.objectId
        }).$promise;

        pCentroFinanceiro.then(function(data) {
            $ctrl.object = data;
            initView($ctrl.object);

        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    init();

    /*
    * ===================================
    *   -:: EXIBIÇÃO - DADOS GERAIS ::-
    * ===================================
    */

    initView = (object) => {
        carregarIdentificadorView(object);
        carregarTelContatoView(object);
        carregarAbaFecho(object);
    };

    //Inicia o campo Identificador para o modo VIEW
    carregarIdentificadorView = (object) => {
        object.varIdentificador = '';

        let parte3 = object.identificador.trim().substring(object.identificador.length - 4);

        if (object.pai && object.pai.codigo) {

            let pGerencia = Gerencia.getGerenciaSimplesById({
                id: object.pai.codigo
            }).$promise;
            pGerencia.then(function(data) {
                let gerencia = data;
                let parte1 = gerencia.diretoria.numero;
                let parte2 = gerencia.numero;
                object.varIdentificador = `${parte1}.${parte2}.${parte3}`;
            }).catch(function(response) {
                object.varIdentificador = '';
                $scope.$emit('crud-critical', 'load', response);
            });

        } else {
            object.varIdentificador = object.varIdentificador.concat(parte3);
        }
    };

    // Inicia o campo Tel. contato para o modo VIEW
    carregarTelContatoView = (object) => {
        object.telefone = '';

        if (object && object.contatoAA && object.contatoAA.codigo) {

            let param = {};
            let filtro = { pessoa: object.contatoAA };

            let pTelefone = Telefone.buscarListaTelefone(filtro).$promise;
            pTelefone.then(function(data) {
                data.forEach(tel => {
                    if (tel.principal) {
                        let telView = '';

                        if (tel.numero) {
                            if (tel.tipo) {
                                telView = `${tel.tipo}`;
                            }
                            if (tel.ddd) {
                                telView = `${telView} - (${tel.ddd}) ${tel.numero}`;
                            }
                            if (tel.ramal) {
                                telView = `${telView} r. ${tel.ramal}`;
                            }

                            object.telefone = telView;
                        }
                    }
                });

            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });
        }
    };

    /*
    * --------
    *  BOTÕES
    * --------
    */

    $ctrl.salvar = () => {
        let objectCentroFinanceiro = $ctrl.object;
        let objectFecho = $ctrl.object.fecho;
        let listCentroFinanceiroAliquota = $ctrl.listaCentroFinanceiroAliquota;

        objectFecho.listaFechosDespesasGerais = filtrarListaFechosDespesasGerais();
        objectFecho.listaFechosMaoDeObra = filtrarListaFechosMaoDeObra(objectFecho);

        // Atualizar dados da aba BDI
        $ctrl.listaFechoBdi.forEach(value => {
            if (value.bdi === 'Despesas indiretas') {
                objectFecho.percentualBdiDespesas = value.valor;
            } else {
                objectFecho.percentualBdiBeneficios = value.valor;
            }
        });

        // Atualizar dados da aba Mão de Obra
        objectFecho.percentualEncargosSociais = $ctrl.encargosSociais.percentual;

        // Atualizar valores totais: (este calculo já é feito no backend)
        objectFecho.valorMaoObra = $ctrl.subTotalMaoDeObra + $ctrl.totalEncargosSociais;
        objectFecho.valorGastosGerais = $ctrl.totalFechoGastosGerais;
        objectFecho.valorMaterial = $ctrl.totalFechoMateriais;
        objectFecho.valorServicoTerceiro = $ctrl.totalFechoServicosTercerizados;

        // Atualizar dados da aba tributos
        listCentroFinanceiroAliquota = atualizarListaTributos(listCentroFinanceiroAliquota);

        let centroFinanceiroFechoTO = {
            centroFinanceiro: undefined,
            fecho: undefined,
            listaCentroFinanceiroAliquota: []
        };
        centroFinanceiroFechoTO.centroFinanceiro = objectCentroFinanceiro;
        centroFinanceiroFechoTO.fecho = objectFecho;
        centroFinanceiroFechoTO.listaCentroFinanceiroAliquota = listCentroFinanceiroAliquota;

        centroFinanceiroFechoTO.fecho.percentualImposto = $ctrl.totalPercentualTributo;

        if ($scope.screenMode === 'NEW') {
            criarFecho(centroFinanceiroFechoTO);
        } else {
            if ($ctrl.object.situacao === 'Ativo') {
                alterarFecho(centroFinanceiroFechoTO);
            } else if ($ctrl.object.situacao === 'Em elaboração') {
                atualizarFecho(centroFinanceiroFechoTO);
            }
        }
    };

    let criarFecho = (centroFinanceiroFechoTO) => {
        let pCentroFinanceiro = CentroFinanceiro.criarFecho(centroFinanceiroFechoTO).$promise;

        pCentroFinanceiro.then(function(data) {
            $state.go('centrosfinanceiros.view', {
                objectId: $ctrl.object.codigo
            });
            $scope.notification.success(undefined, 'Fecho criado com sucesso!');

        }).catch(function(response) {
            $state.go('centrosfinanceiros.view', {
                objectId: $ctrl.object.codigo
            });
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    let alterarFecho = (centroFinanceiroFechoTO) => {
        let pCentroFinanceiro = CentroFinanceiro.salvarAlterarFecho(centroFinanceiroFechoTO).$promise;
        pCentroFinanceiro.then(function(data) {
            $state.go('centrosfinanceiros.view', {
                objectId: $ctrl.object.codigo
            });
            $scope.notification.success(undefined, 'Fecho alterado com sucesso!');

        }).catch(function(response) {
            $state.go('centrosfinanceiros.view', {
                objectId: $ctrl.object.codigo
            });
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    let atualizarFecho = (centroFinanceiroFechoTO) => {
        let pCentroFinanceiro = CentroFinanceiro.atualizarFecho(centroFinanceiroFechoTO).$promise;
        pCentroFinanceiro.then(function(data) {
            $state.go('centrosfinanceiros.view', {
                objectId: $ctrl.object.codigo
            });
            $scope.notification.success(undefined, 'Fecho alterado com sucesso!');

        }).catch(function(response) {
            $state.go('centrosfinanceiros.view', {
                objectId: $ctrl.object.codigo
            });
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    atualizarListaTributos = (listCentroFinanceiroAliquota) => {
        let listaAtualizada = [];

        if (listCentroFinanceiroAliquota.length > 0) {

            listaAtualizada = listCentroFinanceiroAliquota.filter((value) => {
                return value.aliquota.categoriaTributo.codigo;
            });

            listaAtualizada.forEach(value => {
                if (!value.codigo) {
                    if (value.aliquota.categoriaTributo.aliquotas[0]) {
                        value.aliquota = value.aliquota.categoriaTributo.aliquotas[0];
                    }
                } else {
                    if (value.aliquota.categoriaTributo.aliquotas[0].codigo != value.aliquota.codigo) {
                        value.aliquota = value.aliquota.categoriaTributo.aliquotas[0];
                    }
                }
            });
        }

        return listaAtualizada;
    };

    $ctrl.cancelar = () => {
        $state.go('centrosfinanceiros.view', { objectId: $ctrl.object.codigo });
    };

    // Este filtro remove os itens em branco da ListaFechosDespesasGerais. Estes itens são adicionados quando não tem itens na aba de "Gastos gerais", "Materiais" e "Serviços Terceiros".
    // Assim ele junta apenas os itens validos das três lista "$ctrl.listaFechoGastosGerais", "$scope.listaFechoMateriais" e  "$ctrl.listaFechoServicosTercerizados".
    filtrarListaFechosDespesasGerais = () => {
        let listaFechosDespesasGerais = [];

        if ($ctrl.listaFechoGastosGerais.length === 1) {
            let item = $ctrl.listaFechoGastosGerais[0];

            if (item.categoriaDespesaGeral && item.categoriaDespesaGeral.codigo && item.quantidade > 0 && item.valorUnitario > 0) {
                listaFechosDespesasGerais.push(item);
            }
        } else {
            listaFechosDespesasGerais = angular.copy(
                    $ctrl.listaFechoGastosGerais.filter(fechoGastosGerais => {
                        return fechoGastosGerais.categoriaDespesaGeral && fechoGastosGerais.categoriaDespesaGeral.codigo && fechoGastosGerais.quantidade && fechoGastosGerais.valorUnitario;
                    })
                );
        }

        if ($ctrl.listaFechoMateriais.length === 1) {
            let item = $ctrl.listaFechoMateriais[0];

            if (item.descricao && item.quantidade > 0 && item.valorUnitario > 0) {
                listaFechosDespesasGerais.push(item);
            }
        } else {
            listaFechosDespesasGerais = listaFechosDespesasGerais.concat(
                    $ctrl.listaFechoMateriais.filter(fechoMateriais => {
                        return fechoMateriais.descricao && fechoMateriais.quantidade && fechoMateriais.unidadeMedida && fechoMateriais.unidadeMedida.codigo && fechoMateriais.valorUnitario;
                    })
                );
        }

        if ($ctrl.listaFechoServicosTercerizados.length === 1) {
            let item = $ctrl.listaFechoServicosTercerizados[0];

            if (item.descricao && item.quantidade > 0 && item.valorUnitario > 0) {
                listaFechosDespesasGerais.push(item);
            }
        } else {
            listaFechosDespesasGerais = listaFechosDespesasGerais.concat(
                    $ctrl.listaFechoServicosTercerizados.filter(fechoServicosTercerizados => {
                        return fechoServicosTercerizados.descricao && fechoServicosTercerizados.quantidade && fechoServicosTercerizados.unidadeMedida && fechoServicosTercerizados.unidadeMedida.codigo && fechoServicosTercerizados.valorUnitario;
                    })
                );
        }

        return listaFechosDespesasGerais;
    };

    filtrarListaFechosMaoDeObra = (fecho) => {
        let novaLista = [];

        if (fecho && fecho.listaFechosMaoDeObra && fecho.listaFechosMaoDeObra.length > 0) {
            novaLista = fecho.listaFechosMaoDeObra.filter(maoDeObra => {
                return maoDeObra.categoriaMaoDeObra && maoDeObra.categoriaMaoDeObra.codigo && maoDeObra.tipoContratacao && maoDeObra.quantidade > 0 && maoDeObra.valorUnitario > 0;
            });
        }

        return novaLista;
    };

    /*
    * -----------
    *  ABA FECHO:
    * -----------
    */

    carregarAbaFecho = (object) => {

        object.fecho = {
            dataReajuste: undefined,
            percentualImposto: undefined,
            prazo: undefined,
            custoMensal: 0,
            bdi: 0,
            tributos: 0,
            valorMensal: 0,
            valorTotal: 0
        };

        // busca partes do fecho (projection)
        let pFecho = Fecho.fechoByCodCentroFinanceiro({id: object.codigo}).$promise;
        pFecho.then(function(data) {

            //busca o fecho completo (repository), para atualizar posteriormente
            let pFecho2 = Fecho.get({id: data.codigo}).$promise;
            pFecho2.then(function(data2) {
                object.fecho = data2;

                if ($scope.screenMode === 'NEW') {
                    object.fecho = {
                        dataReajuste: new Date(),
                        percentualImposto: 0,
                        custoMensal: 0,
                        bdi: 0,
                        tributos: 0,
                        valorMensal: 0,
                        valorTotal: 0
                    };
                }

                object.fecho.prazo = object.contratoTO.prazoAtual;
                carregarFechoMaoDeObra(object.fecho);
            }).catch(function(response2) {
                $scope.$emit('crud-critical', 'load', response2);
            });
        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    calcularValoresDoFecho = () => {
        $ctrl.object.fecho.valorMensal = $ctrl.object.fecho.custoMensal + $ctrl.object.fecho.bdi + $ctrl.object.fecho.tributos;
        if ($ctrl.object.fecho.prazo) {
            $ctrl.object.fecho.valorTotal = $ctrl.object.fecho.valorMensal * $ctrl.object.fecho.prazo;
        } else {
            $ctrl.object.fecho.valorTotal = $ctrl.object.fecho.valorMensal;
        }
    };

    calcularCustoMensal = () => {
        $scope.totalFechoMaoDeObra = FechoMaoDeObraService.getTotal($ctrl.totalEncargosSociais, $ctrl.subTotalMaoDeObra);
        $ctrl.object.fecho.custoMensal = $scope.totalFechoMaoDeObra + $ctrl.totalFechoGastosGerais + $ctrl.totalFechoMateriais +  $ctrl.totalFechoServicosTercerizados;
        $ctrl.listaFechoBdi.forEach((value) => {
            value.custoMensal = $ctrl.object.fecho.custoMensal;
        });
        $ctrl.atualizarBdi();
        calcularValoresDoFecho();
    };

    /*
    *  -----------------------------
    *   ABA FECHO / ABA MÃO DE OBRA
    *  -----------------------------
    */

    carregarFechoMaoDeObra = (fecho) => {
        $ctrl.subTotalMaoDeObra = 0;
        $ctrl.totalEncargosSociais = 0;
        $scope.totalFechoMaoDeObra = 0;

        $ctrl.encargosSociais = {};
        $ctrl.encargosSociais.percentual = fecho.percentualEncargosSociais;

        if (!fecho.listaFechosMaoDeObra) {
            fecho.listaFechosMaoDeObra = [];
        }

        if (fecho.listaFechosMaoDeObra.length === 0) {
            let fechoMaoDeObra = new FechoMaoDeObra();
            fechoMaoDeObra.categoriaMaoDeObra = undefined;
            fechoMaoDeObra.tipoContratacao = undefined;
            fechoMaoDeObra.quantidade = undefined;
            fechoMaoDeObra.valorUnitario = undefined;
            fechoMaoDeObra.totalAdicionais = 0;
            fechoMaoDeObra.valorTotal = 0;
            fechoMaoDeObra.listaMaoDeObraAdicional = [];
            $ctrl.object.fecho.listaFechosMaoDeObra.push(fechoMaoDeObra);
        }

        //caso o FechoMaoDeObra nao tenha MaoDeObraAdicional é preciso adicionar eles para serem exibidos no dialog "Adicionais de mão de obra"
        fecho.listaFechosMaoDeObra
            .filter(maoDeObra => { return maoDeObra.listaMaoDeObraAdicional.length === 0; })
            .forEach(maoDeObra => { adicionarListaMaoDeObraAdicional(maoDeObra); });

        atualizarTotaisMaoDeObra(fecho.listaFechosMaoDeObra);

        $ctrl.totalEncargosSociais = FechoMaoDeObraService.getTotalEncargosSociais($ctrl.encargosSociais.percentual, $ctrl.subTotalMaoDeObra);
        $scope.totalFechoMaoDeObra = FechoMaoDeObraService.getTotal($ctrl.totalEncargosSociais, $ctrl.subTotalMaoDeObra);

        buscarListaContratacao();
        buscarListaCategoriaMaoDeObra();

        carregarFechoGastosGerais(fecho);
    };

    atualizarTotaisMaoDeObra = (listaFechoMaoDeObra) => {
        subTotalMaoDeObra = 0;

        listaFechoMaoDeObra.forEach(maoDeObra => {
			valor = (maoDeObra.valorUnitario + maoDeObra.totalAdicionais) * maoDeObra.quantidade;
            subTotalMaoDeObra +=  Math.round((valor + Number.EPSILON) * 100) / 100;
        });

        $ctrl.subTotalMaoDeObra +=  Math.round((subTotalMaoDeObra + Number.EPSILON) * 100) / 100;
    };

    $ctrl.chamarDialogMaoObraAdicional = (objMaoDeObra) => {
        chamarDialogMaoObraAdicional(objMaoDeObra, false);
    };

    $ctrl.chamarDialogMaoObraAdicionalEdicao = (maoObra) => {
        chamarDialogMaoObraAdicional(maoObra, true);
    };

    chamarDialogMaoObraAdicional = (objMaoDeObra, editar) => {
        $mdDialog.show({
            controller: DialogMaoDeObraAdicional,
            locals: {
                objMaoDeObra: objMaoDeObra,
                editar: editar,
                atualizarSubtotalMaoDeObra: $ctrl.atualizarSubtotalMaoDeObra
            },
            templateUrl: 'partials/comercial/cadastros/centrosfinanceiros/html/dialogs/dialog_mao_de_obra_adicional.html',
            parent: angular.element(document.body),
            clickOutsideToClose: false,
            fullscreen: false
        });
    };

    DialogMaoDeObraAdicional = ($scope, $mdDialog, objMaoDeObra, editar, atualizarSubtotalMaoDeObra) => {
        $scope.title = 'Adicionais de mão de obra';
        $scope.edit = editar;
        $scope.listaAdicional = undefined;
        $scope.listaAdicional = angular.copy(objMaoDeObra.listaMaoDeObraAdicional);

        calcularTotal = (lista) => {
            return lista.filter(value => value.valorUnitario !== undefined).reduce((total, {valorUnitario}) => total + valorUnitario, 0);
        };

        $scope.total = calcularTotal($scope.listaAdicional);

        $scope.cancel = () => {
            $mdDialog.cancel();
        };

        $scope.atualizarTotal = () => {
            $scope.total = calcularTotal($scope.listaAdicional);
        };

        $scope.save = () => {
            atualizarListaAdicional();
            atualizarTotalAdicional();
            atualizarSubtotalMaoDeObra();
            $mdDialog.cancel();
        };

        atualizarTotalAdicional = () => {
            objMaoDeObra.totalAdicionais = angular.copy($scope.total);
        };

        atualizarListaAdicional = () => {
            objMaoDeObra.listaMaoDeObraAdicional = $scope.listaAdicional;
        };
    };

    criarMaoObra = () => {
        let fechoMaoDeObra = new FechoMaoDeObra();
        fechoMaoDeObra.categoriaMaoDeObra = undefined;
        fechoMaoDeObra.tipoContratacao = undefined;
        fechoMaoDeObra.quantidade = undefined;
        fechoMaoDeObra.valorUnitario = undefined;
        fechoMaoDeObra.totalAdicionais = 0;
        fechoMaoDeObra.valorTotal = 0;
        fechoMaoDeObra.listaMaoDeObraAdicional = [];

        adicionarListaMaoDeObraAdicional(fechoMaoDeObra);

        $ctrl.object.fecho.listaFechosMaoDeObra.push(fechoMaoDeObra);
    };

    adicionarListaMaoDeObraAdicional = (fechoMaoDeObra) => {

        let pCategoria = CategoriaMaoDeObraAdicional.buscarTodasCategorias().$promise;
        pCategoria.then(function(data) {
            let listaCategoriaAdicional = data;

            listaCategoriaAdicional.forEach(adicional => {
                let fechoMaoDeObraAdicional = new FechoMaoDeObraAdicional();
                fechoMaoDeObraAdicional.categoriaMaoDeObraAdicional = adicional;
                fechoMaoDeObraAdicional.valorUnitario = 0;

                fechoMaoDeObra.listaMaoDeObraAdicional.push(fechoMaoDeObraAdicional);
            });

        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    $ctrl.editarMaoObra = (item) => {

        if (!edicaoDisponivel($ctrl.object.fecho.listaFechosMaoDeObra)) {
            return;
        }

        $ctrl.object.maoObraSelecionado = undefined;
        $ctrl.object.maoObraSelecionado = angular.copy(item);

        item.edicao = true;
        $ctrl.object.maoObraSelecionado.edicao = true;

        buscarListaContratacao();
        buscarListaCategoriaMaoDeObra();
    };

    //Verifica se já tem algum item da lista em modo de edição.
    edicaoDisponivel = (lista) => {
        return !lista.some(value => value.edicao && value.edicao === true);
    };

    buscarListaContratacao = () => {
        $ctrl.listaContratacao = undefined;

        let pEnum = EnumService.enumValues({
            pacote: 'br.com.araujoabreu.sgi.comercial.domain',
            classe: 'FechoMaoDeObra',
            enum: 'TipoContratacao'
        }).$promise;

        pEnum.then(function(data) {
            $ctrl.listaContratacao = data;
        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    buscarListaCategoriaMaoDeObra = () => {
        $ctrl.listaCategoriaMaoDeObra = undefined;

        let pCategoria = CategoriaMaoDeObra.buscarTodasCategorias().$promise;
        pCategoria.then(function(data) {
            $ctrl.listaCategoriaMaoDeObra = data;

        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
        });

    };

    $ctrl.removerMaoObra = (index) => {
        $ctrl.object.fecho.listaFechosMaoDeObra.splice(index, 1);

        if ($ctrl.object.fecho.listaFechosMaoDeObra.length <= 0) {
            criarMaoObra();
        }

        $ctrl.atualizarSubtotalMaoDeObra();
    };

    $ctrl.adicionarMaoObra = (item) => {
        if (item.quantidade > 0 && item.valorUnitario > 0 && item.categoriaMaoDeObra && item.tipoContratacao) {
            criarMaoObra();
        }
    };

    objetoMaoObraValido = (item) => {
        if ((item.quantidade > 0 && item.valorUnitario > 0 && item.categoriaMaoDeObra && item.tipoContratacao)) {
            return true;
        }
        return false;
    };

    //FIXME: % Encargos Sociais não pode ser maior que 100.
    $scope.$on('atualizarTotalEncargosSociais', function() {
        if ($ctrl.encargosSociais.percentual > 100) {
            $ctrl.encargosSociais.percentual = 100;
        }

        $ctrl.totalEncargosSociais = FechoMaoDeObraService.getTotalEncargosSociais($ctrl.encargosSociais.percentual, $ctrl.subTotalMaoDeObra);

        calcularCustoMensal();
    });

    $ctrl.adicionarCategoriaMaoObra = (maoDeObra) => {
        chamarDialogCategoriaMaoObra(maoDeObra);
    };

    chamarDialogCategoriaMaoObra = (objMaoDeObra) => {
        $mdDialog.show({
            controller: DialogCategoriaMaoDeObra,
            locals: {
                notification: $scope.notification,
                CategoriaMaoDeObra: CategoriaMaoDeObra,
                objMaoDeObra: objMaoDeObra,
                listaCategoriaMaoDeObra: $ctrl.listaCategoriaMaoDeObra

            },
            templateUrl: 'partials/comercial/cadastros/centrosfinanceiros/html/dialogs/dialog_categoria_mao_de_obra.html',
            parent: angular.element(document.body),
            clickOutsideToClose: false,
            fullscreen: false
        });
    };

    DialogCategoriaMaoDeObra = ($scope, $mdDialog, $rootScope, notification, CategoriaMaoDeObra, objMaoDeObra, listaCategoriaMaoDeObra) => {
        $scope.object = new CategoriaMaoDeObra();

        $scope.cancel = () => {
            $mdDialog.cancel();
        };

        $scope.confirm = () => {
            $rootScope.loadingProgress = true;

            $scope.object.$save().then(data => {
                let novaCategoriaMaoDeObra = data;
                objMaoDeObra.categoriaMaoDeObra = novaCategoriaMaoDeObra;
                listaCategoriaMaoDeObra.push(novaCategoriaMaoDeObra);

                $rootScope.loadingProgress = false;
                notification.success("", "Categoria de mão de obra adicionada com sucesso.");
                $mdDialog.cancel();
            }).catch(response => {
                notification.error("", 'Não foi possível adicionar a Categoria de mão de obra.');
                $rootScope.loadingProgress = false;
                $mdDialog.cancel();
            });

        };
    };

    $ctrl.atualizarSubtotalMaoDeObra = () => {
        atualizarTotaisMaoDeObra($ctrl.object.fecho.listaFechosMaoDeObra);
        $ctrl.totalEncargosSociais = FechoMaoDeObraService.getTotalEncargosSociais($ctrl.encargosSociais.percentual, $ctrl.subTotalMaoDeObra);
        calcularCustoMensal();
    };

    /*
    *  -------------------------------
    *   ABA FECHO / ABA GASTOS GERAIS
    *  -------------------------------
    */

    carregarFechoGastosGerais = (fecho) => {
        $ctrl.listaFechoGastosGerais = [];
        $ctrl.totalFechoGastosGerais = 0;

        $ctrl.object.fecho.custoMensal = 0;

        if (fecho.listaFechosDespesasGerais === undefined) {
            fecho.listaFechosDespesasGerais = [];
        }

        fecho.listaFechosDespesasGerais.forEach(despesa => {
            despesa.descricaoCategoriaDespesaGeral = '';
            despesa.descricaoCategoriaDespesaGeral = FechoDespesaGeralService.getDescricaoCategoriaDespesaGeral(despesa);
        });

        $ctrl.listaFechoGastosGerais = FechoDespesaGeralService.filtrarListaPorTipo(fecho.listaFechosDespesasGerais, 'Gasto geral');
        $ctrl.totalFechoGastosGerais = FechoDespesaGeralService.getTotalPorTipo(fecho.listaFechosDespesasGerais, 'Gasto geral');
        $ctrl.object.fecho.custoMensal = FechoService.getCustoMensal($scope.totalFechoMaoDeObra, fecho.listaFechosDespesasGerais);

        if ($ctrl.listaFechoGastosGerais.length === 0) {
            criarFechoDespesaGeralGastos(undefined, undefined);
        }

        buscarListaCategorias();

        carregarFechoMateriais(fecho.listaFechosDespesasGerais);
        carregarFechoServicoTercerizado(fecho.listaFechosDespesasGerais);
        carregarFechoBdi(fecho);
    };

    criarFechoDespesaGeralGastos = (quantidade, valorUnitario) => {
        let fechoDespesaGeral = new FechoDespesaGeral();
        fechoDespesaGeral.quantidade = quantidade;
        fechoDespesaGeral.valorUnitario = valorUnitario;
        fechoDespesaGeral.tipoDespesaGeral = 'Gasto geral';

        fechoDespesaGeral.descricaoCategoriaDespesaGeral = '';

        let unidadeMedida = {
            id: 1,
            descricao: 'Unidade',
            codigo: 1
        };

        fechoDespesaGeral.unidadeMedida = unidadeMedida;

        $ctrl.listaFechoGastosGerais.push(fechoDespesaGeral);
    };

    buscarListaCategorias = () => {
        $ctrl.listaCategorias = undefined;

        let pCategoria = CategoriaDespesaGeral.buscarTodasCategorias().$promise;
        pCategoria.then(function(data) {
            $ctrl.listaCategorias = data;

        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    $ctrl.removerGasto = (index) => {
        $ctrl.listaFechoGastosGerais.splice(index, 1);

        if ($ctrl.listaFechoGastosGerais.length <= 0) {
            criarFechoDespesaGeralGastos(undefined, undefined);
        }

        $ctrl.totalFechoGastosGerais = FechoDespesaGeralService.getTotalPorTipo($ctrl.listaFechoGastosGerais, 'Gasto geral');

        calcularCustoMensal();
    };

    $ctrl.adicionarGasto = (item) => {
        if (item.quantidade > 0 && item.valorUnitario > 0 && item.categoriaDespesaGeral) {
            criarFechoDespesaGeralGastos(undefined, undefined);
        }
    };

    objetoGastoValido = (item) => {
        if (item.quantidade > 0 && item.valorUnitario > 0 && item.categoriaDespesaGeral) {
            return true;
        }
        return false; //FIXME: REFATORAR
    };

    $ctrl.adicionarGastosGerais = (gastosGerais) => {
        chamarDialogGastosGerais(gastosGerais);
    };

    chamarDialogGastosGerais = (objGastosGerais) => {
        $mdDialog.show({
            controller: DialogGastosGerais,
            locals: {
                notification: $scope.notification,
                CategoriaDespesaGeral: CategoriaDespesaGeral,
                objGastosGerais: objGastosGerais,
                listaCategorias: $ctrl.listaCategorias

            },
            templateUrl: 'partials/comercial/cadastros/centrosfinanceiros/html/dialogs/dialog_gastos_gerais.html',
            parent: angular.element(document.body),
            clickOutsideToClose: false,
            fullscreen: false
        });
    };

    DialogGastosGerais = ($scope, $mdDialog, $rootScope, notification, CategoriaDespesaGeral, objGastosGerais, listaCategorias) => {
        $scope.object = new CategoriaDespesaGeral();

        $scope.cancel = () => {
            $mdDialog.cancel();
        };

        $scope.confirm = () => {
            $rootScope.loadingProgress = true;

            $scope.object.$save().then(data => {
                let novaCategoriaDespesaGeral = data;
                objGastosGerais.categoriaDespesaGeral = novaCategoriaDespesaGeral;
                listaCategorias.push(novaCategoriaDespesaGeral);

                $rootScope.loadingProgress = false;
                notification.success("", "Categoria de gastos gerais adicionada com sucesso.");
                $mdDialog.cancel();
            }).catch(response => {
                notification.error("", 'Não foi possível adicionar a Categoria de gastos gerais.');
                $rootScope.loadingProgress = false;
                $mdDialog.cancel();
            });

        };
    };

    $ctrl.atualizarTotalFechoGastosGerais = () => {
        $ctrl.totalFechoGastosGerais = FechoDespesaGeralService.getTotalPorTipo($ctrl.listaFechoGastosGerais, 'Gasto geral');
        calcularCustoMensal();
    };


    /*
    *  -----------------------
    *   ABA FECHO / MATERIAIS
    *  -----------------------
    */

    carregarFechoMateriais = (listaFechoDespesaGeral) => {
        $ctrl.listaFechoMateriais = [];
        $ctrl.totalFechoMateriais = 0;

        $ctrl.listaFechoMateriais = FechoDespesaGeralService.filtrarListaPorTipo(listaFechoDespesaGeral, 'Material');
        $ctrl.totalFechoMateriais = FechoDespesaGeralService.getTotalPorTipo(listaFechoDespesaGeral, 'Material');

        if ($ctrl.listaFechoMateriais && $ctrl.listaFechoMateriais.length === 0) {
            criarFechoDespesaGeralMaterial('', undefined, undefined);
        }

        buscarListaUnidadeMedida();
    };

    criarFechoDespesaGeralMaterial = (descricao, quantidade, valorUnitario) => {
        let fechoDespesaGeral = new FechoDespesaGeral();
        fechoDespesaGeral.descricao = descricao;
        fechoDespesaGeral.quantidade = quantidade;
        fechoDespesaGeral.valorUnitario = valorUnitario;
        fechoDespesaGeral.tipoDespesaGeral = 'Material';

        $ctrl.listaFechoMateriais.push(fechoDespesaGeral);
    };

    buscarListaUnidadeMedida = () => {
        $ctrl.listaUnidadeMedida = undefined;

        let pUnidadeMedida = UnidadeMedida.buscarTodasUnidadesMedida().$promise;
        pUnidadeMedida.then(function(data) {
            $ctrl.listaUnidadeMedida = data;

        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    $ctrl.removerMaterial = (index) => {
        $ctrl.listaFechoMateriais.splice(index, 1);

        if ($ctrl.listaFechoMateriais.length <= 0) {
            criarFechoDespesaGeralMaterial('', undefined, undefined);
        }

        $ctrl.totalFechoMateriais = FechoDespesaGeralService.getTotalPorTipo($ctrl.listaFechoMateriais, 'Material');

        calcularCustoMensal();
    };

    $ctrl.adicionarMaterial = (item) => {
        if (item.descricao && item.descricao.length > 0 && item.quantidade > 0 && item.valorUnitario > 0 && item.unidadeMedida && item.unidadeMedida.codigo) {
            criarFechoDespesaGeralMaterial('', undefined, undefined);
        }
    };

    $ctrl.atualizarTotalFechoMateriais = () => {
        $ctrl.totalFechoMateriais = FechoDespesaGeralService.getTotalPorTipo($ctrl.listaFechoMateriais, 'Material');
        calcularCustoMensal();
    };


    /*
    *  -------------------------------
    *   ABA FECHO / SERVIÇOS TERCEIROS
    *  -------------------------------
    */

    carregarFechoServicoTercerizado = (listaFechoDespesaGeral) => {
        $ctrl.listaFechoServicosTercerizados = [];
        $ctrl.totalFechoServicosTercerizados = 0;

        $ctrl.listaFechoServicosTercerizados = FechoDespesaGeralService.filtrarListaPorTipo(listaFechoDespesaGeral, 'Serviço de terceiro');
        $ctrl.totalFechoServicosTercerizados = FechoDespesaGeralService.getTotalPorTipo(listaFechoDespesaGeral, 'Serviço de terceiro');

        if ($ctrl.listaFechoServicosTercerizados && $ctrl.listaFechoServicosTercerizados.length === 0) {
            criarFechoDespesaGeral('', undefined, undefined);
        }
    };

    criarFechoDespesaGeral = (descricao, quantidade, valorUnitario) => {
        let fechoDespesaGeral = new FechoDespesaGeral();
        fechoDespesaGeral.descricao = descricao;
        fechoDespesaGeral.quantidade = quantidade;
        fechoDespesaGeral.valorUnitario = valorUnitario;
        fechoDespesaGeral.tipoDespesaGeral = 'Serviço de terceiro';

        let unidadeMedida = {
                id: 1,
                descricao: 'Unidade',
                codigo: 1
            };
        fechoDespesaGeral.unidadeMedida = unidadeMedida;

        $ctrl.listaFechoServicosTercerizados.push(fechoDespesaGeral);
    };

    $ctrl.removerServicoTerceiro = (index) => {
        $ctrl.listaFechoServicosTercerizados.splice(index, 1);

        if ($ctrl.listaFechoServicosTercerizados.length <= 0) {
            criarFechoDespesaGeral('', undefined, undefined);
        }

        $ctrl.atualizarTotalFechoServicosTercerizados();
        calcularCustoMensal();
    };

    $ctrl.adicionarServicoTerceiro = (item) => {
        if (item.descricao && item.descricao.length > 0 && item.quantidade > 0 && item.valorUnitario > 0) {
            criarFechoDespesaGeral('', undefined, undefined);
        }
    };

    $ctrl.atualizarTotalFechoServicosTercerizados = () => {
        $ctrl.totalFechoServicosTercerizados = FechoDespesaGeralService.getTotalPorTipo($ctrl.listaFechoServicosTercerizados, 'Serviço de terceiro');
        calcularCustoMensal();
    };


    /*
    *  ----------------------
    *   ABA FECHO / ABA BDI
    *  ----------------------
    */

    carregarFechoBdi = (fecho) => {
        $ctrl.listaFechoBdi = FechoService.getListaBdi(fecho.percentualBdiDespesas, fecho.percentualBdiBeneficios);
        $ctrl.totalPercentualBdi = 0;
        $ctrl.object.fecho.bdi = 0;

        $ctrl.listaFechoBdi.forEach(value => {
            value.custoMensal = angular.copy($ctrl.object.fecho.custoMensal);
            value.listaBdi = $ctrl.listaFechoBdi;
            value.valorTotal = FechoService.getTotalBdi(value);

            $ctrl.totalPercentualBdi += value.valor ? value.valor : 0;
            $ctrl.object.fecho.bdi += value.valorTotal;
        });

        carregarFechoTributos($ctrl.object.codigo);
    };

    $ctrl.atualizarBdi = () => {
        $ctrl.totalPercentualBdi = 0;
        $ctrl.object.fecho.bdi = 0;

        $ctrl.listaFechoBdi.forEach(value => {
            value.valorTotal = FechoService.getTotalBdi(value);
            $ctrl.totalPercentualBdi += value.valor ? value.valor : 0;

            $ctrl.object.fecho.bdi += value.valorTotal;
        });

        calcularValoresDoFecho();

        //atribuir valores aos atributos do objeto fecho
        $ctrl.listaFechoBdi.forEach(value => {
            if (value.bdi === 'Benefícios') {
                $ctrl.object.fecho.percentualBdiBeneficios = value.valor;
            }
            if (value.bdi === 'Despesas indiretas') {
                $ctrl.object.fecho.percentualBdiDespesas = value.valor;
            }
        });

    };


    /*
    *  --------------------------
    *   ABA FECHO / ABA TRIBUTOS
    *  --------------------------
    */

    carregarFechoTributos = (codCentroFinanceiro) => {
        $ctrl.listaCentroFinanceiroAliquota = [];
        $ctrl.subTotalPercentualTributo = 0;
        $ctrl.totalPercentualTributo = 0;

        let pCentroFinanceiroAliquota = CentroFinanceiroAliquota.getListByCodCentroFinanceiro({
            id: codCentroFinanceiro
        }).$promise;
        pCentroFinanceiroAliquota.then(function(data) {
            $ctrl.listaCentroFinanceiroAliquota = data;

            if ($ctrl.listaCentroFinanceiroAliquota && $ctrl.listaCentroFinanceiroAliquota.length > 0) {
                let contador = 0;

                $ctrl.listaCentroFinanceiroAliquota.forEach(centroFinanceiroAliquota => {

                    centroFinanceiroAliquota.aliquota.categoriaTributo = {};

                    let pCategoriaTributo = CategoriaTributo.getCategoriaTributoByCod({
                        id: centroFinanceiroAliquota.aliquota.codigoCategoriaTributo
                    }).$promise;
                    pCategoriaTributo.then(function(data2) {
                        centroFinanceiroAliquota.aliquota.categoriaTributo = data2;

                        centroFinanceiroAliquota.percentualTributo = 0;
                        centroFinanceiroAliquota.percentualTributo = CentroFinanceiroAliquotaService.getPercentualTributo(centroFinanceiroAliquota);
                        $ctrl.totalPercentualTributo += centroFinanceiroAliquota.percentualTributo;

                        if (centroFinanceiroAliquota.aliquota.categoriaTributo.tributoTO.nome !== 'ISS') {
                            $ctrl.subTotalPercentualTributo += centroFinanceiroAliquota.percentualTributo;
                        }

                        buscarAliquotasPorCategoriaTributo(centroFinanceiroAliquota.aliquota.categoriaTributo);

                        contador++;
                        if (contador === $ctrl.listaCentroFinanceiroAliquota.length) {
                            calcularTotalCFAliquota();
                            criarItensVaziosParaEdicao($ctrl.listaCentroFinanceiroAliquota);
                            iniciarTributoISS($ctrl.listaCentroFinanceiroAliquota);
                            criarListasTemporariasParaEdicao();
                        }

                    }).catch(function(response2) {
                        $scope.$emit('crud-critical', 'load', response2);
                    });
                });

            } else {
                calcularValoresDoFecho();
                criarItensVaziosParaEdicao($ctrl.listaCentroFinanceiroAliquota);
                buscarUFs();
            }

        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
        });

        // buscarTributos();
        // buscarCategoriaTributo();
        // buscarUFs();
    };

    let buscarAliquotasPorCategoriaTributo = (categoriaTributoTO) => {

        let pAliquota = Aliquota.getListaAliquotaByCodCategoriaTributo({
            id: categoriaTributoTO.codigo
        }).$promise;

        pAliquota.then(function(data) {
            categoriaTributoTO.aliquotas = data;

        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    // Cria itens vazios para a lista "$ctrl.listaCentroFinanceiroAliquota" para futuras edições de tributos
    criarItensVaziosParaEdicao = (listaTributo) => {

        if (listaTributo.length === 0) {
            criarTributo(true);
            criarTributo(false);

        } else {
            if (listaTributo.filter(value => { return value.aliquota.categoriaTributo.tributoTO.nome !== 'ISS'; }).length === 0) {
                criarTributo(false);
            }

            if (listaTributo.filter(value => { return value.aliquota.categoriaTributo.tributoTO.nome === 'ISS'; }).length === 0) {
                criarTributo(true);
            }
        }
    };

    calcularTotalCFAliquota = () => {
        $ctrl.subTotalValorTributo = 0;
        $ctrl.object.fecho.tributos = 0;

        let percentualTotalTributos = $ctrl.listaCentroFinanceiroAliquota.reduce((acc, cfa) => acc + cfa.percentualTributo, 0);
        let baseDeTributos = $ctrl.object.fecho.custoMensal + $ctrl.object.fecho.bdi;

        $ctrl.listaCentroFinanceiroAliquota.forEach(centroFinanceiroAliquota => {
            centroFinanceiroAliquota.totalTributos = 0;

            centroFinanceiroAliquota.totalTributos = CentroFinanceiroAliquotaService.getValorTotal(baseDeTributos, centroFinanceiroAliquota, percentualTotalTributos);
            $ctrl.object.fecho.tributos += Math.round((centroFinanceiroAliquota.totalTributos + Number.EPSILON) * 100) / 100;

            if (centroFinanceiroAliquota.aliquota.categoriaTributo.tributoTO.nome !== 'ISS') {
                $ctrl.subTotalValorTributo += centroFinanceiroAliquota.totalTributos;
            }
        });  //FIXME: VERIFICAR SE PODE REFATORAR

        calcularValoresDoFecho();
    };

    criarTributo = (isIss) => {
        let nomeTributo = '';

        if (isIss) {
            nomeTributo = 'ISS';
        }

        let centroFinanceiroAliquota = new CentroFinanceiroAliquota();
        centroFinanceiroAliquota.percentualTributo = 0;
        centroFinanceiroAliquota.totalTributos = 0;
        centroFinanceiroAliquota.aliquota = {};
        centroFinanceiroAliquota.aliquota.categoriaTributo = new CategoriaTributo();
        centroFinanceiroAliquota.aliquota.categoriaTributo.tributoTO = new Tributo();
        centroFinanceiroAliquota.aliquota.categoriaTributo.tributoTO.nome = nomeTributo;
        centroFinanceiroAliquota.aliquota.categoriaTributo.cidade = new Cidade();

        $ctrl.listaCentroFinanceiroAliquota.push(centroFinanceiroAliquota);
        criarListasTemporariasParaEdicao();
    };

    criarListasTemporariasParaEdicao = () => {

        if (listaTributoSemIss().length > 0) {

            const pTributo = Tributo.buscarTributoByExibeNota().$promise;

            pTributo.then(function(data) {
                let listaTributo = data.filter(tributo => { return tributo.nome !== 'ISS';});

                listaTributoSemIss().forEach(cFa => {
                    cFa.listaTempTributo = [];
                    // cFa.listaTempCategoriaTributo = [];

                    listaTributo.forEach(tributo => {
                        if (tributo.codigo === cFa.aliquota.categoriaTributo.tributoTO.codigo) {
                            cFa.listaTempTributo.push(tributo);
                        } else {
                            if ( !listaTributoSemIss().some(cFa2 => { return cFa2.aliquota.categoriaTributo.tributoTO.codigo === tributo.codigo;})) {
                                cFa.listaTempTributo.push(tributo);
                            }
                        }
                    });
                });

            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });
        }
    };

    /**
        Nesse método vai ser buscado os tributos que aparecerão no combo da coluna "Tributo".
        Não pode ter tributos repetidos na lista.
        Serão mostrado todos os tributos com excessão do ISS e dos tributos que já estão lista.
    */
    buscarTributos = () => {
        $ctrl.listaTributos = [];
        let pTributo = Tributo.buscarTributoByExibeNota().$promise;

        pTributo.then(function(data) {

            let nomeTributoSelecionado = $ctrl.object.tributoSelecionado.aliquota.categoriaTributo.tributoTO.nome;
            let setNomeTributo = new Set();

            listaTributoSemIss().forEach(value => {
                let nomeTributo = value.aliquota.categoriaTributo.tributoTO.nome;

                if (nomeTributo !== nomeTributoSelecionado) {
                    setNomeTributo.add(nomeTributo);
                }
            });  //FIXME: REFATORAR

            data.forEach(value => {
                let nomeTributo = value.nome;

                if (nomeTributo !== 'ISS' && !setNomeTributo.has(nomeTributo)) {
                    $ctrl.listaTributos.push(value);
                }
            });  //FIXME: REFATORAR

        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    $ctrl.removerTributo = (item) => {
        let index = $ctrl.listaCentroFinanceiroAliquota.indexOf(item);
        $ctrl.listaCentroFinanceiroAliquota.splice(index, 1);

        let contagemTributos = 0;

        $ctrl.listaCentroFinanceiroAliquota.forEach(value => {
            let nomeTributo = value.aliquota.categoriaTributo.tributoTO.nome;

            if (nomeTributo !== 'ISS' && nomeTributo !== '') {
                contagemTributos++;
            }
        });  //FIXME: REFATORAR

        if (contagemTributos <= 0) {
            criarTributo(false);
        }

        calcularTotalValoresTributo();
        calcularTotalCFAliquota();
        criarListasTemporariasParaEdicao();
    };

    calcularTotalValoresTributo = () => {
        $ctrl.subTotalPercentualTributo = 0;
        $ctrl.totalPercentualTributo = 0;

        $ctrl.listaCentroFinanceiroAliquota.forEach(value => {
            let nomeTributo = value.aliquota.categoriaTributo.tributoTO.nome;

            if (nomeTributo !== 'ISS') {
                $ctrl.subTotalPercentualTributo += value.percentualTributo;
            }

            $ctrl.totalPercentualTributo += value.percentualTributo;
        });  //FIXME: REFATORAR

    };

    $ctrl.changeTributo = (centroFinanceiroAliquota) => {
        buscarCategoriaTributo(centroFinanceiroAliquota);
        criarListasTemporariasParaEdicao();
    };

    buscarCategoriaTributo = (centroFinanceiroAliquota) => {
        centroFinanceiroAliquota.listaTempCategoriaTributo = [];

        $ctrl.listaCategoriaTributo = [];
        let codigoTributo = centroFinanceiroAliquota.aliquota.categoriaTributo.tributoTO.codigo;

        if (codigoTributo) {

            let pCategoriaTributo = CategoriaTributo.getListaCategoriaTributoByCodTributo({
                id: codigoTributo
            }).$promise;

            pCategoriaTributo.then(function(data) {

                data.forEach(value => {
                    let ct = value;
                    ct.tributoTO = centroFinanceiroAliquota.aliquota.categoriaTributo.tributoTO;
                    ct.percentualTributo = 0;

                    ct.percentualTributo = Number(ct.percentualAliquota);

                    buscarAliquotasPorCategoriaTributo(ct);

                    centroFinanceiroAliquota.listaTempCategoriaTributo.push(ct);

                });

            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });
        }
    };

    $ctrl.changeCategoriaTributo = (centroFinanceiroAliquota) => {

        centroFinanceiroAliquota.percentualTributo = centroFinanceiroAliquota.aliquota.categoriaTributo.percentualTributo;

        let listaCfaParaCalculo = [];

        if (centroFinanceiroAliquota.codigo === undefined) {
            $ctrl.listaCentroFinanceiroAliquota.forEach(value => {
                listaCfaParaCalculo.push(value);
            });
            listaCfaParaCalculo.push(centroFinanceiroAliquota);

        } else {
            $ctrl.listaCentroFinanceiroAliquota.forEach(value => {
                if (value.codigo === centroFinanceiroAliquota.codigo) {
                    listaCfaParaCalculo.push(centroFinanceiroAliquota);
                } else {
                    listaCfaParaCalculo.push(value);
                }
            });
        }

        let percentualTotalTributos = $ctrl.listaCentroFinanceiroAliquota.reduce((acc, cfa) => acc + cfa.percentualTributo, 0);
        let baseDeTributos = $ctrl.object.fecho.custoMensal + $ctrl.object.fecho.bdi;
        centroFinanceiroAliquota.totalTributos = CentroFinanceiroAliquotaService.getValorTotal(baseDeTributos, centroFinanceiroAliquota, percentualTotalTributos);

        calcularTotalCFAliquota();
        calcularTotalValoresTributo();
    };

    $ctrl.adicionarTributo = (item) => {
        if (listaTributoSemIss().length < 3) {
            if (item.aliquota.categoriaTributo.codigo && item.aliquota.categoriaTributo.tributoTO.codigo) {
                criarTributo(false);
            }
        }
    };

    listaTributoSemIss = () => {
        return $ctrl.listaCentroFinanceiroAliquota.filter(value => { return value.aliquota.categoriaTributo.tributoTO.nome !== 'ISS'; });
    };

    // ## FUNÇÕES PARA O TRIBUTO ISS ##

    let iniciarTributoISS = (listaCentroFinanceiroAliquota) => {
        buscarUFs();

        if (listaCentroFinanceiroAliquota.some(value => { return value.aliquota.categoriaTributo.tributoTO.nome === 'ISS'; })) {
            $ctrl.object.ufTributoIss = '';

            let centroFinanceiroAliquota = listaCentroFinanceiroAliquota.filter(value => { return value.aliquota.categoriaTributo.tributoTO.nome === 'ISS'; })[0];
            $ctrl.object.ufTributoIss = centroFinanceiroAliquota.aliquota.categoriaTributo.cidade.uf;

            if ($ctrl.object.ufTributoIss) {
                buscarListaCategoriaTributoIss(4, $ctrl.object.ufTributoIss);
            }
        }
    };

    $ctrl.removerTributoIss = (item) => {
        let index = $ctrl.listaCentroFinanceiroAliquota.indexOf(item);
        $ctrl.listaCentroFinanceiroAliquota.splice(index, 1);
        $ctrl.object.ufTributoIss = undefined;

        criarTributo(true);
        calcularTotalValoresTributo();
        calcularTotalCFAliquota();
    };

    buscarUFs = () => {
        let pCidade = Cidade.buscarUFs().$promise;
        pCidade.then(function(data) {
            $ctrl.listaUF = data;
        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    //fixme
    $ctrl.cancelarTributoIss = () => {
        $ctrl.listaCentroFinanceiroAliquota.filter(value => value.aliquota.categoriaTributo.tributoTO.nome === 'ISS').forEach(value => value.edicao = false);
        $ctrl.object.tributoSelecionadoIss.edicao = false;
    };

    $scope.$on('comboUF', function() {
        let codigoTributoIss = 4;
        let ufSelecionado = $ctrl.object.ufTributoIss;
        buscarListaCategoriaTributoIss(codigoTributoIss, ufSelecionado);
    });

    buscarListaCategoriaTributoIss = (codigoTributoIss, ufSelecionado) => {
        $ctrl.listaCategoriaTributoIss = [];

        let pCategoriaTributo = CategoriaTributo.getListaCategoriaTributoByTributoUF({
            codTributo: codigoTributoIss,
            uf: ufSelecionado
        }).$promise;

        pCategoriaTributo.then(function(data) {
            data.forEach(value => {
                let ct = value;
                ct.tributoTO = {
                    codigo: 4,
                    nome: 'ISS'
                };
                ct.percentualTributo = 0;

                // Só é criado o objeto 'centroFinanceiroAliquota' para utilizar o metodo
                // 'CentroFinanceiroAliquotaService.getPercentualTributo' para calcular o percentual do tributo
                let centroFinanceiroAliquota = {
                    aliquota: {
                        categoriaTributo: {}
                    }
                };

                centroFinanceiroAliquota.aliquota.categoriaTributo = ct;

                ct.percentualTributo = Number(ct.percentualAliquota);

                buscarAliquotasPorCategoriaTributo(ct);

                $ctrl.listaCategoriaTributoIss.push(ct);
            });

        }).catch(function(response) {
            $scope.$emit('crud-critical', 'load', response);
        });
    };

    $ctrl.changeCategoriaIss = (centroFinanceiroAliquota) => {
        centroFinanceiroAliquota.percentualTributo = centroFinanceiroAliquota.aliquota.categoriaTributo.percentualTributo;

        let listaCfaParaCalculo = [];

        if (!centroFinanceiroAliquota.codigo) {
            $ctrl.listaCentroFinanceiroAliquota.forEach(value => {
                listaCfaParaCalculo.push(value);
            });
            listaCfaParaCalculo.push(centroFinanceiroAliquota);

        } else {
            $ctrl.listaCentroFinanceiroAliquota.forEach(value => {
                if (value.codigo === centroFinanceiroAliquota.codigo) {
                    listaCfaParaCalculo.push(centroFinanceiroAliquota);
                } else {
                    listaCfaParaCalculo.push(value);
                }
            });
        }

        let baseDeTributos = $ctrl.object.fecho.custoMensal + $ctrl.object.fecho.bdi;
        centroFinanceiroAliquota.totalTributos = CentroFinanceiroAliquotaService.getTotalCFAliquota(baseDeTributos, centroFinanceiroAliquota);

        calcularTotalCFAliquota();
        calcularTotalValoresTributo();

    };

}
