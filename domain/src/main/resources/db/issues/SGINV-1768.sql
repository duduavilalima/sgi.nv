/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3                     */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2018-07-17 17:00                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.contabilista							DROP CONSTRAINT fk_contabilista_pessoa_fisica;

ALTER TABLE public.nota_fiscal							DROP CONSTRAINT fk_nota_fiscal_documento_fiscal;

ALTER TABLE public.nota									DROP CONSTRAINT fk_nota_natureza_pagamento;

ALTER TABLE public.pagamento							DROP CONSTRAINT fk_pagamento_natureza_pagamento;

ALTER TABLE public.parcela_nota							DROP CONSTRAINT fk_parcela_nota_pagamento;
ALTER TABLE public.parcela_nota							DROP CONSTRAINT fk_parcela_nota_nota;

ALTER TABLE public.fornecedor_nat_fornecimento			DROP CONSTRAINT fk_fornecedor_nat_fornecimento_natureza_fornecimento;
ALTER TABLE public.fornecedor_nat_fornecimento			DROP CONSTRAINT fk_fornecedor_nat_fornecimento_pessoa;

ALTER TABLE public.colaborador_centro_financeiro		DROP CONSTRAINT fk_colaborador_centro_financeiro_centro_financeiro;
ALTER TABLE public.colaborador_centro_financeiro		DROP CONSTRAINT fk_colaborador_centro_financeiro_pessoa;

ALTER TABLE public.natureza_despesa						DROP CONSTRAINT fk_natureza_despesa_colaborador;
ALTER TABLE public.natureza_despesa						DROP CONSTRAINT fk_natureza_despesa_tipo_natureza_despesa;

ALTER TABLE public.item_demonstrativo					DROP CONSTRAINT fk_item_demonstrativo_natureza_despesa;

ALTER TABLE public.natureza_despesa_conta_contabil		DROP CONSTRAINT fk_natureza_despesa_conta_contabil_natureza_despesa;

ALTER TABLE public.modulo								DROP CONSTRAINT fk87de9a769ebb8129;
ALTER TABLE public.modulo								DROP CONSTRAINT fk87de9a764930c140;

ALTER TABLE public.usuario								DROP CONSTRAINT fk_usuario_receptor;

ALTER TABLE public.permissao							DROP CONSTRAINT fkce6399678988d433;

ALTER TABLE public.perfil 								DROP CONSTRAINT fk_perfil_receptor;

ALTER TABLE public.funcionalidade						DROP CONSTRAINT fk491699183350af67;

ALTER TABLE public.motivo_retorno_documento_cf			DROP CONSTRAINT fk_motivo_retorno_documento_cf_centro_financeiro;
ALTER TABLE public.motivo_retorno_documento_cf			DROP CONSTRAINT fk_motivo_retorno_documento_cf_motivo_retorno_documento;

ALTER TABLE public.motivo_retorno_documento_contrato	DROP CONSTRAINT fk_motivo_retorno_documento_contrato_contrato;
ALTER TABLE public.motivo_retorno_documento_contrato	DROP CONSTRAINT fk_motivo_retorno_documento_contrato_motivo_retorno_documento;

/* ---------------------------------------------------------------------- */
/* Modify table "unidade"                                                 */
/* ---------------------------------------------------------------------- */
/* Alter table */
ALTER TABLE public.unidade
	ADD emite_nf_faturamento BOOLEAN DEFAULT true,
	ALTER COLUMN tipo SET DEFAULT 1;

/* Add constraint */
ALTER TABLE public.unidade
	ADD CONSTRAINT ck_unidade_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1]));

/* Comments */
COMMENT ON COLUMN public.unidade.codigo					IS 'Código interno da unidade (empresa/filial - código interno da pessoa).';
COMMENT ON COLUMN public.unidade.codigo_matriz			IS 'Código interno da matriz (autorelacionamento, se filial) ou nulo se for uma matriz.';
COMMENT ON COLUMN public.unidade.zerar_numeracao_nf_ano	IS 'Indica se a numeração da nota fiscal de saída (faturamento) da unidade será reiniciada a cada ano de emissão (true) ou não (false).';
COMMENT ON COLUMN public.unidade.mascara_conta_contabil	IS 'Máscara para o número das contas contábeis da unidade';
COMMENT ON COLUMN public.unidade.emite_nf_faturamento	IS 'Indica se será possível emitir NF/Faturamento para a unidade (true) ou não (false).';

/* Update data */
UPDATE unidade SET emite_nf_faturamento = false;
UPDATE unidade SET emite_nf_faturamento = true WHERE codnome IN ('Matriz - Araujo Abreu', 'Filial São Paulo', 'Filial Belo Horizonte', 'Filial Brasília', 'Matriz - Norte');

/* Alter table */
ALTER TABLE public.unidade
	ALTER COLUMN emite_nf_faturamento SET NOT NULL;

/* ---------------------------------------------------------------------- */
/* Adjust data into table "nota_fiscal"                                   */
/* ---------------------------------------------------------------------- */
/* Comments */
COMMENT ON COLUMN public.nota_fiscal.tipo_frete IS 'Tipo do frete (apenas para NF/Materiais ou NF/Devolução): 0 = Por conta do destinatário/remetente / 1 = Por conta do emitente / 2 = Por conta de terceiros / 3 = Sem frete.';

/* Update data */
UPDATE nota_fiscal SET tipo_frete = 2 WHERE tipo_frete = 0 AND tipo_nota_fiscal IN (0, 1, 2, 3, 4, 5, 8, 9, 10);			--Exceto: 6 = Devolução parcial / 7 = Devolução total / 11 = NF/Faturamento
UPDATE nota_fiscal SET tipo_frete = 0 WHERE tipo_frete = 1 AND tipo_nota_fiscal IN (0, 1, 2, 3, 4, 5, 8, 9, 10);
UPDATE nota_fiscal SET tipo_frete = 1 WHERE tipo_frete = 2 AND tipo_nota_fiscal IN (0, 1, 2, 3, 4, 5, 8, 9, 10);

/* ---------------------------------------------------------------------- */
/* Modify table "contabilista"                                            */
/* ---------------------------------------------------------------------- */
/* Comments */
COMMENT ON TABLE public.contabilista			IS 'Tabela de contabilistas (contadores) da aplicação.';
COMMENT ON COLUMN public.contabilista.codigo	IS 'Código interno (código interno da pessoa física, código interno da pessoa) do contabilista (contador).';
COMMENT ON COLUMN public.contabilista.crc		IS 'Número da inscrição do contabilista (contador) no Conselho Regional de Contabilidade (CRC).';

/* ---------------------------------------------------------------------- */
/* Modify table "documento_fiscal"                                        */
/* ---------------------------------------------------------------------- */
/* Drop index */
DROP INDEX public.ix_documento_fiscal_codigo_receita;

/* Create primary key */
ALTER TABLE public.documento_fiscal
	DROP CONSTRAINT documento_fiscal_pkey;

/* Create primary key */
ALTER TABLE public.documento_fiscal
	ADD CONSTRAINT pk_documento_fiscal 
    PRIMARY KEY (codigo);

/* Create index */
CREATE UNIQUE INDEX uq_documento_fiscal_codigo_receita ON public.documento_fiscal (codigo_receita);

/* Comments */
COMMENT ON TABLE public.documento_fiscal						IS 'Tabela de tipos de documentos fiscais.';
COMMENT ON COLUMN public.documento_fiscal.codigo				IS 'Código interno do tipo de documento fiscal. Sequência: seq_documento_fiscal.';
COMMENT ON COLUMN public.documento_fiscal.codigo_receita		IS 'Código da receita do tipo de documento fiscal. Chave única.';
COMMENT ON COLUMN public.documento_fiscal.nome					IS 'Nome do tipo de documento fiscal.';
COMMENT ON COLUMN public.documento_fiscal.indicador_consumo		IS 'Indica se o tipo de documento fiscal é de contas de consumo (energia elétrica, água, gás, etc). Conforme este tipo, haverá influência no cadastramento da nota fiscal.';
COMMENT ON COLUMN public.documento_fiscal.indicador_transporte	IS 'Indica se o tipo de documento fiscal é de transporte. Conforme este tipo, haverá influência no cadastramento da nota fiscal.';
COMMENT ON COLUMN public.documento_fiscal.situacao				IS 'Situação do tipo de documento fiscal: ativo (true) ou inativo (false).';

