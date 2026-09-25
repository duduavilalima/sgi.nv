/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2019-08-21 17:04                                */
/* ---------------------------------------------------------------------- */
/*                                2ª parte                                */
/* ---------------------------------------------------------------------- */

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
  IS 'Atualizada SGINV-2434 / versão 5.12.2';

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
  IS 'Atualizada SGINV-2434 / versão 5.12.2';

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
  IS 'Atualizada SGINV-2434 / versão 5.12.2';

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
            WHEN cf.tipo = 1 AND cf.cumulativo = false AND (ic.codigo IN ( SELECT vw_item_compra_credito_efd.codigo_item_compra
               FROM vw_item_compra_credito_efd
              WHERE vw_item_compra_credito_efd.codigo_item_compra = ic.codigo)) THEN '50'::text
            ELSE '70'::text
        END AS cst_pis,
    rnf.valor AS vl_bc_pis,
        CASE
            WHEN cf.cumulativo = true THEN 0.65
            ELSE 1.65
        END AS aliq_pis,
        CASE
            WHEN cf.tipo = 1 AND cf.cumulativo = false AND (ic.codigo IN ( SELECT vw_item_compra_credito_efd.codigo_item_compra
               FROM vw_item_compra_credito_efd
              WHERE vw_item_compra_credito_efd.codigo_item_compra = ic.codigo)) THEN '50'::text
            ELSE '70'::text
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
            WHEN centro_financeiro.tipo = 1 AND centro_financeiro.cumulativo = false AND (item_compra.codigo IN ( SELECT vw_item_compra_credito_efd.codigo_item_compra
               FROM vw_item_compra_credito_efd
              WHERE vw_item_compra_credito_efd.codigo_item_compra = item_compra.codigo)) THEN '50'::text
            ELSE '70'::text
        END AS cst_pis,
    (item_recebimento.preco * item_recebimento.quantidade)::numeric(9,2) AS vl_bc_pis,
        CASE
            WHEN centro_financeiro.cumulativo = true THEN 0.65
            ELSE 1.65
        END AS aliq_pis,
        CASE
            WHEN centro_financeiro.tipo = 1 AND centro_financeiro.cumulativo = false AND (item_compra.codigo IN ( SELECT vw_item_compra_credito_efd.codigo_item_compra
               FROM vw_item_compra_credito_efd
              WHERE vw_item_compra_credito_efd.codigo_item_compra = item_compra.codigo)) THEN '50'::text
            ELSE '70'::text
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
  IS 'Atualizada SGINV-2434 / versão 5.12.2';

/* vw_total_credito_por_nf */
CREATE MATERIALIZED VIEW public.vw_total_credito_por_nf AS
 SELECT vw_itens.codigo_nota,
    sum(vw_itens.vl_bc_cofins * (vw_itens.aliq_cofins / 100::numeric) + vw_itens.vl_bc_cofins * (vw_itens.aliq_pis / 100::numeric))::numeric(9,2) AS valor_credito,
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
  IS 'Atualizada SGINV-2434 / versão 5.12.2';

/* vw_custos_receita_realizada */
CREATE OR REPLACE VIEW public.vw_custos_receita_realizada AS
 SELECT cf.codigo_unidade,
    u.codigo_matriz,
    n.codigo_centro_financeiro,
    cf.codigo_pai AS codigo_gerencia,
    date_part('YEAR'::text, nsp.data_pagamento) AS ano_pagamento,
    date_part('MONTH'::text, nsp.data_pagamento) AS mes_pagamento,
    sum(nsp.valor_liquido) AS valor_liquido,
    f.tipo AS tipo_faturamento
   FROM nota n,
    nota_fiscal nf,
    nota_saida ns,
    nota_saida_parcela nsp,
    unidade u,
    centro_financeiro cf,
    faturamento_etapa fe,
    faturamento f
  WHERE n.codigo = nf.codigo AND nf.codigo = ns.codigo AND ns.codigo = nsp.codigo_nota_saida AND cf.codigo_unidade = u.codigo AND n.codigo_centro_financeiro = cf.codigo AND ns.codigo = fe.codigo_nota_saida AND fe.codigo_faturamento = f.codigo AND nf.tipo_nota_fiscal = 11 AND n.status = 16
  GROUP BY cf.codigo_unidade, u.codigo_matriz, n.codigo_centro_financeiro, cf.codigo_pai, (date_part('YEAR'::text, nsp.data_pagamento)), (date_part('MONTH'::text, nsp.data_pagamento)), f.tipo
  ORDER BY (date_part('YEAR'::text, nsp.data_pagamento)), (date_part('MONTH'::text, nsp.data_pagamento)), n.codigo_centro_financeiro, f.tipo;

