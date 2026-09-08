//this references to the current object invoking the method, constructor etc.
public class This {
  protected int point1, point2;

  This() {
    this(0, 0);
  }

  This(int p1, int p2) {
    this.point1 = p1;
    this.point2 = p2;
  }

  private void disp(This cord) {
    System.out.println("(" + point1 + "," + point2 + ")");
  }

  public void scan() {
    disp(this);
  }

  public static void main(String[] args) {
    This c1 = new This();

    c1.scan();
  }
}
