/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2018-03-27 10:20                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Update data into table "nota_saida"                                    */
/* ---------------------------------------------------------------------- */
UPDATE	nota_saida
SET		iss_retido_cliente = fat.iss_retido_cliente
FROM	faturamento_etapa fe, faturamento fat, nota_fiscal nf
WHERE	nota_saida.codigo = fe.codigo_nota_saida
AND		fe.codigo_faturamento = fat.codigo
AND		nota_saida.codigo = nf.codigo
AND		nf.tipo_nota_fiscal = 11;
