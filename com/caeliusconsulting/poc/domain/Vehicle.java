package domain;

import contracts.Movable;
import contracts.Openable;
import exceptions.InvalidFloorException;

public abstract class Vehicle implements Movable, Openable {

  protected final String id;
  protected int currentFloor;
  protected Direction direction;
  protected ElevatorState state;
  private boolean doorOpen;

  protected Vehicle(String id, int startFloor) {
    this.id = id;
    this.currentFloor = startFloor;
    this.direction = Direction.IDLE;
    this.state = ElevatorState.STOPPED;
    this.doorOpen = false;
  }

  @Override
  public final void moveTo(int floor) throws InvalidFloorException {
    validateFloor(floor);

    this.direction = floor > currentFloor ? Direction.UP : floor < currentFloor ? Direction.DOWN : Direction.IDLE;
    this.state = ElevatorState.MOVING;

    simulateMotorTravel(floor);

    this.currentFloor = floor;

    this.direction = Direction.IDLE;
    openDoor();
  }

  protected void simulateMotorTravel(int targetFloor) {
    System.out.println("Elevator is moving to floor " + targetFloor);
  }

  protected abstract void validateFloor(int floor) throws InvalidFloorException;

  @Override
  public abstract void openDoor();

  protected final void baseDoorSequence() {
    this.doorOpen = true;
    this.state = ElevatorState.DOOR_OPEN;
  }

  @Override
  public void closeDoor() {
    this.doorOpen = false;
    if (this.state != ElevatorState.OUT_OF_SERVICE) {
      this.state = ElevatorState.STOPPED;
    }
  }

  @Override
  public boolean isDoorOpen() {
    return doorOpen;
  }

  @Override
  public Direction getCurrentDirection() {
    return direction;
  }

  public void releaseDoorLock() {
    if (doorOpen) {
      closeDoor();
    }
  }

  public String getId() {
    return id;
  }

  public int getCurrentFloor() {
    return currentFloor;
  }

  public ElevatorState getState() {
    return state;
  }

  public void setState(ElevatorState state) {
    this.state = state;
  }
}