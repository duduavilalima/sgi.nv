/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2020-11-18 14:00                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Desfazer ajustes                                                       */
/* ---------------------------------------------------------------------- */
/*
ALTER TABLE public.faturamento_etapa
	DROP CONSTRAINT fk_faturamento_etapa_nota_saida;

ALTER TABLE public.faturamento_etapa
	DROP COLUMN codigo_nota_debito;

ALTER TABLE public.faturamento
	DROP COLUMN tipo_documento_faturamento;

DROP TABLE public.nota_debito;

DROP SEQUENCE public.seq_nota_debito;

DROP SEQUENCE public.seq_motivo_retorno_documento_nota_debito;

ALTER TABLE public.unidade
	DROP COLUMN proxima_nf_material,
    DROP COLUMN proxima_nd;

DELETE FROM aplicacao_motivo_retorno WHERE chave IN ('faturamento-cancelar_nd', 'faturamento-rever_canc_nd');
*/

/* ---------------------------------------------------------------------- */
/* Drop views                                                             */
/* ---------------------------------------------------------------------- */
DROP VIEW IF EXISTS public.vw_custos_receita_prevista;
DROP VIEW IF EXISTS public.vw_custos_faturamento_previsto;
DROP VIEW IF EXISTS public.vw_custos_receita_realizada;
DROP VIEW IF EXISTS public.vw_custos_faturamento_realizado;
DROP MATERIALIZED VIEW IF EXISTS public.vw_compras_itens_por_natureza;
DROP MATERIALIZED VIEW IF EXISTS public.vw_custos_gastos_previstos;
DROP MATERIALIZED VIEW IF EXISTS public.vw_total_credito_por_nf;
DROP MATERIALIZED VIEW IF EXISTS public.vw_inrf_itens_nota_fiscal;
DROP VIEW IF EXISTS public.vw_inrf_filiais;
DROP VIEW IF EXISTS public.vw_inrf_matrizes;

/* ---------------------------------------------------------------------- */
/* Create sequences                                                       */
/* ---------------------------------------------------------------------- */
CREATE SEQUENCE public.seq_nota_debito							INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE public.seq_motivo_retorno_documento_nota_debito	INCREMENT 1 MINVALUE 1 START 1;

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_cliente;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_centro_financeiro;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_conta_corrente;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_tipo_cobranca_faturamento;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_cancelamento;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_municipio_ect;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_servico_tipo;

ALTER TABLE public.faturamento_etapa					DROP CONSTRAINT fk_faturamento_etapa_cancelamento;
ALTER TABLE public.faturamento_etapa					DROP CONSTRAINT fk_faturamento_etapa_nota_saida;
ALTER TABLE public.faturamento_etapa					DROP CONSTRAINT fk_faturamento_etapa_resp_faturamento;
ALTER TABLE public.faturamento_etapa					DROP CONSTRAINT fk_faturamento_etapa_ult_alteracao_valor;
ALTER TABLE public.faturamento_etapa					DROP CONSTRAINT fk_faturamento_etapa_faturamento;

ALTER TABLE public.faturamento_aliquota					DROP CONSTRAINT fk_faturamento_aliquota_faturamento;

ALTER TABLE public.motivo_retorno_documento_faturamento	DROP CONSTRAINT fk_motivo_retorno_documento_faturamento_faturamento;

ALTER TABLE public.motivo_retorno_documento_etapa		DROP CONSTRAINT fk_motivo_retorno_documento_etapa_faturamento_etapa;

ALTER TABLE public.faturamento_retencao					DROP CONSTRAINT fk_faturamento_retencao_faturamento;

ALTER TABLE public.faturamento_contato					DROP CONSTRAINT fk_faturamento_contato_faturamento;

ALTER TABLE public.unidade								DROP CONSTRAINT fk_unidade_matriz;
ALTER TABLE public.unidade								DROP CONSTRAINT fk_unidade_pessoa_juridica;

ALTER TABLE public.gerencia								DROP CONSTRAINT fk_gerencia_unidade;

ALTER TABLE public.diretoria							DROP CONSTRAINT fk_diretoria_unidade;

ALTER TABLE public.centro_financeiro					DROP CONSTRAINT fk_centro_financeiro_unidade;

ALTER TABLE public.nota									DROP CONSTRAINT fk_nota_unidade;

ALTER TABLE public.ordem_compra							DROP CONSTRAINT fk_ordem_compra_unidade;

ALTER TABLE public.lote_interface						DROP CONSTRAINT fk_lote_interface_unidade;

ALTER TABLE public.relacao_bancaria						DROP CONSTRAINT fk_relacao_bancaria_unidade;

ALTER TABLE public.lancamento							DROP CONSTRAINT fk_lancamento_unidade;

ALTER TABLE public.nota_saida							DROP CONSTRAINT fk_nota_saida_unidade;

ALTER TABLE public.contrato								DROP CONSTRAINT fk_contrato_unidade;

ALTER TABLE public.conta_contabil						DROP CONSTRAINT fk_conta_contabil_unidade;

ALTER TABLE public.processo								DROP CONSTRAINT fk_processo_unidade;

ALTER TABLE public.previsao_fluxo_caixa					DROP CONSTRAINT fk_previsao_fluxo_caixa_unidade;

ALTER TABLE public.previsao_custos						DROP CONSTRAINT fk_previsao_custos_unidade;

ALTER TABLE public.reinf								DROP CONSTRAINT fk_reinf_unidade;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "faturamento"                                  */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.faturamento DROP CONSTRAINT pk_faturamento;
ALTER TABLE public.faturamento DROP CONSTRAINT ck_faturamento_situacao;
ALTER TABLE public.faturamento DROP CONSTRAINT ck_faturamento_tipo;
ALTER TABLE public.faturamento DROP CONSTRAINT ck_faturamento_tipo_fornecimento_nf;

/* Create "new" table */
CREATE TABLE public.faturamento_TMP (
    codigo								BIGINT			DEFAULT nextval('seq_faturamento')	NOT NULL,
    codigo_cliente						BIGINT												NOT NULL,
    codigo_centro_financeiro			BIGINT												NOT NULL,
    codigo_conta_corrente				BIGINT												NOT NULL,
    codigo_tipo_cobranca				BIGINT												NOT NULL,
    codigo_servico_tipo					BIGINT,
    codigo_cidade_prestacao_servicos	BIGINT,
    codigo_cancelamento					BIGINT,
    situacao							SMALLINT		DEFAULT 1							NOT NULL,
    tipo								SMALLINT		DEFAULT 1							NOT NULL,
    identificacao						CHARACTER VARYING(100),
    valor								NUMERIC(15,2)										NOT NULL,
    tipo_fornecimento_nf				SMALLINT		DEFAULT 1							NOT NULL,
    tipo_documento_faturamento			SMALLINT		DEFAULT 0							NOT NULL,
    percentual_servico					NUMERIC(5,2)										NOT NULL,
    tipo_documento_pagamento			SMALLINT											NOT NULL,
    iss_retido_cliente					BOOLEAN												NOT NULL,
    resumo								CHARACTER VARYING(2000)								NOT NULL,
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

/* Move data from "old" table to "new" table */
INSERT INTO public.faturamento_TMP
    (codigo,codigo_cliente,codigo_centro_financeiro,codigo_conta_corrente,codigo_tipo_cobranca,codigo_servico_tipo,codigo_cidade_prestacao_servicos,codigo_cancelamento,situacao,tipo,identificacao,valor,tipo_fornecimento_nf,percentual_servico,tipo_documento_pagamento,iss_retido_cliente,resumo,discriminacao_inicial,discriminacao_central,discriminacao_final,periodo,data_vencimento_1,data_vencimento_2,data_cancelamento,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_cliente,codigo_centro_financeiro,codigo_conta_corrente,codigo_tipo_cobranca,codigo_servico_tipo,codigo_cidade_prestacao_servicos,codigo_cancelamento,situacao,tipo,identificacao,valor,tipo_fornecimento_nf,percentual_servico,tipo_documento_pagamento,iss_retido_cliente,resumo,discriminacao_inicial,discriminacao_central,discriminacao_final,periodo,data_vencimento_1,data_vencimento_2,data_cancelamento,criador,data_criacao,atualizador,data_atualizacao
FROM public.faturamento;

/* Drop indexes */
DROP INDEX public.ix_faturamento_cliente;
DROP INDEX public.ix_faturamento_centro_financeiro;
DROP INDEX public.ix_faturamento_conta_corrente;
DROP INDEX public.ix_faturamento_tipo_cobranca;
DROP INDEX public.ix_faturamento_cidade_prestacao_servicos;
DROP INDEX public.ix_faturamento_servico_tipo;
DROP INDEX public.ix_faturamento_cancelamento;

/* Drop "old" table */
DROP TABLE public.faturamento;

/* Rename "old" table to "new" table */
ALTER TABLE public.faturamento_TMP
	RENAME TO faturamento;

/* Create primary key */
ALTER TABLE public.faturamento
	ADD CONSTRAINT pk_faturamento 
    PRIMARY KEY (codigo);

/* Create constraints */
ALTER TABLE public.faturamento
	ADD CONSTRAINT ck_faturamento_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1, 2, 3, 4]));

ALTER TABLE public.faturamento
	ADD CONSTRAINT ck_faturamento_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1]));

