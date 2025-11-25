# Documento Técnico: Pipeline CI/CD - Sistema de Análisis de Divisas

**Estudiante:** Sergio Arboleda  
**Materia:** Metodologías de Desarrollo de Software  
**Fecha:** 25 de Noviembre de 2025  
**Evaluación:** 40% Final

---

## 📋 Tabla de Contenidos

1. [Introducción](#introducción)
2. [Arquitectura General](#arquitectura-general)
3. [Pipeline CI/CD](#pipeline-cicd)
4. [Proveedor Cloud Seleccionado](#proveedor-cloud-seleccionado)
5. [Servicios Utilizados](#servicios-utilizados)
6. [Configuración de Base de Datos](#configuración-de-base-de-datos)
7. [Configuración de la Aplicación](#configuración-de-la-aplicación)
8. [Estrategia de Despliegue](#estrategia-de-despliegue)
9. [Seguridad y Mejores Prácticas](#seguridad-y-mejores-prácticas)
10. [Conclusiones](#conclusiones)

---

## 1. Introducción

Este documento describe la implementación de un pipeline de **Integración Continua (CI)** y **Despliegue Continuo (CD)** para el Sistema de Análisis de Divisas, una aplicación Spring Boot desarrollada con arquitectura modular.

### Objetivos del Pipeline

- ✅ Automatizar la compilación del código
- ✅ Ejecutar pruebas unitarias automáticamente
- ✅ Generar artefactos desplegables (JAR)
- ✅ Desplegar automáticamente en ambiente de producción
- ✅ Garantizar calidad y trazabilidad del código

---

## 2. Arquitectura General

### Stack Tecnológico

**Backend:**
- Java 17
- Spring Boot 3.5.6
- Maven (gestión de dependencias)
- PostgreSQL (base de datos)

**Arquitectura:**
- Patrón de capas (Presentación, Aplicación, Core, Dominio, Infraestructura)
- API RESTful
- JPA/Hibernate para persistencia

**Control de Versiones:**
- Git
- Azure DevOps Repos

**CI/CD:**
- Azure Pipelines (YAML)

---

## 3. Pipeline CI/CD

### Archivo: `azure-pipelines.yml`

El pipeline está dividido en 2 stages principales:

#### **Stage 1: CI (Integración Continua)**

```yaml
stages:
  - stage: CI
    displayName: 'Build & Test'
    jobs:
      - job: MavenBuild
        displayName: 'Maven build + unit tests'
        pool:
          vmImage: ubuntu-latest
```

**Pasos del Stage CI:**

1. **Checkout del código**
   - Descarga el código fuente desde Azure Repos
   - Limpia el workspace para evitar conflictos

2. **Cache de dependencias Maven**
   - Cachea el repositorio local de Maven (`.m2`)
   - Reduce tiempo de build en ejecuciones posteriores
   - Key: `maven | "$(Agent.OS)" | api/pom.xml`

3. **Configuración de Java 17**
   - Usa Microsoft OpenJDK 17
   - Configura JAVA_HOME automáticamente

4. **Compilación con Maven**
   ```bash
   ./mvnw -B -Dmaven.repo.local=$(MAVEN_CACHE_FOLDER) clean verify
   ```
   - `-B`: Modo batch (no interactivo)
   - `clean`: Limpia builds anteriores
   - `verify`: Compila, ejecuta tests y valida

5. **Publicación de resultados de pruebas**
   - Formato: JUnit XML
   - Ubicación: `**/surefire-reports/TEST-*.xml`
   - Visible en Azure DevOps UI

6. **Generación de artefacto**
   - Copia el JAR generado: `presentacion-*.jar`
   - Lo publica como artefacto del pipeline
   - Nombre: `drop`

#### **Stage 2: CD (Despliegue Continuo)**

```yaml
  - stage: CD
    displayName: 'Deploy to Azure App Service'
    dependsOn: CI
    condition: and(succeeded(), eq(variables['Build.SourceBranch'], 'refs/heads/main'))
```

**Características:**

- **Dependencia:** Solo se ejecuta si CI pasa exitosamente
- **Condición:** Solo se despliega desde la rama `main`
- **Environment:** `production` (permite aprobaciones manuales)
- **Estrategia:** `runOnce` (despliegue simple)

**Pasos del Stage CD:**

1. **Descarga del artefacto**
   - Obtiene el JAR del stage CI
   - No requiere checkout del código

2. **Despliegue a Azure App Service**
   ```yaml
   - task: AzureWebApp@1
     inputs:
       azureSubscription: $(AZURE_SERVICE_CONNECTION)
       appType: webAppLinux
       appName: $(AZURE_WEBAPP_NAME)
       package: $(Pipeline.Workspace)/$(ARTIFACT_NAME)/app/$(JAR_NAME)
       runtimeStack: 'JAVA|17-java17'
   ```

### Triggers del Pipeline

**Push Triggers:**
```yaml
trigger:
  branches:
    include:
      - main
      - feature/*
```
- Se ejecuta en cada push a `main` o ramas `feature/*`

**Pull Request Triggers:**
```yaml
pr:
  branches:
    include:
      - main
```
- Se ejecuta automáticamente en PRs hacia `main`
- Valida el código antes de merge

---

## 4. Proveedor Cloud Seleccionado

### **Microsoft Azure**

**Razones de la elección:**

1. ✅ **Integración nativa con Azure DevOps**
   - Service Connections automáticas
   - Despliegue simplificado con tasks predefinidas

2. ✅ **Soporte completo para Java/Spring Boot**
   - Runtime Java 17 preconfigurado
   - Detección automática de aplicaciones Spring Boot

3. ✅ **Tier gratuito disponible**
   - F1 (Free): Ideal para desarrollo/pruebas
   - B1 (Basic): $13/mes para producción pequeña

4. ✅ **Escalabilidad**
   - Fácil upgrade de plan sin cambios en código
   - Auto-scaling disponible en planes superiores

5. ✅ **Ecosistema completo**
   - Azure Database for PostgreSQL
   - Azure Container Registry (si se conteneriza)
   - Azure Monitor para observabilidad

### Alternativas Consideradas

| Proveedor | Pros | Contras | Decisión |
|-----------|------|---------|----------|
| **AWS** | Más servicios, mayor adopción | Más complejo, curva de aprendizaje | ❌ Descartado |
| **Railway** | Muy fácil, gratis para empezar | Limitado para escalar | ⚠️ Viable para MVP |
| **Render** | Simple, buen tier gratuito | Menos features empresariales | ⚠️ Viable para MVP |
| **Azure** | Integración perfecta, profesional | Requiere suscripción | ✅ **Seleccionado** |

---

## 5. Servicios Utilizados

### 5.1 Azure App Service

**Propósito:** Hosting de la aplicación Spring Boot

**Configuración:**
```
Name: app-analisis-divisas
Resource Group: rg-analisis-divisas
Runtime: Java 17 (Java SE)
OS: Linux
Region: East US
Plan: B1 Basic (1 Core, 1.75 GB RAM)
```

**Características:**
- Despliegue directo de JAR
- HTTPS automático
- Logs en tiempo real
- Variables de entorno configurables
- Reinicio automático en fallos

### 5.2 Azure Database for PostgreSQL

**Propósito:** Base de datos relacional

**Configuración:**
```
Server name: postgres-analisis-divisas
Version: PostgreSQL 14
Compute + Storage: Basic, 1 vCore, 5 GB
Backup retention: 7 días
Geo-redundancy: Deshabilitado (costo)
```

**Seguridad:**
- SSL/TLS obligatorio
- Firewall configurado para permitir solo Azure Services
- Usuario admin con contraseña fuerte
- Conexión desde App Service vía Private Endpoint (opcional)

### 5.3 Azure Key Vault (Opcional pero recomendado)

**Propósito:** Gestión de secretos

**Secretos almacenados:**
- `DB-PASSWORD`: Contraseña de PostgreSQL
- `DB-USERNAME`: Usuario de base de datos
- `JWT-SECRET`: Clave para tokens (si se implementa auth)

**Acceso:**
- Managed Identity del App Service
- Sin credenciales hardcodeadas

### 5.4 Azure Container Registry (Si se conteneriza)

**Propósito:** Registro privado de imágenes Docker

**Configuración:**
```
Registry name: acranalisisdivis

as
SKU: Basic
Admin user: Habilitado
```

**Uso:**
```bash
# Build de imagen
docker build -t acranalisisdivis.azurecr.io/analisis-divisas:latest .

# Push al registry
docker push acranalisisdivis.azurecr.io/analisis-divisas:latest
```

---

## 6. Configuración de Base de Datos

### 6.1 Creación de la Base de Datos

**Opción A: Azure Portal**
1. Crear Azure Database for PostgreSQL
2. Configurar firewall rules
3. Crear base de datos `monedas`

**Opción B: Azure CLI**
```bash
# Crear servidor PostgreSQL
az postgres server create \
  --resource-group rg-analisis-divisas \
  --name postgres-analisis-divisas \
  --location eastus \
  --admin-user adminuser \
  --admin-password <PASSWORD> \
  --sku-name B_Gen5_1 \
  --version 14

# Crear base de datos
az postgres db create \
  --resource-group rg-analisis-divisas \
  --server-name postgres-analisis-divisas \
  --name monedas
```

### 6.2 Configuración de Firewall

```bash
# Permitir servicios de Azure
az postgres server firewall-rule create \
  --resource-group rg-analisis-divisas \
  --server postgres-analisis-divisas \
  --name AllowAzureServices \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0
```

### 6.3 String de Conexión

```
jdbc:postgresql://postgres-analisis-divisas.postgres.database.azure.com:5432/monedas?sslmode=require
```

---

## 7. Configuración de la Aplicación

### 7.1 Cambios en `application.properties`

**Archivo original (desarrollo local):**
```properties
spring.application.name=api
spring.datasource.url=jdbc:postgresql://localhost:5432/Monedas
spring.datasource.username=postgres
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

**Archivo para producción (Azure):**
```properties
# Application
spring.application.name=analisis-divisas-api
server.port=${PORT:8080}

# Database - Usando variables de entorno
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://postgres-analisis-divisas.postgres.database.azure.com:5432/monedas?sslmode=require}
spring.datasource.username=${DB_USERNAME:adminuser@postgres-analisis-divisas}
spring.datasource.password=${DB_PASSWORD}

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=false

# Logging
logging.level.root=INFO
logging.level.monedas.api=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n

# Actuator (para health checks)
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=always
```

### 7.2 Perfiles de Spring

**Crear `application-prod.properties`:**
```properties
# Producción - Azure
spring.datasource.url=jdbc:postgresql://postgres-analisis-divisas.postgres.database.azure.com:5432/monedas?sslmode=require
spring.jpa.hibernate.ddl-auto=validate
logging.level.root=WARN
```

**Activar perfil en Azure App Service:**
```bash
az webapp config appsettings set \
  --name app-analisis-divisas \
  --resource-group rg-analisis-divisas \
  --settings SPRING_PROFILES_ACTIVE=prod
```

### 7.3 Variables de Entorno en Azure

**Configurar en App Service:**
```bash
az webapp config appsettings set \
  --name app-analisis-divisas \
  --resource-group rg-analisis-divisas \
  --settings \
    DATABASE_URL="jdbc:postgresql://postgres-analisis-divisas.postgres.database.azure.com:5432/monedas?sslmode=require" \
    DB_USERNAME="adminuser@postgres-analisis-divisas" \
    DB_PASSWORD="<PASSWORD_SEGURO>" \
    SPRING_PROFILES_ACTIVE="prod"
```

---

## 8. Estrategia de Despliegue

### 8.1 Opción A: Despliegue Directo de JAR (Implementada)

**Ventajas:**
- ✅ Simple y directo
- ✅ No requiere Docker
- ✅ Menos overhead
- ✅ Ideal para aplicaciones Spring Boot

**Flujo:**
1. Pipeline compila y genera JAR
2. JAR se publica como artefacto
3. Azure App Service descarga el JAR
4. Se ejecuta con: `java -jar presentacion-*.jar`

**Configuración en `azure-pipelines.yml`:**
```yaml
- task: AzureWebApp@1
  inputs:
    appType: webAppLinux
    package: $(Pipeline.Workspace)/drop/app/presentacion-*.jar
    runtimeStack: 'JAVA|17-java17'
```

### 8.2 Opción B: Contenerización con Docker (Alternativa)

**Ventajas:**
- ✅ Portabilidad total
- ✅ Mismo entorno en dev/prod
- ✅ Fácil rollback
- ✅ Escalabilidad con Kubernetes

**Dockerfile:**
```dockerfile
# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY api/pom.xml .
COPY api/*/pom.xml ./
RUN mvn dependency:go-offline

COPY api/ .
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/presentacion/target/presentacion-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Pipeline modificado:**
```yaml
- task: Docker@2
  inputs:
    command: buildAndPush
    repository: analisis-divisas
    dockerfile: Dockerfile
    containerRegistry: acranalisisdivis
    tags: |
      $(Build.BuildId)
      latest

- task: AzureWebAppContainer@1
  inputs:
    appName: app-analisis-divisas
    imageName: acranalisisdivis.azurecr.io/analisis-divisas:$(Build.BuildId)
```

### 8.3 Estrategia de Rollback

**Slots de despliegue:**
```bash
# Crear slot de staging
az webapp deployment slot create \
  --name app-analisis-divisas \
  --resource-group rg-analisis-divisas \
  --slot staging

# Desplegar a staging primero
# Validar
# Swap a producción
az webapp deployment slot swap \
  --name app-analisis-divisas \
  --resource-group rg-analisis-divisas \
  --slot staging \
  --target-slot production
```

---

## 9. Seguridad y Mejores Prácticas

### 9.1 Gestión de Secretos

**❌ NUNCA hacer:**
```properties
# NO hardcodear credenciales
spring.datasource.password=admin123
```

**✅ Hacer:**
```properties
# Usar variables de entorno
spring.datasource.password=${DB_PASSWORD}
```

**En Azure DevOps:**
- Variable Groups con secretos marcados como "secret"
- Service Connections con autenticación automática

### 9.2 Seguridad de Red

**Firewall de PostgreSQL:**
- Solo permitir IPs de Azure Services
- Considerar Private Endpoints para mayor seguridad

**App Service:**
- HTTPS obligatorio
- TLS 1.2 mínimo
- CORS configurado apropiadamente

### 9.3 Monitoreo y Logs

**Application Insights:**
```bash
az monitor app-insights component create \
  --app app-insights-analisis-divisas \
  --location eastus \
  --resource-group rg-analisis-divisas
```

**Configurar en Spring Boot:**
```xml
<dependency>
    <groupId>com.microsoft.azure</groupId>
    <artifactId>applicationinsights-spring-boot-starter</artifactId>
    <version>2.6.4</version>
</dependency>
```

### 9.4 Branch Policies

**Configuradas en Azure DevOps:**
- ✅ Require pull request para merge a `main`
- ✅ Mínimo 1 revisor
- ✅ Build validation (CI debe pasar)
- ✅ Work items vinculados obligatorios

---

## 10. Conclusiones

### Logros Alcanzados

1. ✅ **Pipeline CI/CD funcional**
   - Compilación automatizada
   - Pruebas ejecutadas en cada commit
   - Artefactos generados correctamente

2. ✅ **Arquitectura cloud-ready**
   - Configuración basada en variables de entorno
   - Perfiles de Spring para diferentes ambientes
   - Diseño escalable

3. ✅ **Buenas prácticas implementadas**
   - Branch policies
   - Pull requests obligatorios
   - Gestión segura de secretos
   - Trazabilidad completa (commits → PRs → work items)

### Próximos Pasos

1. **Implementar autenticación**
   - Spring Security
   - JWT tokens
   - Roles y permisos

2. **Agregar más pruebas**
   - Tests de integración
   - Tests end-to-end
   - Cobertura de código >80%

3. **Mejorar observabilidad**
   - Métricas personalizadas
   - Alertas automáticas
   - Dashboards en Application Insights

4. **Optimizar costos**
   - Auto-scaling basado en demanda
   - Reserved instances para producción
   - Limpieza de recursos no usados

### Lecciones Aprendidas

- La integración Azure DevOps + Azure es muy fluida
- Los pipelines YAML son declarativos y versionables
- La contenerización agrega complejidad pero mejora portabilidad
- Las branch policies son esenciales para calidad de código
- La gestión de secretos debe ser prioritaria desde el inicio

---

## Anexos

### A. Comandos Útiles

**Azure CLI:**
```bash
# Ver logs en tiempo real
az webapp log tail --name app-analisis-divisas --resource-group rg-analisis-divisas

# Reiniciar app
az webapp restart --name app-analisis-divisas --resource-group rg-analisis-divisas

# Ver configuración
az webapp config show --name app-analisis-divisas --resource-group rg-analisis-divisas
```

**Maven:**
```bash
# Compilar localmente
./mvnw clean package

# Ejecutar localmente
java -jar api/presentacion/target/presentacion-0.0.1-SNAPSHOT.jar

# Ejecutar tests
./mvnw test
```

### B. URLs de Referencia

- Pipeline: `https://dev.azure.com/sergioarboledaDevOps1123410/AnalisisDivisas/_build`
- Repositorio: `https://dev.azure.com/sergioarboledaDevOps1123410/AnalisisDivisas/_git/AnalisisDivisas`
- App (cuando se despliegue): `https://app-analisis-divisas.azurewebsites.net`

### C. Costos Estimados (Azure)

| Servicio | Plan | Costo Mensual |
|----------|------|---------------|
| App Service | B1 Basic | $13 USD |
| PostgreSQL | Basic, 1 vCore | $25 USD |
| **Total** | | **$38 USD/mes** |

**Alternativa gratuita:**
- App Service: F1 Free (limitado)
- PostgreSQL: Usar Railway/Render (tier gratuito)
- **Total: $0 USD/mes** (con limitaciones)

---

**Documento preparado por:** Sergio Arboleda  
**Fecha:** 25 de Noviembre de 2025  
**Versión:** 1.0
