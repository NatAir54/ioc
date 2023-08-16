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

    public List<Bean> getBeansReady(Map<String, BeanDefinition> beanDefinitionsMap) throws BeanInstantiationException {
        List<Bean> beansInitialized = initializeBeans(beanDefinitionsMap);
        List<Bean> beansConstructorDependenciesInjected = injectConstructorDependencies(beansInitialized, beanDefinitionsMap);
        List<Bean> beansValueDependenciesInjected = injectValueDependencies(beansConstructorDependenciesInjected, beanDefinitionsMap);
        List<Bean> beansReady = injectRefDependencies(beansValueDependenciesInjected, beanDefinitionsMap);
        return beansReady;
    }

    private List<Bean> initializeBeans(Map<String, BeanDefinition> beanDefinitionsMap) throws BeanInstantiationException {
        List<Bean> beans = new ArrayList<>();
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
            beans.add(bean);
        }
        return beans;
    }

    private List<Bean> injectConstructorDependencies(List<Bean> beans, Map<String, BeanDefinition> beanDefinitionsMap) throws BeanInstantiationException {
        for (Map.Entry<String, BeanDefinition> item : beanDefinitionsMap.entrySet()) {
            for (Bean bean : beans) {
                if (bean.getBeanName().equals(item.getKey())) {
                    Class<?> someClass = bean.getValue().getClass();
                    Map<String, String> refDependencies = item.getValue().getRefDependencies();
                    for (Map.Entry<String, String> refDependency : refDependencies.entrySet()) {
                        if (refDependency.getKey().equals("propertyForConstructor")) {
                            for (Bean beanCreated : beans) {
                                if ((refDependency.getValue().equals(beanCreated.getBeanName()))) {
                                    Class<?> dependencyClass = beanCreated.getValue().getClass();
                                    try {
                                        Constructor<?> constructor = someClass.getDeclaredConstructor(dependencyClass);
                                        bean.setValue(constructor.newInstance(beanCreated.getValue()));
                                    } catch (Exception e) {
                                        throw new BeanInstantiationException("Bean creation injecting constructor dependency failed");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return beans;
    }

    private List<Bean> injectValueDependencies(List<Bean> beans, Map<String, BeanDefinition> beanDefinitionsMap) {
        for (Map.Entry<String, BeanDefinition> item : beanDefinitionsMap.entrySet()) {
            for (Bean bean : beans) {
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
        return beans;
    }


    private List<Bean> injectRefDependencies(List<Bean> beans, Map<String, BeanDefinition> beanDefinitionsMap) {
        for (Map.Entry<String, BeanDefinition> item : beanDefinitionsMap.entrySet()) {
            for (Bean bean : beans) {
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
                                        for (Bean beanCreated : beans) {
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
        return beans;
    }

}
