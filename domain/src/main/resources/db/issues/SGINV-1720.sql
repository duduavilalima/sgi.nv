/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2018-06-19 15:40                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Criar novo "sistema"                                                   */
/* ---------------------------------------------------------------------- */
INSERT INTO sistema (codigo, nome) VALUES (2, 'Sistema de Gestão Integrada (V2)');

/* ---------------------------------------------------------------------- */
/* Ajustar atuais "modulo"                                                */
/* ---------------------------------------------------------------------- */
UPDATE modulo SET codigo_sistema = 1;

/* ---------------------------------------------------------------------- */
/* Incluir novos "modulo"                                                 */
/* ---------------------------------------------------------------------- */
/*
DELETE	FROM	funcionalidade
		USING	modulo
		WHERE	codigo_modulo = modulo.codigo
		AND		modulo.codigo_sistema = 2;
*/

/* TESOURARIA */
INSERT	INTO modulo	(codigo, nome, ordem, codigo_modulo_pai, codigo_sistema, contexto, status)
		VALUES		(NEXTVAL('seq_modulo'), 'Tesouraria', 10, NULL, 2, 'icon-bank', 1);

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Movimento autorizado', 10, 'movimentoautorizado.list', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Tesouraria'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Movimento autorizado (boletos)', 20, 'movimentoautorizadoboletos.list', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Tesouraria'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Relação bancária', 30, 'relacaobancaria.list', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Tesouraria'
		AND			m2.codigo_sistema = 2;

INSERT	INTO modulo	(codigo, nome, ordem, codigo_modulo_pai, codigo_sistema, contexto, status)
		SELECT		NEXTVAL('seq_modulo'), 'Conciliação bancária', 40, m2.codigo, 2, NULL, 1
		FROM		modulo m2
		WHERE		m2.nome = 'Tesouraria'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Importação', 10, 'importarconciliacao.importar', m3.codigo
		FROM		modulo m2, modulo m3
		WHERE		m2.codigo = m3.codigo_modulo_pai
		AND			m3.nome = 'Conciliação bancária'
		AND			m3.codigo_sistema = 2
		AND			m2.nome = 'Tesouraria'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Lançamentos', 50, 'lancamentos.list', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Tesouraria'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Relatórios', 60, 'relatorios.list', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Tesouraria'
		AND			m2.codigo_sistema = 2;

INSERT	INTO modulo	(codigo, nome, ordem, codigo_modulo_pai, codigo_sistema, contexto, status)
		SELECT		NEXTVAL('seq_modulo'), 'Cadastros', 70, m2.codigo, 2, NULL, 1
		FROM		modulo m2
		WHERE		m2.nome = 'Tesouraria'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Conta corrente', 20, 'conta_corrente.list', m3.codigo
		FROM		modulo m2, modulo m3
		WHERE		m2.codigo = m3.codigo_modulo_pai
		AND			m3.nome = 'Cadastros'
		AND			m3.codigo_sistema = 2
		AND			m2.nome = 'Tesouraria'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Histórico de conciliação bancária', 30, 'historico_conciliacao_bancaria.list', m3.codigo
		FROM		modulo m2, modulo m3
		WHERE		m2.codigo = m3.codigo_modulo_pai
		AND			m3.nome = 'Cadastros'
		AND			m3.codigo_sistema = 2
		AND			m2.nome = 'Tesouraria'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Banco', 10, 'banco.list', m3.codigo
		FROM		modulo m2, modulo m3
		WHERE		m2.codigo = m3.codigo_modulo_pai
		AND			m3.nome = 'Cadastros'
		AND			m3.codigo_sistema = 2
		AND			m2.nome = 'Tesouraria'
		AND			m2.codigo_sistema = 2;

INSERT	INTO permissao (codigo, acao, alvo, codigo_receptor)
		SELECT		NEXTVAL('seq_permissao'), 'render', f.url, p.codigo
		FROM		perfil p, funcionalidade f, modulo m
		LEFT JOIN	modulo pai
		ON			m.codigo_modulo_pai = pai.codigo
		WHERE		f.codigo_modulo = m.codigo
		AND			(m.nome = 'Tesouraria' OR pai.nome = 'Tesouraria')
		AND			m.codigo_sistema = 2
		AND			p.nome = 'operador-tesouraria';

