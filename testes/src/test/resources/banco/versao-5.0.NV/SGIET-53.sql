/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-03-09 11:39                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Modify table "lote_interface"                                          */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.lote_interface
	DROP CONSTRAINT ck_lote_interface_tipo;

ALTER TABLE public.lote_interface
	ADD CONSTRAINT ck_lote_interface_tipo 
    CHECK ((tipo >= 0) AND (tipo <= 17));

COMMENT ON COLUMN public.lote_interface.tipo IS 'Tipo do lote: 0 - Provisionamento / 1- Tesouraria / 2 - Custos (pagamentos) / 3 - Novos fornecedores / 4 - Fluxo de caixa (pagamentos) / 5 - NF/Saída / 6 - DIRF (anual) / 7 - FATN / 8 - FATC / 9 - DSFP / 10 - INSS / 11 - SSNI / 12 - IRRF / 13 - Fluxo de caixa (faturamento) / 14 - Custos (faturamento) / 15 - ISS e PIS/COFINS/CSLL (Sistema Fiscal) / 16 - Nota Carioca / 17 - Boletos (Itaú)';

/* ---------------------------------------------------------------------- */
/* Modify table "conta_contabil"                                          */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.conta_contabil
	DROP CONSTRAINT ck_conta_contabil_tipo_especial;

ALTER TABLE public.conta_contabil
	ADD CONSTRAINT ck_conta_contabil_tipo_especial 
    CHECK (tipo_especial <= 21);

COMMENT ON COLUMN public.conta_contabil.tipo_especial IS 'Tipo especial da conta contábil para algumas funcionalidades: 0 - Fundo fixo / 1 - Adiantamento / 2 - Reembolso / 3 - Empréstimo / 4 - Fornecedor / 5 - NP rescisão/Salários a Pagar / 6 - Multas / 7 - Juros / 8 - Descontos concedidos / 9 - Cliente onerado / 10 - Cliente desonerado / 11 - Faturamento contratual (onerado) / 12 - Faturamento extra (onerado) / 13 - Faturamento contratual (desonerado) / 14 - Faturamento extra (desonerado) / 15 - Faturamento cancelados (onerado) / 16 - Faturamento cancelados (desonerado) / 17 - Faturamento - INSS - Desoneração MP 612/2013 - Débito / 18 - Faturamento - INSS - Desoneração MP 612/2013 - Crédito / 19 - Faturamento - INSS a compensar / 20 - Faturamento - INSS retido pelos clientes';

UPDATE conta_contabil SET tipo_especial =  9 WHERE numero = '1.1.2.01.01.001';	--Cliente onerado
UPDATE conta_contabil SET tipo_especial = 10 WHERE numero = '1.2.6.01.01.001';	--Cliente desonerado
UPDATE conta_contabil SET tipo_especial = 11 WHERE numero = '7.1.1.01';			--Faturamento contratual (onerado)
UPDATE conta_contabil SET tipo_especial = 12 WHERE numero = '7.1.1.02';			--Faturamento extra (onerado)
UPDATE conta_contabil SET tipo_especial = 13 WHERE numero = '7.1.1.05';			--Faturamento contratual (desonerado)
UPDATE conta_contabil SET tipo_especial = 14 WHERE numero = '7.1.1.06';			--Faturamento extra (desonerado)
UPDATE conta_contabil SET tipo_especial = 15 WHERE numero = '7.1.2.03';			--Faturamento cancelados (onerado)
UPDATE conta_contabil SET tipo_especial = 16 WHERE numero = '7.1.2.04';			--Faturamento cancelados (desonerado)

/*
UPDATE conta_contabil SET tipo_especial = 17 WHERE numero = '4.4.3.18';			--Faturamento - INSS - Desoneração MP 612/2013 - Débito
UPDATE conta_contabil SET tipo_especial = 18 WHERE numero = '2.1.1.10.16';		--Faturamento - INSS - Desoneração MP 612/2013 - Crédito
UPDATE conta_contabil SET tipo_especial = 19 WHERE numero = '1.1.2.08.20';		--Faturamento - INSS a compensar
UPDATE conta_contabil SET tipo_especial = 20 WHERE numero = '2.1.1.10.12';		--Faturamento - INSS retido pelos clientes
*/

/* ---------------------------------------------------------------------- */
/* Parâmetro                                                              */
/* ---------------------------------------------------------------------- */
INSERT INTO parametro (chave, valor) VALUES ('faturamento.interface.anexo.diretorio',		'/var/lib/sgi/interface/');
INSERT INTO parametro (chave, valor) VALUES ('faturamento.interface.fatn.crontab',			'0 0,30 7-22 ? * 2-6');
INSERT INTO parametro (chave, valor) VALUES ('faturamento.interface.fatc.crontab',			'0 2,32 7-22 ? * 2-6');
INSERT INTO parametro (chave, valor) VALUES ('faturamento.interface.dsfp.crontab',			'0 4,34 7-22 ? * 2-6');
INSERT INTO parametro (chave, valor) VALUES ('faturamento.interface.inss.crontab',			'0 6,36 7-22 ? * 2-6');
INSERT INTO parametro (chave, valor) VALUES ('faturamento.interface.ssni.crontab',			'0 8,38 7-22 ? * 2-6');
INSERT INTO parametro (chave, valor) VALUES ('faturamento.interface.irrf.crontab',			'0 10,40 7-22 ? * 2-6');
INSERT INTO parametro (chave, valor) VALUES ('faturamento.interface.fluxocaixa.crontab',	'0 12,42 7-22 ? * 2-6');
INSERT INTO parametro (chave, valor) VALUES ('faturamento.interface.custos.crontab',		'0 14,44 7-22 ? * 2-6');
INSERT INTO parametro (chave, valor) VALUES ('faturamento.interface.sistemafiscal.crontab',	'0 16,46 7-22 ? * 2-6');
INSERT INTO parametro (chave, valor) VALUES ('faturamento.interface.notacarioca.crontab',	'0 18,48 7-22 ? * 2-6');
INSERT INTO parametro (chave, valor) VALUES ('faturamento.interface.boletoitau.crontab',	'0 20,50 7-22 ? * 2-6');

INSERT INTO parametro (chave, valor) VALUES ('faturamento.interface.dsfp.percentual',		'0.045');

