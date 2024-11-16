package sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dataSQL {

    // Verificar usuario y obtener su rol
    public String verificarUsuario(String email, String contraseña) {
        // Caso especial para el administrador
        if (email.equals("skibidi") && contraseña.equals("toilet")) {
            return "administrador";
        }
    
        // Consulta normal para clientes
        String query = "SELECT 'cliente' AS rol FROM cliente WHERE email = ? AND contraseña = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            statement.setString(1, email);
            statement.setString(2, contraseña);
    
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("rol"); // Devuelve el rol, aquí siempre será "cliente"
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar usuario: " + e.getMessage());
        }
        return null;
    }
    

    // Registrar un usuario
    public boolean registrarUsuario(String nombre, String contraseña, String rol) {
        String query = "INSERT INTO cliente (nombre, direccion, telefono, email, contraseña) VALUES (?, ?, NULL, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            statement.setString(1, nombre);
            statement.setString(2, "Usuario por defecto"); // O un valor real para la dirección
            statement.setString(3,nombre); 
            statement.setString(4, contraseña);
    
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
        }
        return false;
    }
    

    // Realizar un depósito
    public void realizarDeposito(int idCuenta, double monto) {
        String query = "UPDATE cuenta SET saldo = saldo + ? WHERE id_cuenta = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDouble(1, monto);
            statement.setInt(2, idCuenta);

            statement.executeUpdate();
            System.out.println("Depósito realizado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al realizar depósito: " + e.getMessage());
        }
    }

    // Realizar un retiro
    public void realizarRetiro(int idCuenta, double monto) {
        String query = "UPDATE cuenta SET saldo = saldo - ? WHERE id_cuenta = ? AND saldo >= ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDouble(1, monto);
            statement.setInt(2, idCuenta);
            statement.setDouble(3, monto);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Retiro realizado exitosamente.");
            } else {
                System.out.println("Saldo insuficiente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al realizar retiro: " + e.getMessage());
        }
    }

    // Realizar una transferencia
    public void realizarTransferencia(int idCuentaOrigen, int idCuentaDestino, double monto) {
        String verificarSaldo = "SELECT saldo FROM cuenta WHERE id_cuenta = ?";
        String actualizarOrigen = "UPDATE cuenta SET saldo = saldo - ? WHERE id_cuenta = ?";
        String actualizarDestino = "UPDATE cuenta SET saldo = saldo + ? WHERE id_cuenta = ?";
        String registrarTransferencia = "INSERT INTO transferencia (id_cuenta_origen, id_cuenta_destino, monto_transferencia, fecha_transferencia) VALUES (?, ?, ?, NOW())";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            // Verificar saldo
            try (PreparedStatement verificarStmt = connection.prepareStatement(verificarSaldo)) {
                verificarStmt.setInt(1, idCuentaOrigen);
                ResultSet resultSet = verificarStmt.executeQuery();
                if (resultSet.next()) {
                    double saldo = resultSet.getDouble("saldo");
                    if (saldo < monto) {
                        System.out.println("Saldo insuficiente.");
                        return;
                    }
                } else {
                    System.out.println("Cuenta origen no encontrada.");
                    return;
                }
            }

            // Actualizar saldo cuenta origen
            try (PreparedStatement origenStmt = connection.prepareStatement(actualizarOrigen)) {
                origenStmt.setDouble(1, monto);
                origenStmt.setInt(2, idCuentaOrigen);
                origenStmt.executeUpdate();
            }

            // Actualizar saldo cuenta destino
            try (PreparedStatement destinoStmt = connection.prepareStatement(actualizarDestino)) {
                destinoStmt.setDouble(1, monto);
                destinoStmt.setInt(2, idCuentaDestino);
                destinoStmt.executeUpdate();
            }

            // Registrar transferencia
            try (PreparedStatement registrarStmt = connection.prepareStatement(registrarTransferencia)) {
                registrarStmt.setInt(1, idCuentaOrigen);
                registrarStmt.setInt(2, idCuentaDestino);
                registrarStmt.setDouble(3, monto);
                registrarStmt.executeUpdate();
            }

            connection.commit();
            System.out.println("Transferencia realizada exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al realizar transferencia: " + e.getMessage());
        }
    }

    // Consultar saldo
    public void consultarSaldo(int idCuenta) {
        String query = "SELECT saldo FROM cuenta WHERE id_cuenta = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, idCuenta);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                double saldo = resultSet.getDouble("saldo");
                System.out.println("Saldo actual: " + saldo);
            } else {
                System.out.println("Cuenta no encontrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar saldo: " + e.getMessage());
        }
    }

    // Consultar historial de transacciones
