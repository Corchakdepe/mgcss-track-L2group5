FROM eclipse-temurin:17-jre AS builder
RUN jlink --add-modules java.base,java.compiler,java.desktop,java.instrument,java.logging,java.management,java.naming,java.net.http,java.prefs,java.scripting,java.security.jgss,java.security.sasl,java.sql,java.xml,jdk.crypto.ec,jdk.crypto.cryptoki,jdk.unsupported,jdk.zipfs --output /jre --strip-debug --no-man-pages --no-header-files --compress=2

FROM debian:bookworm-slim
COPY --from=builder /jre /jre
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["/jre/bin/java", "-jar", "app.jar"]
