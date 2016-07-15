package com.wz.main;

import com.wz.dubbo.api.DemoService;
import com.wz.dubbo.api.MsgInfo;
import com.wz.spring.depends.test.service.DependService;
import com.wz.spring.schema.People;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhen on 2016-07-15.
 */
public class Main {
    public static void main(String[] args) {
        String configLocation="dubbo-consumer.xml";
        ApplicationContext context =new ClassPathXmlApplicationContext(configLocation);
        DependService ds = context.getBean("dependService", DependService.class);
        System.out.println("=====================" + ds.getDomain());
        String[] names=context.getBeanDefinitionNames();
        System.out.print("Beans:");
        for(String string : names) {
            System.out.print(string);
            System.out.print("  |  ");
        }
        System.exit(0);
    }
}
