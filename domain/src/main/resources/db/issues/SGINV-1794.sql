/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2018-08-08 14:56                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.servico_tipo					DROP CONSTRAINT fk_servico_tipo_servico_tipo;

ALTER TABLE public.nota_saida_parcela_retencao	DROP CONSTRAINT fk_nota_saida_parcela_retencao_nota_saida_parcela;
ALTER TABLE public.nota_saida_parcela_retencao	DROP CONSTRAINT fk_nota_saida_parcela_retencao_tipo;

ALTER TABLE public.nota_saida					DROP CONSTRAINT fk_nota_saida_servico_tipo;

ALTER TABLE public.faturamento					DROP CONSTRAINT fk_faturamento_servico_tipo;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "servico_tipo"                                 */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.servico_tipo DROP CONSTRAINT pk_servico_tipo;
ALTER TABLE public.servico_tipo DROP CONSTRAINT ck_servico_tipo_situacao;

/* Create "new" table */
CREATE TABLE public.servico_tipo_TMP (
    codigo					BIGINT					DEFAULT nextval('seq_servico_tipo')	NOT NULL,
    codigo_pai				BIGINT,
    sigla					CHARACTER VARYING(30)										NOT NULL,
    nome					CHARACTER VARYING(1000)										NOT NULL,
    situacao				SMALLINT				DEFAULT 0							NOT NULL,
    habilita_faturamento	BOOLEAN					DEFAULT false						NOT NULL,
    tipo_faturamento		SMALLINT				DEFAULT 0,
    criador					CHARACTER VARYING(255),
    data_criacao			TIMESTAMP,
    atualizador				CHARACTER VARYING(255),
    data_atualizacao		TIMESTAMP);

/* Move data from "old" table to "new" table */
INSERT INTO public.servico_tipo_TMP
    (codigo,codigo_pai,sigla,nome,situacao,habilita_faturamento,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_pai,sigla,nome,situacao,habilita_faturamento,criador,data_criacao,atualizador,data_atualizacao
FROM public.servico_tipo;

/* Drop indexes  */
DROP INDEX public.ix_servico_tipo_pai;
DROP INDEX public.uq_servico_tipo_sigla;

/* Drop "old" table  */
DROP TABLE public.servico_tipo;

/* Rename "old" table to "new" table */
ALTER TABLE public.servico_tipo_TMP
	RENAME TO servico_tipo;

/* Create primary key */
ALTER TABLE public.servico_tipo
	ADD CONSTRAINT pk_servico_tipo 
    PRIMARY KEY (codigo);

/* Create constraints */
ALTER TABLE public.servico_tipo
	ADD CONSTRAINT ck_servico_tipo_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1]));

ALTER TABLE public.servico_tipo
	ADD CONSTRAINT ck_servico_tipo_tipo_faturamento 
    CHECK (tipo_faturamento = ANY (ARRAY[0, 1, 2]));

/* Create indexes */
CREATE INDEX ix_servico_tipo_pai			ON public.servico_tipo (codigo_pai);
CREATE UNIQUE INDEX uq_servico_tipo_sigla	ON public.servico_tipo (sigla);

/* Comments */
COMMENT ON TABLE public.servico_tipo						IS 'Tabela de tipos de serviço de cada Prefeitura Municipal.';
COMMENT ON COLUMN public.servico_tipo.codigo				IS 'Código interno do tipo de serviço. Sequência: seq_servico_tipo.';
COMMENT ON COLUMN public.servico_tipo.codigo_pai			IS 'Código interno do tipo de serviço ao qual o tipo de serviços está associado (auto relacionamento).';
COMMENT ON COLUMN public.servico_tipo.sigla					IS 'Sigla do tipo de serviço. Chave única (com código do município).';
COMMENT ON COLUMN public.servico_tipo.nome					IS 'Nome/descrição do tipo de serviço.';
COMMENT ON COLUMN public.servico_tipo.situacao				IS 'Situação do tipo de serviço: 0 = Inativo / 1 = Ativo.';
COMMENT ON COLUMN public.servico_tipo.habilita_faturamento	IS 'Indica se o tipo de serviço está habilitado para seleção no Faturamento (true) ou não (false).';
COMMENT ON COLUMN public.servico_tipo.tipo_faturamento		IS 'Relacionamento do tipo de serviço com o faturamento: 0 = Não habilitado para o faturamento / 1 = Habilitado para o faturamento / 2 = Habilitado para o faturamento E permite seleção da cidade da matriz do contrato como local de prestação dos serviços.';
COMMENT ON COLUMN public.servico_tipo.criador				IS 'Responsável (login) pela criação do tipo de serviço.';
COMMENT ON COLUMN public.servico_tipo.data_criacao			IS 'Data/hora de criação do tipo de serviço.';
COMMENT ON COLUMN public.servico_tipo.atualizador			IS 'Responsável (login) pela última atualização do tipo de serviço.';
COMMENT ON COLUMN public.servico_tipo.data_atualizacao		IS 'Data/hora da última atualização do tipo de serviço.';

/* Set owner */
ALTER TABLE public.servico_tipo OWNER TO sgi;

/* Update data */
UPDATE servico_tipo SET tipo_faturamento = 0 WHERE habilita_faturamento = false;
UPDATE servico_tipo SET tipo_faturamento = 1 WHERE habilita_faturamento = true;
UPDATE servico_tipo SET tipo_faturamento = 2 WHERE habilita_faturamento = true AND LEFT(sigla, 2) = '14';

/* Alter table */
ALTER TABLE public.servico_tipo
	ALTER COLUMN tipo_faturamento SET NOT NULL;

