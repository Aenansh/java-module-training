package exceptions;

public class MechanicalFaultException extends ElevatorRuntimeException {

  private final String carId;

  public MechanicalFaultException(String message, String carId) {
    super(message);
    this.carId = carId;
  }

  public String getCarId() {
    return carId;
  }
}