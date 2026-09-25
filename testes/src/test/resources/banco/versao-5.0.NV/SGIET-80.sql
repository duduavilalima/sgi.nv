/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-04-07 16:50                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.categoria_tributo	DROP CONSTRAINT fk_categoria_tributo_municipio_ect;
ALTER TABLE public.categoria_tributo	DROP CONSTRAINT fk_categoria_tributo_tributo;
ALTER TABLE public.aliquota				DROP CONSTRAINT fk_aliquota_categoria_tributo;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "categoria_tributo"                            */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.categoria_tributo	DROP CONSTRAINT pk_categoria_tributo;
ALTER TABLE public.categoria_tributo	DROP CONSTRAINT ck_antecipa_posterga;
ALTER TABLE public.categoria_tributo	DROP CONSTRAINT ck_referencia_data;
ALTER TABLE public.categoria_tributo	DROP CONSTRAINT ck_tipo_aliquota;

/* Create temporary table */
CREATE TABLE public.categoria_tributo_TMP (
    codigo					BIGINT DEFAULT nextval('seq_categoria_tributo')	NOT NULL,
    codigo_tributo			BIGINT											NOT NULL,
    codigo_municipio_ect	INTEGER,
    principal				BOOLEAN DEFAULT false							NOT NULL,
    codigo_receita			INTEGER  NOT NULL,
    descricao				CHARACTER VARYING(255)							NOT NULL,
    tipo_aliquota			INTEGER  NOT NULL,
    minimo_guia				NUMERIC(14,2) DEFAULT 0							NOT NULL,
    referencia_data			SMALLINT DEFAULT 0								NOT NULL,
    periodo_apuracao		SMALLINT DEFAULT 0								NOT NULL,
    antecipa_posterga		SMALLINT DEFAULT 0								NOT NULL,
    primeiro_dia			SMALLINT DEFAULT 0								NOT NULL,
    habilita_faturamento	BOOLEAN DEFAULT false							NOT NULL,
    vigencia_inicio			DATE,
    vigencia_fim			DATE,
    observacao				TEXT,
    criador					CHARACTER VARYING(255),
    data_criacao			TIMESTAMP,
    atualizador				CHARACTER VARYING(255),
    data_atualizacao		TIMESTAMP);

