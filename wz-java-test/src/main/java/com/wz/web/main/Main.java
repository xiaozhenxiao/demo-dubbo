package com.wz.web.main;

import com.wz.web.domain.Demo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by wangzhen on 2016-07-15.
 */
public class Main {
    public static void main(String[] args) {
        String configLocation="spring-config.xml";
        ApplicationContext context =new ClassPathXmlApplicationContext(configLocation);
        Demo demo = context.getBean("demo", Demo.class);
        System.out.println("demo: " + demo);
        String[] names=context.getBeanDefinitionNames();
        System.out.print("Beans:");
        for(String string : names) {
            System.out.print(string);
            System.out.print("  |  ");
        }
        System.exit(0);
    }
}
