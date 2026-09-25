/* ---------------------------------------------------------------------- */
/* Nextval Obra                                                      */
/* ---------------------------------------------------------------------- */
ALTER TABLE obra ALTER COLUMN codigo SET DEFAULT nextval('seq_obra');

/* ---------------------------------------------------------------------- */
/* Nextval obra_cliente                                                   */
/* ---------------------------------------------------------------------- */
ALTER TABLE obra_cliente ALTER COLUMN codigo SET DEFAULT nextval('seq_obra_cliente');

/* ---------------------------------------------------------------------- */
/* Nextval Relacionamento Cliente                                         */
/* ---------------------------------------------------------------------- */
ALTER TABLE motivo_retorno_documento_cf ALTER COLUMN codigo SET DEFAULT nextval('seq_motivo_retorno_documento_cf');

/* ---------------------------------------------------------------------- */
/* Nextval forma_contratacao                                              */
/* ---------------------------------------------------------------------- */
ALTER TABLE forma_contratacao ALTER COLUMN codigo SET DEFAULT nextval('seq_forma_contratacao');

/* ---------------------------------------------------------------------- */
/* Nextval categoria_contrato                                             */
/* ---------------------------------------------------------------------- */
ALTER TABLE categoria_contrato ALTER COLUMN codigo SET DEFAULT nextval('seq_categoria_contrato');

/* ---------------------------------------------------------------------- */
/* Nextval pessoa_conta_contabil                                          */
/* ---------------------------------------------------------------------- */
ALTER TABLE pessoa_conta_contabil ALTER COLUMN codigo SET DEFAULT nextval('seq_pessoa_conta_contabil');

/* ---------------------------------------------------------------------- */
/* Nextval telefone                                                       */
/* ---------------------------------------------------------------------- */
ALTER TABLE telefone ALTER COLUMN codigo SET DEFAULT nextval('seq_telefone');

/* ---------------------------------------------------------------------- */
/* Nextval contrato_endereco                                              */
/* ---------------------------------------------------------------------- */
ALTER TABLE contrato_endereco ALTER COLUMN codigo SET DEFAULT nextval('seq_contrato_endereco');

/* ---------------------------------------------------------------------- */
/* Nextval aditivo                                                        */
/* ---------------------------------------------------------------------- */
ALTER TABLE aditivo ALTER COLUMN codigo SET DEFAULT nextval('seq_aditivo');

/* ---------------------------------------------------------------------- */
/* Nextval contrato_contato                                               */
/* ---------------------------------------------------------------------- */
ALTER TABLE contrato_contato ALTER COLUMN codigo SET DEFAULT nextval('seq_contrato_contato');

/* ---------------------------------------------------------------------- */
/* Nextval contrato_anexo                                                 */
/* ---------------------------------------------------------------------- */
ALTER TABLE contrato_anexo ALTER COLUMN codigo SET DEFAULT nextval('seq_contrato_anexo');

/* ---------------------------------------------------------------------- */
/* Nextval fecho                                                          */
/* ---------------------------------------------------------------------- */
ALTER TABLE fecho ALTER COLUMN codigo SET DEFAULT nextval('seq_fecho');

/* ---------------------------------------------------------------------- */
/* Nextval centro_financeiro_situacao                                     */
/* ---------------------------------------------------------------------- */
ALTER TABLE centro_financeiro_situacao ALTER COLUMN codigo SET DEFAULT nextval('seq_centro_financeiro_situacao');

/* ---------------------------------------------------------------------- */
/* Nextval centro_financeiro_fluxo_etapa                                  */
/* ---------------------------------------------------------------------- */
ALTER TABLE centro_financeiro_fluxo_etapa ALTER COLUMN codigo SET DEFAULT nextval('seq_centro_financeiro_fluxo_etapa');

/* ---------------------------------------------------------------------- */
/* Nextval fecho_mao_obra                                                 */
/* ---------------------------------------------------------------------- */
ALTER TABLE fecho_mao_obra ALTER COLUMN codigo SET DEFAULT nextval('seq_fecho_mao_obra');

/* ---------------------------------------------------------------------- */
/* Nextval fecho_mao_obra_adicional                                       */
/* ---------------------------------------------------------------------- */
ALTER TABLE fecho_mao_obra_adicional ALTER COLUMN codigo SET DEFAULT nextval('seq_fecho_mao_obra_adicional');

/* ---------------------------------------------------------------------- */
/* Nextval fecho_despesa_geral */
/* ---------------------------------------------------------------------- */
ALTER TABLE fecho_despesa_geral ALTER COLUMN codigo SET DEFAULT nextval('seq_fecho_despesa_geral');

/* ---------------------------------------------------------------------- */
/* Nextval categoria_mao_obra */
/* ---------------------------------------------------------------------- */
ALTER TABLE categoria_mao_obra ALTER COLUMN codigo SET DEFAULT nextval('seq_categoria_mao_obra');

/* ---------------------------------------------------------------------- */
/* Nextval categoria_despesa_geral */
/* ---------------------------------------------------------------------- */
ALTER TABLE categoria_despesa_geral ALTER COLUMN codigo SET DEFAULT nextval('seq_categoria_despesa_geral');

