import java.util.concurrent.locks.StampedLock;

class Resource {
  private int value = 0;

  StampedLock lock = new StampedLock();

  public int read() {
    long stamp = lock.tryOptimisticRead();
    int currValue = value;
    try {
      Thread.sleep(1000);
    } catch (Exception e) {
    }

    if (lock.validate(stamp) == false) {
      stamp = lock.readLock();
      try {
        currValue = value;
      } catch (Exception e) {
      } finally {
        lock.unlockRead(stamp);
      }
    }

    System.out.println(Thread.currentThread().getName() + " reads " + currValue);
    return currValue;
  }

  public void write(int newValue) {
    long stamp = lock.writeLock();
    try {
      Thread.sleep(1000);
      value = newValue;
    } catch (Exception e) {
    } finally {
      lock.unlock(stamp);
    }
    System.out.println(Thread.currentThread().getName() + " writes " + newValue);
  }
}

public class StampLock {
  public static void main(String[] args) {
    Resource r = new Resource();
    Thread t1 = new Thread(() -> r.read());
    Thread t2 = new Thread(() -> r.read());
    Thread t3 = new Thread(() -> r.read());

    Thread w1 = new Thread(() -> r.write(1));
    Thread w2 = new Thread(() -> r.write(2));

    t1.start(); 
    t2.start();
    t3.start();

    w1.start();
    w2.start();
  }
}
