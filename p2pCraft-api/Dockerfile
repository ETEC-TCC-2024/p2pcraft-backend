FROM gradle:jdk-21-and-22-alpine AS builder
WORKDIR /app
COPY . .

RUN gradle bootJar

FROM openjdk:22-jdk-slim AS stage
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/app.jar
COPY .env /app/.env

CMD ["java", "-jar", "/app/app.jar"]

