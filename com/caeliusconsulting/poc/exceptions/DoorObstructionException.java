package exceptions;

public class DoorObstructionException extends ElevatorRuntimeException {

  public DoorObstructionException(String message) {
    super(message);
  }
}