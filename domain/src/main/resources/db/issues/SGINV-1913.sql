/* ---------------------------------------------------------------------- */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Eduardo Ávila                                   */
/* Script type:           Alter database script                           */
/* Created on:            2018-10-25 16:00                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Create views                                                           */
/* ---------------------------------------------------------------------- */

/* vw_custos_faturamento_previsto - Gráfico "Faturamento (NF emitidas)" - "Previsto (OS Inicial)" */
-- SELECT	f.codigo_centro_financeiro, cf.identificador, cf.nome, cf.codigo_pai AS codigo_gerencia, cf.codigo_unidade, u.codigo_matriz, f.valor_mao_obra, f.valor_gastos_gerais, f.valor_material, f.valor_servico_terceiro,
-- 	(f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro) AS custo_mensal, f.percentual_bdi_beneficios, f.percentual_bdi_despesas,
-- 	ROUND((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material+valor_servico_terceiro) * ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100) / (1 - ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100)), 2) AS valor_bdi,
-- 	ROUND((((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro) + ROUND((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material+valor_servico_terceiro) * ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100) / (1 - ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100)), 2)) * COALESCE((SELECT sum(a.percentual) FROM centro_financeiro_aliquota cfa, aliquota a WHERE cfa.codigo_centro_financeiro = f.codigo_centro_financeiro AND cfa.codigo_aliquota = a.codigo),0)) / 100 / (1 - (COALESCE((SELECT sum(a.percentual) FROM centro_financeiro_aliquota cfa, aliquota a WHERE cfa.codigo_centro_financeiro = f.codigo_centro_financeiro AND cfa.codigo_aliquota = a.codigo),0)) / 100),2) AS valor_tributos,
-- 	COALESCE((SELECT sum(a.percentual) FROM centro_financeiro_aliquota cfa, aliquota a WHERE cfa.codigo_centro_financeiro = f.codigo_centro_financeiro AND cfa.codigo_aliquota = a.codigo),0) AS percentual_tributos,
-- 	((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro) + ROUND((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material+valor_servico_terceiro) * ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100) / (1 - ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100)), 2) + ROUND((((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro) + ROUND((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material+valor_servico_terceiro) * ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100) / (1 - ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100)), 2)) * COALESCE((SELECT sum(a.percentual) FROM centro_financeiro_aliquota cfa, aliquota a WHERE cfa.codigo_centro_financeiro = f.codigo_centro_financeiro AND cfa.codigo_aliquota = a.codigo),0)) / 100 / (1 - (COALESCE((SELECT sum(a.percentual) FROM centro_financeiro_aliquota cfa, aliquota a WHERE cfa.codigo_centro_financeiro = f.codigo_centro_financeiro AND cfa.codigo_aliquota = a.codigo),0)) / 100),2)) AS valor_mensal
-- DROP VIEW vw_custos_faturamento_previsto;
CREATE OR REPLACE VIEW vw_custos_faturamento_previsto AS
SELECT	cf.codigo_unidade, u.codigo_matriz, f.codigo_centro_financeiro, cf.codigo_pai AS codigo_gerencia,
	((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro) + ROUND((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material+valor_servico_terceiro) * ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100) / (1 - ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100)), 2) + ROUND((((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro) + ROUND((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material+valor_servico_terceiro) * ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100) / (1 - ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100)), 2)) * COALESCE((SELECT sum(a.percentual) FROM centro_financeiro_aliquota cfa, aliquota a WHERE cfa.codigo_centro_financeiro = f.codigo_centro_financeiro AND cfa.codigo_aliquota = a.codigo),0)) / 100 / (1 - (COALESCE((SELECT sum(a.percentual) FROM centro_financeiro_aliquota cfa, aliquota a WHERE cfa.codigo_centro_financeiro = f.codigo_centro_financeiro AND cfa.codigo_aliquota = a.codigo),0)) / 100),2)) AS valor_mensal,
	(f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material + f.valor_servico_terceiro) AS custo_mensal, ROUND((f.valor_mao_obra + f.valor_gastos_gerais + f.valor_material+valor_servico_terceiro) * ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100) / (1 - ((f.percentual_bdi_beneficios + f.percentual_bdi_despesas) / 100)), 2) AS valor_bdi,
	0 AS tipo_faturamento
