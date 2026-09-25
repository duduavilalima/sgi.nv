/* ---------------------------------------------------------------------- */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2019-08-01 16:30                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Criar novo perfil                                                      */
/* ---------------------------------------------------------------------- */
/* Gestor de Faturamento/Contas a Receber */
INSERT
	INTO	receptor (codigo, tipo, criador, data_criacao, atualizador, data_atualizacao)
	VALUES	(nextval('seq_receptor'), 0, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT
	INTO	perfil (codigo, tipo_acesso, nome, descricao, obriga_gerencia, obriga_banco)
	VALUES	(currval('seq_receptor'), 0, 'faturamento-gestor', 'Gestor de Faturamento/Contas a Receber', TRUE, FALSE);

/* Consulta ao Faturamento/Contas a Receber - Todas as gerências (geral) */
INSERT
	INTO	receptor (codigo, tipo, criador, data_criacao, atualizador, data_atualizacao)
	VALUES	(nextval('seq_receptor'), 0, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT
	INTO	perfil (codigo, tipo_acesso, nome, descricao, obriga_gerencia, obriga_banco)
	VALUES	(currval('seq_receptor'), 0, 'faturamento-cns-geral', 'Consulta de Faturamento/Contas a Receber - Geral', TRUE, FALSE);

/* Consulta ao Faturamento/Contas a Receber - Gerências */
INSERT
	INTO	receptor (codigo, tipo, criador, data_criacao, atualizador, data_atualizacao)
	VALUES	(nextval('seq_receptor'), 0, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT
	INTO	perfil (codigo, tipo_acesso, nome, descricao, obriga_gerencia, obriga_banco)
	VALUES	(currval('seq_receptor'), 0, 'faturamento-cns-gerencia', 'Consulta de Faturamento/Contas a Receber - Gerência', TRUE, FALSE);

/* ---------------------------------------------------------------------- */
/* Remover acessos indevidos do perfil "@Consultor"                       */
/* ---------------------------------------------------------------------- */
DELETE
FROM	permissao p
WHERE	p.codigo_receptor = (SELECT codigo FROM perfil WHERE descricao = '@Consultor');

/* ---------------------------------------------------------------------- */
/* Remover acessos indevidos do perfil "Controlador fiscal"               */
/* ---------------------------------------------------------------------- */
DELETE
FROM	permissao p
WHERE	p.codigo_receptor = (SELECT codigo FROM perfil WHERE descricao = 'Controlador fiscal')
AND		p.alvo IN ('faturamento.listar', 'etapasnaofaturadas.listar', 'interfaces.listar', 'tiposServico.list', 'previsaoFaturamento.filtro');

/* ---------------------------------------------------------------------- */
/* Adicionar acessos ao perfil de "Gestor"                                */
/* ---------------------------------------------------------------------- */
INSERT
	INTO	permissao (codigo, acao, alvo, codigo_receptor)
	SELECT	NEXTVAL('seq_permissao'), 'render' , 'faturamento.listar', perfil.codigo
	FROM	perfil
	WHERE	perfil.nome = 'faturamento-gestor';
INSERT
	INTO	permissao (codigo, acao, alvo, codigo_receptor)
	SELECT	NEXTVAL('seq_permissao'), 'render' , 'etapasnaofaturadas.listar', perfil.codigo
	FROM	perfil
	WHERE	perfil.nome = 'faturamento-gestor';
INSERT
	INTO	permissao (codigo, acao, alvo, codigo_receptor)
	SELECT	NEXTVAL('seq_permissao'), 'render' , 'interfaces.listar', perfil.codigo
	FROM	perfil
	WHERE	perfil.nome = 'faturamento-gestor';
INSERT
	INTO	permissao (codigo, acao, alvo, codigo_receptor)
	SELECT	NEXTVAL('seq_permissao'), 'render' , 'tiposServico.list', perfil.codigo
	FROM	perfil
	WHERE	perfil.nome = 'faturamento-gestor';
INSERT
	INTO	permissao (codigo, acao, alvo, codigo_receptor)
	SELECT	NEXTVAL('seq_permissao'), 'render' , 'previsaoFaturamento.filtro', perfil.codigo
	FROM	perfil
	WHERE	perfil.nome = 'faturamento-gestor';

/* ---------------------------------------------------------------------- */
/* Adicionar acessos aos novos perfis e outros                            */
/* ---------------------------------------------------------------------- */
INSERT
	INTO	permissao (codigo, acao, alvo, codigo_receptor)
	SELECT	NEXTVAL('seq_permissao'), 'render' , 'notafiscal.listar', perfil.codigo
	FROM	perfil
	WHERE	perfil.nome IN ('financeiro-diretor', 'contabilidade-conferencia', 'faturamento-gestor', 'faturamento-cns-geral', 'faturamento-cns-gerencia');

INSERT
	INTO	permissao (codigo, acao, alvo, codigo_receptor)
	SELECT	NEXTVAL('seq_permissao'), 'render' , 'faturamento.relatorios', perfil.codigo
	FROM	perfil
	WHERE	perfil.nome IN ('financeiro-diretor', 'contabilidade-conferencia', 'faturamento-gestor', 'faturamento-cns-geral', 'faturamento-cns-gerencia');

INSERT
	INTO	permissao (codigo, acao, alvo, codigo_receptor)
	SELECT	NEXTVAL('seq_permissao'), 'render' , 'contasareceber.listar', perfil.codigo
	FROM	perfil
	WHERE	perfil.nome IN ('financeiro-diretor', 'contabilidade-conferencia', 'faturamento-gestor', 'faturamento-cns-geral', 'faturamento-cns-gerencia');

INSERT
	INTO	permissao (codigo, acao, alvo, codigo_receptor)
	SELECT	NEXTVAL('seq_permissao'), 'render' , 'contasareceber.relatorios', perfil.codigo
	FROM	perfil
	WHERE	perfil.nome IN ('financeiro-diretor', 'contabilidade-conferencia', 'faturamento-gestor', 'faturamento-cns-geral', 'faturamento-cns-gerencia');
