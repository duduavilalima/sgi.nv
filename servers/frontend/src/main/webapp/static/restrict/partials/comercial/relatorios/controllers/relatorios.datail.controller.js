angular
	.module('app.comercialRelatorios')

	.controller('RelatoriosComercialCtrl', listRelatorios);

function listRelatorios($scope, $state, Clientes, $rootScope, $window, EnumService, Unidade,
	UsuarioLogadoService, CentroFinanceiro, Parametro) {

	const $ctrl = this;

	$ctrl.object = {};
	$ctrl.relatorio = {};

	$ctrl.modulo = $state.current.data.modulo;
	$ctrl.titulo = $state.current.data.titulo;

	$ctrl.object.gerencia = {};
	$ctrl.gerenciaList = [];
	$ctrl.object.gerencia = {};
	$ctrl.object.gerencia.codigo = -1;

	const initCampoSituacao = () => {
		$ctrl.listSituacoes = [];
		$ctrl.listSituacoes.push(undefined);
		$ctrl.listSituacoes.push({codigo: 1, nome: 'Ativo'});
		$ctrl.listSituacoes.push({codigo: 0, nome: 'Inativo'});
		$ctrl.listSituacoes.push({codigo: 2, nome: 'Em elaboração'});
		$ctrl.listSituacoes.push({codigo: 4, nome: 'Em análise - Gerência'});
		$ctrl.listSituacoes.push({codigo: 5, nome: 'Em análise - Diretoria'});
		$ctrl.listSituacoes.push({codigo: 16, nome: 'Em análise - Gestão'});
		$ctrl.listSituacoes.push({codigo: 11, nome: 'Encerrado'});
		$ctrl.listSituacoes.push({codigo: 17, nome: 'Encerrado, mas com faturamento'});
		$ctrl.listSituacoes.push({codigo: 13, nome: 'Paralisado'});
		$ctrl.listSituacoes.push({codigo: 12, nome: 'Cancelado'});

	};

	initCampoSituacao();

	$ctrl.listRelatorios = [{
		id: 0,
		nome: "Contratos X Centro de Lucro",
		chave: 'contrato_cl'
	}];

	$ctrl.tiposFormatos = [{
		nome: "Excel",
		codigo: 0
	}, {
		nome: "PDF",
		codigo: 1
	}];

	$ctrl.object.tipoFormato = { codigo: 1 };

	$ctrl.buscarEmpresas = () => {
		// Tive que fazer uma promise pois o valor que estava me retornando não estava certo
		return new Promise((resolve, reject) => {
			let codigosUnidadesIgnoradas = [];
			Unidade.unidadesporempresaexceto(undefined, codigosUnidadesIgnoradas)
				.$promise.then((data) => {
					resolve(data);
					listaEmpresas = data;
				})
				.catch((error) => {
					reject(error);
				});
		});
	};


	$ctrl.buscarEmpresas()
		.then((data) => {

			$scope.conjCodMatriz = new Set();

			$scope.empresaList = [];
			data.forEach((value) => {
				// Criei um array de filiais para poder coloca-lo como filho de uma matriz
				let filiais = data.filter((filial) => {
					return filial.matriz && filial.matriz.codigo === value.codigo;
				});
				//Coloco a matriz também para ser selecionada no option
				if (!value.matriz) {
					filiais.unshift(value);
				}
				//Crio um objeto para fazer a divisão
				let obj = {
					empresa: !value.matriz ? value.codnome : undefined,
					filiais: filiais
				};
				//Se esse objeto tem empresa preenchido eu adiciono na lista
				if (obj.empresa) {
					$scope.empresaList.push(obj);
				}

				// Conjunto com apenas os codigos das Matrizes.
				// Será necessário verificar se a filial escolhida é matriz na hora de criar o relatório.
				if (value.tipoUnidade === 'Matriz') {
					$scope.conjCodMatriz.add(value.codigo);
				}
			});
		})
		.catch((error) => {
			console.log(error);
		});

	$scope.todos = (isSelecionado, unidade) => {

		let conjunto = new Set();

		if ($ctrl.object.unidade && $ctrl.object.unidade.length !== 0) {
			angular.forEach($ctrl.object.unidade, (value) => {
				conjunto.add(value);
			});
		}

		if (isSelecionado) {
			angular.forEach(unidade.filiais, (value) => {
				conjunto.add(value.codigo);
			});
		} else {
			angular.forEach(unidade.filiais, (value) => {
				conjunto.delete(value.codigo);
			});
		}

		$ctrl.object.unidade = [];

		angular.forEach(conjunto, (value) => {
			$ctrl.object.unidade.push(value);
		});
	};

	$ctrl.gerenciaList = CentroFinanceiro.buscarCFsPorPerfilFaturamento();
	$ctrl.gerenciaList.$promise.then((data) => {
		if (data.length === 1)
			$ctrl.object.gerencia = data[0];
	});

	const carregarClassificacaoCliente = () => {
		Clientes.buscarClassificacaoCliente().$promise.then((data) => {
			$ctrl.listClassificacao = [];
			$ctrl.listClassificacao.push({
				codigo: undefined,
				nome: ''
			});
			angular.forEach(data, (value, key) => {
				$ctrl.listClassificacao.push({
					codigo: value.codigo,
					nome: value.nome
				});
			});
		}).catch((response) => {
			$scope.$emit('crud-critical', 'classificacao', response);
		});
	};

	carregarClassificacaoCliente();

	// fim - base relatório


	//FUNÇÃO DE LIMPAR

	$ctrl.reset = (form) => {
		$ctrl.object = {};
		delete $ctrl.object.gerencia;

		$scope.form.$setPristine();
		$scope.form.$setUntouched();
		$scope.form.empresa.$error = {};
	};

	// FUNÇÃO PARA GERAR RELATÓRIO
	$ctrl.gerarRelatorio = () => {

		if ((angular.isUndefined($ctrl.object.relatorios)) || $ctrl.object.relatorios === undefined || $ctrl.object.relatorios === '') {
			$scope.notification.error("", "Relatorio é um campo de preenchimento obrigatório.");
			return;
		}

		let url;

		Parametro.page({
			chave: 'layout.relatorios.temp.url'
		}).$promise.then((parametro) => {

			let caminhoBase;

			if (angular.isDefined($ctrl.object.tipoFormato) && $ctrl.object.tipoFormato != undefined && $ctrl.object.tipoFormato.nome != undefined && $ctrl.object.tipoFormato.codigo === 0) {
				caminhoBase = '/relatorio/frameset?__locale=pt_BR&__emitterid=org.eclipse.birt.report.engine.emitter.prototype.excel&__asattachment=true&__format=xls&__masterpage=false&__showtitle=false&__report=report/sgi/comercial/';
			} else {
				caminhoBase = '/relatorio/frameset?__locale=pt_BR&__format=pdf&__showtitle=false&__report=report/sgi/comercial/';
			}

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

			montarURLRelatorio(url);

		}).catch((response) => {
			console.error('crud-critical', 'dialog', response);
			$scope.action = false;
			$scope.notification.criticalError(response.status, response.statusText, response.data);
		});
	};

	const montarURLRelatorio = (url) => {
		if (angular.isDefined($ctrl.object.relatorios) && $ctrl.object.relatorios != undefined && $ctrl.object.relatorios.nome != undefined && $ctrl.object.relatorios.chave != undefined) {
			url = url + $ctrl.object.relatorios.chave + ".rptdesign&nomRelat=" + $ctrl.object.relatorios.nome;
		}

		if (angular.isDefined($ctrl.object.unidade) && $ctrl.object.unidade != undefined && $ctrl.object.unidade.length > 0) {
			let codigoUnidade = '';
			$ctrl.object.unidade.forEach((value) => {
				codigoUnidade = codigoUnidade + value + ',';
			});
			url = url + "&codEmpresa=" + codigoUnidade.substring(0, codigoUnidade.length - 1);
			//                    url = url + "&nomeUnidade=" + $scope.object.unidade.codnome;
			if ($scope.conjCodMatriz.has($ctrl.object.unidade[0])) {
				url = url + "&isMatriz=true";
			} else {
				url = url + "&isMatriz=false";
			}
		}

		if (angular.isDefined($ctrl.object.gerencia) && $ctrl.object.gerencia != undefined && angular.isDefined($ctrl.object.gerencia.codigo) && $ctrl.object.gerencia.codigo != undefined && angular.isDefined($ctrl.object.gerencia.nome) && $ctrl.object.gerencia.nome != undefined) {
			url = url + "&codGerencia=" + $ctrl.object.gerencia.codigo; // + "&nomGer=" + $scope.object.gerencia.nome;
			url = url + "&nomeGerencia=" + $ctrl.object.gerencia.nome;
		}

		if ($ctrl.object.situacao) {
			url = url + "&situacao=" + $ctrl.object.situacao.codigo;
		}

		if ($ctrl.object.classCliente != undefined && $ctrl.object.classCliente != "" && angular.isDefined($ctrl.object.classCliente)) {
			url = url + "&codCCliente=" + $ctrl.object.classCliente.codigo;
		}

		url = url.replace(" ", "_").replace(',"', '"') + "&tot=1";
		console.log(url);

		$window.open(url);
	};

}
