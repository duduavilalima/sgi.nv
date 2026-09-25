angular.module('SGIApp')

.controller('GridController', ['$scope', 'Resource', 'PessoaJuridica', 'DadosBancarios', 'Pagamento', 'uiGridConstants', '$templateCache', '$mdDialog', '$mdMedia', '$controller', function($scope, Resource, PessoaJuridica, DadosBancarios, Pagamento, uiGridConstants, $templateCache, $mdDialog, $mdMedia, $controller) {
    $controller('SearchController', { $scope: $scope, Resource: Resource});

	/*
	* Tutorial do ui-grid: http://ui-grid.info/docs/#/tutorial
	* Api do ui-grid: http://ui-grid.info/docs/#/api
	*
	* Para declarar utilize como exemplo a tela '/src/main/webapp/static/restrict/partials/faturamento/listar.html'
	* Para que a tela seja redimensionada de acordo com resultado é obrigatório declarar na tag: 'ui-grid-auto-resize class="sgiUiGridPesquisa" ng-style="ajustarAlturaTabela(gridPesquisa)"'
	* A opção de fixar colunas depende da declaração: ui-grid-pinning
	*
	* Algumas opções extras foram inseridas pela FASOLTI as descrições estão abaixo:
	*
	* Para transformar o conteúdo da coluna em um link, basta declarar a propriedade 'funcao' na configuração da coluna
	* com o seguinte padrão: funcao: [nome_funcao, parametro1, parametro2].
	* Exemplos:
	*   - metodo: ['exibir', 'row.entity'], chamará o método 'exibir' passando como parâmetro o objeto da entidade.
	*   - metodo: ['editar', 'row.entity.codigo'], chamará o método 'editar' passando como parâmetro o codigo da entidade.
	*   - metodo: ['abrirJanela', '\'janela\'', true, 10], chamará o método 'abrirJanela' passando os parâmetros string 'janela', o booleano 'true' e o numeral '10'.
	*
	*
	* tooltip - deverá ser declarado na configuração da coluna a propriedade tooltip informando o valor a ser exibido, em forma de array,
	* conforme o padrão a seguir:
	*   - tooltip : ['TEXTO'] . Exibe exatamente o texto informado.
	*   - tooltip : ['field']. Exibe o valor declarado na propriedade 'field' configurada para a coluna.
	*   - tooltip : ['field.outro.campo']. Exibe o valor declarado na propriedade 'outro.campo' da entidade sendo pesquisada. Exemplo: [field.cliente.nome] .
	*   - tooltip : ['field.outro.campo', 'field.mais.um.campo']. Exibe o valor concatenado de todos os valores passados. Exemplo: ['field.centroFinanceiro.identificadorFinal', ' - ', 'field.centroFinanceiro.nome'].
	*
	*
	* tooltip para o cabeçalho da coluna - deverá ser declarado na configuração da coluna a propriedade 'headerTooltip', que habilita o tooltip, junto com a propriedade
	* 'displayName' informando o valor a ser exibido na forma de um texto simples.
	*
	*	- headerTooltip: true,
	*	- displayName: 'Indica se a folha de cheques possui carbono ou será necessário a impressão da cópia do cheque',
	*
	*
	* opções - para exibir a coluna de opções. Incluir na configuração das colunas do grid:
    *    {
    *       name:'Opções', acoes: ['exibir'], width: '5%', pinnedRight: false, enableSorting: false, acoesAdicionais: [lista de ações adicionais]
    *    }
    *   - Esta coluna deverá sempre ser a última.
    *   - Na propriedade 'acoes' será informada a lista de ações, os valores possíveis são:
    *           - ['exibir', 'exibir.objeto', 'editar', 'editar.objeto', 'imprimir', 'imprimir.objeto', 'excluir', 'excluir.objeto'].
    *   - No Controller da tela de pesquisa deverá ser implementado o método da ação, recebendo como parâmetro o código da entidade ou o objeto da representando
    *   a entidade, ou seja, se for declarada a açao 'exibir', o Controller tem que apresentar o método exibir: $scope.exibir = function(codigo) {  }.
    *   Se na declaração for informado a 'exibir.objeto', o Controller tem que apresentar o método 'exibir', recebedo como parâmetro o objeto: $scope.exibir = function(objeto) {  }.
    *   - Para declarar ações adicionais, diferentes das básicas disponibilizadas, deverá ser informada a propriedade 'acoesAdicionais', com a lista de ações. Exemplo:
	*		var acoesAdicionais = [
	*   		{
	*	    		nome : 'download.objeto', label: 'Download', icone : 'icon-eye', tooltip: 'Realizar o download deste lote.'
	*		    },
	*		    {
	*   			nome : 'reprocessar', label: 'Reprocessar', icone : 'icon-check', tooltip: 'Reprocessar este lote contábil.'
	*	    	}
	*		];
	*
	* ordenação: utilizar como exemplo '/src/main/webapp/static/restrict/partials/tesouraria/cadastros/conta_corrente/conta_corrente.listar.html'
	*	A ordenação default da tela deve ser definida pelos parâmetros: $scope.paginationOptions.sortField e $scope.paginationOptions.sortDir.
	*	Para a ordenação clicando nas colunas:
	*	- Na declaração da coluna incluir as propriedades:
	*		- 'enableSorting: true'
	*		- 'sort: [{ direction: ASC ou DESC, field : '<primeiro campo>' }, { direction: ASC ou DESC, field : '<segundo campo>' }]
	*	A primeira propriedade habilita a ordenação pelo clique e a segundo define os parâmetros de ordenação, podendo conter 'n' campos e ordenando na sequência que foi declarada.
	*
	*	Exemplo de ordenção:
	*	{
	*		name: 'Banco',
	*		field: 'banco.nome',
	*		width: '12%',
	*		enableSorting: true,
	*		sort: [{ direction: 'ASC', field : 'banco.nome' }, { direction: 'DESC', field : 'contaCorrente.agencia' }]
	*    }
	*
	*/

	$scope.paginationOptions = {
        pageNumber: 1,              //página atual
        pageSize: 20,               //número de registros por página
        totalPages: 1,              //número total de páginas
        sortField: 'codigo',        //campo na entidade utilizado para ordenação
        sortFieldString: false,     //indica se o campo é do tipo string e se deverá ser ordenado no modo CaseInsensitive
        sortDir: 'asc'              //aponta se a ordenação é ascendente(asc) ou descendente(desc)
      };

	var acoesBasicas = [
		{
			nome : 'exibir', label: 'Exibir', icone : 'icon-eye', tooltip: 'Exibir dados dest'
		},
		{
			nome : 'editar', label: 'Editar', icone : 'icon-check', tooltip: 'Editar dados dest'
		},
        {
            nome : 'imprimir', label: 'Imprimir', icone : 'icon-printer', tooltip: 'Imprimir est'
        },
        {
            nome : 'excluir', label: 'Excluir', icone : 'icon-delete', tooltip: 'Excluir est'
        }
	];

	//Define o template para as células de header da tabela
	var headerTemplate = $templateCache.get('headerCellTemplate.html');

	var defaultCellClass = ' h5 over-flow-hidden ';
	$scope.getDefaultCellClass = function() {
		return defaultCellClass;
	}

	var defaultCellClassToolTip = ' h5 ';
	$scope.getDefaultCellClassToolTip = function() {
		return defaultCellClassToolTip;
	}

    $scope.setRowHeight = function(altura) {
        if (gridBase && gridBase != null) {
            if (angular.isNumber(altura))
                gridBase.rowHeight = altura;
            else
                gridBase.rowHeight = 35;
        }
    }

    var gridBase = {
        enableSorting: true,
        useExternalSorting: true,
        enableGridMenu: false,
        useExternalPagination:false,
        headerRowHeight: 45,
        columnFooterHeight: 45,
        enableHorizontalScrollbar: uiGridConstants.scrollbars.NEVER,
        enableVerticalScrollbar: uiGridConstants.scrollbars.NEVER
    };


    $scope.configuracaoGrid = function(columnDefs, nomeEntidade, conectorGenero) {
        if (!angular.isDefined(nomeEntidade) || nomeEntidade == null)
            nomeEntidade = 'registro';

        gridBase.columnDefs = columnDefs;

		angular.forEach(gridBase.columnDefs, function(coluna) {
			coluna.enableHiding = false;
			coluna.enableColumnMenu = false;
			coluna.enableFiltering = false;
			coluna.headerCellClass = 'text-center';

			if (!angular.isDefined(coluna.enableSorting))
				coluna.enableSorting = false;
			else if (angular.isDefined(coluna.sort)) {
			    coluna.sort.direction = uiGridConstants.DESC;
			}

			if (!angular.isDefined(coluna.sortFieldString))
				coluna.sortFieldString = false;

			if (coluna.cellClass && coluna.cellClass != null)
				coluna.cellClass += $scope.getDefaultCellClass();
			else
				coluna.cellClass = $scope.getDefaultCellClass();

			coluna.headerCellTemplate = headerTemplate;

			var cellId = 'column_{{colRenderIndex + 1}}_result_{{rowRenderIndex + 1}}';

			//Configuração da coluna de opções
			if ( (gridBase.columnDefs[gridBase.columnDefs.length - 1].name == coluna.name)
				&& ( (angular.isDefined(coluna.acoes) && coluna.acoes != null) || (angular.isDefined(coluna.acoesAdicionais) && coluna.acoesAdicionais != null) ) ) {

				var qtdAcoes = 0;
				if (angular.isDefined(coluna.acoes))
					qtdAcoes += coluna.acoes.length;

				if (angular.isDefined(coluna.acoesAdicionais))
					qtdAcoes += coluna.acoesAdicionais.length;

				if (qtdAcoes == 1) {
					var cellTemplateOpcoes = '<section layout="row" style="margin-top: -11px;" layout-sm="column" layout-align="center center" layout-wrap class="opcao_table_list">';

					if (angular.isDefined(coluna.acoes)) {
					    for (i=0; i < acoesBasicas.length; i++) {
					        if (acoesBasicas[i].nome == coluna.acoes[0] || (acoesBasicas[i].nome + '.objeto') == coluna.acoes[0]) {
                                var indice = coluna.acoes[0].indexOf('.objeto');
                                var parametro = (indice > 0) ? 'row.entity' : 'row.entity.codigo';
                                var nome = (indice > 0) ? coluna.acoes[0].substring(0, indice) : coluna.acoes[0];

							    cellTemplateOpcoes += '' +
						            '<md-button class="md-icon-button" id="btn_'+ nome +'_result_{{rowRenderIndex + 1}}" ng-click="grid.appScope.'+ nome +'('+ parametro +')" ng-show="grid.appScope.exibirAcao != null ? grid.appScope.exibirAcao('+ "'" + nome + "'" + ', row) : true">' +
										'<md-icon md-font-icon="'+ acoesBasicas[i].icone +'" class="s20"></md-icon>' +
										'<md-tooltip id="btn_'+ nome +'_result_{{rowRenderIndex + 1}}_tool_tip" md-direction="bottom">' +
										'	<span class="'+ $scope.getDefaultCellClassToolTip() +'">'+ acoesBasicas[i].tooltip + conectorGenero + ' ' + nomeEntidade +'</span>' +
										'</md-tooltip>' +
						            '</md-button>' ;
				            }
						}
					}
					else {
	                    var indice = coluna.acoesAdicionais[0].nome.indexOf('.objeto');
	                    var parametro = (indice > 0) ? 'row.entity' : 'row.entity.codigo';
	                    var nome = (indice > 0) ? coluna.acoesAdicionais[0].nome.substring(0, indice) : coluna.acoesAdicionais[0].nome;

					    cellTemplateOpcoes += '' +
                            '<md-button class="md-icon-button" id="btn_'+ nome +'_result_{{rowRenderIndex + 1}}" ng-click="grid.appScope.'+ nome +'('+ parametro +')" ng-show="grid.appScope.exibirAcao != null ? grid.appScope.exibirAcao('+ "'" + nome + "'" + ', row) : true">' +
                                '<md-icon md-font-icon="'+ coluna.acoesAdicionais[0].icone +'" class="s20"></md-icon>' +
                                '<md-tooltip id="btn_'+ nome +'_result_{{rowRenderIndex + 1}}_tool_tip" md-direction="bottom">' +
                                '	<span class="'+ $scope.getDefaultCellClassToolTip() +'">'+ coluna.acoesAdicionais[0].tooltip +'</span>' +
                                '</md-tooltip>' +
                            '</md-button>' ;
					}

					coluna.cellTemplate = cellTemplateOpcoes + '</section>';
				}
				else {
					var cellTemplateOpcoes = '' +
						'<section layout="row" layout-sm="column" layout-align="center center" layout-wrap class="opcao_table_list" style="margin-top: -11px;">' +
							'<md-menu>' +
							'	<md-button id="btn_menu_result_{{rowRenderIndex + 1}}" aria-label="Menu opções" class="md-icon-button" ng-click="$mdMenu.open($event)">' +
							'       <md-icon md-font-icon="icon-dots-horizontal" class="s24"></md-icon>' +
							'       <md-tooltip id="btn_menu_result_{{rowRenderIndex + 1}}_tool_tip" md-direction="bottom">' +
							'           <span class="'+ $scope.getDefaultCellClassToolTip() +'">Clique para ver o menu de opções</span>' +
							'       </md-tooltip>' +
	                        '   </md-button>' +
	                        '   <md-menu-content width="4">';

					if (angular.isDefined(coluna.acoes)) {
						for(i=0; i < coluna.acoes.length; i++) {
							for (j=0; j < acoesBasicas.length; j++) {
	                            if (acoesBasicas[j].nome == coluna.acoes[i] || (acoesBasicas[j].nome + '.objeto') == coluna.acoes[i]) {
	                                var indice = coluna.acoes[i].indexOf('.objeto');
	                                var parametro = (indice > 0) ? 'row.entity' : 'row.entity.codigo';
	                                var nome = (indice > 0) ? coluna.acoes[i].substring(0, indice) : coluna.acoes[i];

	                                cellTemplateOpcoes += '' +
	                                        '<md-menu-item ng-show="grid.appScope.exibirAcao != null ? grid.appScope.exibirAcao('+ "'" + nome + "'" + ', row) : true">' +
	                                        '   <md-button id="btn_menu_'+ nome +'_result_{{rowRenderIndex + 1}}" ng-click="grid.appScope.'+ nome +'('+ parametro +')">' +
	                                        '       <md-icon md-font-icon="'+ acoesBasicas[j].icone +'" class="s20"></md-icon>' +
	                                        '           <span class='+ $scope.getDefaultCellClass() +'>'+ acoesBasicas[j].label +'</span>' +
	                                        '       <md-tooltip id="btn_menu_'+ nome +'_result_{{rowRenderIndex + 1}}_tool_tip" md-direction="bottom">' +
	                                        '           <span class="'+ $scope.getDefaultCellClassToolTip() +'">'+ acoesBasicas[j].tooltip + conectorGenero + ' ' + nomeEntidade +'</span>' +
	                                        '       </md-tooltip>' +
	                                        '   </md-button>' +
	                                        '</md-menu-item>';
	                            }
							}
						}
					}

					if (angular.isDefined(coluna.acoesAdicionais)) {
						for(j=0; j < coluna.acoesAdicionais.length; j++) {
                            var indice = coluna.acoesAdicionais[j].nome.indexOf('.objeto');
                            var parametro = (indice > 0) ? 'row.entity' : 'row.entity.codigo';
                            var nome = (indice > 0) ? coluna.acoesAdicionais[j].nome.substring(0, indice) : coluna.acoesAdicionais[j].nome;

	                        cellTemplateOpcoes += '' +
	                                '<md-menu-item ng-show="grid.appScope.exibirAcao != null ? grid.appScope.exibirAcao('+ "'" + nome + "'" + ', row) : true">' +
	                                '   <md-button id="btn_menu_'+ nome +'_result_{{rowRenderIndex + 1}}" ng-click="grid.appScope.'+ nome +'('+ parametro +')">' +
	                                '       <md-icon md-font-icon="'+ coluna.acoesAdicionais[j].icone +'" class="s20"></md-icon>' +
	                                '           <span class='+ $scope.getDefaultCellClass() +'>'+ coluna.acoesAdicionais[j].label +'</span>' +
	                                '       <md-tooltip id="btn_menu_'+ nome +'_result_{{rowRenderIndex + 1}}_tool_tip" md-direction="bottom">' +
	                                '           <span class="'+ $scope.getDefaultCellClassToolTip() +'">'+ coluna.acoesAdicionais[j].tooltip + '</span>' +
	                                '       </md-tooltip>' +
	                                '   </md-button>' +
	                                '</md-menu-item>';
						}
					}

	                coluna.cellTemplate = cellTemplateOpcoes +
							'    </md-menu-content>' +
							'</md-menu>' +
						'</section>';

				}
			}
			//configuração das demais colunas
			else {
				var valorTooltip = '';
				var valorCampo = '';
				var tooltip = '';
				var mouseOverPessoa = '';
				var mouseOverContas = '';
				var mouseOverPopup = '';

				if (angular.isDefined(coluna.cellFilter) && coluna.cellFilter != null) {
					valorCampo = '{{row.entity.' + coluna.field + ' | ' + coluna.cellFilter + '}}'
				}
				else {
					valorCampo = '{{row.entity.' + coluna.field + '}}';
				}

				if ( (angular.isDefined(coluna.tooltip) && coluna.tooltip != null) || (angular.isDefined(coluna.tooltipFunction) && coluna.tooltipFunction != null) ) {

				    if (angular.isDefined(coluna.tooltipFunction) && coluna.tooltipFunction != null) {
				    	var tooltipFunction = coluna.tooltipFunction[0] + '(';
                        for(i=1; i < coluna.tooltipFunction.length; i++) {
                            tooltipFunction += (i==1) ? coluna.tooltipFunction[i] : (',' + coluna.tooltipFunction[i]);
                        }
                        tooltipFunction += ')';

                        valorTooltip = '<span>{{grid.appScope.'+ tooltipFunction +'}}</span>';
				    }
                    else {
                        for (i=0; i < coluna.tooltip.length; i++) {
                            var valorAux = coluna.tooltip[i];
                            if (coluna.tooltip[i] == 'field')
                                valorAux = valorCampo;
                            else if (coluna.tooltip[i].substring(0, 6) == 'field.')
                                valorAux = '{{row.entity.' + coluna.tooltip[i].substring(6) + '}}';

                            valorTooltip += valorAux;
                        }
                    }

                    tooltip = '' +
                            '<md-tooltip id="'+ cellId +'_tool_tip" md-direction="bottom">' +
			                    '	<span class="'+ $scope.getDefaultCellClassToolTip() +'">'+ valorTooltip +'</span>' +
		                    '</md-tooltip>';
				}

				if ( (angular.isDefined(coluna.detalhePessoa) && coluna.detalhePessoa != null) || (angular.isDefined(coluna.detalheCliente) && coluna.detalheCliente != null)) {
				    verificaSubCliente = '';
				    detalhePessoa = '';
				    if (angular.isDefined(coluna.detalheCliente) && coluna.detalheCliente != null) {
				        detalhePessoa = coluna.detalheCliente;
				        verificaSubCliente = 'ng-show="row.entity.tipo == \'Subcliente\'"';
				    }
				    else {
				        detalhePessoa = coluna.detalhePessoa;
				    }

					mouseOverPessoa = ' ' +
					'	<md-button id="btn_info_result_{{rowRenderIndex + 1}}" aria-label="Detalhes pessoa" '+ verificaSubCliente +' style="margin-top: -8px; margin-left: -10px;" class="md-icon-button" ng-click="grid.appScope.exibirDetalhePessoa(row.entity.' + detalhePessoa[0] + ', row.entity.' + detalhePessoa[1] + ', $event)">' +
                    '       <md-icon md-font-icon="icon-information-outline" class="s16"></md-icon>' +
                    '       <md-tooltip id="btn_info_result_{{rowRenderIndex + 1}}_tool_tip_pessoa" md-direction="bottom">' +
                    '           <span class="'+ $scope.getDefaultCellClassToolTip() +'">Clique para ver detalhes</span>' +
                    '       </md-tooltip>' +
                    '   </md-button>';
				}

				if (angular.isDefined(coluna.detalheContas) && coluna.detalheContas != null) {
					mouseOverContas = ' ' +
					'	<md-button id="btn_info_result_{{rowRenderIndex + 1}}" aria-label="Detalhes contas" style="margin-top: -8px; margin-left: -10px;" class="md-icon-button" ng-click="grid.appScope.exibirDetalheContas(row.entity.' + coluna.detalheContas[0] + ', row.entity.' + coluna.detalheContas[1] + ', $event)">' +
                    '       <md-icon md-font-icon="icon-information-outline" class="s16"></md-icon>' +
                    '       <md-tooltip id="btn_info_result_{{rowRenderIndex + 1}}_tool_tip_conta" md-direction="bottom">' +
                    '           <span class="'+ $scope.getDefaultCellClassToolTip() +'">Clique para ver detalhes</span>' +
                    '       </md-tooltip>' +
                    '   </md-button>';
				}

				if (angular.isDefined(coluna.detalhePagamento) && coluna.detalhePagamento != null) {
					mouseOverPagamento = ' ' +
					'	<md-button id="btn_info_result_{{rowRenderIndex + 1}}" aria-label="Detalhes pagamento" style="margin-top: -8px; margin-left: -10px;" class="md-icon-button" ng-click="grid.appScope.exibirDetalhePagamento(row.entity.' + coluna.detalhePagamento[0] + ', row.entity.' + coluna.detalhePagamento[1] + ', row.entity.' + coluna.detalhePagamento[2] + ', $event)">' +
					'       <md-icon md-font-icon="icon-information-outline" class="s16"></md-icon>' +
					'       <md-tooltip id="btn_info_result_{{rowRenderIndex + 1}}_tool_tip_pagamento" md-direction="bottom">' +
					'           <span class="'+ $scope.getDefaultCellClassToolTip() +'">Clique para ver detalhes</span>' +
					'       </md-tooltip>' +
					'   </md-button>';
				}

				if (angular.isDefined(coluna.detalhePopup) && coluna.detalhePopup != null) {
                        mouseOverPopup = ' ' +
                        '	<md-button id="btn_info_result_{{rowRenderIndex + 1}}" aria-label="Detalhes" style="margin-top: -8px; margin-left: -10px;" class="md-icon-button" ng-click="grid.appScope.'+ coluna.detalhePopup[0] +'">' +
                        '       <md-icon md-font-icon="icon-information-outline" class="s16"></md-icon>' +
                        '       <md-tooltip id="btn_info_result_{{rowRenderIndex + 1}}_tool_tip" md-direction="bottom">' +
                        '           <span class="'+ $scope.getDefaultCellClassToolTip() +'">Clique para ver detalhes</span>' +
                        '       </md-tooltip>' +
                        '   </md-button>';
				}

				if (angular.isDefined(coluna.cellTemplate) && coluna.cellTemplate != null)
					coluna.cellTemplate = '<span id="' + cellId + '">'+ coluna.cellTemplate + tooltip + '</span>';
				else {
					if (angular.isDefined(coluna.funcao) && coluna.funcao != null) {
						var funcao = coluna.funcao[0] + '(';
						for(i=1; i < coluna.funcao.length; i++) {
							funcao += (i==1) ? coluna.funcao[i] : (',' + coluna.funcao[i]);
						}
						funcao += ')';

                        var cellTemplateLink = '<span id="' + cellId + '"><a id="' + cellId + '_link" href ng-click="grid.appScope.'+ funcao +'">'+ valorCampo + '</a>' + tooltip  + '</span>';
                        coluna.cellTemplate = cellTemplateLink;
                    }
                    else {
                        coluna.cellTemplate = '<span id="' + cellId + '">'+ valorCampo + tooltip + '</span>';
					}
				}

                if ( (angular.isDefined(coluna.detalhePessoa) && coluna.detalhePessoa != null) || (angular.isDefined(coluna.detalheCliente) && coluna.detalheCliente != null)) {
                    coluna.cellTemplate = '' +
                        '<section layout="row" layout-sm="column">' +
                        '   <div flex="5" style="margin-right: 8px;" id="' + cellId + '_detalhe_pessoa" ng-show="grid.appScope.verificarValorCampo(row.entity.' + coluna.field + ')">'+ mouseOverPessoa +'</div>' +
                        '	<div flex="95" id="' + cellId + '_conteudo_pessoa">'+ coluna.cellTemplate + '</div>' +
                        '</section>';
                }
                else if (angular.isDefined(coluna.detalheContas) && coluna.detalheContas != null) {
                     coluna.cellTemplate = '' +
                         '<section layout="row" layout-sm="column">' +
                         '  <div flex="15" style="margin-right: 8px;" id="' + cellId + '_detalhe_contas" ng-show="grid.appScope.verificarValorCampo(row.entity.' + coluna.field + ')">'+ mouseOverContas +'</div>' +
                         '	<div flex="85" id="' + cellId + '_conteudo_contas">'+ coluna.cellTemplate + '</div>' +
                         '</section>';
                }
				else if (angular.isDefined(coluna.detalhePagamento) && coluna.detalhePagamento != null) {
					 coluna.cellTemplate = '' +
						 '<section layout="row" layout-sm="column">' +
						 '  <div flex="15" style="margin-right: 8px;" id="' + cellId + '_detalhe_pagamento" ng-show="grid.appScope.verificarValorCampo(row.entity.' + coluna.field + ')">'+ mouseOverPagamento +'</div>' +
						 '	<div flex="85" id="' + cellId + '_conteudo_pagamento">'+ coluna.cellTemplate + '</div>' +
						 '</section>';
				}
                else if (angular.isDefined(coluna.detalhePopup) && coluna.detalhePopup != null) {
                    if (coluna.detalhePopup[1] == 'esquerda') {
                        coluna.cellTemplate = '' +
                            '<section layout="row" layout-sm="column">' +
                            '   <div flex="5" id="' + cellId + '_detalhe_contas" ng-show="grid.appScope.verificarValorCampo(row.entity.' + coluna.field + ')">'+ mouseOverPopup +'</div>' +
                            '	<div flex="95" id="' + cellId + '_conteudo_contas">'+ coluna.cellTemplate + '</div>' +
                            '</section>';
                    }
                    else {
                         coluna.cellTemplate = '' +
                            '<section layout="row" layout-sm="column">' +
                            '   <div flex="90" id="' + cellId + '_conteudo_contas">'+ coluna.cellTemplate + '</div>' +
                            '   <div flex="10" id="' + cellId + '_detalhe_contas" ng-show="grid.appScope.verificarValorCampo(row.entity.' + coluna.field + ')">'+ mouseOverPopup +'</div>' +
                            '</section>';
                    }
                }
			}
	    });

	    return gridBase;
    }

    $scope.verificarValorCampo = function(valorCampo) {
        if (valorCampo != null)
            return true;

        return false;
    }

	$scope.ajustarAlturaTabela = function(grid) {
		var rowHeight = grid.rowHeight;
        var headerHeight = grid.headerRowHeight;
        var footerHeight = grid.columnFooterHeight;

        var rodape = 0;
        if ( (angular.isDefined(grid.showColumnFooter) && grid.showColumnFooter) || (angular.isDefined(grid.showGridFooter) && grid.showGridFooter)) {
            rodape = 1;
        }
        else {
            angular.forEach(gridBase.columnDefs, function(coluna) {
                if (angular.isDefined(coluna.showColumnFooter) && coluna.showColumnFooter)
                    rodape = 1;
            });
        }

        var altura = (grid.data.length == 0 ? 1 : grid.data.length)  * (1.5 * rowHeight) + (headerHeight) + (rodape == 0 ? 0 : footerHeight) + 5 ;
        angular.element(document.getElementsByClassName('sgiUiGridPesquisa')[0]).css('height', altura + 'px');

    };

    $scope.exibirGrid = function(grid) {
        if (angular.isDefined(grid) && grid != null) {
            if (grid.totalItems > 0)
                return true;
        }

        return false;
    }

    $scope.mensagemPesquisa = function(entidade) {
        return 'Nenhum resultado encontrado.';
    }

    $scope.getPageGrid = function(grid, page) {
//        console.time('grid');
        var params = {};
        params.sortField = $scope.paginationOptions.sortField;
        params.sortFieldString = $scope.paginationOptions.sortFieldString;
        params.pageSize = $scope.paginationOptions.pageSize;
        params.sortDir = $scope.paginationOptions.sortDir;
        params.pageNumber = page  - 1;

        $scope.paginationOptions.pageNumber = page;

        var resultado = Resource.page(params, $scope.object).$promise;

        resultado.then(function(data) {
            var lista = [];
            angular.forEach(data.content, function(entidade) {
                lista.push(entidade);
            })
			$scope.paginationOptions.totalPages = data.totalPages;
            grid.paginationPageSize = data.pageSize;
            grid.totalItems = data.totalElements;
            grid.data = lista;
            $scope.result = lista;
        }).catch(function(response) {
		  $scope.$emit('crud-critical', 'dialog', response);
        });;
    };

	$scope.exibirProximo = function() {
		if ( ($scope.paginationOptions.pageNumber != $scope.paginationOptions.totalPages) && ($scope.paginationOptions.totalPages > 0))
			return true;

		return false;
	}

	$scope.exibirAnterior = function() {
		if ($scope.paginationOptions.pageNumber > 1)
			return true;

		return false;
	}

    $scope.ordenar = function(col) {
        if (col.sort[0].direction == uiGridConstants.DESC)
            col.sort[0].direction = uiGridConstants.ASC;
        else
            col.sort[0].direction = uiGridConstants.DESC;

		if (col.sort.length == 1) {
        	$scope.paginationOptions.sortField = col.sort[0].field;
        	$scope.paginationOptions.sortDir = col.sort[0].direction;
		}
		else {
			$scope.paginationOptions.sortDir = null;
			var ordenacao = col.sort[0].field + " " + col.sort[0].direction ;
			for(i = 1; i < col.sort.length; i++) {
				if (i == (col.sort.length - 1)) {
					ordenacao += ", " + col.sort[i].field + " ";
					$scope.paginationOptions.sortDir = col.sort[i].direction
				}
				else
					ordenacao += ", " + col.sort[i].field + " " + col.sort[i].direction ;
			}
			$scope.paginationOptions.sortField = ordenacao;
		}

        $scope.getPageGrid(gridBase, 1);

    }

    $scope.definirIcone = function(col) {
        if (!angular.isDefined(col.sort[0].direction))
            return 'icon-menu-down';

        if (col.sort[0].direction == uiGridConstants.DESC)
            return 'icon-menu-down';
        else
            return 'icon-menu-up';
    }

    $scope.alturaHeader = function() {
        if (!angular.isDefined(gridBase.headerRowHeight))
            return 45;

		return gridBase.headerRowHeight;

    }

	$scope.exibirDetalhePessoa = function(codigo, tipo, ev) {
		var useFullScreen = true;

		if (tipo == null)
			tipo = 'ninformado';

		$scope.pessoa = PessoaJuridica.popup({id: codigo, tipo: tipo});

		$mdDialog.show({
			controller: DialogControllerPessoa,
			locals: {pessoa: $scope.pessoa},
			templateUrl: 'js/util/templates/detalhepessoa.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose:true,
			fullscreen: useFullScreen,
			arialabel: 'Detalhe pessoa'
		})
	};

	$scope.exibirDetalheCliente = function(codigo, tipo, ev) {
	    if (codigo == null) {
	        return;
	    }
		var useFullScreen = true;

		if (tipo == null)
			tipo = 'ninformado';

		$scope.pessoa = PessoaJuridica.popup({id: codigo, tipo: tipo});

		$mdDialog.show({
			controller: DialogControllerPessoa,
			locals: {pessoa: $scope.pessoa},
			templateUrl: 'js/util/templates/detalhecliente.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose:true,
			fullscreen: useFullScreen,
			arialabel: 'Detalhe cliente'
		})
	};

	function DialogControllerPessoa($scope, $mdDialog, pessoa) {
	    $scope.pessoa = pessoa;

		$scope.fechar = function() {
			$mdDialog.cancel();
		};
	}

	$scope.exibirDetalheContas = function(codigoPessoa, codigoConta, ev) {
		var useFullScreen = true;

		$scope.listaDadosBancarios = DadosBancarios.popup({id: codigoPessoa});

		$mdDialog.show({
			controller: DialogControllerContas,
			locals: {listaDadosBancarios: $scope.listaDadosBancarios, contaSelecionada: codigoConta},
			templateUrl: 'js/util/templates/detalhecontas.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose:true,
			fullscreen: useFullScreen,
			arialabel: 'Detalhe pessoa'
		})
	};

	function DialogControllerContas($scope, $mdDialog, listaDadosBancarios, contaSelecionada) {
	    $scope.listaDadosBancarios = listaDadosBancarios;
	    $scope.contaSelecionada = contaSelecionada;

		$scope.fechar = function() {
			$mdDialog.cancel();
		};
	}

	$scope.exibirDetalhePagamento = function(codigo, tipo, codNota, ev) {
		var useFullScreen = true;

		if (tipo == null)
			tipo = 'ninformado';

		if(codNota == null){
			codNota = 0;
		}

		$scope.pagamento = Pagamento.popup({id: codigo, tipo: tipo, codNota: codNota});

		$mdDialog.show({
			controller: DialogControllerPagamento,
			locals: {pagamento: $scope.pagamento},
			templateUrl: 'js/util/templates/detalhepagamento.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose:true,
			fullscreen: useFullScreen,
			arialabel: 'Detalhe pagamento'
		})
	};

	function DialogControllerPagamento($scope, $mdDialog, pagamento) {
		$scope.pagamento = pagamento;

		$scope.fechar = function() {
			$mdDialog.cancel();
		};
	}

}])

.filter('formatacaoData', function($filter){
    return function(input) {
       if (angular.isDefined(input))
			return moment.utc(input).utcOffset("Z").format('DD/MM/YYYY');
		else
			return null;
    }
})

.filter('formatacaoMoeda', function($filter){
    return function(input) {
       if (angular.isDefined(input))
			return $filter('currency')(input, '', 2);
		else
			return null;
    }
})

.filter('formatacaoDataFull', function($filter){
    return function(input) {
       if (angular.isDefined(input))
			return $filter('date')(input, 'dd/MM/yyyy HH:mm:ss');
		else
			return null;
    }
})
