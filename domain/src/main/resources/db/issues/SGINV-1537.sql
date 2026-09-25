/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2018-03-29 15:42                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Create sequence                                                        */
/* ---------------------------------------------------------------------- */
CREATE SEQUENCE public.seq_cheque_conta_corrente_doc_impressao INCREMENT 1 MINVALUE 1 START 1;

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_resp_cancelamento;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_resp_recebimento;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_conta_corrente_receb;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_conta_corrente_previsao;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_nota_saida;

ALTER TABLE public.cheque_conta_corrente DROP CONSTRAINT fk_cheque_conta_corrente_cancelamento;
ALTER TABLE public.cheque_conta_corrente DROP CONSTRAINT fk_cheque_conta_corrente_conciliacao;
ALTER TABLE public.cheque_conta_corrente DROP CONSTRAINT fk_cheque_conta_corrente_conta_corrente;

ALTER TABLE public.motivo_retorno_documento_nota_saida_parcela DROP CONSTRAINT fk_motivo_retorno_documento_ns_parcela_nota_saida_parcela;

ALTER TABLE public.nota_saida_parcela_retencao DROP CONSTRAINT fk_nota_saida_parcela_retencao_nota_saida_parcela;

ALTER TABLE public.cheque_conta_corrente_item_relacao DROP CONSTRAINT fk_cheque_conta_corrente_item_relacao_cheque_conta_corrente;

ALTER TABLE public.conciliacao_lancamento_associacao DROP CONSTRAINT fk_conciliacao_lancamento_associacao_nota_saida_parcela;

ALTER TABLE public.motivo_retorno_documento_cheque_conta_corrente DROP CONSTRAINT fk_mot_ret_documento_cheque_conta_corrente_cheque;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "documentos_impressao"                         */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.documentos_impressao DROP CONSTRAINT pk_documentos_impressao;

/* Create "new" table */
CREATE TABLE public.documentos_impressao_TMP (
    codigo			BIGINT					DEFAULT nextval('seq_doc_impressao')	NOT NULL,
    tipo			SMALLINT														NOT NULL,
    status			SMALLINT														NOT NULL,
    texto_impressao	TEXT															NOT NULL,
    data_impressao	TIMESTAMP,
    criador			CHARACTER VARYING(100)											NOT NULL,
    data_criacao	TIMESTAMP														NOT NULL);

/* Move data from "old" table to "new" table */
INSERT INTO public.documentos_impressao_TMP
    (codigo,tipo,status,texto_impressao,data_impressao,criador,data_criacao)
SELECT
    codigo,tipo,status,texto_impressao,data_impressao,criador,data_criacao
FROM public.documentos_impressao;

/* Drop "old" table  */
DROP TABLE public.documentos_impressao;

/* Rename "old" table to "new" table */
ALTER TABLE public.documentos_impressao_TMP
	RENAME TO documentos_impressao;

/* Create primary key */
ALTER TABLE public.documentos_impressao
	ADD CONSTRAINT pk_documentos_impressao 
    PRIMARY KEY (codigo);

/* Create constraints */
ALTER TABLE public.documentos_impressao
	ADD CONSTRAINT ck_documentos_impressao_status 
    CHECK (status = ANY (ARRAY[0, 1, 2]));

ALTER TABLE public.documentos_impressao
	ADD CONSTRAINT ck_documentos_impressao_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1, 2]));

/* Comments */
COMMENT ON TABLE public.documentos_impressao					IS 'Documentos (cheques) para impressão local.';
COMMENT ON COLUMN public.documentos_impressao.codigo			IS 'Código interno do documento (cheque) para impressão. Sequência: seq_documentos_impressao.';
COMMENT ON COLUMN public.documentos_impressao.tipo				IS 'Tipo da impressão: 0 = Cheque / 1 = Endosso / 2 = Cópia (2ª via).';
COMMENT ON COLUMN public.documentos_impressao.status			IS 'Situação da impressão: 0 = Liberado para impressão / 1 = Em processo de impressão / 2 = Impresso.';
COMMENT ON COLUMN public.documentos_impressao.texto_impressao	IS 'Conteúdo para impressão.';
COMMENT ON COLUMN public.documentos_impressao.data_impressao	IS 'Data/hora da impressão.';
COMMENT ON COLUMN public.documentos_impressao.criador			IS 'Responsável (login) pela criação da impressão.';
COMMENT ON COLUMN public.documentos_impressao.data_criacao		IS 'Data/hora de criação da impressão.';

