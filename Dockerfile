# syntax=docker/dockerfile:1
FROM openjdk:17.0.1-jdk-slim
WORKDIR /opt/pricer
COPY target/tgbot.jar .
ENTRYPOINT ["java", "-jar", "/opt/pricer/tgbot.jar"]