package domain;

import exceptions.InvalidFloorException;

public class FreightElevator extends ElevatorCar {
  private static final double DEFAULT_MAX_CAPACITY_KG = 2500.0;
  private static final int MIN_SERVICEABLE_FLOOR = -2;
  
  public FreightElevator(String id, int startFloor) {
    super(id, startFloor, DEFAULT_MAX_CAPACITY_KG);
  }

  @Override 
  protected void validateFloor(int floor) throws InvalidFloorException {
    if (floor < MIN_SERVICEABLE_FLOOR) {
      throw new InvalidFloorException(
          "Freight elevator " + getId()
              + " floor " + floor + " is below serviceable basement range",
          floor);
    }
  }

  @Override
  public void openDoor() {
    soundLoadWarningBuzzer();
    super.baseDoorSequence();
  }

  private void soundLoadWarningBuzzer() {
    System.out.println("Buzz-Buzz");
  }
}