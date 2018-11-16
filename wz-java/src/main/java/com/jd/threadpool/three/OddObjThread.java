package com.jd.threadpool.three;

/**
 * 打印奇数,基于object.wait/notify
 * wangzhen23
 * 2018/7/3.
 */
public class OddObjThread implements Runnable {
    private NumMain numMonitor;

    public OddObjThread(NumMain object) {
        this.numMonitor = object;
    }

    @Override
    public void run() {
        while (numMonitor.getStart() < numMonitor.getEnd()) {
            synchronized (numMonitor) {
                if (numMonitor.isFlag()) {
                    System.out.println("奇数:" + numMonitor.getStart());
                    numMonitor.setStart(numMonitor.getStart() + 1);
                    numMonitor.setFlag(false);
                    numMonitor.notify();
                }else {
                    try {
                        numMonitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