/* Move data to the temporary table */
INSERT INTO public.categoria_tributo_TMP
    (codigo,codigo_tributo,codigo_municipio_ect,principal,codigo_receita,descricao,tipo_aliquota,minimo_guia,referencia_data,periodo_apuracao,antecipa_posterga,primeiro_dia,vigencia_inicio,vigencia_fim,observacao,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_tributo,codigo_municipio_ect,principal,codigo_receita,descricao,tipo_aliquota,minimo_guia,referencia_data,periodo_apuracao,antecipa_posterga,primeiro_dia,vigencia_inicio,vigencia_fim,observacao,criador,data_criacao,atualizador,data_atualizacao
FROM public.categoria_tributo;

/* Drop indexes */
DROP INDEX public.ix_categoria_tributo_municipio_ect;
DROP INDEX public.ix_categoria_tributo_tributo;

/* Drop "old" table */
DROP TABLE public.categoria_tributo;

/* Rename temporary table to "new" table */
ALTER TABLE public.categoria_tributo_TMP
	RENAME TO categoria_tributo;

/* Create primary key */
ALTER TABLE public.categoria_tributo
	ADD CONSTRAINT pk_categoria_tributo 
    PRIMARY KEY (codigo);

/* Create constraints */
ALTER TABLE public.categoria_tributo
	ADD CONSTRAINT ck_antecipa_posterga 
    CHECK (antecipa_posterga = ANY (ARRAY[0, 1]));

ALTER TABLE public.categoria_tributo
	ADD CONSTRAINT ck_referencia_data 
    CHECK (referencia_data = ANY (ARRAY[0, 1]));

ALTER TABLE public.categoria_tributo
	ADD CONSTRAINT ck_tipo_aliquota 
    CHECK (tipo_aliquota = ANY (ARRAY[0, 1, 2]));

/* Create indexes */
CREATE INDEX ix_categoria_tributo_tributo		ON public.categoria_tributo (codigo_tributo);
CREATE INDEX ix_categoria_tributo_municipio_ect	ON public.categoria_tributo (codigo_municipio_ect);

/* Comments */
COMMENT ON TABLE public.categoria_tributo						IS 'Categorias de tributos.';
COMMENT ON COLUMN public.categoria_tributo.codigo				IS 'Código interno da categoria de tributo. Sequência: seq_categoria_tributo.';
COMMENT ON COLUMN public.categoria_tributo.codigo_tributo		IS 'Código interno do tributo.';
COMMENT ON COLUMN public.categoria_tributo.codigo_municipio_ect	IS 'Código interno do município (usado para o ISS).';
COMMENT ON COLUMN public.categoria_tributo.principal			IS 'Indica se é a categoria principal do tributo (true) ou não (false).';
COMMENT ON COLUMN public.categoria_tributo.codigo_receita		IS 'Código da receita da categoria de tributo.';
COMMENT ON COLUMN public.categoria_tributo.descricao			IS 'Descrição da categoria de tributo.';
COMMENT ON COLUMN public.categoria_tributo.tipo_aliquota		IS 'Tipo de aliquota da categoria de tributo: 0 - Fixa/Única / 1 - Fixa/Agregado / 2 - Variável.';
COMMENT ON COLUMN public.categoria_tributo.minimo_guia			IS 'Valor mínimo para emissão da guia.';
COMMENT ON COLUMN public.categoria_tributo.referencia_data		IS 'Data (referência) utilizada para cálculo da data de vencimento do tributo: 0 - Data de pagamento da NF / 1 - Data de emissão da NF';
COMMENT ON COLUMN public.categoria_tributo.periodo_apuracao		IS 'Período para cálculo da apruração: 1 (diário), 7 (semanal), 15 (quinzenal), 30 (mensal), 60 (bimestral) e 90 (trimestral).';
COMMENT ON COLUMN public.categoria_tributo.antecipa_posterga	IS 'Indica se a data de vencimento será antecipada (0) ou postergada (1).';
COMMENT ON COLUMN public.categoria_tributo.primeiro_dia			IS 'Número de dias após o 1º dia do mês para cálculo do vencimento.';
COMMENT ON COLUMN public.categoria_tributo.habilita_faturamento	IS 'Indica se a categoria está habilitada para seleção no Faturamento (true) ou não (false).';
COMMENT ON COLUMN public.categoria_tributo.vigencia_inicio		IS 'Início da data de vigência da categoria de tributo.';
COMMENT ON COLUMN public.categoria_tributo.vigencia_fim			IS 'Término da data de vigência da categoria de tributo.';
COMMENT ON COLUMN public.categoria_tributo.observacao			IS 'Observações da categoria de tributo.';
COMMENT ON COLUMN public.categoria_tributo.criador				IS 'Responsável (login) pela criação da categoria de tributo.';
COMMENT ON COLUMN public.categoria_tributo.data_criacao			IS 'Data/hora de criação da categoria de tributo.';
COMMENT ON COLUMN public.categoria_tributo.atualizador			IS 'Responsável (login) pela última atualização da categoria de tributo.';
COMMENT ON COLUMN public.categoria_tributo.data_atualizacao		IS 'Data/hora da última atualização da categoria de tributo.';

/* Set owner */
ALTER TABLE public.categoria_tributo OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.categoria_tributo		ADD CONSTRAINT fk_categoria_tributo_tributo 
    FOREIGN KEY (codigo_tributo)			REFERENCES public.tributo (codigo);
ALTER TABLE public.categoria_tributo		ADD CONSTRAINT fk_categoria_tributo_municipio_ect 
    FOREIGN KEY (codigo_municipio_ect)		REFERENCES public.municipio_ect (codigo);

ALTER TABLE public.aliquota					ADD CONSTRAINT fk_aliquota_categoria_tributo 
    FOREIGN KEY (codigo_categoria_tributo)	REFERENCES public.categoria_tributo (codigo);
