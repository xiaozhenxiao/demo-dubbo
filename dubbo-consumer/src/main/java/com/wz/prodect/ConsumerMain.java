package com.wz.prodect;

import com.wz.dubbo.api.DemoService;
import com.wz.dubbo.api.IDemoService;
import com.wz.dubbo.api.Person;
import com.wz.prodect.callback.NotifyImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by wangzhen on 2016-06-16.
 */
public class ConsumerMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        String configLocation="dubbo-consumer.xml";
        ApplicationContext context =new ClassPathXmlApplicationContext(configLocation);
//        DemoService ds=(DemoService) context.getBean("demoService");
        String[] names=context.getBeanDefinitionNames();
        System.out.print("Beans:");
        for(String string : names) {
            System.out.print(string);
            System.out.print("  |  ");
        }
        System.out.println();

        /*MsgInfo info =new MsgInfo();
        info.setId(1);
        info.setName("ruisheh");
        List<String> msgs=new ArrayList<String>();
        msgs.add("I");
        msgs.add("am");
        msgs.add("test");
        info.setMsgs(msgs);

        ds.sayHello("test 测试");*/
//        System.out.println(ds.returnMsgInfo(info).getMsgs());

        /******************************************** 参数回调 ***************************************************/
        /*CallbackService callbackService = (CallbackService) context.getBean("callbackService");
        callbackService.addListener("foo.bar", new CallbackListener() {
            public void changed(String msg) {
                System.out.println("callback1:" + msg);
            }
        });*/
        /******************************************** 参数回调 ***************************************************/

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
        System.out.println(notify.ret.get(requestId).getId());
        /******************************************** 异步回调 ***************************************************/
        System.in.read();
    }
}