/* Set owner */
ALTER TABLE public.documentos_impressao OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "nota_saida_parcela"                           */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT pk_nota_saida_parcela;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT ck_nota_saida_parcela_situacao;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT ck_nota_saida_parcela_tipo_documento_previsto;
ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT ck_nota_saida_parcela_tipo_documento_recebimento;

/* Create "new" table */
CREATE TABLE public.nota_saida_parcela_TMP (
    codigo								BIGINT					DEFAULT nextval('seq_nota_saida_parcela')	NOT NULL,
    codigo_nota_saida					BIGINT																NOT NULL,
    codigo_conta_corrente_prevista		BIGINT,
    codigo_resp_recebimento				BIGINT,
    codigo_resp_canc_recebimento		BIGINT,
    codigo_resp_cancelamento			BIGINT,
    codigo_conta_corrente_recebimento	BIGINT,
    ordem								SMALLINT															NOT NULL,
    situacao							SMALLINT				DEFAULT 0									NOT NULL,
    valor_bruto							NUMERIC(19,2)														NOT NULL,
    valor_desconto						NUMERIC(9,2)			DEFAULT 0.00								NOT NULL,
    valor_multa							NUMERIC(9,2)			DEFAULT 0.00								NOT NULL,
    valor_juros							NUMERIC(9,2)			DEFAULT 0.00								NOT NULL,
    valor_permuta						NUMERIC(9,2)			DEFAULT 0.00								NOT NULL,
    valor_devolucao						NUMERIC(9,2)			DEFAULT 0.00								NOT NULL,
    valor_liquido						NUMERIC(19,2)			DEFAULT 0									NOT NULL,
    data_vencimento						DATE																NOT NULL,
    data_pagamento						DATE																NOT NULL,
    data_contabilizacao					DATE,
    motivo_data_vencimento				CHARACTER VARYING(200),
    motivo_data_pagamento				CHARACTER VARYING(200),
    motivo_devolucao					CHARACTER VARYING(200),
    motivo_desconto						CHARACTER VARYING(200),
    tipo_documento_previsto				SMALLINT,
    codigo_barras_boleto				CHARACTER VARYING(44),
    tipo_documento_recebimento			SMALLINT,
    data_resp_recebimento				TIMESTAMP,
    data_resp_canc_recebimento			TIMESTAMP,
    data_resp_cancelamento				TIMESTAMP,
    criador								CHARACTER VARYING(255),
    data_criacao						TIMESTAMP,
    atualizador							CHARACTER VARYING(255),
    data_atualizacao					TIMESTAMP);

/* Move data from "old" table to "new" table */
INSERT INTO public.nota_saida_parcela_TMP
    (codigo,codigo_nota_saida,codigo_conta_corrente_prevista,codigo_resp_recebimento,codigo_resp_cancelamento,codigo_conta_corrente_recebimento,ordem,situacao,valor_bruto,valor_desconto,valor_multa,valor_juros,valor_permuta,valor_devolucao,valor_liquido,data_vencimento,data_pagamento,data_contabilizacao,motivo_data_vencimento,motivo_data_pagamento,motivo_devolucao,motivo_desconto,tipo_documento_previsto,codigo_barras_boleto,tipo_documento_recebimento,data_resp_recebimento,data_resp_cancelamento,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_nota_saida,codigo_conta_corrente_prevista,codigo_resp_recebimento,codigo_resp_cancelamento,codigo_conta_corrente_recebimento,ordem,situacao,valor_bruto,valor_desconto,valor_multa,valor_juros,valor_permuta,valor_devolucao,valor_liquido,data_vencimento,data_pagamento,data_contabilizacao,motivo_data_vencimento,motivo_data_pagamento,motivo_devolucao,motivo_desconto,tipo_documento_previsto,codigo_barras_boleto,tipo_documento_recebimento,data_resp_recebimento,data_resp_cancelamento,criador,data_criacao,atualizador,data_atualizacao
FROM public.nota_saida_parcela;

/* Drop indexes */
DROP INDEX public.ix_nota_saida_parcela_nota_saida;
DROP INDEX public.ix_nota_saida_parcela_conta_corrente_prevista;
DROP INDEX public.ix_nota_saida_parcela_resp_recebimento;
DROP INDEX public.ix_nota_saida_parcela_resp_cancelamento;
DROP INDEX public.ix_nota_saida_parcela_conta_corrente_recebimento;
DROP INDEX public.uq_nota_saida_parcela_nota_saida_ordem;

/* Drop "old" table  */
DROP TABLE public.nota_saida_parcela;

/* Rename "old" table to "new" table */
ALTER TABLE public.nota_saida_parcela_TMP
	RENAME TO nota_saida_parcela;

/* Create primary key */
ALTER TABLE public.nota_saida_parcela
	ADD CONSTRAINT pk_nota_saida_parcela 
    PRIMARY KEY (codigo);

/* Create constraints */
ALTER TABLE public.nota_saida_parcela
	ADD CONSTRAINT ck_nota_saida_parcela_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1, 2, 3]));

