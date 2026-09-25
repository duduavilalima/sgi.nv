angular.module('app.tiposServico')

    .controller('TiposServicoListarCtrl', ['$scope', '$controller', '$state', 'TiposServico', 'EnumService', 'Unidade', '$mdDialog', '$rootScope', '$timeout', '$window', 'CalculadorFaturamento', function($scope, $controller, $state, TiposServico, EnumService, Unidade, $mdDialog, $rootScope, $timeout, $window, CalculadorFaturamento) {
    	"use strict";
        $controller('SearchController', {$scope: $scope, Resource: TiposServico});
        $controller('GridController', {$scope: $scope, Resource: TiposServico});

        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;
        $scope.titulo_pesquisa = $state.current.data.titulo_pesquisa;

        $scope.enumStatus = ['Ativo', 'Inativo'];
        $scope.tipoFaturamentoList = [{label: "Selecione..." , value: null },
									  {label: "Sim" , value: "Habilitado" },
									  {label: "Sim (permite incluir cidade do contrato)" , value: "Habilitado com cidade" },
									  {label: "Não" , value: "Não habilitado" }];

        var mapTipoPesquisa = new Map();
        mapTipoPesquisa.set("Habilitado", "");
        mapTipoPesquisa.set("Não habilitado", "Não");
        mapTipoPesquisa.set("Habilitado com cidade", "Sim (com cidade)");

		$scope.getTipoFaturamento = function(tipo) {
			if (tipo == null) return "";
			return mapTipoPesquisa.get(tipo);
		}

        //FIXME: displayName e para usar o texto sem modificaçao.
        var columnDefs = [{
                name: 'Sigla',
                field: 'sigla',
                enableSorting: false,
                sortFieldString: false,
                width: '9%',
                pinnedLeft: false,
                funcao: ['exibir', 'row.entity.codigo']
            },
            {
                name: 'Nome',
                field: 'nome',
				tooltip: ['field'],
				cellTemplate: '<div class="text-left text-overflow-cut">{{row.entity.nome}}</div>'
            },
			{
				name: 'Habilitado',
				field: 'tipoFaturamento',
				cellTemplate: '<div class="text-center text-overflow-cut"> {{grid.appScope.getTipoFaturamento(row.entity.tipoFaturamento)}}</div>',
				width: '8%'
			},
			{
				name: 'Situação',
				field: 'situacao',
				cellTemplate: '<div>{{(row.entity.situacao === "Ativo") ? " " : row.entity.situacao}}</div>',
				width: '10%',
				cellClass: 'text-center'
			},
            {
                //				name:'Opções', acoes: ['exibir', 'editar', 'imprimir', 'excluir'], width: '5%', pinnedRight: false, enableSorting: false
                name: 'Opções',
                acoes: ['exibir'],
                width: '6%',
                pinnedRight: false,
                enableSorting: false,
//                acoesAdicionais: acoesAdicionais
                //				name:'Opções', acoes: ['exibir'], width: '5%', pinnedRight: false, enableSorting: false, acoesAdicionais: acoesAdicionais
                //				name:'O1pções', width: '5%', pinnedRight: false, enableSorting: false, acoesAdicionais: acoesAdicionais
            }
        ];

        $scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'lote', 'e');

        $scope.setDefaultParams = function() {
            $scope.paginationOptions.sortField = 'codigo';
            $scope.paginationOptions.sortFieldString = false;
            $scope.paginationOptions.sortDir = 'asc';
            //      $scope.paginationOptions.pageSize = 5; //parametrizar para utilização do teste automatizado.
        }

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
            $state.go('tiposServico.list');
        };

        $scope.exibir = function(codigo) {
            $state.go('tiposServico.exibir', {
                objectId: codigo
            });
        };

        $scope.editar = function(codigo) {
            $state.go('tiposServico.editar', {
                objectId: codigo
            });
        };

        $scope.reset = function() {
            $scope.result = null;
            $scope.clear();
        };

    }])

    .controller('TiposServicoCadastroCtrl', ['$scope', '$state', '$stateParams', '$controller', 'TiposServico', '$mdDialog', 'EnumService', 'Unidade', '$rootScope', '$document', '$timeout', 'DialogConfirm', function($scope, $state, $stateParams, $controller, TiposServico, $mdDialog, EnumService, Unidade, $rootScope, $document, $timeout, DialogConfirm) {

        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
        $scope.subtitulo = $state.current.data.subtitulo;
        $scope.titulo_pesquisa = $state.current.data.titulo_pesquisa;
        $scope.action = false;
        $scope.object = null;

        $scope.enumStatus = ['Ativo', 'Inativo'];
		$scope.tipoFaturamentoList = [{label: "Sim" , value: "Habilitado" },
									  {label: "Sim (permite incluir cidade do contrato)" , value: "Habilitado com cidade" },
									  {label: "Não" , value: "Não habilitado" }];

		$scope.tipoFaturamento = "";
		var mapTipo = new Map();
		mapTipo.set("Habilitado", "Sim");
		mapTipo.set("Não habilitado", "Não");
		mapTipo.set("Habilitado com cidade", "Sim (permite incluir cidade do contrato)");

        $scope.loadObj = function() {
            $scope.action = true;
            $scope.objectId = $stateParams.objectId;

            if (!$scope.objectId) {
                $scope.$emit('crud-error', 'invalid-id');
                $scope.action = false;
                return;
            } else {
                $rootScope.loadingProgress = true;
                TiposServico.get({id: $scope.objectId}).$promise.then(function(data) {
						$scope.object = angular.copy(data);
						getTipoServicoPai($scope.object);
                        $scope.tipoFaturamento = mapTipo.get(data.tipoFaturamento);
                        $rootScope.loadingProgress = false;
                        $scope.action = false;
                    }).catch(function(response) {
                        console.error(response);
                        $rootScope.loadingProgress = false;
                        $scope.action = false;
                        $scope.$emit('crud-critical', 'load', response);
                    });
            }
        };

        function getTipoServicoPai(tipoServico) {
            if (tipoServico && tipoServico.codigoPai) {
				TiposServico.get({id: tipoServico.codigoPai }).$promise.then(function(data) {
					tipoServico.servicoTipoPai = angular.copy(data);
				}).catch(function(response) {
					console.error(response);
					$scope.$emit('crud-critical', 'load', response);
				});
            }
        }

        $scope.newObj = function() {
            $rootScope.loadingProgress = true;
            $scope.action = true;
            TiposServico.get().$promise.then(function(data, status, headers, config) {
                    $rootScope.loadingProgress = false;
                    $scope.object = new TiposServico();
                    $scope.object = angular.merge(data, $scope.object);
                    $scope.action = false;
                }, function(response) {
                    $rootScope.loadingProgress = false;
                    $scope.action = false;
                    $scope.$emit('crud-critical', 'load', response);
                });
        }

        $scope.listar = function() {
            $state.go('tiposServico.list');
        };

        $scope.buscarTipoDeServico = function(sigla) {
        	TiposServico.buscarServicoTipo({sigla: $scope.object.sigla}).$promise.then(function(data) {
				$scope.object.servicoTipoPai = angular.copy(data);
				$rootScope.loadingProgress = false;
				$scope.action = false;
			}).catch(function(response) {
			    console.error(response);
				$rootScope.loadingProgress = false;
				$scope.action = false;
				$scope.$emit('crud-critical', 'load', response);
			});

        }

        $scope.salvar = function() {

        if ($scope.object.sigla.length < 2 && (($scope.object.sigla.length > 2 && $scope.object.sigla.substring(2,3) !== ".") || ($scope.object.sigla.length > 6 && $scope.object.sigla.substring(5,6) !== "."))) {
            $scope.notification.error("", "Formato errado para o campo sigla.");
		    return;
        }

        var stringSplit = $scope.object.sigla.split(".");

		var passou = true;

        for (var i = 0; i < stringSplit.length; i++) {
            if (!$.isNumeric(stringSplit[i]) || stringSplit[i].length < 2 || stringSplit[i].length > 2) {
                passou = false;
            }
        }

        if (!passou) {
            $scope.notification.error("", "Formato errado para o campo sigla.");
            return;
        }

            $rootScope.loadingProgress = true;
            $scope.action = true;
            if ($scope.screenMode === 'NEW') {
                TiposServico.save($scope.object).$promise.then(function(data) {
					$scope.notification.success("", "Tipos de serviço salvo com sucesso");
					$state.go('tiposServico.exibir', {objectId: data.codigo});
				}).catch(function(response) {
					$scope.action = false;
					$rootScope.loadingProgress = false;
					console.error(response);
					$scope.$emit('crud-critical', 'save', response);
				});

            } else if ($scope.screenMode === 'EDIT') {
                $scope.object.$update({id : $scope.object.codigo}).then(function(data) {
                        $scope.notification.success("", "Tipos de serviço atualizado com sucesso");
                        $state.go('tiposServico.exibir', {objectId: data.codigo});
                    }).catch(function(response) {
                        $scope.action = false;
                        console.error(response);
                        $scope.$emit('crud-critical', 'save', response);
                    });
            }
        };

        $scope.remover = function() {
			var tituloDialog = "Tipo de serviço - Remover: Confirmação";
			var msgDialog = "Deseja realmente remover este tipo de serviço (esta operação não poderá ser desfeita)?";
			var tituloBotaoConfirmar = "CONFIRMAR";
			var tituloBotaoCancelar = "CANCELAR";

			function confirmRemover(object, fnHide) {
				$scope.action = true;
				$rootScope.loadingProgress = true;
				TiposServico.delete({id: object.codigo}).$promise.then(function(data){
					var title = 'Remover tipo de serviço';
					var message = 'Tipo de serviço ' + object.sigla + ' removido com sucesso.';
					$scope.notification.success(title, message);
					fnHide();
					$state.go('tiposServico.list');
					$scope.action = false;
					$rootScope.loadingProgress = false;
				}).catch(function(response){
					console.error('crud-critical', 'dialog', response);
					$scope.action = false;
					$rootScope.loadingProgress = false;
					$scope.notification.criticalError(response.status, response.statusText, response.data);
					fnHide();
				});
			};

            DialogConfirm.show(null, $scope.object, tituloDialog, msgDialog, tituloBotaoConfirmar, tituloBotaoCancelar, confirmRemover, function(fnHide) {fnHide();});
        };

        $scope.editar = function() {
            $state.go('tiposServico.editar', {objectId: $scope.object.codigo});
        };

        $scope.cancelar = function() {
			$state.go('tiposServico.list', {objectId: $scope.object.codigo});
		};

		if ($scope.screenMode === 'NEW') {
			$scope.newObj();
		} else if ($scope.screenMode === 'EDIT' || $scope.screenMode === 'VIEW') {
			$scope.loadObj();
		};
    }]);
