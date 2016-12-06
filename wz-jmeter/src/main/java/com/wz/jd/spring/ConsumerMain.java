package com.wz.jd.spring;

import com.wz.dubbo.api.DemoService;
import com.wz.dubbo.api.MsgInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhen on 2016-06-16.
 */
public class ConsumerMain {
    public static void main(String[] args) {
        DemoService ds = ApplicationContextUtil.getSpringBean("demoService", DemoService.class);

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