/* ---------------------------------------------------------------------- */
/* Modify table "parcela_nota"                                            */
/* ---------------------------------------------------------------------- */
/* Drop primary key */
ALTER TABLE public.parcela_nota
	DROP CONSTRAINT parcela_nota_pkey;

/* Create primary key */
ALTER TABLE public.parcela_nota
	ADD CONSTRAINT pk_parcela_nota 
    PRIMARY KEY (codigo);

/* Create index */
CREATE UNIQUE INDEX uq_parcela_nota_nota_orderm ON public.parcela_nota (codigo_nota,ordem);

/* Comments */
COMMENT ON TABLE public.parcela_nota				IS 'Relacionamento entre nota e seu(s) pagamento(s) (ou parcelas).';
COMMENT ON COLUMN public.parcela_nota.codigo		IS 'Código interno do pagamento.';
COMMENT ON COLUMN public.parcela_nota.codigo_nota	IS 'Código interno da nota.';
COMMENT ON COLUMN public.parcela_nota.ordem			IS 'Ordem do pagamento (parcela), iniciando em zero, ou seja, o 1º pagamento é 0.';

/* ---------------------------------------------------------------------- */
/* Modify table "natureza_pagamento"                                      */
/* ---------------------------------------------------------------------- */
/* Drop primary key */
ALTER TABLE public.natureza_pagamento
	DROP CONSTRAINT natureza_pagamento_pkey;

/* Create constraint */
ALTER TABLE public.natureza_pagamento
	ADD CONSTRAINT ck_natureza_pagamento_tipo_despesa 
    CHECK (tipo_despesa = ANY (ARRAY[0, 1]));

/* Create primary key */
ALTER TABLE public.natureza_pagamento
	ADD CONSTRAINT pk_natureza_pagamento 
    PRIMARY KEY (codigo);

/* Comments */
COMMENT ON TABLE public.natureza_pagamento					IS 'Natureza dos pagamentos (fornecedor, prestação de contas, etc).';
COMMENT ON COLUMN public.natureza_pagamento.codigo			IS 'Código interno da natureza de pagamento. Sequência: seq_natureza_pagamento.';
COMMENT ON COLUMN public.natureza_pagamento.nome			IS 'Nome/descrição da natureza de pagamento.';
COMMENT ON COLUMN public.natureza_pagamento.conta_contabil	IS 'Conta contábil associada à natureza de pagamento. Fora de uso.';
COMMENT ON COLUMN public.natureza_pagamento.tipo_despesa	IS 'Tipo de despesa associada à natureza de pagamento: 0 = Interna / 1 = Externa.';

/* ---------------------------------------------------------------------- */
/* Modify table "historico_aplicacao"                                     */
/* ---------------------------------------------------------------------- */
/* Drop indexes */
DROP INDEX public.idx_historico_aplicacao_1;
DROP INDEX public.idx_historico_aplicacao_2;
DROP INDEX public.idx_historico_aplicacao_3;
DROP INDEX public.idx_historico_aplicacao_4;

/* Create indexes */
CREATE INDEX ix_historico_aplicacao_entidade					ON public.historico_aplicacao (entidade);
CREATE INDEX ix_historico_aplicacao_entidade_codigo_entidade	ON public.historico_aplicacao (entidade,codigo_entidade);
CREATE INDEX ix_historico_aplicacao_momento_metodo				ON public.historico_aplicacao (momento,metodo);
CREATE INDEX ix_historico_aplicacao_entidade_momento_metodo		ON public.historico_aplicacao (entidade,momento,metodo);

/* Comments */
COMMENT ON TABLE public.historico_aplicacao						IS 'Histórico das operação realizadas no banco de dados das principais entidades da aplicação.';
COMMENT ON COLUMN public.historico_aplicacao.codigo				IS 'Código interno do histórico. Sequência: seq_historico_aplicacao.';
COMMENT ON COLUMN public.historico_aplicacao.usuario			IS 'Login do usuário que realizou a mudança de situação da entidade.';
COMMENT ON COLUMN public.historico_aplicacao.data				IS 'Data/hora da ação.';
COMMENT ON COLUMN public.historico_aplicacao.entidade			IS 'Nome da entidade ("ParcelaNota - NF", "NotaFiscal", "OrdemCompra", etc.)';
COMMENT ON COLUMN public.historico_aplicacao.codigo_entidade	IS 'Código interno do registro modificado na entidade.';
COMMENT ON COLUMN public.historico_aplicacao.momento			IS 'Momento da realização da modificação ("PRE" ou "POST")';
COMMENT ON COLUMN public.historico_aplicacao.metodo				IS 'Método realizado: "CREATE" / "UPDATE" / "DELETE".';
COMMENT ON COLUMN public.historico_aplicacao.mensagem			IS 'Conteúdo antes da realização da operação.';
COMMENT ON COLUMN public.historico_aplicacao.serializacao		IS 'Informação associada a etapa da programação.';

/* ---------------------------------------------------------------------- */
/* Modify table "parametro"                                               */
/* ---------------------------------------------------------------------- */
/* Drop primary key */
ALTER TABLE public.parametro
	DROP CONSTRAINT parametro_pkey;

/* Create primary key */
ALTER TABLE public.parametro
	ADD CONSTRAINT pk_parametro 
    PRIMARY KEY (chave);

/* Comments */
COMMENT ON TABLE public.parametro			IS 'Parâmetros da aplicação.';
COMMENT ON COLUMN public.parametro.chave	IS 'Chave para acesso ao parâmetro.';
COMMENT ON COLUMN public.parametro.valor	IS 'Valor do parâmetro.';

/* ---------------------------------------------------------------------- */
/* Modify table "fornecedor_nat_fornecimento"                             */
/* ---------------------------------------------------------------------- */
/* Create indexes */
CREATE INDEX ix_fornecedor_nat_fornecimento_fornecedor				ON public.fornecedor_nat_fornecimento (codigo_fornecedor);
CREATE INDEX ix_fornecedor_nat_fornecimento_natureza_fornecimento	ON public.fornecedor_nat_fornecimento (codigo_natureza_fornecimento);

/* Comments */
COMMENT ON TABLE public.fornecedor_nat_fornecimento									IS 'Relacionamento entre os fornecedores e as naturezas de fornecimento deles.';
COMMENT ON COLUMN public.fornecedor_nat_fornecimento.codigo_fornecedor				IS 'Código interno do fornecedor (código interno da pessoa).';
COMMENT ON COLUMN public.fornecedor_nat_fornecimento.codigo_natureza_fornecimento	IS 'Código interno da natureza de fornecimento.';

/* ---------------------------------------------------------------------- */
/* Modify table "pontuacao"                                               */
/* ---------------------------------------------------------------------- */
/* Comments */
COMMENT ON TABLE public.pontuacao					IS 'Pontuação para as avaliações de fornecedores.';
COMMENT ON COLUMN public.pontuacao.codigo			IS 'Código interno da pontução. Sequência: seq_pontuacao.';
COMMENT ON COLUMN public.pontuacao.nome				IS 'Nome da pontução.';
COMMENT ON COLUMN public.pontuacao.criador			IS 'Responsável (login) pela criação da pontução.';
COMMENT ON COLUMN public.pontuacao.data_criacao		IS 'Data/hora de criação da pontução.';
COMMENT ON COLUMN public.pontuacao.atualizador		IS 'Responsável (login) pela última atualização da pontução.';
COMMENT ON COLUMN public.pontuacao.data_atualizacao	IS 'Data/hora da última atualização da pontução.';

/* ---------------------------------------------------------------------- */
/* Modify table "item_avaliacao"                                          */
/* ---------------------------------------------------------------------- */
/* Create indexes */
CREATE INDEX ix_item_avaliacao_avaliacao	ON public.item_avaliacao (codigo_avaliacao);
CREATE INDEX ix_item_avaliacao_criterio		ON public.item_avaliacao (codigo_criterio);
CREATE INDEX ix_item_avaliacao_pontuacao	ON public.item_avaliacao (codigo_pontuacao);

/* Comments */
COMMENT ON TABLE public.item_avaliacao						IS 'Associação entre uma avaliação de uma nota fiscal, um critério a ser avaliado e a pontuação aplicada.';
COMMENT ON COLUMN public.item_avaliacao.codigo				IS 'Código interno da associação entre avaliação de NF e critério.';
COMMENT ON COLUMN public.item_avaliacao.codigo_avaliacao	IS 'Código interno da avaliação de NF.';
COMMENT ON COLUMN public.item_avaliacao.codigo_criterio		IS 'Código interno do critério a ser avaliado.';
COMMENT ON COLUMN public.item_avaliacao.codigo_pontuacao	IS 'Código interno da pontuação da avaliação x critério.';
COMMENT ON COLUMN public.item_avaliacao.justificativa		IS 'Justificativa da avaliação.';

