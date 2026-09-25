/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2019-08-30 15:19                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_saida_parcela_baixa_retencao DROP CONSTRAINT fk_nota_saida_parcela_baixa_retencao_nota_saida_parcela;
ALTER TABLE public.nota_saida_parcela_baixa_retencao DROP CONSTRAINT fk_nota_saida_parcela_baixa_retencao_conta_corrente;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "nota_saida_parcela_baixa_retencao"            */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.nota_saida_parcela_baixa_retencao DROP CONSTRAINT pk_nota_saida_parcela_baixa_retencao;
ALTER TABLE public.nota_saida_parcela_baixa_retencao DROP CONSTRAINT ck_nota_saida_parcela_baixa_retencao_situacao;

/* Create "new" table */
CREATE TABLE public.nota_saida_parcela_baixa_retencao_TMP (
    codigo						BIGINT			DEFAULT nextval('seq_nota_saida_parcela_baixa_retencao')	NOT NULL,
    codigo_nota_saida_parcela	BIGINT																		NOT NULL,
    codigo_conta_corrente		BIGINT																		NOT NULL,
    data_pagamento				DATE																		NOT NULL,
    data_contabilizacao			DATE																		NOT NULL,
    valor_baixa					NUMERIC(9,2)	DEFAULT 0.00												NOT NULL,
    valor_juros					NUMERIC(9,2)	DEFAULT 0.00												NOT NULL,
    criador						CHARACTER VARYING(255),
    data_criacao				TIMESTAMP);

/* Move data from "old" table to "new" table */
INSERT INTO public.nota_saida_parcela_baixa_retencao_TMP
    (codigo,codigo_nota_saida_parcela,codigo_conta_corrente,data_contabilizacao,valor_baixa,valor_juros,criador,data_criacao)
SELECT
    codigo,codigo_nota_saida_parcela,codigo_conta_corrente,data_contabilizacao,valor_baixa,valor_juros,criador,data_criacao
FROM public.nota_saida_parcela_baixa_retencao;

/* Drop indexes  */
DROP INDEX public.ix_nota_saida_parcela_baixa_retencao_nota_saida_parcela;
DROP INDEX public.ix_nota_saida_parcela_baixa_retencao_conta_corrente;

/* Drop "old" table  */
DROP TABLE public.nota_saida_parcela_baixa_retencao;

/* Rename "old" table to "new" table */
ALTER TABLE public.nota_saida_parcela_baixa_retencao_TMP
	RENAME TO nota_saida_parcela_baixa_retencao;

/* Create primary key */
ALTER TABLE public.nota_saida_parcela_baixa_retencao
	ADD CONSTRAINT pk_nota_saida_parcela_baixa_retencao 
    PRIMARY KEY (codigo);

/* Create indexes */
CREATE INDEX ix_nota_saida_parcela_baixa_retencao_nota_saida_parcela	ON public.nota_saida_parcela_baixa_retencao (codigo_nota_saida_parcela);
CREATE INDEX ix_nota_saida_parcela_baixa_retencao_conta_corrente		ON public.nota_saida_parcela_baixa_retencao (codigo_conta_corrente);

/* Comments */
COMMENT ON TABLE public.nota_saida_parcela_baixa_retencao								IS 'Retenções realizadas nas parcelas.';
COMMENT ON COLUMN public.nota_saida_parcela_baixa_retencao.codigo						IS 'Código interno da baixa da retenção da parcela. Sequência: seq_nota_saida_parcela_baixa_retencao.';
COMMENT ON COLUMN public.nota_saida_parcela_baixa_retencao.codigo_nota_saida_parcela	IS 'Código interno da parcela da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela_baixa_retencao.codigo_conta_corrente		IS 'Código interno da conta corrente utilizada para recebimento da baixa da retenção da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela_baixa_retencao.data_pagamento				IS 'Data de contabilização da baixa da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela_baixa_retencao.data_contabilizacao			IS 'Data de contabilização da baixa da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela_baixa_retencao.valor_baixa					IS 'Valor da baixa da retenção da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela_baixa_retencao.valor_juros					IS 'Valor de juros da baixa da retenção da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela_baixa_retencao.criador						IS 'Responsável (login) pela criação da baixa da retenção da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela_baixa_retencao.data_criacao					IS 'Data/hora de criação da baixa da retenção da parcela.';

/* Set owner */
ALTER TABLE public.nota_saida_parcela_baixa_retencao OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_saida_parcela_baixa_retencao	ADD CONSTRAINT fk_nota_saida_parcela_baixa_retencao_nota_saida_parcela 
    FOREIGN KEY (codigo_nota_saida_parcela)				REFERENCES public.nota_saida_parcela (codigo);
ALTER TABLE public.nota_saida_parcela_baixa_retencao	ADD CONSTRAINT fk_nota_saida_parcela_baixa_retencao_conta_corrente 
    FOREIGN KEY (codigo_conta_corrente)					REFERENCES public.conta_corrente (codigo);
