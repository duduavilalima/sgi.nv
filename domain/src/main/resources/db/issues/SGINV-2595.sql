/* ---------------------------------------------------------------------- */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2019-11-20 15:30                                */
/* ---------------------------------------------------------------------- */

/* Ajuste na descrição do atual perfil "Relatório especial" */
UPDATE	public.perfil
SET		descricao = '@Relatorio especial (???)'
WHERE	nome = 'relatorio-especial';

/* Criação do novo perfil "Relatório especial (NP)" */
INSERT
INTO	public.receptor(codigo, tipo, criador, data_criacao)
VALUES	(NEXTVAL('seq_receptor'), 0, 'admin', CURRENT_TIMESTAMP);

INSERT
INTO	public.perfil(codigo, tipo_acesso, nome, descricao, obriga_gerencia, obriga_banco)
VALUES	(CURRVAL('seq_receptor'), 0, 'relatorio-especial-np', '@Relatorio especial (NP)', TRUE, FALSE);

INSERT
INTO	public.usuario_perfil(codigo, codigo_usuario, codigo_perfil)
SELECT	NEXTVAL('seq_usuario_perfil'), usuario.codigo, perfil.codigo
FROM	perfil, usuario
WHERE	perfil.nome = 'relatorio-especial-np'
AND		usuario.login IN ('Fred Abreu', 'mwaldeck', 'daniel.oliveira');
