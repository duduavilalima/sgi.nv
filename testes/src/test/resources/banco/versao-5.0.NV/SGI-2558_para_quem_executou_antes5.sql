/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-03-01 17:25                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.contrato								DROP CONSTRAINT fk_contrato_diretoria;
ALTER TABLE public.contrato								DROP CONSTRAINT fk_contrato_forma_contratacao;
ALTER TABLE public.contrato								DROP CONSTRAINT fk_contrato_pessoa;
ALTER TABLE public.contrato								DROP CONSTRAINT fk_contrato_unidade;

ALTER TABLE public.aditivo								DROP CONSTRAINT fk_aditivo_contrato;

ALTER TABLE public.centro_financeiro					DROP CONSTRAINT fk_centro_financeiro_contrato;

ALTER TABLE public.contrato_endereco					DROP CONSTRAINT fk_contrato_endereco_contrato;

ALTER TABLE public.contrato_anexo						DROP CONSTRAINT fk_contrato_anexo_contrato;

ALTER TABLE public.contrato_contato						DROP CONSTRAINT fk_contrato_contato_contrato;

ALTER TABLE public.contrato_situacao					DROP CONSTRAINT fk_contrato_situacao_contrato;

ALTER TABLE public.motivo_retorno_documento_contrato	DROP CONSTRAINT fk_motivo_retorno_documento_contrato_contrato;

ALTER TABLE public.contrato_gerencia					DROP CONSTRAINT fk_contrato_gerencia_contrato;

ALTER TABLE public.contrato_parcela						DROP CONSTRAINT fk_contrato_parcela_contrato;

ALTER TABLE public.contrato_categoria_contrato			DROP CONSTRAINT fk_contrato_categoria_contrato_contrato;

ALTER TABLE public.contrato_aliquota					DROP CONSTRAINT fk_contrato_aliquota_contrato;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "contrato"                                     */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.contrato DROP CONSTRAINT pk_contrato;

/* Drop constraints */
ALTER TABLE public.contrato DROP CONSTRAINT ck_contrato_estagio_comercial;
ALTER TABLE public.contrato DROP CONSTRAINT ck_contrato_periodicidade_pagamento;
ALTER TABLE public.contrato DROP CONSTRAINT ck_contrato_periodo_reajuste_atual;
ALTER TABLE public.contrato DROP CONSTRAINT ck_contrato_prorrogacao;
ALTER TABLE public.contrato DROP CONSTRAINT ck_contrato_tipo;

/* Create temporary table */
CREATE TABLE public.contrato_TMP (
    codigo						BIGINT DEFAULT NEXTVAL('seq_contrato')  NOT NULL,
    codigo_unidade				BIGINT  NOT NULL,
    codigo_diretoria			BIGINT  NOT NULL,
    codigo_pessoa				BIGINT  NOT NULL,
    codigo_forma_contratacao	BIGINT,
    situacao					SMALLINT DEFAULT 0  NOT NULL,
    tipo						SMALLINT DEFAULT 0  NOT NULL,
    proposta					CHARACTER VARYING(20),
    contrato_original			CHARACTER VARYING(20),
    estagio_comercial			SMALLINT DEFAULT 2,
    prorrogacao					SMALLINT DEFAULT (0)::smallint  NOT NULL,
    data_assinatura_atual		DATE,
    inicio_vigencia_atual		DATE,
    fim_vigencia_atual			DATE,
    prazo_atual					SMALLINT,
    valor_mensal_atual			NUMERIC(11,2),
    periodo_reajuste_atual		SMALLINT DEFAULT 0,
    indice_reajuste_atual		CHARACTER VARYING(255),
    resumo						CHARACTER VARYING(2000)  NOT NULL,
    resumo_faturamento			CHARACTER VARYING(2000),
    valor_total					NUMERIC(15,2),
    periodicidade_pagamento		SMALLINT DEFAULT 2,
    periodos_vigencia			SMALLINT,
    dia_vencimento				SMALLINT,
    dia_vencimento_tipo			SMALLINT,
    dia_vencimento2				SMALLINT,
    dia_vencimento2_tipo		SMALLINT,
    data_primeira_parcela		DATE,
    criador						CHARACTER VARYING(255),
    data_criacao				TIMESTAMP,
    atualizador					CHARACTER VARYING(255),
    data_atualizacao			TIMESTAMP);

