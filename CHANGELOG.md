# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

### [5.13.4-rc.8](http://git.fasolti.com.br/aa/sgi/compare/v5.13.4-rc.7...v5.13.4-rc.8) (2020-03-11)


### Features

* exibir os valores das colunas em cinza (indicando que não será considerado no total - a cor do foreground deve ser cinza, não do backgroud) (SGINV-2667). ([73a5d90](http://git.fasolti.com.br/aa/sgi/commit/73a5d903c0f29a16168ab8a038f4febb22311181))
* Infraestrutura > Script: parametrizar assinatura da Contabilidade (SGINV-2671). ([237a345](http://git.fasolti.com.br/aa/sgi/commit/237a34502ef7e4005fb4fa3d6da4a138a5abc942))
* Parametrizar a assinatura (novo campo "validar_conta_contabil_cliente", tabela "etapa_fluxo"), ou seja, não usar mais o código da etapa da Contabilidade (código = 6) (SGINV-2670). ([86609fc](http://git.fasolti.com.br/aa/sgi/commit/86609fc6ccaca2e5114f978428a24e8c6dd78fa2))


### Bug Fixes

* Ajuste no label do campo "Estágio comercial" (SGINV-2613). ([bc66309](http://git.fasolti.com.br/aa/sgi/commit/bc6630983113317842380593ede3dd1f4fe162ca))
* Campo "Engenheiro(a) responsável": se não possui valor, sempre aparece com "123123" (SGINV-2669). ([eebe73b](http://git.fasolti.com.br/aa/sgi/commit/eebe73b09ecb21dcdc7e1b7cf61b8f7eec2bfdc0))
* Campo "Gerência": listar opções conforme o acesso do usuário (SGINV-2666). ([d87e1e3](http://git.fasolti.com.br/aa/sgi/commit/d87e1e38b50f6e109e6e7cd02fdecda33c3175e1))
* Modo de exibição, botão "IMPRIMIR OS INICIAL" / Ajustar para exibir valores zerados / Campos "Encargos sociais" e "Impostos": se zerados, não exibir o símbolo "%" (SGINV-2668). ([b56a352](http://git.fasolti.com.br/aa/sgi/commit/b56a352ef241aa1fec23dd3c2476bf4758dc7fc8))

### [5.13.4-rc.7](http://git.fasolti.com.br/aa/sgi/compare/v5.13.4-rc.6...v5.13.4-rc.7) (2020-03-09)


### Features

* Finalizando email CLs com despesas acima do limite (SGINV-2652). ([ca2e276](http://git.fasolti.com.br/aa/sgi/commit/ca2e276b24d54d45d9187aea9cb86fa401e378b0))
* Finalizando email CLs com despesas acima do limite (SGINV-2652). ([9ae5e56](http://git.fasolti.com.br/aa/sgi/commit/9ae5e5658fc0bd8ce0c37676b7710ef2176fccbf))
* Infraestrutura > Job > CLs com despesas acima do limite: criar (SGINV-2652). ([dfa576a](http://git.fasolti.com.br/aa/sgi/commit/dfa576a02caf9fa60a9997d0a4b5e7cde980b7e2))
* Infraestrutura > Job > CLs com despesas acima do limite: criar (SGINV-2652). ([93113db](http://git.fasolti.com.br/aa/sgi/commit/93113dbba26144d961d10f18ffe44d3c25261328))
* Infraestrutura > Job > CLs com despesas acima do limite: criar (SGINV-2652). ([4e6ea97](http://git.fasolti.com.br/aa/sgi/commit/4e6ea9725fe2e5b4ffb155d276e6728b8c3c0352))
* Infraestrutura > Job > CLs com despesas acima do limite: criar (SGINV-2652). ([2825244](http://git.fasolti.com.br/aa/sgi/commit/2825244b7c59e16f4d5b72c90acc53b99ec1f73b))
* Infraestrutura > Job > CLs com despesas acima do limite: criar (SGINV-2652). ([d51bcb0](http://git.fasolti.com.br/aa/sgi/commit/d51bcb06f4959576373b343baeca07304c7a6193))
* Infraestrutura > Job > CLs com despesas acima do limite: criar (SGINV-2652). ([28f30d6](http://git.fasolti.com.br/aa/sgi/commit/28f30d6a32840979177102e963f8795db2093d80))
* Permitir qualquer cliente para o faturamento (SGINV-2659). ([9d8f288](http://git.fasolti.com.br/aa/sgi/commit/9d8f28855c75c829d2f16e1bbb9ad8d650e063e1))
* Permitir que a data de contabilização (campo "Contabilização") seja um sábado (manter demais restrições, ou seja, domingos e feriados) (SGINV-2672). ([c992b1f](http://git.fasolti.com.br/aa/sgi/commit/c992b1f9728fc298606a061219438454eab0b13e))


### Bug Fixes

* **Job:** Corrigindo cálculo de valores quando rateio de NF (SGINV-2652). ([cd3d1ef](http://git.fasolti.com.br/aa/sgi/commit/cd3d1ef4b1d081447610eb632c5d05527d77f8c6))
* Campo "Subcliente": ao alterar (de "Sim" para "Não") não é gravado (a trilha de auditoria sim, mas este campo em si, não) (SGINV-2663). ([ffd63c9](http://git.fasolti.com.br/aa/sgi/commit/ffd63c9f34d726b3a5b30fe075cd5fd5495b2e8a))
* Correção da ordem do email do CLs com gastos acima do limite. ([f100b07](http://git.fasolti.com.br/aa/sgi/commit/f100b07e637e15ae30cc7aaaa8a48af34382d9ab))
* Correção da ordem do email do CLs com gastos acima do limite. ([247390f](http://git.fasolti.com.br/aa/sgi/commit/247390f75f41f9e408754b44dc42830b2f5e2247))
* Correção do cliente e centro de lucro do faturamento na edição. ([1eefae3](http://git.fasolti.com.br/aa/sgi/commit/1eefae3665780608d507a5b50d03852c68d21154))
* Correção na edição do faturamento. ([b75d377](http://git.fasolti.com.br/aa/sgi/commit/b75d3772a1d6e676c05913d1ff98a7667111c219))
* Criação do script (SGINV-2653). ([9f3bed6](http://git.fasolti.com.br/aa/sgi/commit/9f3bed6b21c943976c7248a7f4435088327df4ba))
* Criação do script (SGINV-2653). ([1b238a0](http://git.fasolti.com.br/aa/sgi/commit/1b238a0a0693616121d1d9fb22154e8cd95aa61b))
* Criação do script (SGINV-2653). ([62cfb4e](http://git.fasolti.com.br/aa/sgi/commit/62cfb4ee5b575c82cf4756928d56322f85835561))

### [5.13.4-rc.6](http://git.fasolti.com.br/aa/sgi/compare/v5.13.4-rc.5...v5.13.4-rc.6) (2020-02-13)


### Features

* **ci:** adicionar cache para artefatos gerados pelo frontend-maven-plugin ([9ec7f93](http://git.fasolti.com.br/aa/sgi/commit/9ec7f93817e7e3320d14fc39fc8a44ece842b8f4))
* **ci:** adicionar geração local de imagens docker, adicionar pastas de cache do Yarn ([7531e23](http://git.fasolti.com.br/aa/sgi/commit/7531e230663861a5507e1119d95cde6d56635f8e))
* **ci:** configurar build e deploy via CI ([aef5f86](http://git.fasolti.com.br/aa/sgi/commit/aef5f863c2951e8d16ebeb9b1bcf6396163ccea8))
* Parcelas, modo de exibição, aba "CONTROLES", edição de observação (SGINV-2642). ([9164336](http://git.fasolti.com.br/aa/sgi/commit/9164336a2c98b549d34fb70ae0e6e0c0ae6fd310))
* **ci:** adicionar cache para artefatos gerados pelo frontend-maven-plugin ([8e785b9](http://git.fasolti.com.br/aa/sgi/commit/8e785b957089aaa37301d0d3b76f8a9e236d0e4b))
* **ci:** adicionar geração local de imagens docker, adicionar pastas de cache do Yarn ([d954018](http://git.fasolti.com.br/aa/sgi/commit/d95401882652cacf7a8e17ad8d826c8dec519916))
* **ci:** configurar build e deploy via CI ([218905f](http://git.fasolti.com.br/aa/sgi/commit/218905f5ef81001d5cd9a38d51de694ccb65167a))


### Bug Fixes

* Correção da edição do cliente (SGINV-2660). ([a08f3ad](http://git.fasolti.com.br/aa/sgi/commit/a08f3ad4c5208e9d322b428b36baab68a8141870))
* Correção da exibição do cliente (SGINV-2661). ([2a1d51f](http://git.fasolti.com.br/aa/sgi/commit/2a1d51faa75f37a3ff6ca4f9d450ea95682a8a2b))
* Correção no cadastro do cliente. ([c00e122](http://git.fasolti.com.br/aa/sgi/commit/c00e12298c74bfd26cfc65e8ef6c9a1785445703))
* **ci:** adicionar variável PROJECT_GROUP à imagem, corrigir valores das labels de build nas imagens, condicionar docker-push à env-var NOPUSH ([9429b0a](http://git.fasolti.com.br/aa/sgi/commit/9429b0aca14eb4176472b7a222ca80344637ad95))
* Edição de subliente no faturamento. ([506a61b](http://git.fasolti.com.br/aa/sgi/commit/506a61be0ec84851d5e7acdc389f9dad8f721a65))
* **ci:** adicionar variável PROJECT_GROUP à imagem, corrigir valores das labels de build nas imagens, condicionar docker-push à env-var NOPUSH ([4491ea1](http://git.fasolti.com.br/aa/sgi/commit/4491ea16bc4275adbbd09a99f1bf019991a8e648))

### [5.13.4-rc.5](http://git.fasolti.com.br/aa/sgi/compare/v5.13.4-rc.4...v5.13.4-rc.5) (2020-01-30)


### Features

* Mensagem de confirmação da assinatura do centro financeiro e mensagem de alerta para clientes que não possuem dados contabeis associada a matriz do CF. SGINV-2639 ([34a5172](http://git.fasolti.com.br/aa/sgi/commit/34a51727b19991055e4abd46a24b41de14bfc301))


### Bug Fixes

* Ajuste na data de pagamento e agendamento da nota, considerando finais de semana e feriados. SGINV-2631 ([9b80ae4](http://git.fasolti.com.br/aa/sgi/commit/9b80ae4fa5537842b88098e630d344012636fb7f))
* Ajuste no carregamento do autocomplete de serviço da NF (SGINV-2641). ([c530cc4](http://git.fasolti.com.br/aa/sgi/commit/c530cc481fea5b7a58e3023fbcb8f9674819dea6))
* Ajuste no layout dos campos Motivo da alteração data de vencimento e Observações; atendendo as regras do issue. SGINV-2633 ([c45169a](http://git.fasolti.com.br/aa/sgi/commit/c45169a2b8b3f9d4b9ad791ee0b198c6a845e495))
* Ajuste nova data de vencimento na parcela. SGINV-2631 ([698f30d](http://git.fasolti.com.br/aa/sgi/commit/698f30dd7c1635a4207f86ceff45b83e0585b617))
* Ajuste nova data de vencimento na parcela. SGINV-2631 ([791692e](http://git.fasolti.com.br/aa/sgi/commit/791692e65e7df663f56214dc87a25ff020a438ca))
* Alteração de tooltip para mostrar o documento e nome do cliente na coluna Cliente/Sub (nome fantasia). ([193c442](http://git.fasolti.com.br/aa/sgi/commit/193c442cc2bcc211a56aa36f2c701efde5e58ef4))
* Comercial > Cadastros > Centros Financeiros > Edição: ajustar (SGINV-2638). ([62e5b5d](http://git.fasolti.com.br/aa/sgi/commit/62e5b5d431a82350bbea35353e195716ccddeb1d))
* Correção da impressão do endosso do cheque da RB. ([840b93f](http://git.fasolti.com.br/aa/sgi/commit/840b93fc8612296d54f31c0fa64a723668efcbf5))
* Correção do conta contábil do popup de pessoa jurídica. ([bce6b73](http://git.fasolti.com.br/aa/sgi/commit/bce6b734a82cab69d52089e70b73bf37e205ec42))
* Corrigindo cálculo dos tributos da NF criada a partir de uma etapa. ([c24960c](http://git.fasolti.com.br/aa/sgi/commit/c24960c22785b10c66d74385edd81d873c048dd8))
* Faturamento > Notas fiscais > Edição/Novo: ajustar arredondamento do ISS (SGINV-2635). ([893c748](http://git.fasolti.com.br/aa/sgi/commit/893c7489ee19b35f7c674843dded9f020cb3be9f))

### [5.13.4-rc.4](http://git.fasolti.com.br/aa/sgi/compare/v5.13.4-rc.3...v5.13.4-rc.4) (2020-01-09)


### Bug Fixes

* Correção no nome do atualizador do centro financeiro (SGINV-2637). ([b38d2b4](http://git.fasolti.com.br/aa/sgi/commit/b38d2b412aec5cc90fa6cab4fbe4e73e8b89e263))

### [5.13.4-rc.3](http://git.fasolti.com.br/aa/sgi/compare/v5.13.4-rc.2...v5.13.4-rc.3) (2020-01-06)


### Bug Fixes

* Ajustando valor imposto do Fecho (SGINV-2624). ([4a00438](http://git.fasolti.com.br/aa/sgi/commit/4a0043891db1bc456093e6fe9330d80d1fef6226))
* Ajuste do reset da numeração na virada do ano e ajuste para a nota fiscal quando zerada começar com o numero 1. SGINV-2629 ([d1ae95f](http://git.fasolti.com.br/aa/sgi/commit/d1ae95fc11c935f0aa3fd2ed4a8f3eb1520ac103))
* Ajuste no gráfico do menu Custos >> comparativo anual >> Faturamento (NF emitidas) na linha(azul) de previsto, passando a considerar a data de previsão do primeiro faturamento e a data do encerramento do CL. SGINV-1740 ([6bc9151](http://git.fasolti.com.br/aa/sgi/commit/6bc9151fdb00e355b0062776e4ec1e4b3211d498))
* Ajuste no gráfico do menu Custos >> comparativo anual, passando a considerar a data de previsão do primeiro faturamento e a data do encerramento do CL. SGINV-1740 ([77df8ca](http://git.fasolti.com.br/aa/sgi/commit/77df8cafc833e1fa818da6d6c876fdb1c8be3abf))
* Ajuste para mostrar o valor líquido negativo em vermelho e exibir a mensagem de erro na tentativa de receber a parcela. SGINV-2630 ([032b109](http://git.fasolti.com.br/aa/sgi/commit/032b109c972e011294f50bc2d70c67488fd691c9))

### [5.13.4-rc.0](http://git.fasolti.com.br/aa/sgi/compare/v5.13.3-rc.2...v5.13.4-rc.0) (2019-12-05)


### Features

* Ajustes no comportamento da tela e layout SGINV-2511 ([7f4dd60](http://git.fasolti.com.br/aa/sgi/commit/7f4dd600ad2fa5188cb7e8fb3e5be2b403356d6f))
* Ajustes no filtro de relatórios do menu contas a receber SGINV-2581 ([417bab0](http://git.fasolti.com.br/aa/sgi/commit/417bab02c9e40b1bab7b318205f64288058fe778))
* Alteração do comportamento do campo data. SGINV-2597 ([8d34bb8](http://git.fasolti.com.br/aa/sgi/commit/8d34bb8f31dd2c6c5f6da69b8f3f3bff1d0a4a5c))
* Alterações nas regras do relatório de mapa de vendas do menu faturamento ([5c83835](http://git.fasolti.com.br/aa/sgi/commit/5c8383547898f0db806639f654d1a09b32d7f09d))
* Inserção de novos campos e validações do formulário para o relatório de mapa de vendas. SGINV-2511 ([d235682](http://git.fasolti.com.br/aa/sgi/commit/d235682206ed2bfb541f265f4d508ff991634c24))
* Inserção do relatório: Duplicatas em aberto (auditoria externa). SGINV-2506 ([8f35f7c](http://git.fasolti.com.br/aa/sgi/commit/8f35f7c84a48d445d5b0f40aa26a0ef7e266ed0c))
* Inserção do relatório: Duplicatas em aberto (auditoria externa). SGINV-2506 ([b287fc9](http://git.fasolti.com.br/aa/sgi/commit/b287fc900172ba71fae1c073ca62240403ebed20))
* Inserção do relatório: Duplicatas em aberto (auditoria externa). SGINV-2601 ([9bbd7a3](http://git.fasolti.com.br/aa/sgi/commit/9bbd7a369667d2f4b8576e65c77efc96521f9c93))
* Inserção dos relatórios Posição e Resumo de posição de faturamento por gerência. SGINV-2591 ([c6f483a](http://git.fasolti.com.br/aa/sgi/commit/c6f483ada4b2519877fab5b8ea19fbfa44608ea0))
* **Boleto Itaú:** Tarefas Referentes aos Issue (SGINV-2515). ([cacc34c](http://git.fasolti.com.br/aa/sgi/commit/cacc34cbf14a71de4994615c40236c21c53393c5))
* **Comercial:** Ajuste Centros Financeiro (SGINV-2603) ([ae5c891](http://git.fasolti.com.br/aa/sgi/commit/ae5c8915e28ec20aacf91b70e46fa80d44ddea34))
* **Comercial:** Ajuste Centros Financeiro (SGINV-2603) ([3245a6a](http://git.fasolti.com.br/aa/sgi/commit/3245a6a9caae3c402fad88f7ae2322907c16d4e5))
* **Comercial:** Ajuste Centros Financeiro (SGINV-2603) ([b2ac294](http://git.fasolti.com.br/aa/sgi/commit/b2ac294c81cd8243d867a4ad2b22529652e8a9c3))
* **Comercial:** Ajuste Centros Financeiro (SGINV-2603) ([189beb9](http://git.fasolti.com.br/aa/sgi/commit/189beb93bf19f0c30b8b8ba57061548e280f2645))
* **Comercial:** Ajustes para o Centro Financeiro ([a4f1b1a](http://git.fasolti.com.br/aa/sgi/commit/a4f1b1a19d34518f272b76979ea2dd24255e6474))
* **Comercial:** Ajustes para o Centro Financeiro ([68c7902](http://git.fasolti.com.br/aa/sgi/commit/68c79020afb3dbf78acc575937a275b3fbbb7323))
* **Comercial:** Ajustes para o Centro Financeiro (SGINV-2574) ([5c097ed](http://git.fasolti.com.br/aa/sgi/commit/5c097ed423e3df9b97786a49b66708a89642e824))
* **Comercial:** Ajustes para o Centro Financeiro (SGINV-2574) ([3ca6cc3](http://git.fasolti.com.br/aa/sgi/commit/3ca6cc3de9f83374079e02738f68a7278a7b7f94))
* **Comercial:** Ajustes para o Centro Financeiro (SGINV-2574) ([4a5b71c](http://git.fasolti.com.br/aa/sgi/commit/4a5b71cfce1f9fbb4a387d70a75dd5a7f6b11b6e))
* **Comercial:** Ajustes para o Centro Financeiro (SGINV-2574) ([e929d99](http://git.fasolti.com.br/aa/sgi/commit/e929d99cb04c30f9bb62c553d0d9b8b0793ea41e))
* **Comercial:** Ajustes para o Centro Financeiro (SGINV-2574) ([d0353c9](http://git.fasolti.com.br/aa/sgi/commit/d0353c9444d0a95990cd7a172441148636b965d0))
* **Comercial:** Ajustes para o Centro Financeiro (SGINV-2574) ([cc6e162](http://git.fasolti.com.br/aa/sgi/commit/cc6e16290d76c216b44d525190b7e2f3ff990082))
* **Comercial:** Ajustes para o Centro Financeiro (SGINV-2574) ([9078b59](http://git.fasolti.com.br/aa/sgi/commit/9078b59ca048164705bf2a245952269a7e03df77))
* **Comercial:** Ajustes para o Centros Financeiros (SGINV-2577) ([99688a2](http://git.fasolti.com.br/aa/sgi/commit/99688a2c3066800472ef0a366e7ab51d588c9d1a))
* **Comercial:** Ajustes para os Centros Financeiros (SGINV-2592) ([a07eade](http://git.fasolti.com.br/aa/sgi/commit/a07eade6e4ce81f848543cbf863ead0f76a7ee23))
* **Contas a Receber:** Ajuste para edição de Parcela (SGINV-2579) ([3fabf09](http://git.fasolti.com.br/aa/sgi/commit/3fabf0924923e5e7bc5e76de6c21991d2bca3dbc))
* **Contas a Receber:** Ajuste Parcela (SGINV-2578) ([4349edd](http://git.fasolti.com.br/aa/sgi/commit/4349edde5527b3aae0bb6986c089c968a67a6b09))
* **Contas a Receber:** Ajuste Parcela (SGINV-2578) ([e3508d0](http://git.fasolti.com.br/aa/sgi/commit/e3508d059c3bf6caa08c7dbf7936e0bb32a42871))
* **Contas a Receber:** Receber Parcela (SGINV-2580) ([d0dc7f6](http://git.fasolti.com.br/aa/sgi/commit/d0dc7f6e38410ad7bfff75b4e4f6c27075e5cba0))
* **Faturamento:** Ajustes para o Faturamento (SGINV-2583) ([cd9e4d4](http://git.fasolti.com.br/aa/sgi/commit/cd9e4d4766fd868061ed8cfd85c85bed7db0e544))
* **Tesouraria:** Ajuste Movimento Autorizado (SGINV-2594) ([6625dd3](http://git.fasolti.com.br/aa/sgi/commit/6625dd3e0f4166f8143d8e74dc8516d883ca87c9))
* **Tesouraria:** Ajuste Movimento Autorizado (SGINV-2594) ([fe18a9f](http://git.fasolti.com.br/aa/sgi/commit/fe18a9f2f971bce3c606f432000bf07d1de811ba))


### Bug Fixes

* **Comercial:** Ajuste para a tela de edição de Centro Financeiro. ([19ee97f](http://git.fasolti.com.br/aa/sgi/commit/19ee97fdf782068ccca863cfabf7b5895bf308ec))
* **Comercial:** Ajustes para edição de clientes (SGINV-2509) ([6d9f40d](http://git.fasolti.com.br/aa/sgi/commit/6d9f40dfbb4c059f94499bd479647435f4c13e5c))
* **Comercial:** Ajustes para edição do Fecho (SGINV-2574) ([3aee816](http://git.fasolti.com.br/aa/sgi/commit/3aee816a9e6603f46db35ebda3c0afc292ab4b7f))
* **Comercial:** Ajustes para o Centro Financeiro ([88349b2](http://git.fasolti.com.br/aa/sgi/commit/88349b2188f6a2a0c82c3b28b8fbc5fba38f8629))
* **Comercial:** Ajustes para o Centro Financeiro (SGINV-2576) ([6a9ac9a](http://git.fasolti.com.br/aa/sgi/commit/6a9ac9a444d9998ebb5917f8e05df52d6333017a))
* **Comercial:** Ajustes para o Centros Financeiro (SGINV-2575) ([4af1a2b](http://git.fasolti.com.br/aa/sgi/commit/4af1a2b90de0c9f9fd6dcfe8152bcba187ce2559))
* **Comercial:** Ajustes para o Centros Financeiros ([#4](http://git.fasolti.com.br/aa/sgi/issues/4)) ([da2afa5](http://git.fasolti.com.br/aa/sgi/commit/da2afa54adf433700a76e0fd5b23faabd964c50d))
* **Comercial/Cadastro/Obras:** Ultimos ajustes de refatoração geral e ajustes na list.controller e list.html([#7](http://git.fasolti.com.br/aa/sgi/issues/7)). ([cb5dd3a](http://git.fasolti.com.br/aa/sgi/commit/cb5dd3a2aca33e7e964426de9a5568fbf2c8f757))
* **Comercial/Cadastro/Relacionamentosdecontratos:** correção do module com a inclusão do controllerAs ([#11](http://git.fasolti.com.br/aa/sgi/issues/11)). ([8031d5e](http://git.fasolti.com.br/aa/sgi/commit/8031d5e6e75d159da5425369e915089285db3069))
* **Comercial/Contratos:** ajuste backend para ajustar a remoção do Aditivo adicinando @Transational no service ([#9](http://git.fasolti.com.br/aa/sgi/issues/9)). ([b5bf205](http://git.fasolti.com.br/aa/sgi/commit/b5bf2056cdd0303cb420ca8a0328e831f2c9a09f))
* **Comercial/Contratos:** ajuste backend para ajustar a remoção do contrato endereço adicinando @Transational no service ([#9](http://git.fasolti.com.br/aa/sgi/issues/9)). ([855c93f](http://git.fasolti.com.br/aa/sgi/commit/855c93f4ba7166e055b211613751f6e6e8a9b477))
* **Comercial/Contratos:** Ajuste Backend, transação remover não removia contatoContrato, inserido @Transactional  ([#9](http://git.fasolti.com.br/aa/sgi/issues/9)). ([39bd18b](http://git.fasolti.com.br/aa/sgi/commit/39bd18bc2d6447310278d4f495cb50d5cdee79ba))
* **Comercial/Contratos:** Ajuste de recursividade de TOs de Gerencia por diretoria ([#9](http://git.fasolti.com.br/aa/sgi/issues/9)). ([4c3a170](http://git.fasolti.com.br/aa/sgi/commit/4c3a170fef6c590e9efdfbfa354caae56b7f0297))
* **Comercial/Contratos:** Ajuste gerais. ([#9](http://git.fasolti.com.br/aa/sgi/issues/9)). ([d5ded0c](http://git.fasolti.com.br/aa/sgi/commit/d5ded0ca078b34f9daf0beab131bb46a4c45682a))
* **Comercial/Contratos:** Ajustes no método encerrar contrato, pois retornava 500. ([#9](http://git.fasolti.com.br/aa/sgi/issues/9)). ([0f6c75d](http://git.fasolti.com.br/aa/sgi/commit/0f6c75dd52f721dc5686b4e9e48be89128f68d01))
* **Comercial/Contratos:** Criação da função no frontend para remover contrato sem centro de lucro atrelado. ([#9](http://git.fasolti.com.br/aa/sgi/issues/9)). ([290c404](http://git.fasolti.com.br/aa/sgi/commit/290c404675a1005d2d748a4b9885242770ac9700))
* **Comercial/Contratos:** inicio dos ajustes na list. ([#9](http://git.fasolti.com.br/aa/sgi/issues/9)). ([2d3bfbc](http://git.fasolti.com.br/aa/sgi/commit/2d3bfbc9aa54c8e47abd83ebaeffae93ede0b3ec))
* **Comercial/Contratos:** Ultimos ajustes. ([#9](http://git.fasolti.com.br/aa/sgi/issues/9)). ([5bc31a3](http://git.fasolti.com.br/aa/sgi/commit/5bc31a3e57eccf2149476ad9fbdce08264a15e8c))
* **Comercial/Relatorios:** ajustes de erros de refatoração (SGINV-2529). ([0dc6622](http://git.fasolti.com.br/aa/sgi/commit/0dc6622e31bcb6c1eb645c7ea318dd2beb4cce01))
* **Contas a Receber:** Ajuste para a tela de pesquisa. ([4d9d05d](http://git.fasolti.com.br/aa/sgi/commit/4d9d05dfa8cafa6af20981fdcea0ef8526c875af))
* **Faturamento:** Ajuste Interface (SGINV-2608) ([baecdda](http://git.fasolti.com.br/aa/sgi/commit/baecdda9e0362cf874ce27075f91991c40eae8d5))
* Ajuste na mensagem do campo quantidade de clientes ([e1c7a8c](http://git.fasolti.com.br/aa/sgi/commit/e1c7a8c6c9cd0ab6a9489379d31aab6c10aa3829))
* Ajuste na ordem dos relatórios do faturamento ([9f00c88](http://git.fasolti.com.br/aa/sgi/commit/9f00c8849aeea5f44a3757a42ce573c891003974))
* Ajuste no comportamento dos campos ao selecionar o relatório Duplicatas - auditoria externa. SGINV-2601 ([a307ad5](http://git.fasolti.com.br/aa/sgi/commit/a307ad598d4fbfdb06dafadb8f9a6f1a930ba6e7))
* Ajuste nos tooltips dos campos Cliente da NF e Cliente 'pai' do relatório de contas a receber SGINV-2581 ([bd07801](http://git.fasolti.com.br/aa/sgi/commit/bd078019a4dd85297fd15126b582fca5596eed3f))
* ajustes no comportamento do form de Mapa de vendas SGINV-2511 ([77c5c76](http://git.fasolti.com.br/aa/sgi/commit/77c5c7681a7edc4cd4acbdc9c7a426c7e7f4cbae))
* Correção após testes e refatoração de módulo comercial. ([0af69b2](http://git.fasolti.com.br/aa/sgi/commit/0af69b2d654be1fc919bca31e58ccaa1a1c30138))
* Correção do valor minimo do campo quantidade de cliente de 0 para 1 SGINV-2511 ([e4544cd](http://git.fasolti.com.br/aa/sgi/commit/e4544cd9fde489e42dc38eadbb935e74d025265f))
* Correção do valor minimo do campo quantidade de cliente de 0 para 1 SGINV-2511 ([6b58021](http://git.fasolti.com.br/aa/sgi/commit/6b580216b4d6b374f2273820b47769a406f3e545))
* Faturamento - ajuste da exibição da data de emissão na impressão da NF ([71cce72](http://git.fasolti.com.br/aa/sgi/commit/71cce7260356c0ec616aba0d0a0d73968b9ec860))
* Faturamento - correção na exibição da data de emissão ([20373de](http://git.fasolti.com.br/aa/sgi/commit/20373dec580c4fdb2754af53065e66b08cd001ef))
* **Faturamento:** Ajustes para a Nota Fiscal (SGINV-2587) ([104b2f7](http://git.fasolti.com.br/aa/sgi/commit/104b2f766c4980ac0c2c3f621782aaf58e15c021))
* **Faturamento:** Ajustes para a Nota Fiscal (SGINV-2587) ([3455d99](http://git.fasolti.com.br/aa/sgi/commit/3455d99d57fb3dcb46710e1a0a86204c530133db))
* **Faturamento/Interfaces:** Ajuste de ordem da lista de parcelas pelo numero da nota e ordem das parcelas. (SGINV-2515). ([f395878](http://git.fasolti.com.br/aa/sgi/commit/f39587895b06c39a276b22903b08c38323ac1a74))
* **Tesouraria:** Ajustes para a Relação bancária (SGINV-2588) ([75a2505](http://git.fasolti.com.br/aa/sgi/commit/75a250595625ee3e9570dc58db341d6fb0059a11))
* Faturamento - modificação no update da data de emissão da NF de faturamento ([3385872](http://git.fasolti.com.br/aa/sgi/commit/338587286525e92045ce5fdf83231189833b3b00))

### [5.13.3-rc.1](http://git.fasolti.com.br/aa/sgi/compare/v5.13.2-rc.0...v5.13.3-rc.1) (2019-10-14)


### Bug Fixes

* **Comercial:** Ajustes para edição e criação de PJ e PF (SGINV-2504) ([acf13ac](http://git.fasolti.com.br/aa/sgi/commit/acf13ac))
* **Faturamento:** Ajustes para etapa e nota fiscal (SGINV-2501) ([9935179](http://git.fasolti.com.br/aa/sgi/commit/9935179))
* Ajuste na consulta de CNPJ no cadastro de clientes. ([f910c92](http://git.fasolti.com.br/aa/sgi/commit/f910c92))
* Ajuste na consultarCnpjReceitaFederal no cadastro de clientes. ([6013eb2](http://git.fasolti.com.br/aa/sgi/commit/6013eb2))
* Ajuste na consultarCpfReceitaFederalSoaWebService no cadastro de clientes. ([c735888](http://git.fasolti.com.br/aa/sgi/commit/c735888))


### Features

* **Segunda parte:** Ajustado dados apois converter pessoa fornecedor em cliente (SGINV-2500). ([739688d](http://git.fasolti.com.br/aa/sgi/commit/739688d))

### [5.13.2-rc.0](http://git.fasolti.com.br/aa/sgi/compare/v5.13.2...v5.13.2-rc.0) (2019-10-07)


### Bug Fixes

* Ajustando script para realizar o drop das views. ([24fcf2c](http://git.fasolti.com.br/aa/sgi/commit/24fcf2c))
* Correçao do tributos da nota para receber da  aliquota ou da emissao. ([9f5b9b1](http://git.fasolti.com.br/aa/sgi/commit/9f5b9b1))
* Refatorando codigo para issRetidoRecebimento e impressao de parcela (SGINV-2494). ([331a630](http://git.fasolti.com.br/aa/sgi/commit/331a630))
* **Criaçao de views no final:** Ajustando script para realizar o drop das views. ([c6f60a6](http://git.fasolti.com.br/aa/sgi/commit/c6f60a6))
* Validação de email no cadastro de clientes. ([e99016d](http://git.fasolti.com.br/aa/sgi/commit/e99016d))
* **Correçao de consulta:** Consulta de CNPJ e CPF de cadastro de cliente (SGINV-2500). ([fe58281](http://git.fasolti.com.br/aa/sgi/commit/fe58281))
* **Faturamento:** Campo "Discriminação dos serviços" (SGINV-2499) ([202830e](http://git.fasolti.com.br/aa/sgi/commit/202830e))
* **Faturamento:** Correção do valor do tributo (SGINV-2501) ([e51c849](http://git.fasolti.com.br/aa/sgi/commit/e51c849))
* **Faturamento:** Criação de atributo ValorLiquido em NotaFiscalFaruramentoTO e função calcularValorLiquido, ajustes de styles para class no front (SGINV-2493). ([1490745](http://git.fasolti.com.br/aa/sgi/commit/1490745))
* **Tesouraria:** Ajuste na tela de pesquisa (SGINV-2497). ([8266dd1](http://git.fasolti.com.br/aa/sgi/commit/8266dd1))
* **Tesouraria:** Ajuste na tela de pesquisa (SGINV-2498). ([4d9f269](http://git.fasolti.com.br/aa/sgi/commit/4d9f269))


### Features

* **faturamento/relatorios:** habilitar pesquisa por todas as gerências a depender dos perfis do usuário (SGINV-2491) ([6c49744](http://git.fasolti.com.br/aa/sgi/commit/6c49744))
* **usuario:** gravar data de último acesso após o login (SGINV-2490) ([cda3ce3](http://git.fasolti.com.br/aa/sgi/commit/cda3ce3))
* Ajustado o recebimento para "olhar" a nova coluna de iss retido cliente recebimento (SGINV-2496). ([4d205be](http://git.fasolti.com.br/aa/sgi/commit/4d205be))

### [5.13.1-rc.1](http://git.fasolti.com.br/aa/sgi/compare/v5.13.1-rc.0...v5.13.1-rc.1) (2019-09-06)

### [5.13.1-rc.0](http://git.fasolti.com.br/aa/sgi/compare/v5.13.0...v5.13.1-rc.0) (2019-09-04)

### [5.13.2](http://git.fasolti.com.br/aa/sgi/compare/v5.13.0...v5.13.2) (2019-09-20)


### Bug Fixes

*  Contratos", modo de exibição (SGINV-2461). ([b68b1ca](http://git.fasolti.com.br/aa/sgi/commit/b68b1ca))
* "Comercial > Centros Financeiros", pesquisa/resultado (SGINV-2460). ([6b5fd21](http://git.fasolti.com.br/aa/sgi/commit/6b5fd21))
* Adicionado troca de matriz para as interfaces do faturamento conforme escolha do tipo. ([cc01d56](http://git.fasolti.com.br/aa/sgi/commit/cc01d56))
* Ajustando valor tributos ao receber a parcela. ([1ea4801](http://git.fasolti.com.br/aa/sgi/commit/1ea4801))
* Ajuste na combo de banco no dialog de baixa de retenção da parcela. ([4f67b19](http://git.fasolti.com.br/aa/sgi/commit/4f67b19))
* Ajuste na exibição do botão remover do centro finacneiro. ([8a87a00](http://git.fasolti.com.br/aa/sgi/commit/8a87a00))
* Ajuste nos serviços de contrato do backend. ([b3b68c9](http://git.fasolti.com.br/aa/sgi/commit/b3b68c9))
* Contas a Receber > Parcelas > Exibição: ajustar acesso (SGINV-2465). ([21d20ee](http://git.fasolti.com.br/aa/sgi/commit/21d20ee))
* Correção da diferenças de centavos na parcela. Problema no cálculo vindo da tela ao criar NF. ([349dc0b](http://git.fasolti.com.br/aa/sgi/commit/349dc0b))
* Correção da rentenção tabelas antigas. ([1a7272a](http://git.fasolti.com.br/aa/sgi/commit/1a7272a))
* Correção das validações de data. ([23ec2bb](http://git.fasolti.com.br/aa/sgi/commit/23ec2bb))
* correção do donwload na tela de pesquisa de interfaces do faturamento. ([b18d2af](http://git.fasolti.com.br/aa/sgi/commit/b18d2af))
* Correção do layout da impressão da OS inicial (SGINV-2459). ([481e848](http://git.fasolti.com.br/aa/sgi/commit/481e848))
* Correção do parcelamento da Nota Fiscal. ([6e3d4e6](http://git.fasolti.com.br/aa/sgi/commit/6e3d4e6))
* Correção do valor da parcela ao criar nota fiscal, obtendo o valor líquido sem o tributo. ([76e5779](http://git.fasolti.com.br/aa/sgi/commit/76e5779))
* Correção do valor da parcela quando criado e NF com iss retido. ([3efa6c7](http://git.fasolti.com.br/aa/sgi/commit/3efa6c7))
* Correção no valor default da nota saida parcela e ajuste de líquido ao adicionar retenção. ([645f94c](http://git.fasolti.com.br/aa/sgi/commit/645f94c))
* Corrigidos as retençoes antigas. Sera removido depois, com a sinalizaçao do Marcel. ([d1b3326](http://git.fasolti.com.br/aa/sgi/commit/d1b3326))
* Edição de conta contábil na edição do pagamento na relação bancária. ([a8639f4](http://git.fasolti.com.br/aa/sgi/commit/a8639f4))
* Erro ao salvar uma RB sem os valores do cheque. Agora o backend valida e adiciona os valores dos cheques caso (por algum motivo, não venha do frontend). ([f471d17](http://git.fasolti.com.br/aa/sgi/commit/f471d17))
* Erro ao validar dados bancários ao enviar RB. ([40df67c](http://git.fasolti.com.br/aa/sgi/commit/40df67c))
* Erro no envio da relação bancária. ([7a2e79b](http://git.fasolti.com.br/aa/sgi/commit/7a2e79b))
* Faltou o "centralizar" na coluna "CNPJ/CPF". ([3763a00](http://git.fasolti.com.br/aa/sgi/commit/3763a00))
* Indica se os itens com saldo zerado (retenção totalmente recebida) serão exibidos ou não (SGINV-2447). ([1653209](http://git.fasolti.com.br/aa/sgi/commit/1653209))
* Recalculando o valor líquido da parcela no recebimento (Baixa). ([0d446b9](http://git.fasolti.com.br/aa/sgi/commit/0d446b9))
* Relação Bancária - erro na indicação da linha com dados bancários inativos ([379272c](http://git.fasolti.com.br/aa/sgi/commit/379272c))
* Removendo nota fiscal. ([172cf3a](http://git.fasolti.com.br/aa/sgi/commit/172cf3a))
* Tela de relatórios de faturamento parâmetro de saldo sempre enviado (true/false). ([ac445b9](http://git.fasolti.com.br/aa/sgi/commit/ac445b9))
* Tesouraria > Movimento Autorizado: ajustar ícone (SGINV-2483). ([e32aa97](http://git.fasolti.com.br/aa/sgi/commit/e32aa97))
* Validação de vencimento de boleto na Relação Bancária apenas para internet banking. ([53ffb0e](http://git.fasolti.com.br/aa/sgi/commit/53ffb0e))
* Valor apenas de retenção contratual. ([b37283c](http://git.fasolti.com.br/aa/sgi/commit/b37283c))
* Valor boleto zerado não deve passar na validação de envio da RB. ([59e704f](http://git.fasolti.com.br/aa/sgi/commit/59e704f))
* **Validação de botões:** Faltou o "centralizar" na coluna "CNPJ/CPF". ([b272591](http://git.fasolti.com.br/aa/sgi/commit/b272591))


### Features

* **Boleto Itau:** Faturamento > Interfaces > Exibição: incluir CNPJ (SGINV-2477). ([4898388](http://git.fasolti.com.br/aa/sgi/commit/4898388))
* **Finalizado:** Aba "BAIXA DE RETENÇÃO" (SGINV-2444). ([6201a41](http://git.fasolti.com.br/aa/sgi/commit/6201a41))
* **Prefeituras:** Faturamento > Interfaces > Exibição: incluir CNPJ (SGINV-2477). ([b90adbc](http://git.fasolti.com.br/aa/sgi/commit/b90adbc))
* "Meio de pagamento" = "Cheque", verificar o número do cheque de cada item da RB (SGINV-2476). ([71fc79e](http://git.fasolti.com.br/aa/sgi/commit/71fc79e))
* Atualização dos valores da retenção nas parcelas ao excluir e adicionar baixas. ([285d53a](http://git.fasolti.com.br/aa/sgi/commit/285d53a))
* Comercial > Centros Financeiros > Exibição: ajustar (SGINV-2474). ([ba17be8](http://git.fasolti.com.br/aa/sgi/commit/ba17be8))
* Contas a Receber > Parcelas > Exibição > Aba "Baixa de retenção": ajustar (SGINV-2481). ([d587da1](http://git.fasolti.com.br/aa/sgi/commit/d587da1))
* Faturamento > Relatórios > Tela de filtro (1): ajustar cliente/sub (SGINV-2468). ([e2cf651](http://git.fasolti.com.br/aa/sgi/commit/e2cf651))
* Obtendo cheque conta corrente apenas com status EMITIDO. ([71adc8f](http://git.fasolti.com.br/aa/sgi/commit/71adc8f))
* Substituir o campo "Cliente/Subcliente" de suggestion para texto (SGINV-2470). ([4db0f96](http://git.fasolti.com.br/aa/sgi/commit/4db0f96))



## [5.13.0](http://git.fasolti.com.br/aa/sgi/compare/v5.12.2-rc.3...v5.13.0) (2019-09-03)


### Bug Fixes

* Ajuste do POI no POM dos projetos ([45e7272](http://git.fasolti.com.br/aa/sgi/commit/45e7272))
* Ajustes para a homologação de dados bancários na Relação Bancária (SGINV-2453). ([61b1007](http://git.fasolti.com.br/aa/sgi/commit/61b1007))
* Ajustes para evitar o envio de pagamentos duplicados na relação bancária. ([9e1a108](http://git.fasolti.com.br/aa/sgi/commit/9e1a108))
* Ajustes para evitar o envio de pagamentos duplicados na relação bancária. ([3184d43](http://git.fasolti.com.br/aa/sgi/commit/3184d43))
* Se o usuário possui o perfil "Consulta de faturamento/CAR - Geral", o campo "Gerência" não está como defulat a opção branco (todas as gerências). Está sendo selecionada a gerência atual do usuário (SGINV-2454). ([efecfbc](http://git.fasolti.com.br/aa/sgi/commit/efecfbc))


### Features

* **Baixa Retenção:** Backend criado para baixas. ([67abcc1](http://git.fasolti.com.br/aa/sgi/commit/67abcc1))
* **CRUD criação de telas:** Criação de telas e controllers para Despesas de custos e Previsão de Custos. ([765ee14](http://git.fasolti.com.br/aa/sgi/commit/765ee14))
* **CRUD criação de telas:** Criação de telas e controllers para Despesas de custos e Previsão de Custos. ([696b454](http://git.fasolti.com.br/aa/sgi/commit/696b454))
* **CRUD criação de telas:** Criação de telas e controllers para Despesas de custos e Previsão de Custos. ([ebc770b](http://git.fasolti.com.br/aa/sgi/commit/ebc770b))
* **CRUD criação de telas:** Criação de telas e controllers para Despesas de custos e Previsão de Custos. ([7eee880](http://git.fasolti.com.br/aa/sgi/commit/7eee880))
* **CRUD inicial:** Iniciando CRUD para Despesas de custos e Previsão de Custos. ([f7b9abb](http://git.fasolti.com.br/aa/sgi/commit/f7b9abb))
* **CRUD inicial:** Iniciando CRUD para Despesas de custos e Previsão de Custos. ([cbd96f8](http://git.fasolti.com.br/aa/sgi/commit/cbd96f8))
* **CRUD inicial:** Iniciando CRUD para Despesas de custos e Previsão de Custos. ([d2d4bb8](http://git.fasolti.com.br/aa/sgi/commit/d2d4bb8))
* **CRUD inicial:** Iniciando CRUD para Despesas de custos e Previsão de Custos. ([dc7204a](http://git.fasolti.com.br/aa/sgi/commit/dc7204a))
* Add identificação da linha com dados bancários inativos na relação bacária (SGINV-2452). ([4c281c4](http://git.fasolti.com.br/aa/sgi/commit/4c281c4))
* **Validação de planilha:** Upload de excel custos. ([dc7e86e](http://git.fasolti.com.br/aa/sgi/commit/dc7e86e))
* Add titulo para motivo de retorno na aba de cancelamento da tela de exibição de Parcela de Contas a Receber. ([44476b7](http://git.fasolti.com.br/aa/sgi/commit/44476b7))
* Add titulo para motivo de retorno na aba de cancelamento da tela de exibição de Parcela de Contas a Receber. ([5b2d1b0](http://git.fasolti.com.br/aa/sgi/commit/5b2d1b0))
* Ajustes para a tela de impressão de OS Inicial do Centro Financeiro. ([758589e](http://git.fasolti.com.br/aa/sgi/commit/758589e))
* Ajustes para a validação do envio da Relação bancária (SGINV-2452). ([0dd3ea8](http://git.fasolti.com.br/aa/sgi/commit/0dd3ea8))
* Ajustes para os campos 'Tipo' e 'Matriz / Filial' da tela de pesquisa de Lote. ([115b83d](http://git.fasolti.com.br/aa/sgi/commit/115b83d))
* Ajustes para os campos 'Tipo' e 'Matriz / Filial' da tela de pesquisa de Lote. ([c6335da](http://git.fasolti.com.br/aa/sgi/commit/c6335da))
* **Validação de planilha:** Upload de excel custos. ([6cff0ff](http://git.fasolti.com.br/aa/sgi/commit/6cff0ff))
* Incluir aba "BAIXA DE RETENÇÃO" (SGINV-2444). ([e175ae7](http://git.fasolti.com.br/aa/sgi/commit/e175ae7))
* **Custos:** Ajustes no gráfico de comparativo anual e nova tela de relatórios de custos. ([f0e168a](http://git.fasolti.com.br/aa/sgi/commit/f0e168a))
* **Finalizado:** CRUD de Rubricas (Despesa de custos). ([d1b891d](http://git.fasolti.com.br/aa/sgi/commit/d1b891d))
* **Finalizado:** CRUD de Rubricas (Despesa de custos). ([0742c2e](http://git.fasolti.com.br/aa/sgi/commit/0742c2e))
* **Finalizado:** CRUD de Rubricas (Despesa de custos). ([ec36a04](http://git.fasolti.com.br/aa/sgi/commit/ec36a04))
* **Finalizado:** CRUD de Rubricas (Despesa de custos). ([5f68a0f](http://git.fasolti.com.br/aa/sgi/commit/5f68a0f))
* **Importação da tela:** Importação de orçamento empresarial via upload. ([a7011c5](http://git.fasolti.com.br/aa/sgi/commit/a7011c5))
* **Importação da tela:** Importação de orçamento empresarial via upload. ([4bb94d0](http://git.fasolti.com.br/aa/sgi/commit/4bb94d0))
* **Inicial:** Importação de orçamento empresarial. ([cfb35e0](http://git.fasolti.com.br/aa/sgi/commit/cfb35e0))
* **Inicial:** Importação de orçamento empresarial. ([b14fc7f](http://git.fasolti.com.br/aa/sgi/commit/b14fc7f))
* **Novo cadastro:** Tela de novo orçamento empresarial. ([f0e401f](http://git.fasolti.com.br/aa/sgi/commit/f0e401f))
* **Novo cadastro:** Tela de novo orçamento empresarial. ([ba60a74](http://git.fasolti.com.br/aa/sgi/commit/ba60a74))
* Novos campos na entidade NotaSaidaParcela. ([b94c15f](http://git.fasolti.com.br/aa/sgi/commit/b94c15f))
* **Pequisa/resultado:** Pesquisa e resultado para despesa de custos. ([53c498c](http://git.fasolti.com.br/aa/sgi/commit/53c498c))
* **Pequisa/resultado:** Pesquisa e resultado para despesa de custos. ([59576f0](http://git.fasolti.com.br/aa/sgi/commit/59576f0))
* **Pequisa/resultado:** Pesquisa e resultado para despesa de custos. ([16bf4f2](http://git.fasolti.com.br/aa/sgi/commit/16bf4f2))
* **Pequisa/resultado:** Pesquisa e resultado para despesa de custos. ([caa6184](http://git.fasolti.com.br/aa/sgi/commit/caa6184))
* **Segunda parte:** Refatoração da estrutura das parcelas. ([dab593f](http://git.fasolti.com.br/aa/sgi/commit/dab593f))
* **Updaload Excel:** Finalizado o upload do excel. ([f15cf25](http://git.fasolti.com.br/aa/sgi/commit/f15cf25))
* **Updaload Excel:** Finalizado o upload do excel. ([deac1ac](http://git.fasolti.com.br/aa/sgi/commit/deac1ac))
* Refatoração da estrutura das parcelas. ([5e1f1bb](http://git.fasolti.com.br/aa/sgi/commit/5e1f1bb))



### [5.12.2-rc.3](http://git.fasolti.com.br/aa/sgi/compare/v5.12.2-rc.1...v5.12.2-rc.3) (2019-08-30)


### Bug Fixes

* Ajustando valor total na tela de pesquisa de relaçao bancaria. ([826e75b](http://git.fasolti.com.br/aa/sgi/commit/826e75b))
* Ajuste da combo de gerências quando há perfis. ([ee66c62](http://git.fasolti.com.br/aa/sgi/commit/ee66c62))
* Ajuste no layout do botão nova relação bancária. ([70c933d](http://git.fasolti.com.br/aa/sgi/commit/70c933d))
* Ajustes para aba de Reajuste da tela de centro financeiro. ([ce66eaf](http://git.fasolti.com.br/aa/sgi/commit/ce66eaf))
* Ajustes para o ContratoTO. ([c4fc727](http://git.fasolti.com.br/aa/sgi/commit/c4fc727))
* Ajustes pós homologação (SGINV-2428) ([09efc50](http://git.fasolti.com.br/aa/sgi/commit/09efc50))
* All itens issue (SGINV-2429) ([c3c7c7d](http://git.fasolti.com.br/aa/sgi/commit/c3c7c7d))
* Calculo Mensal ([32de408](http://git.fasolti.com.br/aa/sgi/commit/32de408))
* Calculo Valor Mensal e Valor total ([895822e](http://git.fasolti.com.br/aa/sgi/commit/895822e))
* Correçao do cheque anterior - Tesouraria. ([74168fa](http://git.fasolti.com.br/aa/sgi/commit/74168fa))
* Correçao no NOVO FECHO ao atualizar o BDI nos ajustes de cada item adicionado novo. ([174fd08](http://git.fasolti.com.br/aa/sgi/commit/174fd08))
* Item da combobox Conta Contábil (SGINV-2430) ([47a8641](http://git.fasolti.com.br/aa/sgi/commit/47a8641))
* Voltando a coluna DATA_PRIMEIRA_PARCELA para o contrato. ([2686a1a](http://git.fasolti.com.br/aa/sgi/commit/2686a1a))


### Features

* Add funcionalidades da terceira linha da tela de pesquisa do cliente (SGINV-2430). ([af8b545](http://git.fasolti.com.br/aa/sgi/commit/af8b545))
* Add verificações para descrição da etapa do faturamento (SGINV-2433). ([5c94013](http://git.fasolti.com.br/aa/sgi/commit/5c94013))
* Ajustes para a tela de impressão de OS Inicial do Centro Financeiro. ([e8aa7ef](http://git.fasolti.com.br/aa/sgi/commit/e8aa7ef))
* Ajustes para o botão Reativar do Centro Financeiro. ([9703efc](http://git.fasolti.com.br/aa/sgi/commit/9703efc))
* Ajustes para o Encerramento do Centro Financeiro. ([fa4761a](http://git.fasolti.com.br/aa/sgi/commit/fa4761a))
* Ajustes para os campos de Centro Financeiro e Contrato. E ajustes para o encerramento do Centro Financeiro. ([32cd861](http://git.fasolti.com.br/aa/sgi/commit/32cd861))
* Atualização de toda a tela do Contrato. ([343fab9](http://git.fasolti.com.br/aa/sgi/commit/343fab9))
* Atualização do AngularJS Material 1.1.20 (2019-08-15). ([fae7323](http://git.fasolti.com.br/aa/sgi/commit/fae7323))
* Divisão da tela de contrato usando include. ([b78eab1](http://git.fasolti.com.br/aa/sgi/commit/b78eab1))
* Exibir todos os bancos no cadastro de conta corrente. ([89a514a](http://git.fasolti.com.br/aa/sgi/commit/89a514a))
* **Comercial:** Pequenos ajustes e refatoração. ([1eec69c](http://git.fasolti.com.br/aa/sgi/commit/1eec69c))
* FrontEnd Pesquisa Cliente (SGINV-2430) ([0dac117](http://git.fasolti.com.br/aa/sgi/commit/0dac117))



### [5.12.2-rc.1](http://git.fasolti.com.br/aa/sgi/compare/v5.12.2-rc.0...v5.12.2-rc.1) (2019-08-16)


### Bug Fixes

* Ajuste do arredondamento do bdi. ([dc20b51](http://git.fasolti.com.br/aa/sgi/commit/dc20b51))
* Ajuste em excluir Contato e Telefone (SGINV-2429) ([6165ddc](http://git.fasolti.com.br/aa/sgi/commit/6165ddc))
* Ajuste para criação/alteração de fecho, na aba fecho da tela de centro financeiro. ([863b91f](http://git.fasolti.com.br/aa/sgi/commit/863b91f))
* Ajustes para as assinaturas do centro financeiro (SGINV-2410). ([84dbe63](http://git.fasolti.com.br/aa/sgi/commit/84dbe63))
* Correção na edição de centro financeiro. ([8dfe630](http://git.fasolti.com.br/aa/sgi/commit/8dfe630))
* EFD - ajuste do formato do campo de data de emissão nos arquivos gerados para envio. ([479a023](http://git.fasolti.com.br/aa/sgi/commit/479a023))
* Item 1 (SGINV-2427) ([0e9a60a](http://git.fasolti.com.br/aa/sgi/commit/0e9a60a))
* Item 1 e separação do list do detail (SGINV-2428) ([ef5ed15](http://git.fasolti.com.br/aa/sgi/commit/ef5ed15))
* Item 6 (SGINV-2428) ([24b43de](http://git.fasolti.com.br/aa/sgi/commit/24b43de))
* Itens 3 e 4 (SGINV-2428) ([0b30e6c](http://git.fasolti.com.br/aa/sgi/commit/0b30e6c))
* Organização das abas de Cliente. ([121a8a1](http://git.fasolti.com.br/aa/sgi/commit/121a8a1))
* Salvar, editar e remover pessoa conta contabil (SGINV-2428) ([39c293a](http://git.fasolti.com.br/aa/sgi/commit/39c293a))


### Features

* Adicionar protocolo de entrega na pesquisa de nota da saída. ([f1f0e4c](http://git.fasolti.com.br/aa/sgi/commit/f1f0e4c))
* **Ajuste:** Menu guardado localmente, e so traz quando feito logout. ([1b514ea](http://git.fasolti.com.br/aa/sgi/commit/1b514ea))
* Ajustes no comercial (Fecho). ([92676ce](http://git.fasolti.com.br/aa/sgi/commit/92676ce))
* Cancelamento para os status do Faturamento com verificação de etapas e notas fiscais. ([aa83779](http://git.fasolti.com.br/aa/sgi/commit/aa83779))
* Colocando premissão por perfil no ícone de adicionar protocolo na tela de pesquisa de nota fiscal. ([caf71de](http://git.fasolti.com.br/aa/sgi/commit/caf71de))
* Criação do Fecho. ([bac5f96](http://git.fasolti.com.br/aa/sgi/commit/bac5f96))
* Função para Calcular o Prazo do Contrato. ([125363f](http://git.fasolti.com.br/aa/sgi/commit/125363f))
* Item 2 (SGINV-2428) ([8406784](http://git.fasolti.com.br/aa/sgi/commit/8406784))
* Menu guardado localmente, e so traz quando feito logout. ([5fb3de5](http://git.fasolti.com.br/aa/sgi/commit/5fb3de5))
* Nova estrutura para o módulo de Notas Fiscais. ([c1efeb8](http://git.fasolti.com.br/aa/sgi/commit/c1efeb8))



### [5.12.2-rc.0](http://git.fasolti.com.br/aa/sgi/compare/v5.12.1...v5.12.2-rc.0) (2019-08-12)


### Bug Fixes

* Ajuste para a exibição do Contrato, pro campo 'Cadastramento' da aba de controles. ([3731f65](http://git.fasolti.com.br/aa/sgi/commit/3731f65))
* Calculos de BDI e Tributos do Fecho. ([72b851b](http://git.fasolti.com.br/aa/sgi/commit/72b851b))
* Correção de erro na tela do centro financeiro (SGINV-2417). ([81202d0](http://git.fasolti.com.br/aa/sgi/commit/81202d0))
* Correção de erro na tela do centro financeiro (SGINV-2417). ([aa782f2](http://git.fasolti.com.br/aa/sgi/commit/aa782f2))
* Tela de Relatórios filtro classificação do cliente(SGINV-2425) ([04fdc40](http://git.fasolti.com.br/aa/sgi/commit/04fdc40))


### Features

* Ajustes de layout ([d7179f7](http://git.fasolti.com.br/aa/sgi/commit/d7179f7))



### [5.12.1](http://git.fasolti.com.br/aa/sgi/compare/v5.12.1-rc.3...v5.12.1) (2019-08-08)


### Bug Fixes

* Ajuste na função de consulta de CLs ativos no Faturamento (SGINV-2420) ([92a6fd8](http://git.fasolti.com.br/aa/sgi/commit/92a6fd8))
* Ajuste para aba de tributos do fecho do centro financeiro (SGINV-2417). ([f5a9270](http://git.fasolti.com.br/aa/sgi/commit/f5a9270))
* Comercial -> Clientes -> Novo :: Erro ao obter tipo logradouro quando pesquisado endereço do cliente no webservice. ([75c48f1](http://git.fasolti.com.br/aa/sgi/commit/75c48f1))
* Pequeno ajuste para exibir contador de caracteres (SGINV-2420) ([8c9429b](http://git.fasolti.com.br/aa/sgi/commit/8c9429b))
* Removendo pasta .idea. ([7b57c49](http://git.fasolti.com.br/aa/sgi/commit/7b57c49))
* Removendo pasta .idea. ([4873eaa](http://git.fasolti.com.br/aa/sgi/commit/4873eaa))
* Removido código de carregamento de aliquota em categoria. ([dfe4242](http://git.fasolti.com.br/aa/sgi/commit/dfe4242))


### Features

* Ajustes para tela de Contrato (SGINV-2419). ([121ef95](http://git.fasolti.com.br/aa/sgi/commit/121ef95))
* Item 2 - Mudança de layout da aba Disriminação do serviço  (SGINV-2420) ([22e4987](http://git.fasolti.com.br/aa/sgi/commit/22e4987))



### [5.12.1-rc.1](http://git.fasolti.com.br/aa/sgi/compare/v5.12.0-rc.0...v5.12.1-rc.1) (2019-07-29)



## [5.12.0-rc.0](http://git.fasolti.com.br/aa/sgi/compare/v5.11.21-rc.0...v5.12.0-rc.0) (2019-07-24)


### Bug Fixes

* Add anotações para Unidade. ([98ee8ec](http://git.fasolti.com.br/aa/sgi/commit/98ee8ec))
* Ajsute de remoção de conta corrente quando há cheque associado. ([b4e55ea](http://git.fasolti.com.br/aa/sgi/commit/b4e55ea))
* Ajsutes após merge do Master > Development. ([d4877c2](http://git.fasolti.com.br/aa/sgi/commit/d4877c2))
* Ajuste na remoção de historicoConciliacao. ([34c6cce](http://git.fasolti.com.br/aa/sgi/commit/34c6cce))
* Ajuste na tela Contratos - Filtros de pesquisa. ([fd7b776](http://git.fasolti.com.br/aa/sgi/commit/fd7b776))
* Ajuste para os botões de paginação da tela de pesquisa dos Bancos. ([0b0957a](http://git.fasolti.com.br/aa/sgi/commit/0b0957a))
* Ajuste para tela de edição e exibição da Conta Corrente da Tesouraria. ([72f824c](http://git.fasolti.com.br/aa/sgi/commit/72f824c))
* Ajuste para tela de edição e exibição da previsão do Fluxo de Caixa. ([ed56010](http://git.fasolti.com.br/aa/sgi/commit/ed56010))
* Ajuste para tela de Funcionalidades da Administração. ([6441cf6](http://git.fasolti.com.br/aa/sgi/commit/6441cf6))
* Ajustes após merge. ([098a88c](http://git.fasolti.com.br/aa/sgi/commit/098a88c))
* Ajustes css no menu dinâmico. ([f2f3d24](http://git.fasolti.com.br/aa/sgi/commit/f2f3d24))
* Ajustes no menu dinâmico. ([9fc5bb7](http://git.fasolti.com.br/aa/sgi/commit/9fc5bb7))
* Ajustes para a criação da Conta corrente. ([62fbf8c](http://git.fasolti.com.br/aa/sgi/commit/62fbf8c))
* Ajustes para corrigir erro de dependência e injeção de emailservice. ([69d62cc](http://git.fasolti.com.br/aa/sgi/commit/69d62cc))
* Ajustes para exibir itens de tesouraria que ficaram faltando. ([35efd76](http://git.fasolti.com.br/aa/sgi/commit/35efd76))
* configuração AMQP ([100fc2d](http://git.fasolti.com.br/aa/sgi/commit/100fc2d))
* Configuraçao do CAS ([2c51fd4](http://git.fasolti.com.br/aa/sgi/commit/2c51fd4))
* Correção da busca de cidades do contrato. ([f53f621](http://git.fasolti.com.br/aa/sgi/commit/f53f621))
* Correção da paginação das abas de etapas e nota fiscal do faturamento. ([ddb0091](http://git.fasolti.com.br/aa/sgi/commit/ddb0091))
* Correção da remoção de uma nota fiscal de um faturamento. ([a150db2](http://git.fasolti.com.br/aa/sgi/commit/a150db2))
* Correção das rotas de algumas telas após merge. ([7ab45a2](http://git.fasolti.com.br/aa/sgi/commit/7ab45a2))
* Correção do CSS da aplicação. ([221141b](http://git.fasolti.com.br/aa/sgi/commit/221141b))
* Correção do erro do jpaQuery.fetchOne. ([787f7ae](http://git.fasolti.com.br/aa/sgi/commit/787f7ae))
* Correção dos tributos do faturamento. ([d17b4b2](http://git.fasolti.com.br/aa/sgi/commit/d17b4b2))
* Correção na tela de pesquisa de obras. ([3d1d6ef](http://git.fasolti.com.br/aa/sgi/commit/3d1d6ef))
* Correção no cadastro de categoria de contrato. ([0e6b99d](http://git.fasolti.com.br/aa/sgi/commit/0e6b99d))
* Correção no cancelamento da criação de parcela e atualização de pacotes de dependências do POM. ([2a5a48d](http://git.fasolti.com.br/aa/sgi/commit/2a5a48d))
* erro de sintaxe ([7ff2d5b](http://git.fasolti.com.br/aa/sgi/commit/7ff2d5b))
* erro de sintaxe ([65e9fab](http://git.fasolti.com.br/aa/sgi/commit/65e9fab))
* erro de sintaxe ([c5dee9b](http://git.fasolti.com.br/aa/sgi/commit/c5dee9b))
* erros de sintaxe durante merge do master ([7c0b058](http://git.fasolti.com.br/aa/sgi/commit/7c0b058))
* geração do fat jar do sgi-jobs, configurações do ehcache.xml ([f30a618](http://git.fasolti.com.br/aa/sgi/commit/f30a618))
* **chore; deploy:** mover configuração de dataSource local (não-JNDI) para o único projeto que utiliza-o ([391d7ba](http://git.fasolti.com.br/aa/sgi/commit/391d7ba))
* Refatoração do frontend. ([a3b61c5](http://git.fasolti.com.br/aa/sgi/commit/a3b61c5))
* Refatoração do frontend. ([875f084](http://git.fasolti.com.br/aa/sgi/commit/875f084))
* Refatoração do pacote inrf.domain. ([ea81bc7](http://git.fasolti.com.br/aa/sgi/commit/ea81bc7))
* Refatoração do pacote Tesouraria. ([309fef9](http://git.fasolti.com.br/aa/sgi/commit/309fef9))
* Refatoração dos Resources. ([fc51abf](http://git.fasolti.com.br/aa/sgi/commit/fc51abf))
* Refatoração dos Resources. ([e7f7ec1](http://git.fasolti.com.br/aa/sgi/commit/e7f7ec1))
* Refatoração no frontend. ([cdfc940](http://git.fasolti.com.br/aa/sgi/commit/cdfc940))
* Refatoração no frontend. ([400e7c6](http://git.fasolti.com.br/aa/sgi/commit/400e7c6))
* Refatoração no frontend. ([4a118f2](http://git.fasolti.com.br/aa/sgi/commit/4a118f2))
* Refatorações no frontend e backend. ([06fc4df](http://git.fasolti.com.br/aa/sgi/commit/06fc4df))
* **chore, servers:** fixes para execução conjunta dos servidores frontend e jobs ([1e283df](http://git.fasolti.com.br/aa/sgi/commit/1e283df))
* **chore; deploy:** impedir exportação MBean (via Spring) do recurso JNDI externo "dataSource" ([b4bd96a](http://git.fasolti.com.br/aa/sgi/commit/b4bd96a))
* **merge:** Ajustes após merge. ([67d22d0](http://git.fasolti.com.br/aa/sgi/commit/67d22d0))


### Features

* Adicionada possibilidade de obter configurações de System.getProperty ([6db88f3](http://git.fasolti.com.br/aa/sgi/commit/6db88f3))
* Ajuste após merge. ([61e74a4](http://git.fasolti.com.br/aa/sgi/commit/61e74a4))
* Ajuste após merge. ([6aed947](http://git.fasolti.com.br/aa/sgi/commit/6aed947))
* Ajuste após merge. ([71a2375](http://git.fasolti.com.br/aa/sgi/commit/71a2375))
* Ajuste após merge. ([675e2ad](http://git.fasolti.com.br/aa/sgi/commit/675e2ad))
* Ajuste após merge. ([5c03de3](http://git.fasolti.com.br/aa/sgi/commit/5c03de3))
* Ajuste da combo da tela de movimento autorizado boleto. ([2fbaad5](http://git.fasolti.com.br/aa/sgi/commit/2fbaad5))
* Ajuste dependências da aplicação. ([3fc3d6b](http://git.fasolti.com.br/aa/sgi/commit/3fc3d6b))
* Ajuste na pesquisa de pagamentos da relação bancária. ([031b02e](http://git.fasolti.com.br/aa/sgi/commit/031b02e))
* Ajuste parte de cheques da relação bancária. ([999c880](http://git.fasolti.com.br/aa/sgi/commit/999c880))
* Ajustes após merge. ([22720c9](http://git.fasolti.com.br/aa/sgi/commit/22720c9))
* Ajustes no maven-checkstyle-plugin. ([d05e4e5](http://git.fasolti.com.br/aa/sgi/commit/d05e4e5))
* Correção após merge. ([bf7ea88](http://git.fasolti.com.br/aa/sgi/commit/bf7ea88))
* **Framework:** Refatorando projeto para a versão 5.12. ([1949c83](http://git.fasolti.com.br/aa/sgi/commit/1949c83))
* Ajustes para a versão commons 2.0.x. ([5bff51f](http://git.fasolti.com.br/aa/sgi/commit/5bff51f))
* Atualização após merge 5.11.20. ([977d340](http://git.fasolti.com.br/aa/sgi/commit/977d340))
* Atualização de pacote ([203b3c0](http://git.fasolti.com.br/aa/sgi/commit/203b3c0))
* Atualizaçao para o Fasolti Commons 2.0.2 ([fd82746](http://git.fasolti.com.br/aa/sgi/commit/fd82746))
* Configuraçao do RabbitMQ em email-service ([cc0a0c2](http://git.fasolti.com.br/aa/sgi/commit/cc0a0c2))
* Correção após merge. ([3975435](http://git.fasolti.com.br/aa/sgi/commit/3975435))
* Correção das mensagem de validação da aplicação com o novo framework. ([5f35f7e](http://git.fasolti.com.br/aa/sgi/commit/5f35f7e))
* Correção do cadastro de conta corrente. ([9b96242](http://git.fasolti.com.br/aa/sgi/commit/9b96242))
* Correção no carregamento da Relação Bancária. ([5c985e0](http://git.fasolti.com.br/aa/sgi/commit/5c985e0))
* Finalizada ajuste na Relação Bancária. ([324cc4b](http://git.fasolti.com.br/aa/sgi/commit/324cc4b))
* InativacaoUsuarioJob, correções no envio de e-mail, etc ([a29f8ec](http://git.fasolti.com.br/aa/sgi/commit/a29f8ec))
* InativacaoUsuarioJob, melhorias nas dependências entre beans, etc. ([a3f0ba0](http://git.fasolti.com.br/aa/sgi/commit/a3f0ba0))
* jobs-seervice, correções para o Java 8/Spring Boot 2, etc ([8238f54](http://git.fasolti.com.br/aa/sgi/commit/8238f54))
* **Parte 01:** Ajustando aplicação para rodar junto com os jobs. ([e730e92](http://git.fasolti.com.br/aa/sgi/commit/e730e92))
* Progress-linear ao carregar menu via banco de dados. ([8b126f8](http://git.fasolti.com.br/aa/sgi/commit/8b126f8))
* **Atualização:** Ajustes no maven-checkstyle-plugin. ([f25c097](http://git.fasolti.com.br/aa/sgi/commit/f25c097))
* **Atualização 02:** Ajustes no maven-checkstyle-plugin. ([60cde40](http://git.fasolti.com.br/aa/sgi/commit/60cde40))
* Refatorando classes de serviço. ([a46b6b8](http://git.fasolti.com.br/aa/sgi/commit/a46b6b8))
* **Atualização 03:** Ajustes no maven-checkstyle-plugin. ([ee49703](http://git.fasolti.com.br/aa/sgi/commit/ee49703))
* **Atualização 04:** Ajustes no maven-checkstyle-plugin. ([1b03d36](http://git.fasolti.com.br/aa/sgi/commit/1b03d36))
* Refatorando nota fiscal. ([023dd2b](http://git.fasolti.com.br/aa/sgi/commit/023dd2b))
* **Atualização 05:** Ajustes no maven-checkstyle-plugin. ([70949f5](http://git.fasolti.com.br/aa/sgi/commit/70949f5))
* Refatorando classes do projeto Domain. ([b999c76](http://git.fasolti.com.br/aa/sgi/commit/b999c76))
* Usando Custom Error Message Handling for REST API. ([edcfe2a](http://git.fasolti.com.br/aa/sgi/commit/edcfe2a))
* Uso dos delimitadores do Spring Boot em /src/main/resources e similares, etc ([053fc59](http://git.fasolti.com.br/aa/sgi/commit/053fc59))
* **Atualização 06:** Ajustes no maven-checkstyle-plugin. ([228e9f8](http://git.fasolti.com.br/aa/sgi/commit/228e9f8))
* **Atualização 07:** Ajustes no maven-checkstyle-plugin. ([e38816a](http://git.fasolti.com.br/aa/sgi/commit/e38816a))
* **Parte 02:** Correção após merge. ([60b08b1](http://git.fasolti.com.br/aa/sgi/commit/60b08b1))
* **Parte 03:** Correção após merge. ([e9ca0fc](http://git.fasolti.com.br/aa/sgi/commit/e9ca0fc))
* **Parte 04:** Correção após merge. ([bf81401](http://git.fasolti.com.br/aa/sgi/commit/bf81401))



### [5.11.20-rc.1](http://git.fasolti.com.br/aa/sgi/compare/v5.11.20-rc.0...v5.11.20-rc.1) (2019-07-10)


### Bug Fixes

* Ajustando troca de banco dos dados bancários. ([5041c83](http://git.fasolti.com.br/aa/sgi/commit/5041c83))
* Ajuste da unidade de Nota Fiscal (SGINV-2320) ([a9e9c15](http://git.fasolti.com.br/aa/sgi/commit/a9e9c15))
* Ajuste de cálculo de parcelamento (SGINV-2340). ([70bacd7](http://git.fasolti.com.br/aa/sgi/commit/70bacd7))
* Ajuste na discriminação ao criar etapa. ([d871266](http://git.fasolti.com.br/aa/sgi/commit/d871266))
* Ajuste no calculo do valor liquido da parcela da nota fiscal. ([d64e0e8](http://git.fasolti.com.br/aa/sgi/commit/d64e0e8))
* Ajuste para corrigir o erro de calculo do valor liquido de diversas telas (SGINV-2326). ([da5f1b8](http://git.fasolti.com.br/aa/sgi/commit/da5f1b8))
* Ajuste para o campo 'Meio de pagamento' na tela de edição de Relação Bancária (SGINV-2325). ([a5dd1e7](http://git.fasolti.com.br/aa/sgi/commit/a5dd1e7))
* Ajuste para o campo 'Praça de pagamento' da aba de faturamento da nota fiscal. ([576b3bf](http://git.fasolti.com.br/aa/sgi/commit/576b3bf))
* Ajustes na validação de realizar baixa bancária. ([94bb784](http://git.fasolti.com.br/aa/sgi/commit/94bb784))
* Ajustes no "zerar" do tributo da parcela. ([1b75dc7](http://git.fasolti.com.br/aa/sgi/commit/1b75dc7))
* Ajustes no campo tipo na tela de pesquisa e edição da interface do faturamento (SGINV-2332, SGINV-2334). ([94563ea](http://git.fasolti.com.br/aa/sgi/commit/94563ea))
* Ajustes no total do calculo da tabela de baixa bancária. ([0c8b610](http://git.fasolti.com.br/aa/sgi/commit/0c8b610))
* Ajustes no valor liquido da parcela (SGINV-2345). ([d87feb7](http://git.fasolti.com.br/aa/sgi/commit/d87feb7))
* Ajustes no valor liquido da parcela (SGINV-2345). ([1d22ca5](http://git.fasolti.com.br/aa/sgi/commit/1d22ca5))
* Ajustes para o botão de Receber da tela de parcelas (SGINV-2287). ([a8550a8](http://git.fasolti.com.br/aa/sgi/commit/a8550a8))
* Ajustes para o campo Cidade ISS da aba de tributos da nota fiscal (SGINV-2322). ([350b29d](http://git.fasolti.com.br/aa/sgi/commit/350b29d))
* Ajustes para os campos de lote da aba de controle da nota fiscal (SGINV-2349). ([dac0bf1](http://git.fasolti.com.br/aa/sgi/commit/dac0bf1))
* Ajustes para relação bancária (SGINV-2351). ([704d1c2](http://git.fasolti.com.br/aa/sgi/commit/704d1c2))
* Ajustes para relação bancária (SGINV-2351). ([e15ea7f](http://git.fasolti.com.br/aa/sgi/commit/e15ea7f))
* Ao exibir dialog de "Zerar" tributos, ao confirmar, a descriminação recebe o valor de ISS mesmo que a NF seja de não retenção de ISS (SGINV-2344). ([d86c4af](http://git.fasolti.com.br/aa/sgi/commit/d86c4af))
* Cannot read property 'numero' of undefined at notasFiscais.detail.controller.js:233. ([4bae9d7](http://git.fasolti.com.br/aa/sgi/commit/4bae9d7))
* Complementação da homologação (SGINV-2282) ([f3f0667](http://git.fasolti.com.br/aa/sgi/commit/f3f0667))
* Correção do percentual ao editar tributos de uma nota fiscal. ([5f67ba2](http://git.fasolti.com.br/aa/sgi/commit/5f67ba2))
* Correção na exibição dos serviços de uma nota fiscal. ([d76c57e](http://git.fasolti.com.br/aa/sgi/commit/d76c57e))
* Correção nas horas da data de contabilização no recebimento do pagamento. ([f672b39](http://git.fasolti.com.br/aa/sgi/commit/f672b39))
* Corrigido a data de pagamento da relação bancária. ([f46f34c](http://git.fasolti.com.br/aa/sgi/commit/f46f34c))
* Dailog Dados Bancarios (SGINV-2327) ([e2c557b](http://git.fasolti.com.br/aa/sgi/commit/e2c557b))
* Edição de Pagamento - salvar dados bancarios (SGINV-2324) ([1ff626d](http://git.fasolti.com.br/aa/sgi/commit/1ff626d))
* Item 1 - Style de Opções (SGINV-2321) ([859cc9a](http://git.fasolti.com.br/aa/sgi/commit/859cc9a))
* Layout das telas e item 2 (SGINV-2321) ([751fe0b](http://git.fasolti.com.br/aa/sgi/commit/751fe0b))
* **Parte 02:** Ajuste de cálculo de parcelamento (SGINV-2340). ([9b7e904](http://git.fasolti.com.br/aa/sgi/commit/9b7e904))
* Na edição da relação bancária e no valor final do parcelamento da NF. ([dcda22b](http://git.fasolti.com.br/aa/sgi/commit/dcda22b))
* Os serviços do Faturamento não levaram em consideração o  Habilitado com cidade. ([c7502da](http://git.fasolti.com.br/aa/sgi/commit/c7502da))
* Pagamento em cheques não estavam sendo exibidos. ([1e83a62](http://git.fasolti.com.br/aa/sgi/commit/1e83a62))
* Pequenos ajustes (SGINV-2283) ([0af5396](http://git.fasolti.com.br/aa/sgi/commit/0af5396))
* Style ([8bfa143](http://git.fasolti.com.br/aa/sgi/commit/8bfa143))
* Style Botão Homologação (SGINV-2321) ([4424f03](http://git.fasolti.com.br/aa/sgi/commit/4424f03))
* Voltando o cálculo da etapa para não receber valor de tributos do faturamento. ([7975ae2](http://git.fasolti.com.br/aa/sgi/commit/7975ae2))


### Features

* Add função de zerar tributos na edição da nota fiscal (SGINV-2318). ([2389273](http://git.fasolti.com.br/aa/sgi/commit/2389273))
* Add novos lotes interfaces (SGINV-2332, SGINV-2334, SGINV-2333). ([871e8f9](http://git.fasolti.com.br/aa/sgi/commit/871e8f9))
* Add track by na combo de nome do relatorio ([c2f47b4](http://git.fasolti.com.br/aa/sgi/commit/c2f47b4))
* Ajuste na edição das parcelas da nota fiscal (SGINV-2340). ([e09db14](http://git.fasolti.com.br/aa/sgi/commit/e09db14))
* Ajuste no dialog da tela de exibição da Relação Bancária (SGINV-2319). ([1d09db5](http://git.fasolti.com.br/aa/sgi/commit/1d09db5))
* Faltou a verificação por feriado (SGINV-2317). ([3d44c20](http://git.fasolti.com.br/aa/sgi/commit/3d44c20))
* Faturamento exibicao - Novos campos (SGINV-2341) ([91675c4](http://git.fasolti.com.br/aa/sgi/commit/91675c4))
* Item 1 - Add novo relatorio (SGINV-2337) ([fed0c3d](http://git.fasolti.com.br/aa/sgi/commit/fed0c3d))
* Item 2 - Separacao entre opcoes (SGINV-2337) ([9dba118](http://git.fasolti.com.br/aa/sgi/commit/9dba118))
* Novo ícone para homologação de dados bancários do movimento autorizado. ([458b618](http://git.fasolti.com.br/aa/sgi/commit/458b618))
* Novo ícone para zerar valores dos tributos. ([93fc1bf](http://git.fasolti.com.br/aa/sgi/commit/93fc1bf))
* Para o "Meio de pagamento" = "Cheque", manter a ordem de numeração dos cheques (SGINV-2343). ([9a30547](http://git.fasolti.com.br/aa/sgi/commit/9a30547))
* Pequenos ajustes cadastro Bancos (SGINV-2279). ([86102ba](http://git.fasolti.com.br/aa/sgi/commit/86102ba))
* Recolocando botão de 'Confirmar cadastro'. ([0be0e04](http://git.fasolti.com.br/aa/sgi/commit/0be0e04))
* Refatoração (SGINV-2322). ([cf380f5](http://git.fasolti.com.br/aa/sgi/commit/cf380f5))
* Refatoração para a tela de edição da Relação Bancária (SGINV-2325). ([412b696](http://git.fasolti.com.br/aa/sgi/commit/412b696))
* Refatorando o cancelamento da Nota Fiscal. ([a710053](http://git.fasolti.com.br/aa/sgi/commit/a710053))
* Refatorando recebimento de parcela. ([bfadb36](http://git.fasolti.com.br/aa/sgi/commit/bfadb36))
* Removendo o cancelamento da parcela. ([d3b34ca](http://git.fasolti.com.br/aa/sgi/commit/d3b34ca))
* Removido o Botão "CONFIRMAR CADASTRO" e ajustes na data de cadastramento na Prefeitura (SGINV-2340). ([eacf233](http://git.fasolti.com.br/aa/sgi/commit/eacf233))



### [5.11.20-rc.0](http://git.fasolti.com.br/aa/sgi/compare/v5.11.19...v5.11.20-rc.0) (2019-06-24)


### Bug Fixes

* Ajuste em produção do pesquisaPorNumeroAutoComplete. ([fa6bd46](http://git.fasolti.com.br/aa/sgi/commit/fa6bd46))
* Ajuste para aba de Discriminação de Serviço. ([144aabc](http://git.fasolti.com.br/aa/sgi/commit/144aabc))
* Ajuste para verificação da data do lote FATN, quando criar ou numerar nota fiscal (SGINV-2286). ([773aa98](http://git.fasolti.com.br/aa/sgi/commit/773aa98))
* Ajustes na tela de pesquisa de 'Movimento Autorizado' (SGINV-2280). ([761c1d5](http://git.fasolti.com.br/aa/sgi/commit/761c1d5))
* Atualizando dados. ([ab6597f](http://git.fasolti.com.br/aa/sgi/commit/ab6597f))
* **Parte 02:** Ajustes de cancelamento de Nota Fiscal. Adicionado email caso ocorra erro. ([e6e6757](http://git.fasolti.com.br/aa/sgi/commit/e6e6757))
* Ajustes de cancelamento de Nota Fiscal. Adicionado email caso ocorra erro. ([49a6bb5](http://git.fasolti.com.br/aa/sgi/commit/49a6bb5))
* Ajustes no campo Ordem na tela de filtro de relatório de Contas a Receber (SGINV-2293). ([b34c50d](http://git.fasolti.com.br/aa/sgi/commit/b34c50d))
* Ajustes para correção de ordem de nota tribulo alíquota. ([9dddc5a](http://git.fasolti.com.br/aa/sgi/commit/9dddc5a))
* Ao alterar o cliente/CL, ocorrem problemas na cidade do contrato E na edição de tributos E na criação de NF: não permitir criar etapas até que os "erros" tenham sido resolvidos (SGINV-2288). ([035b358](http://git.fasolti.com.br/aa/sgi/commit/035b358))
* Ao clicar no ícone  (coluna "A pagar"): o dialog de demonstração dos valores não é exibido (SGINV-2300); ([dfceafd](http://git.fasolti.com.br/aa/sgi/commit/dfceafd))
* Ao gravar um TR/TA com juros, o valor gravado (campo "total", tabela "item_relacao") não está considerando este valor dos juros (verificar para a multa também) (SGINV-2302) ([f826e35](http://git.fasolti.com.br/aa/sgi/commit/f826e35))
* Ao marcar os tributos a serem zerados, ocorre erro no botão "CONFIRMAR" (SGINV-2309). ([e7a56a7](http://git.fasolti.com.br/aa/sgi/commit/e7a56a7))
* Ao remover tributos, nada acontece (ver sequência de imagens anexo) ([a213563](http://git.fasolti.com.br/aa/sgi/commit/a213563))
* Correção da classe BaseEntity. ([303709a](http://git.fasolti.com.br/aa/sgi/commit/303709a))
* Correção de cheques e endosso da relação bancária. ([af2e982](http://git.fasolti.com.br/aa/sgi/commit/af2e982))
* Correção na edição do faturamento. A conta corrente não está sendo carregada na edição. ([55ac767](http://git.fasolti.com.br/aa/sgi/commit/55ac767))
* Correção na pesquisa botão "ASSOCIAR NF SUBSTITUTA": retirar restrição de faturamento (SGINV-2285). ([279c6dc](http://git.fasolti.com.br/aa/sgi/commit/279c6dc))
* Correção no CRUD de Banco (SGINV-2279). ([2b662e0](http://git.fasolti.com.br/aa/sgi/commit/2b662e0))
* Correção no inativação de dados bancários (SGINV-2282). ([1e68f7a](http://git.fasolti.com.br/aa/sgi/commit/1e68f7a))
* Corrigindo Banco com DomainObject. ([0595950](http://git.fasolti.com.br/aa/sgi/commit/0595950))
* Dialog Tributo NF (SGINV-2305) ([e24370b](http://git.fasolti.com.br/aa/sgi/commit/e24370b))
* Permitir pesquisa com sufixo "NF/". ([6ace8f0](http://git.fasolti.com.br/aa/sgi/commit/6ace8f0))
* Refatorando a edição do Faturamento, pois ocorria erro ao editar onde dados sumiam defido a ações de tela e limpeza de combos. ([236b586](http://git.fasolti.com.br/aa/sgi/commit/236b586))
* Reinf: correção do cálculo do faturamento para o registro R-2060. ([b837b4c](http://git.fasolti.com.br/aa/sgi/commit/b837b4c))
* Troca de "Cheque" para "Bankline manual": mantém-se o número dos cheques (SGINV-2277). ([584571d](http://git.fasolti.com.br/aa/sgi/commit/584571d))
* Troca de "Cheque" para "Bankline manual": mantém-se o número dos cheques, mas o correto é que estes número sejam apagados (não temos mais numeração de cheques) (SGINV-2277). ([922e18c](http://git.fasolti.com.br/aa/sgi/commit/922e18c))
* Validação para valor mínimo (SGINV-2305) ([1b49535](http://git.fasolti.com.br/aa/sgi/commit/1b49535))


### Features

* Add mensagem ao numerar nota fiscal (SGINV-2295). ([5018b52](http://git.fasolti.com.br/aa/sgi/commit/5018b52))
* Add mensagem ao salvar uma nova nota fiscal (SGINV-2286). ([6bb7c0e](http://git.fasolti.com.br/aa/sgi/commit/6bb7c0e))
* Add mensagem de verificação do lote (SGINV-2287). ([1730a1b](http://git.fasolti.com.br/aa/sgi/commit/1730a1b))
* Add novos atributos na classe DadosBancarios (SGINV-2281). ([b71c6a5](http://git.fasolti.com.br/aa/sgi/commit/b71c6a5))
* Add validação para o botão 'Enviar' da Relação Bancária (SGINV-2283). ([8a8c04f](http://git.fasolti.com.br/aa/sgi/commit/8a8c04f))
* Ajuste no campo 'Ordem' da tela de relatório de Contas a Receber (SGINV-2293). ([116e28c](http://git.fasolti.com.br/aa/sgi/commit/116e28c))
* Ajustes para classe DadosBancarios (SGINV-2281). ([5eec0c0](http://git.fasolti.com.br/aa/sgi/commit/5eec0c0))
* Ajustes para tela de edição de Movimento Autorizado (boletos) (SGINV-2282). ([697382e](http://git.fasolti.com.br/aa/sgi/commit/697382e))
* Bancos - Filtros de pesquisa (SGINV-2279). ([b99d041](http://git.fasolti.com.br/aa/sgi/commit/b99d041))
* botão "ASSOCIAR NF SUBSTITUTA": retirar restrição de faturamento (SGINV-2285). ([c7e0114](http://git.fasolti.com.br/aa/sgi/commit/c7e0114))
* Criado arquivo SH para abrir o VSCode na pasta frontend. ([abbf38c](http://git.fasolti.com.br/aa/sgi/commit/abbf38c))
* CRUD de banco (SGINV-2279). ([9dbe0c4](http://git.fasolti.com.br/aa/sgi/commit/9dbe0c4))
* Incluir o CNPJ/CPF na impressão do endosso (quando for ocaso, já que TA/TR não possuem estes campos) (SGINV-2278). ([965ffdd](http://git.fasolti.com.br/aa/sgi/commit/965ffdd))
* Não permitir data de contabilização fora de dia útil (SGINV-2317). ([b68fef7](http://git.fasolti.com.br/aa/sgi/commit/b68fef7))
* Refatoração do TipoLoteInterface. ([9a53b53](http://git.fasolti.com.br/aa/sgi/commit/9a53b53))
* Remoção de opção do 'Custos' (SGINV-2296). ([eab2e86](http://git.fasolti.com.br/aa/sgi/commit/eab2e86))
* Validar se o CL possui a situação "Ativo" OU "Encerrado, mas com faturamento" (SGINV-2286). ([d2b4a28](http://git.fasolti.com.br/aa/sgi/commit/d2b4a28))



### [5.11.19](http://git.fasolti.com.br/aa/sgi/compare/v5.11.18-rc.1...v5.11.19) (2019-06-07)


### Bug Fixes

* Ajustando label do número da NF associada ao cancelamento. ([bad9b23](http://git.fasolti.com.br/aa/sgi/commit/bad9b23))
* Ajustes para não duplicar as retenções da nota saída parcela. ([83304f7](http://git.fasolti.com.br/aa/sgi/commit/83304f7))
* Correção da tela de dados do cliente (SGINV-2262). ([280c548](http://git.fasolti.com.br/aa/sgi/commit/280c548))
* Correção da tela de relatório contas a receber para informar o período de data de cancelamento (SGINV-2264). ([b70c7ac](http://git.fasolti.com.br/aa/sgi/commit/b70c7ac))
* Correção do valor do tributo de uma parcela nota. ([f07d05d](http://git.fasolti.com.br/aa/sgi/commit/f07d05d))
* Correção na empresa da nota fiscal que estava alterando para a matriz e ajuste de data na edição das parcelas. ([f43ba30](http://git.fasolti.com.br/aa/sgi/commit/f43ba30))
* Correções em produção. ([0b4167a](http://git.fasolti.com.br/aa/sgi/commit/0b4167a))
* Recuperação dos arquivos faturamento.controller.js e notasFicaisController.js ([8fc0206](http://git.fasolti.com.br/aa/sgi/commit/8fc0206))
* Removendo cálculo de valor líquido de parcela do backend. Valor já é definido no banco. ([3610835](http://git.fasolti.com.br/aa/sgi/commit/3610835))


### Features

* Ação de voltar ajustada para uma nova nota fiscal. ([34f92a0](http://git.fasolti.com.br/aa/sgi/commit/34f92a0))
* Add parametro 'paramOi' para o relatório de 'Fluxo de caixa' do 'Contas a receber'. ([0400bd2](http://git.fasolti.com.br/aa/sgi/commit/0400bd2))
* Add parametros para o relatório "Relação de Notas Fiscais do lote" (SGINV-2261). ([a87ff5a](http://git.fasolti.com.br/aa/sgi/commit/a87ff5a))
* Adicionando conta contábil no dialog do cliente/empresa do faturamento SGINV-2262. ([ab94ca9](http://git.fasolti.com.br/aa/sgi/commit/ab94ca9))
* ASSOCIAR NF SUBSTITUTA (SGINV-2268). ([ef97b39](http://git.fasolti.com.br/aa/sgi/commit/ef97b39))
* Associar NF substituta e refatoração de controller. ([61e099a](http://git.fasolti.com.br/aa/sgi/commit/61e099a))
* Botão "IMPRIMIR": salvar observações (SGINV-2270). ([9d2e066](http://git.fasolti.com.br/aa/sgi/commit/9d2e066))
* Gravar observação da parcela da NF (SGINV-2270). ([8fbc06c](http://git.fasolti.com.br/aa/sgi/commit/8fbc06c))
* Popup de dados do cliente trazendo conta contábil na tela de nota fiscal (SGINV-2265). ([37fcea5](http://git.fasolti.com.br/aa/sgi/commit/37fcea5))



### [5.11.18-rc.1](http://git.fasolti.com.br/aa/sgi/compare/v5.11.18-rc.0...v5.11.18-rc.1) (2019-05-23)


### Bug Fixes

* Aba "TRIBUTOS": Campo "ISS será retido pelo cliente": deve ser conforme a NF (campo "iss_retido_cliente", tabela "nota_saida")Deverá ser a 2ª aba (após a aba "DADOS PRINCIPAIS") (SGINV-2220). ([ae11257](http://git.fasolti.com.br/aa/sgi/commit/ae11257))
* Ajuste de layout (SGINV-2242) ([a1b8f0d](http://git.fasolti.com.br/aa/sgi/commit/a1b8f0d))
* Ajuste do gulp ao deletar e limpar a lib. ([4077d60](http://git.fasolti.com.br/aa/sgi/commit/4077d60))
* Ajuste do gulp para criar a pasta lib. ([0989fe6](http://git.fasolti.com.br/aa/sgi/commit/0989fe6))
* Ajustes (SGINV-2247) ([0175cf3](http://git.fasolti.com.br/aa/sgi/commit/0175cf3))
* Ajustes na edição da aba parcelas da notas fiscal (SGINV-2244). ([707615a](http://git.fasolti.com.br/aa/sgi/commit/707615a))
* Ajustes na impressão da nota fiscal. ([a8b8659](http://git.fasolti.com.br/aa/sgi/commit/a8b8659))
* Ajustes para o parcelamento de Contas a receber (SGINV-2219). ([d1935a2](http://git.fasolti.com.br/aa/sgi/commit/d1935a2))
* Ajustes relacionados a aba de parcelas da nota fiscal (SGINV-2218). ([7dee68d](http://git.fasolti.com.br/aa/sgi/commit/7dee68d))
* Correção do bower_components passando para o package.json e usando yarn no compile.web. ([dff63db](http://git.fasolti.com.br/aa/sgi/commit/dff63db))
* Correção no valor total quando nota fiscal parcelada. ([976d32a](http://git.fasolti.com.br/aa/sgi/commit/976d32a))
* Erro ao editar discriminação de nota fiscal. ([74f989c](http://git.fasolti.com.br/aa/sgi/commit/74f989c))
* Erro ao exibir tributos da nota fiscal. Erro na otimização. ([949c16f](http://git.fasolti.com.br/aa/sgi/commit/949c16f))
* Label (SGINV-2253) ([ff157e2](http://git.fasolti.com.br/aa/sgi/commit/ff157e2))
* Mensagem de advertência para descrições iguais de itens de etapas(SGINV-2248) ([5fc70f5](http://git.fasolti.com.br/aa/sgi/commit/5fc70f5))
* **Segunda parte:** Ajuste do gulp ao deletar e limpar a lib. ([749e79d](http://git.fasolti.com.br/aa/sgi/commit/749e79d))
* Novos ajustes no issue SGINV-2244. ([72bc251](http://git.fasolti.com.br/aa/sgi/commit/72bc251))
* O valor dos tributos deve refletir conforme a modificação acima da aba "TRIBUTOS", ou seja, vai "sofrer" influência do "ISS será retido pelo cliente" da NF (e não do Faturamento, como está atualmente) (SGINV-2220). ([cbb8476](http://git.fasolti.com.br/aa/sgi/commit/cbb8476))


### Features

* Add Novo relatório (SGINV-2228) ([f815470](http://git.fasolti.com.br/aa/sgi/commit/f815470))
* Ajuste após merge. ([d22c360](http://git.fasolti.com.br/aa/sgi/commit/d22c360))
* Ajustes para o relatório de contas a receber (SGINV-2246). ([cbdd8c7](http://git.fasolti.com.br/aa/sgi/commit/cbdd8c7))
* Atualização do gulp. ([e724022](http://git.fasolti.com.br/aa/sgi/commit/e724022))
* Cobrança crítica (SGINV-2226) ([2edeb45](http://git.fasolti.com.br/aa/sgi/commit/2edeb45))
* Correção no total e parcelamento de uma nota fiscal. Aba parcelas ajustada a partir do reparcelamento. ([b6ecead](http://git.fasolti.com.br/aa/sgi/commit/b6ecead))
* Novo tipo de Situação para CentroFinanceiro (SGINV-2225) ([7767769](http://git.fasolti.com.br/aa/sgi/commit/7767769))
* Novos campos (SGINV-2228) ([279afaa](http://git.fasolti.com.br/aa/sgi/commit/279afaa))
* Refatorado o módulo e criação do frontend. ([8ce49c0](http://git.fasolti.com.br/aa/sgi/commit/8ce49c0))



## [5.11.18-rc.1](http://git.fasolti.com.br/aa/sgi/compare/v5.11.18-rc.0...v5.11.18-rc.1) (2019-05-16)


### Bug Fixes

* Aba "TRIBUTOS": Campo "ISS será retido pelo cliente": deve ser conforme a NF (campo "iss_retido_cliente", tabela "nota_saida")Deverá ser a 2ª aba (após a aba "DADOS PRINCIPAIS") (SGINV-2220). ([ae11257](http://git.fasolti.com.br/aa/sgi/commits/ae11257))
* Ajuste de layout (SGINV-2242) ([a1b8f0d](http://git.fasolti.com.br/aa/sgi/commits/a1b8f0d))
* Ajustes na impressão da nota fiscal. ([a8b8659](http://git.fasolti.com.br/aa/sgi/commits/a8b8659))
* Ajustes para o parcelamento de Contas a receber (SGINV-2219). ([d1935a2](http://git.fasolti.com.br/aa/sgi/commits/d1935a2))
* Ajustes relacionados a aba de parcelas da nota fiscal (SGINV-2218). ([7dee68d](http://git.fasolti.com.br/aa/sgi/commits/7dee68d))
* Erro ao editar discriminação de nota fiscal. ([74f989c](http://git.fasolti.com.br/aa/sgi/commits/74f989c))
* Erro ao exibir tributos da nota fiscal. Erro na otimização. ([949c16f](http://git.fasolti.com.br/aa/sgi/commits/949c16f))
* O valor dos tributos deve refletir conforme a modificação acima da aba "TRIBUTOS", ou seja, vai "sofrer" influência do "ISS será retido pelo cliente" da NF (e não do Faturamento, como está atualmente) (SGINV-2220). ([cbb8476](http://git.fasolti.com.br/aa/sgi/commits/cbb8476))


### Features

* Add Novo relatório (SGINV-2228) ([f815470](http://git.fasolti.com.br/aa/sgi/commits/f815470))
* Cobrança crítica (SGINV-2226) ([2edeb45](http://git.fasolti.com.br/aa/sgi/commits/2edeb45))
* Correção no total e parcelamento de uma nota fiscal. Aba parcelas ajustada a partir do reparcelamento. ([b6ecead](http://git.fasolti.com.br/aa/sgi/commits/b6ecead))
* Novos campos (SGINV-2228) ([279afaa](http://git.fasolti.com.br/aa/sgi/commits/279afaa))


## [5.11.18-rc.0](http://git.fasolti.com.br/aa/sgi/compare/v5.11.17-rc.2...v5.11.18-rc.0) (2019-04-29)


### Bug Fixes

* Ajsutado o método ajustarTimeZoneData para colocar as horas como "12h" e não "00h". ([217bb2f](http://git.fasolti.com.br/aa/sgi/commits/217bb2f))
* Ajuste dos pom para <version>5.11.18-SNAPSHOT</version>. ([ddbe9d3](http://git.fasolti.com.br/aa/sgi/commits/ddbe9d3))
* Ajuste nos totais da tabela de previsão de faturamento (SGINV-2175). ([4f443fa](http://git.fasolti.com.br/aa/sgi/commits/4f443fa))
* Complementação da especificação (SGINV-2207) ([bbd091b](http://git.fasolti.com.br/aa/sgi/commits/bbd091b))
* Correção do erro da data de emissão ao carregar a nota fiscal. ([471e90b](http://git.fasolti.com.br/aa/sgi/commits/471e90b))
* Correção do erro da data de emissão ao carregar a nota fiscal. ([b76a13a](http://git.fasolti.com.br/aa/sgi/commits/b76a13a))
* Correção do erro da tela de edição da Relação Bancária (SGINV-2214). ([74ec817](http://git.fasolti.com.br/aa/sgi/commits/74ec817))
* Correção do erro da tela de movimento autorizado (SGINV-2212). ([7a231bc](http://git.fasolti.com.br/aa/sgi/commits/7a231bc))
* Correção e ajuste na atualização do protocolo para data de atualização. ([a4526e9](http://git.fasolti.com.br/aa/sgi/commits/a4526e9))
* Correção e ajuste na atualização do protocolo. ([e086892](http://git.fasolti.com.br/aa/sgi/commits/e086892))
* Correção na data de emissão ao numerar a nota fiscal e ajuste na data na edição da nota. ([aa42813](http://git.fasolti.com.br/aa/sgi/commits/aa42813))
* Erro com ng-if e ng-show. Não sei o motivo, mas foi trocado para uma tela e par aoutra não. ([2b93944](http://git.fasolti.com.br/aa/sgi/commits/2b93944))


### Features

* Refatoração do repositório da Aliquota. ([50a0382](http://git.fasolti.com.br/aa/sgi/commits/50a0382))
* Refatoração do repositório da NotaTributoAliquota. ([b9f7a26](http://git.fasolti.com.br/aa/sgi/commits/b9f7a26))
* Refatoração do repositório da Obra. ([81cc441](http://git.fasolti.com.br/aa/sgi/commits/81cc441))



# Change Log

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

## [5.11.17-rc.2](http://git.fasolti.com.br/aa/sgi/compare/v5.11.17-rc.1...v5.11.17-rc.2) (2019-04-22)


### Bug Fixes

* Adicionando valor zero para campos nulos da parcela. Exibição de parcelas da nota fiscal. ([9049b44](http://git.fasolti.com.br/aa/sgi/commits/9049b44))
* Ajustando bug no parcelamento da nota fiscal em recebimento (SGINV-2202). ([fc388e6](http://git.fasolti.com.br/aa/sgi/commits/fc388e6))
* Ao salvar, os registros de retenção da(s) parcela(s) (no banco de dados) estão sendo sempre criados, logo duplicados, triplicados, etc (conforme o número de alterações realizadas na NF) (SGINV-2197). ([17f296e](http://git.fasolti.com.br/aa/sgi/commits/17f296e))
* Ao salvar, quando há inclusão de tributos, ocorre o erro java.lang.NullPointerException (SGINV-2189). ([5c1bcba](http://git.fasolti.com.br/aa/sgi/commits/5c1bcba))
* Botão cadastro na prefeitura não estava aparecendo (SGINV-2190). ([ff4a450](http://git.fasolti.com.br/aa/sgi/commits/ff4a450))
* Campo "Vencimento": se não for possível calcular (tipo de cobrança solicita digitação pelo usuário), deixar em branco (está vindo com default igual à emissão) (SGINV-2185). ([dd49e44](http://git.fasolti.com.br/aa/sgi/commits/dd49e44))
* Correção da mensagem de centro financeiro inativo no faturamento (SGINV-2198). ([34920d5](http://git.fasolti.com.br/aa/sgi/commits/34920d5))
* Correção do erro do campo Cidade ISS da aba de tributos da tela de novo faturamento (SGINV-2196). ([e0d345c](http://git.fasolti.com.br/aa/sgi/commits/e0d345c))
* Correção do erro na numeração da nota fiscal. ([4eafb27](http://git.fasolti.com.br/aa/sgi/commits/4eafb27))
* Correção no status da parcela quando nota em recebimento.. ([f987ddf](http://git.fasolti.com.br/aa/sgi/commits/f987ddf))
* Correção para o campo 'Banco' do recebimento da parcela (SGINV-2192). ([e9c54f8](http://git.fasolti.com.br/aa/sgi/commits/e9c54f8))
* Correção relatórios de contas a receber. ([98ca5dc](http://git.fasolti.com.br/aa/sgi/commits/98ca5dc))
* Estava removendo tributos ao receber segudna parcela. ([d49c2d7](http://git.fasolti.com.br/aa/sgi/commits/d49c2d7))
* Exibir desconto/devolução em outras parcelas além da primeira. ([15c36db](http://git.fasolti.com.br/aa/sgi/commits/15c36db))
* Nome da empresa na nota fiscal. ([e6e92a9](http://git.fasolti.com.br/aa/sgi/commits/e6e92a9))
* Ordenação da combo "Relacionamento" (SGINV-2205) ([5daa621](http://git.fasolti.com.br/aa/sgi/commits/5daa621))
* **Backend:** Correção do modelo e backend (SGINV-2170). ([fe44a95](http://git.fasolti.com.br/aa/sgi/commits/fe44a95))
* Quando o relatório é executado, o campo "Ordem" fica bloqueado (SGINV-2186). ([e34c8d0](http://git.fasolti.com.br/aa/sgi/commits/e34c8d0))


### Features

* Criação da tela 'Previsão de faturamento' (SGINV-2170). ([e7a2f2a](http://git.fasolti.com.br/aa/sgi/commits/e7a2f2a))
* **Backend:** Faturamento > Cadastros > Previsão de faturamento (SGINV-2170). ([0749965](http://git.fasolti.com.br/aa/sgi/commits/0749965))
* Ajuste na data de vincimento da NotaFiscal (NOVO) e remoção de campos da aba de estapas da NF (SGINV-2185). ([b4b19c6](http://git.fasolti.com.br/aa/sgi/commits/b4b19c6))
* Ajuste na tela de faturamento (SGINV-2198). ([d6e1738](http://git.fasolti.com.br/aa/sgi/commits/d6e1738))
* Ajustes na pesquisa do campo numero da nota fiscal (SGINV-2193). ([29ce7e9](http://git.fasolti.com.br/aa/sgi/commits/29ce7e9))
* Ajustes na tela de exibição e no backend (SGINV-2175). ([b6e5262](http://git.fasolti.com.br/aa/sgi/commits/b6e5262))
* Ajustes no campo numero na tela de pesquisa de parcela (SGINV-2194). ([80952cb](http://git.fasolti.com.br/aa/sgi/commits/80952cb))
* Criação da tela de edição de 'Previsão de faturamento' (SGINV-2199). ([ac771fd](http://git.fasolti.com.br/aa/sgi/commits/ac771fd))
* Criação de serviço para calcular o valor da 'OS Inicial' (SGINV-2175). ([2453369](http://git.fasolti.com.br/aa/sgi/commits/2453369))
* Habilitar o campo ordem no relatório de contas a receber (SGINV-2186). ([70e9ebe](http://git.fasolti.com.br/aa/sgi/commits/70e9ebe))
* Novo campo - Excluir clientes (cobrança) (SGINV-2207) ([1d1c528](http://git.fasolti.com.br/aa/sgi/commits/1d1c528))
* Redirecionamento para a página anterior em um login e logout. Apresentação de dialog de sessão expirada. ([d3f7ec9](http://git.fasolti.com.br/aa/sgi/commits/d3f7ec9))


## [5.11.17-rc.1](http://git.fasolti.com.br/aa/sgi/compare/v5.11.17-rc.0...v5.11.17-rc.1) (2019-04-08)


### Bug Fixes

* Aba "ETAPA": está considerando o valor recebido dos tributos (tabela "nota_tributo_aliquota") para o cálculo do valor líquido (para a comparação com o valor gravado): DEVE ser o valor conforme as regras acima (SGINV-2149). ([f695267](http://git.fasolti.com.br/aa/sgi/commits/f695267))
* Ajuste para desabilitar o botão 'Cancelar' e 'Remover NF numerada' quando tiver pelo menos uma parcela em situação 'Recebida'. Ajustes para o botão 'Cancelar modificação' da aba de parcelas. ([3e56bc0](http://git.fasolti.com.br/aa/sgi/commits/3e56bc0))
* Ao adicionar um tributo (em edição, ou seja, se já existir o tributo, manter a base "gravada") a base de cálculo DEVE ser o valor bruto da parcela (seja a 1ª ou não) (SGINV-2153). ([aebd46c](http://git.fasolti.com.br/aa/sgi/commits/aebd46c))
* **Parte 01:** Aba "PARCELAS" (se a NF for parcelada) (SGINV-2149). ([aef88ad](http://git.fasolti.com.br/aa/sgi/commits/aef88ad))
* **Parte 02:** Aba "PARCELAS" (se a NF for parcelada) (SGINV-2149). ([4054873](http://git.fasolti.com.br/aa/sgi/commits/4054873))
* Ao cancelar o recebimento (após as confirmações existentes): limpar a data de contabilização da parcela (SGINV-2172). ([81d1a07](http://git.fasolti.com.br/aa/sgi/commits/81d1a07))
* Ao reparcelas a NF, manter as parcelas recebidas sem alteração (as parcelas não podem ser TODAS removidas e recriadas) ([c42812a](http://git.fasolti.com.br/aa/sgi/commits/c42812a))
* Campo "Subtotal": atualiza durante o recebimento, como acontece com o campo "Tributos" (só está sendo atualizado após o "RECEBER") (SGINV-2153). ([fdf9ee7](http://git.fasolti.com.br/aa/sgi/commits/fdf9ee7))


### Features

* Abrindo faturamento em uma nova aba (SGINV-2182). ([240fcaa](http://git.fasolti.com.br/aa/sgi/commits/240fcaa))
* Abrir parcela em uma nova aba. ([a61a62d](http://git.fasolti.com.br/aa/sgi/commits/a61a62d))
* Add do campo "ordem" na nota tributo aliquota (SGINV-2144). ([a3d42ed](http://git.fasolti.com.br/aa/sgi/commits/a3d42ed))
* Ajuste na exibição e recebimento da parcela de contas a receber (SGINV-2153). ([2828af3](http://git.fasolti.com.br/aa/sgi/commits/2828af3))
* Ajustes na aba de tributos da nota fiscal (SGINV-2149). ([d030003](http://git.fasolti.com.br/aa/sgi/commits/d030003))
* Refatoração do repositório do ServicoTipo (SGINV-2084). ([0652afc](http://git.fasolti.com.br/aa/sgi/commits/0652afc))


## [5.11.17-rc.0](http://git.fasolti.com.br/aa/sgi/compare/v5.11.16-rc.4...v5.11.17-rc.0) (2019-04-03)


### Bug Fixes

* Ajuste na hora do cancelamento na tela de exibição de nota fiscal ([03356f0](http://git.fasolti.com.br/aa/sgi/commits/03356f0))
* Ajuste no cancelamento das parcelas da nota saída (SGINV-2178). ([d8fa201](http://git.fasolti.com.br/aa/sgi/commits/d8fa201))
* Ajustes ao deletar tributos de parcelas em recebimento. ([f079e81](http://git.fasolti.com.br/aa/sgi/commits/f079e81))
* Apagar data contabilização ao cancelar recebimento (SGINV-2172). ([43a91f5](http://git.fasolti.com.br/aa/sgi/commits/43a91f5))
* Correção de erro no campo Centro Financeiro (SGINV-2151). ([cdfe4cc](http://git.fasolti.com.br/aa/sgi/commits/cdfe4cc))
* Correção do erro que mudava a situação da nota fiscal ([39e7e6a](http://git.fasolti.com.br/aa/sgi/commits/39e7e6a))
* Erro ao obter código pai de tipo serviço sem pai (SGINV-2169). ([524b767](http://git.fasolti.com.br/aa/sgi/commits/524b767))
* Liberar situação para o filtro de pesquisa de contas a receber, quando relatório for duplicata (SGINV-2171). ([4a4720c](http://git.fasolti.com.br/aa/sgi/commits/4a4720c))
* Pesquisa de parcelas canceladas independentes da nota cancelada(SGINV-2180). ([8956a66](http://git.fasolti.com.br/aa/sgi/commits/8956a66))


### Features

* Ajuste na data de agendamento (SGINV-2156). ([96f5653](http://git.fasolti.com.br/aa/sgi/commits/96f5653))
* Ajuste na tela de pesquisa de parcelas (SGINV-2152). ([f77bdb6](http://git.fasolti.com.br/aa/sgi/commits/f77bdb6))
* Ajuste na tela de relatório de contas a receber (SGINV-2151). ([26e9bf4](http://git.fasolti.com.br/aa/sgi/commits/26e9bf4))
* Ajuste no campo Gerência no filtro do relatório (SGINV-2150). ([ea322ae](http://git.fasolti.com.br/aa/sgi/commits/ea322ae))

## [5.11.16-rc.4](http://git.fasolti.com.br/aa/sgi/compare/v5.11.16-rc.3...v5.11.16-rc.4) (2019-03-13)


### Bug Fixes

* Ajustes na tela de relatório de faturamento (SGINV-2103). ([ce7ddda](http://git.fasolti.com.br/aa/sgi/commits/ce7ddda))
* Correção da criação de alíquota na criação da nota fiscal saída. ([daa4979](http://git.fasolti.com.br/aa/sgi/commits/daa4979))
* Correção da criação de novo tributo na tela de nova nota fiscal. ([d9d8614](http://git.fasolti.com.br/aa/sgi/commits/d9d8614))
* Correção na edição de tributo de uma parcela em recebimento (SGINV-2131). ([b84d6ea](http://git.fasolti.com.br/aa/sgi/commits/b84d6ea))
* Remoção das linhas vermelhas na exibição da interface do Portal Oi (SGINV-2097). ([bd744f1](http://git.fasolti.com.br/aa/sgi/commits/bd744f1))


### Features

* Ajuste no campo 'Tipo' de nova interface (SGINV-2117). ([b5b5fe1](http://git.fasolti.com.br/aa/sgi/commits/b5b5fe1))
* Criando nota saída parcela retenção quando parcela não tem a retenção (SGINV-2131). ([54f69cb](http://git.fasolti.com.br/aa/sgi/commits/54f69cb))

## [5.11.16-rc.3](http://git.fasolti.com.br/aa/sgi/compare/v5.11.16-rc.2...v5.11.16-rc.3) (2019-03-08)


### Bug Fixes

* Ajuste das classes MotivoDescontoDevolucao. ([e59ac9b](http://git.fasolti.com.br/aa/sgi/commits/e59ac9b))
* Correção do envio da relação bancária (SGINV-2113). ([ab0e41c](http://git.fasolti.com.br/aa/sgi/commits/ab0e41c))
* Correção do valor liquido da tela de pesquisa de parcela (SGINV-2108). ([931b760](http://git.fasolti.com.br/aa/sgi/commits/931b760))


### Features

* Ajuste no campo "Contabilização" na tela de recebimento da parcela (SGINV-2112). ([5ceedd1](http://git.fasolti.com.br/aa/sgi/commits/5ceedd1))
* Ajustes no filtro da tela de pesquisa de interface (SGINV-2111). ([d89a972](http://git.fasolti.com.br/aa/sgi/commits/d89a972))
* Ajustes nos campos de datas na tela de relatórios (SGINV-2103). ([151aa0d](http://git.fasolti.com.br/aa/sgi/commits/151aa0d))
* Atualização das classes MotivoDescontoDevolucao. ([ca15ba0](http://git.fasolti.com.br/aa/sgi/commits/ca15ba0))


## [5.11.16-rc.2](http://git.fasolti.com.br/aa/sgi/compare/v5.11.16-rc.1...v5.11.16-rc.2) (2019-03-07)


### Bug Fixes

* **FT/NF/ED:** selecionar automaticamente a cidade do ISS na combo Tributos/Cidade (SGINV-2105) ([7a8970a](http://git.fasolti.com.br/aa/sgi/commits/7a8970a))
* Correção da lista de aliquota no método listarPorCodigoNota em NotaTributoAliquotaServiceImpl. ([efc8afb](http://git.fasolti.com.br/aa/sgi/commits/efc8afb))
* Correção do erro de cancelamento da nota fiscal e parcela (SGINV-2095). ([f276e37](http://git.fasolti.com.br/aa/sgi/commits/f276e37))
* Erro no package.json na instalação do node-sass. ([1505152](http://git.fasolti.com.br/aa/sgi/commits/1505152))


### Features

* Add quadro com as notas fiscais do lote Portal Oi (SGINV-2097). ([3010738](http://git.fasolti.com.br/aa/sgi/commits/3010738))
* Adicionando desconto e devolução em parcelas e otimizando o recebimento da parcela (SGINV-2100). ([4fa9154](http://git.fasolti.com.br/aa/sgi/commits/4fa9154))
* Ajustes nos campos e add de opção de relatórios (SGINV-2093). ([9722214](http://git.fasolti.com.br/aa/sgi/commits/9722214))
* Atualização da edição da Parcela e data de agendamento atualizada automaticamente (SGINV-2099). ([a288cc2](http://git.fasolti.com.br/aa/sgi/commits/a288cc2))
* Nova aba de desconto/devolução em parcela (SGINV-2101). ([dabb932](http://git.fasolti.com.br/aa/sgi/commits/dabb932))
* Refatorado pesquisa para faturamento usando QueryDsl (SGINV-2084). ([4f64f01](http://git.fasolti.com.br/aa/sgi/commits/4f64f01))


## [5.11.16-rc.1](http://git.fasolti.com.br/aa/sgi/compare/v5.11.16-rc.0...v5.11.16-rc.1) (2019-02-27)


### Bug Fixes

* Erro ao remover faturamentoAliquotaService.deletarFaturamentoAliquota. Estava com código errado. ([e9e5e4b](http://git.fasolti.com.br/aa/sgi/commits/e9e5e4b))
* Erro no cálculo dos valores das notasFiscais. ([755e8b8](http://git.fasolti.com.br/aa/sgi/commits/755e8b8))


### Features

* Ajuste da impressão da parcela e abrindo dialig de observação ao iniciar a página. ([6299621](http://git.fasolti.com.br/aa/sgi/commits/6299621))
* Ajustes na aba de NF na exibição do faturamento (SGINV-2087). ([f37f541](http://git.fasolti.com.br/aa/sgi/commits/f37f541))
* Ajustes na aba de nota fiscal na tela de exibição de faturamento (SGINV-2087). ([531b76a](http://git.fasolti.com.br/aa/sgi/commits/531b76a))
* Ajustes na exibição das parcelas e Add das entidades 'MotivoDescontoDevolucao' e 'MotivoDescontoDevolucaoContaContabil' (SGINV-2043). ([b4d2f64](http://git.fasolti.com.br/aa/sgi/commits/b4d2f64))
* Ajustes na tela de impressão de parcela e add do botão de observações (SGINV-2089). ([46fc186](http://git.fasolti.com.br/aa/sgi/commits/46fc186))
* Ajustes nas colunas e na ordenação da tela de pesquisa de parcela (SGINV-2077). ([e564e0b](http://git.fasolti.com.br/aa/sgi/commits/e564e0b))
* Ajustes nos campos na edição da parcela (SGINV-2078). ([2188dab](http://git.fasolti.com.br/aa/sgi/commits/2188dab))
* Ajustes nos campos na tela de recebimento da parcela (SGINV-2082). ([6ee0bd3](http://git.fasolti.com.br/aa/sgi/commits/6ee0bd3))
* Ajustes nos labels dos campos na tela de parcela (SGINV-2043). ([eb4da72](http://git.fasolti.com.br/aa/sgi/commits/eb4da72))
* Criado novo método queryDsl JPAUpdateClause para update de dados. ([72679c1](http://git.fasolti.com.br/aa/sgi/commits/72679c1))
* deletarFaturamentoAliquota usando QueryDsl (SGINV-2084). ([f6e5601](http://git.fasolti.com.br/aa/sgi/commits/f6e5601))
* Refatoração para os campos 'Motivo do desconto' e 'Motivo da devolução' (SGINV-2082). ([238ff7f](http://git.fasolti.com.br/aa/sgi/commits/238ff7f))
* Refatorado update para notasFiscal usando QueryDsl (SGINV-2084). ([96551e5](http://git.fasolti.com.br/aa/sgi/commits/96551e5))
* Update data atualização do faturamento usando QueryDsl (SGINV-2084). ([14559be](http://git.fasolti.com.br/aa/sgi/commits/14559be))
* Update data atualizador do faturamento usando QueryDsl (SGINV-2084). ([987f4d7](http://git.fasolti.com.br/aa/sgi/commits/987f4d7))
* Update no serviço de Nota e Despesa usando QueryDsl (SGINV-2084). ([601c04a](http://git.fasolti.com.br/aa/sgi/commits/601c04a))
* Update no serviço NotaService usando QueryDsl (SGINV-2084). ([94dd9b8](http://git.fasolti.com.br/aa/sgi/commits/94dd9b8))
* Update refatorado do faturamentoEtapa usando QueryDsl (SGINV-2084). ([8c9c978](http://git.fasolti.com.br/aa/sgi/commits/8c9c978))
* Update statsu do faturamento usando QueryDsl (SGINV-2084). ([4af255b](http://git.fasolti.com.br/aa/sgi/commits/4af255b))
* Valor zero em valorTributo na classe NotaSaidaParcela. ([9126ba9](http://git.fasolti.com.br/aa/sgi/commits/9126ba9))


## 5.11.16-rc.0 (2019-02-20)


### Bug Fixes

* Adicionado os novos campos na classe FaturamentoEtapa. ([e4e7175](http://git.fasolti.com.br/aa/sgi/commits/e4e7175))
* Ajustando combo de filiais na tela Contas a Receber - Filtros de relatório. ([ef2e688](http://git.fasolti.com.br/aa/sgi/commits/ef2e688))
* Ajuste do intervalo das marcações em vermelho (SGINV-2030). ([bc43b46](http://git.fasolti.com.br/aa/sgi/commits/bc43b46))
* Ajustes na aba de tributos na tela de receber parcela (SGINV-2057). ([4558ba3](http://git.fasolti.com.br/aa/sgi/commits/4558ba3))
* ajustes no valor da base de cálculo da impressão da nota fiscal (SGINV-2059). ([5f32d51](http://git.fasolti.com.br/aa/sgi/commits/5f32d51))
* Ajustes para aba 'INFORMAÇÕES COMPLEMENTARES' (SGINV-1982). ([dbf9f87](http://git.fasolti.com.br/aa/sgi/commits/dbf9f87))
* Ajustes para atualização da data de vencimento de uma nova nota fiscal (SGINV-1980). ([f801736](http://git.fasolti.com.br/aa/sgi/commits/f801736))
* Ajustes para impressão (SGINV-1978). ([e96fb0f](http://git.fasolti.com.br/aa/sgi/commits/e96fb0f))
* **FT/NF:** permitir salvar notas fiscais sem tributos (SGINV-2018) ([bd69747](http://git.fasolti.com.br/aa/sgi/commits/bd69747))
* Combo cidade em edição de notafiscal estava vazia (SGINV-2052). ([d4ba896](http://git.fasolti.com.br/aa/sgi/commits/d4ba896))
* Correção ao carregar cidade ISS no tributo da nota fiscal - Novo (SGINV-2052). ([b367abb](http://git.fasolti.com.br/aa/sgi/commits/b367abb))
* Correção da numeração das etapas (SGINV-1973). ([fed1298](http://git.fasolti.com.br/aa/sgi/commits/fed1298))
* Erro ao obter lista de notas fiscais em faturamento. Erro na chamada REST (SGINV-1961). ([8cfc26f](http://git.fasolti.com.br/aa/sgi/commits/8cfc26f))
* Refatorando datas da tela de relatório de contas a receber. ([117164e](http://git.fasolti.com.br/aa/sgi/commits/117164e))
* **Contas a receber:** Ajsute na impressão da parcela. ([0772e96](http://git.fasolti.com.br/aa/sgi/commits/0772e96))
* Impressão de nota fiscal em uma nova aba e troca de label para alteração de último valor (SGINV-2053). ([1500281](http://git.fasolti.com.br/aa/sgi/commits/1500281))
* **faturamento:** lazy loading de dados das abas "Notas Fiscais" e "Etapas" na tela "Faturamento/Faturamento/Exibir" (SGINV-1981) ([3c33887](http://git.fasolti.com.br/aa/sgi/commits/3c33887))
* Refatoramento na numeração de nota fiscal (SGINV-2064). Estava dando erro ao gravar nova numeração. ([1e06efd](http://git.fasolti.com.br/aa/sgi/commits/1e06efd))
* Removendo código do cliente quando relatório diferente de nf.referencia_cliente.Ajuste na tela de relatórios de faturamento (SGINV-2040). ([a0e6571](http://git.fasolti.com.br/aa/sgi/commits/a0e6571))
* SGINV-1970 ([aff0c4e](http://git.fasolti.com.br/aa/sgi/commits/aff0c4e))
* **frontend:FT/IF/PR:** permitir download para nota carioca/importado, alterar exibição das opções de pesquisa (SGINV-1999) ([48b8b43](http://git.fasolti.com.br/aa/sgi/commits/48b8b43))
* **FT/IF:** substituir todas as ocorrências de descrições dos TipoLoteInterface modificados (SGINV-1999, SGINV-2002) ([01f2ef1](http://git.fasolti.com.br/aa/sgi/commits/01f2ef1))
* **FT/IF/NV:** ajustar opções de tipo de lote de interface (SGINV-2002) ([51e6bd2](http://git.fasolti.com.br/aa/sgi/commits/51e6bd2))
* **FT/IF/PR:** ajustar opções de tipo na pesquisa (complemento) (SGINV-1999) ([08c113b](http://git.fasolti.com.br/aa/sgi/commits/08c113b))


### Features

* Add botão de impressão da parcela (SGINV-2061). ([021e62e](http://git.fasolti.com.br/aa/sgi/commits/021e62e))
* Add botão Imprimir e ajuste na aba de tributos da tela de nota fiscal do faturamento (SGINV-2053). ([0d67c24](http://git.fasolti.com.br/aa/sgi/commits/0d67c24))
* Add campo "Subtotal" na aba de recebimento de parcela (SGINV-2056). ([4733ddd](http://git.fasolti.com.br/aa/sgi/commits/4733ddd))
* Add das marcações em vermelho na tela de exibição (SGINV-2030). ([be75313](http://git.fasolti.com.br/aa/sgi/commits/be75313))
* Add tela de impressão da parcela (SGINV-2062). ([2551401](http://git.fasolti.com.br/aa/sgi/commits/2551401))
* Adição de serviço para buscar o próximo número da etapa de um faturamento (SGINV-1973). ([2e94674](http://git.fasolti.com.br/aa/sgi/commits/2e94674))
* Adição e remoção de campos da tela de pesquisa de Parcelas (SGINV-2021). ([4139853](http://git.fasolti.com.br/aa/sgi/commits/4139853))
* adicionar tributo (SGINV-2020). ([7a46e67](http://git.fasolti.com.br/aa/sgi/commits/7a46e67))
* Ajuste na tela de relatórios de faturamento (SGINV-2040). ([4aa04c8](http://git.fasolti.com.br/aa/sgi/commits/4aa04c8))
* Ajuste na tela de relatórios de faturamento (SGINV-2045). ([fbb6a55](http://git.fasolti.com.br/aa/sgi/commits/fbb6a55))
* Ajuste no layout da aba de "INFO. COMPLEMENTARES" e otimização ao salvar e atualizar um faturamento (SGINV-1997). ([b793286](http://git.fasolti.com.br/aa/sgi/commits/b793286))
* Ajustes de layout, inteligência nas escolhas de datas e correção do banco e agẽncia de uma unidade (SGINV-2032). ([374f0b9](http://git.fasolti.com.br/aa/sgi/commits/374f0b9))
* Ajustes e add do campo "Subtotal" na aba de recebimento da parcela (SGINV-2042). ([efb16f0](http://git.fasolti.com.br/aa/sgi/commits/efb16f0))
* Ajustes e add do campo "Subtotal" na aba de recebimento da parcela (SGINV-2057). ([4acf7d5](http://git.fasolti.com.br/aa/sgi/commits/4acf7d5))
* Ajustes e add nova linha na aba controle do faturamento da nota fiscal (SGINV-2053). ([9540948](http://git.fasolti.com.br/aa/sgi/commits/9540948))
* Ajustes lote interface portal oi (SGINV-2069). ([a279503](http://git.fasolti.com.br/aa/sgi/commits/a279503))
* Ajustes na aba controles na exibição da nota fiscal (SGINV-2053). ([add0813](http://git.fasolti.com.br/aa/sgi/commits/add0813))
* Ajustes na impressão da nota fiscal (SGINV-1978). ([89df0ca](http://git.fasolti.com.br/aa/sgi/commits/89df0ca))
* ajustes nos campos e nos calculos dos tributos da impressão da nota fiscal (SGINV-2059). ([3c5cd0a](http://git.fasolti.com.br/aa/sgi/commits/3c5cd0a))
* Ajustes para tela de nota fiscal (SGINV-1980). ([00cd110](http://git.fasolti.com.br/aa/sgi/commits/00cd110))
* Atualiza a data de agendamento da nota saida parcela (SGINV-2026). ([ef746f9](http://git.fasolti.com.br/aa/sgi/commits/ef746f9))
* Atualização (SGINV-2003) ([322f055](http://git.fasolti.com.br/aa/sgi/commits/322f055))
* Botão cancelar (SGINV-2020). ([1786520](http://git.fasolti.com.br/aa/sgi/commits/1786520))
* Carregar combo da forma de pagamento como "Parcela única", nova nota fiscal (SGINV-2052). ([c3b4dc0](http://git.fasolti.com.br/aa/sgi/commits/c3b4dc0))
* editar tributo (SGINV-2020). ([80720ec](http://git.fasolti.com.br/aa/sgi/commits/80720ec))
* excluir tributo (SGINV-2020). ([24ad267](http://git.fasolti.com.br/aa/sgi/commits/24ad267))
* Faturamento > Faturamento", modo de exibição, aba "INFO. COMPLEMENTARES (SGINV-1998). ([063b5a5](http://git.fasolti.com.br/aa/sgi/commits/063b5a5))
* Gravar dados de att valor na tabela faturamento etapa (SGINV-2022). ([de859b5](http://git.fasolti.com.br/aa/sgi/commits/de859b5))
* Mudança da fonte do tooltip na aba de Etapas da tela de exibição do Faturamento (SGINV-1973). ([4098b7d](http://git.fasolti.com.br/aa/sgi/commits/4098b7d))
* Novos campos e dialog para o agendamento da nota saida parcela (SGINV-2025). ([fdd60c6](http://git.fasolti.com.br/aa/sgi/commits/fdd60c6))
* Numeração de Nota Fiscal (SGINV-2064). ([1554d7b](http://git.fasolti.com.br/aa/sgi/commits/1554d7b))
* Otimização da aba de notas fiscais no faturamento (SGINV-1967). ([a188556](http://git.fasolti.com.br/aa/sgi/commits/a188556))
* **faturamento:** ajustar pesquisa de notas fiscais para a visualização de Faturamento; ajustar visual da tela de etapas (SGINV-1976) ([c227ad6](http://git.fasolti.com.br/aa/sgi/commits/c227ad6))
* Paginação da aba de etapas do faturamento (SGINV-1961). ([925e64c](http://git.fasolti.com.br/aa/sgi/commits/925e64c))
* Paginação de notas fiscais na aba de NF em faturamento (SGINV-1961). ([e8b2bbe](http://git.fasolti.com.br/aa/sgi/commits/e8b2bbe))
* **FT/NF:** ajustar impressão da NF (SGINV-2054) ([168ee23](http://git.fasolti.com.br/aa/sgi/commits/168ee23))
* Refatoramento na numeração de nota fiscal (SGINV-2064). ([854ee1f](http://git.fasolti.com.br/aa/sgi/commits/854ee1f))
* Remoção da seção "Prestador de Serviços" da tela de impressão de nota fiscal (SGINV-2059). ([ea3eaf5](http://git.fasolti.com.br/aa/sgi/commits/ea3eaf5))
* Retirado a restrição da data do pagamento para o internet banking (SGINV-1876). ([7f62901](http://git.fasolti.com.br/aa/sgi/commits/7f62901))
* salvar tributo (SGINV-2020). ([fbafb08](http://git.fasolti.com.br/aa/sgi/commits/fbafb08))
* Troca do texto de exibição do campo Subcliente (SGINV-1997). ([1c32ed7](http://git.fasolti.com.br/aa/sgi/commits/1c32ed7))



## 5.8.2 (2017-10-05)



## 5.8.1 (2017-09-21)



# 5.4.0 (2016-10-18)