ALTER TABLE public.faturamento
	ADD CONSTRAINT ck_faturamento_tipo_documento_faturamento 
    CHECK (tipo_documento_faturamento = ANY (ARRAY[0, 1]));

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
COMMENT ON COLUMN public.faturamento.tipo_documento_faturamento			IS 'Tipo de documento para faturamento: 0 = NF / 1 = ND (nota de débito).';
COMMENT ON COLUMN public.faturamento.percentual_servico					IS 'Percentual de serviço/mão de obra. Se 100%, não há divisão entre material e serviço. Se < 100%, material será a diferença. Se 0%, só material.';
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
ALTER TABLE public.faturamento OWNER TO sgi;

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
    codigo_nota_debito			BIGINT,
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
    (codigo,codigo_faturamento,codigo_nota_saida,codigo_resp_faturamento,codigo_cancelamento,codigo_ult_alteracao_valor,numero,situacao,inicio,termino,valor,valor_permuta,valor_desconto,valor_multa,valor_juros,descricao,discriminacao_inicial,discriminacao_central,discriminacao_final,exibe_retencao_contratual,exibe_retencao_acessoria,exibe_permuta,data_faturamento,data_cancelamento,ult_alteracao_valor,data_ult_alteracao_valor,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_faturamento,codigo_nota_saida,codigo_resp_faturamento,codigo_cancelamento,codigo_ult_alteracao_valor,numero,situacao,inicio,termino,valor,valor_permuta,valor_desconto,valor_multa,valor_juros,descricao,discriminacao_inicial,discriminacao_central,discriminacao_final,exibe_retencao_contratual,exibe_retencao_acessoria,exibe_permuta,data_faturamento,data_cancelamento,ult_alteracao_valor,data_ult_alteracao_valor,criador,data_criacao,atualizador,data_atualizacao
FROM public.faturamento_etapa;

/* Drop indexes */
DROP INDEX public.ix_faturamento_etapa_cancelamento;
DROP INDEX public.ix_faturamento_etapa_faturamento;
DROP INDEX public.ix_faturamento_etapa_nota_saida;
DROP INDEX public.ix_faturamento_etapa_resp_faturamento;
DROP INDEX public.ix_faturamento_etapa_ult_alteracao_valor;

/* Drop "old" table */
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

/* Create indexes */
CREATE INDEX ix_faturamento_etapa_faturamento			ON public.faturamento_etapa (codigo_faturamento);
CREATE INDEX ix_faturamento_etapa_nota_saida			ON public.faturamento_etapa (codigo_nota_saida);
CREATE INDEX ix_faturamento_etapa_nota_debito			ON public.faturamento_etapa (codigo_nota_debito);
CREATE INDEX ix_faturamento_etapa_resp_faturamento		ON public.faturamento_etapa (codigo_resp_faturamento);
CREATE INDEX ix_faturamento_etapa_cancelamento			ON public.faturamento_etapa (codigo_cancelamento);
CREATE INDEX ix_faturamento_etapa_ult_alteracao_valor	ON public.faturamento_etapa (codigo_ult_alteracao_valor);

/* Comments */
COMMENT ON TABLE public.faturamento_etapa								IS 'Etapa do faturamento. Cada etapa será uma nota fiscal.';
COMMENT ON COLUMN public.faturamento_etapa.codigo						IS 'Código interno da etapa. Sequência: seq_faturamento_etapa.';
COMMENT ON COLUMN public.faturamento_etapa.codigo_faturamento			IS 'Código interno do faturamento.';
COMMENT ON COLUMN public.faturamento_etapa.codigo_nota_saida			IS 'Código interno da nota de saída associada.';
COMMENT ON COLUMN public.faturamento_etapa.codigo_nota_debito			IS 'Código interno da nota de débito associada.';
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
/* Add table "nota_debito"                                                */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.nota_debito (
    codigo								BIGINT			DEFAULT nextval('seq_nota_debito')	NOT NULL,
    codigo_empresa						BIGINT												NOT NULL,
    codigo_centro_financeiro			BIGINT												NOT NULL,
    codigo_cliente						BIGINT												NOT NULL,
    codigo_servico_tipo					BIGINT,
    codigo_cidade_prestacao_servicos	BIGINT,
    codigo_conta_corrente_prevista		BIGINT												NOT NULL,
    codigo_conta_corrente_recebimento	BIGINT,
    codigo_resp_numeracao				BIGINT,
    codigo_resp_canc_numeracao			BIGINT,
    codigo_resp_recebimento				BIGINT,
    codigo_resp_canc_recebimento		BIGINT,
    codigo_resp_cancelamento			BIGINT,
    codigo_resp_reversao_cancelamento	BIGINT,
    numero								CHARACTER VARYING(12)								NOT NULL,
    situacao							SMALLINT		DEFAULT 0							NOT NULL,
    data_emissao						DATE												NOT NULL,
    data_vencimento						DATE												NOT NULL,
    data_pagamento						DATE												NOT NULL,
    data_agendamento					DATE												NOT NULL,
    data_contabilizacao					DATE,
    descricao							CHARACTER VARYING(2000)								NOT NULL,
    valor_bruto							NUMERIC(11,2)	DEFAULT 0.00						NOT NULL,
    valor_tributo_previsto				NUMERIC(9,2)	DEFAULT 0.00						NOT NULL,
    valor_desconto_previsto				NUMERIC(9,2)	DEFAULT 0.00						NOT NULL,
    valor_devolucao_previsto			NUMERIC(9,2)	DEFAULT 0.00						NOT NULL,
    valor_liquido_previsto				NUMERIC(11,2)	DEFAULT 0.00						NOT NULL,
    valor_multa							NUMERIC(9,2)	DEFAULT 0.00						NOT NULL,
    valor_juros							NUMERIC(9,2)	DEFAULT 0.00						NOT NULL,
    valor_tributo_recebido				NUMERIC(9,2)	DEFAULT 0.00						NOT NULL,
    valor_desconto_recebido				NUMERIC(9,2)	DEFAULT 0.00						NOT NULL,
    valor_devolucao_recebido			NUMERIC(9,2)	DEFAULT 0.00						NOT NULL,
    valor_liquido_recebido				NUMERIC(11,2)	DEFAULT 0.00						NOT NULL,
    tipo_documento_previsto				SMALLINT,
    tipo_documento_recebimento			SMALLINT,
    protocolo_entrega					CHARACTER VARYING(25),
    data_resp_numeracao					TIMESTAMP,
    data_resp_canc_numeracao			TIMESTAMP,
    data_resp_recebimento				TIMESTAMP,
    data_resp_canc_recebimento			TIMESTAMP,
    data_resp_cancelamento				TIMESTAMP,
    data_resp_reversao_cancelamento		TIMESTAMP,
    criador								CHARACTER VARYING(255),
    data_criacao						TIMESTAMP,
    atualizador							CHARACTER VARYING(255),
    data_atualizacao					TIMESTAMP,
    CONSTRAINT pk_nota_debito PRIMARY KEY (codigo)
);

/* Create constraints */
ALTER TABLE public.nota_debito
	ADD CONSTRAINT ck_nota_debito_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1, 2, 3]));

ALTER TABLE public.nota_debito
	ADD CONSTRAINT ck_nota_debito_tipo_documento_previsto 
    CHECK (tipo_documento_previsto = ANY (ARRAY[0, 1]));

ALTER TABLE public.nota_debito
	ADD CONSTRAINT ck_nota_debito_tipo_documento_recebimento 
    CHECK (tipo_documento_recebimento = ANY (ARRAY[0, 1]));

/* Create indexes */
CREATE INDEX ix_nota_debito_empresa						ON public.nota_debito (codigo_empresa);
CREATE INDEX ix_nota_debito_centro_financeiro			ON public.nota_debito (codigo_centro_financeiro);
CREATE INDEX ix_nota_debito_cliente						ON public.nota_debito (codigo_cliente);
CREATE INDEX ix_nota_debito_servico_tipo				ON public.nota_debito (codigo_servico_tipo);
CREATE INDEX ix_nota_debito_cidade_prestacao_servicos	ON public.nota_debito (codigo_cidade_prestacao_servicos);
CREATE INDEX ix_nota_debito_conta_corrente_prevista		ON public.nota_debito (codigo_conta_corrente_prevista);
CREATE INDEX ix_nota_debito_conta_corrente_recebimento	ON public.nota_debito (codigo_conta_corrente_recebimento);
CREATE INDEX ix_nota_debito_resp_numeracao				ON public.nota_debito (codigo_resp_numeracao);
CREATE INDEX ix_nota_debito_resp_canc_numeracao			ON public.nota_debito (codigo_resp_canc_numeracao);
CREATE INDEX ix_nota_debito_resp_recebimento			ON public.nota_debito (codigo_resp_recebimento);
CREATE INDEX ix_nota_debito_resp_canc_recebimento		ON public.nota_debito (codigo_resp_canc_recebimento);
CREATE INDEX ix_nota_debito_resp_cancelamento			ON public.nota_debito (codigo_resp_cancelamento);
CREATE INDEX ix_nota_debito_resp_reversao_cancelamento	ON public.nota_debito (codigo_resp_reversao_cancelamento);