/* Move data to the temporary table */
INSERT INTO public.contrato_TMP
    (codigo,codigo_unidade,codigo_diretoria,codigo_pessoa,codigo_forma_contratacao,situacao,tipo,proposta,contrato_original,estagio_comercial,prorrogacao,data_assinatura_atual,inicio_vigencia_atual,fim_vigencia_atual,prazo_atual,valor_mensal_atual,periodo_reajuste_atual,indice_reajuste_atual,resumo,valor_total,periodicidade_pagamento,periodos_vigencia,dia_vencimento,dia_vencimento_tipo,dia_vencimento2,dia_vencimento2_tipo,data_primeira_parcela,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_unidade,codigo_diretoria,codigo_pessoa,codigo_forma_contratacao,situacao,tipo,proposta,contrato_original,estagio_comercial,prorrogacao,data_assinatura_atual,inicio_vigencia_atual,fim_vigencia_atual,prazo_atual,valor_mensal_atual,periodo_reajuste_atual,indice_reajuste_atual,resumo,valor_total,periodicidade_pagamento,periodos_vigencia,dia_vencimento,dia_vencimento_tipo,dia_vencimento2,dia_vencimento2_tipo,data_primeira_parcela,criador,data_criacao,atualizador,data_atualizacao
FROM public.contrato;

/* Drop indexes */
DROP INDEX public.ix_contrato_cliente;
DROP INDEX public.ix_contrato_diretoria;
DROP INDEX public.ix_contrato_fim_vigencia_atual;
DROP INDEX public.ix_contrato_forma_contratacao;
DROP INDEX public.ix_contrato_unidade;

/* Drop "old" table */
DROP TABLE public.contrato;

/* Rename temporary table to "new" table */
ALTER TABLE public.contrato_TMP
	RENAME TO contrato;

/* Add primary key */
ALTER TABLE public.contrato
	ADD CONSTRAINT pk_contrato 
    PRIMARY KEY (codigo);

/* Add constraints */
ALTER TABLE public.contrato
	ADD CONSTRAINT ck_contrato_estagio_comercial 
    CHECK (estagio_comercial = ANY (ARRAY[0, 1, 2]));

ALTER TABLE public.contrato
	ADD CONSTRAINT ck_contrato_periodicidade_pagamento 
    CHECK ((periodicidade_pagamento >= 0) AND (periodicidade_pagamento <= 7));

ALTER TABLE public.contrato
	ADD CONSTRAINT ck_contrato_periodo_reajuste_atual 
    CHECK (periodo_reajuste_atual = ANY (ARRAY[0, 1, 2]));

ALTER TABLE public.contrato
	ADD CONSTRAINT ck_contrato_prorrogacao 
    CHECK (prorrogacao = ANY (ARRAY[0, 1]));

ALTER TABLE public.contrato
	ADD CONSTRAINT ck_contrato_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1]));

/* Create indexes */
CREATE INDEX ix_contrato_unidade			ON public.contrato (codigo_unidade);
CREATE INDEX ix_contrato_diretoria			ON public.contrato (codigo_diretoria);
CREATE INDEX ix_contrato_cliente			ON public.contrato (codigo_pessoa);
CREATE INDEX ix_contrato_forma_contratacao	ON public.contrato (codigo_forma_contratacao);
CREATE INDEX ix_contrato_fim_vigencia_atual	ON public.contrato (fim_vigencia_atual);

