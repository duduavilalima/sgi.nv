/* ---------------------------------------------------------------------- */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-12-07 12:30                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Alter table                                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE	forma_contratacao
	ALTER COLUMN codigo SET DEFAULT nextval('seq_forma_contratacao');