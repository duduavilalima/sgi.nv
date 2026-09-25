/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-03-09 16:49                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Modify table "contrato_cidade_faturamento"                             */
/* ---------------------------------------------------------------------- */
CREATE UNIQUE INDEX uq_contrato_cidade_faturamento_contrato_municipio_ect ON public.contrato_cidade_faturamento (codigo_contrato,codigo_municipio_ect);

COMMENT ON COLUMN contrato_cidade_faturamento.codigo_contrato		IS 'Código interno do contrato. Chave única.';
COMMENT ON COLUMN contrato_cidade_faturamento.codigo_municipio_ect	IS 'Código interno do município. Chave única.';
