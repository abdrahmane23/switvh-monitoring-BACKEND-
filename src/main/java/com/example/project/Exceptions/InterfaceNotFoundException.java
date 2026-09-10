package com.example.project.Exceptions;

public class InterfaceNotFoundException extends RuntimeException {

    public InterfaceNotFoundException() {
    }

    public InterfaceNotFoundException(String message) {
        super(message);
    }

    public InterfaceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public InterfaceNotFoundException(Throwable cause) {
        super(cause);
    }

    public InterfaceNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
