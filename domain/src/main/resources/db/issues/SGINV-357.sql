/* ---------------------------------------------------------------------- */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2017-03-10 10:35                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Update data into "centro_financeiro"                                   */
/* ---------------------------------------------------------------------- */
--Novas associações
UPDATE centro_financeiro SET codigo_contrato = 318 WHERE right(identificador, 4) = '3388' AND situacao = 1 AND codigo_contrato IS NULL AND proxima_oc > 1; --Existem 2 CLs
UPDATE centro_financeiro SET codigo_contrato = 301 WHERE right(identificador, 4) = '3599' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 302 WHERE right(identificador, 4) = '3622' AND situacao = 1 AND codigo_contrato IS NULL AND proxima_oc > 1; --Existem 2 CLs
UPDATE centro_financeiro SET codigo_contrato = 311 WHERE right(identificador, 4) = '3670' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 319 WHERE right(identificador, 4) = '3711' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 310 WHERE right(identificador, 4) = '3725' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 303 WHERE right(identificador, 4) = '3743' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 309 WHERE right(identificador, 4) = '3934' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 305 WHERE right(identificador, 4) = '4067' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 307 WHERE right(identificador, 4) = '4094' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 312 WHERE right(identificador, 4) = '4126' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 316 WHERE right(identificador, 4) = '4139' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 313 WHERE right(identificador, 4) = '4152' AND situacao = 1 AND codigo_contrato IS NULL;

--Já associados (anteriormente)
UPDATE centro_financeiro SET codigo_contrato = 4   WHERE right(identificador, 4) = '4163' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 9   WHERE right(identificador, 4) = '4197' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 38  WHERE right(identificador, 4) = '4228' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 55  WHERE right(identificador, 4) = '4248' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 60  WHERE right(identificador, 4) = '4252' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 65  WHERE right(identificador, 4) = '4257' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 77  WHERE right(identificador, 4) = '4270' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 192 WHERE right(identificador, 4) = '4304' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 194 WHERE right(identificador, 4) = '4306' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 204 WHERE right(identificador, 4) = '4333' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 206 WHERE right(identificador, 4) = '4335' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 273 WHERE right(identificador, 4) = '4342' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 280 WHERE right(identificador, 4) = '4352' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 280 WHERE right(identificador, 4) = '4352' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 288 WHERE right(identificador, 4) = '4356' AND situacao = 1 AND codigo_contrato IS NULL;
UPDATE centro_financeiro SET codigo_contrato = 288 WHERE right(identificador, 4) = '4357' AND situacao = 1 AND codigo_contrato IS NULL;
