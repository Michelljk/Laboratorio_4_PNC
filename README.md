# 🛡️ Laboratorio 4.0: Sistema de Seguridad con Spring Boot y JWT

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.8-brightgreen)
![Java](https://img.shields.io/badge/Java-21-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-lightgrey)
![JWT](https://img.shields.io/badge/JWT-0.12.6-orange)
![Maven](https://img.shields.io/badge/Maven-3.8.6-red)

Este proyecto presenta una implementación de un sistema de seguridad para aplicaciones web, desarrollado con **Spring Boot**, **Spring Security** y **JSON Web Tokens (JWT)**. El laboratorio se centra en la gestión de autenticación, autorización basada en roles y permisos, y la implementación de un mecanismo de autorización dinámica para el control de acceso a recursos de la API. El objetivo es proporcionar una comprensión práctica de los componentes esenciales para asegurar una API RESTful moderna.

## 📋 Tabla de Contenidos

- [Introducción](#introducción)
- [Características Clave](#características-clave)
- [Tecnologías Empleadas](#tecnologías-empleadas)
- [Entorno de Desarrollo y Flujo de Trabajo](#entorno-de-desarrollo-y-flujo-de-trabajo)
  - [Gestión de Base de Datos](#gestión-de-base-de-datos)
  - [Desarrollo de Backend](#desarrollo-de-backend)
  - [Pruebas de API](#pruebas-de-api)
- [Arquitectura de Seguridad](#arquitectura-de-seguridad)
- [Configuración y Despliegue](#configuración-y-despliegue)
- [Endpoints de la API](#endpoints-de-la-api)
- [Autorización Dinámica](#autorización-dinámica-1)
- [Manejo de Excepciones](#manejo-de-excepciones)
- [Contribución](#contribución)
- [Licencia](#licencia)

## 🌟 Características Clave

El sistema incorpora las siguientes funcionalidades principales:

-   **Autenticación de Usuarios**: Implementación de procesos de registro (`signup`) e inicio de sesión (`login`) con validación de credenciales. 🔑
-   **Gestión de Usuarios**: Funcionalidades para la actualización de perfiles, cambio de contraseñas y administración de usuarios (bloqueo, asignación de roles) por parte de usuarios con privilegios administrativos. 👥
-   **Control de Acceso Basado en Roles (RBAC)**: Asignación granular de roles a usuarios y definición de permisos específicos para cada rol. 🚦
-   **Autorización Dinámica**: Evaluación de permisos en tiempo de ejecución para controlar el acceso a los recursos de la API, ofreciendo flexibilidad en la gestión de políticas de seguridad. 🔄
-   **JSON Web Tokens (JWT)**: Utilización de tokens para la autenticación y el intercambio seguro de información entre el cliente y el servidor. 🔒
-   **Persistencia de Datos**: Integración con PostgreSQL mediante Spring Data JPA para la gestión de datos relacionales. 💾
-   **Validación de Datos**: Aplicación de `spring-boot-starter-validation` para asegurar la integridad y el formato correcto de los datos de entrada. ✅
-   **Configuración CORS**: Soporte para solicitudes de origen cruzado, facilitando la integración con aplicaciones frontend. 🌐

## 🛠️ Tecnologías Empleadas

El proyecto se ha desarrollado utilizando las siguientes tecnologías y herramientas:

-   **Lenguaje de Programación**: Java 21 ☕
-   **Framework Backend**: Spring Boot 3.5.8 🍃
    -   `spring-boot-starter-data-jpa`
    -   `spring-boot-starter-security`
    -   `spring-boot-starter-web`
    -   `spring-boot-starter-validation`
-   **Base de Datos**: PostgreSQL 🐘
-   **Utilidades**: Lombok (para reducción de código repetitivo) 🧩
-   **Seguridad**: jjwt 0.12.6 (implementación de JSON Web Tokens) 📜
-   **Gestión de Proyectos**: Apache Maven 3.8.6+ 📦



### Variables de Entorno

El proyecto utiliza variables de entorno para la configuración de la base de datos y parámetros de seguridad. Es necesario definir las siguientes:

| Variable                       | Descripción                                                                 | Ejemplo                                                                  |
| :----------------------------- | :-------------------------------------------------------------------------- | :----------------------------------------------------------------------- |
| `SPRING_APPLICATION_NAME`      | Nombre de la aplicación.                                                    | `spring-security-labo`                                                   |
| `SPRING_DATASOURCE_URL`        | URL de conexión a la base de datos PostgreSQL.                              | `jdbc:postgresql://localhost:5432/security_db`                           |
| `SPRING_DATASOURCE_USERNAME`   | Nombre de usuario de la base de datos.                                      | `user`                                                                   |
| `SPRING_DATASOURCE_PASSWORD`   | Contraseña del usuario de la base de datos.                                 | `password`                                                               |
| `SERVER_PORT`                  | Puerto en el que se ejecutará la aplicación.                                | `8080`                                                                   |
| `SECURITY_JWT_SECRET_KEY`      | Clave secreta para la firma y verificación de JWTs (debe ser robusta).      | `YourSuperSecretKeyForJWTGenerationAndValidationThatShouldBeLongAndRandom` |
| `SECURITY_JWT_EXPIRATION_TIME` | Tiempo de expiración del JWT en milisegundos.                               | `3600000` (1 hora)                                                       |


## 🌐 Endpoints de la API

Se presenta una descripción de los endpoints principales expuestos por la API:

### Autenticación y Perfil

-   `POST /api/auth/signup`: Registro de nuevos usuarios. (Request: `UserCreateDto`, Response: `User`)
-   `POST /api/auth/login`: Inicio de sesión y obtención de JWT. (Request: `LoginDto`, Response: `TokenResponse`)
-   `GET /api/auth/profile`: Recuperación del perfil del usuario autenticado. (Requiere JWT, Response: `User`)
-   `PUT /api/auth/update/profile`: Actualización del perfil del usuario autenticado. (Requiere JWT, Request: `UserUpdateDto`, Response: `User`)
-   `PUT /api/auth/update/password`: Cambio de contraseña del usuario autenticado. (Requiere JWT, Request: `UpdatePasswordDto`, Response: `User`)

### Gestión Administrativa (Requiere Permisos Específicos)

-   `GET /api/users`: Listado paginado de usuarios con opción de búsqueda. (Requiere JWT con permisos, Query Params: `page`, `size`, `search`, Response: `Pagination<User>`)
-   `POST /api/users`: Creación administrativa de usuarios. (Requiere JWT con permisos, Request: `UserCreateDto`, Response: `User`)
-   `PUT /api/users/{id}`: Actualización administrativa de un usuario. (Requiere JWT con permisos, Request: `UserUpdateDto`, Response: `User`)
-   `GET /api/roles`: Listado paginado de roles. (Requiere JWT con permisos, Response: `Pagination<Role>`)
-   `POST /api/roles`: Creación de nuevos roles. (Requiere JWT con permisos, Request: `RoleDto`, Response: `Role`)
-   `PUT /api/roles/{id}`: Actualización de un rol existente. (Requiere JWT con permisos, Request: `RoleDto`, Response: `Role`)
-   `GET /api/permissions`: Listado paginado de permisos. (Requiere JWT con permisos, Response: `Pagination<Permission>`)
-   `PUT /api/permissions/{id}`: Actualización de un permiso. (Requiere JWT con permisos, Request: `PermissionDto`, Response: `Permission`)
