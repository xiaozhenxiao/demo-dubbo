package com.jd.threadpool.three;

/**
 * wangzhen23
 * 2018/7/3.
 */
public class NumMain {
    private int start;
    private int end = 100;
    private boolean flag = false;

    public static void main(String[] args) {
        NumMain numMain = new NumMain();
        Thread oddThread = new Thread(new OddObjThread(numMain));
        Thread evenThread = new Thread(new EvenObjThread(numMain));

        oddThread.start();
        evenThread.start();
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
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
