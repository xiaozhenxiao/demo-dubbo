package com.wz.dubbo.provider;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by wangzhen on 2016-06-16.
 */
public class ProviderMain {
    public static void main(String[] args) throws InterruptedException {
        String configLocation="dubbo-provider.xml";
        ApplicationContext context =new ClassPathXmlApplicationContext(configLocation);
        String[] names=context.getBeanDefinitionNames();
        System.out.print("Beans:");
        for(String string : names)
            System.out.print(string+" ,||, ");
        System.out.println();
        Thread.sleep(1000*60*10);
    }
}
