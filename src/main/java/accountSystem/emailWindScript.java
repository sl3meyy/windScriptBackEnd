package accountSystem;

import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;

public class emailWindScript {
    public static void main(String[] args) {
        int randomNumber = emailWindScript.generateRandomNumber(100000, 999999);

        // Sende die Zahl per E-Mail
        emailWindScript.sendEmail("sl3mey@icloud.com", "Auth Code", "Your authentication code is \n\n" + randomNumber + "\n\nDon't give anyone this code");
    }

    // Methode zur Generierung einer zufälligen Zahl
    public static int generateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    // Methode zum Senden einer E-Mail
    public static void sendEmail(String receiver, String subject, String body) {
        final String senderEmail = "sl3mey@gmail.com";
        final String senderPassword = "vvaq itwc hemu gcvv"; // Ersetzen Sie dies mit Ihrem tatsächlichen Passwort
        final String recipientEmail = receiver;


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
            System.out.println("E-Mail has been sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