public void consultarHistorialTransacciones() {
    String query = "SELECT 'Retiro' AS tipo, id_retiro AS id_transaccion, monto_retiro AS monto, fecha_retiro AS fecha " +
                   "FROM retiro " +
                   "UNION ALL " +
                   "SELECT 'Depósito' AS tipo, id_deposito AS id_transaccion, monto_deposito AS monto, fecha_deposito AS fecha " +
                   "FROM deposito " +
                   "UNION ALL " +
                   "SELECT 'Transferencia' AS tipo, id_transferencia AS id_transaccion, monto_transferencia AS monto, fecha_transferencia AS fecha " +
                   "FROM transferencia " +
                   "ORDER BY fecha";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

        System.out.printf("%-15s %-15s %-15s %-25s\n", "Tipo", "ID Transacción", "Monto", "Fecha");
        System.out.println("-------------------------------------------------------------");

        while (resultSet.next()) {
            String tipo = resultSet.getString("tipo");
            int idTransaccion = resultSet.getInt("id_transaccion");
            double monto = resultSet.getDouble("monto");
            String fecha = resultSet.getString("fecha");

            System.out.printf("%-15s %-15d %-15.2f %-25s\n", tipo, idTransaccion, monto, fecha);
        }
    } catch (SQLException e) {
        System.err.println("Error al consultar historial de transacciones: " + e.getMessage());
    }
}

// Generar reportes financieros
public void generarReportesFinancieros() {
    String saldoPromedioQuery = "SELECT AVG(saldo) AS saldo_promedio FROM cuenta";
    String cuentasActivasQuery = "SELECT id_cuenta, COUNT(*) AS transacciones " +
                                 "FROM (" +
                                 "    SELECT id_cuenta FROM retiro " +
                                 "    UNION ALL " +
                                 "    SELECT id_cuenta FROM deposito " +
                                 "    UNION ALL " +
                                 "    SELECT id_cuenta_origen AS id_cuenta FROM transferencia " +
                                 "    UNION ALL " +
                                 "    SELECT id_cuenta_destino AS id_cuenta FROM transferencia " +
                                 ") AS transacciones_totales " +
                                 "GROUP BY id_cuenta " +
                                 "ORDER BY transacciones DESC " +
                                 "LIMIT 1";
    String ingresosNetosQuery = "SELECT " +
                                 "    (SELECT SUM(monto_deposito) FROM deposito) - (SELECT SUM(monto_retiro) FROM retiro) AS ingresos_netos";

    try (Connection connection = DatabaseConnection.getConnection()) {
        // Saldo promedio
        try (PreparedStatement statement = connection.prepareStatement(saldoPromedioQuery);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                System.out.println("Saldo promedio de las cuentas: " + resultSet.getDouble("saldo_promedio"));
            }
        }

        // Cuenta con más transacciones
        try (PreparedStatement statement = connection.prepareStatement(cuentasActivasQuery);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                System.out.println("Cuenta con más transacciones: ID " + resultSet.getInt("id_cuenta") +
                                   " (Total: " + resultSet.getInt("transacciones") + ")");
            }
        }

        // Ingresos netos
        try (PreparedStatement statement = connection.prepareStatement(ingresosNetosQuery);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                System.out.println("Ingresos netos de la institución: " + resultSet.getDouble("ingresos_netos"));
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al generar reportes financieros: " + e.getMessage());
    }
}

// Ver cuentas inactivas
public void verCuentasInactivas() {
    String query = "SELECT id_cuenta, saldo, fecha_creacion " +
                   "FROM cuenta " +
                   "WHERE id_cuenta NOT IN (" +
                   "    SELECT DISTINCT id_cuenta FROM retiro WHERE fecha_retiro >= NOW() - INTERVAL '30 days' " +
                   "    UNION " +
                   "    SELECT DISTINCT id_cuenta FROM deposito WHERE fecha_deposito >= NOW() - INTERVAL '30 days' " +
                   "    UNION " +
                   "    SELECT DISTINCT id_cuenta_origen AS id_cuenta FROM transferencia WHERE fecha_transferencia >= NOW() - INTERVAL '30 days' " +
                   "    UNION " +
                   "    SELECT DISTINCT id_cuenta_destino AS id_cuenta FROM transferencia WHERE fecha_transferencia >= NOW() - INTERVAL '30 days' " +
                   ")";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

        System.out.println("Cuentas inactivas en los últimos 30 días:");
        System.out.printf("%-10s %-15s %-25s\n", "ID Cuenta", "Saldo", "Fecha Creación");
        System.out.println("-----------------------------------------------------");

        while (resultSet.next()) {
            int idCuenta = resultSet.getInt("id_cuenta");
            double saldo = resultSet.getDouble("saldo");
            String fechaCreacion = resultSet.getString("fecha_creacion");

            System.out.printf("%-10d %-15.2f %-25s\n", idCuenta, saldo, fechaCreacion);
        }
    } catch (SQLException e) {
        System.err.println("Error al consultar cuentas inactivas: " + e.getMessage());
    }
}

public void modificarUsuario(int idUsuario, String nuevoNombre) {
    String query = "UPDATE cliente SET nombre = ? WHERE id_cliente = ?";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setString(1, nuevoNombre);
        statement.setInt(2, idUsuario);

        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Usuario modificado exitosamente.");
        } else {
            System.out.println("Usuario no encontrado.");
        }
    } catch (SQLException e) {
        System.err.println("Error al modificar usuario: " + e.getMessage());
    }
}

public void eliminarUsuario(int idUsuario) {
    String query = "DELETE FROM cliente WHERE id_cliente = ?";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setInt(1, idUsuario);

        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Usuario eliminado exitosamente.");
        } else {
            System.out.println("Usuario no encontrado.");
        }
    } catch (SQLException e) {
        System.err.println("Error al eliminar usuario: " + e.getMessage());
    }
}

}

