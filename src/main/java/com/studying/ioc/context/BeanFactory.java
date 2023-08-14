package com.studying.ioc.context;

import com.studying.ioc.entity.Bean;
import com.studying.ioc.entity.BeanDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeanFactory {
    List<Bean> beansContainer;

    List<Bean> createBeans(Map<String, BeanDefinition> beanDefinitionsMap) {
        beansContainer = new ArrayList<>();
        for(Map.Entry<String, BeanDefinition> item : beanDefinitionsMap.entrySet()) {
            Bean bean = new Bean();
            bean.setBeanName(item.getKey());
            String classType = item.getValue().getClassType();
            try {
                Class<?> someClass = Class.forName(classType);
                bean.setValue(someClass.newInstance());


            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            beansContainer.add(bean);
        }
        return beansContainer;
    }
    private void injectDependencies() {}

    private void injectRefDependencies() {}
}
