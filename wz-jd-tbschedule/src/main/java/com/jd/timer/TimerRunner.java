package com.jd.timer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * JDK Timer
 * wangzhen23
 * 2017/8/21.
 */
public class TimerRunner {
    public static void main(String[] args) {
        Timer timer = new Timer(true);
        TimerTask timerTask = new SimpleTimerTask();
        timer.schedule(timerTask, 1000L, 5000L);
    }
}
