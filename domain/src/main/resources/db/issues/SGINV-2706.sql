/* ---------------------------------------------------------------------- */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2020-10-16 12:30                                */
/* ---------------------------------------------------------------------- */

/* Ajuste nos motivos/justificativas relacionados às alterações de datas (vencimento, agendamento e pagamento) */
ALTER TABLE nota_saida_parcela
ALTER COLUMN motivo_data_vencimento SET DATA TYPE character varying(2000), 
ALTER COLUMN motivo_data_agendamento SET DATA TYPE character varying(2000), 
ALTER COLUMN motivo_data_pagamento SET DATA TYPE character varying(2000);
