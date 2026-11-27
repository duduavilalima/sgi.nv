# Tomcat do frontend da SGI.nv no deploy (T-023). O WAR entra por volume (deploy/docker-compose.yml).
# O datasource jdbc/SGIDB vem do server.xml de build/images/web, que o monta-release.sh copia para
# cá, parametrizado por -Ddb.* no JAVA_OPTS.
FROM tomcat:9.0-jdk11-temurin

RUN rm -rf /usr/local/tomcat/webapps/*
ADD --checksum=sha256:7ffa46f8c619377cdebcd17721b6b21ecf6659850179f96fec3d1035cf5a0cdc \
    https://repo1.maven.org/maven2/org/postgresql/postgresql/42.2.5/postgresql-42.2.5.jar /usr/local/tomcat/lib/
COPY server.xml /usr/local/tomcat/conf/server.xml

EXPOSE 8080
