package com.wz.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerLock {

        private int count = 0;
        private final static int FULL = 10;
        private Lock lock;
        private Condition notEmptyCondition;
        private Condition notFullCondition;
        {
            lock = new ReentrantLock();
            notEmptyCondition = lock.newCondition();
            notFullCondition = lock.newCondition();

        }

        class Producer implements Runnable {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lock.lock();
                    try {
                        while(count == FULL) {
                            try {
                                notFullCondition.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        count++;
                        System.out.println("生产者 " + Thread.currentThread().getName() + " 总共有 " + count + " 个资源");
                        notEmptyCondition.signal();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }

        class Consumer implements Runnable {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lock.lock();
                    try {
                        while(count == 0) {
                            try {
                                notEmptyCondition.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        count--;
                        System.out.println("消费者 " + Thread.currentThread().getName() + " 总共有 " + count + " 个资源");
                        notFullCondition.signal();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }

        public static void main(String[] args) {
            ProducerConsumerLock demo = new ProducerConsumerLock();
            for (int i = 1; i <= 5; i++) {
                new Thread(demo.new Producer(), "生产者-" + i).start();
                new Thread(demo.new Consumer(), "消费者-" + i).start();
            }
        }

}