INSERT	INTO permissao (codigo, acao, alvo, codigo_receptor)
		SELECT		NEXTVAL('seq_permissao'), 'render', f.url, p.codigo
		FROM		perfil p, funcionalidade f, modulo m
		WHERE		p.nome = 'financeiro-diretor'
		AND         m.nome = 'Tesouraria'
		AND			f.codigo_modulo = m.codigo
		AND			m.codigo_sistema = 2
		AND			f.nome = 'Relatórios';

/* COMERCIAL */
INSERT	INTO modulo	(codigo, nome, ordem, codigo_modulo_pai, codigo_sistema, contexto, status)
		VALUES		(NEXTVAL('seq_modulo'), 'Comercial', 20, NULL, 2, 'icon-cash-usd', 1);

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Contratos', 10, 'contratos.list', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Comercial'
		AND			m2.codigo_sistema = 2;

INSERT	INTO modulo	(codigo, nome, ordem, codigo_modulo_pai, codigo_sistema, contexto, status)
		SELECT		NEXTVAL('seq_modulo'), 'Cadastros', 20, m2.codigo, 2, NULL, 1
		FROM		modulo m2
		WHERE		m2.nome = 'Comercial'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Clientes', 10, 'clientes.list', m3.codigo
		FROM		modulo m2, modulo m3
		WHERE		m2.codigo = m3.codigo_modulo_pai
		AND			m3.nome = 'Cadastros'
		AND			m3.codigo_sistema = 2
		AND			m2.nome = 'Comercial'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Centros financeiros', 20, 'centrosfinanceiros.list', m3.codigo
		FROM		modulo m2, modulo m3
		WHERE		m2.codigo = m3.codigo_modulo_pai
		AND			m3.nome = 'Cadastros'
		AND			m3.codigo_sistema = 2
		AND			m2.nome = 'Comercial'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Categorias de contrato', 30, 'categoriacontrato.list', m3.codigo
		FROM		modulo m2, modulo m3
		WHERE		m2.codigo = m3.codigo_modulo_pai
		AND			m3.nome = 'Cadastros'
		AND			m3.codigo_sistema = 2
		AND			m2.nome = 'Comercial'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Formas de contratação', 40, 'formacontratacao.list', m3.codigo
		FROM		modulo m2, modulo m3
		WHERE		m2.codigo = m3.codigo_modulo_pai
		AND			m3.nome = 'Cadastros'
		AND			m3.codigo_sistema = 2
		AND			m2.nome = 'Comercial'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Obras', 50, 'obras.list', m3.codigo
		FROM		modulo m2, modulo m3
		WHERE		m2.codigo = m3.codigo_modulo_pai
		AND			m3.nome = 'Cadastros'
		AND			m3.codigo_sistema = 2
		AND			m2.nome = 'Comercial'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Relacionamentos de contato', 60, 'relacionamentocontato.list', m3.codigo
		FROM		modulo m2, modulo m3
		WHERE		m2.codigo = m3.codigo_modulo_pai
		AND			m3.nome = 'Cadastros'
		AND			m3.codigo_sistema = 2
		AND			m2.nome = 'Comercial'
		AND			m2.codigo_sistema = 2;


INSERT	INTO permissao (codigo, acao, alvo, codigo_receptor)
		SELECT		NEXTVAL('seq_permissao'), 'render', f.url, p.codigo
		FROM		perfil p, funcionalidade f, modulo m
		LEFT JOIN	modulo pai
		ON			m.codigo_modulo_pai = pai.codigo
		WHERE		f.codigo_modulo = m.codigo
		AND			(m.nome = 'Comercial' OR pai.nome = 'Comercial')
		AND			m.codigo_sistema = 2
		AND			p.nome = 'admin-contrato';

