package domain;

import exceptions.InvalidFloorException;

public class PassengerElevator extends ElevatorCar {
  private static final double DEFAULT_MAX_CAPACITY_KG = 700.0;
  private static final int MIN_SERVICEABLE_FLOOR = 0;

  public PassengerElevator(String id, int startFloor) {
    super(id, startFloor, DEFAULT_MAX_CAPACITY_KG);
  }

  @Override 
  protected void validateFloor(int floor) throws InvalidFloorException {
    if (floor < MIN_SERVICEABLE_FLOOR) {
      throw new InvalidFloorException(
          "Passenger elevator " + getId() + " has no access to floor " + floor + " (basement is freight-only)", floor);
    }
  }

  @Override 
  public void openDoor() {
    playChime();
    super.baseDoorSequence();
  }

  private void playChime() {
    System.out.println("Ting-ting");
  }
}