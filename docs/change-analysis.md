# Análisis de Impacto - Change Request: Allow reopen and state history

### 1. ¿Qué métodos del dominio se ven afectados?
Afecta a la entidad `Solicitud`:
* Constructor para inicializar el historial.
* Método `reabrir()` para transicionar de `CERRADA` a `EN_PROCESO`.
* Método `cerrar()` para registrar el cambio a `CERRADA`.
* Método `asignar(Tecnico tecnico)` para transicionar de `ABIERTA` a `EN_PROCESO`. 

### 2. ¿Qué reglas actuales cambian?
* El estado `CERRADA` ya no es inmutable, ahora se permite volver a `EN_PROCESO`.
* Se introduce la obligatoriedad de registrar todos los cambios de estado de una solicitud.
* Se introduce que para asignar un `Tecnico`, la solicitud debe estar en estado `ABIERTA`.

### 3. ¿Qué tests deberían romperse?
Actualmente no hay ningún test que verifique explicitamente las condición de inmutabilidad de una solicitud `CERRADA`.

### 4. ¿Qué parte del modelo debe extenderse?
La clase `Solicitud` debe incluir una estructura de datos interna que almacene objetos de tipo `EstadoChange`, para listar el historico de cambios de estado de `Solicitud`. 

### 5. ¿Qué impacto tiene en persistencia?
Es necesario mapear la nueva relación del histórico de cambios de estado en la base de datos, probablemente mediante una tabla secundaria vinculada.

# Análisis de Impacto - Change Request: API REST and DTO / Entity division

### 1. ¿Qué métodos del dominio se ven afectados?
Afecta a las entidades `SolicitudService` y `SolicitudRepository`:
* Métodos para listar todas las solicitudes.

### 2. ¿Qué reglas actuales cambian?
* 

### 3. ¿Qué tests deberían romperse?
Aquellos implementados en `JpaSolicitudRepositoryIT`.

### 4. ¿Qué parte del modelo debe extenderse?
Todas las clases relativas al funcionamiento de la API REST y DTO's de `Solicitud`.

### 5. ¿Qué impacto tiene en persistencia?
Es necesario mapear DTO al dominio.