import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

class LikeCounter {
  // private AtomicReference<Integer> likeCount = new AtomicReference<>(0);
  private AtomicInteger likeCount = new AtomicInteger(0);

  public void like(String name) {
    likeCount.incrementAndGet();
    // while (true) {
    // Integer currentCount = likeCount.get();
    // Integer finalCount = currentCount + 1;

    // if (likeCount.compareAndSet(currentCount, finalCount)) {
    System.out.println("Liked by: " + name + ". Like Count: " + likeCount);
    // return;
    // }

    // System.out.println(name + " is retrying to like...");
    // }
    // }
  }
}

public class Retry {
  public static void main(String[] args) {
    LikeCounter lc = new LikeCounter();
    Thread t1 = new Thread(() -> lc.like("Aenansh"));
    Thread t2 = new Thread(() -> lc.like("Nikshit"));

    t1.start();
    t2.start();
  }
}