# mgcss-track-L2group5 | [![Java CI](https://github.com/jdc99/mgcss-track-L2group5/actions/workflows/ci.yml/badge.svg)](https://github.com/jdc99/mgcss-track-L2group5/actions/workflows/ci.yml)

## Ejecución del proyecto
- Situate en la carpeta raiz en la terminal
- Contruye la imagen del Dockerfile ejecutando: `docker build -t mgcss-track .`
- Contruye y ejecuta el contenedor con configuracion externalizada: `docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod mgcss-track`

## Estrategia de ramas
* **main**: Versión estable y protegida.
* **feature/*** : Ramas de desarrollo para nuevas funcionalidades.
* **refactor/*** : Ramas de refactorizacion del codigo.