#!/usr/bin/env bash

set -e

. ./conf.sh

# TODO Rever o uso do --cache-from
#CACHE_TAG=${1}
#
#if [[ "$CACHE_TAG" != "" ]]; then
#	CACHE_CMD="--cache-from ${SGI_IMAGE}:${CACHE_TAG}"
#	# Tenta baixar a imagem para o cache mas não falha se não conseguir
#	docker pull ${SGI_IMAGE}:${CACHE_TAG} || true
#fi

for IMAGE_DIR in $(find_image_dirs); do
	IMAGE_BUILD_DIR="${IMAGE_DIR}/build"
	IMAGE_BUILD_TARGET_DIR="${IMAGE_BUILD_DIR}/target"
	mkdir -p "${IMAGE_BUILD_DIR}"
	cp -rv -T "${TARGET_DIR}" "${IMAGE_BUILD_TARGET_DIR}"
done

export SGI_CI=true

if [[ -z "${CI_JOB_ID}" ]]; then
	export SGI_CI=false
	export CI_COMMIT_REF_NAME="${CI_COMMIT_REF_NAME:-"$(git symbolic-ref --short HEAD)"}"
	export CI_COMMIT_SHORT_SHA="${CI_COMMIT_SHORT_SHA:-"$(git rev-parse --short HEAD)"}"
	export CI_COMMIT_SHA="${CI_COMMIT_SHA:-"$(git rev-parse HEAD)"}"

	export SGI_PHASE="${SGI_PHASE:-"staging"}"
fi

SGI_PHASE="${SGI_PHASE:?Faltando SGI_PHASE.}"
CI_COMMIT_REF_NAME="${CI_COMMIT_REF_NAME:?Faltando CI_COMMIT_REF_NAME.}"
CI_COMMIT_SHORT_SHA="${CI_COMMIT_SHORT_SHA:?Faltando CI_COMMIT_SHORT_SHA.}"
CI_COMMIT_SHA="${CI_COMMIT_SHA:?Faltando CI_COMMIT_SHA.}"
CI_PROJECT_NAMESPACE="${CI_PROJECT_NAMESPACE:?Faltando CI_PROJECT_NAMESPACE.}"
CI_PROJECT_NAME="${CI_PROJECT_NAME:?Faltando CI_PROJECT_NAME.}"

IMAGE_TAG="${SGI_PHASE}-${CI_COMMIT_SHORT_SHA}"
IMAGE_TAGS="${SGI_PHASE}"

# FIXME Atualizar o Gitlab para a versão 12.3 e migrar a lógica abaixo para o gitlab-ci.common.yml usando "rules:if"
if [[ ! -z "${CI_COMMIT_TAG}" ]]; then
	REF_TYPE="tag"
	IMAGE_TAGS="${IMAGE_TAGS};${SGI_PHASE}-${CI_COMMIT_REF_NAME}"
else
	REF_TYPE="branch"
fi

for IMAGE_DIR in $(find_image_dirs); do
	(
		export IMAGE_DIR
		export IMAGE_NAME="$(basename "$IMAGE_DIR")"
		export PROJECT_NAME="${IMAGE_NAME}"
		export SGI_IMAGE="${SGI_DOCKER_GROUP}/${PROJECT_GROUP}-${PROJECT_NAME}"
		export REF_TYPE

		printf '*** INFO: Criando imagem "%s": estágio="%s", ref="%s", commit="%s", docker-tags="%s"\n' \
			"${SGI_IMAGE}" \
			"${SGI_PHASE}" \
			"${CI_COMMIT_REF_NAME}" \
			"${CI_COMMIT_SHORT_SHA}" \
			"${IMAGE_TAG};${IMAGE_TAGS}" \
			>&2

		cd "$IMAGE_DIR"
		docker build ${CACHE_CMD} ${BUILD_FLAGS} --pull --quiet \
			--tag "${SGI_IMAGE}:${IMAGE_TAG}" \
			--build-arg SGI_CI \
			--build-arg CI_PIPELINE_ID \
			--build-arg CI_PIPELINE_IID \
			--build-arg GITLAB_USER_LOGIN \
			--build-arg CI_PIPELINE_SOURCE \
			--build-arg PROJECT_NAME \
			--build-arg PROJECT_GROUP \
			--build-arg SGI_PHASE \
			--build-arg CI_PROJECT_NAMESPACE \
			--build-arg CI_PROJECT_NAME \
			--build-arg REF_TYPE \
			--build-arg CI_COMMIT_REF_NAME \
			--build-arg CI_COMMIT_SHA \
			--build-arg CI_COMMIT_SHORT_SHA \
			.

		if [ -z "${NOPUSH}" ]; then
			printf '*** INFO: Enviando imagem ao Nexus: %s\n' "${SGI_IMAGE}:${IMAGE_TAG}" >&2
			docker push "${SGI_IMAGE}:${IMAGE_TAG}"
		fi

		IFS=";"; for tag in ${IMAGE_TAGS}; do
			printf '*** INFO: Adicionando tag: %s\n' "${SGI_IMAGE}:${tag}" >&2
			docker tag "${SGI_IMAGE}:${IMAGE_TAG}" "${SGI_IMAGE}:${tag}"
			if [ -z "${NOPUSH}" ]; then
				printf '*** INFO: Enviando imagem ao Nexus; tag: %s\n' "${SGI_IMAGE}:${tag}" >&2
				docker push "${SGI_IMAGE}:${tag}"
			fi
		done
	)
done
