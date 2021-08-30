FROM maven:3.8.1-openjdk-11-slim AS tenpo_challenge_jar
#3.8.1-ibmjava-alpine
WORKDIR /build/

COPY pom.xml /build/
COPY src /build/src/

RUN mvn package

FROM openjdk:11-jdk-slim

WORKDIR /app

COPY --from=tenpo_challenge_jar /build/target/challenge-1.0.0.jar /app/

ENTRYPOINT ["java", "-jar", "challenge-1.0.0.jar"]