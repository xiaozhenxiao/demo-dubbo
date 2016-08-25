package com.wz.dubbo.provider;

import com.wz.dubbo.api.DemoService;
import com.wz.dubbo.api.MsgInfo;

/**
 * Created by wangzhen on 2016-06-16.
 */
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        System.out.println("i'm provider , thank you!");
        return "i'm provider , thank you!";
    }
    @Override
    public String returnHello() {
        return "hello world!";
    }
    @Override
    public MsgInfo returnMsgInfo(MsgInfo info) {
        info.getMsgs().add("处理完毕");
        return info;
    }
}
