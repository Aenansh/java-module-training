package management;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import domain.ElevatorCar;
import domain.ElevatorState;

public final class ElevatorFleetManager {
  private static volatile ElevatorFleetManager instance;

  private final List<ElevatorCar> registry = new ArrayList<>();

  private ElevatorFleetManager() {
  }

  public static ElevatorFleetManager getInstance() {
    ElevatorFleetManager result = instance;
    if (result == null) {
      synchronized (ElevatorFleetManager.class) {
        result = instance;
        if (result == null) {
          instance = result = new ElevatorFleetManager();
        }
      }
    }
    return result;
  }

  public synchronized void register(ElevatorCar car) {
    registry.add(car);
  }

  public synchronized void markOutOfService(ElevatorCar car) {
    car.setState(ElevatorState.OUT_OF_SERVICE);
  }

  public synchronized void restoreToService(ElevatorCar car) {
    car.setState(ElevatorState.STOPPED);
  }

  public synchronized List<ElevatorCar> getActiveFleet() {
    List<ElevatorCar> active = new ArrayList<>();
    for (ElevatorCar car : registry) {
      if (car.getState() != ElevatorState.OUT_OF_SERVICE) {
        active.add(car);
      }
    }

    return Collections.unmodifiableList(active);
  }

  public synchronized List<ElevatorCar> getFullFleet() {
    return Collections.unmodifiableList(new ArrayList<>(registry));
  }

  public synchronized int activeCarCount() {
    return getActiveFleet().size();
  }
}