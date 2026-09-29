public class PriorityDemo extends Thread {
  @Override
  public void run() {
    System.out.println("Task running..." + this.getPriority());
  }
  public static void main(String[] args) {
    PriorityDemo t1 = new PriorityDemo();
    currentThread().setPriority(10);
    System.out.println(currentThread().getPriority());
    t1.setPriority(1);
    t1.start();
  }
}
