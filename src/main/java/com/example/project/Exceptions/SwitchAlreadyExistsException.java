package com.example.project.Exceptions;

public class SwitchAlreadyExistsException extends RuntimeException {
    public SwitchAlreadyExistsException(String message) {
        super(message);
    }

    public SwitchAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public SwitchAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public SwitchAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SwitchAlreadyExistsException() {
    }
}
