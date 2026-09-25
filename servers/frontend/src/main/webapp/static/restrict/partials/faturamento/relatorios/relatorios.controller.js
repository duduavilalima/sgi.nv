angular.module('app.faturamentoRelatorios')

    .controller('FaturamentoRelatoriosListarCtrl', function($scope, $state, EnumService, Unidade, AutocompleteService, UsuarioLogadoService, Usuario, $rootScope, $window, $filter, CentroFinanceiro, Parametro, Tributo, CategoriaTributo, Cidade, PessoaJuridica, Gerencia) {

        $scope.object = {};
        $scope.relatorio = {};
        $scope.action = false;
		$scope.comboGerencia = {isDisabled: true};
        $scope.modulo = $state.current.data.modulo;
		$scope.titulo = $state.current.data.titulo;
		$scope.isGerente = false;

        $scope.form = undefined;

		$scope.usuario = UsuarioLogadoService.getUsuarioLogado();
		Usuario.usuarioLogado().$promise.then(function(data) {
			if (data.perfis) {data.perfis.forEach(perfil => {				
				if (perfil.nome === 'faturamento-cns-gerencia') {$scope.isGerente = true;}});
			}
		});
        var perfisRelatoriosFaturamento = ['visualizacao-pagamentos'];
        $scope.possuiPerfilFaturamento = [];

        $scope.object.gerencia = {};
        $scope.object.gerencia.codigo = -1;

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
		$scope.tipoDeValorList = [{
            codigo: 1,
            nome: "Bruto"
        }, {
            codigo: 2,
            nome: "Líquido"
		}];
		$scope.ordemMapaDeValorList = [{
			codigo: 3,
			nome: "Valor total (decrescente)"
		}, {
			codigo: 1,
			nome: "Centro financeiro"
		}, {
			codigo: 2,
			nome: "Cliente"
		}];
		$scope.agrupamentoList = [{
			codigo: 1,
			nome: "Cliente (raiz do CNPJ da NF)"
		}, {
			codigo: 2,
			nome: "Cliente + CL"
		}];
        $scope.gerenciaList = [];
        $scope.centroFinanceiroList = [];
        $scope.object.notaSaida = {};

        $scope.situacaoNotaFaturamento = {};
        $scope.tipoRelatorio = {};

        $scope.categoriaInssList = [];
        $scope.categoriaIrrfList = [];
        $scope.categoriaPisList = [];
        $scope.cidadeIssList = [];
        $scope.categoriaIssList = [];

        carregarTributos();
        buscarCategoriaTributoInss();
        buscarCategoriaTributoIrrf();
        buscarCategoriaTributoPis();

        $scope.desabilitarInss = true;
		$scope.desabilitarIrrf = true;
		$scope.desabilitarPis = true;
		$scope.desabilitarIss = true;

		$scope.cidadeIssObrigatorio = false;

		$scope.conjCodMatriz = new Set();

		$scope.conjRelSugg = new Set();

		$scope.habilitarPesquisaTodasGerencias = false;
		$scope.nao_permissao_faturamento = false;

		$scope.gerenciaList = [];
		buscarGerencias();

		function buscarGerencias() {
			let pGerencia = Gerencia.buscarTodasGerenciasHabFat().$promise;

			pGerencia.then(function(data) {
				UsuarioLogadoService.apenasFaturamento().$promise.then(response => {
					const { value, habilitarTodasGerencias } = response;
					$scope.gerenciaList = data;
					if (!value) {
						if (data.length === 1) {
							$scope.object.gerencia = $scope.gerenciaList[0];
							$scope.nao_permissao_faturamento = true;
						}
					}
					$scope.habilitarPesquisaTodasGerencias = habilitarTodasGerencias;
				});
			}).catch(function(response) {
				$scope.$emit('crud-critical', 'contrato load', response);
				$scope.gerenciaList = [];
			});
		}

		function carregarTributos() {

			 var filtroTributo = {};

			filtroTributo.exibeNota = true;

			Tributo.page(undefined, filtroTributo)
			.$promise.then(function(data){

				$scope.listTributos = [];

				angular.forEach(data.content, function(value, key) {
					$scope.listTributos.push(value);
				});

				let ultimo = $scope.listTributos.pop();
				let penultimo = $scope.listTributos.pop();
				$scope.listTributos.push(ultimo);
				$scope.listTributos.push(penultimo);

				}).catch(function(response) {
					$scope.$emit('crud-critical', 'contrato load', response);
					$scope.tributosList = [];
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
                    codigo: undefined,
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

        $scope.situacoesNotaFaturamento = [{
            codigo: -3,
            nome: ""
        }, {
            codigo: 15,
            nome: "Em recebimento"
        }, {
            codigo: -1,
            nome: "... A vencer"
        }, {
            codigo: -2,
            nome: "... Vencida"
        }, {
            codigo: 16,
            nome: "Recebida"
        }, {
            codigo: 17,
            nome: "Cancelada"
        }];


        $scope.tiposRelatorios = [{
        	id: 0,
            nome: "Notas fiscais por referência por centro de lucro",
            chave: "nf_referencia_cl"
        }, {
        	id: 1,
            nome: "Notas fiscais por referência por cliente",
            chave: "nf_referencia_cliente"
        }, {
        	id: 2,
            nome: "Notas fiscais por referência por diretoria",
            chave: "nf_referencia_diretoria"
        }, {
        	id: 3,
            nome: "Notas fiscais por referência por filial do CL",
            chave: "nf_referencia_filial_cl"
        }, {
        	id: 4,
            nome: "Notas fiscais por referência por filial do CL x Cidade ISS",
            chave: "nf_referencia_filial_cl_cidade_iss"
        }, {
        	id: 5,
            nome: "Notas fiscais por referência por gerência",
            chave: "nf_referencia_gerencia"
        }, {
        	id: 6,
            nome: "Notas fiscais por referência por tipo",
            chave: "nf_referencia_tipo"
        }, {
        	id: 7,
			nome: "Notas fiscais em aberto por filial",
			chave: "nf_aberto_filial"
		}, {
			id: 8,
			nome: "----------------------------------------------------------------------------------------------------------------------------------------------------------",
			chave: "-"
		}, {
			id: 9,
			nome: "Relação de notas fiscais do lote",
			chave: "relacao_nf_lote"
		}, {
			id: 10,
			nome: "Relação de notas fiscais do Portal Oi",
			chave: "relacao_nf_portal_oi"
		}, {
			id: 11,
			nome: "----------------------------------------------------------------------------------------------------------------------------------------------------------",
			chave: "-"
		}, {
			id: 12,
			nome: "Relação de notas fiscais com retenção contratual",
			chave: "relacao_nf_retencao_contratual"
		}, {
			id: 13,
			nome: "Resumo de retenção contratual",
			chave: "resumo_retencao_contratual"
		}, {
			id: 14,
			nome: "----------------------------------------------------------------------------------------------------------------------------------------------------------",
			chave: "-"
		}, {
			id: 15,
			nome: "Filial do contrato x filial da nota fiscal",
			chave: "filial_contrato_nf"
		}, {
			id: 16,
			nome: "Relação de notas fiscais - ISS/INSS",
			chave: "relacao_nf_Iss_inss"
		}, {
			id: 17,
            nome: "Histórico do CL",
            chave: "historico_cl"
		}, {
			id: 23,
            nome: "Mapa de vendas",
            chave: "mapa_de_vendas_faturamento"
		}, {
			id: 18,
			nome: "----------------------------------------------------------------------------------------------------------------------------------------------------------",
			chave: "-"
        }, {
			id: 24,
            nome: "Posição de faturamento por gerência (analítico)",
            chave: "posicao_faturamento_gerencia"
        }, {
        	id: 19,
			nome: "Posição de faturamento por diretoria (analítico)",
			chave: "posicao_faturamento_diretoria"
		}, {
			id: 20,
            nome: "Posição de faturamento por filial (analítico)",
			chave: "posicao_faturamento_filial"
		}, {
			id: 25,
            nome: "Resumo de posição de faturamento por gerência",
            chave: "resumo_posicao_faturamento_gerencia"
        }, {
        	id: 21,
			nome: "Resumo de posição de faturamento por diretoria",
			chave: "resumo_posicao_faturamento_diretoria"
		}, {
			id: 22,
            nome: "Resumo de posição de faturamento por filial",
			chave: "resumo_posicao_faturamento_filial"
		}];

		// FIXME: Refatorar
        for(let i = 0; i <= 6; i++) {
        	$scope.conjRelSugg.add($scope.tiposRelatorios[i].chave);
		}
		$scope.conjRelSugg.add('historico_cl');
		$scope.conjRelSugg.add('mapa_de_vendas_faturamento');
		$scope.conjRelSugg.add('nf_aberto_filial');
		$scope.conjRelSugg.add('relacao_nf_lote');
		$scope.conjRelSugg.add('relacao_nf_portal_oi');

		$scope.conjRelSugg.add('relacao_nf_retencao_contratual');
		$scope.conjRelSugg.add('resumo_retencao_contratual');
		$scope.conjRelSugg.add('filial_contrato_nf');
		$scope.conjRelSugg.add('relacao_nf_Iss_inss');

		$scope.conjRelSugg.add('posicao_faturamento_diretoria');
		$scope.conjRelSugg.add('posicao_faturamento_filial');
		$scope.conjRelSugg.add('posicao_faturamento_gerencia');
		$scope.conjRelSugg.add('resumo_posicao_faturamento_diretoria');
		$scope.conjRelSugg.add('resumo_posicao_faturamento_filial');
		$scope.conjRelSugg.add('resumo_posicao_faturamento_gerencia');

        $scope.tiposFormatos = [{
			nome: "Excel",
			codigo: 0
		}, {
			nome: "PDF",
			codigo: 1
		}];

        $scope.tiposRetencao = [{
            codigo: -3,
            nome: ""
        }, {
            codigo: 0,
            nome: "Tomador"
        }, {
            codigo: 1,
            nome: "Araujo Abreu"
        }];

		$scope.object.tipoFormato = {codigo: 1};

        $scope.buscarEmpresas = function() {
			// Tive que fazer uma promise pois o valor que estava me retornando não estava certo
			return new Promise(function(resolve, reject){
				var codigosUnidadesIgnoradas = [];
				Unidade.unidadesporempresaexceto(undefined, codigosUnidadesIgnoradas)
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

				$scope.conjCodMatriz = new Set();

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

					// Conjunto com apenas os codigos das Matrizes.
					// Será necessário verificar se a filial escolhida é matriz na hora de criar o relatório.
					if (value.tipoUnidade === 'Matriz') {
						$scope.conjCodMatriz.add(value.codigo);
					}
				});
			})
			.catch(function(error) {
				console.log(error);
			});

		$scope.todos = function(isSelecionado, unidade) {

			let conjunto = new Set();

			if ($scope.object.unidade && $scope.object.unidade.length !== 0) {
				angular.forEach($scope.object.unidade, function(value) {
					conjunto.add(value);
				});
			}

			if(isSelecionado) {
				angular.forEach(unidade.filiais, function(value) {
					conjunto.add(value.codigo);
				});
			} else {
				angular.forEach(unidade.filiais, function(value) {
					conjunto.delete(value.codigo);
				});
			}

			$scope.object.unidade = [];

			angular.forEach(conjunto, function(value) {
				$scope.object.unidade.push(value);
			});
		}

        $scope.desabilitarCampo = function(nomeCampo) {
            if (nomeCampo == 'lote') {
                if ($scope.object.tipoRelatorio !== undefined) {
                    return $scope.object.tipoRelatorio.chave == 'relacao_nf_lote';
                }
            }
        }

		$scope.hideCampos = function(nomeCampo) {

			if (nomeCampo == 'mapaDeVendas') {
				return $scope.object.tipoRelatorio !== undefined &&
				!($scope.object.tipoRelatorio.chave === 'relacao_nf_Iss_inss' ||
				$scope.object.tipoRelatorio.chave === 'nf_referencia_filial_cl_cidade_iss') &&
				$scope.object.tipoRelatorio.chave === 'mapa_de_vendas_faturamento';
			}

			if (nomeCampo == 'lote') {
				return $scope.object.tipoRelatorio === undefined ||
				($scope.object.tipoRelatorio !== undefined &&
				!($scope.object.tipoRelatorio.chave === 'nf_referencia_filial_cl_cidade_iss' ||
				$scope.object.tipoRelatorio.chave === 'relacao_nf_Iss_inss' ||
				$scope.object.tipoRelatorio.chave === 'mapa_de_vendas_faturamento'));

			}

			if (nomeCampo == 'ret') {
				return $scope.object.tipoRelatorio !== undefined &&
				!($scope.object.tipoRelatorio.chave === 'mapa_de_vendas_faturamento') &&
				($scope.object.tipoRelatorio.chave === 'nf_referencia_filial_cl_cidade_iss' ||
				$scope.object.tipoRelatorio.chave === 'relacao_nf_Iss_inss');
			}
		}

        //obter gerencia cuja usuario está logado
        UsuarioLogadoService.possuiUmPerfilLista(perfisRelatoriosFaturamento)
            .$promise.then(function(data) {
                $scope.possuiPerfilFaturamento = data.possuiUmPerfilLista;
            }).catch(function(response) {
                $rootScope.loadingProgress = false;
            });

        $scope.getGerenciaLogada = function() {
            CentroFinanceiro.getGerenciaLogada()
                .$promise.then(function(data) {
                    $scope.gerenciaLogada = data;
//                    $scope.buscarTodasGerencias();
                }).catch(function(response) {
                    $scope.$emit('crud-critical', 'search', response);
                });
        }

        $scope.marcarOpcao = function() {

			// Essa parte é para marcar/desmarcar uma matriz,
			// caso seja marcado todas as filias dessa matriz.
        	angular.forEach($scope.empresaList, function(unidade) {
        		let conjCodFilial = new Set();

        		angular.forEach(unidade.filiais, function(filial) {
	        		conjCodFilial.add(filial.codigo);
				});

				let qtdFilialSelecionado = 0;

				angular.forEach($scope.object.unidade, function(codUnidade) {
					if (conjCodFilial.has(codUnidade)) {
						qtdFilialSelecionado++;
					}
				});

				if (qtdFilialSelecionado === unidade.filiais.length) {
					unidade.todos = true;
				} else {
					unidade.todos = false;
				}
			});
        };

		// FIXME: Verificar se o método está sendo utilizado.
        //obter todas gerencias do sistema
        $scope.buscarGerenciasPorUnidade = function() {
            $scope.gerenciaList = [];
            if ($scope.object.unidade && $scope.object.unidade.length !== 0) {
                $scope.object.unidade.forEach((item) => {
					CentroFinanceiro.buscarGerenciasPorUnidade({id: item}, undefined).$promise.then(function(data) {
							data.forEach((ger) => {
							    $scope.gerenciaList.push(ger);
							});
							$scope.comboGerencia.isDisabled = false;
							$scope.object.gerencia = undefined;
							delete $scope.object.gerencia;
						}).catch(function(response) {
							$scope.$emit('crud-critical', 'search', response);
						});
                });
            } else {
                $scope.gerenciaList = CentroFinanceiro.buscarTodasGerencias();
            }
        };

        $scope.getGerenciaLogada();

        $scope.buscarEmpresas();

        $scope.listar = function() {
            $state.go('interfaces.listar');
        };

        $scope.reset = function(form) {
            $scope.object = {};
            $scope.gerencia = [];
            $scope.centroFinanceiroList = [];
            document.querySelectorAll('input[type=search]')[0].value = '';
            document.querySelectorAll('input[type=search]')[1].value = '';
            $scope.searchTextCliente = undefined;
            $scope.selectedItemCliente = undefined;
            $scope.resultsClient = [];
            $scope.object = {"gerencia":{"codigo":-1},"notaSaida":{},"tipoFormato":{"nome":"PDF","codigo":1},"_1DataDe":"","_1DataAte":"","data1De":"1ª data de","data1Ate":"1ª data até","_2DataDe":"","_2DataAte":"","data2De":"2ª data de","data2Ate":"2ª data até"};
            $scope.form.$setPristine();
			$scope.form.$setUntouched();
			$scope.form.relatorio.$error = {};
			$scope.form.empresa.$error = {};
        };

        $scope.$on('changeTipoLoteInterface', function() {
            $scope.renderizarLblDataCancelamento();
        });

        $scope.renderizarLblDataCancelamento = function() {
            if ($scope.object.tipoLoteInterface === 'FATC - Cancelamentos fora do período') {
                return true;
            }
            return false;
        }

        /**
         * Pesquisa de Clientes
         */
        $scope.searchTextCliente = undefined;
        $scope.selectedItemCliente = undefined;
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
        }

        $scope.querySearchCliente = function(query) {
            $rootScope.loadingProgress = true;
            let nomeFantasia = query;
            let clientes = [];

            let resultado = PessoaJuridica.buscarListaPorNomeFantasia(nomeFantasia).$promise;

			return resultado.then(function(data) {
				$rootScope.loadingProgress = false;
				angular.forEach(data, function(cliente) {
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
                } else {
                    $scope.resultsClient = [];
                    delete $scope.selectedItemCliente;
                    delete $scope.object.cliente;
                    $scope.resultsSubClient = [];
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

		// Início AutoComplete Centro de Lucro
		$scope.searchTextCentroLucro = undefined;
		$scope.selectedItemCentroLucro = undefined;

		$scope.fnSelectedItemCentroLucro = function(item) {
			$scope.action = true;
			$scope.object.centroFinanceiro = item;
		};

		$scope.querySearchCentroLucro = function(query){
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

        $scope.buscarCentros = function(unidade) {
            if (angular.isDefined(unidade) && unidade !== undefined) {
                var obj = {};
                obj.unidade = unidade;
                CentroFinanceiro.buscarPorUnidadeTelaRelatorio({pageSize: 10000}, obj).$promise.then(function(data) {
                        $scope.centros = data;

                        if ($scope.centros.length == 1) {
                            $scope.object.centroFinanceiro = $scope.centros[0];
                            $scope.mudarCentroLucro();
                        } else if ($scope.centros.length == 0) {
							$scope.centros = [];
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
        }
        $scope.formsIncomplete = function() {
            if (angular.isDefined($scope.form)) {
                if ($scope.form.$invalid === true) {
                    return true;
                }
            }
            return false;
        };

        $scope.validaValorDeAte = function() {
            if (angular.isDefined($scope.object.valorDe) && $scope.object.valorDe !== undefined && angular.isDefined($scope.object.valorAte) && $scope.object.valorAte !== undefined) {
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
        }

        $scope.$on('changeComboRelatorio', function() {

			let objetoAnterior = angular.copy($scope.object);

            if ($scope.object.tipoRelatorio.chave === 'filial_contrato_nf' && $scope.object.unidade) {
                delete $scope.object.unidade;
            }

            if ($scope.object.tipoRelatorio.chave !== 'relacao_nf_lote') {
                delete $scope.object.numeroLote;
            }

			if ($scope.object.retISS) {
				delete $scope.object.retISS;
			}

			if ($scope.object.tipoRelatorio.chave === 'resumo_retencao_contratual' || $scope.object.tipoRelatorio.chave === 'relacao_nf_retencao_contratual') {
				$scope.object.onSaldo = true;
			}

			if ($scope.object.tipoRelatorio.chave === 'resumo_retencao_contratual') {
				$scope.object.situacaoNotaFaturamento = { codigo: 16, nome: "Recebida" };
				$scope.tipoRelatorioAnterior = true;

			} else if ($scope.tipoRelatorioAnterior) {
				$scope.object.situacaoNotaFaturamento = { codigo: -3, nome: "" };
				$scope.tipoRelatorioAnterior = false;
			}

			if ($scope.object.tipoRelatorio.chave !== 'relacao_nf_lote') {
				if ($scope.object.tipoRelatorio.chave === 'mapa_de_vendas_faturamento') {
					$scope.object.tipoDeValor = $scope.tipoDeValorList[0];
					$scope.object.ordemMapaDeValor = $scope.ordemMapaDeValorList[0];
					$scope.object.agrupamento = $scope.agrupamentoList[0];
					$scope.object.quantidadeDeCliente = 40;
					$scope.object.data1 = 2;
					$scope.$emit('changeData1');
				}else{
					delete $scope.object.situacaoNotaFaturamento;
					delete $scope.object.tipoDeValor;
					delete $scope.object.quantidadeDeCliente;
					delete $scope.object.ordemMapaDeValor;
					delete $scope.object.agrupamento;
					$scope.object.data1 = 1;
					$scope.object.data1 = objetoAnterior.data1;
					$scope.object.data1Ate = objetoAnterior.data1Ate;
					$scope.object.data1De = objetoAnterior.data1De;
					$scope.object.data1 = objetoAnterior.data1;
					$scope.object._1DataAte = objetoAnterior._1DataAte;
					$scope.object._1DataDe = objetoAnterior._1DataDe;
					$scope.$emit('changeData1');
				}
			}else{
				delete $scope.object.data1;
				$scope.object.data1De = '1ª data de';
				$scope.object.data1Ate = '1ª data até';
				delete $scope.object._1DataAte;
				delete $scope.object._1DataDe;
				$scope.$emit('changeData1');
			}

        });

        // FUNÇÃO PARA GERAR RELATÓRIO
        $scope.gerarRelatorio = function() {
			let errorMapaDeVendas = false;
			$scope.action = true;


			// VALIDAR DATAS - OBS.: Não validar quando o relatório for "Relação de notas fiscais do lote" (relacao_nf_lote)
			if ($scope.object.tipoRelatorio.chave !== 'relacao_nf_lote') {
				if ((angular.isUndefined($scope.object._1DataDe) || $scope.object._1DataDe == undefined || $scope.object._1DataDe == '') ||
					(angular.isUndefined($scope.object._1DataAte) || $scope.object._1DataAte == undefined || $scope.object._1DataAte == '')) {

					if ((angular.isUndefined($scope.object._2DataDe) || $scope.object._2DataDe == undefined || $scope.object._2DataDe == '') ||
						(angular.isUndefined($scope.object._2DataAte) || $scope.object._2DataAte == undefined || $scope.object._2DataAte == '')) {
						$scope.notification.error("", "É obrigatório informar, pelo menos, um período de datas (emissão, vencimento ou pagamento).");
						$scope.action = false;
						return;
					}
				}
			}


			if ($scope.object.tipoRelatorio.chave === 'mapa_de_vendas_faturamento') {
				
				if ($scope.object.quantidadeDeCliente <= 0) {
					errorMapaDeVendas = true;
					$scope.notification.error("", "O valor mínimo para a quantidade de clientes é 1.");
				} else if (!$scope.object.quantidadeDeCliente) {
					errorMapaDeVendas = true;
					$scope.notification.error("", "É obrigatório informar a quantidade de clientes.");
				}

				if (errorMapaDeVendas) {
					return;
				}
			}

			// VALIDAR NÚMERO DO LOTE
			if ($scope.object.tipoRelatorio.chave === 'relacao_nf_lote') {
				if (!angular.isDefined($scope.object.numeroLote) || $scope.object.numeroLote == undefined) {
					$scope.notification.error("", "É obrigatório informar o número do lote");
					$scope.action = false;
					return;
				}
			}


            var url;
            Parametro.page({chave: 'layout.relatorios.temp.url'}).$promise.then(function(parametro) {

				var caminhoBase;

				if(angular.isDefined($scope.object.tipoFormato) && $scope.object.tipoFormato !== undefined && $scope.object.tipoFormato.nome !== undefined && $scope.object.tipoFormato.codigo == 0){
					caminhoBase = '/relatorio/frameset?__locale=pt_BR&__emitterid=org.eclipse.birt.report.engine.emitter.prototype.excel&__asattachment=true&__format=xls&__masterpage=false&__showtitle=false&__report=report/sgi/faturamento/';
				}else{
				    caminhoBase = '/relatorio/frameset?__locale=pt_BR&__format=pdf&__showtitle=false&__report=report/sgi/faturamento/';
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
					// Usar a url http://localhost:8080/sgi para gerar url com apontamento local.
					// Depois voltar para parametro.valor ao subimeter o código a um commit.
					//url = "http://localhost:8080/sgi" + caminhoBase;
                    url = parametro.valor + caminhoBase;
				}

				let pParamOi = Parametro.page({chave: 'faturamento.interface.oitelemar.cnpj'}).$promise;
				let pParamTim = Parametro.page({chave: 'faturamento.interface.tim.cnpj'}).$promise;

                if ($scope.object.tipoRelatorio.chave === 'relacao_nf_portal_oi') {
                	pParamOi.then(function(parametro2) {

                		$scope.listaCnpjPortalOi = parametro2.valor;
                		doGerarRelatorio();
						$scope.action = false;

                	}).catch(function(response) {
						console.error('crud-critical', 'dialog', response);
					  	$scope.action = false;
					  	$scope.notification.criticalError(response.status, response.statusText, response.data);
				  	});

                } else if ($scope.object.tipoRelatorio.chave === 'relacao_nf_lote') {
					pParamOi.then(function(parametro3) {

                		$scope.listaCnpjPortalOi = parametro3.valor;

						return pParamTim;
                	}).then(function(parametro4) {

                		$scope.listaCnpjPortalTim = parametro4.valor;
                		doGerarRelatorio();
						$scope.action = false;

                	}).catch(function(response) {
						console.error('crud-critical', 'dialog', response);
					  	$scope.action = false;
					  	$scope.notification.criticalError(response.status, response.statusText, response.data);
				  	});

                } else {
					doGerarRelatorio();
					$scope.action = false;
                }

			}).catch(function(response) {
                console.error('crud-critical', 'dialog', response);
                $scope.action = false;
                $scope.notification.criticalError(response.status, response.statusText, response.data);
            });

            function doGerarRelatorio() {
                /** ABA FATURAMENTO **/
                if (angular.isDefined($scope.object.tipoRelatorio) && $scope.object.tipoRelatorio !== undefined && $scope.object.tipoRelatorio.nome !== undefined && angular.isDefined($scope.object.tipoRelatorio) && $scope.object.tipoRelatorio.chave !== undefined) {
                    url = url + $scope.object.tipoRelatorio.chave + ".rptdesign&nomRelat=" + $scope.object.tipoRelatorio.nome;
                }

                if (angular.isDefined($scope.object.unidade) && $scope.object.unidade !== undefined && $scope.object.unidade.length > 0) {
                    let codigoUnidade = '';
                    $scope.object.unidade.forEach((value) => {
                        codigoUnidade = codigoUnidade + value + ',';
                    });
                    url = url + "&codigoUnidade=" + codigoUnidade.substring(0, codigoUnidade.length - 1);
//                    url = url + "&nomeUnidade=" + $scope.object.unidade.codnome;
                    if ($scope.conjCodMatriz.has($scope.object.unidade[0])) {
                        url = url + "&isMatriz=true";
                    } else {
                        url = url + "&isMatriz=false";
                    }
                }

                if (angular.isDefined($scope.object.situacao) && angular.isDefined($scope.object.situacao.codigo) && $scope.object.situacao.codigo !== undefined) {
                    url = url + "&situacaoFaturamento=" + $scope.object.situacao.codigo;
                }

                if (angular.isDefined($scope.object.tipoFaturamento) && $scope.object.tipoFaturamento !== undefined && ($scope.object.tipoFaturamento.codigo !== -1)) {
                    url = url + "&tipoFaturamento=" + $scope.object.tipoFaturamento.codigo;
                }

                if (angular.isDefined($scope.object.codigo) && $scope.object.codigo !== undefined) {
                    url = url + "&codigoFaturamento=" + $scope.object.codigo;
                }

                if (angular.isDefined($scope.object.valorDe) && $scope.object.valorDe !== undefined) {
                    url = url + "&valorDe=" + $scope.object.valorDe;
                }

                if (angular.isDefined($scope.object.valorAte) && $scope.object.valorAte !== undefined) {
                    url = url + "&valorAte=" + $scope.object.valorAte;
                }

                if (angular.isDefined($scope.object.cliente) && $scope.object.cliente !== undefined) {
                    if ($scope.object.cliente.codigo && !$scope.conjRelSugg.has($scope.object.tipoRelatorio.chave)) {
                        url = url + "&codigoCliente=" + $scope.object.cliente.codigo;
                    }
                }

                if (angular.isDefined($scope.object.centroFinanceiro) && $scope.object.centroFinanceiro !== undefined) {
                    url = url + "&codigoCentroLucro=" + $scope.object.centroFinanceiro.codigo;
                    url = url + "&nomeCentroLucro=" + $scope.object.centroFinanceiro.identificadorFinal + " - " + $scope.object.centroFinanceiro.nome;

                }

                /** ABA NOTA FISCAL **/

                if (angular.isDefined($scope.object.situacaoNotaFaturamento) && $scope.object.situacaoNotaFaturamento !== undefined && $scope.object.situacaoNotaFaturamento.nome !== undefined && $scope.object.situacaoNotaFaturamento.codigo !== -3) {
                    url = url + "&situacaoNotaFaturamento=" + $scope.object.situacaoNotaFaturamento.codigo;
                }

                if (angular.isDefined($scope.object.numeroLote) && angular.isDefined($scope.object.numeroLote) && $scope.object.numeroLote !== undefined) {
                    url = url + "&numeroLote=" + $scope.object.numeroLote;
                }

				if (angular.isDefined($scope.object.retISS) && angular.isDefined($scope.object.retISS) && $scope.object.retISS !== undefined && $scope.object.retISS.codigo !== -3) {
					url = url + "&retISS=" + $scope.object.retISS.codigo;
				}

                if (angular.isDefined($scope.object.gerencia) && $scope.object.gerencia !== undefined && angular.isDefined($scope.object.gerencia.codigo) && $scope.object.gerencia.codigo !== undefined && angular.isDefined($scope.object.gerencia.nome) && $scope.object.gerencia.nome !== undefined) {
                    url = url + "&codigoGerencia=" + $scope.object.gerencia.codigo; // + "&nomGer=" + $scope.object.gerencia.nome;
                    url = url + "&nomeGerencia=" + $scope.object.gerencia.nome;
				}else if($scope.isGerente){
					let gerenciasDoGerente = "";
					$scope.gerenciaList.forEach((gerencia, key) => {
						gerenciasDoGerente += gerencia.codigo + ($scope.gerenciaList.length == key+1 ? '' : ',');
                    });
					url = url + "&gerenciasDoGerente=" + gerenciasDoGerente;
				}

				if ($scope.object.onSaldo) {
                    url = url + `&onSaldo=${$scope.object.onSaldo}`;
                } else {
					url = url + "&onSaldo=false";
				}

                if (angular.isDefined($scope.object.cliente) && $scope.object.cliente !== undefined) {
                    url = url + "&nomeCliente=" + $scope.object.cliente.nome;
				}
				
				//MAPA DE VENDAS 

				if ($scope.object.tipoRelatorio.chave === 'mapa_de_vendas_faturamento') {
					url = url + `&agrupamento=${$scope.object.agrupamento.codigo}`;
					url = url + `&ordem_mapa_vendas=${$scope.object.ordemMapaDeValor.codigo}`;
					url = url + `&tipo_valor_mapa_vendas=${$scope.object.tipoDeValor.codigo}`;
					url = url + `&qtd_cliente_mapa_vendas=${$scope.object.quantidadeDeCliente}`;
				}

                //tributos
				let arrayCodigoTributo = [];
				arrayCodigoTributo = buscarCodigoTributo();

                if(arrayCodigoTributo && arrayCodigoTributo.length > 0) {

					var tributo = '';
					angular.forEach(arrayCodigoTributo, function(value, key) {

						if(key !== 0) {
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

						if(key !== 0) {
							aliquota = aliquota + (',');
						}

						aliquota = aliquota + value.toString();
					});

					url = url + '&codigoAliquota="' + aliquota + '"';
				}

				if (cidadeIssSelecionada) {
					if (cidadeIssSelecionada.uf && cidadeIssSelecionada.nome) {
						url = url + '&cidadeISS=' + $scope.getNomeCidade(cidadeIssSelecionada);
					}

					if (cidadeIssSelecionada.codigo) {
						url = url + '&codigoCidadeISS=' + cidadeIssSelecionada.codigo;
					}
				}

				if (($scope.object.tipoRelatorio.chave === 'relacao_nf_portal_oi' || $scope.object.tipoRelatorio.chave === 'relacao_nf_lote')
					&& angular.isDefined($scope.listaCnpjPortalOi)) {
					url = url + '&paramOi="' + $scope.listaCnpjPortalOi + '"';
				}

				if ($scope.object.tipoRelatorio.chave === 'relacao_nf_lote' && angular.isDefined($scope.listaCnpjPortalTim)) {
					url = url + '&paramTim="' + $scope.listaCnpjPortalTim + '"';
				}

				url = montarUrlDatas(url, 1);
				url = montarUrlDatas(url, 2);

                url = url.replace(" ", "_").replace(',"', '"') + "&tot=1";

                $window.open(url);
            }
        }

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

			CategoriaTributo.page(undefined, filtroCategoriaTributo)
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

			CategoriaTributo.page(undefined, filtroCategoriaTributo)
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

			CategoriaTributo.page(undefined, filtroCategoriaTributo)
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

			CategoriaTributo.page(undefined, filtroCategoriaTributo)
				.$promise.then(function(data) {
					$scope.categoriaIssList = data.content;
					$scope.categoriaIssList.sort(function(a,b){return $scope.getPercentualAliquota(a) - $scope.getPercentualAliquota(b)});
					$scope.categoriaIssList.unshift(categoriaVazio);
				}).catch(function(response) {
					$scope.$emit('crud-critical', 'search', response);
					$scope.categoriaIssList = [];
				});
		}

		$scope.$watch('object.tributoLista', function() {

			let mySet = new Set();

			angular.forEach($scope.object.tributoLista, function(value, key) {
				mySet.add(value.codigo);
			});

			if (mySet.has(1)) {
				$scope.desabilitarPis = false;
			} else {
				$scope.desabilitarPis = true;
				delete $scope.object.categoriaPis;
			}

			if (mySet.has(4)) {
				$scope.desabilitarIss = false;
				$scope.cidadeIssObrigatorio = true;

			} else {
				$scope.desabilitarIss = true;
				$scope.cidadeIssObrigatorio = false;

				$scope.categoriaIssList = [];
				delete $scope.object.categoriaIss;

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
			} else {
				$scope.desabilitarIrrf = true;
				delete $scope.object.categoriaIrff;
			}

			if (mySet.has(6)) {
				$scope.desabilitarInss = false;
			} else {
				$scope.desabilitarInss = true;
				delete $scope.object.categoriaInss;
			}

		});

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

			return undefined;
		}

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


		/**
		* CAMPOS DATAS
		*/

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
//			 limparCamposDataDoRelatorio();
		}

		function limparCamposData2() {
			 $scope.object.data2 = undefined;
			 $scope.object._2DataDe = undefined;
			 $scope.object._2DataAte = undefined;
			 $scope.object.data2De = '2ª data de';
			 $scope.object.data2Ate = '2ª data até';
//			 limparCamposDataDoRelatorio();
		}

		limparCamposData1();
		limparCamposData2();

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
		acaoDataMap.set(TipoDataEnum.VAZIO, {label: undefined, fn: labelCamposDatas});
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

		function montarUrlDatas(url, tipoData) {
			if (TipoDataEnum.EMISSAO === $scope.object[`data${tipoData}`]) {
				url = url + "&dtEmiNfDe=" + $filter('date')($scope.object[`_${tipoData}DataDe`], 'yyyy-MM-dd');
				url = url + "&dtEmiNfAte=" + $filter('date')($scope.object[`_${tipoData}DataAte`], 'yyyy-MM-dd');
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
			}
			return url;
		}

		// VALIDAÇÔES DO RELATÓRIO DE MAPA DE VENDAS

		$scope.$on('quantidadeDeCliente', function() {
			if (!$scope.object.quantidadeDeCliente) {
				$scope.object.quantidadeDeCliente = 0;
			}
	   });

    });