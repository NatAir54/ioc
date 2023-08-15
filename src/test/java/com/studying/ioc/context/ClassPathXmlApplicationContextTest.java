package com.studying.ioc.context;

import com.studying.ioc.entity.MailService;
import com.studying.ioc.entity.PaymentService;
import com.studying.ioc.entity.UserService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClassPathXmlApplicationContextTest {

    private final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("src/main/resources/context.xml");

    @Test
    public void testCreateBeansNotNull() {
        MailService mailService = applicationContext.getBean(MailService.class);
        assertNotNull(mailService);
        UserService userService = (UserService) applicationContext.getBean("userService");
        assertNotNull(userService);
        UserService userService1 = applicationContext.getBean("userService", UserService.class);
        assertNotNull(userService1);
        PaymentService paymentService = applicationContext.getBean("paymentService", PaymentService.class);
        assertNotNull(paymentService);
        PaymentService paymentServiceWithMax = applicationContext.getBean("paymentServiceWithMax", PaymentService.class);
        assertNotNull(paymentServiceWithMax);
    }

    @Test
    public void testGetBeanNames() {
        List<String> actual = applicationContext.getBeanNames();
        List<String> expected = new ArrayList<>(List.of("mailService", "userService", "paymentService", "paymentServiceWithMax"));
        Collections.sort(actual);
        Collections.sort(expected);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testBeanPrimitiveDependencies() {
        PaymentService paymentServiceWithMax = applicationContext.getBean("paymentServiceWithMax", PaymentService.class);
        assertEquals(5000, paymentServiceWithMax.getMaxAmount());
        MailService mailService = applicationContext.getBean(MailService.class);
        assertNotNull(mailService);
        assertEquals(3000, mailService.getPort());
        assertEquals("POP3", mailService.getProtocol());
    }

    @Test
    public void testBeanRefDependencies() {

    }

}