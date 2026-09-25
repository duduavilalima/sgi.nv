/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-11-16 14:30                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Create sequence                                                        */
/* ---------------------------------------------------------------------- */
CREATE SEQUENCE public.seq_motivo_retorno_documento_cheque_conta_corrente	INCREMENT 1 MINVALUE 1 START 1;

/* ---------------------------------------------------------------------- */
/* Add table "motivo_retorno_documento_cheque_conta_corrente"             */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.motivo_retorno_documento_cheque_conta_corrente (
    codigo							BIGINT	DEFAULT nextval('seq_motivo_retorno_documento_cheque_conta_corrente')	NOT NULL,
    codigo_motivo_retorno_documento	BIGINT																			NOT NULL,
    codigo_cheque_conta_corrente	BIGINT																			NOT NULL,
    CONSTRAINT pk_motivo_retorno_documento_cheque_conta_corrente PRIMARY KEY (codigo)
);

/* Create index */
CREATE INDEX ix_mot_ret_documento_cheque_mot_ret_documento					ON public.motivo_retorno_documento_cheque_conta_corrente (codigo_motivo_retorno_documento);
CREATE INDEX ix_mot_ret_documento_cheque_cheque								ON public.motivo_retorno_documento_cheque_conta_corrente (codigo_cheque_conta_corrente);
CREATE UNIQUE INDEX ix_mot_ret_documento_cheque_mot_ret_documento_cheque	ON public.motivo_retorno_documento_cheque_conta_corrente (codigo_motivo_retorno_documento,codigo_cheque_conta_corrente);

/* Comments */
COMMENT ON TABLE public.motivo_retorno_documento_cheque_conta_corrente									IS 'Associação entre um retorno (motivo de retorno x documento) e o cheque de uma conta corrente.';
COMMENT ON COLUMN public.motivo_retorno_documento_cheque_conta_corrente.codigo							IS 'Código interno da associação entre um retorno (motivo de retorno x documento) e o cheque de uma conta corrente. Sequência: seq_motivo_retorno_documento_cheque_conta_corrente.';
COMMENT ON COLUMN public.motivo_retorno_documento_cheque_conta_corrente.codigo_motivo_retorno_documento	IS 'Código interno do motivo de retorno do documento.';
COMMENT ON COLUMN public.motivo_retorno_documento_cheque_conta_corrente.codigo_cheque_conta_corrente	IS 'Código interno do cheque de uma conta corrente.';

/* Set owner */
ALTER TABLE public.motivo_retorno_documento_cheque_conta_corrente OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.motivo_retorno_documento_cheque_conta_corrente	ADD CONSTRAINT fk_mot_ret_documento_cheque_conta_corrente_mot_ret_documento 
    FOREIGN KEY (codigo_motivo_retorno_documento)					REFERENCES public.motivo_retorno_documento (codigo);
ALTER TABLE public.motivo_retorno_documento_cheque_conta_corrente	ADD CONSTRAINT fk_mot_ret_documento_cheque_conta_corrente_cheque
    FOREIGN KEY (codigo_cheque_conta_corrente)						REFERENCES public.cheque_conta_corrente (codigo);

