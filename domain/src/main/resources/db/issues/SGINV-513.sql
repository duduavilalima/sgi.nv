/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2017-05-08 18:13                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Modify table "historico_conciliacao"                                   */
/* ---------------------------------------------------------------------- */
/* Drop constraints */
ALTER TABLE public.historico_conciliacao
	DROP CONSTRAINT ck_historico_conciliacao_tipo_acao;

/* Create new constraints */
ALTER TABLE historico_conciliacao
	ADD CONSTRAINT ck_historico_conciliacao_tipo_acao 
    CHECK (tipo_acao = ANY (ARRAY[0, 1, 2, 3, 4, 5]));

/* Comments */
COMMENT ON COLUMN historico_conciliacao.tipo_acao IS 'Tipo de ação para lançamentos com o histórico da conciliação bancária: 0 = Nenhuma ação / 1 = Lançamento de tesouraria / 2 = Pagamento / 3 = Pagamento (cheque) / 4 = NF/Saída (faturamento) / 5 = Outros recebimentos.';
