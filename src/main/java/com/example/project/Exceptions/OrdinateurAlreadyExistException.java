package com.example.project.Exceptions;

public class OrdinateurAlreadyExistException extends RuntimeException {
  public OrdinateurAlreadyExistException(String message) {
    super(message);
  }

  public OrdinateurAlreadyExistException(String message, Throwable cause) {
    super(message, cause);
  }

  public OrdinateurAlreadyExistException(Throwable cause) {
    super(cause);
  }

  public OrdinateurAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public OrdinateurAlreadyExistException() {
  }
}
