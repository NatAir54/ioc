package com.studying.ioc.context;

import com.studying.ioc.entity.Bean;
import com.studying.ioc.entity.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeanFactory {
    private final List<Bean> BEANS = new ArrayList<>();

    List<Bean> createBeans(Map<String, BeanDefinition> beanDefinitionsMap) {
        for (Map.Entry<String, BeanDefinition> item : beanDefinitionsMap.entrySet()) {
            Bean bean = new Bean();
            bean.setBeanName(item.getKey());
            String classType = item.getValue().getClassType();

            try {
                Class<?> someClass = Class.forName(classType);
                bean.setValue(someClass.newInstance());

                Map<String, String> refDependencies = item.getValue().getRefDependencies();
                for (Map.Entry<String, String> refDependency : refDependencies.entrySet()) {
                    if (refDependency.getKey().equals("propertyForConstructor")) {
                        for (Bean beanItem : BEANS) {
                            if ((refDependency.getValue().equals(beanItem.getBeanName()))) {
                                Class<?> dependencyClass = beanItem.getValue().getClass();
                                Constructor<?> constructor = someClass.getDeclaredConstructor(dependencyClass);
                                bean.setValue(constructor.newInstance(beanItem.getValue()));
                            }
                        }
                    }
                }

                Object value = bean.getValue();
                Map<String, String> dependencies = item.getValue().getDependencies();
                this.injectDependencies(value, dependencies);
                Map<String, String> refDependencies1 = item.getValue().getRefDependencies();
                this.injectRefDependencies(value, refDependencies1);

            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException |
                     InvocationTargetException e) {
                e.printStackTrace();
            }

            BEANS.add(bean);
        }
        return BEANS;
    }


    private void injectDependencies(Object value, Map<String, String> dependencies) {
        if (dependencies != null) {
            for (Map.Entry<String, String> dependency : dependencies.entrySet()) {
                Field[] fields = value.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.getName().equals(dependency.getKey())) {
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

    private void injectRefDependencies(Object value, Map<String, String> refDependencies) {
        if (refDependencies != null) {
            for (Map.Entry<String, String> refDependency : refDependencies.entrySet()) {
                Field[] fields = value.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.getName().equals(refDependency.getKey())) {
                        field.setAccessible(true);
                        for (Bean bean : BEANS) {
                            if (refDependency.getValue().equals(bean.getBeanName())) {
                                try {
                                    field.set(value, bean.getValue());
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
