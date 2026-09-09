package com.example.project.Exceptions;


public class OrdinateurNotFoundException extends RuntimeException {
    public OrdinateurNotFoundException(String message) {
        super(message);
    }

    public OrdinateurNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrdinateurNotFoundException(Throwable cause) {
        super(cause);
    }

    public OrdinateurNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public OrdinateurNotFoundException() {
    }
}
