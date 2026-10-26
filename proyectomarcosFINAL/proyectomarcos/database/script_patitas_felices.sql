-- =====================================================================
-- Proyecto: Patitas Felices - Sistema de adopción de mascotas
-- Entrega 3 - Base de datos MySQL
-- Uso: ejecutar en MySQL Workbench / consola:  source script_patitas_felices.sql
-- (La aplicación también crea estas tablas sola con spring.jpa.hibernate.ddl-auto=update)
-- =====================================================================

CREATE DATABASE IF NOT EXISTS db_patitas_felices
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE db_patitas_felices;

-- Limpieza para poder re-ejecutar el script
DROP TABLE IF EXISTS solicitudes;
DROP TABLE IF EXISTS mascotas;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS categorias;
DROP TABLE IF EXISTS roles;

-- ---------------------------------------------------------------------
-- 1) ROLES
-- ---------------------------------------------------------------------
CREATE TABLE roles (
    id     BIGINT      NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_roles_nombre (nombre)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 2) USUARIOS  (N usuarios -> 1 rol)
-- ---------------------------------------------------------------------
CREATE TABLE usuarios (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    nombre   VARCHAR(100) NOT NULL,
    email    VARCHAR(150) NOT NULL,
    password VARCHAR(60)  NOT NULL,
    rol_id   BIGINT       NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_usuarios_email (email),
    CONSTRAINT fk_usuarios_rol FOREIGN KEY (rol_id) REFERENCES roles (id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 3) CATEGORIAS
-- ---------------------------------------------------------------------
CREATE TABLE categorias (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(50)  NOT NULL,
    descripcion VARCHAR(255) NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 4) MASCOTAS  (N mascotas -> 1 categoría)
-- ---------------------------------------------------------------------
CREATE TABLE mascotas (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    nombre       VARCHAR(50)  NOT NULL,
    raza         VARCHAR(60)  NULL,
    edad         VARCHAR(30)  NULL,
    sexo         VARCHAR(10)  NULL,
    tamano       VARCHAR(30)  NULL,
    peso         DOUBLE       NULL,
    personalidad VARCHAR(50)  NULL,
    descripcion  TEXT         NULL,
    imagen       VARCHAR(255) NULL,
    categoria_id BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_mascotas_categoria FOREIGN KEY (categoria_id) REFERENCES categorias (id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- 5) SOLICITUDES DE ADOPCIÓN (N solicitudes -> 1 mascota, N solicitudes -> 1 usuario)
-- ---------------------------------------------------------------------
CREATE TABLE solicitudes (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    nombre_completo VARCHAR(100) NULL,
    telefono        VARCHAR(20)  NULL,
    motivo          TEXT         NULL,
    estado          VARCHAR(20)  NULL DEFAULT 'Pendiente',
    fecha_solicitud DATE         NULL,
    mascota_id      BIGINT       NULL,
    usuario_id      BIGINT       NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_solicitudes_mascota FOREIGN KEY (mascota_id) REFERENCES mascotas (id) ON DELETE CASCADE,
    CONSTRAINT fk_solicitudes_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- =====================================================================
-- DATOS INICIALES
-- =====================================================================
INSERT INTO roles (nombre) VALUES ('Administrador'), ('Adoptante');

INSERT INTO usuarios (nombre, email, password, rol_id) VALUES
    ('Administrador General', 'admin@patitas.com',  '123456', 1),
    ('María Gómez',           'maria@gmail.com',    '123456', 2),
    ('Juan Pérez',            'juan@gmail.com',     '123456', 2);

INSERT INTO categorias (nombre, descripcion) VALUES
    ('Cachorros',   'Mascotas menores a 6 meses'),
    ('Adolescente', 'Mascotas entre los 6 meses a 2 años'),
    ('Adulto',      'Mascotas entre los 2 a 7 años');

INSERT INTO mascotas (nombre, raza, edad, sexo, tamano, peso, personalidad, descripcion, imagen, categoria_id) VALUES
    ('Max',   'Labrador',   '4 meses', 'Macho',  'Mediano', 8.5,  'Juguetón',  'Cachorro muy cariñoso que busca una familia activa.', '/Img/perrito1.jpg', 1),
    ('Luna',  'Siamesa',    '1 año',   'Hembra', 'Pequeño', 3.2,  'Tranquila', 'Gata dulce y tranquila, ideal para departamento.',    '/Img/gato1.jpg',    2),
    ('Rocky', 'Mestizo',    '4 años',  'Macho',  'Grande',  25.0, 'Leal',      'Perro adulto, educado y excelente con niños.',        '/Img/perro3.jpg',   3),
    ('Mia',   'Persa',      '3 años',  'Hembra', 'Mediano', 4.5,  'Cariñosa',  'Gata adulta, muy cariñosa y juguetona.',              '/Img/gato2.jpg',    3),
    ('Toby',  'Beagle',     '6 meses', 'Macho',  'Mediano', 9.0,  'Activo',    'Cachorro enérgico y curioso, ideal para familias con tiempo.', '/Img/perrito2.jpg', 1), 
    ('Nala',  'Mestiza',    '2 años',  'Hembra', 'Grande',  20.0, 'Amigable',  'Perra adulta, sociable y juguetona.',                 '/Img/perro4.jpg',   3),
    ('Simba', 'Mestizo',    '5 años',  'Macho',  'Grande',  30.0, 'Protector', 'Perro adulto, protector y leal.',                     '/Img/perro5.jpg',   3),
    ('Coco',  'Mestizo',    '1 año',   'Macho',  'Mediano', 10.0, 'Juguetón',  'Perro joven, juguetón y activo.',                     '/Img/perrito3.jpg', 2),
    ('Lola',  'Mestiza',    '3 años',  'Hembra', 'Mediano', 12.0, 'Cariñosa',  'Perra adulta, cariñosa y tranquila.',                 '/Img/perro6.jpg',   3),
    ('Chloe', 'Mestiza',    '2 años',  'Hembra', 'Pequeño', 4.0,  'Tranquila', 'Gata adulta, tranquila y sociable.',                  '/Img/gato3.jpg',    3),
    ('Buddy', 'Mestizo',    '7 meses', 'Macho',  'Mediano', 11.0, 'Activo',    'Cachorro enérgico y juguetón.',                       '/Img/perrito4.jpg', 1),
    ('Daisy', 'Mestiza',    '4 años',  'Hembra', 'Grande',  22.0, 'Amigable',  'Perra adulta, amigable y sociable.',                  '/Img/perro7.jpg',   3),
    ('Leo',   'Mestizo',    '3 años',  'Macho',  'Grande',  28.0, 'Protector', 'Perro adulto, protector y leal.',                     '/Img/perro8.jpg',   3),
    ('Bella', 'Mestiza',    '1 año',   'Hembra', 'Mediano', 9.5,  'Cariñosa',  'Perra joven, cariñosa y juguetona.',                  '/Img/perrito5.jpg', 2),
    ('Milo',  'Mestizo',    '5 años',  'Macho',  'Grande',  26.0, 'Leal',      'Perro adulto, leal y protector.',                     '/Img/perro9.jpg',   3);
INSERT INTO solicitudes (nombre_completo, telefono, motivo, estado, fecha_solicitud, mascota_id, usuario_id) VALUES
    ('María Gómez', '987654321', 'Tengo casa con jardín y mucho tiempo para dedicarle.', 'Pendiente',   CURDATE(), 2, 2),
    ('Juan Pérez',  '912345678', 'Siempre quise un perro y mi familia está de acuerdo.',  'En revisión', CURDATE(), 1, 3);

-- Consulta de verificación (JOIN entre las 5 tablas)
-- SELECT s.id, s.nombre_completo, m.nombre AS mascota, c.nombre AS categoria, u.email, r.nombre AS rol
-- FROM solicitudes s
-- JOIN mascotas m   ON m.id = s.mascota_id
-- JOIN categorias c ON c.id = m.categoria_id
-- LEFT JOIN usuarios u ON u.id = s.usuario_id
-- LEFT JOIN roles r    ON r.id = u.rol_id;
