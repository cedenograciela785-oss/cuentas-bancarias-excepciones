package app;

import Entidad.*;
import Excepciones.*;
import java.math.BigDecimal;

/** Pruebas automáticas sin librerías externas. Ejecutar: java -cp out app.Pruebas */
public class Pruebas {
    interface Accion { void ejecutar() throws Exception; }
    static int ok = 0, fallos = 0;

    public static void main(String[] args) throws Exception {
        Titular t1 = new Titular("1700000001", "Ana Torres");
        Titular t2 = new Titular("1700000002", "Luis Mora");
        Ahorros ah = new Ahorros(1, bd("500"), t1, bd("5"), bd("50"));
        Corriente co = new Corriente(2, bd("100"), t2, bd("2"), bd("200"));

        // Titular
        lanza("Cédula con letras", DatoInvalidoException.class, () -> new Titular("12345abcde", "Ana"));
        lanza("Cédula de 9 dígitos", DatoInvalidoException.class, () -> new Titular("123456789", "Ana"));
        lanza("Nombre vacío", DatoInvalidoException.class, () -> new Titular("1700000003", " "));

        // Depósitos
        co.depositar(bd("50"));
        igual("Depósito corriente", "150.00", co.getSaldo());
        lanza("Depósito negativo", MontoInvalidoException.class, () -> co.depositar(bd("-5")));
        lanza("Depósito en cero", MontoInvalidoException.class, () -> co.depositar(bd("0")));

        // Ahorros
        ah.retirar(bd("100"));
        igual("Retiro ahorros", "400.00", ah.getSaldo());
        lanza("Retiro bajo el mínimo", SaldoInsuficienteException.class, () -> ah.retirar(bd("360")));
        igual("Saldo intacto tras rechazo", "400.00", ah.getSaldo());
        ah.calcularPorcentaje();
        igual("Interés 5 %", "420.00", ah.getSaldo());

        // Corriente
        co.retirar(bd("300"));
        igual("Retiro con sobregiro", "-150.00", co.getSaldo());
        lanza("Excede sobregiro", SaldoInsuficienteException.class, () -> co.retirar(bd("100")));
        co.calcularPorcentaje();
        igual("Sin costo con saldo negativo", "-150.00", co.getSaldo());
        co.depositar(bd("1150"));      // saldo 1000
        co.calcularPorcentaje();
        igual("Costo 2 % sobre 1000", "980.00", co.getSaldo());

        // Transferencias
        Corriente x = new Corriente(3, bd("300"), t1, bd("1"), bd("0"));
        Ahorros y = new Ahorros(4, bd("100"), t2, bd("3"), bd("0.01"));
        x.transferir(y, bd("120"));
        igual("Transferencia origen", "180.00", x.getSaldo());
        igual("Transferencia destino", "220.00", y.getSaldo());
        lanza("Transferencia sin fondos", SaldoInsuficienteException.class, () -> x.transferir(y, bd("500")));
        igual("Destino sin cambios", "220.00", y.getSaldo());
        lanza("Transferir a sí misma", DatoInvalidoException.class, () -> x.transferir(x, bd("1")));

        // Creación inválida
        lanza("Ahorros bajo el mínimo", DatoInvalidoException.class, () -> new Ahorros(5, bd("10"), t1, bd("5"), bd("50")));
        lanza("Saldo inicial negativo", DatoInvalidoException.class, () -> new Corriente(6, bd("-1"), t1, bd("1"), bd("0")));
        lanza("Interés en cero", DatoInvalidoException.class, () -> new Ahorros(7, bd("100"), t1, bd("0"), bd("0")));

        // Banco
        Banco b = new Banco();
        b.agregar(ah);
        lanza("Número de cuenta repetido", DatoInvalidoException.class, () -> b.agregar(ah));
        lanza("Cuenta inexistente", CuentaNoEncontradaException.class, () -> b.buscar(99));

        // Polimorfismo
        Cuenta[] todas = { ah, co };
        BigDecimal antesAh = ah.getSaldo(), antesCo = co.getSaldo();
        for (Cuenta c : todas) c.calcularPorcentaje();
        verdad("Polimorfismo: ahorros sube", ah.getSaldo().compareTo(antesAh) > 0);
        verdad("Polimorfismo: corriente baja", co.getSaldo().compareTo(antesCo) < 0);

        System.out.println("\nResultado: " + ok + " pruebas correctas, " + fallos + " fallidas.");
        if (fallos > 0) System.exit(1);
    }

    static BigDecimal bd(String s) { return new BigDecimal(s); }

    static void igual(String n, String esperado, BigDecimal real) {
        if (real.compareTo(new BigDecimal(esperado)) == 0) { ok++; System.out.println("OK    " + n); }
        else { fallos++; System.out.println("FALLO " + n + " (esperado " + esperado + ", real " + real + ")"); }
    }

    static void verdad(String n, boolean cond) {
        if (cond) { ok++; System.out.println("OK    " + n); } else { fallos++; System.out.println("FALLO " + n); }
    }

    static void lanza(String n, Class<? extends Exception> tipo, Accion a) {
        try { a.ejecutar(); fallos++; System.out.println("FALLO " + n + " (no lanzó excepción)"); }
        catch (Exception e) {
            if (tipo.isInstance(e)) { ok++; System.out.println("OK    " + n); }
            else { fallos++; System.out.println("FALLO " + n + " (lanzó " + e.getClass().getSimpleName() + ")"); }
        }
    }
}
