/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2019-09-25 19:04                                */
/* ---------------------------------------------------------------------- */


/* ---------------------------------------------------------------------- */
/* Drop views                                                             */
/* ---------------------------------------------------------------------- */


DROP VIEW public.vw_custos_receita_realizada;
DROP VIEW public.vw_custos_receita_prevista;
DROP VIEW public.vw_custos_faturamento_realizado;
DROP VIEW public.vw_custos_faturamento_previsto;



/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_cfop_transporte;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_municipio_ect;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_nota_associada_cancelamento;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_nota_fiscal;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_resp_cad_prefeitura;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_resp_canc_numeracao;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_resp_envio_rfb;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_resp_nota_associada_cancelamento;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_resp_numeracao;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_resp_ultimo_arquivo;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_servico_tipo;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_transportadora;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_transportadora_municipio;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_unidade;

ALTER TABLE public.item_nota_saida		DROP CONSTRAINT fk_item_nota_saida_nota_saida;

ALTER TABLE public.faturamento_etapa	DROP CONSTRAINT fk_faturamento_etapa_nota_saida;

ALTER TABLE public.nota_saida_parcela	DROP CONSTRAINT fk_nota_saida_parcela_nota_saida;

ALTER TABLE public.item_lote_contabil	DROP CONSTRAINT fk_item_lote_contabil_nota_saida;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "nota_saida"                                   */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.nota_saida DROP CONSTRAINT pk_nota_saida;

/* Create "new" table */
CREATE TABLE public.nota_saida_TMP (
    codigo									BIGINT									NOT NULL,
    codigo_emitente							BIGINT									NOT NULL,
    codigo_transportadora					BIGINT,
    codigo_municipio_transportadora			BIGINT,
    codigo_cfop_transporte					NUMERIC(4),
    codigo_municipio_transporte				BIGINT,
    codigo_resp_ultimo_arquivo				BIGINT,
    codigo_resp_envio_rfb					BIGINT,
    codigo_resp_numeracao					BIGINT,
    codigo_resp_canc_numeracao				BIGINT,
    codigo_resp_cad_prefeitura				BIGINT,
    codigo_servico_tipo						BIGINT,
    codigo_nota_associada_cancelamento		BIGINT,
    codigo_resp_nota_associada_cancelamento	BIGINT,
    isento_icms								BOOLEAN					DEFAULT false	NOT NULL,
    iss_retido_cliente						BOOLEAN					DEFAULT false	NOT NULL,
    iss_retido_cliente_recebimento			BOOLEAN,
    inscr_suframa							CHARACTER VARYING(9),
    tipo_documento_transportadora			INTEGER,
    documento_transportadora				CHARACTER VARYING(18),
    nome_transportadora						CHARACTER VARYING(100),
    inscricao_estadual_transportadora		CHARACTER VARYING(20),
    isento_icms_transportadora				BOOLEAN,
    endereco_transportadora					CHARACTER VARYING(255),
    icms_base_calculo_transporte			NUMERIC(15,2),
    icms_aliquota_transporte				NUMERIC(4,2),
    info_complementares_contribuinte		CHARACTER VARYING(2000),
    data_resp_ultimo_arquivo				TIMESTAMP,
    data_resp_envio_rfb						TIMESTAMP,
    data_resp_numeracao						TIMESTAMP,
    data_resp_canc_numeracao				TIMESTAMP,
    data_resp_cad_prefeitura				TIMESTAMP,
    data_resp_nota_associada_cancelamento	TIMESTAMP,
    protocolo_entrega						CHARACTER VARYING(25));

/* Move data from "old" table to "new" table */
INSERT INTO public.nota_saida_TMP
    (codigo,codigo_emitente,codigo_transportadora,codigo_municipio_transportadora,codigo_cfop_transporte,codigo_municipio_transporte,codigo_resp_ultimo_arquivo,codigo_resp_envio_rfb,codigo_resp_numeracao,codigo_resp_canc_numeracao,codigo_resp_cad_prefeitura,codigo_servico_tipo,codigo_nota_associada_cancelamento,codigo_resp_nota_associada_cancelamento,isento_icms,iss_retido_cliente,inscr_suframa,tipo_documento_transportadora,documento_transportadora,nome_transportadora,inscricao_estadual_transportadora,isento_icms_transportadora,endereco_transportadora,icms_base_calculo_transporte,icms_aliquota_transporte,info_complementares_contribuinte,data_resp_ultimo_arquivo,data_resp_envio_rfb,data_resp_numeracao,data_resp_canc_numeracao,data_resp_cad_prefeitura,data_resp_nota_associada_cancelamento,protocolo_entrega)
