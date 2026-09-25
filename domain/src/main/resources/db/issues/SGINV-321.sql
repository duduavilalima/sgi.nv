/* ---------------------------------------------------------------------- */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2017-03-08 11:25                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Modify table "tipo_cobranca_faturamento"                               */
/* ---------------------------------------------------------------------- */
ALTER TABLE tipo_cobranca_faturamento
	ALTER COLUMN descricao TYPE CHARACTER VARYING(50);

/* ---------------------------------------------------------------------- */
/* Restart sequences                                                      */
/* ---------------------------------------------------------------------- */
ALTER SEQUENCE seq_tipo_cobranca_faturamento	RESTART WITH 1;
ALTER SEQUENCE seq_tipo_cobranca_vencimento		RESTART WITH 1;

/* ---------------------------------------------------------------------- */
/* Insert data into "tipo_cobranca_faturamento"                           */
/* ---------------------------------------------------------------------- */
INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), '1º DIA UTIL DO MÊS SUBSEQUENTE');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), FALSE, 0, 1, 1, 1);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), '5º DIA UTIL DO MÊS SUBSEQUENTE');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), FALSE, 0, 1, 5, 1);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), 'DIA 5 DO MÊS SUBSEQUENTE');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), FALSE, 0, 1, 5, 0);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), '8 DIAS UTEIS DA EMISSAO DA NF');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), FALSE, 0, 0, 8, 1);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), 'DIA 10 DO MÊS');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), FALSE, 0, 0, 10, 0);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), '10 DO MÊS SUBSEQUENTE');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), FALSE, 0, 1, 10, 0);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), 'DIA 15 DO MÊS');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), FALSE, 0, 0, 15, 0);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), '15 DO MÊS SUBSEQUENTE');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), FALSE, 0, 1, 15, 0);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), '15 DIAS - PAGA NOS DIAS 7/15 OU 25');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), TRUE, NULL, NULL, NULL, NULL);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), '11 DIAS CORRIDOS DA EMISSAO DA NF');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), FALSE, 0, 0, 11, 0);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), '20 DIAS CORRIDOS - PAGA NOS DIAS 10/20 OU 30');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), TRUE, NULL, NULL, NULL, NULL);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), 'DIA 25 DO MÊS');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), FALSE, 0, 0, 25, 0);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), '28 DIAS CORRIDOS');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), FALSE, 0, 0, 28, 0);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), 'DIA 30 DO MÊS');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), FALSE, 0, 0, 30, 0);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), '30 DIA DA EMISSAO DA NF');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), FALSE, 0, 0, 30, 0);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), '35 DIAS DA EMISSAO DA NF');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), FALSE, 0, 0, 35, 0);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), '43 EMISSAO DA NF, SENDO NOS DIAS 10 OU 29 APÓS');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), TRUE, NULL, NULL, NULL, NULL);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), '45 DIAS DA EMISSAO DA NF');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), FALSE, 0, 0, 45, 0);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), '60 DIAS DA EMISSAO DA NF');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), FALSE, 0, 0, 60, 0);

INSERT	INTO	tipo_cobranca_faturamento (codigo, descricao)
		VALUES	(NEXTVAL('seq_tipo_cobranca_faturamento'), '180 DIAS DA EMISSAO DA NF');
INSERT	INTO	tipo_cobranca_vencimento (codigo, codigo_tipo_cobranca_faturamento, solicita_data, data_referencia, mes_referencia, dia_vencimento, dia_vencimento_tipo)
		VALUES	(NEXTVAL('seq_tipo_cobranca_vencimento'), CURRVAL('seq_tipo_cobranca_faturamento'), FALSE, 0, 0, 180, 0);
