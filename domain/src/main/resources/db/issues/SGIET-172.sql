/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-06-29 13:22                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Modify table "nota_tributo_aliquota"                                   */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_tributo_aliquota
	ADD valor NUMERIC(14,2) DEFAULT 0.00,
	ALTER COLUMN codigo SET DEFAULT nextval('seq_nota_tributo_aliquota');

/* Comments */
COMMENT ON COLUMN public.nota_tributo_aliquota.valor IS 'Valor do tributo (inicialmente só será utilizado no faturamento e contas a receber).';
