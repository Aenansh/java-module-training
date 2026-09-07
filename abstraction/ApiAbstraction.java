package abstraction;

abstract class Api {
  static public String version = "v1";

  abstract public String get();

  abstract public void post(String data);

  abstract public void put(String up, String ref);

  abstract public void patch(String up, String ref);

  abstract public void delete(String ref);
}

class UserApi extends Api {
  public String get() {
    String data = "{Name: some_name, Id: 1}";
    return data;
  }

  public void post(String data) {
    System.out.println("Uploaded data successfully!.");
  }

  public void put(String up, String ref) {
    System.out.println("Replaced data at " + ref);
  }

  public void patch(String up, String ref) {
    System.out.println("Modified data at " + ref);
  }

  public void delete(String ref) {
    System.out.println("Deleted data at " + ref);
  }
}

public class ApiAbstraction {
  public static void main(String[] args) {
    Api service = new UserApi();

    service.get();
    service.post("{Name: Aenansh, Id: 2}");
    service.put("{Name: Vasu, Id: 3}", "Id: 2");
    service.patch("Name: Vasu", "Id: 2");
    service.delete("Id: 1");
  }
}