ALTER TABLE public.nota_saida_parcela
	ADD CONSTRAINT ck_nota_saida_parcela_tipo_documento_previsto 
    CHECK (tipo_documento_previsto = ANY (ARRAY[0, 1]));

ALTER TABLE public.nota_saida_parcela
	ADD CONSTRAINT ck_nota_saida_parcela_tipo_documento_recebimento 
    CHECK (tipo_documento_recebimento = ANY (ARRAY[0, 1]));

/* Create indexes */
CREATE INDEX ix_nota_saida_parcela_nota_saida					ON public.nota_saida_parcela (codigo_nota_saida);
CREATE INDEX ix_nota_saida_parcela_conta_corrente_prevista		ON public.nota_saida_parcela (codigo_conta_corrente_prevista);
CREATE INDEX ix_nota_saida_parcela_resp_recebimento				ON public.nota_saida_parcela (codigo_resp_recebimento);
CREATE INDEX ix_nota_saida_parcela_resp_canc_recebimento		ON public.nota_saida_parcela (codigo_resp_canc_recebimento);
CREATE INDEX ix_nota_saida_parcela_resp_cancelamento			ON public.nota_saida_parcela (codigo_resp_cancelamento);
CREATE INDEX ix_nota_saida_parcela_conta_corrente_recebimento	ON public.nota_saida_parcela (codigo_conta_corrente_recebimento);
CREATE UNIQUE INDEX uq_nota_saida_parcela_nota_saida_ordem		ON public.nota_saida_parcela (codigo_nota_saida,ordem);

/* Comments */
COMMENT ON TABLE public.nota_saida_parcela										IS 'Parcelas das notas de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo								IS 'Código interno da parcela da nota de saída.Sequência: seq_nota_saida_parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_nota_saida					IS 'Código interno da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_conta_corrente_prevista		IS 'Código interno da conta corrente prevista para recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_resp_recebimento				IS 'Código interno da pessoa responsável pelo último recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_resp_canc_recebimento		IS 'Código interno da pessoa responsável pelo último cancelamento do recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_resp_cancelamento			IS 'Código interno da pessoa responsável pelo cancelamento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_conta_corrente_recebimento	IS 'Código interno da conta corrente utilizada para recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.ordem								IS 'Ordem da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.situacao							IS 'Situação: 0 = Aberta (NF não processada) / 1 = Em recebimento / 2 = Finalizada (recebida) / 3 = Cancelada.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_bruto							IS 'Valor bruto da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_desconto						IS 'Valor de desconto da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_multa							IS 'Valor de multa recebida da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_juros							IS 'Valor de juros recebido da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_permuta						IS 'Valor de permuta (troca de valores entre o cliente e a AA) da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_devolucao						IS 'Valor de devolução da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.valor_liquido						IS 'Valor líquido da parcela (valor buto - desconto + multa + juros + permuta + retenções - tributos, se for a 1ª parcela).';
COMMENT ON COLUMN public.nota_saida_parcela.data_vencimento						IS 'Data de vencimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.data_pagamento						IS 'Data de pagamento da parcela (até o efetivo pagamento, será igual ao vencimento).';
COMMENT ON COLUMN public.nota_saida_parcela.data_contabilizacao					IS 'Data de contabilidade da parcela (até a efetiva contabilização, será igual ao recebimento).';
COMMENT ON COLUMN public.nota_saida_parcela.motivo_data_vencimento				IS 'Motivo de alteração da data de vencimento.';
COMMENT ON COLUMN public.nota_saida_parcela.motivo_data_pagamento				IS 'Motivo de alteração da data de pagamento.';
COMMENT ON COLUMN public.nota_saida_parcela.motivo_devolucao					IS 'Motivo da devolução.';
COMMENT ON COLUMN public.nota_saida_parcela.motivo_desconto						IS 'Motivo do desconto.';
COMMENT ON COLUMN public.nota_saida_parcela.tipo_documento_previsto				IS 'Tipo de documento de pagamento: 0 = Boleto / 1 = Depósito em conta/transferência';
COMMENT ON COLUMN public.nota_saida_parcela.codigo_barras_boleto				IS 'Código de barras gerado para o boleto da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.tipo_documento_recebimento			IS 'Tipo de documento de recebimento da parcela: 0 = Boleto / 1 = Depósito em conta/transferência';
COMMENT ON COLUMN public.nota_saida_parcela.data_resp_recebimento				IS 'Data/hora de confirmação do último recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.data_resp_canc_recebimento			IS 'Data/hora do último cancelamento do recebimento da parcela.';
COMMENT ON COLUMN public.nota_saida_parcela.data_resp_cancelamento				IS 'Data/hora do cancelamento.';
COMMENT ON COLUMN public.nota_saida_parcela.criador								IS 'Responsável (login) pela criação da parcela da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.data_criacao						IS 'Data/hora de criação da parcela da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.atualizador							IS 'Responsável (login) pela última atualização da parcela da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela.data_atualizacao					IS 'Data/hora da última atualização da parcela da nota de saída.';

