package exceptions;

public class ElevatorRuntimeException extends RuntimeException {

  protected ElevatorRuntimeException(String message) {
    super(message);
  }

  protected ElevatorRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }
}
