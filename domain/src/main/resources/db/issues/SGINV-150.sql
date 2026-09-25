/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-11-10 11:00                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Create sequence                                                        */
/* ---------------------------------------------------------------------- */
CREATE SEQUENCE public.seq_cheque_conta_corrente							INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE public.seq_conciliacao_bancaria								INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE public.seq_conciliacao_lancamento							INCREMENT 1 MINVALUE 1 START 1;
CREATE SEQUENCE public.seq_conciliacao_lancamento_associacao				INCREMENT 1 MINVALUE 1 START 1;

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.conta_corrente		DROP CONSTRAINT fk_conta_corrente_conta_contabil;
ALTER TABLE public.conta_corrente		DROP CONSTRAINT fk_conta_corrente_dados_bancarios;

ALTER TABLE public.relacao_bancaria		DROP CONSTRAINT fk_relacao_bancaria_conta_corrente;

ALTER TABLE public.lancamento			DROP CONSTRAINT fk_lancamento_conta_corrente;

ALTER TABLE public.faturamento			DROP CONSTRAINT fk_faturamento_conta_corrente;

ALTER TABLE public.nota_saida_parcela	DROP CONSTRAINT fk_nota_saida_parcela_conta_corrente_receb;
ALTER TABLE public.nota_saida_parcela	DROP CONSTRAINT fk_nota_saida_parcela_conta_corrente_previsao;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "conta_corrente"                               */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.conta_corrente		DROP CONSTRAINT pk_conta_corrente;

/* Drop check constraints */
ALTER TABLE public.conta_corrente		DROP CONSTRAINT ck_conta_corrente_tipo;

/* Create temporary table */
CREATE TABLE public.conta_corrente_TMP (
    codigo						BIGINT									NOT NULL,
    codigo_conta_contabil		BIGINT,
    tipo						SMALLINT				DEFAULT 0		NOT NULL,
    cheque_carbono				BOOLEAN					DEFAULT true	NOT NULL,
    internet_banking			BOOLEAN					DEFAULT false	NOT NULL,
    boleto_faturamento			BOOLEAN					DEFAULT false	NOT NULL,
    conciliacao_bancaria		BOOLEAN					DEFAULT false	NOT NULL,
    cheque_anterior				BIGINT					DEFAULT 0		NOT NULL,
    cheque_inicial				BIGINT					DEFAULT 0		NOT NULL,
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

/* Move data to the temporary table */
INSERT INTO public.conta_corrente_TMP
    (codigo,codigo_conta_contabil,tipo,cheque_carbono,internet_banking,boleto_faturamento,cheque_anterior,espaco_antes_valor,salto_valor_extenso,espaco_antes_extenso,salto_extenso_extenso2,espaco_antes_extenso2,salto_extenso2_favorecido,espaco_antes_favorecido,salto_favorecido_data,espaco_antes_data,salto_endosso,espaco_antes_endosso,espaco_antes_assinatura,identificador_cliente,identificador_fise,prazo_processamento_ib,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_conta_contabil,tipo,cheque_carbono,internet_banking,boleto_faturamento,cheque_anterior,espaco_antes_valor,salto_valor_extenso,espaco_antes_extenso,salto_extenso_extenso2,espaco_antes_extenso2,salto_extenso2_favorecido,espaco_antes_favorecido,salto_favorecido_data,espaco_antes_data,salto_endosso,espaco_antes_endosso,espaco_antes_assinatura,identificador_cliente,identificador_fise,prazo_processamento_ib,criador,data_criacao,atualizador,data_atualizacao
FROM public.conta_corrente;

/* Drop indexes */
DROP INDEX public.ix_conta_corrente_conta_contabil;

/* Drop "old" table */
DROP TABLE public.conta_corrente;

/* Rename temporary table to "new" table */
ALTER TABLE public.conta_corrente_TMP
	RENAME TO conta_corrente;

/* Add primary key */
ALTER TABLE public.conta_corrente
	ADD CONSTRAINT pk_conta_corrente 
    PRIMARY KEY (codigo);

/* Create constraint */
ALTER TABLE public.conta_corrente
	ADD CONSTRAINT ck_conta_corrente_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1, 2]));

