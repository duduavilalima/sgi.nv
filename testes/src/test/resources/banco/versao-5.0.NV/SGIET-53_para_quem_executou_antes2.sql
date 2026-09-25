/* ---------------------------------------------------------------------- */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-03-09 11:39                                */
/* ---------------------------------------------------------------------- */

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
