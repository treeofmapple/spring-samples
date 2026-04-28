FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -B
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -T 6C package -DskipTests -B


FROM eclipse-temurin:21-jre
WORKDIR /app

ARG KEYS_PATH=/app/keys

RUN groupadd -r spring && useradd -r -g spring spring

RUN mkdir -p ${KEYS_PATH} && chown -R spring:spring ${KEYS_PATH}

USER spring

COPY --from=builder --chown=spring:spring /app/target/*.jar app.jar

EXPOSE 8000
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=80.0 -XX:+UseG1GC"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
