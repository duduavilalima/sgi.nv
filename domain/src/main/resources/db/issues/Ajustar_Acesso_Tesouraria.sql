/* ---------------------------------------------------------------------- */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2017-02-20 15:15                                */
/* ---------------------------------------------------------------------- */

/* Ajustar controle de acesso das funcionalidades da Tesouraria */
INSERT
INTO	permissao (codigo, acao, alvo, codigo_receptor)
SELECT	NEXTVAL('seq_permissao'), 'render', '/relacoes/editar.xhtml', perfil.codigo
FROM	perfil
WHERE	perfil.nome = 'operador-tesouraria';

INSERT
INTO	permissao (codigo, acao, alvo, codigo_receptor)
SELECT	NEXTVAL('seq_permissao'), 'restore', '/relacoes/editar.xhtml', perfil.codigo
FROM	perfil
WHERE	perfil.nome = 'operador-tesouraria';

INSERT
INTO	permissao (codigo, acao, alvo, codigo_receptor)
SELECT	NEXTVAL('seq_permissao'), 'render', '/relacoes/exibir.xhtml', perfil.codigo
FROM	perfil
WHERE	perfil.nome = 'operador-tesouraria';

INSERT
INTO	permissao (codigo, acao, alvo, codigo_receptor)
SELECT	NEXTVAL('seq_permissao'), 'restore', '/relacoes/exibir.xhtml', perfil.codigo
FROM	perfil
WHERE	perfil.nome = 'operador-tesouraria';
