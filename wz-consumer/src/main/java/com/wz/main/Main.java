package com.wz.main;

import com.alibaba.fastjson.JSON;
import com.wz.spark.api.DemoService;
import com.wz.spark.api.MsgInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;

/**
 * Created by wangzhen on 2016-07-15.
 */
public class Main {
    public static void main(String[] args) {
        String configLocation="dubbo-consumer.xml";
        ApplicationContext context =new ClassPathXmlApplicationContext(configLocation);
//        DependService ds = context.getBean("dependService", DependService.class);
//        System.out.println("=====================" + ds.getDomain());
        DemoService demoService = (DemoService) context.getBean("demoService");
        System.out.println(demoService.sayHello("xiaoxiao"));
        MsgInfo info = new MsgInfo();
        info.setId(1);
        info.setName("zhangsan");
        info.setMsgs(new ArrayList<String>());
        System.out.println(JSON.toJSONString(demoService.returnMsgInfo(info)));
        String[] names=context.getBeanDefinitionNames();
        System.out.print("Beans:");
        for(String string : names) {
            System.out.print(string);
            System.out.print("  |  ");
        }
        System.exit(0);
    }
}
