# mgcss-track-L2group5 

## Ejecución del proyecto
- Situate en la carpeta raiz en la terminal
- Contruye la imagen del Dockerfile ejecutando: `docker build -t mgcss-track .`
- Contruye y ejecuta el contenedor con configuracion externalizada: `docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod mgcss-track`

## Estrategia de ramas
* **main**: Versión estable y protegida.
* **feature/*** : Ramas de desarrollo para nuevas funcionalidades.
* **refactor/*** : Ramas de refactorizacion del codigo.

## Calificadores de calidad
[![Java CI](https://github.com/jdc99/mgcss-track-L2group5/actions/workflows/ci.yml/badge.svg)](https://github.com/jdc99/mgcss-track-L2group5/actions/workflows/ci.yml)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jdc99_mgcss-track-L2group5&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=jdc99_mgcss-track-L2group5)

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=jdc99_mgcss-track-L2group5&metric=bugs)](https://sonarcloud.io/summary/new_code?id=jdc99_mgcss-track-L2group5)

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=jdc99_mgcss-track-L2group5&metric=bugs)](https://sonarcloud.io/summary/new_code?id=jdc99_mgcss-track-L2group5)

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=jdc99_mgcss-track-L2group5&metric=coverage)](https://sonarcloud.io/summary/new_code?id=jdc99_mgcss-track-L2group5)

[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=jdc99_mgcss-track-L2group5&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=jdc99_mgcss-track-L2group5)

[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=jdc99_mgcss-track-L2group5&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=jdc99_mgcss-track-L2group5)

[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=jdc99_mgcss-track-L2group5&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=jdc99_mgcss-track-L2group5)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=jdc99_mgcss-track-L2group5&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=jdc99_mgcss-track-L2group5)

[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=jdc99_mgcss-track-L2group5&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=jdc99_mgcss-track-L2group5)

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=jdc99_mgcss-track-L2group5&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=jdc99_mgcss-track-L2group5)

[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=jdc99_mgcss-track-L2group5&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=jdc99_mgcss-track-L2group5)