ALTER TABLE public.vw_custos_receita_realizada
  OWNER TO sgi;

COMMENT ON VIEW public.vw_custos_receita_realizada
  IS 'Atualizada SGINV-2434 / versão 5.12.2';

/* vw_custos_receita_prevista */
CREATE OR REPLACE VIEW public.vw_custos_receita_prevista AS
 SELECT cf.codigo_unidade,
    u.codigo_matriz,
    n.codigo_centro_financeiro,
    cf.codigo_pai AS codigo_gerencia,
    date_part('YEAR'::text, nsp.data_vencimento) AS ano_vencimento,
    date_part('MONTH'::text, nsp.data_vencimento) AS mes_vencimento,
    sum(nsp.valor_liquido) AS valor_liquido,
    f.tipo AS tipo_faturamento
   FROM nota n,
    nota_fiscal nf,
    nota_saida ns,
    nota_saida_parcela nsp,
    unidade u,
    centro_financeiro cf,
    faturamento_etapa fe,
    faturamento f
  WHERE n.codigo = nf.codigo AND nf.codigo = ns.codigo AND ns.codigo = nsp.codigo_nota_saida AND cf.codigo_unidade = u.codigo AND n.codigo_centro_financeiro = cf.codigo AND ns.codigo = fe.codigo_nota_saida AND fe.codigo_faturamento = f.codigo AND nf.tipo_nota_fiscal = 11 AND (n.status = ANY (ARRAY[15, 16]))
  GROUP BY cf.codigo_unidade, u.codigo_matriz, n.codigo_centro_financeiro, cf.codigo_pai, (date_part('YEAR'::text, nsp.data_vencimento)), (date_part('MONTH'::text, nsp.data_vencimento)), f.tipo
  ORDER BY (date_part('YEAR'::text, nsp.data_vencimento)), (date_part('MONTH'::text, nsp.data_vencimento)), n.codigo_centro_financeiro, f.tipo;

ALTER TABLE public.vw_custos_receita_prevista
  OWNER TO sgi;

COMMENT ON VIEW public.vw_custos_receita_prevista
  IS 'Atualizada SGINV-2434 / versão 5.12.2';

/* vw_custos_gastos_previstos */
CREATE MATERIALIZED VIEW public.vw_custos_gastos_previstos AS
 SELECT n.codigo,
    cf.codigo_unidade,
    u.codigo_matriz,
    date_part('YEAR'::text, p.data_pagamento) AS ano_pagamento,
    date_part('MONTH'::text, p.data_pagamento) AS mes_pagamento,
    d.valor AS valor_total_nf,
    p.valor + p.valor_multa + p.valor_juros - p.valor_desconto - p.valor_devolucao AS valor_liquido_pagamento,
    rir.codigo_centro_financeiro,
    cf.codigo_pai AS codigo_gerencia,
    round(sum(ir.preco * rir.quantidade), 2) AS valor_recebimento,
    d.valor + nf.desconto - nf.valor_frete - nf.valor_seguro - nf.valor_outras_despesas - nf.valor_ipi AS total_itens,
        CASE
            WHEN tc.chave::text = 'CONTRATO'::text THEN 0
            WHEN tc.chave::text = 'CONTRATO_REEMBOLSAVEL'::text THEN 0
            ELSE 1
        END AS tipo_faturamento
   FROM nota n,
    nota_fiscal nf,
    despesa d,
    parcela_nota pn,
    pagamento p,
    recebimento rec,
    item_recebimento ir,
    rateio_item_recebimento rir,
    centro_financeiro cf,
    unidade u,
    tipo_cobranca tc
  WHERE nf.codigo = n.codigo AND d.codigo = n.codigo AND n.codigo = pn.codigo_nota AND pn.codigo = p.codigo AND rec.codigo_nota_fiscal = nf.codigo AND ir.codigo_recebimento = rec.codigo AND rir.codigo_item_recebimento = ir.codigo AND cf.codigo = rir.codigo_centro_financeiro AND u.codigo = cf.codigo_unidade AND tc.codigo = nf.codigo_tipo_cobranca AND ir.preco > 0::numeric
  GROUP BY n.codigo, cf.codigo_unidade, u.codigo_matriz, (date_part('YEAR'::text, p.data_pagamento)), (date_part('MONTH'::text, p.data_pagamento)), d.valor, nf.valor_frete, nf.valor_seguro, nf.desconto, nf.valor_outras_despesas, nf.valor_ipi, p.valor, (p.valor + p.valor_multa + p.valor_juros - p.valor_desconto - p.valor_devolucao), rir.codigo_centro_financeiro, cf.codigo_pai, tc.chave
