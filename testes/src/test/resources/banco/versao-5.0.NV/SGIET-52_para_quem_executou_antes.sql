/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-03-04 10:56                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Modify table "faturamento"                                             */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.faturamento
	ADD codigo_contrato_cidade_faturamento BIGINT;

CREATE INDEX ix_faturamento_contrato_cidade_faturamento ON public.faturamento (codigo_contrato_cidade_faturamento);

COMMENT ON COLUMN public.faturamento.codigo_contrato_cidade_faturamento IS 'Código interno do relacionamento entre o contrato e a munícipio de faturamento.';

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.faturamento							ADD CONSTRAINT fk_faturamento_contrato_cidade_faturamento 
    FOREIGN KEY (codigo_contrato_cidade_faturamento)	REFERENCES contrato_cidade_faturamento (codigo);
