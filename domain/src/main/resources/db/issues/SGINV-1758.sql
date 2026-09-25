/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2018-07-10 17:50                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop views                                                             */
/* ---------------------------------------------------------------------- */
DROP MATERIALIZED VIEW IF EXISTS "vw_total_credito_por_nf";
DROP VIEW IF EXISTS "vw_inrf_notas_validas";
DROP MATERIALIZED VIEW IF EXISTS "vw_inrf_itens_nota_fiscal";
DROP VIEW IF EXISTS "vw_inrf_notas";
DROP VIEW IF EXISTS "vw_inrf_relatorio_itens";
DROP VIEW IF EXISTS "vw_inrf_relatorio_notas";
DROP VIEW IF EXISTS "vw_lista_itens_adquiridos_por_uf";
DROP VIEW IF EXISTS "vw_lista_nf_confrontamento";

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota								DROP CONSTRAINT fk_nota_cancelamento;
ALTER TABLE public.nota								DROP CONSTRAINT fk_nota_centro_financeiro;
ALTER TABLE public.nota								DROP CONSTRAINT fk_nota_conferencia;
ALTER TABLE public.nota								DROP CONSTRAINT fk_nota_contr_fiscal;
ALTER TABLE public.nota								DROP CONSTRAINT fk_nota_credor;
ALTER TABLE public.nota								DROP CONSTRAINT fk_nota_emissor;
ALTER TABLE public.nota								DROP CONSTRAINT fk_nota_gerente;
ALTER TABLE public.nota								DROP CONSTRAINT fk_nota_natureza_pagamento;
ALTER TABLE public.nota								DROP CONSTRAINT fk_nota_unidade;

ALTER TABLE public.nota_pagamento					DROP CONSTRAINT fk_nota_pagamento_nota;

ALTER TABLE public.nota_fiscal						DROP CONSTRAINT fk_nota_fiscal_nota;

ALTER TABLE public.parcela_nota						DROP CONSTRAINT fk_parcela_nota_nota;

ALTER TABLE public.nota_tributo_aliquota			DROP CONSTRAINT fk_nota_tributo_aliquota_nota;

ALTER TABLE public.nota_anexo						DROP CONSTRAINT fk_nota_anexo_nota;

ALTER TABLE public.motivo_retorno_documento_nota	DROP CONSTRAINT fk_motivo_retorno_documento_nota_nota;

ALTER TABLE public.item_demonstrativo				DROP CONSTRAINT fk_item_demonstrativo_nota;

ALTER TABLE public.nota_outro_desconto				DROP CONSTRAINT fk_nota_outro_desconto_nota;

ALTER TABLE public.lote_interface_nota				DROP CONSTRAINT fk_lote_interface_nota_nota;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "nota"                                         */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.nota DROP CONSTRAINT pk_nota;

ALTER TABLE public.nota DROP CONSTRAINT ck_nota_forma_pagamento;
ALTER TABLE public.nota DROP CONSTRAINT ck_nota_tipo;
ALTER TABLE public.nota DROP CONSTRAINT ck_nota_tipo_doc_pagamento;

/* Create temporary table */
CREATE TABLE public.nota_TMP (
    codigo							BIGINT					NOT NULL,
    codigo_centro_financeiro		BIGINT,
    codigo_empresa					BIGINT,
    codigo_credor					BIGINT					NOT NULL,
    codigo_tipo_pagamento			BIGINT,
    codigo_emissor					BIGINT,
    conferencia_codigo_colaborador	BIGINT,
    codigo_gerente					BIGINT,
    codigo_contr_fiscal				BIGINT,
    codigo_cancelamento				BIGINT,
    codigo_reversao_cancelamento	BIGINT,
    numero							CHARACTER VARYING(25)	NOT NULL,
    status							INTEGER					NOT NULL,
    tipo							INTEGER					NOT NULL,
    forma_pagamento					INTEGER					NOT NULL,
    tipo_doc_pagamento				INTEGER,
    boleto_anexo					INTEGER,
    duplicata						CHARACTER VARYING(20),
    data_emissor					TIMESTAMP,
    data_retorno_financeiro			DATE,
    conferencia_contabilidade		BOOLEAN					DEFAULT false,
    conferencia_data_atualizacao	TIMESTAMP,
    data_contr_fiscal				TIMESTAMP,
    data_cancelamento				TIMESTAMP,
    data_reversao_cancelamento		TIMESTAMP,
    criador							CHARACTER VARYING(255),
    data_criacao					TIMESTAMP,
    atualizador						CHARACTER VARYING(255),
    data_atualizacao				TIMESTAMP);

/* Copy data do temporary table */
INSERT INTO public.nota_TMP
    (codigo,codigo_centro_financeiro,codigo_empresa,codigo_credor,codigo_tipo_pagamento,codigo_emissor,conferencia_codigo_colaborador,codigo_gerente,codigo_contr_fiscal,codigo_cancelamento,numero,status,tipo,forma_pagamento,tipo_doc_pagamento,boleto_anexo,duplicata,data_emissor,data_retorno_financeiro,conferencia_contabilidade,conferencia_data_atualizacao,data_contr_fiscal,data_cancelamento,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_centro_financeiro,codigo_empresa,codigo_credor,codigo_tipo_pagamento,codigo_emissor,conferencia_codigo_colaborador,codigo_gerente,codigo_contr_fiscal,codigo_cancelamento,numero,status,tipo,forma_pagamento,tipo_doc_pagamento,boleto_anexo,duplicata,data_emissor,data_retorno_financeiro,conferencia_contabilidade,conferencia_data_atualizacao,data_contr_fiscal,data_cancelamento,criador,data_criacao,atualizador,data_atualizacao
FROM public.nota;

/* Drop indexes */
DROP INDEX public.ix_nota_cancelamento;
DROP INDEX public.ix_nota_codigo_centro_financeiro;
DROP INDEX public.ix_nota_conferencia;
DROP INDEX public.ix_nota_contr_fiscal;
DROP INDEX public.ix_nota_credor;
DROP INDEX public.ix_nota_emissor;
DROP INDEX public.ix_nota_empresa;
DROP INDEX public.ix_nota_gerente;
DROP INDEX public.ix_nota_numero;
DROP INDEX public.ix_nota_status;
DROP INDEX public.ix_nota_tipo;
DROP INDEX public.ix_nota_tipo_pagamento;

/* Drop old table */
DROP TABLE public.nota;

/* Rename temporary table to the table */
ALTER TABLE public.nota_TMP
	RENAME TO nota;

/* Add primary key */
ALTER TABLE public.nota
	ADD CONSTRAINT pk_nota 
    PRIMARY KEY (codigo);

/* Add constraints */
ALTER TABLE public.nota
	ADD CONSTRAINT ck_nota_forma_pagamento 
    CHECK (forma_pagamento = ANY (ARRAY[0, 1]));

ALTER TABLE public.nota
	ADD CONSTRAINT ck_nota_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1]));

ALTER TABLE public.nota
	ADD CONSTRAINT ck_nota_tipo_doc_pagamento 
    CHECK (tipo_doc_pagamento = ANY (ARRAY[0, 1, 2, 3]));

/* Create indexes */
CREATE INDEX ix_nota_codigo_centro_financeiro	ON public.nota (codigo_centro_financeiro);
CREATE INDEX ix_nota_empresa					ON public.nota (codigo_empresa);
CREATE INDEX ix_nota_credor						ON public.nota (codigo_credor);
CREATE INDEX ix_nota_tipo_pagamento				ON public.nota (codigo_tipo_pagamento);
CREATE INDEX ix_nota_emissor					ON public.nota (codigo_emissor);
CREATE INDEX ix_nota_conferencia				ON public.nota (conferencia_codigo_colaborador);
CREATE INDEX ix_nota_gerente					ON public.nota (codigo_gerente);
CREATE INDEX ix_nota_contr_fiscal				ON public.nota (codigo_contr_fiscal);
CREATE INDEX ix_nota_cancelamento				ON public.nota (codigo_cancelamento);
CREATE INDEX ix_nota_reversao_cancelamento		ON public.nota (codigo_reversao_cancelamento);
CREATE INDEX ix_nota_numero						ON public.nota (numero);
CREATE INDEX ix_nota_status						ON public.nota (status);
CREATE INDEX ix_nota_tipo						ON public.nota (tipo);

