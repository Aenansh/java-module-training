package interfaces;

import java.util.Arrays;
import java.util.List;

class Kafka implements EventPublisher<String> {
  @Override
  public void publish(String topic, String event) {
    System.out.println("Event published to the Kafka stream.");
    System.out.println("Event topic: " + topic + " Event: " + event);
  }

  @Override
  public void publishBatch(String topic, List<?> events) {
    for (Object event : events) {
      System.out.println("Event: " + event + " published to the Kafka stream.");
      System.out.println(event + " topic: " + topic);
    }
  }
}

class RabbitMQ implements EventPublisher<Integer> {
  @Override
  public void publish(String topic, Integer event) {
    System.out.println("Event published to the RabbitMQ stream.");
    System.out.println("Event topic: " + topic + " Event ID: " + event);
  }

  @Override
  public void publishBatch(String topic, List<?> events) {
    for (Object event : events) {
      System.out.println("Event ID: " + event + " published to the RabbitMQ stream.");
      System.out.println(event + " topic: " + topic);
    }
  }
}

public class EventClass {
  public static void main(String[] args) {
    Kafka kafka = new Kafka();
    kafka.publish("user-events", "User registered");
    kafka.publishBatch(
        "user-events",
        Arrays.asList("User registered", "User updated", "User deleted"));

    System.out.println();

    RabbitMQ rabbitMQ = new RabbitMQ();
    rabbitMQ.publish("order-events", 1001);
    rabbitMQ.publishBatch("order-events", Arrays.asList(1001, 1002, 1003));
  }
}
