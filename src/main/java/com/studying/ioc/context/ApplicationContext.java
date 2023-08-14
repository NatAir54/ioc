package com.studying.ioc.context;

import com.studying.ioc.resource_reader.BeanDefinitionReader;

import java.util.List;

public interface ApplicationContext {
    void setBeanDefinitionReader(BeanDefinitionReader reader);
    Object getBean(String id);
    <T> T getBean(Class<T> classType);
    <T> T getBean(String id, Class<T> classType);
    List<String> getBeanNames();
}
