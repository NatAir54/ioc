package com.nataliiakoval.studying.entity;

public class MailService {
    private String protocol;
    private int port;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void sendEmail(String emailAddress, String message) {
        System.out.println("Sending email to: " + emailAddress);
        System.out.println("With content: " + message);
    }
}
