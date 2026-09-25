/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-07-14 15:15                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Modify table "pessoa_conta_contabil"                                   */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.pessoa_conta_contabil
	DROP CONSTRAINT ck_pessoa_conta_contabil_tipo;

ALTER TABLE public.pessoa_conta_contabil
	ADD CONSTRAINT ck_pessoa_conta_contabil_tipo 
    CHECK (tipo <= 9);

COMMENT ON COLUMN public.pessoa_conta_contabil.tipo IS 'Tipo do relacionamento entre pessoa e conta contábil: 0 = Fornecedor / 1 = Colaborador - Fundo fixo / 2 = Colaborador - Adiantamento / 2 = Colaborador - Reembolso / 4 = Colaborador - Empréstimo / 5 = Cliente - Reembolso / 6 = Faturamento - Contratual / 7 = Faturamento - Extra contratual / 8 = Faturamento - A receber / 9 = Cliente.';

/* ---------------------------------------------------------------------- */
/* Update data                                                            */
/* ---------------------------------------------------------------------- */
UPDATE historico_padrao_contabil SET tipo_especial = 9  WHERE codigo = 135;
UPDATE historico_padrao_contabil SET tipo_especial = 10 WHERE codigo = 136;
UPDATE historico_padrao_contabil SET tipo_especial = 11 WHERE codigo = 138;
UPDATE historico_padrao_contabil SET tipo_especial = 12 WHERE codigo = 43;
UPDATE historico_padrao_contabil SET tipo_especial = 13 WHERE codigo = 129;
