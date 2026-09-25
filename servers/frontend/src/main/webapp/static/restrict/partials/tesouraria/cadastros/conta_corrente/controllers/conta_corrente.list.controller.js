(function(){

	'use strict'

	angular
		.module('app.conta_corrente')
		.controller('ContaCorrenteListarCtrl', listController);

function listController($scope, $controller, $state, EnumService, $rootScope, Unidade, ContaCorrente, DialogConfirm){

    $controller('GridController', {$scope: $scope, Resource: ContaCorrente});

    $scope.modulo = $state.current.data.modulo;
    $scope.titulo = $state.current.data.titulo;
    $scope.subtitulo = $state.current.data.subtitulo;
    $scope.object = {};

    $scope.novaConta = function(){
        $state.go('conta_corrente.new');
    };

    $scope.exibir = function(codigo){
        $state.go('conta_corrente.view', { objectId: codigo});
    };

    $scope.editar = function(item){
        $state.go('conta_corrente.edit', { objectId: item.codigo});
    };

    function buscarEmpresas() {
        var filter = { tipoUnidade: 'MATRIZ' };
        Unidade.page(null, filter)
            .$promise.then(function(data) {
                $scope.empresaList = [];
                $scope.empresaList.push({});
                angular.forEach(data.content, function(value, key) {
                    $scope.empresaList.push(value);
                });
            }).catch(function(response) {
                console.error(response);
                $scope.$emit('crud-critical', 'search', response);
            });
    };

    $scope.changeEmpresa = function() {
       if ($scope.object.unidadePesquisa.codigo) {
           carregarBancos($scope.object.unidadePesquisa);
       } else {

       }
    };

    buscarEmpresas();
    var objQBE = {};
    carregarBancos(objQBE);

    function verificarDuplicidade(array, value) {
          var check = true;
		  angular.forEach(array, function(item, key) {
		      if(item.nome === value.nome) {
				  check = false;
				  return;
		      }
		  });
		  return check;
    };

	function carregarBancos(object) {
		$scope.bancoList = [];

		var objContaCorrente = {};

		if (angular.isDefined(object) && angular.isDefined(object.codigo) && object.codigo !== null) {
			objContaCorrente.pessoa = {codigo: object.codigo}
		}

		var pContaCorrente = ContaCorrente.page(null, objContaCorrente).$promise;

		pContaCorrente.then(function(data) {
			angular.forEach(data.content, function(value, key) {
				if(verificarDuplicidade($scope.bancoList, value.banco))
					$scope.bancoList.push(value.banco);
			});
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'contrato load', response);
			$scope.bancoList = [];
			$scope.object.agencia = "";
			$scope.object.digitoAgencia = "";
			$scope.object.conta = "";
			$scope.object.digito = "";
		});
	};


    EnumService.enumValues({
        pacote: 'br.com.araujoabreu.sgi.tesouraria.domain',
        classe: 'ContaCorrente',
        enum: 'TipoContaCorrente'
    })
    .$promise.then(function(data) {
        $scope.listTiposContas = [];
        angular.forEach(data, function(value, key) {
            $scope.listTiposContas.push(value);
        });
    }).catch(function(response) {
        $scope.$emit('crud-critical', 'load', response);
    });


    $scope.getAjustarColunaCheque = function(entity) {

        if (entity.chequeCarbono == true){
            return "Sim";
        }

        return "";
    }
    var cellTemplateTipoColunaCheque = '<span>{{grid.appScope.getAjustarColunaCheque(row.entity)}}</span>';

    $scope.getAjustarColunaInternet = function(entity) {

        if (entity.internetBanking == true){
            return "Sim";
        }

        return "";
    }
    var cellTemplateTipoColunaInternet = '<span>{{grid.appScope.getAjustarColunaInternet(row.entity)}}</span>';

    $scope.getAjustarColunaEmiteBoleto = function(entity) {
        return entity.boletoFaturamento === true ? "Sim" : "";
    };
    var cellTemplateColunaEmiteBoleto = '<span>{{grid.appScope.getAjustarColunaEmiteBoleto(row.entity)}}</span>';

    $scope.getAjustarColunaConBancaria = function(entity) {
        return entity.conciliacao === true ? "Sim" : "";
    };
    var cellTemplateColunaConBancaria = '<span>{{grid.appScope.getAjustarColunaConBancaria(row.entity)}}</span>';

    var acoesAdicionais = [
        {
            nome : 'editar.objeto', label: 'Editar', icone : 'icon-pencil', tooltip: 'Editar os dados desta conta corrente'
        },
        {
            nome : 'remover.objeto', label: 'Remover', icone : 'icon-close', tooltip: 'Remover esta conta bancária (esta operação não poderá ser desfeita)'
        }
    ];

    /* CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA */
    var columnDefs = [{
            name: 'Empresa',
            field: 'unidade.codnome',
            width: '15%',
            pinnedLeft: false,
            funcao: ['exibir', 'row.entity.codigo']
        }, {
            name: 'Banco',
            field: 'banco.nome',
            width: '12%'
        },  {
            name: 'Agência',
            field: 'agencia',
            width: '7%',
            cellClass: 'text-center'
        }, {
            name: 'DV agência',
            displayName: 'DV agência',
            field: 'digitoAgencia',
            tooltip: ['Dígito verificador da agência'],
            width: '7%',
            cellClass: 'text-center',
        }, {
            name: 'Conta',
            field: 'conta',
            width: '7%',
            cellClass: 'text-center padding-left-0-important padding-right-0-important'
        }, {
            name: 'DV conta',
            displayName: 'DV conta',
            field: 'digito',
            tooltip: ['Dígito verificador da conta'],
            width: '5%',
            cellClass: 'text-center'
        }, {
            name: 'Tipo',
            field: 'tipoContaCorrente',
            width: '10%',
            cellClass: 'text-left'
        } ,{
            name: 'Ch. Carbono',
            field: 'chequeCarbono',
            cellTemplate: cellTemplateTipoColunaCheque,
            headerTooltip: true,
            displayName: 'Indica se a folha de cheques possui carbono ou será necessário a impressão da cópia do cheque',
            width: '8%',
            cellClass: 'text-center'
        }, {
            name: 'I. Banking',
            field: 'internetBanking',
            cellTemplate: cellTemplateTipoColunaInternet,
            headerTooltip: true,
            displayName: 'Indica se possui interface através do internet banking ou não',
            width: '6%',
            cellClass: 'text-center'
        }, {
			name: 'Emite boleto',
			field: 'internetBanking',
			cellTemplate: cellTemplateColunaEmiteBoleto,
			width: '7%',
			cellClass: 'text-center'
		}, {
			name: 'Conciliação bancária',
			field: 'conciliacao',
			cellTemplate: cellTemplateColunaConBancaria,
			width: '11%',
			cellClass: 'text-center'
		}
        , {
            name: 'Opções',
            width: '5%',
            pinnedRight: false,
            enableSorting: false,
            acoesAdicionais: acoesAdicionais
    }];


    $scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'contacorrente', 'e');

    $scope.setDefaultParams = function() {
         $scope.paginationOptions.sortField = 'contaCorrente.codigo';
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
        $scope.object.tipoUnidade = "Matriz";
        $rootScope.loadingProgress = true;
        $scope.setDefaultParams();
        $scope.getPage(1);
    };

    $scope.$on('crud-success', function(event, action, data) {
        $rootScope.loadingProgress = false;
    });

    $scope.$watch('result', function() {
        $rootScope.loadingProgress = false;
    });


    $scope.reset = function() {
        $scope.result = null;
        $scope.clear();
    };

    $scope.remover = function(item) {
        // Chamada do serviço que criará um dialog de confirmação.
        DialogConfirm.show(
            null,
            item,
            'Remover conta corrente: Confirmação',
            'Deseja realmente remover esta conta corrente (esta operação não poderá ser desfeita)?',
            'Confirmar',
            'Cancelar',
            confirmRemover,
            cancelRemover);
    };

    // Função adicionada na chamada do dialog para confirmação.
    function confirmRemover(object, fnHide) {
        ContaCorrente.delete({id: object.codigo}).$promise.then(function(data){
            $scope.notification.success("Conta corrente removida com sucesso.");

            var index = null;
            // Framework do grid usar um array chamado "$scope.result"
            angular.forEach($scope.result, function(value, key) {
                if (value.codigo === object.codigo) {
                    index = key;
                    return;
                }
            });

            if (index !== null) {
                $scope.result.splice(index, 1);
                index = null;
            }

            // Esta função veio do dialog.
            fnHide();
        }).catch(function(response){
            fnHide();
            console.error('crud-critical', 'dialog', response);
            $scope.$emit('crud-critical', 'dialog', response);
        });
    };

    // Função adicionada na chamada do dialog para cancelamento do mesmo.
    function cancelRemover(fnHide) {
        // Esta função veio do dialog.
        // Aqui você pode fazer qualquer ação ao cancelar um dialog de confirmação.
        fnHide();
    };
};

})();
