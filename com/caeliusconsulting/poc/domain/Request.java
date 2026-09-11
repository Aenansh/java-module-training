package domain;

public final class Request {
  private final int floor;
  private final Direction direction;
  private final double estimatedLoadKg;
  private final long timestamp;

  private Request(Builder builder) {
    this.floor = builder.floor;
    this.direction = builder.direction;
    this.estimatedLoadKg = builder.estimatedLoadKg;
    this.timestamp = builder.timestamp;
  }

  public int getFloor() {
    return floor;
  }

  public Direction getDirection() {
    return direction;
  }

  public double getEstimatedLoadKg() {
    return estimatedLoadKg;
  }

  public long getTimestamp() {
    return timestamp;
  }

  @Override
  public String toString() {
    return "Request{floor=" + floor + ", direction=" + direction
        + ", loadKg=" + estimatedLoadKg + ", ts=" + timestamp + "}";
  }

  public static class Builder {
    private int floor;
    private Direction direction = Direction.IDLE;
    private double estimatedLoadKg = 0.0;
    private long timestamp = System.currentTimeMillis();

    public Builder floor(int floor) {
      this.floor = floor;
      return this;
    }

    public Builder direction(Direction direction) {
      this.direction = direction;
      return this;
    }

    public Builder estimatedLoadKg(double kg) {
      this.estimatedLoadKg = kg;
      return this;
    }

    public Request build() {
      return new Request(this);
    }
  }
}