/* Comments */
COMMENT ON TABLE public.nota									IS 'Notas fiscais (NF) ou notas de pagamento (NP, pagamentos sem documento fiscal).';
COMMENT ON COLUMN public.nota.codigo							IS 'Código interno da nota. Sequência: seq_nota.';
COMMENT ON COLUMN public.nota.codigo_centro_financeiro			IS 'Código interno da gerência (código interno do centro financeiro, associado à uma gerência) responsável pelo acesso à nota .';
COMMENT ON COLUMN public.nota.codigo_empresa					IS 'Código interno da empre (código interno da unidade, código interno da pessoa) resposável pela nota.';
COMMENT ON COLUMN public.nota.codigo_credor						IS 'Código interno do credor (código interno da pessoa), a quem será pago a nota.';
COMMENT ON COLUMN public.nota.codigo_tipo_pagamento				IS 'Código interno do tipo de pagamento.';
COMMENT ON COLUMN public.nota.codigo_emissor					IS 'Código interno do emissor (código interno do colaborador, da pessoa) da nota.';
COMMENT ON COLUMN public.nota.conferencia_codigo_colaborador	IS 'Código interno do responsável pela conferência (código interno do colaborador, da pessoa) da nota.';
COMMENT ON COLUMN public.nota.codigo_gerente					IS 'Código interno do gerente (código interno da pessoa) a qual nota está associado.';
COMMENT ON COLUMN public.nota.codigo_contr_fiscal				IS 'Código interno do responsável pela avaliação no Controle Fiscal (código interno do colaborador, da pessoa) da nota.';
COMMENT ON COLUMN public.nota.codigo_cancelamento				IS 'Código interno (código interno da pessoa) do responsável pelo último cancelamento.';
COMMENT ON COLUMN public.nota.codigo_reversao_cancelamento		IS 'Código interno (código interno da pessoa) do responsável pela última reversão do cancelamento.';
COMMENT ON COLUMN public.nota.numero							IS 'Número da nota.';
COMMENT ON COLUMN public.nota.status							IS 'Situação da nota: 0 = Aberta / 1 = Reaberta / 2 = Em Aprovação (nova nota) / 3 = Em Aprovação (retorno do financeiro) / 4 = Em Aprovação (após ajustes) / 5 = Em Ajuste Fiscal / 6 = Aprovada / 7 = Cancelada / 8 = Em Aprovação (Prestação de contas) / 9 = Em Relação de Notas / 10 = Recebida pelo Financeiro / 11 = Finalizado / 12 = NF/Faturamento - Aberta / 13 = NF/Faturamento - A processar / 14 = NF/Faturamento - Em processamento / 15 = NF/Faturamento - Em recebimento / 16 = NF/Faturamento - Recebida / 17 = NF/Faturamento - Cancelada / 18 = Devolução total (cancelada).';
COMMENT ON COLUMN public.nota.tipo								IS 'Tipo da nota: 0 = NF / 1 = NP.';
COMMENT ON COLUMN public.nota.forma_pagamento					IS 'Forma de pagamento da nota: 0 = Parcela única / 1 = Parcelada.';
COMMENT ON COLUMN public.nota.tipo_doc_pagamento				IS 'Tipo de documento de pagamento. NF: 0 = Boleto anexo / 1 = Boleto correio / 2 = Depósito em conta / 3 = Duplicata. NP: 0 = Boleto / 1 = Depósito/Transf. (não usado) / 2 = Depósito, DDA, DOC ou  TED / 3 = Guia, Concessionária ou Outros.';
COMMENT ON COLUMN public.nota.boleto_anexo						IS 'Número de boletos anexos à nota.';
COMMENT ON COLUMN public.nota.duplicata							IS 'Número da duplkicata associada à nota (não usado).';
COMMENT ON COLUMN public.nota.data_emissor						IS 'Data/hora do último envio da nota para aprovação.';
COMMENT ON COLUMN public.nota.data_retorno_financeiro			IS 'Data de retorno da nota do financeiro para a gerência.';
COMMENT ON COLUMN public.nota.conferencia_contabilidade			IS 'Indica se a nota foi avaliada pela Contabilidade (true) ou não (false).';
COMMENT ON COLUMN public.nota.conferencia_data_atualizacao		IS 'Data/hora de conferência da nota pela Contabilidade.';
COMMENT ON COLUMN public.nota.data_contr_fiscal					IS 'Data/hora da última avaliação da nota pelo Controle Fiscal.';
COMMENT ON COLUMN public.nota.data_cancelamento					IS 'Data/hora do último cancelamento.';
COMMENT ON COLUMN public.nota.data_reversao_cancelamento		IS 'Data/hora da última reversão do cancelamento.';
COMMENT ON COLUMN public.nota.criador							IS 'Responsável (login) pela criação da nota.';
COMMENT ON COLUMN public.nota.data_criacao						IS 'Data/hora de criação da nota.';
COMMENT ON COLUMN public.nota.atualizador						IS 'Responsável (login) pela última atualização da nota.';
COMMENT ON COLUMN public.nota.data_atualizacao					IS 'Data/hora da última atualização da nota.';

/* Set owner */
ALTER TABLE public.nota
	OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Modify table "nota_fiscal"                                             */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_fiscal
	ADD CONSTRAINT ck_nota_fiscal_natureza 
    CHECK (natureza = ANY (ARRAY[0, 1]));

ALTER TABLE public.nota_fiscal
	ADD CONSTRAINT ck_nota_fiscal_tipo_fornecimento 
    CHECK (tipo_fornecimento = ANY (ARRAY[0, 1, 2]));

ALTER TABLE public.nota_fiscal
	ADD CONSTRAINT ck_nota_fiscal_tipo_frete 
    CHECK (tipo_frete = ANY (ARRAY[0, 1, 2, 3]));

ALTER TABLE public.nota_fiscal
	ADD CONSTRAINT ck_nota_fiscal_tipo_nota_fiscal 
    CHECK (tipo_nota_fiscal <= 11);

ALTER TABLE public.nota_fiscal
	ADD CONSTRAINT ck_nota_fiscal_tipo_seguro 
    CHECK (tipo_seguro = ANY (ARRAY[0, 1]));

