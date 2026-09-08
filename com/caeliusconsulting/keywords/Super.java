/* Super key word is used to address or call the immediate parent's 
property like attribute, method or constructor. When not explicitly mentioned, 
a child class object's constructor will always call parent's default / implicit 
constructor. */
class Captain {
  Captain() {
    System.out.println("Captain contacted");
  }
}

class ChiefOfficer extends Captain {
  ChiefOfficer() {
    System.out.println("Chief Officer contacted");
  }

  ChiefOfficer(String exp) {
    System.out.println("Chief Officer is " + exp);
  }
}

class Cadet extends ChiefOfficer {
  Cadet() {
    super("smiling");
    System.out.println("Cadet contacted");
  }

  public void salute() {
    System.out.println("Salute!");
  }
}

public class Super {
  public static void main(String[] args) {
    Cadet c = new Cadet();

    c.salute();
  }
}