INSERT	INTO permissao (codigo, acao, alvo, codigo_receptor)
		SELECT		NEXTVAL('seq_permissao'), 'render', f.url, p.codigo
		FROM		perfil p, funcionalidade f, modulo m
		WHERE		f.codigo_modulo = m.codigo
		AND			f.nome = 'Contratos'
		AND			m.codigo_sistema = 2
		AND			m.nome = 'Comercial'
		AND			p.nome IN ('aprov-contrato-com', 'aprov-contrato-cont', 'aprov-contrato-dir', 'aprov-contrato-fin', 'aprov-contrato-ger', 'aprov-contrato-pres', 'aprov-contrato-rh', 'faturamento-analista');

INSERT	INTO permissao (codigo, acao, alvo, codigo_receptor)
		SELECT		NEXTVAL('seq_permissao'), 'render', f.url, p.codigo
		FROM		perfil p, funcionalidade f, modulo m, modulo pai
		WHERE		f.codigo_modulo = m.codigo
		AND			m.codigo_modulo_pai = pai.codigo
		AND			f.nome = 'Centros financeiros'
		AND			m.codigo_sistema = 2
		AND			pai.nome = 'Comercial'
		AND			p.nome IN ('analista-cf', 'aprov-contrato-com', 'aprov-contrato-cont', 'aprov-contrato-dir', 'aprov-contrato-fin', 'aprov-contrato-ger', 'aprov-contrato-pres', 'aprov-contrato-rh', 'faturamento-analista');

INSERT	INTO permissao (codigo, acao, alvo, codigo_receptor)
		SELECT		NEXTVAL('seq_permissao'), 'render', f.url, p.codigo
		FROM		perfil p, funcionalidade f, modulo m, modulo pai
		WHERE		f.codigo_modulo = m.codigo
		AND			m.codigo_modulo_pai = pai.codigo
		AND			f.nome = 'Clientes'
		AND			m.codigo_sistema = 2
		AND			pai.nome = 'Comercial'
		AND			p.nome = 'faturamento-analista';

INSERT	INTO permissao (codigo, acao, alvo, codigo_receptor)
		SELECT		NEXTVAL('seq_permissao'), 'render', f.url, p.codigo
		FROM		perfil p, funcionalidade f, modulo m
		LEFT JOIN	modulo pai
		ON			m.codigo_modulo_pai = pai.codigo
		WHERE		f.codigo_modulo = m.codigo
		AND			(m.nome = 'Comercial' OR pai.nome = 'Comercial')
		AND			m.codigo_sistema = 2
		AND			p.nome = 'consultor-terceiro';

/* FATURAMENTO */
INSERT	INTO modulo	(codigo, nome, ordem, codigo_modulo_pai, codigo_sistema, contexto, status)
		VALUES		(NEXTVAL('seq_modulo'), 'Faturamento', 30, NULL, 2, 'icon-chart-line', 1);

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Faturamentos', 10, 'faturamento.listar', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Faturamento'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Etapas não faturadas', 20, 'etapasnaofaturadas.listar', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Faturamento'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Notas fiscais', 30, 'notafiscal.listar', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Faturamento'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Interfaces', 40, 'interfaces.listar', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Faturamento'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Relatórios', 50, 'faturamento.relatorios', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Faturamento'
		AND			m2.codigo_sistema = 2;

INSERT	INTO modulo	(codigo, nome, ordem, codigo_modulo_pai, codigo_sistema, contexto, status)
		SELECT		NEXTVAL('seq_modulo'), 'Cadastros', 60, m2.codigo, 2, NULL, 1
		FROM		modulo m2
		WHERE		m2.nome = 'Faturamento'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Tipos de serviço', 10, 'tiposServico.list', m3.codigo
		FROM		modulo m2, modulo m3
		WHERE		m2.codigo = m3.codigo_modulo_pai
		AND			m3.nome = 'Cadastros'
		AND			m3.codigo_sistema = 2
		AND			m2.nome = 'Faturamento'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Previsão de faturamento', 20, 'previsaoFaturamento.filtro', m3.codigo
		FROM		modulo m2, modulo m3
		WHERE		m2.codigo = m3.codigo_modulo_pai
		AND			m3.nome = 'Cadastros'
		AND			m3.codigo_sistema = 2
		AND			m2.nome = 'Faturamento'
		AND			m2.codigo_sistema = 2;

