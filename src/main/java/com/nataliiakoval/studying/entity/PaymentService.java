package com.nataliiakoval.studying.entity;

public class PaymentService {
    private MailService mailService;

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public void pay(String from, String to, double amount) {
        // payment logic

        mailService.sendEmail(from, "payment succeed");
        mailService.sendEmail(to, "payment succeed");
    }
}