ALTER TABLE public.servico_tipo
	DROP COLUMN habilita_faturamento;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "nota_saida_parcela_retencao"                  */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.nota_saida_parcela_retencao DROP CONSTRAINT pk_nota_saida_parcela_retencao;

/* Create "new" table */
CREATE TABLE public.nota_saida_parcela_retencao_TMP (
    codigo						BIGINT			DEFAULT nextval('seq_nota_saida_parcela_retencao')	NOT NULL,
    codigo_nota_saida_parcela	BIGINT																NOT NULL,
    codigo_tipo_retencao		BIGINT																NOT NULL,
    tipo						SMALLINT,
    valor_a_reter				NUMERIC(9,2)	DEFAULT 0.00										NOT NULL,
    valor_retido				NUMERIC(9,2)	DEFAULT 0.00										NOT NULL);

/* Move data from "old" table to "new" table */
INSERT INTO public.nota_saida_parcela_retencao_TMP
    (codigo,codigo_nota_saida_parcela,codigo_tipo_retencao,valor_a_reter,valor_retido)
SELECT
    codigo,codigo_nota_saida_parcela,codigo_tipo_retencao,valor_a_reter,valor_retido
FROM public.nota_saida_parcela_retencao;

/* Drop indexes  */
DROP INDEX public.ix_nota_saida_parcela_retencao_nota_saida_parcela;
DROP INDEX public.ix_nota_saida_parcela_retencao_tipo_retencao;
DROP INDEX public.uq_nota_saida_parcela_retencao_nota_saida_parcela_tipo_retencao;

/* Drop "old" table  */
DROP TABLE public.nota_saida_parcela_retencao;

/* Rename "old" table to "new" table */
ALTER TABLE public.nota_saida_parcela_retencao_TMP
	RENAME TO nota_saida_parcela_retencao;

/* Create primary key */
ALTER TABLE public.nota_saida_parcela_retencao
	ADD CONSTRAINT pk_nota_saida_parcela_retencao 
    PRIMARY KEY (codigo);

/* Create indexes */
CREATE INDEX ix_nota_saida_parcela_retencao_nota_saida_parcela				ON public.nota_saida_parcela_retencao (codigo_nota_saida_parcela);
CREATE UNIQUE INDEX uq_nota_saida_parcela_retencao_nota_saida_parcela_tipo	ON public.nota_saida_parcela_retencao (codigo_nota_saida_parcela,tipo);

/* Comments */
COMMENT ON TABLE public.nota_saida_parcela_retencao								IS 'Retenções realizadas nas parcelas.';
COMMENT ON COLUMN public.nota_saida_parcela_retencao.codigo						IS 'Código interno da retenção para a parcela da nota de saída.Sequência: seq_nota_saida_parcela_retencao.';
COMMENT ON COLUMN public.nota_saida_parcela_retencao.codigo_nota_saida_parcela	IS 'Código interno da parcela da nota de saída.';
COMMENT ON COLUMN public.nota_saida_parcela_retencao.codigo_tipo_retencao		IS 'Código interno do tipo de retenção.';
COMMENT ON COLUMN public.nota_saida_parcela_retencao.tipo						IS 'Tipo de retenção: 0 = Retenção contratual / 1 = Retenção acessória.';
COMMENT ON COLUMN public.nota_saida_parcela_retencao.valor_a_reter				IS 'Valor originalmente a ser retirdo (percentual de retenção do tipo aplicado ao valor da parcela).';
COMMENT ON COLUMN public.nota_saida_parcela_retencao.valor_retido				IS 'Soma dos valores retidos no(s) recebimento(s), ou seja, a cada recebimento associado ao faturamento/retenção deve-se somar o valor aqui. Ao final, este campo conterá o valor verdadeiramente retido pelo cliente (que poderá ser diferente do valor que deveria ter sido retido).';

/* Set owner */
ALTER TABLE public.nota_saida_parcela_retencao OWNER TO sgi;

/* Update data */
UPDATE nota_saida_parcela_retencao SET tipo = 0 WHERE codigo_tipo_retencao = 1;
UPDATE nota_saida_parcela_retencao SET tipo = 1 WHERE codigo_tipo_retencao = 2;

/* Alter table */
ALTER TABLE public.nota_saida_parcela_retencao
	ALTER COLUMN tipo SET NOT NULL;

ALTER TABLE public.nota_saida_parcela_retencao
	DROP COLUMN codigo_tipo_retencao;

/* Create constraint */
ALTER TABLE public.nota_saida_parcela_retencao
	ADD CONSTRAINT ck_nota_saida_parcela_retencao_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1]));

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.servico_tipo					ADD CONSTRAINT fk_servico_tipo_servico_tipo 
    FOREIGN KEY (codigo_pai)					REFERENCES public.servico_tipo (codigo);

ALTER TABLE public.nota_saida_parcela_retencao	ADD CONSTRAINT fk_nota_saida_parcela_retencao_nota_saida_parcela 
    FOREIGN KEY (codigo_nota_saida_parcela)		REFERENCES public.nota_saida_parcela (codigo);

ALTER TABLE public.nota_saida					ADD CONSTRAINT fk_nota_saida_servico_tipo 
    FOREIGN KEY (codigo_servico_tipo)			REFERENCES public.servico_tipo (codigo);

ALTER TABLE public.faturamento					ADD CONSTRAINT fk_faturamento_servico_tipo 
    FOREIGN KEY (codigo_servico_tipo)			REFERENCES public.servico_tipo (codigo);
