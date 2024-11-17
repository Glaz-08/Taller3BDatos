-- Cuenta para Carlos (actividad reciente)
INSERT INTO cuenta (id_cliente, saldo, fecha_creacion)
VALUES (1, 1000.00, '2023-11-01'); -- Carlos Pérez

-- Cuenta para María (inactiva)
INSERT INTO cuenta (id_cliente, saldo, fecha_creacion)
VALUES (2, 500.00, '2023-09-01'); -- María López

-- Dos cuentas para Juan (múltiples cuentas)
INSERT INTO cuenta (id_cliente, saldo, fecha_creacion)
VALUES (3, 3000.00, '2023-10-01'); -- Juan García
INSERT INTO cuenta (id_cliente, saldo, fecha_creacion)
VALUES (3, 1500.00, '2023-10-05'); -- Juan García
