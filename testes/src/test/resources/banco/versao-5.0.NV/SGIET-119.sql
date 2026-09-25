/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-05-16 16:21                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.faturamento_etapa					DROP CONSTRAINT fk_faturamento_etapa_resp_faturamento;
ALTER TABLE public.faturamento_etapa					DROP CONSTRAINT fk_faturamento_etapa_cancelamento;
ALTER TABLE public.faturamento_etapa					DROP CONSTRAINT fk_faturamento_etapa_nota_saida;
ALTER TABLE public.faturamento_etapa					DROP CONSTRAINT fk_faturamento_etapa_faturamento;

ALTER TABLE public.motivo_retorno_documento_etapa		DROP CONSTRAINT fk_motivo_retorno_documento_etapa_faturamento_etapa;

ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_cliente;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_centro_financeiro;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_conta_corrente;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_tipo_cobranca_faturamento;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_cancelamento;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_municipio_ect;

ALTER TABLE public.faturamento_aliquota					DROP CONSTRAINT fk_faturamento_aliquota_faturamento;

ALTER TABLE public.motivo_retorno_documento_faturamento	DROP CONSTRAINT fk_motivo_retorno_documento_faturamento_faturamento;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "faturamento_etapa"                            */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.faturamento_etapa DROP CONSTRAINT pk_faturamento_etapa;

/* Create temporary table */
CREATE TABLE public.faturamento_etapa_TMP (
    codigo					BIGINT DEFAULT nextval('seq_faturamento_etapa')	NOT NULL,
    codigo_faturamento		BIGINT											NOT NULL,
    codigo_nota_saida		BIGINT,
    codigo_resp_faturamento	BIGINT,
    codigo_cancelamento		BIGINT,
    numero					SMALLINT										NOT NULL,
    situacao				SMALLINT DEFAULT 0								NOT NULL,
    inicio					DATE											NOT NULL,
    termino					DATE											NOT NULL,
    valor					NUMERIC(15,2)									NOT NULL,
    valor_permuta			NUMERIC(9,2) DEFAULT 0.00						NOT NULL,
    valor_desconto			NUMERIC(9,2) DEFAULT 0.00						NOT NULL,
    valor_multa				NUMERIC(9,2) DEFAULT 0.00						NOT NULL,
    valor_juros				NUMERIC(9,2) DEFAULT 0.00						NOT NULL,
    descricao				CHARACTER VARYING(255)							NOT NULL,
    discriminacao_inicial	CHARACTER VARYING(2000)							NOT NULL,
    discriminacao_central	CHARACTER VARYING(2000),
    discriminacao_final		CHARACTER VARYING(2000)							NOT NULL,
    data_faturamento		TIMESTAMP,
    data_cancelamento		TIMESTAMP,
    criador					CHARACTER VARYING(255),
    data_criacao			TIMESTAMP,
    atualizador				CHARACTER VARYING(255),
    data_atualizacao		TIMESTAMP);

/* Move data to the temporary table */
INSERT INTO public.faturamento_etapa_TMP
    (codigo,codigo_faturamento,codigo_nota_saida,codigo_resp_faturamento,codigo_cancelamento,numero,situacao,inicio,termino,descricao,discriminacao_inicial,discriminacao_central,discriminacao_final,data_faturamento,data_cancelamento,criador,data_criacao,atualizador,data_atualizacao,valor)
SELECT
    codigo,codigo_faturamento,codigo_nota_saida,codigo_resp_faturamento,codigo_cancelamento,numero,situacao,inicio,termino,descricao,discriminacao_inicial,discriminacao_central,discriminacao_final,data_faturamento,data_cancelamento,criador,data_criacao,atualizador,data_atualizacao,valor_servico
FROM public.faturamento_etapa;

/* Drop indexes */
DROP INDEX public.ix_faturamento_etapa_faturamento;
DROP INDEX public.ix_faturamento_etapa_nota_saida;
DROP INDEX public.ix_faturamento_etapa_resp_faturamento;
DROP INDEX public.ix_faturamento_etapa_cancelamento;

/* Drop "old" table */
DROP TABLE public.faturamento_etapa;

/* Rename temporary table to "new" table */
ALTER TABLE public.faturamento_etapa_TMP
	RENAME TO faturamento_etapa;

