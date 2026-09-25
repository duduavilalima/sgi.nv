/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2019-02-27 20:02                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Create sequence                                                        */
/* ---------------------------------------------------------------------- */
CREATE SEQUENCE public.seq_nota_saida_parcela_motivo_desc_dev INCREMENT 1 MINVALUE 1 START 1;

/* ---------------------------------------------------------------------- */
/* Add table "nota_saida_parcela_motivo_desc_dev"                         */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.nota_saida_parcela_motivo_desc_dev (
    codigo								BIGINT			DEFAULT nextval('seq_nota_saida_parcela_motivo_desc_dev')	NOT NULL,
    codigo_nota_saida_parcela			BIGINT																		NOT NULL,
    codigo_motivo_desconto_devolucao	BIGINT																		NOT NULL,
    valor								NUMERIC(9,2)																NOT NULL,
	tipo								SMALLINT																	NOT NULL,
    CONSTRAINT pk_nota_saida_parcela_motivo_desc_dev PRIMARY KEY (codigo)
);

/* Create constraint */
ALTER TABLE public.nota_saida_parcela_motivo_desc_dev
	ADD CONSTRAINT ck_nota_saida_parcela_motivo_desc_dev_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1]));

/* Create indexes  */
CREATE INDEX ix_nota_saida_parcela_motivo_desc_dev_nota_saida_parcela			ON public.nota_saida_parcela_motivo_desc_dev (codigo_nota_saida_parcela);
CREATE INDEX ix_nota_saida_parcela_motivo_desc_dev_motivo_desconto_devolucao	ON public.nota_saida_parcela_motivo_desc_dev (codigo_motivo_desconto_devolucao);

/* Comments */
COMMENT ON TABLE public.nota_saida_parcela_motivo_desc_dev										IS 'Relacionamento entre as parcelas (duplicatas) das notas fiscais e os motivos de desconto e devolução.';
COMMENT ON COLUMN public.nota_saida_parcela_motivo_desc_dev.codigo								IS 'Código interno do relacionamento entre as parcelas (duplicatas) das notas fiscais e os motivos de desconto e devolução. Sequência: seq_nota_saida_parcela_motivo_desc_dev.';
COMMENT ON COLUMN public.nota_saida_parcela_motivo_desc_dev.codigo_nota_saida_parcela			IS 'Código interno da parcela (duplicata) da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela_motivo_desc_dev.codigo_motivo_desconto_devolucao	IS 'Código interno do motivo de desconto e/ou devolução das parcelas (duplicatas) das notas fiscais.';
COMMENT ON COLUMN public.nota_saida_parcela_motivo_desc_dev.valor								IS 'Valor do desconto ou devolução.';
COMMENT ON COLUMN public.nota_saida_parcela_motivo_desc_dev.tipo								IS 'Tipo: 0 = desconto / 1 = devolução.';

/* Set owner */
ALTER TABLE public.nota_saida_parcela_motivo_desc_dev OWNER TO sgi;

/* Adjusta data */
--Desconto
INSERT	INTO nota_saida_parcela_motivo_desc_dev (codigo_nota_saida_parcela, codigo_motivo_desconto_devolucao, valor)
SELECT	nsp.codigo, nsp.codigo_motivo_desconto, nsp.valor_desconto
FROM	nota_saida_parcela nsp
WHERE	nsp.codigo_motivo_desconto IS NOT NULL;

--Devolução
INSERT	INTO nota_saida_parcela_motivo_desc_dev (codigo_nota_saida_parcela, codigo_motivo_desconto_devolucao, valor)
SELECT	nsp.codigo, nsp.codigo_motivo_devolucao, nsp.valor_devolucao
FROM	nota_saida_parcela nsp
WHERE	nsp.codigo_motivo_devolucao IS NOT NULL;

/* ---------------------------------------------------------------------- */
/* Modify table "nota_saida_parcela"                                      */
/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints */
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_motivo_desconto;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_motivo_devolucao;

/* Drop indexes  */
DROP INDEX public.ix_nota_saida_parcela_motivo_desconto;
DROP INDEX public.ix_nota_saida_parcela_motivo_devolucao;

/* Drop columns */
ALTER TABLE public.nota_saida_parcela
	DROP codigo_motivo_desconto,
	DROP codigo_motivo_devolucao;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE nota_saida_parcela_motivo_desc_dev		ADD CONSTRAINT fk_nota_saida_parcela_motivo_desc_dev_nota_saida_parcela 
    FOREIGN KEY (codigo_nota_saida_parcela)			REFERENCES public.nota_saida_parcela (codigo);
ALTER TABLE nota_saida_parcela_motivo_desc_dev		ADD CONSTRAINT fk_nota_saida_parcela_motivo_desc_dev_motivo_desconto_devolucao 
    FOREIGN KEY (codigo_motivo_desconto_devolucao)	REFERENCES motivo_desconto_devolucao (codigo);
