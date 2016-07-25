package com.wz.web.main;

import com.wz.web.domain.Demo;
import com.wz.web.service.DemoService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by wangzhen on 2016-07-15.
 */
public class Main {
    public static void main(String[] args) {
        String configLocation="spring-config.xml";
        ApplicationContext context =new ClassPathXmlApplicationContext(configLocation);
        DemoService demoService = context.getBean("demoService", DemoService.class);
        Demo demo = new Demo();
        demo.setUsername("笑笑");
        demo.setPassword("123456");
        int result = 0;
        try {
            result = demoService.addDemoSelective(demo);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("结果：" + result);
        String[] names=context.getBeanDefinitionNames();
        System.out.print("Beans:");
        for(String string : names) {
            System.out.print(string);
            System.out.print("  |  ");
        }
        System.exit(0);
    }
}
