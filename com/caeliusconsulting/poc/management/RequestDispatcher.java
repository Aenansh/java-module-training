package management;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import contracts.DispatchStrategy;
import domain.ElevatorCar;
import domain.Request;
import exceptions.DoorObstructionException;
import exceptions.InvalidFloorException;
import exceptions.MechanicalFaultException;
import exceptions.OverloadException;
import logging.DispatchLogger;

public class RequestDispatcher {
  private static final int MAX_REQUEUE_ATTEMPTS = 3;

  private final ElevatorFleetManager fleetManager;
  private final DispatchLogger logger;
  private final Deque<Request> requeueBuffer = new ArrayDeque<>();
  private final Map<Request, Integer> retryCounts = new HashMap<>();

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

  private void requeueIfAllowed(Request request, String failureReason) {
    int attempts = retryCounts.getOrDefault(request, 0) + 1;
    if (attempts > MAX_REQUEUE_ATTEMPTS) {
      retryCounts.remove(request);
      logger.log("DROP [" + request + "]: exceeded retry limit after " + MAX_REQUEUE_ATTEMPTS
          + " attempts (" + failureReason + ")");
      return;
    }

    retryCounts.put(request, attempts);
    requeueBuffer.offer(request);
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
        requeueIfAllowed(request, "overload");
        return;
      }

      SafetyController.verifyDoorCycleSafe(car);

      car.moveTo(request.getFloor());
      logger.log("SERVED [" + car.getId() + "] -> floor " + request.getFloor()
          + " via " + strategy.getStrategyName());
      retryCounts.remove(request);

    } catch (InvalidFloorException e) {
      logger.log("REJECTED [" + (car != null ? car.getId() : "?") + "]: " + e.getMessage());
    } catch (DoorObstructionException e) {
      logger.log("SAFETY_HALT [" + (car != null ? car.getId() : "?") + "]: " + e.getMessage());
      requeueIfAllowed(request, "door obstruction");
    } catch (MechanicalFaultException e) {
      logger.log("FAULT [" + e.getCarId() + "]: " + e.getMessage()
          + " — marking out of service and redispatching");
      if (car != null) {
        fleetManager.markOutOfService(car);
      }
      requeueIfAllowed(request, "mechanical fault");
    } finally {
      if (car != null) {
        car.releaseDoorLock();
      }
    }
  }
}