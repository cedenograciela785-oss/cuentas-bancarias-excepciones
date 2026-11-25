package Entidad;

import Excepciones.DatoInvalidoException;
import Excepciones.SaldoInsuficienteException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Cuenta corriente: cobra un costo de operaciones (porcentaje sobre el saldo
 * positivo) y permite sobregiro hasta un límite autorizado.
 */
public class Corriente extends Cuenta {
    private BigDecimal costoOperaciones;      // porcentaje
    private final BigDecimal limiteSobregiro;

    public Corriente(int numero, BigDecimal saldoInicial, Titular titular,
                     BigDecimal costoOperaciones, BigDecimal limiteSobregiro) {
        super(numero, saldoInicial, titular);
        if (limiteSobregiro == null || limiteSobregiro.signum() < 0)
            throw new DatoInvalidoException("El límite de sobregiro no puede ser negativo.");
        setCostoOperaciones(costoOperaciones);
        this.limiteSobregiro = redondear(limiteSobregiro);
    }

    public BigDecimal getCostoOperaciones() { return costoOperaciones; }

    public void setCostoOperaciones(BigDecimal costo) {
        if (costo == null || costo.signum() <= 0)
            throw new DatoInvalidoException("El costo de operaciones debe ser mayor que cero.");
        this.costoOperaciones = costo;
    }

    /** Dinero realmente utilizable: saldo más sobregiro autorizado. */
    public BigDecimal getDisponible() {
        return getSaldo().add(limiteSobregiro);
    }

    @Override
    public void calcularPorcentaje() {
        if (getSaldo().signum() <= 0) return;     // no se cobra sobre saldo cero o en rojo
        BigDecimal costo = getSaldo().multiply(costoOperaciones).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        if (costo.signum() > 0) debitar(costo, "COSTO OPERACIONES");
    }

    @Override
    public void retirar(BigDecimal monto) throws SaldoInsuficienteException {
        validarMonto(monto);
        if (monto.compareTo(getDisponible()) > 0)
            throw new SaldoInsuficienteException("Retiro rechazado: supera el disponible de $" + getDisponible()
                    + " (saldo + sobregiro de $" + limiteSobregiro + ").");
        debitar(monto, "RETIRO");
    }

    @Override
    public String getTipo() { return "CORRIENTE"; }

    @Override
    public String toString() {
        return super.toString() + " | Costo: " + costoOperaciones + "% | Sobregiro: $" + limiteSobregiro;
    }
}