/* Create index */
CREATE INDEX ix_conta_corrente_conta_contabil	ON public.conta_corrente (codigo_conta_contabil);

/* Comments */
COMMENT ON TABLE public.conta_corrente								IS 'Contas correntes.';
COMMENT ON COLUMN public.conta_corrente.codigo						IS 'Código interno da conta corrente (código interno dos dados bancários).';
COMMENT ON COLUMN public.conta_corrente.codigo_conta_contabil		IS 'Código interno da conta contábil relacionada a conta corrente.';
COMMENT ON COLUMN public.conta_corrente.tipo						IS 'Código interno do tipo da conta corrente: 0 = Normal / 1 = Apenas usada para transferências (e recebimento) / 2 = Inativa.';
COMMENT ON COLUMN public.conta_corrente.cheque_carbono				IS 'Indica se o talão de cheques da conta corrente possui carbono (true) ou não (false).';
COMMENT ON COLUMN public.conta_corrente.internet_banking			IS 'Indica se a conta corrente está habilitada para internet banking (true) ou não (false).';
COMMENT ON COLUMN public.conta_corrente.boleto_faturamento			IS 'Indica se a conta corrente está habilitada para emissão de boletos para o faturamento (true) ou não (false).';
COMMENT ON COLUMN public.conta_corrente.conciliacao_bancaria		IS 'Indica se a conta corrente está habilitada para conciliação bancária (true) ou não (false).';
COMMENT ON COLUMN public.conta_corrente.cheque_anterior				IS 'Indica o nº do último cheque da conta corrente utilizado.';
COMMENT ON COLUMN public.conta_corrente.cheque_inicial				IS 'Nº do 1º cheque da conta corrente considerado para a conciliação bancária.';
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

/* Set owner */
ALTER TABLE public.conta_corrente OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add table "cheque_conta_corrente"                                      */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.cheque_conta_corrente (
    codigo						BIGINT			DEFAULT nextval('seq_cheque_conta_corrente')	NOT NULL,
    codigo_conta_corrente		BIGINT															NOT NULL,
    codigo_resp_conciliacao		BIGINT,
    codigo_resp_cancelamento	BIGINT,
    cheque						BIGINT															NOT NULL,
    situacao					SMALLINT		DEFAULT 0										NOT NULL,
    valor						NUMERIC(19,2)	DEFAULT 0										NOT NULL,
    data_compensacao			DATE,
    data_conciliacao			TIMESTAMP,
    data_cancelamento			TIMESTAMP,
    criador						CHARACTER VARYING(255),
    data_criacao				TIMESTAMP,
    atualizador					CHARACTER VARYING(255),
    data_atualizacao			TIMESTAMP,
    CONSTRAINT pk_cheque_conta_corrente PRIMARY KEY (codigo)
);

/* Create index */
CREATE INDEX ix_cheque_conta_corrente_conta_corrente				ON cheque_conta_corrente (codigo_conta_corrente);
CREATE INDEX ix_cheque_conta_corrente_resp_conciliacao				ON cheque_conta_corrente (codigo_resp_conciliacao);
CREATE INDEX ix_cheque_conta_corrente_resp_cancelamento				ON cheque_conta_corrente (codigo_resp_cancelamento);
CREATE UNIQUE INDEX uq_cheque_conta_corrente_conta_corrente_cheque	ON cheque_conta_corrente (codigo_conta_corrente,cheque);

/* Create constraint */
ALTER TABLE cheque_conta_corrente
	ADD CONSTRAINT ck_cheque_conta_corrente_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1, 2, 3]));

