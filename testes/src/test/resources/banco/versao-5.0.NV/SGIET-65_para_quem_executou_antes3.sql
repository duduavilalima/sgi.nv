/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-04-06 17:15                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.conta_corrente	DROP CONSTRAINT fk_conta_corrente_conta_contabil;
ALTER TABLE public.conta_corrente	DROP CONSTRAINT fk_conta_corrente_dados_bancarios;

ALTER TABLE public.relacao_bancaria	DROP CONSTRAINT fk_relacao_bancaria_conta_corrente;

ALTER TABLE public.lancamento		DROP CONSTRAINT fk_lancamento_conta_corrente;

ALTER TABLE public.faturamento		DROP CONSTRAINT fk_faturamento_conta_corrente;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "conta_corrente"                               */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.conta_corrente	DROP CONSTRAINT pk_conta_corrente;
ALTER TABLE public.conta_corrente	DROP CONSTRAINT ck_conta_corrente_tipo;

CREATE TABLE public.conta_corrente_TMP (
    codigo						BIGINT  NOT NULL,
    codigo_conta_contabil		BIGINT,
    tipo						SMALLINT DEFAULT 0  NOT NULL,
    cheque_carbono				BOOLEAN DEFAULT true  NOT NULL,
    internet_banking			BOOLEAN DEFAULT false  NOT NULL,
    boleto_faturamento			BOOLEAN DEFAULT false  NOT NULL,
    cheque_anterior				BIGINT  NOT NULL,
    espaco_antes_valor			SMALLINT,
    salto_valor_extenso			SMALLINT,
    espaco_antes_extenso		SMALLINT,
    salto_extenso_extenso2		SMALLINT,
    espaco_antes_extenso2		SMALLINT,
    salto_extenso2_favorecido	SMALLINT,
    espaco_antes_favorecido		SMALLINT,
    salto_favorecido_data		SMALLINT,
    espaco_antes_data			SMALLINT,
    salto_endosso				SMALLINT,
    espaco_antes_endosso		SMALLINT,
    espaco_antes_assinatura		SMALLINT,
    identificador_cliente		INTEGER,
    identificador_fise			INTEGER,
    prazo_processamento_ib		SMALLINT,
    criador						CHARACTER VARYING(255),
    data_criacao				TIMESTAMP,
    atualizador					CHARACTER VARYING(255),
    data_atualizacao			TIMESTAMP);

INSERT INTO public.conta_corrente_TMP
    (codigo,codigo_conta_contabil,tipo,cheque_carbono,internet_banking,cheque_anterior,espaco_antes_valor,salto_valor_extenso,espaco_antes_extenso,salto_extenso_extenso2,espaco_antes_extenso2,salto_extenso2_favorecido,espaco_antes_favorecido,salto_favorecido_data,espaco_antes_data,salto_endosso,espaco_antes_endosso,espaco_antes_assinatura,identificador_cliente,identificador_fise,prazo_processamento_ib,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_conta_contabil,tipo,cheque_carbono,internet_banking,cheque_anterior,espaco_antes_valor,salto_valor_extenso,espaco_antes_extenso,salto_extenso_extenso2,espaco_antes_extenso2,salto_extenso2_favorecido,espaco_antes_favorecido,salto_favorecido_data,espaco_antes_data,salto_endosso,espaco_antes_endosso,espaco_antes_assinatura,identificador_cliente,identificador_fise,prazo_processamento_ib,criador,data_criacao,atualizador,data_atualizacao
FROM public.conta_corrente;

DROP INDEX public.ix_conta_corrente_conta_contabil;

DROP TABLE public.conta_corrente;

ALTER TABLE public.conta_corrente_TMP
	RENAME TO conta_corrente;

ALTER TABLE public.conta_corrente
	ADD CONSTRAINT pk_conta_corrente 
    PRIMARY KEY (codigo);

ALTER TABLE public.conta_corrente
	ADD CONSTRAINT ck_conta_corrente_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1, 2]));

CREATE INDEX ix_conta_corrente_conta_contabil ON public.conta_corrente (codigo_conta_contabil);

