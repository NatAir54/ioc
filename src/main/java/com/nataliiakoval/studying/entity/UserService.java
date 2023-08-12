package com.nataliiakoval.studying.entity;

public class UserService {
    private MailService mailService;

    public UserService(MailService mailService) {
        this.mailService = mailService;
    }

    public void sendEmailWithUsersCount() {
        int numberOfUsersInSystem = getUsersCount();
        mailService.sendEmail("boss@project.com", "There are " + numberOfUsersInSystem + " users in system.");
    }

    public int getUsersCount() {
        return (int) (Math.random() * 1000);
    }
}
