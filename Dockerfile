FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S nonroot \
    && adduser -S nonroot -G nonroot
WORKDIR /app
COPY target/*.jar app.jar
USER nonroot
ENTRYPOINT ["java","-jar","app.jar"]