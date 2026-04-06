# BTG Fondos MS

Microservicio REST para la gestión de fondos de inversión BTG. Permite registrar clientes, suscribirlos a fondos de inversión, cancelar suscripciones y consultar el historial de transacciones.

## Stack tecnológico

| Tecnología | Versión |
|---|---|
| Java | 17 |
| Spring Boot | 4.0.5 |
| MongoDB Atlas | 7 |
| JWT (jjwt) | 0.12.6 |
| MapStruct | 1.6.3 |
| Lombok | latest |

---

## Requisitos previos

- Java 17
- Gradle (wrapper incluido)
- MongoDB Atlas o instancia local

---

## Configuración

En `src/main/resources/application.properties`:

```properties
spring.mongodb.uri=mongodb+srv://<user>:<password>@<cluster>.mongodb.net/<database>?appName=<app>
jwt.secret=<clave-minimo-32-caracteres>
jwt.expiration=86400000
```

---

## Comandos

```bash
./gradlew build        # Compilar y empaquetar
./gradlew bootRun      # Iniciar la aplicación
./gradlew test         # Ejecutar tests unitarios
./gradlew clean build  # Rebuild completo
```

---

## Autenticación

La API usa **JWT (HMAC-SHA256)**. Para acceder a los endpoints protegidos:

### Paso 1 — Obtener el token

```http
POST /auth/login
Content-Type: application/json
```

```json
{
  "usuario": "julian",
  "clave": "123456"
}
```

**Respuesta exitosa (`200`):**
```json
{
  "jwt": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Errores (`400`):**
```json
{ "timestamp": "2026-04-05T10:00:00", "error": "Usuario no encontrado" }
{ "timestamp": "2026-04-05T10:00:00", "error": "Contraseña incorrecta" }
```

```bash
curl -X POST http://localhost:8080/auth/login \
     -H "Content-Type: application/json" \
     -d '{"usuario":"julian","clave":"123456"}'
```

### Paso 2 — Usar el token

Incluir en cada petición protegida:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

> El token tiene vigencia de **24 horas**. Pasado ese tiempo se debe hacer login nuevamente.

---

## Endpoints

### Clientes

#### `POST /cliente/registrar` — Registrar cliente `🔒`

Registra un nuevo cliente. El saldo inicial asignado es **$500.000 COP**.

```json
{
  "nombre": "Andres",
  "apellido": "Lopez",
  "email": "andres@correo.com",
  "telefono": "3001234567",
  "ciudad": "Bogota",
  "tipoNotificacion": "EMAIL"
}
```

| Campo | Descripción |
|---|---|
| `tipoNotificacion` | `EMAIL` o `SMS` |

**Respuesta (`200`):**
```json
{
  "mensaje": "Cliente Andres Lopez inscrito correctamente"
}
```

```bash
curl -X POST http://localhost:8080/cliente/registrar \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer <token>" \
     -d '{"nombre":"Andres","apellido":"Lopez","email":"andres@correo.com","telefono":"3001234567","ciudad":"Bogota","tipoNotificacion":"EMAIL"}'
```

---

### Fondos

#### `GET /fondos/productos` — Listar fondos disponibles `🔒`

Retorna todos los fondos de inversión disponibles.

**Respuesta (`200`):**
```json
[
  { "id": "1", "nombre": "FPV_BTG_PACTUAL_RECAUDADORA", "tipoProducto": "FPV", "monto": 75000  },
  { "id": "2", "nombre": "FPV_BTG_PACTUAL_ECOPETROL",   "tipoProducto": "FPV", "monto": 125000 },
  { "id": "3", "nombre": "DEUDAPRIVADA",                 "tipoProducto": "FIC", "monto": 50000  },
  { "id": "4", "nombre": "FDO-ACCIONES",                 "tipoProducto": "FIC", "monto": 250000 },
  { "id": "5", "nombre": "FPV_BTG_PACTUAL_DINAMICA",     "tipoProducto": "FPV", "monto": 100000 }
]
```

```bash
curl -X GET http://localhost:8080/fondos/productos \
     -H "Authorization: Bearer <token>"