COMMENT ON TABLE public.nota_fiscal									IS 'Notas fiscais (NF) para pagamento (relacionadas aos pagamentos), devolução (relacionadas às NF/Saída) ou faturamento/recebimento (também relacionadas às NF/Saída).';
COMMENT ON COLUMN public.nota_fiscal.codigo							IS 'Código interno da nota fiscal (código interno da nota).';
COMMENT ON COLUMN public.nota_fiscal.codigo_documento_fiscal		IS 'Código interno do documento fiscal.';
COMMENT ON COLUMN public.nota_fiscal.codigo_natureza_aquisicao		IS 'Código interno da natureza de aquisição.';
COMMENT ON COLUMN public.nota_fiscal.codigo_tipo_cobranca			IS 'Código interno do tipo de cobrança.';
COMMENT ON COLUMN public.nota_fiscal.codigo_nota_pagamento			IS 'Código interno da nota de pagamento (usada anteriormente para Fundo Fixo - DEPRECIADO).';
COMMENT ON COLUMN public.nota_fiscal.codigo_nota_fiscal_transporte	IS 'Código interno da nota fiscal de transporte a qual esta NF está associada.';
COMMENT ON COLUMN public.nota_fiscal.codigo_cfop					IS 'Código interno do CFOP.';
COMMENT ON COLUMN public.nota_fiscal.codigo_nota_fiscal_origem		IS 'Código interno da nota fiscal de origem (se for NF/devolução).';
COMMENT ON COLUMN public.nota_fiscal.tipo_nota_fiscal				IS 'Tipo da nota fiscal: 0 = Fornecimento / 1 = Ordem de Compra / 2 = NF/PC/Fundo Fixo / 3 = NF/PC/Reembolso / 4 = Transporte / 5 = NF/PC/Adiantamento / 6 = Devolução parcial / 7 = Devolução total / 8 = OC/PC/Fundo Fixo / 9 = OC/PC/Reembolso / 10 = OC/PC/Adiantamento / 11 = NF/Faturamento.';
COMMENT ON COLUMN public.nota_fiscal.natureza						IS 'Natureza da NF: 0 = Venda à vista / 1 = Venda a prazo.';
COMMENT ON COLUMN public.nota_fiscal.tipo_fornecimento				IS 'Tipo de fornecimento: 0 = Materiais / 1 = Serviços / 2 = Conjugada.';
COMMENT ON COLUMN public.nota_fiscal.chave_eletronica				IS 'Chave eletrônica. NF-e: exatamente 44 posições ou NFS-e: até 44 posições (dependendo de cada Prefeitura).';
COMMENT ON COLUMN public.nota_fiscal.data_emissao					IS 'Data de emissão.';
COMMENT ON COLUMN public.nota_fiscal.limite_emissao					IS 'Data limite para emissão de NF (quando não for NF-e/NFS-e).';
COMMENT ON COLUMN public.nota_fiscal.saida							IS 'Data de saída (apenas para NF/Materiais).';
COMMENT ON COLUMN public.nota_fiscal.conclusao						IS 'Data de conclusão dos serviços (apenas para NF/Serviços ou NF/Conjugada).';
COMMENT ON COLUMN public.nota_fiscal.serie							IS 'Série.';
COMMENT ON COLUMN public.nota_fiscal.subserie						IS 'Subsérie.';
COMMENT ON COLUMN public.nota_fiscal.valor_frete					IS 'Valor do frete (apenas para NF/Materiais).';
COMMENT ON COLUMN public.nota_fiscal.valor_seguro					IS 'Valor do seguro (apenas para NF/Materiais).';
COMMENT ON COLUMN public.nota_fiscal.desconto 						IS 'Valor do desconto (apenas para NF/Materiais).';
COMMENT ON COLUMN public.nota_fiscal.valor_outras_despesas			IS 'Valor de outras despesas (apenas para NF/Materiais).';
COMMENT ON COLUMN public.nota_fiscal.valor_ipi						IS 'Valor do IPI (apenas para NF/Materiais).';
COMMENT ON COLUMN public.nota_fiscal.tipo_frete						IS 'Tipo do frete (apenas para NF/Materiais ou NF/Devolução): 0 = Por conta do emitente / 1 = Por conta do destinatário/remetente / 2 = Por conta de terceiros / 3 = Sem frete.';
COMMENT ON COLUMN public.nota_fiscal.tipo_seguro					IS 'Tipo do seguro (apenas para NF/Materiais): 0 = Por conta do emitente / 1 = Por conta do destinatário/remetente.';
COMMENT ON COLUMN public.nota_fiscal.hora_saida						IS 'Horário da saída (apenas para NF/Devolução).';
COMMENT ON COLUMN public.nota_fiscal.codigo_processo				IS 'Código interno do processo EFD/Contribuições.';
COMMENT ON COLUMN public.nota_fiscal.codigo_cliente					IS 'Código interno do cliente (código interno da pessoa) quando cobrança da NF for "Cliente - Reembolso".';
COMMENT ON COLUMN public.nota_fiscal.codigo_erro_validacao_nf		IS 'Código de erro da validação da NF-e (NF/Materiais). Se nulo, NF não validada ou não é NF-e.';
COMMENT ON COLUMN public.nota_fiscal.data_movimento_anterior		IS 'Data de movimento salva após o retorno do CAP (e depois de ter sido aprovada pelo CAP).';
COMMENT ON COLUMN public.nota_fiscal.data_estorno_movimento			IS 'Data de estorno pelo CAP da data de movimento salva após o retorno (e depois de ter sido aprovada pelo CAP, ou seja, retornou depois de ser aprovada pelo CAP).';

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota								ADD CONSTRAINT fk_nota_natureza_pagamento 
    FOREIGN KEY (codigo_tipo_pagamento)				REFERENCES public.natureza_pagamento (codigo);
ALTER TABLE public.nota								ADD CONSTRAINT fk_nota_emissor 
    FOREIGN KEY (codigo_emissor)					REFERENCES public.colaborador (codigo);
ALTER TABLE public.nota								ADD CONSTRAINT fk_nota_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);
ALTER TABLE public.nota								ADD CONSTRAINT fk_nota_credor 
    FOREIGN KEY (codigo_credor)						REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota								ADD CONSTRAINT fk_nota_unidade 
    FOREIGN KEY (codigo_empresa)					REFERENCES public.unidade (codigo);
ALTER TABLE public.nota								ADD CONSTRAINT fk_nota_conferencia 
    FOREIGN KEY (conferencia_codigo_colaborador)	REFERENCES public.colaborador (codigo);
ALTER TABLE public.nota								ADD CONSTRAINT fk_nota_gerente 
    FOREIGN KEY (codigo_gerente)					REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota								ADD CONSTRAINT fk_nota_contr_fiscal 
    FOREIGN KEY (codigo_contr_fiscal)				REFERENCES public.colaborador (codigo);
ALTER TABLE public.nota								ADD CONSTRAINT fk_nota_cancelamento 
    FOREIGN KEY (codigo_cancelamento)				REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota								ADD CONSTRAINT fk_nota_reversao_canc 
    FOREIGN KEY (codigo_reversao_cancelamento)		REFERENCES public.pessoa (codigo);

ALTER TABLE public.nota_pagamento					ADD CONSTRAINT fk_nota_pagamento_nota 
    FOREIGN KEY (codigo)							REFERENCES public.nota (codigo);

ALTER TABLE public.nota_fiscal						ADD CONSTRAINT fk_nota_fiscal_nota 
    FOREIGN KEY (codigo)							REFERENCES public.nota (codigo);

ALTER TABLE public.parcela_nota						ADD CONSTRAINT fk_parcela_nota_nota 
    FOREIGN KEY (codigo_nota)						REFERENCES public.nota (codigo);

ALTER TABLE public.nota_tributo_aliquota			ADD CONSTRAINT fk_nota_tributo_aliquota_nota 
    FOREIGN KEY (codigo_nota)						REFERENCES public.nota (codigo);

ALTER TABLE public.nota_anexo						ADD CONSTRAINT fk_nota_anexo_nota 
    FOREIGN KEY (codigo_nota)						REFERENCES public.nota (codigo);

ALTER TABLE public.motivo_retorno_documento_nota	ADD CONSTRAINT fk_motivo_retorno_documento_nota_nota 
    FOREIGN KEY (codigo_nota)						REFERENCES public.nota (codigo);

ALTER TABLE public.item_demonstrativo				ADD CONSTRAINT fk_item_demonstrativo_nota 
    FOREIGN KEY (codigo_nota)						REFERENCES public.nota (codigo);

ALTER TABLE public.nota_outro_desconto				ADD CONSTRAINT fk_nota_outro_desconto_nota 
    FOREIGN KEY (codigo_nota)						REFERENCES public.nota (codigo);

ALTER TABLE public.lote_interface_nota				ADD CONSTRAINT fk_lote_interface_nota_nota 
    FOREIGN KEY (codigo_nota)						REFERENCES public.nota (codigo);

