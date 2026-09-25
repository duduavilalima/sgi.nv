/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3  (PARTE 1)          */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-03-21 17:59                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Create sequences                                                       */
/* ---------------------------------------------------------------------- */
CREATE SEQUENCE public.seq_servico_tipo INCREMENT 1 MINVALUE 1 START 1;

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_nota_fiscal;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_unidade;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_transportadora_municipio;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_cfop_transporte;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_municipio_ect;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_transportadora;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_resp_ultimo_arquivo;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_resp_envio_rfb;
ALTER TABLE public.nota_saida			DROP CONSTRAINT fk_nota_saida_cancelamento;

ALTER TABLE public.item_nota_saida		DROP CONSTRAINT fk_item_nota_saida_nota_saida;

ALTER TABLE public.faturamento_etapa	DROP CONSTRAINT fk_faturamento_etapa_nota_saida;

ALTER TABLE public.nota_saida_parcela	DROP CONSTRAINT fk_nota_saida_parcela_nota_saida;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "nota_saida"                                   */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.nota_saida DROP CONSTRAINT pk_nota_saida;

/* Create temporary table */
CREATE TABLE public.nota_saida_TMP (
    codigo								BIGINT  NOT NULL,
    codigo_emitente						BIGINT  NOT NULL,
    codigo_transportadora				BIGINT,
    codigo_municipio_transportadora		BIGINT,
    codigo_cfop_transporte				NUMERIC(4),
    codigo_municipio_transporte			BIGINT,
    codigo_resp_ultimo_arquivo			BIGINT,
    codigo_resp_envio_rfb				BIGINT,
    codigo_cancelamento					BIGINT,
    codigo_servico_tipo					BIGINT,
    isento_icms							BOOLEAN DEFAULT false  NOT NULL,
    inscr_suframa						CHARACTER VARYING(9),
    tipo_documento_transportadora		INTEGER,
    documento_transportadora			CHARACTER VARYING(18),
    nome_transportadora					CHARACTER VARYING(100),
    inscricao_estadual_transportadora	CHARACTER VARYING(20),
    isento_icms_transportadora			BOOLEAN,
    endereco_transportadora				CHARACTER VARYING(255),
    icms_base_calculo_transporte		NUMERIC(15,2),
    icms_aliquota_transporte			NUMERIC(4,2),
    info_complementares_contribuinte	CHARACTER VARYING(2000),
    data_resp_ultimo_arquivo			TIMESTAMP,
    data_resp_envio_rfb					TIMESTAMP,
    data_cancelamento					TIMESTAMP);

/* Move data to the temporary table */
INSERT INTO public.nota_saida_TMP
    (codigo,codigo_emitente,codigo_transportadora,codigo_municipio_transportadora,codigo_cfop_transporte,codigo_municipio_transporte,codigo_resp_ultimo_arquivo,codigo_resp_envio_rfb,codigo_cancelamento,isento_icms,inscr_suframa,tipo_documento_transportadora,documento_transportadora,nome_transportadora,inscricao_estadual_transportadora,isento_icms_transportadora,endereco_transportadora,icms_base_calculo_transporte,icms_aliquota_transporte,info_complementares_contribuinte,data_resp_ultimo_arquivo,data_resp_envio_rfb,data_cancelamento)
SELECT
    codigo,codigo_emitente,codigo_transportadora,codigo_municipio_transportadora,codigo_cfop_transporte,codigo_municipio_transporte,codigo_resp_ultimo_arquivo,codigo_resp_envio_rfb,codigo_cancelamento,isento_icms,inscr_suframa,tipo_documento_transportadora,documento_transportadora,nome_transportadora,inscricao_estadual_transportadora,isento_icms_transportadora,endereco_transportadora,icms_base_calculo_transporte,icms_aliquota_transporte,info_complementares_contribuinte,data_resp_ultimo_arquivo,data_resp_envio_rfb,data_cancelamento
FROM public.nota_saida;

/* Drop indexes */
DROP INDEX public.ix_nota_saida_cancelamento;
DROP INDEX public.ix_nota_saida_emitente;
DROP INDEX public.ix_nota_saida_resp_envio_rfb;
DROP INDEX public.ix_nota_saida_resp_ultimo_arquivo;
DROP INDEX public.ix_nota_saida_transportadora;
DROP INDEX public.ix_nota_saida_municipio_transportadora;
DROP INDEX public.ix_nota_saida_cfop_transporte;
DROP INDEX public.ix_nota_saida_municipio_transporte;

/* Drop "old" table */
DROP TABLE public.nota_saida;