/* Comments */
COMMENT ON TABLE public.nota_debito										IS 'Notas de débito (ND) relacionada ao Faturamento de itens que não podem ser incluídos em NF.';
COMMENT ON COLUMN public.nota_debito.codigo								IS 'Código interno da nota de débito. Sequência: seq_nota_debito.';
COMMENT ON COLUMN public.nota_debito.codigo_empresa						IS 'Código interno da empresa (código interno da unidade, código interno da pessoa) resposável pela emissão da nota de débito.';
COMMENT ON COLUMN public.nota_debito.codigo_centro_financeiro			IS 'Código interno do centro financeiro.';
COMMENT ON COLUMN public.nota_debito.codigo_cliente						IS 'Código interno do cliente (código interno da pessoa) para o qual a nota de débito foi emitida.';
COMMENT ON COLUMN public.nota_debito.codigo_servico_tipo				IS 'Código interno do tipo de serviço.';
COMMENT ON COLUMN public.nota_debito.codigo_cidade_prestacao_servicos	IS 'Código interno da cidade onde os serviços foram presatados e deve ser selecionada entre uma das cidades: cidade do endereço do contrato OU cidade do endereço da unidade de emissão.';
COMMENT ON COLUMN public.nota_debito.codigo_conta_corrente_prevista		IS 'Código interno da conta corrente para recebimento (prevista).';
COMMENT ON COLUMN public.nota_debito.codigo_conta_corrente_recebimento	IS 'Código interno da conta corrente para recebimento (recebida).';
COMMENT ON COLUMN public.nota_debito.codigo_resp_numeracao				IS 'Código interno (código interno da pessoa) responsável pela última numeração da ND.';
COMMENT ON COLUMN public.nota_debito.codigo_resp_canc_numeracao			IS 'Código interno (código interno da pessoa) responsável pelo último cancelamento de numeração da ND.';
COMMENT ON COLUMN public.nota_debito.codigo_resp_recebimento			IS 'Código interno (código interno da pessoa) do responsável pelo último recebimento.';
COMMENT ON COLUMN public.nota_debito.codigo_resp_canc_recebimento		IS 'Código interno (código interno da pessoa) do responsável pelo último cancelamento do recebimento.';
COMMENT ON COLUMN public.nota_debito.codigo_resp_cancelamento			IS 'Código interno (código interno da pessoa) do responsável pelo último cancelamento.';
COMMENT ON COLUMN public.nota_debito.codigo_resp_reversao_cancelamento	IS 'Código interno (código interno da pessoa) do responsável pelo última reversão do cancelamento.';
COMMENT ON COLUMN public.nota_debito.numero								IS 'Número da nota de débito.';
COMMENT ON COLUMN public.nota_debito.situacao							IS 'Situação da nota de débito: 0 = Aberta / 1 = Em recebimento / 2 = Recebida / 3 = Cancelada.';
COMMENT ON COLUMN public.nota_debito.data_emissao						IS 'Data de emissão.';
COMMENT ON COLUMN public.nota_debito.data_vencimento					IS 'Data de vencimento.';
COMMENT ON COLUMN public.nota_debito.data_pagamento						IS 'Data de pagamento (até o efetivo pagamento, será igual ao vencimento).';
COMMENT ON COLUMN public.nota_debito.data_agendamento					IS 'Data de agendamento do pagamento.';
COMMENT ON COLUMN public.nota_debito.data_contabilizacao				IS 'Data de contabilização.';
COMMENT ON COLUMN public.nota_debito.descricao							IS 'Discriminação dos serviços/materiais.';
COMMENT ON COLUMN public.nota_debito.valor_bruto						IS 'Valor bruto.';
COMMENT ON COLUMN public.nota_debito.valor_tributo_previsto				IS 'Valor previsto de retenção de tributos (emissão).';
COMMENT ON COLUMN public.nota_debito.valor_desconto_previsto			IS 'Valor previsto de desconto (emissão).';
COMMENT ON COLUMN public.nota_debito.valor_devolucao_previsto			IS 'Valor previsto de devolução (emissão).';
COMMENT ON COLUMN public.nota_debito.valor_liquido_previsto				IS 'Valor líquido previsto (valor buto - tributos - desconto + devolução, emissão).';
COMMENT ON COLUMN public.nota_debito.valor_multa						IS 'Valor da multa recebida.';
COMMENT ON COLUMN public.nota_debito.valor_juros						IS 'Valor de juros recebidos.';
COMMENT ON COLUMN public.nota_debito.valor_tributo_recebido				IS 'Valor retido de tributos (recebimento).';
COMMENT ON COLUMN public.nota_debito.valor_desconto_recebido			IS 'Valor de desconto (recebimento).';
COMMENT ON COLUMN public.nota_debito.valor_devolucao_recebido			IS 'Valor de devolução (recebimento).';
COMMENT ON COLUMN public.nota_debito.valor_liquido_recebido				IS 'Valor líquido (valor buto - tributos - desconto + devolução + multa + juros, recebimento).';
COMMENT ON COLUMN public.nota_debito.tipo_documento_previsto			IS 'Tipo de documento de pagamento (previsto): 0 = Boleto / 1 = Depósito em conta/transferência.';
COMMENT ON COLUMN public.nota_debito.tipo_documento_recebimento			IS 'Tipo de documento de pagamento (recebimento): 0 = Boleto / 1 = Depósito em conta/transferência.';
COMMENT ON COLUMN public.nota_debito.protocolo_entrega					IS 'Protocolo (nº, data, nome, etc.) de entrega da ND ao cliente.';
COMMENT ON COLUMN public.nota_debito.data_resp_numeracao				IS 'Data/hora da última numeração da ND.';
COMMENT ON COLUMN public.nota_debito.data_resp_canc_numeracao			IS 'Data/hora do último cancelamento da numeração da ND.';
COMMENT ON COLUMN public.nota_debito.data_resp_recebimento				IS 'Data/hora de confirmação do último recebimento.';
COMMENT ON COLUMN public.nota_debito.data_resp_canc_recebimento			IS 'Data/hora do último cancelamento do recebimento.';
COMMENT ON COLUMN public.nota_debito.data_resp_cancelamento				IS 'Data/hora do último cancelamento.';
COMMENT ON COLUMN public.nota_debito.data_resp_reversao_cancelamento	IS 'Data/hora da última reversão do cancelamento.';
COMMENT ON COLUMN public.nota_debito.criador							IS 'Responsável (login) pela criação da nota de devolução.';
COMMENT ON COLUMN public.nota_debito.data_criacao						IS 'Data/hora de criação da nota de devolução.';
COMMENT ON COLUMN public.nota_debito.atualizador						IS 'Responsável (login) pela última atualização da nota de devolução.';
COMMENT ON COLUMN public.nota_debito.data_atualizacao					IS 'Data/hora da última atualização da nota de devolução.';

/* Set owner */
ALTER TABLE public.nota_debito OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add table "motivo_retorno_documento_nota_debito"                       */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.motivo_retorno_documento_nota_debito (
    codigo							BIGINT	DEFAULT nextval('seq_motivo_retorno_documento_nota_debito')	NOT NULL,
    codigo_nota_debito				BIGINT																NOT NULL,
    codigo_motivo_retorno_documento	BIGINT																NOT NULL,
    CONSTRAINT pk_motivo_retorno_documento_nota_debito PRIMARY KEY (codigo)
);

/* Create indexes */
CREATE INDEX ix_mot_ret_documento_nota_debito_nota_debito						ON public.motivo_retorno_documento_nota_debito (codigo_nota_debito);
CREATE INDEX ix_mot_ret_documento_nota_debito_mot_ret_documento					ON public.motivo_retorno_documento_nota_debito (codigo_motivo_retorno_documento);
CREATE UNIQUE INDEX uq_mot_ret_documento_nota_debito_mot_ret_doc_nota_debito	ON public.motivo_retorno_documento_nota_debito (codigo_motivo_retorno_documento,codigo_nota_debito);

/* Comments */
COMMENT ON TABLE public.motivo_retorno_documento_nota_debito									IS 'Associação entre um retorno (motivo de retorno x documento) e a nota de débito.';
COMMENT ON COLUMN public.motivo_retorno_documento_nota_debito.codigo							IS 'Código interno da associação entre um retorno (motivo de retorno x documento) e uma parcela de nota de saída. Sequência: seq_motivo_retorno_documento_nota_saida_parcela';
COMMENT ON COLUMN public.motivo_retorno_documento_nota_debito.codigo_nota_debito				IS 'Código interno da nota de débito.';
COMMENT ON COLUMN public.motivo_retorno_documento_nota_debito.codigo_motivo_retorno_documento	IS 'Código interno do motivo de retorno do documento.';

/* Set owner */
ALTER TABLE public.motivo_retorno_documento_nota_debito OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "unidade"                                      */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.unidade DROP CONSTRAINT pk_unidade;
ALTER TABLE public.unidade DROP CONSTRAINT ck_unidade_tipo;

/* Create "new" table */
CREATE TABLE public.unidade_TMP (
    codigo					BIGINT									NOT NULL,
    codigo_matriz			INTEGER,
    codnome					CHARACTER VARYING(60)					NOT NULL,
    tipo					INTEGER					DEFAULT 1		NOT NULL,
    mascara_conta_contabil	CHARACTER VARYING(30)					NOT NULL,
    emite_nf_faturamento	BOOLEAN					DEFAULT true	NOT NULL,
    zerar_numeracao_nf_ano	BOOLEAN					DEFAULT false	NOT NULL,
    ano_proxima_nf			SMALLINT				DEFAULT 0		NOT NULL,
    proxima_nf				INTEGER					DEFAULT 1		NOT NULL,
    proxima_nf_material		INTEGER					DEFAULT 1		NOT NULL,
    proxima_nd				INTEGER					DEFAULT 1		NOT NULL);

