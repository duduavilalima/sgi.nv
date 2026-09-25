/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2020-01-27 16:50                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop view                                                              */
/* ---------------------------------------------------------------------- */
DROP VIEW IF EXISTS vw_custos_faturamento_realizado;
DROP VIEW IF EXISTS vw_custos_faturamento_previsto;
DROP VIEW IF EXISTS vw_custos_receita_prevista;
DROP VIEW IF EXISTS vw_custos_receita_realizada;

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_cancelamento;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_centro_financeiro;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_cliente;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_conta_corrente;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_municipio_ect;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_servico_tipo;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_tipo_cobranca_faturamento;

ALTER TABLE public.faturamento_aliquota					DROP CONSTRAINT fk_faturamento_aliquota_faturamento;

ALTER TABLE public.faturamento_etapa					DROP CONSTRAINT fk_faturamento_etapa_faturamento;

ALTER TABLE public.motivo_retorno_documento_faturamento	DROP CONSTRAINT fk_motivo_retorno_documento_faturamento_faturamento;

ALTER TABLE public.faturamento_retencao					DROP CONSTRAINT fk_faturamento_retencao_faturamento;

ALTER TABLE public.faturamento_contato					DROP CONSTRAINT fk_faturamento_contato_faturamento;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "faturamento"                                  */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.faturamento DROP CONSTRAINT pk_faturamento;
ALTER TABLE public.faturamento DROP CONSTRAINT ck_faturamento_situacao;
ALTER TABLE public.faturamento DROP CONSTRAINT ck_faturamento_tipo;

/* Create temporary table */
CREATE TABLE public.faturamento_TMP (
    codigo								BIGINT					DEFAULT nextval('seq_faturamento')	NOT NULL,
    codigo_cliente						BIGINT														NOT NULL,
    codigo_centro_financeiro			BIGINT														NOT NULL,
    codigo_conta_corrente				BIGINT														NOT NULL,
    codigo_tipo_cobranca				BIGINT														NOT NULL,
    codigo_servico_tipo					BIGINT,
    codigo_cidade_prestacao_servicos	BIGINT,
    codigo_cancelamento					BIGINT,
    situacao							SMALLINT				DEFAULT 1							NOT NULL,
    tipo								SMALLINT				DEFAULT 1							NOT NULL,
    identificacao						CHARACTER VARYING(100),
    valor								NUMERIC(15,2)												NOT NULL,
    tipo_fornecimento_nf				SMALLINT				DEFAULT 1							NOT NULL,
    percentual_servico					NUMERIC(5,2)												NOT NULL,
    tipo_documento_pagamento			SMALLINT													NOT NULL,
    iss_retido_cliente					BOOLEAN														NOT NULL,
    resumo								CHARACTER VARYING(2000)										NOT NULL,
    discriminacao_inicial				CHARACTER VARYING(2000),
    discriminacao_central				CHARACTER VARYING(2000),
    discriminacao_final					CHARACTER VARYING(2000),
    periodo								CHARACTER VARYING(50),
    data_vencimento_1					DATE,
    data_vencimento_2					DATE,
    data_cancelamento					TIMESTAMP,
    criador								CHARACTER VARYING(255),
    data_criacao						TIMESTAMP,
    atualizador							CHARACTER VARYING(255),
    data_atualizacao					TIMESTAMP);

