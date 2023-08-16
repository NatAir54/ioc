package com.studying.ioc.resource_reader;

import com.studying.ioc.entity.BeanDefinition;
import java.util.Map;

public interface BeanDefinitionReader {
    Map<String, BeanDefinition> readBeanDefinitions();
}
