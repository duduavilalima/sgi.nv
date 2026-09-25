/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2017-11-08 12:24                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Create sequences                                                       */
/* ---------------------------------------------------------------------- */
CREATE SEQUENCE public.seq_historico_conciliacao_tipo INCREMENT 1 MINVALUE 1 START 1;

/* ---------------------------------------------------------------------- */
/* Add table "historico_conciliacao_tipo"                                 */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.historico_conciliacao_tipo (
    codigo							BIGINT	DEFAULT nextval('seq_historico_conciliacao_tipo')	NOT NULL,
    codigo_historico_conciliacao	BIGINT														NOT NULL,
    codigo_banco					BIGINT,
    tipo_doc_pagamento				INTEGER														NOT NULL,
    CONSTRAINT pk_historico_conciliacao_tipo PRIMARY KEY (codigo)
);

/* Create indexes */
CREATE INDEX ix_historico_conciliacao_tipo_historico_conciliacao					ON public.historico_conciliacao_tipo (codigo_historico_conciliacao);
CREATE INDEX ix_historico_conciliacao_tipo_banco									ON public.historico_conciliacao_tipo (codigo_banco);
CREATE UNIQUE INDEX uq_historico_conciliacao_tipo_hist_conciliacao_banco_tipo_doc	ON public.historico_conciliacao_tipo (codigo_historico_conciliacao,codigo_banco,tipo_doc_pagamento);

/* Comments */
COMMENT ON TABLE public.historico_conciliacao_tipo									IS 'Relacionamento entre o histórico da conciliação e os tipos de documento de pagamento.';
COMMENT ON COLUMN public.historico_conciliacao_tipo.codigo							IS 'Código interno da associação entre o histórico da conciliação bancária e o tipo de documento de pagamento. Sequência: seq_historico_conciliacao_tipo.';
COMMENT ON COLUMN public.historico_conciliacao_tipo.codigo_historico_conciliacao	IS 'Código interno do histórico da conciliação bancária.';
COMMENT ON COLUMN public.historico_conciliacao_tipo.codigo_banco					IS 'Código interno do banco a ser considerado (ou nulo se independe do banco).';
COMMENT ON COLUMN public.historico_conciliacao_tipo.tipo_doc_pagamento				IS 'Tipo de documento de pagamento (conforme campo "tipo_doc_pagamento" da tabela "pagamento"): 0 = Boleto / 1 = "Depósito em conta/Transferência" / 2 = "DDA" / 3 = "Guia" / 4 = "DOC" / 5 = "TED" / 6 = "Concessionária" / 7 = "Outros".';

/* Set owner */
ALTER TABLE public.historico_conciliacao_tipo OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE historico_conciliacao_tipo			ADD CONSTRAINT fk_historico_conciliacao_tipo_historico_conciliacao 
    FOREIGN KEY (codigo_historico_conciliacao)	REFERENCES public.historico_conciliacao (codigo);
ALTER TABLE historico_conciliacao_tipo			ADD CONSTRAINT fk_historico_conciliacao_tipo_banco 
    FOREIGN KEY (codigo_banco)					REFERENCES public.banco (codigo);
