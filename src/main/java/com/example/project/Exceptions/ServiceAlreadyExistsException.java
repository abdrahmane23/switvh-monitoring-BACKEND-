package com.example.project.Exceptions;

public class ServiceAlreadyExistsException extends RuntimeException {
    public ServiceAlreadyExistsException(String message) {
        super(message);
    }

    public ServiceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public ServiceAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ServiceAlreadyExistsException() {
    }
}
