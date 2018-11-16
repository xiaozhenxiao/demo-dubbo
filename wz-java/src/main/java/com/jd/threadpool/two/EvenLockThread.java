package com.jd.threadpool.two;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 打印偶数，基于锁
 * wangzhen23
 * 2018/7/3.
 */
public class EvenLockThread implements Runnable {
    ReentrantLock lock;
    Num num;


    public EvenLockThread(ReentrantLock lock, Num num) {
        this.lock = lock;
        this.num = num;
    }

    @Override
    public void run() {
        while (num.getStart() < num.getEnd()) {
            lock.lock();
            try {
                if (!num.isFlag()) {
                    System.out.println("偶数:     " + num.getStart());
                    num.setStart(num.getStart() + 1);
                    num.setFlag(true);
                } else {
//                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                }
            } finally {
                lock.unlock();
            }


        }
    }
}
