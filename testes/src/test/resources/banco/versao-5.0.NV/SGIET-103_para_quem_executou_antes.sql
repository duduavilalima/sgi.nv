/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-04-13 14:32                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Adicionar aplicação (motivo de retorno)                                */
/* ---------------------------------------------------------------------- */
INSERT
INTO	aplicacao_motivo_retorno
		(codigo, codigo_perfil, nome, chave, tipo, criador, data_criacao, atualizador, data_atualizacao)
VALUES	(NEXTVAL('seq_aplicacao_motivo_retorno'), (SELECT codigo FROM perfil WHERE nome = 'faturamento-analista'), 'Cancelar Nota Fiscal (Faturamento)', 'faturamento-canc_nf', 1, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

/* ---------------------------------------------------------------------- */
/* Modify table "municipio_ect"                                           */
/* ---------------------------------------------------------------------- */
/* Add column */
ALTER TABLE public.municipio_ect
	ADD interface_prefeitura BOOLEAN DEFAULT false;

/* Update "old" data */
UPDATE municipio_ect SET interface_prefeitura = false;

UPDATE municipio_ect SET interface_prefeitura = true WHERE nome = 'RIO DE JANEIRO' AND uf = 'RJ';

/* Add not null */
ALTER TABLE public.municipio_ect
	ALTER COLUMN interface_prefeitura SET NOT NULL;

/* Comments */
COMMENT ON TABLE public.municipio_ect						IS 'Tabela de municípios (criada pela ECT, Empresa Brasileira de Correios e Telégrafos)';
COMMENT ON COLUMN public.municipio_ect.codigo				IS 'Código interno do município.';
COMMENT ON COLUMN public.municipio_ect.nome					IS 'Nome do município.';
COMMENT ON COLUMN public.municipio_ect.uf					IS 'UF (unidade da federação) a qual o município pertence.';
COMMENT ON COLUMN public.municipio_ect.nome_com_acento		IS 'Nome do município (com acentos). Usado para testes e ajustes futuros.';
COMMENT ON COLUMN public.municipio_ect.interface_prefeitura	IS 'Indica se a interface com a Prefeitura do município está deselvolvida (true) ou não (false).';
