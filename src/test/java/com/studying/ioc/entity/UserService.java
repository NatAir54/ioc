package com.studying.ioc.entity;

import lombok.Data;

@Data
public class UserService {
    private MailService mailService;

    public UserService() {}

    public UserService(MailService mailService) {
        this.mailService = mailService;
    }

    public void sendEmailWithUsersCount() {
        int numberOfUsersInSystem = getUsersCount();
        System.out.println(numberOfUsersInSystem);
        mailService.sendEmail("boss@project.com", "There are " + numberOfUsersInSystem + " users in system.");
    }

    public int getUsersCount() {
        return (int) (Math.random() * 1000);
    }
}
