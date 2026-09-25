/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2019-08-21 17:04                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop views                                                             */
/* ---------------------------------------------------------------------- */
DROP MATERIALIZED VIEW IF EXISTS "vw_compras_itens_por_natureza";
DROP VIEW IF EXISTS "vw_custos_faturamento_previsto";
DROP VIEW IF EXISTS "vw_custos_faturamento_realizado";
DROP MATERIALIZED VIEW IF EXISTS "vw_custos_gastos_previstos";
DROP VIEW IF EXISTS "vw_custos_receita_prevista";
DROP VIEW IF EXISTS "vw_custos_receita_realizada";
DROP MATERIALIZED VIEW IF EXISTS "vw_total_credito_por_nf";
DROP MATERIALIZED VIEW IF EXISTS "vw_inrf_itens_nota_fiscal";
DROP VIEW IF EXISTS "vw_inrf_notas_validas";
DROP VIEW IF EXISTS "vw_inrf_relatorio_notas";
DROP VIEW IF EXISTS "vw_lista_nf_confrontamento";

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.centro_financeiro				DROP CONSTRAINT fk_centro_financeiro_centro_custo;
ALTER TABLE public.centro_financeiro				DROP CONSTRAINT fk_centro_financeiro_contato_aae;
ALTER TABLE public.centro_financeiro				DROP CONSTRAINT fk_centro_financeiro_contrato;
ALTER TABLE public.centro_financeiro				DROP CONSTRAINT fk_centro_financeiro_engenheiro_responsavel;
ALTER TABLE public.centro_financeiro				DROP CONSTRAINT fk_centro_financeiro_fluxo;
ALTER TABLE public.centro_financeiro				DROP CONSTRAINT fk_centro_financeiro_unidade;

ALTER TABLE public.gerencia							DROP CONSTRAINT fk_gerencia_centro_financeiro;

ALTER TABLE public.colaborador						DROP CONSTRAINT fk_colaborador_centro_financeiro;

ALTER TABLE public.fundo_fixo						DROP CONSTRAINT fk_fundo_fixo_centro_financeiro;

ALTER TABLE public.nota								DROP CONSTRAINT fk_nota_centro_financeiro;

ALTER TABLE public.transacao						DROP CONSTRAINT fk_transacao_centro_financeiro;

ALTER TABLE public.ordem_compra						DROP CONSTRAINT fk_ordem_compra_centro_financeiro;

ALTER TABLE public.pagamento						DROP CONSTRAINT fk_pagamento_centro_financeiro;

ALTER TABLE public.requisicao_compra				DROP CONSTRAINT fk_requisicao_compra_centro_financeiro;

ALTER TABLE public.rateio_nota_fiscal				DROP CONSTRAINT fk_rateio_nota_fiscal_centro_financeiro;

ALTER TABLE public.colaborador_centro_financeiro	DROP CONSTRAINT fk_colaborador_centro_financeiro_centro_financeiro;

ALTER TABLE public.rateio_nota_pagamento			DROP CONSTRAINT fk_rateio_nota_pagamento_centro_financeiro;

ALTER TABLE public.relacao_nf						DROP CONSTRAINT fk_relacao_nf_centro_financeiro;

ALTER TABLE public.mensagem_centro_financeiro		DROP CONSTRAINT fk_mensagem_centro_financeiro_centro_financeiro;

ALTER TABLE public.fecho							DROP CONSTRAINT fk_fecho_centro_financeiro;

ALTER TABLE public.centro_financeiro_endereco		DROP CONSTRAINT fk_centro_financeiro_endereco_centro_financeiro;

ALTER TABLE public.centro_financeiro_fluxo_etapa	DROP CONSTRAINT fk_centro_financeiro_fluxo_etapa_centro_financeiro;

ALTER TABLE public.motivo_retorno_documento_cf		DROP CONSTRAINT fk_motivo_retorno_documento_cf_centro_financeiro;