/* Rename temporary table to "new" table */
ALTER TABLE public.nota_saida_TMP
	RENAME TO nota_saida;

/* Add primary key */
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
CREATE INDEX ix_nota_saida_cancelamento				ON public.nota_saida (codigo_cancelamento);
CREATE INDEX ix_nota_saida_servico_tipo				ON public.nota_saida (codigo_servico_tipo);

/* Comments */
COMMENT ON TABLE public.nota_saida										IS 'Notas fiscais de saída.';
COMMENT ON COLUMN public.nota_saida.codigo								IS 'Código interno (código interno da nota fiscal e código interno da nota) da nota fiscal de saída.';
COMMENT ON COLUMN public.nota_saida.codigo_emitente						IS 'Código interno do emitente (empresa/unidade).';
COMMENT ON COLUMN public.nota_saida.codigo_transportadora				IS 'Código interno (código interno da pessoa) da transportadora. Se estiver no cadastro de pessoas, terá este código, senão os dados relacionados ao nome e ao endereço devem estar preenchidos.';
COMMENT ON COLUMN public.nota_saida.codigo_municipio_transportadora 	IS 'Código interno do município do endereço da transportadora.';
COMMENT ON COLUMN public.nota_saida.codigo_cfop_transporte				IS 'Código interno do CFOP de transporte.';
COMMENT ON COLUMN public.nota_saida.codigo_municipio_transporte			IS 'Código interno do município do ICMS de transporte.';
COMMENT ON COLUMN public.nota_saida.codigo_resp_ultimo_arquivo			IS 'Código interno (código interno da pessoa) responsável pela criação do último arquivo para envio à RFB.';
COMMENT ON COLUMN public.nota_saida.codigo_resp_envio_rfb				IS 'Código interno (código interno da pessoa) responsável pela confirmação do envio do arquivo à RFB.';
COMMENT ON COLUMN public.nota_saida.codigo_cancelamento					IS 'Código interno (código interno da pessoa) responsável pelo cancelamento.';
COMMENT ON COLUMN public.nota_saida.codigo_servico_tipo					IS 'Código interno do tipo de serviço.';
COMMENT ON COLUMN public.nota_saida.isento_icms							IS 'Indica se o destinatário é isento de ICMS ou não.';
COMMENT ON COLUMN public.nota_saida.inscr_suframa						IS 'Código SUFRAMA do destinatário (fornecedor).';
COMMENT ON COLUMN public.nota_saida.tipo_documento_transportadora		IS 'Indica o tipo de documento (0 = CNPJ / 1 = CPF) da transportadora.';
COMMENT ON COLUMN public.nota_saida.documento_transportadora			IS 'Documento (CPF ou CNPJ) da transportadora.';
COMMENT ON COLUMN public.nota_saida.nome_transportadora					IS 'Nome (CPF) ou razão social (CNPJ) da transportadora.';
COMMENT ON COLUMN public.nota_saida.inscricao_estadual_transportadora	IS 'Inscrição estadual (apenas CNPJ) da transportadora.';
COMMENT ON COLUMN public.nota_saida.isento_icms_transportadora			IS 'Indica se a transportadora é isenta de ICMS ou não.';
COMMENT ON COLUMN public.nota_saida.endereco_transportadora				IS 'Endereço completo da transportadora.';
COMMENT ON COLUMN public.nota_saida.icms_base_calculo_transporte		IS 'Base de cálculo do ICMS de transporte.';
COMMENT ON COLUMN public.nota_saida.icms_aliquota_transporte			IS 'Alíquota do ICMS de transporte.';
COMMENT ON COLUMN public.nota_saida.info_complementares_contribuinte	IS 'Informações complementares de interesse do contribuinte.';
COMMENT ON COLUMN public.nota_saida.data_resp_ultimo_arquivo			IS 'Data/hora da criação do último arquivo para envio à RFB.';
COMMENT ON COLUMN public.nota_saida.data_resp_envio_rfb					IS 'Data/hora de confirmação do envio do arquivo à RFB.';
COMMENT ON COLUMN public.nota_saida.data_cancelamento					IS 'Data/hora do cancelamento.';

/* Set owner */
ALTER TABLE public.nota_saida OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add table "servico_tipo"                                               */
/* ---------------------------------------------------------------------- */
CREATE TABLE public.servico_tipo (
    codigo					BIGINT		DEFAULT nextval('seq_servico_tipo')	NOT NULL,
    codigo_pai				BIGINT,
    sigla					CHARACTER VARYING(30)							NOT NULL,
    nome					CHARACTER VARYING(1000)							NOT NULL,
    situacao				SMALLINT	DEFAULT 0							NOT NULL,
    habilita_faturamento	BOOLEAN		DEFAULT false						NOT NULL,
    criador					CHARACTER VARYING(255),
    data_criacao			TIMESTAMP,
    atualizador				CHARACTER VARYING(255),
    data_atualizacao		TIMESTAMP,
    CONSTRAINT pk_servico_tipo PRIMARY KEY (codigo)
);