UNION
 SELECT n.codigo,
    cf.codigo_unidade,
    u.codigo_matriz,
    date_part('YEAR'::text, p.data_pagamento) AS ano_pagamento,
    date_part('MONTH'::text, p.data_pagamento) AS mes_pagamento,
    d.valor AS valor_total_nf,
    p.valor + p.valor_multa + p.valor_juros - p.valor_desconto - p.valor_devolucao AS valor_liquido_pagamento,
    rnf.codigo_centro_financeiro,
    COALESCE(cf.codigo_pai, rnf.codigo_centro_financeiro) AS codigo_gerencia,
    round(sum(rnf.valor), 2) AS valor_recebimento,
    d.valor + nf.desconto - nf.valor_frete - nf.valor_seguro - nf.valor_outras_despesas - nf.valor_ipi AS total_itens,
        CASE
            WHEN tc.chave::text = 'CONTRATO'::text THEN 0
            WHEN tc.chave::text = 'CONTRATO_REEMBOLSAVEL'::text THEN 0
            ELSE 1
        END AS tipo_faturamento
   FROM nota n,
    nota_fiscal nf,
    despesa d,
    parcela_nota pn,
    pagamento p,
    item_nota_fiscal inf,
    rateio_nota_fiscal rnf,
    centro_financeiro cf,
    unidade u,
    tipo_cobranca tc
  WHERE nf.codigo = n.codigo AND d.codigo = n.codigo AND n.codigo = pn.codigo_nota AND pn.codigo = p.codigo AND inf.codigo_pagamento = nf.codigo AND rnf.codigo_item_nota_fiscal = inf.codigo AND cf.codigo = rnf.codigo_centro_financeiro AND u.codigo = cf.codigo_unidade AND tc.codigo = nf.codigo_tipo_cobranca
  GROUP BY n.codigo, cf.codigo_unidade, u.codigo_matriz, (date_part('YEAR'::text, p.data_pagamento)), (date_part('MONTH'::text, p.data_pagamento)), d.valor, nf.valor_frete, nf.valor_seguro, nf.desconto, nf.valor_outras_despesas, nf.valor_ipi, p.valor, (p.valor + p.valor_multa + p.valor_juros - p.valor_desconto - p.valor_devolucao), rnf.codigo_centro_financeiro, cf.codigo_pai, tc.chave
UNION
 SELECT n.codigo,
    cf.codigo_unidade,
    u.codigo_matriz,
    date_part('YEAR'::text, p.data_pagamento) AS ano_pagamento,
    date_part('MONTH'::text, p.data_pagamento) AS mes_pagamento,
    d.valor AS valor_total_nf,
    p.valor + p.valor_multa + p.valor_juros - p.valor_desconto AS valor_liquido_pagamento,
    rnp.codigo_centro_financeiro,
    COALESCE(cf.codigo_pai, rnp.codigo_centro_financeiro) AS codigo_gerencia,
    round(sum(rnp.valor), 2) AS valor_recebimento,
    d.valor AS total_itens,
    0 AS tipo_faturamento
   FROM nota n,
    nota_pagamento np,
    despesa d,
    parcela_nota pn,
    pagamento p,
    rateio_nota_pagamento rnp,
    centro_financeiro cf,
    unidade u
  WHERE np.codigo = n.codigo AND d.codigo = n.codigo AND n.codigo = pn.codigo_nota AND pn.codigo = p.codigo AND rnp.codigo_nota_pagamento = np.codigo AND cf.codigo = rnp.codigo_centro_financeiro AND u.codigo = cf.codigo_unidade
  GROUP BY n.codigo, cf.codigo_unidade, u.codigo_matriz, (date_part('YEAR'::text, p.data_pagamento)), (date_part('MONTH'::text, p.data_pagamento)), d.valor, p.valor, (p.valor + p.valor_multa + p.valor_juros - p.valor_desconto), rnp.codigo_centro_financeiro, cf.codigo_pai
  ORDER BY 1, 10 DESC, 8
