/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-06-10 12:40                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Create sequence                                                        */
/* ---------------------------------------------------------------------- */
CREATE SEQUENCE public.seq_tipo					INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE public.seq_situacao				INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE public.seq_faturamento_retencao	INCREMENT 1 MINVALUE 1 START 1;

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_resp_cancelamento;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_resp_recebimento;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_nota_saida;

/* ---------------------------------------------------------------------- */
/* Modify table "faturamento"                                             */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.faturamento
	DROP percentual_retencao;

/* ---------------------------------------------------------------------- */
/* Add table "tipo"                                                       */
/* ---------------------------------------------------------------------- */
/* Create table */
CREATE TABLE public.tipo (
    codigo				BIGINT DEFAULT nextval('seq_tipo')	NOT NULL,
    codigo_situacao		BIGINT,
    tabela				CHARACTER VARYING(100)				NOT NULL,
    subtipo				CHARACTER VARYING(50)				NOT NULL,
    chave				CHARACTER VARYING(10)				NOT NULL,
    ordem				SMALLINT							NOT NULL,
    nome				CHARACTER VARYING(20)				NOT NULL,
    descricao			CHARACTER VARYING(50)				NOT NULL,
    criador				CHARACTER VARYING(255),
    data_criacao		TIMESTAMP,
    atualizador			CHARACTER VARYING(255),
    data_atualizacao	TIMESTAMP,
    CONSTRAINT pk_tipo PRIMARY KEY (codigo)
);

/* Create indexes */
CREATE INDEX ix_tipo_situacao						ON public.tipo (codigo_situacao);
CREATE UNIQUE INDEX uq_tipo_tabela_subtipo_chave	ON public.tipo (tabela,subtipo,chave);

/* Comments */
COMMENT ON TABLE public.tipo					IS 'Tabela de tipos para as demais tabelas do modelo.';
COMMENT ON COLUMN public.tipo.codigo			IS 'Código interno do tipo. Sequência: seq_tipo.';
COMMENT ON COLUMN public.tipo.codigo_situacao	IS 'Código interno da situação do tipo.';
COMMENT ON COLUMN public.tipo.tabela			IS 'Tabela (modelo de dados) onde o tipo está associado (para fácil localização dos tipos de uma tabela/entidade).';
COMMENT ON COLUMN public.tipo.subtipo			IS 'Subtipo da tabela para identificar o tipo (se só houver um único tipo na tabela, usar o próprio nome da tabela).';
COMMENT ON COLUMN public.tipo.chave				IS 'Chave para localização do tipo.';
COMMENT ON COLUMN public.tipo.ordem				IS 'Ordem de exibição do tipo.';
COMMENT ON COLUMN public.tipo.nome				IS 'Nome (curto) do tipo.';
COMMENT ON COLUMN public.tipo.descricao			IS 'Descrição do tipo.';
COMMENT ON COLUMN public.tipo.criador			IS 'Responsável (login) pela criação do tipo.';
COMMENT ON COLUMN public.tipo.data_criacao		IS 'Data/hora de criação do tipo.';
COMMENT ON COLUMN public.tipo.atualizador		IS 'Responsável (login) pela última atualização do tipo.';
COMMENT ON COLUMN public.tipo.data_atualizacao	IS 'Data/hora da última atualização do tipo.';

/* Set owner */
ALTER TABLE public.tipo OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add table "situacao"                                                   */
/* ---------------------------------------------------------------------- */
/* Create table */
CREATE TABLE public.situacao (
    codigo				BIGINT DEFAULT nextval('seq_situacao')	NOT NULL,
    tabela				CHARACTER VARYING(100)					NOT NULL,
    subsituacao			CHARACTER VARYING(50)					NOT NULL,
    chave				CHARACTER VARYING(10)					NOT NULL,
    ordem				SMALLINT								NOT NULL,
    nome				CHARACTER VARYING(20)					NOT NULL,
    descricao			CHARACTER VARYING(50)					NOT NULL,
    permissao_editar	BOOLEAN DEFAULT false					NOT NULL,
    permissao_suspender	BOOLEAN DEFAULT false					NOT NULL,
    permissao_reativar	BOOLEAN DEFAULT false					NOT NULL,
    permissao_cancelar	BOOLEAN DEFAULT false					NOT NULL,
    criador				CHARACTER VARYING(255),
    data_criacao		TIMESTAMP,
    atualizador			CHARACTER VARYING(255),
    data_atualizacao	TIMESTAMP,
    CONSTRAINT pk_situacao PRIMARY KEY (codigo)
);

