/* ---------------------------------------------------------------------- */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2017-05-05 11:00                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Add "Parâmetro"                                                        */
/* ---------------------------------------------------------------------- */
INSERT
INTO	parametro (chave, valor)
VALUES	('tesouraria.conciliacao.anexo.tipos', 'RET, TXT');
