- [Executando com Docker (Tomcat)](#executando-com-docker-tomcat)
- [Servidor de JOBs](#servidor-de-jobs)
	- [Configurando o RUN para executar o servidor de JOBS](#configurando-o-run-para-executar-o-servidor-de-jobs)
	- [Executando localmente pelo script](#executando-localmente-pelo-script)
- [Executando aplicação](#executando-aplica%C3%A7%C3%A3o)
	- [Select run (Maven):](#select-run-maven)
- [Teste](#teste)
	- [Comando MAVEN:](#comando-maven)
	- [Select run (Maven):](#select-run-maven-1)
	- [Select run (Remote):](#select-run-remote)
- [Executando testes](#executando-testes)
	- [Select run (Maven):](#select-run-maven-2)
- [Padrão de mensagem de commit (CHANGELOG automatizados):](#padr%C3%A3o-de-mensagem-de-commit-changelog-automatizados)
		- [Referências:](#refer%C3%AAncias)
- [Gerando o CHANGELOG do projeto](#gerando-o-changelog-do-projeto)

# Executando com Docker (Tomcat)

Executa os WARs `frontend` e `rest` em containers Tomcat. O Postgres e o servidor de e-mail (ex.: MailHog) **não** sobem no compose: devem estar rodando no host.

Pré-requisitos:

- Postgres e SMTP acessíveis no host.
- Arquivo `.env` na raiz (não versionado) com as variáveis `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASS`, `MAIL_HOST`, `MAIL_PORT`, `MAIL_STARTTLS`, `MAIL_SSL` e, em testes, `MAIL_REDIRECT_TO`.

```bash
mvn package -DskipTests                           # gera servers/*/target/*.war
docker compose -f docker-compose.web.yml up --build
```

| Serviço    | Porta | Contexto Tomcat |
|------------|-------|-----------------|
| `frontend` | 8080  | `/sgi/nv`       |
| `rest`     | 8081  | `/sgi/rest`     |

Os containers acessam o Postgres e o SMTP do host via `host.docker.internal`. Para parar: `docker compose -f docker-compose.web.yml down`.

# Servidor de JOBs

Os jobs rodam no `jobs-service` (Spring Boot, `JobScheduler`). Não há mais RabbitMQ nem fila de e-mail.

## Configurando o RUN para executar o servidor de JOBS

No IntelliJ IDE configurar:

- Name: `JOBS`
- Working_directory: `Raiz do projeto sgi.nv`
- Command line: `test -pl servers/jobs-service -am`
- Profiles: `debug.jobs`

## Executando localmente pelo script

Após `mvn package -DskipTests`, execute `./jobs_service_local`. O script carrega o `.env` e inicia `servers/jobs-service/target/jobs-service.jar`. Opções `-D` extras da JVM podem ser passadas como argumento, ex.: `./jobs_service_local -Djobs.scheduler.enabled=false`.

# Executando aplicação

Criar comando MAVEN no intellij

## Select run (Maven): 

Name: `FRONTEND`

Working Diretory: `~/git/sgi.nv`

Command line: `test`

Profiles: `debug.jetty`

Aba Runner (VM options): `-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005`

`Profiles: debug.jetty`

# Teste

Teste não serão usados neste momento.

## Comando MAVEN:
`Work directory: /home/FASOLTI/daniel.oliveira/git/sgi.nv`

`Command line: test`

`Profiles: test.unit`

Adicionar na aba RUNNER mais memória: `VM Options: -Xmx512M -XX:MaxPermSize=512M`

## Select run (Maven):

Name: `COMPILE.WEB`

Working Diretory: `~/git/sgi.nv/servers/frontend`

Command line: `test`

Profiles: `compile.web`

## Select run (Remote):

Name: `DEBUG`

# Executando testes

Teste não serão usados neste momento.

## Select run (Maven): 

Name: `TESTE`

Working Diretory: `~/git/sgi.nv`

Command line: `test`

Profiles: `test.unit`

# Padrão de mensagem de commit (CHANGELOG automatizados):

O projeto terá o controle de versão e a geração do CHANGELOG automatizados com mensagens de confirmação  convencionais seguindo o padrão estabelecido por este documento.

`Lembrando: A mensagem de commit será pré-estabelecida no issue`

Para os commites do projeto, se o mensagem do commite não for definida no issue, usar o padrão definido abaixo:

_Para bugs:_

```sh
git commit -m "fix: texto_do_que_foi_feito_no_issue (número_do_issue_com_#)"
```

_Para os demais:_

```sh
git commit -m "feat: texto_do_que_foi_feito_no_issue (número_do_issue_com_#)"
```

_Para escopo opcional de um commit:_

```sh
git commit -m "feat(optional_scope): texto_do_que_foi_feito_no_issue (número_do_issue_com_#)"
```

_Algumas regras:_

1. Commits deve ser prefixado com dockerum tipo, o qual consiste de um substantivo, `feat`, `fix`, etc, seguida de dois pontos e um espaço.
2. O tipo `feat`DEVE ser usado quando um commit adiciona um novo recurso ao seu aplicativo ou biblioteca.
3. O tipo `fix` DEVE ser usado quando um commit representa uma correção de bug para seu aplicativo.
4. Um escopo opcional pode ser fornecido após um tipo. Um escopo é uma frase que descreve uma seção da base de código entre parênteses, por exemplo, `fix(parser)`:

### Referências:
[Standard Version](https://github.com/conventional-changelog/standard-version/blob/master/README.md)
[Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0-beta.2/)

# Gerando o CHANGELOG do projeto

```sh
npm run release -- --release-as 5.13.6-rc.0  --dry-run

npm run release -- --release-as patch --dry-run

npm run release -- --release-as minor  --dry-run

npm run release -- --release-as major  --dry-run
```
# VS Code JSLint extension
[VS Code JSLint extension](https://marketplace.visualstudio.com/items?itemName=ajhyndman.jslint)

# Jshint
[jshint](https://marketplace.visualstudio.com/items?itemName=dbaeumer.jshint)

## Exemplo de .jshintrc no root da aplicação
```json
{
  "esversion": 6,
  "enforcing": {
    "bitwise": true,
    "freeze": true,
    "camelcase": true,
    "curly": true,
    "eqeqeq": true,
    "futurehostile": true,
    "notypeof": true,
    "es3": false,
    "es5": false,
    "forin": true,
    "funcscope": true,
    "immed": true,
    "iterator": true,
    "newcap": true,
    "noarg": true,
    "nocomma": true,
    "noempty": true,
    "nonbsp": true,
    "nonew": true,
    "undef": true,
    "singleGroups": false,
    "varstmt": false,
    "enforceall": false
  },
  "relaxing": {
    "asi": true,
    "multistr": true,
    "debug": true,
    "boss": true,
    "evil": true,
    "globalstrict": true,
    "plusplus": true,
    "proto": true,
    "scripturl": true,
    "sub": true,
    "supernew": true,
    "laxbreak": true,
    "laxcomma": true,
    "validthis": true,
    "withstmt": true,
    "moz": true,
    "noyield": true,
    "eqnull": true,
    "lastsemic": true,
    "loopfunc": true,
    "expr": true,
    "esnext": true,
    "elision": true
  },
  "environments": {
    "mootools": true,
    "couch": true,
    "jasmine": true,
    "jquery": true,
    "node": true,
    "qunit": true,
    "rhino": true,
    "shelljs": true,
    "prototypejs": true,
    "yui": true,
    "mocha": true,
    "module": true,
    "wsh": true,
    "worker": true,
    "nonstandard": true,
    "browser": true,
    "browserify": true,
    "devel": true,
    "dojo": true,
    "typed": true,
    "phantom": true
  },
  "obsolete": {
    "onecase": true,
    "regexp": true,
    "regexdash": true
  }
}
```

# VSCode Beautify
[HookyQR.beautify](https://marketplace.visualstudio.com/items?itemName=HookyQR.beautify)

[VSCodeBeautify Settings](https://github.com/HookyQR/VSCodeBeautify/blob/master/Settings.md)

## Exemplo de .jsbeautifyrc no root da aplicação
[Exemplo .jsbeautifyrc](https://gist.github.com/wzup/fc3254562236c1ec3f69)

# Ícones do Material

https://material.io/tools/icons/?icon=report_problem&style=baseline

# Depgraph-maven-plugin

Um plugin do Maven que gera gráficos de dependência em vários formatos (DOT, GML, PlantUML, JSON e Text)

[Depgraph-maven-plugin](https://github.com/ferstl/depgraph-maven-plugin)

### Comando

```
mvn depgraph:graph -DshowGroupIds -DshowVersions -DshowDuplicates -DshowConflicts -DcustomStyleConfiguration=../graph-style-configuration.json
```
