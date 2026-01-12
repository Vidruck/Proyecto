-- =============================================
-- 1. CATÁLOGOS BASE
-- =============================================
INSERT INTO cca01_genero(id_genero, tx_nombre, tx_descripcion, st_activo) VALUES (0, 'Hombre', 'Hombre', true);
INSERT INTO cca01_genero(id_genero, tx_nombre, tx_descripcion, st_activo) VALUES (1, 'Mujer', 'Mujer', true);

INSERT INTO cca02_rol(id_rol, tx_nombre, tx_descripcion, st_activo) VALUES (1, 'Admin', 'Admin', true);
INSERT INTO cca02_rol(id_rol, tx_nombre, tx_descripcion, st_activo) VALUES (2, 'Empleado', 'Empleado', true);
INSERT INTO cca02_rol(id_rol, tx_nombre, tx_descripcion, st_activo) VALUES (3, 'Cliente', 'Cliente', true);

INSERT INTO tci01_estado_lista_precio (id_estado, tx_nombre) VALUES (1, 'Activa');
INSERT INTO tci03_lista_precio (id_lista_precio, fk_id_estado, tx_nombre, fh_inicio) VALUES (1, 1, 'Lista General 2026', NOW());

-- =============================================
-- 2. NEGOCIO Y SUCURSALES
-- =============================================
INSERT INTO tce01_establecimiento (id_establecimiento, tx_nombre) VALUES (1, 'Barbería UPIICSA');
INSERT INTO tce02_sucursal (id_sucursal, fk_id_establecimiento, tx_nombre, gm_ubicacion)
VALUES (1, 1, 'Campus UPIICSA', ST_GeomFromText('POINT(-99.0913 19.3962)', 4326));

-- =============================================
-- 3. SERVICIOS Y PRECIOS
-- =============================================
INSERT INTO cci01_servicio (id_servicio, tx_nombre, tx_descripcion, st_activo, nu_duracion) VALUES (1, 'Corte Clásico', 'Corte de cabello tradicional.', 1, 45);
INSERT INTO cci01_servicio (id_servicio, tx_nombre, tx_descripcion, st_activo, nu_duracion) VALUES (2, 'Barba Express', 'Perfilado de barba.', 1, 30);
INSERT INTO cci01_servicio (id_servicio, tx_nombre, tx_descripcion, st_activo, nu_duracion) VALUES (3, 'Paquete Completo', 'Corte, barba y mascarilla.', 1, 90);

INSERT INTO tci02_servicio_lista_precio (fk_id_servicio, fk_id_lista_precio, nu_precio) VALUES (1, 1, 150);
INSERT INTO tci02_servicio_lista_precio (fk_id_servicio, fk_id_lista_precio, nu_precio) VALUES (2, 1, 100);
INSERT INTO tci02_servicio_lista_precio (fk_id_servicio, fk_id_lista_precio, nu_precio) VALUES (3, 1, 350);

-- =============================================
-- 4. USUARIOS (Con Hash Validado)
-- =============================================
-- Password para todos: 'prueba' (o la que usaste para generar el log exitoso)
-- Hash Validado: rNuPWumSSmV+hDT+0iPLd2+FEXukGbP2+Eu/s+z0L1rStTJKf/iTzXyHnS0cIQgIhbkbDSgsu8FHpqFbRtM61w==

-- ADMIN
INSERT INTO tca01_persona (id_persona, fk_id_genero, tx_nombre, tx_primer_apellido, tx_segundo_apellido, fh_nacimiento) VALUES (1, 0, 'Admin', 'General', 'Sistema', '1990-01-01');
INSERT INTO tca02_usuario (id_usuario, fk_id_rol, tx_login, tx_password, st_activo)
VALUES (1, 1, 'admin@upiicsa.mx', 'rNuPWumSSmV+hDT+0iPLd2+FEXukGbP2+Eu/s+z0L1rStTJKf/iTzXyHnS0cIQgIhbkbDSgsu8FHpqFbRtM61w==', true);

-- EMPLEADO (JUAN)
INSERT INTO tca01_persona (id_persona, fk_id_genero, tx_nombre, tx_primer_apellido, tx_segundo_apellido, fh_nacimiento) VALUES (2, 0, 'Juan', 'Pérez', 'Barbero', '1995-05-15');
INSERT INTO tce03_empleado (id_empleado, fk_id_sucursal) VALUES (2, 1);
INSERT INTO tca02_usuario (id_usuario, fk_id_rol, tx_login, tx_password, st_activo)
VALUES (2, 2, 'juan@upiicsa.mx', 'rNuPWumSSmV+hDT+0iPLd2+FEXukGbP2+Eu/s+z0L1rStTJKf/iTzXyHnS0cIQgIhbkbDSgsu8FHpqFbRtM61w==', true);

-- CLIENTE (CARLOS)
INSERT INTO tca01_persona (id_persona, fk_id_genero, tx_nombre, tx_primer_apellido, tx_segundo_apellido, fh_nacimiento) VALUES (4, 0, 'Carlos', 'Cliente', 'Frecuente', '2000-10-10');
INSERT INTO tca02_usuario (id_usuario, fk_id_rol, tx_login, tx_password, st_activo)
VALUES (4, 3, 'cliente@gmail.com', 'rNuPWumSSmV+hDT+0iPLd2+FEXukGbP2+Eu/s+z0L1rStTJKf/iTzXyHnS0cIQgIhbkbDSgsu8FHpqFbRtM61w==', true);

-- =============================================
-- 5. AJUSTE DE SECUENCIAS
-- =============================================
SELECT setval('tce01_establecimiento_id_establecimiento_seq', 1, true);
SELECT setval('tce02_sucursal_id_sucursal_seq', 1, true);
SELECT setval('cci01_servicio_id_servicio_seq', 3, true);
SELECT setval('tci01_estado_lista_precio_id_estado_seq', 1, true);
SELECT setval('tci03_lista_precio_id_lista_precio_seq', 1, true);
SELECT setval('tca01_persona_id_persona_seq', 4, true);