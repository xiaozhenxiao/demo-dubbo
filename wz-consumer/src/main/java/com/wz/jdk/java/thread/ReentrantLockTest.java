package com.wz.jdk.java.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wangzhen on 2016-08-01.
 */
class Depot {
    private int size;        // 仓库的实际数量
    private Lock lock;        // 独占锁

    public Depot() {
        this.size = 0;
        this.lock = new ReentrantLock();
    }

    public void produce(int val) {
        lock.lock();
        System.out.println(Thread.currentThread().getName() + "produce 上锁");
        try {
            size += val;
            System.out.printf("%s produce(%d) --> size=%d\n",
                Thread.currentThread().getName(), val, size);
        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + "produce 解锁");
        }
    }

    public void consume(int val) {
        lock.lock();
        System.out.println(Thread.currentThread().getName() + "consume 上锁");
        try {
        size -= val;
        System.out.printf("%s consume(%d) <-- size=%d\n",
                Thread.currentThread().getName(), val, size);
        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + "consume 解锁");
        }
    }
};

// 生产者
class Producer {
    private Depot depot;

    public Producer(Depot depot) {
        this.depot = depot;
    }

    // 消费产品：新建一个线程向仓库中生产产品。
    public void produce(final int val) {
        new Thread() {
            public void run() {
                depot.produce(val);
            }
        }.start();
    }
}

// 消费者
class Customer {
    private Depot depot;

    public Customer(Depot depot) {
        this.depot = depot;
    }

    // 消费产品：新建一个线程从仓库中消费产品。
    public void consume(final int val) {
        new Thread() {
            public void run() {
                depot.consume(val);
            }
        }.start();
    }
}

public class ReentrantLockTest {
    public static void main(String[] args) {
        Depot mDepot = new Depot();
        Producer mPro = new Producer(mDepot);
        Customer mCus = new Customer(mDepot);

        mPro.produce(60);
        mPro.produce(120);
        mCus.consume(90);
        mCus.consume(150);
        mPro.produce(110);
    }
}
