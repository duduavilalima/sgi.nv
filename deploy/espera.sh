#!/usr/bin/env bash
# Entrypoint dos containers da SGI.nv no deploy (T-024): só executa o comando quando as dependências
# respondem. Sem isso, com a db ou o CAS fora, o frontend e o jobs-service sobem e falham. Fica no
# container, e não no depends_on do compose, porque depois de um reboot o Docker religa os containers
# sozinho (restart: unless-stopped) e ignora o depends_on: a espera tem de valer nos dois casos.
#
#   ESPERA_TCP="host:porta ..."    portas TCP que precisam aceitar conexão (ex. a db:5432)
#   ESPERA_HTTP="http://... ..."   URLs que precisam responder 2xx ou 3xx (ex. o login do CAS)
#   ESPERA_INTERVALO=5             segundos entre tentativas
#
# Espera sem limite: o serviço fica parado, com uma linha de log por minuto, e sobe assim que tudo
# responde. Usa só o bash (/dev/tcp) e o timeout, que as duas imagens têm.
set -euo pipefail

INTERVALO=${ESPERA_INTERVALO:-5}
read -r -a tcps <<<"${ESPERA_TCP:-}"
read -r -a urls <<<"${ESPERA_HTTP:-}"

log() { echo "[espera] $*" >&2; }

for u in "${urls[@]}"; do
  [[ $u == http://* ]] || { log "URL não suportada (só http://): $u"; exit 64; }
done
for t in "${tcps[@]}"; do
  [[ $t == *:* ]] || { log "esperado host:porta: $t"; exit 64; }
done

tcp_ok() { # host:porta
  timeout 3 bash -c 'exec 3<>"/dev/tcp/$1/$2"' _ "${1%:*}" "${1##*:}" 2>/dev/null
}

http_ok() { # http://host[:porta]/caminho -> 2xx ou 3xx
  local resto=${1#http://} hp caminho host porta status
  hp=${resto%%/*}
  caminho=/
  [[ $resto == */* ]] && caminho=/${resto#*/}
  host=${hp%%:*}
  porta=80
  [[ $hp == *:* ]] && porta=${hp##*:}
  status=$(timeout 5 bash -c '
    exec 3<>"/dev/tcp/$1/$2" || exit 1
    # Com User-Agent: sem ele, o CAS do SGI responde 500 ao /sgi/security/login (visto na T-024).
    printf "GET %s HTTP/1.0\r\nHost: %s\r\nUser-Agent: sgi-nv-espera\r\nConnection: close\r\n\r\n" "$3" "$4" >&3
    head -n 1 <&3' _ "$host" "$porta" "$caminho" "$hp" 2>/dev/null) || return 1
  [[ $status =~ ^HTTP/[0-9.]+\ [23][0-9][0-9] ]]
}

pendentes() {
  local p=()
  for t in "${tcps[@]}"; do tcp_ok "$t" || p+=("$t"); done
  for u in "${urls[@]}"; do http_ok "$u" || p+=("$u"); done
  echo "${p[*]}"
}

inicio=$SECONDS
ultimo_log=-60
while falta=$(pendentes) && [ -n "$falta" ]; do
  if ((SECONDS - ultimo_log >= 60)); then
    log "aguardando: $falta ($((SECONDS - inicio)) s)"
    ultimo_log=$SECONDS
  fi
  sleep "$INTERVALO"
done
((${#tcps[@]} + ${#urls[@]} == 0)) || log "dependências no ar em $((SECONDS - inicio)) s: ${tcps[*]} ${urls[*]}"

exec "$@"
