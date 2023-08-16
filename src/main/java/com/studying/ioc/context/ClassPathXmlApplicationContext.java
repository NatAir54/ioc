package com.studying.ioc.context;

import com.studying.ioc.entity.Bean;
import com.studying.ioc.entity.BeanDefinition;
import com.studying.ioc.exception.BeanInstantiationException;
import com.studying.ioc.resource_reader.XmlBeanDefinitionReader;
import com.studying.ioc.resource_reader.BeanDefinitionReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassPathXmlApplicationContext implements ApplicationContext {
    private final String[] PATH;
    private BeanDefinitionReader beanReader;
    private Map<String, BeanDefinition> beanDefinitions;
    private List<Bean> beans;


    public ClassPathXmlApplicationContext(String... configXmlFilePath) {
        this.PATH = configXmlFilePath;
        getBeanDefinitionsFromBeanDefinitionReader();
        fillBeansListFromBeanDefinitions();
    }

    @Override
    public void setBeanDefinitionReader(BeanDefinitionReader reader) {
        this.beanReader = reader;
    }

    @Override
    public Object getBean(String id) {
        Object value = new Object();
        for (Bean bean : beans) {
            if (bean.getBeanName().equals(id)) {
                value = bean.getValue();
            }
        }
        return value;
    }

    @Override
    public <T> T getBean(Class<T> classType) {
        T object = null;
        for (Bean bean : beans) {
            if (bean.getValue().getClass().equals(classType)) {
                object = classType.cast(bean.getValue());
            }
        }
        return object;
    }

    @Override
    public <T> T getBean(String id, Class<T> classType) {
        T object = null;
        for (Bean bean : beans) {
            if(classType.isInstance(bean.getValue())) {
                if (bean.getBeanName().equals(id)) {
                    object = classType.cast(bean.getValue());
                }
            }
        }
        return object;
    }

    @Override
    public List<String> getBeanNames() {
        List<String> beanNames = new ArrayList<>();
        for (Bean bean : beans) {
            beanNames.add(bean.getBeanName());
        }
        return beanNames;
    }

    private void getBeanDefinitionsFromBeanDefinitionReader() {
        setBeanDefinitionReader(new XmlBeanDefinitionReader(PATH));
        beanDefinitions = beanReader.readBeanDefinitions();
    }

    private void fillBeansListFromBeanDefinitions() {
        BeanFactory beanFactory = new BeanFactory();
        try {
            beans = beanFactory.getBeansReady(beanDefinitions);
        } catch (BeanInstantiationException e) {
            e.printStackTrace();
        }
    }
}
