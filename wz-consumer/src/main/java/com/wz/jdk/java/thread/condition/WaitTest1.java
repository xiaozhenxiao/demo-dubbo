package com.wz.jdk.java.thread.condition;

/**
 * 通过Object的wait(), notify()来演示线程的休眠/唤醒功能。
 * wangzhen23
 * 2018/3/14.
 */
public class WaitTest1 {
    public static void main(String[] args) {
        ThreadA ta = new ThreadA("ta");
        synchronized (ta) {
            // 通过synchronized(ta)获取“对象ta的同步锁”
            try {
                System.out.println(Thread.currentThread().getName() + " start ta");
                ta.start();
                System.out.println(Thread.currentThread().getName() + " block");
                ta.wait(); // 等待
                System.out.println(Thread.currentThread().getName() + " continue");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class ThreadA extends Thread {
        public ThreadA(String name) {
            super(name);
        }

        public void run() {
            synchronized (this) {
                // 通过synchronized(this)获取“当前对象的同步锁”
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " wakup others");
                notify();
                // 唤醒“当前对象上的等待线程”
            }
        }
    }
}
