/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2017-12-05 16:01                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Create sequences                                                       */
/* ---------------------------------------------------------------------- */
CREATE SEQUENCE public.seq_despesa_custos	INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE public.seq_previsao_custos	INCREMENT 1 MINVALUE 1 START 1;

/* ---------------------------------------------------------------------- */
/* Add table "despesa_custos"                                             */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.despesa_custos (
    codigo				BIGINT					DEFAULT nextval('seq_despesa_custos')	NOT NULL,
    codigo_pai			BIGINT,
    descricao			CHARACTER VARYING(50)											NOT NULL,
    ordem				SMALLINT														NOT NULL,
    situacao			SMALLINT				DEFAULT 1								NOT NULL,
    tipo				SMALLINT														NOT NULL,
    operacao			SMALLINT				DEFAULT 0								NOT NULL,
    criador				CHARACTER VARYING(255),
    data_criacao		TIMESTAMP,
    atualizador			CHARACTER VARYING(255),
    data_atualizacao	TIMESTAMP,
    CONSTRAINT pk_despesa_custos PRIMARY KEY (codigo)
);

/* Create index */
CREATE INDEX ix_despesa_custos_pai ON public.despesa_custos (codigo_pai);

/* Create constraints */
ALTER TABLE public.despesa_custos
	ADD CONSTRAINT ck_despesa_custos_operacao
    CHECK (operacao = ANY (ARRAY[0, 1]));
ALTER TABLE public.despesa_custos
	ADD CONSTRAINT ck_despesa_custos_situacao
    CHECK (situacao = ANY (ARRAY[0, 1]));
ALTER TABLE public.despesa_custos
	ADD CONSTRAINT ck_despesa_custos_tipo
    CHECK (tipo = ANY (ARRAY[0, 1, 2, 3]));

/* Comments */
COMMENT ON TABLE public.despesa_custos						IS 'Despesas para a contabilidade de custos.';
COMMENT ON COLUMN public.despesa_custos.codigo				IS 'Código interno da despesa. Sequência: seq_despesa_custos.';
COMMENT ON COLUMN public.despesa_custos.codigo_pai			IS 'Código interno da despesa a qual a despesa está vinculada, ou seja, despesa "pai".';
COMMENT ON COLUMN public.despesa_custos.descricao			IS 'Descrição da despesa.';
COMMENT ON COLUMN public.despesa_custos.ordem				IS 'Ordem da despesa no relatório e na entrada de dados.';
COMMENT ON COLUMN public.despesa_custos.situacao			IS 'Situação da despesa: 0 = Inativa / 1 = Ativa.';
COMMENT ON COLUMN public.despesa_custos.tipo				IS 'Tipo da despesa: 0 = Recebimento / 1 = Custo / 2 = Despesa de centro de custos (não disponível para CL, somente gerências, diretorias e empresas) / 3 = Resultado (soma dos recebimentos e custos).';
COMMENT ON COLUMN public.despesa_custos.operacao			IS 'Operação a ser realizada na despesa "pai": 0 = Soma / 1 = Subtração.';
COMMENT ON COLUMN public.despesa_custos.criador				IS 'Responsável (login) pela criação da despesa.';
COMMENT ON COLUMN public.despesa_custos.data_criacao		IS 'Data/hora de criação da despesa.';
COMMENT ON COLUMN public.despesa_custos.atualizador			IS 'Responsável (login) pela última atualização da despesa.';
COMMENT ON COLUMN public.despesa_custos.data_atualizacao	IS 'Data/hora da última atualização da despesa.';

/* Set owner */
ALTER TABLE public.despesa_custos OWNER TO sgi;