/* Move data from "old" table to "new" table */
INSERT INTO public.unidade_TMP
    (codigo,codigo_matriz,codnome,tipo,zerar_numeracao_nf_ano,mascara_conta_contabil,emite_nf_faturamento,ano_proxima_nf,proxima_nf)
SELECT
    codigo,codigo_matriz,codnome,tipo,zerar_numeracao_nf_ano,mascara_conta_contabil,emite_nf_faturamento,ano_proxima_nf,proxima_nf
FROM public.unidade;

/* Drop indexes */
DROP INDEX public.ix_unidade_matriz;
DROP INDEX public.ix_unidade_nome;

/* Drop "old" table */
DROP TABLE public.unidade;

/* Rename "old" table to "new" table */
ALTER TABLE public.unidade_TMP
	RENAME TO unidade;

/* Create primary key */
ALTER TABLE public.unidade
	ADD CONSTRAINT pk_unidade 
    PRIMARY KEY (codigo);

/* Create constraint */
ALTER TABLE public.unidade
	ADD CONSTRAINT ck_unidade_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1]));

/* Create indexes */
CREATE INDEX ix_unidade_matriz	ON public.unidade (codigo_matriz);
CREATE INDEX ix_unidade_nome	ON public.unidade (codnome);

/* Comments */
COMMENT ON TABLE public.unidade							IS 'Unidades (empresas e filiais).';
COMMENT ON COLUMN public.unidade.codigo					IS 'Código interno da unidade (empresa/filial - código interno da pessoa).';
COMMENT ON COLUMN public.unidade.codigo_matriz			IS 'Código interno da matriz (autorelacionamento, se filial) ou nulo se for uma matriz.';
COMMENT ON COLUMN public.unidade.codnome				IS 'Nome reduzido (ou interno) da empresa/filial.';
COMMENT ON COLUMN public.unidade.tipo					IS 'Tipo da unidade. 0 = Matriz / 1 = Filial.';
COMMENT ON COLUMN public.unidade.mascara_conta_contabil	IS 'Máscara para o número das contas contábeis da unidade';
COMMENT ON COLUMN public.unidade.emite_nf_faturamento	IS 'Indica se será possível emitir NF/Faturamento (serviço/material) ou ND (nota de débito) para a unidade (true) ou não (false).';
COMMENT ON COLUMN public.unidade.zerar_numeracao_nf_ano	IS 'Indica se a numeração das notas fiscais de saída (faturamento, serviço ou serviço/material) será reiniciada a cada ano de emissão (true) ou não (false).';
COMMENT ON COLUMN public.unidade.ano_proxima_nf			IS 'Ano do controle para o número da próxima NF (serviço ou serviço/material) associada à unidade. Se a numeração NÃO FOR zerada a cada ano (conforme o campo "zerar_numeracao_nf_ano") deve ser ignorado (conterá zero).';
COMMENT ON COLUMN public.unidade.proxima_nf				IS 'Número da próxima NF (serviço ou serviço/material) associada à unidade. O número da NF dependerá também se a numeração FOR zerada a cada ano (campo "zerar_numeracao_nf_ano") e, neste caso, do campo "ano_proxima_nf".';
COMMENT ON COLUMN public.unidade.proxima_nf_material	IS 'Número da próxima NF (material) associada à unidade.';
COMMENT ON COLUMN public.unidade.proxima_nd				IS 'Número da próxima ND (nota de débito) associada à unidade.';

/* Set owner */
ALTER TABLE public.unidade OWNER TO sgi;

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

ALTER TABLE public.faturamento_etapa					ADD CONSTRAINT fk_faturamento_etapa_faturamento
    FOREIGN KEY (codigo_faturamento)					REFERENCES public.faturamento (codigo);
ALTER TABLE public.faturamento_etapa					ADD CONSTRAINT fk_faturamento_etapa_nota_saida
    FOREIGN KEY (codigo_nota_saida)						REFERENCES public.nota_saida (codigo);
ALTER TABLE public.faturamento_etapa					ADD CONSTRAINT fk_faturamento_etapa_nota_debito
    FOREIGN KEY (codigo_nota_debito)					REFERENCES nota_debito (codigo);
ALTER TABLE public.faturamento_etapa					ADD CONSTRAINT fk_faturamento_etapa_resp_faturamento
    FOREIGN KEY (codigo_cancelamento)					REFERENCES public.pessoa (codigo);
ALTER TABLE public.faturamento_etapa					ADD CONSTRAINT fk_faturamento_etapa_cancelamento
    FOREIGN KEY (codigo_resp_faturamento)				REFERENCES public.pessoa (codigo);
ALTER TABLE public.faturamento_etapa					ADD CONSTRAINT fk_faturamento_etapa_ult_alteracao_valor
    FOREIGN KEY (codigo_ult_alteracao_valor)			REFERENCES public.pessoa (codigo);

ALTER TABLE nota_debito									ADD CONSTRAINT fk_nota_debito_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)				REFERENCES public.centro_financeiro (codigo);
ALTER TABLE nota_debito									ADD CONSTRAINT fk_nota_debito_cliente 
    FOREIGN KEY (codigo_cliente)						REFERENCES public.pessoa (codigo);
ALTER TABLE nota_debito									ADD CONSTRAINT fk_nota_debito_empresa 
    FOREIGN KEY (codigo_empresa)						REFERENCES public.pessoa (codigo);
ALTER TABLE nota_debito									ADD CONSTRAINT fk_nota_debito_servico_tipo 
    FOREIGN KEY (codigo_servico_tipo)					REFERENCES public.servico_tipo (codigo);
ALTER TABLE nota_debito									ADD CONSTRAINT fk_nota_debito_municipio_ect 
    FOREIGN KEY (codigo_cidade_prestacao_servicos)		REFERENCES public.municipio_ect (codigo);
ALTER TABLE nota_debito									ADD CONSTRAINT fk_nota_debito_resp_cancelamento 
    FOREIGN KEY (codigo_resp_cancelamento)				REFERENCES public.pessoa (codigo);
ALTER TABLE nota_debito									ADD CONSTRAINT fk_nota_debito_resp_reversao_cancelamento 
    FOREIGN KEY (codigo_resp_reversao_cancelamento) 	REFERENCES public.pessoa (codigo);
ALTER TABLE nota_debito									ADD CONSTRAINT fk_nota_debito_resp_recebimento 
    FOREIGN KEY (codigo_resp_recebimento)				REFERENCES public.pessoa (codigo);
ALTER TABLE nota_debito									ADD CONSTRAINT fk_nota_debito_resp_canc_recebimento 
    FOREIGN KEY (codigo_resp_canc_recebimento)			REFERENCES public.pessoa (codigo);
ALTER TABLE nota_debito									ADD CONSTRAINT fk_nota_debito_conta_corrente_prevista 
    FOREIGN KEY (codigo_conta_corrente_prevista)		REFERENCES public.conta_corrente (codigo);
ALTER TABLE nota_debito									ADD CONSTRAINT fk_nota_debito_conta_corrente_recebimento 
    FOREIGN KEY (codigo_conta_corrente_recebimento)		REFERENCES public.conta_corrente (codigo);

ALTER TABLE public.motivo_retorno_documento_nota_debito	ADD CONSTRAINT fk_motivo_retorno_documento_nota_debito_nota_debito 
    FOREIGN KEY (codigo_nota_debito)					REFERENCES public.nota_debito (codigo);
ALTER TABLE public.motivo_retorno_documento_nota_debito	ADD CONSTRAINT fk_motivo_retorno_documento_nota_debito_motivo_retorno_doc
    FOREIGN KEY (codigo_motivo_retorno_documento)		REFERENCES public.motivo_retorno_documento (codigo);

ALTER TABLE public.faturamento_aliquota					ADD CONSTRAINT fk_faturamento_aliquota_faturamento 
    FOREIGN KEY (codigo_faturamento)					REFERENCES public.faturamento (codigo);

ALTER TABLE public.motivo_retorno_documento_faturamento	ADD CONSTRAINT fk_motivo_retorno_documento_faturamento_faturamento 
    FOREIGN KEY (codigo_faturamento)					REFERENCES public.faturamento (codigo);

ALTER TABLE public.motivo_retorno_documento_etapa		ADD CONSTRAINT fk_motivo_retorno_documento_etapa_faturamento_etapa 
    FOREIGN KEY (codigo_etapa)							REFERENCES public.faturamento_etapa (codigo);

ALTER TABLE public.faturamento_retencao					ADD CONSTRAINT fk_faturamento_retencao_faturamento 
    FOREIGN KEY (codigo_faturamento)					REFERENCES public.faturamento (codigo);

ALTER TABLE public.faturamento_contato					ADD CONSTRAINT fk_faturamento_contato_faturamento 
    FOREIGN KEY (codigo_faturamento)					REFERENCES public.faturamento (codigo);

ALTER TABLE public.unidade								ADD CONSTRAINT fk_unidade_matriz 
    FOREIGN KEY (codigo_matriz)							REFERENCES public.unidade (codigo);

ALTER TABLE public.unidade								ADD CONSTRAINT fk_unidade_pessoa_juridica 
    FOREIGN KEY (codigo)								REFERENCES public.pessoa_juridica (codigo);

ALTER TABLE public.gerencia								ADD CONSTRAINT fk_gerencia_unidade 
    FOREIGN KEY (codigo_unidade)						REFERENCES public.unidade (codigo);

