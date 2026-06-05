# Memoria Técnica — mgcss-track-L2group5

**Asignatura:** Mantenimiento y Gestión del Cambio en Sistemas Software (MGCSS)  
**Curso:** 2025/26 — 4º Grado en Ingeniería Informática  
**Grupo:** L2group5  
**Autores:** Ageu Depetris Filho, Jesús Díaz Ruiz
---

## Índice

1. [Arquitectura y Diseño](#1-arquitectura-y-diseño)
2. [Gestión del Cambio y Evolución](#2-gestión-del-cambio-y-evolución)
3. [Aseguramiento de la Calidad](#3-aseguramiento-de-la-calidad)
4. [Auditoría de Métricas y Análisis de Deuda Técnica](#4-auditoría-de-métricas-y-análisis-de-deuda-técnica)
5. [Instrucciones de instalación y ejecución](#5-instrucciones-de-instalación-y-ejecución)

---

## 1. Arquitectura y Diseño

### 1.1 Arquitectura Hexagonal (Puertos y Adaptadores)
El sistema ha sido diseñado bajo los principios de la Arquitectura Hexagonal para garantizar el aislamiento de la lógica de negocio respecto a los detalles de infraestructura y presentación.

* **Dominio Puro:** Las entidades de negocio como `Solicitud`, `Cliente` y `Tecnico` se han diseñado de manera agnóstica, sin dependencias de frameworks externos ni anotaciones de base de datos.
* **Adaptadores de Persistencia:** Se han separado las entidades de base de datos (`SolicitudEntity`, `ClienteEntity`) y se ha utilizado el patrón Adapter (por ejemplo, `SolicitudRepositoryAdapter`) para conectar los puertos del dominio con la capa `JpaRepository` de Spring Data.
* **Separación REST / DTOs:** La capa de la API (`com.mgcss.api.controller`) expone únicamente Objetos de Transferencia de Datos (DTOs como `SolicitudResponseDTO`). La traducción entre dominio y DTOs se realiza a través de mapeadores explícitos (`SolicitudMapper`), evitando la sobreexposición del modelo interno.

### 1.2 Decisiones Técnicas
* **Base de datos en memoria (H2):** Elegida para simplificar el despliegue y favorecer la ejecución autónoma de tests de integración sin requerir infraestructura externa compleja.
* **Gestión de Excepciones Centralizada:** Se ha implementado un `@RestControllerAdvice` (`ExceptionController.java`) para capturar excepciones del dominio (como `IllegalArgumentException` o `IllegalStateException`) y traducirlas automáticamente a códigos HTTP estándar (400, 404), mejorando la legibilidad de la API.
* **Lombok:** Implementado estratégicamente (`@Getter`, `@AllArgsConstructor`) para reducir el código repetitivo (*boilerplate*), manteniendo la inmutabilidad de los atributos clave sin ensuciar la legibilidad de las clases.

---

## 2. Gestión del Cambio y Evolución

### 2.1 Estrategia de Ramas y Versionado Semántico
El desarrollo se ha regido por una estrategia estructurada de ramas (`main` protegida, `feature/*`, `refactor/*`, `ci/*`) integrando el código exclusivamente mediante *Pull Requests*. Las entregas se han consolidado bajo un versionado semántico formal:
* **`v1.0.0`**: Primera release estable, validando el despliegue de la API REST completa (`commit 6382c5d`).
* **`v1.1.0`**: Incremento MINOR (`commit 60fb058`) que automatiza el pipeline de *Release*, generando artefactos adjuntos y publicando la imagen en Docker Hub sin alterar la funcionalidad base del negocio.

### 2.2 Evolución por Fases
El historial de integración refleja un crecimiento incremental guiado por metodologías ágiles y de calidad:

* **Fase 1: Dominio y TDD:** El proyecto se inició modelando las reglas del negocio puro aplicando ciclos estrictos de *Test-Driven Development* (TDD Red-Green-Refactor). Se tomaron decisiones técnicas clave en etapas tempranas, como la migración de `java.util.Date` a `java.time.LocalDate` (`commit aa0d5fd`) para un manejo temporal moderno.
* **Fase 2: Aislamiento y Persistencia:** Se integraron las interfaces de repositorios y los adaptadores JPA, testeando el guardado y recuperación en la base de datos de H2 (`commit 053156e` y `e5b1a82`).
* **Fase 3: Gestión del Cambio (Histórico y Reapertura):** Se implementó un *Change Request* que rompía la inmutabilidad original de una solicitud `CERRADA`. Se introdujo la lógica para `reabrir()` (`commit b3e8b84`) y se creó la entidad `EstadoChangeEntity` para registrar y persistir todo el historial de transiciones de estado de forma trazable (`commit 6bb3f0a`).
* **Fase 4: Capa API REST y Swagger:** Expansión hacia el exterior mediante controladores y DTOs, documentando los *endpoints* interactivos con `springdoc-openapi` (`commit 98b0aee`).
* **Fase 5: Automatización CI/CD:** En el tramo final, se creó la rama `ci/automatizacion-release` (`commit 6fa95ac`), dotando al repositorio de un pipeline completo que analiza el código, empaqueta el artefacto `.jar` (`bc399ec`) y construye y publica automáticamente la imagen en Docker Hub (`5f4b56e`).

---

## 3. Aseguramiento de la Calidad

La confiabilidad del software se garantiza en múltiples niveles de la pirámide de testing:

1.  **Testing Unitario (Dominio):** Validaciones rigurosas sobre la lógica de negocio sin dependencias externas. Por ejemplo, `SolicitudTest.java` verifica que transiciones de estado inválidas lancen excepciones correctamente.
2.  **Testing Unitario (Servicios):** Se orquestan los casos de uso utilizando `Mockito` para aislar la capa de repositorio (ej. `SolicitudServiceTest.java`).
3.  **Testing de Integración:** Empleo de `@DataJpaTest` para validar los adaptadores y consultas en base de datos H2 (`JpaSolicitudRepositoryIntegrationTest.java`), y `@WebMvcTest` con `MockMvc` para asegurar el correcto mapeo HTTP y respuestas JSON en los controladores (`SolicitudControllerTest.java`).
4.  **Pipeline de Integración Continua (CI):** Todo *Pull Request* hacia `main` se enfrenta a un workflow automatizado que obliga al código a compilar y a pasar íntegramente la batería de pruebas antes de autorizar su integración.
5.  **Pipeline de Entregas Finales:** Todo *Tag* derivara automaticamente en un *Release* no sin antes enfrentar los estandares de calidad garantizado por la bateria de tests asi como por las metricas de SonarCloud.

---

## 4. Auditoría de Métricas y Análisis de Deuda Técnica

Como barrera de seguridad en la nube, el pipeline de *Release* incorpora un *Quality Gate* estricto contra SonarCloud. No se permite generar ni liberar ninguna versión con métricas en rojo.

### 4.1 Métricas Finales (Release `v1.1.0`)
El análisis estático final avala un estado de excelencia técnica:

| Métrica                          | Valor | Calificación |
|:---------------------------------|:-----:|:------------:|
| **Bugs**                         |   0   |      A       |
| **Vulnerabilidades**             |   0   |      A       |
| **Security Hotspots**            |   0   |      A       |
| **Code Smells (Mantenibilidad)** |   0   |      A       |
| **Cobertura (Coverage)**         | 92.2% |    Passed    |
| **Duplicaciones**                | 0.0%  |    Passed    |

### 4.2 Análisis de Deuda Técnica y Refactorizaciones
Durante la evolución del ciclo de vida se ha monitorizado y subsanado la deuda técnica en varios frentes:

* **Eliminación de Código Muerto:** En sesiones previas, SonarCloud reportó 12 *Code Smells* por campos privados no utilizados, representando 1 hora de deuda técnica. Estos fueron erradicados en un ciclo de refactorización aplicando la técnica *Self Encapsulate Field* (implementación de getters).
* **Mitigación de Hotspots de Seguridad:** Durante la automatización, se reportó un *Security Hotspot* originado por el uso de etiquetas de versión inestables (`@v2`, `@v4`) en GitHub Actions. Se solucionó (commit `e2fccff`) anclando las dependencias a Hashes de Commit inmutables, blindando la seguridad de la cadena de suministro.
* **Deuda Técnica Asumida:** El proyecto ostenta una cobertura del **92.2%** sobre las mas de 1200 líneas de código evaluables. Se ha asumido de forma deliberada y consciente la falta de cobertura en los constructores de entidades JPA de persistencia (`EstadoChangeEntity`), dado que forzar tests sobre ellos introduciría una complejidad artificial que no aporta valor a la verificación del modelo de negocio real.

---

## 5. Instrucciones de instalación y ejecución

Para ejecutar la aplicación en un entorno de validación **no es necesario descargar el código fuente ni compilar el proyecto**. A continuación se detallan los métodos para desplegar la última versión estable generada y empaquetada automáticamente por nuestro pipeline.

### Prerrequisitos
* **Para Opción A:** Tener instalado Docker Desktop o Docker Engine.
* **Para Opción B:** Tener instalado Java 17 (JRE o JDK).

### Instalación y ejecucion
#### Opción A: Despliegue mediante Docker Hub (Recomendado)
Nuestro pipeline construye y publica automáticamente las imágenes versionadas en Docker Hub. Para descargar y ejecutar la aplicación de forma contenerizada en un solo paso, abre tu terminal y ejecuta:

```
docker pull jesus1199/mgcss-track-l2group5:<tag-name>
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod jesus1199/mgcss-track-l2group5:<tag-name> 
```
###### _nota: sustituye `<tag-name>` por el nombre de la ultima entrega disponible en github_

#### Opción B: Ejecución nativa desde GitHub Releases
Si prefieres no usar un entorno Docker, puedes ejecutar el artefacto binario generado por nuestro pipeline:

-   Navega a la página de Releases de nuestro repositorio en GitHub.
-   En la versión `<tag-name>`, ve a la sección Assets y descarga el archivo empaquetado .jar (ej. `<nombre-del-ejecutable>.jar`).

Abre una terminal en el directorio donde se encuentre el .jar descargado y ejecuta:

```
java -jar <nombre-del-ejecutable>.jar
```
###### _nota: sustituye `<tag-name>` por el nombre de la ultima entrega disponible en github_ y `<nombre-del-ejecutable>` por el nombre que le hayas dado al descargar.

### Uso
De forma común a ambas opciones, para probar la demo ejecutable accede en tu navegador a: `http://localhost:8080/swagger-ui/index.html` 