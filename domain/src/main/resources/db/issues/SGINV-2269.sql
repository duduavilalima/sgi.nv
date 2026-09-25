/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2019-06-03 17:00                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop views                                                             */
/* ---------------------------------------------------------------------- */
DROP VIEW IF EXISTS "vw_custos_faturamento_realizado";
DROP VIEW IF EXISTS "vw_custos_receita_prevista";
DROP VIEW IF EXISTS "vw_custos_receita_realizada";

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_resp_cancelamento;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_resp_recebimento;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_conta_corrente_receb;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_conta_corrente_previsao;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_resp_canc_recebimento;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_resp_marca_cobranca_cronica;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_resp_desmarca_cobranca_cronica;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_nota_saida;

ALTER TABLE public.motivo_retorno_documento_nota_saida_parcela DROP CONSTRAINT fk_motivo_retorno_documento_ns_parcela_nota_saida_parcela;

ALTER TABLE public.nota_saida_parcela_retencao DROP CONSTRAINT fk_nota_saida_parcela_retencao_nota_saida_parcela;

ALTER TABLE public.conciliacao_lancamento_associacao DROP CONSTRAINT fk_conciliacao_lancamento_associacao_nota_saida_parcela;

ALTER TABLE public.nota_saida_parcela_motivo_desc_dev DROP CONSTRAINT fk_nota_saida_parcela_motivo_desc_dev_nota_saida_parcela;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "nota_saida_parcela"                           */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT pk_nota_saida_parcela;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT ck_nota_saida_parcela_situacao;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT ck_nota_saida_parcela_tipo_documento_previsto;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT ck_nota_saida_parcela_tipo_documento_recebimento;

/* Create "new" table */
CREATE TABLE public.nota_saida_parcela_TMP (
    codigo BIGINT DEFAULT nextval('seq_nota_saida_parcela')  NOT NULL,
    codigo_nota_saida BIGINT  NOT NULL,
    codigo_conta_corrente_prevista BIGINT,
    codigo_resp_recebimento BIGINT,
    codigo_resp_canc_recebimento BIGINT,
    codigo_resp_cancelamento BIGINT,
    codigo_conta_corrente_recebimento BIGINT,
    codigo_resp_marca_cobranca_cronica BIGINT,
    codigo_resp_desmarca_cobranca_cronica BIGINT,
    ordem SMALLINT  NOT NULL,
    situacao SMALLINT DEFAULT 0  NOT NULL,
    valor_bruto NUMERIC(19,2) DEFAULT 0.00  NOT NULL,
    valor_tributo NUMERIC(9,2) DEFAULT 0.00,
    valor_permuta NUMERIC(9,2) DEFAULT 0.00  NOT NULL,
    valor_desconto NUMERIC(9,2) DEFAULT 0.00  NOT NULL,
    valor_devolucao NUMERIC(9,2) DEFAULT 0.00  NOT NULL,
    valor_multa NUMERIC(9,2) DEFAULT 0.00  NOT NULL,
    valor_juros NUMERIC(9,2) DEFAULT 0.00  NOT NULL,
    valor_liquido NUMERIC(19,2) DEFAULT 0.00  NOT NULL,
    data_vencimento DATE  NOT NULL,
    data_agendamento DATE  NOT NULL,
    data_pagamento DATE  NOT NULL,
    data_contabilizacao DATE,
    motivo_data_vencimento CHARACTER VARYING(200),
    motivo_data_agendamento CHARACTER VARYING(200),
    motivo_data_pagamento CHARACTER VARYING(200),
    motivo_devolucao CHARACTER VARYING(200),
    motivo_desconto CHARACTER VARYING(200),
    tipo_documento_previsto SMALLINT,
    codigo_barras_boleto CHARACTER VARYING(44),
    tipo_documento_recebimento SMALLINT,
    cobranca_cronica BOOLEAN DEFAULT false,
    data_resp_recebimento TIMESTAMP,
    data_resp_canc_recebimento TIMESTAMP,
    data_resp_cancelamento TIMESTAMP,
    data_resp_marca_cobranca_cronica TIMESTAMP,
    data_resp_desmarca_cobranca_cronica TIMESTAMP,
    observacoes_impressao CHARACTER VARYING(200),
    criador CHARACTER VARYING(255),
    data_criacao TIMESTAMP,
    atualizador CHARACTER VARYING(255),
    data_atualizacao TIMESTAMP);