/* Comments */
COMMENT ON TABLE public.cheque_conta_corrente							IS 'Cheques emitidos para as contas correntes.';
COMMENT ON COLUMN public.cheque_conta_corrente.codigo					IS 'Código interno do cheque emitido para a conta corrente. Sequência: seq_cheque_conta_corrente.';
COMMENT ON COLUMN public.cheque_conta_corrente.codigo_conta_corrente	IS 'Código interno da conta corrente.';
COMMENT ON COLUMN public.cheque_conta_corrente.codigo_resp_conciliacao	IS 'Código interno (código interno da pessoa) do responsável pela conciliação (confrontamento entre o que foi enviado ao banco e o que foi realmente pago) do cheque.';
COMMENT ON COLUMN public.cheque_conta_corrente.codigo_resp_cancelamento	IS 'Código interno (código interno da pessoa) do responsável pelo cancelamento do cheque.';
COMMENT ON COLUMN public.cheque_conta_corrente.cheque					IS 'Nº do cheque.';
COMMENT ON COLUMN public.cheque_conta_corrente.situacao					IS 'Situação do cheque: 0 = Emitido (a compensar) / 1 = Pago (compensado) / 2 = Estornado (pagamento não aprovado pela instituição financeira) / 3 = Cancelado.';
COMMENT ON COLUMN public.cheque_conta_corrente.valor					IS 'Valor total a pagar do cheque.';
COMMENT ON COLUMN public.cheque_conta_corrente.data_compensacao			IS 'Data de compensação do cheque na instituição financeira.';
COMMENT ON COLUMN public.cheque_conta_corrente.data_conciliacao			IS 'Data/hora da conciliação bancária (cheque) da relação bancária.';
COMMENT ON COLUMN public.cheque_conta_corrente.data_cancelamento		IS 'Data/hora de cancelamento da relação bancária.';
COMMENT ON COLUMN public.cheque_conta_corrente.criador					IS 'Responsável (login) pela criação do cheque.';
COMMENT ON COLUMN public.cheque_conta_corrente.data_criacao				IS 'Data/hora de criação do cheque.';
COMMENT ON COLUMN public.cheque_conta_corrente.atualizador				IS 'Responsável (login) pela última atualização do cheque.';
COMMENT ON COLUMN public.cheque_conta_corrente.data_atualizacao			IS 'Data/hora da última atualização do cheque.';

/* Set owner */
ALTER TABLE public.cheque_conta_corrente OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add table "cheque_conta_corrente_item_relacao"                         */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.cheque_conta_corrente_item_relacao (
    codigo_cheque_conta_corrente	BIGINT	NOT NULL,
    codigo_item_relacao				BIGINT	NOT NULL,
    CONSTRAINT pk_cheque_conta_corrente_item_relacao PRIMARY KEY (codigo_cheque_conta_corrente, codigo_item_relacao)
);

/* Create index */
CREATE INDEX ix_cheque_conta_corrente_item_relacao_cheque_conta_corrente	ON cheque_conta_corrente_item_relacao (codigo_cheque_conta_corrente);
CREATE INDEX ix_cheque_conta_corrente_item_relacao_item_relacao				ON cheque_conta_corrente_item_relacao (codigo_item_relacao);

/* Comments */
COMMENT ON TABLE public.cheque_conta_corrente_item_relacao									IS 'Relacionamento entre o cheque emitidos de uma conta corrente e o(s) pagamento(s).';
COMMENT ON COLUMN public.cheque_conta_corrente_item_relacao.codigo_cheque_conta_corrente	IS 'Código interno do cheque emitido para a conta corrente.';
COMMENT ON COLUMN public.cheque_conta_corrente_item_relacao.codigo_item_relacao				IS 'Código interno do item da relação bancária.';

/* Set owner */
ALTER TABLE public.cheque_conta_corrente_item_relacao OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add table "conciliacao_bancaria"                                       */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.conciliacao_bancaria (
    codigo					BIGINT					DEFAULT nextval('seq_conciliacao_bancaria')	NOT NULL,
    codigo_conta_corrente	BIGINT																NOT NULL,
    codigo_anexo			BIGINT																NOT NULL,
    data					DATE																NOT NULL,
    situacao				SMALLINT				DEFAULT 0									NOT NULL,
    total_debito			NUMERIC(19,2)			DEFAULT 0.00								NOT NULL,
    total_credito			NUMERIC(19,2)			DEFAULT 0.00								NOT NULL,
    saldo					NUMERIC(19,2)			DEFAULT 0.00								NOT NULL,
    criador					CHARACTER VARYING(255),
    data_criacao			TIMESTAMP,
    atualizador				CHARACTER VARYING(255),
    data_atualizacao		TIMESTAMP,
    CONSTRAINT pk_conciliacao_bancaria PRIMARY KEY (codigo)
);

