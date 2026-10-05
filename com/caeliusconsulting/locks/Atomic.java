import java.util.concurrent.atomic.AtomicInteger;

class Resource {
  private AtomicInteger value = new AtomicInteger(0);

  public void increment() {
    value.incrementAndGet();
  }

  public AtomicInteger getValue() {
    return value;
  }
}

public class Atomic {
  public static void main(String[] args) {
    Resource r = new Resource();

    Thread t1 = new Thread(() -> {
      for (int i = 0; i < 1000; i++) {
        r.increment();
      }
    });

    Thread t2 = new Thread(() -> {
      for (int i = 0; i < 1000; i++) {
        r.increment();
      }
    });

    t1.start();
    t2.start();

    try {
      Thread.sleep(2000);
    } catch (Exception e) {
    }

    System.out.println(r.getValue());
  }
}
