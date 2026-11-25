package Excepciones;

/** Excepción no verificada: un dato de entrada (cédula, nombre, número, porcentaje) no cumple las reglas. */
public class DatoInvalidoException extends RuntimeException {
    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
