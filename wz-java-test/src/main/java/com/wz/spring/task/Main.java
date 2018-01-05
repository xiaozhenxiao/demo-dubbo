package com.wz.spring.task;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * TODO
 * wangzhen23
 * 2018/1/2.
 */
public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext cpxc = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
//        TaskExecutorExample taskExecutorExampl
        cpxc.start();
    }
}
