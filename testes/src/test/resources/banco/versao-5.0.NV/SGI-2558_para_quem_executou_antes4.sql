/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-02-15 12:00                                */
/* ---------------------------------------------------------------------- */
INSERT
INTO	relacionamento_contato (codigo, codigo_perfil, nome, criador, data_criacao, atualizador, data_atualizacao)
VALUES	(NEXTVAL('seq_relacionamento_contato'), (SELECT codigo FROM perfil WHERE nome = 'faturamento'), 'Faturamento', 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT
INTO	relacionamento_contato (codigo, codigo_perfil, nome, criador, data_criacao, atualizador, data_atualizacao)
VALUES	(NEXTVAL('seq_relacionamento_contato'), (SELECT codigo FROM perfil WHERE nome = 'faturamento'), 'Faturamento (gestor de contrato)', 'admin', CURRENT_TIMESTAMP, NULL, NULL);