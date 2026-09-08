/*Static, all in all means to belong to the class and not to an individual object. 
All objects of the class share this property (attribute or method) and they can be 
accessed / invoked without any object interference */
public class Static {
  Static() {
    count++;
  }
  public static int count = 0;
  public static String title;

  private String status;

  static {
    title = "Static Class";
  }

  public static void remote() {
    System.out.println("Activation of Static class.");
  }

  public void on() {
    status = "on";
    System.out.println("Object on");
  }

  public void off() {
    status = "off";
    System.out.println("Object off");
  }

  public void state() {
    System.out.println(status);
  }
  public static void main(String[] args) {
    remote();
    
    Static s1 = new Static();

    s1.on();
    s1.state();
    s1.off();
    s1.state();

    Static s2 = new Static();
    s2.state();

    System.out.println("Switch count: " + count);
  }
}