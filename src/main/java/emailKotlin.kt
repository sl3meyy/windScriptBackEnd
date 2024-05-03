import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object emailWindScriptt {
    @JvmStatic
    fun main(args: Array<String>) {
        val randomNumber = generateRandomNumber(100000, 999999)

        // Sende die Zahl per E-Mail
        sendEmail(
            "sl3mey@icloud.com", "Auth Code",
            "Your authentication code is \n\n$randomNumber\n\nDon't give anyone this code"
        )
    }

    // Methode zur Generierung einer zufälligen Zahl
    fun generateRandomNumber(min: Int, max: Int): Int {
        val random = Random()
        return random.nextInt(max - min + 1) + min
    }

    // Methode zum Senden einer E-Mail
    fun sendEmail(receiver: String, subject: String?, body: String?) {
        val senderEmail = "sl3mey@gmail.com"
        val senderPassword = "vvaq itwc hemu gcvv" // Ersetzen Sie dies mit Ihrem tatsächlichen Passwort
        val recipientEmail = receiver


        val props = Properties()
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = "465"

        val session = Session.getDefaultInstance(props,
            object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(senderEmail, senderPassword)
                }
            })

        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(senderEmail))
            message.addRecipient(Message.RecipientType.TO, InternetAddress(recipientEmail))
            message.subject = subject
            message.setText(body)

            Transport.send(message)
            println("E-Mail has been sent successfully!")
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }
}