/* Set owner */
ALTER TABLE public.nota_saida_parcela OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Modify table "cheque_conta_corrente"                                   */
/* ---------------------------------------------------------------------- */
/* Drop constraint */
ALTER TABLE public.cheque_conta_corrente
	DROP CONSTRAINT ck_cheque_conta_corrente_situacao;

/* Create constraint */
ALTER TABLE public.cheque_conta_corrente
	ADD CONSTRAINT ck_cheque_conta_corrente_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1, 2, 3, 4]));

/* Comments */
COMMENT ON COLUMN public.cheque_conta_corrente.situacao IS 'Situação do cheque: 0 = Emitido (a compensar) / 1 = Pago (compensado) / 2 = Estornado (pagamento não aprovado pela instituição financeira) / 3 = Cancelado (pelo usuário) / 4 - Cancelado (por reimpressão).';

/* ---------------------------------------------------------------------- */
/* Add table "cheque_conta_corrente_doc_impressao"                        */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.cheque_conta_corrente_doc_impressao (
    codigo							BIGINT	DEFAULT nextval('seq_cheque_conta_corrente_doc_impressao')	NOT NULL,
    codigo_cheque_conta_corrente	BIGINT																NOT NULL,
    codigo_documentos_impressao		BIGINT																NOT NULL,
    CONSTRAINT pk_cheque_conta_corrente_doc_impressao PRIMARY KEY (codigo)
);

/* Create indexes */
CREATE INDEX ix_cheque_conta_corrente_doc_impressao_cheque_conta_corrente	ON public.cheque_conta_corrente_doc_impressao (codigo_cheque_conta_corrente);
CREATE INDEX ix_cheque_conta_corrente_doc_impressao_documentos_impressao	ON public.cheque_conta_corrente_doc_impressao (codigo_documentos_impressao);

/* Comments */
COMMENT ON TABLE public.cheque_conta_corrente_doc_impressao									IS 'Relacionamento entre os cheques emitidos e a impressão (do cheque, do endosso e da 2ª via).';
COMMENT ON COLUMN public.cheque_conta_corrente_doc_impressao.codigo							IS 'Código interno do relacionamento entre o cheque e a impressão. Sequência: seq_cheque_conta_corrente_doc_impressao.';
COMMENT ON COLUMN public.cheque_conta_corrente_doc_impressao.codigo_cheque_conta_corrente	IS 'Código interno do cheque.';
COMMENT ON COLUMN public.cheque_conta_corrente_doc_impressao.codigo_documentos_impressao	IS 'Código interno do documentos impresso.';

/* Set owner */
ALTER TABLE public.cheque_conta_corrente_doc_impressao OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_saida_parcela								ADD CONSTRAINT fk_nota_saida_parcela_nota_saida 
    FOREIGN KEY (codigo_nota_saida)									REFERENCES public.nota_saida (codigo);
ALTER TABLE public.nota_saida_parcela								ADD CONSTRAINT fk_nota_saida_parcela_resp_cancelamento 
    FOREIGN KEY (codigo_resp_cancelamento)							REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida_parcela								ADD CONSTRAINT fk_nota_saida_parcela_resp_recebimento 
    FOREIGN KEY (codigo_resp_recebimento)							REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida_parcela								ADD CONSTRAINT fk_nota_saida_parcela_conta_corrente_receb 
    FOREIGN KEY (codigo_conta_corrente_recebimento)					REFERENCES public.conta_corrente (codigo);
