package com.nataliiakoval.studying;

import com.nataliiakoval.studying.di_container.ApplicationContext;
import com.nataliiakoval.studying.di_container.ClassPathXmlApplicationContext;
import com.nataliiakoval.studying.entity.MailService;
import com.nataliiakoval.studying.entity.PaymentService;

public class Starter {
    public static void main(String[] args) throws InterruptedException {
//        // MailService creation & configuration
//        MailService mailService = new MailService();
//        mailService.setPort(1000);
//        mailService.setProtocol("POP3");
//
//        // UserService creation & configuration
//        UserService userService = new UserService(mailService);
//
//        // PaymentService creation & configuration
//        PaymentService paymentService = new PaymentService();
//        paymentService.setMailService(mailService);

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("resources/context.xml");
        MailService mailService = (MailService) applicationContext.getBean("mailService");
        PaymentService paymentService = applicationContext.getBean(PaymentService.class);

        while(true) {
            Thread.sleep(1000);
            userService.sendEmailWithUsersCount();
            System.out.println("---------------------------------");
            paymentService.pay("payerEmail", "recipientEmail", 1000);
            System.out.println("---------------------------------");
        }
    }
}