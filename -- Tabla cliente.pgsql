-- Tabla cliente
CREATE TABLE cliente (
    id_cliente SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    direccion VARCHAR(255),
    telefono VARCHAR(15),
    email VARCHAR(255) UNIQUE,
    contraseña VARCHAR(255) NOT NULL
);

-- Tabla cuenta
CREATE TABLE cuenta (
    id_cuenta SERIAL PRIMARY KEY,
    id_cliente INTEGER NOT NULL REFERENCES cliente(id_cliente),
    saldo NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
    fecha_creacion DATE NOT NULL
);

-- Tabla retiro
CREATE TABLE retiro (
    id_retiro SERIAL PRIMARY KEY,
    id_cuenta INTEGER NOT NULL REFERENCES cuenta(id_cuenta),
    monto_retiro NUMERIC(15, 2) NOT NULL CHECK (monto_retiro > 0),
    fecha_retiro TIMESTAMP NOT NULL
);

-- Tabla deposito
CREATE TABLE deposito (
    id_deposito SERIAL PRIMARY KEY,
    id_cuenta INTEGER NOT NULL REFERENCES cuenta(id_cuenta),
    monto_deposito NUMERIC(15, 2) NOT NULL CHECK (monto_deposito > 0),
    fecha_deposito TIMESTAMP NOT NULL
);

-- Tabla transferencia
CREATE TABLE transferencia (
    id_transferencia SERIAL PRIMARY KEY,
    id_cuenta_origen INTEGER NOT NULL REFERENCES cuenta(id_cuenta),
    id_cuenta_destino INTEGER NOT NULL REFERENCES cuenta(id_cuenta),
    monto_transferencia NUMERIC(15, 2) NOT NULL CHECK (monto_transferencia > 0),
    fecha_transferencia TIMESTAMP NOT NULL,
    CHECK (id_cuenta_origen <> id_cuenta_destino)
);

-- Índices para mejorar el rendimiento
CREATE INDEX idx_retiro_id_cuenta ON retiro (id_cuenta);
CREATE INDEX idx_deposito_id_cuenta ON deposito (id_cuenta);
CREATE INDEX idx_transferencia_id_cuenta_origen ON transferencia (id_cuenta_origen);
CREATE INDEX idx_transferencia_id_cuenta_destino ON transferencia (id_cuenta_destino);