ALTER TABLE public.centro_financeiro_situacao		DROP CONSTRAINT fk_centro_financeiro_situacao_centro_financeiro;

ALTER TABLE public.rateio_item_recebimento			DROP CONSTRAINT fk_rateio_item_recebimento_centro_financeiro;

ALTER TABLE public.centro_financeiro_aliquota		DROP CONSTRAINT fk_centro_financeiro_aliquota_centro_financeiro;

ALTER TABLE public.faturamento						DROP CONSTRAINT fk_faturamento_centro_financeiro;

ALTER TABLE public.mensagem_usuario					DROP CONSTRAINT fk_mensagem_usuario_centro_financeiro;

ALTER TABLE public.lote_inativacao_registro			DROP CONSTRAINT fk_lote_inativacao_registro_centro_financeiro;

ALTER TABLE public.centro_financeiro_previsao		DROP CONSTRAINT fk_centro_financeiro_previsao_centro_financeiro;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "centro_financeiro"                            */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.centro_financeiro DROP CONSTRAINT pk_centro_financeiro;

ALTER TABLE public.centro_financeiro DROP CONSTRAINT ck_centro_financeiro_garantias;
ALTER TABLE public.centro_financeiro DROP CONSTRAINT ck_centro_financeiro_situacao;
ALTER TABLE public.centro_financeiro DROP CONSTRAINT ck_centro_financeiro_tipo;

/* Create "new" table */
CREATE TABLE public.centro_financeiro_TMP (
    codigo							BIGINT					DEFAULT nextval('seq_centro_financeiro')	NOT NULL,
    codigo_unidade					BIGINT					DEFAULT 1									NOT NULL,
    codigo_pai						BIGINT,
    codigo_contrato					BIGINT,
    codigo_engenheiro_responsavel	BIGINT,
    codigo_contato_aae				BIGINT,
    codigo_fluxo					BIGINT,
    nome							CHARACTER VARYING(100)												NOT NULL,
    identificador					CHARACTER VARYING(11)												NOT NULL,
    tipo							SMALLINT				DEFAULT 1									NOT NULL,
    situacao						SMALLINT				DEFAULT 1									NOT NULL,
    proxima_oc						INTEGER					DEFAULT 1									NOT NULL,
    escopo_compra					SMALLINT,
    cumulativo						BOOLEAN,
    engenheiro_responsavel			CHARACTER VARYING(100),
    forma_pagamento					CHARACTER VARYING(100),
    garantias						SMALLINT				DEFAULT 0,
    criterio_medicao				CHARACTER VARYING(100),
    data_previsao_reajuste			DATE,
    total_hh						NUMERIC(9),
    cei								CHARACTER VARYING(15),
    observacao						CHARACTER VARYING(2000),
    data_previsao_primeira_parcela	DATE,
    data_encerramento				DATE,
    criador							CHARACTER VARYING(255),
    data_criacao					TIMESTAMP,
    atualizador						CHARACTER VARYING(255),
    data_atualizacao				TIMESTAMP);

