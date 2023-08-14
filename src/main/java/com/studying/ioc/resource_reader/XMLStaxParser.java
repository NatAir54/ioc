package com.studying.ioc.resource_reader;

import com.studying.ioc.entity.BeanDefinition;
import lombok.Data;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Data
public class XMLStaxParser {
    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();


    Map<String, BeanDefinition> parseXmlFile(String... paths) {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        for (String path : paths) {
            try {
                XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(path));
                while (reader.hasNext()) {
                    XMLEvent xmlEvent = reader.nextEvent();

                }
            } catch (XMLStreamException | FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        return beanDefinitions;
    }
}
