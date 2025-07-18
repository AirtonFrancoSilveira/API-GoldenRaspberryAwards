package com.texoit.airton.movieapi.shared.exception;

public class InvalidIntervalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidIntervalException(String message) {
        super(message);
    }

    public InvalidIntervalException(String message, Throwable cause) {
        super(message, cause);
    }
}