/* ---------------------------------------------------------------------- */
/* Recreate views                                                         */
/* ---------------------------------------------------------------------- */
/* vw_lista_nf_confrontamento */
CREATE OR REPLACE VIEW public.vw_lista_nf_confrontamento AS 
 SELECT
        CASE
            WHEN nota_fiscal.tipo_nota_fiscal <> 1 THEN ( SELECT rateio.codigo_centro_financeiro
               FROM rateio_nota_fiscal rateio
              WHERE rateio.codigo_item_nota_fiscal = (( SELECT max(item_nf.codigo) AS max
                       FROM item_nota_fiscal item_nf
                      WHERE item_nf.codigo_pagamento = nota_fiscal.codigo))
             LIMIT 1)
            ELSE ( SELECT rc.codigo_centro_financeiro
               FROM requisicao_compra rc,
                requisicao_compra_refinada rcr,
                ordem_compra oc,
                pedido,
                recebimento
              WHERE rc.codigo = rcr.codigo_requisicao_compra AND rcr.codigo_ordem_compra = oc.codigo AND oc.codigo = pedido.codigo_ordem_compra AND pedido.codigo = recebimento.codigo_pedido AND recebimento.codigo_nota_fiscal = nota_fiscal.codigo
             LIMIT 1)
        END AS codigo_cf_item,
        CASE
            WHEN nota_fiscal.tipo_nota_fiscal <> 1 THEN ( SELECT cf.identificador
               FROM centro_financeiro cf,
                rateio_nota_fiscal rateio
              WHERE cf.codigo = rateio.codigo_centro_financeiro AND rateio.codigo_item_nota_fiscal = (( SELECT max(item_nf.codigo) AS max
                       FROM item_nota_fiscal item_nf
                      WHERE item_nf.codigo_pagamento = nota_fiscal.codigo))
             LIMIT 1)
            ELSE ( SELECT cf.identificador
               FROM requisicao_compra rc,
                requisicao_compra_refinada rcr,
                ordem_compra oc,
                pedido,
                recebimento,
                centro_financeiro cf
              WHERE rc.codigo = rcr.codigo_requisicao_compra AND rcr.codigo_ordem_compra = oc.codigo AND oc.codigo = pedido.codigo_ordem_compra AND pedido.codigo = recebimento.codigo_pedido AND recebimento.codigo_nota_fiscal = nota_fiscal.codigo AND rc.codigo_centro_financeiro = cf.codigo
             LIMIT 1)
        END AS identificador_cf_item,
        CASE
            WHEN (nota.codigo IN ( SELECT item_nota_fiscal.codigo_pagamento
               FROM item_nota_fiscal,
                rateio_nota_fiscal
              WHERE item_nota_fiscal.codigo = rateio_nota_fiscal.codigo_item_nota_fiscal
              GROUP BY item_nota_fiscal.codigo_pagamento
             HAVING count(DISTINCT rateio_nota_fiscal.codigo_centro_financeiro) > 1)) THEN 1
            ELSE 0
        END AS ind_possui_rateio,
        CASE
            WHEN centro_financeiro.codigo_pai IS NULL THEN centro_financeiro.identificador
            ELSE centro_financeiro_pai.identificador
        END AS identificador,
        CASE
            WHEN centro_financeiro.codigo_pai IS NULL THEN centro_financeiro.nome
            ELSE centro_financeiro_pai.nome
        END AS nome_cc_cl,
    nota_fiscal.codigo,
    pagamento.data_movimento,
    pagamento.data_pagamento,
    pagamento.data_vencimento,
    pagamento.data_aprovador,
    nota_fiscal.data_emissao,
    nota.numero,
    despesa.valor,
    parcela_nota.ordem,
    pessoa.nome,
    pessoa.documento,
        CASE
            WHEN nota_fiscal.tipo_nota_fiscal = 1 THEN ( SELECT rc.numero
               FROM requisicao_compra rc,
                requisicao_compra_refinada rcr,
                ordem_compra oc,
                recebimento,
                pedido
              WHERE rc.codigo = rcr.codigo_requisicao_compra AND rcr.codigo_ordem_compra = oc.codigo AND oc.codigo = pedido.codigo_ordem_compra AND pedido.codigo = recebimento.codigo_pedido AND recebimento.codigo_nota_fiscal = nota_fiscal.codigo
             LIMIT 1)
            ELSE NULL::character varying
        END AS numero_rc,
        CASE
            WHEN nota_fiscal.tipo_nota_fiscal = 1 THEN ( SELECT pessoa_1.nome
               FROM requisicao_compra rc,
                requisicao_compra_refinada rcr,
                ordem_compra oc,
                recebimento,
                pessoa pessoa_1,
                pessoa_fisica,
                usuario,
                pedido
              WHERE rc.codigo = rcr.codigo_requisicao_compra AND rcr.codigo_ordem_compra = oc.codigo AND oc.codigo = pedido.codigo_ordem_compra AND pedido.codigo = recebimento.codigo_pedido AND recebimento.codigo_nota_fiscal = nota_fiscal.codigo AND pessoa_1.codigo = pessoa_fisica.codigo AND pessoa_fisica.codigo_usuario = usuario.codigo AND usuario.login::text = rc.criador::text
             LIMIT 1)
            ELSE NULL::character varying
        END AS criador_rc,
        CASE
            WHEN nota_fiscal.tipo_nota_fiscal = 1 THEN ( SELECT rc.data_criacao
               FROM requisicao_compra rc,
                requisicao_compra_refinada rcr,
                ordem_compra oc,
                recebimento,
                pedido
              WHERE rc.codigo = rcr.codigo_requisicao_compra AND rcr.codigo_ordem_compra = oc.codigo AND oc.codigo = pedido.codigo_ordem_compra AND pedido.codigo = recebimento.codigo_pedido AND recebimento.codigo_nota_fiscal = nota_fiscal.codigo
             LIMIT 1)
            ELSE NULL::timestamp without time zone
        END AS data_criacao_rc,
        CASE
            WHEN nota_fiscal.tipo_fornecimento = 0 THEN 'MATERIAL'::text
            WHEN nota_fiscal.tipo_fornecimento = 1 THEN 'SERVIÇO'::text
            WHEN nota_fiscal.tipo_fornecimento = 2 THEN 'CONJUGADA'::text
            ELSE NULL::text
        END AS tipo_fornecimento,
        CASE
            WHEN nota.status = 0 THEN 'ABERTA'::text
            WHEN nota.status = 1 THEN 'REABERTA'::text
            WHEN nota.status = 2 THEN 'EM APROVACAO (NOVA NF)'::text
            WHEN nota.status = 3 THEN 'EM APROVACAO (RETORNO FINANCEIRO)'::text
            WHEN nota.status = 4 THEN 'EM APROVACAO (RETORNO AJUSTES)'::text
            WHEN nota.status = 5 THEN 'EM AJUSTE FISCAL'::text
            WHEN nota.status = 6 THEN 'APROVADA'::text
            WHEN nota.status = 7 THEN 'CANCELADA'::text
            ELSE NULL::text
        END AS nota_status,
    nota.status AS codigo_status_nota,
        CASE
            WHEN pagamento.status = 0 THEN 'ABERTO'::text
            WHEN pagamento.status = 1 THEN 'AJUSTE_FISCAL'::text
            WHEN pagamento.status = 2 THEN 'ANALISE'::text
            WHEN pagamento.status = 3 THEN 'REABERTO'::text
            WHEN pagamento.status = 4 THEN 'AUTORIZACAO'::text
            WHEN pagamento.status = 5 THEN 'CANCELAMENTO'::text
            WHEN pagamento.status = 6 THEN 'CANCELADO'::text
            WHEN pagamento.status = 7 THEN 'AUTORIZADO'::text
            WHEN pagamento.status = 8 THEN 'PAGAMENTO'::text
            WHEN pagamento.status = 9 THEN 'RELACIONADO'::text
            WHEN pagamento.status = 10 THEN 'FINALIZADO'::text
            WHEN pagamento.status = 11 THEN 'APROVACAO'::text
            ELSE NULL::text
        END AS pagamento_status,
    pagamento.status AS codigo_status_pagamento,
        CASE
            WHEN nota_fiscal.tipo_nota_fiscal = 0 THEN 'AVULSA'::text
            WHEN nota_fiscal.tipo_nota_fiscal = 1 THEN 'OC'::text
            WHEN nota_fiscal.tipo_nota_fiscal = 2 THEN 'FUNDO FIXO'::text
            WHEN nota_fiscal.tipo_nota_fiscal = 3 THEN 'REEMBOLSO'::text
            WHEN nota_fiscal.tipo_nota_fiscal = 4 THEN 'TRANSPORTE'::text
            ELSE NULL::text
        END AS tipo_nota_fiscal,
    tipo_cobranca.nome AS cobranca,
    ( SELECT pessoa_1.nome
           FROM pessoa pessoa_1,
            pessoa_fisica,
            usuario
          WHERE pessoa_1.codigo = pessoa_fisica.codigo AND pessoa_fisica.codigo_usuario = usuario.codigo AND usuario.login::text = despesa.criador::text) AS criador_nota,
    despesa.data_criacao,
    ( SELECT pessoa_1.nome
           FROM pessoa pessoa_1,
            pessoa_fisica,
            usuario
          WHERE pessoa_1.codigo = pessoa_fisica.codigo AND pessoa_fisica.codigo_usuario = usuario.codigo AND usuario.login::text = despesa.atualizador::text) AS atualizador_nota,
    despesa.data_atualizacao,
    ( SELECT pessoa_1.nome
           FROM pessoa pessoa_1,
            pessoa_fisica,
            usuario
          WHERE pessoa_1.codigo = pessoa_fisica.codigo AND pessoa_fisica.codigo_usuario = usuario.codigo AND pessoa_fisica.codigo = pagamento.codigo_aprovador) AS aprovador_nf,
    pagamento.data_aprovador AS data_aprovador_nf
   FROM centro_financeiro centro_financeiro
     JOIN nota nota ON centro_financeiro.codigo = nota.codigo_centro_financeiro
     JOIN parcela_nota parcela_nota ON nota.codigo = parcela_nota.codigo_nota
     JOIN nota_fiscal nota_fiscal ON nota.codigo = nota_fiscal.codigo
     JOIN despesa ON nota.codigo = despesa.codigo
     JOIN tipo_cobranca ON nota_fiscal.codigo_tipo_cobranca = tipo_cobranca.codigo
     JOIN pagamento pagamento ON parcela_nota.codigo = pagamento.codigo
     JOIN pessoa ON pessoa.codigo = nota.codigo_credor
     JOIN pessoa_juridica ON pessoa_juridica.codigo = pessoa.codigo
     LEFT JOIN centro_financeiro centro_financeiro_pai ON centro_financeiro.codigo_pai = centro_financeiro_pai.codigo
  WHERE nota.codigo = nota_fiscal.codigo AND date(despesa.data_criacao) >= '2013-06-01'::date
  ORDER BY nota_fiscal.data_emissao, (
        CASE
            WHEN centro_financeiro.codigo_pai IS NULL THEN centro_financeiro.identificador
            ELSE centro_financeiro_pai.identificador
        END), nota.numero, parcela_nota.ordem;

