# Documentación Técnica: Pipeline CI/CD y Despliegue Cloud

## 1. Introducción
Este documento detalla la implementación del pipeline de Integración Continua y Despliegue Continuo (CI/CD) para el proyecto "AnalisisDivisas", así como su despliegue en la nube.

## 2. Arquitectura del Proyecto
- **Lenguaje:** Java 17
- **Framework:** Spring Boot 3.5.6
- **Gestor de Dependencias:** Maven
- **Base de Datos:** PostgreSQL (Desarrollo) / H2 (Producción/Demo)
- **Contenedorización:** Docker

## 3. Estrategia de Ramificación (Branching Strategy)
Se utiliza una estrategia basada en **Trunk Based Development** simplificado:
- **main:** Rama principal, siempre desplegable. Protegida mediante Branch Policies.
- **feature/*:** Ramas para desarrollo de nuevas funcionalidades.

### Políticas de Rama (Branch Policies)
Para proteger la rama `main` en Azure DevOps:
- **Pull Request (PR) obligatorio:** No se permiten commits directos a main.
- **Revisores mínimos:** 1 revisor requerido.
- **Build Validation:** El pipeline de CI debe pasar exitosamente antes de completar el PR.
- **Work Item Linking:** Todo PR debe estar vinculado a una tarea o historia de usuario.

## 4. Pipeline de Integración Continua (CI)
Implementado en **Azure DevOps** (`azure-pipelines.yml`).

### Stages del Pipeline
1. **Build:**
   - Compila el proyecto usando Maven Wrapper (`mvnw`).
   - Ejecuta pruebas unitarias.
   - Genera el artefacto (`.jar`).
   - Publica el artefacto para su uso posterior.

### Trigger
- Automático al hacer push a `main`.
- Automático al crear/actualizar un Pull Request hacia `main`.

## 5. Pipeline de Despliegue Continuo (CD)
Debido a restricciones en el tier gratuito de Azure (aprobación de agentes hosted), se implementó una estrategia híbrida:

1. **Definición en Azure DevOps:** El stage de despliegue está definido en `azure-pipelines.yml` (actualmente comentado) para desplegar a Azure App Service.
2. **Implementación Real en Render:** Se configuró un despliegue automático en **Render.com** conectado al repositorio.

## 6. Dockerización
Se creó un `Dockerfile` multi-stage para optimizar la imagen final:
- **Stage 1 (Build):** Usa imagen Maven para compilar el proyecto.
- **Stage 2 (Run):** Usa imagen OpenJDK 17 ligera (slim) para ejecutar la aplicación.

## 7. Despliegue en Render.com (Producción)
La aplicación se encuentra desplegada y operativa en la plataforma Render.

### Configuración
- **Servicio:** Web Service (Docker)
- **Base de Datos:** H2 Database (En memoria, configurada para producción)
- **Variables de Entorno:** `SPRING_PROFILES_ACTIVE=prod`

### Enlaces de Acceso (Producción)
- **Estado del Servicio (Health Check):**  
  [https://analisisdivisas.onrender.com/api/monedas/health](https://analisisdivisas.onrender.com/api/monedas/health)
  *(Debe responder: "Servicio de monedas funcionando correctamente")*

- **Documentación API (Swagger UI):**  
  [https://analisisdivisas.onrender.com/swagger-ui/index.html](https://analisisdivisas.onrender.com/swagger-ui/index.html)
  *(Interfaz interactiva para probar los endpoints)*

- **Listado de Monedas:**  
  [https://analisisdivisas.onrender.com/api/monedas/listar](https://analisisdivisas.onrender.com/api/monedas/listar)

## 8. Conclusiones
El proyecto cuenta con un ciclo de vida de desarrollo completo y moderno:
1. Código en **GitHub**.
2. CI/CD definido en **Azure DevOps**.
3. Contenedorización con **Docker**.
4. Despliegue automático en **Render**.
