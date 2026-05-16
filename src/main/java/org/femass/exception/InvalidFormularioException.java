package org.femass.exception;

public class InvalidFormularioException extends RuntimeException {
    public InvalidFormularioException(String message) {
        super(message);
    }

    public InvalidFormularioException(String message, Throwable cause) {
        super(message, cause);
    }
}

