package interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Persistence<Api, IdType> {

  public String connection = "";

  //Create operations
  Api save(Api entity);

  List<Api> saveAll(Collection<Api> entities);

  //Read operations
  Optional<Api> findById(IdType id);

  List<Api> findAll();

  boolean existsById(IdType id);

  long count();

  //Update operations
  Api updateById(IdType id);

  Api update(String cond);

  //Delete operations
  void deleteById(IdType id);

  void deleteAll();
}
