class BookTheatreSeat {
  private static int totalSeats = 10;

  public synchronized void bookSeat(int seatCount, String user) {
    if (seatCount > totalSeats) {
      System.out.println("No seats left for " + user + ".");
    } else {
      System.out.println("Booked " + seatCount + " seats for " + user + ".");
      totalSeats -= seatCount;
    }
    System.out.println("Total seats left: " + totalSeats);
  }
}

class Request1 extends Thread {
  private BookTheatreSeat booker;
  private int seats;

  public Request1(int seats, BookTheatreSeat b) {
    booker = b;
    this.seats = seats;
  }

  @Override
  public void run() {
    booker.bookSeat(seats, getName());
  }
}
class Request2 extends Thread {
  private BookTheatreSeat booker;
  private int seats;

  public Request2(int seats, BookTheatreSeat b) {
    booker = b;
    this.seats = seats;
  }

  @Override
  public void run() {
    booker.bookSeat(seats, getName());
  }
}

public class MovieBookingApp {
  public static void main(String[] args) {
    BookTheatreSeat booker = new BookTheatreSeat();
    Request1 t1 = new Request1(4, booker);
    Request2 t2 = new Request2(7, booker);

    BookTheatreSeat booker2 = new BookTheatreSeat();
    Request1 t3 = new Request1(7, booker2);
    Request2 t4 = new Request2(8, booker2);  

    t1.start();
    t2.start();
    t3.start();
    t4.start();
  }
}