/* Move data from "old" table to "new" table */
INSERT INTO public.centro_financeiro_TMP
    (codigo,codigo_unidade,codigo_pai,codigo_contrato,codigo_engenheiro_responsavel,codigo_contato_aae,codigo_fluxo,nome,identificador,tipo,situacao,proxima_oc,escopo_compra,cumulativo,engenheiro_responsavel,forma_pagamento,garantias,criterio_medicao,data_previsao_reajuste,total_hh,cei,observacao,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_unidade,codigo_pai,codigo_contrato,codigo_engenheiro_responsavel,codigo_contato_aae,codigo_fluxo,nome,identificador,tipo,situacao,proxima_oc,escopo_compra,cumulativo,engenheiro_responsavel,forma_pagamento,garantias,criterio_medicao,data_previsao_reajuste,total_hh,cei,observacao,criador,data_criacao,atualizador,data_atualizacao
FROM public.centro_financeiro;

/* Drop indexes  */
DROP INDEX public.ix_centro_financeiro_contato_aae;
DROP INDEX public.ix_centro_financeiro_contrato;
DROP INDEX public.ix_centro_financeiro_cumulativo;
DROP INDEX public.ix_centro_financeiro_engenheiro_responsavel;
DROP INDEX public.ix_centro_financeiro_fluxo;
DROP INDEX public.ix_centro_financeiro_identificador;
DROP INDEX public.ix_centro_financeiro_pai;
DROP INDEX public.ix_centro_financeiro_tipo;
DROP INDEX public.ix_centro_financeiro_unidade;

/* Drop "old" table  */
DROP TABLE public.centro_financeiro;

/* Rename "old" table to "new" table */
ALTER TABLE public.centro_financeiro_TMP
	RENAME TO centro_financeiro;

/* Create primary key */
ALTER TABLE public.centro_financeiro
	ADD CONSTRAINT pk_centro_financeiro 
    PRIMARY KEY (codigo);

/* Create constraints  */
ALTER TABLE public.centro_financeiro
	ADD CONSTRAINT ck_centro_financeiro_garantias 
    CHECK (garantias = ANY (ARRAY[0, 1, 2, 3]));
ALTER TABLE public.centro_financeiro
	ADD CONSTRAINT ck_centro_financeiro_situacao 
    CHECK (situacao <= 17);
ALTER TABLE public.centro_financeiro
	ADD CONSTRAINT ck_centro_financeiro_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1]));

/* Create indexes  */
CREATE INDEX ix_centro_financeiro_unidade					ON public.centro_financeiro (codigo_unidade);
CREATE INDEX ix_centro_financeiro_pai						ON public.centro_financeiro (codigo_pai);
CREATE INDEX ix_centro_financeiro_contrato					ON public.centro_financeiro (codigo_contrato);
CREATE INDEX ix_centro_financeiro_engenheiro_responsavel	ON public.centro_financeiro (codigo_engenheiro_responsavel);
CREATE INDEX ix_centro_financeiro_contato_aae				ON public.centro_financeiro (codigo_contato_aae);
CREATE INDEX ix_centro_financeiro_fluxo						ON public.centro_financeiro (codigo_fluxo);
CREATE INDEX ix_centro_financeiro_identificador				ON public.centro_financeiro (identificador);
CREATE INDEX ix_centro_financeiro_tipo						ON public.centro_financeiro (tipo);
CREATE INDEX ix_centro_financeiro_cumulativo				ON public.centro_financeiro (cumulativo);

