/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2018-03-26 15:00                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_saida DROP CONSTRAINT fk_nota_saida_municipio_ect;
ALTER TABLE public.nota_saida DROP CONSTRAINT fk_nota_saida_nota_fiscal;
ALTER TABLE public.nota_saida DROP CONSTRAINT fk_nota_saida_resp_cad_prefeitura;
ALTER TABLE public.nota_saida DROP CONSTRAINT fk_nota_saida_resp_canc_numeracao;
ALTER TABLE public.nota_saida DROP CONSTRAINT fk_nota_saida_resp_envio_rfb;
ALTER TABLE public.nota_saida DROP CONSTRAINT fk_nota_saida_resp_numeracao;
ALTER TABLE public.nota_saida DROP CONSTRAINT fk_nota_saida_resp_ultimo_arquivo;
ALTER TABLE public.nota_saida DROP CONSTRAINT fk_nota_saida_servico_tipo;
ALTER TABLE public.nota_saida DROP CONSTRAINT fk_nota_saida_transportadora;
ALTER TABLE public.nota_saida DROP CONSTRAINT fk_nota_saida_transportadora_municipio;
ALTER TABLE public.nota_saida DROP CONSTRAINT fk_nota_saida_unidade;
ALTER TABLE public.nota_saida DROP CONSTRAINT fk_nota_saida_cfop_transporte;

ALTER TABLE public.item_nota_saida DROP CONSTRAINT fk_item_nota_saida_nota_saida;

ALTER TABLE public.faturamento_etapa DROP CONSTRAINT fk_faturamento_etapa_nota_saida;

ALTER TABLE public.nota_saida_parcela DROP CONSTRAINT fk_nota_saida_parcela_nota_saida;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "nota_saida"                                   */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.nota_saida DROP CONSTRAINT pk_nota_saida;

/* Create "new" table */
CREATE TABLE public.nota_saida_TMP (
    codigo BIGINT  NOT NULL,
    codigo_emitente BIGINT  NOT NULL,
    codigo_transportadora BIGINT,
    codigo_municipio_transportadora BIGINT,
    codigo_cfop_transporte NUMERIC(4),
    codigo_municipio_transporte BIGINT,
    codigo_resp_ultimo_arquivo BIGINT,
    codigo_resp_envio_rfb BIGINT,
    codigo_resp_numeracao BIGINT,
    codigo_resp_canc_numeracao BIGINT,
    codigo_resp_cad_prefeitura BIGINT,
    codigo_servico_tipo BIGINT,
    isento_icms BOOLEAN DEFAULT false  NOT NULL,
    iss_retido_cliente BOOLEAN DEFAULT false  NOT NULL,
    inscr_suframa CHARACTER VARYING(9),
    tipo_documento_transportadora INTEGER,
    documento_transportadora CHARACTER VARYING(18),
    nome_transportadora CHARACTER VARYING(100),
    inscricao_estadual_transportadora CHARACTER VARYING(20),
    isento_icms_transportadora BOOLEAN,
    endereco_transportadora CHARACTER VARYING(255),
    icms_base_calculo_transporte NUMERIC(15,2),
    icms_aliquota_transporte NUMERIC(4,2),
    info_complementares_contribuinte CHARACTER VARYING(2000),
    data_resp_ultimo_arquivo TIMESTAMP,
    data_resp_envio_rfb TIMESTAMP,
    data_resp_numeracao TIMESTAMP,
    data_resp_canc_numeracao TIMESTAMP,
    data_resp_cad_prefeitura TIMESTAMP,
    protocolo_entrega CHARACTER VARYING(25));

/* Move data from "old" table to "new" table */
INSERT INTO public.nota_saida_TMP
    (codigo,codigo_emitente,codigo_transportadora,codigo_municipio_transportadora,codigo_cfop_transporte,codigo_municipio_transporte,codigo_resp_ultimo_arquivo,codigo_resp_envio_rfb,codigo_resp_numeracao,codigo_resp_canc_numeracao,codigo_resp_cad_prefeitura,codigo_servico_tipo,isento_icms,inscr_suframa,tipo_documento_transportadora,documento_transportadora,nome_transportadora,inscricao_estadual_transportadora,isento_icms_transportadora,endereco_transportadora,icms_base_calculo_transporte,icms_aliquota_transporte,info_complementares_contribuinte,data_resp_ultimo_arquivo,data_resp_envio_rfb,data_resp_numeracao,data_resp_canc_numeracao,data_resp_cad_prefeitura,protocolo_entrega)
