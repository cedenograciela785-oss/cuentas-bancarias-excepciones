package Entidad;

import Excepciones.DatoInvalidoException;
import Excepciones.SaldoInsuficienteException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Cuenta de ahorros: gana interés (porcentaje sobre el saldo), no admite
 * sobregiro y debe conservar un saldo mínimo.
 */
public class Ahorros extends Cuenta {
    private BigDecimal interes;               // porcentaje, por ejemplo 5 = 5 %
    private final BigDecimal saldoMinimo;

    public Ahorros(int numero, BigDecimal saldoInicial, Titular titular,
                   BigDecimal interes, BigDecimal saldoMinimo) {
        super(numero, saldoInicial, titular);
        if (saldoMinimo == null || saldoMinimo.signum() < 0)
            throw new DatoInvalidoException("El saldo mínimo no puede ser negativo.");
        if (saldoInicial.compareTo(saldoMinimo) < 0)
            throw new DatoInvalidoException("El saldo inicial debe ser al menos el saldo mínimo ($" + saldoMinimo + ").");
        setInteres(interes);
        this.saldoMinimo = redondear(saldoMinimo);
    }

    public BigDecimal getInteres() { return interes; }

    public void setInteres(BigDecimal interes) {
        if (interes == null || interes.signum() <= 0)
            throw new DatoInvalidoException("El interés debe ser mayor que cero.");
        this.interes = interes;
    }

    public BigDecimal getMaximoRetirable() {
        return getSaldo().subtract(saldoMinimo).max(BigDecimal.ZERO);
    }

    @Override
    public void calcularPorcentaje() {
        BigDecimal ganancia = getSaldo().multiply(interes).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        if (ganancia.signum() > 0) acreditar(ganancia, "INTERÉS");
    }

    @Override
    public void retirar(BigDecimal monto) throws SaldoInsuficienteException {
        validarMonto(monto);
        if (getSaldo().subtract(monto).compareTo(saldoMinimo) < 0)
            throw new SaldoInsuficienteException("Retiro rechazado: la cuenta debe conservar $" + saldoMinimo
                    + ". Máximo retirable: $" + getMaximoRetirable() + ".");
        debitar(monto, "RETIRO");
    }

    @Override
    public String getTipo() { return "AHORROS"; }

    @Override
    public String toString() {
        return super.toString() + " | Interés: " + interes + "% | Mínimo: $" + saldoMinimo;
    }
}