/* Add data */
/*
INSERT INTO despesa_custos (codigo, codigo_pai, descricao, ordem, situacao, tipo, operacao, criador, data_criacao)
	VALUES	(NEXTVAL('seq_despesa_custos'), NULL, 'Rec. Líquida (menos ret. de 11%)', 100, 1, 0, 0, 'admin', CURRENT_TIMESTAMP);

INSERT INTO despesa_custos (codigo, codigo_pai, descricao, ordem, situacao, tipo, operacao, criador, data_criacao)
	SELECT	NEXTVAL('seq_despesa_custos'), despesa_custos2.codigo, 'Receita Líquida', 80, 1, 0, 0, 'admin', CURRENT_TIMESTAMP
	FROM	despesa_custos despesa_custos2
	WHERE	despesa_custos2.descricao = 'Rec. Líquida (menos ret. de 11%)';

INSERT INTO despesa_custos (codigo, codigo_pai, descricao, ordem, situacao, tipo, operacao, criador, data_criacao)
	SELECT	NEXTVAL('seq_despesa_custos'), despesa_custos2.codigo, 'Receita Bruta', 50, 1, 0, 0, 'admin', CURRENT_TIMESTAMP
	FROM	despesa_custos despesa_custos2
	WHERE	despesa_custos2.descricao = 'Receita Líquida';

INSERT INTO despesa_custos (codigo, codigo_pai, descricao, ordem, situacao, tipo, operacao, criador, data_criacao)
	SELECT	NEXTVAL('seq_despesa_custos'), despesa_custos2.codigo, 'Receita Contratual', 10, 1, 0, 0, 'admin', CURRENT_TIMESTAMP
	FROM	despesa_custos despesa_custos2
	WHERE	despesa_custos2.descricao = 'Receita Bruta';

INSERT INTO despesa_custos (codigo, codigo_pai, descricao, ordem, situacao, tipo, operacao, criador, data_criacao)
	SELECT	NEXTVAL('seq_despesa_custos'), despesa_custos2.codigo, 'Receita Extra (15%)', 20, 1, 0, 0, 'admin', CURRENT_TIMESTAMP
	FROM	despesa_custos despesa_custos2
	WHERE	despesa_custos2.descricao = 'Receita Bruta';

INSERT INTO despesa_custos (codigo, codigo_pai, descricao, ordem, situacao, tipo, operacao, criador, data_criacao)
	SELECT	NEXTVAL('seq_despesa_custos'), despesa_custos2.codigo, 'Receita Reembolsável', 30, 1, 0, 0, 'admin', CURRENT_TIMESTAMP
	FROM	despesa_custos despesa_custos2
	WHERE	despesa_custos2.descricao = 'Receita Bruta';

INSERT INTO despesa_custos (codigo, codigo_pai, descricao, ordem, situacao, tipo, operacao, criador, data_criacao)
	SELECT	NEXTVAL('seq_despesa_custos'), despesa_custos2.codigo, 'Receitas Ano Anterior', 40, 1, 0, 0, 'admin', CURRENT_TIMESTAMP
	FROM	despesa_custos despesa_custos2
	WHERE	despesa_custos2.descricao = 'Receita Bruta';

INSERT INTO despesa_custos (codigo, codigo_pai, descricao, ordem, situacao, tipo, operacao, criador, data_criacao)
	SELECT	NEXTVAL('seq_despesa_custos'), despesa_custos2.codigo, 'ISS', 60, 1, 0, 1, 'admin', CURRENT_TIMESTAMP
	FROM	despesa_custos despesa_custos2
	WHERE	despesa_custos2.descricao = 'Receita Líquida';

INSERT INTO despesa_custos (codigo, codigo_pai, descricao, ordem, situacao, tipo, operacao, criador, data_criacao)
	SELECT	NEXTVAL('seq_despesa_custos'), despesa_custos2.codigo, 'PIS/COFINS', 70, 1, 0, 1, 'admin', CURRENT_TIMESTAMP
	FROM	despesa_custos despesa_custos2
	WHERE	despesa_custos2.descricao = 'Receita Líquida';

INSERT INTO despesa_custos (codigo, codigo_pai, descricao, ordem, situacao, tipo, operacao, criador, data_criacao)
	SELECT	NEXTVAL('seq_despesa_custos'), despesa_custos2.codigo, 'INSS (Retenção de 11%)', 90, 1, 0, 1, 'admin', CURRENT_TIMESTAMP
	FROM	despesa_custos despesa_custos2
	WHERE	despesa_custos2.descricao = 'Rec. Líquida (menos ret. de 11%)';
*/

