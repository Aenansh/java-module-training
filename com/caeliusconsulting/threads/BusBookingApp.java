class BusBooking {
  public static int totalSeats = 10;

  public static synchronized void bookSeat(int seatCount, String user) {
    System.out.println("Seats left " + totalSeats);
    if (seatCount > totalSeats) {
      System.out.println("Not enough seats left for " + user);
      return;
    }
    System.out.println("User " + user + " booked a seat!");
    BusBooking.totalSeats -= seatCount;
  }

  public void bookSeat(String user, int seatCount) {
    System.out.println("Seats left " + totalSeats);
    synchronized (this) {
    if (seatCount > totalSeats) {
      System.out.println("Not enough seats left for " + user);
      return;
    }
      System.out.println("User " + user + " booked a seat!");
      BusBooking.totalSeats -= seatCount;
    }
  }
}

public class BusBookingApp extends Thread {
  private static BusBooking b;
  private int seats;

  public BusBookingApp(int seats) {
    b = new BusBooking();
    this.seats = seats;
  }

  @Override
  public void run() {
    b.bookSeat(this.getName(), this.seats);
  }

  public static void main(String[] args) throws InterruptedException {
    BusBookingApp t1 = new BusBookingApp(1);
    BusBookingApp t2 = new BusBookingApp(3);
    BusBookingApp t3 = new BusBookingApp(7);

    t1.setName("Aenansh");
    t2.setName("Vahinee");
    t3.setName("Vasu");
    t1.start();
    t2.start();
    t3.start();

    Thread.sleep(1000);
    System.out.println(BusBooking.totalSeats);
  }
}
