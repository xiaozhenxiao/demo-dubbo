package com.wz.netty.future.wz.bootstrap;

import com.wz.dubbo.api.DemoService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * TODO
 * wangzhen23
 * 2018/3/10.
 */
public class ConsumerMain {
    public static void main(String[] args) {
        String configLocation = "dubbo-consumer.xml";
        ApplicationContext context = new ClassPathXmlApplicationContext(configLocation);
        DemoService ds = (DemoService) context.getBean("consumer1");

        System.out.println("===============================================");
        System.out.println(ds.sayHello("客户端调用"));
        System.out.println("===============================================");
    }
}
