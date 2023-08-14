package com.studying.ioc.entity;

import lombok.Data;

@Data
public class MailService {
    private String protocol;
    private int port;

    public void sendEmail(String emailAddress, String message) {
        System.out.println("Sending email to: " + emailAddress);
        System.out.println("With content: " + message);
    }
}
