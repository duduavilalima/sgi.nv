/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2018-08-20 14:53                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Create sequence                                                        */
/* ---------------------------------------------------------------------- */
CREATE SEQUENCE public.seq_faturamento_contato INCREMENT 1 MINVALUE 1 START 1;

/* Set owner */
ALTER TABLE public.seq_faturamento_contato OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add table "faturamento_contato"                                        */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.faturamento_contato (
    codigo				BIGINT DEFAULT nextval('seq_faturamento_contato')	NOT NULL,
    codigo_faturamento	BIGINT												NOT NULL,
    codigo_contato		BIGINT												NOT NULL,
    ordem				SMALLINT											NOT NULL,
    CONSTRAINT pk_faturamento_contato PRIMARY KEY (codigo)
);

/* Create indexes */
CREATE INDEX ix_faturamento_contato_faturamento				ON public.faturamento_contato (codigo_faturamento);
CREATE INDEX ix_faturamento_contato_contato					ON public.faturamento_contato (codigo_contato);
CREATE UNIQUE INDEX uq_faturamento_contato_contrato_contato	ON public.faturamento_contato (codigo_faturamento,codigo_contato);

/* Comments */
COMMENT ON TABLE public.faturamento_contato						IS 'Relacionamento entre faturamento e contato.';
COMMENT ON COLUMN public.faturamento_contato.codigo				IS 'Código interno da associação faturamento e contato. Sequência: seq_faturamento_contato.';
COMMENT ON COLUMN public.faturamento_contato.codigo_faturamento	IS 'Código interno do faturamento.';
COMMENT ON COLUMN public.faturamento_contato.codigo_contato		IS 'Código interno do contato.';
COMMENT ON COLUMN public.faturamento_contato.ordem				IS 'Ordem do contato na lista de contatos do faturamento.';

/* Set owner */
ALTER TABLE public.faturamento_contato OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.faturamento_contato	ADD CONSTRAINT fk_faturamento_contato_faturamento 
    FOREIGN KEY (codigo_faturamento)	REFERENCES public.faturamento (codigo);
ALTER TABLE public.faturamento_contato	ADD CONSTRAINT fk_faturamento_contato_contato 
    FOREIGN KEY (codigo_contato)		REFERENCES public.contato (codigo);
