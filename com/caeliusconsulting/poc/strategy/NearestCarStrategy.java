package strategy;

import java.util.Comparator;
import java.util.List;

import contracts.DispatchStrategy;
import domain.ElevatorCar;
import domain.Request;
import exceptions.MechanicalFaultException;

public class NearestCarStrategy implements DispatchStrategy {
  @Override
  public ElevatorCar selectCar(List<ElevatorCar> availableCars, Request request) {
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
    return "NEAREST_CAR";
  }
}