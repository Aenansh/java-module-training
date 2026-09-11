package contracts;

import domain.Direction;
import exceptions.InvalidFloorException;

public interface Movable {
  void moveTo(int targetFloor) throws InvalidFloorException;

  Direction getCurrentDirection();
}