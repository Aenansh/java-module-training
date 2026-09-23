import java.util.Arrays;
import java.util.List;

import contracts.DispatchStrategy;
import domain.Direction;
import domain.FreightElevator;
import domain.PassengerElevator;
import domain.Request;
import exceptions.MechanicalFaultException;
import logging.DispatchLogger;
import management.ElevatorFleetManager;
import management.RequestDispatcher;
import strategy.LeastBusyStrategy;
import strategy.NearestCarStrategy;
import strategy.ZoneBasedStrategy;

public class Main {
  public static void main(String[] args) {
    DispatchLogger logger = new DispatchLogger();
    ElevatorFleetManager fleetManager = ElevatorFleetManager.getInstance();

    PassengerElevator p1 = new PassengerElevator("P1", 0);
    PassengerElevator p2 = new PassengerElevator("P2", 5);
    PassengerElevator p3 = new PassengerElevator("P3", 10);
    FreightElevator f1 = new FreightElevator("F1", -1);

    FaultInjectingPassengerElevator faulty = new FaultInjectingPassengerElevator("P4-FAULTY", 3);

    fleetManager.register(p1);
    fleetManager.register(p2);
    fleetManager.register(p3);
    fleetManager.register(f1);
    fleetManager.register(faulty);

    DispatchStrategy nearest = new NearestCarStrategy();
    RequestDispatcher dispatcher = new RequestDispatcher(nearest, fleetManager, logger);

    System.out.println("=== Phase 1: NearestCarStrategy ===");
    dispatchBatch(dispatcher, logger);

    System.out.println();
    System.out.println("=== Phase 2: swap to LeastBusyStrategy at runtime ===");
    dispatcher.setStrategy(new LeastBusyStrategy());
    dispatchBatch(dispatcher, logger);

    System.out.println();
    System.out.println("=== Phase 3: swap to ZoneBasedStrategy(zoneSize=5) ===");
    dispatcher.setStrategy(new ZoneBasedStrategy(5));
    dispatchBatch(dispatcher, logger);

    System.out.println();
    System.out.println("=== Phase 4: overload rejection demo ===");
    Request overloadedRequest = new Request.Builder()
        .floor(2)
        .direction(Direction.UP)
        .estimatedLoadKg(5000)
        .build();
    dispatcher.dispatch(overloadedRequest);

    System.out.println();
    System.out.println("=== Phase 5: mechanical fault + graceful degradation demo ===");
    System.out.println("Active fleet before fault: " + fleetManager.activeCarCount());
    Request faultTriggerRequest = new Request.Builder()
        .floor(3)
        .direction(Direction.IDLE)
        .estimatedLoadKg(70)
        .build();

    dispatcher.setStrategy(new NearestCarStrategy());
    dispatcher.dispatch(faultTriggerRequest);
    System.out.println("Active fleet after fault: " + fleetManager.activeCarCount());

    dispatcher.drainRequeueBuffer();

    System.out.println();
    System.out.println("=== Full dispatch log (" + logger.getEventCount() + " events) ===");
    logger.printLog();
  }

  private static void dispatchBatch(RequestDispatcher dispatcher, DispatchLogger logger) {
    List<Request> batch = Arrays.asList(
        new Request.Builder().floor(7).direction(Direction.UP).estimatedLoadKg(80).build(),
        new Request.Builder().floor(2).direction(Direction.DOWN).estimatedLoadKg(60).build(),
        new Request.Builder().floor(12).direction(Direction.UP).estimatedLoadKg(150).build(),
        new Request.Builder().floor(-1).direction(Direction.UP).estimatedLoadKg(400).build()
    );
    for (Request r : batch) {
      dispatcher.dispatch(r);
    }
  }

  private static class FaultInjectingPassengerElevator extends PassengerElevator {

    FaultInjectingPassengerElevator(String id, int startFloor) {
      super(id, startFloor);
    }

    @Override
    protected void simulateMotorTravel(int targetFloor) {
      throw new MechanicalFaultException(
          "Simulated sensor corruption while traveling to floor " + targetFloor,
          getId());
    }
  }
}
