package com.studying.ioc.resource_reader;

import com.studying.ioc.entity.BeanDefinition;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class XmlBeanDefinitionReader implements BeanDefinitionReader {
    private final String[] URI;

    public XmlBeanDefinitionReader(String... path) {
        this.URI = path;
    }

    @Override
    public Map<String, BeanDefinition> readBeanDefinitions() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XMLHandler handler = new XMLHandler();
        try {
            SAXParser parser = factory.newSAXParser();
            for (String uri : URI) {
                parser.parse(uri, handler);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
       // return handler.getBeanDefinitions();

        // StAX XML Parser


        XMLStaxParser xmlStaxParser = new XMLStaxParser();
        return xmlStaxParser.parseXmlFile(URI);
    }
}