/* Move data from "old" table to "new" table */
INSERT INTO public.nota_saida_parcela_TMP
    (codigo,codigo_nota_saida,codigo_conta_corrente_prevista,codigo_resp_recebimento,codigo_resp_canc_recebimento,codigo_resp_cancelamento,codigo_conta_corrente_recebimento,codigo_resp_marca_cobranca_cronica,codigo_resp_desmarca_cobranca_cronica,ordem,situacao,valor_bruto,valor_tributo,valor_permuta,valor_desconto,valor_devolucao,valor_multa,valor_juros,valor_liquido,data_vencimento,data_agendamento,data_pagamento,data_contabilizacao,motivo_data_vencimento,motivo_data_agendamento,motivo_data_pagamento,motivo_devolucao,motivo_desconto,tipo_documento_previsto,codigo_barras_boleto,tipo_documento_recebimento,cobranca_cronica,data_resp_recebimento,data_resp_canc_recebimento,data_resp_cancelamento,data_resp_marca_cobranca_cronica,data_resp_desmarca_cobranca_cronica,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_nota_saida,codigo_conta_corrente_prevista,codigo_resp_recebimento,codigo_resp_canc_recebimento,codigo_resp_cancelamento,codigo_conta_corrente_recebimento,codigo_resp_marca_cobranca_cronica,codigo_resp_desmarca_cobranca_cronica,ordem,situacao,valor_bruto,valor_tributo,valor_permuta,valor_desconto,valor_devolucao,valor_multa,valor_juros,valor_liquido,data_vencimento,data_agendamento,data_pagamento,data_contabilizacao,motivo_data_vencimento,motivo_data_agendamento,motivo_data_pagamento,motivo_devolucao,motivo_desconto,tipo_documento_previsto,codigo_barras_boleto,tipo_documento_recebimento,cobranca_cronica,data_resp_recebimento,data_resp_canc_recebimento,data_resp_cancelamento,data_resp_marca_cobranca_cronica,data_resp_desmarca_cobranca_cronica,criador,data_criacao,atualizador,data_atualizacao
FROM public.nota_saida_parcela;

/* Drop indexes  */
DROP INDEX public.ix_nota_saida_parcela_nota_saida;
DROP INDEX public.ix_nota_saida_parcela_conta_corrente_prevista;
DROP INDEX public.ix_nota_saida_parcela_resp_recebimento;
DROP INDEX public.ix_nota_saida_parcela_resp_canc_recebimento;
DROP INDEX public.ix_nota_saida_parcela_resp_cancelamento;
DROP INDEX public.ix_nota_saida_parcela_conta_corrente_recebimento;
DROP INDEX public.ix_nota_saida_parcela_resp_marca_cobranca_cronica;
DROP INDEX public.ix_nota_saida_parcela_resp_desmarca_cobranca_cronica;
DROP INDEX public.uq_nota_saida_parcela_nota_saida_ordem;

/* Drop "old" table  */
DROP TABLE public.nota_saida_parcela;

/* Rename "old" table to "new" table */
ALTER TABLE public.nota_saida_parcela_TMP
	RENAME TO nota_saida_parcela;

/* Create primary key */
ALTER TABLE public.nota_saida_parcela
	ADD CONSTRAINT pk_nota_saida_parcela 
    PRIMARY KEY (codigo);

/* Create constraints */
ALTER TABLE public.nota_saida_parcela
	ADD CONSTRAINT ck_nota_saida_parcela_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1, 2, 3]));
