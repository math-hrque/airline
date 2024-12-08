FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
EXPOSE 8085
ENTRYPOINT ["mvn", "spring-boot:run"]