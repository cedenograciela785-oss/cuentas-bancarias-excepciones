package Entidad;

import Excepciones.DatoInvalidoException;
import Excepciones.MontoInvalidoException;
import Excepciones.SaldoInsuficienteException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase abstracta. Reúne lo común de toda cuenta y obliga a cada tipo
 * a definir cómo retira dinero y cómo calcula su porcentaje (polimorfismo).
 */
public abstract class Cuenta {
    private final int numero;
    private BigDecimal saldo;
    private final Titular titular;
    private final List<String> historial = new ArrayList<>();

    protected Cuenta(int numero, BigDecimal saldoInicial, Titular titular) {
        if (numero <= 0)
            throw new DatoInvalidoException("El número de cuenta debe ser mayor que cero.");
        if (titular == null)
            throw new DatoInvalidoException("La cuenta necesita un titular.");
        if (saldoInicial == null || saldoInicial.signum() < 0)
            throw new DatoInvalidoException("El saldo inicial no puede ser negativo.");
        this.numero = numero;
        this.titular = titular;
        this.saldo = redondear(saldoInicial);
        registrar("APERTURA", this.saldo);
    }

    // ---- Métodos que cada tipo de cuenta debe implementar ----

    /** Interés (ahorros) o costo de operaciones (corriente). */
    public abstract void calcularPorcentaje();

    /** Cada cuenta decide hasta dónde puede bajar el saldo. */
    public abstract void retirar(BigDecimal monto) throws SaldoInsuficienteException;

    public abstract String getTipo();

    // ---- Operaciones comunes ----

    public void depositar(BigDecimal monto) {
        validarMonto(monto);
        acreditar(monto, "DEPÓSITO");
    }

    public void transferir(Cuenta destino, BigDecimal monto) throws SaldoInsuficienteException {
        if (destino == null)
            throw new DatoInvalidoException("La cuenta destino no existe.");
        if (destino == this)
            throw new DatoInvalidoException("No se puede transferir a la misma cuenta.");
        validarMonto(monto);
        retirar(monto);                // si falla, el destino no recibe nada
        destino.depositar(monto);
    }

    // ---- Apoyo para las subclases ----

    protected void acreditar(BigDecimal valor, String tipo) {
        saldo = saldo.add(redondear(valor));
        registrar(tipo, valor);
    }

    protected void debitar(BigDecimal valor, String tipo) {
        saldo = saldo.subtract(redondear(valor));
        registrar(tipo, valor);
    }

    protected static void validarMonto(BigDecimal monto) {
        if (monto == null || monto.signum() <= 0)
            throw new MontoInvalidoException("El monto debe ser mayor que cero.");
    }

    protected static BigDecimal redondear(BigDecimal v) {
        return v.setScale(2, RoundingMode.HALF_UP);
    }

    private void registrar(String tipo, BigDecimal monto) {
        historial.add(String.format("%-20s $%10s | saldo: $%s", tipo, redondear(monto), saldo));
    }

    // ---- Getters ----

    public int getNumero() { return numero; }
    public BigDecimal getSaldo() { return saldo; }
    public Titular getTitular() { return titular; }
    public List<String> getHistorial() { return Collections.unmodifiableList(historial); }

    @Override
    public String toString() {
        return String.format("[%s] N° %d | %s | Saldo: $%s", getTipo(), numero, titular, saldo);
    }
}
