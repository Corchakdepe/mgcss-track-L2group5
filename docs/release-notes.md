# Release Notes - v1.1.0

## Versión semántica: MINOR bump (1.0.0 → 1.1.0)

### Justificación
Según el análisis de commits desde `v1.0.0` hasta `HEAD`:

| Commit | Tipo | Descripción |
|--------|------|-------------|
| `78bd111` | `feat:` | Añadido Dockerfile para containerización del proyecto |
| `cf59fad` | `doc:` | Actualización de README.md |

- **MAJOR** (2.0.0): No procede. No hay cambios incompatibles con versiones anteriores. La API, el modelo de dominio y la persistencia no se han modificado.
- **MINOR** (1.1.0): Procede. El commit `feat: add Dockerfile` introduce nueva funcionalidad (containerización) que es completamente compatible hacia atrás.
- **PATCH** (1.0.1): No procede. No hay correcciones de bugs.

### Cambios incluidos
- Containerización del proyecto mediante Dockerfile con imagen `eclipse-temurin:17-jre-alpine`
- Usuario no root por seguridad
- Actualización de documentación README
