(function() {
    'use strict';

    angular.module('app.notaFiscalFaturamento').controller('NotaFiscalFaturamentoListarCtrl', notaFiscalFaturamentoListarCtrlFn);


    function notaFiscalFaturamentoListarCtrlFn($scope, $controller, $state, NotaFiscalFaturamento, $rootScope, Unidade, NotaSaidaParcela, $mdDialog, UsuarioLogadoService, EnumService) {
        $controller('SearchController', {
            $scope: $scope,
            Resource: NotaSaidaParcela
        });
        $controller('GridController', {
            $scope: $scope,
            Resource: NotaSaidaParcela
        });

        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;

        $scope.exibirBotoes = false;
        $scope.object = {};
        $scope.result = undefined;

        $scope.verificarPermissao = () => {
            this.isAction = true;
            // Exibir apenas quando for usuários do faturamento;
            UsuarioLogadoService.apenasFaturamento().$promise.then(response => {
                this.isAction = false;
                $scope.exibirBotoes = response.value;
            });
        };

        $scope.verificarPermissao();

        $scope.buscarEmpresas = function() {
            var codigosUnidadesIgnoradas = [8]; // vide SGINV-1405

            Unidade.unidadesporempresaexceto(undefined, codigosUnidadesIgnoradas).$promise.then(function(data) {
                $scope.empresaList = [];
                $scope.empresaList.push({
                    codigo: -1,
                    codnome: ''
                });
                angular.forEach(data, function(value, key) {
                    $scope.empresaList.push(value);
                });
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'search', response);
            });
        };
        $scope.$on('changeEmpresa', function() {
            if ($scope.object.notaSaida.empresa.codigo == -1) {
                delete $scope.object.notaSaida.empresa.codigo;
            }
        });

        $scope.buscarEmpresas();

        //FIXME: a listaSituacao está sendo criada no frontend, mas deveria ser criada no backend.
        $scope.listaSituacao = [
        	{
                nome: '',
                nomeStatus: '',
                ordemStatus: -1
            },
            {
                nome: 'ABERTA_FATURAMENTO',
                nomeStatus: 'Aberta',
                ordemStatus: 50
            },
            {
                nome: 'A_PROCESSAR_FATURAMENTO',
                nomeStatus: 'A processar',
                ordemStatus: 51
            },
            {
                nome: 'EM_PROCESSAMENTO_FATURAMENTO',
                nomeStatus: 'Em processamento',
                ordemStatus: 52
            },
            {
                nome: 'EM_RECEBIMENTO_FATURAMENTO',
                nomeStatus: 'Em recebimento',
                ordemStatus: 53
            },
            {
                nome: 'RECEBIDA_FATURAMENTO',
                nomeStatus: 'Recebida',
                ordemStatus: 54
            },
            {
                nome: 'CANCELADA_FATURAMENTO',
                nomeStatus: 'Cancelada',
                ordemStatus: 55
            },
        ];

        $scope.$on('changeSituacao', function() {
            if ($scope.object.notaSaida.status === -1) {
                delete $scope.object.notaSaida.status;
            }
        });

        $scope.carregarListaItem = function() {
                    EnumService.enumValues({
                        pacote: 'br.com.araujoabreu.sgi.administracao.domain',
                        classe: 'NaturezaFornecimento',
                        enum: 'TipoFornecimento'
                    })
                    .$promise.then((data) => {
                    	$scope.listaItem = [];
                    	$scope.listaItem.push('');
                        	angular.forEach(data, (value) => {
                            	$scope.listaItem.push(value);
                            });
					}).catch((response) => {
						$scope.$emit('crud-critical', 'load', response);
					});
                };

        $scope.$on('changeItem', function() {
        	if ($scope.object.notaSaida.tipoFornecimento === '') {
        	    delete $scope.object.notaSaida.tipoFornecimento;
        	}
        });

        $scope.carregarListaItem();

		/**
         * CAMPOS DATAS
         */

        $scope.tipoDataEnum = [
          { id: 1, tipo: "" },
          { id: 2, tipo: "Emissão" },
          { id: 3, tipo: "Vencimento" },
          { id: 4, tipo: "Agendamento" },
          { id: 5, tipo: "Pagamento" },
          { id: 6, tipo: "Contabilização" },
          { id: 7, tipo: "Cancelamento" }
        ];

        function limparCamposData1() {
          $scope.object.data1 = undefined;
          $scope.object.dataDe = undefined;
          $scope.object.dataAte = undefined;
          $scope.lblDataDe = 'Data de';
          $scope.lblDataAte = 'Data até';
        }

		limparCamposData1();

		let labelCamposDatas = (label, posicaoCampoData) => {
			if (label) {
				$scope.lblDataDe = `${label} de`;
				$scope.lblDataAte = `${label} até`;
			} else {
				if (posicaoCampoData === 1) {
					limparCamposData1();
				}
			}
		};

		let acaoDataMap = new Map();
		acaoDataMap.set(1, {label: undefined, fn: labelCamposDatas});
		acaoDataMap.set(2, {label: 'Emissão', fn: labelCamposDatas});
		acaoDataMap.set(3, {label: 'Vencimento', fn: labelCamposDatas});
		acaoDataMap.set(4, {label: 'Agendamento', fn: labelCamposDatas});
		acaoDataMap.set(5, {label: 'Pagamento', fn: labelCamposDatas});
		acaoDataMap.set(6, {label: 'Contabilização', fn: labelCamposDatas});
		acaoDataMap.set(7, {label: 'Cancelamento', fn: labelCamposDatas});

		$scope.$on('changeData1', function() {
			if ($scope.object.data1) {
				acaoDataMap.get($scope.object.data1.id).fn(acaoDataMap.get($scope.object.data1.id).label, 1);
				if ($scope.object.data1)
					$scope.object.tipoData = $scope.object.data1.tipo;
			}
			$scope.$applyAsync();
		});

        $scope.reset = function() {
            $scope.result = undefined;
            $scope.clear();
            delete $scope.object.servicoTipo;
            delete $scope.searchTextServico;
            delete $scope.object.servicoTipoCodigo;
            $scope.object = {};

            document.querySelectorAll('input[type=search]')[0].value = '';
        };

        /* CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */

        /* Criar métodos de formatação específicos para pesquisa */
        //Colunas
        $scope.getTipoContrato = function(tipo) {
            if (tipo == undefined) return "";

            return tipo == 'Contratual' ? 'C' : 'E';
        };

        $scope.getTipoSituacao = function (tipo) {
        	if (tipo == null) return "";

        	return tipo == 'Cancelada Faturamento' ? 'Cancelada' : tipo;
        };

        $scope.getTipoItem = function (tipo) {
        	if(tipo == "Só serviço") {
        		return "Serv"
        	} else if (tipo == "Serviço/Material") {
        		return "S/M"
        	} else if (tipo == "Só material") {
        		return "Mat"
        	} else {
        		return "";
        	}
        }

        $scope.getCLSituacao = function (situacao) {

        	if(situacao == 'Ativo' || situacao == 'Encerrado, mas com faturamento') {
        		return '';
        	} else {
        		return ` (${situacao})`
        	}
        }

        $scope.getCLSituacaoTachado = function(situacaoTachado) {
        	if(situacaoTachado == 'Ativo' || situacaoTachado == 'Encerrado, mas com faturamento') {
        		return '';
        	} else {
        		return 'text-decoration: line-through;';
        	}
        }

        var cellTemplateSituacao = '<span style="white-space: nowrap;">{{grid.appScope.getTipoSituacao(row.entity.notaSaida.status)}}</span>';

        var cellTemplateClienteSubcliente = '<span style="white-space: nowrap;">{{row.entity.nomeFantasiaDocumento}}</span>';

        var cellTemplateTipoItem = '<span style="white-space: nowrap;">{{grid.appScope.getTipoItem(row.entity.notaSaida.tipoFornecimento)}}</span>';

        var cellTemplateCLSituacao = '<span style="white-space: nowrap;">{{grid.appScope.getCLSituacao(row.entity.notaSaida.centroFinanceiro.situacao)}}</span>';

		var cellTemplateCLSituacaoTachado = '<span style="white-space: nowrap; {{grid.appScope.getCLSituacaoTachado(row.entity.notaSaida.centroFinanceiro.situacao)}} ">{{row.entity.notaSaida.centroFinanceiro.identificadorFinal}}</span>';

        var acoesAdicionais = [{
				nome: 'protocolo.objeto',
                label: 'Protocolo',
                icone: 'icon-file-document-box',
                tooltip: 'Incluir protocolo'
            }
		];

		$scope.exibirAcao = function(acao, row) {
            if (acao === 'protocolo') {
                if (row.entity.notaSaida.status === 'Em recebimento' && $scope.exibirBotoes) {
                    return true;
                }
            }
            if (acao === 'imprimir') {
                return true;
            }
            if (acao === 'exibir') {
                return true;
            }
            return false;
        };

		// FIXME: Refatorar
		const salvarProtocolo = function(nf) {
            $scope.actionProtocolo = true;
            $rootScope.loadingProgress = true;
			$scope.action = true;
            NotaFiscalFaturamento.salvarProtocolo(nf).$promise.then(function(data) {
                $scope.notification.success(undefined, `Protocolo da ${nf.numero} atualizado com sucesso.`);
                $rootScope.loadingProgress = false;
                $scope.actionProtocolo = false;
				$scope.action = false;
            }).catch(function(response) {
                $rootScope.loadingProgress = false;
                $scope.actionProtocolo = false;
                $scope.action = false;
                $scope.$emit('crud-critical', 'search', response);
                console.error(response);
            });
        };

		$scope.protocolo = function(notaFiscalParcela) {
			// FIXME: Refatorar
            $mdDialog.show({
                controller: DialogControllerInserirProtocolo,
                locals: {
                    fnSalvar: salvarProtocolo,
                    nf: notaFiscalParcela.notaSaida
                },
                templateUrl: 'partials/faturamento/notasFiscais/html/partials/dialog_protocolo_nf.html',
                parent: angular.element(document.body),
                targetEvent: undefined,
                clickOutsideToClose: false,
                fullscreen: false
            });
		};
		// FIXME: Refatorar
		function DialogControllerInserirProtocolo($scope, $mdDialog, fnSalvar, nf) {
			$scope.notaFiscal = nf;
			$scope.copiaProtocoloEntrega = nf.protocoloEntrega;
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
            $scope.confirmar = function() {
				$scope.notaFiscal.protocoloEntrega = $scope.copiaProtocoloEntrega;
                fnSalvar($scope.notaFiscal);
                $mdDialog.cancel();
            };
        }

        //FIXME: displayName e para usar o texto sem modificaçao.
        var columnDefs = [{
                name: 'Número',
                field: 'notaSaida.numero',
                enableSorting: false,
                sortFieldString: false,
                width: '10%',
                pinnedLeft: false,
                funcao: ['exibir', 'row.entity']
            },
            {
            	name: 'Itens',
            	field: 'notaSaida.tipoFornecimento',
            	cellTemplate: cellTemplateTipoItem,
            	tooltip: ['field'],
            	cellClass: 'text-center',
            	enableSorting: false,
            	sortFieldString: false,
            	width: '5%',
            	pinnedLeft: false
            },
            {
                name: 'Situação',
                field: 'notaSaida.status',
                cellTemplate: cellTemplateSituacao,
                tooltip: ['field'],
                width: '10%'
            },
            {
                name: 'Empresa',
                field: 'notaSaida.empresa.codnome',
                width: '13%'
            },
            {
                name: 'Cliente/Sub (nome fantasia)',
                displayName: 'Cliente/Sub (nome fantasia)',
                field: 'nomeFantasiaDocumento',
                cellTemplate: cellTemplateClienteSubcliente,
                tooltip: ['field.nomeFantasiaDocumento'],
            },
            {
                name: 'CL',
                displayName: 'CL',
                field: 'notaSaida.centroFinanceiro.identificadorFinal',
                cellTemplate: cellTemplateCLSituacaoTachado,
                cellClass: 'text-center',
                width: '5%',
                tooltip: ['field.notaSaida.centroFinanceiro.identificadorFinal', ' - ', 'field.notaSaida.centroFinanceiro.nome', `${cellTemplateCLSituacao}`]
            },
            {
                name: 'Emissão',
                field: 'notaSaida.dataEmissao',
                cellClass: 'text-center',
                width: '8%',
                cellFilter: 'formatacaoData'
            },
            {
                name: 'Parcela',
                field: 'ordem',
                width: '5%',
                cellClass: 'text-center'
            },
            {
                name: 'Vencimento',
                field: 'dataVencimento',
                cellClass: 'text-center',
                width: '8%',
                cellFilter: 'formatacaoData'
            },
            {
                name: 'Pagamento',
                field: 'dataPagamento',
                cellClass: 'text-center',
                width: '8%',
                cellFilter: 'formatacaoData'
            },
            {
                name: 'Opções',
                acoes: ['exibir.objeto', 'imprimir.objeto'],
                acoesAdicionais: acoesAdicionais,
                width: '5%',
                pinnedRight: false,
                enableSorting: false
            }
        ];

        $scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'nota fiscal', 'a');

        $scope.setDefaultParams = function() {
            $scope.paginationOptions.sortField = 'notaSaidaParcela.numero';
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

        /**
         * Pesquisa do Código do Serviço
         */
        $scope.querySearchServico = function(query) {
            $rootScope.loadingProgress = true;
            var obj = {};
            obj.nome = query;
            obj.situacao = 'Ativo';

            var servicos = [];
            var resultado = NotaFiscalFaturamento.qbeST({}, obj).$promise;

            return resultado.then(function(data) {
                $rootScope.loadingProgress = false;
                angular.forEach(data, function(servico) {
                    servicos.push(servico);
                });
                return servicos;
            }).catch(function(response) {
                console.error(response);
                $scope.$emit('crud-critical', 'search', response);
                servicos = [];
                return servicos;
            });
        };

        $scope.selectedItemChangeService = function(item) {
            if (angular.isDefined(item)) {
                if (angular.isUndefined($scope.object.notaSaida)) {
                    $scope.object.notaSaida = {};
                }
                $scope.object.notaSaida.servicoTipo = {};
                $scope.object.notaSaida.servicoTipoCodigo = item.codigo;
                $scope.object.notaSaida.servicoTipo.codigo = item.codigo;
                $scope.object.notaSaida.servicoTipo.nome = item.nome;
                $scope.object.notaSaida.servicoTipo.sigla = item.sigla;
            } else {
                $scope.resultsServico = [];
                delete $scope.selectedItemServico;
                if (angular.isDefined($scope.object.notaSaida)) {
                    delete $scope.object.notaSaida.servicoTipo;
                }
            }
        };

        $scope.exibir = function(notaFiscalParcela) {
            window.open(`/sgi/nv/app/main.html#!/notafiscal/${notaFiscalParcela.notaSaida.codigo}/exibir`, '_blank');
        };

        $scope.imprimir = function(notaFiscalParcela) {
            var url = $state.href('notafiscal.imprimir', {
                objectId: notaFiscalParcela.notaSaida.codigo
            });
            window.open(url, '_blank');
        };

    }

})();
