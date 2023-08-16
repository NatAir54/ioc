package com.studying.ioc.context;

import com.studying.ioc.entity.Bean;
import com.studying.ioc.entity.BeanDefinition;
import com.studying.ioc.exception.BeanInstantiationException;
import lombok.Data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class BeanFactory {
    private final Map<String, BeanDefinition> beanDefinitionsMap;
    private final List<Bean> BEANS = new ArrayList<>();

    public BeanFactory(Map<String, BeanDefinition> beanDefinitionsMap) {
        this.beanDefinitionsMap = beanDefinitionsMap;
    }

    void getBeansReady() throws BeanInstantiationException {
        initializeBeans();
        injectValueDependencies();
        injectConstructorDependencies();
        injectRefDependencies();
    }

    private void initializeBeans() throws BeanInstantiationException {
        for (Map.Entry<String, BeanDefinition> item : beanDefinitionsMap.entrySet()) {
            Bean bean = new Bean();
            bean.setBeanName(item.getKey());
            String classType = item.getValue().getClassType();

            try {
                Class<?> someClass = Class.forName(classType);
                bean.setValue(someClass.newInstance());
            } catch (Exception ex) {
                throw new BeanInstantiationException("Bean creation failed");
            }
            BEANS.add(bean);
        }
    }

    private void injectValueDependencies() {
        for (Map.Entry<String, BeanDefinition> item : beanDefinitionsMap.entrySet()) {
            for (Bean bean : BEANS) {
                if (bean.getBeanName().equals(item.getKey())) {
                    Object value = bean.getValue();
                    Map<String, String> dependencies = item.getValue().getDependencies();
                    for (Map.Entry<String, String> dependency : dependencies.entrySet()) {
                        Field[] fields = value.getClass().getDeclaredFields();
                        for (Field field : fields) {
                            if (field.getName().equals(dependency.getKey())) {
                                Method[] methods = value.getClass().getDeclaredMethods();
                                for (Method method : methods) {
                                    if (method.getName().equalsIgnoreCase("set" + dependency.getKey())) {
                                        field.setAccessible(true);
                                        try {
                                            if (field.getType().equals(String.class)) {
                                                field.set(value, dependency.getValue());
                                            }
                                            if (field.getType() == Integer.TYPE || field.getType() == Integer.class) {
                                                field.set(value, Integer.valueOf(dependency.getValue()));
                                            }
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void injectConstructorDependencies() throws BeanInstantiationException {
        for (Map.Entry<String, BeanDefinition> item : beanDefinitionsMap.entrySet()) {
            for (Bean bean : BEANS) {
                if (bean.getBeanName().equals(item.getKey())) {
                    Class<?> someClass = bean.getValue().getClass();
                    Map<String, String> refDependencies = item.getValue().getRefDependencies();
                    for (Map.Entry<String, String> refDependency : refDependencies.entrySet()) {
                        if (refDependency.getKey().equals("propertyForConstructor")) {
                            for (Bean beanCreated : BEANS) {
                                if ((refDependency.getValue().equals(beanCreated.getBeanName()))) {
                                    Class<?> dependencyClass = beanCreated.getValue().getClass();
                                    try {
                                        Constructor<?> constructor = someClass.getDeclaredConstructor(dependencyClass);
                                        bean.setValue(constructor.newInstance(beanCreated.getValue()));
                                    } catch (Exception e) {
                                        throw new BeanInstantiationException("Bean creation failed");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void injectRefDependencies() {
        for (Map.Entry<String, BeanDefinition> item : beanDefinitionsMap.entrySet()) {
            for (Bean bean : BEANS) {
                if (bean.getBeanName().equals(item.getKey())) {
                    Object value = bean.getValue();
                    Map<String, String> refDependencies = item.getValue().getRefDependencies();
                    for (Map.Entry<String, String> refDependency : refDependencies.entrySet()) {
                        Field[] fields = value.getClass().getDeclaredFields();
                        for (Field field : fields) {
                            if (field.getName().equals(refDependency.getKey())) {
                                Method[] methods = value.getClass().getDeclaredMethods();
                                for (Method method : methods) {
                                    if (method.getName().equalsIgnoreCase("set" + refDependency.getKey())) {
                                        field.setAccessible(true);
                                        for (Bean beanCreated : BEANS) {
                                            if (refDependency.getValue().equals(beanCreated.getBeanName())) {
                                                try {
                                                    field.set(value, beanCreated.getValue());
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