ALTER TABLE public.vw_lista_nf_confrontamento
  OWNER TO sgi;

COMMENT ON VIEW public.vw_lista_nf_confrontamento
  IS 'Atualizada SGINV-1758 / versão 5.11.8';

/* vw_lista_itens_adquiridos_por_uf */
CREATE OR REPLACE VIEW public.vw_lista_itens_adquiridos_por_uf AS 
 SELECT ic.codigo,
    ic.nome,
    inf.descricao AS descricao_item_compra,
    inf.preco,
    inf.quantidade,
    u.nome AS unidade_medida,
    nf.data_emissao,
    e.uf,
    inf.preco * inf.quantidade AS total
   FROM item_compra ic,
    item_nota_fiscal inf,
    unidade_medida u,
    nota_fiscal nf,
    nota n,
    pessoa_juridica pj,
    pessoa p,
    endereco e
  WHERE u.codigo = ic.codigo_unidade_medida AND inf.codigo_item_compra = ic.codigo AND inf.codigo_pagamento = nf.codigo AND nf.codigo = n.codigo AND nf.data_emissao >= '2013-01-01'::date AND nf.data_emissao <= '2013-12-01'::date AND n.codigo_credor = pj.codigo AND pj.codigo = p.codigo AND p.codigo_endereco = e.codigo
UNION
 SELECT ic.codigo,
    ic.nome,
    ipr.descricao AS descricao_item_compra,
    ir.preco,
    ir.quantidade,
    u.nome AS unidade_medida,
    nf.data_emissao,
    e.uf,
    ir.preco * ir.quantidade AS total
   FROM item_compra ic,
    unidade_medida u,
    nota_fiscal nf,
    nota n,
    pessoa_juridica pj,
    pessoa p,
    endereco e,
    recebimento r,
    item_recebimento ir,
    item_pedido ip,
    item_proposta ipr
  WHERE u.codigo = ic.codigo_unidade_medida AND nf.codigo = n.codigo AND n.codigo = r.codigo_nota_fiscal AND nf.data_emissao >= '2013-01-01'::date AND nf.data_emissao <= '2013-12-01'::date AND n.codigo_credor = pj.codigo AND r.codigo = ir.codigo_recebimento AND ir.codigo_item_pedido = ip.codigo AND ip.codigo_item_proposta = ipr.codigo AND ipr.codigo_item_compra = ic.codigo AND pj.codigo = p.codigo AND p.codigo_endereco = e.codigo
  ORDER BY 3, 1, 8;

ALTER TABLE public.vw_lista_itens_adquiridos_por_uf
  OWNER TO sgi;
  
COMMENT ON VIEW public.vw_lista_itens_adquiridos_por_uf
  IS 'Atualizada SGINV-1758 / versão 5.11.8';

/* vw_inrf_relatorio_notas */
CREATE OR REPLACE VIEW public.vw_inrf_relatorio_notas AS 
 SELECT n.codigo,
    n.numero AS nf,
    p.nome AS fornecedor,
    pg.data_movimento,
    d.valor AS valor_nf,
    nf.desconto,
    nf.valor_ipi AS ipi,
    nf.valor_frete AS frete,
    nf.valor_seguro AS seguro,
    nf.valor_outras_despesas AS outras,
        CASE
            WHEN nf.tipo_fornecimento = 0 THEN 'MAT'::text
            WHEN nf.tipo_fornecimento = 1 THEN 'SERV'::text
            ELSE 'ERRO'::text
        END AS tipo_nota,
    cf.codigo AS codigo_gerencia,
    cf.nome AS gerencia,
    cf.codigo_pai AS gerencia_pai
   FROM nota n,
    nota_fiscal nf,
    pessoa p,
    despesa d,
    centro_financeiro cf,
    parcela_nota pn,
    pagamento pg
  WHERE n.codigo = pn.codigo_nota AND pn.codigo = pg.codigo AND n.codigo_credor = p.codigo AND n.codigo = d.codigo AND n.codigo = nf.codigo AND n.codigo_centro_financeiro = cf.codigo
  ORDER BY pg.data_pagamento, p.nome;

ALTER TABLE public.vw_inrf_relatorio_notas
  OWNER TO sgi;

COMMENT ON VIEW public.vw_inrf_relatorio_notas
  IS 'Atualizada SGINV-1758 / versão 5.11.8';

