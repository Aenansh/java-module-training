import java.util.Arrays;

public class StringDemo {
  public static void demonstrateStringImmutability() {
    String base = "Car-";
    String result = base + "P1";
    System.out.println("Original base unchanged: " + base);
    System.out.println("New concatenated object: " + result);
    System.out.println("Same reference? " + (base == result));

    System.out.println("substring(0,3): " + result.substring(0, 3));
    System.out.println("equalsIgnoreCase('car-p1'): " + result.equalsIgnoreCase("car-p1"));
    System.out.println("split('-'): " + Arrays.toString(result.split("-")));
  }

  public static void demonstrateStringBuilderPerformance() {
    int iterations = 20000;

    long start = System.nanoTime();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < iterations; i++) {
      sb.append("Car-").append(i).append(";");
    }
    long sbTimeNs = System.nanoTime() - start;

    start = System.nanoTime();
    String s = "";
    for (int i = 0; i < iterations; i++) {
      s = s + "Car-" + i + ";";
    }
    long strTimeNs = System.nanoTime() - start;

    System.out.println("StringBuilder (" + iterations + " appends): " + sbTimeNs + " ns");
    System.out.println("String concatenation (" + iterations + " appends): " + strTimeNs + " ns");
    System.out.println("StringBuilder wins because it mutates a single buffer instead of "
        + "reallocating a new immutable String object on every iteration.");
  }

  public static synchronized void demonstrateStringBufferUseCase() {
      StringBuffer sharedLog = new StringBuffer();
    sharedLog.append("[Thread-Safe Log] ")
        .append("Maintenance check started. ")
        .append("StringBuffer synchronizes its methods internally, ")
        .append("making concurrent appends from multiple threads safe.");
    System.out.println(sharedLog.toString());
    }
}
