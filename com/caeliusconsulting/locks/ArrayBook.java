import java.util.concurrent.atomic.AtomicReferenceArray;

class SeatBooking {
  private AtomicReferenceArray<Boolean> seats = new AtomicReferenceArray<>(15);

  public SeatBooking() {
    for (int i = 0; i < 15; i++)
      seats.set(i, false);
  }

  public boolean bookSeat() {
    return true;
  }
}

public class ArrayBook {
  public static void main(String[] args) {

  }
}
