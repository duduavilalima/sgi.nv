/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2017-04-27 12:10                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Create sequences                                                       */
/* ---------------------------------------------------------------------- */
CREATE SEQUENCE public.seq_despesa_fluxo_caixa	INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE public.seq_previsao_fluxo_caixa	INCREMENT 1 MINVALUE 1 START 1;

/* ---------------------------------------------------------------------- */
/* Add table "despesa_fluxo_caixa"                                        */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.despesa_fluxo_caixa (
    codigo				BIGINT					DEFAULT nextval('seq_despesa_fluxo_caixa')	NOT NULL,
    sigla				CHARACTER(2)														NOT NULL,
    descricao			CHARACTER VARYING(30)												NOT NULL,
    situacao			SMALLINT				DEFAULT 1									NOT NULL,
    criador				CHARACTER VARYING(255),
    data_criacao		TIMESTAMP,
    atualizador			CHARACTER VARYING(255),
    data_atualizacao	TIMESTAMP,
    CONSTRAINT pk_despesa_fluxo_caixa PRIMARY KEY (codigo)
);

/* Create index */
CREATE UNIQUE INDEX uq_despesa_fluxo_caixa_sigla ON public.despesa_fluxo_caixa (sigla);

/* Create constraint */
ALTER TABLE public.despesa_fluxo_caixa
	ADD CONSTRAINT ck_despesa_fluxo_caixa_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1]));

/* Comments */
COMMENT ON TABLE public.despesa_fluxo_caixa						IS 'Despesas para o fluxo de caixa.';
COMMENT ON COLUMN public.despesa_fluxo_caixa.codigo				IS 'Código interno da despesas para o fluxo de caixa. Sequência: seq_despesa_fluxo_caixa.';
COMMENT ON COLUMN public.despesa_fluxo_caixa.sigla				IS 'Sigla da despesas para o fluxo de caixa. Chave única.';
COMMENT ON COLUMN public.despesa_fluxo_caixa.descricao			IS 'Descrição da despesas para o fluxo de caixa.';
COMMENT ON COLUMN public.despesa_fluxo_caixa.situacao			IS 'Situação da despesas para o fluxo de caixa: 0 = Inativa / 1 = Ativa.';
COMMENT ON COLUMN public.despesa_fluxo_caixa.criador			IS 'Responsável (login) pela criação da despesas para o fluxo de caixa.';
COMMENT ON COLUMN public.despesa_fluxo_caixa.data_criacao		IS 'Data/hora de criação da despesas para o fluxo de caixa.';
COMMENT ON COLUMN public.despesa_fluxo_caixa.atualizador		IS 'Responsável (login) pela última atualização da despesas para o fluxo de caixa.';
COMMENT ON COLUMN public.despesa_fluxo_caixa.data_atualizacao	IS 'Data/hora da última atualização da despesas para o fluxo de caixa.';

/* Set owner */
ALTER TABLE public.despesa_fluxo_caixa OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add table "previsao_fluxo_caixa"                                       */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.previsao_fluxo_caixa (
    codigo						BIGINT					DEFAULT nextval('seq_previsao_fluxo_caixa')	NOT NULL,
    codigo_unidade				BIGINT																NOT NULL,
    codigo_diretoria			BIGINT																NOT NULL,
    codigo_despesa_fluxo_caixa	BIGINT																NOT NULL,
    data						DATE																NOT NULL,
    valor						NUMERIC(19,2)														NOT NULL,
    criador						CHARACTER VARYING(255),
    data_criacao				TIMESTAMP,
    atualizador					CHARACTER VARYING(255),
    data_atualizacao			TIMESTAMP,
    CONSTRAINT pk_previsao_fluxo_caixa PRIMARY KEY (codigo)
);

/* Create indexes */
CREATE INDEX ix_previsao_fluxo_caixa_unidade								ON public.previsao_fluxo_caixa (codigo_unidade);
CREATE INDEX ix_previsao_fluxo_caixa_diretoria								ON public.previsao_fluxo_caixa (codigo_diretoria);
CREATE INDEX ix_previsao_fluxo_caixa_despesa_fluxo_caixa					ON public.previsao_fluxo_caixa (codigo_despesa_fluxo_caixa);
CREATE UNIQUE INDEX uq_previsao_fluxo_caixa_unidade_diretoria_despesa_data	ON public.previsao_fluxo_caixa (codigo_unidade,codigo_diretoria,codigo_despesa_fluxo_caixa,data);

