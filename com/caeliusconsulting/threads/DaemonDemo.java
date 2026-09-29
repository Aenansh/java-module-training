public class DaemonDemo extends Thread {
  @Override
  public void run() {
    if (this.isDaemon()) {
      System.out.println(this.getName() + " Daemon thread is running...");
    }
    else {
      System.out.println(this.getName() + " Child thread is running...");
    }
  }

  public static void main(String[] args) {
    System.out.println("Main thread");
    DaemonDemo d1 = new DaemonDemo();
    d1.setDaemon(true);
    d1.start();
  }
}
