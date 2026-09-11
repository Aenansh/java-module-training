package contracts;

import exceptions.OverloadException;

public interface Loadable {
  void addLoad(double kg) throws OverloadException;

  void removeLoad(double kg);

  double getCurrentLoad();
}