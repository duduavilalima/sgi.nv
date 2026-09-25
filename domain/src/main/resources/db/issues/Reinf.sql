CREATE SEQUENCE public.seq_reinf;
ALTER TABLE public.seq_reinf OWNER TO sgi;

CREATE SEQUENCE public.seq_reinf_registro_evento;
ALTER SEQUENCE public.seq_reinf_registro_evento OWNER TO sgi;

CREATE SEQUENCE public.seq_reinf_evento;
ALTER TABLE public.seq_reinf_evento OWNER TO sgi;

CREATE SEQUENCE public.seq_reinf_evento_nf;
ALTER TABLE public.seq_reinf_evento_nf OWNER TO sgi;

CREATE SEQUENCE public.seq_reinf_evento_anexo;
ALTER TABLE public.seq_reinf_evento_anexo OWNER TO sgi;

CREATE SEQUENCE public.seq_reinf_evento_erro;
ALTER SEQUENCE public.seq_reinf_evento_erro OWNER TO sgi;


CREATE TABLE public.reinf
(
  codigo bigint NOT NULL DEFAULT nextval('seq_reinf'::regclass),
  codigo_unidade bigint NOT NULL, -- Código da empresa (SA ou Norte)
  referencia character(7) NOT NULL, -- Ano e mês referência para a geração dos eventos do REINF
  data_criacao timestamp without time zone NOT NULL DEFAULT now(),
  data_atualizacao timestamp without time zone, -- Data/hora da última atualização  
  situacao smallint NOT NULL DEFAULT 0,
  CONSTRAINT pk_reinf PRIMARY KEY (codigo),
  CONSTRAINT fk_reinf_unidade FOREIGN KEY (codigo_unidade)
      REFERENCES public.unidade (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.reinf OWNER TO sgi;

COMMENT ON COLUMN public.reinf.referencia IS 'Ano e mês referência para a geração dos eventos do REINF';
COMMENT ON COLUMN public.reinf.data_atualizacao IS 'Data/hora da última atualização';
COMMENT ON COLUMN public.reinf.codigo_unidade IS 'Código da empresa (SA ou Norte)';

CREATE INDEX fki_reinf_unidade ON public.reinf USING btree (codigo_unidade);

CREATE TABLE public.reinf_registro_evento
(
  codigo bigint NOT NULL DEFAULT nextval('seq_reinf_registro_evento'::regclass),
  nome character(6) NOT NULL,
  descricao character varying NOT NULL,
  namespace character varying,
  periodico boolean NOT NULL,
  obrigatorio boolean NOT NULL,
  CONSTRAINT pk_reinf_registro_evento PRIMARY KEY (codigo)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.reinf_registro_evento OWNER TO sgi;
COMMENT ON TABLE public.reinf_registro_evento IS 'Descreve cada um dos eventos do REINF ';

INSERT INTO reinf_registro_evento (nome, descricao, namespace, periodico, obrigatorio)
VALUES ('R-1000', 'Informações do Contribuinte', 'evtInfoContribuinte', false, true);

INSERT INTO reinf_registro_evento (nome, descricao, namespace, periodico, obrigatorio)
VALUES ('R-1070', 'Tabela de Processos Administrativos/Judiciais', 'evtTabProcesso', false, true);
		
INSERT INTO reinf_registro_evento (nome, descricao, namespace, periodico, obrigatorio)
VALUES ('R-2010', 'Retenção Contribuição Previdenciária - Serviços Tomados', 'evtTomadorServicos', true, true);

INSERT INTO reinf_registro_evento (nome, descricao, namespace, periodico, obrigatorio)
VALUES ('R-2020', 'Retenção Contribuição Previdenciária - Serviços Prestados', 'evtPrestadorServicos', true, true);

INSERT INTO reinf_registro_evento (nome, descricao, namespace, periodico, obrigatorio)
VALUES ('R-2030', 'Recursos Recebidos por Associação Desportiva', 'evtRecursoRecebidoAssociacao', true, false);

INSERT INTO reinf_registro_evento (nome, descricao, namespace, periodico, obrigatorio)
VALUES ('R-2040', 'Recursos Repassados para Associação Desportiva', 'evtRecursoRepassadoAssociacao', true, false);

INSERT INTO reinf_registro_evento (nome, descricao, namespace, periodico, obrigatorio)
VALUES ('R-2050', 'Comercialização da Produção por Produtor Rural PJ/Agroindústria', 'evtInfoProdRural', true, false);
		
INSERT INTO reinf_registro_evento (nome, descricao, namespace, periodico, obrigatorio)
VALUES ('R-2060', 'Contribuição Previdenciária sobre a Receita Bruta - CPRB', 'evtInfoCPRB', true, true);
		
INSERT INTO reinf_registro_evento (nome, descricao, namespace, periodico, obrigatorio)
VALUES ('R-2070', 'Retenções na Fonte - IR, CSLL, Cofins, PIS/PASEP', null, true, false);
	
INSERT INTO reinf_registro_evento (nome, descricao, namespace, periodico, obrigatorio)
VALUES ('R-2098', 'Reabertura dos Eventos Periódicos', 'evtReabreEvPer', false, false);

INSERT INTO reinf_registro_evento (nome, descricao, namespace, periodico, obrigatorio)
VALUES ('R-2099', 'Fechamento dos Eventos Periódicos', 'evtFechamento', true, true);
	
INSERT INTO reinf_registro_evento (nome, descricao, namespace, periodico, obrigatorio)
VALUES ('R-3010', 'Receita de Espetáculo Desportivo', 'evtEspDesportivo', false, false);
		
INSERT INTO reinf_registro_evento (nome, descricao, namespace, periodico, obrigatorio)
VALUES ('R-5001', 'Informações de bases e tributos por evento', null, false, false);
		
INSERT INTO reinf_registro_evento (nome, descricao, namespace, periodico, obrigatorio)
VALUES ('R-5011', 'Informações de bases e tributos consolidadas por período de apuração', null, false, true);
	
INSERT INTO reinf_registro_evento (nome, descricao, namespace, periodico, obrigatorio)
VALUES ('R-9000', 'Exclusão de Eventos', 'evtExclusao', false, false);


CREATE TABLE public.reinf_evento
(
  codigo bigint NOT NULL DEFAULT nextval('seq_reinf_evento'::regclass),
  codigo_reinf bigint,
  codigo_prestador bigint, -- Codigo do prestador do serviço. Utilizado para controlar os envios de eventos R-2010
  codigo_cliente bigint, -- Codigo do cliente do contratante do serviço. Utilizado para controlar os envios de eventos R-2020
  codigo_registro_evento bigint NOT NULL, -- Identificação do registro do evento ...
  tipo_evento smallint NOT NULL, -- Informe [1] para arquivo original ou [2] para arquivo de retificação.  
  data_criacao timestamp without time zone NOT NULL DEFAULT now(),
  data_envio timestamp without time zone, -- Data de envio para a receita  
  id_transmissao character(36) NOT NULL,  
  numero_recibo character(24), -- Número do recibo de entrega
  situacao smallint NOT NULL, -- 0 - Evento possui arquivo gerado e aguardando envio...
  CONSTRAINT pk_reinf_evento PRIMARY KEY (codigo),
  CONSTRAINT fk_cliente FOREIGN KEY (codigo_cliente) REFERENCES public.pessoa (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_pessoa FOREIGN KEY (codigo_prestador) REFERENCES public.pessoa (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_reinf FOREIGN KEY (codigo_reinf) REFERENCES public.reinf (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_registro_evento FOREIGN KEY (codigo_registro_evento) REFERENCES public.reinf_registro_evento (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.reinf_evento OWNER TO sgi;
COMMENT ON COLUMN public.reinf_evento.tipo_evento IS 'Informe [1] para arquivo original ou [2] para arquivo de retificação.';
COMMENT ON COLUMN public.reinf_evento.codigo_registro_evento IS  'Identificação do registro do evento 
R_1000 // R_1070 // R_2010 // R_2020 // R_2030 // R_2040 // R_2050 // R_2060 // R_2070 // R_2098 // R_2099 // R_3010 // R_5001 // R_5011 // R_9000';

COMMENT ON COLUMN public.reinf_evento.data_envio IS 'Data de envio para a receita';
COMMENT ON COLUMN public.reinf_evento.codigo_prestador IS 'Codigo do prestador do serviço. Utilizado para controlar os envios de eventos R-2010';
COMMENT ON COLUMN public.reinf_evento.situacao IS '0 - Evento possui arquivo gerado e aguardando envio
1 - Evento enviado com sucesso
2 - Evento enviado, porém contém erros de validação';
COMMENT ON COLUMN public.reinf_evento.codigo_cliente IS ' -- Codigo do cliente do contratante do serviço. Utilizado para controlar os envios de eventos R-2020';

-- Index: public.fki_cliente
CREATE INDEX fki_cliente ON public.reinf_evento USING btree (codigo_cliente);

-- Index: public.fki_pessoa
CREATE INDEX fki_pessoa ON public.reinf_evento USING btree (codigo_prestador);


CREATE TABLE public.reinf_evento_erro
(
   codigo bigint NOT NULL DEFAULT nextval('seq_reinf_evento_erro'::regclass),
   codigo_reinf_evento bigint NOT NULL, 
   id_erro character(15) NOT NULL, 
   descricao character varying(255) NOT NULL, 
   CONSTRAINT pk_reinf_evento_erro PRIMARY KEY (codigo), 
   CONSTRAINT fr_reinf_evento FOREIGN KEY (codigo_reinf_evento) REFERENCES public.reinf_evento (codigo) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH (
  OIDS = FALSE
)
;
ALTER TABLE public.reinf_evento_erro  OWNER TO sgi;



CREATE TABLE public.reinf_evento_nota_fiscal
(
  codigo bigint NOT NULL DEFAULT nextval('seq_reinf_evento_nf'::regclass),
  codigo_reinf_evento bigint NOT NULL,
  codigo_nota_fiscal bigint NOT NULL,
  CONSTRAINT pk_reinf_evento_nf PRIMARY KEY (codigo),
/*  CONSTRAINT fk_nota_fiscal FOREIGN KEY (codigo_nota_fiscal) REFERENCES public.nota_fiscal (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION, */
  CONSTRAINT fk_reinf_evento FOREIGN KEY (codigo_reinf_evento) REFERENCES public.reinf_evento (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.reinf_evento_nota_fiscal OWNER TO sgi;


CREATE TABLE public.reinf_evento_anexo
(
   codigo bigint DEFAULT nextval('seq_reinf_evento_anexo'), 
   codigo_reinf_evento bigint NOT NULL, 
   codigo_anexo bigint NOT NULL, 
   tipo smallint NOT NULL,
   CONSTRAINT pk_reinf_evento_anexo PRIMARY KEY (codigo), 
   CONSTRAINT fk_reinf_evento FOREIGN KEY (codigo_reinf_evento) REFERENCES public.reinf_evento (codigo) ON UPDATE NO ACTION ON DELETE NO ACTION, 
   CONSTRAINT fk_anexo FOREIGN KEY (codigo_anexo) REFERENCES public.anexo (codigo) ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITH (
  OIDS = FALSE
)
;
ALTER TABLE public.reinf_evento_anexo OWNER TO sgi;
COMMENT ON COLUMN public.reinf_evento_anexo.tipo IS '0 - arquivo de envio / 1 - arquivo de retorno';

/* Já executado em produção no dia 28/08/2018

CREATE SEQUENCE public.seq_reinf_servico;
ALTER SEQUENCE public.seq_reinf_servico OWNER TO sgi;

CREATE TABLE public.reinf_servico
(
   codigo bigint NOT NULL DEFAULT nextval('seq_reinf_servico'::regclass),
   codigo_classificacao character(9) NOT NULL, 
   tipo_servico character varying NOT NULL, 
   CONSTRAINT pk_reinf_servico PRIMARY KEY (codigo)
) 
WITH (
  OIDS = FALSE
)
;
ALTER TABLE public.reinf_servico OWNER TO sgi;

INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000001', 'Limpeza, conservação ou zeladoria');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000002', 'Vigilância ou segurança');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000003', 'Construção civil');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000004', 'Serviços de natureza rural');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000005', 'Digitação');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000006', 'Preparação de dados para processamento');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000007', 'Acabamento');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000008', 'Embalagem');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000009', 'Acondicionamento');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000010', 'Cobrança');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000011', 'Coleta ou reciclagem de lixo ou de resíduos');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000012', 'Copa');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000013', 'Hotelaria');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000014', 'Corte ou ligação de serviços públicos');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000015', 'Distribuição');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000016', 'Treinamento e ensino');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000017', 'Entrega de contas e de documentos');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000018', 'Ligação de medidores');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000019', 'Leitura de medidores');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000020', 'Manutenção de instalações, de máquinas ou de equipamentos');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000021', 'Montagem');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000022', 'Operação de máquinas, de equipamentos e de veículos');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000023', 'Operação de pedágio ou de terminal de transporte');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000024', 'Operação de transporte de passageiros');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000025', 'Portaria, recepção ou ascensorista');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000026', 'Recepção, triagem ou movimentação de materiais');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000027', 'Promoção de vendas ou de eventos');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000028', 'Secretaria e expediente');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000029', 'Saúde');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000030', 'Telefonia ou telemarketing');
INSERT INTO reinf_servico(codigo_classificacao, tipo_servico) VALUES ('100000031', 'Trabalho temporário na forma da Lei no 6.019, de janeiro de 1974');

ALTER TABLE public.servico_tipo ADD COLUMN codigo_reinf bigint;
ALTER TABLE public.servico_tipo ADD CONSTRAINT fk_reinf_servico FOREIGN KEY (codigo_reinf) REFERENCES public.reinf_servico (codigo) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_reinf_servico ON public.servico_tipo(codigo_reinf);

UPDATE servico_tipo SET codigo_reinf = 6 WHERE codigo = 20;
UPDATE servico_tipo SET codigo_reinf = 3 WHERE codigo = 231;
UPDATE servico_tipo SET codigo_reinf = 3 WHERE codigo = 233;
UPDATE servico_tipo SET codigo_reinf = 3 WHERE codigo = 234;
UPDATE servico_tipo SET codigo_reinf = 3 WHERE codigo = 235;
UPDATE servico_tipo SET codigo_reinf = 3 WHERE codigo = 269;
UPDATE servico_tipo SET codigo_reinf = 3 WHERE codigo = 284;
UPDATE servico_tipo SET codigo_reinf = 3 WHERE codigo = 302;
UPDATE servico_tipo SET codigo_reinf = 3 WHERE codigo = 308;
UPDATE servico_tipo SET codigo_reinf = 3 WHERE codigo = 393;
UPDATE servico_tipo SET codigo_reinf = 1 WHERE codigo = 399;
UPDATE servico_tipo SET codigo_reinf = 3 WHERE codigo = 410;
UPDATE servico_tipo SET codigo_reinf = 3 WHERE codigo = 435;
UPDATE servico_tipo SET codigo_reinf = 26 WHERE codigo = 551;
UPDATE servico_tipo SET codigo_reinf = 20 WHERE codigo = 676;
UPDATE servico_tipo SET codigo_reinf = 20 WHERE codigo = 697;
UPDATE servico_tipo SET codigo_reinf = 1 WHERE codigo = 723;
UPDATE servico_tipo SET codigo_reinf = 20 WHERE codigo = 776;
UPDATE servico_tipo SET codigo_reinf = 3 WHERE codigo = 787;
UPDATE servico_tipo SET codigo_reinf = 30 WHERE codigo = 954;
UPDATE servico_tipo SET codigo_reinf = 3 WHERE codigo = 970;
UPDATE servico_tipo SET codigo_reinf = 3 WHERE codigo = 973;
UPDATE servico_tipo SET codigo_reinf = 3 WHERE codigo = 993;
UPDATE servico_tipo SET codigo_reinf = 3 WHERE codigo = 1105;
UPDATE servico_tipo SET codigo_reinf = 17 WHERE codigo = 1121;
UPDATE servico_tipo SET codigo_reinf = 3 WHERE codigo = 1141;

*/


INSERT INTO parametro(chave, valor) VALUES ('controlefiscal.reinf.anexo.diretorio', '/var/lib/sgi/reinf');



