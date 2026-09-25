/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2019-06-11 16:54                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.dados_bancarios	DROP CONSTRAINT fk_dados_bancarios_banco;

ALTER TABLE public.pagamento		DROP CONSTRAINT fk_pagamento_banco;

ALTER TABLE public.item_relacao		DROP CONSTRAINT fk_item_relacao_banco;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "banco"                                        */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.banco DROP CONSTRAINT pk_banco;

/* Create "new" table */
CREATE TABLE public.banco_TMP (
    codigo				BIGINT								NOT NULL,
    nome				CHARACTER VARYING(50)				NOT NULL,
    descricao			CHARACTER VARYING(50),
    situacao			SMALLINT				DEFAULT 1	NOT NULL,
    criador				CHARACTER VARYING(255),
    data_criacao		TIMESTAMP,
    atualizador			CHARACTER VARYING(255),
    data_atualizacao	TIMESTAMP);

/* Move data from "old" table to "new" table */
INSERT INTO public.banco_TMP
    (codigo,nome,descricao,criador,data_criacao,atualizador,data_atualizacao,situacao)
SELECT
    codigo,nome,descricao,criador,data_criacao,atualizador,data_atualizacao,1
FROM public.banco;

/* Drop index */
DROP INDEX public.uq_banco_nome;

/* Drop "old" table  */
DROP TABLE public.banco;

/* Rename "old" table to "new" table */
ALTER TABLE public.banco_TMP
	RENAME TO banco;

/* Create primary key */
ALTER TABLE public.banco
	ADD CONSTRAINT pk_banco 
    PRIMARY KEY (codigo);

/* Create constraint */
ALTER TABLE public.banco
	ADD CONSTRAINT ck_banco_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1]));

/* Create index */
CREATE UNIQUE INDEX uq_banco_nome ON public.banco (nome);

/* Comments */
COMMENT ON TABLE public.banco					IS 'Bancos (instituições financeiras).';
COMMENT ON COLUMN public.banco.codigo			IS 'Código do banco (conforme o código no Bacen).';
COMMENT ON COLUMN public.banco.nome				IS 'Nome do banco.';
COMMENT ON COLUMN public.banco.descricao		IS 'Descrição do banco.';
COMMENT ON COLUMN public.banco.situacao			IS 'Situação do banco: 0 = Inativo / 1 = Ativo.';
COMMENT ON COLUMN public.banco.criador			IS 'Responsável (login) pela criação do banco.';
COMMENT ON COLUMN public.banco.data_criacao		IS 'Data/hora de criação do banco.';
COMMENT ON COLUMN public.banco.atualizador		IS 'Responsável (login) pela última atualização do banco.';
COMMENT ON COLUMN public.banco.data_atualizacao	IS 'Data/hora da última atualização do banco.';

/* Set owner */
ALTER TABLE public.banco OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.dados_bancarios				ADD CONSTRAINT fk_dados_bancarios_banco 
    FOREIGN KEY (codigo_banco)					REFERENCES public.banco (codigo);

ALTER TABLE public.pagamento					ADD CONSTRAINT fk_pagamento_banco 
    FOREIGN KEY (codigo_banco_boleto)			REFERENCES public.banco (codigo);

ALTER TABLE public.item_relacao					ADD CONSTRAINT fk_item_relacao_banco 
    FOREIGN KEY (codigo_banco)					REFERENCES public.banco (codigo);

-- ALTER TABLE public.historico_conciliacao_tipo	ADD CONSTRAINT fk_historico_conciliacao_tipo_banco 
--     FOREIGN KEY (codigo_banco)					REFERENCES public.banco (codigo);