/* Copy data do temporary table */
INSERT INTO public.faturamento_TMP
    (codigo,codigo_cliente,codigo_centro_financeiro,codigo_conta_corrente,codigo_tipo_cobranca,codigo_servico_tipo,codigo_cidade_prestacao_servicos,codigo_cancelamento,situacao,tipo,identificacao,valor,tipo_fornecimento_nf,percentual_servico,tipo_documento_pagamento,iss_retido_cliente,resumo,discriminacao_inicial,discriminacao_central,discriminacao_final,periodo,data_vencimento_1,data_vencimento_2,data_cancelamento,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_cliente,codigo_centro_financeiro,codigo_conta_corrente,codigo_tipo_cobranca,codigo_servico_tipo,codigo_cidade_prestacao_servicos,codigo_cancelamento,situacao,tipo,identificacao,valor,CASE WHEN percentual_servico=100.0 THEN 1 ELSE 2 END,percentual_servico,tipo_documento_pagamento,iss_retido_cliente,resumo,discriminacao_inicial,discriminacao_central,discriminacao_final,periodo,data_vencimento_1,data_vencimento_2,data_cancelamento,criador,data_criacao,atualizador,data_atualizacao
FROM public.faturamento;

/* Drop indexes */
DROP INDEX public.ix_faturamento_cancelamento;
DROP INDEX public.ix_faturamento_centro_financeiro;
DROP INDEX public.ix_faturamento_cidade_prestacao_servicos;
DROP INDEX public.ix_faturamento_cliente;
DROP INDEX public.ix_faturamento_conta_corrente;
DROP INDEX public.ix_faturamento_servico_tipo;
DROP INDEX public.ix_faturamento_tipo_cobranca;

/* Drop old table */
DROP TABLE public.faturamento;

/* Rename temporary table to the table */
ALTER TABLE public.faturamento_TMP
	RENAME TO faturamento;

/* Add primary key */
ALTER TABLE public.faturamento
	ADD CONSTRAINT pk_faturamento
    PRIMARY KEY (codigo);

/* Add constraints */
ALTER TABLE public.faturamento
	ADD CONSTRAINT ck_faturamento_situacao
    CHECK (situacao = ANY (ARRAY[0, 1, 2, 3, 4]));

ALTER TABLE public.faturamento
	ADD CONSTRAINT ck_faturamento_tipo
    CHECK (tipo = ANY (ARRAY[0, 1]));

ALTER TABLE public.faturamento
	ADD CONSTRAINT ck_faturamento_tipo_fornecimento_nf
    CHECK (tipo_fornecimento_nf = ANY (ARRAY[0, 1, 2]));

/* Create indexes */
CREATE INDEX ix_faturamento_cliente						ON public.faturamento (codigo_cliente);
CREATE INDEX ix_faturamento_centro_financeiro			ON public.faturamento (codigo_centro_financeiro);
CREATE INDEX ix_faturamento_conta_corrente				ON public.faturamento (codigo_conta_corrente);
CREATE INDEX ix_faturamento_tipo_cobranca				ON public.faturamento (codigo_tipo_cobranca);
CREATE INDEX ix_faturamento_cidade_prestacao_servicos	ON public.faturamento (codigo_cidade_prestacao_servicos);
CREATE INDEX ix_faturamento_servico_tipo				ON public.faturamento (codigo_servico_tipo);
CREATE INDEX ix_faturamento_cancelamento				ON public.faturamento (codigo_cancelamento);

/* Comments */
COMMENT ON TABLE public.faturamento										IS 'Faturamentos, conjunto de etapas (posteriormente transformadas em notas fiscais para os clientes).';
COMMENT ON COLUMN public.faturamento.codigo								IS 'Código interno do faturamento. Sequência: seq_faturamento.';
COMMENT ON COLUMN public.faturamento.codigo_cliente						IS 'Código interno do cliente ou subcliente (código interno da pessoa) do faturamento.';
COMMENT ON COLUMN public.faturamento.codigo_centro_financeiro			IS 'Código interno do centro financeiro do faturamento.';
COMMENT ON COLUMN public.faturamento.codigo_conta_corrente				IS 'Código interno da conta corrente utilizada no faturamento.';
COMMENT ON COLUMN public.faturamento.codigo_tipo_cobranca				IS 'Código interno do tipo de cobrança do faturamento.';
COMMENT ON COLUMN public.faturamento.codigo_servico_tipo				IS 'Código interno do tipo de serviço do faturamento (será carregado para as NFs e para os próximos períodos).';
COMMENT ON COLUMN public.faturamento.codigo_cidade_prestacao_servicos	IS 'Código interno da cidade onde os serviços foram presatados e deve ser selecionada entre uma das cidades do contrato (na tabela "") e a cidade do endereço da cidade do contrato (cidade de emissão das NFs). É uma das cidades possíveis para retenção do ISS.';
COMMENT ON COLUMN public.faturamento.codigo_cancelamento				IS 'Código do responsável (código interno da pessoa) pelo cancelamento do faturamento.';
COMMENT ON COLUMN public.faturamento.situacao							IS 'Situação: 0 = Aberto / 1 = Em faturamento (uma etapa com NF) / 2 = Em recebimento (uma etapa com NF recebida) / 3 = Recebido (todas as etapas com as NFs recebidas) / 4 = Cancelado.';
COMMENT ON COLUMN public.faturamento.tipo								IS 'Tipo do faturamento: 0 = Contratual / 1 = Extra contratual.';
COMMENT ON COLUMN public.faturamento.identificacao						IS 'Identificação do faturamento.';
COMMENT ON COLUMN public.faturamento.valor								IS 'Valor total do faturamento.';
COMMENT ON COLUMN public.faturamento.tipo_fornecimento_nf				IS 'Tipo de fornecimento: 0 = Materiais / 1 = Serviços / 2 = Conjugada.';
COMMENT ON COLUMN public.faturamento.percentual_servico					IS 'Percentual de serviço/mão-de-obra. Se 100%, não há divisão entre material e serviço. Se < 100%, material será a diferença. Se 0%, só material.';
COMMENT ON COLUMN public.faturamento.tipo_documento_pagamento			IS 'Tipo de documento de pagamento: 0 = Boleto / 1 = Depósito em conta/transferência';
COMMENT ON COLUMN public.faturamento.iss_retido_cliente					IS 'Indica se o ISS será retido pelo cliente (true) ou não (false).';
COMMENT ON COLUMN public.faturamento.resumo								IS 'Resumo (objeto) do contrato segundo a visão do faturamento para cada faturamento.';
COMMENT ON COLUMN public.faturamento.discriminacao_inicial				IS 'Discriminação dos serviços, parte inicial (para posterior uso na etapa e emissão da NF). Poderá ser montada pela aplicação.';
COMMENT ON COLUMN public.faturamento.discriminacao_central				IS 'Discriminação dos serviços, parte central (para posterior uso na etapa e emissão da NF). Deverá ser digitada pelo usuário.';
COMMENT ON COLUMN public.faturamento.discriminacao_final				IS 'Discriminação dos serviços, parte final (para posterior uso na etapa e emissão da NF). Poderá ser montada pela aplicaÃ§Ã£o.';
COMMENT ON COLUMN public.faturamento.periodo							IS 'Período do faturamento.';
COMMENT ON COLUMN public.faturamento.data_vencimento_1					IS 'Data prevista para o 1º vencimento das notas fiscais emitidas para este faturamento.';
COMMENT ON COLUMN public.faturamento.data_vencimento_2					IS 'Data prevista para o 2º vencimento das notas fiscais emitidas para este faturamento.';
COMMENT ON COLUMN public.faturamento.data_cancelamento					IS 'Data/hora de cancelamento do faturamento.';
COMMENT ON COLUMN public.faturamento.criador							IS 'Responsável (login) pela criação do faturamento.';
COMMENT ON COLUMN public.faturamento.data_criacao						IS 'Data/hora de criação do faturamento.';
COMMENT ON COLUMN public.faturamento.atualizador						IS 'Responsável (login) pela última atualização do faturamento.';
COMMENT ON COLUMN public.faturamento.data_atualizacao					IS 'Data/hora da última atualização do faturamento.';

/* Set owner */
ALTER TABLE public.faturamento
	OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.faturamento							ADD CONSTRAINT fk_faturamento_cliente
    FOREIGN KEY (codigo_cliente)						REFERENCES public.pessoa (codigo);
ALTER TABLE public.faturamento							ADD CONSTRAINT fk_faturamento_centro_financeiro
    FOREIGN KEY (codigo_centro_financeiro)				REFERENCES public.centro_financeiro (codigo);
ALTER TABLE public.faturamento							ADD CONSTRAINT fk_faturamento_conta_corrente
    FOREIGN KEY (codigo_conta_corrente)					REFERENCES public.conta_corrente (codigo);
ALTER TABLE public.faturamento							ADD CONSTRAINT fk_faturamento_tipo_cobranca_faturamento
    FOREIGN KEY (codigo_tipo_cobranca)					REFERENCES public.tipo_cobranca_faturamento (codigo);
ALTER TABLE public.faturamento							ADD CONSTRAINT fk_faturamento_cancelamento
    FOREIGN KEY (codigo_cancelamento)					REFERENCES public.pessoa (codigo);
ALTER TABLE public.faturamento							ADD CONSTRAINT fk_faturamento_municipio_ect
    FOREIGN KEY (codigo_cidade_prestacao_servicos)		REFERENCES public.municipio_ect (codigo);
ALTER TABLE public.faturamento							ADD CONSTRAINT fk_faturamento_servico_tipo
    FOREIGN KEY (codigo_servico_tipo)					REFERENCES public.servico_tipo (codigo);

ALTER TABLE public.faturamento_aliquota					ADD CONSTRAINT fk_faturamento_aliquota_faturamento
    FOREIGN KEY (codigo_faturamento)					REFERENCES public.faturamento (codigo);

ALTER TABLE public.faturamento_etapa					ADD CONSTRAINT fk_faturamento_etapa_faturamento
    FOREIGN KEY (codigo_faturamento)					REFERENCES public.faturamento (codigo);

ALTER TABLE public.motivo_retorno_documento_faturamento	ADD CONSTRAINT fk_motivo_retorno_documento_faturamento_faturamento
    FOREIGN KEY (codigo_faturamento)					REFERENCES public.faturamento (codigo);

ALTER TABLE public.faturamento_retencao					ADD CONSTRAINT fk_faturamento_retencao_faturamento
    FOREIGN KEY (codigo_faturamento)					REFERENCES public.faturamento (codigo);

ALTER TABLE public.faturamento_contato					ADD CONSTRAINT fk_faturamento_contato_faturamento
    FOREIGN KEY (codigo_faturamento)					REFERENCES public.faturamento (codigo);

/* ---------------------------------------------------------------------- */
/* Recreate views                                                         */
/* ---------------------------------------------------------------------- */
/* vw_custos_receita_realizada */
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

COMMENT ON VIEW vw_custos_receita_realizada
  IS 'Atualizada SGINV-2640 / versão 5.13.4';

/* vw_custos_receita_prevista */
CREATE OR REPLACE VIEW public.vw_custos_receita_prevista AS
 SELECT cf.codigo_unidade,
    u.codigo_matriz,
    n.codigo_centro_financeiro,
    date_part('YEAR'::text, cf.data_previsao_primeira_parcela) AS ano_previsao_primeira_parcela,
    date_part('MONTH'::text, cf.data_previsao_primeira_parcela) AS mes_previsao_primeira_parcela,
    date_part('YEAR'::text, cf.data_encerramento) AS ano_encerramento,
    date_part('MONTH'::text, cf.data_encerramento) AS mes_encerramento,
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
  GROUP BY cf.codigo_unidade, u.codigo_matriz, n.codigo_centro_financeiro, cf.codigo_pai, (date_part('YEAR'::text, nsp.data_vencimento)), (date_part('MONTH'::text, nsp.data_vencimento)), f.tipo, (date_part('MONTH'::text, cf.data_previsao_primeira_parcela)), (date_part('YEAR'::text, cf.data_previsao_primeira_parcela)), (date_part('YEAR'::text, cf.data_encerramento)), (date_part('MONTH'::text, cf.data_encerramento))
  ORDER BY (date_part('YEAR'::text, cf.data_previsao_primeira_parcela)), (date_part('MONTH'::text, cf.data_previsao_primeira_parcela)), (date_part('YEAR'::text, nsp.data_vencimento)), (date_part('MONTH'::text, nsp.data_vencimento)), n.codigo_centro_financeiro, f.tipo;

ALTER TABLE public.vw_custos_receita_prevista
  OWNER TO sgi;

COMMENT ON VIEW vw_custos_receita_prevista
  IS 'Atualizada SGINV-2640 / versão 5.13.4';

/* vw_custos_faturamento_previsto */
CREATE OR REPLACE VIEW public.vw_custos_faturamento_previsto AS
 SELECT cf.codigo_unidade,
    u.codigo_matriz,
    f.codigo_centro_financeiro,
    date_part('YEAR'::text, cf.data_previsao_primeira_parcela) AS ano_previsao_primeira_parcela,
    date_part('MONTH'::text, cf.data_previsao_primeira_parcela) AS mes_previsao_primeira_parcela,
    date_part('YEAR'::text, cf.data_encerramento) AS ano_encerramento,
    date_part('MONTH'::text, cf.data_encerramento) AS mes_encerramento,
    cf.codigo_pai AS codigo_gerencia,
    f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro + round((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro) * ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100::numeric) / (1::numeric - (f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100::numeric), 2) + round((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro + round((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro) * ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100::numeric) / (1::numeric - (f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100::numeric), 2)) * COALESCE(( SELECT sum(a.percentual) AS sum
           FROM centro_financeiro_aliquota cfa,
            aliquota a
          WHERE cfa.codigo_centro_financeiro = f.codigo_centro_financeiro AND cfa.codigo_aliquota = a.codigo), 0::numeric) / 100::numeric / (1::numeric - COALESCE(( SELECT sum(a.percentual) AS sum
           FROM centro_financeiro_aliquota cfa,
            aliquota a
          WHERE cfa.codigo_centro_financeiro = f.codigo_centro_financeiro AND cfa.codigo_aliquota = a.codigo), 0::numeric) / 100::numeric), 2) AS valor_mensal,
    f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro AS custo_mensal,
    round((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro) * ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100::numeric) / (1::numeric - (f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100::numeric), 2) AS valor_bdi,
    0 AS tipo_faturamento
   FROM fecho f,
    centro_financeiro cf,
    contrato c,
    unidade u
  WHERE f.codigo_centro_financeiro = cf.codigo AND cf.codigo_contrato = c.codigo AND cf.codigo_unidade = u.codigo AND f.numero = (( SELECT max(f2.numero) AS max
           FROM fecho f2
          WHERE f2.codigo_centro_financeiro = f.codigo_centro_financeiro)) AND cf.situacao = 1 AND c.situacao = 2
  ORDER BY cf.data_previsao_primeira_parcela, f.codigo_centro_financeiro;

ALTER TABLE public.vw_custos_faturamento_previsto
  OWNER TO sgi;

COMMENT ON VIEW vw_custos_faturamento_previsto
  IS 'Atualizada SGINV-2640 / versão 5.13.4';

/* ---------------------------------------------------------------------- */
/* Update data (tabela "nota_fiscal")                                     */
/* ---------------------------------------------------------------------- */
-- select nf.*
-- from nota n, nota_fiscal nf, faturamento_etapa fe, faturamento f
-- where n.codigo = nf.codigo
-- and fe.codigo_nota_saida = n.codigo
-- and fe.codigo_faturamento = f.codigo
-- and f.percentual_servico < 100 --88 (2) / 56330 (1)

UPDATE	nota_fiscal nf
SET		tipo_fornecimento = 2
FROM	nota n, faturamento_etapa fe, faturamento f
WHERE	n.codigo = nf.codigo
AND		fe.codigo_nota_saida = n.codigo
AND		fe.codigo_faturamento = f.codigo
AND		f.percentual_servico < 100;

UPDATE	nota_fiscal nf
SET		tipo_fornecimento = 1
FROM	nota n, faturamento_etapa fe, faturamento f
WHERE	n.codigo = nf.codigo
AND		fe.codigo_nota_saida = n.codigo
AND		fe.codigo_faturamento = f.codigo
AND		f.percentual_servico = 100;

/* vw_custos_faturamento_realizado */
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
