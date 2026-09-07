public class Final {
  protected final int enrNo = 231; // can't be edited

  public final void mentionEnrollment() { // can't be overidden
    System.out.println(enrNo);
  }

  public static final class Desk { // can't be extended / inherited
    public void location() {
      System.out.println("Row 4, Column 2");
    }
  }

  public static void main(String[] args) {
    Final emp = new Final();

    emp.mentionEnrollment();

    Final.Desk d1 = new Final.Desk();
    d1.location();
  }
}
