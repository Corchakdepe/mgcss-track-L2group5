# mgcss-track-L2group5 | [![Java CI](https://github.com/jdc99/mgcss-track-L2group5/actions/workflows/ci.yml/badge.svg)](https://github.com/jdc99/mgcss-track-L2group5/actions/workflows/ci.yml)

Sistema de gestión de solicitudes de mantenimiento (tracking) desarrollado con **Spring Boot 4.0.3** y **Java 17**. Sigue una arquitectura hexagonal con dominio puro, persistencia JPA/H2, API REST documentada con OpenAPI/Swagger, y pipeline CI/CD con GitHub Actions + SonarCloud.

**Versiones:** `v1.0.0` · `v1.1.0`

---

## Ejecución del proyecto

```bash
# Construir imagen Docker
docker build -t mgcss-track .

# Ejecutar contenedor
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod mgcss-track
```

Accede a la API en `http://localhost:8080/api/solicitudes` y a Swagger UI en `http://localhost:8080/swagger-ui.html`.

## Estrategia de ramas

| Rama | Propósito |
|---|---|
| `main` | Versión estable y protegida. Requiere PR obligatorio. |
| `feature/*` | Nuevas funcionalidades. |
| `refactor/*` | Refactorización de código. |
| `ci/*` | Integración y despliegue continuo. |
| `docs/*` | Documentación técnica. |
| `release/*` | Preparación de releases. |