/* Create index */
CREATE INDEX ix_conciliacao_bancaria_conta_corrente	ON conciliacao_bancaria (codigo_conta_corrente);
CREATE INDEX ix_conciliacao_bancaria_anexo			ON conciliacao_bancaria (codigo_anexo);

/* Comments */
COMMENT ON TABLE public.conciliacao_bancaria						IS 'Conciliação bancária realizada.';
COMMENT ON COLUMN public.conciliacao_bancaria.codigo				IS 'Código interno da conciliação bancária realizada. Sequência: seq_conciliacao_bancaria.';
COMMENT ON COLUMN public.conciliacao_bancaria.codigo_conta_corrente	IS 'Código interno da conta corrente que foi feita a conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_bancaria.codigo_anexo			IS 'Código interno do arquivo anexado para a conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_bancaria.data					IS 'Data da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_bancaria.situacao				IS 'Situação da conciliação bancária: 0 = Conciliada com pendências / 1 = Conciliada sem pendências / 2 = Cancelada.';
COMMENT ON COLUMN public.conciliacao_bancaria.total_debito			IS 'Total de débitos da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_bancaria.total_credito			IS 'Total de créditos da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_bancaria.saldo					IS 'Saldo final da data.';
COMMENT ON COLUMN public.conciliacao_bancaria.criador				IS 'Responsável (login) pela criação da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_bancaria.data_criacao			IS 'Data/hora de criação da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_bancaria.atualizador			IS 'Responsável (login) pela última atualização da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_bancaria.data_atualizacao		IS 'Data/hora da última atualização da conciliação bancária.';

/* Set owner */
ALTER TABLE public.conciliacao_bancaria OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add table "conciliacao_lancamento"                                     */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.conciliacao_lancamento (
    codigo						BIGINT					DEFAULT nextval('seq_conciliacao_lancamento')	NOT NULL,
    codigo_conciliacao_bancaria	BIGINT,
    registro					SMALLINT																NOT NULL,
    natureza					CHARACTER(3)															NOT NULL,
    lancamento					DATE																	NOT NULL,
    situacao					SMALLINT				DEFAULT 0										NOT NULL,
    historico					CHARACTER VARYING(60)													NOT NULL,
    valor						NUMERIC(19,2)															NOT NULL,
    debito_credito				CHARACTER(1)															NOT NULL,
    CONSTRAINT pk_conciliacao_lancamento PRIMARY KEY (codigo)
);

/* Create index */
CREATE INDEX ix_conciliacao_lancamento_conciliacao_bancaria	ON conciliacao_lancamento (codigo_conciliacao_bancaria);

/* Create constraint */
ALTER TABLE public.conciliacao_lancamento
	ADD CONSTRAINT ck_conciliacao_lancamento_debito_credito 
    CHECK ((debito_credito) = ANY (ARRAY['D', 'C']));

/* Comments */
COMMENT ON TABLE public.conciliacao_lancamento								IS 'Lançamentos da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_lancamento.codigo						IS 'Código interno do lançamento na conciliação bancária. Sequência: seq_conciliacao_lancamento.';
COMMENT ON COLUMN public.conciliacao_lancamento.codigo_conciliacao_bancaria	IS 'Código interno da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_lancamento.registro					IS 'Número do registro do lançamento na conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_lancamento.natureza					IS 'Natureza do lançamento na conciliação bancária: CDS = Lançamento composto de recursos disponíveis e bloqueados / DPV = Lançamento ocorrido em recurso "disponível" / SCR = Lançamento ocorrido em recurso "disponível", porém não liberado para uso / SSR = Lançamento ocorrido em recurso "a compensar".';
COMMENT ON COLUMN public.conciliacao_lancamento.lancamento					IS 'Data do lançamento na conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_lancamento.situacao					IS 'Situação do lançamento na conciliação bancária: 0 = Pendente / 1 = Não pendente / 2 = Cancelado.';
COMMENT ON COLUMN public.conciliacao_lancamento.historico					IS 'Histórico do lançamento na conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_lancamento.valor						IS 'Valor do lançamento na conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_lancamento.debito_credito				IS 'Indica se o lançamento na conciliação bancária é de débito (D) ou crédito (C).';

