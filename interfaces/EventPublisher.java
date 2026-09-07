package interfaces;

import java.util.List;

public interface EventPublisher<EventType> {
  void publish(String topic, EventType event);

  void publishBatch(String topic, List<?> events);
}