FROM	fecho f, centro_financeiro cf, contrato c, unidade u
WHERE	f.codigo_centro_financeiro = cf.codigo
AND		cf.codigo_contrato = c.codigo
AND		cf.codigo_unidade = u.codigo
AND		f.numero = (SELECT MAX(f2.numero) FROM fecho f2 WHERE f2.codigo_centro_financeiro = f.codigo_centro_financeiro)
AND		cf.situacao = 1 --CF ativos
AND		c.situacao = 2
ORDER BY	f.codigo_centro_financeiro; --Contratos ativos

/* Set owner */
ALTER TABLE public.vw_custos_faturamento_previsto
  OWNER TO sgi;

/* Comment */
COMMENT ON VIEW public.vw_custos_faturamento_previsto
  IS 'Atualizada SGINV-1913 / versão 5.11.13';

/* vw_custos_faturamento_realizado - Gráfico "Faturamento (NF emitidas)" - "Realizado (NF emitidas)" */
CREATE OR REPLACE VIEW vw_custos_faturamento_realizado AS
SELECT	cf.codigo_unidade, u.codigo_matriz, n.codigo_centro_financeiro, cf.codigo_pai AS codigo_gerencia, 
	date_part('YEAR', nf.data_emissao) AS ano_emissao, date_part('MONTH', nf.data_emissao) AS mes_emissao, SUM(nsp.valor_bruto) AS valor_bruto, f.tipo AS tipo_faturamento
FROM	nota n, nota_fiscal nf, nota_saida ns, nota_saida_parcela nsp, unidade u, centro_financeiro cf, faturamento_etapa fe, faturamento f
WHERE	n.codigo = nf.codigo
AND		nf.codigo = ns.codigo
AND		ns.codigo = nsp.codigo_nota_saida
AND		cf.codigo_unidade = u.codigo
AND		n.codigo_centro_financeiro = cf.codigo
AND		ns.codigo = fe.codigo_nota_saida
AND		fe.codigo_faturamento = f.codigo
AND		nf.tipo_nota_fiscal = 11
AND		n.status IN (15, 16)
GROUP BY	cf.codigo_unidade, u.codigo_matriz, n.codigo_centro_financeiro, cf.codigo_pai, date_part('YEAR', nf.data_emissao), date_part('MONTH', nf.data_emissao), f.tipo
ORDER BY	date_part('YEAR', nf.data_emissao), date_part('MONTH', nf.data_emissao), n.codigo_centro_financeiro, f.tipo;

/* Set owner */
ALTER TABLE public.vw_custos_faturamento_realizado
  OWNER TO sgi;

/* Comment */
COMMENT ON VIEW public.vw_custos_faturamento_realizado
  IS 'Atualizada SGINV-1913 / versão 5.11.13';
--select mes_emissao, sum(valor_bruto) from vw_custos_faturamento_realizado where ano_emissao = 2018 group by mes_emissao

/* vw_custos_receita_prevista - Gráfico "Receita Líquida" - "Previsto (NF emitidas menos tributos)" */
CREATE OR REPLACE VIEW vw_custos_receita_prevista AS
SELECT	cf.codigo_unidade, u.codigo_matriz, n.codigo_centro_financeiro, cf.codigo_pai AS codigo_gerencia, 
	date_part('YEAR', nsp.data_vencimento) AS ano_vencimento, date_part('MONTH', nsp.data_vencimento) AS mes_vencimento, SUM(nsp.valor_liquido) AS valor_liquido, f.tipo AS tipo_faturamento
