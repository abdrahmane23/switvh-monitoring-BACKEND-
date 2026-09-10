package com.example.project.Exceptions;

public class SwitchConnectionException extends RuntimeException {
    public SwitchConnectionException(String message) {
        super(message);
    }

    public SwitchConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SwitchConnectionException(Throwable cause) {
        super(cause);
    }

    public SwitchConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SwitchConnectionException() {
    }
}