/* ---------------------------------------------------------------------- */
/* Modify table "criterio"                                                */
/* ---------------------------------------------------------------------- */
/* Create indexes */
CREATE INDEX ix_criterio_nome ON public.criterio (nome);

/* Comments */
COMMENT ON TABLE public.criterio					IS 'Critérios para avaliação dos fornecedores.';
COMMENT ON COLUMN public.criterio.codigo			IS 'Código interno do critério para avaliação dos fornecedores. Sequência: seq_criterio.';
COMMENT ON COLUMN public.criterio.tipo				IS 'Tipo do critério para avaliação dos fornecedores.';
COMMENT ON COLUMN public.criterio.nome				IS 'Nome do critério para avaliação dos fornecedores.';
COMMENT ON COLUMN public.criterio.descricao			IS 'Descrição do critério para avaliação dos fornecedores.';
COMMENT ON COLUMN public.criterio.ordem				IS 'Ordem de exibição do critério para avaliação dos fornecedores.';
COMMENT ON COLUMN public.criterio.peso				IS 'Peso do critério para avaliação dos fornecedores.';
COMMENT ON COLUMN public.criterio.criador			IS 'Responsável (login) pela criação do critério para avaliação dos fornecedores.';
COMMENT ON COLUMN public.criterio.data_criacao		IS 'Data/hora de criação do critério para avaliação dos fornecedores.';
COMMENT ON COLUMN public.criterio.data_atualizacao	IS 'Responsável (login) pela última atualização do critério para avaliação dos fornecedores.';
COMMENT ON COLUMN public.criterio.atualizador		IS 'Data/hora da última atualização do critério para avaliação dos fornecedores.';

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "colaborador_centro_financeiro"                */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.colaborador_centro_financeiro
	DROP CONSTRAINT pk_colaborador_centro_financeiro;

/* Create temporary table */
CREATE TABLE public.colaborador_centro_financeiro_TMP (
    codigo						BIGINT	NOT NULL,
    codigo_colaborador			BIGINT	NOT NULL,
    codigo_centro_financeiro	BIGINT	NOT NULL);

/* Copy data do temporary table */
INSERT INTO public.colaborador_centro_financeiro_TMP
    (codigo,codigo_colaborador,codigo_centro_financeiro)
SELECT
    codigo,codigo_colaborador,codigo_centro_financeiro
FROM public.colaborador_centro_financeiro;

/* Drop indexes */
DROP INDEX public.ix_colaborador_centro_financeiro_centro_financeiro;
DROP INDEX public.ix_colaborador_centro_financeiro_colaborador;
DROP INDEX public.uq_colaborador_centro_financeiro_colaborador_centro_financeiro;

/* Drop old table */
DROP TABLE public.colaborador_centro_financeiro;

/* Rename temporary table to the table */
ALTER TABLE public.colaborador_centro_financeiro_TMP
	RENAME TO colaborador_centro_financeiro;

/* Add primary key */
ALTER TABLE public.colaborador_centro_financeiro
	ADD CONSTRAINT pk_colaborador_centro_financeiro 
    PRIMARY KEY (codigo);

/* Create indexes */
CREATE INDEX ix_colaborador_centro_financeiro_colaborador							ON public.colaborador_centro_financeiro (codigo_colaborador);
CREATE INDEX ix_colaborador_centro_financeiro_centro_financeiro						ON public.colaborador_centro_financeiro (codigo_centro_financeiro);
CREATE UNIQUE INDEX uq_colaborador_centro_financeiro_colaborador_centro_financeiro	ON public.colaborador_centro_financeiro (codigo_colaborador,codigo_centro_financeiro);

/* Comments */
COMMENT ON TABLE public.colaborador_centro_financeiro							IS 'Centro financeiro do colaborador atualmente selecionado.';
COMMENT ON COLUMN public.colaborador_centro_financeiro.codigo					IS 'Código interno do centro financeiro do colaborador atualmente selecionado. Sequência: seq_colaborador_centro_financeiro.';
COMMENT ON COLUMN public.colaborador_centro_financeiro.codigo_colaborador		IS 'Código interno da pessoa (colaborador).';
COMMENT ON COLUMN public.colaborador_centro_financeiro.codigo_centro_financeiro	IS 'Código interno do centro financeiro.';

/* Set owner */
ALTER TABLE public.colaborador_centro_financeiro
	OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Modify table "conta_contabil_gerencia"                                 */
/* ---------------------------------------------------------------------- */
/* Comments */
COMMENT ON TABLE public.conta_contabil_gerencia							IS 'Relacionamento entre a gerência e a(s) conta(s) contábil(eis).';
COMMENT ON COLUMN public.conta_contabil_gerencia.codigo_conta_contabil	IS 'Código interno da conta contábil.';
COMMENT ON COLUMN public.conta_contabil_gerencia.codigo_gerencia		IS 'Código interno da gerência (código interno do centro financeiro).';

/* ---------------------------------------------------------------------- */
/* Modify table "criterio_pontuacao"                                      */
/* ---------------------------------------------------------------------- */
/* Comments */
COMMENT ON TABLE public.criterio_pontuacao						IS 'Relacionamento entre os critérios e sua pontuação.';
COMMENT ON COLUMN public.criterio_pontuacao.codigo				IS 'Código interno da associação entre critérios e pontuações. Sequência: seq_criterio_pontuacao.';
COMMENT ON COLUMN public.criterio_pontuacao.codigo_criterio		IS 'Código interno do critério.';
COMMENT ON COLUMN public.criterio_pontuacao.codigo_pontuacao	IS 'Código interno da pontuação.';
COMMENT ON COLUMN public.criterio_pontuacao.conformidade		IS 'Indica se a pontuação está em conformidade e não precisa de justificativa (true) ou não e precisa (false).';
COMMENT ON COLUMN public.criterio_pontuacao.peso				IS 'Peso da pontução para o critério.';
COMMENT ON COLUMN public.criterio_pontuacao.ordem				IS 'Ordem de exibição da pontuação para o critério.';
COMMENT ON COLUMN public.criterio_pontuacao.descricao			IS 'Descrição da pontuação para o critério.';

/* ---------------------------------------------------------------------- */
/* Modify table "faixa_avaliacao"                                         */
/* ---------------------------------------------------------------------- */
/* Comments */
COMMENT ON TABLE public.faixa_avaliacao						IS 'Faixa de avaliação utilizadas no relatório "Comparação Semestral" da avaliação de fornecedores.';
COMMENT ON COLUMN public.faixa_avaliacao.codigo				IS 'Código interno da faixa de avaliação. Sequência: seq_faixa_avaliacao.';
COMMENT ON COLUMN public.faixa_avaliacao.faixa_inicial		IS 'Valor inicial para a faixa de avaliação.';
COMMENT ON COLUMN public.faixa_avaliacao.faixa_final		IS 'Valor final para a faixa de avaliação.';
COMMENT ON COLUMN public.faixa_avaliacao.nome				IS 'Nome da faixa de avaliação.';
COMMENT ON COLUMN public.faixa_avaliacao.descricao			IS 'Descrição da faixa de avaliação.';
COMMENT ON COLUMN public.faixa_avaliacao.rgb				IS 'Cor da legenda a ser aplicada para a faixa de avaliação.';
COMMENT ON COLUMN public.faixa_avaliacao.legenda			IS 'Descrição da legenda para a faixa de avaliação.';
COMMENT ON COLUMN public.faixa_avaliacao.criador			IS 'Responsável (login) pela criação da faixa de avaliação.';
COMMENT ON COLUMN public.faixa_avaliacao.data_criacao		IS 'Data/hora de criação da faixa de avaliação.';
COMMENT ON COLUMN public.faixa_avaliacao.atualizador		IS 'Responsável (login) pela última atualização da faixa de avaliação.';
COMMENT ON COLUMN public.faixa_avaliacao.data_atualizacao	IS 'Data/hora da última atualização da faixa de avaliação.';

