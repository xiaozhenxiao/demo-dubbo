package com.wz.dubbo.provider;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhen on 2016-06-16.
 */
public class ConsumerMain {
    public static void main(String[] args) {
        String configLocation="dubbo-consumer.xml";
        ApplicationContext context =new ClassPathXmlApplicationContext(configLocation);
        DemoService ds=(DemoService) context.getBean("demoService");
        String[] names=context.getBeanDefinitionNames();
        System.out.print("Beans:");
        for(String string : names) {
            System.out.print(string);
            System.out.print(" ,|, ");
        }
        System.out.println();

        MsgInfo info =new MsgInfo();
        info.setId(1);
        info.setName("ruisheh");
        List<String> msgs=new ArrayList<String>();
        msgs.add("I");
        msgs.add("am");
        msgs.add("test");
        info.setMsgs(msgs);

        ds.sayHello("test 测试");
        System.out.println(ds.returnMsgInfo(info).getMsgs());
    }
}
