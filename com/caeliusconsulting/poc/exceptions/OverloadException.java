package exceptions;

public class OverloadException extends ElevatorException {
  private final double attemptedLoadKg;
  private final double maxCapacityKg;

  public OverloadException(String message, double attemptedLoadKg, double maxCapacityKg) {
    super(message);
    this.attemptedLoadKg = attemptedLoadKg;
    this.maxCapacityKg = maxCapacityKg;
  }

  public double getAttemptedKg() {
    return attemptedLoadKg;
  }

  public double getMaxCapacityKg() {
    return maxCapacityKg;
  }
}