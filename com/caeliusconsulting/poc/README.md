# Overview
Simulates a multi-elevator dispatch algorithm for a high-rise building — deciding which elevator car should respond to a floor request based on direction, current load, and distance, while handling overload and mechanical-fault scenarios. Real-world utility: this is the actual algorithmic core vendors like Otis/KONE solve for energy efficiency and wait-time reduction.

## Scope
- N elevators
- M floors
- Up/Down hall calls
- In-car floor selection
- Dispatch algorithm selection at run-time
- Overload detection
- Mechanical fault simulation
- Real-time logging
- Graceful degradation (one car goes out of service, fleet re-balances)

## Package Structure

```bash
com.elevatorsim
├── domain
│   ├── Vehicle.java                 (abstract class)
│   ├── ElevatorCar.java             (extends Vehicle)
│   ├── PassengerElevator.java       (extends ElevatorCar)
│   ├── FreightElevator.java         (extends ElevatorCar)
│   ├── Direction.java               (enum: UP, DOWN, IDLE)
│   ├── ElevatorState.java           (enum: MOVING, STOPPED, DOOR_OPEN, OUT_OF_SERVICE)
│   └── Request.java                 (immutable-ish value object via Builder)
│
├── contracts                        (pure interfaces — no state)
│   ├── Movable.java
│   ├── Loadable.java
│   └── DispatchStrategy.java
│
├── strategy
│   ├── NearestCarStrategy.java
│   ├── LeastBusyStrategy.java
│   └── ZoneBasedStrategy.java
│
├── management
│   ├── ElevatorFleetManager.java    (singleton, static registry)
│   ├── RequestDispatcher.java       (orchestrates strategy selection + assignment)
│   └── SafetyController.java        (final class — capacity/door interlock checks)
│
├── exceptions
│   ├── ElevatorException.java       (abstract base, extends Exception)
│   ├── OverloadException.java       (checked)
│   ├── InvalidFloorException.java   (checked)
│   ├── MechanicalFaultException.java (unchecked, extends RuntimeException)
│   └── DoorObstructionException.java (unchecked)
│
├── logging
│   └── DispatchLogger.java          (StringBuilder-backed event log)
│
└── Main.java                        (simulation driver)
```