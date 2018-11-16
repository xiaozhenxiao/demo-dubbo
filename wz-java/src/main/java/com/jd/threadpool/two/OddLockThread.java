package com.jd.threadpool.two;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 打印奇数，基于锁
 * wangzhen23
 * 2018/7/3.
 */
public class OddLockThread implements Runnable {
    ReentrantLock lock;
    Num num;

    public OddLockThread(ReentrantLock lock, Num num) {
        this.lock = lock;
        this.num = num;
    }

    @Override
    public void run() {
        while (num.getStart() < num.getEnd()) {
            lock.lock();
            try {
                if (num.isFlag()) {
                    System.out.println("奇数: " + num.getStart());
                    num.setStart(num.getStart() + 1);
                    num.setFlag(false);
                } else {
//                System.out.println("****************");
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
