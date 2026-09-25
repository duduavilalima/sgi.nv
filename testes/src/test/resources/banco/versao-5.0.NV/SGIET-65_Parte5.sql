/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases v6.1.3  (PARTE 5)          */
/* Target DBMS:           PostgreSQL 8.3                                  */
/* Project file:          sgi.dez                                         */
/* Project name:          Sistema de Gestão Integrada (SGI)               */
/* Author:                Marcel Waldeck                                  */
/* Script type:           Alter database script                           */
/* Created on:            2016-03-21 17:59                                */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Insert "servico_tipo" (continuação)                                    */
/* ---------------------------------------------------------------------- */
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '18.01'), '18.01.04', 'liderança em co-seguro', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '18.01'), '18.01.05', 'serviços relativos a seguros', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '19', 'Serviços de distribuição e venda de bilhetes e demais produtos de loteria, bingos, cartões, pules ou cupons de apostas, sorteios, prêmios, inclusive os decorrentes de títulos de capitalização e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '19'), '19.01', 'Serviços de distribuição e venda de bilhetes e demais produtos de loteria, bingos, cartões, pules ou cupons de apostas, sorteios, prêmios, inclusive os decorrentes de títulos de capitalização e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '19.01'), '19.01.01', 'Serviços de distribuição e venda de bilhetes e demais produtos de loteria, bingos, cartões, pules ou cupons de apostas, sorteios, prêmios, inclusive os decorrentes de títulos de capitalização e congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '19.01'), '19.01.02', 'serviços relativos a títulos de capitalização e congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '20', 'Serviços portuários, aeroportuários, ferroportuários, de terminais rodoviários, ferroviários e metroviários.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20'), '20.01', 'Serviços portuários, ferroportuários, utilização de porto, movimentação de passageiros, reboque de embarcações, rebocador escoteiro, atracação, desatracação, serviços de praticagem, capatazia, armazenagem de qualquer natureza, serviços acessórios, movimentação de mercadorias, serviços de apoio marítimo, de movimentação ao largo, serviços de armadores, estiva, conferência, logística e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.01', 'serviços ferroportuários', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.02', 'logística ferroportuária', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.03', 'armazenagem de qualquer natureza, relacionada a terminal ferroportuário', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.04', 'movimentação de mercadorias em terminal ferroportuário e/ou operaçoes congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.05', 'serviços acessórios ferroportuários', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.06', 'administração da infra-estrutura ferroportuária', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.07', 'serviços portuários', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.08', 'serviços vinculados às operações dos terminais portuários na Zona Oeste do Município do Rio de Janeiro, de importação de carvão e outros insumos e exportação de placas de aço produzidas no Complexo Siderúrgico destinado à produção e à exportação de placas de aço na Zona Oeste do Município do Rio de Janeiro, satisfazendo os termos da Lei nº 4.372, de 13 de junho de 2006', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.09', 'logística portuária', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.10', 'utilização de porto', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.11', 'movimentação de passageiros em porto', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.12', 'reboque de embarcações e/ou rebocador escoteiro', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.13', 'atracação e desatracação', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.14', 'serviços de praticagem', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.15', 'capatazia em porto', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.16', 'armazenagem de qualquer natureza, em porto ou relacionada a porto', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.17', 'serviços acessórios ferroportuários', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.18', 'movimentação de mercadorias em porto e/ou operaçoes congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.19', 'serviços de apoio marítimo e/ou movimentação ao largo', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.20', 'escolta de embarcações', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.21', 'serviços de armador', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.22', 'serviços de estiva, conferência e congêneres, em portos', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.01'), '20.01.23', 'administração da infra-estrutura portuária', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20'), '20.02', 'Serviços aeroportuários, utilização de aeroporto, movimentação de passageiros, armazenagem de qualquer natureza, capatazia, movimentação de aeronaves, serviços de apoio aeroportuários, serviços acessórios, movimentação de mercadorias, logística e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.02'), '20.02.01', 'serviços aeroportuários', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.02'), '20.02.02', 'logística aeroportuária', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.02'), '20.02.03', 'utilização de aeroporto', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.02'), '20.02.04', 'movimentação de passageiros em aeroporto', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.02'), '20.02.05', 'armazenagem de qualquer natureza, em aeroporto ou relacionada a aeroporto', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.02'), '20.02.06', 'movimentação de mercadorias em aeroporto e operaçoes congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.02'), '20.02.07', 'apoio aeroportuário', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20'), '20.03', 'Serviços de terminais rodoviários, ferroviários, metroviários, movimentação de passageiros, mercadorias, inclusive     suas operações, logística e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.03'), '20.03.01', 'serviços de terminais rodoviários, movimentação de passageiros, mercadorias, inclusive suas operações, logística e congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.03'), '20.03.02', 'serviços de terminais ferroviários, movimentação de passageiros, mercadorias, inclusive suas operações, logística e congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '20.03'), '20.03.03', 'serviços de terminais metroviários, movimentação de passageiros, mercadorias, inclusive suas operações, logística e congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '21', 'Serviços de registros públicos, cartorários e notariais', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '21'), '21.01', 'Serviços de registros públicos, cartorários e notariais', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '21.01'), '21.01.01', 'serviços de registros públicos, cartorários e notariais', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '22', 'Serviços de exploração de rodovia.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '22'), '22.01', 'Serviços de exploração de rodovia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '22.01'), '22.01.01', 'serviços de exploração de rodovia mediante cobrança de preço ou pedágio dos usuários, envolvendo execução de serviços de conservação, manutenção, melhoramentos para adequação de capacidade e segurança de trânsito, operação, monitoração, assistência aos usuários e/ou outros serviços definidos em contratos, atos de concessão ou de permissão ou em normas oficiais', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '23', 'Serviços de programação e comunicação visual, desenho industrial e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '23'), '23.01', 'Serviços de programação e comunicação visual, desenho industrial e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '23.01'), '23.01.01', 'serviços de programação visual, comunicação visual ou congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '23.01'), '23.01.02', 'desenho industrial ou serviço congênere', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '24', 'Serviços de chaveiros, confecção de carimbos, placas, sinalização visual, banners, adesivos e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '24'), '24.01', 'Serviços de chaveiros, confecção de carimbos, placas, sinalização visual, banners, adesivos e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '24.01'), '24.01.01', 'chaveiro', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '24.01'), '24.01.02', 'confecção de carimbos', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '24.01'), '24.01.03', 'confecção de placas, sinalização visual, banners, adesivos e congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '25', 'Serviços funerários.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '25'), '25.01', 'Funerais, inclusive fornecimento de caixão, urna ou esquifes; aluguel de capela; transporte do corpo cadavérico; fornecimento de flores, coroas e outros paramentos; desembaraço de certidão de óbito; fornecimento de véu, essa e outros adornos; embalsamamento, embelezamento, conservação ou restauração de cadáveres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '25.01'), '25.01.01', 'serviços funerários', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '25'), '25.02', 'Cremação de corpos e partes de corpos cadavéricos.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '25.02'), '25.02.01', 'cremação de partes e/ou corpos cadavéricos', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '25'), '25.03', 'Planos ou convênio funerários.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '25.03'), '25.03.01', 'plano funerário', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '25.03'), '25.03.02', 'convênio funerário', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '25'), '25.04', 'Manutenção e conservação de jazigos e cemitérios.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '25.04'), '25.04.01', 'manutenção e conservação de jazigos e cemitérios', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '26', 'Serviços de coleta, remessa ou entrega de correspondências, documentos, objetos, bens ou valores, inclusive pelos correios e suas agências franqueadas; courrier e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '26'), '26.01', 'Serviços de coleta, remessa ou entrega de correspondências, documentos, objetos, bens ou valores, inclusive pelos correios e suas agências franqueadas; courrier e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '26.01'), '26.01.01', 'serviços de coleta, remessa ou entrega de correspondências, documentos, objetos, bens ou valores, inclusive pelos correios e suas agências franqueadas', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '26.01'), '26.01.02', 'serviços de courrier ou congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '27', 'Serviços de assistência social.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '27'), '27.01', 'Serviços de assistência social.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '27.01'), '27.01.01', 'serviços de assistência social', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '28', 'Serviços de avaliação de bens e serviços de qualquer natureza.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '28'), '28.01', 'Serviços de avaliação de bens e serviços de qualquer natureza.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '28.01'), '28.01.01', 'avaliação de bens de qualquer natureza', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '28.01'), '28.01.02', 'avaliação de serviços de qualquer natureza', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '28.01'), '28.01.03', 'avaliação de imóveis', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '29', 'Serviços de biblioteconomia.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '29'), '29.01', 'Serviços de biblioteconomia.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '29.01'), '29.01.01', 'serviços de biblioteconomia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '30', 'Serviços de biologia, biotecnologia e química.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '30'), '30.01', 'Serviços de biologia, biotecnologia e química.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '30.01'), '30.01.01', 'serviços de biologia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '30.01'), '30.01.02', 'serviços de biotecnologia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '30.01'), '30.01.03', 'serviços de química', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '31', 'Serviços técnicos em edificações, eletrônica, eletrotécnica, mecânica, telecomunicações e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '31'), '31.01', 'Serviços técnicos em edificações, eletrônica, eletrotécnica, mecânica, telecomunicações e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '31.01'), '31.01.01', 'serviços técnicos em edificações', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '31.01'), '31.01.02', 'serviços técnicos em eletrônica', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '31.01'), '31.01.03', 'serviços técnicos em eletrotécnica', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '31.01'), '31.01.04', 'serviços técnicos em mecânica', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '31.01'), '31.01.05', 'serviços técnicos em telecomunicações', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '31.01'), '31.01.06', 'serviços técnicos e congêneres, não especificados', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '31.01'), '31.01.07', 'SVA ¿ serviços de valor adicionado suportados por serviços de telecomunicações, nos termos da Lei Federal nº 9.472/97', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '32', 'Serviços de desenhos técnicos.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '32'), '32.01', 'Serviços de desenhos técnicos.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '32.01'), '32.01.01', 'serviços de desenhos técnicos', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
--100
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '33', 'Serviços de desembaraço aduaneiro, comissários, despachantes e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '33'), '33.01', 'Serviços de desembaraço aduaneiro, comissários, despachantes e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '33.01'), '33.01.01', 'desembaraço aduaneiro', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '33.01'), '33.01.02', 'comissário aduaneiro', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '33.01'), '33.01.03', 'despachante aduaneiro', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '33.01'), '33.01.04', 'serviços de despachantes e congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '34', 'Serviços de investigações particulares, detetives e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '34'), '34.01', 'Serviços de investigações particulares, detetives e congêneres.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '34.01'), '34.01.01', 'serviços de investigações particulares, detetives ou congêneres', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '35', 'Serviços de reportagem, assessoria de imprensa, jornalismo e relações públicas.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '35'), '35.01', 'Serviços de reportagem, assessoria de imprensa, jornalismo e relações públicas.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '35.01'), '35.01.01', 'reportagem', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '35.01'), '35.01.02', 'jornalismo', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '35.01'), '35.01.03', 'assessoria de imprensa', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '35.01'), '35.01.04', 'relações públicas', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '35.01'), '35.01.05', 'assessoria de relações públicas', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '36', 'Serviços de meteorologia.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '36'), '36.01', 'Serviços de meteorologia.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '36.01'), '36.01.01', 'serviços de meteorologia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '37', 'Serviços de artistas, atletas, modelos e manequins.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '37'), '37.01', 'Serviços de artistas, atletas, modelos e manequins.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '37.01'), '37.01.01', 'serviços de artistas', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '37.01'), '37.01.02', 'serviços de atletas', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '37.01'), '37.01.03', 'serviços de modelos e manequins', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '38', 'Serviços de museologia.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '38'), '38.01', 'Serviços de museologia.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '38.01'), '38.01.01', 'museologia', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '39', 'Serviços de ourivesaria e lapidação.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '39'), '39.01', 'Serviços de ourivesaria e lapidação (quando o material for fornecido pelo tomador do serviço).', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '39.01'), '39.01.01', 'serviços de ourivesaria e lapidação', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);

INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES (NULL, '40', 'Serviços relativos a obras de arte sob encomenda.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '40'), '40.01', 'Obras de arte sob encomenda.', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
INSERT INTO servico_tipo (codigo_pai, sigla, nome, situacao, habilita_faturamento, criador, data_criacao, atualizador, data_atualizacao) VALUES ((SELECT codigo FROM  servico_tipo WHERE sigla = '40.01'), '40.01.01', 'serviços relativos a obras de arte sob encomenda', 1, false, 'admin', CURRENT_TIMESTAMP, NULL, NULL);
--133

UPDATE servico_tipo SET habilita_faturamento = true WHERE sigla IN ('07.02.16', '07.02.18', '07.02.19', '07.02.20', '07.02.98', '07.10.02', '07.10.08', '07.12.01', '07.19.01', '14.01.07', '14.06.02', '17.05.01', '26.01.01', '31.01.01');

/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.conta_corrente	DROP CONSTRAINT fk_conta_corrente_conta_contabil;
ALTER TABLE public.conta_corrente	DROP CONSTRAINT fk_conta_corrente_dados_bancarios;

ALTER TABLE public.relacao_bancaria	DROP CONSTRAINT fk_relacao_bancaria_conta_corrente;

ALTER TABLE public.lancamento		DROP CONSTRAINT fk_lancamento_conta_corrente;

ALTER TABLE public.faturamento		DROP CONSTRAINT fk_faturamento_conta_corrente;

/* ---------------------------------------------------------------------- */
/* Drop and recreate table "conta_corrente"                               */
/* ---------------------------------------------------------------------- */
/* Table must be recreated because some of the changes can't be done with the regular commands available. */
ALTER TABLE public.conta_corrente	DROP CONSTRAINT pk_conta_corrente;
ALTER TABLE public.conta_corrente	DROP CONSTRAINT ck_conta_corrente_tipo;

/* Create temporary table */
CREATE TABLE public.conta_corrente_TMP (
    codigo						BIGINT  NOT NULL,
    codigo_conta_contabil		BIGINT,
    tipo						SMALLINT DEFAULT 0  NOT NULL,
    cheque_carbono				BOOLEAN DEFAULT true  NOT NULL,
    internet_banking			BOOLEAN DEFAULT false  NOT NULL,
    boleto_faturamento			BOOLEAN DEFAULT false  NOT NULL,
    cheque_anterior				BIGINT  NOT NULL,
    espaco_antes_valor			SMALLINT,
    salto_valor_extenso			SMALLINT,
    espaco_antes_extenso		SMALLINT,
    salto_extenso_extenso2		SMALLINT,
    espaco_antes_extenso2		SMALLINT,
    salto_extenso2_favorecido	SMALLINT,
    espaco_antes_favorecido		SMALLINT,
    salto_favorecido_data		SMALLINT,
    espaco_antes_data			SMALLINT,
    salto_endosso				SMALLINT,
    espaco_antes_endosso		SMALLINT,
    espaco_antes_assinatura		SMALLINT,
    identificador_cliente		INTEGER,
    identificador_fise			INTEGER,
    prazo_processamento_ib		SMALLINT,
    criador						CHARACTER VARYING(255),
    data_criacao				TIMESTAMP,
    atualizador					CHARACTER VARYING(255),
    data_atualizacao			TIMESTAMP);

/* Move data to the temporary table */
INSERT INTO public.conta_corrente_TMP
    (codigo,codigo_conta_contabil,tipo,cheque_carbono,internet_banking,cheque_anterior,espaco_antes_valor,salto_valor_extenso,espaco_antes_extenso,salto_extenso_extenso2,espaco_antes_extenso2,salto_extenso2_favorecido,espaco_antes_favorecido,salto_favorecido_data,espaco_antes_data,salto_endosso,espaco_antes_endosso,espaco_antes_assinatura,identificador_cliente,identificador_fise,prazo_processamento_ib,criador,data_criacao,atualizador,data_atualizacao)
SELECT
    codigo,codigo_conta_contabil,tipo,cheque_carbono,internet_banking,cheque_anterior,espaco_antes_valor,salto_valor_extenso,espaco_antes_extenso,salto_extenso_extenso2,espaco_antes_extenso2,salto_extenso2_favorecido,espaco_antes_favorecido,salto_favorecido_data,espaco_antes_data,salto_endosso,espaco_antes_endosso,espaco_antes_assinatura,identificador_cliente,identificador_fise,prazo_processamento_ib,criador,data_criacao,atualizador,data_atualizacao
FROM public.conta_corrente;

/* Drop index */
DROP INDEX public.ix_conta_corrente_conta_contabil;

/* Drop "old" table */
DROP TABLE public.conta_corrente;

/* Rename temporary table to "new" table */
ALTER TABLE public.conta_corrente_TMP
	RENAME TO conta_corrente;

/* Add primary key */
ALTER TABLE public.conta_corrente
	ADD CONSTRAINT pk_conta_corrente 
    PRIMARY KEY (codigo);

/* Add constraint */
ALTER TABLE public.conta_corrente
	ADD CONSTRAINT ck_conta_corrente_tipo 
    CHECK (tipo = ANY (ARRAY[0, 1, 2]));

/* Create index */
CREATE INDEX ix_conta_corrente_conta_contabil ON public.conta_corrente (codigo_conta_contabil);

/* Comments */
COMMENT ON TABLE public.conta_corrente								IS 'Contas correntes.';
COMMENT ON COLUMN public.conta_corrente.codigo						IS 'Código interno da conta corrente (código interno dos dados bancários).';
COMMENT ON COLUMN public.conta_corrente.codigo_conta_contabil		IS 'Código interno da conta contábil relacionada a conta corrente.';
COMMENT ON COLUMN public.conta_corrente.tipo						IS 'Código interno do tipo da conta corrente: 0 = Normal / 1 = Apenas usada para transferências (e recebimento) / 2 = Inativa.';
COMMENT ON COLUMN public.conta_corrente.cheque_carbono				IS 'Indica se o talão de cheques da conta corrente possui carbono (true) ou não (false).';
COMMENT ON COLUMN public.conta_corrente.internet_banking			IS 'Indica se a conta corrente está habilitada para internet banking (true) ou não (false).';
COMMENT ON COLUMN public.conta_corrente.boleto_faturamento			IS 'Indica se a conta corrente está habilitada para emissão de boletos para o faturamento (true) ou não (false).';
COMMENT ON COLUMN public.conta_corrente.cheque_anterior				IS 'Indica o nº do último cheque da conta corrente utilizado.';
COMMENT ON COLUMN public.conta_corrente.espaco_antes_valor			IS 'Indica, na impressão do cheque da conta corrente, o nº de espaços antes do valor.';
COMMENT ON COLUMN public.conta_corrente.salto_valor_extenso			IS 'Indica, na impressão do cheque da conta corrente, o nº de linhas de salto entre o valor e a 1ª linha do valor por extenso.';
COMMENT ON COLUMN public.conta_corrente.espaco_antes_extenso		IS 'Indica, na impressão do cheque da conta corrente, o nº de espaços antes da 1ª linha do valor por extenso.';
COMMENT ON COLUMN public.conta_corrente.salto_extenso_extenso2		IS 'Indica, na impressão do cheque da conta corrente, o nº de linhas de salto entre a 1ª linha do valor por extenso e a 2ª linha do valor por extenso.';
COMMENT ON COLUMN public.conta_corrente.espaco_antes_extenso2		IS 'Indica, na impressão do cheque da conta corrente, o nº de espaços antes da 2ª linha do valor por extenso.';
COMMENT ON COLUMN public.conta_corrente.salto_extenso2_favorecido	IS 'Indica, na impressão do cheque da conta corrente, o nº de linhas de salto entre a 2ª linha do valor por extenso e o nome do favorecido.';
COMMENT ON COLUMN public.conta_corrente.espaco_antes_favorecido		IS 'Indica, na impressão do cheque da conta corrente, o nº de espaços antes do nome do favorecido.';
COMMENT ON COLUMN public.conta_corrente.salto_favorecido_data		IS 'Indica, na impressão do cheque da conta corrente, o nº de linhas de salto entre o nome do favorecido e a data do cheque.';
COMMENT ON COLUMN public.conta_corrente.espaco_antes_data			IS 'Indica, na impressão do cheque da conta corrente, o nº de espaços antes da data do cheque.';
COMMENT ON COLUMN public.conta_corrente.salto_endosso				IS 'Indica, na impressão do endosso (verso do cheque) da conta corrente, o nº de linhas de salto antes do início do endosso.';
COMMENT ON COLUMN public.conta_corrente.espaco_antes_endosso		IS 'Indica, na impressão do endosso (verso do cheque) da conta corrente, o nº de espaços antes do endosso.';
COMMENT ON COLUMN public.conta_corrente.espaco_antes_assinatura		IS 'Indica, na impressão do endosso (verso do cheque) da conta corrente, o nº de espaços antes da assinatura.';
COMMENT ON COLUMN public.conta_corrente.identificador_cliente		IS 'Código identificado do cliente na instituição financeira (interface bancária).';
COMMENT ON COLUMN public.conta_corrente.identificador_fise			IS 'Código identificado FISE do cliente na instituição financeira (interface bancária).';
COMMENT ON COLUMN public.conta_corrente.prazo_processamento_ib		IS 'Prazo máximo, em horas, para processamento de um arquivo enviado à instituição financeira. Após este prazo, e somente após ele, será permitida a conciliação manual da RB.';
COMMENT ON COLUMN public.conta_corrente.criador						IS 'Responsável (login) pela criação da conta corrente.';
COMMENT ON COLUMN public.conta_corrente.data_criacao				IS 'Data/hora de criação da conta corrente.';
COMMENT ON COLUMN public.conta_corrente.atualizador					IS 'Responsável (login) pela última atualização da conta corrente.';
COMMENT ON COLUMN public.conta_corrente.data_atualizacao			IS 'Data/hora da última atualização da conta corrente.';

/* Set owner */
ALTER TABLE public.conta_corrente OWNER TO sgi;

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */
ALTER TABLE public.conta_corrente					ADD CONSTRAINT fk_conta_corrente_dados_bancarios 
    FOREIGN KEY (codigo)							REFERENCES public.dados_bancarios (codigo);
ALTER TABLE public.conta_corrente					ADD CONSTRAINT fk_conta_corrente_conta_contabil 
    FOREIGN KEY (codigo_conta_contabil)				REFERENCES public.conta_contabil (codigo);

ALTER TABLE public.relacao_bancaria					ADD CONSTRAINT fk_relacao_bancaria_conta_corrente 
    FOREIGN KEY (codigo_conta_corrente)				REFERENCES public.conta_corrente (codigo);

ALTER TABLE public.lancamento						ADD CONSTRAINT fk_lancamento_conta_corrente 
    FOREIGN KEY (codigo_conta_corrente)				REFERENCES public.conta_corrente (codigo);

ALTER TABLE public.faturamento						ADD CONSTRAINT fk_faturamento_conta_corrente 
    FOREIGN KEY (codigo_conta_corrente)				REFERENCES public.conta_corrente (codigo);

/* ---------------------------------------------------------------------- */
/* Updata data                                                            */
/* ---------------------------------------------------------------------- */
UPDATE	conta_corrente
SET		boleto_faturamento = TRUE
FROM	dados_bancarios db
WHERE	conta_corrente.codigo = db.codigo
AND		db.codigo_banco = 341;
