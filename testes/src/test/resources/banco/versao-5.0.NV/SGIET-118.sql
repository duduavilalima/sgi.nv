/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-05-16 11:44                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_cliente;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_centro_financeiro;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_tipo_cobranca_faturamento;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_cancelamento;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_contrato_cidade_faturamento;
ALTER TABLE public.faturamento							DROP CONSTRAINT fk_faturamento_conta_corrente;

ALTER TABLE public.contrato_cidade_faturamento			DROP CONSTRAINT fk_contrato_cidade_faturamento_municipio_ect;
ALTER TABLE public.contrato_cidade_faturamento			DROP CONSTRAINT fk_contrato_cidade_faturamento_contrato;

ALTER TABLE public.faturamento_aliquota					DROP CONSTRAINT fk_faturamento_aliquota_faturamento;

ALTER TABLE public.faturamento_etapa					DROP CONSTRAINT fk_faturamento_etapa_faturamento;

ALTER TABLE public.motivo_retorno_documento_faturamento	DROP CONSTRAINT fk_motivo_retorno_documento_faturamento_faturamento;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "faturamento"                                  */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.faturamento DROP CONSTRAINT pk_faturamento;

ALTER TABLE public.faturamento DROP CONSTRAINT ck_centro_financeiro_tipo;

/* Create temporary table */
CREATE TABLE public.faturamento_TMP (
    codigo								BIGINT DEFAULT nextval('seq_faturamento')  NOT NULL,
    codigo_cliente						BIGINT  NOT NULL,
    codigo_centro_financeiro			BIGINT  NOT NULL,
    codigo_conta_corrente				BIGINT  NOT NULL,
    codigo_tipo_cobranca				BIGINT  NOT NULL,
    codigo_cidade_prestacao_servicos	BIGINT,
    codigo_cancelamento					BIGINT,
    situacao							SMALLINT DEFAULT 1  NOT NULL,
    tipo								SMALLINT DEFAULT 1  NOT NULL,
    identificacao						CHARACTER VARYING(100),
    valor								NUMERIC(15,2)  NOT NULL,
    percentual_servico					NUMERIC(5,2)  NOT NULL,
    tipo_documento_pagamento			SMALLINT  NOT NULL,
    iss_retido_cliente					BOOLEAN  NOT NULL,
    discriminacao_inicial				CHARACTER VARYING(2000)  NOT NULL,
    discriminacao_central				CHARACTER VARYING(2000),
    discriminacao_final					CHARACTER VARYING(2000)  NOT NULL,
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
    (codigo,codigo_cliente,codigo_centro_financeiro,codigo_conta_corrente,codigo_tipo_cobranca,codigo_cancelamento,situacao,tipo,identificacao,valor,percentual_servico,tipo_documento_pagamento,iss_retido_cliente,discriminacao_inicial,discriminacao_central,discriminacao_final,periodo,data_vencimento_1,data_vencimento_2,data_cancelamento,criador,data_criacao,atualizador,data_atualizacao,codigo_cidade_prestacao_servicos)
SELECT
    codigo,codigo_cliente,codigo_centro_financeiro,codigo_conta_corrente,codigo_tipo_cobranca,codigo_cancelamento,situacao,tipo,identificacao,valor,percentual_servico,tipo_documento_pagamento,iss_retido_cliente,discriminacao_inicial,discriminacao_central,discriminacao_final,periodo,data_vencimento_1,data_vencimento_2,data_cancelamento,criador,data_criacao,atualizador,data_atualizacao,3304557
FROM public.faturamento;

/* Drop indexes */
DROP INDEX public.ix_faturamento_contrato_cidade_faturamento;
DROP INDEX public.ix_faturamento_cancelamento;
DROP INDEX public.ix_faturamento_tipo_cobranca;
DROP INDEX public.ix_faturamento_conta_corrente;
DROP INDEX public.ix_faturamento_centro_financeiro;
DROP INDEX public.ix_faturamento_cliente;

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
CREATE INDEX ix_faturamento_cliente							ON public.faturamento (codigo_cliente);
CREATE INDEX ix_faturamento_centro_financeiro				ON public.faturamento (codigo_centro_financeiro);
CREATE INDEX ix_faturamento_conta_corrente					ON public.faturamento (codigo_conta_corrente);
CREATE INDEX ix_faturamento_tipo_cobranca					ON public.faturamento (codigo_tipo_cobranca);
CREATE INDEX ix_faturamento_cidade_prestacao_servicos		ON public.faturamento (codigo_cidade_prestacao_servicos);
CREATE INDEX ix_faturamento_cancelamento					ON public.faturamento (codigo_cancelamento);

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
/* Create/set sequence                                                    */
/* ---------------------------------------------------------------------- */
/* Create sequences */
CREATE SEQUENCE public.seq_contrato_cidade_prestacao_servicos INCREMENT 1 MINVALUE 1 START 1;

/* Update value */
SELECT setval('public.seq_contrato_cidade_prestacao_servicos', nextval('public.seq_contrato_cidade_faturamento'), false); 

/* ---------------------------------------------------------------------- */
/* Modify table "contrato_cidade_faturamento"                             */
/* ---------------------------------------------------------------------- */
/* Drop indexes */
DROP INDEX public.uq_contrato_cidade_faturamento_contrato_municipio_ect;
DROP INDEX public.ix_contrato_cidade_faturamento_municipio_ect;
DROP INDEX public.ix_contrato_cidade_faturamento_contrato;

/* Drop primary key */
ALTER TABLE public.contrato_cidade_faturamento
	DROP CONSTRAINT pk_contrato_cidade_faturamento;

/* Rename table */
ALTER TABLE public.contrato_cidade_faturamento
	RENAME TO contrato_cidade_prestacao_servicos;

/* Set "new" sequence default to the primary key */
ALTER TABLE public.contrato_cidade_prestacao_servicos
	ALTER COLUMN codigo DROP DEFAULT;

ALTER TABLE public.contrato_cidade_prestacao_servicos
	ALTER COLUMN codigo SET DEFAULT nextval('seq_contrato_cidade_prestacao_servicos');

/* Create primary key */
ALTER TABLE public.contrato_cidade_prestacao_servicos
	ADD CONSTRAINT pk_contrato_cidade_prestacao_servicos 
    PRIMARY KEY (codigo);

/* Create indexes */
CREATE UNIQUE INDEX uq_contrato_cidade_prestacao_servicos_contrato_municipio_ect	ON public.contrato_cidade_prestacao_servicos (codigo_contrato,codigo_municipio_ect);
CREATE INDEX ix_contrato_cidade_prestacao_servicos_municipio_ect					ON public.contrato_cidade_prestacao_servicos (codigo_municipio_ect);
CREATE INDEX ix_contrato_cidade_prestacao_servicos_contrato							ON public.contrato_cidade_prestacao_servicos (codigo_contrato);

/* Comments */
COMMENT ON TABLE public.contrato_cidade_prestacao_servicos							IS 'Relacionamento entre os contratos e seus municípios onde os serviçoes podem ser prestados.';
COMMENT ON COLUMN public.contrato_cidade_prestacao_servicos.codigo					IS 'Código interno do relacionamento entre o contrato e o município onde os serviçoes podem ser prestados. Sequência: seq_contrato_cidade_prestacao_servicos.';
COMMENT ON COLUMN public.contrato_cidade_prestacao_servicos.codigo_contrato			IS 'Código interno do contrato. Chave única.';
COMMENT ON COLUMN public.contrato_cidade_prestacao_servicos.codigo_municipio_ect	IS 'Código interno do município. Chave única.';
COMMENT ON COLUMN public.contrato_cidade_prestacao_servicos.criador					IS 'Responsável (login) pela criação do relacionamento entre o contrato e o município onde os serviçoes podem ser prestados.';
COMMENT ON COLUMN public.contrato_cidade_prestacao_servicos.data_criacao			IS 'Data/hora de criação do relacionamento entre o contrato e o município onde os serviçoes podem ser prestados.';
COMMENT ON COLUMN public.contrato_cidade_prestacao_servicos.atualizador				IS 'Responsável (login) pela última atualização do relacionamento entre o contrato e o município onde os serviçoes podem ser prestados.';
COMMENT ON COLUMN public.contrato_cidade_prestacao_servicos.data_atualizacao		IS 'Data/hora da última atualização do relacionamento entre o contrato e o município onde os serviçoes podem ser prestados.';

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

ALTER TABLE public.contrato_cidade_prestacao_servicos	ADD CONSTRAINT fk_contrato_cidade_faturamento_municipio_ect 
    FOREIGN KEY (codigo_municipio_ect)					REFERENCES public.municipio_ect (codigo);

ALTER TABLE public.contrato_cidade_prestacao_servicos	ADD CONSTRAINT fk_contrato_cidade_faturamento_contrato 
    FOREIGN KEY (codigo_contrato)						REFERENCES public.contrato (codigo);

ALTER TABLE public.faturamento_aliquota					ADD CONSTRAINT fk_faturamento_aliquota_faturamento 
    FOREIGN KEY (codigo_faturamento)					REFERENCES public.faturamento (codigo);

ALTER TABLE public.faturamento_etapa					ADD CONSTRAINT fk_faturamento_etapa_faturamento 
    FOREIGN KEY (codigo_faturamento)					REFERENCES public.faturamento (codigo);

ALTER TABLE public.motivo_retorno_documento_faturamento	ADD CONSTRAINT fk_motivo_retorno_documento_faturamento_faturamento 
    FOREIGN KEY (codigo_faturamento)					REFERENCES public.faturamento (codigo);

/* ---------------------------------------------------------------------- */
/* Drop old sequence                                                      */
/* ---------------------------------------------------------------------- */
DROP SEQUENCE public.seq_contrato_cidade_faturamento;
