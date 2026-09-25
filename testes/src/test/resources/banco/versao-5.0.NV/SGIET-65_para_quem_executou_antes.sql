/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-03-24 17:02                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.servico_tipo	DROP CONSTRAINT fk_servico_tipo_municipio_ect;
ALTER TABLE public.servico_tipo	DROP CONSTRAINT fk_servico_tipo_servico_tipo;

ALTER TABLE public.nota_saida	DROP CONSTRAINT fk_nota_saida_servico_tipo;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "servico_tipo"                                 */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.servico_tipo	DROP CONSTRAINT pk_servico_tipo;
ALTER TABLE public.servico_tipo	DROP CONSTRAINT ck_servico_tipo_situacao;

CREATE TABLE public.servico_tipo_TMP (
    codigo					BIGINT		DEFAULT nextval('seq_servico_tipo')	NOT NULL,
    codigo_municipio_ect	BIGINT											NOT NULL,
    codigo_pai				BIGINT,
    sigla					CHARACTER VARYING(30)							NOT NULL,
    nome					CHARACTER VARYING(1000)							NOT NULL,
    situacao				SMALLINT	DEFAULT 0							NOT NULL,
    habilita_faturamento	BOOLEAN		DEFAULT false						NOT NULL,
    criador					CHARACTER VARYING(255),
    data_criacao			TIMESTAMP,
    atualizador				CHARACTER VARYING(255),
    data_atualizacao		TIMESTAMP);

INSERT INTO public.servico_tipo_TMP
    (codigo,codigo_municipio_ect,codigo_pai,sigla,nome,situacao,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_municipio_ect,codigo_pai,sigla,nome,situacao,criador,data_criacao,atualizador,data_atualizacao
FROM public.servico_tipo;

DROP INDEX public.uq_servico_tipo_municipio_ect_sigla;
DROP INDEX public.ix_servico_tipo_municipio_ect;
DROP INDEX public.ix_servico_tipo_pai;

DROP TABLE public.servico_tipo;

ALTER TABLE public.servico_tipo_TMP
	RENAME TO servico_tipo;

ALTER TABLE public.servico_tipo
	ADD CONSTRAINT pk_servico_tipo 
    PRIMARY KEY (codigo);

ALTER TABLE public.servico_tipo
	ADD CONSTRAINT ck_servico_tipo_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1]));

CREATE UNIQUE INDEX uq_servico_tipo_municipio_ect_sigla		ON public.servico_tipo (codigo_municipio_ect,sigla);
CREATE INDEX ix_servico_tipo_municipio_ect					ON public.servico_tipo (codigo_municipio_ect);
CREATE INDEX ix_servico_tipo_pai							ON public.servico_tipo (codigo_pai);

COMMENT ON TABLE public.servico_tipo						IS 'Tabela de tipos de serviço de cada Prefeitura Municipal.';
COMMENT ON COLUMN public.servico_tipo.codigo				IS 'Código interno do tipo de serviço. Sequência: seq_servico_tipo.';
COMMENT ON COLUMN public.servico_tipo.codigo_municipio_ect	IS 'Código interno do município. Chave única (com sigla).';
COMMENT ON COLUMN public.servico_tipo.codigo_pai			IS 'Código interno do tipo de serviço ao qual o tipo de serviços está associado (auto relacionamento).';
COMMENT ON COLUMN public.servico_tipo.sigla					IS 'Sigla do tipo de serviço. Chave única (com código do município).';
COMMENT ON COLUMN public.servico_tipo.nome					IS 'Nome/descrição do tipo de serviço.';
COMMENT ON COLUMN public.servico_tipo.situacao				IS 'Situação do tipo de serviço: 0 = Inativo / 1 = Ativo.';
COMMENT ON COLUMN public.servico_tipo.habilita_faturamento	IS 'Indica se o tipo de serviço está habilitado para seleção no Faturamento (true) ou não (false).';
COMMENT ON COLUMN public.servico_tipo.criador				IS 'Responsável (login) pela criação do tipo de serviço.';
COMMENT ON COLUMN public.servico_tipo.data_criacao			IS 'Data/hora de criação do tipo de serviço.';
COMMENT ON COLUMN public.servico_tipo.atualizador			IS 'Responsável (login) pela última atualização do tipo de serviço.';
COMMENT ON COLUMN public.servico_tipo.data_atualizacao		IS 'Data/hora da última atualização do tipo de serviço.';

UPDATE servico_tipo SET situacao = 1;
UPDATE servico_tipo SET habilita_faturamento = true WHERE sigla IN ('07.02.16', '07.02.18', '07.02.19', '07.02.20', '07.02.98', '07.10.02', '07.10.08', '07.12.01', '07.19.01', '14.01.07', '14.06.02', '17.05.01', '26.01.01', '31.01.01');

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.servico_tipo			ADD CONSTRAINT fk_servico_tipo_municipio_ect 
    FOREIGN KEY (codigo_municipio_ect)	REFERENCES public.municipio_ect (codigo);
ALTER TABLE public.servico_tipo			ADD CONSTRAINT fk_servico_tipo_servico_tipo 
    FOREIGN KEY (codigo_pai)			REFERENCES public.servico_tipo (codigo);

ALTER TABLE public.nota_saida			ADD CONSTRAINT fk_nota_saida_servico_tipo 
    FOREIGN KEY (codigo_servico_tipo)	REFERENCES public.servico_tipo (codigo);
