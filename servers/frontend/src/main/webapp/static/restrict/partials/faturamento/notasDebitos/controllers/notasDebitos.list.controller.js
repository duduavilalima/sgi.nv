(function() {
    'use strict';

    angular.module('app.notaDebito').controller('NotaDebitoFaturamentoListarCtrl', notaDebitoFaturamentoListarCtrlFn);


    function notaDebitoFaturamentoListarCtrlFn($scope, $controller, $state, NotaDebito, NotaFiscalFaturamento, $rootScope, Unidade, $mdDialog, UsuarioLogadoService, EnumService) {
        $controller('SearchController', {
            $scope: $scope,
            Resource: NotaDebito
        });
        $controller('GridController', {
            $scope: $scope,
            Resource: NotaDebito
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
            if ($scope.object.empresa.codigo == -1) {
                delete $scope.object.empresa;
            }
        });

        $scope.buscarEmpresas();

		EnumService.enumValues({
			pacote: 'br.com.araujoabreu.sgi.compras.domain',
			classe: 'NotaDebito',
			enum: 'SituacaoNotaDebito'
		})
		.$promise.then(function (data) {
				$scope.enumStatus = [];
				$scope.enumStatus.push('');
				angular.forEach(data, function(value, key){
					$scope.enumStatus.push(value)
				});
			}).catch(function (response) {
				$scope.$emit('crud-critical', 'load', response);
			});

		$scope.$on('changeSituacao', function () {
			if ($scope.object.situacao === '') {
				delete $scope.object.situacao;
			}
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
        //Coluna Tipo
        $scope.getTipoContrato = function(tipo) {
            if (tipo == undefined) return "";

            return tipo == 'Contratual' ? 'C' : 'E';
        };

		$scope.getCLSituacao = function(situacao) {
			if ((situacao == 'Ativo' || situacao == 'Encerrado, mas com faturamento')){
				return "";
			} else {
				return ` (${situacao})`;
			}
		};
		$scope.getCLSituacaoTachado = function(situacaoTachado) {
			if (situacaoTachado == 'Ativo'  || situacaoTachado == 'Encerrado, mas com faturamento'){
				return "";
			} else {
				return "text-decoration: line-through;";
			}
		};

		var cellTemplateCLSituacao = '<span style="white-space: nowrap;">{{ grid.appScope.getCLSituacao(row.entity.centroFinanceiro.situacao) }}</span>';

		var cellTemplateCLSituacaoTachado = '<span style="white-space: nowrap; {{ grid.appScope.getCLSituacaoTachado(row.entity.centroFinanceiro.situacao) }} ">{{ row.entity.centroFinanceiro.identificadorFinal }}</span>';

        var acoesAdicionais = [{
				nome: 'protocolo.objeto',
                label: 'Protocolo',
                icone: 'icon-file-document-box',
                tooltip: 'Incluir protocolo'
            }
		];

		$scope.exibirAcao = function(acao, row) {
            if (acao === 'protocolo') {
                if (row.entity.status === 'Em recebimento' && $scope.exibirBotoes) {
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
            NotaDebito.salvarProtocolo(nf).$promise.then(function(data) {
                $scope.notification.success(undefined, `Protocolo da ${nd.numero} atualizado com sucesso.`);
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

		$scope.protocolo = function(notaDebito) {
			// FIXME: Refatorar
            $mdDialog.show({
                controller: DialogControllerInserirProtocolo,
                locals: {
                    fnSalvar: salvarProtocolo,
                    nd: notaDebito
                },
                templateUrl: 'partials/notasDebitos/html/partials/dialog_protocolo_nd.html',
                parent: angular.element(document.body),
                targetEvent: undefined,
                clickOutsideToClose: false,
                fullscreen: false
            });
		};
		// FIXME: Refatorar
		function DialogControllerInserirProtocolo($scope, $mdDialog, fnSalvar, nd) {
			$scope.notaDebito = nd;
			$scope.copiaProtocoloEntrega = nd.protocoloEntrega;
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
            $scope.confirmar = function() {
				$scope.notaDebito.protocoloEntrega = $scope.copiaProtocoloEntrega;
                fnSalvar($scope.notaDebito);
                $mdDialog.cancel();
            };
        }
		/**
		* CAMPOS DATAS
		*/

		$scope.tipoDataEnum = [
                {
                    id : 1,
                    tipo: ""
                },
                {
                    id : 2,
                    tipo: "Emissão"
                },
                {
                    id : 3,
                    tipo: "Vencimento"
                },
                {
                    id : 4,
                    tipo: "Agendamento"
                },
                {
                    id : 5,
                    tipo: "Pagamento"
                },
                {
                    id : 6,
                    tipo: "Contabilização"
                },
                {
                    id : 7,
                    tipo: "Cancelamento"
                }
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

        //FIXME: displayName e para usar o texto sem modificaçao.
        var columnDefs = [{
                name: 'Número',
                field: 'numero',
                enableSorting: false,
                sortFieldString: false,
                width: '10%',
                pinnedLeft: false,
                funcao: ['exibir', 'row.entity']
            },
            {
                name: 'Situação',
                field: 'situacao',
                tooltip: ['field'],
                width: '11%'
            },
            {
                name: 'Empresa',
                field: 'empresa.codnome',
                width: '13%'
            },
            {
                name: 'Cliente/Sub (nome fantasia)',
                displayName: 'Cliente/Sub (nome fantasia)',
                field: 'cliente.nomeFantasia',
                tooltip: ['field.cliente.nomeFantasia']
            },
            {
                name: 'CL',
                displayName: 'CL',
                field: 'centroFinanceiro.identificadorFinal',
                cellTemplate: cellTemplateCLSituacaoTachado,
                cellClass: 'text-center',
                width: '5%',
                tooltip: ['field.centroFinanceiro.identificadorFinal', ' - ', 'field.centroFinanceiro.nome', `${cellTemplateCLSituacao}`]
            },
            {
                name: 'Emissão',
                field: 'dataEmissao',
                cellClass: 'text-center',
                width: '8%',
                cellFilter: 'formatacaoData'
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

        $scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'nota de débito', 'a');

        $scope.setDefaultParams = function() {
            $scope.paginationOptions.sortField = 'notaDebito.numero';
            $scope.paginationOptions.sortFieldString = false;
            $scope.paginationOptions.sortDir = 'asc';
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
            var resultado = NotaDebito.qbeST({}, obj).$promise;

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
                if (angular.isUndefined($scope.object.servicoTipo)) {
                    $scope.object.servicoTipo = {};
                }
                $scope.object.servicoTipo = {};
                $scope.object.servicoTipoCodigo = item.codigo;
                $scope.object.servicoTipo.codigo = item.codigo;
                $scope.object.servicoTipo.nome = item.nome;
                $scope.object.servicoTipo.sigla = item.sigla;
            } else {
                $scope.resultsServico = [];
                delete $scope.selectedItemServico;
                if (angular.isDefined($scope.object.servicoTipo)) {
                    delete $scope.object.servicoTipo;
                }
            }
        };

        $scope.exibir = function(notaDebito) {
            window.open(`/sgi/nv/app/main.html#!/notadebito/${notaDebito.codigo}/exibir`, '_blank');
        };

        $scope.imprimir = function(notaDebito) {
            var url = $state.href('notadebito.imprimir', {
                objectId: notaDebito.codigo
            });
            window.open(url, '_blank');
        };

    }

})();