/* ---------------------------------------------------------------------- */
/* Nextval contrato_anexo                                                 */
/* ---------------------------------------------------------------------- */
ALTER TABLE contrato_anexo ALTER COLUMN codigo SET DEFAULT nextval('seq_contrato_anexo');

/* ---------------------------------------------------------------------- */
/* Nextval motivo_retorno_documento_contrato                              */
/* ---------------------------------------------------------------------- */
ALTER TABLE motivo_retorno_documento_contrato ALTER COLUMN codigo SET DEFAULT nextval('seq_motivo_retorno_documento_contrato');

/* ---------------------------------------------------------------------- */
/* Nextval contrato_situacao                                              */
/* ---------------------------------------------------------------------- */
ALTER TABLE contrato_situacao ALTER COLUMN codigo SET DEFAULT nextval('seq_contrato_situacao');

/* ---------------------------------------------------------------------- */
/* Nextval centro_financeiro_endereco                                     */
/* ---------------------------------------------------------------------- */
ALTER TABLE centro_financeiro_endereco ALTER COLUMN codigo SET DEFAULT nextval('seq_centro_financeiro_endereco');

/* ---------------------------------------------------------------------- */
/* Nextval centro_financeiro_aliquota                                     */
/* ---------------------------------------------------------------------- */
ALTER TABLE centro_financeiro_aliquota ALTER COLUMN codigo SET DEFAULT nextval('seq_centro_financeiro_aliquota');

/* ---------------------------------------------------------------------- */
/* Nextval contrato_contato                                               */
/* ---------------------------------------------------------------------- */
ALTER TABLE contrato_contato ALTER COLUMN codigo SET DEFAULT nextval('seq_contrato_contato');

/* ---------------------------------------------------------------------- */
/* Nextval telefone                                                       */
/* ---------------------------------------------------------------------- */
ALTER TABLE telefone ALTER COLUMN codigo SET DEFAULT nextval('seq_telefone');

/* ---------------------------------------------------------------------- */
/* Nextval contrato_endereco                                              */
/* ---------------------------------------------------------------------- */
ALTER TABLE contrato_endereco ALTER COLUMN codigo SET DEFAULT nextval('seq_contrato_endereco');

/* ---------------------------------------------------------------------- */
/* Nextval aditivo                                                        */
/* ---------------------------------------------------------------------- */
ALTER TABLE aditivo ALTER COLUMN codigo SET DEFAULT nextval('seq_aditivo');

/* ---------------------------------------------------------------------- */
/* Nextval contrato_anexo                                                 */
/* ---------------------------------------------------------------------- */
ALTER TABLE contrato_anexo ALTER COLUMN codigo SET DEFAULT nextval('seq_contrato_anexo');

/* ---------------------------------------------------------------------- */
/* Nextval aditivo                                                        */
/* ---------------------------------------------------------------------- */
ALTER TABLE aditivo ALTER COLUMN codigo SET DEFAULT nextval('seq_aditivo');

/* ---------------------------------------------------------------------- */
/* Nextval motivo_retorno_documento_cf                                    */
/* ---------------------------------------------------------------------- */
ALTER TABLE motivo_retorno_documento_cf ALTER COLUMN codigo SET DEFAULT nextval('seq_motivo_retorno_documento_cf');

/* ---------------------------------------------------------------------- */
/* Nextval item_relacao */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.item_relacao ALTER COLUMN codigo SET DEFAULT nextval('seq_item_relacao'::regclass);
COMMENT ON COLUMN public.item_relacao.codigo IS 'Código interno do item da relação bancária. Sequência: seq_item_relacao.';

/* ---------------------------------------------------------------------- */
/* Nextval item_relacao_pagamento */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.item_relacao_pagamento ALTER COLUMN codigo SET DEFAULT nextval('seq_item_relacao_pagamento'::regclass);


/* ---------------------------------------------------------------------- */
/* Nextval tipo_servico */
/* ---------------------------------------------------------------------- */
ALTER TABLE tipo_servico ALTER COLUMN codigo SET DEFAULT nextval('seq_tipo_servico');

/* ---------------------------------------------------------------------- */
/* Nextval etapa_fluxo */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.etapa_fluxo ALTER COLUMN codigo SET DEFAULT nextval('seq_etapa_fluxo'::regclass);


/* ---------------------------------------------------------------------- */
/* Nextval fluxo */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.fluxo ALTER COLUMN codigo SET DEFAULT nextval('seq_fluxo'::regclass);

/* ---------------------------------------------------------------------- */
/* Nextval centro_financeiro_endereco */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.centro_financeiro_endereco ALTER COLUMN codigo SET DEFAULT nextval('seq_centro_financeiro_endereco'::regclass);

/* ---------------------------------------------------------------------- */
/* Nextval centro_financeiro_situacao */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.centro_financeiro_situacao ALTER COLUMN codigo SET DEFAULT nextval('seq_centro_financeiro_situacao'::regclass);

/* ---------------------------------------------------------------------- */
/* Nextval obra */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.obra ALTER COLUMN codigo SET DEFAULT nextval('seq_obra'::regclass);

/* ---------------------------------------------------------------------- */
/* Nextval obra_cliente */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.obra_cliente ALTER COLUMN codigo SET DEFAULT nextval('seq_obra_cliente'::regclass);


