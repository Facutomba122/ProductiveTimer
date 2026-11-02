# ProductiveTimer API

Proyecto backend desarrollado con **Spring Boot 4.1.0** y **Java 25**, configurado con **Spring Web** y documentación interactiva mediante **Swagger / OpenAPI 3 (SpringDoc)**.

---

## 🚀 Requisitos y Tecnologías

- **Java**: 25 (ej. Microsoft OpenJDK 25)
- **Framework**: Spring Boot 4.1.0
  - `spring-boot-starter-web`: Servidor web embebido y arquitectura REST.
  - `spring-boot-starter-validation`: Validaciones de campos (Jakarta Validation).
  - `springdoc-openapi-starter-webmvc-ui` (v3.0.0): Swagger UI interactivo y especificaciones OpenAPI 3 compatibles con Spring Boot 4.
  - `spring-boot-starter-test`: Pruebas unitarias y de integración (JUnit 5, Mockito).
- **Gestor de dependencias**: Maven Wrapper (`mvnw` / `mvnw.cmd`) incluido.

---

## 🏃‍♂️ Cómo levantar el servidor

### Opción 1: Desde IntelliJ IDEA
1. Abre el proyecto en IntelliJ.
2. Asegúrate de que el SDK del proyecto esté configurado en Java 25 (`File > Project Structure > Project > SDK`).
3. Ve a `src/main/java/com/productivetimer/ProductiveTimerApplication.java` y haz clic en el botón verde de **Run**.

### Opción 2: Desde la terminal (PowerShell / CMD)
```powershell
.\mvnw.cmd spring-boot:run
```

---

## 📖 Documentación y Swagger UI

Una vez levantado el servidor (por defecto en el puerto `8080`):

- **Swagger UI Interactivo**:  
  👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
  *Permite visualizar todos los endpoints, modelos DTO y ejecutar peticiones directamente desde el navegador con el botón "Try it out".*

- **Especificación OpenAPI en formato JSON**:  
  👉 [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🛠️ Endpoints de Ejemplo incluidos

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/v1/timers` | Lista todas las sesiones de temporizador |
| `GET` | `/api/v1/timers/{id}` | Obtiene el detalle de un temporizador por su ID |
| `POST` | `/api/v1/timers` | Crea un nuevo temporizador (POMODORO, SHORT_BREAK, LONG_BREAK, CUSTOM) |
| `PATCH` | `/api/v1/timers/{id}/status?status=...` | Cambia el estado (PENDING, RUNNING, PAUSED, COMPLETED, CANCELLED) |
| `DELETE` | `/api/v1/timers/{id}` | Elimina una sesión por su ID |

---

## 🧪 Pruebas automáticas

Para ejecutar la suite de pruebas:
```powershell
.\mvnw.cmd test
```