/* Comments */
COMMENT ON TABLE public.contrato							IS 'Contratos de clientes e fornecedores.';
COMMENT ON COLUMN public.contrato.codigo					IS 'Código interno do contrato. Sequência: seq_contrato.';
COMMENT ON COLUMN public.contrato.codigo_unidade			IS 'Código interno (código  interno da pessoa) da matriz/filial (unidade).';
COMMENT ON COLUMN public.contrato.codigo_diretoria			IS 'Código interno da diretoria.';
COMMENT ON COLUMN public.contrato.codigo_pessoa				IS 'Código interno do cliente (código interno da pessoa).';
COMMENT ON COLUMN public.contrato.situacao					IS 'Situação: 0 = Em elaboração / 1 = Em análise / 2 = Ativo / 3 = Encerrado / 4 = Paralisado / 5 = Cancelado.';
COMMENT ON COLUMN public.contrato.tipo						IS 'Tipo do contrato: 0 = cliente / 1 = fornecedor.';
COMMENT ON COLUMN public.contrato.proposta					IS 'Número da proposta.';
COMMENT ON COLUMN public.contrato.estagio_comercial			IS 'Estágio comercial do contrato: 0 = Carta de intenção / 1 = Proposta aceita / 2 = Contrato assinado.';
COMMENT ON COLUMN public.contrato.prorrogacao				IS 'Indica como será a prorrogação do contrato: 0 = Não se aplica / 1 = Por iguais períodos.';
COMMENT ON COLUMN public.contrato.data_assinatura_atual		IS 'Data de assinatura atual (último aditivo) do contrato.';
COMMENT ON COLUMN public.contrato.inicio_vigencia_atual		IS 'Início da vigência atual (último aditivo) do contrato.';
COMMENT ON COLUMN public.contrato.fim_vigencia_atual		IS 'Fim da vigência atual (último aditivo) do contrato.';
COMMENT ON COLUMN public.contrato.prazo_atual				IS 'Prazo, em meses, de duração atual (último aditivo) do contrato.';
COMMENT ON COLUMN public.contrato.valor_mensal_atual		IS 'Valor mensal atual (último aditivo) do contrato.';
COMMENT ON COLUMN public.contrato.periodo_reajuste_atual	IS 'Período de reajuste atual (último aditivo) do contrato: 0 = Anual / 1 = Semestral / 2 = Trimestral.';
COMMENT ON COLUMN public.contrato.indice_reajuste_atual		IS 'Índice de reajuste atual (último aditivo) do contrato.';
COMMENT ON COLUMN public.contrato.resumo					IS 'Resumo (objeto) do contrato.';
COMMENT ON COLUMN public.contrato.resumo_faturamento		IS 'Resumo (objeto) do contrato segundo a visão do faturamento.';
COMMENT ON COLUMN public.contrato.valor_total				IS 'Valor total do contrato (se não for apenas valor_mensal x prazo).';
COMMENT ON COLUMN public.contrato.periodicidade_pagamento	IS 'Periodicidade de pagamento das parcelas: 0 - Contra-apresentação / 1 - Quinzenal / 2 - Mensal / 3 - Bimestral / 4 - Trimestral / 5 - Quadrimestral / 6 - Semestral / 7 - Anual.';
COMMENT ON COLUMN public.contrato.periodos_vigencia			IS 'Número de períodos (parcelas) durante a vigência.';
COMMENT ON COLUMN public.contrato.dia_vencimento			IS '1º dia de vencimento conforme a periodicidade das parcelas.';
COMMENT ON COLUMN public.contrato.dia_vencimento_tipo		IS 'Tipo do dia de vencimento conforme a periodicidade das parcelas: 0 - dia corrido / 1 - dia útil.';
COMMENT ON COLUMN public.contrato.dia_vencimento2			IS '2º dia de vencimento conforme a periodicidade das parcelas (apenas quinzenal).';
COMMENT ON COLUMN public.contrato.dia_vencimento2_tipo		IS 'Tipo do 2º dia de vencimento conforme a periodicidade das parcelas (apenas quinzenal): 0 - dia corrido / 1 - dia útil.';
COMMENT ON COLUMN public.contrato.data_primeira_parcela		IS 'Data de vencimento da 1ª parcela do contrato.';
COMMENT ON COLUMN public.contrato.criador					IS 'Responsável (login) pela criação do contrato.';
COMMENT ON COLUMN public.contrato.data_criacao				IS 'Data/hora de criação do contrato.';
COMMENT ON COLUMN public.contrato.atualizador				IS 'Responsável (login) pela última atualização do contrato.';
COMMENT ON COLUMN public.contrato.data_atualizacao			IS 'Data/hora da última atualização do contrato.';

