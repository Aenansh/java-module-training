package interfaces;

import java.time.Duration;
import java.util.Optional;

public interface Cache<K, V> {
  void connect();
  
  public Optional<V> get(K key);

  public void set(K key, V value, Duration ttl);

  boolean evict(K key);

}