WITH DATA;

ALTER TABLE public.vw_custos_gastos_previstos
  OWNER TO sgi;

COMMENT ON MATERIALIZED VIEW public.vw_custos_gastos_previstos
  IS 'Atualizada SGINV-2434 / versão 5.12.2';

/* vw_custos_faturamento_realizado */
CREATE OR REPLACE VIEW public.vw_custos_faturamento_realizado AS
 SELECT cf.codigo_unidade,
    u.codigo_matriz,
    n.codigo_centro_financeiro,
    cf.codigo_pai AS codigo_gerencia,
    date_part('YEAR'::text, nf.data_emissao) AS ano_emissao,
    date_part('MONTH'::text, nf.data_emissao) AS mes_emissao,
    sum(nsp.valor_bruto) AS valor_bruto,
    f.tipo AS tipo_faturamento
   FROM nota n,
    nota_fiscal nf,
    nota_saida ns,
    nota_saida_parcela nsp,
    unidade u,
    centro_financeiro cf,
    faturamento_etapa fe,
    faturamento f
  WHERE n.codigo = nf.codigo AND nf.codigo = ns.codigo AND ns.codigo = nsp.codigo_nota_saida AND cf.codigo_unidade = u.codigo AND n.codigo_centro_financeiro = cf.codigo AND ns.codigo = fe.codigo_nota_saida AND fe.codigo_faturamento = f.codigo AND nf.tipo_nota_fiscal = 11 AND (n.status = ANY (ARRAY[15, 16]))
  GROUP BY cf.codigo_unidade, u.codigo_matriz, n.codigo_centro_financeiro, cf.codigo_pai, (date_part('YEAR'::text, nf.data_emissao)), (date_part('MONTH'::text, nf.data_emissao)), f.tipo
  ORDER BY (date_part('YEAR'::text, nf.data_emissao)), (date_part('MONTH'::text, nf.data_emissao)), n.codigo_centro_financeiro, f.tipo;

ALTER TABLE public.vw_custos_faturamento_realizado
  OWNER TO sgi;

COMMENT ON VIEW public.vw_custos_faturamento_realizado
  IS 'Atualizada SGINV-2434 / versão 5.12.2';

/* vw_custos_faturamento_previsto */
CREATE OR REPLACE VIEW public.vw_custos_faturamento_previsto AS
 SELECT cf.codigo_unidade,
    u.codigo_matriz,
    f.codigo_centro_financeiro,
    cf.codigo_pai AS codigo_gerencia,
    f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro + round((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro) * ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100::numeric) / (1::numeric - (f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100::numeric), 2) + round((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro + round((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro) * ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100::numeric) / (1::numeric - (f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100::numeric), 2)) * COALESCE(( SELECT sum(a.percentual) AS sum
           FROM centro_financeiro_aliquota cfa,
            aliquota a
          WHERE cfa.codigo_centro_financeiro = f.codigo_centro_financeiro AND cfa.codigo_aliquota = a.codigo), 0::numeric) / 100::numeric / (1::numeric - COALESCE(( SELECT sum(a.percentual) AS sum
           FROM centro_financeiro_aliquota cfa,
            aliquota a
          WHERE cfa.codigo_centro_financeiro = f.codigo_centro_financeiro AND cfa.codigo_aliquota = a.codigo), 0::numeric) / 100::numeric), 2) AS valor_mensal,
    f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro AS custo_mensal,
    round((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro) * ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100::numeric) / (1::numeric - (f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100::numeric), 2) AS valor_bdi,
    0 AS tipo_faturamento
   FROM fecho f,
    centro_financeiro cf,
    contrato c,
    unidade u
  WHERE f.codigo_centro_financeiro = cf.codigo AND cf.codigo_contrato = c.codigo AND cf.codigo_unidade = u.codigo AND f.numero = (( SELECT max(f2.numero) AS max
           FROM fecho f2
          WHERE f2.codigo_centro_financeiro = f.codigo_centro_financeiro)) AND cf.situacao = 1 AND c.situacao = 2
  ORDER BY f.codigo_centro_financeiro;

