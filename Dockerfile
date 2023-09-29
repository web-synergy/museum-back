FROM openjdk:17.0.2-slim-bullseye AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests


FROM openjdk:17.0.2-slim-bullseye
WORKDIR /app
COPY --from=builder /app/target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]