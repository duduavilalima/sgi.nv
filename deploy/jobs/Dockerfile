# jobs-service da SGI.nv no deploy (T-023), como testado no S4 (T-005): JRE 11, o mesmo runtime do
# Tomcat do frontend. O jar entra por volume (deploy/docker-compose.yml).
FROM eclipse-temurin:11-jre

# Os crontabs dos jobs supõem o horário de Brasília; o container vem em UTC.
ENV TZ=America/Sao_Paulo
# Diretórios de interface do InativacaoUsuarioJob (padrão /var/lib/sgi/interface/rh/ e .../history/).
RUN mkdir -p /var/lib/sgi/interface/rh/history
WORKDIR /app
# Forma exec: propriedades -D com espaço (crontabs) vão como argumentos antes de -jar; opções da JVM
# (ex. -Xmx) por JAVA_TOOL_OPTIONS.
ENTRYPOINT ["java", "-jar", "/app/jobs-service.jar"]