ALTER TABLE public.nota_saida_parcela
	ADD CONSTRAINT ck_nota_saida_parcela_tipo_documento_previsto 
    CHECK (tipo_documento_previsto = ANY (ARRAY[0, 1]));
ALTER TABLE public.nota_saida_parcela
	ADD CONSTRAINT ck_nota_saida_parcela_tipo_documento_recebimento 
    CHECK (tipo_documento_recebimento = ANY (ARRAY[0, 1]));

/* Create indexes  */
CREATE INDEX ix_nota_saida_parcela_nota_saida ON public.nota_saida_parcela (codigo_nota_saida);
CREATE INDEX ix_nota_saida_parcela_conta_corrente_prevista ON public.nota_saida_parcela (codigo_conta_corrente_prevista);
CREATE INDEX ix_nota_saida_parcela_resp_recebimento ON public.nota_saida_parcela (codigo_resp_recebimento);
CREATE INDEX ix_nota_saida_parcela_resp_canc_recebimento ON public.nota_saida_parcela (codigo_resp_canc_recebimento);
CREATE INDEX ix_nota_saida_parcela_resp_cancelamento ON public.nota_saida_parcela (codigo_resp_cancelamento);
CREATE INDEX ix_nota_saida_parcela_conta_corrente_recebimento ON public.nota_saida_parcela (codigo_conta_corrente_recebimento);
CREATE INDEX ix_nota_saida_parcela_resp_marca_cobranca_cronica ON public.nota_saida_parcela (codigo_resp_marca_cobranca_cronica);
CREATE INDEX ix_nota_saida_parcela_resp_desmarca_cobranca_cronica ON public.nota_saida_parcela (codigo_resp_desmarca_cobranca_cronica);
CREATE UNIQUE INDEX uq_nota_saida_parcela_nota_saida_ordem ON public.nota_saida_parcela (codigo_nota_saida,ordem);

