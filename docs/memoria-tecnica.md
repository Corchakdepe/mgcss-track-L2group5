# Memoria Técnica — mgcss-track-L2group5

**Asignatura:** Mantenimiento y Gestión del Cambio en Sistemas Software (MGCSS)  
**Curso:** 2025/26 — 4º Grado en Ingeniería Informática  
**Grupo:** L2group5  
**Repositorio:** [jdc99/mgcss-track-L2group5](https://github.com/jdc99/mgcss-track-L2group5)

---

## Índice

1. [Resumen del proyecto](#resumen-del-proyecto)
2. [Arquitectura y decisiones técnicas](#arquitectura-y-decisiones-técnicas)
3. [Estrategia de ramas](#estrategia-de-ramas)
4. [Versiones publicadas](#versiones-publicadas)
5. [Entregas y cambios por sesión](#entregas-y-cambios-por-sesión)
   - [Entrega 1 — Cimientos y Pipeline](#entrega-1-cimientos-y-pipeline)
   - [Entrega 2 — Calidad Estática y Dominio](#entrega-2-calidad-estática-y-dominio)
   - [Entrega 3 — Aislamiento y Persistencia](#entrega-3-aislamiento-y-persistencia)
   - [Entrega 4 — Refactorización y Cambio](#entrega-4-refactorización-y-cambio)
   - [Entrega 5 — API y Despliegue](#entrega-5-api-y-despliegue)
   - [Entrega Final — Release y Automatización](#entrega-final-release-y-automatización)
6. [Incidencias y fallos encontrados](#incidencias-y-fallos-encontrados)
7. [Tests y cobertura](#tests-y-cobertura)
8. [Métricas SonarCloud](#métricas-sonarcloud)
9. [Instrucciones de instalación y ejecución](#instrucciones-de-instalación-y-ejecución)

---

## 1. Resumen del proyecto

Sistema de gestión de solicitudes de mantenimiento (tracking) desarrollado en **Spring Boot 4.0.3** con **Java 17**, siguiendo una **arquitectura hexagonal (puertos y adaptadores)**. El proyecto gestiona el ciclo de vida completo de solicitudes técnicas: creación, asignación de técnicos, cierre y reapertura, con un histórico de cambios de estado.

**Stack tecnológico:**

| Componente | Versión |
|---|---|
| Java | 17 |
| Spring Boot | 4.0.3 |
| Maven | Wrapper (3.x) |
| H2 Database | Runtime |
| Lombok | Última |
| SpringDoc OpenAPI | 2.5.0 |
| JaCoCo | 0.8.12 |
| SonarCloud | — |
| Docker | Imagen `eclipse-temurin:17-jre` |

---

## 2. Arquitectura y decisiones técnicas

### 2.1 Arquitectura hexagonal (Puertos y Adaptadores)

Se optó por una arquitectura hexagonal para garantizar la separación de responsabilidades y el aislamiento del dominio:

```
src/main/java/com/mgcss/
├── domain/            ← Núcleo: entidades, repositorios (puertos)
│   ├── model/         ←   Entidades y value objects
│   └── repository/    ←   Interfaces de repositorio (puertos de salida)
├── service/           ← Casos de uso: orquestan la lógica de negocio
├── infrastructure/    ← Adaptadores: implementaciones técnicas
│   ├── config/        ←   Configuración Spring
│   └── persistence/   ←   Adaptadores JPA (entidades, repositorios, mappers)
├── api/               ← Puertos de entrada: REST controllers, DTOs, mappers
│   ├── controller/    ←   Controladores REST
│   ├── dto/           ←   Objetos de transferencia
│   └── mapper/        ←   Mapeo DTO ↔ Dominio
```

**Decisiones clave:**

- **Dominio puro sin anotaciones JPA:** Las entidades de dominio (`Solicitud`, `Cliente`, `Tecnico`) no tienen dependencias de infraestructura.
- **Entidades de persistencia separadas:** `SolicitudEntity`, `ClienteEntity`, `TecnicoEntity` en `infrastructure/persistence/entity/` con el mapeo JPA.
- **Adaptadores de repositorio:** Clases como `SolicitudRepositoryAdapter` implementan los puertos del dominio y delegan en los `JpaRepository` de Spring Data.
- **DTOs separados de entidades:** `SolicitudRequestDTO` / `SolicitudResponseDTO` en `api/dto/` con mappers explícitos (`SolicitudMapper`) para no exponer el modelo interno.
- **Mappers manuales:** Se optó por mappers explícitos sin librerías externas (MapStruct) para mantener el control total y evitar dependencias adicionales.

### 2.2 Gestión de errores

Se implementó un `ExceptionController` (`@RestControllerAdvice`) que captura excepciones del dominio y las traduce a respuestas HTTP con código de estado apropiado (400, 404, 500) y un cuerpo JSON descriptivo.

### 2.3 Configuración externalizada

- Fichero `application-test.yml` para el perfil de tests con H2 y `ddl-auto=create-drop`.
- Variables de entorno para Docker: `SPRING_PROFILES_ACTIVE=prod` para entornos productivos.

---

## 3. Estrategia de ramas

Documentada en `README.md`:

| Rama | Propósito |
|---|---|
| `main` | Versión estable y protegida. Requiere PR obligatorio. |
| `feature/*` | Ramas de desarrollo para nuevas funcionalidades. |
| `refactor/*` | Ramas de refactorización de código. |

**Convención de commits:** Conventional Commits (`feat:`, `fix:`, `refactor:`, `test:`, `docs:`, `chore:`).

---

## 4. Versiones publicadas

| Tag | Fecha | Descripción |
|---|---|---|
| `v1.0.0` | 24-05-2026 | Primera versión estable. API REST completa, Docker, Swagger. |
| `v1.1.0` | 29-05-2026 | MINOR bump. Añadido Dockerfile (feature compatible hacia atrás). Release automatizada con push a Docker Hub. |

---

## 5. Entregas y cambios por sesión

### Entrega 1 — Cimientos y Pipeline

**Sesiones 1-3:** Setup profesional, arquitectura base, CI inicial.

| Fecha | Commit | Tipo | Descripción |
|---|---|---|---|
| 19-03 | `1e8f836` | `feat:` | add initial project structure |
| 19-03 | `96fe2b4` | `feat:` | add README.md |
| 19-03 | `50e53ab` | `refactor:` | add new package structure (`domain/`, `service/`, `infrastructure/`, `api/`) |
| 19-03 | `29eab44` | `feat:` | add new class `Solicitud.java` |
| 19-03 | `634ce65` | `fix:` | change on ci.yml |
| 19-03 | `d313126` | `feat:` | add new ci.yml file |
| 19-03 | `6db80e8` | `refactor:` | added badge to README.md |
| 19-03 | `b368a7b` | `test:` | added error on purpose (prueba de fallo controlado) |
| 19-03 | `b9b0197` | `fix:` | fixed previous error on badge test |
| 20-03 | `aeab41c` | `refactor:` | add sonarqube requirements |

**Decisiones:**
- Se estableció la estructura de paquetes con separación clara entre dominio, servicio, infraestructura y API.
- Se configuró GitHub Actions con CI (`ci.yml`) que compila, ejecuta tests y análisis Sonar.
- Se probó el fallo controlado del pipeline.

---

### Entrega 2 — Calidad Estática y Dominio

**Sesiones 4-5:** SonarCloud, Quality Gates, TDD y modelado del dominio.

| Fecha | Commit | Tipo | Descripción |
|---|---|---|---|
| 27-03 | `6598efe` | `test:` | added unique id validation in Solicitud |
| 27-03 | `ac67655` | `feat:` | added domain |
| 27-03 | `fc375ae` | `refactor:` | deleted afterAll and beforeAll tests |
| 27-03 | `f540271` | `feat:` | added tests |
| 10-04 | `c0a44f4` | `feat:` | added tests class ClienteTest and TecnicoTest |
| 10-04 | `f0a2ca3` | `feat:` | added tests class ClienteTest and TecnicoTest (duplicated) |
| 10-04 | `46e5044` | `feat:` | add new Solicitud behavior |
| 10-04 | `f84bdd1` | `feat:` | modified pom.xml (JaCoCo, Sonar) |
| 11-04 | `a4a7457` | `refactor:` | TDD - REFACTOR explicit getters → `@Getter` lombok, fields final |
| 11-04 — 13-04 | varios | TDD RED→GREEN→REFACTOR | Ciclos completos de TDD para `Cliente`, `Solicitud`, `Tecnico` |
| 13-04 | varios | Reverts | Se revirtieron algunos commits para limpiar el historial |
| 16-04 | `5d06d3c` — `ff78af2` | TDD | Cierre y asignación de técnico con TDD |
| 20-04 | `3ba0b44` | `test:` | TDD - RED `SolicitudServiceTest` |
| 23-04 | `aa0d5fd` | `refactor:` | Migración `java.util.Date` → `java.time.LocalDate` |
| 11-05 | `e1146f6` | `doc:` | refactor-notes.md |
| 11-05 | `ab38d96` | `refactor:` | deleting unused imports |
| 20-05 | `c15eff0` | `fix:` | fixed warnings from SonarQube |

**Decisiones:**
- TDD estricto: cada funcionalidad se implementó siguiendo el ciclo RED→GREEN→REFACTOR.
- Se migró de `java.util.Date` a `java.time.LocalDate` para mejor manejo de fechas.
- Se integró SonarCloud con Quality Gate (coverage ≥ 80%, 0 Bugs, 0 Vulnerabilities).
- Se usó Lombok `@Getter` para reducir boilerplate, manteniendo campos `final`.

---

### Entrega 3 — Aislamiento y Persistencia

**Sesiones 6-7:** Mocks, capa de servicios, persistencia JPA y tests de integración.

| Fecha | Commit | Tipo | Descripción |
|---|---|---|---|
| 17-04 | `cf7932d` | `refactor:` | improving project package structure |
| 17-04 | `4094a06` | `feat:` | added repository classes and `SolicitudService` |
| 17-04 | `e9000be` | `feat:` | add new entities for future data base implementation |
| 17-04 | `dceac46` | `fix:` | cleaned warnings from sonar |
| 20-04 | `16871c6` | `feat:` | added `ClienteRepository` |
| 23-04 | `053156e` | `feat:` | added JPA repositories and entity classes |
| 23-04 | `41a860e` | `feat:` | added `application-test.yml` |
| 23-04 | `1e068c3` | `refactor:` | project packages structure improved |
| 23-04 | `7eaa1fe` | `feat:` | added new JPA repositories |
| 23-04 | `bd51e29` | `test:` | integration tests for each JPA repository with H2 |
| 23-04 | varios | TDD service | Tests de servicio con Mockito: `SolicitudServiceTest` |
| 21-05 | `e5b1a82` | `feat:` | implement `SolicitudRepositoryAdapter` |
| 21-05 | `3c03442` | `test:` | coverage on `SolicitudRepositoryAdapter` |
| 21-05 | `5d5d2a6` | `feat:` | implement `ClienteRepositoryAdapter` |
| 21-05 | `c9bb0c3` | `test:` | coverage on `ClienteRepositoryAdapter` |
| 21-05 | `0b58698` | `feat:` | implement `TecnicoRepositoryAdapter` |
| 21-05 | `e4aa259` | `test:` | coverage on `TecnicoRepositoryAdapter` |
| 21-05 | `6260e59` | `feat:` | api configuration class (`DomainConfig`) |

**Decisiones:**
- Se implementó el patrón **Adapter** para separar los puertos del dominio de las implementaciones JPA.
- Se usó `@DataJpaTest` para tests de integración con H2 en memoria.
- Se etiquetaron los tests de integración en el paquete `integration/`.
- Se usó Mockito para los tests de servicio, mockeando solo los repositorios.

---

### Entrega 4 — Refactorización y Cambio

**Sesiones 8-9:** Refactorización guiada por métricas y gestión del cambio (reopen + histórico).

#### Sesión 8 — Refactorización

| Fecha | Commit | Tipo | Descripción |
|---|---|---|---|
| 11-05 | `2f452cc` | `chore:` | snapshot before refactor |
| 11-05 | `e1146f6` | `doc:` | refactor-notes.md (dead code: 12 Code Smells, 1h deuda) |
| 11-05 | `ab38d96` | `refactor:` | deleting unused imports |
| 22-05 | `d80d8d6` | `refactor:` | finalizing fields (Self Encapsulate Field) |

**Problema identificado:** 12 Code Smells por campos privados no usados.  
**Técnica aplicada:** Self Encapsulate Field (getters explícitos).  
**Resultado:** 0 Code Smells, 0h deuda técnica.

#### Sesión 9 — Gestión del Cambio (Reopen + State History)

| Fecha | Commit | Tipo | Descripción |
|---|---|---|---|
| 13-05 | `fa5c124` | `chore:` | snapshot before change |
| 13-05 | `c46c91e` | `test:` | add reopen test |
| 13-05 | `b3e8b84` | `feat:` | implement reopen logic |
| 13-05 | `58620c0` | `refactor:` | improving closing logic |
| 14-05 | `1808788` | `docs:` | update change analysis |
| 14-05 | `4a6bbd2` | `test:` | add state history test |
| 14-05 | `6053c69` | `feat:` | implement state history |
| 14-05 | `3a1555d` | `refactor:` | extend state history test |
| 14-05 | `6c44753` | `refactor:` | simplify state history |
| 14-05 | `97c531d` | `refactor:` | simplify state history |
| 14-05 | `d1fa403` | `test:` | add state history JPA test |
| 14-05 | `6bb3f0a` | `feat:` | implement JPA state history |
| 14-05 | `586be46` | `refactor:` | simplify JPA state history |
| 14-05 | `9cd7900` | `test:` | fixing Solicitud coverage (80.65%) |
| 14-05 | `564fff7` | `docs:` | update refactor notes |
| 22-05 | `bef0288` | `fix:` | test not passing after domain changes |
| 22-05 | `e0df4d3` | `refactor:` | deleting dead code |

**Análisis de impacto documentado en `docs/change-analysis.md`:**

1. **Métodos afectados:** `Solicitud.reabrir()`, `Solicitud.cerrar()`, `Solicitud.asignar()`
2. **Regla que cambia:** `CERRADA` ya no es inmutable → se permite reabrir a `EN_PROCESO`
3. **Extensión del modelo:** Se añadió `EstadoChange` como value object interno y `EstadoChangeEntity` para persistencia
4. **Impacto en persistencia:** Nueva tabla para histórico de cambios de estado

---

### Entrega 5 — API y Despliegue

**Sesiones 10-11:** API REST, DTOs, Swagger, Docker.

| Fecha | Commit | Tipo | Descripción |
|---|---|---|---|
| 20-05 | `a5984c9` | `chore:` | snapshot before change |
| 20-05 | `e0ba53c` | `feat:` | add Solicitud DTO's |
| 20-05 | `c894b57` | `test:` | add request creation test |
| 20-05 | `3908593` | `feat:` | implement request creation service |
| 20-05 | `1d24d0a` | `feat:` | created all API endpoints |
| 21-05 | `7a587e4` | `test:` | add request fetch test |
| 21-05 | `7b90856` | `feat:` | implement request fetch logic |
| 21-05 | `0cca89c` | `refactor:` | improve request fetch logic |
| 21-05 | `a369817` | `test:` | add request fetch failure test |
| 21-05 | `ea4027d` | `test:` | add listed requests test |
| 21-05 | `8d7e951` | `feat:` | implement listed requests logic |
| 21-05 | `3d13f11` | `test:` | add request closing test |
| 21-05 | `f760c50` | `feat:` | implement request closing logic |
| 21-05 | `004aee1` | `refactor:` | better request closing test readability |
| 21-05 | `97b3f62` | `refactor:` | better request closing readability |
| 21-05 | `61c6b1a` | `test:` | add request reopen test |
| 21-05 | `e5c58a1` | `feat:` | implement request reopen service logic |
| 21-05 | `db819c3` | `refactor:` | better request reopen service readability |
| 21-05 | `3286668` | `refactor:` | getter on tecnicoAsignado Solicitud |
| 21-05 | `b4a1dcc` | `refactor:` | improved logic on `SolicitudResponseDTO` |
| 21-05 | `69b9d7b` | `feat:` | add DTO request mapper |
| 21-05 | `2b05125` | `refactor:` | setter for entities id's |
| 21-05 | `4f64de3` | `feat:` | implement `Solicitud` all fields constructor |
| 21-05 | `8fd3f69` | `refactor:` | improve readability on `Solicitud` getters |
| 21-05 | `6c617ff` | `fix:` | initialize entity request history |
| 21-05 | `e5b1a82` | `feat:` | implement request repository adapter |
| 22-05 | `373200c` | `build:` | add springdocapi dependency |
| 22-05 | `3386fd7` | `feat:` | add server exception handler |
| 22-05 | `98b0aee` | `feat:` | add Swagger documentation of request controller and DTO's |
| 22-05 | `64dbddc` | `test:` | add listed requests controller test |
| 22-05 | `1d24d0a` | `feat:` | created all the API endpoints |
| 22-05 | varios | `feat:`, `test:` | Controladores, DTOs y tests para Cliente y Técnico |
| 23-05 | `239b4ea` | `feature:` | add docker config |
| 23-05 | `78bd111` | `feat:` | add Dockerfile |
| 23-05 | `cf59fad` | `doc:` | update README.md |

**Endpoints implementados:**

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/api/solicitudes` | Crear solicitud |
| `GET` | `/api/solicitudes` | Listar solicitudes |
| `GET` | `/api/solicitudes/{id}` | Consultar solicitud |
| `PUT` | `/api/solicitudes/{id}/asignar` | Asignar técnico |
| `PUT` | `/api/solicitudes/{id}/estado` | Cambiar estado (cerrar) |
| `PATCH` | `/api/solicitudes/{id}/reabrir` | Reabrir solicitud |
| `POST` | `/api/clientes` | Crear cliente |
| `POST` | `/api/tecnicos` | Crear técnico |

**Casos de uso documentados en `docs/use-cases.md`** con 12 escenarios completos.

---

### Entrega Final — Release y Automatización

**Sesiones 12-13:** Release management, CD, auditoría.

| Fecha | Commit | Tipo | Descripción |
|---|---|---|---|
| 29-05 | `6fa95ac` | `documentation:` | added `release-notes.md` for v1.1.0 |
| 29-05 | `ddac84a` | `ci:` | add automated release workflow (`release.yml`) |
| 29-05 | `40669e1` | `docs:` + `ci:` | add release-notes.md for v1.1.0, add Docker Hub push to release workflow |

**Workflow de Release (`release.yml`):**
1. **Quality Gate** (tests + Sonar) — obligatorio pasar
2. **Release** (necesita Quality Gate) → build, Docker image, push a Docker Hub, GitHub Release

**Tags creados:**
- `v1.0.0` — Primera release estable
- `v1.1.0` — MINOR bump: Dockerfile feature

---

## 6. Incidencias y fallos encontrados

### 6.1 CI pipeline roto (19-03)
- **Problema:** Primeros intentos de `ci.yml` fallaron por sintaxis incorrecta.
- **Solución:** Se iteró hasta obtener un workflow funcional (`634ce65`, `d313126`).
- **Prueba de fallo controlado:** Se introdujo un error deliberado (`b368a7b`) para verificar que el pipeline se bloquea, luego se corrigió (`b9b0197`).

### 6.2 Duplicación de tests (10-04)
- **Problema:** Dos commits (`c0a44f4` y `f0a2ca3`) añadieron los mismos tests `ClienteTest` y `TecnicoTest`.
- **Solución:** Se mantuvo uno y se limpió con reverts posteriores.

### 6.3 Reverts masivos (13-04)
- **Problema:** Se revirtieron 6 commits seguidos para reestructurar el enfoque de tests.
- **Causa:** Cambio de estrategia en la organización de tests del dominio.

### 6.4 Warnings de Sonar (17-04, 20-05)
- **Problema:** SonarQube detectaba warnings de código no usado y `java.util.Date`.
- **Solución:** Se limpiaron imports, se migró a `java.time.LocalDate` y se añadieron getters con Lombok.

### 6.5 Cobertura insuficiente en nuevo código (14-05)
- **Problema:** Solo 67.74% de cobertura en el nuevo código del histórico de estados (necesario ≥80%).
- **Solución:** Se añadieron tests adicionales hasta alcanzar 80.65%.
- **Decisión:** Los constructores de `EstadoChangeEntity` no se cubrieron deliberadamente (deuda técnica asumida).

### 6.6 Tests rotos tras cambios de dominio (22-05)
- **Problema:** Tests existentes dejaron de pasar tras añadir nuevas entidades y cambios en `Solicitud`.
- **Solución:** Se corrigieron los tests en `bef0288`.

### 6.7 Fallo de SonarCloud por dependencia spring-h2console
- **Problema:** SonarCloud no reconoce la dependencia `spring-boot-h2console` (no disponible en su catálogo).
- **Solución:** Se ignoró esta dependencia puntual al no afectar a la compilación ni ejecución.

---

## 7. Tests y cobertura

### 7.1 Estructura de tests

```
src/test/java/com/mgcss/
├── MgcssTrackL2group5ApplicationTests.java
├── integration/
│   ├── JpaClienteRepositoryIntegrationTest.java       ← @DataJpaTest
│   ├── JpaSolicitudRepositoryIntegrationTest.java     ← @DataJpaTest
│   ├── JpaTecnicoRepositoryIntegrationTest.java       ← @DataJpaTest
│   └── api/
│       ├── ClienteControllerTest.java                 ← @WebMvcTest
│       ├── SolicitudControllerTest.java               ← @WebMvcTest
│       └── TecnicoControllerTest.java                 ← @WebMvcTest
└── unit/
    ├── domain/
    │   └── model/
    │       ├── ClienteTest.java        ← Tests de reglas de negocio
    │       ├── SolicitudTest.java      ← Tests de reglas de negocio
    │       └── TecnicoTest.java        ← Tests de reglas de negocio
    └── service/
        ├── ClienteServiceTest.java     ← Mockito
        ├── SolicitudServiceTest.java   ← Mockito
        └── TecnicoServiceTest.java     ← Mockito
```

### 7.2 Tipos de test

| Tipo | Tecnología | Propósito |
|---|---|---|
| Unitarios (dominio) | JUnit 5 | Verificar reglas de negocio (sin mocks) |
| Unitarios (servicio) | JUnit 5 + Mockito | Verificar orquestación de casos de uso |
| Integración (JPA) | `@DataJpaTest`, H2 | Verificar persistencia correcta |
| Integración (API) | `@WebMvcTest`, MockMvc | Verificar controladores REST |

### 7.3 Cobertura

- **Objetivo:** ≥ 80% (Quality Gate de SonarCloud)
- **Resultado:** ~80.65% tras ajustes en la Sesión 9
- **Herramienta:** JaCoCo 0.8.12 (integrado en `pom.xml`)

---

## 8. Métricas SonarCloud

### 8.1 Antes de la refactorización (Sesión 8)

| Métrica | Valor |
|---|---|
| Code Smells | 12 |
| Deuda técnica | 1h |
| Coverage | ~67% |
| Bugs | 0 |
| Vulnerabilities | 0 |

### 8.2 Después de la refactorización (Sesión 8)

| Métrica | Valor |
|---|---|
| Code Smells | 0 |
| Deuda técnica | 0h |
| Coverage | ~80% |
| Maintainability Rating | A |

### 8.3 Después del cambio (Sesión 9)

| Métrica | Valor |
|---|---|
| Coverage (nuevo código) | 80.65% |
| Code Smells | 0 |
| Quality Gate | Passed |

---

## 9. Instrucciones de instalación y ejecución

### 9.1 Requisitos

- Docker (recomendado) o Java 17 + Maven

### 9.2 Ejecución con Docker

```bash
# Construir imagen
docker build -t mgcss-track .

# Ejecutar contenedor
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod mgcss-track
```

### 9.3 Ejecución con Maven (desarrollo)

```bash
./mvnw spring-boot:run
```

### 9.4 Ejecución de tests

```bash
./mvnw clean verify
```

### 9.5 Acceso a la API

- **API:** `http://localhost:8080/api/solicitudes`
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **Consola H2 (dev):** `http://localhost:8080/h2-console`

### 9.6 Releases disponibles

- [v1.0.0](https://github.com/jdc99/mgcss-track-L2group5/releases/tag/v1.0.0)
- [v1.1.0](https://github.com/jdc99/mgcss-track-L2group5/releases/tag/v1.1.0)

La imagen Docker también está disponible en Docker Hub.

---

*Documento generado a partir del historial de Git, handouts de la asignatura y documentación del proyecto.*
