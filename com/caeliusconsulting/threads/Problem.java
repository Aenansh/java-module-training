class Threads extends Thread {
  public Threads(Runnable r) {
    super(r);
  }
}

public class Problem {
  volatile Object resource = null;
  volatile boolean flag = false;

  synchronized public void consumer() throws InterruptedException {
    while (flag != true) {
      wait();
    }
    System.out.println("Consumer consumed resource: " + resource);
    resource = null;
    flag = false;
    notify();
  }

  synchronized public void producer(Object i) throws InterruptedException {
    while (flag == true) {
      wait();
    }
    resource = i;
    flag = true;
    System.out.println("Producer produced resource: " + resource);
    notify();
  }

  public static void main(String[] args) {
    Problem p = new Problem();
    Threads t1 = new Threads(() -> {
      for (int i = 1; i <= 20; i++) {
        try {
          p.producer(i);
        } catch (Exception e) {
        }
      }
    });
    Threads t2 = new Threads(() -> {
      try {
        for (int i = 1; i <= 20; i++) {
          p.consumer();
        }
      } catch (Exception e) {
      }
    });

    t1.start();
    t2.start();
  }
}
