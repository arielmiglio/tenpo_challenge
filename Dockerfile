FROM maven:3.8.2-openjdk-16-slim AS tenpo_challenge_jar

WORKDIR /build/

COPY pom.xml /build/
COPY src /build/src/

RUN mvn package

FROM openjdk:16-slim

WORKDIR /app

COPY --from=tenpo_challenge_jar /build/target/challenge-1.0.0.jar /app/

ENTRYPOINT ["java", "-jar", "challenge-1.0.0.jar"]