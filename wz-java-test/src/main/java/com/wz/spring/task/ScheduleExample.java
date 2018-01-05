package com.wz.spring.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * TODO
 * wangzhen23
 * 2018/1/2.
 */
@Component
public class ScheduleExample {

    int i = 0;

    @Scheduled(cron = "0/5 * * * * ? ")
    public void doJob(){
        System.out.println(Thread.currentThread().getName() + " " + i++);
    }
}
