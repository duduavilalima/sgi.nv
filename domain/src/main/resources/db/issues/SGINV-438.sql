/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2017-04-13 09:06                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Create sequences                                                       */
/* ---------------------------------------------------------------------- */
CREATE SEQUENCE public.seq_historico_conciliacao				INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE public.seq_historico_conciliacao_conta_contabil	INCREMENT 1 MINVALUE 1 START 1;

/* ---------------------------------------------------------------------- */
/* Add table "historico_conciliacao"                                      */
/* ---------------------------------------------------------------------- */
CREATE TABLE historico_conciliacao (
    codigo				BIGINT					DEFAULT nextval('seq_historico_conciliacao')	NOT NULL,
    descricao			CHARACTER VARYING(255)													NOT NULL,
    situacao			SMALLINT				DEFAULT 0										NOT NULL,
    tipo_acao			SMALLINT																NOT NULL,
    data_criacao		TIMESTAMP,
    criador				CHARACTER VARYING(255),
    atualizador			CHARACTER VARYING(255),
    data_atualizacao	TIMESTAMP,
    CONSTRAINT pk_historico_conciliacao PRIMARY KEY (codigo)
);

/* Create index */
CREATE UNIQUE INDEX uq_historico_conciliacao_descricao ON historico_conciliacao (descricao);

/* Create constraints */
ALTER TABLE historico_conciliacao
	ADD CONSTRAINT ck_historico_conciliacao_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1]));

ALTER TABLE historico_conciliacao
	ADD CONSTRAINT ck_historico_conciliacao_tipo_acao 
    CHECK (tipo_acao = ANY (ARRAY[0, 1, 2, 3, 4]));

/* Comments */
COMMENT ON TABLE historico_conciliacao						IS 'Ação a ser realizada para os históricos recebidos nos arquivos de conciliação bancária.';
COMMENT ON COLUMN historico_conciliacao.codigo				IS 'Código interno do histórico da conciliação bancária. Sequência: seq_historico_conciliacao.';
COMMENT ON COLUMN historico_conciliacao.descricao			IS 'Descrição (parte inicial) do histórico da conciliação bancária.';
COMMENT ON COLUMN historico_conciliacao.situacao			IS 'Situação do histórico da conciliação bancária: 0 = Inativo / 1 = Ativo.';
COMMENT ON COLUMN historico_conciliacao.tipo_acao			IS 'Tipo de ação para lançamentos com o histórico da conciliação bancária: 0 = Nenhuma ação / 1 = Lançamento de tesouraria / 2 = Pagamento / 3 = NF/Saída (faturamento) / 4 = Outros recebimentos.';
COMMENT ON COLUMN historico_conciliacao.data_criacao		IS 'Data/hora de criação do histórico da conciliação bancária.';
COMMENT ON COLUMN historico_conciliacao.criador				IS 'Responsável (login) pela criação do histórico da conciliação bancária.';
COMMENT ON COLUMN historico_conciliacao.atualizador			IS 'Responsável (login) pela última atualização do histórico da conciliação bancária.';
COMMENT ON COLUMN historico_conciliacao.data_atualizacao	IS 'Data/hora da última atualização do histórico da conciliação bancária.';

/* Set owner */
ALTER TABLE public.historico_conciliacao OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add table "historico_conciliacao_conta_contabil"                       */
/* ---------------------------------------------------------------------- */
CREATE TABLE historico_conciliacao_conta_contabil (
    codigo							BIGINT	DEFAULT nextval('seq_historico_conciliacao_conta_contabil')	NOT NULL,
    codigo_historico_conciliacao	BIGINT																NOT NULL,
    codigo_conta_contabil			BIGINT																NOT NULL,
    CONSTRAINT pk_historico_conciliacao_conta_contabil PRIMARY KEY (codigo)
);

/* Create indexes */
CREATE INDEX ix_historico_conciliacao_conta_contabil_historico_conciliacao							ON historico_conciliacao_conta_contabil (codigo_historico_conciliacao);
CREATE INDEX ix_historico_conciliacao_conta_contabil_conta_contabil									ON historico_conciliacao_conta_contabil (codigo_conta_contabil);
CREATE UNIQUE INDEX uq_historico_conciliacao_conta_contabil_historico_conciliacao_conta_contabil	ON historico_conciliacao_conta_contabil (codigo_historico_conciliacao,codigo_conta_contabil);

