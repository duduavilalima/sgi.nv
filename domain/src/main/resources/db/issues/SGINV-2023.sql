/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2019-01-25 15:57                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop view                                                              */
/* ---------------------------------------------------------------------- */
DROP VIEW IF EXISTS "vw_custos_receita_prevista";
DROP VIEW IF EXISTS "vw_custos_faturamento_realizado";
DROP VIEW IF EXISTS "vw_custos_receita_realizada";

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.faturamento_etapa				DROP CONSTRAINT fk_faturamento_etapa_cancelamento;
ALTER TABLE public.faturamento_etapa				DROP CONSTRAINT fk_faturamento_etapa_faturamento;
ALTER TABLE public.faturamento_etapa				DROP CONSTRAINT fk_faturamento_etapa_nota_saida;
ALTER TABLE public.faturamento_etapa				DROP CONSTRAINT fk_faturamento_etapa_resp_faturamento;

ALTER TABLE public.motivo_retorno_documento_etapa	DROP CONSTRAINT fk_motivo_retorno_documento_etapa_faturamento_etapa;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "faturamento_etapa"                            */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.faturamento_etapa DROP CONSTRAINT pk_faturamento_etapa;
ALTER TABLE public.faturamento_etapa DROP CONSTRAINT ck_faturamento_etapa_situacao;

/* Create "new" table */
CREATE TABLE public.faturamento_etapa_TMP (
    codigo						BIGINT			DEFAULT nextval('seq_faturamento_etapa')	NOT NULL,
    codigo_faturamento			BIGINT														NOT NULL,
    codigo_nota_saida			BIGINT,
    codigo_resp_faturamento		BIGINT,
    codigo_cancelamento			BIGINT,
    codigo_ult_alteracao_valor	BIGINT,
    numero						SMALLINT													NOT NULL,
    situacao					SMALLINT		DEFAULT 0									NOT NULL,
    inicio						DATE														NOT NULL,
    termino						DATE														NOT NULL,
    valor						NUMERIC(15,2)												NOT NULL,
    valor_permuta				NUMERIC(9,2)	DEFAULT 0.00								NOT NULL,
    valor_desconto				NUMERIC(9,2)	DEFAULT 0.00								NOT NULL,
    valor_multa					NUMERIC(9,2)	DEFAULT 0.00								NOT NULL,
    valor_juros					NUMERIC(9,2)	DEFAULT 0.00								NOT NULL,
    descricao					CHARACTER VARYING(255)										NOT NULL,
    discriminacao_inicial		CHARACTER VARYING(2000)										NOT NULL,
    discriminacao_central		CHARACTER VARYING(2000),
    discriminacao_final			CHARACTER VARYING(2000)										NOT NULL,
    exibe_retencao_contratual	BOOLEAN			DEFAULT false								NOT NULL,
    exibe_retencao_acessoria	BOOLEAN			DEFAULT false								NOT NULL,
    exibe_permuta				BOOLEAN			DEFAULT false								NOT NULL,
    data_faturamento			TIMESTAMP,
    data_cancelamento			TIMESTAMP,
    ult_alteracao_valor			NUMERIC(15,2),
    data_ult_alteracao_valor	TIMESTAMP,
    criador						CHARACTER VARYING(255),
    data_criacao				TIMESTAMP,
    atualizador					CHARACTER VARYING(255),
    data_atualizacao			TIMESTAMP);

/* Move data from "old" table to "new" table */
INSERT INTO public.faturamento_etapa_TMP
    (codigo,codigo_faturamento,codigo_nota_saida,codigo_resp_faturamento,codigo_cancelamento,numero,situacao,inicio,termino,valor,valor_permuta,valor_desconto,valor_multa,valor_juros,descricao,discriminacao_inicial,discriminacao_central,discriminacao_final,exibe_retencao_contratual,exibe_retencao_acessoria,exibe_permuta,data_faturamento,data_cancelamento,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_faturamento,codigo_nota_saida,codigo_resp_faturamento,codigo_cancelamento,numero,situacao,inicio,termino,valor,valor_permuta,valor_desconto,valor_multa,valor_juros,descricao,discriminacao_inicial,discriminacao_central,discriminacao_final,exibe_retencao_contratual,exibe_retencao_acessoria,exibe_permuta,data_faturamento,data_cancelamento,criador,data_criacao,atualizador,data_atualizacao
FROM public.faturamento_etapa;

/* Drop indexes  */
DROP INDEX public.ix_faturamento_etapa_cancelamento;
DROP INDEX public.ix_faturamento_etapa_faturamento;
DROP INDEX public.ix_faturamento_etapa_nota_saida;
DROP INDEX public.ix_faturamento_etapa_resp_faturamento;

/* Drop "old" table  */
DROP TABLE public.faturamento_etapa;

/* Rename "old" table to "new" table */
ALTER TABLE public.faturamento_etapa_TMP
	RENAME TO faturamento_etapa;

/* Create primary key */
ALTER TABLE public.faturamento_etapa
	ADD CONSTRAINT pk_faturamento_etapa 
    PRIMARY KEY (codigo);

/* Create constraint */
ALTER TABLE public.faturamento_etapa
	ADD CONSTRAINT ck_faturamento_etapa_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1, 2, 3, 4]));

/* Create indexes  */
CREATE INDEX ix_faturamento_etapa_faturamento			ON public.faturamento_etapa (codigo_faturamento);
CREATE INDEX ix_faturamento_etapa_nota_saida			ON public.faturamento_etapa (codigo_nota_saida);
CREATE INDEX ix_faturamento_etapa_resp_faturamento		ON public.faturamento_etapa (codigo_resp_faturamento);
CREATE INDEX ix_faturamento_etapa_cancelamento			ON public.faturamento_etapa (codigo_cancelamento);
CREATE INDEX ix_faturamento_etapa_ult_alteracao_valor	ON public.faturamento_etapa (codigo_ult_alteracao_valor);

