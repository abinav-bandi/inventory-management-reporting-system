package com.inventory.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.util.Properties;

public class EmailService {

    public static void sendReport(String toEmail, String subject, String body, String attachmentPath) {
        final String fromEmail = System.getenv("MAIL_USER");  // your email
        final String password = System.getenv("MAIL_PASS");   // your app password

        if (fromEmail == null || password == null) {
            throw new RuntimeException("❌ Email  credentials not set in environment variables!");
        }

        // SMTP configuration
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();

            // Email body
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            multipart.addBodyPart(textPart);

            // Attachment (only if path is provided)
            if (attachmentPath != null && !attachmentPath.isEmpty()) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(new File(attachmentPath));
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);

            Transport.send(message);

        } catch (MessagingException e) {
            System.out.println("❌ Error sending email: " + e.getMessage());
            e.printStackTrace(); // <-- full error details
        } catch (Exception e) {
            System.out.println("❌ General error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void sendOtp(String toEmail, String otp) {
        final String fromEmail = System.getenv("MAIL_USER");  // your email
        final String password = System.getenv("MAIL_PASS");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Inventory system-Email verification");
            message.setText("Your otp for email verificaion"+otp);
            Transport.send(message);

        }catch (Exception e) {
            System.out.println("Error sending otp for email: " + e.getMessage());

        }
    }
    public static void sendAlert(String toEmail,String sub, String msg) {
        final String fromEmail = System.getenv("MAIL_USER");  // your email
        final String password = System.getenv("MAIL_PASS");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(sub);
            message.setText(msg);
            Transport.send(message);
            System.out.println("Stock Alert sent successfully");

        }catch (Exception e) {
            System.out.println("Error sending Stock Alert for email: " + e.getMessage());

        }
    }
}
