/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2019-02-12 12:00                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Modify table "unidade"                                                 */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.unidade
	ADD proxima_nf		INTEGER		DEFAULT 1	NOT NULL,
	ADD ano_proxima_nf	SMALLINT	DEFAULT 0	NOT NULL;

/* Comments */
COMMENT ON COLUMN public.unidade.codigo					IS 'Código interno da unidade (empresa/filial - código interno da pessoa).';
COMMENT ON COLUMN public.unidade.codigo_matriz			IS 'Código interno da matriz (autorelacionamento, se filial) ou nulo se for uma matriz.';
COMMENT ON COLUMN public.unidade.zerar_numeracao_nf_ano	IS 'Indica se a numeração da nota fiscal de saída (faturamento) da unidade será reiniciada a cada ano de emissão (true) ou não (false).';
COMMENT ON COLUMN public.unidade.mascara_conta_contabil	IS 'Máscara para o número das contas contábeis da unidade';
COMMENT ON COLUMN public.unidade.emite_nf_faturamento	IS 'Indica se será possível emitir NF/Faturamento para a unidade (true) ou não (false).';
COMMENT ON COLUMN public.unidade.proxima_nf				IS 'Número da próxima NF associada à unidade. O número da NF dependerá também se a numeração FOR zerada a cada ano (campo "zerar_numeracao_nf_ano") e, neste caso, do campo "proxima_nf_ano".';
COMMENT ON COLUMN public.unidade.ano_proxima_nf			IS 'Ano do controle para o número da próxima NF associada à unidade. Se a numeração NÃO FOR zerada a cada ano (conforme o campo "zerar_numeracao_nf_ano") deve ser ignorado (conterá zero).';

/* Update data */
UPDATE	unidade
SET		ano_proxima_nf = 2019,
		proxima_nf = (
			SELECT	CAST(MAX(RIGHT(n.numero,9)) AS INTEGER) + 1
			FROM	nota n, nota_fiscal nf
			WHERE	n.codigo = nf.codigo
			AND		nf.tipo_nota_fiscal = 11
			AND		DATE_PART('YEAR', nf.data_emissao) = 2019
			AND		n.codigo_empresa = unidade.codigo
			)
WHERE	emite_nf_faturamento
AND		zerar_numeracao_nf_ano;

UPDATE	unidade
SET		proxima_nf = (
			SELECT	CAST(MAX(RIGHT(n.numero,9)) AS INTEGER) + 1
			FROM	nota n, nota_fiscal nf
			WHERE	n.codigo = nf.codigo
			AND		nf.tipo_nota_fiscal = 11
			AND		n.codigo_empresa = unidade.codigo
			)
WHERE	emite_nf_faturamento
AND		NOT zerar_numeracao_nf_ano;