ALTER TABLE public.diretoria							ADD CONSTRAINT fk_diretoria_unidade 
    FOREIGN KEY (codigo_unidade)						REFERENCES public.unidade (codigo);

ALTER TABLE public.centro_financeiro					ADD CONSTRAINT fk_centro_financeiro_unidade 
    FOREIGN KEY (codigo_unidade)						REFERENCES public.unidade (codigo);

ALTER TABLE public.nota									ADD CONSTRAINT fk_nota_unidade 
    FOREIGN KEY (codigo_empresa)						REFERENCES public.unidade (codigo);

ALTER TABLE public.ordem_compra							ADD CONSTRAINT fk_ordem_compra_unidade 
    FOREIGN KEY (codigo_unidade)						REFERENCES public.unidade (codigo);

ALTER TABLE public.lote_interface						ADD CONSTRAINT fk_lote_interface_unidade 
    FOREIGN KEY (codigo_unidade)						REFERENCES public.unidade (codigo);

ALTER TABLE public.relacao_bancaria						ADD CONSTRAINT fk_relacao_bancaria_unidade 
    FOREIGN KEY (codigo_unidade)						REFERENCES public.unidade (codigo);

ALTER TABLE public.lancamento							ADD CONSTRAINT fk_lancamento_unidade 
    FOREIGN KEY (codigo_unidade)						REFERENCES public.unidade (codigo);

ALTER TABLE public.nota_saida							ADD CONSTRAINT fk_nota_saida_unidade 
    FOREIGN KEY (codigo_emitente)						REFERENCES public.unidade (codigo);

ALTER TABLE public.contrato								ADD CONSTRAINT fk_contrato_unidade 
    FOREIGN KEY (codigo_unidade)						REFERENCES public.unidade (codigo);

ALTER TABLE public.conta_contabil						ADD CONSTRAINT fk_conta_contabil_unidade 
    FOREIGN KEY (codigo_unidade)						REFERENCES public.unidade (codigo);

ALTER TABLE public.processo								ADD CONSTRAINT fk_processo_unidade 
    FOREIGN KEY (codigo_unidade)						REFERENCES public.unidade (codigo);

ALTER TABLE public.previsao_fluxo_caixa					ADD CONSTRAINT fk_previsao_fluxo_caixa_unidade 
    FOREIGN KEY (codigo_unidade)						REFERENCES public.unidade (codigo);

ALTER TABLE public.previsao_custos						ADD CONSTRAINT fk_previsao_custos_unidade 
    FOREIGN KEY (codigo_unidade)						REFERENCES public.unidade (codigo);

ALTER TABLE public.reinf								ADD CONSTRAINT fk_reinf_unidade 
    FOREIGN KEY (codigo_unidade)						REFERENCES public.unidade (codigo);

/* ---------------------------------------------------------------------- */
/* Recreate views                                                         */
/* ---------------------------------------------------------------------- */
/* vw_inrf_matrizes */
CREATE OR REPLACE VIEW public.vw_inrf_matrizes AS 
 SELECT p.codigo,
    p.documento AS cnpj,
    p.nome,
    pj.uf_inscricao_estadual AS uf,
    pj.inscricao_estadual AS ie,
    pj.inscricao_municipal AS im,
    e.codigo_municipio AS ibge,
    ''::text AS suframa,
    '00'::text AS natureza,
    '1'::text AS atividade
   FROM unidade u,
    pessoa p,
    pessoa_juridica pj,
    endereco e
  WHERE p.codigo = u.codigo AND p.codigo = pj.codigo AND p.codigo_endereco = e.codigo AND u.tipo = 0;

ALTER TABLE public.vw_inrf_matrizes
  OWNER TO sgi;

COMMENT ON VIEW public.vw_inrf_matrizes
  IS 'Atualizada SGINV-2719 / versão 5.14';

/* vw_inrf_filiais */
CREATE OR REPLACE VIEW public.vw_inrf_filiais AS 
 SELECT pj.codigo,
    u.codigo_matriz,
    p.documento AS cnpj,
    p.nome,
    pj.uf_inscricao_estadual AS uf,
    pj.inscricao_estadual AS ie,
    pj.inscricao_municipal AS im,
    e.codigo_municipio AS ibge,
    ''::text AS suframa
   FROM pessoa p,
    pessoa_juridica pj,
    unidade u,
    endereco e
  WHERE p.codigo = pj.codigo AND pj.codigo = u.codigo AND p.codigo_endereco = e.codigo AND u.tipo = 1;

ALTER TABLE public.vw_inrf_filiais
  OWNER TO sgi;

COMMENT ON VIEW public.vw_inrf_filiais
  IS 'Atualizada SGINV-2719 / versão 5.14';

/* vw_inrf_itens_nota_fiscal */
CREATE MATERIALIZED VIEW public.vw_inrf_itens_nota_fiscal AS 
 SELECT inf.codigo_pagamento AS codigo_nota,
    inf.codigo AS codigo_item_nota,
    ic.codigo AS cod_item,
    ic.nome AS descr_compl,
    rnf.valor AS vl_item,
    1 AS qtd,
    ic.codigo_unidade_medida AS unidade_medida,
    ic.codigo_barra,
    ic.codigo_tipo_item_compra,
    ic.codigo_sub_tipo_servico AS codigo_subtipo_servico,
    0 AS vl_desc,
    0 AS ind_orig_cred,
        CASE
            WHEN cf.tipo = 1 AND cf.cumulativo = false AND (ic.codigo IN ( SELECT vw_item_compra_credito_efd.codigo_item_compra
               FROM vw_item_compra_credito_efd
              WHERE vw_item_compra_credito_efd.codigo_item_compra = ic.codigo)) THEN '50'::text
            ELSE '70'::text
        END AS cst_pis,
    rnf.valor AS vl_bc_pis,
        CASE
            WHEN cf.cumulativo = true THEN 0.65
            ELSE 1.65
        END AS aliq_pis,
        CASE
            WHEN cf.tipo = 1 AND cf.cumulativo = false AND (ic.codigo IN ( SELECT vw_item_compra_credito_efd.codigo_item_compra
               FROM vw_item_compra_credito_efd
              WHERE vw_item_compra_credito_efd.codigo_item_compra = ic.codigo)) THEN '50'::text
            ELSE '70'::text
        END AS cst_cofins,
    rnf.valor AS vl_bc_cofins,
        CASE
            WHEN cf.cumulativo = true THEN 3::numeric
            ELSE 7.6
        END AS aliq_cofins,
        CASE
            WHEN ic.codigo_tipo_item_compra = 9 THEN '03'::text
            WHEN ic.codigo_tipo_item_compra = 10 THEN '02'::text
            ELSE ''::text
        END AS nat_bc_cred,
    ''::text AS cod_cta,
    ''::text AS cod_ccus,
    cf.tipo AS tipo_cf,
    cf.cumulativo AS cf_cumulativo,
    cf.codigo AS codigo_cf,
    cf.identificador,
    ic.codigo_tipo_item_compra_amplo,
        CASE
            WHEN inf.codigo_conta_contabil_centro_lucro IS NOT NULL AND cf.codigo_pai IS NOT NULL THEN inf.codigo_conta_contabil_centro_lucro
            WHEN inf.codigo_conta_contabil_centro_custo IS NOT NULL AND cf.codigo_pai IS NULL THEN inf.codigo_conta_contabil_centro_custo
            WHEN cf.codigo_pai IS NOT NULL THEN ( SELECT max(iccc.codigo_conta_contabil) AS max
               FROM item_compra_conta_contabil iccc,
                conta_contabil cc,
                unidade uni
              WHERE iccc.codigo_item_compra = ic.codigo AND iccc.tipo = 1 AND iccc.codigo_conta_contabil = cc.codigo AND cf.codigo_unidade = uni.codigo AND uni.codigo_matriz = cc.codigo_unidade)
            ELSE ( SELECT max(iccc.codigo_conta_contabil) AS max
               FROM item_compra_conta_contabil iccc,
                conta_contabil cc,
                unidade uni
              WHERE iccc.codigo_item_compra = ic.codigo AND iccc.tipo = 0 AND iccc.codigo_conta_contabil = cc.codigo AND cf.codigo_unidade = uni.codigo AND uni.codigo_matriz = cc.codigo_unidade)
        END AS codigo_cc,
        CASE
            WHEN inf.codigo_conta_contabil_centro_lucro IS NOT NULL AND cf.codigo_pai IS NOT NULL THEN (( SELECT cc.numero
               FROM conta_contabil cc
              WHERE cc.codigo = inf.codigo_conta_contabil_centro_lucro))::text
            WHEN inf.codigo_conta_contabil_centro_custo IS NOT NULL AND cf.codigo_pai IS NULL THEN (( SELECT cc.numero
               FROM conta_contabil cc
              WHERE cc.codigo = inf.codigo_conta_contabil_centro_custo))::text
            WHEN cf.codigo_pai IS NOT NULL THEN ( SELECT max(cc.numero::text) AS max
               FROM item_compra_conta_contabil iccc,
                conta_contabil cc,
                unidade uni
              WHERE iccc.codigo_item_compra = ic.codigo AND iccc.tipo = 1 AND iccc.codigo_conta_contabil = cc.codigo AND cf.codigo_unidade = uni.codigo AND uni.codigo_matriz = cc.codigo_unidade)
            ELSE ( SELECT max(cc.numero::text) AS max
               FROM item_compra_conta_contabil iccc,
                conta_contabil cc,
                unidade uni
              WHERE iccc.codigo_item_compra = ic.codigo AND iccc.tipo = 0 AND iccc.codigo_conta_contabil = cc.codigo AND cf.codigo_unidade = uni.codigo AND uni.codigo_matriz = cc.codigo_unidade)
        END AS numero_cc
   FROM item_nota_fiscal inf,
    rateio_nota_fiscal rnf,
    nota_fiscal nf,
    item_compra ic,
    natureza_aquisicao na,
    base_calculo_credito bcc,
    centro_financeiro cf
  WHERE inf.codigo_item_compra = ic.codigo AND inf.codigo_pagamento = nf.codigo AND inf.codigo = rnf.codigo_item_nota_fiscal AND rnf.codigo_centro_financeiro = cf.codigo AND nf.codigo_natureza_aquisicao = na.codigo AND na.codigo_base_calculo_credito = bcc.codigo AND nf.tipo_nota_fiscal <> 1
