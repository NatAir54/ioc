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
    private List<Bean> beans;

    public ClassPathXmlApplicationContext(String... configXmlFilePath) {
        BeanDefinitionReader beanReader = new XmlBeanDefinitionReader(configXmlFilePath);
        Map<String, BeanDefinition> beanDefinitions = beanReader.readBeanDefinitions();
        getBeansFromBeanDefinitions(beanDefinitions);
    }

    @Override
    public List<String> getBeanNames() {
        List<String> beanNames = new ArrayList<>();
        for (Bean bean : beans) {
            beanNames.add(bean.getBeanName());
        }
        return beanNames;
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
            if (classType.isInstance(bean.getValue())) {
                if (bean.getBeanName().equals(id)) {
                    object = classType.cast(bean.getValue());
                }
            }
        }
        return object;
    }

    private void getBeansFromBeanDefinitions(Map<String, BeanDefinition> beanDefinitions) {
        BeanFactory beanFactory = new BeanFactory();
        try {
            beans = beanFactory.createBeans(beanDefinitions);
        } catch (BeanInstantiationException e) {
            e.printStackTrace();
        }
    }
}
