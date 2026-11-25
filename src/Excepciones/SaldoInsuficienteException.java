package Excepciones;

/**
 * Excepción verificada (checked): obliga a quien llama a capturarla o declararla.
 * Se lanza cuando un retiro o una transferencia supera lo que la cuenta permite.
 */
public class SaldoInsuficienteException extends Exception {
    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