/* Create indexes */
CREATE UNIQUE INDEX uq_situacao_tabela_subsituacao_chave ON public.situacao (tabela,subsituacao,chave);

/* Comments */
COMMENT ON TABLE public.situacao						IS 'Tabela de situações para as demais tabelas do modelo.';
COMMENT ON COLUMN public.situacao.codigo				IS 'Código interno da situação. Sequência: seq_situacao.';
COMMENT ON COLUMN public.situacao.tabela				IS 'Tabela (modelo de dados) onde a situação está associada (para fácil localização das situações de uma tabela/entidade).';
COMMENT ON COLUMN public.situacao.subsituacao			IS 'Subsituacao da tabela para identificar a situação (se só houver uma única situacão na tabela, usar o próprio nome da tabela).';
COMMENT ON COLUMN public.situacao.chave					IS 'Chave para localização da situação.';
COMMENT ON COLUMN public.situacao.ordem					IS 'Ordem de exibição do tipo.';
COMMENT ON COLUMN public.situacao.nome					IS 'Nome (curto) da situação.';
COMMENT ON COLUMN public.situacao.descricao				IS 'Descrição da situação.';
COMMENT ON COLUMN public.situacao.permissao_editar		IS 'Indica se a situação permite que o objeto seja editado.';
COMMENT ON COLUMN public.situacao.permissao_suspender	IS 'Indica se a situação permite que o objeto seja suspenso.';
COMMENT ON COLUMN public.situacao.permissao_reativar	IS 'Indica se a situação permite que o objeto seja reativado.';
COMMENT ON COLUMN public.situacao.permissao_cancelar	IS 'Indica se a situação permite que o objeto seja cancelado.';
COMMENT ON COLUMN public.situacao.criador				IS 'Responsável (login) pela criação da situação.';
COMMENT ON COLUMN public.situacao.data_criacao			IS 'Data/hora de criação da situação.';
COMMENT ON COLUMN public.situacao.atualizador			IS 'Responsável (login) pela última atualização da situação.';
COMMENT ON COLUMN public.situacao.data_atualizacao		IS 'Data/hora da última atualização da situação.';

/* Set owner */
ALTER TABLE public.situacao OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add table "faturamento_retencao"                                       */
/* ---------------------------------------------------------------------- */
/* Create table */
CREATE TABLE public.faturamento_retencao (
    codigo							BIGINT DEFAULT nextval('seq_faturamento_retencao')	NOT NULL,
    codigo_faturamento				BIGINT												NOT NULL,
    codigo_tipo_retencao			BIGINT												NOT NULL,
    codigo_situacao					BIGINT												NOT NULL,
    codigo_responsavel_baixa		BIGINT,
    codigo_responsavel_cancelamento	BIGINT,
    percentual						NUMERIC(5,2)										NOT NULL,
    data_baixa						TIMESTAMP,
    data_cancelamento				TIMESTAMP,
    CONSTRAINT pk_faturamento_retencao PRIMARY KEY (codigo)
);

/* Create indexes */
CREATE INDEX ix_faturamento_retencao_faturamento				ON public.faturamento_retencao (codigo_faturamento);
CREATE INDEX ix_faturamento_retencao_tipo_retencao				ON public.faturamento_retencao (codigo_tipo_retencao);
CREATE INDEX ix_faturamento_retencao_situacao					ON public.faturamento_retencao (codigo_situacao);
CREATE INDEX ix_faturamento_retencao_responsavel_baixa			ON public.faturamento_retencao (codigo_responsavel_baixa);
CREATE INDEX ix_faturamento_retencao_responsavel_cancelamento	ON public.faturamento_retencao (codigo_responsavel_cancelamento);

