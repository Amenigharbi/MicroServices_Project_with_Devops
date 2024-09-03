package com.example.springsecurity.utils;

import com.example.springsecurity.models.Mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {



        @Autowired
        private JavaMailSender emailSender ;

        public void sendSimpleMessage(final Mail mail){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(mail.getSubject());
            message.setText(mail.getContent());
            message.setTo(mail.getTo());
            message.setFrom(mail.getFrom());

            emailSender.send(message);
        }

    public void sendEmail(String recipientEmail, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        emailSender.send(mailMessage);
    }


}