SELECT
    codigo,codigo_emitente,codigo_transportadora,codigo_municipio_transportadora,codigo_cfop_transporte,codigo_municipio_transporte,codigo_resp_ultimo_arquivo,codigo_resp_envio_rfb,codigo_resp_numeracao,codigo_resp_canc_numeracao,codigo_resp_cad_prefeitura,codigo_servico_tipo,isento_icms,inscr_suframa,tipo_documento_transportadora,documento_transportadora,nome_transportadora,inscricao_estadual_transportadora,isento_icms_transportadora,endereco_transportadora,icms_base_calculo_transporte,icms_aliquota_transporte,info_complementares_contribuinte,data_resp_ultimo_arquivo,data_resp_envio_rfb,data_resp_numeracao,data_resp_canc_numeracao,data_resp_cad_prefeitura,protocolo_entrega
FROM public.nota_saida;

/* Drop indexes */
DROP INDEX public.ix_nota_saida_cfop_transporte;
DROP INDEX public.ix_nota_saida_emitente;
DROP INDEX public.ix_nota_saida_municipio_transportadora;
DROP INDEX public.ix_nota_saida_municipio_transporte;
DROP INDEX public.ix_nota_saida_resp_cad_prefeitura;
DROP INDEX public.ix_nota_saida_resp_canc_numeracao;
DROP INDEX public.ix_nota_saida_resp_envio_rfb;
DROP INDEX public.ix_nota_saida_resp_numeracao;
DROP INDEX public.ix_nota_saida_resp_ultimo_arquivo;
DROP INDEX public.ix_nota_saida_servico_tipo;
DROP INDEX public.ix_nota_saida_transportadora;

/* Drop "old" table  */
DROP TABLE public.nota_saida;

/* Rename "old" table to "new" table */
ALTER TABLE public.nota_saida_TMP
	RENAME TO nota_saida;

/* Create primary key */
ALTER TABLE public.nota_saida
	ADD CONSTRAINT pk_nota_saida 
    PRIMARY KEY (codigo);

/* Create indexes */
CREATE INDEX ix_nota_saida_emitente					ON public.nota_saida (codigo_emitente);
CREATE INDEX ix_nota_saida_transportadora			ON public.nota_saida (codigo_transportadora);
CREATE INDEX ix_nota_saida_municipio_transportadora	ON public.nota_saida (codigo_municipio_transportadora);
CREATE INDEX ix_nota_saida_cfop_transporte			ON public.nota_saida (codigo_cfop_transporte);
CREATE INDEX ix_nota_saida_municipio_transporte		ON public.nota_saida (codigo_municipio_transporte);
CREATE INDEX ix_nota_saida_resp_ultimo_arquivo		ON public.nota_saida (codigo_resp_ultimo_arquivo);
CREATE INDEX ix_nota_saida_resp_envio_rfb			ON public.nota_saida (codigo_resp_envio_rfb);
CREATE INDEX ix_nota_saida_resp_numeracao			ON public.nota_saida (codigo_resp_numeracao);
CREATE INDEX ix_nota_saida_resp_canc_numeracao		ON public.nota_saida (codigo_resp_canc_numeracao);
CREATE INDEX ix_nota_saida_resp_cad_prefeitura		ON public.nota_saida (codigo_resp_cad_prefeitura);
CREATE INDEX ix_nota_saida_servico_tipo				ON public.nota_saida (codigo_servico_tipo);