/* Create primary key */
ALTER TABLE public.faturamento_etapa
	ADD CONSTRAINT pk_faturamento_etapa 
    PRIMARY KEY (codigo);

/* Create indexes */
CREATE INDEX ix_faturamento_etapa_faturamento		ON public.faturamento_etapa (codigo_faturamento);
CREATE INDEX ix_faturamento_etapa_nota_saida		ON public.faturamento_etapa (codigo_nota_saida);
CREATE INDEX ix_faturamento_etapa_resp_faturamento	ON public.faturamento_etapa (codigo_resp_faturamento);
CREATE INDEX ix_faturamento_etapa_cancelamento		ON public.faturamento_etapa (codigo_cancelamento);

/* Comments */
COMMENT ON TABLE public.faturamento_etapa							IS 'Etapa do faturamento. Cada etapa será uma nota fiscal.';
COMMENT ON COLUMN public.faturamento_etapa.codigo					IS 'Código interno da etapa. Sequência: seq_faturamento_etapa.';
COMMENT ON COLUMN public.faturamento_etapa.codigo_faturamento		IS 'Código interno do faturamento.';
COMMENT ON COLUMN public.faturamento_etapa.codigo_nota_saida		IS 'Código interno da nota de saída associada.';
COMMENT ON COLUMN public.faturamento_etapa.codigo_resp_faturamento	IS 'Código do responsável (código interno da pessoa) pelo faturamento da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.codigo_cancelamento		IS 'Código do responsável (código interno da pessoa) pelo cancelamento da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.numero					IS 'Número da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.situacao					IS 'Situação: 0 = A faturar / 1 = Faturada / 2 = Recebida / 4 = Cancelada.';
COMMENT ON COLUMN public.faturamento_etapa.inicio					IS 'Data de início da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.termino					IS 'Data de término da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.valor					IS 'Valor da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.valor_permuta			IS 'Valor de permuta (troca de valores entre o cliente e a AA) da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.valor_desconto			IS 'Valor de desconto da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.valor_multa				IS 'Valor de multa recebida da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.valor_juros				IS 'Valor de juros recebido da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.descricao				IS 'Descrição do item da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.discriminacao_inicial	IS 'Discriminação dos serviços, parte inicial (para posterior emissão da NF).';
COMMENT ON COLUMN public.faturamento_etapa.discriminacao_central	IS 'Discriminação dos serviços, parte central (para posterior emissão da NF).';
COMMENT ON COLUMN public.faturamento_etapa.discriminacao_final		IS 'Discriminação dos serviços, parte final (para posterior emissão da NF).';
COMMENT ON COLUMN public.faturamento_etapa.data_faturamento			IS 'Data/hora de faturamento da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.data_cancelamento		IS 'Data/hora de cancelamento da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.criador					IS 'Responsável (login) pela criação da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.data_criacao				IS 'Data/hora de criação da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.atualizador				IS 'Responsável (login) pela última atualização da etapa.';
COMMENT ON COLUMN public.faturamento_etapa.data_atualizacao			IS 'Data/hora da última atualização da etapa.';

/* Set owner */
ALTER TABLE public.faturamento_etapa OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "faturamento"                                  */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.faturamento DROP CONSTRAINT pk_faturamento;

/* Drop constraint */
ALTER TABLE public.faturamento DROP CONSTRAINT ck_centro_financeiro_tipo;

/* Create temporary table */
CREATE TABLE public.faturamento_TMP (
    codigo								BIGINT DEFAULT nextval('seq_faturamento')	NOT NULL,
    codigo_cliente						BIGINT										NOT NULL,
    codigo_centro_financeiro			BIGINT										NOT NULL,
    codigo_conta_corrente				BIGINT										NOT NULL,
    codigo_tipo_cobranca				BIGINT										NOT NULL,
    codigo_cidade_prestacao_servicos	BIGINT,
    codigo_cancelamento					BIGINT,
    situacao							SMALLINT DEFAULT 1							NOT NULL,
    tipo								SMALLINT DEFAULT 1							NOT NULL,
    identificacao						CHARACTER VARYING(100),
    valor								NUMERIC(15,2)								NOT NULL,
    percentual_servico					NUMERIC(5,2)								NOT NULL,
    tipo_documento_pagamento			SMALLINT									NOT NULL,
    iss_retido_cliente					BOOLEAN										NOT NULL,
    discriminacao_inicial				CHARACTER VARYING(2000)						NOT NULL,
    discriminacao_central				CHARACTER VARYING(2000),
    discriminacao_final					CHARACTER VARYING(2000)						NOT NULL,
    percentual_retencao					NUMERIC(5,2) DEFAULT 0.00					NOT NULL,
    periodo								CHARACTER VARYING(50),
    data_vencimento_1					DATE,
    data_vencimento_2					DATE,
    data_cancelamento					TIMESTAMP,
    criador								CHARACTER VARYING(255),
    data_criacao						TIMESTAMP,
    atualizador							CHARACTER VARYING(255),
    data_atualizacao					TIMESTAMP);

