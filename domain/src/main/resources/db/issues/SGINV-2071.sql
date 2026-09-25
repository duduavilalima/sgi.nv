/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2019-02-14 17:00                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Modify table "lote_interface_nota"                                     */
/* ---------------------------------------------------------------------- */
/* Drop constraint  */
ALTER TABLE public.lote_interface_nota
	DROP CONSTRAINT ck_lote_interface_nota_tipo;

/* Create constraint  */
ALTER TABLE public.lote_interface_nota
	ADD CONSTRAINT ck_lote_interface_nota_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1, 2]));

/* Comments */
COMMENT ON COLUMN public.lote_interface_nota.codigo					IS 'Código interno do relacionamento entre lote e nota. Sequência: seq_lote_interface_nota.';
COMMENT ON COLUMN public.lote_interface_nota.codigo_lote_interface	IS 'Código interno do lote. Chave única (com codigo_nota e tipo).';
COMMENT ON COLUMN public.lote_interface_nota.codigo_nota			IS 'Código interno da nota. Chave única (com codigo_lote_interface e tipo).';
COMMENT ON COLUMN public.lote_interface_nota.tipo					IS 'Tipo do relacionamento entre lote e a nota: 0 - Envio para a Prefeitura / 1 - Cancelamento / 2 - Portal Oi. Chave única (com codigo_lote_interface e codigo_nota).';
