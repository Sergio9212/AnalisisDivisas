# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar archivos del proyecto
COPY api/ ./

# Compilar
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar JAR del stage anterior
COPY --from=build /app/presentacion/target/presentacion-*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Ejecutar aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