/* Create indexes */
CREATE UNIQUE INDEX uq_servico_tipo_sigla	ON public.servico_tipo (sigla);
CREATE INDEX ix_servico_tipo_pai			ON public.servico_tipo (codigo_pai);

/* Add constraint */
ALTER TABLE public.servico_tipo
	ADD CONSTRAINT ck_servico_tipo_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1]));

/* Comments */
COMMENT ON TABLE public.servico_tipo						IS 'Tabela de tipos de serviço de cada Prefeitura Municipal.';
COMMENT ON COLUMN public.servico_tipo.codigo				IS 'Código interno do tipo de serviço. Sequência: seq_servico_tipo.';
COMMENT ON COLUMN public.servico_tipo.codigo_pai			IS 'Código interno do tipo de serviço ao qual o tipo de serviços está associado (auto relacionamento).';
COMMENT ON COLUMN public.servico_tipo.sigla					IS 'Sigla do tipo de serviço. Chave única (com código do município).';
COMMENT ON COLUMN public.servico_tipo.nome					IS 'Nome/descrição do tipo de serviço.';
COMMENT ON COLUMN public.servico_tipo.situacao				IS 'Situação do tipo de serviço: 0 = Inativo / 1 = Ativo.';
COMMENT ON COLUMN public.servico_tipo.habilita_faturamento	IS 'Indica se o tipo de serviço está habilitado para seleção no Faturamento (true) ou não (false).';
COMMENT ON COLUMN public.servico_tipo.criador				IS 'Responsável (login) pela criação do tipo de serviço.';
COMMENT ON COLUMN public.servico_tipo.data_criacao			IS 'Data/hora de criação do tipo de serviço.';
COMMENT ON COLUMN public.servico_tipo.atualizador			IS 'Responsável (login) pela última atualização do tipo de serviço.';
COMMENT ON COLUMN public.servico_tipo.data_atualizacao		IS 'Data/hora da última atualização do tipo de serviço.';

/* Set owner */
ALTER TABLE public.servico_tipo OWNER TO sgi;

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
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_cancelamento 
    FOREIGN KEY (codigo_cancelamento)				REFERENCES public.pessoa (codigo);
ALTER TABLE public.nota_saida						ADD CONSTRAINT fk_nota_saida_servico_tipo 
    FOREIGN KEY (codigo_servico_tipo)				REFERENCES servico_tipo (codigo);

ALTER TABLE servico_tipo							ADD CONSTRAINT fk_servico_tipo_servico_tipo 
    FOREIGN KEY (codigo_pai)						REFERENCES servico_tipo (codigo);

ALTER TABLE public.item_nota_saida					ADD CONSTRAINT fk_item_nota_saida_nota_saida 
    FOREIGN KEY (codigo_nota_saida)					REFERENCES public.nota_saida (codigo);

ALTER TABLE public.faturamento_etapa				ADD CONSTRAINT fk_faturamento_etapa_nota_saida 
    FOREIGN KEY (codigo_nota_saida)					REFERENCES public.nota_saida (codigo);

ALTER TABLE public.nota_saida_parcela				ADD CONSTRAINT fk_nota_saida_parcela_nota_saida 
    FOREIGN KEY (codigo_nota_saida)					REFERENCES public.nota_saida (codigo);

/* ---------------------------------------------------------------------- */
/* Parâmetro                                                              */
/* ---------------------------------------------------------------------- */
INSERT
	INTO	parametro (chave, valor)
	SELECT	'faturamento.notafiscal.documentofiscal.codigo', codigo
	FROM	documento_fiscal
	WHERE	codigo_receita = '55';
INSERT
	INTO	parametro (chave, valor)
	VALUES	('faturamento.notafiscal.isento_icms', '0');

