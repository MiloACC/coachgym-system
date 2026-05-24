# CoachBoard System

## Descripción del Proyecto

**CoachBoard System** es una plataforma web desarrollada para la gestión y control de pagos, membresías y seguimiento administrativo en instituciones deportivas de Villavicencio.

El sistema permite administrar usuarios, registrar pagos, controlar vencimientos de membresías y visualizar métricas financieras mediante dashboards interactivos.

Además, integra autenticación segura con JWT y control de acceso basado en roles (`ADMIN`, `COACH`, `USER`).


# Tecnologías Utilizadas

## Backend

* Java 21
* Spring Boot
* Spring Security + JWT
* Spring Data JPA
* PostgreSQL
* Maven
* Swagger / OpenAPI

## Frontend

* React
* JavaScript
* HTML5
* CSS3

## Herramientas

* Git & GitHub
* IntelliJ IDEA
* VS Code
* Supabase

---

# Integrantes del Proyecto

| Nombre                           | Rol                                    |
| -------------------------------- | -------------------------------------- |
| Sheila Nayeli Torres Berrio      | Líder Front-end y Documentación        |
| Juan Jose Torres Quintin         | Líder Back-end y Seguridad             |
| Jonathan Alexis Panesso Toro     | Líder de Bases de Datos y Persistencia |
| Camilo Andres Casallas Castañeda | Líder Dashboard, Reportes y Validación |


# Problema que Resuelve

Las instituciones deportivas en Villavicencio manejan pagos, membresías y seguimiento de clientes de forma manual o desorganizada.

Esto genera:

* Pérdidas financieras por falta de control.
* Usuarios morosos sin seguimiento.
* Desorden administrativo.
* Baja fidelización de clientes.
* Dificultad para visualizar métricas del negocio.

CoachBoard System busca centralizar y automatizar estos procesos mediante una plataforma moderna y segura.

# Funcionalidades Principales

* Autenticación con JWT
* Gestión de usuarios
* Gestión de membresías
* Registro de pagos
* Dashboard administrativo
* Notificaciones internas
* Control de roles y permisos
* Historial de pagos
* Visualización de usuarios morosos
* Métricas financieras en tiempo real


# Roles del Sistema

| Rol   | Descripción                          |
| ----- | ------------------------------------ |
| ADMIN | Control total del sistema            |
| STAFF | Consulta usuarios y membresías       |
| USER  | Consulta pagos y estado de membresía |



### Frontend

git clone https://github.com/Joalpato/coach-gym-system-front.git


# Ejecutar Backend

Entrar a la carpeta del backend:

```bash
cd coachgym-system
```

Ejecutar:

```bash
mvn spring-boot:run
```

El backend iniciará en:

```bash
http://localhost:8080
```



# 3️Ejecutar Frontend

Entrar a la carpeta del frontend:

```bash
cd coach-gym-system-front
```

Instalar dependencias:

```bash
npm install
```

Ejecutar:

```bash
npm run dev
```

El frontend iniciará en:

```bash
http://localhost:5173
```

# Endpoints Principales

## Autenticación

| Método | Endpoint         | Descripción       |
| ------ | ---------------- | ----------------- |
| POST   | `/auth/login`    | Iniciar sesión    |
| POST   | `/auth/register` | Registrar usuario |


## Usuarios

| Método | Endpoint          | Descripción      |
| ------ | ----------------- | ---------------- |
| GET    | `/api/users`      | Listar usuarios  |
| POST   | `/api/users`      | Crear usuario    |
| PUT    | `/api/users/{id}` | Editar usuario   |
| DELETE | `/api/users/{id}` | Eliminar usuario |


## Pagos

| Método | Endpoint                     | Descripción        |
| ------ | ---------------------------- | ------------------ |
| POST   | `/api/payments`              | Registrar pago     |
| GET    | `/api/payments/history/{id}` | Historial de pagos |


## Membresías

| Método | Endpoint                | Descripción          |
| ------ | ----------------------- | -------------------- |
| GET    | `/api/memberships`      | Consultar membresías |
| POST   | `/api/memberships`      | Crear membresía      |
| PUT    | `/api/memberships/{id}` | Editar membresía     |


## Dashboard

| Método | Endpoint                 | Descripción              |
| ------ | ------------------------ | ------------------------ |
| GET    | `/api/dashboard/metrics` | Métricas administrativas |



# Arquitectura del Proyecto

El sistema implementa:

* Arquitectura cliente-servidor
* API REST
* Arquitectura por capas
* Seguridad JWT
* Control de acceso por roles


# Estado del Proyecto

Backend funcional
Seguridad JWT implementada
Dashboard administrativo
Gestión de pagos y membresías
Mejoras futuras en frontend y métricas avanzadas


# Licencia

Proyecto académico desarrollado para la Universidad Santo Tomás — Ingeniería de Sistemas.