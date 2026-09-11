package strategy;

import java.util.Comparator;
import java.util.List;

import contracts.DispatchStrategy;
import domain.ElevatorCar;
import domain.Request;
import exceptions.MechanicalFaultException;

public class LeastBusyStrategy implements DispatchStrategy {
  @Override
  public ElevatorCar selectCar(List<ElevatorCar> availableCars, Request request) {
    return availableCars.stream().min(Comparator.comparingDouble(ElevatorCar::getCurrentLoad))
        .orElseThrow(() -> new MechanicalFaultException("No eligible car available for request at floor "
            + request.getFloor(), "FLEET"));
  }

  @Override 
  public String getStrategyName() {
    return "LEAST_BUSY";
  }
}