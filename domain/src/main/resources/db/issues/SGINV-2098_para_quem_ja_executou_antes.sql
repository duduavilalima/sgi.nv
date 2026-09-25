/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2019-02-28 16:13                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Modify table "nota_saida_parcela_motivo_desc_dev"                      */
/* ---------------------------------------------------------------------- */
/* Add column */
ALTER TABLE public.nota_saida_parcela_motivo_desc_dev
	ADD tipo SMALLINT  NOT NULL;

/* Create constraint */
ALTER TABLE public.nota_saida_parcela_motivo_desc_dev
	ADD CONSTRAINT ck_nota_saida_parcela_motivo_desc_dev_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1]));

/* Comment */
COMMENT ON COLUMN public.nota_saida_parcela_motivo_desc_dev.tipo IS 'Tipo: 0 = desconto / 1 = devolução.';
