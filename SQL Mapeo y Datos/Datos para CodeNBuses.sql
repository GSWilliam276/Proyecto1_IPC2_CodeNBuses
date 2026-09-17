USE gestion_buses;

-- ============================================
-- SUCURSALES (ids 1-4)
-- ============================================
INSERT INTO sucursal (nombre, ubicacion, latitud, longitud) VALUES
('Terminal Quetzaltenango', 'Zona 3, Quetzaltenango', 14.8443, -91.5198),
('Terminal Ciudad de Guatemala', 'Zona 4, Ciudad de Guatemala', 14.6349, -90.5069),
('Terminal Cobán', 'Zona 1, Cobán, Alta Verapaz', 15.4708, -90.3708),
('Terminal Huehuetenango', 'Zona 1, Huehuetenango', 15.3197, -91.4708);

-- ============================================
-- ADMIN SISTEMA (id_usuario = 1)
-- ============================================
INSERT INTO usuario (nombre, nit, dpi, telefono, direccion, correo, contrasena, tipo, activo) VALUES
('Roberto Martínez', '1234567', '1111111111111', '55551111', 'Zona 10, Guatemala', 'admin@codenbuses.com', 'admin123', 'ADMIN_SISTEMA', true);
INSERT INTO admin_sistema (id_usuario) VALUES (1);

-- ============================================
-- ADMIN SUCURSAL (ids_usuario 2-5)
-- ============================================
INSERT INTO usuario (nombre, nit, dpi, telefono, direccion, correo, contrasena, tipo, activo) VALUES
('Carlos Méndez', '2234567', '2222222222221', '55552222', 'Zona 3, Quetzaltenango', 'carlos.mendez@codenbuses.com', 'admin123', 'ADMIN_SUCURSAL', true),
('Ana López', '2234568', '2222222222222', '55552223', 'Zona 4, Guatemala', 'ana.lopez@codenbuses.com', 'admin123', 'ADMIN_SUCURSAL', true),
('Marta Gómez', '2234569', '2222222222223', '55552224', 'Zona 1, Cobán', 'marta.gomez@codenbuses.com', 'admin123', 'ADMIN_SUCURSAL', true),
('Luis Fuentes', '2234570', '2222222222224', '55552225', 'Zona 1, Huehuetenango', 'luis.fuentes@codenbuses.com', 'admin123', 'ADMIN_SUCURSAL', true);

INSERT INTO admin_sucursal (id_usuario, id_sucursal) VALUES
(2, 1), (3, 2), (4, 3), (5, 4);

-- ============================================
-- CHOFERES (ids_usuario 6-13)
-- ============================================
INSERT INTO usuario (nombre, nit, dpi, telefono, direccion, correo, contrasena, tipo, activo) VALUES
('Pedro Ramírez', '3234561', '3333333333331', '55553331', 'Xela', 'pedro.ramirez@codenbuses.com', 'chofer123', 'CHOFER', true),
('Juan Castillo', '3234562', '3333333333332', '55553332', 'Xela', 'juan.castillo@codenbuses.com', 'chofer123', 'CHOFER', true),
('Miguel Rodas', '3234563', '3333333333333', '55553333', 'Guatemala', 'miguel.rodas@codenbuses.com', 'chofer123', 'CHOFER', true),
('Fernando Ical', '3234564', '3333333333334', '55553334', 'Guatemala', 'fernando.ical@codenbuses.com', 'chofer123', 'CHOFER', true),
('Oscar Tzul', '3234565', '3333333333335', '55553335', 'Cobán', 'oscar.tzul@codenbuses.com', 'chofer123', 'CHOFER', true),
('Ricardo Xoc', '3234566', '3333333333336', '55553336', 'Cobán', 'ricardo.xoc@codenbuses.com', 'chofer123', 'CHOFER', true),
('Manuel Pérez', '3234567', '3333333333337', '55553337', 'Huehuetenango', 'manuel.perez@codenbuses.com', 'chofer123', 'CHOFER', true),
('Julio Aguilar', '3234568', '3333333333338', '55553338', 'Huehuetenango', 'julio.aguilar@codenbuses.com', 'chofer123', 'CHOFER', true);

INSERT INTO chofer (id_usuario, numero_licencia, tipo_licencia, fecha_vencimiento, salario_base, id_sucursal, foto) VALUES
(6, '3333333333331', 'A', '2027-06-15', 3500.00, 1, NULL),
(7, '3333333333332', 'B', '2026-12-01', 3200.00, 1, NULL),
(8, '3333333333333', 'A', '2027-03-20', 3600.00, 2, NULL),
(9, '3333333333334', 'B', '2027-08-10', 3300.00, 2, NULL),
(10, '3333333333335', 'A', '2026-11-05', 3400.00, 3, NULL),
(11, '3333333333336', 'B', '2027-01-25', 3250.00, 3, NULL),
(12, '3333333333337', 'A', '2027-05-30', 3550.00, 4, NULL),
(13, '3333333333338', 'B', '2026-10-15', 3150.00, 4, NULL);

