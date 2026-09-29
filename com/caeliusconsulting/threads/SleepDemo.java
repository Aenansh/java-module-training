public class SleepDemo {
  public void timer() {
    int j = 0;
    for (j = 0; j <= 3; j++) {
      for (int i = 0; i <= 59; i++) {
        try {
          Thread.sleep(1000);
          System.out.println(j + ":" + (i < 10 ? "0" : "") + i);
        } catch (InterruptedException e) {
          System.out.println(e.getMessage());
        }
      }
    }
    try {
      Thread.sleep(1000);
      System.out.println(j + ":00");
    } catch (InterruptedException e) {
      System.out.println(e.getMessage());
    }
  }

  public static void main(String[] args) {

  }
}