UNION
 SELECT nota_fiscal.codigo AS codigo_nota,
    item_requisicao_compra.codigo AS codigo_item_nota,
    item_compra.codigo AS cod_item,
    item_compra.nome AS descr_compl,
    item_recebimento.preco::numeric(9,2) AS vl_item,
    item_recebimento.quantidade AS qtd,
    item_compra.codigo_unidade_medida AS unidade_medida,
    item_compra.codigo_barra,
    item_compra.codigo_tipo_item_compra,
    item_compra.codigo_sub_tipo_servico AS codigo_subtipo_servico,
    0 AS vl_desc,
    0 AS ind_orig_cred,
        CASE
            WHEN centro_financeiro.tipo = 1 AND centro_financeiro.cumulativo = false AND (item_compra.codigo IN ( SELECT vw_item_compra_credito_efd.codigo_item_compra
               FROM vw_item_compra_credito_efd
              WHERE vw_item_compra_credito_efd.codigo_item_compra = item_compra.codigo)) THEN '50'::text
            ELSE '70'::text
        END AS cst_pis,
    (item_recebimento.preco * item_recebimento.quantidade)::numeric(9,2) AS vl_bc_pis,
        CASE
            WHEN centro_financeiro.cumulativo = true THEN 0.65
            ELSE 1.65
        END AS aliq_pis,
        CASE
            WHEN centro_financeiro.tipo = 1 AND centro_financeiro.cumulativo = false AND (item_compra.codigo IN ( SELECT vw_item_compra_credito_efd.codigo_item_compra
               FROM vw_item_compra_credito_efd
              WHERE vw_item_compra_credito_efd.codigo_item_compra = item_compra.codigo)) THEN '50'::text
            ELSE '70'::text
        END AS cst_cofins,
    (item_recebimento.preco * item_recebimento.quantidade)::numeric(9,2) AS vl_bc_cofins,
        CASE
            WHEN centro_financeiro.cumulativo = true THEN 3::numeric
            ELSE 7.6
        END AS aliq_cofins,
        CASE
            WHEN item_compra.codigo_tipo_item_compra = 9 THEN '03'::text
            WHEN item_compra.codigo_tipo_item_compra = 10 THEN '02'::text
            ELSE ''::text
        END AS nat_bc_cred,
    ''::text AS cod_cta,
    ''::text AS cod_ccus,
    centro_financeiro.tipo AS tipo_cf,
    centro_financeiro.cumulativo AS cf_cumulativo,
    centro_financeiro.codigo AS codigo_cf,
    centro_financeiro.identificador,
    item_compra.codigo_tipo_item_compra_amplo,
        CASE
            WHEN item_recebimento.codigo_conta_contabil_centro_lucro IS NOT NULL AND centro_financeiro.codigo_pai IS NOT NULL THEN item_recebimento.codigo_conta_contabil_centro_lucro
            WHEN item_recebimento.codigo_conta_contabil_centro_custo IS NOT NULL AND centro_financeiro.codigo_pai IS NULL THEN item_recebimento.codigo_conta_contabil_centro_custo
            WHEN centro_financeiro.codigo_pai IS NOT NULL THEN ( SELECT max(iccc.codigo_conta_contabil) AS max
               FROM item_compra_conta_contabil iccc,
                conta_contabil cc,
                unidade uni
              WHERE iccc.codigo_item_compra = item_compra.codigo AND iccc.tipo = 1 AND iccc.codigo_conta_contabil = cc.codigo AND centro_financeiro.codigo_unidade = uni.codigo AND uni.codigo_matriz = cc.codigo_unidade)
            ELSE ( SELECT max(iccc.codigo_conta_contabil) AS max
               FROM item_compra_conta_contabil iccc,
                conta_contabil cc,
                unidade uni
              WHERE iccc.codigo_item_compra = item_compra.codigo AND iccc.tipo = 0 AND iccc.codigo_conta_contabil = cc.codigo AND centro_financeiro.codigo_unidade = uni.codigo AND uni.codigo_matriz = cc.codigo_unidade)
        END AS codigo_cc,
        CASE
            WHEN item_recebimento.codigo_conta_contabil_centro_lucro IS NOT NULL AND centro_financeiro.codigo_pai IS NOT NULL THEN (( SELECT cc.numero
               FROM conta_contabil cc
              WHERE cc.codigo = item_recebimento.codigo_conta_contabil_centro_lucro))::text
            WHEN item_recebimento.codigo_conta_contabil_centro_custo IS NOT NULL AND centro_financeiro.codigo_pai IS NULL THEN (( SELECT cc.numero
               FROM conta_contabil cc
              WHERE cc.codigo = item_recebimento.codigo_conta_contabil_centro_custo))::text
            WHEN centro_financeiro.codigo_pai IS NOT NULL THEN ( SELECT max(cc.numero::text) AS max
               FROM item_compra_conta_contabil iccc,
                conta_contabil cc,
                unidade uni
              WHERE iccc.codigo_item_compra = item_compra.codigo AND iccc.tipo = 1 AND iccc.codigo_conta_contabil = cc.codigo AND centro_financeiro.codigo_unidade = uni.codigo AND uni.codigo_matriz = cc.codigo_unidade)
            ELSE ( SELECT max(cc.numero::text) AS max
               FROM item_compra_conta_contabil iccc,
                conta_contabil cc,
                unidade uni
              WHERE iccc.codigo_item_compra = item_compra.codigo AND iccc.tipo = 0 AND iccc.codigo_conta_contabil = cc.codigo AND centro_financeiro.codigo_unidade = uni.codigo AND uni.codigo_matriz = cc.codigo_unidade)
        END AS numero_cc
   FROM ordem_compra ordem_compra
     JOIN requisicao_compra_refinada requisicao_compra_refinada ON ordem_compra.codigo = requisicao_compra_refinada.codigo_ordem_compra
     JOIN requisicao_compra requisicao_compra ON requisicao_compra_refinada.codigo_requisicao_compra = requisicao_compra.codigo
     JOIN item_requisicao_compra item_requisicao_compra ON requisicao_compra_refinada.codigo = item_requisicao_compra.codigo_rc_refinada
     JOIN item_compra item_compra ON item_requisicao_compra.codigo_item_compra = item_compra.codigo
     JOIN item_proposta item_proposta ON item_compra.codigo = item_proposta.codigo_item_compra
     JOIN proposta proposta ON item_proposta.codigo_proposta = proposta.codigo
     JOIN item_pedido item_pedido ON item_proposta.codigo = item_pedido.codigo_item_proposta
     JOIN pedido pedido ON item_pedido.codigo_pedido = pedido.codigo
     JOIN item_recebimento item_recebimento ON item_pedido.codigo = item_recebimento.codigo_item_pedido
     JOIN recebimento recebimento ON item_recebimento.codigo_recebimento = recebimento.codigo AND pedido.codigo = recebimento.codigo_pedido
     JOIN nota_fiscal nota_fiscal ON recebimento.codigo_nota_fiscal = nota_fiscal.codigo
     JOIN nota nota ON nota_fiscal.codigo = nota.codigo AND ordem_compra.codigo = pedido.codigo_ordem_compra AND ordem_compra.codigo = proposta.codigo_ordem_compra
     JOIN centro_financeiro centro_financeiro ON requisicao_compra.codigo_centro_financeiro = centro_financeiro.codigo
  WHERE nota_fiscal.tipo_nota_fiscal = 1 AND item_recebimento.quantidade <> 0::numeric
WITH NO DATA;

ALTER TABLE public.vw_inrf_itens_nota_fiscal
  OWNER TO sgi;

COMMENT ON MATERIALIZED VIEW public.vw_inrf_itens_nota_fiscal
  IS 'Atualizada SGINV-2719 / versão 5.14';

