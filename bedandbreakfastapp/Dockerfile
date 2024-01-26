# multistage
# 1. Build stage
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY .mvn .mvn
COPY src src
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .

RUN mvn package -Dmaven.test.skip=true

# 2. Publish stage
FROM openjdk:21-jdk-slim 

WORKDIR /app

COPY --from=builder /app/target/assessment-0.0.1-SNAPSHOT.jar assessment-0.0.1-SNAPSHOT.jar 

ENTRYPOINT ["java", "-jar", "d18-checkhealth-0.0.1-SNAPSHOT.jar"]

ENV PORT=8080
EXPOSE ${PORT}