/* Comments */
COMMENT ON TABLE historico_conciliacao_conta_contabil								IS 'Relacionamento entre os lançamentos de Tesouraria a ser realizada para os históricos recebidos nos arquivos de conciliação bancária e a conta contábil default para estes lançamentos (conforme a unidade da conciliação/conta contábil).';
COMMENT ON COLUMN historico_conciliacao_conta_contabil.codigo						IS 'Código interno do relacionamento do histórico da conciliação bancária com a conta contábil. Sequência: seq_historico_conciliacao_conta_contabil.';
COMMENT ON COLUMN historico_conciliacao_conta_contabil.codigo_historico_conciliacao	IS 'Código interno do histórico da conciliação bancária.';
COMMENT ON COLUMN historico_conciliacao_conta_contabil.codigo_conta_contabil		IS 'Código interno da conta contábil default para o lançamento de Tesouraria (conforme a empresa/unidade da conta contábil).';

/* Set owner */
ALTER TABLE public.historico_conciliacao_conta_contabil OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE historico_conciliacao_conta_contabil	ADD CONSTRAINT fk_historico_conciliacao_conta_contabil_historico_conciliacao 
    FOREIGN KEY (codigo_historico_conciliacao)		REFERENCES historico_conciliacao (codigo);
ALTER TABLE historico_conciliacao_conta_contabil	ADD CONSTRAINT fk_historico_conciliacao_conta_contabil_conta_contabil 
    FOREIGN KEY (codigo_conta_contabil)				REFERENCES public.conta_contabil (codigo);

/* ------------------------------------------------------------------------ */
/* Acesso para "Tesouraria > Cadastros > Histórico de conciliação bancária" */
/* ------------------------------------------------------------------------ */
/* Adicionar funcionalidade */
INSERT
INTO	funcionalidade (codigo, nome, ordem, url, codigo_modulo)
SELECT	NEXTVAL('seq_funcionalidade'), 'Histórico de conciliação bancária', 20, '/cadastros/historico/listar.xhtml', m1.codigo
FROM	modulo m1, modulo m2
WHERE	m1.nome = 'Cadastros'
AND		m1.codigo_modulo_pai = m2.codigo
AND		m2.nome = 'Tesouraria'
AND		m2.codigo_modulo_pai IS NULL;

/* Ajustar controle de acesso da nova funcionalidade */
--Pesquisa/resultado
INSERT
INTO	permissao (codigo, acao, alvo, codigo_receptor)
SELECT	NEXTVAL('seq_permissao'), 'render', '/cadastros/historico/listar.xhtml', perfil.codigo
FROM	perfil
WHERE	perfil.nome = 'operador-tesouraria';

INSERT
INTO	permissao (codigo, acao, alvo, codigo_receptor)
SELECT	NEXTVAL('seq_permissao'), 'restore', '/cadastros/historico/listar.xhtml', perfil.codigo
FROM	perfil
WHERE	perfil.nome = 'operador-tesouraria';

--Edição
INSERT
INTO	permissao (codigo, acao, alvo, codigo_receptor)
SELECT	NEXTVAL('seq_permissao'), 'render', '/cadastros/historico/editar.xhtml', perfil.codigo
FROM	perfil
WHERE	perfil.nome = 'operador-tesouraria';

INSERT
INTO	permissao (codigo, acao, alvo, codigo_receptor)
SELECT	NEXTVAL('seq_permissao'), 'restore', '/cadastros/historico/editar.xhtml', perfil.codigo
FROM	perfil
WHERE	perfil.nome = 'operador-tesouraria';

--Exibição
INSERT
INTO	permissao (codigo, acao, alvo, codigo_receptor)
SELECT	NEXTVAL('seq_permissao'), 'render', '/cadastros/historico/exibir.xhtml', perfil.codigo
FROM	perfil
WHERE	perfil.nome = 'operador-tesouraria';

INSERT
INTO	permissao (codigo, acao, alvo, codigo_receptor)
SELECT	NEXTVAL('seq_permissao'), 'restore', '/cadastros/historico/exibir.xhtml', perfil.codigo
FROM	perfil
WHERE	perfil.nome = 'operador-tesouraria';