ALTER TABLE public.nota_saida_parcela								ADD CONSTRAINT fk_nota_saida_parcela_conta_corrente_previsao 
    FOREIGN KEY (codigo_conta_corrente_prevista)					REFERENCES public.conta_corrente (codigo);
ALTER TABLE public.nota_saida_parcela								ADD CONSTRAINT fk_nota_saida_parcela_resp_canc_recebimento 
    FOREIGN KEY (codigo_resp_canc_recebimento)						REFERENCES public.pessoa (codigo);

ALTER TABLE public.cheque_conta_corrente							ADD CONSTRAINT fk_cheque_conta_corrente_conta_corrente 
    FOREIGN KEY (codigo_conta_corrente)								REFERENCES public.conta_corrente (codigo);
ALTER TABLE public.cheque_conta_corrente							ADD CONSTRAINT fk_cheque_conta_corrente_conciliacao 
    FOREIGN KEY (codigo_resp_conciliacao)							REFERENCES public.pessoa (codigo);
ALTER TABLE public.cheque_conta_corrente							ADD CONSTRAINT fk_cheque_conta_corrente_cancelamento 
    FOREIGN KEY (codigo_resp_cancelamento)							REFERENCES public.pessoa (codigo);

ALTER TABLE public.cheque_conta_corrente_doc_impressao				ADD CONSTRAINT fk_cheque_conta_corrente_doc_impressao_cheque_conta_corrente 
    FOREIGN KEY (codigo_cheque_conta_corrente)						REFERENCES public.cheque_conta_corrente (codigo);
ALTER TABLE public.cheque_conta_corrente_doc_impressao				ADD CONSTRAINT fk_cheque_conta_corrente_doc_impressao_documentos_impressao 
    FOREIGN KEY (codigo_documentos_impressao)						REFERENCES public.documentos_impressao (codigo);

--ALTER TABLE public.nota_saida_parcela_conta_contabil ADD CONSTRAINT fk_nota_saida_parcela_conta_contabil_nota_saida_parcela 
--    FOREIGN KEY (codigo_nota_saida_parcela) REFERENCES public.nota_saida_parcela (codigo);

ALTER TABLE public.motivo_retorno_documento_nota_saida_parcela		ADD CONSTRAINT fk_motivo_retorno_documento_ns_parcela_nota_saida_parcela 
    FOREIGN KEY (codigo_nota_saida_parcela)							REFERENCES public.nota_saida_parcela (codigo);

ALTER TABLE public.nota_saida_parcela_retencao						ADD CONSTRAINT fk_nota_saida_parcela_retencao_nota_saida_parcela 
    FOREIGN KEY (codigo_nota_saida_parcela)							REFERENCES public.nota_saida_parcela (codigo);

ALTER TABLE public.cheque_conta_corrente_item_relacao				ADD CONSTRAINT fk_cheque_conta_corrente_item_relacao_cheque_conta_corrente 
    FOREIGN KEY (codigo_cheque_conta_corrente)						REFERENCES public.cheque_conta_corrente (codigo);

ALTER TABLE public.conciliacao_lancamento_associacao				ADD CONSTRAINT fk_conciliacao_lancamento_associacao_nota_saida_parcela 
    FOREIGN KEY (codigo_nota_saida_parcela)							REFERENCES public.nota_saida_parcela (codigo);

ALTER TABLE public.motivo_retorno_documento_cheque_conta_corrente	ADD CONSTRAINT fk_mot_ret_documento_cheque_conta_corrente_cheque 
    FOREIGN KEY (codigo_cheque_conta_corrente)						REFERENCES public.cheque_conta_corrente (codigo);

/* ---------------------------------------------------------------------- */
/* Add data                                                               */
/* ---------------------------------------------------------------------- */
INSERT	INTO aplicacao_motivo_retorno (codigo, codigo_perfil, nome, chave, tipo, criador, data_criacao)
SELECT	nextval('seq_aplicacao_motivo_retorno'), perfil.codigo, 'Cancelar recebimento de parcela (NF/Faturamento)', 'faturamento-canc_receb', 1, 'admin', CURRENT_TIMESTAMP
FROM	perfil
WHERE	perfil.nome = 'faturamento-analista';
