/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iw.framework.utils;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

/**
 *
 * @author dario
 */
public class EmailSender {

    public static void send(final String from, final String password, String to, String sub, String msg) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        //compose message
        try {
            MimeMessage message = new MimeMessage(session);
            InternetAddress [] parse = InternetAddress.parse(to, true);
            message.addRecipients(Message.RecipientType.TO, parse);
            message.setSubject(sub);
            message.setText(msg);
            //send message
            Transport.send(message);
            System.err.println("messaggio inviato correttamente");
        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
