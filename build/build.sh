#!/usr/bin/env bash

set -e

. ./conf.sh

${DOCKER_BUILD_CMD} mvn ${MAVEN_CLI_OPTS} -P ${M2_PROFILES} -Duser.home=${USER_HOME} package
