-- ============================================
-- Base de datos: Gestion de Buses (IPC2 - Proyecto 1)
-- ============================================

CREATE DATABASE IF NOT EXISTS gestion_buses;
USE gestion_buses;

-- ============================================
-- SUCURSAL
-- ============================================
CREATE TABLE sucursal (
    id_sucursal INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(200) NOT NULL,
    latitud DOUBLE,
    longitud DOUBLE
);

-- ============================================
-- USUARIO (tabla base)
-- ============================================
CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    nit VARCHAR(20),
    dpi VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(15),
    direccion VARCHAR(200),
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    tipo ENUM('ADMIN_SISTEMA', 'ADMIN_SUCURSAL', 'CHOFER', 'CLIENTE') NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- ============================================
-- ADMIN_SISTEMA (hereda de usuario)
-- ============================================
CREATE TABLE admin_sistema (
    id_usuario INT PRIMARY KEY,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- ============================================
-- ADMIN_SUCURSAL (hereda de usuario)
-- ============================================
CREATE TABLE admin_sucursal (
    id_usuario INT PRIMARY KEY,
    id_sucursal INT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal)
);

-- ============================================
-- CHOFER (hereda de usuario)
-- ============================================
CREATE TABLE chofer (
    id_usuario INT PRIMARY KEY,
    numero_licencia VARCHAR(30) NOT NULL,
    tipo_licencia ENUM('A', 'B', 'C', 'M') NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    salario_base DECIMAL(10,2) NOT NULL,
    id_sucursal INT NOT NULL,
    foto VARCHAR(255),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal)
);

-- ============================================
-- CLIENTE_REGULAR (hereda de usuario)
-- ============================================
CREATE TABLE cliente_regular (
    id_usuario INT PRIMARY KEY,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- ============================================
-- BUS
-- ============================================
CREATE TABLE bus (
    id_bus INT AUTO_INCREMENT PRIMARY KEY,
    id_sucursal INT NOT NULL,
    placa VARCHAR(15) NOT NULL UNIQUE,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    anio INT NOT NULL,
    capacidad INT NOT NULL,
    estado_operativo ENUM('DISPONIBLE', 'EN_VIAJE', 'EN_MANTENIMIENTO') NOT NULL DEFAULT 'DISPONIBLE',
    kilometraje DECIMAL(10,2) NOT NULL DEFAULT 0,
    foto VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal)
);

-- ============================================
-- RUTA
-- ============================================
CREATE TABLE ruta (
    id_ruta INT AUTO_INCREMENT PRIMARY KEY,
    id_sucursal_origen INT NOT NULL,
    id_sucursal_destino INT NOT NULL,
    distancia_km DECIMAL(10,2) NOT NULL,
    precio_boleto DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_sucursal_origen) REFERENCES sucursal(id_sucursal),
    FOREIGN KEY (id_sucursal_destino) REFERENCES sucursal(id_sucursal)
);

-- ============================================
-- VIAJE (tabla base - estrategia tabla por subclase)
-- ============================================
CREATE TABLE viaje (
    id_viaje INT AUTO_INCREMENT PRIMARY KEY,
    id_bus INT NULL,
    id_chofer INT NULL,
    fecha_hora_salida DATETIME NOT NULL,
    fecha_hora_llegada_estimada DATETIME NOT NULL,
    tipo ENUM('REGULAR', 'PRIVADO') NOT NULL,
    FOREIGN KEY (id_bus) REFERENCES bus(id_bus),
    FOREIGN KEY (id_chofer) REFERENCES chofer(id_usuario)
);

-- ============================================
-- VIAJE_REGULAR (hereda de viaje)
-- ============================================
CREATE TABLE viaje_regular (
    id_viaje INT PRIMARY KEY,
    id_ruta INT NOT NULL,
    FOREIGN KEY (id_viaje) REFERENCES viaje(id_viaje),
    FOREIGN KEY (id_ruta) REFERENCES ruta(id_ruta)
);

-- ============================================
-- VIAJE_PRIVADO (hereda de viaje)
-- ============================================
CREATE TABLE viaje_privado (
    id_viaje INT PRIMARY KEY,
    origen VARCHAR(150) NOT NULL,
    destino VARCHAR(150) NOT NULL,
    pasajeros INT NOT NULL,
    precio_estimado DECIMAL(10,2) NOT NULL,
    precio_confirmado DECIMAL(10,2),
    id_solicitante INT,
    FOREIGN KEY (id_viaje) REFERENCES viaje(id_viaje),
    FOREIGN KEY (id_solicitante) REFERENCES usuario(id_usuario)
);

-- ============================================
-- BOLETO
-- ============================================
CREATE TABLE boleto (
    id_boleto INT AUTO_INCREMENT PRIMARY KEY,
    id_viaje_regular INT NOT NULL,
    id_cliente INT NOT NULL,
    numero_asiento INT NOT NULL,
    fecha_pago DATETIME NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_viaje_regular) REFERENCES viaje_regular(id_viaje),
    FOREIGN KEY (id_cliente) REFERENCES cliente_regular(id_usuario)
);

-- ============================================
-- CARTERA
-- ============================================
CREATE TABLE cartera (
    id_cartera INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL UNIQUE,
    saldo DECIMAL(10,2) NOT NULL DEFAULT 0,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- ============================================
-- MOVIMIENTO_CARTERA
-- ============================================
CREATE TABLE movimiento_cartera (
    id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_cartera INT NOT NULL,
    tipo ENUM('RECARGA', 'PAGO') NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    fecha DATETIME NOT NULL,
    FOREIGN KEY (id_cartera) REFERENCES cartera(id_cartera)
);

-- ============================================
-- REGISTRO_SALIDA (inmutable)
-- ============================================
CREATE TABLE registro_salida (
    id_registro_salida INT AUTO_INCREMENT PRIMARY KEY,
    id_viaje INT NOT NULL UNIQUE,
    hora_salida_real DATETIME NOT NULL,
    kilometraje_salida DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_viaje) REFERENCES viaje(id_viaje)
);

-- ============================================
-- REGISTRO_LLEGADA (inmutable)
-- ============================================
CREATE TABLE registro_llegada (
    id_registro_llegada INT AUTO_INCREMENT PRIMARY KEY,
    id_viaje INT NOT NULL UNIQUE,
    hora_llegada_real DATETIME NOT NULL,
    kilometraje_llegada DECIMAL(10,2) NOT NULL,
    gasto_combustible DECIMAL(10,2) NOT NULL,
    depreciacion_calculada DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_viaje) REFERENCES viaje(id_viaje)
);

-- ============================================
-- GASTO (taller y repuestos)
-- ============================================
CREATE TABLE gasto (
    id_gasto INT AUTO_INCREMENT PRIMARY KEY,
    id_bus INT NOT NULL,
    monto_mano_obra DECIMAL(10,2) NOT NULL,
    monto_repuestos DECIMAL(10,2) NOT NULL,
    fecha DATE NOT NULL,
    FOREIGN KEY (id_bus) REFERENCES bus(id_bus)
);

-- ============================================
-- CONFIGURACION (para el monto de depreciacion por km)
-- ============================================
CREATE TABLE configuracion (
    id_configuracion INT AUTO_INCREMENT PRIMARY KEY,
    monto_depreciacion_km DECIMAL(10,4) NOT NULL,
    fecha_actualizacion DATETIME NOT NULL
);
