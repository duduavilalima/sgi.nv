/* ---------------------------------------------------------------------- */
/* Modify table "dados_bancarios"                                         */
/* ---------------------------------------------------------------------- */
ALTER TABLE dados_bancarios
	ALTER COLUMN codigo SET DEFAULT nextval('seq_dados_bancarios');

/* ---------------------------------------------------------------------- */
/* Modify table "aliquota"                                                */
/* ---------------------------------------------------------------------- */
ALTER TABLE aliquota
	ALTER COLUMN codigo SET DEFAULT nextval('seq_aliquota');

/* ---------------------------------------------------------------------- */
/* Modify table "categoria_tributo"                                       */
/* ---------------------------------------------------------------------- */
ALTER TABLE categoria_tributo
	ALTER COLUMN codigo SET DEFAULT nextval('seq_categoria_tributo');

/* ---------------------------------------------------------------------- */
/* Modify table "tributo"                                                 */
/* ---------------------------------------------------------------------- */
ALTER TABLE tributo
	ALTER COLUMN codigo SET DEFAULT nextval('seq_tributo');

/* ---------------------------------------------------------------------- */
/* Modify table "centro_financeiro"                                       */
/* ---------------------------------------------------------------------- */
ALTER TABLE centro_financeiro
	ALTER COLUMN codigo SET DEFAULT nextval('seq_centro_financeiro');

/* ---------------------------------------------------------------------- */
/* Modify table "despesa"                                                 */
/* ---------------------------------------------------------------------- */
ALTER TABLE despesa
	ALTER COLUMN codigo SET DEFAULT nextval('seq_despesa');

/* ---------------------------------------------------------------------- */
/* Modify table "faturamento"                                             */
/* ---------------------------------------------------------------------- */
ALTER TABLE faturamento
	ALTER COLUMN codigo SET DEFAULT nextval('seq_faturamento');

/* ---------------------------------------------------------------------- */
/* Modify table "faturamento_aliquota"                                    */
/* ---------------------------------------------------------------------- */
ALTER TABLE faturamento_aliquota
	ALTER COLUMN codigo SET DEFAULT nextval('seq_faturamento_aliquota');

/* ---------------------------------------------------------------------- */
/* Modify table "faturamento_etapa"                                       */
/* ---------------------------------------------------------------------- */
ALTER TABLE faturamento_etapa
	ALTER COLUMN codigo SET DEFAULT nextval('seq_faturamento_etapa');

/* ---------------------------------------------------------------------- */
/* Modify table "motivo_retorno_documento"                                */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.motivo_retorno_documento
	ALTER COLUMN codigo SET DEFAULT nextval('seq_motivo_retorno_documento');

/* ---------------------------------------------------------------------- */
/* Modify table "motivo_retorno_documento_etapa"                          */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.motivo_retorno_documento_etapa
	ALTER COLUMN codigo SET DEFAULT nextval('seq_motivo_retorno_documento_etapa');

/* ---------------------------------------------------------------------- */
/* Modify table "motivo_retorno_documento_faturamento"                    */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.motivo_retorno_documento_faturamento
	ALTER COLUMN codigo SET DEFAULT nextval('seq_motivo_retorno_documento_faturamento');

/* ---------------------------------------------------------------------- */
/* Modify table "motivo_retorno_motivo_retorno_documento"                 */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.motivo_retorno_motivo_retorno_documento
	ALTER COLUMN codigo SET DEFAULT nextval('seq_motivo_retorno_motivo_retorno_documento');

/* ---------------------------------------------------------------------- */
/* Modify table "contato"                                                 */
/* ---------------------------------------------------------------------- */
ALTER TABLE contato
   ALTER COLUMN codigo SET DEFAULT nextval('seq_contato');
   
/* ---------------------------------------------------------------------- */
/* Modify table "pessoa"                                                  */
/* ---------------------------------------------------------------------- */
ALTER TABLE pessoa
	ALTER COLUMN codigo SET DEFAULT nextval('seq_pessoa');

/* ---------------------------------------------------------------------- */
/* Modify table "endereco"                                                */
/* ---------------------------------------------------------------------- */
ALTER TABLE endereco
    ALTER COLUMN codigo SET DEFAULT nextval('seq_endereco');
    
/* ---------------------------------------------------------------------- */
/* Modify table "receptor"                                                */
/* ---------------------------------------------------------------------- */
ALTER TABLE receptor
    ALTER COLUMN codigo SET DEFAULT nextval('seq_receptor');

/* ---------------------------------------------------------------------- */
/* Modify table "lote_interface"                                          */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.lote_interface ALTER COLUMN codigo SET DEFAULT nextval('seq_lote_interface');

/* ---------------------------------------------------------------------- */
/* Modify table "contrato_categoria_contrato"                             */
/* ---------------------------------------------------------------------- */
CREATE INDEX ix_contrato_categoria_contrato_contrato			ON public.contrato_categoria_contrato (codigo_contrato);
CREATE INDEX ix_contrato_categoria_contrato_categoria_contrato	ON public.contrato_categoria_contrato (codigo_categoria_contrato);

COMMENT ON TABLE public.contrato_categoria_contrato								IS 'Relacionamento entre contrato e categorias de contrato (um contrato pode ter mais de uma categoria).';
COMMENT ON COLUMN public.contrato_categoria_contrato.codigo_contrato			IS 'Código interno do contrato.';
COMMENT ON COLUMN public.contrato_categoria_contrato.codigo_categoria_contrato	IS 'Código interno da categoria de contrato.';

/* ---------------------------------------------------------------------- */
/* Modify table "motivo_retorno_documento_nota"                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.motivo_retorno_documento_nota
	ALTER COLUMN codigo SET DEFAULT nextval('seq_motivo_retorno_documento_nota');
