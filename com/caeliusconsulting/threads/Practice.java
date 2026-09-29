import java.util.LinkedList;
import java.util.List;

class BoundedBuffer {
  private List<Integer> buffer;
  private final int cap;

  public BoundedBuffer(int cap) {
    this.cap = cap;
    buffer = new LinkedList<Integer>();
  }

  public synchronized void producer(Integer item) throws InterruptedException {
    while (buffer.size() == cap) {
      wait();
    }
    buffer.addLast(item);
    System.out.println("Producer produced " + item);
    Thread.sleep(100);
    notifyAll();
  }

  public synchronized Object consumer() throws InterruptedException {
    while (buffer.isEmpty()) {
      wait();
    }
    Object consumed = buffer.removeFirst();
    System.out.println("Consumer consumed " + consumed);
    Thread.sleep(100);
    notifyAll();  

    return consumed;
  }
}

public class Practice {
  public static void main(String[] args) {
    BoundedBuffer b = new BoundedBuffer(10);
    Thread t1 = new Thread(() -> {
      for (int i = 1; i <= 10; i++) {
        try {
          b.producer(i);
        } catch (Exception e) {
          System.out.println("Error in producing " + i + " item. " + e.getMessage());
        }
      }
    });
    Thread t2 = new Thread(() -> {
      Object item = null;
      while (!Thread.currentThread().isInterrupted()) {
        try {
          item = b.consumer();
        } catch (Exception e) {
          System.out.println("Error in consuming " + item + " item. " + e.getMessage());
        }
      }
    });

    t1.start();
    t2.start();
  }
}