/* ---------------------------------------------------------------------- */
/* Modify table "avaliacao"                                               */
/* ---------------------------------------------------------------------- */
/* Comments */
COMMENT ON TABLE public.avaliacao					IS 'Avaliação dos fornecedores das notas fiscais.';
COMMENT ON COLUMN public.avaliacao.codigo			IS 'Código interno da avaliação da nota fiscal. Sequência: seq_avaliacao.';
COMMENT ON COLUMN public.avaliacao.codigo_nota		IS 'Código interno da nota.';
COMMENT ON COLUMN public.avaliacao.tipo				IS 'Tipo da avaliação:';
COMMENT ON COLUMN public.avaliacao.status			IS 'Situação da avaliação:';
COMMENT ON COLUMN public.avaliacao.data				IS 'Data da avaliação.';
COMMENT ON COLUMN public.avaliacao.avaliador		IS 'Nome informado do avaliador.';
COMMENT ON COLUMN public.avaliacao.criador			IS 'Responsável (login) pela criação da avaliação.';
COMMENT ON COLUMN public.avaliacao.data_criacao		IS 'Data/hora de criação da avaliação.';
COMMENT ON COLUMN public.avaliacao.atualizador		IS 'Responsável (login) pela última atualização da avaliação.';
COMMENT ON COLUMN public.avaliacao.data_atualizacao	IS 'Data/hora da última atualização da avaliação.';

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "modulo"                                       */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.modulo DROP CONSTRAINT modulo_pkey;

/* Create temporary table */
CREATE TABLE public.modulo_TMP (
    codigo				BIGINT								NOT NULL,
    codigo_sistema		BIGINT,
    codigo_modulo_pai	BIGINT,
    nome				CHARACTER VARYING(100)				NOT NULL,
    ordem				INTEGER								NOT NULL,
    status				INTEGER					DEFAULT 0	NOT NULL,
    contexto			CHARACTER VARYING(100)	DEFAULT '/sgi'::character varying);

/* Copy data do temporary table */
INSERT INTO public.modulo_TMP
    (codigo,codigo_sistema,codigo_modulo_pai,nome,ordem,status,contexto)
SELECT
    codigo,codigo_sistema,codigo_modulo_pai,nome,ordem,status,contexto
FROM public.modulo;

/* Drop indexes */
DROP TABLE public.modulo;

/* Drop old table */
ALTER TABLE public.modulo_TMP
	RENAME TO modulo;

/* Add primary key */
ALTER TABLE public.modulo
	ADD CONSTRAINT pk_modulo 
    PRIMARY KEY (codigo);

/* Create indexes */
CREATE INDEX ix_modulo_sistema		ON public.modulo (codigo_sistema);
CREATE INDEX ix_modulo_modulo_pai	ON public.modulo (codigo_modulo_pai);
CREATE INDEX ix_modulo_ordem		ON public.modulo (ordem);

/* Comments */
COMMENT ON TABLE public.modulo						IS 'Tabela de módulos da aplicação.';
COMMENT ON COLUMN public.modulo.codigo				IS 'Código interno do módulo da aplicação. Sequência: seq_modulo.';
COMMENT ON COLUMN public.modulo.codigo_sistema		IS 'Código interno do sistema ao qual o módulo pertence.';
COMMENT ON COLUMN public.modulo.codigo_modulo_pai	IS 'Código interno do módulo da aplicação "pai" (nível superior ao módulo).';
COMMENT ON COLUMN public.modulo.nome				IS 'Nome do módulo da aplicação.';
COMMENT ON COLUMN public.modulo.ordem				IS 'Ordem de exibição do módulo. Se for um módulo principal (sem modulo "pai)), refletirá a ordem de exibição do módulo. Senão, será a ordem de exibição no menu/submenu do módulo "pai".';
COMMENT ON COLUMN public.modulo.contexto			IS 'Contexto (diretório) do módulo da aplicação.';
COMMENT ON COLUMN public.modulo.status				IS 'Situação do módulo da aplicação: DEPRECIADO (sem uso).';

/* Set owner */
ALTER TABLE public.modulo
	OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Modify table "sistema"                                                 */
/* ---------------------------------------------------------------------- */
/* Drop primary key */
ALTER TABLE public.sistema
	DROP CONSTRAINT sistema_pkey;

/* Create primary key */
ALTER TABLE public.sistema
	ADD CONSTRAINT pk_sistema 
    PRIMARY KEY (codigo);

/* Create index */
CREATE INDEX ix_sistema_nome ON public.sistema (nome);

/* Comments */
COMMENT ON TABLE public.sistema			IS 'Tabela de sistemas da aplicação.';
COMMENT ON COLUMN public.sistema.codigo	IS 'Código interno do sistema da aplicação. Sequência: seq_sistema.';
COMMENT ON COLUMN public.sistema.nome	IS 'Nome do sistema da aplicação.';

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "permissao"                                    */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.permissao DROP CONSTRAINT permissao_pkey;

/* Create temporary table */
CREATE TABLE public.permissao_TMP (
    codigo			INTEGER					NOT NULL,
    codigo_receptor	BIGINT					NOT NULL,
    acao			CHARACTER VARYING(10)	NOT NULL,
    alvo			CHARACTER VARYING(255)	NOT NULL);

/* Copy data do temporary table */
INSERT INTO public.permissao_TMP
    (codigo,codigo_receptor,acao,alvo)
SELECT
    codigo,codigo_receptor,acao,alvo
FROM public.permissao;

/* Drop old table */
DROP TABLE public.permissao;

/* Rename temporary table to the table */
ALTER TABLE public.permissao_TMP
	RENAME TO permissao;

/* Add primary key */
ALTER TABLE public.permissao
	ADD CONSTRAINT pk_permissao 
    PRIMARY KEY (codigo);

/* Create index */
CREATE INDEX ix_permissao_receptor ON public.permissao (codigo_receptor);

/* Comments */
COMMENT ON TABLE public.permissao					IS 'Permissão de acesso para cada alvo (funcionalidade).';
COMMENT ON COLUMN public.permissao.codigo			IS 'Código interno da permissão. Sequência: seq_permissao.';
COMMENT ON COLUMN public.permissao.codigo_receptor	IS 'Código interno do recpetor (perfil) associado à permissão.';
COMMENT ON COLUMN public.permissao.acao				IS 'Ação ("render" ou "restore") para a permissão.';
COMMENT ON COLUMN public.permissao.alvo				IS 'Alvo (URL) para a permissão.';

/* Set owner */
ALTER TABLE public.permissao
	OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "receptor"                                     */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.receptor DROP CONSTRAINT receptor_pkey;

/* Create temporary table */
CREATE TABLE public.receptor_TMP (
    codigo				BIGINT					DEFAULT nextval('seq_receptor')	NOT NULL,
    tipo				SMALLINT												NOT NULL,
    criador				CHARACTER VARYING(255),
    data_criacao		TIMESTAMP,
    atualizador			CHARACTER VARYING(255),
    data_atualizacao	TIMESTAMP);

/* Copy data do temporary table */
INSERT INTO public.receptor_TMP
    (codigo,tipo,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,tipo,criador,data_criacao,atualizador,data_atualizacao
FROM public.receptor;

/* Drop old table */
DROP TABLE public.receptor;

/* Rename temporary table to the table */
ALTER TABLE public.receptor_TMP
	RENAME TO receptor;

/* Add primary key */
ALTER TABLE public.receptor
	ADD CONSTRAINT pk_receptor 
    PRIMARY KEY (codigo);

/* Add constraint */
ALTER TABLE public.receptor
	ADD CONSTRAINT ck_receptor_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1]));

/* Comments */
COMMENT ON TABLE public.receptor					IS 'Tabela de receptores (perfis e usuários).';
COMMENT ON COLUMN public.receptor.codigo			IS 'Código interno do receptor (perfil ou usuário). Sequência: seq_receptor.';
COMMENT ON COLUMN public.receptor.tipo				IS 'Tipo do receptor: 0 = Perfil / 1 = Usuário.';
COMMENT ON COLUMN public.receptor.criador			IS 'Responsável (login) pela criação do receptor.';
COMMENT ON COLUMN public.receptor.data_criacao		IS 'Data/hora de criação do receptor.';
COMMENT ON COLUMN public.receptor.atualizador		IS 'Responsável (login) pela última atualização do receptor.';
COMMENT ON COLUMN public.receptor.data_atualizacao	IS 'Data/hora da última atualização do receptor.';

