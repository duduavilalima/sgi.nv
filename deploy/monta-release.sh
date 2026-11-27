#!/bin/bash
# Monta uma release da SGI.nv para /opt/sgi-nv/releases/<versão>/ (T-023; a T-024 e a E6 a usam).
#
# Uso: deploy/monta-release.sh <diretório de saída>
#
# Antes, gere os artefatos no JDK 17 (T-005), com os perfis do pipeline:
#   SERVER_URL=http://10.0.2.10:8080 CAS_EXTERNAL_URL=http://10.8.0.1:8090 \
#     CAS_INTERNAL_URL=http://10.0.1.10:8090 \
#     mvn -B clean package -DskipTests -Pcompile.web,build.docker,build.unified
# O WAR vem de build/target/ (build.docker); o jar, de servers/jobs-service/target/ (ver abaixo).
set -euo pipefail

[ $# -eq 1 ] || { echo "uso: $0 <diretório de saída>" >&2; exit 64; }
RAIZ=$(cd "$(dirname "$0")/.." && pwd)
SAIDA=$1
[ ! -e "$SAIDA" ] || { echo "erro: $SAIDA já existe" >&2; exit 1; }

shopt -s nullglob
wars=("$RAIZ"/build/target/frontend-*.war)
[ ${#wars[@]} -eq 1 ] || { echo "erro: esperado um build/target/frontend-*.war, achados ${#wars[@]} (gere os artefatos antes)" >&2; exit 1; }
WAR=${wars[0]}
# O jar vem do módulo, não de build/target: o build.docker copia o jar antes do repackage do Spring
# Boot (as duas execuções estão na fase package), e o de build/target fica sem as dependências.
JAR=$RAIZ/servers/jobs-service/target/jobs-service.jar
[ -f "$JAR" ] || { echo "erro: falta servers/jobs-service/target/jobs-service.jar (gere os artefatos antes)" >&2; exit 1; }

conteudo() { unzip -l "$1" | awk '{print $4}'; }
# Sem o build.unified o frontend não sobe e não traz o rest (T-005): recusa o WAR sem os Resources.
if ! conteudo "$WAR" | grep -E 'sgi/resources/.*Resource\.class' >/dev/null; then
  echo "erro: $(basename "$WAR") sem o rest embutido (gere com -Pbuild.docker,build.unified)" >&2
  exit 1
fi
# Sem o repackage, o jar não tem as dependências e não roda com java -jar.
if ! conteudo "$JAR" | grep -E '^BOOT-INF/lib/' >/dev/null; then
  echo "erro: $(basename "$JAR") sem BOOT-INF/lib (não passou pelo repackage do Spring Boot)" >&2
  exit 1
fi

mkdir -p "$SAIDA/frontend" "$SAIDA/jobs" "$SAIDA/artefatos"
cp "$RAIZ/deploy/docker-compose.yml" "$RAIZ/deploy/.env.example" "$SAIDA/"
cp "$RAIZ/deploy/frontend/Dockerfile" "$RAIZ/build/images/web/server.xml" "$SAIDA/frontend/"
cp "$RAIZ/deploy/jobs/Dockerfile" "$SAIDA/jobs/"
cp "$RAIZ/deploy/espera.sh" "$SAIDA/frontend/"
cp "$RAIZ/deploy/espera.sh" "$SAIDA/jobs/"
cp "$WAR" "$SAIDA/artefatos/frontend.war"
cp "$JAR" "$SAIDA/artefatos/jobs-service.jar"
git -C "$RAIZ" rev-parse HEAD >"$SAIDA/VERSAO" 2>/dev/null || true
echo "release montada em $SAIDA ($(du -sh "$SAIDA" | cut -f1))"