FROM	nota n, nota_fiscal nf, nota_saida ns, nota_saida_parcela nsp, unidade u, centro_financeiro cf, faturamento_etapa fe, faturamento f
WHERE	n.codigo = nf.codigo
AND		nf.codigo = ns.codigo
AND		ns.codigo = nsp.codigo_nota_saida
AND		cf.codigo_unidade = u.codigo
AND		n.codigo_centro_financeiro = cf.codigo
AND		ns.codigo = fe.codigo_nota_saida
AND		fe.codigo_faturamento = f.codigo
AND		nf.tipo_nota_fiscal = 11
AND		n.status IN (15, 16)
GROUP BY	cf.codigo_unidade, u.codigo_matriz, n.codigo_centro_financeiro, cf.codigo_pai, date_part('YEAR', nsp.data_vencimento), date_part('MONTH', nsp.data_vencimento), f.tipo
ORDER BY	date_part('YEAR', nsp.data_vencimento), date_part('MONTH', nsp.data_vencimento), n.codigo_centro_financeiro, f.tipo;

/* Set owner */
ALTER TABLE public.vw_custos_receita_prevista
  OWNER TO sgi;

/* Comment */
COMMENT ON VIEW public.vw_custos_receita_prevista
  IS 'Atualizada SGINV-1913 / versão 5.11.13';
--select mes_vencimento, sum(valor_liquido) from vw_custos_receita_prevista where ano_vencimento = 2018 group by mes_vencimento

/* vw_custos_receita_prevista - Gráfico "Receita Líquida" - "Previsto (NF emitidas menos tributos)" */
CREATE OR REPLACE VIEW vw_custos_receita_realizada AS
SELECT	cf.codigo_unidade, u.codigo_matriz, n.codigo_centro_financeiro, cf.codigo_pai AS codigo_gerencia, 
	date_part('YEAR', nsp.data_pagamento) AS ano_pagamento, date_part('MONTH', nsp.data_pagamento) AS mes_pagamento, SUM(nsp.valor_liquido) AS valor_liquido, f.tipo AS tipo_faturamento
FROM	nota n, nota_fiscal nf, nota_saida ns, nota_saida_parcela nsp, unidade u, centro_financeiro cf, faturamento_etapa fe, faturamento f
WHERE	n.codigo = nf.codigo
AND		nf.codigo = ns.codigo
AND		ns.codigo = nsp.codigo_nota_saida
AND		cf.codigo_unidade = u.codigo
AND		n.codigo_centro_financeiro = cf.codigo
AND		ns.codigo = fe.codigo_nota_saida
AND		fe.codigo_faturamento = f.codigo
AND		nf.tipo_nota_fiscal = 11
AND		n.status = 16
GROUP BY	cf.codigo_unidade, u.codigo_matriz, n.codigo_centro_financeiro, cf.codigo_pai, date_part('YEAR', nsp.data_pagamento), date_part('MONTH', nsp.data_pagamento), f.tipo
ORDER BY	date_part('YEAR', nsp.data_pagamento), date_part('MONTH', nsp.data_pagamento), n.codigo_centro_financeiro, f.tipo;

/* Set owner */
ALTER TABLE public.vw_custos_receita_realizada
  OWNER TO sgi;

/* Comment */
COMMENT ON VIEW public.vw_custos_receita_realizada
  IS 'Atualizada SGINV-1913 / versão 5.11.13';
--select mes_pagamento, sum(valor_liquido) from vw_custos_receita_realizada where ano_pagamento = 2018 group by mes_pagamento

/* vw_custos_gastos_previstos - Gráfico "Custos" - "Realizado (NF, NP e mão de obra)" */
-- DROP MATERIALIZED VIEW vw_custos_gastos_previstos;
CREATE MATERIALIZED VIEW vw_custos_gastos_previstos AS
SELECT	n.codigo, cf.codigo_unidade, u.codigo_matriz, date_part('YEAR', p.data_pagamento) AS ano_pagamento, date_part('MONTH', p.data_pagamento) AS mes_pagamento, d.valor AS valor_total_nf,
	(p.valor + p.valor_multa + p.valor_juros - valor_desconto - valor_devolucao) AS valor_liquido_pagamento, rir.codigo_centro_financeiro, cf.codigo_pai AS codigo_gerencia,
	ROUND(SUM(ir.preco * rir.quantidade), 2) AS valor_recebimento, (d.valor + nf.desconto - nf.valor_frete - nf.valor_seguro - nf.valor_outras_despesas - nf.valor_ipi) AS total_itens,
	(CASE WHEN tc.chave = 'CONTRATO' THEN 0 WHEN tc.chave = 'CONTRATO_REEMBOLSAVEL' THEN 0 ELSE 1 END) AS tipo_faturamento
