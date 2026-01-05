-- Catálogos Base (Géneros y Roles)
INSERT INTO cca01_genero(id_genero, tx_nombre, tx_descripcion, st_activo) VALUES (0, 'Hombre', 'Hombre', true);
INSERT INTO cca01_genero(id_genero, tx_nombre, tx_descripcion, st_activo) VALUES (1, 'Mujer', 'Mujer', true);

INSERT INTO cca02_rol(id_rol, tx_nombre, tx_descripcion, st_activo) VALUES (1, 'Admin', 'Admin', true);
INSERT INTO cca02_rol(id_rol, tx_nombre, tx_descripcion, st_activo) VALUES (2, 'Empleado', 'Empleado', true);
INSERT INTO cca02_rol(id_rol, tx_nombre, tx_descripcion, st_activo) VALUES (3, 'Cliente', 'Cliente', true);

-- CORRECCIÓN: Configuración inicial de Precios
-- Necesario para que funcione el alta de Servicios y Citas (LISTA_PRECIO_DEFAULT = 1)
INSERT INTO tci01_estado_lista_precio (id_estado, tx_nombre) VALUES (1, 'Activa');
INSERT INTO tci03_lista_precio (id_lista_precio, fk_id_estado, tx_nombre, fh_inicio) VALUES (1, 1, 'Lista General 2026', NOW());