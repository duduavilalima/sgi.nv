/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2019-07-09 16:19                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Modify table "municipio_ect"                                           */
/* ---------------------------------------------------------------------- */
/* Add column */
ALTER TABLE public.municipio_ect
	ADD tipo_lote_interface SMALLINT;

/* Add check constraint */
ALTER TABLE public.municipio_ect
	ADD CONSTRAINT ck_municipio_ect_tipo_lote_interface 
    CHECK (tipo_lote_interface = ANY (ARRAY[16, 19, 20, 21, 22]));

/* Comments */
COMMENT ON COLUMN public.municipio_ect.tipo_lote_interface IS 'Tipo de interface de lote associado à Prefeitura: 16 - Nota Carioca / 19 - Nota Norte (Belém) / 20 - Nota Paulista / 21 - Nota BH / 22 - Nota Brasília.';

/* Adjust data */
UPDATE municipio_ect SET interface_prefeitura = TRUE, tipo_lote_interface = 16 WHERE nome = 'RIO DE JANEIRO'	AND uf = 'RJ';
UPDATE municipio_ect SET interface_prefeitura = TRUE, tipo_lote_interface = 19 WHERE nome = 'BELÉM'				AND uf = 'PA';
UPDATE municipio_ect SET interface_prefeitura = TRUE, tipo_lote_interface = 20 WHERE nome = 'SÃO PAULO'			AND uf = 'SP';
UPDATE municipio_ect SET interface_prefeitura = TRUE, tipo_lote_interface = 21 WHERE nome = 'BELO HORIZONTE'	AND uf = 'MG';
UPDATE municipio_ect SET interface_prefeitura = TRUE, tipo_lote_interface = 22 WHERE nome = 'BRASÍLIA'			AND uf = 'DF';

/* Adjust "parametros" */
DELETE FROM parametro WHERE chave IN ('faturamento.interface.notacarioca.crontab', 'faturamento.interface.notacarioca.tamanho');

INSERT INTO parametro (chave, valor) VALUES ('faturamento.interface.prefeituras.crontab', '0 0/1 5-23 ? * 1-7'	);
INSERT INTO parametro (chave, valor) VALUES ('faturamento.interface.notacarioca.tamanho', '10'					);