/* Comments */
COMMENT ON TABLE public.previsao_fluxo_caixa								IS 'Valor de previsão para as despesas do fluxo de caixa.';
COMMENT ON COLUMN public.previsao_fluxo_caixa.codigo						IS 'Código interno da previsão para a despesa do fluxo de caixa. Sequência: seq_previsao_fluxo_caixa.';
COMMENT ON COLUMN public.previsao_fluxo_caixa.codigo_unidade				IS 'Código interno da unidade da previsão para a despesa do fluxo de caixa. Parte da chave única.';
COMMENT ON COLUMN public.previsao_fluxo_caixa.codigo_diretoria				IS 'Código interno da diretoria da previsão para a despesa do fluxo de caixa. Parte da chave única.';
COMMENT ON COLUMN public.previsao_fluxo_caixa.codigo_despesa_fluxo_caixa	IS 'Cpodigo interno da despesa relacionada a previsão para a despesa do fluxo de caixa. Parte da chave única.';
COMMENT ON COLUMN public.previsao_fluxo_caixa.data							IS 'Data da previsão para a despesa do fluxo de caixa. Parte da chave única.';
COMMENT ON COLUMN public.previsao_fluxo_caixa.valor							IS 'Valor da previsão para a despesa do fluxo de caixa.';
COMMENT ON COLUMN public.previsao_fluxo_caixa.criador						IS 'Responsável (login) pela criação da previsão para a despesa do fluxo de caixa.';
COMMENT ON COLUMN public.previsao_fluxo_caixa.data_criacao					IS 'Data/hora de criação da previsão para a despesa do fluxo de caixa.';
COMMENT ON COLUMN public.previsao_fluxo_caixa.atualizador					IS 'Responsável (login) pela última atualização da previsão para a despesa do fluxo de caixa.';
COMMENT ON COLUMN public.previsao_fluxo_caixa.data_atualizacao				IS 'Data/hora da última atualização da previsão para a despesa do fluxo de caixa.';

/* Set owner */
ALTER TABLE public.previsao_fluxo_caixa OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.previsao_fluxo_caixa			ADD CONSTRAINT fk_previsao_fluxo_caixa_unidade 
    FOREIGN KEY (codigo_unidade)				REFERENCES public.unidade (codigo);
ALTER TABLE public.previsao_fluxo_caixa			ADD CONSTRAINT fk_previsao_fluxo_caixa_diretoria 
    FOREIGN KEY (codigo_diretoria)				REFERENCES public.diretoria (codigo);
ALTER TABLE public.previsao_fluxo_caixa			ADD CONSTRAINT fk_previsao_fluxo_caixa_despesa_fluxo_caixa 
    FOREIGN KEY (codigo_despesa_fluxo_caixa)	REFERENCES public.despesa_fluxo_caixa (codigo);

/* ---------------------------------------------------------------------- */
/* Ajustar menu "Fluxo de Caixa"                                          */
/* ---------------------------------------------------------------------- */
/* Novo menu "Fluxo de Caixa" */
INSERT
INTO	modulo (codigo, nome, ordem, codigo_modulo_pai, codigo_sistema, contexto, status)
VALUES	(NEXTVAL('seq_modulo'), 'Fluxo de Caixa', 85, NULL, 1, '/sgi/fluxocaixa', 0);

/* Novo submenu "Fluxo de Caixa" */
INSERT
INTO	funcionalidade (codigo, nome, ordem, url, codigo_modulo)
SELECT	NEXTVAL('seq_funcionalidade'), 'Gráficos', 10, '/grafico.xhtml', m1.codigo
FROM	modulo m1
WHERE	m1.nome = 'Fluxo de Caixa'
AND		m1.codigo_modulo_pai IS NULL;

INSERT
INTO	funcionalidade (codigo, nome, ordem, url, codigo_modulo)
SELECT	NEXTVAL('seq_funcionalidade'), 'Relatórios', 20, '/relatorio.xhtml', m1.codigo
FROM	modulo m1
WHERE	m1.nome = 'Fluxo de Caixa'
AND		m1.codigo_modulo_pai IS NULL;