/* Comments */
COMMENT ON TABLE public.faturamento_retencao									IS 'Retenções de faturamento.';
COMMENT ON COLUMN public.faturamento_retencao.codigo							IS 'Código interno da retenção do faturamento. Sequência: seq_faturamento_retencao.';
COMMENT ON COLUMN public.faturamento_retencao.codigo_faturamento				IS 'Código interno do faturamento onde ocorreu a retenção.';
COMMENT ON COLUMN public.faturamento_retencao.codigo_tipo_retencao				IS 'Código interno do tipo de retenção.';
COMMENT ON COLUMN public.faturamento_retencao.codigo_situacao					IS 'Código interno da situação da retenção.';
COMMENT ON COLUMN public.faturamento_retencao.codigo_responsavel_baixa			IS 'Código interno da pessoa responsável pela baixa (recebimento) da retenção.';
COMMENT ON COLUMN public.faturamento_retencao.codigo_responsavel_cancelamento	IS 'Código interno da pessoa responsável pelo cancelamento da retenção.';
COMMENT ON COLUMN public.faturamento_retencao.percentual						IS 'Percentual da retenção.';
COMMENT ON COLUMN public.faturamento_retencao.data_baixa						IS 'Data/hora da baixa (recebimento) da retenção.';
COMMENT ON COLUMN public.faturamento_retencao.data_cancelamento					IS 'Data/hora do cancelamento da retenção.';

/* Set owner */
ALTER TABLE public.faturamento_retencao OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "nota_saida_parcela"                           */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT pk_nota_saida_parcela;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT ck_nota_saida_parcela_situacao;

/* Create temporary table */
CREATE TABLE public.nota_saida_parcela_TMP (
    codigo						BIGINT DEFAULT nextval('seq_nota_saida_parcela')  NOT NULL,
    codigo_nota_saida			BIGINT  NOT NULL,
    codigo_resp_recebimento		BIGINT,
    codigo_resp_cancelamento	BIGINT,
    ordem						SMALLINT  NOT NULL,
    situacao					SMALLINT DEFAULT 0  NOT NULL,
    valor						NUMERIC(19,2)  NOT NULL,
    valor_desconto				NUMERIC(9,2) DEFAULT 0.00  NOT NULL,
    valor_multa					NUMERIC(9,2) DEFAULT 0.00  NOT NULL,
    valor_juros					NUMERIC(9,2) DEFAULT 0.00  NOT NULL,
    valor_permuta				NUMERIC(9,2) DEFAULT 0.00  NOT NULL,
    data_vencimento				DATE  NOT NULL,
    data_pagamento				DATE  NOT NULL,
    data_resp_recebimento		TIMESTAMP,
    data_resp_cancelamento		TIMESTAMP,
    criador						CHARACTER VARYING(255),
    data_criacao				TIMESTAMP,
    atualizador					CHARACTER VARYING(255),
    data_atualizacao			TIMESTAMP);

/* Move data to the temporary table */
INSERT INTO public.nota_saida_parcela_TMP
    (codigo,codigo_nota_saida,codigo_resp_recebimento,codigo_resp_cancelamento,ordem,situacao,valor,valor_desconto,valor_multa,valor_juros,data_vencimento,data_pagamento,data_resp_recebimento,data_resp_cancelamento,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_nota_saida,codigo_resp_recebimento,codigo_resp_cancelamento,ordem,situacao,valor,valor_desconto,valor_multa,valor_juros,data_vencimento,data_pagamento,data_resp_recebimento,data_resp_cancelamento,criador,data_criacao,atualizador,data_atualizacao
FROM public.nota_saida_parcela;

/* Drop indexes */
DROP INDEX public.uq_nota_saida_parcela_nota_saida_ordem;
DROP INDEX public.ix_nota_saida_parcela_resp_cancelamento;
DROP INDEX public.ix_nota_saida_parcela_resp_recebimento;
DROP INDEX public.ix_nota_saida_parcela_nota_saida;

/* Drop "old" table */
DROP TABLE public.nota_saida_parcela;

/* Rename temporary table to "new" table */
ALTER TABLE public.nota_saida_parcela_TMP
	RENAME TO nota_saida_parcela;

/* Add primary key */
ALTER TABLE public.nota_saida_parcela
	ADD CONSTRAINT pk_nota_saida_parcela 
    PRIMARY KEY (codigo);

/* Add constraint */
ALTER TABLE public.nota_saida_parcela
	ADD CONSTRAINT ck_nota_saida_parcela_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1, 2, 3]));

