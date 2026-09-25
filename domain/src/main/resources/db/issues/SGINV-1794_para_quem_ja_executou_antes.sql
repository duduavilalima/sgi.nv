/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2018-08-13 10:20                                */
/* ---------------------------------------------------------------------- */
/* Drop constraint */
ALTER TABLE public.nota_saida_parcela_retencao
	DROP CONSTRAINT ck_nota_saida_parcela_retencao_tipo;

/* Update data */
UPDATE nota_saida_parcela_retencao SET tipo = 0 WHERE tipo = 1;
UPDATE nota_saida_parcela_retencao SET tipo = 1 WHERE tipo = 2;

/* Create constraint */
ALTER TABLE public.nota_saida_parcela_retencao
	ADD CONSTRAINT ck_nota_saida_parcela_retencao_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1]));

/* Comments */
COMMENT ON COLUMN public.nota_saida_parcela_retencao.tipo IS 'Tipo de retenção: 0 = Retenção contratual / 1 = Retenção acessória.';
