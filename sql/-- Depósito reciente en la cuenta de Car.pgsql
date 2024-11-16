-- Depósito reciente en la cuenta de Carlos
INSERT INTO deposito (id_cuenta, monto_deposito, fecha_deposito)
VALUES (1, 500.00, '2023-11-10 10:00:00');

-- Depósito antiguo en la cuenta de María (inactiva)
INSERT INTO deposito (id_cuenta, monto_deposito, fecha_deposito)
VALUES (2, 100.00, '2023-09-15 12:00:00');

-- Depósito en ambas cuentas de Juan
INSERT INTO deposito (id_cuenta, monto_deposito, fecha_deposito)
VALUES (3, 1000.00, '2023-10-15 14:00:00');
INSERT INTO deposito (id_cuenta, monto_deposito, fecha_deposito)
VALUES (4, 500.00, '2023-10-20 09:00:00');
