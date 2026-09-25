/* ---------------------------------------------------------------------- */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-05-16 11:44                                */
/* ---------------------------------------------------------------------- */

ALTER TABLE public.faturamento
	ALTER COLUMN codigo_cidade_prestacao_servicos DROP NOT NULL;