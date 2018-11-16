package com.jd.threadpool.two;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 数据实体
 * wangzhen23
 * 2018/7/3.
 */
public class Num {
    private int start;
    private int end;

    private boolean flag = true;

    public Num(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public static void main(String[] args) {
        Num num = new Num(1, 100);
        ReentrantLock lock = new ReentrantLock();
        Thread oddThread = new Thread(new OddLockThread(lock, num), "odd");
        Thread evenThread = new Thread(new EvenLockThread(lock, num), "even");
        oddThread.start();
        evenThread.start();
    }
}
