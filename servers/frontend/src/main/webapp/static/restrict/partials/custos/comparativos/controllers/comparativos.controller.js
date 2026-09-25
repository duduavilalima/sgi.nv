angular
    .module('app.comparativos')

    .controller('ComparativosCtrl', detailGrafico);

function detailGrafico($scope, $mdDialog, Fullscreen, Unidade, $interval, Regional, CentroFinanceiro, GraficoFaturamentoDespesa) {

    const $ctrl = this;

    const errorCatch = (response) => {
        console.error(response);
        $scope.$emit('crud-critical', 'search', response);
        reject(false);
    };

    function init() {
        $ctrl.object = {};
        $ctrl.object.tempoMinutos = 1;
        $ctrl.Fullscreen = Fullscreen;

        $ctrl.progress_chart1 = true;
        $ctrl.progress_chart2 = true;
        $ctrl.progress_chart3 = true;
        $ctrl.progress_chart4 = true;

        $ctrl.object.ano = null;
        $ctrl.mostraGrafico = false;

        $ctrl.empresaList = [];
        $ctrl.listRegional = [];
        $ctrl.listGerencias = [];
        $ctrl.listCls = [];
        $ctrl.listAnos = [];

        gerenciarEmpresas()
        .then(() => buscarRegionais())
		.then(() => buscarGerencias())
        .then(() => buscarAnos());
    }

    $ctrl.reset = function() {
		delete $ctrl.object;
		init();
	};

    // ## CAMPO MATRIZ / FILIAL ##
    $ctrl.buscarEmpresas = function() {
        return new Promise(function(resolve, reject) {
            let codigosUnidadesIgnoradas = [];
            Unidade.unidadesporempresaexceto(undefined, codigosUnidadesIgnoradas)
                .$promise.then(function(data) {
                    listaEmpresas = data;
                    resolve(data);
                })
                .catch(function(error) {
					reject(error);
				});
        });
    }

    function gerenciarEmpresas() {
        return new Promise((resolve, reject) => {
            $ctrl.buscarEmpresas()
                .then(function(data) {
                    $ctrl.conjCodMatriz = new Set();

                    $ctrl.empresaList = [];
                    data.forEach(function(value) {
                        // Criei um array de filiais para poder coloca-lo como filho de uma matriz
                        let filiais = data.filter(function(filial) {
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
                            $ctrl.empresaList.push(obj);
                        }

                        // Conjunto com apenas os codigos das Matrizes.
                        // Será necessário verificar se a filial escolhida é matriz na hora de criar o relatório.
                        if (value.tipoUnidade === 'Matriz') {
                            $ctrl.conjCodMatriz.add(value.codigo);
                        }
                    });
                    resolve(data);
                })
                .catch(errorCatch);
        });
    }

    $ctrl.marcarOpcao = function() {
        angular.forEach($ctrl.empresaList, function(unidade) {
            let conjCodFilial = new Set();

            angular.forEach(unidade.filiais, function(filial) {
                conjCodFilial.add(filial.codigo);
            });

            let qtdFilialSelecionado = 0;

            angular.forEach($ctrl.object.unidade, function(codUnidade) {
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

		$ctrl.changeEmpresa();
    };

    $ctrl.todos = function(isSelecionado, unidade) {
        let conjunto = new Set();

        if ($ctrl.object.unidade && $ctrl.object.unidade.length !== 0) {
            angular.forEach($ctrl.object.unidade, function(value) {
                conjunto.add(value);
            });
        }

        if (isSelecionado) {
            angular.forEach(unidade.filiais, function(value) {
                conjunto.add(value.codigo);
            });
        } else {
            angular.forEach(unidade.filiais, function(value) {
                conjunto.delete(value.codigo);
            });
        }

        $ctrl.object.unidade = [];

        angular.forEach(conjunto, function(value) {
            $ctrl.object.unidade.push(value);
        });
    };

    // ## CAMPO MATRIZ / FILIAL ##
    function buscarRegionais() {
        return new Promise((resolve, reject) => {
            Regional.buscarListaAtiva().$promise.then(function(data) {
                $ctrl.listRegional = [];
                angular.forEach(data, function(value, key) {
                    $ctrl.listRegional.push(value);
                });
                resolve(data);
            }).catch(errorCatch);
        });
    }

    function buscarGerencias() {
        return new Promise((resolve, reject) => {
            let pCentroFinanceiro = null;

            if ($ctrl.object.regional && $ctrl.object.regional.codigo) {
                pCentroFinanceiro = CentroFinanceiro.buscarGerenciasPorRegional({
                    id: $ctrl.object.regional.codigo
                }).$promise;
            }
            else {
                pCentroFinanceiro = CentroFinanceiro.buscarTodasGerencias().$promise;
            }

            pCentroFinanceiro.then(function(data) {
                $ctrl.listGerenciasAtivas = [];
                $ctrl.listGerenciasInativas = [];

                angular.forEach(data, function(value, key) {
                	if(value.situacao === "Ativo") {
                		$ctrl.listGerenciasAtivas.push(value)
                	} else {
                		$ctrl.listGerenciasInativas.push(value)
                	}
                });

                resolve(data);
            }).catch(errorCatch);
        });
    }

    function buscarCLs() {
        return new Promise((resolve, reject) => {
            $ctrl.listCls = [];

            if ($ctrl.object.gerencia && $ctrl.object.gerencia.codigo) {

                let obj = {
                    codigoCentroFinanceiro: $ctrl.object.gerencia.codigo,
                    listaCodEmpresa: $ctrl.object.unidade,
                    ano: $ctrl.object.ano
                };

                GraficoFaturamentoDespesa.buscarCLs({
                    pageSize: 10000
                }, obj).$promise.then(function(data) {
                    $ctrl.listCls = data;
                    resolve(data);
                }).catch(errorCatch);
            }
            resolve(true);
        });
    }

    $ctrl.exibirDialogCls = function() {
    	$mdDialog.show({
    		controller: DialogControllerCL,
			locals: {
				title: 'Centro de lucro - Legenda'
			},
			templateUrl: 'partials/custos/comparativos/html/partials/dialogCL.html',
			parent: angular.element(document.body),
			clickOutsideToClose: true,
			fullscreen: false,
			scope: $scope,
            preserveScope: true,
			arialabel: 'Centro de lucro - Legenda'
		});
    }

    function DialogControllerCL($mdDialog) {
		$ctrl.action = false;

		$ctrl.hide = function() {
			$mdDialog.hide();
		};

		$ctrl.cancel = function() {
			$mdDialog.cancel();
		};
	}

    function buscarAnos() {
        $ctrl.listAnos = [];
        let anoCorrente = moment().year();

        while (anoCorrente >= 2018) {
            $ctrl.listAnos.push(anoCorrente--);
        }

        $ctrl.object.ano = $ctrl.listAnos[0];
    }

    $ctrl.changeEmpresa = function() {
        return new Promise((resolve, reject) => {
            if ($ctrl.object.unidade) {
				delete $ctrl.object.cl;
				buscarCLs();
            }
            return atualizarGraficos();
        });
    };

    $ctrl.changeRegional = function() {
        return new Promise((resolve, reject) => {
            delete $ctrl.object.gerencia;
            delete $ctrl.object.cl;
            buscarGerencias();
            buscarCLs();
            return atualizarGraficos();
        });
    };

    $ctrl.changeGerencia = function() {
        return new Promise((resolve, reject) => {
//        	delete $ctrl.object.unidade;
            delete $ctrl.object.cl;
            buscarCLs();

            return atualizarGraficos();
        });
    };

    $ctrl.changeCLs = function() {
        return atualizarGraficos();
    };


    /*
       ------------------
    	### GRÁFICOS ###
       ------------------

       Existe um issue ainda não tratado na biblioteca de gráficos c3js, sobre a ordenação dos dados após a atualização do chart.
       Como solução de contorno encontrada no https://github.com/c3js/c3/issues/1945, o atributo order deve ser null pois assim a biblioteca irá
       respeitar a ordem em que os dados foram passados no atributo columns.
    */
    function createChart(idChart, listValP, listValR, listValExtra, corPrevisto, corRealizado, corExtra, nomePrevisto, nomeRealizado, nomeExtra, format, ...args) {

    let dadosGrafico =
        {
            bindto: idChart,
            transition: {
                duration: 400
            },
            legend: {
                position: 'bottom',
            },
            data: {
                columns: [
                    ['data1', listValP[0], listValP[1], listValP[2], listValP[3], listValP[4], listValP[5], listValP[6], listValP[7], listValP[8], listValP[9], listValP[10], listValP[11]],
                    ['data2', listValR[0], listValR[1], listValR[2], listValR[3], listValR[4], listValR[5], listValR[6], listValR[7], listValR[8], listValR[9], listValR[10], listValR[11]],
                    ['data3', listValExtra[0], listValExtra[1], listValExtra[2], listValExtra[3], listValExtra[4], listValExtra[5], listValExtra[6], listValExtra[7], listValExtra[8], listValExtra[9], listValExtra[10], listValExtra[11]],
                ],
                type: 'bar',
                types: {
                    data1: 'line'
                },
                order: null,
                groups: [
                    ['data2', 'data3', 'data4']
                ],
                colors: {
                    data1: corPrevisto,
                    data2: corRealizado,
                    data3: corExtra,
                },
                labels: false,
                names: {
                    data1: nomePrevisto,
                    data2: nomeRealizado,
                    data3: nomeExtra
                }
            },
            grid: {
                y: {
                    lines: [{
                        value: 0
                    }]
                }
            },
            axis: {
                x: {
                    type: 'category',
                    categories: ['JAN', 'FEV', 'MAR', 'ABR', 'MAI', 'JUN', 'JUL', 'AGO', 'SET', 'OUT', 'NOV', 'DEZ']
                },
                y: {
                    tick: {
                        format: function(value) {
                            if (format) {
                                return value.format(2, 3, '.', ',');
                            } else {
                                return Math.round(value * 100) / 100 + '%';
                            }
                        }
                    }
                }
            }
        };

        if(args.length > 0) {
        	dadosGrafico.data.columns.push(['data4', args[0][0], args[0][1], args[0][2], args[0][3], args[0][4], args[0][5], args[0][6], args[0][7], args[0][8], args[0][9], args[0][10], args[0][11]])
        	dadosGrafico.data.colors.data4 = args[1]
        	dadosGrafico.data.names.data4 = args[2]
        }
        return c3.generate(dadosGrafico);
    }

    function updateChart(chart, listValP, listValR, listExtra, ...listPMO) {

    	let dadosGrafico = {
    		columns: [
				['data1', listValP[0], listValP[1], listValP[2], listValP[3], listValP[4], listValP[5], listValP[6], listValP[7], listValP[8], listValP[9], listValP[10], listValP[11]],
				['data2', listValR[0], listValR[1], listValR[2], listValR[3], listValR[4], listValR[5], listValR[6], listValR[7], listValR[8], listValR[9], listValR[10], listValR[11]],
				['data3', listExtra[0], listExtra[1], listExtra[2], listExtra[3], listExtra[4], listExtra[5], listExtra[6], listExtra[7], listExtra[8], listExtra[9], listExtra[10], listExtra[11]]
			]
    	}

    	if(listPMO.length > 0) {
    		dadosGrafico.columns.push(['data4', listPMO[0][0], listPMO[0][1], listPMO[0][2], listPMO[0][3], listPMO[0][4], listPMO[0][5], listPMO[0][6], listPMO[0][7], listPMO[0][8], listPMO[0][9], listPMO[0][10], listPMO[0][11]])
    	}

        chart.load(dadosGrafico);
    }

    init();

    var divGraficos = document.querySelector('#div-graficos');

    $ctrl.gerarGrafico = function() {

        $ctrl.mostraGrafico = true;

        $ctrl.progress_chart1 = true;
        $ctrl.progress_chart2 = true;
        $ctrl.progress_chart3 = true;
        $ctrl.progress_chart4 = true;

        if ($ctrl.interval) {
            // Cancelar interval
        }

        // -- REST --
        let obj = getDadosParaGerarGrafico();

        GraficoFaturamentoDespesa.gerarGraficoFaturamento(obj).$promise
            .then(function(data) {
                $ctrl.chart = createChart('#chart', data.previsto, data.realizado, data.extra, '#0090a0', '#8f112e', '#bc686d', 'Previsto (último fecho)', 'Realizado (NF emitidas) - Contratual', 'Realizado (NF emitidas) - Extra', true);
                $ctrl.progress_chart1 = false;
                return GraficoFaturamentoDespesa.gerarGraficoReceitaLiquida(obj).$promise;
            })
            .then(function(data) {
                $ctrl.progress_chart2 = false;
                $ctrl.chart2 = createChart('#chart2', data.previsto, data.realizado, data.extra, '#0090a0', '#8f112e', '#bc686d', 'Previsto (NF emitidas s/tributos)', 'Realizado (NF recebidas s/tributos) - Contratual', 'Realizado (NF recebidas s/tributos) - Extra', true);
                return GraficoFaturamentoDespesa.gerarGraficoCustos(obj).$promise;
            })
            .then(function(data) {
                $ctrl.chart3 = createChart('#chart3', data.previsto, data.realizado, data.extra, '#0090a0', '#8f112e', '#bc686d', 'Previsto (último fecho)', 'Realizado (NF, NP, mão de obra) - Contratual', 'Realizado (NF, NP, mão de obra) - Extra', true, data.previsaoMO, '#e60000', 'Previsão de MO');
                $ctrl.progress_chart3 = false;
                return GraficoFaturamentoDespesa.gerarGraficoMargem(obj).$promise;
            })
            .then(function(data) {
                $ctrl.chart4 = createChart('#chart4', data.previsto, data.realizado, data.extra, '#0090a0', '#8f112e', '#bc686d', 'Previsto (último fecho)', 'Realizado (R. líquida-Despesas) - Contratual', 'Realizado (R. líquida-Despesas) - Extra', true, data.previsaoMO, '#e60000', 'Previsão de MO');
                $ctrl.progress_chart4 = false;
            })
            .catch(function(response) {
                $scope.$emit('crud-critical', 'search', response);
            });

		let atualizaTempo = $ctrl.object.tempoMinutos * 60 * 1000;
        if($ctrl.object.tempoMinutos !== 0) {
        	$ctrl.interval = $interval(function() {
				atualizarGraficos();
			}, atualizaTempo);
        };
    };

    function atualizarGraficos() {
        return new Promise((resolve, reject) => {
            $ctrl.progress_chart1 = true;

            let obj = getDadosParaGerarGrafico();

            GraficoFaturamentoDespesa.gerarGraficoFaturamento(obj).$promise
                .then(function(data) {
                    updateChart($ctrl.chart, data.previsto, data.realizado, data.extra);
                    $ctrl.progress_chart1 = false;
                    $ctrl.progress_chart2 = true;
                    return GraficoFaturamentoDespesa.gerarGraficoReceitaLiquida(obj).$promise;
                })
                .then(function(data) {
                    updateChart($ctrl.chart2, data.previsto, data.realizado, data.extra);
                    $ctrl.progress_chart2 = false;
                    $ctrl.progress_chart3 = true;
                    return GraficoFaturamentoDespesa.gerarGraficoCustos(obj).$promise;
                })
                .then(function(data) {
                    updateChart($ctrl.chart3, data.previsto, data.realizado, data.extra, data.previsaoMO);
                    $ctrl.progress_chart3 = false;
                    $ctrl.progress_chart4 = true;
                    return GraficoFaturamentoDespesa.gerarGraficoMargem(obj).$promise;
                })
                .then(function(data) {
                    $ctrl.progress_chart4 = false;
                    updateChart($ctrl.chart4, data.previsto, data.realizado, data.extra, data.previsaoMO);
                })
                .then(() => resolve(true))
                .catch(errorCatch);
        });
    }

    $ctrl.fullscreenCharts = function() {
        if (Fullscreen.isEnabled()) {
            Fullscreen.cancel();
        } else {
        	//Fullscreen.enable(divGraficos);
			/*	A partir das versões 71 do chromium a função usada pela biblioteca angular Fullscreen nao funciona
				pois usa uma função que foi descontinuada na api webkitRequestFullscreen.
				para reverter o problema seria necessário deixar de usar a biblioteca Fullscreen e usar a api nativa do
				browser, requestFullscreen()
				https://github.com/ghys/habpanel/commit/4a20adb29f69dd33792682b0b12608391b9cb5f2
			*/
			divGraficos.requestFullscreen()
        }
    };

    $ctrl.refresh = function() {
    	setTimeout(function() {
    		atualizarGraficos()
    	}, 1000)
    }

    // Pegando o evento do esc
    $(document).keyup(function(e) {
        if (e.keyCode === 27 || e.keyCode === 13) {
            delete $ctrl.mostraGrafico;
            $interval.cancel($ctrl.interval);
        }
    });

    function getDadosParaGerarGrafico() {

        // GraficoFaturamentoDespesasTO
        let dados = {
            ano: null,
            listaCodEmpresa: [],
            codigoRegional: null,
            codigoGerencia: null,
            codigoCl: null
        };

        if ($ctrl.object.ano) {
            dados.ano = $ctrl.object.ano;
        }

        if ($ctrl.object.unidade && $ctrl.object.unidade.length > 0) {
            dados.listaCodEmpresa = $ctrl.object.unidade;
        }

        if ($ctrl.object.regional && $ctrl.object.regional.codigo) {
            dados.codigoRegional = $ctrl.object.regional.codigo;
        }

        if ($ctrl.object.gerencia && $ctrl.object.gerencia.codigo) {
            dados.codigoGerencia = $ctrl.object.gerencia.codigo;
        }

        if ($ctrl.object.cl && $ctrl.object.cl.codigo) {
            dados.codigoCl = $ctrl.object.cl.codigo;
        }

        return dados;
    }

}

/**
 * Number.prototype.format(n, x, s, c)
 *
 * @param integer n: length of decimal
 * @param integer x: length of whole part
 * @param mixed   s: sections delimiter
 * @param mixed   c: decimal delimiter
 */
Number.prototype.format = function(n, x, s, c) {
    var re = '\\d(?=(\\d{' + (x || 3) + '})+' + (n > 0 ? '\\D' : '$') + ')',
        num = this.toFixed(Math.max(0, ~~n));
    return (c ? num.replace('.', c) : num).replace(new RegExp(re, 'g'), '$&' + (s || ','));
};
