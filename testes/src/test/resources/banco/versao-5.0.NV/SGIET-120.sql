/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-05-16 18:28                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Create sequence                                                        */
/* ---------------------------------------------------------------------- */
CREATE SEQUENCE public.seq_lote_interface_nota INCREMENT 1 MINVALUE 1 START 1;

/* ---------------------------------------------------------------------- */
/* Add table "lote_interface_nota"                                        */
/* ---------------------------------------------------------------------- */
/* Create table */
CREATE TABLE public.lote_interface_nota (
    codigo					BIGINT DEFAULT nextval('seq_lote_interface_nota')	NOT NULL,
    codigo_lote_interface	BIGINT												NOT NULL,
    codigo_nota				BIGINT												NOT NULL,
    tipo					SMALLINT											NOT NULL,
    CONSTRAINT pk_lote_interface_nota PRIMARY KEY (codigo)
);

/* Create indexes */
CREATE INDEX ix_lote_interface_nota_lote_interface					ON public.lote_interface_nota (codigo_lote_interface);
CREATE INDEX ix_lote_interface_nota_nota							ON public.lote_interface_nota (codigo_nota);
CREATE UNIQUE INDEX uq_lote_interface_nota_lote_interface_nota_tipo	ON public.lote_interface_nota (codigo_lote_interface,codigo_nota,tipo);

/* Create constraint */
ALTER TABLE public.lote_interface_nota ADD CONSTRAINT ck_lote_interface_nota_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1]));

/* Comments */
COMMENT ON TABLE public.lote_interface_nota							IS 'Tabela de relacionamento entre o lotes e notas.';
COMMENT ON COLUMN public.lote_interface_nota.codigo					IS 'Código interno do relacionamento entre lote e nota. Sequência: seq_lote_interface_nota.';
COMMENT ON COLUMN public.lote_interface_nota.codigo_lote_interface	IS 'Código interno do lote. Chave única (com codigo_nota e tipo).';
COMMENT ON COLUMN public.lote_interface_nota.codigo_nota			IS 'Código interno da nota. Chave única (com codigo_lote_interface e tipo).';
COMMENT ON COLUMN public.lote_interface_nota.tipo					IS 'Tipo do relacionamento entre lote e a nota: 0 - Envio para a Prefeitura / 1- Cancelamento. Chave única (com codigo_lote_interface e codigo_nota).';

/* Set owner */
ALTER TABLE public.lote_interface_nota OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.lote_interface_nota	ADD CONSTRAINT fk_lote_interface_nota_lote_interface 
    FOREIGN KEY (codigo_lote_interface)	REFERENCES public.lote_interface (codigo);

ALTER TABLE public.lote_interface_nota	ADD CONSTRAINT fk_lote_interface_nota_nota 
    FOREIGN KEY (codigo_nota)			REFERENCES public.nota (codigo);