/* Move data to the temporary table */
INSERT INTO public.faturamento_TMP
    (codigo,codigo_cliente,codigo_centro_financeiro,codigo_cidade_prestacao_servicos,codigo_conta_corrente,codigo_tipo_cobranca,codigo_cancelamento,situacao,tipo,identificacao,valor,percentual_servico,tipo_documento_pagamento,iss_retido_cliente,discriminacao_inicial,discriminacao_central,discriminacao_final,periodo,data_vencimento_1,data_vencimento_2,data_cancelamento,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_cliente,codigo_centro_financeiro,codigo_cidade_prestacao_servicos,codigo_conta_corrente,codigo_tipo_cobranca,codigo_cancelamento,situacao,tipo,identificacao,valor,percentual_servico,tipo_documento_pagamento,iss_retido_cliente,discriminacao_inicial,discriminacao_central,discriminacao_final,periodo,data_vencimento_1,data_vencimento_2,data_cancelamento,criador,data_criacao,atualizador,data_atualizacao
FROM public.faturamento;

/* Drop indexes */
DROP INDEX public.ix_faturamento_cliente;
DROP INDEX public.ix_faturamento_centro_financeiro;
DROP INDEX public.ix_faturamento_cidade_prestacao_servicos;
DROP INDEX public.ix_faturamento_conta_corrente;
DROP INDEX public.ix_faturamento_tipo_cobranca;
DROP INDEX public.ix_faturamento_cancelamento;

/* Drop "old" table */
DROP TABLE public.faturamento;

/* Rename temporary table to "new" table */
ALTER TABLE public.faturamento_TMP
	RENAME TO faturamento;

/* Create primary key */
ALTER TABLE public.faturamento
	ADD CONSTRAINT pk_faturamento 
    PRIMARY KEY (codigo);

/* Create constraint */
ALTER TABLE public.faturamento
	ADD CONSTRAINT ck_centro_financeiro_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1]));

/* Create indexes */
CREATE INDEX ix_faturamento_cliente						ON public.faturamento (codigo_cliente);
CREATE INDEX ix_faturamento_centro_financeiro			ON public.faturamento (codigo_centro_financeiro);
CREATE INDEX ix_faturamento_cidade_prestacao_servicos	ON public.faturamento (codigo_cidade_prestacao_servicos);
CREATE INDEX ix_faturamento_conta_corrente				ON public.faturamento (codigo_conta_corrente);
CREATE INDEX ix_faturamento_tipo_cobranca				ON public.faturamento (codigo_tipo_cobranca);
CREATE INDEX ix_faturamento_cancelamento				ON public.faturamento (codigo_cancelamento);