SELECT
    codigo,codigo_emitente,codigo_transportadora,codigo_municipio_transportadora,codigo_cfop_transporte,codigo_municipio_transporte,codigo_resp_ultimo_arquivo,codigo_resp_envio_rfb,codigo_resp_numeracao,codigo_resp_canc_numeracao,codigo_resp_cad_prefeitura,codigo_servico_tipo,codigo_nota_associada_cancelamento,codigo_resp_nota_associada_cancelamento,isento_icms,iss_retido_cliente,inscr_suframa,tipo_documento_transportadora,documento_transportadora,nome_transportadora,inscricao_estadual_transportadora,isento_icms_transportadora,endereco_transportadora,icms_base_calculo_transporte,icms_aliquota_transporte,info_complementares_contribuinte,data_resp_ultimo_arquivo,data_resp_envio_rfb,data_resp_numeracao,data_resp_canc_numeracao,data_resp_cad_prefeitura,data_resp_nota_associada_cancelamento,protocolo_entrega
FROM public.nota_saida;

/* Drop indexes */
DROP INDEX public.ix_nota_saida_cfop_transporte;
DROP INDEX public.ix_nota_saida_emitente;
DROP INDEX public.ix_nota_saida_municipio_transportadora;
DROP INDEX public.ix_nota_saida_municipio_transporte;
DROP INDEX public.ix_nota_saida_nota_associada_cancelamento;
DROP INDEX public.ix_nota_saida_resp_cad_prefeitura;
DROP INDEX public.ix_nota_saida_resp_canc_numeracao;
DROP INDEX public.ix_nota_saida_resp_envio_rfb;
DROP INDEX public.ix_nota_saida_resp_nota_associada_cancelamento;
DROP INDEX public.ix_nota_saida_resp_numeracao;
DROP INDEX public.ix_nota_saida_resp_ultimo_arquivo;
DROP INDEX public.ix_nota_saida_servico_tipo;
DROP INDEX public.ix_nota_saida_transportadora;

/* Drop "old" table  */
DROP TABLE public.nota_saida;

/* Rename "old" table to "new" table */
ALTER TABLE public.nota_saida_TMP
	RENAME TO nota_saida;

/* Create primary key */
ALTER TABLE public.nota_saida
	ADD CONSTRAINT pk_nota_saida
    PRIMARY KEY (codigo);

/* Create indexes */
CREATE INDEX ix_nota_saida_emitente							ON public.nota_saida (codigo_emitente);
CREATE INDEX ix_nota_saida_transportadora					ON public.nota_saida (codigo_transportadora);
CREATE INDEX ix_nota_saida_municipio_transportadora			ON public.nota_saida (codigo_municipio_transportadora);
CREATE INDEX ix_nota_saida_cfop_transporte					ON public.nota_saida (codigo_cfop_transporte);
CREATE INDEX ix_nota_saida_municipio_transporte				ON public.nota_saida (codigo_municipio_transporte);
CREATE INDEX ix_nota_saida_resp_ultimo_arquivo				ON public.nota_saida (codigo_resp_ultimo_arquivo);
CREATE INDEX ix_nota_saida_resp_envio_rfb					ON public.nota_saida (codigo_resp_envio_rfb);
CREATE INDEX ix_nota_saida_resp_numeracao					ON public.nota_saida (codigo_resp_numeracao);
CREATE INDEX ix_nota_saida_resp_canc_numeracao				ON public.nota_saida (codigo_resp_canc_numeracao);
CREATE INDEX ix_nota_saida_resp_cad_prefeitura				ON public.nota_saida (codigo_resp_cad_prefeitura);
CREATE INDEX ix_nota_saida_servico_tipo						ON public.nota_saida (codigo_servico_tipo);
CREATE INDEX ix_nota_saida_nota_associada_cancelamento		ON public.nota_saida (codigo_nota_associada_cancelamento);
CREATE INDEX ix_nota_saida_resp_nota_associada_cancelamento	ON public.nota_saida (codigo_resp_nota_associada_cancelamento);

