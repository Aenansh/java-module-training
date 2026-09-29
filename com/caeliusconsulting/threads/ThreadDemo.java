class Task1 extends Thread {
  @Override 
  public void run() {
    System.out.println(this.isAlive());
    System.out.println("Running task 1..." + this.getName());
  }
}
class Task2 extends Thread {
  @Override 
  public void run() {
    System.out.println("Running task 2..." + this.getName());
  }
}
class Task3 extends Thread {
  @Override 
  public void run() {
    System.out.println("Running task 3..." + this.getName());
  }
}

public class ThreadDemo {
 public static void main(String[] args) {
   Task1 t1 = new Task1();
   Task1 t11 = new Task1();
   Task2 t2 = new Task2();
   Task3 t3 = new Task3();
   
   
   Thread.currentThread().setName("Aenansh-main");
   t1.start();
   t2.start();
    t11.start();
   t3.start();
   try {
     Thread.sleep(1000);
   } catch (Exception e) {
    System.out.println(e.getMessage());
   }
   System.out.println(t1.isAlive());
 }
}