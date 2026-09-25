/* ---------------------------------------------------------------------- */
/* Nextval Pagamento                                                      */
/* ---------------------------------------------------------------------- */
ALTER TABLE pagamento ALTER COLUMN codigo SET DEFAULT nextval('seq_pagamento');

/* ---------------------------------------------------------------------- */
/* Nextval Pagamento                                                      */
/* ---------------------------------------------------------------------- */
ALTER TABLE relacao_bancaria ALTER COLUMN codigo SET DEFAULT nextval('seq_relacao_bancaria');

/* ---------------------------------------------------------------------- */
/* Nextval conta_contabil                                                 */
/* ---------------------------------------------------------------------- */
ALTER TABLE conta_contabil ALTER COLUMN codigo SET DEFAULT nextval('seq_conta_contabil');

/* ---------------------------------------------------------------------- */
/* Nextval pagamento_conta_contabil                                       */
/* ---------------------------------------------------------------------- */
ALTER TABLE pagamento_conta_contabil ALTER COLUMN codigo SET DEFAULT nextval('seq_pagamento_conta_contabil');

/* ---------------------------------------------------------------------- */
/* Nextval fundo_fixo                                                     */
/* ---------------------------------------------------------------------- */
ALTER TABLE fundo_fixo ALTER COLUMN codigo SET DEFAULT nextval('seq_fundo_fixo');

/* ---------------------------------------------------------------------- */
/* Nextval conta_corrente e dados_bancarios                               */
/* ---------------------------------------------------------------------- */
ALTER TABLE dados_bancarios ALTER COLUMN codigo SET DEFAULT nextval('seq_dados_bancarios');

/* ---------------------------------------------------------------------- */
/* Nextval lancamento                                                     */
/* ---------------------------------------------------------------------- */
ALTER TABLE lancamento ALTER COLUMN codigo SET DEFAULT nextval('seq_lancamento');

/* ---------------------------------------------------------------------- */
/* Nextval anexo                                                     */
/* ---------------------------------------------------------------------- */
ALTER TABLE anexo ALTER COLUMN codigo SET DEFAULT nextval('seq_anexo');

/* ---------------------------------------------------------------------- */
/* Nextval documentos_impressao                                                     */
/* ---------------------------------------------------------------------- */
ALTER TABLE documentos_impressao ALTER COLUMN codigo SET DEFAULT nextval('seq_doc_impressao');

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
/* Modify table "motivo_retorno_documento_nota"                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.motivo_retorno_documento_nota
	ALTER COLUMN codigo SET DEFAULT nextval('seq_motivo_retorno_documento_nota');

/* ---------------------------------------------------------------------- */
/* Modify table "motivo_retorno"                                          */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.motivo_retorno
	ALTER COLUMN codigo SET DEFAULT nextval('seq_motivo_retorno');

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

/* ---------------------------------------------------------------------- */
/* Nextval relacionamento_contato */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.relacionamento_contato ALTER COLUMN codigo SET DEFAULT nextval('seq_relacionamento_contato'::regclass);
