#!/bin/bash
# Teste do compose de deploy (T-023) sem AWS: monta uma release com os artefatos de build/target,
# sobe um postgres:17 descartável e o compose com um .env de teste, e confere:
# - docker compose config sem erro, e recusa sem MAIL_REDIRECT_TO;
# - o mesmo bloco MAIL_* no frontend e no jobs-service;
# - o SMTP do SES resolvendo para loopback nos dois;
# - portas publicadas só no IP de SGINV_IP_PRIVADO (8080 e 8025), nada na 1025;
# - o frontend no ar (/sgi/nv/index.html 200) e o MailHog (/api/v2/messages 200);
# - um e-mail enviado de dentro do frontend chegando ao MailHog;
# - o logoutRequest do CAS (POST sem token CSRF) chegando ao SingleSignOutFilter, e um POST comum
#   sem token ainda recusado (T-050);
# - a espera (T-024): o compose sobe com a db e o CAS fora, o frontend e o jobs-service aguardam; com
#   a db no ar, só o jobs-service sobe; com o CAS (um httpd falso) no ar, o frontend sobe.
# Nunca usa o .env da raiz do repositório (aponta para um banco real; T-005).
#
#   deploy/testa-compose.sh          (gere os artefatos antes; ver monta-release.sh)
set -euo pipefail

RAIZ=$(cd "$(dirname "$0")/.." && pwd)
TMP=$(mktemp -d)
REL=$TMP/release
BANCO=sgi-nv-teste-banco
CAS=sgi-nv-teste-cas
IP=127.0.0.1
export COMPOSE_PROJECT_NAME=sgi-nv-teste

falhas=0
ok() { echo "ok   $*"; }
falha() { echo "FALHA $*"; falhas=$((falhas + 1)); }

limpa() {
  [ -d "$REL" ] && (cd "$REL" && docker compose down -v --remove-orphans >/dev/null 2>&1) || true
  docker rm -f "$BANCO" "$CAS" >/dev/null 2>&1 || true
  rm -rf "$TMP"
}
trap limpa EXIT

for porta in 8080 8025; do
  ! (exec 3<>"/dev/tcp/$IP/$porta") 2>/dev/null || { echo "erro: $IP:$porta já está em uso" >&2; exit 1; }
done

"$RAIZ/deploy/monta-release.sh" "$REL" >/dev/null

# Banco descartável e CAS falso, publicados no gateway da bridge padrão do Docker (alcançável dos
# containers). As portas são escolhidas antes, porque os dois só sobem depois do compose (espera).
GW=$(docker network inspect bridge --format '{{(index .IPAM.Config 0).Gateway}}')
porta_livre() { python3 -c 'import socket,sys; s=socket.socket(); s.bind((sys.argv[1], 0)); print(s.getsockname()[1])' "$GW"; }
PORTA_BANCO=$(porta_livre)
PORTA_CAS=$(porta_livre)
# Senha do banco descartável, gerada a cada execução: nenhuma senha fica escrita no repositório.
SENHA_BANCO=$(od -An -N16 -tx1 /dev/urandom | tr -d ' \n')

cat >"$REL/.env" <<FIM
SGINV_IP_PRIVADO=$IP
CAS_INTERNAL_URL=http://$GW:$PORTA_CAS
DB_HOST=$GW
DB_PORT=$PORTA_BANCO
DB_NAME=sgi
DB_USERNAME=postgres
DB_PASSWORD=$SENHA_BANCO
FRONTEND_XMX=1g
JOBS_XMX=512m
MAIL_REDIRECT_TO=teste@mailhog.local
FIM
cd "$REL"

echo "== config"
docker compose config -q && ok "docker compose config sem erro" || falha "docker compose config"
if sed '/^MAIL_REDIRECT_TO=/d' .env >.env.sem && ! docker compose --env-file .env.sem config -q 2>/dev/null; then
  ok "sem MAIL_REDIRECT_TO o compose recusa"
else
  falha "sem MAIL_REDIRECT_TO o compose deveria recusar"
fi
rm -f .env.sem
if sed '/^CAS_INTERNAL_URL=/d' .env >.env.sem && ! docker compose --env-file .env.sem config -q 2>/dev/null; then
  ok "sem CAS_INTERNAL_URL o compose recusa"
else
  falha "sem CAS_INTERNAL_URL o compose deveria recusar"
fi
rm -f .env.sem
servicos=$(docker compose config --services | sort | tr '\n' ' ')
[ "$servicos" = "frontend jobs-service mailhog " ] && ok "serviços: $servicos" || falha "serviços: $servicos"