/* vw_inrf_relatorio_itens */
CREATE OR REPLACE VIEW public.vw_inrf_relatorio_itens AS 
 SELECT nf.codigo AS codigo_nota,
    'AV'::text AS formato_nota,
        CASE
            WHEN nf.tipo_fornecimento = 0 THEN 'MAT'::text
            WHEN nf.tipo_fornecimento = 1 THEN 'SERV'::text
            ELSE 'ERRO'::text
        END AS tipo_nota,
        CASE
            WHEN ic.codigo_tipo_item_compra = 10 THEN 'MAT'::text
            WHEN ic.codigo_tipo_item_compra = 9 THEN 'SERV'::text
            WHEN ic.codigo_tipo_item_compra = 8 THEN 'ATIVO'::text
            WHEN ic.codigo_tipo_item_compra = 7 THEN 'CONS'::text
            ELSE 'NULO'::text
        END AS tipo_item,
    pg.data_movimento AS dt_mov,
    n.numero,
    p.nome AS fornecedor,
    d.valor AS valor_nota,
    ic.codigo AS id_item,
    ic.nome AS item,
    inf.preco,
    inf.quantidade AS qtd
   FROM nota_fiscal nf,
    nota n,
    pessoa p,
    despesa d,
    item_nota_fiscal inf,
    item_compra ic,
    parcela_nota pn,
    pagamento pg
  WHERE nf.tipo_nota_fiscal = 0 AND n.codigo = nf.codigo AND n.codigo_credor = p.codigo AND n.codigo = d.codigo AND nf.codigo = inf.codigo_pagamento AND inf.codigo_item_compra = ic.codigo AND n.codigo = pn.codigo_nota AND pn.codigo = pg.codigo
UNION
 SELECT nf.codigo AS codigo_nota,
    'OC'::text AS formato_nota,
        CASE
            WHEN rc.codigo_natureza_aquisicao = 1 THEN 'MAT'::text
            WHEN rc.codigo_natureza_aquisicao = ANY (ARRAY[3::bigint, 200::bigint]) THEN 'SERV'::text
            WHEN rc.codigo_natureza_aquisicao = ANY (ARRAY[2::bigint, 201::bigint]) THEN 'CONS'::text
            ELSE 'ERRO'::text
        END AS tipo_nota,
        CASE
            WHEN rc.codigo_natureza_aquisicao = 1 THEN 'MAT'::text
            WHEN rc.codigo_natureza_aquisicao = ANY (ARRAY[3::bigint, 200::bigint]) THEN 'SERV'::text
            WHEN rc.codigo_natureza_aquisicao = ANY (ARRAY[2::bigint, 201::bigint]) THEN 'CONS'::text
            ELSE 'ERRO'::text
        END AS tipo_item,
    pg.data_movimento AS dt_mov,
    n.numero,
    p.nome AS fornecedor,
    d.valor AS valor_nota,
    ic.codigo AS id_item,
    ic.nome AS item,
    irec.preco,
    irec.quantidade AS qtd
   FROM nota_fiscal nf,
    recebimento rec,
    pedido ped,
    ordem_compra oc,
    requisicao_compra_refinada rcr,
    requisicao_compra rc,
    nota n,
    pessoa p,
    despesa d,
    item_recebimento irec,
    item_pedido iped,
    item_proposta iprop,
    item_compra ic,
    parcela_nota pn,
    pagamento pg
  WHERE nf.tipo_nota_fiscal = 1 AND nf.codigo = rec.codigo_nota_fiscal AND rec.codigo_pedido = ped.codigo AND ped.codigo_ordem_compra = oc.codigo AND oc.codigo = rcr.codigo_ordem_compra AND rcr.codigo_requisicao_compra = rc.codigo AND n.codigo = nf.codigo AND n.codigo_credor = p.codigo AND d.codigo = n.codigo AND rec.codigo = irec.codigo_recebimento AND irec.codigo_item_pedido = iped.codigo AND iped.codigo_pedido = ped.codigo AND iped.codigo_item_proposta = iprop.codigo AND iprop.codigo_item_compra = ic.codigo AND n.codigo = pn.codigo_nota AND pn.codigo = pg.codigo;

ALTER TABLE public.vw_inrf_relatorio_itens
  OWNER TO sgi;

COMMENT ON VIEW public.vw_inrf_relatorio_itens
  IS 'Atualizada SGINV-1758 / versão 5.11.8';

/* vw_inrf_notas */
CREATE OR REPLACE VIEW public.vw_inrf_notas AS 
 SELECT n.codigo,
    0 AS ind_oper,
    1 AS ind_emit,
    n.codigo_credor AS cod_part,
    '00'::text AS cod_sit,
    nf.serie AS ser,
    nf.subserie AS sub,
    n.numero AS num_doc,
    nf.chave_eletronica AS chv_nfse,
    nf.data_emissao AS dt_doc,
    nf.conclusao AS dt_exec_serv,
    d.valor AS vl_doc,
    nf.natureza AS ind_pgto,
    nf.desconto AS vl_desc,
    d.valor + nf.desconto AS vl_bc_pis,
    d.valor + nf.desconto AS vl_bc_cofinf,
    0 AS vl_pis_ret,
    0 AS vl_cofins_ret,
    0 AS vl_iss,
    nf.tipo_frete + 1 AS ind_frt,
    df.codigo_receita AS cod_mod,
    nf.valor_frete AS vl_frete,
    nf.valor_seguro AS vl_seguro,
    nf.valor_ipi AS vl_ipi,
    nf.valor_outras_despesas AS vl_outras
   FROM nota n,
    nota_fiscal nf,
    documento_fiscal df,
    despesa d
  WHERE n.codigo = nf.codigo AND nf.codigo_documento_fiscal = df.codigo AND n.codigo = d.codigo;

ALTER TABLE public.vw_inrf_notas
  OWNER TO sgi;

GRANT ALL ON TABLE public.vw_inrf_notas TO sgi;
GRANT SELECT ON TABLE public.vw_inrf_notas TO sgc;

COMMENT ON VIEW public.vw_inrf_notas
  IS 'Atualizada SGINV-1758 / versão 5.11.8';

