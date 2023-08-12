package com.nataliiakoval.studying.di_container;

public class ClassPathXmlApplicationContext implements ApplicationContext {
    String[] paths;

    public ClassPathXmlApplicationContext(String...configXmlFilePath) {
        this.paths = configXmlFilePath;
    }

    @Override
    public Object getBean(String id) {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> classType) {
        return null;
    }
}
