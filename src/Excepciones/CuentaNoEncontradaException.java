package Excepciones;

/** Excepción verificada: se lanza cuando se busca un número de cuenta que no existe. */
public class CuentaNoEncontradaException extends Exception {
    public CuentaNoEncontradaException(int numero) {
        super("No existe ninguna cuenta con el número " + numero + ".");
    }
}