/* vw_inrf_itens_nota_fiscal */
CREATE MATERIALIZED VIEW public.vw_inrf_itens_nota_fiscal AS 
 SELECT inf.codigo_pagamento AS codigo_nota,
    inf.codigo AS codigo_item_nota,
    ic.codigo AS cod_item,
    ic.nome AS descr_compl,
    rnf.valor AS vl_item,
    1 AS qtd,
    ic.codigo_unidade_medida AS unidade_medida,
    ic.codigo_barra,
    ic.codigo_tipo_item_compra,
    ic.codigo_sub_tipo_servico AS codigo_subtipo_servico,
    0 AS vl_desc,
    0 AS ind_orig_cred,
        CASE
            WHEN cf.tipo = 0 OR cf.cumulativo = true OR (ic.codigo_tipo_item_compra <> ALL (ARRAY[9::bigint, 10::bigint, 92::bigint])) THEN '70'::text
            ELSE '50'::text
        END AS cst_pis,
    rnf.valor AS vl_bc_pis,
        CASE
            WHEN cf.cumulativo = true THEN 0.65
            ELSE 1.65
        END AS aliq_pis,
        CASE
            WHEN cf.tipo = 0 OR cf.cumulativo = true OR (ic.codigo_tipo_item_compra <> ALL (ARRAY[9::bigint, 10::bigint, 92::bigint])) THEN '70'::text
            ELSE '50'::text
        END AS cst_cofins,
    rnf.valor AS vl_bc_cofins,
        CASE
            WHEN cf.cumulativo = true THEN 3::numeric
            ELSE 7.6
        END AS aliq_cofins,
        CASE
            WHEN ic.codigo_tipo_item_compra = 9 THEN '03'::text
            WHEN ic.codigo_tipo_item_compra = 10 THEN '02'::text
            ELSE ''::text
        END AS nat_bc_cred,
    ''::text AS cod_cta,
    ''::text AS cod_ccus,
    cf.tipo AS tipo_cf,
    cf.cumulativo AS cf_cumulativo,
    cf.codigo AS codigo_cf,
    cf.identificador,
    ic.codigo_tipo_item_compra_amplo,
        CASE
            WHEN inf.codigo_conta_contabil_centro_lucro IS NOT NULL AND cf.codigo_pai IS NOT NULL THEN inf.codigo_conta_contabil_centro_lucro
            WHEN inf.codigo_conta_contabil_centro_custo IS NOT NULL AND cf.codigo_pai IS NULL THEN inf.codigo_conta_contabil_centro_custo
            WHEN cf.codigo_pai IS NOT NULL THEN ( SELECT max(iccc.codigo_conta_contabil) AS max
               FROM item_compra_conta_contabil iccc,
                conta_contabil cc,
                unidade uni
              WHERE iccc.codigo_item_compra = ic.codigo AND iccc.tipo = 1 AND iccc.codigo_conta_contabil = cc.codigo AND cf.codigo_unidade = uni.codigo AND uni.codigo_matriz = cc.codigo_unidade)
            ELSE ( SELECT max(iccc.codigo_conta_contabil) AS max
               FROM item_compra_conta_contabil iccc,
                conta_contabil cc,
                unidade uni
              WHERE iccc.codigo_item_compra = ic.codigo AND iccc.tipo = 0 AND iccc.codigo_conta_contabil = cc.codigo AND cf.codigo_unidade = uni.codigo AND uni.codigo_matriz = cc.codigo_unidade)
        END AS codigo_cc,
        CASE
            WHEN inf.codigo_conta_contabil_centro_lucro IS NOT NULL AND cf.codigo_pai IS NOT NULL THEN (( SELECT cc.numero
               FROM conta_contabil cc
              WHERE cc.codigo = inf.codigo_conta_contabil_centro_lucro))::text
            WHEN inf.codigo_conta_contabil_centro_custo IS NOT NULL AND cf.codigo_pai IS NULL THEN (( SELECT cc.numero
               FROM conta_contabil cc
              WHERE cc.codigo = inf.codigo_conta_contabil_centro_custo))::text
            WHEN cf.codigo_pai IS NOT NULL THEN ( SELECT max(cc.numero::text) AS max
               FROM item_compra_conta_contabil iccc,
                conta_contabil cc,
                unidade uni
              WHERE iccc.codigo_item_compra = ic.codigo AND iccc.tipo = 1 AND iccc.codigo_conta_contabil = cc.codigo AND cf.codigo_unidade = uni.codigo AND uni.codigo_matriz = cc.codigo_unidade)
            ELSE ( SELECT max(cc.numero::text) AS max
               FROM item_compra_conta_contabil iccc,
                conta_contabil cc,
                unidade uni
              WHERE iccc.codigo_item_compra = ic.codigo AND iccc.tipo = 0 AND iccc.codigo_conta_contabil = cc.codigo AND cf.codigo_unidade = uni.codigo AND uni.codigo_matriz = cc.codigo_unidade)
        END AS numero_cc
   FROM item_nota_fiscal inf,
    rateio_nota_fiscal rnf,
    nota_fiscal nf,
    item_compra ic,
    natureza_aquisicao na,
    base_calculo_credito bcc,
    centro_financeiro cf
  WHERE inf.codigo_item_compra = ic.codigo AND inf.codigo_pagamento = nf.codigo AND inf.codigo = rnf.codigo_item_nota_fiscal AND rnf.codigo_centro_financeiro = cf.codigo AND nf.codigo_natureza_aquisicao = na.codigo AND na.codigo_base_calculo_credito = bcc.codigo AND nf.tipo_nota_fiscal <> 1
UNION
 SELECT nota_fiscal.codigo AS codigo_nota,
    item_requisicao_compra.codigo AS codigo_item_nota,
    item_compra.codigo AS cod_item,
    item_compra.nome AS descr_compl,
    item_recebimento.preco::numeric(9,2) AS vl_item,
    item_recebimento.quantidade AS qtd,
    item_compra.codigo_unidade_medida AS unidade_medida,
    item_compra.codigo_barra,
    item_compra.codigo_tipo_item_compra,
    item_compra.codigo_sub_tipo_servico AS codigo_subtipo_servico,
    0 AS vl_desc,
    0 AS ind_orig_cred,
        CASE
            WHEN centro_financeiro.tipo = 0 OR centro_financeiro.cumulativo = true OR (item_compra.codigo_tipo_item_compra <> ALL (ARRAY[9::bigint, 10::bigint, 92::bigint])) THEN '70'::text
            ELSE '50'::text
        END AS cst_pis,
    (item_recebimento.preco * item_recebimento.quantidade)::numeric(9,2) AS vl_bc_pis,
        CASE
            WHEN centro_financeiro.cumulativo = true THEN 0.65
            ELSE 1.65
        END AS aliq_pis,
        CASE
            WHEN centro_financeiro.tipo = 0 OR centro_financeiro.cumulativo = true OR (item_compra.codigo_tipo_item_compra <> ALL (ARRAY[9::bigint, 10::bigint, 92::bigint])) THEN '70'::text
            ELSE '50'::text
        END AS cst_cofins,
    (item_recebimento.preco * item_recebimento.quantidade)::numeric(9,2) AS vl_bc_cofins,
        CASE
            WHEN centro_financeiro.cumulativo = true THEN 3::numeric
            ELSE 7.6
        END AS aliq_cofins,
        CASE
            WHEN item_compra.codigo_tipo_item_compra = 9 THEN '03'::text
            WHEN item_compra.codigo_tipo_item_compra = 10 THEN '02'::text
            ELSE ''::text
        END AS nat_bc_cred,
    ''::text AS cod_cta,
    ''::text AS cod_ccus,
    centro_financeiro.tipo AS tipo_cf,
    centro_financeiro.cumulativo AS cf_cumulativo,
    centro_financeiro.codigo AS codigo_cf,
    centro_financeiro.identificador,
    item_compra.codigo_tipo_item_compra_amplo,
        CASE
            WHEN item_recebimento.codigo_conta_contabil_centro_lucro IS NOT NULL AND centro_financeiro.codigo_pai IS NOT NULL THEN item_recebimento.codigo_conta_contabil_centro_lucro
            WHEN item_recebimento.codigo_conta_contabil_centro_custo IS NOT NULL AND centro_financeiro.codigo_pai IS NULL THEN item_recebimento.codigo_conta_contabil_centro_custo
            WHEN centro_financeiro.codigo_pai IS NOT NULL THEN ( SELECT max(iccc.codigo_conta_contabil) AS max
               FROM item_compra_conta_contabil iccc,
                conta_contabil cc,
                unidade uni
              WHERE iccc.codigo_item_compra = item_compra.codigo AND iccc.tipo = 1 AND iccc.codigo_conta_contabil = cc.codigo AND centro_financeiro.codigo_unidade = uni.codigo AND uni.codigo_matriz = cc.codigo_unidade)
            ELSE ( SELECT max(iccc.codigo_conta_contabil) AS max
               FROM item_compra_conta_contabil iccc,
                conta_contabil cc,
                unidade uni
              WHERE iccc.codigo_item_compra = item_compra.codigo AND iccc.tipo = 0 AND iccc.codigo_conta_contabil = cc.codigo AND centro_financeiro.codigo_unidade = uni.codigo AND uni.codigo_matriz = cc.codigo_unidade)
        END AS codigo_cc,
        CASE
            WHEN item_recebimento.codigo_conta_contabil_centro_lucro IS NOT NULL AND centro_financeiro.codigo_pai IS NOT NULL THEN (( SELECT cc.numero
               FROM conta_contabil cc
              WHERE cc.codigo = item_recebimento.codigo_conta_contabil_centro_lucro))::text
            WHEN item_recebimento.codigo_conta_contabil_centro_custo IS NOT NULL AND centro_financeiro.codigo_pai IS NULL THEN (( SELECT cc.numero
               FROM conta_contabil cc
              WHERE cc.codigo = item_recebimento.codigo_conta_contabil_centro_custo))::text
            WHEN centro_financeiro.codigo_pai IS NOT NULL THEN ( SELECT max(cc.numero::text) AS max
               FROM item_compra_conta_contabil iccc,
                conta_contabil cc,
                unidade uni
              WHERE iccc.codigo_item_compra = item_compra.codigo AND iccc.tipo = 1 AND iccc.codigo_conta_contabil = cc.codigo AND centro_financeiro.codigo_unidade = uni.codigo AND uni.codigo_matriz = cc.codigo_unidade)
            ELSE ( SELECT max(cc.numero::text) AS max
               FROM item_compra_conta_contabil iccc,
                conta_contabil cc,
                unidade uni
              WHERE iccc.codigo_item_compra = item_compra.codigo AND iccc.tipo = 0 AND iccc.codigo_conta_contabil = cc.codigo AND centro_financeiro.codigo_unidade = uni.codigo AND uni.codigo_matriz = cc.codigo_unidade)
        END AS numero_cc
   FROM ordem_compra ordem_compra
     JOIN requisicao_compra_refinada requisicao_compra_refinada ON ordem_compra.codigo = requisicao_compra_refinada.codigo_ordem_compra
     JOIN requisicao_compra requisicao_compra ON requisicao_compra_refinada.codigo_requisicao_compra = requisicao_compra.codigo
     JOIN item_requisicao_compra item_requisicao_compra ON requisicao_compra_refinada.codigo = item_requisicao_compra.codigo_rc_refinada
     JOIN item_compra item_compra ON item_requisicao_compra.codigo_item_compra = item_compra.codigo
     JOIN item_proposta item_proposta ON item_compra.codigo = item_proposta.codigo_item_compra
     JOIN proposta proposta ON item_proposta.codigo_proposta = proposta.codigo
     JOIN item_pedido item_pedido ON item_proposta.codigo = item_pedido.codigo_item_proposta
     JOIN pedido pedido ON item_pedido.codigo_pedido = pedido.codigo
     JOIN item_recebimento item_recebimento ON item_pedido.codigo = item_recebimento.codigo_item_pedido
     JOIN recebimento recebimento ON item_recebimento.codigo_recebimento = recebimento.codigo AND pedido.codigo = recebimento.codigo_pedido
     JOIN nota_fiscal nota_fiscal ON recebimento.codigo_nota_fiscal = nota_fiscal.codigo
     JOIN nota nota ON nota_fiscal.codigo = nota.codigo AND ordem_compra.codigo = pedido.codigo_ordem_compra AND ordem_compra.codigo = proposta.codigo_ordem_compra
     JOIN centro_financeiro centro_financeiro ON requisicao_compra.codigo_centro_financeiro = centro_financeiro.codigo
  WHERE nota_fiscal.tipo_nota_fiscal = 1 AND item_recebimento.quantidade <> 0::numeric
