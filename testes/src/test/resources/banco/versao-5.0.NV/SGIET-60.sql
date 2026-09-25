/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-03-10 11:33                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Modify table "faturamento"                                             */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.faturamento
	ALTER COLUMN discriminacao_central DROP NOT NULL;

/* ---------------------------------------------------------------------- */
/* Modify table "faturamento_etapa"                                       */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.faturamento_etapa
	ALTER COLUMN discriminacao_central DROP NOT NULL;
