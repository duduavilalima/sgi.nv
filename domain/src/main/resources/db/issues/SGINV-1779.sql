/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2018-07-26 13:06                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Modify table "nota_fiscal"                                             */
/* ---------------------------------------------------------------------- */
/* Drop constraint */
ALTER TABLE public.nota_fiscal DROP CONSTRAINT ck_nota_fiscal_tipo_nota_fiscal;

/* Add constraint */
ALTER TABLE public.nota_fiscal
	ADD CONSTRAINT ck_nota_fiscal_tipo_nota_fiscal 
    CHECK (tipo_nota_fiscal <= 12);

/* Comment */
COMMENT ON COLUMN public.nota_fiscal.tipo_nota_fiscal IS 'Tipo da nota fiscal: 0 = Fornecimento / 1 = Ordem de Compra / 2 = NF/PC/Fundo Fixo / 3 = NF/PC/Reembolso / 4 = Transporte / 5 = NF/PC/Adiantamento / 6 = Devolução parcial / 7 = Devolução total / 8 = OC/PC/Fundo Fixo / 9 = OC/PC/Reembolso / 10 = OC/PC/Adiantamento / 11 = NF/Faturamento / 12 = NF/Faturamento - Exclusão lógica.';

/* ---------------------------------------------------------------------- */
/* Add "aplicacao_motivo_retorno"                                         */
/* ---------------------------------------------------------------------- */
INSERT	INTO aplicacao_motivo_retorno (codigo, codigo_perfil, nome, chave, tipo, criador, data_criacao, atualizador, data_atualizacao) 
SELECT	NEXTVAL('seq_aplicacao_motivo_retorno'), perfil.codigo, 'Remover NF (Faturamento) numerada', 'faturamento-remover_nf', 1, 'admin', CURRENT_TIMESTAMP, NULL, NULL
FROM	perfil
WHERE	perfil.nome = 'faturamento-analista';

/* ---------------------------------------------------------------------- */
/* Add "parametro"                                                        */
/* ---------------------------------------------------------------------- */
INSERT	INTO parametro (chave, valor) VALUES ('faturamento.remover.logico.email', 'car@araujoabreu.com.br;jessica.oliveira@araujoabreu.com.br;marcel.waldeck@fasolti.com.br');
