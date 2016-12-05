package com.wz.jd.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 */
public class ApplicationContextUtil {
    private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-config.xml");

    public static <T> T getSpringBean(String beanId, Class clazz){
        return (T) applicationContext.getBean(beanId, clazz);
    }
}
