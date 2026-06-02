FROM eclipse-temurin:17-jre
RUN groupadd --system nonroot \
    && useradd --system --no-create-home -g nonroot nonroot
WORKDIR /app
COPY target/*.jar app.jar
USER nonroot
ENTRYPOINT ["java","-jar","app.jar"]