/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-02-11 17:49                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Alter table "faturamento"                                              */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.faturamento
	ADD COLUMN data_vencimento_1 DATE,
    ADD COLUMN data_vencimento_2 DATE;
