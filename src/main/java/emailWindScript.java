import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;

public class emailWindScript {
    public static void main(String[] args) {
        int randomNumber = emailWindScript.generateRandomNumber(100000, 999999);

        // Sende die Zahl per E-Mail
        emailWindScript.sendEmail("sl3mey@gmail.com", "sl3mey@icloud.com", "Auth Code", "Your authentification code is" + randomNumber + "\nDon't give anyone this code");

    }
    // Methode zur Generierung einer zufälligen Zahl
    public static int generateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
    // Methode zum Senden einer E-Mail
    public static void sendEmail(String fromEmail, String toEmail, String subject, String body) {
        // SMTP-Server-Konfiguration
        String host = "smtp.gmail.com";
        int port = 465;
        String username = "sl3mey@gmail.com";
        String password = "LuxXenburg77/!&";

        // Erstellen der Eigenschaften
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Erstellen der Sitzung
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Erstellen der Nachricht
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("your_email@example.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("recipient@example.com"));
            message.setSubject("Test-E-Mail");
            message.setText("Dies ist eine Test-E-Mail von meinem SMTP-Client.");

            // Senden der Nachricht
            Transport.send(message);
            System.out.println("E-Mail wurde erfolgreich gesendet.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Senden der E-Mail: " + e.getMessage());
        }
    }
}
