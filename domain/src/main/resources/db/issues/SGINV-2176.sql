/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2019-03-28 18:26                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Create sequence                                                        */
/* ---------------------------------------------------------------------- */
CREATE SEQUENCE public.seq_centro_financeiro_previsao INCREMENT 1 MINVALUE 1 START 1;

/* ---------------------------------------------------------------------- */
/* Add table "centro_financeiro_previsao"                                 */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.centro_financeiro_previsao (
    codigo						BIGINT			DEFAULT nextval('seq_centro_financeiro_previsao')	NOT NULL,
    codigo_centro_financeiro	BIGINT																NOT NULL,
    competencia					CHARACTER(6)														NOT NULL,
    valor_contratual			NUMERIC(11,2)	DEFAULT 0.00										NOT NULL,
    valor_extra					NUMERIC(11,2)	DEFAULT 0.00										NOT NULL,
    criador						CHARACTER VARYING(255),
    data_criacao				TIMESTAMP,
    atualizador					CHARACTER VARYING(255),
    data_atualizacao			TIMESTAMP,
    CONSTRAINT pk_centro_financeiro_previsao PRIMARY KEY (codigo)
);

CREATE INDEX ix_centro_financeiro_previsao_centro_financeiro					ON public.centro_financeiro_previsao (codigo_centro_financeiro);
CREATE UNIQUE INDEX uq_centro_financeiro_previsao_centro_financeiro_competencia	ON public.centro_financeiro_previsao (codigo_centro_financeiro,competencia);

COMMENT ON TABLE public.centro_financeiro_previsao								IS 'Previsão de faturamento (contratual e extra contratual) dos centros financeiros.';
COMMENT ON COLUMN public.centro_financeiro_previsao.codigo						IS 'Código interno da previsão do centro financeiro. Sequência: seq_centro_financeiro_previsao.';
COMMENT ON COLUMN public.centro_financeiro_previsao.codigo_centro_financeiro	IS 'Código interno do centro financeiro.';
COMMENT ON COLUMN public.centro_financeiro_previsao.competencia					IS 'Competência (ano e mês) da previsão do centro financeiro (formato: AAAAMM, onde AAAA é o ano com 4 dígitos e MM é o mês com 2 dígitos).';
COMMENT ON COLUMN public.centro_financeiro_previsao.valor_contratual			IS 'Valor da previsão de faturamento contratual do centro financeiro.';
COMMENT ON COLUMN public.centro_financeiro_previsao.valor_extra					IS 'Valor da previsão de faturamento extra contratual do centro financeiro.';
COMMENT ON COLUMN public.centro_financeiro_previsao.criador						IS 'Responsável (login) pela criação da previsão do centro financeiro.';
COMMENT ON COLUMN public.centro_financeiro_previsao.data_criacao				IS 'Data/hora de criação da previsão do centro financeiro.';
COMMENT ON COLUMN public.centro_financeiro_previsao.atualizador					IS 'Responsável (login) pela última atualização da previsão do centro financeiro.';
COMMENT ON COLUMN public.centro_financeiro_previsao.data_atualizacao			IS 'Data/hora da última atualização da previsão do centro financeiro.';

/* Set owner */
ALTER TABLE public.centro_financeiro_previsao OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.centro_financeiro_previsao	ADD CONSTRAINT fk_centro_financeiro_previsao_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)		REFERENCES public.centro_financeiro (codigo);