INSERT	INTO permissao (codigo, acao, alvo, codigo_receptor)
		SELECT		NEXTVAL('seq_permissao'), 'render', f.url, p.codigo
		FROM		perfil p, funcionalidade f, modulo m
		LEFT JOIN	modulo pai
		ON			m.codigo_modulo_pai = pai.codigo
		WHERE		f.codigo_modulo = m.codigo
		AND			(m.nome = 'Faturamento' OR pai.nome = 'Faturamento')
		AND			m.codigo_sistema = 2
		AND			p.nome IN ('faturamento-analista', 'controlador-fiscal');


INSERT	INTO permissao (codigo, acao, alvo, codigo_receptor)
		SELECT		NEXTVAL('seq_permissao'), 'render', f.url, p.codigo
		FROM		perfil p, funcionalidade f, modulo m
		LEFT JOIN	modulo pai
		ON			m.codigo_modulo_pai = pai.codigo
		WHERE		f.codigo_modulo = m.codigo
		AND			f.nome = 'Faturamentos'
		AND			(m.nome = 'Faturamento')
		AND			m.codigo_sistema = 2
		AND			p.nome IN ('faturamento-analista');

INSERT	INTO permissao (codigo, acao, alvo, codigo_receptor)
		SELECT		NEXTVAL('seq_permissao'), 'render', f.url, p.codigo
		FROM		perfil p, funcionalidade f, modulo m
		LEFT JOIN	modulo pai
		ON			m.codigo_modulo_pai = pai.codigo
		WHERE		f.codigo_modulo = m.codigo
		AND			f.nome = 'Relatórios'
		AND			(m.nome = 'Faturamento')
		AND			m.codigo_sistema = 2
		AND			p.nome IN ('faturamento-analista');



/* CONTAS A RECEBER */
INSERT	INTO modulo	(codigo, nome, ordem, codigo_modulo_pai, codigo_sistema, contexto, status)
		VALUES		(NEXTVAL('seq_modulo'), 'Contas a Receber', 40, NULL, 2, 'icon-currency-usd', 1);

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Parcelas', 10, 'contasareceber.listar', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Contas a Receber'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Relatórios', 20, 'contasareceber.relatorios', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Contas a Receber'
		AND			m2.codigo_sistema = 2;

INSERT	INTO permissao (codigo, acao, alvo, codigo_receptor)
		SELECT		NEXTVAL('seq_permissao'), 'render', f.url, p.codigo
		FROM		perfil p, funcionalidade f, modulo m
		LEFT JOIN	modulo pai
		ON			m.codigo_modulo_pai = pai.codigo
		WHERE		f.codigo_modulo = m.codigo
		AND			(m.nome = 'Contas a Receber' OR pai.nome = 'Contas a Receber')
		AND			m.codigo_sistema = 2
		AND			p.nome IN ('faturamento-analista', 'controlador-fiscal');

/* ADMINISTRAÇÃO */
INSERT	INTO modulo	(codigo, nome, ordem, codigo_modulo_pai, codigo_sistema, contexto, status)
		VALUES		(NEXTVAL('seq_modulo'), 'Administração', 50, NULL, 2, 'icon-key-variant', 1);

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Funcionalidades', 10, 'funcionalidades.list', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Administração'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Usuários', 20, 'usuario.listar', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Administração'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Perfis', 30, 'perfil.listar', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Administração'
		AND			m2.codigo_sistema = 2;

INSERT	INTO permissao (codigo, acao, alvo, codigo_receptor)
		SELECT		NEXTVAL('seq_permissao'), 'render', f.url, p.codigo
		FROM		perfil p, funcionalidade f, modulo m
		LEFT JOIN	modulo pai
		ON			m.codigo_modulo_pai = pai.codigo
		WHERE		f.codigo_modulo = m.codigo
		AND			(m.nome = 'Administração' OR pai.nome = 'Administração')
		AND			m.codigo_sistema = 2
		AND			p.nome IN ('admin-usuario');


/* FLUXO DE CAIXA */
INSERT	INTO modulo	(codigo, nome, ordem, codigo_modulo_pai, codigo_sistema, contexto, status)
		VALUES		(NEXTVAL('seq_modulo'), 'Fluxo de Caixa', 60, NULL, 2, 'icon-repeat', 1);

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Previsão', 10, 'previsao.list', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Fluxo de Caixa'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Gráfico', 20, 'grafico.graficos', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Fluxo de Caixa'
		AND			m2.codigo_sistema = 2;


INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Relatórios', 30, 'fluxocaixa_relatorios.list', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Fluxo de Caixa'
		AND			m2.codigo_sistema = 2;

INSERT	INTO modulo	(codigo, nome, ordem, codigo_modulo_pai, codigo_sistema, contexto, status)
		SELECT		NEXTVAL('seq_modulo'), 'Cadastros', 40, m2.codigo, 2, NULL, 1
		FROM		modulo m2
		WHERE		m2.nome = 'Fluxo de Caixa'
		AND			m2.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Despesas', 10, 'despesas.list', m3.codigo
		FROM		modulo m2, modulo m3
		WHERE		m2.codigo = m3.codigo_modulo_pai
		AND			m3.nome = 'Cadastros'
		AND			m3.codigo_sistema = 2
		AND			m2.nome = 'Fluxo de Caixa'
		AND			m2.codigo_sistema = 2;

INSERT	INTO permissao (codigo, acao, alvo, codigo_receptor)
		SELECT		NEXTVAL('seq_permissao'), 'render', f.url, p.codigo
		FROM		perfil p, funcionalidade f, modulo m
		LEFT JOIN	modulo pai
		ON			m.codigo_modulo_pai = pai.codigo
		WHERE		f.codigo_modulo = m.codigo
		AND			(m.nome = 'Fluxo de Caixa' OR pai.nome = 'Fluxo de Caixa')
		AND			m.codigo_sistema = 2
		AND			p.nome IN ('operador-tesouraria', 'financeiro-diretor');

INSERT	INTO permissao (codigo, acao, alvo, codigo_receptor)
		SELECT		NEXTVAL('seq_permissao'), 'render', f.url, p.codigo
		FROM		perfil p, funcionalidade f, modulo m
		LEFT JOIN	modulo pai
		ON			m.codigo_modulo_pai = pai.codigo
		WHERE		f.codigo_modulo = m.codigo
		AND			f.nome = 'Previsão'
		AND			(m.nome = 'Fluxo de Caixa' OR pai.nome = 'Fluxo de Caixa')
		AND			m.codigo_sistema = 2
		AND			p.nome IN ('operador-tesouraria');

INSERT	INTO permissao (codigo, acao, alvo, codigo_receptor)
		SELECT		NEXTVAL('seq_permissao'), 'render', f.url, p.codigo
		FROM		perfil p, funcionalidade f, modulo m
		LEFT JOIN	modulo pai
		ON			m.codigo_modulo_pai = pai.codigo
		WHERE		f.codigo_modulo = m.codigo
		AND			f.nome = 'Despesas'
		AND			(m.nome = 'Fluxo de Caixa' OR pai.nome = 'Fluxo de Caixa')
		AND			m.codigo_sistema = 2
		AND			p.nome IN ('operador-tesouraria');



/* CUSTOS */
INSERT	INTO modulo	(codigo, nome, ordem, codigo_modulo_pai, codigo_sistema, contexto, status)
		VALUES		(NEXTVAL('seq_modulo'), 'Custos', 70, NULL, 2, 'icon-margin', 1);


INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Gráfico de resultados', 10, 'custos.graficos', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Custos'
		AND			m2.codigo_sistema = 2;


INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
		SELECT		NEXTVAL('seq_funcionalidade'), 'Gráfico Comparativo Anual', 20, 'custos.comparativos', m2.codigo
		FROM		modulo m2
		WHERE		m2.nome = 'Custos'
		AND			m2.codigo_sistema = 2;


INSERT	INTO permissao (codigo, acao, alvo, codigo_receptor)
		SELECT		NEXTVAL('seq_permissao'), 'render', f.url, p.codigo
		FROM		perfil p, funcionalidade f, modulo m
		LEFT JOIN	modulo pai
		ON			m.codigo_modulo_pai = pai.codigo
		WHERE		f.codigo_modulo = m.codigo
		AND			(m.nome = 'Custos' OR pai.nome = 'Custos')
		AND			m.codigo_sistema = 2
		AND			p.nome IN ('admin');