/* Set owner */
ALTER TABLE public.receptor
	OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Modify table "funcionalidade"                                          */
/* ---------------------------------------------------------------------- */
/* Comments */
COMMENT ON TABLE public.funcionalidade					IS 'Tabela de funcionalidades da aplicação.';
COMMENT ON COLUMN public.funcionalidade.codigo			IS 'Código interno da funcionalidade. Sequência: seq_funcionalidade.';
COMMENT ON COLUMN public.funcionalidade.codigo_modulo	IS 'Código interno do módulo ao qual a funcionalidade pertence.';
COMMENT ON COLUMN public.funcionalidade.ordem			IS 'Ordem da funcionalidade na exibição do menu da aplicação.';
COMMENT ON COLUMN public.funcionalidade.url				IS 'URL da funcionalidade na exibição do menu da aplicação.';
COMMENT ON COLUMN public.funcionalidade.nome			IS 'Nome da funcionalidade na exibição do menu da aplicação.';

/* ---------------------------------------------------------------------- */
/* Modify table "lote_inativacao"                                         */
/* ---------------------------------------------------------------------- */
/* Comments */
COMMENT ON TABLE public.lote_inativacao							IS 'Lotes de inativação de usuários demitidos.';
COMMENT ON COLUMN public.lote_inativacao.codigo					IS 'Código interno do lote. Sequência: seq_lote_inativacao.';
COMMENT ON COLUMN public.lote_inativacao.codigo_anexo			IS 'Código interno do anexo utilizado para inativação.';
COMMENT ON COLUMN public.lote_inativacao.codigo_importacao		IS 'Código interno do responsável pela importação (código interno da pessoa) do lote (apenas importação manual).';
COMMENT ON COLUMN public.lote_inativacao.tipo					IS 'Tipo do lote: 0 - Manual / 1- Automático.';
COMMENT ON COLUMN public.lote_inativacao.situacao				IS 'Situação do lote: 0 - Não importado / 1 - Importado.';
COMMENT ON COLUMN public.lote_inativacao.data_importacao		IS 'Data/hora da importação do lote.';
COMMENT ON COLUMN public.lote_inativacao.total_nao_localizado	IS 'Total de registros do arquivo não localizados.';
COMMENT ON COLUMN public.lote_inativacao.total_importado_com_pc	IS 'Total de registros do arquivo importados COM valores em prestação de contas.';
COMMENT ON COLUMN public.lote_inativacao.total_importado_sem_pc	IS 'Total de registros do arquivo importados SEM valores em prestação de contas.';
COMMENT ON COLUMN public.lote_inativacao.total_ja_inativo		IS 'Total de registros do arquivo não importados, pois já estavam inativos.';
COMMENT ON COLUMN public.lote_inativacao.total_nao_inativado	IS 'Total de registros do arquivo importados não inativados, pois são cadastros especiais e não podem ser inativados através da inativação em lote.';
COMMENT ON COLUMN public.lote_inativacao.criador				IS 'Responsável (login) pela criação do lote.';
COMMENT ON COLUMN public.lote_inativacao.data_criacao			IS 'Data/hora de criação do lote.';

/* ---------------------------------------------------------------------- */
/* Modify table "aliquota_agregada"                                       */
/* ---------------------------------------------------------------------- */
/* Alter table */
ALTER TABLE public.aliquota_agregada
	ALTER COLUMN nome TYPE CHARACTER VARYING(50);

/* Update data */
UPDATE aliquota_agregada SET nome = TRIM(nome);

/* ---------------------------------------------------------------------- */
/* Modify table "aplicacao_motivo_retorno"                                */
/* ---------------------------------------------------------------------- */
/* Drop index */
DROP INDEX public.ix_aplicacao_motivo_retorno_chave;

/* Create index */
CREATE UNIQUE INDEX uq_aplicacao_motivo_retorno_chave ON public.aplicacao_motivo_retorno (chave);

/* ---------------------------------------------------------------------- */
/* Modify table "cfop"                                                    */
/* ---------------------------------------------------------------------- */
/* Alter table */
ALTER TABLE public.cfop
	ALTER COLUMN descricao TYPE CHARACTER VARYING(2000);

/* Update data */
UPDATE cfop SET descricao = TRIM(descricao);

/* ---------------------------------------------------------------------- */
/* Modify table "demonstrativo"                                           */
/* ---------------------------------------------------------------------- */
/* Alter table */
ALTER TABLE public.demonstrativo
	ALTER COLUMN status SET DEFAULT 0;

/* Add constraints */
ALTER TABLE public.demonstrativo
	ADD CONSTRAINT ck_demonstrativo_status 
    CHECK (status <= 8);

ALTER TABLE public.demonstrativo
	ADD CONSTRAINT ck_demonstrativo_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1, 2]));

/* ---------------------------------------------------------------------- */
/* Modify table "fluxo"                                                   */
/* ---------------------------------------------------------------------- */
/* Add constraint */
ALTER TABLE public.fluxo
	ADD CONSTRAINT ck_fluxo_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1]));

/* ---------------------------------------------------------------------- */
/* Modify table "fundo_fixo"                                              */
/* ---------------------------------------------------------------------- */
/* Add constraints */
ALTER TABLE public.fundo_fixo
	ADD CONSTRAINT ck_fundo_fixo_status 
    CHECK (status = ANY (ARRAY[0, 1]));

ALTER TABLE public.fundo_fixo
	ADD CONSTRAINT ck_fundo_fixo_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1, 2]));

/* Create indexes */
CREATE INDEX ix_fundo_fixo_colaborador			ON public.fundo_fixo (codigo_colaborador);
CREATE INDEX ix_fundo_fixo_centro_financeiro	ON public.fundo_fixo (codigo_centro_financeiro);
CREATE INDEX ix_fundo_fixo_limite_anterior		ON public.fundo_fixo (codigo_limite_anterior);
CREATE INDEX ix_fundo_fixo_inativador			ON public.fundo_fixo (codigo_inativador);

/* ---------------------------------------------------------------------- */
/* Modify table "lancamento"                                              */
/* ---------------------------------------------------------------------- */
/* Drop index */
DROP INDEX public.ix_lancamento_numero;

/* Add constraint */
ALTER TABLE public.lancamento
	ADD CONSTRAINT ck_lancamento_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1, 2]));

/* Create index */
CREATE UNIQUE INDEX uq_lancamento_numero ON public.lancamento (numero);

/* ---------------------------------------------------------------------- */
/* Modify table "lote_interface_anexo"                                    */
/* ---------------------------------------------------------------------- */
/* Create indexes */
CREATE INDEX ix_lote_interface_anexo_lote_interface	ON public.lote_interface_anexo (codigo_lote_interface);
CREATE INDEX ix_lote_interface_anexo_anexo			ON public.lote_interface_anexo (codigo_anexo);

/* ---------------------------------------------------------------------- */
/* Modify table "mensagem_centro_financeiro"                              */
/* ---------------------------------------------------------------------- */
/* Drop index */
DROP INDEX public.ix_mensagem_centro_financeiro_mensagem_centro_financeiro;

/* Create index */
CREATE UNIQUE INDEX uq_mensagem_centro_financeiro_mensagem_centro_financeiro ON public.mensagem_centro_financeiro (codigo_mensagem,codigo_centro_financeiro);

/* ---------------------------------------------------------------------- */
/* Modify table "mensagem_diretoria"                                      */
/* ---------------------------------------------------------------------- */
/* Drop index */
DROP INDEX public.ix_mensagem_diretoria_mensagem_diretoria;

/* Create index */
CREATE UNIQUE INDEX uq_mensagem_diretoria_mensagem_diretoria ON public.mensagem_diretoria (codigo_mensagem,codigo_diretoria);

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "natureza_despesa"                             */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.natureza_despesa DROP CONSTRAINT pk_natureza_despesa;

/* Create temporary table */
CREATE TABLE public.natureza_despesa_TMP (
    codigo							BIGINT								NOT NULL,
    codigo_tipo_natureza_despesa	BIGINT								NOT NULL,
    codigo_inativacao				BIGINT,
    nome							CHARACTER VARYING(50)				NOT NULL,
    chave							CHARACTER VARYING(25),
    situacao						SMALLINT				DEFAULT 1	NOT NULL,
    ordem							INTEGER								NOT NULL,
    data_inativacao					TIMESTAMP,
    criador							CHARACTER VARYING(255),
    data_criacao					TIMESTAMP,
    atualizador						CHARACTER VARYING(255),
    data_atualizacao				TIMESTAMP);

