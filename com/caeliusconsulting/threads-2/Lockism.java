import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ResourceStore {
  private Lock lock = new ReentrantLock(true);
  private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
  private Lock readLock = rwLock.readLock();
  private Lock writeLock = rwLock.writeLock();

  private final String book = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  public void readBook() {
    lock.lock();
    try {
      System.out.println(Thread.currentThread().getName() + " is reading the book: " + book);
      Thread.sleep(4000);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    } finally {
      lock.unlock();
    }
  }

  public void readTogether() {
    readLock.lock();
    try {
      System.out.println(Thread.currentThread().getName() + " is reading the book: " + book);
      Thread.sleep(4000);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    } finally {
      readLock.unlock();
    }
  }
}

public class Lockism {
  public static void main(String[] args) {
    ResourceStore rs = new ResourceStore();
    Thread t1 = new Thread(() -> rs.readTogether());
    Thread t2 = new Thread(() -> rs.readTogether());

    t1.start();
    t2.start();
  }
}