/* Comments */
COMMENT ON TABLE public.nota_saida_parcela IS 'Parcelas das notas de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo IS 'Código interno da parcela da nota de saída.Sequência: seq_nota_saida_parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_nota_saida IS 'Código interno da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_conta_corrente_prevista IS 'Código interno da conta corrente prevista para recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_resp_recebimento IS 'Código interno da pessoa responsável pelo último recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_resp_canc_recebimento IS 'Código interno da pessoa responsável pelo último cancelamento do recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_resp_cancelamento IS 'Código interno da pessoa responsável pelo cancelamento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_conta_corrente_recebimento IS 'Código interno da conta corrente utilizada para recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_resp_marca_cobranca_cronica IS 'Código interno da pessoa responsável pela marcação de cobrança crônica.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_resp_desmarca_cobranca_cronica IS 'Código interno da pessoa responsável pela desmarcação de cobrança crônica.';
COMMENT ON COLUMN public.nota_saida_parcela.ordem IS 'Ordem da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.situacao IS 'Situação: 0 = Aberta (NF não processada) / 1 = Em recebimento / 2 = Finalizada (recebida) / 3 = Cancelada.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_bruto IS 'Valor bruto da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_tributo IS 'Valor de tributos recebidos da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_permuta IS 'Valor de permuta (troca de valores entre o cliente e a AA) da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_desconto IS 'Valor de desconto da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_devolucao IS 'Valor de devolução da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_multa IS 'Valor de multa recebida da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_juros IS 'Valor de juros recebido da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_liquido IS 'Valor líquido da parcela (valor buto - desconto + multa + juros + permuta + retenções - tributos, se for a 1ª parcela).';
COMMENT ON COLUMN public.nota_saida_parcela.data_vencimento IS 'Data de vencimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.data_agendamento IS 'Data de agendamento do pagamento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.data_pagamento IS 'Data de pagamento da parcela (até o efetivo pagamento, será igual ao vencimento).';
COMMENT ON COLUMN public.nota_saida_parcela.data_contabilizacao IS 'Data de contabilidade da parcela (até a efetiva contabilização, será igual ao recebimento).';
COMMENT ON COLUMN public.nota_saida_parcela.motivo_data_vencimento IS 'Motivo de alteração da data de vencimento.';
COMMENT ON COLUMN public.nota_saida_parcela.motivo_data_agendamento IS 'Motivo de alteração da data de agendamento do pagamento.';
COMMENT ON COLUMN public.nota_saida_parcela.motivo_data_pagamento IS 'Motivo de alteração da data de pagamento.';
COMMENT ON COLUMN public.nota_saida_parcela.motivo_devolucao IS 'Motivo da devolução.';
COMMENT ON COLUMN public.nota_saida_parcela.motivo_desconto IS 'Motivo do desconto.';
COMMENT ON COLUMN public.nota_saida_parcela.tipo_documento_previsto IS 'Tipo de documento de pagamento: 0 = Boleto / 1 = Depósito em conta/transferência';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_barras_boleto IS 'Código de barras gerado para o boleto da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.tipo_documento_recebimento IS 'Tipo de documento de recebimento da parcela: 0 = Boleto / 1 = Depósito em conta/transferência';
COMMENT ON COLUMN public.nota_saida_parcela.cobranca_cronica IS 'Indica se o recebimento tem problema crônico de cobrança (true) ou não (false).';
COMMENT ON COLUMN public.nota_saida_parcela.data_resp_recebimento IS 'Data/hora de confirmação do último recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.data_resp_canc_recebimento IS 'Data/hora do último cancelamento do recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.data_resp_cancelamento IS 'Data/hora do cancelamento.';
COMMENT ON COLUMN public.nota_saida_parcela.data_resp_marca_cobranca_cronica IS 'Data/hora da marcação de cobrança crônica.';
COMMENT ON COLUMN public.nota_saida_parcela.data_resp_desmarca_cobranca_cronica IS 'Data/hora da desmarcação de cobrança crônica.';
COMMENT ON COLUMN public.nota_saida_parcela.observacoes_impressao IS 'Observações relacionados à impressão da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.criador IS 'Responsável (login) pela criação da parcela da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.data_criacao IS 'Data/hora de criação da parcela da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.atualizador IS 'Responsável (login) pela última atualização da parcela da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.data_atualizacao IS 'Data/hora da última atualização da parcela da nota de saída.';

/* Set owner */
ALTER TABLE public.nota_saida_parcela OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_saida_parcela ADD CONSTRAINT fk_nota_saida_parcela_nota_saida 
    FOREIGN KEY (codigo_nota_saida) REFERENCES public.nota_saida (codigo);
ALTER TABLE public.nota_saida_parcela ADD CONSTRAINT fk_nota_saida_parcela_resp_cancelamento 
    FOREIGN KEY (codigo_resp_cancelamento) REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida_parcela ADD CONSTRAINT fk_nota_saida_parcela_resp_recebimento 
    FOREIGN KEY (codigo_resp_recebimento) REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida_parcela ADD CONSTRAINT fk_nota_saida_parcela_conta_corrente_receb 
    FOREIGN KEY (codigo_conta_corrente_recebimento) REFERENCES public.conta_corrente (codigo);
ALTER TABLE public.nota_saida_parcela ADD CONSTRAINT fk_nota_saida_parcela_conta_corrente_previsao 
    FOREIGN KEY (codigo_conta_corrente_prevista) REFERENCES public.conta_corrente (codigo);