FROM	nota n, nota_fiscal nf, despesa d, parcela_nota pn, pagamento p, recebimento rec, item_recebimento ir, rateio_item_recebimento rir, centro_financeiro cf, unidade u, tipo_cobranca tc
WHERE	nf.codigo = n.codigo
AND	d.codigo = n.codigo
AND	n.codigo = pn.codigo_nota
AND	pn.codigo = p.codigo
AND	rec.codigo_nota_fiscal = nf.codigo
AND	ir.codigo_recebimento = rec.codigo
AND	rir.codigo_item_recebimento = ir.codigo
AND	cf.codigo = rir.codigo_centro_financeiro
AND	u.codigo = cf.codigo_unidade
AND	tc.codigo = nf.codigo_tipo_cobranca
AND	ir.preco > 0::numeric
GROUP BY	n.codigo, cf.codigo_unidade, u.codigo_matriz, date_part('YEAR', p.data_pagamento), date_part('MONTH', p.data_pagamento), d.valor, nf.valor_frete, nf.valor_seguro, nf.desconto,
		nf.valor_outras_despesas, nf.valor_ipi, p.valor, (p.valor + p.valor_multa + p.valor_juros - valor_desconto - valor_devolucao), rir.codigo_centro_financeiro, cf.codigo_pai, tc.chave

UNION

SELECT	n.codigo, cf.codigo_unidade, u.codigo_matriz, date_part('YEAR', p.data_pagamento) AS ano_pagamento, date_part('MONTH', p.data_pagamento) AS mes_pagamento, d.valor AS valor_total_nf,
	(p.valor + p.valor_multa + p.valor_juros - valor_desconto - valor_devolucao) AS valor_liquido_pagamento, rnf.codigo_centro_financeiro,
	COALESCE(cf.codigo_pai, rnf.codigo_centro_financeiro) AS codigo_gerencia, ROUND(SUM(rnf.valor),2) AS valor_recebimento,
	(d.valor + nf.desconto - nf.valor_frete - nf.valor_seguro - nf.valor_outras_despesas - nf.valor_ipi) AS total_itens,
	(CASE WHEN tc.chave = 'CONTRATO' THEN 0 WHEN tc.chave = 'CONTRATO_REEMBOLSAVEL' THEN 0 ELSE 1 END) AS tipo_faturamento
FROM	nota n, nota_fiscal nf, despesa d, parcela_nota pn, pagamento p, item_nota_fiscal inf, rateio_nota_fiscal rnf, centro_financeiro cf, unidade u, tipo_cobranca tc
WHERE	nf.codigo = n.codigo
AND	d.codigo = n.codigo
AND	n.codigo = pn.codigo_nota
AND	pn.codigo = p.codigo
AND	inf.codigo_pagamento = nf.codigo
AND rnf.codigo_item_nota_fiscal = inf.codigo
AND	cf.codigo = rnf.codigo_centro_financeiro
AND	u.codigo = cf.codigo_unidade
AND	tc.codigo = nf.codigo_tipo_cobranca
GROUP BY	n.codigo, cf.codigo_unidade, u.codigo_matriz, date_part('YEAR', p.data_pagamento), date_part('MONTH', p.data_pagamento), d.valor, nf.valor_frete, nf.valor_seguro, nf.desconto, nf.valor_outras_despesas, nf.valor_ipi,
		p.valor, (p.valor + p.valor_multa + p.valor_juros - valor_desconto - valor_devolucao), rnf.codigo_centro_financeiro, cf.codigo_pai, tc.chave

UNION

SELECT	n.codigo, cf.codigo_unidade, u.codigo_matriz, date_part('YEAR', p.data_pagamento) AS ano_pagamento, date_part('MONTH', p.data_pagamento) AS mes_pagamento, d.valor AS valor_total_nf,
	(p.valor + p.valor_multa + p.valor_juros - valor_desconto) AS valor_liquido_pagamento, rnp.codigo_centro_financeiro, COALESCE(cf.codigo_pai, rnp.codigo_centro_financeiro) AS codigo_gerencia, round(sum(rnp.valor),2) AS valor_recebimento,
	d.valor AS total_itens, 0 AS tipo_faturamento
