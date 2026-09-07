package abstraction;

import java.util.List;
import java.util.ArrayList;

abstract class PaymentProcessor {
  String DEFAULT_CURRENCY = "USD";
  int DEFAULT_TIMEOUT_SECONDS = 30;
  int MAX_RETRY_ATTEMPTS = 3;

  abstract public String processPayment(double amount);

  abstract public String refund(String key);

  public List<String> transactionHistory() {
    System.out.println("Your PAYTM transaction history:");
    return new ArrayList<String>();
  };
}

class PaytmProcessor extends PaymentProcessor {
  public String processPayment(double amount) {
    System.out.println("Made a PAYTM payment of " + amount);
    return "success";
  }

  public String refund(String key) {
    System.out.println("Refunded PAYTM transaction " + key);
    return "success";
  }
}

class CardProcessor extends PaymentProcessor {
  public String processPayment(double amount) {
    System.out.println("Made a CREDIT CARD payment of " + amount);
    return "success";
  }

  public String refund(String key) {
    System.out.println("Refunded CREDIT CARD transaction " + key);
    return "success";
  }
}

public class PaymentAbstraction {
  public static void main(String[] args) {
    PaymentProcessor method1 = new PaytmProcessor();

    method1.processPayment(10000);
    method1.refund("A1B0");
    method1.transactionHistory();

    PaymentProcessor method2 = new CardProcessor();
    method2.processPayment(81050);
    method2.refund("K2C5");
    method2.transactionHistory();
  }
}
