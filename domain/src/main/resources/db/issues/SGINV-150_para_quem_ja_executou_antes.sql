/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-12-01 18:05                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.conciliacao_lancamento DROP CONSTRAINT fk_conciliacao_lancamento_conciliacao_bancaria;

ALTER TABLE public.conciliacao_lancamento_associacao DROP CONSTRAINT fk_conciliacao_lancamento_associacao_conciliacao_lancamento;
ALTER TABLE public.conciliacao_lancamento_associacao DROP CONSTRAINT fk_conciliacao_lancamento_associacao_nota_saida_parcela;
ALTER TABLE public.conciliacao_lancamento_associacao DROP CONSTRAINT fk_conciliacao_lancamento_associacao_item_relacao;
ALTER TABLE public.conciliacao_lancamento_associacao DROP CONSTRAINT fk_conciliacao_lancamento_associacao_lancamento;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "conciliacao_lancamento"                       */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.conciliacao_lancamento
	DROP CONSTRAINT pk_conciliacao_lancamento;

ALTER TABLE public.conciliacao_lancamento
	DROP CONSTRAINT ck_conciliacao_lancamento_debito_credito;

CREATE TABLE public.conciliacao_lancamento_TMP (
    codigo						BIGINT					DEFAULT nextval('seq_conciliacao_lancamento')	NOT NULL,
    codigo_conciliacao_bancaria	BIGINT,
    registro					SMALLINT																NOT NULL,
    natureza					CHARACTER(3)															NOT NULL,
    lancamento					DATE																	NOT NULL,
    situacao					SMALLINT				DEFAULT 0										NOT NULL,
    historico					CHARACTER VARYING(60)													NOT NULL,
    valor						NUMERIC(19,2)															NOT NULL,
    debito_credito				CHARACTER(1)															NOT NULL);

INSERT INTO public.conciliacao_lancamento_TMP
    (codigo,codigo_conciliacao_bancaria,registro,natureza,lancamento,situacao,valor,debito_credito)
SELECT
    codigo,codigo_conciliacao_bancaria,registro,natureza,lancamento,situacao,valor,debito_credito
FROM public.conciliacao_lancamento;

DROP INDEX public.ix_conciliacao_lancamento_conciliacao_bancaria;

DROP TABLE public.conciliacao_lancamento;

ALTER TABLE public.conciliacao_lancamento_TMP
	RENAME TO conciliacao_lancamento;

/* Create primary key */
ALTER TABLE public.conciliacao_lancamento
	ADD CONSTRAINT pk_conciliacao_lancamento 
    PRIMARY KEY (codigo);

/* Create constraint */
ALTER TABLE public.conciliacao_lancamento
	ADD CONSTRAINT ck_conciliacao_lancamento_debito_credito 
    CHECK ((debito_credito) = ANY (ARRAY['D', 'C']));

/* Create index */
CREATE INDEX ix_conciliacao_lancamento_conciliacao_bancaria ON public.conciliacao_lancamento (codigo_conciliacao_bancaria);

/* Comments */
COMMENT ON TABLE public.conciliacao_lancamento								IS 'Lançamentos da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_lancamento.codigo						IS 'Código interno do lançamento na conciliação bancária. Sequência: seq_conciliacao_lancamento.';
COMMENT ON COLUMN public.conciliacao_lancamento.codigo_conciliacao_bancaria	IS 'Código interno da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_lancamento.registro					IS 'Número do registro do lançamento na conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_lancamento.natureza					IS 'Natureza do lançamento na conciliação bancária: CDS = Lançamento composto de recursos disponíveis e bloqueados / DPV = Lançamento ocorrido em recurso "disponível" / SCR = Lançamento ocorrido em recurso "disponível", porém não liberado para uso / SSR = Lançamento ocorrido em recurso "a compensar".';
COMMENT ON COLUMN public.conciliacao_lancamento.lancamento					IS 'Data do lançamento na conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_lancamento.situacao					IS 'Situação do lançamento na conciliação bancária: 0 = Pendente / 1 = Não pendente / 2 = Cancelado.';
COMMENT ON COLUMN public.conciliacao_lancamento.historico					IS 'Histórico do lançamento na conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_lancamento.valor						IS 'Valor do lançamento na conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_lancamento.debito_credito				IS 'Indica se o lançamento na conciliação bancária é de débito (D) ou crédito (C).';

