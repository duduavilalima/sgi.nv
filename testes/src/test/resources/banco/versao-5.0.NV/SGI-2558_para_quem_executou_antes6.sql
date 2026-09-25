/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-03-18 16:05                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_cancelamento;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_cfop_transporte;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_municipio_ect;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_nota_fiscal;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_resp_envio_rfb;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_resp_ultimo_arquivo;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_transportadora;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_transportadora_municipio;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_unidade;

ALTER TABLE public.nota_saida_parcela	DROP CONSTRAINT fk_nota_saida_parcela_nota_saida;

/* ---------------------------------------------------------------------- */
/* Modify table "nota_saida"                                              */
/* ---------------------------------------------------------------------- */
CREATE INDEX ix_nota_saida_municipio_transportadora	ON public.nota_saida (codigo_municipio_transportadora);
CREATE INDEX ix_nota_saida_cfop_transporte			ON public.nota_saida (codigo_cfop_transporte);
CREATE INDEX ix_nota_saida_municipio_transporte		ON public.nota_saida (codigo_municipio_transporte);

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "nota_saida_parcela"                           */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT pk_nota_saida_parcela;

/* Create temporary table */
CREATE TABLE public.nota_saida_parcela_TMP (
    codigo						BIGINT DEFAULT nextval('seq_nota_saida_parcela') NOT NULL,
    codigo_nota_saida			BIGINT  NOT NULL,
    codigo_resp_recebimento		BIGINT,
    codigo_resp_cancelamento	BIGINT,
    ordem						SMALLINT  NOT NULL,
    situacao					SMALLINT DEFAULT 0  NOT NULL,
    valor						NUMERIC(19,2)  NOT NULL,
    valor_desconto				NUMERIC(9,2) DEFAULT 0.00  NOT NULL,
    valor_multa					NUMERIC(9,2) DEFAULT 0.00  NOT NULL,
    valor_juros					NUMERIC(9,2) DEFAULT 0.00  NOT NULL,
    data_vencimento				DATE  NOT NULL,
    data_pagamento				DATE  NOT NULL,
    data_resp_recebimento		TIMESTAMP,
    data_resp_cancelamento		TIMESTAMP,
    criador						CHARACTER VARYING(255),
    data_criacao				TIMESTAMP,
    atualizador					CHARACTER VARYING(255),
    data_atualizacao			TIMESTAMP);

/* Move data to the temporary table */
INSERT INTO public.nota_saida_parcela_TMP
    (codigo,codigo_nota_saida,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_nota_saida,criador,data_criacao,atualizador,data_atualizacao
FROM public.nota_saida_parcela;

/* Drop "old" table */
DROP TABLE public.nota_saida_parcela;

/* Rename temporary table to "new" table */
ALTER TABLE public.nota_saida_parcela_TMP
	RENAME TO nota_saida_parcela;

/* Add primary key */
ALTER TABLE public.nota_saida_parcela
	ADD CONSTRAINT pk_nota_saida_parcela 
    PRIMARY KEY (codigo);

/* Add constraint */
ALTER TABLE public.nota_saida_parcela
	ADD CONSTRAINT ck_nota_saida_parcela_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1, 2, 3]));

/* Create indexes */
CREATE INDEX ix_nota_saida_parcela_nota_saida				ON public.nota_saida_parcela (codigo_nota_saida);
CREATE INDEX ix_nota_saida_parcela_resp_recebimento			ON public.nota_saida_parcela (codigo_resp_recebimento);
CREATE INDEX ix_nota_saida_parcela_resp_cancelamento		ON public.nota_saida_parcela (codigo_resp_cancelamento);
CREATE UNIQUE INDEX uq_nota_saida_parcela_nota_saida_ordem	ON public.nota_saida_parcela (codigo_nota_saida,ordem);

/* Comments */
COMMENT ON TABLE public.nota_saida_parcela								IS 'Parcelas das notas de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo						IS 'Código interno da parcela da nota de saída.Sequência: seq_nota_saida_parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_nota_saida			IS 'Código interno da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_resp_recebimento		IS 'Código interno da pessoa responsável pelo recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_resp_cancelamento	IS 'Código interno da pessoa responsável pelo cancelamento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.ordem						IS 'Ordem da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.situacao					IS 'Situação: 0 = Aberta (NF não processada) / 1 = Em recebimento / 2 = Finalizada (recebida) / 3 = Cancelada.';
COMMENT ON COLUMN public.nota_saida_parcela.valor						IS 'Valor da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_desconto				IS 'Valor de desconto da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_multa					IS 'Valor de multa recebida da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_juros					IS 'Valor de juros recebido da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.data_vencimento				IS 'Data de vencimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.data_pagamento				IS 'Data de pagamento da parcela (até o efetivo pagamento, será igual ao vencimento).';
COMMENT ON COLUMN public.nota_saida_parcela.data_resp_recebimento		IS 'Data/hora de confirmação do recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.data_resp_cancelamento		IS 'Data/hora do cancelamento.';
COMMENT ON COLUMN public.nota_saida_parcela.criador						IS 'Responsável (login) pela criação da parcela da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.data_criacao				IS 'Data/hora de criação da parcela da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.atualizador					IS 'Responsável (login) pela última atualização da parcela da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.data_atualizacao			IS 'Data/hora da última atualização da parcela da nota de saída.';

/* Set owner */
ALTER TABLE public.nota_saida_parcela OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */

ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_nota_fiscal 
    FOREIGN KEY (codigo)							REFERENCES public.nota_fiscal (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_unidade 
    FOREIGN KEY (codigo_emitente)					REFERENCES public.unidade (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_transportadora_municipio 
    FOREIGN KEY (codigo_municipio_transportadora)	REFERENCES public.municipio_ect (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_cfop_transporte 
    FOREIGN KEY (codigo_cfop_transporte)			REFERENCES public.cfop (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_municipio_ect 
    FOREIGN KEY (codigo_municipio_transporte)		REFERENCES public.municipio_ect (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_transportadora 
    FOREIGN KEY (codigo_transportadora)				REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_resp_ultimo_arquivo 
    FOREIGN KEY (codigo_resp_ultimo_arquivo)		REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_resp_envio_rfb 
    FOREIGN KEY (codigo_resp_envio_rfb)				REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_cancelamento 
    FOREIGN KEY (codigo_cancelamento)				REFERENCES public.pessoa (codigo);

ALTER TABLE public.nota_saida_parcela				ADD CONSTRAINT fk_nota_saida_parcela_nota_saida 
    FOREIGN KEY (codigo_nota_saida)					REFERENCES public.nota_saida (codigo);
ALTER TABLE public.nota_saida_parcela				ADD CONSTRAINT fk_nota_saida_parcela_resp_cancelamento 
    FOREIGN KEY (codigo_resp_cancelamento)			REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida_parcela				ADD CONSTRAINT fk_nota_saida_parcela_resp_recebimento 
    FOREIGN KEY (codigo_resp_recebimento)			REFERENCES public.pessoa (codigo);
