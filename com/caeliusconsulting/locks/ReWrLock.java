import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Resource {
  private int value = 0;
  ReadWriteLock rwLock = new ReentrantReadWriteLock();
  Lock readLock = rwLock.readLock();
  Lock writeLock = rwLock.writeLock();

  public int read() {
    readLock.lock();
    try {
      Thread.sleep(1000);
      return value;
    } catch (Exception e) {
      return -1;
    } finally {
      readLock.unlock();
    }
  }

  public void write(int newValue) {
    writeLock.lock();
    try {
      Thread.sleep(2000);
      value = newValue;
    } catch (Exception e) {
      return;
    } finally {
      writeLock.unlock();
    }
  }
}

public class ReWrLock {
  public static void main(String[] args) {
    Resource r = new Resource();
    Thread t1 = new Thread(() -> System.out.println(Thread.currentThread().getName() + " reads " + r.read()));
    Thread t2 = new Thread(() -> System.out.println(Thread.currentThread().getName() + " reads " + r.read()));
    Thread t3 = new Thread(() -> System.out.println(Thread.currentThread().getName() + " reads " + r.read()));

    Thread w1 = new Thread(() -> r.write(1));
    Thread w2 = new Thread(() -> r.write(2));

    t1.start();
    t2.start();
    t3.start();

    w1.start();
    w2.start();
  }
}