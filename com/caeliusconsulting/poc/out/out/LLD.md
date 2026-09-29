# Smart Elevator Dispatch & Load Balancing System — LLD

## 1. System Scope & Non-Goals

**In scope:** N elevators, M floors, up/down hall calls, in-car floor selection, dispatch algorithm selection at runtime, overload detection, mechanical fault simulation, real-time logging, graceful degradation (one car goes out of service, fleet re-balances).

**Out of scope (say this explicitly in the interview — shows maturity):** persistence/DB, REST API layer, actual hardware I/O. This is a pure in-memory simulation with a console driver — keeps the review focused on OOP/exception design, not plumbing.

---

## 2. Package Structure

```
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

This package split is itself a talking point: **contracts** vs **domain** vs **strategy** vs **management** shows you separate "what a thing can do" from "what a thing is" from "how decisions are made" — a deliberate SRP boundary, not accidental.

---

## 3. Class Design (Core OOP Mapping)

### 3.1 Interfaces (pure contracts — no fields, no shared code)

```java
public interface Movable {
    void moveTo(int floor) throws MechanicalFaultException;
    Direction getCurrentDirection();
}

public interface Loadable {
    void addLoad(double kg) throws OverloadException;
    void removeLoad(double kg);
    double getCurrentLoad();
}

public interface DispatchStrategy {
    ElevatorCar selectCar(List<ElevatorCar> availableCars, Request request);
}
```

**Why interfaces here:** these define *capability contracts* that unrelated classes might implement differently (a `FreightElevator` and a future `Escalator` could both be `Movable` without sharing a common ancestor). This is the textbook justification interviewers want — not "interfaces because Java has them."

### 3.2 Abstract Class (shared state + partial/template logic)

```java
public abstract class Vehicle implements Movable {
    protected int currentFloor;
    protected Direction direction;
    protected ElevatorState state;

    public Vehicle(int startFloor) {
        this.currentFloor = startFloor;
        this.direction = Direction.IDLE;
        this.state = ElevatorState.STOPPED;
    }

    // Template method: subclasses customize openDoor() but the
    // pre/post safety check sequence is fixed here.
    public final void moveTo(int floor) throws MechanicalFaultException {
        validateFloor(floor);
        this.direction = floor > currentFloor ? Direction.UP : Direction.DOWN;
        // ... motor simulation ...
        this.currentFloor = floor;
        openDoor();
    }

    protected abstract void openDoor();
    protected abstract void validateFloor(int floor) throws MechanicalFaultException;
}
```

**Why abstract class here, not interface:** `Vehicle` carries actual mutable state (`currentFloor`, `direction`) and a **template method** (`moveTo`) that must not be overridden (`final`) while still delegating specific steps (`openDoor`) to subclasses. That's the litmus test to state out loud: *"shared state + partial implementation → abstract class; pure behavioral contract → interface."*

### 3.3 Inheritance chain

```
Vehicle (abstract)
  └── ElevatorCar (abstract) implements Loadable
        ├── PassengerElevator
        └── FreightElevator
```

```java
public abstract class ElevatorCar extends Vehicle implements Loadable {
    private double currentLoad;              // encapsulated
    protected static final double MAX_CAPACITY_KG = 1000.0; // final

    public ElevatorCar(int startFloor) { super(startFloor); }

    @Override
    public void addLoad(double kg) throws OverloadException {
        if (currentLoad + kg > MAX_CAPACITY_KG) {
            throw new OverloadException(
                "Car exceeds " + MAX_CAPACITY_KG + "kg limit");
        }
        this.currentLoad += kg;
    }

    @Override
    public double getCurrentLoad() { return currentLoad; }
}

public class FreightElevator extends ElevatorCar {
    public FreightElevator(int startFloor) { super(startFloor); }

    @Override
    protected void openDoor() {
        SafetyController.runFreightDoorCheck(this);  // extra check first
        super.openDoor();                             // then base behavior — wait,
        // note: base openDoor() is abstract in Vehicle, so FreightElevator/
        // PassengerElevator provide the concrete body; if a *shared* partial
        // behavior is wanted, pull a protected helper into ElevatorCar instead
        // and call super.<helper>() from there. Mention this nuance live —
        // it shows you think about where "super" calls actually make sense.
    }

