/* ---------------------------------------------------------------------- */
/* Ajustar as notas fiscais (tipo de fornecimento - Serviço ou Serv/Mat)  */
/* ---------------------------------------------------------------------- */
UPDATE	nota_fiscal nf
SET		tipo_fornecimento = 1
FROM	nota n, nota_saida ns, faturamento_etapa fe, faturamento f
WHERE	nf.codigo = n.codigo
AND		ns.codigo = nf.codigo
AND		fe.codigo_nota_saida = ns.codigo
AND		fe.codigo_faturamento = f.codigo
AND		nf.tipo_fornecimento IS NULL
AND		f.percentual_servico = 100;

UPDATE	nota_fiscal nf
SET		tipo_fornecimento = 2
FROM	nota n, nota_saida ns, faturamento_etapa fe, faturamento f
WHERE	nf.codigo = n.codigo
AND		ns.codigo = nf.codigo
AND		fe.codigo_nota_saida = ns.codigo
AND		fe.codigo_faturamento = f.codigo
AND		nf.tipo_fornecimento IS NULL
AND		f.percentual_servico < 100;
