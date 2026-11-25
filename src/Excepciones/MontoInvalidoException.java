package Excepciones;

/** Excepción no verificada (unchecked): el monto es cero, negativo o nulo. */
public class MontoInvalidoException extends RuntimeException {
    public MontoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
