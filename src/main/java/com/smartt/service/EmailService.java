package com.smartt.service;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

// First, service banaya
// Second, Email request banaya
// Third, controller

@Service
public class EmailService {
	
    public boolean sendEmail(String subject, String message, String to) {
        boolean f = false; // matlab abhi email send nahi kiya gaya hai, isliye false use kiya hai. Iska matlab ye hai ki email successfully send ho chuka hai ya nahi.

        String from = "aayushsingh11800384@gmail.com"; // Aapka Gmail address
		
        // Gmail SMTP server configuration
        String host = "smtp.gmail.com";

        // Get the system properties
        Properties properties = System.getProperties(); // System ke properties ko load kar dega
        System.out.println("PROPERTIES: " + properties); // Yeh properties print ho raha hai

        // Setting important information to properties object
        properties.put("mail.smtp.host", host); // Host set karna
        properties.put("mail.smtp.port", "465"); // Gmail port
        properties.put("mail.smtp.ssl.enable", "true"); // SSL enable karna
        properties.put("mail.smtp.auth", "true"); // Authentication enable karna
		
        // Step 1: Get the session object
        Session session = Session.getInstance(properties, new Authenticator() {
			
            @Override
            protected PasswordAuthentication getPasswordAuthentication() { 
                // Yeh object new Authenticator (username aur password check karega) ko add kiya hai 
                // Niche wala code tab aaya hai jab ise override kiya hai, likha nahi hai
                return new PasswordAuthentication("aayushsingh11800384@gmail.com", "advit@123"); 
                // Replace with your email and app password
            }
        });

        // Debug true isliye kiya hai ki console par sahi se output ka pata chal jaye
        session.setDebug(true); 
        // Java mein debugging ka matlab hai apne code ko run-time pe analyze karna aur potential errors ko identify aur fix karna.

        try {
            // Step 2: Compose the message [text, multimedia]
            MimeMessage mimeMessage = new MimeMessage(session); 
            // MimeMessage subclass hai Message ka. Message original class hai jisme MimeMessage ko subclass bataya gaya hai

            // From email set karna
            mimeMessage.setFrom(from);

            // Adding recipient
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            /*
             * Java mein InetAddress ka mukhya kaam hai IP address aur hostname ko 
             * represent aur manage karna. Yeh hostnames ko IP addresses mein 
             * resolve karne aur local aur remote host information retrieve karne ke liye methods provide karta hai.
             */

            // Adding subject to message
            mimeMessage.setSubject(subject);

            // Adding message text to message
           // mimeMessage.setText(message);
            mimeMessage.setContent(message,"text/html");

            // Step 3: Send the message using Transport class
            Transport.send(mimeMessage); 
            System.out.println("Message sent successfully");
            f = true; // Matlab email send ho chuka hai
        } catch (Exception e) {
            e.printStackTrace(); // Agar koi exception aata hai toh yeh handle karega
        }

        return f; // Return karega ki email send hua ya nahi
    }
}
