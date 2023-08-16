package com.studying.ioc.resource_reader;

import com.studying.ioc.entity.BeanDefinition;
import lombok.Data;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class XMLStaxParser {
    private Map<String, BeanDefinition> beanDefinitions = new LinkedHashMap<>();

    void parseXmlFile(String... paths) {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        for (String path : paths) {
            BeanDefinition beanDefinition = null;
            Map<String, String> dependencies = null;
            Map<String, String> refDependencies = null;
            try {
                XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(path));

                while (reader.hasNext()) {
                    XMLEvent xmlEvent = reader.nextEvent();

                    if (xmlEvent.isStartElement()) {
                        StartElement startElement = xmlEvent.asStartElement();

                        if (startElement.getName().getLocalPart().equals("bean")) {
                            beanDefinition = new BeanDefinition();
                            dependencies = new HashMap<>();
                            refDependencies = new HashMap<>();

                            Attribute idAttribute = startElement.getAttributeByName(new QName("id"));
                            beanDefinition.setBeanName(idAttribute.getValue());
                            Attribute classAttribute = startElement.getAttributeByName(new QName("class"));
                            beanDefinition.setClassType(classAttribute.getValue());

                        } else if (startElement.getName().getLocalPart().equals("property")) {
                            xmlEvent = reader.nextEvent();

                            Attribute attributeByName = startElement.getAttributeByName(new QName("name"));
                            String propertyName = attributeByName.getValue();
                            Attribute attributeValue = startElement.getAttributeByName(new QName("value"));
                            if (attributeValue != null) {
                                String propertyValue = attributeValue.getValue();
                                assert dependencies != null;
                                dependencies.put(propertyName, propertyValue);
                            } else {
                                Attribute attributeRefValue = startElement.getAttributeByName(new QName("ref"));
                                String propertyValue = attributeRefValue.getValue();
                                assert dependencies != null;
                                refDependencies.put(propertyName, propertyValue);
                            }
                        } else if (startElement.getName().getLocalPart().equals("constructor-arg")) {
                            xmlEvent = reader.nextEvent();

                            Attribute attributeRefValue = startElement.getAttributeByName(new QName("ref"));
                            String propertyValue = attributeRefValue.getValue();
                            assert refDependencies != null;
                            refDependencies.put("propertyForConstructor", propertyValue);
                        }
                    }

                    if (xmlEvent.isEndElement()) {
                        EndElement endElement = xmlEvent.asEndElement();
                        if (endElement.getName().getLocalPart().equals("bean")) {
                            assert beanDefinition != null;
                            beanDefinition.setDependencies(dependencies);
                            beanDefinition.setRefDependencies(refDependencies);
                            beanDefinitions.put(beanDefinition.getBeanName(), beanDefinition);
                        }
                    }
                }
            } catch (XMLStreamException | FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
