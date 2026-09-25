/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2022-02-17 19:44                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.servico_tipo	DROP CONSTRAINT fk_servico_tipo_reinf_servico;
ALTER TABLE public.servico_tipo	DROP CONSTRAINT fk_servico_tipo_servico_tipo;

ALTER TABLE public.nota_saida	DROP CONSTRAINT fk_nota_saida_servico_tipo;

ALTER TABLE public.faturamento	DROP CONSTRAINT fk_faturamento_servico_tipo;

ALTER TABLE public.nota_debito	DROP CONSTRAINT fk_nota_debito_servico_tipo;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "servico_tipo"                                 */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.servico_tipo DROP CONSTRAINT pk_servico_tipo;
ALTER TABLE public.servico_tipo DROP CONSTRAINT ck_servico_tipo_situacao;
ALTER TABLE public.servico_tipo DROP CONSTRAINT ck_servico_tipo_tipo_faturamento;

CREATE TABLE public.servico_tipo_TMP (
    codigo					BIGINT DEFAULT nextval('seq_servico_tipo')  NOT NULL,
    codigo_pai				BIGINT,
    codigo_reinf			BIGINT,
    sigla					CHARACTER VARYING(30)  NOT NULL,
    nome					CHARACTER VARYING(1000)  NOT NULL,
    situacao				SMALLINT DEFAULT 0  NOT NULL,
    habilita_faturamento	BOOLEAN DEFAULT false  NOT NULL,
    tipo_faturamento		SMALLINT DEFAULT 0  NOT NULL,
    faturamento_codigo_obra	CHARACTER VARYING(13),
    criador					CHARACTER VARYING(255),
    data_criacao			TIMESTAMP,
    atualizador				CHARACTER VARYING(255),
    data_atualizacao		TIMESTAMP);

INSERT INTO public.servico_tipo_TMP
    (codigo,codigo_pai,codigo_reinf,sigla,nome,situacao,habilita_faturamento,tipo_faturamento,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_pai,codigo_reinf,sigla,nome,situacao,habilita_faturamento,tipo_faturamento,criador,data_criacao,atualizador,data_atualizacao
FROM public.servico_tipo;

DROP INDEX public.uq_servico_tipo_sigla;
DROP INDEX public.ix_servico_tipo_reinf;
DROP INDEX public.ix_servico_tipo_pai;
DROP TABLE public.servico_tipo;

ALTER TABLE public.servico_tipo_TMP RENAME TO servico_tipo;

ALTER TABLE public.servico_tipo
	ADD CONSTRAINT pk_servico_tipo
    PRIMARY KEY (codigo);

ALTER TABLE public.servico_tipo
	ADD CONSTRAINT ck_servico_tipo_situacao
    CHECK (situacao = ANY (ARRAY[0, 1]));

ALTER TABLE public.servico_tipo
	ADD CONSTRAINT ck_servico_tipo_tipo_faturamento
    CHECK (tipo_faturamento = ANY (ARRAY[0, 1, 2]));

CREATE INDEX ix_servico_tipo_pai			ON public.servico_tipo (codigo_pai);
CREATE INDEX ix_servico_tipo_reinf			ON public.servico_tipo (codigo_reinf);
CREATE UNIQUE INDEX uq_servico_tipo_sigla	ON public.servico_tipo (sigla);

COMMENT ON TABLE public.servico_tipo							IS 'Tabela de tipos de serviço de cada Prefeitura Municipal.';
COMMENT ON COLUMN public.servico_tipo.codigo					IS 'Código interno do tipo de serviço. Sequência: seq_servico_tipo.';
COMMENT ON COLUMN public.servico_tipo.codigo_pai				IS 'Código interno do tipo de serviço ao qual o tipo de serviços está associado (auto relacionamento).';
COMMENT ON COLUMN public.servico_tipo.codigo_reinf				IS 'Associaãoo entre o código do serviço e classificaãoo do serviço para o EFD/Reinf (RFB).';
COMMENT ON COLUMN public.servico_tipo.sigla						IS 'Sigla do tipo de serviço. Chave única (com código do município).';
COMMENT ON COLUMN public.servico_tipo.nome						IS 'Nome/descriãoo do tipo de serviço.';
COMMENT ON COLUMN public.servico_tipo.situacao					IS 'Situaãoo do tipo de serviço: 0 = Inativo / 1 = Ativo.';
COMMENT ON COLUMN public.servico_tipo.habilita_faturamento		IS 'Indica se o tipo de serviço está habilitado para seleãoo no Faturamento (true) ou não (false).';
COMMENT ON COLUMN public.servico_tipo.tipo_faturamento			IS 'Relacionamento do tipo de serviço com o faturamento: 0 = Não habilitado para o faturamento / 1 = Habilitado para o faturamento / 2 = Habilitado para o faturamento E permite seleção da cidade da matriz do contrato como local de prestção dos serviços.';
COMMENT ON COLUMN public.servico_tipo.faturamento_codigo_obra	IS 'Código da obra na Nota Carioca (faturamento).';
COMMENT ON COLUMN public.servico_tipo.criador					IS 'Responsável (login) pela criaãoo do tipo de serviço.';
COMMENT ON COLUMN public.servico_tipo.data_criacao				IS 'Data/hora de criaãoo do tipo de serviço.';
COMMENT ON COLUMN public.servico_tipo.atualizador				IS 'Responsável (login) pela última atualizaãoo do tipo de serviço.';
COMMENT ON COLUMN public.servico_tipo.data_atualizacao			IS 'Data/hora da última atualizaãoo do tipo de serviço.';

ALTER TABLE public.servico_tipo OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.servico_tipo			ADD CONSTRAINT fk_servico_tipo_servico_tipo
    FOREIGN KEY (codigo_pai)			REFERENCES public.servico_tipo (codigo);
ALTER TABLE public.servico_tipo			ADD CONSTRAINT fk_servico_tipo_reinf_servico
    FOREIGN KEY (codigo_reinf)			REFERENCES public.reinf_servico (codigo);

ALTER TABLE public.nota_saida			ADD CONSTRAINT fk_nota_saida_servico_tipo
    FOREIGN KEY (codigo_servico_tipo)	REFERENCES public.servico_tipo (codigo);

ALTER TABLE public.faturamento			ADD CONSTRAINT fk_faturamento_servico_tipo
    FOREIGN KEY (codigo_servico_tipo)	REFERENCES public.servico_tipo (codigo);

ALTER TABLE public.nota_debito			ADD CONSTRAINT fk_nota_debito_servico_tipo
    FOREIGN KEY (codigo_servico_tipo)	REFERENCES public.servico_tipo (codigo);

/* ---------------------------------------------------------------------- */
/* Atualizaãoo de dados                                                   */
/* ---------------------------------------------------------------------- */
UPDATE	servico_tipo
SET		faturamento_codigo_obra = 'I', atualizador = 'admin', data_atualizacao = CURRENT_TIMESTAMP
WHERE	sigla LIKE '07.%'
AND		tipo_faturamento IN (1, 2);

UPDATE  servico_tipo
SET    faturamento_codigo_obra = NULL
WHERE  sigla IN ('07.06.07', '07.11.02', '07.10.02', '07.12.01', '07.19.01');
