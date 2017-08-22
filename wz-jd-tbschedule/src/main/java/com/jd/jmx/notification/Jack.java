package com.jd.jmx.notification;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

/**
 * 资源对象
 * wangzhen23
 * 2017/8/22.
 */
public class Jack  extends NotificationBroadcasterSupport implements JackMBean{
    private int seq = 0;
    public void hi()
    {
        System.out.println("I'm jack, i'm saying hello to you!");
        //创建一个信息包
        Notification notify =
                //通知名称；谁发起的通知；序列号；发起通知时间；发送的消息
                new Notification("jack.hi",this,++seq,System.currentTimeMillis(),"jack");
        sendNotification(notify);
    }
}
