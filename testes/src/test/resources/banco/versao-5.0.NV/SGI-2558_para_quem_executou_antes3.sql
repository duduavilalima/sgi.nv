/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-02-11 17:49                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.faturamento_etapa				DROP CONSTRAINT fk_faturamento_etapa_cancelamento;
ALTER TABLE public.faturamento_etapa				DROP CONSTRAINT fk_faturamento_etapa_resp_faturamento;
ALTER TABLE public.faturamento_etapa				DROP CONSTRAINT fk_faturamento_etapa_faturamento;
ALTER TABLE public.faturamento_etapa				DROP CONSTRAINT fk_faturamento_etapa_nota_saida;

ALTER TABLE public.motivo_retorno_documento_etapa	DROP CONSTRAINT fk_motivo_retorno_documento_etapa_faturamento_etapa;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "faturamento_etapa"                            */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.faturamento_etapa DROP CONSTRAINT pk_faturamento_etapa;

/* Create temporary table */
CREATE TABLE public.faturamento_etapa_TMP (
    codigo					BIGINT DEFAULT nextval('seq_faturamento_etapa')  NOT NULL,
    codigo_faturamento		BIGINT  NOT NULL,
    codigo_nota_saida		BIGINT,
    codigo_resp_faturamento	BIGINT,
    codigo_cancelamento		BIGINT,
    numero					SMALLINT,
    situacao				SMALLINT DEFAULT 1  NOT NULL,
    inicio					DATE  NOT NULL,
    termino					DATE  NOT NULL,
    valor_servico			NUMERIC(15,2)  NOT NULL,
    descricao				CHARACTER VARYING(255)  NOT NULL,
    discriminacao_inicial	CHARACTER VARYING(2000)  NOT NULL,
    discriminacao_central	CHARACTER VARYING(2000),
    discriminacao_final		CHARACTER VARYING(2000)  NOT NULL,
    data_faturamento		TIMESTAMP,
    data_cancelamento		TIMESTAMP,
    criador					CHARACTER VARYING(255),
    data_criacao			TIMESTAMP,
    atualizador				CHARACTER VARYING(255),
    data_atualizacao		TIMESTAMP);

/* Move data to the temporary table */
INSERT INTO public.faturamento_etapa_TMP
    (codigo,codigo_faturamento,codigo_nota_saida,codigo_resp_faturamento,codigo_cancelamento,numero,situacao,inicio,termino,valor_servico,descricao,discriminacao_inicial,discriminacao_central,discriminacao_final,data_faturamento,data_cancelamento,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_faturamento,codigo_nota_saida,codigo_resp_faturamento,codigo_cancelamento,1,situacao,inicio,termino,valor_servico,descricao,discriminacao_inicial,discriminacao_central,discriminacao_final,data_faturamento,data_cancelamento,criador,data_criacao,atualizador,data_atualizacao
FROM public.faturamento_etapa;

/* Drop indexes */
DROP INDEX public.ix_faturamento_etapa_cancelamento;
DROP INDEX public.ix_faturamento_etapa_faturamento;
DROP INDEX public.ix_faturamento_etapa_nota_saida;
DROP INDEX public.ix_faturamento_etapa_resp_faturamento;

/* Drop "old" table */
DROP TABLE public.faturamento_etapa;

/* Rename temporary table to "new" table */
ALTER TABLE public.faturamento_etapa_TMP
	RENAME TO faturamento_etapa;

/* Add primary key */
ALTER TABLE public.faturamento_etapa
	ADD CONSTRAINT pk_faturamento_etapa 
    PRIMARY KEY (codigo);

/* Create indexes */
CREATE INDEX ix_faturamento_etapa_faturamento		ON public.faturamento_etapa (codigo_faturamento);
CREATE INDEX ix_faturamento_etapa_nota_saida		ON public.faturamento_etapa (codigo_nota_saida);
CREATE INDEX ix_faturamento_etapa_resp_faturamento	ON public.faturamento_etapa (codigo_resp_faturamento);
CREATE INDEX ix_faturamento_etapa_cancelamento		ON public.faturamento_etapa (codigo_cancelamento);

/* Put data into new column */
UPDATE faturamento_etapa SET numero = 1;

/* Alter column */
ALTER TABLE public.faturamento_etapa
	ALTER COLUMN numero SET NOT NULL;

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
COMMENT ON COLUMN public.faturamento_etapa.valor_servico			IS 'Valor de serviço/mão-de-obra da etapa.';
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

ALTER TABLE public.motivo_retorno_documento_etapa	ADD CONSTRAINT fk_motivo_retorno_documento_etapa_faturamento_etapa
    FOREIGN KEY (codigo_motivo_retorno_documento)	REFERENCES public.motivo_retorno_documento (codigo);
