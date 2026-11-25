package Entidad;

import Excepciones.CuentaNoEncontradaException;
import Excepciones.DatoInvalidoException;
import java.util.ArrayList;
import java.util.List;

/** Guarda las cuentas, evita números repetidos y las busca por número. */
public class Banco {
    private final List<Cuenta> cuentas = new ArrayList<>();

    public void agregar(Cuenta cuenta) {
        for (Cuenta c : cuentas) {
            if (c.getNumero() == cuenta.getNumero())
                throw new DatoInvalidoException("Ya existe una cuenta con el número " + cuenta.getNumero() + ".");
        }
        cuentas.add(cuenta);
    }

    public Cuenta buscar(int numero) throws CuentaNoEncontradaException {
        for (Cuenta c : cuentas) {
            if (c.getNumero() == numero) return c;
        }
        throw new CuentaNoEncontradaException(numero);
    }

    public List<Cuenta> getCuentas() { return cuentas; }
}