/* Create indexes */
CREATE INDEX ix_nota_saida_parcela_nota_saida					ON public.nota_saida_parcela (codigo_nota_saida);
CREATE INDEX ix_nota_saida_parcela_resp_recebimento				ON public.nota_saida_parcela (codigo_resp_recebimento);
CREATE INDEX ix_nota_saida_parcela_resp_cancelamento			ON public.nota_saida_parcela (codigo_resp_cancelamento);
CREATE UNIQUE INDEX uq_nota_saida_parcela_nota_saida_ordem		ON public.nota_saida_parcela (codigo_nota_saida,ordem);

/* Comments */
COMMENT ON TABLE public.nota_saida_parcela								IS 'Parcelas das notas de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo						IS 'Código interno da parcela da nota de saída.Sequência: seq_nota_saida_parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_nota_saida			IS 'Código interno da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_resp_recebimento		IS 'Código interno da pessoa responsável pelo recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_resp_cancelamento	IS 'Código interno da pessoa responsável pelo cancelamento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.ordem						IS 'Ordem da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.situacao					IS 'Situação: 0 = Aberta (NF não processada) / 1 = Em recebimento / 2 = Finalizada (recebida) / 3 = Cancelada.';
COMMENT ON COLUMN public.nota_saida_parcela.valor						IS 'Valor da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_desconto				IS 'Valor de desconto da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_multa					IS 'Valor de multa recebida da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_juros					IS 'Valor de juros recebido da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_permuta				IS 'Valor de permuta (troca de valores entre o cliente e a AA) da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.data_vencimento				IS 'Data de vencimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.data_pagamento				IS 'Data de pagamento da parcela (até o efetivo pagamento, será igual ao vencimento).';
COMMENT ON COLUMN public.nota_saida_parcela.data_resp_recebimento		IS 'Data/hora de confirmação do recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.data_resp_cancelamento		IS 'Data/hora do cancelamento.';
COMMENT ON COLUMN public.nota_saida_parcela.criador						IS 'Responsável (login) pela criação da parcela da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.data_criacao				IS 'Data/hora de criação da parcela da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.atualizador					IS 'Responsável (login) pela última atualização da parcela da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.data_atualizacao			IS 'Data/hora da última atualização da parcela da nota de saída.';

/* Set owner */
ALTER TABLE public.nota_saida_parcela OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.tipo								ADD CONSTRAINT fk_tipo_situacao 
    FOREIGN KEY (codigo_situacao)					REFERENCES public.situacao (codigo);

ALTER TABLE public.faturamento_retencao				ADD CONSTRAINT fk_faturamento_retencao_tipo 
    FOREIGN KEY (codigo_tipo_retencao)				REFERENCES public.tipo (codigo);
ALTER TABLE public.faturamento_retencao				ADD CONSTRAINT fk_faturamento_retencao_faturamento 
    FOREIGN KEY (codigo_faturamento)				REFERENCES public.faturamento (codigo);
ALTER TABLE public.faturamento_retencao				ADD CONSTRAINT fk_faturamento_retencao_situacao 
    FOREIGN KEY (codigo_situacao)					REFERENCES public.situacao (codigo);
ALTER TABLE public.faturamento_retencao				ADD CONSTRAINT fk_faturamento_retencao_responsavel_baixa 
    FOREIGN KEY (codigo_responsavel_baixa)			REFERENCES public.pessoa (codigo);
ALTER TABLE public.faturamento_retencao				ADD CONSTRAINT fk_faturamento_retencao_responsavel_cancelamento 
    FOREIGN KEY (codigo_responsavel_cancelamento)	REFERENCES public.pessoa (codigo);

