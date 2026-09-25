/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-06-29 12:03                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Create sequence                                                        */
/* ---------------------------------------------------------------------- */
CREATE SEQUENCE public.seq_motivo_retorno_documento_nota_saida_parcela INCREMENT 1 MINVALUE 1 START 1;

/* ---------------------------------------------------------------------- */
/* Add table "motivo_retorno_documento_nota_saida_parcela"                */
/* ---------------------------------------------------------------------- */
/* Create new table */
CREATE TABLE public.motivo_retorno_documento_nota_saida_parcela (
    codigo							BIGINT DEFAULT nextval('seq_motivo_retorno_documento_nota_saida_parcela')  NOT NULL,
    codigo_nota_saida_parcela		BIGINT  NOT NULL,
    codigo_motivo_retorno_documento	BIGINT  NOT NULL,
    CONSTRAINT pk_motivo_retorno_documento_nota_saida_parcela PRIMARY KEY (codigo)
);

/* Create indexes */
CREATE INDEX ix_mot_ret_documento_nota_saida_parcela_nota_saida_parcela				ON public.motivo_retorno_documento_nota_saida_parcela (codigo_nota_saida_parcela);
CREATE INDEX ix_mot_ret_documento_nota_saida_parcela_mot_ret_documento				ON public.motivo_retorno_documento_nota_saida_parcela (codigo_motivo_retorno_documento);
CREATE UNIQUE INDEX uq_mot_ret_documento_nota_saida_parcela_mot_ret_doc_ns_parcela	ON public.motivo_retorno_documento_nota_saida_parcela (codigo_nota_saida_parcela,codigo_motivo_retorno_documento);

/* Comments */
COMMENT ON TABLE public.motivo_retorno_documento_nota_saida_parcela										IS 'Associação entre um retorno (motivo de retorno x documento) e a parcela.';
COMMENT ON COLUMN public.motivo_retorno_documento_nota_saida_parcela.codigo								IS 'Código interno da associação entre um retorno (motivo de retorno x documento) e uma parcela de nota de saída. Sequência: seq_motivo_retorno_documento_nota_saida_parcela';
COMMENT ON COLUMN public.motivo_retorno_documento_nota_saida_parcela.codigo_nota_saida_parcela			IS 'Código interno da parcela da nota de saída.';
COMMENT ON COLUMN public.motivo_retorno_documento_nota_saida_parcela.codigo_motivo_retorno_documento	IS 'Código interno do motivo de retorno do documento.';

/* Set owner */
ALTER TABLE public.motivo_retorno_documento_nota_saida_parcela OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.motivo_retorno_documento_nota_saida_parcela	ADD CONSTRAINT fk_motivo_retorno_documento_ns_parcela_motivo_retorno_documento 
    FOREIGN KEY (codigo_motivo_retorno_documento)				REFERENCES public.motivo_retorno_documento (codigo);
ALTER TABLE public.motivo_retorno_documento_nota_saida_parcela	ADD CONSTRAINT fk_motivo_retorno_documento_ns_parcela_nota_saida_parcela 
    FOREIGN KEY (codigo_nota_saida_parcela)						REFERENCES public.nota_saida_parcela (codigo);

/* ---------------------------------------------------------------------- */
/* Adicionar aplicação (motivo de retorno)                                */
/* ---------------------------------------------------------------------- */
INSERT
INTO	aplicacao_motivo_retorno
		(codigo, codigo_perfil, nome, chave, tipo, criador, data_criacao, atualizador, data_atualizacao)
SELECT	NEXTVAL('seq_aplicacao_motivo_retorno'), perfil.codigo, 'Cancelar parcela (NF/Faturamento)', 'faturamento-canc_parcela', 1, 'admin', CURRENT_TIMESTAMP, NULL, NULL
FROM	perfil
WHERE	perfil.nome = 'faturamento-analista';

/* ---------------------------------------------------------------------- */
/* Parâmetro                                                              */
/* ---------------------------------------------------------------------- */
INSERT INTO parametro (chave, valor) VALUES ('faturamento.cancelamento.nf.parcelas', 'NF cancelada pelo cancelamento de todas as parcelas.');