/* Set owner */
ALTER TABLE public.conciliacao_lancamento OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add table "conciliacao_lancamento_associacao"                          */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.conciliacao_lancamento_associacao (
    codigo							BIGINT		DEFAULT nextval('seq_conciliacao_lancamento_associacao')	NOT NULL,
    codigo_conciliacao_lancamento	BIGINT																	NOT NULL,
    codigo_item_relacao				BIGINT,
    codigo_lancamento_tesouraria	BIGINT,
    codigo_nota_saida_parcela		BIGINT,
    tipo							SMALLINT																NOT NULL,
    CONSTRAINT pk_conciliacao_lancamento_associacao PRIMARY KEY (codigo)
);

/* Create index */
CREATE INDEX ix_conciliacao_lancamento_associacao_conciliacao_lancamento	ON conciliacao_lancamento_associacao (codigo_conciliacao_lancamento);
CREATE INDEX ix_conciliacao_lancamento_associacao_item_relacao				ON conciliacao_lancamento_associacao (codigo_item_relacao);
CREATE INDEX ix_conciliacao_lancamento_associacao_lancamento_tesouraria		ON conciliacao_lancamento_associacao (codigo_lancamento_tesouraria);
CREATE INDEX ix_conciliacao_lancamento_associacao_nota_saida_parcela		ON conciliacao_lancamento_associacao (codigo_nota_saida_parcela);

/* Create constraint */
ALTER TABLE public.conciliacao_lancamento_associacao
	ADD CONSTRAINT cc_conciliacao_lancamento_associacao_codigo_item 
    CHECK ((codigo_item_relacao IS NOT NULL) OR (codigo_item_relacao IS NOT NULL) OR (codigo_nota_saida_parcela IS NOT NULL));

ALTER TABLE public.conciliacao_lancamento_associacao
	ADD CONSTRAINT ck_conciliacao_lancamento_associacao_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1, 2]));

/* Comments */
COMMENT ON TABLE public.conciliacao_lancamento_associacao									IS 'Associação entre um lançamento de conciliação bancária e um item de RB, lançamento de Tesouraria, NF/Saída, etc., ou seja, com o(s) lançamento(s) de débito(s) ou crédito(s).';
COMMENT ON COLUMN public.conciliacao_lancamento_associacao.codigo							IS 'Código interno da associação entre o lançamento da conciliação bancária e um dos itens. Sequência: seq_conciliacao_lancamento_associacao.';
COMMENT ON COLUMN public.conciliacao_lancamento_associacao.codigo_conciliacao_lancamento	IS 'Código interno do lançamento na conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_lancamento_associacao.codigo_item_relacao				IS 'Código interno do item da relação bancária associada ao lançamanto da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_lancamento_associacao.codigo_lancamento_tesouraria		IS 'Código interno do lançamento relacionado à Tesouraria.';
COMMENT ON COLUMN public.conciliacao_lancamento_associacao.codigo_nota_saida_parcela		IS 'Código interno da parcela da nota de saída associada ao lançamanto da conciliação bancária.';
COMMENT ON COLUMN public.conciliacao_lancamento_associacao.tipo								IS 'Tipo da associação entre o lançamento da conciliação bancária e um dos itens: 0 = Associação com item de relação bancária / 1 = Associação com lançamento de Tesouraria / 2 = Associação com parcela de nota de saída.';

/* Set owner */
ALTER TABLE public.conciliacao_lancamento_associacao OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.conta_corrente									ADD CONSTRAINT fk_conta_corrente_dados_bancarios 
    FOREIGN KEY (codigo)											REFERENCES public.dados_bancarios (codigo);
ALTER TABLE public.conta_corrente									ADD CONSTRAINT fk_conta_corrente_conta_contabil 
    FOREIGN KEY (codigo_conta_contabil)								REFERENCES public.conta_contabil (codigo);

ALTER TABLE public.cheque_conta_corrente							ADD CONSTRAINT fk_cheque_conta_corrente_conta_corrente 
    FOREIGN KEY (codigo_conta_corrente)								REFERENCES public.conta_corrente (codigo);
ALTER TABLE public.cheque_conta_corrente							ADD CONSTRAINT fk_cheque_conta_corrente_conciliacao 
    FOREIGN KEY (codigo_resp_conciliacao)							REFERENCES public.pessoa (codigo);