ALTER TABLE public.nota_saida_parcela				ADD CONSTRAINT fk_nota_saida_parcela_nota_saida 
    FOREIGN KEY (codigo_nota_saida)					REFERENCES public.nota_saida (codigo);
ALTER TABLE public.nota_saida_parcela				ADD CONSTRAINT fk_nota_saida_parcela_resp_cancelamento 
    FOREIGN KEY (codigo_resp_cancelamento)			REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida_parcela				ADD CONSTRAINT fk_nota_saida_parcela_resp_recebimento 
    FOREIGN KEY (codigo_resp_recebimento)			REFERENCES public.pessoa (codigo);

/* ---------------------------------------------------------------------- */
/* Dados                                                                  */
/* ---------------------------------------------------------------------- */
/* Situação para os tipos */
INSERT INTO situacao (codigo, tabela, subsituacao, chave, ordem, nome, descricao, permissao_editar, permissao_suspender, permissao_reativar, permissao_cancelar, criador, data_criacao, atualizador, data_atualizacao)
	VALUES (nextval('seq_situacao'), 'tipo', 'tipo', 'ativo', 10, 'A', 'Ativo', TRUE, FALSE, FALSE, TRUE, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO situacao (codigo, tabela, subsituacao, chave, ordem, nome, descricao, permissao_editar, permissao_suspender, permissao_reativar, permissao_cancelar, criador, data_criacao, atualizador, data_atualizacao)
	VALUES (nextval('seq_situacao'), 'tipo', 'tipo', 'inativo', 20, 'I', 'Inativo', FALSE, FALSE, FALSE, FALSE, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

/* Tipo de retenção */
INSERT
	INTO	tipo (codigo, codigo_situacao, tabela, subtipo, chave, ordem, nome, descricao, criador, data_criacao, atualizador, data_atualizacao)
	SELECT	nextval('seq_tipo'), situacao.codigo, 'faturamento_retencao', 'faturamento_retencao', 'contratual', 10, 'Contratual', 'Retenção contratual', 'admin', CURRENT_TIMESTAMP, NULL, NULL
	FROM	situacao
	WHERE	situacao.tabela = 'tipo'
	AND		situacao.chave = 'ativo';

INSERT
	INTO	tipo (codigo, codigo_situacao, tabela, subtipo, chave, ordem, nome, descricao, criador, data_criacao, atualizador, data_atualizacao)
	SELECT	nextval('seq_tipo'), situacao.codigo, 'faturamento_retencao', 'faturamento_retencao', 'acessoria', 20, 'Acessoria', 'Retenção acessoria', 'admin', CURRENT_TIMESTAMP, NULL, NULL
	FROM	situacao
	WHERE	situacao.tabela = 'tipo'
	AND		situacao.chave = 'ativo';

/* Situações de retenção */
INSERT INTO situacao (codigo, tabela, subsituacao, chave, ordem, nome, descricao, permissao_editar, permissao_suspender, permissao_reativar, permissao_cancelar, criador, data_criacao, atualizador, data_atualizacao)
	VALUES (nextval('seq_situacao'), 'faturamento_retencao', 'faturamento_retencao', 'a_receber', 10, 'Receber', 'A receber', TRUE, FALSE, FALSE, TRUE, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO situacao (codigo, tabela, subsituacao, chave, ordem, nome, descricao, permissao_editar, permissao_suspender, permissao_reativar, permissao_cancelar, criador, data_criacao, atualizador, data_atualizacao)
	VALUES (nextval('seq_situacao'), 'faturamento_retencao', 'faturamento_retencao', 'recebida', 20, 'Recebida', 'Recebida', TRUE, FALSE, FALSE, TRUE, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO situacao (codigo, tabela, subsituacao, chave, ordem, nome, descricao, permissao_editar, permissao_suspender, permissao_reativar, permissao_cancelar, criador, data_criacao, atualizador, data_atualizacao)
	VALUES (nextval('seq_situacao'), 'faturamento_retencao', 'faturamento_retencao', 'cancelada', 30, 'Cancelada', 'Cancelada', TRUE, FALSE, FALSE, TRUE, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
