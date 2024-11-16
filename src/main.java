package src;
import sql.dataSQL;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        dataSQL data = new dataSQL(); // Instancia para acceder a las funciones SQL

        System.out.println("Bienvenido al sistema bancario.");
        System.out.println("1. Iniciar sesión");
        System.out.println("2. Registrarse");
        System.out.print("Seleccione una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        if (opcion == 1) {
            iniciarSesion(scanner, data);
        } else if (opcion == 2) {
            registrarse(scanner, data);
        } else {
            System.out.println("Opción no válida.");
        }

        scanner.close();
    }

    private static void iniciarSesion(Scanner scanner, dataSQL data) {
        System.out.print("Ingrese su correo: ");
        String email = scanner.nextLine();
    
        System.out.print("Ingrese su contraseña: ");
        String contraseña = scanner.nextLine();
    
        String rol = data.verificarUsuario(email, contraseña);
    
        if (rol != null) {
            System.out.println("Inicio de sesión exitoso. Rol: " + rol);
    
            if (rol.equalsIgnoreCase("cliente")) {
                mostrarMenuCliente(scanner, data);
            } else if (rol.equalsIgnoreCase("administrador")) {
                mostrarMenuAdministrador(scanner, data);
            } else {
                System.out.println("Rol no reconocido.");
            }
        } else {
            System.out.println("Correo o contraseña incorrectos.");
        }
    }
    

    private static void registrarse(Scanner scanner, dataSQL data) {
        System.out.print("Ingrese su nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese su contraseña: ");
        String contraseña = scanner.nextLine();

        System.out.print("Ingrese su rol (cliente/administrador): ");
        String rol = scanner.nextLine();

        boolean registrado = data.registrarUsuario(nombre, contraseña, rol); // Método para registrar usuarios

        if (registrado) {
            System.out.println("Usuario registrado exitosamente.");
        } else {
            System.out.println("Error al registrar al usuario.");
        }
    }

    private static void mostrarMenuCliente(Scanner scanner, dataSQL data) {
        boolean salir = false;
        while (!salir) {
            System.out.println("Menú Cliente:");
            System.out.println("1. Realizar Depósito");
            System.out.println("2. Realizar Retiro");
            System.out.println("3. Transferencia");
            System.out.println("4. Consultar Saldo");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
    
            switch (opcion) {
                case 1:
                    System.out.println("Ingrese la cantidad a depositar: ");
                    double deposito = scanner.nextDouble();
                    System.out.println("Ingrese el ID de su cuenta: ");
                    int idCuentaDep = scanner.nextInt();
                    data.realizarDeposito(idCuentaDep, deposito);
                    break;
                case 2:
                    System.out.println("Ingrese la cantidad a retirar: ");
                    double retiro = scanner.nextDouble();
                    System.out.println("Ingrese el ID de su cuenta: ");
                    int idCuentaRet = scanner.nextInt();
                    data.realizarRetiro(idCuentaRet, retiro);
                    break;
                case 3:
                    System.out.println("Ingrese el ID de la cuenta origen: ");
                    int idCuentaOrigen = scanner.nextInt();
                    System.out.println("Ingrese el ID de la cuenta destino: ");
                    int idCuentaDestino = scanner.nextInt();
                    System.out.println("Ingrese el monto a transferir: ");
                    double montoTransferencia = scanner.nextDouble();
                    data.realizarTransferencia(idCuentaOrigen, idCuentaDestino, montoTransferencia);
                    break;
                case 4:
                    System.out.println("Ingrese el ID de su cuenta: ");
                    int idCuentaSaldo = scanner.nextInt();
                    data.consultarSaldo(idCuentaSaldo);
                    break;
                case 5:
                    salir = true; // Termina el bucle
                    System.out.println("Saliendo del Menú Cliente...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }
    
    private static void mostrarMenuAdministrador(Scanner scanner, dataSQL data) {
        boolean salir = false;
        while (!salir) {
            System.out.println("Menú Administrador:");
            System.out.println("1. Consultar Historial de Transacciones");
            System.out.println("2. Generar Reportes Financieros");
            System.out.println("3. Ver Cuentas Inactivas");
            System.out.println("4. Configuración de Usuarios");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
    
            switch (opcion) {
                case 1:
                    data.consultarHistorialTransacciones();
                    break;
                case 2:
                    data.generarReportesFinancieros();
                    break;
                case 3:
                    data.verCuentasInactivas();
                    break;
                case 4:
                    System.out.println("Ingrese el ID del usuario para eliminar o modificar: ");
                    int idUsuario = scanner.nextInt();
                    System.out.println("1. Eliminar usuario");
                    System.out.println("2. Modificar usuario");
                    int subOpcion = scanner.nextInt();
                    if (subOpcion == 1) {
                        data.eliminarUsuario(idUsuario);
                    } else if (subOpcion == 2) {
                        System.out.println("Ingrese el nuevo nombre del usuario: ");
                        scanner.nextLine(); // Limpiar buffer
                        String nuevoNombre = scanner.nextLine();
                        data.modificarUsuario(idUsuario, nuevoNombre);
                    }
                    break;
                case 5:
                    salir = true; // Termina el bucle
                    System.out.println("Saliendo del Menú Administrador...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }
    

    
}

