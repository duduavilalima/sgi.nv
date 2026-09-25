/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2019-03-20 13:10                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Modify table "nota_tributo_aliquota"                                   */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_tributo_aliquota
	ADD ordem SMALLINT DEFAULT 1;

/* Comments */
COMMENT ON COLUMN public.nota_tributo_aliquota.ordem IS 'Número da parcela a qual o tributo está associado (nem sempre os tributos recebidos de uma parcela de NF/Faturamento estará associado à 1ª parcela, podem ter sido retido em outra(s) parcela(s). Para pagamentos, sempre será 1.';

/* Adjusta data */
UPDATE nota_tributo_aliquota SET ordem = 1;