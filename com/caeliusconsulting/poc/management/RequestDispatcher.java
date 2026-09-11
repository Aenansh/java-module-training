package management;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import contracts.DispatchStrategy;
import domain.ElevatorCar;
import domain.Request;
import exceptions.DoorObstructionException;
import exceptions.InvalidFloorException;
import exceptions.MechanicalFaultException;
import exceptions.OverloadException;
import logging.DispatchLogger;

public class RequestDispatcher {
  private final ElevatorFleetManager fleetManager;
  private final DispatchLogger logger;
  private final Deque<Request> requeueBuffer = new ArrayDeque<>();

  private DispatchStrategy strategy;

  public RequestDispatcher(DispatchStrategy initialStrategy, ElevatorFleetManager fleetManager,
      DispatchLogger logger) {
    this.strategy = initialStrategy;
    this.fleetManager = fleetManager;
    this.logger = logger;
  }

  public void setStrategy(DispatchStrategy strategy) {
    logger.log("STRATEGY_SWITCH: " + this.strategy.getStrategyName() + " -> " + strategy.getStrategyName());
    this.strategy = strategy;
  }

  public String getActiveStrategyName() {
    return strategy.getStrategyName();
  }

  public void drainRequeueBuffer() {
    Request pending;
    while ((pending = requeueBuffer.poll()) != null) {
      dispatch(pending);
    }
  }

  public void dispatch(Request request) {
    ElevatorCar car = null;
    try {
      List<ElevatorCar> eligible = fleetManager.getActiveFleet();
      car = strategy.selectCar(eligible, request);

      try {
        car.addLoad(request.getEstimatedLoadKg());
      } catch (OverloadException e) {
        logger.log("REJECTED [" + car.getId() + "]: " + e.getMessage());
        requeueBuffer.offer(request);
        return;
      }

      SafetyController.verifyDoorCycleSafe(car);

      car.moveTo(request.getFloor());
      logger.log("SERVED [" + car.getId() + "] -> floor " + request.getFloor()
          + " via " + strategy.getStrategyName());

    } catch (InvalidFloorException e) {
      logger.log("REJECTED [" + (car != null ? car.getId() : "?") + "]: " + e.getMessage());
    } catch (DoorObstructionException e) {
      logger.log("SAFETY_HALT [" + (car != null ? car.getId() : "?") + "]: " + e.getMessage());
      requeueBuffer.offer(request);
    } catch (MechanicalFaultException e) {
      logger.log("FAULT [" + e.getCarId() + "]: " + e.getMessage()
          + " — marking out of service and redispatching");
      if (car != null) {
        fleetManager.markOutOfService(car);
      }
      requeueBuffer.offer(request);
    } finally {
      if (car != null) {
        car.releaseDoorLock();
      }
    }
  }
}