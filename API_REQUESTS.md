# CoachBoard API — Peticiones de ejemplo

Base URL: `http://localhost:8080`

Todos los endpoints protegidos requieren el header:
```
Authorization: Bearer <token>
```

---

## Índice

- [Auth](#auth)
- [Platform Admin — Organizaciones](#platform-admin--organizaciones)
- [Organización — Usuarios y Staff](#organización--usuarios-y-staff)
- [Organización — Clientes](#organización--clientes)
- [Organización — Membresías](#organización--membresías)
- [Organización — Inscripciones](#organización--inscripciones)
- [Organización — Pagos y Métricas](#organización--pagos-y-métricas)
- [Organización — Notificaciones](#organización--notificaciones)
- [Cliente — Portal propio](#cliente--portal-propio)

---

## Auth

### Registrar nueva organización (público)

Crea la organización y su usuario `ORG_ADMIN` en una sola llamada. Devuelve JWT listo para usar.

```http
POST /api/auth/register
Content-Type: application/json

{
  "nombreOrganizacion": "GymFit",
  "slug": "gymfit",
  "tipoDeporte": "GYM",
  "plan": "BASICO",
  "username": "admin_gymfit",
  "password": "Admin@1234",
  "email": "admin@gymfit.com"
}
```

Valores válidos para `tipoDeporte`: `GYM` · `NATACION` · `ARTES_MARCIALES` · `CROSSFIT` · `DEPORTE_EQUIPO` · `OTRO`

Valores válidos para `plan`: `BASICO` · `PROFESIONAL`

---

### Login (público)

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin_gymfit",
  "password": "Admin@1234"
}
```

**Respuesta:**
```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
    "type": "Bearer",
    "userId": 1,
    "username": "admin_gymfit",
    "email": "admin@gymfit.com",
    "rol": "ORG_ADMIN",
    "organizacionId": 1
  }
}
```

---

### Renovar token

```http
POST /api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}
```

---

### Cerrar sesión

```http
POST /api/auth/logout
Authorization: Bearer <token>
```

---

### Perfil del usuario autenticado

```http
GET /api/auth/me
Authorization: Bearer <token>
```

---

## Platform Admin — Organizaciones

> Requiere rol `PLATFORM_ADMIN`

### Crear organización

```http
POST /api/platform/organizaciones
Authorization: Bearer <token>
Content-Type: application/json

{
  "nombre": "CrossFit Norte",
  "slug": "crossfit-norte",
  "tipoDeporte": "CROSSFIT",
  "plan": "PROFESIONAL"
}
```

---

### Listar todas las organizaciones

```http
GET /api/platform/organizaciones
Authorization: Bearer <token>
```

---

### Obtener organización por ID

```http
GET /api/platform/organizaciones/1
Authorization: Bearer <token>
```

---

### Actualizar organización

```http
PUT /api/platform/organizaciones/1
Authorization: Bearer <token>
Content-Type: application/json

{
  "nombre": "CrossFit Norte Actualizado",
  "tipoDeporte": "CROSSFIT",
  "plan": "PROFESIONAL"
}
```

---

### Activar / desactivar organización

```http
PATCH /api/platform/organizaciones/1/estado
Authorization: Bearer <token>
```

---

### Crear admin para una organización existente

```http
POST /api/platform/organizaciones/1/admins
Authorization: Bearer <token>
Content-Type: application/json

{
  "username": "admin_norte",
  "password": "Admin@5678",
  "email": "admin@crossfitnorte.com"
}
```

---

## Organización — Usuarios y Staff

> Requiere rol `ORG_ADMIN`

### Crear miembro de staff

```http
POST /api/org/usuarios/staff
Authorization: Bearer <token>
Content-Type: application/json

{
  "username": "recepcion_01",
  "password": "Staff@1234",
  "email": "recepcion@gymfit.com"
}
```

---

### Listar staff de la organización

```http
GET /api/org/usuarios/staff
Authorization: Bearer <token>
```

---

### Activar / desactivar usuario

```http
PATCH /api/org/usuarios/3/estado
Authorization: Bearer <token>
```

---

## Organización — Clientes

> Requiere rol `ORG_ADMIN` o `ORG_STAFF`

### Registrar cliente

Crea el perfil de cliente y su usuario `ORG_CLIENTE` vinculado.

```http
POST /api/org/clientes
Authorization: Bearer <token>
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "celular": "3001234567",
  "documento": "1020304050",
  "username": "juan_perez",
  "password": "Cliente@1234",
  "email": "juan@email.com"
}
```

---

### Listar clientes de la organización

```http
GET /api/org/clientes
Authorization: Bearer <token>
```

---

### Obtener cliente por ID

```http
GET /api/org/clientes/1
Authorization: Bearer <token>
```

---

### Buscar clientes por nombre

```http
GET /api/org/clientes/buscar?nombre=Juan
Authorization: Bearer <token>
```

---

### Actualizar cliente

```http
PUT /api/org/clientes/1
Authorization: Bearer <token>
Content-Type: application/json

{
  "nombre": "Juan Alberto Pérez",
  "celular": "3009876543"
}
```

---

## Organización — Membresías

> Crear / actualizar / toggle: `ORG_ADMIN` · Listar: `ORG_ADMIN` o `ORG_STAFF`

### Crear membresía

```http
POST /api/org/membresias
Authorization: Bearer <token>
Content-Type: application/json

{
  "nombre": "Mensual Básico",
  "descripcion": "Acceso completo al gimnasio por 30 días",
  "duracionDias": 30,
  "precio": 80000
}
```

---

### Listar membresías

```http
GET /api/org/membresias
Authorization: Bearer <token>
```

---

### Listar solo membresías activas

```http
GET /api/org/membresias/activas
Authorization: Bearer <token>
```

---

### Obtener membresía por ID

```http
GET /api/org/membresias/1
Authorization: Bearer <token>
```

---

### Actualizar membresía

```http
PUT /api/org/membresias/1
Authorization: Bearer <token>
Content-Type: application/json

{
  "nombre": "Mensual Básico Plus",
  "descripcion": "Acceso completo más 2 clases grupales",
  "duracionDias": 30,
  "precio": 95000
}
```

---

### Activar / desactivar membresía

```http
PATCH /api/org/membresias/1/estado
Authorization: Bearer <token>
```

---

## Organización — Inscripciones

> Crear / listar: `ORG_ADMIN` o `ORG_STAFF` · Cambiar estado: `ORG_ADMIN`

### Crear inscripción

```http
POST /api/org/inscripciones
Authorization: Bearer <token>
Content-Type: application/json

{
  "clienteId": 1,
  "tipoMembresiaId": 1,
  "fechaDeInicio": "2026-06-01"
}
```

---

### Listar todas las inscripciones

```http
GET /api/org/inscripciones
Authorization: Bearer <token>
```

---

### Listar inscripciones por estado

Valores válidos: `PENDIENTE_PAGO` · `ACTIVA` · `VENCIDA` · `CANCELADA`

```http
GET /api/org/inscripciones?estado=ACTIVA
Authorization: Bearer <token>
```

---

### Obtener inscripción por ID

```http
GET /api/org/inscripciones/1
Authorization: Bearer <token>
```

---

### Historial de inscripciones de un cliente

```http
GET /api/org/clientes/1/inscripciones
Authorization: Bearer <token>
```

---

### Cambiar estado de inscripción

```http
PATCH /api/org/inscripciones/1/estado?estado=CANCELADA
Authorization: Bearer <token>
```

---

## Organización — Pagos y Métricas

> Registrar / listar: `ORG_ADMIN` o `ORG_STAFF` · Métricas: `ORG_ADMIN`

### Registrar pago en una inscripción

Valores válidos para `metodoDePago`: `EFECTIVO` · `TRANSFERENCIA` · `TARJETA`

```http
POST /api/org/inscripciones/1/pagos
Authorization: Bearer <token>
Content-Type: application/json

{
  "monto": 80000,
  "metodoDePago": "EFECTIVO"
}
```

---

### Listar pagos de una inscripción

```http
GET /api/org/inscripciones/1/pagos
Authorization: Bearer <token>
```

---

### Listar pagos pendientes de la organización

```http
GET /api/org/pagos/pendientes
Authorization: Bearer <token>
```

---

### Calcular ingresos en un rango de fechas

```http
GET /api/org/metricas/ingresos?desde=2026-01-01T00:00:00&hasta=2026-05-31T23:59:59
Authorization: Bearer <token>
```

---

## Organización — Notificaciones

> Requiere rol `ORG_ADMIN` o `ORG_STAFF`

### Listar notificaciones no leídas de la organización

```http
GET /api/org/notificaciones
Authorization: Bearer <token>
```

---

## Cliente — Portal propio

> Requiere rol `ORG_CLIENTE`

### Ver mi perfil

```http
GET /api/cliente/perfil
Authorization: Bearer <token>
```

---

### Ver mis inscripciones

```http
GET /api/cliente/inscripciones
Authorization: Bearer <token>
```

---

### Ver mis pagos

```http
GET /api/cliente/pagos
Authorization: Bearer <token>
```

---

### Ver mis notificaciones

```http
GET /api/cliente/notificaciones
Authorization: Bearer <token>
```

---

### Contar notificaciones no leídas

```http
GET /api/cliente/notificaciones/no-leidas/count
Authorization: Bearer <token>
```

---

### Marcar una notificación como leída

```http
PATCH /api/cliente/notificaciones/1/leida
Authorization: Bearer <token>
```

---

### Marcar todas las notificaciones como leídas

```http
PATCH /api/cliente/notificaciones/leidas
Authorization: Bearer <token>
```
