# Etapa 1: Construir a aplicação usando uma imagem com Maven
FROM maven:3.9.5-eclipse-temurin-21 AS builder
COPY src /app/src
COPY pom.xml /app
WORKDIR /app
RUN mvn clean install

# Etapa 2: Criar a imagem final com apenas o JRE
FROM eclipse-temurin:21-jre-alpine
COPY --from=builder /app/target/*.jar /app/app.jar
WORKDIR /app
EXPOSE 8083
CMD ["java", "-jar", "/app/app.jar"]
