package com.example.springsecurity.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
/*
    public static final String ACCOUNT_SID = "AC6d6975eca32caf6018770949df9ee0e8"; //Account SID form twillo dashborad
    public static final String AUTH_TOKEN = "aab8c1983aa137bca88bb8722d2ce516"; //Auth Token form twillo dashboard

    public Message sendSMS(String phoneNumber) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(phoneNumber),// Send message to the number
                        new com.twilio.type.PhoneNumber("+13347317477"),//the twillo generated number
                        "Hello from the other side.") // Messages to be send
                .create();

        return message;
    }

    */

    public static final String ACCOUNT_SID = "AC6d6975eca32caf6018770949df9ee0e8";
    public static final String AUTH_TOKEN = "aab8c1983aa137bca88bb8722d2ce516";


    public Message sendSMS(String phoneNumber, String resetCode) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(phoneNumber), // Le numéro de téléphone de destination
                        new com.twilio.type.PhoneNumber("+13347317477"), // Votre numéro Twilio
                        "Votre code est : " + resetCode) // Message à envoyer
                .create();

        return message;
    }
}

