package app;

import Entidad.Ahorros;
import Entidad.Banco;
import Entidad.Corriente;
import Entidad.Cuenta;
import Entidad.Titular;
import Excepciones.CuentaNoEncontradaException;
import Excepciones.DatoInvalidoException;
import Excepciones.MontoInvalidoException;
import Excepciones.SaldoInsuficienteException;
import java.math.BigDecimal;
import java.util.Scanner;

/** Menú de consola. Un solo Scanner, y todo dato se lee como texto y se convierte con control de errores. */
public class App {
    private static final Scanner sc = new Scanner(System.in);
    private static final Banco banco = new Banco();

    public static void main(String[] args) {
        String opcion;
        do {
            mostrarMenu();
            opcion = leerTexto("Opción: ");
            try {
                ejecutar(opcion);
            } catch (SaldoInsuficienteException | CuentaNoEncontradaException e) {
                System.out.println("⚠ " + e.getMessage());
            } catch (DatoInvalidoException | MontoInvalidoException e) {
                System.out.println("✖ " + e.getMessage());
            } finally {
                if (!opcion.equals("0")) System.out.println("--- Operación finalizada ---");
            }
        } while (!opcion.equals("0"));
        System.out.println("Gracias por usar el sistema.");
    }

    private static void mostrarMenu() {
        System.out.println("\n===== MENÚ BANCO =====");
        System.out.println("1. Crear cuenta de ahorros");
        System.out.println("2. Crear cuenta corriente");
        System.out.println("3. Depositar");
        System.out.println("4. Retirar");
        System.out.println("5. Transferir");
        System.out.println("6. Aplicar interés / costo a todas las cuentas");
        System.out.println("7. Listar cuentas");
        System.out.println("8. Ver historial de una cuenta");
        System.out.println("0. Salir");
    }

    private static void ejecutar(String opcion) throws SaldoInsuficienteException, CuentaNoEncontradaException {
        switch (opcion) {
            case "1" -> {
                int num = leerEntero("N° de cuenta: ");
                Titular t = leerTitular();
                banco.agregar(new Ahorros(num, leerMonto("Saldo inicial: "), t,
                        leerMonto("Interés (%): "), leerMonto("Saldo mínimo: ")));
                System.out.println("Cuenta de ahorros creada.");
            }
            case "2" -> {
                int num = leerEntero("N° de cuenta: ");
                Titular t = leerTitular();
                banco.agregar(new Corriente(num, leerMonto("Saldo inicial: "), t,
                        leerMonto("Costo de operaciones (%): "), leerMonto("Límite de sobregiro: ")));
                System.out.println("Cuenta corriente creada.");
            }
            case "3" -> {
                banco.buscar(leerEntero("N° de cuenta: ")).depositar(leerMonto("Monto: "));
                System.out.println("Depósito realizado.");
            }
            case "4" -> {
                banco.buscar(leerEntero("N° de cuenta: ")).retirar(leerMonto("Monto: "));
                System.out.println("Retiro realizado.");
            }
            case "5" -> {
                Cuenta origen = banco.buscar(leerEntero("Cuenta origen: "));
                Cuenta destino = banco.buscar(leerEntero("Cuenta destino: "));
                origen.transferir(destino, leerMonto("Monto: "));
                System.out.println("Transferencia realizada.");
            }
            case "6" -> {
                for (Cuenta c : banco.getCuentas()) c.calcularPorcentaje();   // polimorfismo
                System.out.println("Porcentajes aplicados.");
            }
            case "7" -> {
                if (banco.getCuentas().isEmpty()) System.out.println("No hay cuentas registradas.");
                banco.getCuentas().forEach(System.out::println);
            }
            case "8" -> banco.buscar(leerEntero("N° de cuenta: ")).getHistorial().forEach(System.out::println);
            case "0" -> { }
            default -> System.out.println("Opción no válida.");
        }
    }

    // ---------- Lectura segura ----------

    private static String leerTexto(String msg) {
        System.out.print(msg);
        return sc.nextLine().trim();
    }

    private static int leerEntero(String msg) {
        try {
            return Integer.parseInt(leerTexto(msg));
        } catch (NumberFormatException e) {
            throw new DatoInvalidoException("Ingrese un número entero válido.");
        }
    }

    private static BigDecimal leerMonto(String msg) {
        try {
            return new BigDecimal(leerTexto(msg).replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new DatoInvalidoException("Ingrese un valor numérico válido.");
        }
    }

    private static Titular leerTitular() {
        return new Titular(leerTexto("Cédula (10 dígitos): "), leerTexto("Nombre del titular: "));
    }
}