    @Override
    protected void validateFloor(int floor) throws MechanicalFaultException {
        if (floor < 0) throw new MechanicalFaultException("Basement freight lock fault");
    }
}
```

> **Interview note on `super`:** don't force a `super.openDoor()` call if `openDoor()` is abstract with no body to call. The clean, defensible version: put a **concrete, protected** `baseDoorSequence()` in `ElevatorCar`, and have `FreightElevator.openDoor()` call `super.baseDoorSequence()` after its own pre-check. Small detail, but panels notice when `super` calls are structurally meaningless.

### 3.4 Polymorphism — Strategy Pattern

```java
public class NearestCarStrategy implements DispatchStrategy {
    public ElevatorCar selectCar(List<ElevatorCar> cars, Request req) {
        return cars.stream()
            .filter(c -> c.getState() != ElevatorState.OUT_OF_SERVICE)
            .min(Comparator.comparingInt(c -> Math.abs(c.getCurrentFloor() - req.getFloor())))
            .orElseThrow(() -> new MechanicalFaultException("No car available"));
    }
}

public class LeastBusyStrategy implements DispatchStrategy {
    public ElevatorCar selectCar(List<ElevatorCar> cars, Request req) {
        return cars.stream()
            .min(Comparator.comparingDouble(ElevatorCar::getCurrentLoad))
            .orElseThrow(() -> new MechanicalFaultException("No car available"));
    }
}
```

The `RequestDispatcher` holds a `DispatchStrategy` reference and can **swap it at runtime** (`setStrategy(new LeastBusyStrategy())`) — this is the exact moment in the live demo where you show polymorphism producing measurably different behavior, not just "method overriding."

### 3.5 `static` — Fleet Registry (Singleton)

```java
public class ElevatorFleetManager {
    private static ElevatorFleetManager instance;
    private static final List<ElevatorCar> registry = new ArrayList<>();

    private ElevatorFleetManager() {}

    public static synchronized ElevatorFleetManager getInstance() {
        if (instance == null) instance = new ElevatorFleetManager();
        return instance;
    }

    public static void register(ElevatorCar car) { registry.add(car); }
    public static List<ElevatorCar> getActiveFleet() {
        return registry.stream()
            .filter(c -> c.getState() != ElevatorState.OUT_OF_SERVICE)
            .collect(Collectors.toList());
    }
}
```

### 3.6 `this` — Builder Pattern for `Request`

```java
public class Request {
    private final int floor;
    private final Direction direction;
    private final long timestamp;

    private Request(Builder b) {
        this.floor = b.floor;
        this.direction = b.direction;
        this.timestamp = b.timestamp;
    }

    public static class Builder {
        private int floor;
        private Direction direction;
        private long timestamp = System.currentTimeMillis();

        public Builder floor(int floor) { this.floor = floor; return this; }
        public Builder direction(Direction d) { this.direction = d; return this; }
        public Request build() { return new Request(this); }
    }
}
```

### 3.7 `final` — `SafetyController`

```java
public final class SafetyController {   // cannot be subclassed — policy is fixed
    private SafetyController() {}        // cannot be instantiated — pure static utility

    public static void runFreightDoorCheck(FreightElevator car) {
        if (car.getCurrentLoad() > ElevatorCar.MAX_CAPACITY_KG * 0.95) {
            throw new DoorObstructionException("Load too near limit for safe door cycle");
        }
    }
}
```

---

## 4. String Class Usage — Deliberate, Not Decorative

| Class | Where used | Why this one specifically |
|---|---|---|
| `String` | Floor codes, car IDs (`"CAR-2"`), immutable config keys | Immutability wanted — car IDs must never mutate after assignment |
| `StringBuilder` | `DispatchLogger` appending per-tick events (`"Car-2 → Floor 7 → Load 82%"`) in a single-threaded simulation loop | Mutable, no sync overhead — you control the only writer thread |
| `StringBuffer` | *Deliberately NOT used here* — call this out explicitly: "I considered `StringBuffer` for the log, but since this simulation runs dispatch on a single control thread, `StringBuilder` is the correct, faster choice. `StringBuffer` would only be justified if multiple elevator threads wrote to one shared log concurrently — which I *did* use in a variant" | This shows you understand the trade-off rather than "always use StringBuilder" as a rule you memorized |

If you want a **legitimate `StringBuffer` moment**, run each `ElevatorCar` on its own `Thread` (real concurrency, not just simulated ticks) and have all of them append to one shared `StringBuffer` fleet log — then you have a true, defensible use case instead of a forced one.

---

## 5. Exception Hierarchy

```
Exception
 └── ElevatorException (abstract, checked)
       ├── OverloadException        — caller MUST handle before allowing boarding
       └── InvalidFloorException    — caller MUST handle before dispatch

