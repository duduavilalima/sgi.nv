angular.module('app.faturamentoInterfaces')

    .controller('FaturamentoInterfacesListarCtrl', ['$scope', '$controller', '$state', 'FaturamentoInterfaces', 'EnumService', 'Unidade', '$mdDialog', '$rootScope', '$timeout', '$window', 'CalculadorFaturamento', 'Parametro', 'DialogConfirm', 'PessoaFisica', function($scope, $controller, $state,
        FaturamentoInterfaces, EnumService, Unidade, $mdDialog, $rootScope, $window, CalculadorFaturamento, Parametro, DialogConfirm, PessoaFisica) {
        $controller('SearchController', {
            $scope: $scope,
            Resource: FaturamentoInterfaces
        });
        $controller('GridController', {
            $scope: $scope,
            Resource: FaturamentoInterfaces
        });

        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;
        $scope.titulo_pesquisa = $state.current.data.titulo_pesquisa;

        $scope.empresaListComFiltro = [];

        EnumService.enumValues({
                pacote: 'br.com.araujoabreu.sgi.contabilidade.domain',
                classe: 'LoteInterface',
                enum: 'SituacaoLoteInterface'
            })
            .$promise.then(function(data) {
                $scope.enumStatus = [];
                var obj1 = {
                    value: "",
                    label: ""
                };
                $scope.enumStatus.push(obj1);
                angular.forEach(data, function(value, key) {

                    var obj = {
                        value: value,
                        label: value
                    };

                    if (value === "Importado na contabilidade") {
                        obj.label = "Importado na contabilidade/prefeitura";
                    }

                    $scope.enumStatus.push(obj);
                });
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });


        $scope.$on('changeSituacao', function() {
            if ($scope.object.situacao === '') {
                delete $scope.object.situacao;
            }
        });

        EnumService.enumValues({
                pacote: 'br.com.araujoabreu.sgi.contabilidade.domain',
                classe: 'LoteInterface',
                enum: 'TipoLoteInterface'
            })
            .$promise.then(function(data) {
                $scope.enumTipo = [];
                $scope.enumTipo.push({
                    tipo: '',
                    desabilitado: false,
                    empresaRelacionado: []
                });

                $scope.enumTipo.push({
                    tipo: data[17],
                    desabilitado: false,
                    empresaRelacionado: [1, 4]
                }, {
                    tipo: data[18],
                    desabilitado: false,
                    empresaRelacionado: [1, 4]
                }, {
                    tipo: '----------------------------------------',
                    desabilitado: true,
                    empresaRelacionado: []
                }, {
                    tipo: data[16],
                    desabilitado: false,
                    empresaRelacionado: [1]
                }, {
                    tipo: data[19],
                    desabilitado: false,
                    empresaRelacionado: [4]
                }, {
                    tipo: data[20],
                    desabilitado: false,
                    empresaRelacionado: [9]
                }, {
                    tipo: data[21],
                    desabilitado: false,
                    empresaRelacionado: [5]
                }, {
                    tipo: data[22],
                    desabilitado: false,
                    empresaRelacionado: [3]
                }, {
				   tipo: '----------------------------------------',
				   desabilitado: true,
				   empresaRelacionado: []
			   	}, {
				   tipo: data[25],
				   desabilitado: false,
				   empresaRelacionado: [3]
			    });

            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });

        $scope.$on('changeTipoLoteInterface', function() {
            if ($scope.object.tipoLoteInterface === '') {
                delete $scope.object.tipoLoteInterface;
                $scope.empresaListComFiltro = angular.copy($scope.empresaList);
            } else {

                let tipo = $scope.enumTipo.find((item) => {
                    return item.tipo === $scope.object.tipoLoteInterface;
                });

                $scope.empresaListComFiltro = $scope.empresaList.filter((itemEmpresa) => {
                    return tipo.empresaRelacionado.some((itemCodEmpresa) => {
                        return itemCodEmpresa === itemEmpresa.codigo;
                    });
                });

                $scope.empresaListComFiltro.unshift({
                    codnome: '',
                    codigo: undefined
                });

                if ($scope.object.unidade && $scope.object.unidade.codigo) {
                    if (!tipo.empresaRelacionado.some((item) => {
                            return item === $scope.object.unidade.codigo;
                        })) {
                        delete $scope.object.unidade;
                    }
                }
            }
        });

        $scope.buscarEmpresas = function() {
            const codigosUnidadesIgnoradas = [0]; // vide SGINV-1405

            // Ordenar pelo codigo da matriz e pelo codigo da unidade
            let ordenarPorMatriz = (a, b) => {
                let cod_a = a.codigo;
                let cod_b = b.codigo;
                let codMatriz_a = a.matriz && a.matriz.codigo ? a.matriz.codigo : a.codigo;
                let codMatriz_b = b.matriz && b.matriz.codigo ? b.matriz.codigo : b.codigo;

                if (codMatriz_a == codMatriz_b) {
                    return (cod_a < cod_b) ? -1 : (cod_a > cod_b) ? 1 : 0;
                } else {
                    return (codMatriz_a < codMatriz_b) ? -1 : 1;
                }
            };

            Unidade.unidadesporempresaexceto(null, codigosUnidadesIgnoradas).$promise.then(function(data) {
                $scope.empresaList = [];
                angular.forEach(data.sort(ordenarPorMatriz), function(value, key) {
                    if (value.emitirNfFaturamento) {
                        $scope.empresaList.push(value);
                    }
                });

                $scope.empresaListComFiltro = angular.copy($scope.empresaList);
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'search', response);
            });

        };

        $scope.$on('changeEmpresa', function() {
            if (!$scope.object.unidade.codigo) {
                delete $scope.object.unidade;
            }
        });

        $scope.buscarEmpresas();

        $scope.dtInstance = {};

        /* CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */

        /* Criar métodos de formatação específicos para pesquisa */
        //Coluna Tipo
        $scope.getTipoContrato = function(tipo) {
            if (tipo == null) return "";

            return tipo == 'Contratual' ? 'C' : 'E';
        };
        var cellTemplateTipo = '<span>{{grid.appScope.getTipoContrato(row.entity.tipo)}}</span>';

        var acoesAdicionais = [{
                nome: 'download.objeto',
                label: 'Download',
                icone: 'icon-download',
                tooltip: 'Realizar o download do arquivo deste lote'
            },
            {
                nome: 'imprimirRelatorio.objeto',
                label: 'Imprimir',
                icone: 'icon-printer',
                tooltip: 'Gerar relatório de críticas deste lote'
            },
            {
                nome: 'reprocessarLote.objeto',
                label: 'Reprocessar',
                icone: 'icon-refresh',
                tooltip: 'Reprocessar este lote'
            },
            {
                nome: 'confirmarEnvio.objeto',
                label: 'Confirmar envio',
                icone: 'icon-upload',
                tooltip: 'Confirmar envio das notas fiscais contidas no lote para a Prefeitura'
            }
        ];

        var cellTemplateTipoLote = '<div class="over-flow-hidden">{{row.entity.tipoLoteInterface}}</div>';
        var cellTemplateCriador = '<div class="over-flow-hidden">{{row.entity.criador}}</div>';

        //FIXME: displayName e para usar o texto sem modificaçao.
        var columnDefs = [{
                name: 'Lote',
                field: 'numero',
                enableSorting: false,
                sortFieldString: false,
                width: '8%',
                pinnedLeft: false,
                funcao: ['exibir', 'row.entity.codigo']
            },
            {
                name: 'Situação',
                field: 'situacao',
                width: '18%',
                tooltip: ['field']
            },
            {
                name: 'Matriz / Filial',
                field: 'unidade.codnome',
                tooltip: ['field'],
                cellClass: 'text-left'
            },
            {
                name: 'Tipo',
                field: 'tipoLoteInterface',
                width: '13%',
                cellTemplate: cellTemplateTipoLote,
                tooltip: ['field']
            },
            {
                name: 'Referência',
                field: 'dataEvento',
                width: '7%',
                cellClass: 'text-center',
                cellFilter: 'formatacaoData'
            },
            {
                name: 'Responsável',
                field: 'criador',
                tooltip: ['field'],
                cellTemplate: cellTemplateCriador,
                width: '25%'
            },
            {
                name: 'Execução',
                field: 'dataCriacao',
                width: '12%',
                cellClass: 'text-center text-truncate',
                tooltip: ['field'],
                cellFilter: 'formatacaoDataFull'
            },
            {
                //				name:'Opções', acoes: ['exibir', 'editar', 'imprimir', 'excluir'], width: '5%', pinnedRight: false, enableSorting: false
                name: 'Opções',
                acoes: ['exibir'],
                width: '5%',
                pinnedRight: false,
                enableSorting: false,
                acoesAdicionais: acoesAdicionais
                //				name:'Opções', acoes: ['exibir'], width: '5%', pinnedRight: false, enableSorting: false, acoesAdicionais: acoesAdicionais
                //				name:'O1pções', width: '5%', pinnedRight: false, enableSorting: false, acoesAdicionais: acoesAdicionais
            }
        ];

        $scope.exibirAcao = function(acao, row) {
            if (acao === 'download') {
                let varTipoLote = row.entity.tipoLoteInterface;
                let varSituacaoLote = row.entity.situacao;

                if ((varSituacaoLote === 'Arquivo gerado' && varTipoLote !== 'Nota Norte (Belém)' && varTipoLote !== 'NF/Material'&& varTipoLote !== 'Nota Paulistana' && varTipoLote !== 'Nota BH' && varTipoLote !== 'Nota Brasília') ||
                    (varTipoLote === 'Nota carioca' && (varSituacaoLote === 'Importado na contabilidade' || varSituacaoLote === 'Importado na Prefeitura'))
                ) {
                    return true;
                }
            }
            if (acao === 'imprimirRelatorio') {
                if ((row.entity.tipoLoteInterface === 'FATN - Faturamento emitido no período' || row.entity.tipoLoteInterface === 'FATC - Cancelamentos fora do período' ||
                        row.entity.tipoLoteInterface === 'DSFP - 2% sobre faturamento (desoneração da folha de pagamento)') &&
                    row.entity.situacao === 'Arquivo gerado') {
                    return true;
                }
            }
            if (acao === 'reprocessarLote') {
                if (row.entity.tipoLoteInterface === 'Nota carioca' && row.entity.situacao === 'Importado na Prefeitura') {
                    return false;
                }
                if (row.entity.situacao === 'Arquivo gerado' || row.entity.situacao === 'Processado com erros') {
                    return true;
                }
                if (row.entity.situacao === 'Arquivo gerado' && row.entity.tipoLoteInterface === 'Nota carioca') {
                    return false;
                }
            }
            if (acao === 'exibir') {
                return true;
            }
            if (acao === 'confirmarEnvio') {
                if (row.entity.situacao === 'Arquivo gerado') {
                    if (row.entity.tipoLoteInterface === 'Nota carioca' || row.entity.tipoLoteInterface === 'Nota Norte (Belém)' || row.entity.tipoLoteInterface === 'Nota Paulistana' ||
                        row.entity.tipoLoteInterface === 'Nota BH' || row.entity.tipoLoteInterface === 'Nota Brasília' || row.entity.tipoLoteInterface === 'NF/Material') {
                        return true;
                    }
                }
            }
            return false;
        };

        $scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'lote', 'e');

        $scope.setDefaultParams = function() {
            $scope.paginationOptions.sortField = 'codigo';
            $scope.paginationOptions.sortFieldString = false;
            $scope.paginationOptions.sortDir = 'asc';
            //      $scope.paginationOptions.pageSize = 5; //parametrizar para utilização do teste automatizado.
        };
        $scope.setDefaultParams();

        $scope.getPage = function(page) {
            $scope.getPageGrid($scope.gridPesquisa, page);
        };

        /* FIM DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */

        $scope.buscar = function() {
            $rootScope.loadingProgress = true;
            $scope.setDefaultParams();
            $scope.getPage(1);
        };

        $scope.$watch('result', function() {
            $rootScope.loadingProgress = false;
        });

        $scope.listar = function() {
            $state.go('interfaces.listar');
        };

        $scope.exibir = function(codigo) {
            $state.go('interfaces.exibir', {
                objectId: codigo
            });
        };

        $scope.editar = function(codigo) {
            $state.go('interfaces.editar', {
                objectId: codigo
            });
        };

        $scope.download = function(item) {
            FaturamentoInterfaces.anexolista({
                id: item.codigo
            }).$promise.then(function(listaAnexo) {

                if (listaAnexo && listaAnexo.length > 0) {

                    FaturamentoInterfaces.verificaranexo(item.codigo).$promise
                        .then(function(data) {
                            window.open(`/sgi/nv/rest/faturamento/interfaces/baixaranexo/${item.codigo}`);
                        }).catch(function(response) {
                            console.error(response);
                            $rootScope.loadingProgress = false;
                            if (response.data) {
                                $scope.notification.error("", response.data.message);
                            } else {
                                $scope.notification.error("", response);
                            }
                        });

                } else {
                    $scope.notification.atencao(undefined, "Não há anexos para este lote.");
                }

            }).catch(function(response) {
                console.error(response);
                $scope.notification.criticalError(response.status, response.statusText, response.data);
            });

        };

        $scope.imprimirRelatorio = function(item) {
            var url;

            Parametro.page({
                chave: 'layout.relatorios.temp.url'
            }).$promise.then(function(parametro) {
                var caminhoBase = '/relatorio/frameset?__locale=pt_BR&__format=pdf&__showtitle=false&__report=report/sgi/contabilidade/interfaceContabil/';

                if (!parametro || !parametro.valor) {
                    // Se a chave não foi encontrada, ou se o valor for vazio/nulo, consideramos que estamos em ambiente de
                    // desenvolvimento, e geramos a página com base no ambiente local (localhost), assim como era feito
                    // anteriormente.
                    url = window.location.hostname + ":8081/sgi" + caminhoBase;

                } else {
                    // Se a chave foi encontrada, consideramos que o valor do parâmetro é a url base (no geral, hostname +
                    // caminho de contexto). O resto da URL será gerado dinamicamente, de acordo com as opções escolhidas
                    // pelo usuário.
                    url = parametro.valor + caminhoBase;
                }

                montarURLRelatorio(url, item);

            }).catch(function(response) {
                console.error('crud-critical', 'dialog', response);
                $scope.action = false;
                $scope.notification.criticalError(response.status, response.statusText, response.data);
            });
        };

        function montarURLRelatorio(url, item) {
            //Chave e nome
            url = url + "relatorioCriticasLote" + ".rptdesign&nomRelatorio=";

            if (angular.isDefined(item.tipoLoteInterface) && item.tipoLoteInterface != null) {
                if (item.tipoLoteInterface === 'FATN - Faturamento emitido no período') {
                    url = url + "FATN";
                }
                if (item.tipoLoteInterface === 'FATC - Cancelamentos fora do período') {
                    url = url + "FATC";
                }
                if (item.tipoLoteInterface === 'DSFP - 2% sobre faturamento (desoneração da folha de pagamento)') {
                    url = url + "DSFP";
                }
            }

            if (angular.isDefined(item.codigo) && item.codigo != null) {
                url = url + "&numLote=" + item.numero + "&codLote=" + item.codigo;
            }

            url = url.replace(" ", "_").replace(',"', '"') + "&tot=1";

            $window.open(url);
        }

        $scope.reprocessarLote = function(item) {
            $mdDialog.show({
                controller: DialogControllerReprocessarLote,
                locals: {
                    faturamentoInterface: item,
                    resource: FaturamentoInterfaces,
                    notification: $scope.notification,
                    loadingProgress: $rootScope.loadingProgress
                },
                templateUrl: 'partials/faturamento/faturamentoInterfaces/html/partials/dialog_reprocessar_lote.html',
                parent: angular.element(document.body),
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        $scope.confirmarEnvio = function(item) {

            let tipoLi = item.tipoLoteInterface;

            if ((tipoLi === "Nota carioca" || tipoLi === "Nota Norte (Belém)" || tipoLi === "Nota Paulistana" || tipoLi === "Nota BH" || tipoLi === "Nota Brasília") &&
                item.situacao === "Arquivo gerado") {

                var funcaoCancelar = function(fnHide) {
                    fnHide();
                };

                var funcaoConfirmar = function(object, fnHide) {
                    efetuarConfirmacaoEnvio(item);
                    item.situacao = 'Importado na Prefeitura';
                    fnHide();
                };

                var mensagem = "Deseja realmente confirmar o envio das notas fiscais do lote " + item.numero + " para a Prefeitura (esta operação não poderá ser desfeita)?";
                var objeto = {};

                let tituloDialog = tipoLi + " - " + "Envio: Confirmação";

                DialogConfirm.show(
                    null,
                    objeto,
                    tituloDialog,
                    mensagem,
                    'Confirmar',
                    'Cancelar',
                    funcaoConfirmar,
                    funcaoCancelar);

            } else {
                efetuarConfirmacaoEnvio(item);
            }
        };

        function efetuarConfirmacaoEnvio(item) {
            FaturamentoInterfaces.confirmarEnvio({
                id: item.codigo
            }).$promise.then(function(data) {
                $scope.notification.success("", "Confirmação de envio das notas fiscais do lote " + item.numero + " realizada com sucesso");
            }).catch(function(response) {
                $scope.action = false;
                console.error('crud-critical', 'dialog', response);
                notification.criticalError(response.status, response.statusText, response.data);
            });
        }

        //==== DIALOG REPROCESSAR LOTE ====//
        function DialogControllerReprocessarLote($scope, $timeout, $mdDialog, $state, faturamentoInterface, resource, notification, loadingProgress) {
            $scope.action = false;
            $scope.confirm = function() {
                loadingProgress = true;
                $scope.action = true;
                FaturamentoInterfaces.reprocessar({
                        id: faturamentoInterface.codigo
                    })
                    .$promise.then(function(data) {
                        $timeout(function() {
                            faturamentoInterface.situacao = data.situacao;
                            faturamentoInterface.dataCriacao = null;
                            title = 'Reprocessar Lote';
                            message = 'Lote LT/' + faturamentoInterface.codigo + ' reprocessado com sucesso.';
                            notification.success(title, message);
                            $scope.cancel();
                            loadingProgress = false;
                            $scope.action = false;

                            var pPessoaFisica = PessoaFisica.buscarNomeByLogin(data.criador).$promise;
                            pPessoaFisica.then(function(data2) {
                                faturamentoInterface.criador = data2.nome;
                            }).catch(function(response2) {
                                $scope.$emit('crud-critical', 'load', response2);
                            });

                        }, 500);
                    }).catch(function(response) {
                        $scope.action = false;
                        console.error('crud-critical', 'dialog', response);
                        notification.criticalError(response.status, response.statusText, response.data);
                    });
            };

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
        }

        $scope.reset = function() {
            $scope.result = null;
            $scope.empresaListComFiltro = angular.copy($scope.empresaList);
            $scope.clear();
        };

        $scope.$on('changeTipoLoteInterface', function() {
            $scope.renderizarLblDataCancelamento();
        });

        $scope.renderizarLblDataCancelamento = function() {
            if ($scope.object.tipoLoteInterface === 'FATC - Cancelamentos fora do período') {
                return true;
            }
            return false;
        };

        $scope.desabilitarReprocessar = function(item) {
            if ((item.situacao != 'Aguardando geração do arquivo' &&
                    item.situacao != 'Gerando arquivo' &&
                    item.situacao != 'Importado na contabilidade') || (item.tipoLoteInterface === 'Nota carioca' && item.situacao == "Arquivo gerado")) {
                return true;
            }
            return false;
        };

        $scope.desabilitarDownload = function(item) {
            if (item.situacao != 'Aguardando geração do arquivo' && item.situacao != 'Gerando arquivo') {
                return true;
            }
            return false;
        };


    }]);
