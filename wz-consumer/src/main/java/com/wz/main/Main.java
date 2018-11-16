package com.wz.main;

import com.alibaba.fastjson.JSON;
import com.wz.dubbo.api.DemoService;
import com.wz.dubbo.api.MsgInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by wangzhen on 2016-07-15.
 */
public class Main {
    public static void main(String[] args) {
        /*String configLocation= "smile-consumer.xml";
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
        System.exit(0);*/

        float f = 123.666f;
        ByteBuffer bbuf = ByteBuffer.allocate(4);
        bbuf.putFloat(f);
        byte[] bBuffer = bbuf.array();
//        bBuffer=dataValueRollback(bBuffer);

        int l = bBuffer[3] & 0xff;
        l |= (bBuffer[2] << 8);
        l &= 0xffff;
        l |= (bBuffer[1] << 16);
        l &= 0xffffff;
        l |= (bBuffer[0] << 24);
        float r = Float.intBitsToFloat(l);
        System.out.println(f + " float:" + r + " : " + l);

        int i = 1;
        i = i >> 2;

        short s = 2;
        int ss = s << 2;

        long ll = 5;
        ll = ll >> 2;
    }
}
