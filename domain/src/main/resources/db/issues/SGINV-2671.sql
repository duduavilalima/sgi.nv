/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2020-03-09 12:30                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Modify table "etapa_fluxo"                                             */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.etapa_fluxo
	ADD validar_conta_contabil_cliente BOOLEAN DEFAULT false NOT NULL;

COMMENT ON COLUMN public.etapa_fluxo.validar_conta_contabil_cliente IS 'Indica se na etapa haverá verificação de associação entre conta contábil e cliente (true) ou não (false).';