/* Comments */
COMMENT ON TABLE public.nota_saida											IS 'Notas fiscais de saída.';
COMMENT ON COLUMN public.nota_saida.codigo									IS 'Código interno (código interno da nota fiscal e código interno da nota) da nota fiscal de saída.';
COMMENT ON COLUMN public.nota_saida.codigo_emitente							IS 'Código interno do emitente (empresa/unidade).';
COMMENT ON COLUMN public.nota_saida.codigo_transportadora					IS 'Código interno (código interno da pessoa) da transportadora. Se estiver no cadastro de pessoas, terá este código, senão os dados relacionados ao nome e ao endereço devem estar preenchidos.';
COMMENT ON COLUMN public.nota_saida.codigo_municipio_transportadora			IS 'Código interno do município do endereço da transportadora.';
COMMENT ON COLUMN public.nota_saida.codigo_cfop_transporte					IS 'Código interno do CFOP de transporte.';
COMMENT ON COLUMN public.nota_saida.codigo_municipio_transporte				IS 'Código interno do município do ICMS de transporte.';
COMMENT ON COLUMN public.nota_saida.codigo_resp_ultimo_arquivo				IS 'Código interno (código interno da pessoa) responsável pela criação do último arquivo para envio à RFB.';
COMMENT ON COLUMN public.nota_saida.codigo_resp_envio_rfb					IS 'Código interno (código interno da pessoa) responsável pela confirmação do envio do arquivo à RFB.';
COMMENT ON COLUMN public.nota_saida.codigo_resp_numeracao					IS 'Código interno (código interno da pessoa) responsável pela última numeração da NF (faturamento).';
COMMENT ON COLUMN public.nota_saida.codigo_resp_canc_numeracao				IS 'Código interno (código interno da pessoa) responsável pelo último cancelamento de numeração da NF (faturamento).';
COMMENT ON COLUMN public.nota_saida.codigo_resp_cad_prefeitura				IS 'Código interno (código interno da pessoa) responsável pelo cadastramento da NF no site da Prefeitura (faturamento).';
COMMENT ON COLUMN public.nota_saida.codigo_servico_tipo						IS 'Código interno do tipo de serviço.';
COMMENT ON COLUMN public.nota_saida.codigo_nota_associada_cancelamento		IS 'Código da nota associada (nova NF faturada), quando esta nota de saída estiver cancelada.';
COMMENT ON COLUMN public.nota_saida.codigo_resp_nota_associada_cancelamento	IS 'Código da pessoa responsável pela última associada da nota (nova NF faturada), quando esta nota de saída estiver cancelada.';
COMMENT ON COLUMN public.nota_saida.isento_icms								IS 'Indica se o destinatário é isento de ICMS ou não.';
COMMENT ON COLUMN public.nota_saida.iss_retido_cliente						IS 'Indica se o ISS será retido pelo cliente (true) ou não (false).';
COMMENT ON COLUMN public.nota_saida.iss_retido_cliente_recebimento			IS 'Indica se o ISS foi retido pelo cliente (true) ou não (false) no recebimento do tributo (na parcela em que foi retido).';
COMMENT ON COLUMN public.nota_saida.inscr_suframa							IS 'Código SUFRAMA do destinatário (fornecedor).';
COMMENT ON COLUMN public.nota_saida.tipo_documento_transportadora			IS 'Indica o tipo de documento (0 = CNPJ / 1 = CPF) da transportadora.';
COMMENT ON COLUMN public.nota_saida.documento_transportadora				IS 'Documento (CPF ou CNPJ) da transportadora.';
COMMENT ON COLUMN public.nota_saida.nome_transportadora						IS 'Nome (CPF) ou razão social (CNPJ) da transportadora.';
COMMENT ON COLUMN public.nota_saida.inscricao_estadual_transportadora		IS 'Inscrição estadual (apenas CNPJ) da transportadora.';
COMMENT ON COLUMN public.nota_saida.isento_icms_transportadora				IS 'Indica se a transportadora é isenta de ICMS ou não.';
COMMENT ON COLUMN public.nota_saida.endereco_transportadora					IS 'Endereço completo da transportadora.';
COMMENT ON COLUMN public.nota_saida.icms_base_calculo_transporte			IS 'Base de cálculo do ICMS de transporte.';
COMMENT ON COLUMN public.nota_saida.icms_aliquota_transporte				IS 'Alíquota do ICMS de transporte.';
COMMENT ON COLUMN public.nota_saida.info_complementares_contribuinte		IS 'Informações complementares de interesse do contribuinte.';
COMMENT ON COLUMN public.nota_saida.data_resp_ultimo_arquivo				IS 'Data/hora da criação do último arquivo para envio à RFB.';
COMMENT ON COLUMN public.nota_saida.data_resp_envio_rfb						IS 'Data/hora de confirmação do envio do arquivo à RFB.';
COMMENT ON COLUMN public.nota_saida.data_resp_numeracao						IS 'Data/hora da última numeração da NF (faturamento).';
COMMENT ON COLUMN public.nota_saida.data_resp_canc_numeracao				IS 'Data/hora do último cancelamento da numeração da NF (faturamento).';
COMMENT ON COLUMN public.nota_saida.data_resp_cad_prefeitura				IS 'Data/hora do cadastramento da NF no site da Prefeitura (faturamento).';
COMMENT ON COLUMN public.nota_saida.data_resp_nota_associada_cancelamento	IS 'Data/hora da última associação da nota (nova NF faturada), quando esta nota de saída estiver cancelada.';
COMMENT ON COLUMN public.nota_saida.protocolo_entrega						IS 'Protocolo (nº, data, nome, etc.) de entrega da NF ao cliente.';

