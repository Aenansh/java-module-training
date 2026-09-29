public class ITC extends Thread {
  public int total = 0;
  public void run() {
    synchronized (this) {
      for (int i = 1; i <= 10; i++) {
        total += 100;
      }

      this.notify();
    }
  }
  public static void main(String[] args) throws InterruptedException {
    ITC t1 = new ITC();
    ITC t2 = new ITC();

    t1.start();
    // System.out.println("Total earnings: " + t1.total + "Rs");
    synchronized (t1) {
      t1.wait();
      System.out.println("Total earnings: " + t1.total + "Rs");
    }
    t2.start();

  }
}

/*
Object -> Lock: {
      ownerThread: null,
      isLocked: false,
      waitingQueue: []
}
*/