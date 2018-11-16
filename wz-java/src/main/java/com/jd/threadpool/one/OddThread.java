package com.jd.threadpool.one;

/**
 * 打印奇数
 * wangzhen23
 * 2018/7/3.
 */
public class OddThread implements Runnable {
    NumMonitor numMonitor;

    public OddThread(NumMonitor numMonitor) {
        this.numMonitor = numMonitor;
    }

    @Override
    public void run() {
        while (numMonitor.getStart() < numMonitor.getEnd()) {
            if (numMonitor.getStart() % 2 == 1) {
                synchronized (numMonitor) {
                    System.out.println("奇数:" + numMonitor.getStart());
                    numMonitor.setStart(numMonitor.getStart() + 1);
                }
            }
        }
    }
}
