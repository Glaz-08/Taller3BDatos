-- Transferencia entre las cuentas de Juan
INSERT INTO transferencia (id_cuenta_origen, id_cuenta_destino, monto_transferencia, fecha_transferencia)
VALUES (3, 4, 300.00, '2023-10-25 18:00:00');

-- Transferencia desde la cuenta de Carlos a la cuenta de Juan
INSERT INTO transferencia (id_cuenta_origen, id_cuenta_destino, monto_transferencia, fecha_transferencia)
VALUES (1, 3, 200.00, '2023-11-10 13:00:00');