/* Set owner */
ALTER TABLE public.contrato OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Modify table "faturamento"                                             */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.faturamento DROP resumo;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.contrato								ADD CONSTRAINT fk_contrato_unidade 
    FOREIGN KEY (codigo_unidade)						REFERENCES public.unidade (codigo);
ALTER TABLE public.contrato								ADD CONSTRAINT fk_contrato_diretoria 
    FOREIGN KEY (codigo_diretoria)						REFERENCES public.diretoria (codigo);
ALTER TABLE public.contrato								ADD CONSTRAINT fk_contrato_pessoa 
    FOREIGN KEY (codigo_pessoa)							REFERENCES public.pessoa (codigo);
ALTER TABLE public.contrato								ADD CONSTRAINT fk_contrato_forma_contratacao 
    FOREIGN KEY (codigo_forma_contratacao)				REFERENCES public.forma_contratacao (codigo);

ALTER TABLE public.aditivo								ADD CONSTRAINT fk_aditivo_contrato 
    FOREIGN KEY (codigo_contrato)						REFERENCES public.contrato (codigo);

ALTER TABLE public.centro_financeiro					ADD CONSTRAINT fk_centro_financeiro_contrato 
    FOREIGN KEY (codigo_contrato)						REFERENCES public.contrato (codigo);

ALTER TABLE public.contrato_endereco					ADD CONSTRAINT fk_contrato_endereco_contrato 
    FOREIGN KEY (codigo_contrato)						REFERENCES public.contrato (codigo);

ALTER TABLE public.contrato_anexo						ADD CONSTRAINT fk_contrato_anexo_contrato 
    FOREIGN KEY (codigo_contrato)						REFERENCES public.contrato (codigo);

ALTER TABLE public.contrato_contato						ADD CONSTRAINT fk_contrato_contato_contrato 
    FOREIGN KEY (codigo_contrato)						REFERENCES public.contrato (codigo);

ALTER TABLE public.contrato_situacao					ADD CONSTRAINT fk_contrato_situacao_contrato 
    FOREIGN KEY (codigo_contrato)						REFERENCES public.contrato (codigo);

ALTER TABLE public.motivo_retorno_documento_contrato	ADD CONSTRAINT fk_motivo_retorno_documento_contrato_contrato 
    FOREIGN KEY (codigo_contrato)						REFERENCES public.contrato (codigo);

ALTER TABLE public.contrato_gerencia					ADD CONSTRAINT fk_contrato_gerencia_contrato 
    FOREIGN KEY (codigo_contrato)						REFERENCES public.contrato (codigo);

ALTER TABLE public.contrato_parcela						ADD CONSTRAINT fk_contrato_parcela_contrato 
    FOREIGN KEY (codigo_contrato)						REFERENCES public.contrato (codigo);

ALTER TABLE public.contrato_categoria_contrato			ADD CONSTRAINT fk_contrato_categoria_contrato_contrato 
    FOREIGN KEY (codigo_contrato)						REFERENCES public.contrato (codigo);

ALTER TABLE public.contrato_aliquota					ADD CONSTRAINT fk_contrato_aliquota_contrato 
    FOREIGN KEY (codigo_contrato)						REFERENCES public.contrato (codigo);
