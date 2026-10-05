import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Resource {
  private Lock lock = new ReentrantLock(true);

  public void f1() {
    try {
      lock.lock();
      System.out.println(Thread.currentThread().getName() + " entered f1.");
      try {
        Thread.sleep(2000);
      } catch (Exception e) {
      }
      System.out.println(Thread.currentThread().getName() + " exited f1.");
    } catch (Exception e) {
      System.out.println(e.getMessage());
    } finally {
      lock.unlock();
    }
  }

  public void f2() {
    if (lock.tryLock()) {
      try {
        System.out.println(Thread.currentThread().getName() + " entered f2.");
        Thread.sleep(2000);
        System.out.println(Thread.currentThread().getName() + " exited f2.");
      } catch (Exception e) {
        System.out.println(e.getMessage());
      } finally {
        lock.unlock();
      }
    } else {
      System.out.println(Thread.currentThread().getName() + " is waiting.");
      f1();
    }
  }
}

public class ReentLock {
  public static void main(String[] args) {
    Resource r1 = new Resource();
    // Resource r2 = new Resource();
    // Resource r3 = new Resource();
    // Thread t1 = new Thread(() -> r1.f1());
    // Thread t2 = new Thread(() -> r2.f1());
    // Thread t3 = new Thread(() -> r3.f1());

    Thread t1 = new Thread(() -> r1.f2());
    Thread t2 = new Thread(() -> r1.f2());
    Thread t3 = new Thread(() -> r1.f2());

    t1.start();
    t2.start();
    t3.start();
  }
}