/* ---------------------------------------------------------------------- */
/* Add data                                                               */
/* ---------------------------------------------------------------------- */
/* Novo submódulo "Tesouraria > Conciliação Bancária" */
/*
--Reordenar subopções
UPDATE funcionalidade	SET ordem = 50 WHERE nome = 'Lançamentos'	AND codigo_modulo = (SELECT codigo FROM modulo WHERE nome = 'Tesouraria');
UPDATE funcionalidade	SET ordem = 60 WHERE nome = 'Relatórios'	AND codigo_modulo = (SELECT codigo FROM modulo WHERE nome = 'Tesouraria');

--Reordenar submódulo "Cadastros"
UPDATE modulo			SET ordem = 70 WHERE nome = 'Cadastros'		AND codigo_modulo_pai = (SELECT codigo FROM modulo WHERE nome = 'Tesouraria');

--Novo submódulo "Conciliação Bancária"
INSERT
INTO	modulo (codigo, nome, ordem, codigo_modulo_pai, codigo_sistema, contexto, status)
VALUES	(NEXTVAL('seq_modulo'), 'Conciliação Bancária', 40, (SELECT codigo FROM modulo WHERE nome = 'Tesouraria'), 1, '/sgi/tesouraria', 0);

--Opções do submódulo "Conciliação Bancária"
INSERT
INTO	funcionalidade (codigo, nome, ordem, url, codigo_modulo)
VALUES	(NEXTVAL('seq_funcionalidade'), 'Importação',				10, '/conciliacao/importar.xhtml',	CURRVAL('seq_modulo'));

INSERT
INTO	funcionalidade (codigo, nome, ordem, url, codigo_modulo)
VALUES	(NEXTVAL('seq_funcionalidade'), 'Baixa de Pendências',		20, '/conciliacao/listar.xhtml',	CURRVAL('seq_modulo'));

INSERT
INTO	funcionalidade (codigo, nome, ordem, url, codigo_modulo)
VALUES	(NEXTVAL('seq_funcionalidade'), 'Relatório de Pendências',	30, '/conciliacao/relatorio.xhtml',	CURRVAL('seq_modulo'));

--Controle de acesso
INSERT INTO permissao (codigo, codigo_receptor, acao, alvo) VALUES (NEXTVAL('seq_permissao'), (SELECT codigo FROM perfil WHERE nome = 'operador-tesouraria' ), 'render' , '/conciliacao/importar.xhtml');
INSERT INTO permissao (codigo, codigo_receptor, acao, alvo) VALUES (NEXTVAL('seq_permissao'), (SELECT codigo FROM perfil WHERE nome = 'operador-tesouraria' ), 'restore', '/conciliacao/importar.xhtml');
INSERT INTO permissao (codigo, codigo_receptor, acao, alvo) VALUES (NEXTVAL('seq_permissao'), (SELECT codigo FROM perfil WHERE nome = 'operador-tesouraria' ), 'render' , '/conciliacao/listar.xhtml');
INSERT INTO permissao (codigo, codigo_receptor, acao, alvo) VALUES (NEXTVAL('seq_permissao'), (SELECT codigo FROM perfil WHERE nome = 'operador-tesouraria' ), 'restore', '/conciliacao/listar.xhtml');
INSERT INTO permissao (codigo, codigo_receptor, acao, alvo) VALUES (NEXTVAL('seq_permissao'), (SELECT codigo FROM perfil WHERE nome = 'operador-tesouraria' ), 'render' , '/conciliacao/relatorio.xhtml');
INSERT INTO permissao (codigo, codigo_receptor, acao, alvo) VALUES (NEXTVAL('seq_permissao'), (SELECT codigo FROM perfil WHERE nome = 'operador-tesouraria' ), 'restore', '/conciliacao/relatorio.xhtml');

INSERT INTO permissao (codigo, codigo_receptor, acao, alvo) VALUES (NEXTVAL('seq_permissao'), (SELECT codigo FROM perfil WHERE nome = 'faturamento-analista'), 'render' , '/conciliacao/listar.xhtml');
INSERT INTO permissao (codigo, codigo_receptor, acao, alvo) VALUES (NEXTVAL('seq_permissao'), (SELECT codigo FROM perfil WHERE nome = 'faturamento-analista'), 'restore', '/conciliacao/listar.xhtml');
INSERT INTO permissao (codigo, codigo_receptor, acao, alvo) VALUES (NEXTVAL('seq_permissao'), (SELECT codigo FROM perfil WHERE nome = 'faturamento-analista'), 'render' , '/conciliacao/relatorio.xhtml');
INSERT INTO permissao (codigo, codigo_receptor, acao, alvo) VALUES (NEXTVAL('seq_permissao'), (SELECT codigo FROM perfil WHERE nome = 'faturamento-analista'), 'restore', '/conciliacao/relatorio.xhtml');
*/