/* Comments */
COMMENT ON TABLE public.centro_financeiro									IS 'Centros financeiros (centro de custo = gerência / centro de lucro = contrato).';
COMMENT ON COLUMN public.centro_financeiro.codigo							IS 'Código interno do centro financeiro. Sequência: seq_centro_financeiro';
COMMENT ON COLUMN public.centro_financeiro.codigo_unidade					IS 'Código interno da unidade (código interno da pessoa) a qual o centro financeiro está vinculado.';
COMMENT ON COLUMN public.centro_financeiro.codigo_pai						IS 'Código interno do centro financeiro pai (autorrelacionamento). Se houver autorrelacionamento, significa que o centro financeiro é um contrato (tem pai, que é uma gerência), senão é uma gerência.';
COMMENT ON COLUMN public.centro_financeiro.codigo_contrato					IS 'Código do contrato a qual este centro financeiro (centro de lucro) pertence.';
COMMENT ON COLUMN public.centro_financeiro.codigo_engenheiro_responsavel	IS 'Código interno (código interno do colaborador) do engenhiero reponsável pelo centro financeiro. Utilizado para o caso do engenheiro ser um colaborador já cadastrado.';
COMMENT ON COLUMN public.centro_financeiro.codigo_contato_aae				IS 'Código interno (código interno do colaborador) do contato (ponto focal) do centro financeiro.';
COMMENT ON COLUMN public.centro_financeiro.codigo_fluxo						IS 'Código interno do fluxo de assinaturas.';
COMMENT ON COLUMN public.centro_financeiro.nome								IS 'Nome do centro financeiro.';
COMMENT ON COLUMN public.centro_financeiro.identificador					IS 'Identificador (XX.YYY.ZZZZ, onde XX é a diretoria, YYY é a gerência e ZZZZ o código único do centro financeiro).';
COMMENT ON COLUMN public.centro_financeiro.tipo								IS 'Tipo do centro financeiro: 0 = gerência / 1 = contrato.';
COMMENT ON COLUMN public.centro_financeiro.situacao							IS 'Situação: 0 = Inativo / 1 = Ativo / 2 = Em elaboração / 3 = Em revisão / 4 = Em análise - Gerência / 5 = Em análise - Diretoria / 6 = Em análise - RH / 7 = Em análise - Financeiro / 8 = Em análise - Contabilidade / 9 = Em análise - Comercial / 10 = Em análise - Presidência / 11 = Encerrado / 12 = Cancelado / 13 = Paralisado / 14 = Reativado (uso interno) / 15 = Criado (uso interno) / 16 = Gestão (fluxo em paralelo) / 17 = Encerrado, mas com faturamento. Se não estiver associado a um contrato só estão disponíveis 0 e 1.';
COMMENT ON COLUMN public.centro_financeiro.proxima_oc						IS 'Número da próxima OC associada ao centro financeiro. O número de cada OC será "CCCC/NNN", onde "CCCC" são os 4 últimos digitos do identificados do centro financeiro e "NNN" o número sequencial da OC, conforme este campo (preenchido com zeros à esquerda).';
COMMENT ON COLUMN public.centro_financeiro.escopo_compra					IS 'Escopo da compra (sem uso).';
COMMENT ON COLUMN public.centro_financeiro.cumulativo						IS 'Indica se o centro financeiro é não cumulativo (false) ou cumulativo (true).';
COMMENT ON COLUMN public.centro_financeiro.engenheiro_responsavel			IS 'Nome do engenheiro responsável pelo centro financeiro (se contrato e não houver ligação com uma pessoa).';
COMMENT ON COLUMN public.centro_financeiro.forma_pagamento					IS 'Forma de pagamento ("Até o último dia do mês", "5 dias após a entrega da fatura", etc.)';
COMMENT ON COLUMN public.centro_financeiro.garantias						IS 'Garantias para o contrato: 0 = Não exigidas / 1 = Fiança bancária / 2 = Caução / 3 = Seguro-garantia';
COMMENT ON COLUMN public.centro_financeiro.criterio_medicao					IS 'Critério de medição deste centro financeiro para aprovação e faturamento do contrato.';
COMMENT ON COLUMN public.centro_financeiro.data_previsao_reajuste			IS 'Data de previsão de reajuste para este centro financeiro.';
COMMENT ON COLUMN public.centro_financeiro.total_hh							IS 'Total de HH dos serviços do centro financeiro.';
COMMENT ON COLUMN public.centro_financeiro.cei								IS 'Matrícula no Cadastro Específico do INSS (matrícula CEI/código da obra).';
COMMENT ON COLUMN public.centro_financeiro.observacao						IS 'Observações.';
COMMENT ON COLUMN public.centro_financeiro.data_previsao_primeira_parcela	IS 'Data de previsão da emissão da 1ª parcela (I-0).';
COMMENT ON COLUMN public.centro_financeiro.data_encerramento				IS 'Data de encerramento (sem mais nenhum faturamento).';
COMMENT ON COLUMN public.centro_financeiro.criador							IS 'Responsável (login) pela criação do centro financeiro.';
COMMENT ON COLUMN public.centro_financeiro.data_criacao						IS 'Data/hora de criação do centro financeiro.';
COMMENT ON COLUMN public.centro_financeiro.atualizador						IS 'Responsável (login) pela última atualização do centro financeiro.';
COMMENT ON COLUMN public.centro_financeiro.data_atualizacao					IS 'Data/hora da última atualização do centro financeiro.';

