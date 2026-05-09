# ==================== Build Stage ====================
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia arquivos essenciais
COPY ApiJogo-main/pom.xml .
COPY ApiJogo-main/mvnw .
COPY .mvn .mvn
COPY ApiJogo-main/src ./src

# Dá permissão ao Maven Wrapper
RUN chmod +x ./mvnw

# Faz o build (gera o JAR)
RUN ./mvnw clean package -DskipTests

# ==================== Runtime Stage ====================
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia o JAR gerado
COPY --from=build /app/target/apiJogo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]