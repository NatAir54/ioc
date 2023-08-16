package com.studying.ioc.context;

import java.util.List;

public interface ApplicationContext {
    Object getBean(String id);

    <T> T getBean(Class<T> classType);

    <T> T getBean(String id, Class<T> classType);

    List<String> getBeanNames();
}
