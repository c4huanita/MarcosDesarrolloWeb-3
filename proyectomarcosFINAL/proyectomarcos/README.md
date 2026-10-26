# Patitas Felices – Entrega 3

Aplicación web dinámica: **Spring Boot 3.2 · Thymeleaf · Spring Data JPA/Hibernate · MySQL · Bean Validation**.

## Cómo ejecutar
1. Tener MySQL encendido en `localhost:3306` (usuario `root`, contraseña `Greco1607!`).
   La base `db_patitas_felices` se crea sola; opcionalmente ejecuta `database/script_patitas_felices.sql`.
2. En la carpeta `demo/`:  `./mvnw spring-boot:run`  (Windows: `mvnw.cmd spring-boot:run`) o ejecuta
   `MarcosDesarrolloWebApplication` desde VS Code.
3. Abrir <http://localhost:8085>.

Si cambias la contraseña o el puerto de MySQL, edita `demo/src/main/resources/application.properties`.

## Acceso
- Administrador: `admin@patitas.com` / `123456` (se crea solo al primer arranque).
- Registro público: un correo `@patitas.com` queda como Administrador; cualquier otro como Adoptante.

## Módulos CRUD (panel `/admin`, solo rol Administrador)
| Módulo | Registrar | Listar | Editar | Eliminar |
|---|---|---|---|---|
| Categorías | ✔ | ✔ | ✔ | ✔ |
| Mascotas (con categoría y foto) | ✔ | ✔ | ✔ | ✔ |
| Usuarios (con rol) | ✔ | ✔ | ✔ | ✔ |
| Solicitudes de adopción | ✔ (formulario público) | ✔ | ✔ (estado) | ✔ |

Modelo relacional completo en `docs/MODELO_ENTIDAD_RELACION.md`.