/* Set owner */
ALTER TABLE public.centro_financeiro OWNER TO sgi;

/* Set data */
--Data de previsão do 1º faturamento
UPDATE	centro_financeiro cf
SET		data_previsao_primeira_parcela =	(	SELECT		MAX(cfs.data_situacao)
												FROM		centro_financeiro_situacao cfs
												WHERE		cf.codigo = cfs.codigo_centro_financeiro
												AND			cfs.situacao = 1
												GROUP BY	cfs.codigo_centro_financeiro
											)
WHERE	cf.situacao IN (1, 11, 12, 13, 17)
AND		codigo_contrato IS NOT NULL;

UPDATE	centro_financeiro
SET		data_previsao_primeira_parcela = '2019-01-01'
WHERE	codigo_contrato IS NOT NULL
AND		data_previsao_primeira_parcela IS NULL;

--Encerramento
UPDATE	centro_financeiro cf
SET		data_encerramento = (	SELECT		MAX(cfs.data_situacao)
								FROM		centro_financeiro_situacao cfs
								WHERE		cf.codigo = cfs.codigo_centro_financeiro
								AND			cfs.situacao = 11
								GROUP BY	cfs.codigo_centro_financeiro
							)
WHERE	codigo_contrato IS NOT NULL
AND		cf.situacao = 11;

/* ---------------------------------------------------------------------- */
/* Modify table "contrato"                                                */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.contrato
	-- DROP data_primeira_parcela,
	DROP resumo_faturamento;

/*
ALTER TABLE public.contrato
	ADD COLUMN data_primeira_parcela date;

COMMENT ON COLUMN public.contrato.data_primeira_parcela IS 'Data de vencimento da 1ª parcela do contrato.';
*/

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.centro_financeiro				ADD CONSTRAINT fk_centro_financeiro_centro_custo 
    FOREIGN KEY (codigo_pai)						REFERENCES public.centro_financeiro (codigo);
ALTER TABLE public.centro_financeiro				ADD CONSTRAINT fk_centro_financeiro_unidade 
    FOREIGN KEY (codigo_unidade)					REFERENCES public.unidade (codigo);
ALTER TABLE public.centro_financeiro				ADD CONSTRAINT fk_centro_financeiro_contrato 
    FOREIGN KEY (codigo_contrato)					REFERENCES public.contrato (codigo);
ALTER TABLE public.centro_financeiro				ADD CONSTRAINT fk_centro_financeiro_contato_aae 
    FOREIGN KEY (codigo_contato_aae)				REFERENCES public.colaborador (codigo);
ALTER TABLE public.centro_financeiro				ADD CONSTRAINT fk_centro_financeiro_engenheiro_responsavel 
    FOREIGN KEY (codigo_engenheiro_responsavel)		REFERENCES public.colaborador (codigo);
ALTER TABLE public.centro_financeiro				ADD CONSTRAINT fk_centro_financeiro_fluxo 
    FOREIGN KEY (codigo_fluxo)						REFERENCES public.fluxo (codigo);

