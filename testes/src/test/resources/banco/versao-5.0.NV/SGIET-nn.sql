/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-04-07 16:36                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Create sequences                                                       */
/* ---------------------------------------------------------------------- */
CREATE SEQUENCE public.seq_nota_saida_parcela_conta_contabil			INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE public.seq_motivo_retorno_documento_nota_saida_parcela	INCREMENT 1 MINVALUE 1 START 1;

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_resp_cancelamento;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_resp_recebimento;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_nota_saida;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "nota_saida_parcela"                           */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT pk_nota_saida_parcela;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT ck_nota_saida_parcela_situacao;

/* Create temporary table */
CREATE TABLE public.nota_saida_parcela_TMP (
    codigo								BIGINT DEFAULT nextval('seq_nota_saida_parcela')  NOT NULL,
    codigo_nota_saida					BIGINT  NOT NULL,
    codigo_resp_recebimento				BIGINT,
    codigo_resp_cancelamento			BIGINT,
    codigo_conta_corrente_recebimento	BIGINT,
    ordem								SMALLINT  NOT NULL,
    situacao							SMALLINT DEFAULT 0  NOT NULL,
    valor								NUMERIC(19,2)  NOT NULL,
    valor_desconto						NUMERIC(9,2) DEFAULT 0.00  NOT NULL,
    valor_multa							NUMERIC(9,2) DEFAULT 0.00  NOT NULL,
    valor_juros							NUMERIC(9,2) DEFAULT 0.00  NOT NULL,
    data_vencimento						DATE  NOT NULL,
    data_pagamento						DATE  NOT NULL,
    codigo_barras_boleto				CHARACTER VARYING(44),
    tipo_documento_recebimento			SMALLINT,
    data_resp_recebimento				TIMESTAMP,
    data_resp_cancelamento				TIMESTAMP,
    criador								CHARACTER VARYING(255),
    data_criacao						TIMESTAMP,
    atualizador							CHARACTER VARYING(255),
    data_atualizacao					TIMESTAMP);

/* Move data to the temporary table */
INSERT INTO public.nota_saida_parcela_TMP
    (codigo,codigo_nota_saida,codigo_resp_recebimento,codigo_resp_cancelamento,ordem,situacao,valor,valor_desconto,valor_multa,valor_juros,data_vencimento,data_pagamento,data_resp_recebimento,data_resp_cancelamento,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_nota_saida,codigo_resp_recebimento,codigo_resp_cancelamento,ordem,situacao,valor,valor_desconto,valor_multa,valor_juros,data_vencimento,data_pagamento,data_resp_recebimento,data_resp_cancelamento,criador,data_criacao,atualizador,data_atualizacao
FROM public.nota_saida_parcela;

/* Drop indexes */
DROP INDEX public.ix_nota_saida_parcela_nota_saida;
DROP INDEX public.ix_nota_saida_parcela_resp_recebimento;
DROP INDEX public.ix_nota_saida_parcela_resp_cancelamento;
DROP INDEX public.uq_nota_saida_parcela_nota_saida_ordem;

/* Drop "old" table */
DROP TABLE public.nota_saida_parcela;

/* Rename temporary table to "new" table */
ALTER TABLE public.nota_saida_parcela_TMP
	RENAME TO nota_saida_parcela;

/* Add primary key */
ALTER TABLE public.nota_saida_parcela
	ADD CONSTRAINT pk_nota_saida_parcela 
    PRIMARY KEY (codigo);

/* Add constraints */
ALTER TABLE public.nota_saida_parcela
	ADD CONSTRAINT ck_nota_saida_parcela_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1, 2, 3]));

/* Create indexes */
CREATE INDEX ix_nota_saida_parcela_nota_saida					ON public.nota_saida_parcela (codigo_nota_saida);
CREATE INDEX ix_nota_saida_parcela_resp_recebimento				ON public.nota_saida_parcela (codigo_resp_recebimento);
CREATE INDEX ix_nota_saida_parcela_resp_cancelamento			ON public.nota_saida_parcela (codigo_resp_cancelamento);
CREATE INDEX ix_nota_saida_parcela_conta_corrente_recebimento	ON public.nota_saida_parcela (codigo_conta_corrente_recebimento);
CREATE UNIQUE INDEX uq_nota_saida_parcela_nota_saida_ordem		ON public.nota_saida_parcela (codigo_nota_saida,ordem);

