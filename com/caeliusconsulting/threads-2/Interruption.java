class Worker extends Thread {
  @Override
  public void run() {
    try {
      System.out.println("Worker starts working..." + this.isInterrupted());
      for (int i = 0; i < 10; i++) {
        System.out.println("Working...");
        // Thread.sleep(100);
        synchronized (this) {
          this.wait();
        }
      }
      System.out.println("Worker stops working..." + this.isInterrupted());
    } catch (Exception e) {
      System.out.println("Work interrupted!" + e.getMessage());
    }
  }
}

/**
 * Interruption
 */
public class Interruption {
  public static void main(String[] args) throws InterruptedException {
    Worker w = new Worker();
    w.start();
    // Thread.yield();
    synchronized (w) {
      w.notifyAll();
    }
    w.interrupt();
    // Thread.sleep(100);
  }
}