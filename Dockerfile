# Build Stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia tudo
COPY . .

# Faz o build usando o Maven direto (sem wrapper)
RUN mvn clean package -DskipTests -B --no-transfer-progress

# Runtime Stage
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]