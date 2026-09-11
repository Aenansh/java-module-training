package contracts;

import java.util.List;

import domain.ElevatorCar;
import domain.Request;

public interface DispatchStrategy {
  ElevatorCar selectCar(List<ElevatorCar> availableCars, Request request);

  String getStrategyName();
}