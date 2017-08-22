package com.jd.jmx.notification;

import com.jd.jmx.Hello;

import javax.management.Notification;
import javax.management.NotificationListener;

/**
 * TODO
 * wangzhen23
 * 2017/8/22.
 */
public class HelloListener implements NotificationListener {
    @Override
    public void handleNotification(Notification notification, Object handback) {
        //execute this method when listener receive notification
        System.out.println("Type=" + notification.getType()
                + ";\nSource=" + notification.getSource()
                + ";\nSequenceNumber=" + notification.getSequenceNumber()
                + "\nSend time=" + notification.getTimeStamp()
                + "\nMessage=" + notification.getMessage());

        if(handback instanceof Hello)
        {
            Hello hello = (Hello)handback;
            hello.printHello(notification.getMessage());
        }
    }
}
