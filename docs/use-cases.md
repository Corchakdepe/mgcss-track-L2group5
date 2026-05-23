# Casos de uso – API REST Solicitudes

## Caso 1 – Crear solicitud correctamente

**Request:**
```
POST /api/solicitudes
Content-Type: application/json

{
  "clienteId": 1,
  "descripcion": "Incidencia en la red interna"
}
```

**Response esperado:**
```
200 OK
{
  "id": 1,
  "descripcion": "Incidencia en la red interna",
  "estado": "ABIERTA",
  "fechaCreacion": "2026-05-20",
  "fechaCierre": null,
  "clienteNombre": "NombreCliente",
  "tecnicoNombre": null
}
```

---

## Caso 2 – Crear solicitud con cliente inexistente

**Precondición:** No existe cliente con id = 99

**Request:**
```
POST /api/solicitudes
Content-Type: application/json

{
  "clienteId": 99,
  "descripcion": "Incidencia"
}
```

**Response esperado:**
```
404 Not Found
{
  "error": "Cliente no encontrado con id: 99"
}
```

---

## Caso 3 – Consultar solicitud existente

**Precondición:** Solicitud con id = 1 creada previamente

**Request:**
```
GET /api/solicitudes/1
```

**Response esperado:**
```
200 OK
{
  "id": 1,
  "descripcion": "Incidencia en la red interna",
  "estado": "ABIERTA",
  "fechaCreacion": "2026-05-20",
  "fechaCierre": null,
  "clienteNombre": "NombreCliente",
  "tecnicoNombre": null
}
```

---

## Caso 4 – Consultar solicitud inexistente

**Request:**
```
GET /api/solicitudes/999
```

**Response esperado:**
```
404 Not Found
{
  "error": "Solicitud no encontrada con id: 999"
}
```

---

## Caso 5 – Listar solicitudes

**Precondición:** Existen solicitudes registradas

**Request:**
```
GET /api/solicitudes
```

**Response esperado:**
```
200 OK
[
  {
    "id": 1,
    "descripcion": "Incidencia en la red interna",
    "estado": "ABIERTA",
    ...
  },
  {
    "id": 2,
    "descripcion": "Problema con el servidor",
    "estado": "EN_PROCESO",
    ...
  }
]
```

---

## Caso 6 – Asignar técnico a solicitud abierta

**Precondición:**
- Solicitud con id = 1 en estado `ABIERTA`
- Técnico con id = 1 existe y está activo

**Request:**
```
PUT /api/solicitudes/1/asignar
Content-Type: application/json

{
  "tecnicoId": 1
}
```

**Response esperado:**
```
200 OK
{
  "id": 1,
  "descripcion": "Incidencia en la red interna",
  "estado": "EN_PROCESO",
  "fechaCreacion": "2026-05-20",
  "fechaCierre": null,
  "clienteNombre": "NombreCliente",
  "tecnicoNombre": "TecnicoAsignado"
}
```

---

## Caso 7 – Asignar técnico a solicitud no abierta

**Precondición:**
- Solicitud con id = 1 en estado `CERRADA`

**Request:**
```
PUT /api/solicitudes/1/asignar
Content-Type: application/json

{
  "tecnicoId": 1
}
```

**Response esperado:**
```
400 Bad Request
{
  "error": "Solo se puede asignar técnicos a solicitudes abiertas."
}
```

---

## Caso 8 – Asignar técnico inactivo

**Precondición:**
- Solicitud con id = 1 en estado `ABIERTA`
- Técnico con id = 2 está inactivo

**Request:**
```
PUT /api/solicitudes/1/asignar
Content-Type: application/json

{
  "tecnicoId": 2
}
```

**Response esperado:**
```
400 Bad Request
{
  "error": "No se puede asignar un tecnico inactivo."
}
```

---

## Caso 9 – Cambiar estado (cerrar solicitud correctamente)

**Precondición:**
- Solicitud con id = 1 en estado `EN_PROCESO` (técnico asignado previamente)

**Request:**
```
PUT /api/solicitudes/1/estado
```

**Response esperado:**
```
200 OK
{
  "id": 1,
  "estado": "CERRADA",
  "fechaCierre": "2026-05-21",
  ...
}
```

---

## Caso 10 – Cerrar solicitud incorrectamente (no está en proceso)

**Precondición:**
- Solicitud con id = 1 en estado `ABIERTA`

**Acción:** `cerrar()`

**Request:**
```
PUT /api/solicitudes/1/estado
```

**Response esperado:**
```
400 Bad Request
{
  "error": "No se puede cerrar si no esta en proceso."
}
```

---

## Caso 11 – Reabrir solicitud correctamente

**Precondición:**
- Solicitud con id = 1 en estado `CERRADA`

**Request:**
```
PATCH /api/solicitudes/1/reabrir
```

**Response esperado:**
```
200 OK
{
  "id": 1,
  "estado": "EN_PROCESO",
  "fechaCierre": null,
  ...
}
```

---

## Caso 12 – Reabrir solicitud incorrectamente (no está cerrada)

**Precondición:**
- Solicitud con id = 1 en estado `ABIERTA`

**Request:**
```
PATCH /api/solicitudes/1/reabrir
```

**Response esperado:**
```
400 Bad Request
{
  "error": "Solo se pueden reabrir solicitudes que estén cerradas"
}
```

---

## Resumen de estados y transiciones

```
ABIERTA --[asignar técnico]--> EN_PROCESO
EN_PROCESO --[cerrar]--> CERRADA
CERRADA --[reabrir]--> EN_PROCESO
```