ALTER TABLE public.cheque_conta_corrente							ADD CONSTRAINT fk_cheque_conta_corrente_cancelamento 
    FOREIGN KEY (codigo_resp_cancelamento)							REFERENCES public.pessoa (codigo);

ALTER TABLE public.cheque_conta_corrente_item_relacao				ADD CONSTRAINT fk_cheque_conta_corrente_item_relacao_cheque_conta_corrente 
    FOREIGN KEY (codigo_cheque_conta_corrente)						REFERENCES public.cheque_conta_corrente (codigo);

ALTER TABLE public.cheque_conta_corrente_item_relacao				ADD CONSTRAINT fk_cheque_conta_corrente_item_relacao_item_relacao 
    FOREIGN KEY (codigo_item_relacao)								REFERENCES public.item_relacao (codigo);

ALTER TABLE public.conciliacao_bancaria								ADD CONSTRAINT fk_conciliacao_bancaria_conta_corrente 
    FOREIGN KEY (codigo_conta_corrente)								REFERENCES public.conta_corrente (codigo);
ALTER TABLE public.conciliacao_bancaria								ADD CONSTRAINT fk_conciliacao_bancaria_anexo 
    FOREIGN KEY (codigo_anexo)										REFERENCES public.anexo (codigo);

ALTER TABLE public.conciliacao_lancamento							ADD CONSTRAINT fk_conciliacao_lancamento_conciliacao_bancaria 
    FOREIGN KEY (codigo_conciliacao_bancaria)						REFERENCES public.conciliacao_bancaria (codigo);

ALTER TABLE public.conciliacao_lancamento_associacao				ADD CONSTRAINT fk_conciliacao_lancamento_associacao_conciliacao_lancamento 
    FOREIGN KEY (codigo_conciliacao_lancamento)						REFERENCES public.conciliacao_lancamento (codigo);
ALTER TABLE public.conciliacao_lancamento_associacao				ADD CONSTRAINT fk_conciliacao_lancamento_associacao_nota_saida_parcela 
    FOREIGN KEY (codigo_nota_saida_parcela)							REFERENCES public.nota_saida_parcela (codigo);
ALTER TABLE public.conciliacao_lancamento_associacao				ADD CONSTRAINT fk_conciliacao_lancamento_associacao_item_relacao 
    FOREIGN KEY (codigo_item_relacao)								REFERENCES public.item_relacao (codigo);
ALTER TABLE public.conciliacao_lancamento_associacao				ADD CONSTRAINT fk_conciliacao_lancamento_associacao_lancamento 
    FOREIGN KEY (codigo_lancamento_tesouraria)						REFERENCES public.lancamento (codigo);

ALTER TABLE public.relacao_bancaria									ADD CONSTRAINT fk_relacao_bancaria_conta_corrente 
    FOREIGN KEY (codigo_conta_corrente)								REFERENCES public.conta_corrente (codigo);

ALTER TABLE public.lancamento										ADD CONSTRAINT fk_lancamento_conta_corrente 
    FOREIGN KEY (codigo_conta_corrente)								REFERENCES public.conta_corrente (codigo);

ALTER TABLE public.faturamento										ADD CONSTRAINT fk_faturamento_conta_corrente 
    FOREIGN KEY (codigo_conta_corrente)								REFERENCES public.conta_corrente (codigo);

ALTER TABLE public.nota_saida_parcela								ADD CONSTRAINT fk_nota_saida_parcela_conta_corrente_receb 
    FOREIGN KEY (codigo_conta_corrente_recebimento)					REFERENCES public.conta_corrente (codigo);
ALTER TABLE public.nota_saida_parcela								ADD CONSTRAINT fk_nota_saida_parcela_conta_corrente_previsao 
    FOREIGN KEY (codigo_conta_corrente_prevista)					REFERENCES public.conta_corrente (codigo);

/* ---------------------------------------------------------------------- */
/* Add data                                                               */
/* ---------------------------------------------------------------------- */
/* Parâmetro */
INSERT
INTO	parametro (chave, valor)
VALUES	('tesouraria.conciliacao.anexo.diretorio', '/var/lib/sgi/conciliacao/');
