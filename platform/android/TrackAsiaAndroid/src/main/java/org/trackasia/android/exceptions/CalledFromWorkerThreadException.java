package org.trackasia.android.exceptions;

public class CalledFromWorkerThreadException extends RuntimeException {

  public CalledFromWorkerThreadException(String message) {
    super(message);
  }
}