/* vw_total_credito_por_nf */
CREATE MATERIALIZED VIEW public.vw_total_credito_por_nf AS 
 SELECT vw_itens.codigo_nota,
    sum(vw_itens.vl_bc_cofins * (vw_itens.aliq_cofins / 100::numeric) + vw_itens.vl_bc_cofins * (vw_itens.aliq_pis / 100::numeric))::numeric(9,2) AS valor_credito,
    sum(vw_itens.vl_bc_cofins * (vw_itens.aliq_cofins / 100::numeric))::numeric(9,2) AS credito_cofins,
    sum(vw_itens.vl_bc_cofins * (vw_itens.aliq_pis / 100::numeric))::numeric(9,2) AS credito_pis,
    sum(vw_itens.vl_bc_cofins)::numeric(9,2) AS base_calculo,
    sum(
        CASE
            WHEN vw_itens.codigo_tipo_item_compra = ANY (ARRAY[9::bigint, 92::bigint]) THEN vw_itens.vl_bc_cofins
            ELSE 0::numeric
        END) AS base_servico,
    sum(
        CASE
            WHEN vw_itens.codigo_tipo_item_compra = 10 THEN vw_itens.vl_bc_cofins
            ELSE 0::numeric
        END) AS base_material
   FROM vw_inrf_itens_nota_fiscal vw_itens,
    vw_inrf_notas_validas vw_notas
  WHERE vw_itens.codigo_nota = vw_notas.codigo_nota AND (vw_itens.cst_cofins = '50'::text OR vw_itens.cst_pis = '50'::text)
  GROUP BY vw_itens.codigo_nota
WITH NO DATA;

ALTER TABLE public.vw_total_credito_por_nf
  OWNER TO sgi;

COMMENT ON MATERIALIZED VIEW public.vw_total_credito_por_nf
  IS 'Atualizada SGINV-2719 / versão 5.14';

/* vw_custos_gastos_previstos */
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
  GROUP BY n.codigo, cf.codigo_unidade, u.codigo_matriz, (date_part('YEAR'::text, cf.data_encerramento)), (date_part('MONTH'::text, cf.data_encerramento)), (date_part('YEAR'::text, cf.data_previsao_primeira_parcela)), (date_part('MONTH'::text, cf.data_previsao_primeira_parcela)), (date_part('YEAR'::text, p.data_pagamento)), (date_part('MONTH'::text, p.data_pagamento)), d.valor, nf.valor_frete, nf.valor_seguro, nf.desconto, nf.valor_outras_despesas, nf.valor_ipi, p.valor, (p.valor + p.valor_multa + p.valor_juros - p.valor_desconto - p.valor_devolucao), rir.codigo_centro_financeiro, cf.codigo_pai, tc.chave
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
  GROUP BY n.codigo, cf.codigo_unidade, u.codigo_matriz, (date_part('YEAR'::text, cf.data_encerramento)), (date_part('MONTH'::text, cf.data_encerramento)), (date_part('YEAR'::text, cf.data_previsao_primeira_parcela)), (date_part('MONTH'::text, cf.data_previsao_primeira_parcela)), (date_part('YEAR'::text, p.data_pagamento)), (date_part('MONTH'::text, p.data_pagamento)), d.valor, nf.valor_frete, nf.valor_seguro, nf.desconto, nf.valor_outras_despesas, nf.valor_ipi, p.valor, (p.valor + p.valor_multa + p.valor_juros - p.valor_desconto - p.valor_devolucao), rnf.codigo_centro_financeiro, cf.codigo_pai, tc.chave
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
  GROUP BY n.codigo, cf.codigo_unidade, u.codigo_matriz, (date_part('YEAR'::text, cf.data_encerramento)), (date_part('MONTH'::text, cf.data_encerramento)), (date_part('YEAR'::text, cf.data_previsao_primeira_parcela)), (date_part('MONTH'::text, cf.data_previsao_primeira_parcela)), (date_part('YEAR'::text, p.data_pagamento)), (date_part('MONTH'::text, p.data_pagamento)), d.valor, p.valor, (p.valor + p.valor_multa + p.valor_juros - p.valor_desconto), rnp.codigo_centro_financeiro, cf.codigo_pai
  ORDER BY 1, 14 DESC, 3, 4
WITH DATA;

ALTER TABLE public.vw_custos_gastos_previstos
  OWNER TO sgi;

COMMENT ON MATERIALIZED VIEW public.vw_custos_gastos_previstos
  IS 'Atualizada SGINV-2719 / versão 5.14';

/* vw_compras_itens_por_natureza */
CREATE MATERIALIZED VIEW public.vw_compras_itens_por_natureza AS 
 SELECT nat.nome AS natureza,
    nat.codigo AS codigo_natureza,
    cf.codigo AS codigo_cf,
    cf.codigo_pai AS codigo_pai_cf,
    ic.nome AS item,
    date(d.data) AS data_compra,
    ir.preco,
    ir.quantidade,
    pessoa.documento AS cnpj,
    pessoa.nome AS razao_social,
    nota.codigo_centro_financeiro AS codigo_ger,
    u.codigo_matriz AS codigo_empresa
   FROM centro_financeiro cf,
    rateio_item_recebimento rir,
    item_recebimento ir,
    recebimento rec,
    item_pedido ip,
    item_proposta iprop,
    item_compra ic,
    natureza_fornecimento nat,
    nota_fiscal nf,
    nota,
    despesa d,
    pessoa,
    unidade u
  WHERE cf.codigo = rir.codigo_centro_financeiro AND rir.codigo_item_recebimento = ir.codigo AND ir.codigo_item_pedido = ip.codigo AND ip.codigo_item_proposta = iprop.codigo AND iprop.codigo_item_compra = ic.codigo AND ic.codigo_natureza_fornecimento = nat.codigo AND ir.codigo_recebimento = rec.codigo AND rec.codigo_nota_fiscal = nf.codigo AND nf.codigo = nota.codigo AND nota.codigo = d.codigo AND nota.codigo_credor = pessoa.codigo AND cf.codigo_unidade = u.codigo AND ir.preco > 0::numeric
UNION
 SELECT nat.nome AS natureza,
    nat.codigo AS codigo_natureza,
    cf.codigo AS codigo_cf,
    cf.codigo_pai AS codigo_pai_cf,
    ic.nome AS item,
    date(d.data) AS data_compra,
    inf.preco,
    inf.quantidade,
    pessoa.documento AS cnpj,
    pessoa.nome AS razao_social,
    nota.codigo_centro_financeiro AS codigo_ger,
    u.codigo_matriz AS codigo_empresa
   FROM centro_financeiro cf,
    rateio_nota_fiscal rnf,
    item_nota_fiscal inf,
    item_compra ic,
    natureza_fornecimento nat,
    nota_fiscal nf,
    nota,
    despesa d,
    pessoa,
    unidade u
  WHERE cf.codigo = rnf.codigo_centro_financeiro AND rnf.codigo_item_nota_fiscal = inf.codigo AND inf.codigo_item_compra = ic.codigo AND ic.codigo_natureza_fornecimento = nat.codigo AND inf.codigo_pagamento = nf.codigo AND nf.codigo = nota.codigo AND nota.codigo = d.codigo AND nota.codigo_credor = pessoa.codigo AND cf.codigo_unidade = u.codigo AND inf.preco > 0::numeric
  ORDER BY 1, 5
WITH DATA;

ALTER TABLE public.vw_compras_itens_por_natureza
  OWNER TO sgi;

COMMENT ON MATERIALIZED VIEW public.vw_compras_itens_por_natureza
  IS 'Atualizada SGINV-2719 / versão 5.14';

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

COMMENT ON VIEW public.vw_custos_faturamento_realizado
  IS 'Atualizada SGINV-2719 / versão 5.14';

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

COMMENT ON VIEW public.vw_custos_receita_realizada
  IS 'Atualizada SGINV-2719 / versão 5.14';

/* vw_custos_faturamento_previsto */
CREATE OR REPLACE VIEW public.vw_custos_faturamento_previsto
AS

SELECT cf.codigo_unidade,
    u.codigo_matriz,
    f.codigo_centro_financeiro,
    cf.codigo_pai AS codigo_gerencia,
    (((((f.valor_mao_obra + f.valor_gastos_gerais) + f.valor_material) + f.valor_servico_terceiro) + round((((((f.valor_mao_obra + f.valor_gastos_gerais) + f.valor_material) + f.valor_servico_terceiro) * ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / (100)::numeric)) / ((1)::numeric - ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / (100)::numeric)))
, 2)) + round((((((((f.valor_mao_obra + f.valor_gastos_gerais) + f.valor_material) + f.valor_servico_terceiro) + round((((((f.valor_mao_obra + f.valor_gastos_gerais) + f.valor_material) + f.valor_servico_terceiro) * ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / (100)::numeric)) / ((1)::numeric - ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / (100)::numeric)))
, 2)) * COALESCE((
SELECT sum(a.percentual) AS sum
          
FROM centro_financeiro_aliquota cfa,
            aliquota a
         
WHERE ((cfa.codigo_centro_financeiro = f.codigo_centro_financeiro)
AND (cfa.codigo_aliquota = a.codigo)))
, (0)::numeric)) / (100)::numeric) / ((1)::numeric - (COALESCE((
SELECT sum(a.percentual) AS sum
          
FROM centro_financeiro_aliquota cfa,
            aliquota a
         
WHERE ((cfa.codigo_centro_financeiro = f.codigo_centro_financeiro)
AND (cfa.codigo_aliquota = a.codigo)))
, (0)::numeric) / (100)::numeric)))
, 2)) AS valor_mensal,
    (((f.valor_mao_obra + f.valor_gastos_gerais) + f.valor_material) + f.valor_servico_terceiro) AS custo_mensal,
    round((((((f.valor_mao_obra + f.valor_gastos_gerais) + f.valor_material) + f.valor_servico_terceiro) * ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / (100)::numeric)) / ((1)::numeric - ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / (100)::numeric)))
