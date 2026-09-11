package strategy;

import java.util.Comparator;
import java.util.List;

import contracts.DispatchStrategy;
import domain.ElevatorCar;
import domain.Request;
import exceptions.MechanicalFaultException;

public class ZoneBasedStrategy implements DispatchStrategy {
  private final int zoneSizeFloors;

  public ZoneBasedStrategy(int zoneSizeFloors) {
    if (zoneSizeFloors <= 0) {
      throw new IllegalArgumentException("zoneSizeFloors must be positive");
    }
    this.zoneSizeFloors = zoneSizeFloors;
  }

  @Override
  public ElevatorCar selectCar(List<ElevatorCar> availableCars, Request request) {
    int requestZone = zoneOf(request.getFloor());

    return availableCars.stream()
        .filter(car -> zoneOf(car.getCurrentFloor()) == requestZone)
        .min(Comparator.comparingInt(
            car -> Math.abs(car.getCurrentFloor() - request.getFloor())))
        .orElseGet(() -> fallbackToNearestOverall(availableCars, request));
  }

  private int zoneOf(int floor) {
    return floor / zoneSizeFloors;
  }

  private ElevatorCar fallbackToNearestOverall(List<ElevatorCar> availableCars, Request request) {
    return availableCars.stream()
        .min(Comparator.comparingInt(
            car -> Math.abs(car.getCurrentFloor() - request.getFloor())))
        .orElseThrow(() -> new MechanicalFaultException(
            "No eligible car available for request at floor "
                + request.getFloor(),
            "FLEET"));
  }

  @Override
  public String getStrategyName() {
    return "ZONE_BASED(zoneSize=" + zoneSizeFloors + ")";
  }
}