ALTER TABLE public.gerencia							ADD CONSTRAINT fk_gerencia_centro_financeiro 
    FOREIGN KEY (codigo)							REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.colaborador						ADD CONSTRAINT fk_colaborador_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.fundo_fixo						ADD CONSTRAINT fk_fundo_fixo_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.nota								ADD CONSTRAINT fk_nota_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.transacao						ADD CONSTRAINT fk_transacao_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.ordem_compra						ADD CONSTRAINT fk_ordem_compra_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.pagamento						ADD CONSTRAINT fk_pagamento_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.requisicao_compra				ADD CONSTRAINT fk_requisicao_compra_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.rateio_nota_fiscal				ADD CONSTRAINT fk_rateio_nota_fiscal_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.colaborador_centro_financeiro	ADD CONSTRAINT fk_colaborador_centro_financeiro_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.rateio_nota_pagamento			ADD CONSTRAINT fk_rateio_nota_pagamento_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.relacao_nf						ADD CONSTRAINT fk_relacao_nf_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.mensagem_centro_financeiro		ADD CONSTRAINT fk_mensagem_centro_financeiro_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.fecho							ADD CONSTRAINT fk_fecho_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.centro_financeiro_endereco		ADD CONSTRAINT fk_centro_financeiro_endereco_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.centro_financeiro_fluxo_etapa	ADD CONSTRAINT fk_centro_financeiro_fluxo_etapa_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.motivo_retorno_documento_cf		ADD CONSTRAINT fk_motivo_retorno_documento_cf_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.centro_financeiro_situacao		ADD CONSTRAINT fk_centro_financeiro_situacao_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.rateio_item_recebimento			ADD CONSTRAINT fk_rateio_item_recebimento_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.centro_financeiro_aliquota		ADD CONSTRAINT fk_centro_financeiro_aliquota_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.faturamento						ADD CONSTRAINT fk_faturamento_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.mensagem_usuario					ADD CONSTRAINT fk_mensagem_usuario_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.lote_inativacao_registro			ADD CONSTRAINT fk_lote_inativacao_registro_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

ALTER TABLE public.centro_financeiro_previsao		ADD CONSTRAINT fk_centro_financeiro_previsao_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)			REFERENCES public.centro_financeiro (codigo);

/* ---------------------------------------------------------------------- */
/* Controle de acesso                                                     */
/* ---------------------------------------------------------------------- */
/* Criar novos perfis */
INSERT
	INTO	receptor (codigo, tipo, criador, data_criacao, atualizador, data_atualizacao)
	VALUES	(nextval('seq_receptor'), 0, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT
	INTO	perfil (codigo, tipo_acesso, nome, descricao, obriga_gerencia, obriga_banco)
	VALUES	(currval('seq_receptor'), 0, 'comercial-cns-geral', 'Comercial - Consulta geral', TRUE, FALSE);

INSERT
	INTO	receptor (codigo, tipo, criador, data_criacao, atualizador, data_atualizacao)
	VALUES	(nextval('seq_receptor'), 0, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT
	INTO	perfil (codigo, tipo_acesso, nome, descricao, obriga_gerencia, obriga_banco)
	VALUES	(currval('seq_receptor'), 0, 'comercial-cns-gerencia', 'Comercial - Consulta gerência', TRUE, FALSE);

/* Adicionar funcionalidade e ajustar módulo */
UPDATE	modulo m
SET		ordem = 30
FROM	modulo pai
WHERE	m.nome = 'Cadastros'
AND		m.codigo_modulo_pai = pai.codigo
AND		pai.nome = 'Comercial'
AND		m.codigo_sistema = 2
AND		pai.codigo_sistema = 2;

INSERT	INTO funcionalidade (codigo, nome, ordem, url, codigo_modulo)
SELECT	NEXTVAL('seq_funcionalidade'), 'Relatórios', 20, 'comercialRelatorios.relatorios', m.codigo
FROM	modulo m
WHERE	m.nome = 'Comercial'
AND		m.codigo_sistema = 2;

/* Adicionar acesso ao relatório */
INSERT
INTO	permissao (codigo, codigo_receptor, acao, alvo)
SELECT	NEXTVAL('seq_permissao'), perfil.codigo, 'render', 'comercialRelatorios.relatorios'
FROM	perfil
WHERE	perfil.nome IN ('analista-cf', 'admin-contrato', 'comercial-cns-geral', 'comercial-cns-gerencia');
