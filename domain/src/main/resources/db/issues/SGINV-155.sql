/* ---------------------------------------------------------------------- */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-11-14 11:00                                */
/* ---------------------------------------------------------------------- */

/* Aplicação de motivo de retorno */
INSERT
INTO	aplicacao_motivo_retorno (codigo, codigo_perfil, nome, chave, tipo, criador, data_criacao, atualizador, data_atualizacao)
SELECT	NEXTVAL('seq_aplicacao_motivo_retorno'), perfil.codigo, 'Cancelar cheque (Tesouraria)', 'cancelar-cheque', 1, 'admim', CURRENT_TIMESTAMP, NULL, NULL
FROM	perfil
WHERE	perfil.nome = 'operador-tesouraria';