ALTER TABLE public.nota_saida_parcela ADD CONSTRAINT fk_nota_saida_parcela_resp_canc_recebimento 
    FOREIGN KEY (codigo_resp_canc_recebimento) REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida_parcela ADD CONSTRAINT fk_nota_saida_parcela_resp_marca_cobranca_cronica 
    FOREIGN KEY (codigo_resp_marca_cobranca_cronica) REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida_parcela ADD CONSTRAINT fk_nota_saida_parcela_resp_desmarca_cobranca_cronica 
    FOREIGN KEY (codigo_resp_desmarca_cobranca_cronica) REFERENCES public.pessoa (codigo);
-- ALTER TABLE public.nota_saida_parcela_conta_contabil ADD CONSTRAINT fk_nota_saida_parcela_conta_contabil_nota_saida_parcela 
--     FOREIGN KEY (codigo_nota_saida_parcela) REFERENCES public.nota_saida_parcela (codigo);

ALTER TABLE public.motivo_retorno_documento_nota_saida_parcela ADD CONSTRAINT fk_motivo_retorno_documento_ns_parcela_nota_saida_parcela 
    FOREIGN KEY (codigo_nota_saida_parcela) REFERENCES public.nota_saida_parcela (codigo);

ALTER TABLE public.nota_saida_parcela_retencao ADD CONSTRAINT fk_nota_saida_parcela_retencao_nota_saida_parcela 
    FOREIGN KEY (codigo_nota_saida_parcela) REFERENCES public.nota_saida_parcela (codigo);

ALTER TABLE public.conciliacao_lancamento_associacao ADD CONSTRAINT fk_conciliacao_lancamento_associacao_nota_saida_parcela 
    FOREIGN KEY (codigo_nota_saida_parcela) REFERENCES public.nota_saida_parcela (codigo);

ALTER TABLE public.nota_saida_parcela_motivo_desc_dev ADD CONSTRAINT fk_nota_saida_parcela_motivo_desc_dev_nota_saida_parcela 
    FOREIGN KEY (codigo_nota_saida_parcela) REFERENCES public.nota_saida_parcela (codigo);

/* ---------------------------------------------------------------------- */
/* Recreate view                                                          */
/* ---------------------------------------------------------------------- */
/* "vw_custos_receita_realizada" */
CREATE OR REPLACE VIEW public.vw_custos_receita_realizada AS 
 SELECT cf.codigo_unidade,
    u.codigo_matriz,
    n.codigo_centro_financeiro,
    cf.codigo_pai AS codigo_gerencia,
    date_part('YEAR'::text, nsp.data_pagamento) AS ano_pagamento,
    date_part('MONTH'::text, nsp.data_pagamento) AS mes_pagamento,
    sum(nsp.valor_liquido) AS valor_liquido,
    f.tipo AS tipo_faturamento
   FROM nota n,
    nota_fiscal nf,
    nota_saida ns,
    nota_saida_parcela nsp,
    unidade u,
    centro_financeiro cf,
    faturamento_etapa fe,
    faturamento f
  WHERE n.codigo = nf.codigo AND nf.codigo = ns.codigo AND ns.codigo = nsp.codigo_nota_saida AND cf.codigo_unidade = u.codigo AND n.codigo_centro_financeiro = cf.codigo AND ns.codigo = fe.codigo_nota_saida AND fe.codigo_faturamento = f.codigo AND nf.tipo_nota_fiscal = 11 AND n.status = 16
  GROUP BY cf.codigo_unidade, u.codigo_matriz, n.codigo_centro_financeiro, cf.codigo_pai, (date_part('YEAR'::text, nsp.data_pagamento)), (date_part('MONTH'::text, nsp.data_pagamento)), f.tipo
  ORDER BY (date_part('YEAR'::text, nsp.data_pagamento)), (date_part('MONTH'::text, nsp.data_pagamento)), n.codigo_centro_financeiro, f.tipo;

ALTER TABLE public.vw_custos_receita_realizada
  OWNER TO sgi;

COMMENT ON VIEW public.vw_custos_receita_realizada
  IS 'Atualizada SGINV-2269 / versão 5.11.19';