```

---

#### `POST /fondos/suscribir` — Suscribirse a un fondo `🔒`

Suscribe a un cliente a un fondo. Descuenta el monto del saldo del cliente y envía notificación por EMAIL o SMS.

**Validaciones:**
- El cliente debe tener saldo suficiente
- El cliente no puede tener una suscripción activa al mismo fondo

```json
{
  "idCliente": "1",
  "idProducto": "1"
}
```

**Respuesta exitosa (`200`):**
```json
{
  "mensaje": "Suscripción al fondo FPV_BTG_PACTUAL_RECAUDADORA realizada exitosamente",
  "saldo": 425000,
  "transaccion": {
    "id": "abc123",
    "idProducto": "1",
    "idCliente": "1",
    "estado": "ACTIVO",
    "fechaApertura": "2026-04-05",
    "fechaCancelacion": null,
    "monto": 75000
  }
}
```

**Saldo insuficiente (`200`):**
```json
{
  "mensaje": "No tiene saldo disponible para vincularse al fondo FPV_BTG_PACTUAL_RECAUDADORA",
  "saldo": 10000
}
```

**Ya suscrito (`200`):**
```json
{
  "mensaje": "Ya tiene una suscripción activa al fondo FPV_BTG_PACTUAL_RECAUDADORA",
  "saldo": 425000
}
```

```bash
curl -X POST http://localhost:8080/fondos/suscribir \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer <token>" \
     -d '{"idCliente":"1","idProducto":"1"}'
```

---

#### `POST /fondos/cancelar` — Cancelar suscripción `🔒`

Cancela la suscripción activa de un cliente a un fondo. El monto es devuelto al saldo del cliente.

```json
{
  "idCliente": "1",
  "idProducto": "1"
}
```

**Respuesta exitosa (`200`):**
```json
{
  "mensaje": "Suscripción al fondo FPV_BTG_PACTUAL_RECAUDADORA cancelada. Monto retornado: COP $75000",
  "saldo": 500000,
  "transaccion": {
    "id": "abc123",
    "idProducto": "1",
    "idCliente": "1",
    "estado": "CANCELADO",
    "fechaApertura": "2026-04-05",
    "fechaCancelacion": "2026-04-05",
    "monto": 75000
  }
}
```

**Error (`400`):**
```json
{ "timestamp": "2026-04-05T10:00:00", "error": "No tiene suscripción activa al fondo FPV_BTG_PACTUAL_RECAUDADORA" }
```

```bash
curl -X POST http://localhost:8080/fondos/cancelar \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer <token>" \
     -d '{"idCliente":"1","idProducto":"1"}'
```

---

#### `GET /fondos/transacciones/{idCliente}` — Historial de transacciones `🔒`

Retorna el historial completo de suscripciones y cancelaciones de un cliente.

```bash
curl -X GET http://localhost:8080/fondos/transacciones/1 \
     -H "Authorization: Bearer <token>"
```

**Respuesta (`200`):**
```json
[
  {
    "id": "abc123",
    "idProducto": "1",
    "idCliente": "1",
    "estado": "CANCELADO",
    "fechaApertura": "2026-04-01",
    "fechaCancelacion": "2026-04-05",
    "monto": 75000
  },
  {
    "id": "def456",
    "idProducto": "3",
    "idCliente": "1",
    "estado": "ACTIVO",
    "fechaApertura": "2026-04-05",
    "fechaCancelacion": null,
    "monto": 50000
  }
]
```

---

### Health Check

#### `GET /actuator/health` — Estado del servicio

No requiere autenticación. Usado por el ALB de AWS para verificar disponibilidad.

**Respuesta (`200`):**
```json
{ "status": "UP" }
```

---

## Resumen de endpoints

| Método | Ruta | Auth | Descripción |
|---|---|---|---|
| `POST` | `/auth/login` | No | Obtener JWT |
| `POST` | `/cliente/registrar` | 🔒 | Registrar cliente |
| `GET` | `/fondos/productos` | 🔒 | Listar fondos disponibles |
| `POST` | `/fondos/suscribir` | 🔒 | Suscribir cliente a fondo |
| `POST` | `/fondos/cancelar` | 🔒 | Cancelar suscripción |
| `GET` | `/fondos/transacciones/{idCliente}` | 🔒 | Historial de transacciones |
| `GET` | `/actuator/health` | No | Health check |

---

## Manejo de errores

| HTTP | Causa |
|---|---|
| `400` | Error de negocio (cliente no encontrado, saldo insuficiente, etc.) |
| `401/403` | Token ausente, inválido o expirado |
| `500` | Error inesperado del servidor |

```json
{
  "timestamp": "2026-04-05T10:00:00.123",
  "error": "descripción del error"
}
```

---

## Flujo de uso

```
1. POST /auth/login              → Obtener token
2. POST /cliente/registrar       → Registrar cliente (saldo inicial $500.000)
3. GET  /fondos/productos        → Ver fondos disponibles
4. POST /fondos/suscribir        → Suscribirse (descuenta saldo, notifica)
5. GET  /fondos/transacciones/1  → Ver historial
6. POST /fondos/cancelar         → Cancelar (devuelve saldo)
```