/* Comments */
COMMENT ON TABLE public.faturamento_etapa								IS 'Etapa do faturamento. Cada etapa será uma nota fiscal.';
COMMENT ON COLUMN public.faturamento_etapa.codigo						IS 'Código interno da etapa. Sequência: seq_faturamento_etapa.';
COMMENT ON COLUMN public.faturamento_etapa.codigo_faturamento			IS 'Código interno do faturamento.';
COMMENT ON COLUMN public.faturamento_etapa.codigo_nota_saida			IS 'Código interno da nota de saída associada.';
COMMENT ON COLUMN public.faturamento_etapa.codigo_resp_faturamento		IS 'Código do responsável (código interno da pessoa) pelo faturamento da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.codigo_cancelamento			IS 'Código do responsável (código interno da pessoa) pelo cancelamento da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.codigo_ult_alteracao_valor	IS 'Código do responsável (código interno da pessoa) pela última alteração de valor da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.numero						IS 'Número da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.situacao						IS 'Situação: 0 = A faturar / 1 = Faturada / 2 = Recebida / 4 = Cancelada.';
COMMENT ON COLUMN public.faturamento_etapa.inicio						IS 'Data de início da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.termino						IS 'Data de término da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.valor						IS 'Valor da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.valor_permuta				IS 'Valor de permuta (troca de valores entre o cliente e a AA) da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.valor_desconto				IS 'Valor de desconto da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.valor_multa					IS 'Valor de multa recebida da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.valor_juros					IS 'Valor de juros recebido da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.descricao					IS 'Descrição do item da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.discriminacao_inicial		IS 'Discriminação dos serviços, parte inicial (para posterior emissão da NF).';
COMMENT ON COLUMN public.faturamento_etapa.discriminacao_central		IS 'Discriminação dos serviços, parte central (para posterior emissão da NF).';
COMMENT ON COLUMN public.faturamento_etapa.discriminacao_final			IS 'Discriminação dos serviços, parte final (para posterior emissão da NF).';
COMMENT ON COLUMN public.faturamento_etapa.exibe_retencao_contratual	IS 'Indica se a linha correspondente a retenção contratual, caso exista, será exibida na discriminação dos serviços da NF (true) ou não (false).';
COMMENT ON COLUMN public.faturamento_etapa.exibe_retencao_acessoria		IS 'Indica se a retenção acessória, caso exista, será exibida no corpo da NF (true) ou não (false).';
COMMENT ON COLUMN public.faturamento_etapa.exibe_permuta				IS 'Indica se a permuta, caso exista, será exibida no corpo da NF (true) ou não (false).';
COMMENT ON COLUMN public.faturamento_etapa.data_faturamento				IS 'Data/hora de faturamento da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.data_cancelamento			IS 'Data/hora de cancelamento da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.ult_alteracao_valor			IS 'Valor anterior da  última alteração de valor da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.data_ult_alteracao_valor		IS 'Data/hora da última alteração de valor da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.criador						IS 'Responsável (login) pela criação da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.data_criacao					IS 'Data/hora de criação da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.atualizador					IS 'Responsável (login) pela última atualização da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.data_atualizacao				IS 'Data/hora da última atualização da etapa.';

/* Set owner */
ALTER TABLE public.faturamento_etapa OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.faturamento_etapa				ADD CONSTRAINT fk_faturamento_etapa_faturamento 
    FOREIGN KEY (codigo_faturamento)				REFERENCES public.faturamento (codigo);
ALTER TABLE public.faturamento_etapa				ADD CONSTRAINT fk_faturamento_etapa_nota_saida 
    FOREIGN KEY (codigo_nota_saida)					REFERENCES public.nota_saida (codigo);
ALTER TABLE public.faturamento_etapa				ADD CONSTRAINT fk_faturamento_etapa_resp_faturamento 
    FOREIGN KEY (codigo_cancelamento)				REFERENCES public.pessoa (codigo);
ALTER TABLE public.faturamento_etapa				ADD CONSTRAINT fk_faturamento_etapa_cancelamento 
    FOREIGN KEY (codigo_resp_faturamento)			REFERENCES public.pessoa (codigo);
ALTER TABLE public.faturamento_etapa				ADD CONSTRAINT fk_faturamento_etapa_ult_alteracao_valor 
    FOREIGN KEY (codigo_ult_alteracao_valor)		REFERENCES public.pessoa (codigo);

ALTER TABLE public.motivo_retorno_documento_etapa	ADD CONSTRAINT fk_motivo_retorno_documento_etapa_faturamento_etapa 
    FOREIGN KEY (codigo_etapa)						REFERENCES public.faturamento_etapa (codigo);

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

ALTER TABLE vw_custos_receita_realizada
  OWNER TO sgi;

COMMENT ON VIEW vw_custos_receita_realizada
  IS 'Atualizada SGINV-2023 / versão 5.11.15';

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

ALTER TABLE vw_custos_faturamento_realizado
  OWNER TO sgi;

COMMENT ON VIEW vw_custos_faturamento_realizado
  IS 'Atualizada SGINV-2023 / versão 5.11.15';

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

ALTER TABLE vw_custos_receita_prevista
  OWNER TO sgi;

COMMENT ON VIEW vw_custos_receita_prevista
  IS 'Atualizada SGINV-2023 / versão 5.11.15';
