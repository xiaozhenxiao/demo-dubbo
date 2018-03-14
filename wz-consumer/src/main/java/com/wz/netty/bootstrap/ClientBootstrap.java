package com.wz.netty.bootstrap;

import com.wz.dubbo.api.DemoService;
import com.wz.dubbo.api.MsgInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;

/**
 * 消费端启动
 * wangzhen23
 * 2018/3/10.
 */
public class ClientBootstrap {
    public static void main(String[] args) {
        String configLocation = "dubbo-consumer.xml";
        ApplicationContext context = new ClassPathXmlApplicationContext(configLocation);
        DemoService ds = (DemoService) context.getBean("consumer1");

        System.out.println("===============================================");
        MsgInfo info = new MsgInfo();
        info.setId(1);
        info.setName("zhangsan");
        info.setMsgs(new ArrayList<String>());
        MsgInfo infoResult = ds.returnMsgInfo(info);
        info.setId(2);
        MsgInfo infoResult1 = ds.returnMsgInfo(info);
        System.out.println(infoResult);
        System.out.println(infoResult1);

        System.out.println("===============================================");
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