/* ---------------------------------------------------------------------- */
/* Insert "servico_tipo"                                                  */
/* ---------------------------------------------------------------------- */
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '01', 'Serviços de informática e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01'), '01.01', 'Análise e desenvolvimento de sistemas.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.01'), '01.01.01', 'análise de sistemas,', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.01'), '01.01.02', 'desenvolvimento de sistemas', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01'), '01.02', 'Programação.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.02'), '01.02.01', 'programação', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01'), '01.03', 'Processamento de dados e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.03'), '01.03.01', 'processamento de dados ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.03'), '01.03.02', 'provimento de acesso à internet', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.03'), '01.03.03', 'provimento de voz sobre internet', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.03'), '01.03.04', 'provimento de conteúdo para a internet', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.03'), '01.03.05', 'provimento de serviço de aplicação', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.03'), '01.03.06', 'hospedagem para a internet', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.03'), '01.03.07', 'serviços de data center, parque tecnológico ou congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01'), '01.04', 'Elaboração de programas de computadores, inclusive de jogos eletrônicos.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.04'), '01.04.01', 'elaboração de programa de computadores', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.04'), '01.04.02', 'elaboração de jogo eletrônico', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.04'), '01.04.03', 'elaboração de programa de computador sob encomenda, não derivado de software preexistente, cujo desenvolvimento se dê integralmente no Brasil e cujos direitos autorais permaneçam reservados ao contratante do serviço, desde que atendidos os demais requisitos previstos no art. 8º da Instrução Normativa SMF nº 16, de 02 de julho de 2012', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01'), '01.05', 'Licenciamento ou cessão de direito de uso de programas de computação.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.05'), '01.05.01', 'licenciamento de uso de programa de computação', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.05'), '01.05.02', 'cessão de direito de uso de programa de computação', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01'), '01.06', 'Assessoria e consultoria em informática.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.06'), '01.06.01', 'assessoria ou consultoria em informática', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01'), '01.07', 'Suporte técnico em informática, inclusive instalação, configuração e manutenção de programas de computação e bancos de dados.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.07'), '01.07.01', 'suporte técnico em informática', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.07'), '01.07.02', 'instalação e/ou configuração de programa de computação e/ou banco de dados', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.07'), '01.07.03', 'manutenção de programa de computação e/ou banco de dados', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.07'), '01.07.04', 'customização de programa de computador', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.07'), '01.07.05', 'atualização de software', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01'), '01.08', 'Planejamento, confecção, manutenção e atualização de páginas eletrônicas.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.08'), '01.08.01', 'projeto de páginas eletrônicas', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.08'), '01.08.02', 'confecção ou desenvolvimento de páginas eletrônicas', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '01.08'), '01.08.03', 'manutenção e/ou atualização de páginas eletrônicas', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '02', 'Serviços de pesquisas e desenvolvimento de qualquer natureza.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '02'), '02.01', 'Serviços de pesquisas e desenvolvimento de qualquer natureza.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '02.01'), '02.01.01', 'pesquisa e desenvolvimento (P&D)', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '02.01'), '02.01.02', 'pesquisa de mercado ou de opinião pública', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '03', 'Serviços prestados mediante locação, cessão de direito de uso e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03'), '03.02', 'Cessão de direito de uso de marcas e de sinais de propaganda.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.02'), '03.02.01', 'cessão de direito de uso de sinais de propaganda', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.02'), '03.02.02', 'cessão de direito de uso de marca', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.02'), '03.02.04', 'serviços relativos à indústria cinematográfica, correspondentes a receitas de licenciamento para exibição da obra cinematográfica, exclusivamente vinculados a filme brasileiro, natural ou de enredo', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.02'), '03.02.05', 'serviços relativos à indústria cinematográfica, correspondentes a receitas de licenciamento para exibição da obra cinematográfica, vinculados a filme estrangeiro', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.02'), '03.02.06', 'serviços prestados mediante locação, cessão de direito de uso e congêneres, não especificados em outros códigos, exceto locação de bens móveis pura e simples, nos termos da Instrução Normativa SMF Nº 15, de 12/01/2012', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03'), '03.03', 'Exploração de salões de festas, centro de convenções, escritórios virtuais, stands, quadras esportivas, estádios, ginásios, auditórios, casas de espetáculos, parques de diversões, canchas e congêneres, para realização de eventos ou negócios de qualquer natureza.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.03'), '03.03.01', 'exploração de salão de festas', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.03'), '03.03.02', 'exploração de centro de convenções', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.03'), '03.03.03', 'exploração de escritório virtual', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.03'), '03.03.04', 'exploração de stand', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.03'), '03.03.05', 'exploração de quadra esportiva', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.03'), '03.03.06', 'exploração de estádio', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.03'), '03.03.07', 'exploração de ginásio', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.03'), '03.03.08', 'exploração de auditório', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.03'), '03.03.09', 'exploração de casa de espetáculos', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.03'), '03.03.10', 'exploração de parque de diversões', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03'), '03.04', 'Locação, sublocação, arrendamento, direito de passagem ou permissão de uso, compartilhado ou não, de ferrovia, rodovia, postes, cabos, dutos e condutos de qualquer natureza', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.04'), '03.04.01', 'locação, sublocação, arrendamento, direito de passagem ou permissão de uso, compartilhado ou não, de ferrovias', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.04'), '03.04.02', 'locação, sublocação, arrendamento, direito de passagem ou permissão de uso, compartilhado ou não, de rodovias', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.04'), '03.04.03', 'locação, sublocação, arrendamento, direito de passagem ou permissão de uso, compartilhado ou não, de postes', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.04'), '03.04.04', 'locação, sublocação, arrendamento, direito de passagem ou permissão de uso, compartilhado ou não, de cabos', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.04'), '03.04.05', 'locação, sublocação, arrendamento, direito de passagem ou permissão de uso, compartilhado ou não, de dutos ou condutos de qualquer natureza', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.04'), '03.04.06', 'locação, sublocação ou arrendamento de condutos em geral', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.04'), '03.04.07', 'cessão de direitos sobre ferrovia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.04'), '03.04.08', 'cessão de direitos sobre rodovia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.04'), '03.04.09', 'cessão de direitos sobre postes', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.04'), '03.04.10', 'cessão de direitos sobre cabos', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.04'), '03.04.11', 'cessão de direitos sobre dutos', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.04'), '03.04.12', 'cessão de direitos sobre condutos em geral', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03'), '03.05', 'Cessão de andaimes, palcos, coberturas e outras estruturas de uso temporário.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.05'), '03.05.01', 'cessão de andaimes', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.05'), '03.05.02', 'cessão de palco', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '03.05'), '03.05.03', 'cessão de estrutura de uso temporário', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '04', 'Serviços de saúde, assistência médica e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.01', 'Medicina e biomedicina.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.01'), '04.01.01', 'medicina', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.01'), '04.01.02', 'biomedicina', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.02', 'Análises clínicas, patologia, eletricidade médica, radioterapia, quimioterapia, ultra-sonografia, ressonância magnética, radiologia, tomografia e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.02'), '04.02.01', 'análises clínicas, patologia ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.02'), '04.02.02', 'eletricidade médica ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.02'), '04.02.03', 'radioterapia ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.02'), '04.02.04', 'quimioterapia ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.02'), '04.02.05', 'ultra-sonografia, ressonância magnética, radiologia, tomografia ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.02'), '04.02.06', 'hemoterapia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.02'), '04.02.07', 'litotripsia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.02'), '04.02.08', 'exames médicos e de saúde em geral', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.03', 'Hospitais, clínicas, laboratórios, sanatórios, manicômios, casas de saúde, prontos-socorros, ambulatórios e         congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.03'), '04.03.01', 'serviços prestados por hospital', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.03'), '04.03.02', 'Serviços de saúde ou de assistência médica prestados por hospital apto a efetuar internações', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.03'), '04.03.03', 'serviços prestados por clínica', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.03'), '04.03.04', 'Serviços de saúde ou de assistência médica prestados por clínica apta a efetuar internações', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.03'), '04.03.05', 'serviços de laboratório', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.03'), '04.03.06', 'serviços prestados por sanatório', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.03'), '04.03.07', 'Serviços de saúde ou de assistência médica prestados por sanatório apto a efetuar internações', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.03'), '04.03.08', 'serviços prestados por manicômio', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.03'), '04.03.09', 'Serviços de saúde ou de assistência médica prestados por manicômio apto a efetuar internações', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.03'), '04.03.10', 'serviços prestados por casa de saúde', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.03'), '04.03.11', 'Serviços de saúde ou de assistência médica prestados por casa de saúde apta a efetuar internações', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.03'), '04.03.12', 'serviços prestados por pronto-socorro', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.03'), '04.03.13', 'Serviços de saúde ou de assistência médica prestados por pronto-socorro apto a efetuar internações', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.03'), '04.03.14', 'serviços prestados por ambulatórios', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
-- 100
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.03'), '04.03.15', 'serviços prestados por posto de vacinação', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.04', 'Instrumentação cirúrgica.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.04'), '04.04.01', 'instrumentação cirúrgica', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.05', 'Acupuntura.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.05'), '04.05.01', 'acupuntura', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.06', 'Enfermagem, inclusive serviços auxiliares.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.06'), '04.06.01', 'enfermagem', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.06'), '04.06.02', 'enfermagem - serviços auxiliares', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.07', 'Serviços farmacêuticos.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.07'), '04.07.01', 'serviços farmacêuticos', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.08', 'Terapia ocupacional, fisioterapia e fonoaudiologia.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.08'), '04.08.01', 'terapia ocupacional', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.08'), '04.08.02', 'fisioterapia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.08'), '04.08.03', 'fonoaudiologia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.09', 'Terapias de qualquer espécie destinadas ao tratamento físico, orgânico e mental.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.09'), '04.09.01', 'terapias de qualquer espécie destinadas ao tratamento físico, orgânico ou mental', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.10', 'Nutrição.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.10'), '04.10.01', 'nutrição', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.11', 'Obstetrícia.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.11'), '04.11.01', 'obstetrícia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.12', 'Odontologia.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.12'), '04.12.01', 'odontologia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.13', 'Ortóptica.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.13'), '04.13.01', 'ortóptica', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.14', 'Próteses sob encomenda.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.14'), '04.14.01', 'confecção de próteses', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.15', 'Psicanálise.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.15'), '04.15.01', 'psicanálise', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.16', 'Psicologia.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.16'), '04.16.01', 'psicologia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.17', 'Casas de repouso e de recuperação, creches, asilos e congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.17'), '04.17.01', 'serviços de casa de repouso, de recuperação, asilo ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.18', 'Inseminação artificial, fertilização in vitro e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.18'), '04.18.01', 'inseminação artificial, fertilização in vitro ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.18'), '04.18.02', 'reprodução assistida', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.19', 'Bancos de sangue, leite, pele, olhos, óvulos, sêmen e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.19'), '04.19.01', 'serviços de banco de sangue ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.19'), '04.19.02', 'serviços de banco de leite, cordões umbilicais, pele, olhos, demais órgãos ou tecidos ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.19'), '04.19.03', 'serviços de banco de óvulos, sêmem ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.20', 'Coleta de sangue, leite, tecidos, sêmen, órgãos e materiais biológicos de qualquer espécie.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.20'), '04.20.01', 'coleta de sangue', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.20'), '04.20.02', 'coleta de leite, tecidos, sêmen, órgãos ou materiais biológicos de qualquer espécie', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.21', 'Unidade de atendimento, assistência ou tratamento móvel e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.21'), '04.21.01', 'serviços de unidade de atendimento, assistência ou tratamento móve, ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.22', 'Planos de medicina de grupo ou individual e convênios para prestação de assistência médica, hospitalar, odontológica e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.22'), '04.22.01', 'plano de medicina de grupo ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.22'), '04.22.02', 'plano de medicina individual ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.22'), '04.22.03', 'convênio para prestação de assistência médica, hospitalar, odontológica e congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04'), '04.23', 'Outros planos de saúde que se cumpram através de serviços de terceiros contratados, credenciados, cooperados ou apenas pagos pelo operador do plano mediante indicação do beneficiário.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '04.23'), '04.23.01', 'plano de saúde cumprido através de serviços de terceiros contratados, credenciados, cooperados ou apenas pagos pelo operador do plano mediante indicação do beneficiário', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '05', 'Serviços de medicina e assistência veterinária e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05'), '05.01', 'Medicina veterinária e zootecnia.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.01'), '05.01.01', 'medicina veterinária', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.01'), '05.01.02', 'zootecnia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05'), '05.02', 'Hospitais, clínicas, ambulatórios, prontos-socorros e congêneres, na área veterinária.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.02'), '05.02.01', 'serviços prestados por hospital veterinário', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.02'), '05.02.02', 'serviços prestados por clínica veterinária', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.02'), '05.02.03', 'serviços prestados por ambulatório veterinário', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.02'), '05.02.04', 'serviços prestados por pronto-socorro veterinário', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.02'), '05.02.05', 'serviços prestados por unidade de atendimento não especificada, na área veterinária', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05'), '05.03', 'Laboratórios de análise na área veterinária', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.03'), '05.03.01', 'serviços de laboratórios de análise, na área veterinária', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05'), '05.04', 'Inseminação artificial, fertilização in vitro e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.04'), '05.04.01', 'inseminação artificial, fertilização in vitro ou congênere, na área veterinária', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.04'), '05.04.02', 'reprodução animal assistida', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05'), '05.05', 'Bancos de sangue e de órgãos e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.05'), '05.05.01', 'serviços de banco de sangue animal ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.05'), '05.05.02', 'serviços de banco de órgãos de animais ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05'), '05.06', 'Coleta de sangue, leite, tecidos, sêmen, órgãos e materiais biológicos de qualquer espécie.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.06'), '05.06.01', 'Coleta de sangue, leite, tecidos, sêmen, órgãos ou materiais biológicos de qualquer espécie, de animais', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.06'), '05.06.02', 'manejo de animais', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05'), '05.07', 'Unidade de atendimento, assistência ou tratamento móvel e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.07'), '05.07.01', 'serviços de unidade de atendimento, assistência ou tratamento móvel, ou congênere, para animais', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05'), '05.08', 'Guarda, tratamento, amestramento, embelezamento, alojamento e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.08'), '05.08.01', 'guarda,  alojamento ou serviço congênere, para animais', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.08'), '05.08.02', 'tratamento, embelezamento, tosquia ou serviço congênere, para animais', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.08'), '05.08.03', 'amestramento de animais', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05'), '05.09', 'Planos de atendimento e assistência médico-veterinária.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '05.09'), '05.09.01', 'plano de atendimento e assistência médico-veterinária', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '06', 'Serviços de cuidados pessoais, estética, atividades físicas e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06'), '06.01', 'Barbearia, cabeleireiros, manicuros, pedicuros e congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.01'), '06.01.01', 'barbearia ou serviço congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.01'), '06.01.02', 'serviços de cabeleireiro ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.01'), '06.01.03', 'serviços de cabeleireiro, manicuro ou pedicuro', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.01'), '06.01.04', 'serviços típicos de salões de beleza - cortes de cabelo, maquiagem, serviços de manicuro, pedicuro e outros', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06'), '06.02', 'Esteticistas, tratamento de pele, depilação e congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.02'), '06.02.01', 'serviços de esteticistas, tratamento de pele, depilação ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.02'), '06.02.02', 'tatuagem', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06'), '06.03', 'Banhos, duchas, sauna, massagens e congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.03'), '06.03.01', 'banhos, duchas, sauna, massagens ou serviço congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.03'), '06.03.02', 'exploração de sanitários', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06'), '06.04', 'Ginástica, dança, esportes, natação, artes marciais e demais atividades físicas.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.04'), '06.04.01', 'aulas de ginástica', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.04'), '06.04.02', 'aulas de dança', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.04'), '06.04.03', 'aulas de esportes', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.04'), '06.04.04', 'aulas de natação', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.04'), '06.04.05', 'aulas de artes marciais', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.04'), '06.04.06', 'serviços de academia de ginástica', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.04'), '06.04.07', 'serviços de academia de dança', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.04'), '06.04.08', 'serviços de academia de esportes', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
--200
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.04'), '06.04.09', 'serviços de academia de natação', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.04'), '06.04.10', 'serviços de academia de artes marciais', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.04'), '06.04.11', 'serviços relativos à prática de atividades físicas', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06'), '06.05', 'Centros de emagrecimento, spa e congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '06.05'), '06.05.01', 'serviços de centro de emagrecimento, spa ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '07', 'Serviços relativos a engenharia, arquitetura, geologia, urbanismo, construção civil, manutenção, limpeza, meio ambiente, saneamento e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07'), '07.01', 'Engenharia, agronomia, agrimensura, arquitetura, geologia, urbanismo, paisagismo e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.01'), '07.01.01', 'engenharia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.01'), '07.01.02', 'agronomia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.01'), '07.01.03', 'agrimensura ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.01'), '07.01.04', 'arquitetura', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.01'), '07.01.05', 'geologia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.01'), '07.01.06', 'urbanismo ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.01'), '07.01.07', 'paisagismo ou congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.01'), '07.01.08', 'acompanhamento e fiscalização da execução de serviços de engenharia, arquitetura ou urbanismo', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.01'), '07.01.09', 'engenharia consultiva - acompanhamento e  fiscalização da execução de serviços de engenharia, arquitetura e urbanismo (relacionados com obras de construção civil, hidráulicas, de escoramento  ou de contenção de encostas)', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07'), '07.02', 'Execução, por administração, empreitada ou subempreitada, de obras de construção civil, hidráulica ou elétrica e de outras obras semelhantes, inclusive sondagem, perfuração de poços, escavação, drenagem e irrigação, terraplanagem, pavimentação, concretagem e a instalação e montagem de produtos, peças e equipamentos (exceto o fornecimento de mercadorias produzidas pelo prestador de serviços fora do local da prestação dos serviços, que fica sujeito ao ICMS).', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.02', 'execução, por empreitada ou subempreitada, de obra licenciada de construção civil, visando a erguimento de edificação para utilização como hotel', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.03', 'execução, por empreitada ou subempreitada, de obra licenciada de construção civil, visando à transformação de imóvel em hotel', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.04', 'execução, por empreitada ou subempreitada, de obra licenciada de construção civil, visando a acréscimo de edificação para aumentar o número de apartamentos de hotel já em funcionamento', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.05', 'execução, por empreitada ou subempreitada, de obra licenciada de construção civil, visando à incorporação, a hotel já em funcionamento, de imóvel ou parte de imóvel antes não utilizado com finalidade hoteleira, criando-se novos apartamentos', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.07', 'execução, por administração, de obra licenciada de construção civil, visando a erguimento de edificação para utilização como hotel', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.08', 'execução, por administração, de obra licenciada de construção civil, visando à transformação de imóvel em hotel', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.09', 'execução, por administração, de obra licenciada de construção civil, visando a acréscimo de edificação para aumentar o número de apartamentos de hotel já em funcionamento', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.10', 'execução, por administração, de obra licenciada de construção civil, visando à incorporação, a hotel já em funcionamento, de imóvel ou parte de imóvel antes não utilizado com finalidade hoteleira, criando-se novos apartamentos', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.11', 'execução, por empreitada ou subempreitada, de obras hidráulicas', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.12', 'execução, por empreitada ou subempreitada, de obras hidráulicas componentes de obra licenciada, visando a: erguimento de edificação para utilização como hotel; transformação de imóvel em hotel; acréscimo de edificação para aumentar o número de apartamentos de hotel já em funcionamento; ou incorporação, a hotel já em funcionamento, de imóvel ou parte de imóvel antes não utilizado com finalidade hoteleira, criando-se novos apartamentos', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.13', 'execução, por administração, de obras hidráulicas', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.14', 'execução, por administração, de obras hidráulicas componentes de obra licenciada, visando a: erguimento de edificação para utilização como hotel; transformação de imóvel em hotel; acréscimo de edificação para aumentar o número de apartamentos de hotel já em funcionamento; ou incorporação, a hotel já em funcionamento, de imóvel ou parte de imóvel antes não utilizado com finalidade hoteleira, criando-se novos apartamentos', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.15', 'execução, por empreitada ou subempreitada, de obras elétricas', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.16', 'execução, por empreitada ou subempreitada, de obras elétricas componentes de obra licenciada, visando a: erguimento de edificação para utilização como hotel; transformação de imóvel em hotel; acréscimo de edificação para aumentar o número de apartamentos de hotel já em funcionamento; ou incorporação, a hotel já em funcionamento, de imóvel ou parte de imóvel antes não utilizado com finalidade hoteleira, criando-se novos apartamentos', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.17', 'execução, por administração, de obras elétricas', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.18', 'execução, por administração, de obras elétricas componentes de obra licenciada, visando a: erguimento de edificação para utilização como hotel; transformação de imóvel em hotel; acréscimo de edificação para aumentar o número de apartamentos de hotel já em funcionamento; ou incorporação, a hotel já em funcionamento, de imóvel ou parte de imóvel antes não utilizado com finalidade hoteleira, criando-se novos apartamentos', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.19', 'obras assemelhadas a  obras de construção civil, hidráulicas ou elétricas', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.20', 'obras assemelhadas a  obras de construçao civil, hidráulicas ou elétricas, inclusive sondagem, perfuração de poços, escavação, drenagem e irrigação, terraplanagem, pavimentação, concretagem e a instalação e montagem de produtos, peças e equipamentos quando tais obras ou serviços forem componentes de obra, licenciada, que vise a: erguimento de edificação para utilização como hotel; transformação de imóvel em hotel; acréscimo de edificação para aumentar o número de apartamentos de hotel já em funcionamento; ou incorporação, a hotel já em funcionamento, de imóvel ou parte de imóvel antes não utilizado com finalidade hoteleira, criando-se novos apartamentos', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.21', 'construção de empreendimentos habitacionais de interesse social e de arrendamento residencial assim entendidos aqueles expressamente reconhecidos pela Secretaria Municipal de Habitação como inseridos na política habitacional municipal, estadual e federal, destinados à população com renda de até dez salários mínimos, conforme a Lei nº 5.065, de 10 de julho de 2009', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.22', 'sondagem', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.23', 'perfuração de poços', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.24', 'escavação', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.25', 'drenagem', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.26', 'irrigação', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.27', 'terraplanagem', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.28', 'pavimentação', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.29', 'concretagem de fundações ou contenções', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.30', 'concretagem, exceto fundações ou contenções', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.31', 'instalação de equipamentos, incluindo elevadores ou congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.32', 'montagem de equipamentos, incluindo elevadores ou congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.33', 'instalação de peças', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.34', 'montagem de peças', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '07.02'), '07.02.35', 'montagem de fôrmas in loco de fundações ou contenções', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
--250