/* "vw_custos_receita_prevista" */
CREATE OR REPLACE VIEW public.vw_custos_receita_prevista AS 
 SELECT cf.codigo_unidade,
    u.codigo_matriz,
    n.codigo_centro_financeiro,
    cf.codigo_pai AS codigo_gerencia,
    date_part('YEAR'::text, nsp.data_vencimento) AS ano_vencimento,
    date_part('MONTH'::text, nsp.data_vencimento) AS mes_vencimento,
    sum(nsp.valor_liquido) AS valor_liquido,
    f.tipo AS tipo_faturamento
   FROM nota n,
    nota_fiscal nf,
    nota_saida ns,
    nota_saida_parcela nsp,
    unidade u,
    centro_financeiro cf,
    faturamento_etapa fe,
    faturamento f
  WHERE n.codigo = nf.codigo AND nf.codigo = ns.codigo AND ns.codigo = nsp.codigo_nota_saida AND cf.codigo_unidade = u.codigo AND n.codigo_centro_financeiro = cf.codigo AND ns.codigo = fe.codigo_nota_saida AND fe.codigo_faturamento = f.codigo AND nf.tipo_nota_fiscal = 11 AND (n.status = ANY (ARRAY[15, 16]))
  GROUP BY cf.codigo_unidade, u.codigo_matriz, n.codigo_centro_financeiro, cf.codigo_pai, (date_part('YEAR'::text, nsp.data_vencimento)), (date_part('MONTH'::text, nsp.data_vencimento)), f.tipo
  ORDER BY (date_part('YEAR'::text, nsp.data_vencimento)), (date_part('MONTH'::text, nsp.data_vencimento)), n.codigo_centro_financeiro, f.tipo;

ALTER TABLE public.vw_custos_receita_prevista
  OWNER TO sgi;

COMMENT ON VIEW public.vw_custos_receita_prevista
  IS 'Atualizada SGINV-2269 / versão 5.11.19';

/* "vw_custos_faturamento_realizado" */
CREATE OR REPLACE VIEW public.vw_custos_faturamento_realizado AS 
 SELECT cf.codigo_unidade,
    u.codigo_matriz,
    n.codigo_centro_financeiro,
    cf.codigo_pai AS codigo_gerencia,
    date_part('YEAR'::text, nf.data_emissao) AS ano_emissao,
    date_part('MONTH'::text, nf.data_emissao) AS mes_emissao,
    sum(nsp.valor_bruto) AS valor_bruto,
    f.tipo AS tipo_faturamento
   FROM nota n,
    nota_fiscal nf,
    nota_saida ns,
    nota_saida_parcela nsp,
    unidade u,
    centro_financeiro cf,
    faturamento_etapa fe,
    faturamento f
  WHERE n.codigo = nf.codigo AND nf.codigo = ns.codigo AND ns.codigo = nsp.codigo_nota_saida AND cf.codigo_unidade = u.codigo AND n.codigo_centro_financeiro = cf.codigo AND ns.codigo = fe.codigo_nota_saida AND fe.codigo_faturamento = f.codigo AND nf.tipo_nota_fiscal = 11 AND (n.status = ANY (ARRAY[15, 16]))
  GROUP BY cf.codigo_unidade, u.codigo_matriz, n.codigo_centro_financeiro, cf.codigo_pai, (date_part('YEAR'::text, nf.data_emissao)), (date_part('MONTH'::text, nf.data_emissao)), f.tipo
  ORDER BY (date_part('YEAR'::text, nf.data_emissao)), (date_part('MONTH'::text, nf.data_emissao)), n.codigo_centro_financeiro, f.tipo;

ALTER TABLE public.vw_custos_faturamento_realizado
  OWNER TO sgi;

COMMENT ON VIEW public.vw_custos_faturamento_realizado
  IS 'Atualizada SGINV-2269 / versão 5.11.19';