/* ---------------------------------------------------------------------- */
/* Add table "previsao_custos"                                            */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.previsao_custos (
    codigo						BIGINT					DEFAULT nextval('seq_previsao_custos')	NOT NULL,
    codigo_despesa_custos		BIGINT															NOT NULL,
    codigo_unidade				BIGINT,
    codigo_diretoria			BIGINT,
    codigo_gerencia				BIGINT,
    codigo_centro_financeiro	BIGINT,
    referencia_ano				NUMERIC(4)														NOT NULL,
    referencia_mes				NUMERIC(2)														NOT NULL,
    valor						NUMERIC(19,2)													NOT NULL,
    criador						CHARACTER VARYING(255),
    data_criacao				TIMESTAMP,
    atualizador					CHARACTER VARYING(255),
    data_atualizacao			TIMESTAMP,
    CONSTRAINT pk_previsao_custos PRIMARY KEY (codigo)
);

/* Create indexes */
CREATE INDEX ix_previsao_custos_despesa_custos					ON public.previsao_custos (codigo_despesa_custos);
CREATE INDEX ix_previsao_custos_unidade							ON public.previsao_custos (codigo_unidade);
CREATE INDEX ix_previsao_custos_diretoria						ON public.previsao_custos (codigo_diretoria);
CREATE INDEX ix_previsao_custos_gerencia						ON public.previsao_custos (codigo_gerencia);
CREATE INDEX ix_previsao_custos_centro_financeiro				ON public.previsao_custos (codigo_centro_financeiro);
CREATE UNIQUE INDEX uq_previsao_custos_despesa_chave_referencia	ON public.previsao_custos (codigo_despesa_custos,codigo_unidade,codigo_diretoria,codigo_gerencia,codigo_centro_financeiro,referencia_ano,referencia_mes);

/* Create constraint */
ALTER TABLE public.previsao_custos
	ADD CONSTRAINT ck_previsao_custos_chaves
    CHECK (((CASE WHEN codigo_unidade IS NULL THEN 0 ELSE 1 END)+(CASE WHEN codigo_diretoria IS NULL THEN 0 ELSE 1 END)+(CASE WHEN codigo_gerencia IS NULL THEN 0 ELSE 1 END)+(CASE WHEN codigo_centro_financeiro IS NULL THEN 0 ELSE 1 END)) = 1);

/* Comments */
COMMENT ON TABLE public.previsao_custos								IS 'Valor de previsão para as despesas de custos por empresa, diretoria, gerência ou centro de lucro.';
COMMENT ON COLUMN public.previsao_custos.codigo						IS 'Código interno da previsão para a despesa da contabilidade de custos. Sequência: seq_previsao_custos.';
COMMENT ON COLUMN public.previsao_custos.codigo_despesa_custos		IS 'Cpodigo interno da despesa relacionada a previsão para a despesa. Parte da chave única.';
COMMENT ON COLUMN public.previsao_custos.codigo_unidade				IS 'Código interno da unidade da previsão para a despesa. Parte da chave única.';
COMMENT ON COLUMN public.previsao_custos.codigo_diretoria			IS 'Código interno da diretoria da previsão para a despesa. Parte da chave única.';
COMMENT ON COLUMN public.previsao_custos.codigo_gerencia			IS 'Código interno da gerência da previsão para a despesa. Parte da chave única.';
COMMENT ON COLUMN public.previsao_custos.codigo_centro_financeiro	IS 'Código interno do centro de lucro da previsão para a despesa. Parte da chave única.';
COMMENT ON COLUMN public.previsao_custos.referencia_ano				IS 'Ano de referência da previsão para a despesa. Parte da chave única.';
COMMENT ON COLUMN public.previsao_custos.referencia_mes				IS 'Mês de referência da previsão para a despesa. Parte da chave única.';
COMMENT ON COLUMN public.previsao_custos.valor						IS 'Valor da previsão para a despesa.';
COMMENT ON COLUMN public.previsao_custos.criador					IS 'Responsável (login) pela criação da previsão para a despesa.';
COMMENT ON COLUMN public.previsao_custos.data_criacao				IS 'Data/hora de criação da previsão para a despesa.';
COMMENT ON COLUMN public.previsao_custos.atualizador				IS 'Responsável (login) pela última atualização da previsão para a despesa.';
COMMENT ON COLUMN public.previsao_custos.data_atualizacao			IS 'Data/hora da última atualização da previsão para a despesa.';