/* Copy data do temporary table */
INSERT INTO public.natureza_despesa_TMP
    (codigo,codigo_tipo_natureza_despesa,codigo_inativacao,nome,chave,situacao,ordem,data_inativacao,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_tipo_natureza_despesa,codigo_inativacao,nome,chave,situacao,10,data_inativacao,criador,data_criacao,atualizador,data_atualizacao
FROM public.natureza_despesa;

/* Drop indexes */
DROP INDEX public.ix_natureza_despesa_inativacao;
DROP INDEX public.ix_natureza_despesa_nome;
DROP INDEX public.ix_natureza_despesa_tipo_natureza_despesa;
DROP INDEX public.uq_natureza_despesa_chave;

/* Drop old table */
DROP TABLE public.natureza_despesa;

/* Rename temporary table to the table */
ALTER TABLE public.natureza_despesa_TMP
	RENAME TO natureza_despesa;

/* Add primary key */
ALTER TABLE public.natureza_despesa
	ADD CONSTRAINT pk_natureza_despesa 
    PRIMARY KEY (codigo);

/* Add constraint */
ALTER TABLE public.natureza_despesa
	ADD CONSTRAINT ck_natureza_despesa_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1]));

/* Create indexes */
CREATE INDEX ix_natureza_despesa_inativacao				ON public.natureza_despesa (codigo_inativacao);
CREATE INDEX ix_natureza_despesa_nome					ON public.natureza_despesa (nome);
CREATE INDEX ix_natureza_despesa_tipo_natureza_despesa	ON public.natureza_despesa (codigo_tipo_natureza_despesa);
CREATE UNIQUE INDEX uq_natureza_despesa_chave			ON public.natureza_despesa (chave);

/* Comments */
COMMENT ON TABLE public.natureza_despesa								IS 'Natureza de despesas para prestação de contas (fundo fixo, adiantamento e reembolso).';
COMMENT ON COLUMN public.natureza_despesa.codigo						IS 'Código interno da natureza de despesa. Sequência: seq_natureza_despesa.';
COMMENT ON COLUMN public.natureza_despesa.codigo_tipo_natureza_despesa	IS 'Código interno do tipo de natureza de despesa.';
COMMENT ON COLUMN public.natureza_despesa.codigo_inativacao				IS 'Código interno (código interno do colaborador) do responsável pela inativação da natureza de despesa.';
COMMENT ON COLUMN public.natureza_despesa.nome							IS 'Nome da natureza de despesa.';
COMMENT ON COLUMN public.natureza_despesa.chave							IS 'Chave interna (utilizada no código java do sistema) da natureza de despesa. Chave única.';
COMMENT ON COLUMN public.natureza_despesa.situacao						IS 'Situação: 0 = Inativo / 1 = Ativo.';
COMMENT ON COLUMN public.natureza_despesa.ordem							IS 'Ordem da natureza de despesa no Relatório de Despesas (usar, se possível, espaçamento de 10 unidades entre cada natureza para cada tipo).';
COMMENT ON COLUMN public.natureza_despesa.data_inativacao				IS 'Data/hora da inativação da natureza de despesa.';
COMMENT ON COLUMN public.natureza_despesa.criador						IS 'Responsável (login) pela criação da natureza de despesas.';
COMMENT ON COLUMN public.natureza_despesa.data_criacao					IS 'Data/hora de criação da natureza de despesas.';
COMMENT ON COLUMN public.natureza_despesa.atualizador					IS 'Responsável (login) pela última atualização da natureza de despesas.';
COMMENT ON COLUMN public.natureza_despesa.data_atualizacao				IS 'Data/hora da última atualização da natureza de despesas.';

/* Set owner */
ALTER TABLE public.natureza_despesa
	OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Modify table "natureza_juridica"                                       */
/* ---------------------------------------------------------------------- */
/* Drop index */
DROP INDEX public.ix_natureza_juridica_nome;

/* Create index */
CREATE UNIQUE INDEX uq_natureza_juridica_nome ON public.natureza_juridica (nome);

/* ---------------------------------------------------------------------- */
/* Modify table "outro_desconto"                                          */
/* ---------------------------------------------------------------------- */
/* Alter table */
ALTER TABLE public.outro_desconto
	ALTER COLUMN criador		DROP NOT NULL,
	ALTER COLUMN data_criacao	DROP NOT NULL;

/* ---------------------------------------------------------------------- */
/* Modify table "outro_desconto_conta_contabil"                           */
/* ---------------------------------------------------------------------- */
/* Create indexes */
CREATE INDEX ix_outro_desconto_conta_contabil_outro_desconto ON public.outro_desconto_conta_contabil (codigo_outro_desconto);
CREATE INDEX ix_outro_desconto_conta_contabil_conta_contabil ON public.outro_desconto_conta_contabil (codigo_conta_contabil);

/* ---------------------------------------------------------------------- */
/* Modify table "pessoa_juridica"                                         */
/* ---------------------------------------------------------------------- */
/* Add constraint */
ALTER TABLE public.pessoa_juridica
	ADD CONSTRAINT ck_pessoa_juridica_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1, 2, 3, 4]));

/* Comments */
COMMENT ON COLUMN public.pessoa_juridica.tipo IS 'Tipo da pessoa jurídica: 0 = Fornecedor / 1 = Cliente / 2 = Fornecedor/Cliente / 3 = Empresa (unidade) / 4 = Subcliente';

/* ---------------------------------------------------------------------- */
/* Modify table "proposta"                                                */
/* ---------------------------------------------------------------------- */
/* Add constraints */
ALTER TABLE public.proposta
	ADD CONSTRAINT ck_proposta_tipo_desconto_negociado 
    CHECK (tipo_desconto_negociado = ANY (ARRAY[0, 1]));

ALTER TABLE public.proposta
	ADD CONSTRAINT ck_proposta_tipo_prazo_negociado 
    CHECK (tipo_prazo_negociado = ANY (ARRAY[0, 1]));

/* ---------------------------------------------------------------------- */
/* Modify table "relacao_nf"                                              */
/* ---------------------------------------------------------------------- */
/* Alter table */
ALTER TABLE public.relacao_nf
	ALTER COLUMN criador DROP NOT NULL,
	ALTER COLUMN data_criacao DROP NOT NULL;

/* ---------------------------------------------------------------------- */
/* Modify table "requisicao_compra"                                       */
/* ---------------------------------------------------------------------- */
/* Alter table */
ALTER TABLE public.requisicao_compra
	ALTER COLUMN status SET DEFAULT 0;

/* Add constraint */
ALTER TABLE public.requisicao_compra
	ADD CONSTRAINT ck_requisicao_compra_status 
    CHECK (status = ANY (ARRAY[0, 1, 2, 3, 4]));

/* Drop index */
DROP INDEX public.ix_requisicao_compra_numero;

/* Create index */
CREATE UNIQUE INDEX uq_requisicao_compra_numero ON public.requisicao_compra (numero);

/* ---------------------------------------------------------------------- */
/* Modify table "sub_tipo_servico"                                        */
/* ---------------------------------------------------------------------- */
/* Drop index */
DROP INDEX public.ix_sub_tipo_servico_sigla;

/* Create index */
CREATE UNIQUE INDEX uq_sub_tipo_servico_sigla ON public.sub_tipo_servico (sigla);

/* ---------------------------------------------------------------------- */
/* Modify table "tipo_cobranca_vencimento"                                */
/* ---------------------------------------------------------------------- */
/* Add constraints */
ALTER TABLE public.tipo_cobranca_vencimento
	ADD CONSTRAINT ck_tipo_cobranca_vencimento_data_referencia 
    CHECK (data_referencia = ANY (ARRAY[0, 1]));

ALTER TABLE public.tipo_cobranca_vencimento
	ADD CONSTRAINT ck_tipo_cobranca_vencimento_dia_vencimento_tipo 
    CHECK (dia_vencimento_tipo = ANY (ARRAY[0, 1]));

ALTER TABLE public.tipo_cobranca_vencimento
	ADD CONSTRAINT ck_tipo_cobranca_vencimento_mes_referencia 
    CHECK (mes_referencia = ANY (ARRAY[0, 1]));

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "tipo_natureza_despesa"                        */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.tipo_natureza_despesa DROP CONSTRAINT pk_tipo_natureza_despesa;

