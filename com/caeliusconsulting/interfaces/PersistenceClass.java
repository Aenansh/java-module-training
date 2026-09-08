package com.caeliusconsulting.interfaces;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

class UserApi {
}

class ProductApi {
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

class ProductDatabase implements Persistence<ProductApi, String> {
  @Override
  public ProductApi save(ProductApi entity) {
    System.out.println("Product save called with entity: " + entity);
    return null;
  }

  @Override
  public List<ProductApi> saveAll(Collection<ProductApi> entities) {
    System.out.println("Product saveAll called with entities: " + entities);
    return null;
  }

  @Override
  public Optional<ProductApi> findById(String id) {
    System.out.println("Product findById called with id: " + id);
    return Optional.empty();
  }

  @Override
  public List<ProductApi> findAll() {
    System.out.println("Product findAll called");
    return null;
  }

  @Override
  public boolean existsById(String id) {
    System.out.println("Product existsById called with id: " + id);
    return false;
  }

  @Override
  public long count() {
    System.out.println("Product count called");
    return 0;
  }

  @Override
  public ProductApi updateById(String id) {
    System.out.println("Product updateById called with id: " + id);
    return null;
  }

  @Override
  public ProductApi update(String condition) {
    System.out.println("Product update called with condition: " + condition);
    return null;
  }

  @Override
  public void deleteById(String id) {
    System.out.println("Product deleteById called with id: " + id);
  }

  @Override
  public void deleteAll() {
    System.out.println("Product deleteAll called");
  }
}

public class PersistenceClass {
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
    database.update("name = 'Aenansh'");

    database.deleteById(1);
    database.deleteAll();

    ProductDatabase database2 = new ProductDatabase();
    ProductApi product = new ProductApi();
    List<ProductApi> products = Arrays.asList(product, new ProductApi());

    database2.save(product);
    database2.saveAll(products);

    database2.findById("product-1");
    database2.findAll();
    database2.existsById("product-1");
    database2.count();

    database2.updateById("product-1");
    database2.update("price > 100");

    database2.deleteById("product-1");
    database2.deleteAll();
  }
}
