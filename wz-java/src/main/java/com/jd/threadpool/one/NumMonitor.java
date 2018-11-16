package com.jd.threadpool.one;

/**
 * 主类
 * wangzhen23
 * 2017/12/19.
 */
public class NumMonitor {
    private int start;
    private int end = 51;
    public static void main(String[] args) throws InterruptedException {
        NumMonitor numMonitor = new NumMonitor();
        Thread oddThread = new Thread(new OddThread(numMonitor));
        Thread evenThread = new Thread(new EvenThread(numMonitor));
        oddThread.start();
        evenThread.start();

        oddThread.join();
        evenThread.join();
        System.out.println("main Thread^^^^^?><^&*()_+$#@!");
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
}
