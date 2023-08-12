package com.nataliiakoval.studying.dependency_injection_container;

public class ClassPathXmlApplicationContext implements ApplicationContext {
    String[] paths;
    public ClassPathXmlApplicationContext(String...configXmlFilePath) {
        this.paths = configXmlFilePath;
    }
}