/* Set owner */
ALTER TABLE public.conciliacao_lancamento OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Modify table "conciliacao_lancamento_associacao"                       */
/* ---------------------------------------------------------------------- */
/* Drop constraints */
ALTER TABLE public.conciliacao_lancamento_associacao
	DROP CONSTRAINT cc_conciliacao_lancamento_associacao_codigo_item;

ALTER TABLE public.conciliacao_lancamento_associacao
	DROP CONSTRAINT ck_conciliacao_lancamento_associacao_tipo;

/* Create constraints */
ALTER TABLE public.conciliacao_lancamento_associacao
	ADD CONSTRAINT ck_conciliacao_lancamento_associacao_codigo_item 
    CHECK (	((codigo_item_relacao			IS NOT NULL) AND (codigo_lancamento_tesouraria	IS NULL) AND (codigo_nota_saida_parcela		IS NULL)) OR
			((codigo_lancamento_tesouraria	IS NOT NULL) AND (codigo_item_relacao			IS NULL) AND (codigo_nota_saida_parcela		IS NULL)) OR
			((codigo_nota_saida_parcela		IS NOT NULL) AND (codigo_item_relacao			IS NULL) AND (codigo_lancamento_tesouraria	IS NULL))	);

ALTER TABLE public.conciliacao_lancamento_associacao
	ADD CONSTRAINT ck_conciliacao_lancamento_associacao_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1, 2]));

/* Comments */
COMMENT ON TABLE public.conciliacao_lancamento_associacao									IS 'Associação entre um lançamento de conciliação bancária e um item de RB, lançamento de Tesouraria, NF/Saída, etc., ou seja, com o(s) lançamento(s) de débito(s) ou crédito(s).';
COMMENT ON COLUMN public.conciliacao_lancamento_associacao.codigo							IS 'Código interno da associação entre o lançamento da conciliação bancária e um dos itens. Sequência: seq_conciliacao_lancamento_associacao.';
COMMENT ON COLUMN public.conciliacao_lancamento_associacao.codigo_conciliacao_lancamento	IS 'Código interno do lançamento na conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_lancamento_associacao.codigo_item_relacao				IS 'Código interno do item da relação bancária associada ao lançamanto da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_lancamento_associacao.codigo_lancamento_tesouraria		IS 'Código interno do lançamento relacionado à Tesouraria.';
COMMENT ON COLUMN public.conciliacao_lancamento_associacao.codigo_nota_saida_parcela		IS 'Código interno da parcela da nota de saída associada ao lançamanto da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_lancamento_associacao.tipo								IS 'Tipo da associação entre o lançamento da conciliação bancária e um dos itens: 0 = Associação com item de relação bancária / 1 = Associação com lançamento de Tesouraria / 2 = Associação com parcela de nota de saída.';

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.conciliacao_lancamento				ADD CONSTRAINT fk_conciliacao_lancamento_conciliacao_bancaria 
    FOREIGN KEY (codigo_conciliacao_bancaria)			REFERENCES public.conciliacao_bancaria (codigo);

ALTER TABLE public.conciliacao_lancamento_associacao	ADD CONSTRAINT fk_conciliacao_lancamento_associacao_conciliacao_lancamento 
    FOREIGN KEY (codigo_conciliacao_lancamento)			REFERENCES public.conciliacao_lancamento (codigo);
ALTER TABLE public.conciliacao_lancamento_associacao	ADD CONSTRAINT fk_conciliacao_lancamento_associacao_nota_saida_parcela 
    FOREIGN KEY (codigo_nota_saida_parcela)				REFERENCES public.nota_saida_parcela (codigo);
ALTER TABLE public.conciliacao_lancamento_associacao	ADD CONSTRAINT fk_conciliacao_lancamento_associacao_item_relacao 
    FOREIGN KEY (codigo_item_relacao)					REFERENCES public.item_relacao (codigo);
ALTER TABLE public.conciliacao_lancamento_associacao	ADD CONSTRAINT fk_conciliacao_lancamento_associacao_lancamento 
    FOREIGN KEY (codigo_lancamento_tesouraria)			REFERENCES public.lancamento (codigo);