/* Submenu "Cadastros" */
INSERT
INTO	modulo (codigo, nome, ordem, codigo_modulo_pai, codigo_sistema, contexto, status)
SELECT	NEXTVAL('seq_modulo'), 'Cadastros', 30, m2.codigo, 1, '/sgi', 0
FROM	modulo m2
WHERE	m2.nome = 'Fluxo de Caixa'
AND		m2.codigo_modulo_pai IS NULL;

/* Novo submenu "Fluxo de Caixa > Cadastros" */
INSERT
INTO	funcionalidade (codigo, nome, ordem, url, codigo_modulo)
SELECT	NEXTVAL('seq_funcionalidade'), 'Despesas', 10, '/despesa/listar.xhtml', m2.codigo
FROM	modulo m1, modulo m2
WHERE	m2.nome = 'Cadastros'
AND		m2.codigo_modulo_pai = m1.codigo
AND		m1.nome = 'Fluxo de Caixa'
AND		m1.codigo_modulo_pai IS NULL;

INSERT
INTO	funcionalidade (codigo, nome, ordem, url, codigo_modulo)
SELECT	NEXTVAL('seq_funcionalidade'), 'Previsões', 20, '/previsao/listar.xhtml', m2.codigo
FROM	modulo m1, modulo m2
WHERE	m2.nome = 'Cadastros'
AND		m2.codigo_modulo_pai = m1.codigo
AND		m1.nome = 'Fluxo de Caixa'
AND		m1.codigo_modulo_pai IS NULL;

/* Criar tabelas temporárias */
CREATE TABLE public.TMP_acao (
    acao CHARACTER VARYING(10)
);

INSERT INTO TMP_acao (acao) VALUES ('render' );
INSERT INTO TMP_acao (acao) VALUES ('restore');

CREATE TABLE public.TMP_alvo (
    alvo	CHARACTER VARYING(255),
	tipo	SMALLINT
);

INSERT INTO TMP_alvo (alvo, tipo) VALUES ('/fluxocaixa/grafico.xhtml'			, 1);	-- 0 = Só operador de tesouraria (operador-tesouraria)
INSERT INTO TMP_alvo (alvo, tipo) VALUES ('/fluxocaixa/relatorio.xhtml'			, 1);	-- 1 = OT e Diretoria (financeiro-diretor)
INSERT INTO TMP_alvo (alvo, tipo) VALUES ('/fluxocaixa/despesa/listar.xhtml'	, 0);	-- 2 = OT, Analista de faturamento (faturamento-analista) e Conferente de notas (contabilidade-conferencia)
INSERT INTO TMP_alvo (alvo, tipo) VALUES ('/fluxocaixa/despesa/editar.xhtml'	, 0);
INSERT INTO TMP_alvo (alvo, tipo) VALUES ('/fluxocaixa/despesa/exibir.xhtml'	, 0);
INSERT INTO TMP_alvo (alvo, tipo) VALUES ('/fluxocaixa/previsao/listar.xhtml'	, 2);
INSERT INTO TMP_alvo (alvo, tipo) VALUES ('/fluxocaixa/previsao/editar.xhtml'	, 2);
INSERT INTO TMP_alvo (alvo, tipo) VALUES ('/fluxocaixa/previsao/exibir.xhtml'	, 2);

/* Ajustar controle de acesso do novo menu */
INSERT
INTO	permissao (codigo, acao, alvo, codigo_receptor)
SELECT	NEXTVAL('seq_permissao'), TMP_acao.acao, TMP_alvo.alvo, perfil.codigo
FROM	perfil, TMP_acao, TMP_alvo
WHERE	perfil.nome = 'operador-tesouraria';

INSERT
INTO	permissao (codigo, acao, alvo, codigo_receptor)
SELECT	NEXTVAL('seq_permissao'), TMP_acao.acao, TMP_alvo.alvo, perfil.codigo
FROM	perfil, TMP_acao, TMP_alvo
WHERE	perfil.nome = 'financeiro-diretor'
AND		TMP_alvo.tipo = 1;

INSERT
INTO	permissao (codigo, acao, alvo, codigo_receptor)
SELECT	NEXTVAL('seq_permissao'), TMP_acao.acao, TMP_alvo.alvo, perfil.codigo
FROM	perfil, TMP_acao, TMP_alvo
WHERE	perfil.nome IN ('faturamento-analista', 'contabilidade-conferencia')
AND		TMP_alvo.tipo = 2;

/* Remover tabelas temporárias */
DROP TABLE public.TMP_acao;
DROP TABLE public.TMP_alvo;
