public class Interrupt extends Thread {
  @Override
  public void run() {
    try {
      for (int i = 0; i <= 5; i++) {
        System.out.println(this.isInterrupted());
        System.out.println(getName() + " is running..." + i);
        Thread.sleep(1000);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public static void main(String[] args) {
    Interrupt t1 = new Interrupt();
    t1.setName("T1");

    t1.start();
    System.out.println(t1.isInterrupted());

    t1.interrupt();

    System.out.println(t1.isInterrupted());

    try {
      for (int i = 0; i <= 5; i++) {
        System.out.println(currentThread().getName() + " is running..." + i);
        Thread.sleep(1000);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
