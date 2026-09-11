package domain;

import contracts.Loadable;
import exceptions.OverloadException;

public abstract class ElevatorCar extends Vehicle implements Loadable {

  private double currentLoad;
  private final double maxCapacityKg;

  protected ElevatorCar(String id, int startFloor, double maxCapacityKg) {
    super(id, startFloor);
    this.maxCapacityKg = maxCapacityKg;
    this.currentLoad = 0.0;
  }

  @Override
  public void addLoad(double kg) throws OverloadException {
    if (currentLoad + kg > maxCapacityKg) {
      throw new OverloadException(
          "Car " + getId() + " would exceed capacity: " + (currentLoad + kg) + "kg > " + maxCapacityKg + "kg",
          currentLoad + kg, maxCapacityKg);
    }

    this.currentLoad += kg;
  }

  @Override
  public void removeLoad(double kg) {
    this.currentLoad = Math.max(0.0, this.currentLoad - kg);
  }

  @Override
  public double getCurrentLoad() {
    return currentLoad;
  }

  public double getMaxCapacityKg() {
    return maxCapacityKg;
  }
}