/* Comments */
COMMENT ON TABLE public.nota_saida_parcela										IS 'Parcelas das notas de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo								IS 'Código interno da parcela da nota de saída.Sequência: seq_nota_saida_parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_nota_saida					IS 'Código interno da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_resp_recebimento				IS 'Código interno da pessoa responsável pelo recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_resp_cancelamento			IS 'Código interno da pessoa responsável pelo cancelamento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_conta_corrente_recebimento	IS 'Código interno da conta corrente utilizada para recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.ordem								IS 'Ordem da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.situacao							IS 'Situação: 0 = Aberta (NF não processada) / 1 = Em recebimento / 2 = Finalizada (recebida) / 3 = Cancelada.';
COMMENT ON COLUMN public.nota_saida_parcela.valor								IS 'Valor da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_desconto						IS 'Valor de desconto da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_multa							IS 'Valor de multa recebida da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_juros							IS 'Valor de juros recebido da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.data_vencimento						IS 'Data de vencimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.data_pagamento						IS 'Data de pagamento da parcela (até o efetivo pagamento, será igual ao vencimento).';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_barras_boleto				IS 'Código de barras gerado para o boleto da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.tipo_documento_recebimento			IS 'Tipo de documento de recebimento da parcela: 0 = Boleto / 1 = Depósito em conta/transferência';
COMMENT ON COLUMN public.nota_saida_parcela.data_resp_recebimento				IS 'Data/hora de confirmação do recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.data_resp_cancelamento				IS 'Data/hora do cancelamento.';
COMMENT ON COLUMN public.nota_saida_parcela.criador								IS 'Responsável (login) pela criação da parcela da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.data_criacao						IS 'Data/hora de criação da parcela da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.atualizador							IS 'Responsável (login) pela última atualização da parcela da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.data_atualizacao					IS 'Data/hora da última atualização da parcela da nota de saída.';

/* Set owner */
ALTER TABLE public.nota_saida_parcela OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add table "nota_saida_parcela_conta_contabil"                          */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.nota_saida_parcela_conta_contabil (
    codigo						BIGINT DEFAULT nextval('seq_nota_saida_parcela_conta_contabil')  NOT NULL,
    codigo_nota_saida_parcela	BIGINT  NOT NULL,
    codigo_conta_contabil		BIGINT  NOT NULL,
    tipo						SMALLINT  NOT NULL,
    CONSTRAINT pk_nota_saida_parcela_conta_contabil PRIMARY KEY (codigo)
);

/* Create indexes */
CREATE INDEX ix_nota_saida_parcela_conta_contabil_nota_saida_parcela		ON public.nota_saida_parcela_conta_contabil (codigo_nota_saida_parcela);
CREATE INDEX ix_nota_saida_parcela_conta_contabil_conta_contabil			ON public.nota_saida_parcela_conta_contabil (codigo_conta_contabil);
CREATE UNIQUE INDEX uq_nota_saida_parcela_conta_contabil_ns_parcela_cc_tipo	ON public.nota_saida_parcela_conta_contabil (codigo_nota_saida_parcela,codigo_conta_contabil,tipo);

/* Add constraints */
ALTER TABLE public.nota_saida_parcela_conta_contabil
	ADD CONSTRAINT ck_nota_saida_parcela_conta_contabil_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1, 2]));

/* Comments */
COMMENT ON TABLE public.nota_saida_parcela_conta_contabil								IS 'Relacionamentos entre a parcela de nota de saída e a conta contábil para cada um dos valores de multa, juros e/ou desconto concedido.';
COMMENT ON COLUMN public.nota_saida_parcela_conta_contabil.codigo						IS 'Código interno do relacionamentos entre a parcela da nota de saída e a conta contábil. Sequência: seq_nota_saida_parcela_conta_contabil';
COMMENT ON COLUMN public.nota_saida_parcela_conta_contabil.codigo_nota_saida_parcela	IS 'Código interno da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela_conta_contabil.codigo_conta_contabil		IS 'Código interno da conta contábil.';
COMMENT ON COLUMN public.nota_saida_parcela_conta_contabil.tipo							IS 'Tipo da conta contábil: 0 - Multas / 1 - Juros / 2 - Descontos concedidos.';