/* Comments */
COMMENT ON TABLE public.faturamento										IS 'Faturamentos, conjunto de etapas (posteriormente transformadas em notas fiscais para os clientes).';
COMMENT ON COLUMN public.faturamento.codigo								IS 'Código interno do faturamento. Sequência: seq_faturamento.';
COMMENT ON COLUMN public.faturamento.codigo_cliente						IS 'Código interno do cliente ou subcliente (código interno da pessoa) do faturamento.';
COMMENT ON COLUMN public.faturamento.codigo_centro_financeiro			IS 'Código interno do centro financeiro do faturamento.';
COMMENT ON COLUMN public.faturamento.codigo_conta_corrente				IS 'Código interno da conta corrente utilizada no faturamento.';
COMMENT ON COLUMN public.faturamento.codigo_tipo_cobranca				IS 'Código interno do tipo de cobrança do faturamento.';
COMMENT ON COLUMN public.faturamento.codigo_cidade_prestacao_servicos	IS 'Código interno da cidade onde os serviços foram presatados e deve ser selecionada entre uma das cidades do contrato (na tabela "") e a cidade do endereço da cidade do contrato (cidade de emissão das NFs). É uma das cidades possíveis para retenção do ISS.';
COMMENT ON COLUMN public.faturamento.codigo_cancelamento				IS 'Código do responsável (código interno da pessoa) pelo cancelamento do faturamento.';
COMMENT ON COLUMN public.faturamento.situacao							IS 'Situação: 0 = Aberto / 1 = Em faturamento (uma etapa com NF) / 2 = Em recebimento (uma etapa com NF recebida) / 3 = Recebido (todas as etapas com as NFs recebidas) / 4 = Cancelado.';
COMMENT ON COLUMN public.faturamento.tipo								IS 'Tipo do faturamento: 0 = Contratual / 1 = Extra contratual.';
COMMENT ON COLUMN public.faturamento.identificacao						IS 'Identificação do faturamento.';
COMMENT ON COLUMN public.faturamento.valor								IS 'Valor total do faturamento.';
COMMENT ON COLUMN public.faturamento.percentual_servico					IS 'Percentual de serviço/mão-de-obra. Se 100%, não há divisão entre material e serviço. Se < 100%, material será a diferença.';
COMMENT ON COLUMN public.faturamento.tipo_documento_pagamento			IS 'Tipo de documento de pagamento: 0 = Boleto / 1 = Depósito em conta/transferência';
COMMENT ON COLUMN public.faturamento.iss_retido_cliente					IS 'Indica se o ISS será retido pelo cliente (true) ou não (false).';
COMMENT ON COLUMN public.faturamento.discriminacao_inicial				IS 'Discriminação dos serviços, parte inicial (para posterior uso na etapa e emissão da NF). Poderá ser montada pela aplicação.';
COMMENT ON COLUMN public.faturamento.discriminacao_central				IS 'Discriminação dos serviços, parte central (para posterior uso na etapa e emissão da NF). Deverá ser digitada pelo usuário.';
COMMENT ON COLUMN public.faturamento.discriminacao_final				IS 'Discriminação dos serviços, parte final (para posterior uso na etapa e emissão da NF). Poderá ser montada pela aplicaÃ§Ã£o.';
COMMENT ON COLUMN public.faturamento.percentual_retencao				IS 'Percentual de retenção a ser aplicado ao faturamento. Este valor é retido a cada faturamento contratual (não incide sobre extra-contratual) e será devolvido ao término do contrato. Também está no contrato, mas será mantido aqui como histórico.';
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
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.faturamento_etapa					ADD CONSTRAINT fk_faturamento_etapa_faturamento 
    FOREIGN KEY (codigo_faturamento)					REFERENCES public.faturamento (codigo);
ALTER TABLE public.faturamento_etapa					ADD CONSTRAINT fk_faturamento_etapa_nota_saida 
    FOREIGN KEY (codigo_nota_saida)						REFERENCES public.nota_saida (codigo);
ALTER TABLE public.faturamento_etapa					ADD CONSTRAINT fk_faturamento_etapa_resp_faturamento 
    FOREIGN KEY (codigo_cancelamento)					REFERENCES public.pessoa (codigo);
ALTER TABLE public.faturamento_etapa					ADD CONSTRAINT fk_faturamento_etapa_cancelamento 
    FOREIGN KEY (codigo_resp_faturamento)				REFERENCES public.pessoa (codigo);

ALTER TABLE public.motivo_retorno_documento_etapa		ADD CONSTRAINT fk_motivo_retorno_documento_etapa_faturamento_etapa 
    FOREIGN KEY (codigo_etapa)							REFERENCES public.faturamento_etapa (codigo);

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

ALTER TABLE public.faturamento_aliquota					ADD CONSTRAINT fk_faturamento_aliquota_faturamento 
    FOREIGN KEY (codigo_faturamento)					REFERENCES public.faturamento (codigo);

ALTER TABLE public.motivo_retorno_documento_faturamento	ADD CONSTRAINT fk_motivo_retorno_documento_faturamento_faturamento 
    FOREIGN KEY (codigo_faturamento)					REFERENCES public.faturamento (codigo);
