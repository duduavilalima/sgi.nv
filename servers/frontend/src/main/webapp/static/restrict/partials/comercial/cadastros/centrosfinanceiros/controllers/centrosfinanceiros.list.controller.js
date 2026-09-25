angular
.module('app.centrosfinanceiros').controller('CentrosFinanceirosListarCtrl', listController);

function listController($scope, $state, $rootScope, $controller, CentroFinanceiro, Unidade, Cidade) {
	$controller('GridController', {
		$scope: $scope,
		Resource: CentroFinanceiro
	});

	const $ctrl = this;

	init = () => {
		$scope.object = {};
		$scope.object.codigoCliente = undefined;
		$scope.object.identificadorFinal = undefined;
		$scope.object.nome = undefined;
		$scope.object.codigoContrato = undefined;
		$scope.object.unidade = undefined;
		$scope.object.situacao = undefined;

		$scope.object.identificador = undefined;
		$scope.object.cumulativo = undefined;
		$scope.object.uf = undefined;
		$scope.object.cidade = undefined;
		$scope.object.gerencia = undefined;
		$scope.object.permiteClienteLivre = undefined;

		$ctrl.cumulativo = undefined;

		initCampoGerencia();
		initCampoMatrizFilial();
		initCampoSituacao();
		initCampoRegime();
		initCampoUF();
	};

	/*
	* -------------------------
	*  CAMPOS - INICIO
	* -------------------------
	*/

	initCampoGerencia = () => {
		$ctrl.listaGerencia = [];

		const pCentroFinanceiro = CentroFinanceiro.buscarGerenciasDaPesquisa().$promise;
		pCentroFinanceiro.then(function(data) {
			$ctrl.listaGerencia.push({codigo: undefined, nome: ''});
			$ctrl.listaGerencia.push(...data);
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'load', response);
			$ctrl.listaGerencia = [];
		});
	};

	initCampoMatrizFilial = () => {
		let codigosUnidadesIgnoradas = [8]; // vide SGINV-1405

		Unidade.unidadesporempresaexceto(undefined, codigosUnidadesIgnoradas).$promise.then(function(data) {
			$ctrl.listUnidades = [];
			$ctrl.listUnidades.push({codigo: undefined, codnome: ''});
			$ctrl.listUnidades.push(...data);
		}).catch(function(response) {
			$scope.$emit('crud-critical', 'search', response);
		});
	};

	initCampoSituacao = () => {
		$ctrl.listSituacoes = ['', 'Ativo', 'Inativo', 'Em elaboração', 'Em análise - Gerência', 'Em análise - Diretoria',
				'Em análise - Gestão', 'Encerrado', 'Encerrado, mas com faturamento', 'Paralisado', 'Cancelado'];
	};

	initCampoRegime = () => {
		$ctrl.listRegime = [{valor: undefined, nome: ''}, {valor: true, nome: 'Cumulativo'}, {valor: false, nome: 'Não Cumulativo'}];
	};

	initCampoUF = () => {
		$ctrl.listEstados = ['', 'AC', 'AL', 'AM', 'AP', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MG', 'MS', 'MT', 'PA', 'PB', 'PE',
			 'PI', 'PR', 'RJ', 'RN', 'RO', 'RR', 'RS', 'SC', 'SE', 'SP', 'TO'];
	};

	$ctrl.querySearchCidade = (query) => {
		let nomeCidade = query;
		let listaCidade = [];

		const pCidade = Cidade.buscarListaPorNome(nomeCidade).$promise;

		return pCidade.then(function(data) {
				listaCidade.push(...data);
				return listaCidade;

			}).catch(function(response) {
				$scope.$emit('crud-critical', 'load', response);
				listaCidade = [];
				return listaCidade;
			});
	};

	$ctrl.selectedItemChange = (item) => { $scope.object.cidade = item; };

	// change

	$ctrl.changeGerencia = () => {
		if ($scope.object.gerencia.codigo === undefined) {
			delete $scope.object.gerencia;
		}
	};

	$ctrl.changeUnidade = () => {
		if ($scope.object.unidade.codigo === undefined) {
			delete $scope.object.unidade;
		}
	};

	$ctrl.changeSituacao = () => {
		if ($scope.object.situacao === '') {
			delete $scope.object.situacao;
		}
	};

	$ctrl.changeRegime = () => {
		$scope.object.cumulativo = angular.copy($ctrl.cumulativo.valor);

		if ($scope.object.cumulativo === undefined) {
			delete $scope.object.cumulativo;
			delete $ctrl.cumulativo;
		}
	};

	$ctrl.changeUf = () => {
		if ($scope.object.uf === '') {
			delete $scope.object.uf;
		}
	};

	$ctrl.changePermiteClienteLivre = () => {
		if ($scope.object.permiteClienteLivre === '') {
			delete $scope.object.permiteClienteLivre;
		}
	};


	/*
	* ----------------------
	*  CAMPOS - FIM
	* ----------------------
	*/


	/*
	* --------------------------------
	*  RESULTADO DA PESQUISA - INICIO
	* --------------------------------
	*/

	$scope.$on('crud-success', function(event, action, data) {
		$rootScope.loadingProgress = false;
	});

	$scope.$watch('result', function() {
		$rootScope.loadingProgress = false;
	});

	$scope.getRegime = function(regime) {
		return regime ? 'Cumulativo' : '';
	};

	$scope.getContrato = function(contrato) {
		if (contrato && contrato.codigo) {
			let numeroString = '' + contrato.codigo;
			let tamanhoString = numeroString.length;
			let zerosRestantes = 6 - tamanhoString;
			let zeros = '';

			for (let i = 0; i < zerosRestantes; i++) {
				zeros = zeros + '0';
			}

			return `CT/${zeros}${numeroString}`;
		} else {
			return '';
		}
	};

	let cellTemplateRegime = '<span>{{grid.appScope.getRegime(row.entity.cumulativo)}}</span>';
	let cellTemplateContrato = '<span>{{grid.appScope.getContrato(row.entity.contrato)}}</span>';

	let acoesAdicionais = [
		{ nome : 'editar.objeto', label: 'Editar', icone : 'icon-pencil', tooltip: 'Editar os dados deste centro financeiro' }
	];

	let columnDefs = [
		{
			name: 'Identificador',
			field: 'identificador',
			width: '10%',
			cellClass: 'text-center',
			enableSorting: false,
			sortFieldString: false,
			pinnedLeft: false,
			funcao: ['exibir', 'row.entity.codigo']
		},
		{
			name: 'Nome',
			field: 'nome',
			cellClass: 'text-truncate',
			tooltip: ['field'],
			width: '19%'
		},
		{
			name: 'Gerência',
			field: 'pai.nome',
			width: '30%',
			cellClass: 'text-truncate',
			tooltip: ['field']
		},
		{
			name: 'Contrato',
			field: 'contrato',
			cellTemplate: cellTemplateContrato,
			cellClass: 'text-center',
			width: '9%'
		},
		{
			name: 'Regime',
			field: 'cumulativo',
			cellTemplate: cellTemplateRegime,
			cellClass: 'text-center',
			width: '9%'
		},
		{
			name: 'Situação',
			field: 'situacao',
			cellClass: 'text-left text-truncate',
			width: '16%',
			tooltip: ['field']
		},
		{
			name: 'Opções',
			width: '7%',
			pinnedRight: false,
			enableSorting: false,
			acoes: ['exibir'],
			acoesAdicionais: acoesAdicionais
		}
	];

	$scope.gridPesquisa = $scope.configuracaoGrid(columnDefs, 'centro financeiro', 'e');

	$scope.setDefaultParams = function() {
		$scope.paginationOptions.sortField = 'codigo';
		$scope.paginationOptions.sortFieldString = false;
		$scope.paginationOptions.sortDir = 'asc';
	};

	$scope.setDefaultParams();

	$scope.getPage = function(page) {
		$scope.getPageGrid($scope.gridPesquisa, page);
	};

	$scope.exibir = function(codigo) {
		window.open(`/sgi/nv/app/main.html#!/comercial/cadastros/centrosfinanceiros/${codigo}`, '_blank');
	};

	$scope.editar = function(item) {
		$state.go('centrosfinanceiros.edit', { objectId: item.codigo });
	};


	/*
	* -----------------------------
	*  RESULTADO DA PESQUISA - FIM
	* -----------------------------
	*/

	/*
	* -----------------
	*  BOTÕES - INICIO
	* -----------------
	*/

	$ctrl.buscar = function() {
		$rootScope.loadingProgress = true;
		$scope.setDefaultParams();
		$scope.getPage(1);
	};

	$ctrl.reset = function() {
		$scope.object.cumulativo = undefined;
		$ctrl.cumulativo = undefined;
		$scope.result = undefined;
		$scope.clear();

		document.querySelectorAll('input[type=search]')[0].value = '';
	};

	/*
	* --------------
	*  BOTÕES - FIM
	* --------------
	*/

	init();
}