/* Set owner */
ALTER TABLE public.previsao_custos OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.despesa_custos			ADD CONSTRAINT fk_despesa_custos_despesa_custos
    FOREIGN KEY (codigo_pai)				REFERENCES public.despesa_custos (codigo);

ALTER TABLE public.previsao_custos			ADD CONSTRAINT fk_previsao_custos_despesa_custos
    FOREIGN KEY (codigo_despesa_custos)		REFERENCES public.despesa_custos (codigo);
ALTER TABLE public.previsao_custos			ADD CONSTRAINT fk_previsao_custos_unidade
    FOREIGN KEY (codigo_unidade)			REFERENCES public.unidade (codigo);
ALTER TABLE public.previsao_custos			ADD CONSTRAINT fk_previsao_custos_diretoria
    FOREIGN KEY (codigo_diretoria)			REFERENCES public.diretoria (codigo);
ALTER TABLE public.previsao_custos			ADD CONSTRAINT fk_previsao_custos_gerencia
    FOREIGN KEY (codigo_gerencia)			REFERENCES public.gerencia (codigo);
ALTER TABLE public.previsao_custos			ADD CONSTRAINT fk_previsao_custos_centro_financeiro
    FOREIGN KEY (codigo_centro_financeiro)	REFERENCES public.centro_financeiro (codigo);


/* AJUSTES DA VIEW PARA OS GRÁFICOS */

/* ---------------------------------------------------------------------- */
/* Recreate views                                                         */
/* ---------------------------------------------------------------------- */

DROP VIEW IF EXISTS public.vw_custos_faturamento_previsto;

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
COMMENT ON VIEW public.vw_custos_faturamento_previsto
  IS 'Atualizada versão 5.13';




DROP VIEW IF EXISTS "vw_custos_receita_prevista";

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
  GROUP BY cf.codigo_unidade, u.codigo_matriz, n.codigo_centro_financeiro, cf.codigo_pai, (date_part('YEAR'::text, nsp.data_vencimento)), (date_part('MONTH'::text, nsp.data_vencimento)), f.tipo, date_part('MONTH'::text, cf.data_previsao_primeira_parcela), date_part('YEAR'::text, cf.data_previsao_primeira_parcela), date_part('YEAR'::text, cf.data_encerramento), date_part('MONTH'::text, cf.data_encerramento)
  ORDER BY date_part('YEAR'::text, cf.data_previsao_primeira_parcela), date_part('MONTH'::text, cf.data_previsao_primeira_parcela), (date_part('YEAR'::text, nsp.data_vencimento)), (date_part('MONTH'::text, nsp.data_vencimento)), n.codigo_centro_financeiro, f.tipo;

ALTER TABLE public.vw_custos_receita_prevista
  OWNER TO sgi;
