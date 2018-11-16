package com.jd.threadpool.one;

/**
 * 打印偶数
 * wangzhen23
 * 2018/7/3.
 */
public class EvenThread implements Runnable{
    NumMonitor numMonitor;

    public EvenThread(NumMonitor numMonitor) {
        this.numMonitor = numMonitor;
    }

    @Override
    public void run() {
        while (numMonitor.getStart() < numMonitor.getEnd()) {
            if (numMonitor.getStart() % 2 == 0) {
                synchronized (numMonitor) {
                    System.out.println("偶数:     " + numMonitor.getStart());
                    numMonitor.setStart(numMonitor.getStart() + 1);
                }
            }
        }
    }
}
