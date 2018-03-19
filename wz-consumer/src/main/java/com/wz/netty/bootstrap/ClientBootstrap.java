package com.wz.netty.bootstrap;

import com.wz.dubbo.api.DemoService;
import com.wz.dubbo.api.MsgInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.smile.wz.rpc.WZRpcContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 消费端启动
 * wangzhen23
 * 2018/3/10.
 */
public class ClientBootstrap {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String configLocation = "dubbo-consumer.xml";
        ApplicationContext context = new ClassPathXmlApplicationContext(configLocation);
        DemoService ds = (DemoService) context.getBean("myService");

        System.out.println("===============================================");
        MsgInfo info = new MsgInfo();
        info.setId(3);
        info.setName("zhangsan");
        info.setMsgs(new ArrayList<String>());
        MsgInfo infoResult = ds.returnMsgInfo(info);
        Future<MsgInfo> future = WZRpcContext.getContext().getFuture();
        MsgInfo info1 = new MsgInfo();
        info1.setName("lisi");
        info1.setId(2);
        List list = new ArrayList();
        list.add("test1");
        list.add("test2");
        info1.setMsgs(list);
        MsgInfo infoResult1 = ds.returnMsgInfo(info1);
        Future<MsgInfo> future1 = WZRpcContext.getContext().getFuture();
        MsgInfo infoResultAsync = future.get();
        MsgInfo infoResultAsync2 = future1.get();
        System.out.println(infoResult);
        System.out.println(infoResult1);
        System.out.println(infoResultAsync);
        System.out.println(infoResultAsync2);

        System.out.println("===============================================");

    }
}