COMMENT ON VIEW public.vw_custos_receita_prevista
  IS 'Atualizada versão 5.13';

   DROP VIEW IF EXISTS "vw_custos_receita_prevista";
   CREATE MATERIALIZED VIEW public.vw_custos_gastos_previstos AS
   SELECT n.codigo,
	 cf.codigo_unidade,
	 date_part('YEAR'::text, cf.data_previsao_primeira_parcela) AS ano_previsao_primeira_parcela,
	 date_part('MONTH'::text, cf.data_previsao_primeira_parcela) AS mes_previsao_primeira_parcela,
	 date_part('YEAR'::text, cf.data_encerramento) AS ano_encerramento,
	 date_part('MONTH'::text, cf.data_encerramento) AS mes_encerramento,
	 u.codigo_matriz,
	 date_part('YEAR'::text, p.data_pagamento) AS ano_pagamento,
	 date_part('MONTH'::text, p.data_pagamento) AS mes_pagamento,
	 d.valor AS valor_total_nf,
	 p.valor + p.valor_multa + p.valor_juros - p.valor_desconto - p.valor_devolucao AS valor_liquido_pagamento,
	 rir.codigo_centro_financeiro,
	 cf.codigo_pai AS codigo_gerencia,
	 round(sum(ir.preco * rir.quantidade), 2) AS valor_recebimento,
	 d.valor + nf.desconto - nf.valor_frete - nf.valor_seguro - nf.valor_outras_despesas - nf.valor_ipi AS total_itens,
	     CASE
		 WHEN tc.chave::text = 'CONTRATO'::text THEN 0
		 WHEN tc.chave::text = 'CONTRATO_REEMBOLSAVEL'::text THEN 0
		 ELSE 1
	     END AS tipo_faturamento
	FROM nota n,
	 nota_fiscal nf,
	 despesa d,
	 parcela_nota pn,
	 pagamento p,
	 recebimento rec,
	 item_recebimento ir,
	 rateio_item_recebimento rir,
	 centro_financeiro cf,
	 unidade u,
	 tipo_cobranca tc
       WHERE nf.codigo = n.codigo AND d.codigo = n.codigo AND n.codigo = pn.codigo_nota AND pn.codigo = p.codigo AND rec.codigo_nota_fiscal = nf.codigo AND ir.codigo_recebimento = rec.codigo AND rir.codigo_item_recebimento = ir.codigo AND cf.codigo = rir.codigo_centro_financeiro AND u.codigo = cf.codigo_unidade AND tc.codigo = nf.codigo_tipo_cobranca AND ir.preco > 0::numeric
       GROUP BY n.codigo, cf.codigo_unidade, u.codigo_matriz, date_part('YEAR'::text, cf.data_encerramento), date_part('MONTH'::text, cf.data_encerramento), date_part('YEAR'::text, cf.data_previsao_primeira_parcela), date_part('MONTH'::text, cf.data_previsao_primeira_parcela), (date_part('YEAR'::text, p.data_pagamento)), (date_part('MONTH'::text, p.data_pagamento)), d.valor, nf.valor_frete, nf.valor_seguro, nf.desconto, nf.valor_outras_despesas, nf.valor_ipi, p.valor, (p.valor + p.valor_multa + p.valor_juros - p.valor_desconto - p.valor_devolucao), rir.codigo_centro_financeiro, cf.codigo_pai, tc.chave
     UNION
      SELECT n.codigo,
	 cf.codigo_unidade,
	 date_part('YEAR'::text, cf.data_previsao_primeira_parcela) AS ano_previsao_primeira_parcela,
	 date_part('MONTH'::text, cf.data_previsao_primeira_parcela) AS mes_previsao_primeira_parcela,
	 date_part('YEAR'::text, cf.data_encerramento) AS ano_encerramento,
	 date_part('MONTH'::text, cf.data_encerramento) AS mes_encerramento,
	 u.codigo_matriz,
	 date_part('YEAR'::text, p.data_pagamento) AS ano_pagamento,
	 date_part('MONTH'::text, p.data_pagamento) AS mes_pagamento,
	 d.valor AS valor_total_nf,
	 p.valor + p.valor_multa + p.valor_juros - p.valor_desconto - p.valor_devolucao AS valor_liquido_pagamento,
	 rnf.codigo_centro_financeiro,
	 COALESCE(cf.codigo_pai, rnf.codigo_centro_financeiro) AS codigo_gerencia,
	 round(sum(rnf.valor), 2) AS valor_recebimento,
	 d.valor + nf.desconto - nf.valor_frete - nf.valor_seguro - nf.valor_outras_despesas - nf.valor_ipi AS total_itens,
	     CASE
		 WHEN tc.chave::text = 'CONTRATO'::text THEN 0
		 WHEN tc.chave::text = 'CONTRATO_REEMBOLSAVEL'::text THEN 0
		 ELSE 1
	     END AS tipo_faturamento
	FROM nota n,
	 nota_fiscal nf,
	 despesa d,
	 parcela_nota pn,
	 pagamento p,
	 item_nota_fiscal inf,
	 rateio_nota_fiscal rnf,
	 centro_financeiro cf,
	 unidade u,
	 tipo_cobranca tc
       WHERE nf.codigo = n.codigo AND d.codigo = n.codigo AND n.codigo = pn.codigo_nota AND pn.codigo = p.codigo AND inf.codigo_pagamento = nf.codigo AND rnf.codigo_item_nota_fiscal = inf.codigo AND cf.codigo = rnf.codigo_centro_financeiro AND u.codigo = cf.codigo_unidade AND tc.codigo = nf.codigo_tipo_cobranca
       GROUP BY n.codigo, cf.codigo_unidade, u.codigo_matriz, date_part('YEAR'::text, cf.data_encerramento), date_part('MONTH'::text, cf.data_encerramento), date_part('YEAR'::text, cf.data_previsao_primeira_parcela), date_part('MONTH'::text, cf.data_previsao_primeira_parcela), (date_part('YEAR'::text, p.data_pagamento)), (date_part('MONTH'::text, p.data_pagamento)), d.valor, nf.valor_frete, nf.valor_seguro, nf.desconto, nf.valor_outras_despesas, nf.valor_ipi, p.valor, (p.valor + p.valor_multa + p.valor_juros - p.valor_desconto - p.valor_devolucao), rnf.codigo_centro_financeiro, cf.codigo_pai, tc.chave
     UNION
      SELECT n.codigo,
	 cf.codigo_unidade,
	 date_part('YEAR'::text, cf.data_previsao_primeira_parcela) AS ano_previsao_primeira_parcela,
	 date_part('MONTH'::text, cf.data_previsao_primeira_parcela) AS mes_previsao_primeira_parcela,
	 date_part('YEAR'::text, cf.data_encerramento) AS ano_encerramento,
	 date_part('MONTH'::text, cf.data_encerramento) AS mes_encerramento,
	 u.codigo_matriz,
	 date_part('YEAR'::text, p.data_pagamento) AS ano_pagamento,
	 date_part('MONTH'::text, p.data_pagamento) AS mes_pagamento,
	 d.valor AS valor_total_nf,
	 p.valor + p.valor_multa + p.valor_juros - p.valor_desconto AS valor_liquido_pagamento,
	 rnp.codigo_centro_financeiro,
	 COALESCE(cf.codigo_pai, rnp.codigo_centro_financeiro) AS codigo_gerencia,
	 round(sum(rnp.valor), 2) AS valor_recebimento,
	 d.valor AS total_itens,
	 0 AS tipo_faturamento
	FROM nota n,
	 nota_pagamento np,
	 despesa d,
	 parcela_nota pn,
	 pagamento p,
	 rateio_nota_pagamento rnp,
	 centro_financeiro cf,
	 unidade u
       WHERE np.codigo = n.codigo AND d.codigo = n.codigo AND n.codigo = pn.codigo_nota AND pn.codigo = p.codigo AND rnp.codigo_nota_pagamento = np.codigo AND cf.codigo = rnp.codigo_centro_financeiro AND u.codigo = cf.codigo_unidade
       GROUP BY n.codigo, cf.codigo_unidade, u.codigo_matriz, date_part('YEAR'::text, cf.data_encerramento), date_part('MONTH'::text, cf.data_encerramento), date_part('YEAR'::text, cf.data_previsao_primeira_parcela), date_part('MONTH'::text, cf.data_previsao_primeira_parcela), (date_part('YEAR'::text, p.data_pagamento)), (date_part('MONTH'::text, p.data_pagamento)), d.valor, p.valor, (p.valor + p.valor_multa + p.valor_juros - p.valor_desconto), rnp.codigo_centro_financeiro, cf.codigo_pai
       ORDER BY 1, 14 DESC, 3, 4
  WITH DATA;

  ALTER TABLE public.vw_custos_gastos_previstos
    OWNER TO sgi;
  COMMENT ON MATERIALIZED VIEW public.vw_custos_gastos_previstos
    IS 'Atualizada SGINV-2434 / versão 5.13';
