angular.module('app.etapasNaoFaturadas')

    .controller('EtapasNaoFaturadasListarCtrl', ['$scope', '$controller', '$state', 'FaturamentoEtapa', 'Faturamento', 'EnumService', 'AutocompleteService', 'CalculadorFaturamento', '$mdDialog', '$rootScope', '$timeout', 'Etapasnaofaturadas', function($scope, $controller, $state, FaturamentoEtapa, Faturamento, EnumService, AutocompleteService, CalculadorFaturamento, $mdDialog, $rootScope, $timeout, Etapasnaofaturadas) {
        $controller('SearchController', {
            $scope: $scope,
            Resource: FaturamentoEtapa
        });
        $controller('GridController', {
            $scope: $scope,
            Resource: FaturamentoEtapa
        });

        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;
        $scope.titulo_pesquisa = $state.current.data.titulo_pesquisa;


        $scope.object.faturamento = {}
        $scope.object.situacao = 'A faturar';


        EnumService.enumValues({
                pacote: 'br.com.araujoabreu.sgi.faturamento.domain',
                classe: 'Faturamento',
                enum: 'Status'
            })
            .$promise.then(function(data) {
                $scope.enumStatus = [];
                $scope.enumStatus.push('');
                angular.forEach(data, function(value, key) {
                    $scope.enumStatus.push(value);
                });
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });
        $scope.$on('changeSituacao', function() {
            if ($scope.object.situacao === '') {
                $timeout(function() {
                    delete $scope.object.situacao;
                }, 200);
            }
        });

        EnumService.enumValues({
                pacote: 'br.com.araujoabreu.sgi.faturamento.domain',
                classe: 'Faturamento',
                enum: 'TipoFaturamento'
            })
            .$promise.then(function(data) {
                $scope.enumTipo = [];
                $scope.enumTipo.push('');
                angular.forEach(data, function(value, key) {
                    $scope.enumTipo.push(value);
                });
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });
        $scope.$on('changeTipo', function() {
            if ($scope.object.tipo === '') {
                $timeout(function() {
                    delete $scope.object.tipo;
                }, 200);
            }
        });

        EnumService.enumValues({
				pacote: 'br.com.araujoabreu.sgi.faturamento.domain',
				classe: 'Faturamento',
				enum: 'TipoDocumentoFaturamento'
			})
			.$promise.then(function(data) {
				$scope.tipoDocFatList = [];
				$scope.tipoDocFatList.push('');
				angular.forEach(data, function(value, key) {
					$scope.tipoDocFatList.push(value);
				});
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});
		$scope.$on('changeTipoDocFat', function() {
			if ($scope.object.tipoDocumentoFaturamento === '') {
				$timeout(function() {
					delete $scope.object.tipoDocumentoFaturamento;
				}, 200);
			}
		});

		EnumService.enumValues({
				pacote: 'br.com.araujoabreu.sgi.faturamento.domain',
				classe: 'Faturamento',
				enum: 'TipoDocumentoFaturamento'
			})
			.$promise.then(function(data) {
				$scope.tipoDocFatList = [];
				$scope.tipoDocFatList.push('');
				angular.forEach(data, function(value, key) {
					$scope.tipoDocFatList.push(value);
				});
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});
		$scope.$on('changeTipoDocFat', function() {
			if ($scope.object.tipoDocumentoFaturamento === '') {
				$timeout(function() {
					delete $scope.object.tipoDocumentoFaturamento;
				}, 200);
			}
		});

		EnumService.enumValues({
				pacote: 'br.com.araujoabreu.sgi.faturamento.domain',
				classe: 'Faturamento',
				enum: 'TipoFornecimentoNF'
			})
			.$promise.then(function(data) {
				$scope.divMatSerList = [];
				$scope.divMatSerList.push('');
				angular.forEach(data, function(value, key) {
					$scope.divMatSerList.push(value);
				});
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
			});
		$scope.$on('changeTipoFornec', function() {
			if ($scope.object.tipoFornecimento === '') {
				$timeout(function() {
					delete $scope.object.tipoFornecimento;
				}, 200);
			}
		});

        $scope.dtInstance = {};


        /* CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */

        /* Criar métodos de formatação específicos para pesquisa */
        //Coluna Tipo
        $scope.getTipoContrato = function(tipo) {
            if (tipo == null) return "";

            return tipo == 'Contratual' ? 'C' : 'E';
        }
        var cellTemplateTipo = '<span>{{grid.appScope.getTipoContrato(row.entity.faturamento.tipo)}}</span>';

        var cellTemplateIdentificacao = '<span style="white-space: nowrap;">{{row.entity.faturamento.identificacao}}</span>';

        var acoesAdicionais = [{
            nome: 'emitir',
            label: 'Emitir',
            icone: 'icon-checkbox-marked-outline',
            tooltip: 'Emitir nota fiscal'
        }];

        var columnDefs = [{
                name: 'Faturamento',
                field: 'faturamento.numero',
                width: '8%'
            },
            {
                name: 'Tipo',
                field: 'faturamento.tipo',
                width: '5%',
                cellClass: 'text-center',
                cellTemplate: cellTemplateTipo,
                tooltip: ['field']
            },
            {
                name: 'Identificação',
                field: 'faturamento.identificacao',
                cellTemplate: cellTemplateIdentificacao,
                tooltip: ['field'],
                width: '8%'
            },
            {
                name: 'Cliente/Subcliente',
                displayName: 'Cliente/Sub (nome fantasia)',
                field: 'nomeCliente',
                tooltip: ['field.nomeCliente']
            },
            {
                name: 'CL',
                displayName: 'CL',
                cellClass: 'text-center',
                field: 'faturamento.centroFinanceiro.identificadorFinal',
                width: '5%',
                tooltip: ['field.faturamento.centroFinanceiro.identificadorFinal', ' - ', 'field.faturamento.centroFinanceiro.nome']
            },
            {
                name: 'Período',
                displayName: 'Período',
                field: 'faturamento.periodo',
                width: '10%'
            },
            {
                name: 'Etapa',
                field: 'numero',
                cellClass: 'text-center',
                width: '5%'
            },
            {
                name: 'Descrição do item',
                displayName: 'Descrição do item',
                field: 'descricao',
                width: '15%',
                cellTemplate: '<span>{{row.entity.descricao}}</span>'
            },
            {
                name: 'Valor',
                displayName: 'Valor',
                field: 'valor',
                width: '8%',
                cellFilter: 'formatacaoMoeda',
                cellClass: 'text-right'
            },
            {
                name: 'Opções',
                width: '5%',
                pinnedRight: false,
                enableSorting: false,
                acoesAdicionais: acoesAdicionais
            }
        ];

        $scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'nota fiscal', 'a');

        $scope.setDefaultParams = function() {
            $scope.paginationOptions.sortField = 'codigo';
            $scope.paginationOptions.sortFieldString = false;
            $scope.paginationOptions.sortDir = 'asc';
        }
        $scope.setDefaultParams();

        $scope.getPage = function(page) {
            $scope.getPageGrid($scope.gridPesquisa, page);
        };

        /* FIM DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */

        $scope.buscar = function() {
            $scope.object.situacao = 'A faturar';
            $rootScope.loadingProgress = true;
            $scope.setDefaultParams();
            $scope.getPage(1);
        };

        $scope.$on('crud-success', function(event, action, data) {
            for (var i = 0; i < data.content.length; i++) {
                var etapa = data.content[i];
                etapa.numero = CalculadorFaturamento.formataNumero(etapa.numero, 3);
            }
        });

        $scope.$watch('result', function() {
            $rootScope.loadingProgress = false;
        });

        $scope.listar = function() {
            $state.go('etapasnaofaturadas.listar');
        };

        $scope.editar = function(item) {
            $state.go('etapasnaofaturadas.exibir', {
                objectId: item.codigo
            });
        };

        $scope.emitir = function(codigo) {
            Etapasnaofaturadas.validarCL({id: codigo}).$promise
            .then(function (data) {
                if (data.validadoCL) {
                    // $state.go('notafiscal.novo', {objectIdEtapa: codigo});
                    let url = $state.href('notafiscal.novo', { objectIdEtapa: codigo });
                    window.open(url, '_blank');
                } else {
                    $scope.notification.error('', `O centro de lucro ${data.centroFinanceiro.identificadorFinal} não está ativo. Criação de NF não permitida.`);
                }
            }).catch(function (response) {
                console.error(response);
                $scope.$emit('crud-critical', 'load', response);
            });
        };

        $scope.getPessoas = function(texto) {
            return AutocompleteService.getPessoas({
                nome: texto
            });
        };

        $scope.reset = function() {
            $scope.result = null;
            $scope.clear();
            $scope.object.situacao = 'A faturar';
            $scope.object.faturamento = {}
        };



        $scope.$watch('result', function() {
            $rootScope.loadingProgress = false;
        });

        $scope.emitirNota = function(ev, item) {
            $mdDialog.show({
                controller: DialogControllerEmitirNota,
                locals: {
                    object: item,
                    notification: $scope.notification,
                    loadingProgress: $rootScope.loadingProgress
                },
                templateUrl: 'partials/etapas_nao_faturadas/partials/dialog_nf.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                fullscreen: false
            });
        };

        //==== DIALOG - SERÁ USADO O CONTROLE EtapasNaoFaturadasCadastroCtrl ====//
        function DialogControllerEmitirNota($scope, notification, loadingProgress) {
            $scope.$scope = $scope;
            $scope.notification = notification;
            $scope.loadingProgress = loadingProgress;

            $scope.hide = function() {
                $mdDialog.hide();
            };
            $scope.cancel = function() {
                $mdDialog.cancel();
            };
        }

        $scope.reset = function() {
            $scope.result = null;
            $scope.clear();
        };

    }]);