-- ============================================
-- CLIENTES (ids_usuario 14-17)
-- ============================================
INSERT INTO usuario (nombre, nit, dpi, telefono, direccion, correo, contrasena, tipo, activo) VALUES
('Sofía Herrera', '4234561', '4444444444441', '55554441', 'Zona 1, Guatemala', 'sofia.herrera@gmail.com', 'cliente123', 'CLIENTE', true),
('Diego Morales', '4234562', '4444444444442', '55554442', 'Zona 5, Guatemala', 'diego.morales@gmail.com', 'cliente123', 'CLIENTE', true),
('Valeria Soto', '4234563', '4444444444443', '55554443', 'Xela', 'valeria.soto@gmail.com', 'cliente123', 'CLIENTE', true),
('Andrés Pineda', '4234564', '4444444444444', '55554444', 'Cobán', 'andres.pineda@gmail.com', 'cliente123', 'CLIENTE', true);

INSERT INTO cliente_regular (id_usuario) VALUES (14), (15), (16), (17);

INSERT INTO cartera (id_usuario, saldo) VALUES
(14, 1500.00),
(15, 800.00),
(16, 2000.00),
(17, 500.00);

-- ============================================
-- BUSES (ids 1-8)
-- ============================================
INSERT INTO bus (id_sucursal, placa, marca, modelo, anio, capacidad, estado_operativo, kilometraje, foto, activo) VALUES
(1, 'P123ABC', 'Volvo', 'B270F', 2018, 45, 'DISPONIBLE', 85000.00, NULL, true),
(1, 'P124ABD', 'Mercedes-Benz', 'OF-1721', 2019, 40, 'DISPONIBLE', 62000.00, NULL, true),
(2, 'P125ABE', 'Scania', 'K310', 2020, 50, 'DISPONIBLE', 45000.00, NULL, true),
(2, 'P126ABF', 'Volvo', 'B270F', 2017, 45, 'DISPONIBLE', 98000.00, NULL, true),
(3, 'P127ABG', 'Mercedes-Benz', 'OF-1721', 2021, 40, 'DISPONIBLE', 30000.00, NULL, true),
(3, 'P128ABH', 'Scania', 'K310', 2019, 50, 'DISPONIBLE', 55000.00, NULL, true),
(4, 'P129ABI', 'Volvo', 'B270F', 2020, 45, 'DISPONIBLE', 40000.00, NULL, true),
(4, 'P130ABJ', 'Mercedes-Benz', 'OF-1721', 2018, 40, 'DISPONIBLE', 75000.00, NULL, true);

-- ============================================
-- RUTAS (ids 1-8)
-- ============================================
INSERT INTO ruta (id_sucursal_origen, id_sucursal_destino, distancia_km, precio_boleto) VALUES
(1, 2, 210.00, 75.00),
(2, 1, 210.00, 75.00),
(2, 3, 215.00, 80.00),
(3, 2, 215.00, 80.00),
(1, 4, 90.00, 40.00),
(4, 1, 90.00, 40.00),
(2, 4, 265.00, 90.00),
(4, 2, 265.00, 90.00);

-- ============================================
-- CONFIGURACION
-- ============================================
INSERT INTO configuracion (monto_depreciacion_km, fecha_actualizacion) VALUES
(0.85, '2026-08-01 08:00:00');

-- ============================================
-- VIAJES REGULARES (ids_viaje 1-8)
-- ============================================
INSERT INTO viaje (id_bus, id_chofer, fecha_hora_salida, fecha_hora_llegada_estimada, tipo) VALUES
(1, 6, '2026-09-10 06:00:00', '2026-09-10 09:30:00', 'REGULAR'),
(3, 8, '2026-09-12 07:00:00', '2026-09-12 10:30:00', 'REGULAR'),
(5, 10, '2026-09-14 08:00:00', '2026-09-14 12:00:00', 'REGULAR'),
(7, 12, '2026-09-16 06:30:00', '2026-09-16 07:30:00', 'REGULAR'),
(2, 7, '2026-09-16 15:00:00', '2026-09-16 18:30:00', 'REGULAR'),
(4, 9, '2026-09-18 07:00:00', '2026-09-18 10:30:00', 'REGULAR'),
(6, 11, '2026-09-20 08:00:00', '2026-09-20 12:00:00', 'REGULAR'),
(8, 13, '2026-09-22 06:30:00', '2026-09-22 07:30:00', 'REGULAR');

INSERT INTO viaje_regular (id_viaje, id_ruta) VALUES
(1, 1), (2, 3), (3, 5), (4, 7), (5, 2), (6, 4), (7, 6), (8, 8);

-- ============================================
-- REGISTROS DE SALIDA (viajes 1-5)
-- ============================================
INSERT INTO registro_salida (id_viaje, hora_salida_real, kilometraje_salida) VALUES
(1, '2026-09-10 06:05:00', 85000.00),
(2, '2026-09-12 07:03:00', 45000.00),
(3, '2026-09-14 08:02:00', 30000.00),
(4, '2026-09-16 06:32:00', 40000.00),
(5, '2026-09-16 15:04:00', 62000.00);

