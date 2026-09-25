/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2018-09-24 17:40                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Create sequence                                                        */
/* ---------------------------------------------------------------------- */
CREATE SEQUENCE public.seq_nota_tributo_aliquota_emissao INCREMENT 1 MINVALUE 1 START 1;

/* Set owner */
ALTER TABLE public.seq_nota_tributo_aliquota_emissao OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add table "nota_tributo_aliquota_emissao"                              */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.nota_tributo_aliquota_emissao (
    codigo			BIGINT			DEFAULT nextval('seq_nota_tributo_aliquota_emissao')	NOT NULL,
    codigo_nota		BIGINT																	NOT NULL,
    codigo_aliquota	BIGINT																	NOT NULL,
    base_calculo	NUMERIC(14,2)															NOT NULL,
    percentual		NUMERIC(5,2)															NOT NULL,
    valor			NUMERIC(14,2)	DEFAULT 0.00											NOT NULL,
    CONSTRAINT pk_nota_tributo_aliquota_emissao PRIMARY KEY (codigo)
);

/* Create indexes */
CREATE INDEX ix_nota_tributo_aliquota_emissao_aliquota	ON public.nota_tributo_aliquota_emissao (codigo_aliquota);
CREATE INDEX ix_nota_tributo_aliquota_emissao_nota		ON public.nota_tributo_aliquota_emissao (codigo_nota);

/* Comments */
COMMENT ON TABLE public.nota_tributo_aliquota_emissao					IS 'Relacionamentos entre categorias de tributos e notas (só Faturamento). Contém os tributos (e categorias) associados a uma nota, no momento da emissão.';
COMMENT ON COLUMN public.nota_tributo_aliquota_emissao.codigo			IS 'Código interno do relacionamentos entre categorias de tributos e notas. Sequência: seq_nota_tributo_aliquota_emissao.';
COMMENT ON COLUMN public.nota_tributo_aliquota_emissao.codigo_nota		IS 'Código interno da nota.';
COMMENT ON COLUMN public.nota_tributo_aliquota_emissao.codigo_aliquota	IS 'Código interno da categoria de tributo (na emissão).';
COMMENT ON COLUMN public.nota_tributo_aliquota_emissao.base_calculo		IS 'Base de cálculo para o valor do tributo deste relacionamento nota (na emissão) x categoria de tributo.';
COMMENT ON COLUMN public.nota_tributo_aliquota_emissao.percentual		IS 'Percentual para cálculo do valor do tributo deste relacionamento nota (na emissão) x categoria de tributo.';
COMMENT ON COLUMN public.nota_tributo_aliquota_emissao.valor			IS 'Valor do tributo deste relacionamento nota (na emissão) x categoria de tributo.';

/* Set owner */
ALTER TABLE public.seq_nota_tributo_aliquota_emissao OWNER TO sgi;

/* Adjust data */
INSERT	INTO nota_tributo_aliquota_emissao (codigo_nota, codigo_aliquota, base_calculo, percentual, valor)
SELECT	nota_tributo_aliquota.codigo_nota, nota_tributo_aliquota.codigo_aliquota, nota_tributo_aliquota.base_calculo, nota_tributo_aliquota.percentual, nota_tributo_aliquota.valor
FROM	nota_tributo_aliquota, nota, nota_fiscal
WHERE	nota_tributo_aliquota.codigo_nota = nota.codigo
AND		nota.codigo = nota_fiscal.codigo
AND		nota_fiscal.tipo_nota_fiscal = 11;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_tributo_aliquota_emissao	ADD CONSTRAINT fk_nota_tributo_aliquota_emissao_aliquota 
    FOREIGN KEY (codigo_aliquota)					REFERENCES public.aliquota (codigo);
ALTER TABLE public.nota_tributo_aliquota_emissao	ADD CONSTRAINT fk_nota_tributo_aliquota_emissao_nota 
    FOREIGN KEY (codigo_nota)						REFERENCES public.nota (codigo);
