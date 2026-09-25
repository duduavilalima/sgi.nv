/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-03-10 16:11                                */
/* ---------------------------------------------------------------------- */

/* Update data */
UPDATE historico_padrao_contabil SET tipo_especial = 9  WHERE codigo = 135;
UPDATE historico_padrao_contabil SET tipo_especial = 10 WHERE codigo = 136;
UPDATE historico_padrao_contabil SET tipo_especial = 11 WHERE codigo = 138;
UPDATE historico_padrao_contabil SET tipo_especial = 12 WHERE codigo = 43;
UPDATE historico_padrao_contabil SET tipo_especial = 13 WHERE codigo = 129;

/* ---------------------------------------------------------------------- */
/* Modify table "lote_interface"                                          */
/* ---------------------------------------------------------------------- */
/* Drop constraint */
ALTER TABLE public.lote_interface
	DROP CONSTRAINT ck_lote_interface_tipo;

/* Update value */
SELECT setval('public.seq_lote_interface', nextval('public.seq_lote_contabil'), false); 

/* Set column default */
ALTER TABLE public.lote_interface
	ALTER COLUMN codigo SET DEFAULT nextval('seq_lote_interface');

/* Add constraint */
ALTER TABLE public.lote_interface
	ADD CONSTRAINT ck_lote_interface_tipo 
    CHECK ((tipo >= 0) AND (tipo <= 17));

/* Comments */
COMMENT ON COLUMN public.lote_interface.codigo IS 'Código interno do lote. Sequência: seq_lote_interface.';
COMMENT ON COLUMN public.lote_interface.tipo IS 'Tipo do lote: 0 - Provisionamento / 1 - Tesouraria / 2 - Custos (pagamentos) / 3 - Novos fornecedores / 4 - Fluxo de caixa (pagamentos) / 5 - NF/Saída / 6 - DIRF (anual) / 7 - FATN / 8 - FATC / 9 - DSFP / 10 - INSS / 11 - SSNI / 12 - IRRF / 13 - Fluxo de caixa (faturamento) / 14 - Custos (faturamento) / 15 - ISS e PIS/COFINS/CSLL (Sistema Fiscal) / 16 - Nota Carioca / 17 - Boletos (Itaú)';

/* Drop sequence */
DROP SEQUENCE seq_lote_contabil;