/* Comments */
COMMENT ON TABLE public.nota_saida IS 'Notas fiscais de saída.';
COMMENT ON COLUMN public.nota_saida.codigo IS 'Código interno (código interno da nota fiscal e código interno da nota) da nota fiscal de saída.';
COMMENT ON COLUMN public.nota_saida.codigo_emitente IS 'Código interno do emitente (empresa/unidade).';
COMMENT ON COLUMN public.nota_saida.codigo_transportadora IS 'Código interno (código interno da pessoa) da transportadora. Se estiver no cadastro de pessoas, terá este código, senão os dados relacionados ao nome e ao endereço devem estar preenchidos.';
COMMENT ON COLUMN public.nota_saida.codigo_municipio_transportadora IS 'Código interno do município do endereço da transportadora.';
COMMENT ON COLUMN public.nota_saida.codigo_cfop_transporte IS 'Código interno do CFOP de transporte.';
COMMENT ON COLUMN public.nota_saida.codigo_municipio_transporte IS 'Código interno do município do ICMS de transporte.';
COMMENT ON COLUMN public.nota_saida.codigo_resp_ultimo_arquivo IS 'Código interno (código interno da pessoa) responsável pela criação do último arquivo para envio à RFB.';
COMMENT ON COLUMN public.nota_saida.codigo_resp_envio_rfb IS 'Código interno (código interno da pessoa) responsável pela confirmação do envio do arquivo à RFB.';
COMMENT ON COLUMN public.nota_saida.codigo_resp_numeracao IS 'Código interno (código interno da pessoa) responsável pela última numeração da NF (faturamento).';
COMMENT ON COLUMN public.nota_saida.codigo_resp_canc_numeracao IS 'Código interno (código interno da pessoa) responsável pelo último cancelamento de numeração da NF (faturamento).';
COMMENT ON COLUMN public.nota_saida.codigo_resp_cad_prefeitura IS 'Código interno (código interno da pessoa) responsável pelo cadastramento da NF no site da Prefeitura (faturamento).';
COMMENT ON COLUMN public.nota_saida.codigo_servico_tipo IS 'Código interno do tipo de serviço.';
COMMENT ON COLUMN public.nota_saida.isento_icms IS 'Indica se o destinatário é isento de ICMS ou não.';
COMMENT ON COLUMN public.nota_saida.iss_retido_cliente IS 'Indica se o ISS será retido pelo cliente (true) ou não (false).';
COMMENT ON COLUMN public.nota_saida.inscr_suframa IS 'Código SUFRAMA do destinatário (fornecedor).';
COMMENT ON COLUMN public.nota_saida.tipo_documento_transportadora IS 'Indica o tipo de documento (0 = CNPJ / 1 = CPF) da transportadora.';
COMMENT ON COLUMN public.nota_saida.documento_transportadora IS 'Documento (CPF ou CNPJ) da transportadora.';
COMMENT ON COLUMN public.nota_saida.nome_transportadora IS 'Nome (CPF) ou razão social (CNPJ) da transportadora.';
COMMENT ON COLUMN public.nota_saida.inscricao_estadual_transportadora IS 'Inscrição estadual (apenas CNPJ) da transportadora.';
COMMENT ON COLUMN public.nota_saida.isento_icms_transportadora IS 'Indica se a transportadora é isenta de ICMS ou não.';
COMMENT ON COLUMN public.nota_saida.endereco_transportadora IS 'Endereço completo da transportadora.';
COMMENT ON COLUMN public.nota_saida.icms_base_calculo_transporte IS 'Base de cálculo do ICMS de transporte.';
COMMENT ON COLUMN public.nota_saida.icms_aliquota_transporte IS 'Alíquota do ICMS de transporte.';
COMMENT ON COLUMN public.nota_saida.info_complementares_contribuinte IS 'Informações complementares de interesse do contribuinte.';
COMMENT ON COLUMN public.nota_saida.data_resp_ultimo_arquivo IS 'Data/hora da criação do último arquivo para envio à RFB.';
COMMENT ON COLUMN public.nota_saida.data_resp_envio_rfb IS 'Data/hora de confirmação do envio do arquivo à RFB.';
COMMENT ON COLUMN public.nota_saida.data_resp_numeracao IS 'Data/hora da última numeração da NF (faturamento).';
COMMENT ON COLUMN public.nota_saida.data_resp_canc_numeracao IS 'Data/hora do último cancelamento da numeração da NF (faturamento).';
COMMENT ON COLUMN public.nota_saida.data_resp_cad_prefeitura IS 'Data/hora do cadastramento da NF no site da Prefeitura (faturamento).';
COMMENT ON COLUMN public.nota_saida.protocolo_entrega IS 'Protocolo (nº, data, nome, etc.) de entrega da NF ao cliente.';

/* Set owner */
ALTER TABLE public.nota_saida OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_nota_fiscal 
    FOREIGN KEY (codigo)							REFERENCES public.nota_fiscal (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_unidade 
    FOREIGN KEY (codigo_emitente)					REFERENCES public.unidade (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_transportadora_municipio 
    FOREIGN KEY (codigo_municipio_transportadora)	REFERENCES public.municipio_ect (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_cfop_transporte 
    FOREIGN KEY (codigo_cfop_transporte)			REFERENCES public.cfop (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_municipio_ect 
    FOREIGN KEY (codigo_municipio_transporte)		REFERENCES public.municipio_ect (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_transportadora 
    FOREIGN KEY (codigo_transportadora)				REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_resp_ultimo_arquivo 
    FOREIGN KEY (codigo_resp_ultimo_arquivo)		REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_resp_envio_rfb 
    FOREIGN KEY (codigo_resp_envio_rfb)				REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_servico_tipo 
    FOREIGN KEY (codigo_servico_tipo)				REFERENCES public.servico_tipo (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_resp_numeracao 
    FOREIGN KEY (codigo_resp_numeracao)				REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_resp_canc_numeracao 
    FOREIGN KEY (codigo_resp_canc_numeracao)		REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_resp_cad_prefeitura 
    FOREIGN KEY (codigo_resp_cad_prefeitura)		REFERENCES public.pessoa (codigo);

ALTER TABLE public.item_nota_saida					ADD CONSTRAINT fk_item_nota_saida_nota_saida 
    FOREIGN KEY (codigo_nota_saida)					REFERENCES public.nota_saida (codigo);

ALTER TABLE public.faturamento_etapa				ADD CONSTRAINT fk_faturamento_etapa_nota_saida 
    FOREIGN KEY (codigo_nota_saida)					REFERENCES public.nota_saida (codigo);

ALTER TABLE public.nota_saida_parcela				ADD CONSTRAINT fk_nota_saida_parcela_nota_saida 
    FOREIGN KEY (codigo_nota_saida)					REFERENCES public.nota_saida (codigo);

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
