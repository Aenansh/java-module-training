# Smart Elevator Dispatch & Load Balancing System — Java POC

## How to run

```bash
cd src
javac -d out elevator/*.java
java -cp out elevator.Main
```

(Requires a JDK — a JRE alone won't have `javac`.)

## What it demonstrates, file by file

| File | Feature(s) it carries |
|---|---|
| `Movable.java` | Interface — pure contract, no state |
| `Vehicle.java` | Abstraction + abstract class — shared state & default `openDoor()` logic |
| `ElevatorCar.java` | Encapsulation — private `currentLoadKg`/`maxCapacityKg`, validated mutation only via `loadPassengers()` |
| `PassengerElevator.java` / `FreightElevator.java` | Inheritance; `FreightElevator.openDoor()` shows an **override + `super`** call |
| `DispatchStrategy.java`, `NearestCarStrategy.java`, `LeastBusyStrategy.java` | Polymorphism — the manager swaps strategy implementations at runtime |
| `Request.java` | `this` keyword — disambiguation + fluent builder chaining |
| `SafetyController.java` | `final` keyword — final class *and* final constant |
| `ElevatorFleetManager.java` | `static` keyword — singleton instance + shared fleet registry; `try-catch-finally` across two custom exception types; `StringBuilder` for the running dispatch log |
| `OverloadException.java` | Custom **checked** exception (`extends Exception`) |
| `MechanicalFaultException.java` | Custom **unchecked** exception (`extends RuntimeException`) |
| `StringDemo.java` | `String` immutability + core methods, `StringBuilder` vs `String` performance timing, `StringBuffer` thread-safety use case |
| `Main.java` | Wires it all together into one runnable simulation |

## Suggested live-demo flow (~5 min)

1. Run it once end-to-end so the panel sees output.
2. Point at the strategy swap in `Main.java` (`NearestCarStrategy` → `LeastBusyStrategy`) — same manager code, different runtime behavior. This is your polymorphism moment.
3. Trigger the overload path — show the **checked** `OverloadException` and explain why it's checked (recoverable, caller must decide).
4. Trigger the mechanical fault — show the **unchecked** `MechanicalFaultException` and explain why it *isn't* checked (unrecoverable inline, fail fast).
5. Open `FreightElevator.java` and trace the `super.openDoor()` call live.
6. Close with `StringDemo.java` — the `StringBuilder` vs `String` timing numbers are a concrete, non-hand-wavy answer to "why StringBuilder."

## Where to extend if asked "what would you add next?"

- A `Comparable<Request>`/priority queue for request ordering (ties into the Hospital Triage idea's pattern).
- A `ScheduledExecutorService` to actually run cars on separate threads — a natural follow-up to the `StringBuffer` thread-safety note.
- Externalizing capacities/floors into a config object instead of constructor literals.