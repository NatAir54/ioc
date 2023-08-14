package com.studying.ioc.resource_reader;

import com.studying.ioc.entity.BeanDefinition;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import java.util.HashMap;
import java.util.Map;


public class XMLHandler extends DefaultHandler {
    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

    public Map<String, BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("bean")) {
            String beanName = attributes.getValue("id");
            String classType = attributes.getValue("class");
            Map<String, String> dependencies = new HashMap<>();
            Map<String, String> refDependencies = new HashMap<>();

            beanDefinitions.put(beanName, new BeanDefinition(beanName, classType, dependencies, refDependencies));
        }
    }
}