echo "== subida"
# O jobs-service lê a tabela parametro ao subir. Como no S4 (T-005), só neste teste o Hibernate cria
# as tabelas, vazias, no banco descartável; o compose de deploy não tem isso.
cat >teste.yml <<'FIM'
services:
  jobs-service:
    environment:
      JAVA_TOOL_OPTIONS: "-Xmx512m -Dhibernate.hbm2ddl.auto=update"
FIM
export COMPOSE_FILE=docker-compose.yml:teste.yml
docker compose up -d --build --quiet-pull >/dev/null 2>&1 || { docker compose up -d --build; exit 1; }

echo "== espera (T-024)"
# Sem -q: o grep lê tudo, e o docker compose logs não leva SIGPIPE (com pipefail, seria falha).
log_tem() { docker compose logs "$1" 2>&1 | grep -F -- "$2" >/dev/null; }
reinicios() { docker inspect -f '{{.RestartCount}}' "$1"; }
URL_CAS="http://$GW:$PORTA_CAS/sgi/security/login"
sleep 15
log_tem frontend "[espera] aguardando: $GW:$PORTA_BANCO $URL_CAS" && ok "frontend aguarda a db e o CAS" ||
  falha "frontend sem a linha de espera pela db e pelo CAS"
log_tem jobs-service "[espera] aguardando: $GW:$PORTA_BANCO (" && ok "jobs-service aguarda só a db" ||
  falha "jobs-service sem a linha de espera pela db"
! log_tem frontend "Server version name" && [ "$(reinicios sgi-nv-frontend)" = 0 ] &&
  ok "Tomcat não iniciou e o container não reiniciou" || falha "frontend subiu ou reiniciou sem a db e o CAS"
! log_tem jobs-service "Starting JobsServiceApplication" && [ "$(reinicios sgi-nv-jobs)" = 0 ] &&
  ok "jobs-service não iniciou e o container não reiniciou" || falha "jobs-service subiu ou reiniciou sem a db"

docker run -d --name "$BANCO" -p "$GW:$PORTA_BANCO:5432" -e POSTGRES_PASSWORD="$SENHA_BANCO" -e POSTGRES_DB=sgi postgres:17 >/dev/null
for _ in $(seq 1 60); do docker exec "$BANCO" pg_isready -q -U postgres && break; sleep 1; done
for _ in $(seq 1 30); do log_tem jobs-service "[espera] dependências no ar" && break; sleep 2; done
log_tem jobs-service "[espera] dependências no ar" && ok "com a db no ar, o jobs-service segue" ||
  falha "jobs-service continua esperando com a db no ar"
sleep 10
! log_tem frontend "Server version name" && ok "com a db no ar e o CAS fora, o frontend continua esperando" ||
  falha "frontend subiu sem o CAS"

# CAS falso: 200 com User-Agent e 500 sem ele, como o CAS do SGI (visto na AWS, T-024).
cat >"$TMP/cas.sh" <<'FIM'
cat >/resposta.sh <<'R'
#!/bin/sh
ua=
while IFS= read -r l; do
  l=${l%$(printf '\r')}
  [ -z "$l" ] && break
  case $l in [Uu]ser-[Aa]gent:*) ua=1 ;; esac
done
if [ -n "$ua" ]; then
  printf 'HTTP/1.1 200 OK\r\nContent-Length: 6\r\nConnection: close\r\n\r\nlogin\n'
else
  printf 'HTTP/1.1 500 Internal Server Error\r\nContent-Length: 0\r\nConnection: close\r\n\r\n'
fi
R
chmod +x /resposta.sh
while true; do nc -l -p 8090 -e /resposta.sh; done
FIM
docker run -d --name "$CAS" -p "$GW:$PORTA_CAS:8090" -v "$TMP/cas.sh:/cas.sh:ro" busybox:1.36 sh /cas.sh >/dev/null
for _ in $(seq 1 30); do log_tem frontend "[espera] dependências no ar" && break; sleep 2; done
log_tem frontend "[espera] dependências no ar" && ok "com o CAS no ar, o frontend segue" ||
  falha "frontend continua esperando com a db e o CAS no ar"

echo "== portas"
publicadas=$(docker ps --filter "label=com.docker.compose.project=$COMPOSE_PROJECT_NAME" --format '{{.Ports}}' |
  tr ',' '\n' | { grep -oE '[0-9.:]+:[0-9]+->[0-9]+' || true; } | sed 's/^ *//' | sort -u | tr '\n' ' ')
