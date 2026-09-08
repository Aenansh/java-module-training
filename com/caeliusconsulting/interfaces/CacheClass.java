package com.caeliusconsulting.interfaces;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

class Redis implements Cache<String, String> {
  private final String port = "6379";
  private final String host = "redis";

  public void connect() {
    System.out.println("Redis connected on host " + host + " and port " + port);
  }

  public Optional<String> get(String key) {
    int value = ThreadLocalRandom.current().nextInt(2);
    if (value == 1) {
      System.out.println("Key found in Redis.");
      return Optional.of("data");
    }
    System.out.println("No key found in Redis.");
    return Optional.empty();
  }

  public void set(String key, String value, Duration ttl) {
    System.out.println("Setting [" + key + "] : " + value + " for duration: " + ttl);
  }

  public boolean evict(String key) {
    int value = ThreadLocalRandom.current().nextInt(2);
    if (value == 1) {
      System.out.println("Key evicted in Redis.");
      return true;
    }
    System.out.println("No key found in Redis to evict.");
    return false;
  }
}

class Memcached implements Cache<Integer, String> {
  private final String port = "11211";
  private final String host = "localhost";

  public void connect() {
    System.out.println("Memcached connected on host " + host + " and port " + port);
  }

  public Optional<String> get(Integer key) {
    int value = ThreadLocalRandom.current().nextInt(2);
    if (value == 1) {
      System.out.println("Key found in Memcached.");
      return Optional.of("data");
    }
    System.out.println("No key found in Memcached.");
    return Optional.empty();
  }

  public void set(Integer key, String value, Duration ttl) {
    System.out.println("Setting [" + key + "] : " + value + " for duration: " + ttl);
  }

  public boolean evict(Integer key) {
    int value = ThreadLocalRandom.current().nextInt(2);
    if (value == 1) {
      System.out.println("Key evicted in Memcached.");
      return true;
    }
    System.out.println("No key found in Memcached to evict.");
    return false;
  }
}

public class CacheClass {
  public static void main(String[] args) {
    Cache<String, String> redis = new Redis();
    redis.connect();
    redis.set("user:1", "Aenansh", Duration.ofMinutes(10));
    System.out.println(redis.get("user:1"));
    redis.evict("user:1");

    System.out.println();

    Cache<Integer, String> memcached = new Memcached();
    memcached.connect();
    memcached.set(101, "Aenansh", Duration.ofMinutes(10));
    System.out.println(memcached.get(101));
    memcached.evict(101);
  }

}