-- ============================================
-- REGISTROS DE LLEGADA (viajes 1-4, completados)
-- ============================================
INSERT INTO registro_llegada (id_viaje, hora_llegada_real, kilometraje_llegada, gasto_combustible, depreciacion_calculada) VALUES
(1, '2026-09-10 09:35:00', 85210.00, 450.00, 178.50),
(2, '2026-09-12 10:28:00', 45215.00, 480.00, 182.75),
(3, '2026-09-14 11:55:00', 30090.00, 200.00, 76.50),
(4, '2026-09-16 07:28:00', 40090.00, 180.00, 76.50);

UPDATE bus SET kilometraje = 85210.00 WHERE id_bus = 1;
UPDATE bus SET kilometraje = 45215.00 WHERE id_bus = 3;
UPDATE bus SET kilometraje = 30090.00 WHERE id_bus = 5;
UPDATE bus SET kilometraje = 40090.00 WHERE id_bus = 7;

-- ============================================
-- BOLETOS
-- ============================================
INSERT INTO boleto (id_viaje_regular, id_cliente, numero_asiento, fecha_pago, precio) VALUES
(1, 14, 5, '2026-09-09 18:00:00', 75.00),
(1, 15, 6, '2026-09-09 19:30:00', 75.00),
(2, 16, 3, '2026-09-11 10:00:00', 80.00),
(3, 14, 10, '2026-09-13 20:00:00', 40.00),
(3, 17, 11, '2026-09-13 21:15:00', 40.00),
(5, 15, 2, '2026-09-15 14:00:00', 75.00),
(6, 16, 8, '2026-09-17 09:00:00', 90.00);

UPDATE cartera SET saldo = saldo - 150.00 WHERE id_usuario = 14;
UPDATE cartera SET saldo = saldo - 150.00 WHERE id_usuario = 15;
UPDATE cartera SET saldo = saldo - 120.00 WHERE id_usuario = 16;
UPDATE cartera SET saldo = saldo - 40.00 WHERE id_usuario = 17;

-- ============================================
-- MOVIMIENTOS DE CARTERA
-- ============================================
INSERT INTO movimiento_cartera (id_cartera, tipo, monto, fecha) VALUES
(1, 'RECARGA', 1500.00, '2026-09-01 10:00:00'),
(2, 'RECARGA', 800.00, '2026-09-01 11:00:00'),
(3, 'RECARGA', 2000.00, '2026-09-01 12:00:00'),
(4, 'RECARGA', 500.00, '2026-09-01 13:00:00');

-- ============================================
-- GASTOS DE TALLER
-- ============================================
INSERT INTO gasto (id_bus, monto_mano_obra, monto_repuestos, fecha) VALUES
(1, 350.00, 800.00, '2026-08-15'),
(3, 200.00, 450.00, '2026-08-20'),
(5, 150.00, 300.00, '2026-08-25'),
(2, 400.00, 950.00, '2026-09-01'),
(7, 180.00, 400.00, '2026-09-05');

-- ============================================
-- ALQUILERES PRIVADOS (ids_viaje 9-11)
-- Uno completado, uno confirmado sin completar, uno pendiente
-- ============================================
INSERT INTO viaje (id_bus, id_chofer, fecha_hora_salida, fecha_hora_llegada_estimada, tipo) VALUES
(2, 7, '2026-09-13 08:00:00', '2026-09-13 20:00:00', 'PRIVADO'),
(4, 9, '2026-09-19 09:00:00', '2026-09-19 17:00:00', 'PRIVADO'),
(NULL, NULL, '2026-09-25 10:00:00', '2026-09-25 15:00:00', 'PRIVADO');

INSERT INTO viaje_privado (id_viaje, origen, destino, pasajeros, precio_estimado, precio_confirmado, id_solicitante) VALUES
(9, 'Terminal Quetzaltenango', 'Finca San José, Retalhuleu', 15, 850.00, 800.00, 14),
(10, 'Terminal Ciudad de Guatemala', 'Antigua Guatemala', 20, 900.00, 950.00, 15),
(11, 'Terminal Cobán', 'Semuc Champey', 12, 700.00, 0.00, 16);

-- Registro de salida/llegada solo para el alquiler ya completado (id_viaje 9)
INSERT INTO registro_salida (id_viaje, hora_salida_real, kilometraje_salida) VALUES
(9, '2026-09-13 08:05:00', 62200.00);

INSERT INTO registro_llegada (id_viaje, hora_llegada_real, kilometraje_llegada, gasto_combustible, depreciacion_calculada) VALUES
(9, '2026-09-13 19:50:00', 62480.00, 600.00, 238.00);

UPDATE bus SET kilometraje = 62480.00 WHERE id_bus = 2;
