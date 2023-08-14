package com.studying.ioc.entity;

import lombok.Data;

import java.util.Map;

@Data
public class BeanDefinition {
    private String beanName;
    private String classType;
    private Map<String, String> dependencies;
    private Map<String, String> refDependencies;

    public BeanDefinition(String beanName, String classType, Map<String, String> dependencies, Map<String, String> refDependencies) {
        this.beanName = beanName;
        this.classType = classType;
        this.dependencies = dependencies;
        this.refDependencies = refDependencies;
    }
}
