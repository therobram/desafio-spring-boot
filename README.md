# Entrega Final - Desafío Técnico NUEVO SPA

## 🌟 Información General

**Nombre:** Roberto Ramírez Romero
**Correo:** rramirezr2005@gmail.com  
**Cargo al que postula:** Backend Developer Java / Spring Boot

---

## 🚀 Tecnologías Utilizadas

- Java 17
- Spring Boot 3.4.5
- Maven
- Spring Security con JWT
- Spring Data JPA
- Base de datos en memoria H2
- OpenAPI 3 + Swagger UI
- API First con openapi-generator

---

## 💳 Estructura del Proyecto

- `entity/`: Entidades JPA `User`, `Task`, `TaskStatus`
- `dto/`: DTOs para creación, respuesta y actualización de tareas y login
- `repository/`: Repositorios JPA
- `service/`: Servicio de tareas y seguridad JWT
- `controller/`: Controladores REST de autenticación y tareas
- `security/`: Filtros, providers y servicios para JWT y autenticación
- `config/`: Seguridad y carga de datos iniciales (usuarios + estados)

---

## 🔐 Seguridad con JWT

- Endpoint de login en `/auth/login` devuelve un JWT válido.
- Endpoints de tareas están protegidos por `JwtAuthFilter`.
- Token JWT se valida en cada request.

---

## 🏃️ CRUD de Tareas

- `GET /tasks` → listar tareas del usuario autenticado
- `GET /tasks/{id}` → Ver una tarea
- `POST /tasks` → crear tarea
- `PUT /tasks/{id}` → actualizar tarea (solo suya)
- `DELETE /tasks/{id}` → eliminar tarea (solo suya)

---

## 🌐 Documentación Swagger / OpenAPI

- Accede a [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Generado automáticamente con springdoc-openapi-starter 2.8.5 (compatible con Spring Boot 3.4.x)

---

## 🔹 Ejecución del Proyecto

```bash
# Clonar el repositorio
$ git clone https://github.com/therobram/desafio-spring-boot.git
$ cd desafio-spring-boot

# Compilar y ejecutar
$ mvn clean install
$ mvn spring-boot:run
```

---

## 👥 Usuarios precargados (en H2)

- `admin` / `admin123`
- `user` / `user123`

Consola H2: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:mem:taskdb`
- Usuario: `sa`
- Password: *(vacío)*

---

## 📂 Archivos Incluidos

- `openapi.yml` → definición de la API (API First)
- `Task Manager API - Desafío NUEVO SPA.postman_collection.json` → colección de Postman completa
- `README.md` profesional con instrucciones

---

📅 **Fecha de entrega:** 2025-04-07  
🙌 **Gracias por su consideración.**

