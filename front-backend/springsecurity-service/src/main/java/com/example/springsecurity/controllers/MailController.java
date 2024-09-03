package com.example.springsecurity.controllers;


import com.example.springsecurity.models.Mail;
import com.example.springsecurity.utils.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/email")
@CrossOrigin("*")
public class MailController {

        @Autowired
        private EmailService emailService;
    @Autowired
    private JavaMailSender emailSender;

        @PostMapping("/sendMail")
        public String sendMail(){
            System.out.println("Spring Mail - Sending Simple Email with JavaMailSender Example");
            Mail mail = new Mail();
            mail.setFrom("******@gmail.com");
            mail.setTo("chetouiiftikhar@gmail.com");
            mail.setSubject("Sending Simple Email with JavaMailSender Example");
            mail.setContent("This tutorial demonstrates how to send a simple email using Spring Framework.");
            emailService.sendSimpleMessage(mail);
            return "ok";
        }




    @PostMapping("/send")
    public ResponseEntity<Mail> sendMail(Mail mail) {
        try {
            sendEmail(mail);
            return new ResponseEntity<>(mail, HttpStatus.OK);
        } catch (MessagingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void sendEmail(Mail mail) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(mail.getFrom());
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        helper.setText("content :"+ mail.getContent(), true);

        emailSender.send(message);
    }





}

