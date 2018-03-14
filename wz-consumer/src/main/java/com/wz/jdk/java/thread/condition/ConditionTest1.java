package com.wz.jdk.java.thread.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO
 * wangzhen23
 * 2018/3/14.
 */
public class ConditionTest1 {
    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public static void main(String[] args) {
        ThreadA ta = new ThreadA("ta");
        lock.lock();
        // 获取锁
        try {
            System.out.println(Thread.currentThread().getName() + " start ta");
            ta.start();
            System.out.println(Thread.currentThread().getName() + " block");
            condition.await(); // 等待
            System.out.println(Thread.currentThread().getName() + " continue");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock(); // 释放锁
        }
    }

    static class ThreadA extends Thread {
        public ThreadA(String name) {
            super(name);
        }

        public void run() {
            lock.lock(); // 获取锁
            try {
                System.out.println(Thread.currentThread().getName() + " wakup others");
                condition.signal(); // 唤醒“condition所在锁上的其它线程”
            } finally {
                lock.unlock(); // 释放锁
            }
        }
    }
}
