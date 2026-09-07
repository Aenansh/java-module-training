package abstraction;

import java.util.Arrays;
import java.util.List;

abstract class NotificationService {
  protected final String senderName;

  public NotificationService(String senderName) {
    this.senderName = senderName;
  }

  abstract public void send(List<String> recipient, String content);

  protected String formatContent(String text) {
    System.out.println("Formatted your message text.");
    return text;
  }
}

class EmailService extends NotificationService {
  private final String smtpHost;
  private final int smtpPort;

  EmailService(String senderName, String smtpHost, int smtpPort) {
    super(senderName);
    this.smtpHost = smtpHost;
    this.smtpPort = smtpPort;
  }

  @Override
  public void send(List<String> recipient, String content) {
    this.formatContent(content);
    System.out.println("Sending email to all recipients: " + recipient);
    System.out.println("Your SMTP Host and PORT are " + smtpHost + " " + smtpPort);
  }
}

class PushService extends NotificationService {

  PushService(String senderName) {
    super(senderName);
  }

  @Override
  public void send(List<String> recipient, String content) {
    this.formatContent(content);
    System.out.println("Sending push notifications to all recipients: " + recipient);
  }
}

public class NotificationAbstraction {
  public static void main(String[] args) {
    NotificationService em = new EmailService("Aenansh", "gmail", 663);
    em.send(Arrays.asList("nikshit@gmail.com", "shruti@gmail.com"), "Hello email");

    NotificationService pn = new PushService("Aenansh");
    pn.send(Arrays.asList("9897628409", "9816303837"), "Hello message");
  }
}
