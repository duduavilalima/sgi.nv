/* ---------------------------------------------------------------------- */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2020-02-05 17:30                                */
/* ---------------------------------------------------------------------- */

/* Ajuste na descrição do atual perfil "Relatório especial" */
UPDATE	public.perfil
SET		descricao = '@Relatorio especial (NP/NF)'
WHERE	nome = 'relatorio-especial-np';

/* Criação do novo perfil "@Relatório especial (Despesas de CL)" */
INSERT
INTO	public.receptor(codigo, tipo, criador, data_criacao)
VALUES	(NEXTVAL('seq_receptor'), 0, 'admin', CURRENT_TIMESTAMP);

INSERT
INTO	public.perfil(codigo, tipo_acesso, nome, descricao, obriga_gerencia, obriga_banco)
VALUES	(CURRVAL('seq_receptor'), 0, 'relatorio-especial-cl', '@Relatório especial (Despesas de CL)', TRUE, FALSE);

INSERT
INTO	public.usuario_perfil(codigo, codigo_usuario, codigo_perfil)
SELECT	NEXTVAL('seq_usuario_perfil'), usuario.codigo, perfil.codigo
FROM	perfil, usuario
WHERE	perfil.nome = 'relatorio-especial-cl'
AND		usuario.login IN ('Fred Abreu', 'mwaldeck', 'daniel.oliveira');

/* ---------------------------------------------------------------------- */
/* Add "parametro"                                                        */
/* ---------------------------------------------------------------------- */
INSERT INTO parametro (chave, valor) VALUES ('job.notas.cadastradas.crontab'	, '0 0 2 ? * 3-7');
INSERT INTO parametro (chave, valor) VALUES ('job.despesas.cl.crontab'			, '0 0 2 ? * 3-7');
INSERT INTO parametro (chave, valor) VALUES ('job.despesas.cl.limite'			, '80');
