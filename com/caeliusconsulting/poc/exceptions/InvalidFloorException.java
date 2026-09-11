package exceptions;

public class InvalidFloorException extends ElevatorException {
  private final int requestedFloor;

  public InvalidFloorException(String message, int requestedFloor) {
    super(message);
    this.requestedFloor = requestedFloor;
  }

  public int getRequestedFloor() {
    return requestedFloor;
  }
}