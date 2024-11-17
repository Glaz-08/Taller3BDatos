CREATE OR REPLACE FUNCTION verificar_saldo()
RETURNS TRIGGER AS $$
BEGIN
    -- Validar que el saldo no sea negativo
    IF NEW.saldo < 0 THEN
        RAISE EXCEPTION 'Saldo insuficiente: El saldo no puede ser negativo.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER saldo_no_negativo
BEFORE UPDATE ON cuenta
FOR EACH ROW
EXECUTE FUNCTION verificar_saldo();

