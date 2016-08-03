package com.wz.jdk.java.thread.pool;

import java.sql.Time;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangzhen on 2016-08-03.
 */
public class ScheduledThreadPool {
    public static void main(String[] args) {
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(10);

        long initialDelay = 1;
        long period = 1;

        scheduled.schedule(new MyScheduledExecutor("schedule"), period, TimeUnit.SECONDS);

        scheduled.schedule(new MyCallable(), period, TimeUnit.SECONDS);

        // 从现在开始1秒钟之后，每隔1秒钟执行一次job1
        scheduled.scheduleAtFixedRate(new MyScheduledExecutor("job1"), initialDelay, period, TimeUnit.SECONDS);

        // 从现在开始2秒钟之后，任务结束后每隔2秒钟执行一次job2
        scheduled.scheduleWithFixedDelay(new MyScheduledExecutor("job2"), initialDelay, period, TimeUnit.SECONDS);
    }
}

class MyScheduledExecutor implements Runnable {

    private String jobName;

    MyScheduledExecutor() {

    }

    MyScheduledExecutor(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " " + jobName + " is running");
    }
}
