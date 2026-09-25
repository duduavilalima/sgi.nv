/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-03-09 16:15                                */
/* ---------------------------------------------------------------------- */

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