FROM	nota n, nota_pagamento np, despesa d, parcela_nota pn, pagamento p, rateio_nota_pagamento rnp, centro_financeiro cf, unidade u
WHERE	np.codigo = n.codigo
AND	d.codigo = n.codigo
AND	n.codigo = pn.codigo_nota
AND	pn.codigo = p.codigo
AND rnp.codigo_nota_pagamento = np.codigo
AND	cf.codigo = rnp.codigo_centro_financeiro
AND	u.codigo = cf.codigo_unidade
GROUP BY	n.codigo, cf.codigo_unidade, u.codigo_matriz, date_part('YEAR', p.data_pagamento), date_part('MONTH', p.data_pagamento), d.valor, p.valor, (p.valor + p.valor_multa + p.valor_juros - valor_desconto),
		rnp.codigo_centro_financeiro, cf.codigo_pai

ORDER BY	1, 10 DESC, 8;

/* Set owner */
ALTER TABLE public.vw_custos_gastos_previstos
  OWNER TO sgi;

/* Comment */
COMMENT ON MATERIALIZED VIEW public.vw_custos_gastos_previstos
  IS 'Atualizada SGINV-1913 / versão 5.11.13';

/*
SELECT	n.codigo, cf.codigo_unidade, u.codigo_matriz, date_part('YEAR', p.data_pagamento) AS ano_pagamento, date_part('MONTH', p.data_pagamento) AS mes_pagamento, d.valor AS valor_total_nf,
	(p.valor + p.valor_multa + p.valor_juros - valor_desconto - valor_devolucao) AS valor_liquido_pagamento, rir.codigo_centro_financeiro, cf.codigo_pai AS codigo_gerencia, round(sum(ir.preco * rir.quantidade),2) AS valor_recebimento,
	(d.valor + nf.desconto - nf.valor_frete - nf.valor_seguro - nf.valor_outras_despesas - nf.valor_ipi) AS total_itens, 0 AS tipo_faturamento
FROM	nota n, nota_fiscal nf, despesa d, parcela_nota pn, pagamento p, recebimento rec, item_recebimento ir, rateio_item_recebimento rir, centro_financeiro cf, unidade u
WHERE	nf.codigo = n.codigo
AND	d.codigo = n.codigo
AND	n.codigo = pn.codigo_nota
AND	pn.codigo = p.codigo
AND	rec.codigo_nota_fiscal = nf.codigo
AND	ir.codigo_recebimento = rec.codigo
AND	rir.codigo_item_recebimento = ir.codigo
AND	cf.codigo = rir.codigo_centro_financeiro
AND	u.codigo = cf.codigo_unidade
AND	ir.preco > 0::numeric
AND	date_part('YEAR', p.data_pagamento) = 2018
AND	date_part('MONTH', p.data_pagamento) = 9
--AND	nf.desconto > 0
--AND	(p.valor_multa > 0 OR p.valor_juros > 0) valor_desconto valor_devolucao
--AND n.codigo = 567740
AND rir.codigo_centro_financeiro = 591583
GROUP BY	n.codigo, cf.codigo_unidade, u.codigo_matriz, date_part('YEAR', p.data_pagamento), date_part('MONTH', p.data_pagamento), d.valor, nf.valor_frete, nf.valor_seguro, nf.desconto, nf.valor_outras_despesas, nf.valor_ipi,
		p.valor, (p.valor + p.valor_multa + p.valor_juros - valor_desconto - valor_devolucao), rir.codigo_centro_financeiro, cf.codigo_pai
ORDER BY	1, 10 DESC, 8
limit 200

SELECT	n.codigo, cf.codigo_unidade, u.codigo_matriz, date_part('YEAR', p.data_pagamento) AS ano_pagamento, date_part('MONTH', p.data_pagamento) AS mes_pagamento, d.valor AS valor_total_nf,
	(p.valor + p.valor_multa + p.valor_juros - valor_desconto - valor_devolucao) AS valor_liquido_pagamento, rnf.codigo_centro_financeiro, COALESCE(cf.codigo_pai, rnf.codigo_centro_financeiro) AS codigo_gerencia, round(sum(rnf.valor),2) AS valor_recebimento,
	(d.valor + nf.desconto - nf.valor_frete - nf.valor_seguro - nf.valor_outras_despesas - nf.valor_ipi) AS total_itens, 0 AS tipo_faturamento
FROM	nota n, nota_fiscal nf, despesa d, parcela_nota pn, pagamento p, item_nota_fiscal inf, rateio_nota_fiscal rnf, centro_financeiro cf, unidade u
WHERE	nf.codigo = n.codigo
AND	d.codigo = n.codigo
AND	n.codigo = pn.codigo_nota
AND	pn.codigo = p.codigo
AND	inf.codigo_pagamento = nf.codigo
AND 	rnf.codigo_item_nota_fiscal = inf.codigo
AND	cf.codigo = rnf.codigo_centro_financeiro
AND	u.codigo = cf.codigo_unidade
AND	date_part('YEAR', p.data_pagamento) = 2018
AND	date_part('MONTH', p.data_pagamento) = 9
--AND	nf.desconto > 0
--AND	(p.valor_multa > 0 OR p.valor_juros > 0) valor_desconto valor_devolucao
--AND n.codigo = 567740
--AND rnf.codigo_centro_financeiro = 591583
GROUP BY	n.codigo, cf.codigo_unidade, u.codigo_matriz, date_part('YEAR', p.data_pagamento), date_part('MONTH', p.data_pagamento), d.valor, nf.valor_frete, nf.valor_seguro, nf.desconto, nf.valor_outras_despesas, nf.valor_ipi,
		p.valor, (p.valor + p.valor_multa + p.valor_juros - valor_desconto - valor_devolucao), rnf.codigo_centro_financeiro, cf.codigo_pai
ORDER BY	1, 10 DESC, 8
limit 200

SELECT	n.codigo, cf.codigo_unidade, u.codigo_matriz, date_part('YEAR', p.data_pagamento) AS ano_pagamento, date_part('MONTH', p.data_pagamento) AS mes_pagamento, d.valor AS valor_total_nf,
	(p.valor + p.valor_multa + p.valor_juros - valor_desconto) AS valor_liquido_pagamento, rnp.codigo_centro_financeiro, COALESCE(cf.codigo_pai, rnp.codigo_centro_financeiro) AS codigo_gerencia, round(sum(rnp.valor),2) AS valor_recebimento,
	d.valor AS total_itens, 0 AS tipo_faturamento
FROM	nota n, nota_pagamento np, despesa d, parcela_nota pn, pagamento p, rateio_nota_pagamento rnp, centro_financeiro cf, unidade u
WHERE	np.codigo = n.codigo
AND	d.codigo = n.codigo
AND	n.codigo = pn.codigo_nota
AND	pn.codigo = p.codigo
AND 	rnp.codigo_nota_pagamento = np.codigo
AND	cf.codigo = rnp.codigo_centro_financeiro
AND	u.codigo = cf.codigo_unidade
AND	date_part('YEAR', p.data_pagamento) = 2018
AND	date_part('MONTH', p.data_pagamento) = 9
--AND	nf.desconto > 0
--AND	(p.valor_multa > 0 OR p.valor_juros > 0) valor_desconto valor_devolucao
--AND n.codigo = 567740
--AND rnf.codigo_centro_financeiro = 591583
GROUP BY	n.codigo, cf.codigo_unidade, u.codigo_matriz, date_part('YEAR', p.data_pagamento), date_part('MONTH', p.data_pagamento), d.valor, p.valor, (p.valor + p.valor_multa + p.valor_juros - valor_desconto),
		rnp.codigo_centro_financeiro, cf.codigo_pai
ORDER BY	1, 10 DESC, 8
limit 200

*/

/* ---------------------------------------------------------------------- */
/* Create cron schedule                                                   */
/* ---------------------------------------------------------------------- */
-- SELECT cron.schedule('0 7 * * 2-6', 'REFRESH MATERIALIZED VIEW vw_custos_gastos_previstos');
