package management;

import domain.ElevatorCar;
import exceptions.DoorObstructionException;

public final class SafetyController {
  private static final double DOOR_CYCLE_LOAD_THRESHOLD_RATIO = 0.95;

  private SafetyController() {
    throw new AssertionError(
        "SafetyController is a static utility class; it cannot be instantiated and should be used through its static methods.");
  }

  public static void verifyDoorCycleSafe(ElevatorCar car) {
    double ratio = car.getCurrentLoad() / car.getMaxCapacityKg();
    if (ratio > DOOR_CYCLE_LOAD_THRESHOLD_RATIO) {
      throw new DoorObstructionException("Car " + car.getId() + " load at " + Math.round(ratio * 100)
          + "% of capacity - door cycle unsafe, manual clearance required.");
    }
  }
}