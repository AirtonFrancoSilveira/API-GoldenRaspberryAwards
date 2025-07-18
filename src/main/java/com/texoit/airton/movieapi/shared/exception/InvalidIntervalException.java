package com.texoit.airton.movieapi.shared.exception;

/**
 * Exception lançada quando um intervalo de produtor é inválido.
 * Segue o padrão de exceptions de domínio para Clean Architecture.
 */
public class InvalidIntervalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidIntervalException(String message) {
        super(message);
    }

    public InvalidIntervalException(String message, Throwable cause) {
        super(message, cause);
    }
}