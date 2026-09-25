/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2019-11-13 16:30                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Modify table "fecho"                                                   */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.fecho
	ALTER COLUMN percentual_encargos_sociais TYPE NUMERIC(6,4),
	ALTER COLUMN percentual_reajuste_mao_obra TYPE NUMERIC(6,4),
	ALTER COLUMN percentual_reajuste_gastos_gerais TYPE NUMERIC(6,4),
	ALTER COLUMN percentual_reajuste_materiais TYPE NUMERIC(6,4),
	ALTER COLUMN percentual_reajuste_servicos TYPE NUMERIC(6,4);

/* ---------------------------------------------------------------------- */
/* Modify table "fecho_mao_obra"                                          */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.fecho_mao_obra
	ALTER COLUMN quantidade TYPE NUMERIC(11,4);

/* ---------------------------------------------------------------------- */
/* Modify table "fecho_despesa_geral"                                     */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.fecho_despesa_geral
	ALTER COLUMN quantidade TYPE NUMERIC(11,4);
