/* ---------------------------------------------------------------------- */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2017-07-04 11:30                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Add "Parâmetro"                                                        */
/* ---------------------------------------------------------------------- */
INSERT
INTO	parametro (chave, valor)
VALUES	('faturamento.relatorios.temp.url', 'https://sgi.araujoabreu.com.br/sgi/');