RuntimeException
 └── ElevatorRuntimeException (abstract, unchecked)
       ├── MechanicalFaultException — sensor/motor corruption, fail fast
       └── DoorObstructionException — hardware-level, not recoverable by caller logic
```

```java
public abstract class ElevatorException extends Exception {
    public ElevatorException(String msg) { super(msg); }
}

public class OverloadException extends ElevatorException {
    public OverloadException(String msg) { super(msg); }
}

public abstract class ElevatorRuntimeException extends RuntimeException {
    public ElevatorRuntimeException(String msg) { super(msg); }
}

public class MechanicalFaultException extends ElevatorRuntimeException {
    public MechanicalFaultException(String msg) { super(msg); }
}
```

**The one-sentence justification panels want to hear:**
*"Checked exceptions model conditions the caller can reasonably recover from — an overloaded car should be handled by rejecting boarding and re-queuing the passenger. Unchecked exceptions model conditions that indicate a bug or hardware failure the caller can't meaningfully recover from mid-call — a mechanical fault should propagate and trigger fleet-wide re-balancing, not be silently caught at the call site."*

### try-catch-finally flow

```java
public void dispatch(Request request) {
    ElevatorCar car = null;
    try {
        car = strategy.selectCar(ElevatorFleetManager.getActiveFleet(), request);
        car.addLoad(request.getEstimatedLoad());   // may throw OverloadException (checked)
        car.moveTo(request.getFloor());            // may throw MechanicalFaultException (unchecked)
    } catch (OverloadException e) {
        logger.log("REJECTED: " + e.getMessage());
        requeue(request);
    } catch (MechanicalFaultException e) {
        logger.log("FAULT: " + e.getMessage());
        markOutOfService(car);
        redispatch(request);                        // graceful degradation
    } finally {
        if (car != null) car.releaseDoorLock();      // ALWAYS runs
    }
}
```

---

## 6. Sequence Flow (one request, end to end)

```
User/Simulator → Request.Builder → Request
Request → RequestDispatcher.dispatch(request)
RequestDispatcher → DispatchStrategy.selectCar(fleet, request)
DispatchStrategy → ElevatorFleetManager.getActiveFleet()  [static]
DispatchStrategy → returns chosen ElevatorCar
RequestDispatcher → ElevatorCar.addLoad(kg)
    [if overload] → throws OverloadException → caught → requeue
RequestDispatcher → ElevatorCar.moveTo(floor)
    ElevatorCar (Vehicle.moveTo, final template method)
        → validateFloor()  [subclass-specific]
        → openDoor()       [subclass-specific, may call SafetyController]
    [if fault] → throws MechanicalFaultException → caught → markOutOfService + redispatch
RequestDispatcher → DispatchLogger.log(...)   [StringBuilder append]
finally → ElevatorCar.releaseDoorLock()
```

---

## 7. Demo Script for the Review (suggested order)

1. **Show the class hierarchy diagram** (30 sec) — establishes you designed top-down, not bottom-up.
2. **Run baseline simulation** with `NearestCarStrategy` — print average wait time.
3. **Swap to `LeastBusyStrategy` at runtime** via `dispatcher.setStrategy(...)` — re-run, show different (better/worse) average wait time. *This is your polymorphism proof.*
4. **Inject an overload** (send a request with load > capacity) — show `OverloadException` caught, request requeued, system stays up.
5. **Inject a mechanical fault** — show `MechanicalFaultException` propagating, car marked `OUT_OF_SERVICE`, fleet manager re-balances remaining requests to other cars. *This is your "graceful degradation" hook.*
6. **Show the log output** built via `StringBuilder`, and explain the `StringBuffer` trade-off decision even though you didn't need it here — shows depth beyond what the code strictly required.

---

## 8. Extension Points (mention if asked "how would you scale this")

- Replace `List<ElevatorCar>` fleet with a real **Observer pattern**: `ElevatorCar` publishes state-change events, `FloorDisplayPanel` and `DispatchLogger` subscribe — decouples logging from dispatch logic.
- Introduce a `RequestQueue` (priority queue by wait time) instead of immediate synchronous dispatch, to handle burst load.
- Move `ElevatorFleetManager`'s static registry to instance-based + dependency injection if you ever needed multiple independent buildings in one simulation (the current static-singleton design intentionally trades that flexibility for simplicity — say this trade-off out loud, it's a strong signal).