/* Set owner */
ALTER TABLE public.nota_saida OWNER TO sgi;

/* Set data */
UPDATE	nota_saida
SET		iss_retido_cliente_recebimento = iss_retido_cliente;

/* Alter table */
ALTER TABLE public.nota_saida
	ALTER COLUMN iss_retido_cliente_recebimento SET NOT NULL;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_saida								ADD CONSTRAINT fk_nota_saida_nota_fiscal
    FOREIGN KEY (codigo)									REFERENCES public.nota_fiscal (codigo);
ALTER TABLE public.nota_saida								ADD CONSTRAINT fk_nota_saida_unidade
    FOREIGN KEY (codigo_emitente)							REFERENCES public.unidade (codigo);
ALTER TABLE public.nota_saida								ADD CONSTRAINT fk_nota_saida_transportadora_municipio
    FOREIGN KEY (codigo_municipio_transportadora)			REFERENCES public.municipio_ect (codigo);
ALTER TABLE public.nota_saida								ADD CONSTRAINT fk_nota_saida_cfop_transporte
    FOREIGN KEY (codigo_cfop_transporte)					REFERENCES public.cfop (codigo);
ALTER TABLE public.nota_saida								ADD CONSTRAINT fk_nota_saida_municipio_ect
    FOREIGN KEY (codigo_municipio_transporte)				REFERENCES public.municipio_ect (codigo);
ALTER TABLE public.nota_saida								ADD CONSTRAINT fk_nota_saida_transportadora
    FOREIGN KEY (codigo_transportadora)						REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida								ADD CONSTRAINT fk_nota_saida_resp_ultimo_arquivo
    FOREIGN KEY (codigo_resp_ultimo_arquivo)				REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida								ADD CONSTRAINT fk_nota_saida_resp_envio_rfb
    FOREIGN KEY (codigo_resp_envio_rfb)						REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida								ADD CONSTRAINT fk_nota_saida_servico_tipo
    FOREIGN KEY (codigo_servico_tipo)						REFERENCES public.servico_tipo (codigo);
ALTER TABLE public.nota_saida								ADD CONSTRAINT fk_nota_saida_resp_numeracao
    FOREIGN KEY (codigo_resp_numeracao)						REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida								ADD CONSTRAINT fk_nota_saida_resp_canc_numeracao
    FOREIGN KEY (codigo_resp_canc_numeracao)				REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida								ADD CONSTRAINT fk_nota_saida_resp_cad_prefeitura
    FOREIGN KEY (codigo_resp_cad_prefeitura)				REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida								ADD CONSTRAINT fk_nota_saida_resp_nota_associada_cancelamento
    FOREIGN KEY (codigo_resp_nota_associada_cancelamento)	REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida								ADD CONSTRAINT fk_nota_saida_nota_associada_cancelamento
    FOREIGN KEY (codigo_nota_associada_cancelamento)		REFERENCES public.nota_saida (codigo);

ALTER TABLE public.item_nota_saida							ADD CONSTRAINT fk_item_nota_saida_nota_saida
    FOREIGN KEY (codigo_nota_saida)							REFERENCES public.nota_saida (codigo);

ALTER TABLE public.faturamento_etapa						ADD CONSTRAINT fk_faturamento_etapa_nota_saida
    FOREIGN KEY (codigo_nota_saida)							REFERENCES public.nota_saida (codigo);

ALTER TABLE public.nota_saida_parcela						ADD CONSTRAINT fk_nota_saida_parcela_nota_saida
    FOREIGN KEY (codigo_nota_saida)							REFERENCES public.nota_saida (codigo);

ALTER TABLE public.item_lote_contabil						ADD CONSTRAINT fk_item_lote_contabil_nota_saida
    FOREIGN KEY (codigo_nota_saida)							REFERENCES public.nota_saida (codigo);


/* ---------------------------------------------------------------------- */
/* Create views                                                             */
/* ---------------------------------------------------------------------- */


CREATE OR REPLACE VIEW public.vw_custos_receita_realizada
AS SELECT cf.codigo_unidade,
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


CREATE OR REPLACE VIEW public.vw_custos_receita_prevista
AS SELECT cf.codigo_unidade,
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



CREATE OR REPLACE VIEW public.vw_custos_faturamento_previsto
AS SELECT cf.codigo_unidade,
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




CREATE OR REPLACE VIEW public.vw_custos_faturamento_realizado
AS SELECT cf.codigo_unidade,
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
