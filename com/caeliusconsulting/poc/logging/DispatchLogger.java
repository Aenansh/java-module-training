package logging;

public class DispatchLogger {
  private final StringBuilder log = new StringBuilder();
  private int eventCount = 0;

  public void log(String event) {
    eventCount++;
    log.append('[').append(eventCount).append(']').append(event).append(System.lineSeparator());
  }

  public String getFullLog() {
    return log.toString();
  }

  public int getEventCount() {
    return eventCount;
  }

  public void printLog() {
    System.out.print(log);
  }

  public void clear() {
    log.setLength(0);
    eventCount = 0;
  }
}