[ "$publicadas" = "$IP:8025->8025 $IP:8080->8080 " ] && ok "publicadas só $publicadas" || falha "portas publicadas: $publicadas"

echo "== e-mail"
mail_de() { docker compose exec -T "$1" env | grep '^MAIL_' | sort; }
if [ "$(mail_de frontend)" = "$(mail_de jobs-service)" ] && mail_de frontend | grep -qx 'MAIL_HOST=mailhog'; then
  ok "MAIL_* iguais no frontend e no jobs-service (MAIL_HOST=mailhog)"
else
  falha "MAIL_* diferentes ou sem MAIL_HOST=mailhog"
fi
for s in frontend jobs-service; do
  resolvido=$(docker compose exec -T "$s" getent hosts email-smtp.us-east-1.amazonaws.com | awk '{print $1}' || true)
  [ "$resolvido" = 127.0.0.1 ] && ok "$s: SES resolve para 127.0.0.1" || falha "$s: SES resolve para '$resolvido'"
done

echo "== aplicações"
codigo() { curl -s -o /dev/null -w '%{http_code}' "$1"; }
for _ in $(seq 1 90); do [ "$(codigo "http://$IP:8080/sgi/nv/index.html")" = 200 ] && break; sleep 2; done
[ "$(codigo "http://$IP:8080/sgi/nv/index.html")" = 200 ] && ok "frontend: /sgi/nv/index.html 200" || falha "frontend fora do ar"
[ "$(codigo "http://$IP:8025/api/v2/messages")" = 200 ] && ok "MailHog: /api/v2/messages 200" || falha "MailHog fora do ar"

# Logout único (T-050): o CAS avisa por POST, servidor a servidor, sem token CSRF. Com o
# SingleSignOutFilter antes do CsrfFilter, o aviso é tratado (200); antes, o CSRF o barrava e o
# access-denied-handler respondia 405.
pedido='<samlp:LogoutRequest xmlns:samlp="urn:oasis:names:tc:SAML:2.0:protocol" ID="LR-1" Version="2.0"><saml:NameID xmlns:saml="urn:oasis:names:tc:SAML:2.0:assertion">teste</saml:NameID><samlp:SessionIndex>ST-1-teste-cas</samlp:SessionIndex></samlp:LogoutRequest>'
c=$(curl -s -o /dev/null -w '%{http_code}' --data-urlencode "logoutRequest=$pedido" "http://$IP:8080/sgi/nv/login/cas")
[ "$c" = 200 ] && ok "logoutRequest do CAS sem token CSRF: 200 (SingleSignOutFilter)" || falha "logoutRequest do CAS: $c"
c=$(curl -s -o /dev/null -w '%{http_code}' -d campo=valor "http://$IP:8080/sgi/nv/app/main.html")
[ "$c" != 200 ] && ok "POST comum sem token CSRF continua recusado ($c)" || falha "POST sem token CSRF aceito"
! (exec 3<>"/dev/tcp/$IP/1025") 2>/dev/null && ok "SMTP 1025 não publicado" || falha "SMTP 1025 publicado"

# SMTP do MailHog pela rede do compose, a partir do frontend (o bash do container fala TCP).
docker compose exec -T frontend bash -c '
  exec 3<>/dev/tcp/mailhog/1025
  for l in "HELO teste" "MAIL FROM:<suporte_sgi@copony.com.br>" "RCPT TO:<teste@mailhog.local>" DATA \
           "Subject: T-023" "" "teste do compose" . QUIT; do printf "%s\r\n" "$l" >&3; sleep 0.2; done
  cat <&3 >/dev/null' || true
total=$(curl -s "http://$IP:8025/api/v2/messages" | grep -o '"total":[0-9]*' | cut -d: -f2 || true)
[ "${total:-0}" -ge 1 ] && ok "e-mail do frontend chegou ao MailHog" || falha "e-mail não chegou ao MailHog"

echo "== logs"
if docker compose logs jobs-service 2>&1 | grep -q 'Started JobsServiceApplication'; then
  ok "jobs-service iniciou"
else
  for _ in $(seq 1 30); do docker compose logs jobs-service 2>&1 | grep -q 'Started JobsServiceApplication' && break; sleep 2; done
  docker compose logs jobs-service 2>&1 | grep -q 'Started JobsServiceApplication' && ok "jobs-service iniciou" || { falha "jobs-service não iniciou"; docker compose logs --tail 40 jobs-service; }
fi

echo
[ "$falhas" -eq 0 ] && echo "todos os casos passaram" || { echo "$falhas caso(s) falharam"; exit 1; }
