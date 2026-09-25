FROM tomcat:8.5-jdk11-openjdk

RUN rm -rf /usr/local/tomcat/webapps/ROOT*
COPY build/target/frontend-*.war /usr/local/tomcat/webapps/
COPY server.xml /usr/local/tomcat/conf/

ARG SGI_CI
ARG CI_PIPELINE_ID
ARG CI_PIPELINE_IID
ARG GITLAB_USER_LOGIN
ARG CI_PIPELINE_SOURCE
ARG PROJECT_NAME
ARG PROJECT_GROUP
ARG SGI_PHASE
ARG CI_PROJECT_NAMESPACE
ARG CI_PROJECT_NAME
ARG REF_TYPE
ARG CI_COMMIT_REF_NAME
ARG CI_COMMIT_SHA
ARG CI_COMMIT_SHORT_SHA

ENV SGI_CI_PIPELINE_ID="${CI_PIPELINE_ID}" \
	SGI_CI_PIPELINE_IID="${CI_PIPELINE_IID}" \
	SGI_CI_USER_LOGIN="${GITLAB_USER_LOGIN}" \
	SGI_CI_PIPELINE_SOURCE="${CI_PIPELINE_SOURCE}" \
	SGI_PROJECT_GROUP="${PROJECT_GROUP}" \
	SGI_PROJECT_NAME="${PROJECT_NAME}" \
	SGI_PROJECT_PHASE="${SGI_PHASE}" \
	SGI_GIT_NAMESPACE="${CI_PROJECT_NAMESPACE}" \
	SGI_GIT_NAME="${CI_PROJECT_NAME}" \
	SGI_GIT_REF_TYPE="${REF_TYPE}" \
	SGI_GIT_REF_NAME="${CI_COMMIT_REF_NAME}" \
	SGI_GIT_COMMIT_SHA="${CI_COMMIT_SHA}" \
	SGI_GIT_COMMIT_SHORT_SHA="${CI_COMMIT_SHORT_SHA}"

LABEL fasolti="" \
	fasolti.ci="${SGI_CI}" \
	fasolti.ci.id="${CI_PIPELINE_ID}" \
	fasolti.ci.iid="${CI_PIPELINE_IID}" \
	fasolti.ci.user="${GITLAB_USER_LOGIN}" \
	fasolti.ci.source="${CI_PIPELINE_SOURCE}" \
	fasolti.project="" \
	fasolti.project.group="${PROJECT_GROUP}" \
	fasolti.project.name="${PROJECT_NAME}" \
	fasolti.project.phase="${SGI_PHASE}" \
	fasolti.project.git.group="${CI_PROJECT_NAMESPACE}" \
	fasolti.project.git.name="${CI_PROJECT_NAME}" \
	fasolti.project.git.ref-type="${REF_TYPE}" \
	fasolti.project.git.ref="${CI_COMMIT_REF_NAME}" \
	fasolti.project.git.commit="${CI_COMMIT_SHA}" \
	fasolti.project.git.commit-short="${CI_COMMIT_SHORT_SHA}"

EXPOSE 8080
