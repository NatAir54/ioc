package com.studying.ioc.entity;

import lombok.Data;

@Data
public class PaymentService {
    private MailService mailService;
    private int maxAmount;


    public void pay(String from, String to, double amount) {

        mailService.sendEmail(from, "payment succeed");
        mailService.sendEmail(to, "payment succeed");
    }
}