/* Create temporary table */
CREATE TABLE public.tipo_natureza_despesa_TMP (
    codigo				BIGINT					NOT NULL,
    nome				CHARACTER VARYING(50)	NOT NULL,
    chave				CHARACTER VARYING(25),
    ordem				INTEGER					NOT NULL,
    criador				CHARACTER VARYING(255),
    data_criacao		TIMESTAMP,
    atualizador			CHARACTER VARYING(255),
    data_atualizacao	TIMESTAMP);

/* Copy data do temporary table */
INSERT INTO public.tipo_natureza_despesa_TMP
    (codigo,nome,chave,ordem,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,nome,chave,10,criador,data_criacao,atualizador,data_atualizacao
FROM public.tipo_natureza_despesa;

/* Drop indexes */
DROP INDEX public.uq_tipo_natureza_despesa_chave;
DROP INDEX public.uq_tipo_natureza_despesa_nome;

/* Drop old table */
DROP TABLE public.tipo_natureza_despesa;

/* Rename temporary table to the table */
ALTER TABLE public.tipo_natureza_despesa_TMP
	RENAME TO tipo_natureza_despesa;

/* Add primary key */
ALTER TABLE public.tipo_natureza_despesa
	ADD CONSTRAINT pk_tipo_natureza_despesa 
    PRIMARY KEY (codigo);

/* Create indexes */
CREATE UNIQUE INDEX uq_tipo_natureza_despesa_nome	ON public.tipo_natureza_despesa (nome);
CREATE UNIQUE INDEX uq_tipo_natureza_despesa_chave	ON public.tipo_natureza_despesa (chave);

/* Comments */
COMMENT ON TABLE public.tipo_natureza_despesa					IS 'Tipo (agrupamento) de natureza de despesas para prestação de contas (fundo fixo, adiantamento e reembolso).';
COMMENT ON COLUMN public.tipo_natureza_despesa.codigo			IS 'Código interno do tipo de natureza de despesas. Sequência: seq_tipo_natureza_despesa.';
COMMENT ON COLUMN public.tipo_natureza_despesa.nome				IS 'Nome do tipo de natureza de despesas.';
COMMENT ON COLUMN public.tipo_natureza_despesa.chave			IS 'Chave interna (utilizada no código java do sistema) do tipo de natureza de despesa. Chave única.';
COMMENT ON COLUMN public.tipo_natureza_despesa.ordem			IS 'Ordem do tipo de natureza de despesa no Relatório de Despesas (usar, se possível, espaçamento de 10 unidades entre cada tipo).';
COMMENT ON COLUMN public.tipo_natureza_despesa.criador			IS 'Responsável (login) pela criação do tipo de natureza de despesas.';
COMMENT ON COLUMN public.tipo_natureza_despesa.data_criacao		IS 'Data/hora de criação do tipo de natureza de despesas.';
COMMENT ON COLUMN public.tipo_natureza_despesa.atualizador		IS 'Responsável (login) pela última atualização do tipo de natureza de despesas.';
COMMENT ON COLUMN public.tipo_natureza_despesa.data_atualizacao	IS 'Data/hora da última atualização do tipo de natureza de despesas.';

/* Set owner */
ALTER TABLE public.tipo_natureza_despesa
	OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Modify table "lote_inativacao_registro"                                */
/* ---------------------------------------------------------------------- */
/* Drop constraints */
ALTER TABLE public.lote_inativacao_registro
	DROP CONSTRAINT ck_lote_inativacao_registro_tipo_prestacao_contas;
ALTER TABLE public.lote_inativacao_registro
	DROP CONSTRAINT ck_lote_inativacao_registro_situacao;

/* Create constraints */
ALTER TABLE public.lote_inativacao_registro
	ADD CONSTRAINT ck_lote_inativacao_registro_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1, 2, 3, 4]));

ALTER TABLE public.lote_inativacao_registro
	ADD CONSTRAINT ck_lote_inativacao_registro_tipo_prestacao_contas 
    CHECK (tipo_prestacao_contas = ANY (ARRAY[0, 1, 2, 3]));

/* Comments */
COMMENT ON COLUMN public.lote_inativacao_registro.situacao				IS 'Situação do registro do lote: 0 - Não cadastrado / 1 - Cadastrado e já estava inativo / 2 - Cadastrado e inativado SEM prestação de contas / 3 - Cadastrado e inativado COM prestação de contas / 4 - Não inativado, pois possui perfil que não permite inativação.';
COMMENT ON COLUMN public.lote_inativacao_registro.tipo_prestacao_contas	IS 'Tipo da prestação de contas localizada: 0 = Fundo fixo / 1 = Reembolso / 2 = Adiantamento / 3 = Reembolso e adiantamento.';

/* ---------------------------------------------------------------------- */
/* Modify table "transacao"                                               */
/* ---------------------------------------------------------------------- */
/* Create indexes */
CREATE INDEX ix_transacao_fundo_fixo		ON public.transacao (codigo_fundo_fixo);
CREATE INDEX ix_transacao_centro_financeiro	ON public.transacao (codigo_centro_financeiro);

/* ---------------------------------------------------------------------- */
/* Modify table "contrato"                                                */
/* ---------------------------------------------------------------------- */
/* Create constraints */
ALTER TABLE public.contrato
	ADD CONSTRAINT ck_contrato_dia_vencimento_tipo 
    CHECK (dia_vencimento_tipo = ANY (ARRAY[0, 1]));

ALTER TABLE public.contrato
	ADD CONSTRAINT ck_contrato_dia_vencimento2_tipo 
    CHECK (dia_vencimento2_tipo = ANY (ARRAY[0, 1]));

ALTER TABLE public.contrato
	ADD CONSTRAINT ck_contrato_situacao 
    CHECK (situacao = ANY (ARRAY[0, 1, 2, 3, 4, 5]));

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "motivo_retorno_documento_cf"                  */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.motivo_retorno_documento_cf DROP CONSTRAINT pk_motivo_retorno_documento_cf;

/* Create temporary table */
CREATE TABLE public.motivo_retorno_documento_cf_TMP (
    codigo							BIGINT  NOT NULL,
    codigo_motivo_retorno_documento	BIGINT  NOT NULL,
    codigo_centro_financeiro		BIGINT  NOT NULL);

/* Copy data do temporary table */
INSERT INTO public.motivo_retorno_documento_cf_TMP
    (codigo,codigo_motivo_retorno_documento,codigo_centro_financeiro)
SELECT
    codigo,codigo_motivo_retorno_documento,codigo_centro_financeiro
FROM public.motivo_retorno_documento_cf;

/* Drop indexes */
DROP INDEX public.ix_motivo_retorno_documento_cf_centro_financeiro;
DROP INDEX public.ix_motivo_retorno_documento_cf_motivo_retorno_documento;
DROP INDEX public.uq_motivo_retorno_documento_cf_motivo_retorno_documento_cf;

/* Drop old table */
DROP TABLE public.motivo_retorno_documento_cf;

/* Rename temporary table to the table */
ALTER TABLE public.motivo_retorno_documento_cf_TMP
	RENAME TO motivo_retorno_documento_cf;

/* Add primary key */
ALTER TABLE public.motivo_retorno_documento_cf
	ADD CONSTRAINT pk_motivo_retorno_documento_cf 
    PRIMARY KEY (codigo);

/* Create indexes */
CREATE INDEX ix_motivo_retorno_documento_cf_motivo_retorno_documento			ON public.motivo_retorno_documento_cf (codigo_motivo_retorno_documento);
CREATE INDEX ix_motivo_retorno_documento_cf_centro_financeiro					ON public.motivo_retorno_documento_cf (codigo_centro_financeiro);
CREATE UNIQUE INDEX uq_motivo_retorno_documento_cf_motivo_retorno_documento_cf	ON public.motivo_retorno_documento_cf (codigo_centro_financeiro,codigo_motivo_retorno_documento);

/* Comments */
COMMENT ON TABLE public.motivo_retorno_documento_cf										IS 'Associação entre um retorno (motivo de retorno x documento) e um centro financeiro (CF).';
COMMENT ON COLUMN public.motivo_retorno_documento_cf.codigo								IS 'Código interno da associação entre um retorno (motivo de retorno x documento) e um CF. Sequência: seq_motivo_retorno_documento_cf.';
COMMENT ON COLUMN public.motivo_retorno_documento_cf.codigo_motivo_retorno_documento	IS 'Código interno do motivo de retorno do documento.';
COMMENT ON COLUMN public.motivo_retorno_documento_cf.codigo_centro_financeiro			IS 'Código interno do centro financeiro.';

