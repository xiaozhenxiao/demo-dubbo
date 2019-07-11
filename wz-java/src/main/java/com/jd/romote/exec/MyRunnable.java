package com.jd.romote.exec;

/**
 * i++
 * wangzhen23
 * 2019/2/25.
 */
public class MyRunnable implements Runnable {
    public static Integer i = new Integer(0);

    @Override
    public void run() {
        while (true) {
            synchronized (i) {
                if (i < 100) {
                    i++;
                    System.out.println(" i = " + i);
                } else {
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new MyRunnable());
        Thread t2 = new Thread(new MyRunnable());
        t1.start();
        t2.start();
    }
}
