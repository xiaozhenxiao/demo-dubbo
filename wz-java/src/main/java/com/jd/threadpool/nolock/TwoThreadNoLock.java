package com.jd.threadpool.nolock;

/**
 * 不使用锁实现交替输出奇偶数
 * wangzhen23
 * 2018/7/11.
 */
public class TwoThreadNoLock {
    private int start = 1;
    private int end = 51;
    private volatile boolean flag = false;

    public static void main(String[] args) {
        TwoThreadNoLock noLock = new TwoThreadNoLock();

        Thread t1 = new Thread(new TwoThreadNoLock.OddThread(noLock));
        t1.setName("odd");


        Thread t2 = new Thread(new TwoThreadNoLock.EvenThread(noLock));
        t2.setName("even");

        t1.start();
        t2.start();
    }


    public static class OddThread implements Runnable {
        TwoThreadNoLock noLock;

        public OddThread(TwoThreadNoLock noLock) {
            this.noLock = noLock;
        }

        @Override
        public void run() {
            while (noLock.start <= 100) {
                if (!noLock.flag) {
                    System.out.println(Thread.currentThread().getName() + "+-+" + noLock.start);
                    noLock.start++;
                    noLock.flag = true;
                } else {
                }
            }
        }
    }

    public static class EvenThread implements Runnable {
        TwoThreadNoLock noLock;

        public EvenThread(TwoThreadNoLock noLock) {
            this.noLock = noLock;
        }

        @Override
        public void run() {
            while (noLock.start <= 100) {
                if (noLock.flag) {
                    System.out.println(Thread.currentThread().getName() + "     +-+  " + noLock.start);
                    noLock.start++;
                    noLock.flag = false;

                } else {
                }
            }
        }
    }
}
