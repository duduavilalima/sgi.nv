angular.module('app.contasareceberRelatorios')

    .controller('ContasareceberRelatoriosListarCtrl', ['$scope', '$state', 'EnumService', 'Unidade', 'AutocompleteService', 'Pessoa', 'CentroFinanceiro', 'UsuarioLogadoService', 'ContaCorrente', 'CalculadorFaturamento', 'CategoriaTributo', 'Cidade', '$rootScope', '$timeout', '$window', '$filter', 'Tributo', 'Parametro', 'Gerencia', 'Clientes',
     function($scope, $state, EnumService, Unidade, AutocompleteService, Pessoa, CentroFinanceiro, UsuarioLogadoService, ContaCorrente, CalculadorFaturamento, CategoriaTributo, Cidade, $rootScope, $timeout, $window, $filter, Tributo, Parametro, Gerencia, Clientes) {

        $scope.object = {};
        $scope.relatorio = {};
        $scope.object.contaCorrente = {};
        $scope.object.contaCorrente.banco = {};
        $scope.object.contaCorrente.banco.codigo = -1;
        $scope.object.nomeSituacao = {};

        $scope.object.gerencia = {};
        $scope.object.gerencia.codigo = -1;

        $scope.usuario = UsuarioLogadoService.getUsuarioLogado();
        var perfisRelatoriosContasReceber = ['visualizacao-pagamentos'];
        $scope.possuiPerfilContasReceber = [];
        $scope.modulo = $state.current.data.modulo;
        $scope.titulo = $state.current.data.titulo;
		$scope.titulo_pesquisa = $state.current.data.titulo_pesquisa;

		$scope.nao_permissao_faturamento = false;

        $scope.gerenciaLogada = {};

        $scope.tipoList = [{
            codigo: -1,
            nome: ""
        }, {
            codigo: 0,
            nome: "Contratual"
        }, {
            codigo: 1,
            nome: "Extra contratual"
        }];

        $scope.gerenciaList = [];
        $scope.tselected = [];
        $scope.centroFinanceiroList = [];
        $scope.object.notaSaida = {};
        $scope.agenciaContaList = [];
        $scope.bancoList = [];

        $scope.categoriaInssList = [];
		$scope.categoriaIrrfList = [];
		$scope.categoriaPisList = [];
		$scope.cidadeIssList = [];
		$scope.categoriaIssList = [];

        $scope.situacaoNotaContasReceber = {};
        $scope.tipoRelatorio = {};

        carregarTributos();
        buscarCategoriaTributoInss();
		buscarCategoriaTributoIrrf();
		buscarCategoriaTributoPis();
		carregarClassificacaoCliente();

		// Início AutoComplete Centro de Lucro
		$scope.searchTextCentroLucro = null;
		$scope.selectedItemCentroLucro = null;

		$scope.fnSelectedItemCentroLucro = function(item) {
			$scope.tselected = [];
			$scope.tselected.push(item);
		};

		$scope.fnChangeTextCentroLucro = function() {
			$scope.tselected = [];
		};

		$scope.querySearchCentroLucro = function(query) {
			$scope.listaCentroLucro = [];

			var obj = {};
			obj.identificadorFinal = query;
			obj.nome = query;

			var pCF = CentroFinanceiro.buscarFaturamentoCFs(obj).$promise;

			return pCF.then(function(data) {
				$scope.listaCentroLucro = data;
				return $scope.listaCentroLucro;
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'search', response);
				return $scope.listaCentroLucro;
			});

		}; // Fim AutoComplete Centro de Lucro

		let TipoDataEnum = {
		  VAZIO: 1,
		  EMISSAO: 2,
		  VENCIMENTO: 3,
		  AGENDAMENTO: 4,
		  PAGAMENTO: 5,
		  CONTABILIZACAO: 6,
		  CANCELAMENTO: 7
		};

		function limparCamposData1() {
			 $scope.object.data1 = undefined;
			 $scope.object._1DataDe = undefined;
			 $scope.object._1DataAte = undefined;
			 $scope.object.data1De = '1ª data de';
			 $scope.object.data1Ate = '1ª data até';
			 limparCamposDataDoRelatorio();
		}

		function limparCamposData2() {
			 $scope.object.data2 = undefined;
			 $scope.object._2DataDe = undefined;
			 $scope.object._2DataAte = undefined;
			 $scope.object.data2De = '2ª data de';
			 $scope.object.data2Ate = '2ª data até';
			 limparCamposDataDoRelatorio();
		}

		limparCamposData1();
		limparCamposData2();

		function limparCamposDataDoRelatorio() {
			limparCamposDataEmissao();
			limparCamposDataVencimento();
			limparCamposDataAgendamento();
			limparCamposDataPagamento();
			limparCamposDataContabilizacao();
			limparCamposDataCancelamento();
		}

		function limparCamposDataEmissao() {
		    $scope.object.emissaoDe = undefined;
        	$scope.object.emissaoAte = undefined;
		}

		function limparCamposDataVencimento() {
			$scope.object.dataVencimentoDe = undefined;
			$scope.object.dataVencimentoAte = undefined;
		}

		function limparCamposDataAgendamento() {
			$scope.object.agendamentoDe = undefined;
			$scope.object.agendamentoAte = undefined;
		}

		function limparCamposDataPagamento() {
			$scope.object.periodoDe = undefined;
			$scope.object.periodoAte = undefined;
		}

		function limparCamposDataContabilizacao() {
			$scope.object.contabilizacaoDe = undefined;
			$scope.object.contabilizacaoAte = undefined;
		}

		function limparCamposDataCancelamento() {
			$scope.object.cancelamentoDe = undefined;
			$scope.object.cancelamentoAte = undefined;
		}

		function carregarTributos() {

			 var filtroTributo = {};

			filtroTributo.exibeNota = true;

			Tributo.page(null, filtroTributo)
			.$promise.then(function(data){

				$scope.listTributos = [];

				angular.forEach(data.content, function(value, key) {
					$scope.listTributos.push(value);
				});

				let ultimo = $scope.listTributos.pop();
				let penultimo = $scope.listTributos.pop();
				$scope.listTributos.push(ultimo);
				$scope.listTributos.push(penultimo);
				$scope.listTributosExcluidos = angular.copy($scope.listTributos);

				}).catch(function(response) {
					$scope.$emit('crud-critical', 'contrato load', response);
					$scope.tributosList = [];
					$scope.listTributosExcluidos = [];
			});
		}

        EnumService.enumValues({
                pacote: 'br.com.araujoabreu.sgi.faturamento.domain',
                classe: 'Faturamento',
                enum: 'Status'
            })
            .$promise.then(function(data) {
                $scope.enumStatus = [];
                $scope.enumStatus.push({
                    codigo: null,
                    nome: ''
                });
                angular.forEach(data, function(value, key) {
                    $scope.enumStatus.push({
                        codigo: key,
                        nome: value
                    });
                });
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'load', response);
            });

        $scope.situacoesNotaContasReceber = [{
            codigo: -3,
            nome: ""
        }, {
            codigo: 15,
            nome: "Em recebimento"
        }, {
            codigo: 16,
            nome: "Recebida"
        }, {
            codigo: 17,
            nome: "Cancelada"
        }];

        let SizeEnum = {
          FILIAL: 1,
          GERENCIA: 2,
          CENTRO_DE_LUCRO: 3,
        };

		$scope.object.relatorioOrdem = SizeEnum.FILIAL;

		const separador = "--------------------------------------------------------------------";

        $scope.tiposRelatorios = [{
            nome: "Duplicatas em aberto",
            chave: "duplicatas"
        }, {
            nome: "Duplicatas recebidas",
            chave: "duplicatas_recebidas"
        }, {
            nome: "Duplicatas baixadas",
            chave: "duplicatas_baixadas"
        }, {
            nome: "Duplicatas baixadas (com observações)",
            chave: "duplicatas_baixadas_com_obs"
        }, {
			nome: separador,
			chave: "-"
		}, {
            nome: "Duplicatas agendadas por período",
            chave: "duplicatas_agendadas_periodo"
        }, {
			nome: "Notas fiscais emitidas por CNPJ",
			chave: "nf_emitidas_por_cnpj"
		}, {
			nome: "Notas fiscais emitidas por CNPJ (N630)",
			chave: "nf_emitidas_por_cnpj(n630)"
		}, {
			nome: separador,
			chave: "-"
		}, {
            nome: "Notas fiscais canceladas",
            chave: "nfs_canceladas"
        }, {
            nome: "Notas fiscais canceladas (com pedido)",
            chave: "nfs_canceladas_com_pedido"
        }, {
			nome: separador,
			chave: "-"
		}, {
            nome: "Relação de duplicatas e valores retidos",
            chave: "relacao_duplicatas_valores_retidos"
		}, {
			nome: separador,
			chave: "-"
		}, {
			nome: "Fluxo de caixa",
			chave: "fluxo_de_caixa"
		}, {
            nome: "Duplicatas em aberto (auditoria externa)",
            chave: "duplicatas_auditoria_externa"
        }, {
        	nome: separador,
            chave: "-"
        }, {
            nome: "ND em aberto",
            chave: "notas_debito_aberto"
        }, {
             nome: "ND recebidas",
             chave: "notas_debito_recebidas"
        }, {
             nome: "ND baixadas",
             chave: "notas_debito_baixadas"
        }];

        $scope.tiposFormatos = [{
			nome: "Excel",
			codigo: 0
		}, {
			nome: "PDF",
			codigo: 1
		}];

		$scope.object.tipoFormato = {codigo: 1};

		 $scope.situacoes = {};
		 $scope.situacoes.nenhuma = {
			 codigo: -3,
			 nome: ""
		 };
		 $scope.situacoes.recebida = {
			 codigo: 16,
			 nome: "Recebida"
		 };
		 $scope.situacoes.cancelada = {
			 codigo: 17,
			 nome: "Cancelada"
		 };
		 $scope.situacoes.emRecebimento = {
			 codigo: 15,
			 nome: "Em recebimento"
		 };
		 $scope.situacoes.todos = [
			 $scope.situacoes.nenhuma, $scope.situacoes.emRecebimento,
			 {
				 codigo: -1,
				 nome: "... A vencer"
			 }, {
				 codigo: -2,
				 nome: "... Vencida"
			 },
			 $scope.situacoes.recebida, $scope.situacoes.cancelada
		 ];
		 $scope.object.situacao = $scope.situacoes.nenhuma;

		 $scope.desabilitarCampo = function (nomeCampo) {

			 if (nomeCampo === 'banco') {
				 if ($scope.object.tipoRelatorio != null && $scope.object.tipoRelatorio.chave === 'nfs_canceladas') {
					 $scope.object.contaCorrente = null;
				 }
				 return $scope.bancoList === null || $scope.bancoList.length === 0 || ($scope.object.tipoRelatorio != null && $scope.object.tipoRelatorio.chave == 'nfs_canceladas');

			 } else if (nomeCampo === 'contaCorrente') {
				 if ($scope.object.tipoRelatorio != null && $scope.object.tipoRelatorio.chave === 'nfs_canceladas') {
					 $scope.agenciaContaList = null;
				 }
				 return $scope.bancoList === null || $scope.bancoList.length === 0 || ($scope.object.tipoRelatorio != null && $scope.object.tipoRelatorio.chave == 'nfs_canceladas');

			 } else if (nomeCampo === 'dataPagamento') {
				 if ($scope.object.tipoRelatorio != null && $scope.object.tipoRelatorio.chave === 'nfs_canceladas') {
					 delete $scope.object.periodoDe;
					 delete $scope.object.periodoAte;
					 delete $scope.object.dataVencimentoDe;
					 delete $scope.object.dataVencimentoAte;
				 }

				 return $scope.object.tipoRelatorio != null && $scope.object.tipoRelatorio.chave === 'nfs_canceladas';
			 } else if (nomeCampo == 'dataVencimento') {
				 return !($scope.object.tipoRelatorio != null && $scope.object.tipoRelatorio.chave === 'nfs_canceladas');
			 } else if (nomeCampo == 'dataCancelamento') {
				 return $scope.object.tipoRelatorio != null && $scope.object.tipoRelatorio.chave === 'nfs_canceladas';
			 } else if (nomeCampo == 'situacao') {
				 return !$scope.object.tipoRelatorio
				           || !($scope.object.tipoRelatorio.chave === 'nf_emitidas_por_cnpj'
						   || $scope.object.tipoRelatorio.chave === 'duplicatas'
						   || $scope.object.tipoRelatorio.chave === 'duplicatas_auditoria_externa'
				           || $scope.object.tipoRelatorio.chave === 'duplicatas_aberto_filial'
				            || $scope.object.tipoRelatorio.chave === 'notas_debito_aberto' );
			 }

			 return false;
		 }

		let labelCamposDatas = (label, posicaoCampoData) => {
		    if (label) {
		        $scope.object[`data${posicaoCampoData}De`] = `${label} de`;
		        $scope.object[`data${posicaoCampoData}Ate`] = `${label} até`;
		    } else {
		        if (posicaoCampoData === 1) {
		            limparCamposData1();
		        } else {
		            limparCamposData2();
		        }
		    }
		};

		 let acaoDataMap = new Map();
		 acaoDataMap.set(TipoDataEnum.VAZIO, {label: null, fn: labelCamposDatas});
		 acaoDataMap.set(TipoDataEnum.EMISSAO, {label: 'Emissão', fn: labelCamposDatas});
		 acaoDataMap.set(TipoDataEnum.VENCIMENTO, {label: 'Vencimento', fn: labelCamposDatas});
		 acaoDataMap.set(TipoDataEnum.AGENDAMENTO, {label: 'Agendamento', fn: labelCamposDatas});
		 acaoDataMap.set(TipoDataEnum.PAGAMENTO, {label: 'Pagamento', fn: labelCamposDatas});
		 acaoDataMap.set(TipoDataEnum.CONTABILIZACAO, {label: 'Contabilização', fn: labelCamposDatas});
		 acaoDataMap.set(TipoDataEnum.CANCELAMENTO, {label: 'Cancelamento', fn: labelCamposDatas});

		 $scope.$on('changeData1', function() {
		     if ($scope.object.data1) {
		         acaoDataMap.get($scope.object.data1).fn(acaoDataMap.get($scope.object.data1).label, 1);
		     }
		     $scope.$applyAsync();
		 });

		 $scope.$on('changeData2', function() {
			 if ($scope.object.data2) {
				 acaoDataMap.get($scope.object.data2).fn(acaoDataMap.get($scope.object.data2).label, 2);
			 }
			 $scope.$applyAsync();
		 });

		 $scope.$on('changeRelatorio', function () {
		 	 limparCamposData1();
		     limparCamposData2();
		     $scope.object.relatorioOrdem = SizeEnum.FILIAL;
		     $scope.object.faturamentoCobranca = undefined;
		     $scope.object.cobrancaCronica = undefined;
		     $scope.object.classificacaoCliente = undefined;

			 if ($scope.object.tipoRelatorio === null) {
				 $scope.situacaoList = [];
				 $scope.object.situacao = $scope.situacoes.nenhuma;
			 } else if ($scope.object.tipoRelatorio.chave === 'nfs_canceladas' ||
			 	 $scope.object.tipoRelatorio.chave === 'nfs_canceladas_com_pedido') {
				 $scope.situacaoList = [$scope.situacoes.cancelada];
				 $scope.object.situacao = $scope.situacaoList[0];
				 $scope.object.data1 = TipoDataEnum.CANCELAMENTO;
				 $scope.object.data1De = 'Cancelamento de';
                 $scope.object.data1Ate = 'Cancelamento até';
			 } else if ($scope.object.tipoRelatorio.chave === 'nf_emitidas_por_cnpj') {
				 $scope.situacaoList = $scope.situacoes.todos;
				 $scope.object.situacao = $scope.situacoes.nenhuma;
			 } else if ($scope.object.tipoRelatorio.chave === 'nf_emitidas_por_cnpj(n630)') {
				 $scope.situacaoList = $scope.situacoes.todos;
				 $scope.object.situacao = $scope.situacoes.nenhuma;
			 } else if ($scope.object.tipoRelatorio.chave === 'duplicatas' ||
			 	 $scope.object.tipoRelatorio.chave === 'duplicatas_aberto_filial' ||
			 	 $scope.object.tipoRelatorio.chave === 'duplicatas_auditoria_externa' ||
			 	 $scope.object.tipoRelatorio.chave === 'notas_debito_aberto') {
				 $scope.situacaoList = $scope.situacoes.todos;
				 $scope.object.situacao = $scope.situacoes.nenhuma;
			 } else if ($scope.object.tipoRelatorio.chave === 'fluxo_de_caixa') {
				 $scope.situacaoList = [$scope.situacoes.emRecebimento];
				 $scope.object.situacao = $scope.situacaoList[0];
				 $scope.object.data1 = TipoDataEnum.AGENDAMENTO;
				 $scope.object.data1De = 'Agendamento de';
            	 $scope.object.data1Ate = 'Agendamento até';
             } else {
				 $scope.situacaoList = [$scope.situacoes.recebida];
				 $scope.object.situacao = $scope.situacaoList[0];
			 }

			 /*
			    Guardando o último relatório, pois quando sair de um relatório para
			    outro e esse relátório anterior foi de "nfs_canceladas", apagar
			    as datas da tela.
			 */

			 /* conforme issue SGINV-2692 foi identificado que o código das linhas 443 à 447
			  interfere nas regras informadas no ítem 1.1.3
			  */

//			 if ((($scope.tipoRelatorio_old === 'nfs_canceladas') && $scope.object.tipoRelatorio.chave !== 'fluxo_de_caixa')
//			 		|| ($scope.tipoRelatorio_old === 'fluxo_de_caixa' && $scope.object.tipoRelatorio.chave !== 'nfs_canceladas')) {
//			     limparCamposData1();
//                 limparCamposData2();
//			 }

			 $scope.tipoRelatorio_old = $scope.object.tipoRelatorio.chave;
		 });

		$scope.$on('changeSituacao', function() {
		});

		$scope.$on('changeDataDe1', function() {
			if ($scope.object && $scope.object.tipoRelatorio && $scope.object.tipoRelatorio.chave) {
				if ($scope.object.tipoRelatorio.chave === 'fluxo_de_caixa') {
					if ($scope.object._1DataDe) {
						let dataMoment = moment($scope.object._1DataDe);
						let diaDaSemana = dataMoment.day();

						while (diaDaSemana !== 1) {
							let diaDoMes = dataMoment.date();
							dataMoment.date(diaDoMes + 1);
							diaDaSemana = dataMoment.day();
						}

						$scope.object._1DataDe = dataMoment.toDate();
						calcularDataAgendamentoAte();
					} else {
						delete $scope.object._1DataAte;
					}
				}
			}
		});

		const calcularDataAgendamentoAte = () => {
			let dataMoment = moment($scope.object._1DataDe);
			let diaDoMes = dataMoment.date();

			dataMoment.date(diaDoMes + 32);
			let diaDaSemana = dataMoment.day();

			while (diaDaSemana !== 5) {
				diaDoMes = dataMoment.date();
				dataMoment.date(diaDoMes + 1);
				diaDaSemana = dataMoment.day();
			}

			$scope.object._1DataAte = dataMoment.toDate();
		}

		var listaEmpresas = [];
        //ordenadas como no Faturamento, ordenados por empresa e filiais
        $scope.buscarEmpresas = function() {
			// Tive que fazer uma promise pois o valor que estava me retornando não estava certo
        	return new Promise(function(resolve, reject){
				var codigosUnidadesIgnoradas = [];
				Unidade.unidadesfaturamento(null, codigosUnidadesIgnoradas)
					.$promise.then(function(data) {
						resolve(data);
						listaEmpresas = data;
					})
					.catch(function(error) {
						reject(error);
					});
			 });
		}

        $scope.buscarEmpresas()
        	.then(function(data) {
        		$scope.empresaList = [];
        		data.forEach(function(value) {
        			// Criei um array de filiais para poder coloca-lo como filho de uma matriz
        			var filiais = data.filter(function(filial) {
     					return filial.matriz && filial.matriz.codigo === value.codigo;
					});
					//Coloco a matriz também para ser selecionada no option
					if(!value.matriz) {
						filiais.unshift(value);
					}
					//Crio um objeto para fazer a divisão
					var obj = {
						empresa: !value.matriz ? value.codnome : undefined,
						filiais: filiais
					};
					//Se esse objeto tem empresa preenchido eu adiciono na lista
					if(obj.empresa) {
        				$scope.empresaList.push(obj);
					}
        		});
        	})
        	.catch(function(error) {
        		console.log(error);
        	});

		$scope.object.unidade = {codigo: []}

		$scope.todos = function(isSelecionado, unidade) {
			if(isSelecionado) {
			    let codigos = [];
				unidade.filiais.forEach(function(filial) {
					codigos.push(filial.codigo);
				});
				$scope.object.unidade.codigo = codigos;
				changeEmpresa();
			} else {
				$scope.object.unidade.codigo = [];
				changeEmpresa();
			}
		}

        //obter gerencia cuja usuario está logado
        UsuarioLogadoService.possuiUmPerfilLista(perfisRelatoriosContasReceber)
            .$promise.then(function(data) {
                $scope.possuiPerfilContasReceber = data.possuiUmPerfilLista;
            }).catch(function(response) {
                $rootScope.loadingProgress = false;
            });

        $scope.getGerenciaLogada = function() {
            CentroFinanceiro.getGerenciaLogada()
                .$promise.then(function(data) {
                    $scope.gerenciaLogada = data;
                    $scope.buscarTodasGerencias();
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'search', response);
                });
        }


        //obter todas gerencias do sistema
        $scope.buscarTodasGerencias = function() {
			let pGerencia = Gerencia.buscarTodasGerenciasHabFat().$promise;
			pGerencia.then(function(data) {
				UsuarioLogadoService.apenasFaturamento().$promise.then(response => {
					$scope.gerenciaList = data;
					if (!response.value) {
						$scope.object.gerencia = $scope.gerenciaList[0];
						if (data.length === 1) {
							$scope.nao_permissao_faturamento = true;
						}
					}
				});
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'contrato load', response);
				$scope.gerenciaList = [];
			});
        };

        $scope.getGerenciaLogada();

        function changeEmpresa() {
			if ($scope.object.unidade && $scope.object.unidade.codigo && $scope.object.unidade.codigo[0] == -1) {
				$timeout(function() {
					delete $scope.object.unidade.codigo;
				}, 200);
			} else  if ($scope.object.unidade && $scope.object.unidade.codigo && $scope.object.unidade.codigo.length > 0) {
				$scope.carregarBancos(
					angular.isUndefined($scope.object.unidade.codigo[0].matriz) ||
					$scope.object.unidade.codigo[0].matriz == null ? $scope.object.unidade.codigo[0].codigo : $scope.object.unidade.codigo[0].matriz.codigo);
			} else {
				delete $scope.object.contaCorrente;
				$scope.agenciaContaList = [];
				$scope.bancoList = [];
			}
			$scope.$applyAsync();
        };

        $scope.$on('changeEmpresa', changeEmpresa);

		$scope.contemTributo = function(nome) {
		    let tem = false;
		    if ($scope.object.tributoLista != null && $scope.object.tributoLista != undefined) {
				$scope.object.tributoLista.forEach(function(value) {
					if (value.nome === nome) {
						tem = true;
						return;
					}
				});
		    }

		    return tem;
		};

        $scope.$on('changeGerencia', function() {
            $scope.$applyAsync();
        });

        $scope.buscarEmpresas();

        $scope.listar = function() {
            $state.go('interfaces.listar');
        };

        $scope.reset = function(form) {
            $scope.object = {};
            $scope.object.tipoFormato = {codigo: 1};
            $scope.gerencia = [];
            $scope.bancoList = [];
            $scope.object.contaCorrente = {};
            $scope.object.contaCorrente.banco = {};
            $scope.object.contaCorrente.banco.codigo = -1;
            $scope.buscarTodasGerencias();
			$scope.object.gerencia = {};
			$scope.object.gerencia.codigo = -1;


            $scope.centroFinanceiroList = [];
            $scope.searchTextCliente = null;
            $scope.selectedItemCliente = null;
            $scope.resultsClient = [];

            form.$setPristine();
            form.$setUntouched();
            form.relatorio.$error = {};

            let elemento = document.getElementById('autocomplete_cidade_iss');
			if (elemento) {

				let autoChild = elemento.firstElementChild;
				let el = angular.element(autoChild);
				el.scope().$mdAutocompleteCtrl.clear();
				el.scope().$mdAutocompleteCtrl.loading = false;

				delete $scope.selectedCidadeIss;
				delete cidadeIssSelecionada;
			}

            $scope.tselected = [];
			$scope.centroFinanceiroList = [];
			document.querySelectorAll('input[type=search]')[0].value = '';
        };

        $scope.$on('changeTipoLoteInterface', function() {
            $scope.renderizarLblDataCancelamento();
        });

        /*$scope.buscarEmpresas = function() {
            $rootScope.loadingProgress = true;
            var filter = {
                tipoUnidade: ''
            };
            Unidade.page(null, filter)
                .$promise.then(function(data) {
                    $scope.empresaList = [];
                    angular.forEach(data.content, function(value, key) {
                    	console.log(value);
                        $scope.empresaList.push(value);
                    });
                    $rootScope.loadingProgress = false;
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'search', response);
                    $rootScope.loadingProgress = false;
                });
        };*/

        $scope.carregarBancos = function(codigoPessoa) {
            // CARREGAR BANCOS
            $rootScope.loadingProgress = true;
            ContaCorrente.page(null, {
                    pessoa: {
                        codigo: codigoPessoa
                    }
                })
                .$promise.then(function(data) {
                    $scope.bancoList = [];
                    $scope.agenciaContaList = [];
                    $scope.object.contaCorrente = {};
                    if (data.content.length > 0) {
                        angular.forEach(data.content, function(value, key) {
                            $scope.bancoList.push(value);
                        });
                    }
                    $rootScope.loadingProgress = false;
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'contrato load', response);
                    $scope.bancoList = [];
                    $scope.agenciaContaList = [];
                    $scope.object.contaCorrente = {};
                    $rootScope.loadingProgress = false;
                });
        };


        $scope.carregarContasCorrentes = function(codigoBanco, codigoPessoa) {
            // CARREGAR CONTAS CORRENTES
            $rootScope.loadingProgress = true;
            ContaCorrente.page(null, {
                    banco: {
                        codigo: codigoBanco
                    },
                    pessoa: {
                        codigo: codigoPessoa
                    }
                })
                .$promise.then(function(data) {
                    $scope.agenciaContaList = data.content;
                    $rootScope.loadingProgress = false;
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'contrato load', response);
                    $scope.agenciaContaList = [];
                    $rootScope.loadingProgress = false;
                });
        };

        $scope.changeBanco = function() {
            if ($scope.object.contaCorrente.banco === null) {
                $scope.agenciaContaList = [];
            } else {
                $scope.carregarContasCorrentes($scope.object.contaCorrente.banco.codigo, $scope.object.unidade.codigo[0]);
            }
        };

        $scope.montarLabelAgenciaConta = function(contaCorrente) {
            return CalculadorFaturamento.montarCampoAgenciaConta(contaCorrente);
        };

        /**
         * Pesquisa de Clientes
         */
        $scope.searchTextCliente = null;
        $scope.selectedItemCliente = null;
        $scope.resultsClient = [];

        $scope.getClientes = function(query) {
            $rootScope.loadingProgress = true;
            var resultCliente = AutocompleteService.getPessoas({
                nome: query
            }).$promise;

            var clientesArray = [];
            return resultCliente.then(function(clientes) {
                $rootScope.loadingProgress = false;
                angular.forEach(clientes, function(cliente) {
                    clientesArray.push(cliente);
                })
                return clientesArray;
            });
        };

        $scope.querySearchCliente = function(query) {
            $rootScope.loadingProgress = true;
            var obj = {};
            obj.nomeFantasiaDocumento = query;
            obj.tipoPessoa = 'Pessoa Jurídica';
            obj.situacao = 'Ativo';
            obj.ordenacao = 'documento';
            obj.clientePrincipalTO = {};
            obj.clientePrincipalTO.codigo = -1;

            var clientes = [];
            var resultado = Pessoa.page(null, obj).$promise;

            return resultado.then(function(data) {
                $rootScope.loadingProgress = false;
                angular.forEach(data.content, function(cliente) {
                    clientes.push(cliente);
                })
                return clientes;
            }).catch(function(response) {
                $scope.$emit('crud-critical', 'search', response);
                clientes = [];
                return clientes;
            });
        };

        $scope.selectedItemChange = function(item, tipo) {
            if (tipo == 'cliente') {
                if (angular.isDefined(item)) {
                    $scope.object.cliente = {};
                    $scope.object.cliente.codigo = item.codigo;
                    $scope.object.cliente.nome = item.nome;
                    $scope.centros = [];
                    $scope.buscarCentros(item);
                } else {
                    $scope.resultsClient = [];
                    delete $scope.object.centroFinanceiro;
                    delete $scope.selectedItemCliente;
                    delete $scope.object.cliente;
                    $scope.resultsSubClient = [];
                    $scope.centros = [];
                    delete $scope.object.subcliente;
                }
            } else if (tipo == 'subcliente') {
                if (angular.isDefined(item)) {
                    $scope.object.subcliente = {};
                    $scope.object.subcliente.codigo = item.codigo;
                    $scope.object.subcliente.nome = item.nome;
                } else {
                    $scope.resultsSubClient = [];
                    delete $scope.selectedItemSubCliente;
                    delete $scope.object.subcliente;
                }
            }
        };

        //FIXME: Verificar se o objeto '$scope.object.centroFinanceiro' está sendo usado para gerar o relatório.
        //FIXME: Usar o 'CentroFinanceiro.page' apenas na tela de pesquisa.
        $scope.buscarCentros = function(cliente) {
            if (angular.isDefined(cliente) && cliente != null) {
                var obj = {};
                obj.codigoCliente = cliente.codigo;
                CentroFinanceiro.page(null, obj).$promise.then(function(data) {
                        $scope.centros = data.content;

                        if ($scope.centros.length == 1) {
                            $scope.object.centroFinanceiro = $scope.centros[0];
                            $scope.mudarCentroLucro();
                        }

                    }).catch(function(response) {
                        $scope.$emit('crud-critical', 'search', response);
                    });
            } else {
                $scope.centros = [];
            }
        };

        $scope.mudarCentroLucro = function() {
            if (angular.isDefined($scope.object.centroFinanceiro)) {
                $rootScope.loadingProgress = true;

                angular.forEach($scope.centros, function(value, key) {
                    if (value.codigo == $scope.object.centroFinanceiro.codigo) {
                        $scope.object.centroFinanceiro = angular.copy(value);
                        return;
                    }
                });
                $rootScope.loadingProgress = false;
            }
        };
        $scope.formsIncomplete = function() {
            if (angular.isDefined($scope.form)) {
                if ($scope.form.$invalid === true) {
                    return true;
                }
            }
            return false;
        };

        $scope.validaValorDeAte = function() {
            if (angular.isDefined($scope.object.valorDe) && $scope.object.valorDe != null && angular.isDefined($scope.object.valorAte) && $scope.object.valorAte != null) {
                $scope.object.valorDe = Number($scope.object.valorDe);
                $scope.object.valorAte = Number($scope.object.valorAte);

                if ($scope.object.valorDe > $scope.object.valorAte) {
                    return true;
                } else
                if ($scope.object.valorAte < $scope.object.valorDe) {
                    return true;
                }
                return false;
            }
        };

        let carregarCamposDatas = (tipoCampo) => {
            switch ($scope.object[`data${tipoCampo}`]) {
              case TipoDataEnum.VAZIO:
                break;
              case TipoDataEnum.EMISSAO:
                $scope.object.emissaoDe = $scope.object[`_${tipoCampo}DataDe`];
			    $scope.object.emissaoAte = $scope.object[`_${tipoCampo}DataAte`];
                break;
              case TipoDataEnum.VENCIMENTO:
                $scope.object.dataVencimentoDe = $scope.object[`_${tipoCampo}DataDe`];
				$scope.object.dataVencimentoAte = $scope.object[`_${tipoCampo}DataAte`];;
                break;
              case TipoDataEnum.AGENDAMENTO:
                $scope.object.agendamentoDe = $scope.object[`_${tipoCampo}DataDe`];
              	$scope.object.agendamentoAte = $scope.object[`_${tipoCampo}DataAte`];
                break;
              case TipoDataEnum.PAGAMENTO:
                $scope.object.periodoDe = $scope.object[`_${tipoCampo}DataDe`];
				$scope.object.periodoAte = $scope.object[`_${tipoCampo}DataAte`];
                break;
              case TipoDataEnum.CONTABILIZACAO:
                $scope.object.contabilizacaoDe = $scope.object[`_${tipoCampo}DataDe`];
				$scope.object.contabilizacaoAte = $scope.object[`_${tipoCampo}DataAte`];
                break;
			  case TipoDataEnum.CANCELAMENTO:
			    $scope.object.cancelamentoDe = $scope.object[`_${tipoCampo}DataDe`];
				$scope.object.cancelamentoAte = $scope.object[`_${tipoCampo}DataAte`];
				break;
              default:
            }
        };

        // CARREGAR CLASSIFICACAO DE CLIENTES

		function carregarClassificacaoCliente() {
			Clientes.buscarClassificacaoCliente().$promise.then(function(data) {
				$scope.listClassificacao = angular.copy(data);
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'classificacao', response);
			});
		}

        // FUNÇÃO PARA GERAR RELATÓRIO
        $scope.gerarRelatorio = function() {

            if ($scope.object.tipoRelatorio.chave === "nfs_canceladas") {
                if ((angular.isUndefined($scope.object._1DataDe) || $scope.object._1DataDe == null || $scope.object._1DataDe == '') ||
                    (angular.isUndefined($scope.object._1DataAte) || $scope.object._1DataAte == null || $scope.object._1DataAte == '')) {
                     $scope.notification.error("", 'É obrigatório informar o período de data de cancelamento.');
                     return;
                }
            }

            if ((angular.isUndefined($scope.object._1DataDe) || $scope.object._1DataDe == null || $scope.object._1DataDe == '') ||
                (angular.isUndefined($scope.object._1DataAte) || $scope.object._1DataAte == null || $scope.object._1DataAte == '')) {

                if ((angular.isUndefined($scope.object._2DataDe) || $scope.object._2DataDe == null || $scope.object._2DataDe == '') ||
					(angular.isUndefined($scope.object._2DataAte) || $scope.object._2DataAte == null || $scope.object._2DataAte == '')) {
					$scope.notification.error("", "É obrigatório informar, pelo menos, um período de datas (emissão, vencimento ou pagamento).");
					return;
				}
            }

            if ($scope.object.tipoRelatorio) {
            	if ($scope.object.tipoRelatorio.chave === "relacao_duplicatas_valores_retidos") {
            		if ($scope.object.tributoLista && $scope.object.tributoLista.length > 0) {
            			if (!$scope.object.categoriaInss && !$scope.object.categoriaIrff && !$scope.object.categoriaPis && !$scope.object.categoriaIss) {
							$scope.notification.error("", "É obrigatório informar a categoria dos tributos.");
							return;
						}

						let listaCodigoTributo = buscarCodigoTributo();

						if (listaCodigoTributo && listaCodigoTributo.length > 0) {
							$scope.notification.error("", "É obrigatório informar a categoria dos tributos.");
							return;
						}
            		}
            	}

            	if($scope.object.tipoRelatorio.chave === "nf_emitidas_por_cnpj"){
            		if((angular.isUndefined($scope.object.tributoLista) && $scope.object.tributoLista == null) &&
            		  (angular.isUndefined($scope.object.tributoExcluidosLista) && $scope.object.tributoExcluidosLista == null)){
            			$scope.notification.error("", 'Para este relatório é necessário selecionar um tributo a incluir (e a categoria) ' +
                        'OU um tributo a excluir (com alíquota zero).');
                        return;
            		}
            	}
            }

			var url;

			Parametro.page({
				chave: 'layout.relatorios.temp.url'
			}).$promise.then(function(parametro) {
				var caminhoBase;

				if(angular.isDefined($scope.object.tipoFormato) && $scope.object.tipoFormato != null && $scope.object.tipoFormato.nome != null && $scope.object.tipoFormato.codigo == 0){
					caminhoBase = '/relatorio/frameset?__locale=pt_BR&__emitterid=org.eclipse.birt.report.engine.emitter.prototype.excel&__asattachment=true&__format=xls&__masterpage=false&__showtitle=false&__report=report/sgi/contasareceber/';
				}else{
					caminhoBase = '/relatorio/frameset?__locale=pt_BR&__format=pdf&__showtitle=false&__report=report/sgi/contasareceber/';
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

				if ($scope.object.tipoRelatorio.chave === 'fluxo_de_caixa') {
					Parametro.page({chave: 'faturamento.interface.oitelemar.cnpj'}).$promise.then(function(parametro2) {

						$scope.listaCnpjPortalOi = parametro2.valor;
						montarURLRelatorio(url);

					}).catch(function(response) {
						console.error('crud-critical', 'dialog', response);
						$scope.notification.criticalError(response.status, response.statusText, response.data);
					});

				} else {
					montarURLRelatorio(url);
				}

			}).catch(function(response) {
				console.error('crud-critical', 'dialog', response);
				$scope.action = false;
				$scope.notification.criticalError(response.status, response.statusText, response.data);
			});

		};

		function montarUrlDatas(url, tipoData) {
			if (TipoDataEnum.EMISSAO === $scope.object[`data${tipoData}`]) {
				url = url + "&dtEmiDe=" + $filter('date')($scope.object[`_${tipoData}DataDe`], 'yyyy-MM-dd');
				url = url + "&dtEmiAte=" + $filter('date')($scope.object[`_${tipoData}DataAte`], 'yyyy-MM-dd');
			} else if (TipoDataEnum.VENCIMENTO === $scope.object[`data${tipoData}`]) {
				url = url + "&dtVctoDe=" + $filter('date')($scope.object[`_${tipoData}DataDe`], 'yyyy-MM-dd');
				url = url + "&dtVctoAte=" + $filter('date')($scope.object[`_${tipoData}DataAte`], 'yyyy-MM-dd');
			} else if (TipoDataEnum.AGENDAMENTO === $scope.object[`data${tipoData}`]) {
				url = url + "&dtAgendDe=" + $filter('date')($scope.object[`_${tipoData}DataDe`], 'yyyy-MM-dd');
				url = url + "&dtAgendAte=" + $filter('date')($scope.object[`_${tipoData}DataAte`], 'yyyy-MM-dd');
			} else if (TipoDataEnum.PAGAMENTO === $scope.object[`data${tipoData}`]) {
				url = url + "&dtPagDe=" + $filter('date')($scope.object[`_${tipoData}DataDe`], 'yyyy-MM-dd');
				url = url + "&dtPagAte=" + $filter('date')($scope.object[`_${tipoData}DataAte`], 'yyyy-MM-dd');
			} else if (TipoDataEnum.CONTABILIZACAO === $scope.object[`data${tipoData}`]) {
				url = url + "&dtContabDe=" + $filter('date')($scope.object[`_${tipoData}DataDe`], 'yyyy-MM-dd');
				url = url + "&dtContabAte=" + $filter('date')($scope.object[`_${tipoData}DataAte`], 'yyyy-MM-dd');
			} else if (TipoDataEnum.CANCELAMENTO === $scope.object[`data${tipoData}`]) {
				url = url + "&dtCancDe=" + $filter('date')($scope.object[`_${tipoData}DataDe`], 'yyyy-MM-dd');
				url = url + "&dtCancAte=" + $filter('date')($scope.object[`_${tipoData}DataAte`], 'yyyy-MM-dd');
			}
			return url;
		}

		function montarURLRelatorio(url) {
            if (angular.isDefined($scope.object.tipoRelatorio) && $scope.object.tipoRelatorio != null && $scope.object.tipoRelatorio.nome != null && angular.isDefined($scope.object.tipoRelatorio) && $scope.object.tipoRelatorio.chave != null) {
				let nomeRelatorio = $scope.object.tipoRelatorio.chave;

				if ($scope.object.tipoRelatorio.chave === 'duplicatas') {
					nomeRelatorio = 'duplicatas_aberto_' + buscarNomeOrdem();
				}

				if ($scope.object.tipoRelatorio.chave === 'duplicatas_recebidas') {
					nomeRelatorio = nomeRelatorio + '_' + buscarNomeOrdem();
				}

				if ($scope.object.tipoRelatorio.chave === 'notas_debito_aberto') {
                	nomeRelatorio = nomeRelatorio + '_' + buscarNomeOrdem();
                }

                if ($scope.object.tipoRelatorio.chave === 'notas_debito_recebidas') {
                    nomeRelatorio = nomeRelatorio + '_' + buscarNomeOrdem();
                }

                url = url + nomeRelatorio + ".rptdesign&nomRelat=" + $scope.object.tipoRelatorio.nome;
            }

            if (angular.isDefined($scope.object.unidade) && $scope.object.unidade != null && $scope.object.unidade.codigo.length > 0) {
				let codigoUnidade = '';
				var unidadaMatriz = null ;
				listaEmpresas.forEach((value) => {
					for(var i = 0; i < $scope.object.unidade.codigo.length; i++){
						if(value.id === $scope.object.unidade.codigo[i]){
							codigoUnidade = codigoUnidade + value.codigo + ',';
							if(i === 0){
								unidadaMatriz = value;
							}
						}
					}
				});
				url = url + "&codigoUnidade=" + codigoUnidade.substring(0, codigoUnidade.length - 1);
				if (unidadaMatriz.tipoUnidade == 'Matriz') {
					url = url + "&isMatriz=true";
				} else {
					url = url + "&isMatriz=false";
				}
			}

            if (angular.isDefined($scope.object.situacaoNotaContasReceber) && $scope.object.situacaoNotaContasReceber != null && $scope.object.situacaoNotaContasReceber.nome != null && $scope.object.situacaoNotaContasReceber.codigo != -3) {
                url = url + "&situacaoNotaFaturamento=" + $scope.object.situacaoNotaContasReceber.codigo;
            }

            if (angular.isDefined($scope.object.contaCorrente) && $scope.object.contaCorrente != null && $scope.object.contaCorrente.codigo != null) {
                url = url + "&numAgencia=" + $scope.object.contaCorrente.agencia;
                url = url + "&codigoConta=" + $scope.object.contaCorrente.codigo;
                url = url + "&numConta=" + $scope.object.contaCorrente.conta;
                url = url + "&digConta=" + $scope.object.contaCorrente.digito;
            }

            if ($scope.object.contaCorrente != null && angular.isDefined($scope.object.contaCorrente.banco) && $scope.object.contaCorrente.banco != null && $scope.object.contaCorrente.banco.codigo != null && $scope.object.contaCorrente.banco.codigo != -1) {
                url = url + "&codigoBanco=" + $scope.object.contaCorrente.banco.codigo;
                url = url + "&nomeBanco=" + $scope.object.contaCorrente.banco.nome;
            }

            if (angular.isDefined($scope.object.valorDe) && $scope.object.valorDe != null) {
                url = url + "&valorDe=" + $scope.object.valorDe;
            }

            if (angular.isDefined($scope.object.valorAte) && $scope.object.valorAte != null) {
                url = url + "&valorAte=" + $scope.object.valorAte;
            }

            if (angular.isDefined($scope.object.cliente) && $scope.object.cliente != null) {
                url = url + "&nomeCliente=" + $scope.object.cliente.nome;
			}

			if (angular.isDefined($scope.object.subcliente) && $scope.object.subcliente != null) {
				url = url + "&nomeSub=" + $scope.object.subcliente.nome;
			}

			if (angular.isDefined($scope.object.faturamentoCobranca) && $scope.object.faturamentoCobranca != null) {
				url = url + "&fatCobranca=" + $scope.object.faturamentoCobranca;
			}

			if (angular.isDefined($scope.object.cobrancaCronica) && $scope.object.cobrancaCronica != null) {
				url = url + "&cobrancaCronic=" + $scope.object.cobrancaCronica;
			}

			if (angular.isDefined($scope.object.classificacaoCliente) && $scope.object.classificacaoCliente != null) {
				url = url + "&classCliente=" + $scope.object.classificacaoCliente.codigo;
			}

			if ($scope.object.tipoRelatorio.chave === 'fluxo_de_caixa' && angular.isDefined($scope.listaCnpjPortalOi)) {
				url = url + '&paramOi="' + $scope.listaCnpjPortalOi + '"';
			}

            url = montarUrlDatas(url, 1);
            url = montarUrlDatas(url, 2);

            //tributos incluir
			let arrayCodigoTributo = [];
			arrayCodigoTributo = buscarCodigoTributo();

			if(arrayCodigoTributo && arrayCodigoTributo.length > 0) {

				var tributo = '';
				angular.forEach(arrayCodigoTributo, function(value, key) {

					if(key != 0) {
						tributo = tributo + (',');
					}

					tributo = tributo + value.toString();
				});

				url = url + '&codigoTributo="' + tributo + '"';
			}

            //aliquota
			let arrayCodigoAliquota = [];
			arrayCodigoAliquota = buscarAliquotas();

			if(arrayCodigoAliquota && arrayCodigoAliquota.length > 0) {

				let aliquota = '';
				angular.forEach(arrayCodigoAliquota, function(value, key) {
					if(key != 0) {
						aliquota = aliquota + (',');
					}
					aliquota = aliquota + value.toString();
				});

				url = url + '&codigoAliquota="' + aliquota + '"';
			}

			// Apenas para customização do relatório horizontal
			if(angular.isDefined($scope.object.tributoLista) && $scope.object.tributoLista != null){
				var listaTributos = [];
				listaTributos = $scope.object.tributoLista;

				if(listaTributos.length > 0){
					var tributo = '';
					$scope.object.tributoLista.forEach(function(value, key){
						if(key != 0){
							tributo = tributo + (',');
						}
						tributo = tributo + value.codigo.toString();
					});

					url = url + '&codTr="' + tributo + '"';
				}
			}

			// tributos excluir
			if(angular.isDefined($scope.object.tributoExcluidosLista) && $scope.object.tributoExcluidosLista != null){
				var listaExcluiTributos = [];
				listaExcluiTributos = $scope.object.tributoExcluidosLista;

				if(listaExcluiTributos.length > 0){
					var tributo = '';
					$scope.object.tributoExcluidosLista.forEach(function(value, key){
						if(key != 0){
							tributo = tributo + (',');
						}
						tributo = tributo + value.codigo.toString();
					});

					url = url + '&codTrExclui="' + tributo + '"';

				}
			}

            if (angular.isDefined($scope.tselected) && $scope.tselected != null && $scope.tselected.length > 0) {
                var ccCl = '';
                var i = 0;
                angular.forEach($scope.tselected, function(value, key) {
                	if (value) {
                		i++;
						if (key != 0) {
							ccCl = ccCl + (',');
						}
						ccCl = ccCl + value.codigo.toString();
                	}
                });

                if (ccCl) {
                	url = url + '&codigoCcCl="' + ccCl + '"';
                }
            }

            if (angular.isDefined($scope.object.situacao) && $scope.object.situacao && $scope.object.situacao != $scope.situacoes.nenhuma) {
				url = url + '&situacaoNotaFaturamento=' + $scope.object.situacao.codigo;
			}

            if (angular.isDefined($scope.object.gerencia) && $scope.object.gerencia != null && angular.isDefined($scope.object.gerencia.codigo) && $scope.object.gerencia.codigo != null && angular.isDefined($scope.object.gerencia.nome) && $scope.object.gerencia.nome != null) {
                url = url + "&codigoGerencia=" + $scope.object.gerencia.codigo;
                url = url + "&nomeGerencia=" + $scope.object.gerencia.nome;
            }

            if (cidadeIssSelecionada) {
				if (cidadeIssSelecionada.uf && cidadeIssSelecionada.nome) {
					url = url + '&cidadeISS=' + $scope.getNomeCidade(cidadeIssSelecionada);
				}

				if (cidadeIssSelecionada.codigo) {
					url = url + '&codigoCidadeISS=' + cidadeIssSelecionada.codigo;
				}
			}

            url = url.replace(" ", "_").replace(',"', '"') + "&tot=1";

            $window.open(url);
		}

		const buscarNomeOrdem = () => {
			switch($scope.object.relatorioOrdem) {
				case SizeEnum.FILIAL:
					return 'filial';

				case SizeEnum.GERENCIA:
					return 'gerencia';

				case SizeEnum.CENTRO_DE_LUCRO:
					return 'centro_de_lucro';

				default:
					return 'filial';
			}
		}

        delete $scope.object.emissaoDe;
        delete $scope.object.emissaoAte;
        delete $scope.object.dataVencimentoDe;
        delete $scope.object.dataVencimentoAte;
        delete $scope.object.periodoDe;
        delete $scope.object.periodoAte;
        delete $scope.object.cancelamentoDe;
        delete $scope.object.cancelamentoAte;

        $scope.buscarCentros(null);


        /*
			-------------
			ABA TRIBUTOS:
			-------------
		*/

		var categoriaVazio = {codigo: -1, descricao: ""};

		function buscarCategoriaTributoInss() {
			var filtroCategoriaTributo = {};
			filtroCategoriaTributo.tributoTO = {};
			filtroCategoriaTributo.tributoTO.codigo = 6;
			filtroCategoriaTributo.habilitaFaturamento = true;

			CategoriaTributo.page(null, filtroCategoriaTributo)
				.$promise.then(function(data) {
					$scope.categoriaInssList = data.content;
					$scope.categoriaInssList.sort(function(a,b){return $scope.getPercentualAliquota(a) - $scope.getPercentualAliquota(b)});
					$scope.categoriaInssList.unshift(categoriaVazio);
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'search', response);
					$scope.categoriaInssList = [];
				});
		}

		function buscarCategoriaTributoIrrf() {
			var filtroCategoriaTributo = {};
			filtroCategoriaTributo.tributoTO = {};
			filtroCategoriaTributo.tributoTO.codigo = 5;
			filtroCategoriaTributo.habilitaFaturamento = true;

			CategoriaTributo.page(null, filtroCategoriaTributo)
				.$promise.then(function(data) {
					$scope.categoriaIrrfList = data.content;
					$scope.categoriaIrrfList.sort(function(a,b){return $scope.getPercentualAliquota(a) - $scope.getPercentualAliquota(b)});
					$scope.categoriaIrrfList.unshift(categoriaVazio);
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'search', response);
					$scope.categoriaIrrfList = [];
				});
		}

		function buscarCategoriaTributoPis() {
			var filtroCategoriaTributo = {};
			filtroCategoriaTributo.tributoTO = {};
			filtroCategoriaTributo.tributoTO.codigo = 1;
			filtroCategoriaTributo.habilitaFaturamento = true;

			CategoriaTributo.page(null, filtroCategoriaTributo)
				.$promise.then(function(data) {
					$scope.categoriaPisList = data.content;
					$scope.categoriaPisList.sort(function(a,b){return $scope.getPercentualAliquota(a) - $scope.getPercentualAliquota(b)});
					$scope.categoriaPisList.unshift(categoriaVazio);
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'search', response);
					$scope.categoriaPisList = [];
				});
		}

		$scope.getPercentualAliquota = function(categoriaTributo) {
			if (categoriaTributo && categoriaTributo.aliquotas && categoriaTributo.aliquotas[0].percentual) {
				return categoriaTributo.aliquotas[0].percentual;
			}
			return 0.00;
		};

		$scope.querySearchCidadeIss = function(query) {
			$rootScope.loadingProgress = true;
			let textoCidade = query;

			let resultado = Cidade.buscarListaPorNome(textoCidade).$promise;

			return resultado.then(function(data) {
				$rootScope.loadingProgress = false;

				let listaCidade = data;
				listaCidade.sort(function(a,b){return a.uf.localeCompare(b.uf)});

				return listaCidade;
			}).catch(function(response) {
				$rootScope.loadingProgress = false;
				$scope.$emit('crud-critical', 'search', response);
				let cidadesTemp = [];
				return cidadesTemp;
			});
		};

		var cidadeIssSelecionada;

		$scope.selectedCidadeIssChange = function(item) {
			if (item && item.uf && item.codigo) {
				buscarCategoriaTributoIss(item.uf, item.codigo);
			}

			cidadeIssSelecionada = item;
		};

		function buscarCategoriaTributoIss(uf, codigoCidade) {
			var filtroCategoriaTributo = {};
			filtroCategoriaTributo.cidade = {};
			filtroCategoriaTributo.cidade.uf = uf;
			filtroCategoriaTributo.cidade.codigo = codigoCidade;
			filtroCategoriaTributo.habilitaFaturamento = true;

			CategoriaTributo.page(null, filtroCategoriaTributo)
				.$promise.then(function(data) {
					$scope.categoriaIssList = data.content;
					$scope.categoriaIssList.sort(function(a,b){return $scope.getPercentualAliquota(a) - $scope.getPercentualAliquota(b)});
					$scope.categoriaIssList.unshift(categoriaVazio);
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'search', response);
					$scope.categoriaIssList = [];
				});
		}

		$scope.desabilitarInss = false;
		$scope.desabilitarIrrf = false;
		$scope.desabilitarPis = false;
		$scope.desabilitarIss = false;
		var count = 0;

		$scope.changeTributos = function(item) {
			let mySet = new Set();

			var tributosExcluidos = [];

			angular.forEach(item , function(value, key) {
				mySet.add(value.codigo);
			});

			if (mySet.has(1)) {
				$scope.desabilitarPis = false;
				tributosExcluidos.push({codigo: 1});
			} else {
				$scope.desabilitarPis = true;
				delete $scope.object.categoriaPis;
			}

			if (mySet.has(4)) {
				$scope.desabilitarIss = false;
				tributosExcluidos.push({codigo: 4});
			} else {
				$scope.desabilitarIss = true;
				$scope.categoriaIssList = [];
				delete $scope.object.categoriaIss;
				delete $scope.searchTextCidadeIss;

				let elemento = document.getElementById('autocomplete_cidade_iss');
				if (elemento) {

					let autoChild = elemento.firstElementChild;
					let el = angular.element(autoChild);
					el.scope().$mdAutocompleteCtrl.clear();
					el.scope().$mdAutocompleteCtrl.loading = false;

					delete $scope.selectedCidadeIss;
					delete cidadeIssSelecionada;
				}
			}

			if (mySet.has(5)) {
				$scope.desabilitarIrrf = false;
				tributosExcluidos.push({codigo: 5});
			} else {
				$scope.desabilitarIrrf = true;
				delete $scope.object.categoriaIrff;
			}

			if (mySet.has(6)) {
				$scope.desabilitarInss = false;
				tributosExcluidos.push({codigo: 6});
			} else {
				$scope.desabilitarInss = true;
				delete $scope.object.categoriaInss;
			}

			if(count == 0){
				$scope.listTributosExcluidos = angular.copy($scope.listTributosExcluidos);
				count = 1;
			}else{
				$scope.listTributosExcluidos = angular.copy($scope.listTributos);
			}

			if(tributosExcluidos.length > 0) {
				for(var i = 0; i < tributosExcluidos.length; i++) {
					for(var j = 0; j < $scope.listTributosExcluidos.length; j++) {
						if(tributosExcluidos[i].codigo == $scope.listTributosExcluidos[j].codigo) {
							$scope.listTributosExcluidos.splice($scope.listTributosExcluidos.indexOf($scope.listTributosExcluidos[j]), 1);
						}
					}

					if(angular.isDefined($scope.object.tributoExcluidosLista) && $scope.object.tributoExcluidosLista != null){
						for(var k = 0; k < $scope.object.tributoExcluidosLista.length; k++) {
							if(tributosExcluidos[i].codigo === $scope.object.tributoExcluidosLista[k].codigo) {
								console.log($scope.object.tributoExcluidosLista[k].codigo);
								$scope.object.tributoExcluidosLista.splice($scope.object.tributoExcluidosLista.indexOf($scope.object.tributoExcluidosLista[k]), 1);
							}
						}
					}
				}
			}
		};

		function buscarAliquotas() {
			let listaCategoriaTributos = [];
			listaCategoriaTributos.push($scope.object.categoriaPis);
			listaCategoriaTributos.push($scope.object.categoriaIss);
			listaCategoriaTributos.push($scope.object.categoriaIrff);
			listaCategoriaTributos.push($scope.object.categoriaInss);

			let listaCodigo = [];

			angular.forEach(listaCategoriaTributos, function(value, key) {
				let codigo = getCodigoAliquota(value);

				if (codigo) {
					listaCodigo.push(codigo);
				}
			});

			return listaCodigo;
		}

		function getCodigoAliquota(categoriaTributo) {

			if (categoriaTributo && categoriaTributo.aliquotas && categoriaTributo.aliquotas[0].codigo) {
				return categoriaTributo.aliquotas[0].codigo;
			}

			return null;
		}

		//busca apenas os codigos de tributos que não forão escolhidos categoria tributo
		function buscarCodigoTributo() {
			let listaCategoriaTributos = [];
			listaCategoriaTributos.push($scope.object.categoriaPis);
			listaCategoriaTributos.push($scope.object.categoriaIss);
			listaCategoriaTributos.push($scope.object.categoriaIrff);
			listaCategoriaTributos.push($scope.object.categoriaInss);

			let conjCodigoTributo = new Set();

			angular.forEach(listaCategoriaTributos, function(value, key) {
				if (value && value.tributoTO && value.tributoTO.codigo) {
					conjCodigoTributo.add(value.tributoTO.codigo);
				}
			});

			let listaCodigoTributo = [];

			angular.forEach($scope.object.tributoLista, function(value, key) {
				if (value && value.codigo && !conjCodigoTributo.has(value.codigo)) {
					listaCodigoTributo.push(value.codigo);
				}
			});

			return listaCodigoTributo;
		}

		$scope.getNomeCidade = function(item) {

			if (item) {
				let nomeCidade = "";

				if (item.uf && item.nome) {
					nomeCidade = item.uf + "/" + item.nome;
				}

				return nomeCidade;
			}

			return "";
		};

		$scope.$on('changeInss', function() {
			if ($scope.object.categoriaInss.codigo === -1) {
				delete $scope.object.categoriaInss;
			}
		});

		$scope.$on('changeIrrf', function() {
			if ($scope.object.categoriaIrff.codigo === -1) {
				delete $scope.object.categoriaIrff;
			}
		});

		$scope.$on('changePis', function() {
			if ($scope.object.categoriaPis.codigo === -1) {
				delete $scope.object.categoriaPis;
			}
		});

		$scope.$on('changeIss', function() {
			if ($scope.object.categoriaIss.codigo === -1) {
				delete $scope.object.categoriaIss;
			}
		});

    }])