COMMENT ON TABLE public.conta_corrente								IS 'Contas correntes.';
COMMENT ON COLUMN public.conta_corrente.codigo						IS 'Código interno da conta corrente (código interno dos dados bancários).';
COMMENT ON COLUMN public.conta_corrente.codigo_conta_contabil		IS 'Código interno da conta contábil relacionada a conta corrente.';
COMMENT ON COLUMN public.conta_corrente.tipo						IS 'Código interno do tipo da conta corrente: 0 = Normal / 1 = Apenas usada para transferências (e recebimento) / 2 = Inativa.';
COMMENT ON COLUMN public.conta_corrente.cheque_carbono				IS 'Indica se o talão de cheques da conta corrente possui carbono (true) ou não (false).';
COMMENT ON COLUMN public.conta_corrente.internet_banking			IS 'Indica se a conta corrente está habilitada para internet banking (true) ou não (false).';
COMMENT ON COLUMN public.conta_corrente.boleto_faturamento			IS 'Indica se a conta corrente está habilitada para emissão de boletos para o faturamento (true) ou não (false).';
COMMENT ON COLUMN public.conta_corrente.cheque_anterior				IS 'Indica o nº do último cheque da conta corrente utilizado.';
COMMENT ON COLUMN public.conta_corrente.espaco_antes_valor			IS 'Indica, na impressão do cheque da conta corrente, o nº de espaços antes do valor.';
COMMENT ON COLUMN public.conta_corrente.salto_valor_extenso			IS 'Indica, na impressão do cheque da conta corrente, o nº de linhas de salto entre o valor e a 1ª linha do valor por extenso.';
COMMENT ON COLUMN public.conta_corrente.espaco_antes_extenso		IS 'Indica, na impressão do cheque da conta corrente, o nº de espaços antes da 1ª linha do valor por extenso.';
COMMENT ON COLUMN public.conta_corrente.salto_extenso_extenso2		IS 'Indica, na impressão do cheque da conta corrente, o nº de linhas de salto entre a 1ª linha do valor por extenso e a 2ª linha do valor por extenso.';
COMMENT ON COLUMN public.conta_corrente.espaco_antes_extenso2		IS 'Indica, na impressão do cheque da conta corrente, o nº de espaços antes da 2ª linha do valor por extenso.';
COMMENT ON COLUMN public.conta_corrente.salto_extenso2_favorecido	IS 'Indica, na impressão do cheque da conta corrente, o nº de linhas de salto entre a 2ª linha do valor por extenso e o nome do favorecido.';
COMMENT ON COLUMN public.conta_corrente.espaco_antes_favorecido		IS 'Indica, na impressão do cheque da conta corrente, o nº de espaços antes do nome do favorecido.';
COMMENT ON COLUMN public.conta_corrente.salto_favorecido_data		IS 'Indica, na impressão do cheque da conta corrente, o nº de linhas de salto entre o nome do favorecido e a data do cheque.';
COMMENT ON COLUMN public.conta_corrente.espaco_antes_data			IS 'Indica, na impressão do cheque da conta corrente, o nº de espaços antes da data do cheque.';
COMMENT ON COLUMN public.conta_corrente.salto_endosso				IS 'Indica, na impressão do endosso (verso do cheque) da conta corrente, o nº de linhas de salto antes do início do endosso.';
COMMENT ON COLUMN public.conta_corrente.espaco_antes_endosso		IS 'Indica, na impressão do endosso (verso do cheque) da conta corrente, o nº de espaços antes do endosso.';
COMMENT ON COLUMN public.conta_corrente.espaco_antes_assinatura		IS 'Indica, na impressão do endosso (verso do cheque) da conta corrente, o nº de espaços antes da assinatura.';
COMMENT ON COLUMN public.conta_corrente.identificador_cliente		IS 'Código identificado do cliente na instituição financeira (interface bancária).';
COMMENT ON COLUMN public.conta_corrente.identificador_fise			IS 'Código identificado FISE do cliente na instituição financeira (interface bancária).';
COMMENT ON COLUMN public.conta_corrente.prazo_processamento_ib		IS 'Prazo máximo, em horas, para processamento de um arquivo enviado à instituição financeira. Após este prazo, e somente após ele, será permitida a conciliação manual da RB.';
COMMENT ON COLUMN public.conta_corrente.criador						IS 'Responsável (login) pela criação da conta corrente.';
COMMENT ON COLUMN public.conta_corrente.data_criacao				IS 'Data/hora de criação da conta corrente.';
COMMENT ON COLUMN public.conta_corrente.atualizador					IS 'Responsável (login) pela última atualização da conta corrente.';
COMMENT ON COLUMN public.conta_corrente.data_atualizacao			IS 'Data/hora da última atualização da conta corrente.';

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.conta_corrente					ADD CONSTRAINT fk_conta_corrente_dados_bancarios 
    FOREIGN KEY (codigo)							REFERENCES public.dados_bancarios (codigo);
ALTER TABLE public.conta_corrente					ADD CONSTRAINT fk_conta_corrente_conta_contabil 
    FOREIGN KEY (codigo_conta_contabil)				REFERENCES public.conta_contabil (codigo);

ALTER TABLE public.relacao_bancaria					ADD CONSTRAINT fk_relacao_bancaria_conta_corrente 
    FOREIGN KEY (codigo_conta_corrente)				REFERENCES public.conta_corrente (codigo);

ALTER TABLE public.lancamento						ADD CONSTRAINT fk_lancamento_conta_corrente 
    FOREIGN KEY (codigo_conta_corrente)				REFERENCES public.conta_corrente (codigo);

ALTER TABLE public.faturamento						ADD CONSTRAINT fk_faturamento_conta_corrente 
    FOREIGN KEY (codigo_conta_corrente)				REFERENCES public.conta_corrente (codigo);

/* ---------------------------------------------------------------------- */
/* Updata data                                                            */
/* ---------------------------------------------------------------------- */
UPDATE	conta_corrente
SET		boleto_faturamento = TRUE
FROM	dados_bancarios db
WHERE	conta_corrente.codigo = db.codigo
AND		db.codigo_banco = 341;
