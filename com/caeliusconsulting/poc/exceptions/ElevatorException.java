package exceptions;

public class ElevatorException extends Exception {

  protected ElevatorException(String message) {
    super(message);
  }

  protected ElevatorException(String message, Throwable cause) {
    super(message, cause);
  }
}