/* Set owner */
ALTER TABLE public.motivo_retorno_documento_cf
	OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "motivo_retorno_documento_contrato"            */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.motivo_retorno_documento_contrato DROP CONSTRAINT pk_motivo_retorno_documento_contrato;

/* Create temporary table */
CREATE TABLE public.motivo_retorno_documento_contrato_TMP (
    codigo BIGINT  NOT NULL,
    codigo_motivo_retorno_documento BIGINT  NOT NULL,
    codigo_contrato BIGINT  NOT NULL);

/* Copy data do temporary table */
INSERT INTO public.motivo_retorno_documento_contrato_TMP
    (codigo,codigo_motivo_retorno_documento,codigo_contrato)
SELECT
    codigo,codigo_motivo_retorno_documento,codigo_contrato
FROM public.motivo_retorno_documento_contrato;

/* Drop indexes */
DROP INDEX public.ix_motivo_retorno_documento_contrato_contrato;
DROP INDEX public.ix_motivo_retorno_documento_contrato_motivo_retorno_documento;
DROP INDEX public.uq_motivo_retorno_documento_contrato_contrato_motivo_ret_doc;

/* Drop old table */
DROP TABLE public.motivo_retorno_documento_contrato;

/* Rename temporary table to the table */
ALTER TABLE public.motivo_retorno_documento_contrato_TMP
	RENAME TO motivo_retorno_documento_contrato;

/* Add primary key */
ALTER TABLE public.motivo_retorno_documento_contrato
	ADD CONSTRAINT pk_motivo_retorno_documento_contrato 
    PRIMARY KEY (codigo);

/* Create indexes */
CREATE INDEX ix_motivo_retorno_documento_contrato_contrato							ON public.motivo_retorno_documento_contrato (codigo_contrato);
CREATE INDEX ix_motivo_retorno_documento_contrato_motivo_retorno_documento			ON public.motivo_retorno_documento_contrato (codigo_motivo_retorno_documento);
CREATE UNIQUE INDEX uq_motivo_retorno_documento_contrato_contrato_motivo_ret_doc	ON public.motivo_retorno_documento_contrato (codigo_contrato,codigo_motivo_retorno_documento);

/* Comments */
COMMENT ON TABLE public.motivo_retorno_documento_contrato									IS 'Associação entre um retorno (motivo de retorno x documento) e um contrato.';
COMMENT ON COLUMN public.motivo_retorno_documento_contrato.codigo							IS 'Código interno da associação entre um retorno (motivo de retorno x documento) e um contrato. Sequência: seq_motivo_retorno_documento_contrato.';
COMMENT ON COLUMN public.motivo_retorno_documento_contrato.codigo_motivo_retorno_documento	IS 'Código interno do motivo de retorno do documento.';
COMMENT ON COLUMN public.motivo_retorno_documento_contrato.codigo_contrato					IS 'Código interno do contrato.';

/* Set owner */
ALTER TABLE public.motivo_retorno_documento_contrato
	OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.contabilista							ADD CONSTRAINT fk_contabilista_pessoa_fisica 
    FOREIGN KEY (codigo)								REFERENCES public.pessoa_fisica (codigo);

ALTER TABLE public.nota_fiscal							ADD CONSTRAINT fk_nota_fiscal_documento_fiscal 
    FOREIGN KEY (codigo_documento_fiscal)				REFERENCES public.documento_fiscal (codigo);

ALTER TABLE public.nota									ADD CONSTRAINT fk_nota_natureza_pagamento 
    FOREIGN KEY (codigo_tipo_pagamento)					REFERENCES public.natureza_pagamento (codigo);

ALTER TABLE public.pagamento							ADD CONSTRAINT fk_pagamento_natureza_pagamento 
    FOREIGN KEY (codigo_tipo_pagamento)					REFERENCES public.natureza_pagamento (codigo);

ALTER TABLE public.parcela_nota							ADD CONSTRAINT fk_parcela_nota_pagamento 
    FOREIGN KEY (codigo)								REFERENCES public.pagamento (codigo);
ALTER TABLE public.parcela_nota							ADD CONSTRAINT fk_parcela_nota_nota 
    FOREIGN KEY (codigo_nota)							REFERENCES public.nota (codigo);

ALTER TABLE public.fornecedor_nat_fornecimento			ADD CONSTRAINT fk_fornecedor_nat_fornecimento_natureza_fornecimento 
    FOREIGN KEY (codigo_natureza_fornecimento)			REFERENCES public.natureza_fornecimento (codigo);
ALTER TABLE public.fornecedor_nat_fornecimento			ADD CONSTRAINT fk_fornecedor_nat_fornecimento_pessoa 
    FOREIGN KEY (codigo_fornecedor)						REFERENCES public.pessoa (codigo);

ALTER TABLE public.colaborador_centro_financeiro		ADD CONSTRAINT fk_colaborador_centro_financeiro_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)				REFERENCES public.centro_financeiro (codigo);
ALTER TABLE public.colaborador_centro_financeiro		ADD CONSTRAINT fk_colaborador_centro_financeiro_pessoa 
    FOREIGN KEY (codigo_colaborador)					REFERENCES public.pessoa (codigo);

ALTER TABLE public.natureza_despesa						ADD CONSTRAINT fk_natureza_despesa_tipo_natureza_despesa 
    FOREIGN KEY (codigo_tipo_natureza_despesa)			REFERENCES public.tipo_natureza_despesa (codigo);
ALTER TABLE public.natureza_despesa						ADD CONSTRAINT fk_natureza_despesa_colaborador 
    FOREIGN KEY (codigo_inativacao)						REFERENCES public.colaborador (codigo);

ALTER TABLE public.item_demonstrativo					ADD CONSTRAINT fk_item_demonstrativo_natureza_despesa 
    FOREIGN KEY (codigo_natureza_despesa)				REFERENCES public.natureza_despesa (codigo);

ALTER TABLE public.natureza_despesa_conta_contabil		ADD CONSTRAINT fk_natureza_despesa_conta_contabil_natureza_despesa 
    FOREIGN KEY (codigo_natureza_despesa)				REFERENCES public.natureza_despesa (codigo);

ALTER TABLE public.modulo								ADD CONSTRAINT fk_modulo_sistema 
    FOREIGN KEY (codigo_sistema)						REFERENCES public.sistema (codigo);
ALTER TABLE public.modulo								ADD CONSTRAINT fk_modulo_modulo 
    FOREIGN KEY (codigo_modulo_pai)						REFERENCES public.modulo (codigo);

ALTER TABLE public.usuario								ADD CONSTRAINT fk_usuario_receptor 
    FOREIGN KEY (codigo)								REFERENCES public.receptor (codigo);

ALTER TABLE public.permissao							ADD CONSTRAINT fk_permissao_receptor 
    FOREIGN KEY (codigo_receptor)						REFERENCES public.receptor (codigo);

ALTER TABLE public.perfil								ADD CONSTRAINT fk_perfil_receptor 
    FOREIGN KEY (codigo)								REFERENCES public.receptor (codigo);

ALTER TABLE public.funcionalidade						ADD CONSTRAINT fk_funcionalidade_modulo 
    FOREIGN KEY (codigo_modulo)							REFERENCES public.modulo (codigo);

ALTER TABLE public.motivo_retorno_documento_cf			ADD CONSTRAINT fk_motivo_retorno_documento_cf_centro_financeiro 
    FOREIGN KEY (codigo_centro_financeiro)				REFERENCES public.centro_financeiro (codigo);
ALTER TABLE public.motivo_retorno_documento_cf			ADD CONSTRAINT fk_motivo_retorno_documento_cf_motivo_retorno_documento 
    FOREIGN KEY (codigo_motivo_retorno_documento)		REFERENCES public.motivo_retorno_documento (codigo);

ALTER TABLE public.motivo_retorno_documento_contrato	ADD CONSTRAINT fk_motivo_retorno_documento_contrato_contrato 
    FOREIGN KEY (codigo_contrato)						REFERENCES public.contrato (codigo);
ALTER TABLE public.motivo_retorno_documento_contrato	ADD CONSTRAINT fk_motivo_retorno_documento_contrato_motivo_retorno_documento 
    FOREIGN KEY (codigo_motivo_retorno_documento)		REFERENCES public.motivo_retorno_documento (codigo);
