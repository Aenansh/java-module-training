package interfaces;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

class UserApi {
}

class UserDatabase implements Persistence<UserApi, Integer> {

  @Override
  public UserApi save(UserApi entity) {
    System.out.println("save called with entity: " + entity);
    return null;
  }

  @Override
  public List<UserApi> saveAll(Collection<UserApi> entities) {
    System.out.println("saveAll called with entities: " + entities);
    return null;
  }

  @Override
  public Optional<UserApi> findById(Integer id) {
    System.out.println("findById called with id: " + id);
    return Optional.empty();
  }

  @Override
  public List<UserApi> findAll() {
    System.out.println("findAll called");
    return null;
  }

  @Override
  public boolean existsById(Integer id) {
    System.out.println("existsById called with id: " + id);
    return false;
  }

  @Override
  public long count() {
    System.out.println("count called");
    return 0;
  }

  @Override
  public UserApi updateById(Integer id) {
    System.out.println("updateById called with id: " + id);
    return null;
  }

  @Override
  public UserApi update(String cond) {
    System.out.println("update called with condition: " + cond);
    return null;
  }

  @Override
  public void deleteById(Integer id) {
    System.out.println("deleteById called with id: " + id);
  }

  @Override
  public void deleteAll() {
    System.out.println("deleteAll called");
  }
}

public class MainClass {
  public static void main(String[] args) {
    UserDatabase database = new UserDatabase();
    UserApi user = new UserApi();
    List<UserApi> users = Arrays.asList(user, new UserApi());

    database.save(user);
    database.saveAll(users);

    database.findById(1);
    database.findAll();
    database.existsById(1);
    database.count();

    database.updateById(1);
    database.update("name = 'John'");

    database.deleteById(1);
    database.deleteAll();
  }
}