WITH DATA;

ALTER TABLE public.vw_inrf_itens_nota_fiscal
  OWNER TO sgi;

COMMENT ON MATERIALIZED VIEW public.vw_inrf_itens_nota_fiscal
  IS 'Atualizada SGINV-1758 / versão 5.11.8';

/* vw_inrf_notas_validas */
CREATE OR REPLACE VIEW public.vw_inrf_notas_validas AS 
 SELECT nf.codigo AS codigo_nota,
    p.codigo AS codigo_pessoa,
    pg.data_movimento AS data,
    n.codigo_empresa AS codigo_unidade,
        CASE
            WHEN nf.tipo_fornecimento = 1 THEN 'BL-A'::text
            WHEN nf.tipo_fornecimento = 0 AND (df.codigo_receita::text = ANY (ARRAY['01'::character varying::text, '1B'::character varying::text, '04'::character varying::text, '55'::character varying::text])) THEN 'BL-C1'::text
            WHEN nf.tipo_fornecimento = 0 AND (df.codigo_receita::text = ANY (ARRAY['02'::character varying::text, '2D'::character varying::text, '2E'::character varying::text])) THEN 'BL-C1'::text
            WHEN nf.tipo_fornecimento = 0 AND (df.codigo_receita::text = ANY (ARRAY['06'::character varying::text, '28'::character varying::text, '29'::character varying::text])) THEN 'BL-C5'::text
            WHEN nf.tipo_fornecimento = 0 AND (df.codigo_receita::text = ANY (ARRAY['07'::character varying::text, '08'::character varying::text, '8B'::character varying::text, '09'::character varying::text, '10'::character varying::text, '11'::character varying::text, '26'::character varying::text, '27'::character varying::text, '57'::character varying::text])) THEN 'BL-D'::text
            ELSE 'ERRO'::text
        END AS bloco,
    cf.tipo AS tipo_cf,
    cf.cumulativo,
    cf.codigo AS codigo_cf
   FROM nota n,
    nota_fiscal nf,
    pessoa p,
    centro_financeiro cf,
    documento_fiscal df,
    pagamento pg,
    parcela_nota pn
  WHERE n.codigo_credor = p.codigo AND n.codigo = nf.codigo AND n.codigo_centro_financeiro = cf.codigo AND nf.codigo_documento_fiscal = df.codigo AND pg.codigo = pn.codigo AND pn.codigo_nota = n.codigo AND pn.ordem = 0 AND pg.data_movimento IS NOT NULL;

ALTER TABLE public.vw_inrf_notas_validas
  OWNER TO sgi;

COMMENT ON VIEW public.vw_inrf_notas_validas
  IS 'Atualizada SGINV-1758 / versão 5.11.8';

/* vw_total_credito_por_nf */
CREATE MATERIALIZED VIEW public.vw_total_credito_por_nf AS 
 SELECT vw_itens.codigo_nota,
    sum(vw_itens.vl_bc_cofins * (vw_itens.aliq_cofins / 100::numeric))::numeric(9,2) AS credito_cofins,
    sum(vw_itens.vl_bc_cofins * (vw_itens.aliq_pis / 100::numeric))::numeric(9,2) AS credito_pis,
    sum(vw_itens.vl_bc_cofins)::numeric(9,2) AS base_calculo,
    sum(
        CASE
            WHEN vw_itens.codigo_tipo_item_compra = ANY (ARRAY[9::bigint, 92::bigint]) THEN vw_itens.vl_bc_cofins
            ELSE 0::numeric
        END) AS base_servico,
    sum(
        CASE
            WHEN vw_itens.codigo_tipo_item_compra = 10 THEN vw_itens.vl_bc_cofins
            ELSE 0::numeric
        END) AS base_material
   FROM vw_inrf_itens_nota_fiscal vw_itens,
    vw_inrf_notas_validas vw_notas
  WHERE vw_itens.codigo_nota = vw_notas.codigo_nota AND (vw_itens.cst_cofins = '50'::text OR vw_itens.cst_pis = '50'::text)
  GROUP BY vw_itens.codigo_nota
WITH DATA;

ALTER TABLE public.vw_total_credito_por_nf
  OWNER TO sgi;

COMMENT ON MATERIALIZED VIEW public.vw_total_credito_por_nf
  IS 'Atualizada SGINV-1758 / versão 5.11.8';

/* ---------------------------------------------------------------------- */
/* Add "aplicacao_motivo_retorno"                                         */
/* ---------------------------------------------------------------------- */
INSERT	INTO aplicacao_motivo_retorno (codigo, codigo_perfil, nome, chave, tipo, criador, data_criacao, atualizador, data_atualizacao) 
SELECT	NEXTVAL('seq_aplicacao_motivo_retorno'), perfil.codigo, 'Reverter cancelamento de NF (Faturamento)', 'fatura-revert_canc_nf', 1, 'admin', CURRENT_TIMESTAMP, NULL, NULL
FROM	perfil
WHERE	perfil.nome = 'faturamento-analista';
