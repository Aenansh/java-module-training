class Task extends Thread {
  public static Thread main;
  @Override
  public void run() {
    try {
      main.join();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    for (int i = 0; i <= 5; i++) {
      System.out.println(getName() + " is running..." + i);
      try {
        sleep(1000);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }
}

public class JoinDemo extends Thread {
  public static void main(String[] args) {
    Task.main = Thread.currentThread();
    Task t1 = new Task();
    
    t1.start();
    try {
      t1.join();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    for (int i = 0; i <= 5; i++) {
      System.out.println(currentThread().getName() + " is running..." + i);
      try {
        sleep(1000);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }
}
