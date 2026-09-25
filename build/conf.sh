#!/usr/bin/env bash

export PROJECT_GROUP="sginv"
PROJECT_ROOT_DIR="${CI_PROJECT_DIR:-"$(cd "$(dirname "$0")/.."; pwd)"}"
IMAGES_DIR="${PROJECT_ROOT_DIR}/build/images"
TARGET_DIR="${PROJECT_ROOT_DIR}/build/target"

export CI_PROJECT_NAMESPACE="${CI_PROJECT_NAMESPACE:-"aa"}"
export CI_PROJECT_NAME="${CI_PROJECT_NAME:-"sgi.nv"}"

# Configuraçõe que podem ser alteradas pelo ambiente
SGI_DOCKER_GROUP="docker.fasolti.com.br"
M2_IMAGE=${M2_IMAGE:-"maven:3-jdk-11-slim"}
M2_PROFILES=${M2_PROFILES:-"compile.web,build.docker,build.unified"}
MAVEN_CLI_OPTS=${MAVEN_CLI_OPTS:-"-q"}

if ! ip route | grep docker0 >/dev/null; then
	printf 'Serviço do Docker não encontrado!\n' >&2
	exit 1
fi

HOST_IP="$(ip route | grep docker0 | awk '{print $9}' | grep -E -v '^\s?$')"
USER_HOME="/var/maven"
YARN_CACHE_FOLDER="/var/cache/yarn"
M2_DIR="${USER_HOME}/.m2"
INTERNAL_PROJECT_DIR="/usr/src/sgi"

DOCKER_BASE_CMD="docker run \
	--rm --name sgi-compiler \
	--user ${UID} \
	-e MAVEN_CONFIG=${M2_DIR} \
	-e HOME=${M2_DIR} \
	-e YARN_CACHE_FOLDER=${YARN_CACHE_FOLDER} \
	--add-host=db:${HOST_IP} \
	-v "$(pwd)/..":${INTERNAL_PROJECT_DIR} \
	-v "$(pwd)/cache/yarn":${YARN_CACHE_FOLDER}
	-v "$(pwd)/m2":${M2_DIR}"

DOCKER_BUILD_CMD="${DOCKER_BASE_CMD} \
	-w ${INTERNAL_PROJECT_DIR} \
	${M2_IMAGE}"

DOCKER_CMD="${DOCKER_BASE_CMD} \
	-w ${INTERNAL_PROJECT_DIR} \
	${M2_IMAGE}"

list_project_artifacts() {
	find "${PROJECT_ROOT_DIR}" \
		-maxdepth 3 \
		-type f \
		-name '*.war' \
		-not -path "${PROJECT_ROOT_DIR}/build/*" \
		"$@"
}

find_image_dirs() {
	find "$IMAGES_DIR" -maxdepth 1 -mindepth 1 -type d
}

[ ! -e "${TARGET_DIR}" ] && mkdir -p "${TARGET_DIR}"

# Garante que o diretório existe para que não seja criado pelo container como root
mkdir -p m2/repository
mkdir -p cache/yarn

# Copia a configuração do Maven
cp -f settings.xml m2/
