import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class test {

    public static void main(String[] args) {
        final String senderEmail = "sl3mey@gmail.com";
        final String senderPassword = "vvaq itwc hemu gcvv"; // Ersetzen Sie dies mit Ihrem tatsächlichen Passwort
        final String recipientEmail = "sl3mey@icloud.com";
        final String subject = "Test-E-Mail von Java";
        final String body = "Dies ist eine Test-E-Mail, die mithilfe von Java und Googles SMTP gesendet wurde.";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, senderPassword);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("E-Mail erfolgreich gesendet!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
