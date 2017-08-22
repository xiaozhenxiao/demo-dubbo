package com.jd.timer;

import java.util.Date;
import java.util.TimerTask;

/**
 * JDK TimerTask
 * wangzhen23
 * 2017/8/21.
 */
public class SimpleTimerTask extends TimerTask{
    private int count = 0;
    @Override
    public void run() {
        System.out.println("execute task.");
        Date exeTime = new Date(scheduledExecutionTime());
        System.out.println("本次任务安排执行时间点为：" + exeTime);
        if(++count > 10) {
            cancel();
        }
    }
}
