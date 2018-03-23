package com.wz.netty.bootstrap;

import com.wz.dubbo.api.DemoService;
import com.wz.dubbo.api.IDemoService;
import com.wz.dubbo.api.MsgInfo;
import com.wz.dubbo.api.Person;
import com.wz.prodect.callback.NotifyImpl;
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
        String configLocation = "smile-consumer.xml";
        ApplicationContext context = new ClassPathXmlApplicationContext(configLocation);
        /*DemoService ds = (DemoService) context.getBean("myService");

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
        System.out.println(infoResultAsync2);*/

        /******************************************** 异步回调 ***************************************************/
        IDemoService demoService = (IDemoService) context.getBean("demoService1");
        NotifyImpl notify = (NotifyImpl) context.getBean("demoCallback");
        String requestId = "2";
        Person ret = demoService.get(requestId);
        System.out.println(ret);
        //for Test：只是用来说明callback正常被调用，业务具体实现自行决定.
        for (int i = 0; i < 10; i++) {
            if (!notify.ret.containsKey(requestId)) {
                Thread.sleep(1000);
            } else {
                break;
            }
        }
        System.out.println(notify.ret.get(requestId));
        /******************************************** 异步回调 ***************************************************/
        System.out.println("===============================================");

    }
}
