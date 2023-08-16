package com.studying.ioc.resource_reader;

import com.studying.ioc.entity.BeanDefinition;
import java.util.Map;

public class XmlBeanDefinitionReader implements BeanDefinitionReader {
    private final String[] URI;
    public XmlBeanDefinitionReader(String... path) {
        this.URI = path;
    }

    @Override
    public Map<String, BeanDefinition> readBeanDefinitions() {
        XMLStaxParser xmlStaxParser = new XMLStaxParser();
        xmlStaxParser.parseXmlFile(URI);
        return xmlStaxParser.getBeanDefinitions();
    }
}
