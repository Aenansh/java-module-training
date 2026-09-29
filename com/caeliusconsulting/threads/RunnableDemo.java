public class RunnableDemo implements Runnable {
  @Override 
  public void run() {
    System.out.println("Thread running...");
  }

  public static void main(String[] args) {
    RunnableDemo t1 = new RunnableDemo();
    Thread th = new Thread(t1);
    th.start();
    th.start();
  }
}
