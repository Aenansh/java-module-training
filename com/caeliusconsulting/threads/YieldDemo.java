public class YieldDemo extends Thread {
  @Override
  public void run() {
    Thread.yield();
    for (int i = 0; i <= 5; i++) {
      System.out.println(this.getName() + " is running..." + i);
    }
  }

  public static void main(String[] args) {
    YieldDemo t1 = new YieldDemo();
    t1.start();
    for (int i = 0; i <= 5; i++) {
      System.out.println(currentThread().getName() + " is running..." + i);
    }
  }
}