ALTER TABLE public.vw_custos_faturamento_previsto
  OWNER TO sgi;

COMMENT ON VIEW public.vw_custos_faturamento_previsto
  IS 'Atualizada SGINV-2434 / versão 5.12.2';

/* vw_compras_itens_por_natureza */
CREATE MATERIALIZED VIEW public.vw_compras_itens_por_natureza AS
 SELECT nat.nome AS natureza,
    nat.codigo AS codigo_natureza,
    cf.codigo AS codigo_cf,
    cf.codigo_pai AS codigo_pai_cf,
    ic.nome AS item,
    date(d.data) AS data_compra,
    ir.preco,
    ir.quantidade,
    pessoa.documento AS cnpj,
    pessoa.nome AS razao_social,
    nota.codigo_centro_financeiro AS codigo_ger,
    u.codigo_matriz AS codigo_empresa
   FROM centro_financeiro cf,
    rateio_item_recebimento rir,
    item_recebimento ir,
    recebimento rec,
    item_pedido ip,
    item_proposta iprop,
    item_compra ic,
    natureza_fornecimento nat,
    nota_fiscal nf,
    nota,
    despesa d,
    pessoa,
    unidade u
  WHERE cf.codigo = rir.codigo_centro_financeiro AND rir.codigo_item_recebimento = ir.codigo AND ir.codigo_item_pedido = ip.codigo AND ip.codigo_item_proposta = iprop.codigo AND iprop.codigo_item_compra = ic.codigo AND ic.codigo_natureza_fornecimento = nat.codigo AND ir.codigo_recebimento = rec.codigo AND rec.codigo_nota_fiscal = nf.codigo AND nf.codigo = nota.codigo AND nota.codigo = d.codigo AND nota.codigo_credor = pessoa.codigo AND cf.codigo_unidade = u.codigo AND ir.preco > 0::numeric
UNION
 SELECT nat.nome AS natureza,
    nat.codigo AS codigo_natureza,
    cf.codigo AS codigo_cf,
    cf.codigo_pai AS codigo_pai_cf,
    ic.nome AS item,
    date(d.data) AS data_compra,
    inf.preco,
    inf.quantidade,
    pessoa.documento AS cnpj,
    pessoa.nome AS razao_social,
    nota.codigo_centro_financeiro AS codigo_ger,
    u.codigo_matriz AS codigo_empresa
   FROM centro_financeiro cf,
    rateio_nota_fiscal rnf,
    item_nota_fiscal inf,
    item_compra ic,
    natureza_fornecimento nat,
    nota_fiscal nf,
    nota,
    despesa d,
    pessoa,
    unidade u
  WHERE cf.codigo = rnf.codigo_centro_financeiro AND rnf.codigo_item_nota_fiscal = inf.codigo AND inf.codigo_item_compra = ic.codigo AND ic.codigo_natureza_fornecimento = nat.codigo AND inf.codigo_pagamento = nf.codigo AND nf.codigo = nota.codigo AND nota.codigo = d.codigo AND nota.codigo_credor = pessoa.codigo AND cf.codigo_unidade = u.codigo AND inf.preco > 0::numeric
  ORDER BY 1, 5
WITH DATA;

ALTER TABLE public.vw_compras_itens_por_natureza
  OWNER TO sgi;

COMMENT ON MATERIALIZED VIEW public.vw_compras_itens_por_natureza
  IS 'Atualizada SGINV-2434 / versão 5.12.2';
