package com.jd.jmx.notification;

import com.jd.jmx.Hello;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * Agent  Notification
 * MBean之间的通信是必不可少的，Notification就起到了在MBean之间沟通桥梁的作用。JMX 的通知由四部分组成：
 * 1、Notification这个相当于一个信息包，封装了需要传递的信息
 * 2、Notification broadcaster这个相当于一个广播器，把消息广播出。
 * 3、Notification listener 这是一个监听器，用于监听广播出来的通知信息。
 * 4、Notification filiter 这个一个过滤器，过滤掉不需要的通知。这个一般很少使用。
 * wangzhen23
 * 2017/8/22.
 */
public class HelloAgent {
    public static void main(String[] args) throws JMException, Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName helloName = new ObjectName("yunge:name=Hello");
        Hello hello = new Hello();
        server.registerMBean(hello, helloName);
        Jack jack = new Jack();
        server.registerMBean(jack, new ObjectName("jack:name=Jack"));
        jack.addNotificationListener(new HelloListener(), null, hello);
        Thread.sleep(500000);
    }
}
