package com.nataliiakoval.studying.di_container;

import com.nataliiakoval.studying.entity.PaymentService;

public interface ApplicationContext {
    Object getBean(String id);

    <T> T getBean(Class<T> classType);
}
