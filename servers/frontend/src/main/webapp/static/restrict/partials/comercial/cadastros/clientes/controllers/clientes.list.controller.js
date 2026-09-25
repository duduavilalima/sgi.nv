angular.module('app.clientes').controller('ClientesListarCtrl', listController);

function listController($scope, $state, $rootScope, $controller, EnumService, Clientes, AutocompleteService, Cidade) {
	$controller('GridController', {
		$scope: $scope,
		Resource: Clientes
	});

	const $ctrl = this;

	let init = () => {
		$scope.object.ramoNegocio = undefined;
		$scope.object.cidade = undefined;
		initCampoSituacao();
		carregarClassificacaoCliente();

		$scope.object = {};
	};

	$ctrl.tipoList = ['','Cliente', '... Somente cliente', '... Fornecedor/Cliente', 'Subcliente'];

	let carregarClassificacaoCliente = () => {
		Clientes.buscarClassificacaoCliente().$promise.then(function(data) {
			$ctrl.listClassificacao = [];
			$ctrl.listClassificacao.push({codigo: undefined, nome: ''});
			$ctrl.listClassificacao.push(...data);
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'classificacao', response);
		});
	};

	$ctrl.listExcluir = [ {label: '', value: undefined}, {label: 'Sim', value: true}, {label: 'Não', value: false} ];

	$ctrl.listConta = [{
		nome: '',
		valor: undefined
	}, {
		nome: 'Matriz - Araujo Abreu',
		valor: 1
	}, {
		nome: 'Matriz - Norte',
		valor: 4
	}, {
		nome: 'Todas as anteriores',
		valor: 5
	}, {
		nome: '-------------------------------------------------',
		valor: 6
	}, {
		nome: `Não possui 'Matriz - Araujo Abreu'`,
		valor: 7
	}, {
		nome: `Não possui 'Matriz - Norte'`,
		valor: 8
	}, {
		nome: 'Não possui nenhuma',
		valor: 9
	}];

	let initCampoSituacao = () => {
		var pTiposSituacoes = EnumService.enumValues({
			pacote: 'br.com.fasolti.corporativo.pessoa.domain',
			classe: 'Pessoa',
			enum: 'Situacao'
		}).$promise;

		pTiposSituacoes.then(function(data) {
			$ctrl.listSituacao = [];
			$ctrl.listSituacao.push('');
			$ctrl.listSituacao.push(...data);
		}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
		});
	};

	//AUTOCOMPLETE RAMO DE NEGÓCIO
	$ctrl.querySearchRamoNegocio = (query) => {
		let filtro = {nome: query};
		let pRamoNegocio = AutocompleteService.getRamoNegocio(filtro).$promise;
		
		return pRamoNegocio.then(function(data) {
			let listaRamoNegocio = [];
			listaRamoNegocio.push(...data);
			return listaRamoNegocio;
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
			return [];
		});
	};

	$ctrl.selectedItemChange = (item) => {
		$scope.object.ramoNegocio = item;
	};
	//FIM AUTOCOMPLETE

	//AUTOCOMPLETE CIDADE
	$ctrl.querySearchCidade = (query) => {
		let filtro = query;
		let pCidade = Cidade.buscarListaPorNome(filtro).$promise;
		
		return pCidade.then(function(data) {
			let listaCidade = [];
			listaCidade.push(...data);
			return listaCidade;
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
			return [];
		});
	};

	$ctrl.selectedItemCidadeChange = (item) => {
		$scope.object.cidade = item;
	};
	//FIM AUTOCOMPLETE

	$ctrl.buscar = function() {
		$rootScope.loadingProgress = true;
		$scope.setDefaultParams();
		$scope.getPage(1);
	};

	// Criado para diferenciar cliente pessoa fisica e cliente pessoa juridica
	$ctrl.novo = (tipo) => {
		let tipoCliente;

		if(tipo === 'clientePF') {
			tipoCliente = 0;
		} else {
			tipoCliente = 1;
		}
		$state.go('clientes.new', { objectTipo: tipoCliente});
	};

	$ctrl.reset = () => {
		$scope.result = undefined;
		$scope.clear();
		document.querySelectorAll('input[type=search]')[0].value = '';
		document.querySelectorAll('input[type=search]')[1].value = '';
	};

	let acoesAdicionais = [
		{ nome : '$ctrl.exibir.objeto', label: 'Exibir', icone : 'icon-eye', tooltip: 'Exibir este cliente' },
		{ nome : '$ctrl.editar.objeto', label: 'Editar', icone : 'icon-pencil', tooltip: 'Editar este cliente' } 
	];

	/** INICIO DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA  */
	let columnDefs = [
		{
			name: 'Razão social / Nome',
			field: 'nome',
			width: '35%',
			enableSorting: false,
			sortFieldString: false,
			pinnedLeft: false,
			funcao: ['$ctrl.exibir', 'row.entity']
		},
		{
			name: 'Nome fantasia',
			field: 'nomeFantasia',
			cellClass: 'text-left text-truncate',
			tooltip: ['field'],
			width: '20%'
		},
		{
			name: 'CPF/ CNPJ',
			field: 'documento',
			width: '15%'
		},
		{
			name: 'Tipo',
			field: 'tipo',
			width: '15%',
			cellClass: 'text-center',
			detalheCliente: ['codigoClientePrincipal', 'tipoPessoa']
		},
		{
			name: '	Situação',
			field: 'situacao',
			width: '9%',
			cellClass: 'text-center'
		},
		{
			name: 'Opções',
			width: '6%',
			pinnedRight: false,
			enableSorting: false,
			acoesAdicionais: acoesAdicionais
		}
	];

	$scope.$on('crud-success', function(event, action, data) {
		$rootScope.loadingProgress = false;
	});

	$scope.$watch('result', function() {
		$rootScope.loadingProgress = false;
	});

	$scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'cliente', 'e');

	$scope.setDefaultParams = () => {
		$scope.paginationOptions.sortField = 'codigo';
		$scope.paginationOptions.sortFieldString = false;
		$scope.paginationOptions.sortDir = 'asc';
	};
	$scope.setDefaultParams();

	$scope.getPage = (page) => {
		$scope.getPageGrid($scope.gridPesquisa, page);
	};

	$ctrl.exibir = function(item) {
		window.open($state.href('clientes.view', {objectId: item.codigo}, {absolute: true}));
	};

	$ctrl.editar = function(item) {
		$state.go('clientes.edit', { objectId: item.codigo });
	};

	init();

	/** FIM DA CONFIGURAÇÃO - TABELA DE RESULTADO DA PESQUISA  */
}
