# Tomcat para executar os WARs frontend e rest localmente (ver docker-compose.web.yml).
# O datasource jdbc/SGIDB vem do server.xml de build/images/web, parametrizado por -Ddb.*.
FROM tomcat:9.0-jdk11-temurin

RUN rm -rf /usr/local/tomcat/webapps/ROOT* /usr/local/tomcat/webapps/docs /usr/local/tomcat/webapps/examples
ADD https://repo1.maven.org/maven2/org/postgresql/postgresql/42.2.5/postgresql-42.2.5.jar /usr/local/tomcat/lib/
COPY build/images/web/server.xml /usr/local/tomcat/conf/server.xml

EXPOSE 8080
