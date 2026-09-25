/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-12-07 12:30                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.conciliacao_bancaria		DROP CONSTRAINT fk_conciliacao_bancaria_conta_corrente;
ALTER TABLE public.conciliacao_bancaria		DROP CONSTRAINT fk_conciliacao_bancaria_anexo;

ALTER TABLE public.conciliacao_lancamento	DROP CONSTRAINT fk_conciliacao_lancamento_conciliacao_bancaria;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "conciliacao_bancaria"                         */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */

/* Drop check constraint */
ALTER TABLE public.conciliacao_bancaria
	DROP CONSTRAINT pk_conciliacao_bancaria;

/* Create temporary table */
CREATE TABLE public.conciliacao_bancaria_TMP (
    codigo					BIGINT					DEFAULT nextval('seq_conciliacao_bancaria')	NOT NULL,
    codigo_conta_corrente	BIGINT																NOT NULL,
    codigo_anexo			BIGINT																NOT NULL,
    data					DATE																NOT NULL,
    situacao				SMALLINT				DEFAULT 0									NOT NULL,
    saldo_inicial			NUMERIC(19,2)			DEFAULT 0.00								NOT NULL,
    total_debito			NUMERIC(19,2)			DEFAULT 0.00								NOT NULL,
    total_credito			NUMERIC(19,2)			DEFAULT 0.00								NOT NULL,
    saldo_final				NUMERIC(19,2)			DEFAULT 0.00								NOT NULL,
    criador					CHARACTER VARYING(255),
    data_criacao			TIMESTAMP,
    atualizador				CHARACTER VARYING(255),
    data_atualizacao		TIMESTAMP);

/* Move data to the temporary table */
INSERT INTO public.conciliacao_bancaria_TMP
    (codigo,codigo_conta_corrente,codigo_anexo,data,situacao,total_debito,total_credito,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_conta_corrente,codigo_anexo,data,situacao,total_debito,total_credito,criador,data_criacao,atualizador,data_atualizacao
FROM public.conciliacao_bancaria;

/* Create indexes */
DROP INDEX public.ix_conciliacao_bancaria_conta_corrente;
DROP INDEX public.ix_conciliacao_bancaria_anexo;

/* Drop "old" table */
DROP TABLE public.conciliacao_bancaria;

/* Rename temporary table to "new" table */
ALTER TABLE public.conciliacao_bancaria_TMP
	RENAME TO conciliacao_bancaria;

/* Add primary key */
ALTER TABLE public.conciliacao_bancaria
	ADD CONSTRAINT pk_conciliacao_bancaria 
    PRIMARY KEY (codigo);

/* Create indexes */
CREATE INDEX ix_conciliacao_bancaria_conta_corrente	ON public.conciliacao_bancaria (codigo_conta_corrente);
CREATE INDEX ix_conciliacao_bancaria_anexo			ON public.conciliacao_bancaria (codigo_anexo);

/* Comments */
COMMENT ON TABLE public.conciliacao_bancaria						IS 'Conciliação bancária realizada.';
COMMENT ON COLUMN public.conciliacao_bancaria.codigo				IS 'Código interno da conciliação bancária realizada. Sequência: seq_conciliacao_bancaria.';
COMMENT ON COLUMN public.conciliacao_bancaria.codigo_conta_corrente	IS 'Código interno da conta corrente que foi feita a conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_bancaria.codigo_anexo			IS 'Código interno do arquivo anexado para a conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_bancaria.data					IS 'Data da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_bancaria.situacao				IS 'Situação da conciliação bancária: 0 = Conciliada com pendências / 1 = Conciliada sem pendências / 2 = Cancelada.';
COMMENT ON COLUMN public.conciliacao_bancaria.saldo_inicial			IS 'Saldo inicial da data.';
COMMENT ON COLUMN public.conciliacao_bancaria.total_debito			IS 'Total de débitos da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_bancaria.total_credito			IS 'Total de créditos da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_bancaria.saldo_final			IS 'Saldo final da data.';
COMMENT ON COLUMN public.conciliacao_bancaria.criador				IS 'Responsável (login) pela criação da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_bancaria.data_criacao			IS 'Data/hora de criação da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_bancaria.atualizador			IS 'Responsável (login) pela última atualização da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_bancaria.data_atualizacao		IS 'Data/hora da última atualização da conciliação bancária.';

/* Set owner */
ALTER TABLE public.conciliacao_bancaria OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.conciliacao_bancaria			ADD CONSTRAINT fk_conciliacao_bancaria_conta_corrente 
    FOREIGN KEY (codigo_conta_corrente)			REFERENCES public.conta_corrente (codigo);
ALTER TABLE public.conciliacao_bancaria			ADD CONSTRAINT fk_conciliacao_bancaria_anexo 
    FOREIGN KEY (codigo_anexo)					REFERENCES public.anexo (codigo);

ALTER TABLE public.conciliacao_lancamento		ADD CONSTRAINT fk_conciliacao_lancamento_conciliacao_bancaria 
    FOREIGN KEY (codigo_conciliacao_bancaria)	REFERENCES public.conciliacao_bancaria (codigo);
