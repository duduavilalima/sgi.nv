/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-06-08 10:48                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Modify table "nota_saida"                                              */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_saida
	ADD protocolo_entrega CHARACTER VARYING(25);

COMMENT ON COLUMN public.nota_saida.protocolo_entrega IS 'Protocolo (nº, data, nome, etc.) de entrega da NF ao cliente.';
