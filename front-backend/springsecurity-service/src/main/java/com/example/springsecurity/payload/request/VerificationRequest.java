package com.example.springsecurity.payload.request;

public class VerificationRequest {

    private String verificationCode;
    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

   // private String username;


    // Constructeur par défaut
    public VerificationRequest() {
    }

    // Getters et Setters



}
