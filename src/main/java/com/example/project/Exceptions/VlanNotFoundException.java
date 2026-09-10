package com.example.project.Exceptions;

public class VlanNotFoundException extends RuntimeException {
    public VlanNotFoundException(String message) {
        super(message);
    }

  public VlanNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public VlanNotFoundException(Throwable cause) {
    super(cause);
  }

  public VlanNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public VlanNotFoundException() {
  }
}