, 2) AS valor_bdi,
    0 AS tipo_faturamento
  
FROM fecho f,
    centro_financeiro cf,
    contrato c,
    unidade u
 
WHERE ((f.codigo_centro_financeiro = cf.codigo)
AND (cf.codigo_contrato = c.codigo)
AND (cf.codigo_unidade = u.codigo)
AND (f.numero = (
SELECT max(f2.numero) AS max
          
FROM fecho f2
         
WHERE (f2.codigo_centro_financeiro = f.codigo_centro_financeiro)))
AND (cf.situacao = 1)
AND (c.situacao = 2))
  ORDER BY f.codigo_centro_financeiro;

ALTER TABLE public.vw_custos_faturamento_previsto
  OWNER TO sgi;

COMMENT ON VIEW public.vw_custos_faturamento_previsto
  IS 'Atualizada SGINV-2719 / versão 5.14';

/* vw_custos_receita_prevista */
CREATE OR REPLACE VIEW public.vw_custos_receita_prevista
AS

SELECT cf.codigo_unidade,
    u.codigo_matriz,
    n.codigo_centro_financeiro,
    cf.codigo_pai AS codigo_gerencia,
    date_part('YEAR'::text
, nsp.data_vencimento) AS ano_vencimento,
    date_part('MONTH'::text
, nsp.data_vencimento) AS mes_vencimento,
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
 
WHERE ((n.codigo = nf.codigo)
AND (nf.codigo = ns.codigo)
AND (ns.codigo = nsp.codigo_nota_saida)
AND (cf.codigo_unidade = u.codigo)
AND (n.codigo_centro_financeiro = cf.codigo)
AND (ns.codigo = fe.codigo_nota_saida)
AND (fe.codigo_faturamento = f.codigo)
AND (nf.tipo_nota_fiscal = 11)
AND (n.status = ANY (ARRAY[15
, 16])))
  GROUP BY cf.codigo_unidade
, u.codigo_matriz
, n.codigo_centro_financeiro
, cf.codigo_pai
, (date_part('YEAR'::text
, nsp.data_vencimento))
, (date_part('MONTH'::text
, nsp.data_vencimento))
, f.tipo
  ORDER BY (date_part('YEAR'::text
, nsp.data_vencimento))
, (date_part('MONTH'::text
, nsp.data_vencimento))
, n.codigo_centro_financeiro
, f.tipo;

ALTER TABLE public.vw_custos_receita_prevista
  OWNER TO sgi;

COMMENT ON VIEW public.vw_custos_receita_prevista
  IS 'Atualizada SGINV-2719 / versão 5.14';

/* ---------------------------------------------------------------------- */
/* Add "aplicacao_motivo_retorno"                                         */
/* ---------------------------------------------------------------------- */
INSERT	INTO aplicacao_motivo_retorno (codigo, codigo_perfil, nome, chave, tipo, criador, data_criacao, atualizador, data_atualizacao) 
SELECT	NEXTVAL('seq_aplicacao_motivo_retorno'), perfil.codigo, 'Cancelar Nota de Débito (Faturamento)'		, 'faturamento-cancelar_nd'		, 1, 'admin', CURRENT_TIMESTAMP, NULL, NULL
FROM	perfil
WHERE	perfil.nome = 'faturamento-analista';

INSERT	INTO aplicacao_motivo_retorno (codigo, codigo_perfil, nome, chave, tipo, criador, data_criacao, atualizador, data_atualizacao) 
SELECT	NEXTVAL('seq_aplicacao_motivo_retorno'), perfil.codigo, 'Reverter cancelamento de ND (Faturamento)'	, 'faturamento-rever_canc_nd'	, 1, 'admin', CURRENT_TIMESTAMP, NULL, NULL
FROM	perfil
WHERE	perfil.nome = 'faturamento-analista';

INSERT	INTO aplicacao_motivo_retorno (codigo, codigo_perfil, nome, chave, tipo, criador, data_criacao, atualizador, data_atualizacao) 
SELECT	NEXTVAL('seq_aplicacao_motivo_retorno'), perfil.codigo, 'Cancelar recebimento de ND (Faturamento)'	, 'faturamento-canc_receb_nd'	, 1, 'admin', CURRENT_TIMESTAMP, NULL, NULL
FROM	perfil
WHERE	perfil.nome = 'faturamento-analista';

/* ---------------------------------------------------------------------- */
/* Ajustar o menu                                                         */
/* ---------------------------------------------------------------------- */
/*
select * from funcionalidade where codigo_modulo = 106
select * from modulo
select * from permissao where alvo like '%interfaces%'
select * from receptor where codigo in (2579584,2579951)
select * from perfil where codigo in (2579584,2579951)
*/
--Reposicionar opções do "Faturamento"
UPDATE	funcionalidade
SET		ordem = 50
FROM	modulo
WHERE	modulo.codigo = funcionalidade.codigo_modulo
AND		funcionalidade.url = 'interfaces.listar'
AND		modulo.nome = 'Faturamento'
AND		modulo.codigo_sistema = 2;

UPDATE	funcionalidade
SET		ordem = 60
FROM	modulo
WHERE	modulo.codigo = funcionalidade.codigo_modulo
AND		funcionalidade.url = 'faturamento.relatorios'
AND		modulo.nome = 'Faturamento'
AND		modulo.codigo_sistema = 2;

UPDATE	modulo
SET		ordem = 70
FROM	modulo modulo_pai
WHERE	modulo.codigo_modulo_pai = modulo_pai.codigo
AND		modulo.nome = 'Cadastros'
AND		modulo_pai.nome = 'Faturamento'
AND		modulo_pai.codigo_sistema = 2;

--Nova opção "Faturamento > Notas de débito"
INSERT
INTO	funcionalidade (codigo, nome, ordem, url, codigo_modulo)
SELECT	NEXTVAL('seq_funcionalidade'), 'Notas de débito', 40, 'notadebito.listar', modulo.codigo
FROM	modulo
WHERE	modulo.nome = 'Faturamento'
AND		modulo.codigo_sistema = 2;

--Permissões para a nova opção "Faturamento > Notas de débito"
INSERT
INTO	permissao (codigo, codigo_receptor, acao, alvo)
SELECT	NEXTVAL('seq_permissao'), perfil.codigo, 'render', 'notadebito.listar'
FROM	perfil
WHERE	perfil.nome = 'faturamento-analista';

INSERT
INTO	permissao (codigo, codigo_receptor, acao, alvo)
SELECT	NEXTVAL('seq_permissao'), perfil.codigo, 'render', 'notadebito.listar'
FROM	perfil
WHERE	perfil.nome = 'faturamento-gestor';

--Reposicionar opções do "Contas a Receber"
UPDATE	funcionalidade
SET		ordem = 30
FROM	modulo
WHERE	modulo.codigo = funcionalidade.codigo_modulo
AND		funcionalidade.url = 'contasareceber.relatorios'
AND		modulo.nome = 'Contas a Receber'
AND		modulo.codigo_sistema = 2;

UPDATE	funcionalidade
SET		nome = 'Notas fiscais (parcelas)'
FROM	modulo
WHERE	modulo.codigo = funcionalidade.codigo_modulo
AND		funcionalidade.url = 'contasareceber.listar'
AND		modulo.nome = 'Contas a Receber'
AND		modulo.codigo_sistema = 2;

--Nova opção "Contas a Receber > Notas de débito"
INSERT
INTO	funcionalidade (codigo, nome, ordem, url, codigo_modulo)
SELECT	NEXTVAL('seq_funcionalidade'), 'Notas de débito', 20, 'car_notadebito.listar', modulo.codigo
FROM	modulo
WHERE	modulo.nome = 'Contas a Receber'
AND		modulo.codigo_sistema = 2;

--Permissões para a nova opção "Contas a Receber > Notas de débito"
INSERT
INTO	permissao (codigo, codigo_receptor, acao, alvo)
SELECT	NEXTVAL('seq_permissao'), perfil.codigo, 'render', 'car_notadebito.listar'
FROM	perfil
WHERE	perfil.nome = 'faturamento-analista';

INSERT
INTO	permissao (codigo, codigo_receptor, acao, alvo)
SELECT	NEXTVAL('seq_permissao'), perfil.codigo, 'render', 'car_notadebito.listar'
FROM	perfil
WHERE	perfil.nome = 'faturamento-gestor';

/* ---------------------------------------------------------------------- */
/* Ajustar as notas fiscais (tipo de fornecimento - Serviço ou Serv/Mat)  */
/* ---------------------------------------------------------------------- */
UPDATE	nota_fiscal nf
SET		tipo_fornecimento = 1
FROM	nota n, nota_saida ns, faturamento_etapa fe, faturamento f
WHERE	nf.codigo = n.codigo
AND		ns.codigo = nf.codigo
AND		fe.codigo_nota_saida = ns.codigo
AND		fe.codigo_faturamento = f.codigo
AND		nf.tipo_fornecimento IS NULL
AND		f.percentual_servico = 100;

UPDATE	nota_fiscal nf
SET		tipo_fornecimento = 2
FROM	nota n, nota_saida ns, faturamento_etapa fe, faturamento f
WHERE	nf.codigo = n.codigo
AND		ns.codigo = nf.codigo
AND		fe.codigo_nota_saida = ns.codigo
AND		fe.codigo_faturamento = f.codigo
AND		nf.tipo_fornecimento IS NULL
AND		f.percentual_servico < 100;
