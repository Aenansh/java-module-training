import java.util.concurrent.atomic.AtomicReference;

class SeatBooking {
  private AtomicReference<String> seat = new AtomicReference<String>("EMPTY");

  public boolean bookSeat(String name) {
    if (seat.get().equals("EMPTY") == false) {
      return false;
    }
    return seat.compareAndSet("EMPTY", name);
  }
}

public class AtomRef {
  public static void main(String[] args) {
    SeatBooking sb = new SeatBooking();

    Thread b1 = new Thread(() -> {
      if (sb.bookSeat("Aenansh")) {
        System.out.println("Seat booked by Aenansh.");
      } else {
        System.out.println("Seat is already booked by someone else, Aenansh can't book it.");
      }
    });

    Thread b2 = new Thread(() -> {
      if (sb.bookSeat("Nikshit")) {
        System.out.println("Seat booked by Nikshit.");
      } else {
        System.out.println("Seat is already booked by someone else, Nikshit can't book it.");
      }
    });

    b1.start();
    b2.start();
  }
}