/* Set owner */
ALTER TABLE public.nota_saida_parcela_conta_contabil OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add table "motivo_retorno_documento_nota_saida_parcela"                */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.motivo_retorno_documento_nota_saida_parcela (
    codigo							BIGINT DEFAULT nextval('seq_motivo_retorno_documento_nota_saida_parcela')  NOT NULL,
    codigo_nota_saida_parcela		BIGINT  NOT NULL,
    codigo_motivo_retorno_documento	BIGINT  NOT NULL,
    CONSTRAINT pk_motivo_retorno_documento_nota_saida_parcela PRIMARY KEY (codigo)
);

/* Create indexes */
CREATE INDEX ix_mot_ret_documento_nota_saida_parcela_nota_saida_parcela				ON public.motivo_retorno_documento_nota_saida_parcela (codigo_nota_saida_parcela);
CREATE INDEX ix_mot_ret_documento_nota_saida_parcela_mot_ret_documento				ON public.motivo_retorno_documento_nota_saida_parcela (codigo_motivo_retorno_documento);
CREATE UNIQUE INDEX uq_mot_ret_documento_nota_saida_parcela_mot_ret_doc_ns_parcela	ON public.motivo_retorno_documento_nota_saida_parcela (codigo_nota_saida_parcela,codigo_motivo_retorno_documento);

/* Comments */
COMMENT ON TABLE public.motivo_retorno_documento_nota_saida_parcela										IS 'Associação entre um retorno (motivo de retorno x documento) e um faturamento.';
COMMENT ON COLUMN public.motivo_retorno_documento_nota_saida_parcela.codigo								IS 'Código interno da associação entre um retorno (motivo de retorno x documento) e uma parcela de nota de saída. Sequência: seq_motivo_retorno_documento_nota_saida_parcela';
COMMENT ON COLUMN public.motivo_retorno_documento_nota_saida_parcela.codigo_nota_saida_parcela			IS 'Código interno da parcela da nota de saída.';
COMMENT ON COLUMN public.motivo_retorno_documento_nota_saida_parcela.codigo_motivo_retorno_documento	IS 'Código interno do motivo de retorno do documento.';

/* Set owner */
ALTER TABLE public.motivo_retorno_documento_nota_saida_parcela OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_saida_parcela							ADD CONSTRAINT fk_nota_saida_parcela_nota_saida 
    FOREIGN KEY (codigo_nota_saida)								REFERENCES public.nota_saida (codigo);
ALTER TABLE public.nota_saida_parcela							ADD CONSTRAINT fk_nota_saida_parcela_resp_cancelamento 
    FOREIGN KEY (codigo_resp_cancelamento)						REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida_parcela							ADD CONSTRAINT fk_nota_saida_parcela_resp_recebimento 
    FOREIGN KEY (codigo_resp_recebimento)						REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida_parcela							ADD CONSTRAINT fk_nota_saida_parcela_conta_corrente 
    FOREIGN KEY (codigo_conta_corrente_recebimento)				REFERENCES public.conta_corrente (codigo);

ALTER TABLE public.nota_saida_parcela_conta_contabil			ADD CONSTRAINT fk_nota_saida_parcela_conta_contabil_nota_saida_parcela 
    FOREIGN KEY (codigo_nota_saida_parcela)						REFERENCES public.nota_saida_parcela (codigo);
ALTER TABLE public.nota_saida_parcela_conta_contabil			ADD CONSTRAINT fk_nota_saida_parcela_conta_contabil_conta_contabil 
    FOREIGN KEY (codigo_conta_contabil)							REFERENCES public.conta_contabil (codigo);

ALTER TABLE public.motivo_retorno_documento_nota_saida_parcela	ADD CONSTRAINT fk_motivo_retorno_documento_ns_parcela_motivo_retorno_documento 
    FOREIGN KEY (codigo_motivo_retorno_documento)				REFERENCES public.motivo_retorno_documento (codigo);
ALTER TABLE public.motivo_retorno_documento_nota_saida_parcela	ADD CONSTRAINT fk_motivo_retorno_documento_ns_parcela_nota_saida_parcela 
    FOREIGN KEY (codigo_nota_saida_parcela)						REFERENCES public.nota_saida_parcela (codigo);
