package logic;
import com.sendgrid.*;
import java.io.IOException;

public class Message {
	
  private static final String EMAIL = "SmartParking@emailservice.com";
  
  private static final String API_KEY = "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";
  	
  public static void sendEmail(String userEmail, String subject, String message) throws IOException {
    Email from = new Email(EMAIL);
    Email to = new Email(userEmail);
    Content content = new Content("text/plain", message);
    Mail mail = new Mail(from, subject, to, content);
    Request request = new Request();
    request.method = Method.POST;
    request.endpoint = "mail/send";
    request.body = mail.build();
    new SendGrid(API_KEY).api(request);
  }
}