package com.sjsu.healthcare.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    public static void sendEmail(String recepientAddress, String emailMessage)
    {
        final String username = "Healthcaresystem295@yahoo.com";
        final String password = "CMPE295Project";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.mail.yahoo.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));

            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepientAddress));
            message.setSubject("Healthcare App Notification");
            message.setText(emailMessage);
            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
