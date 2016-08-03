package com.wz.jdk.java.thread;

import java.util.PriorityQueue;

/**
 * Created by Administrator on 2016/7/9.
 * Date:2016-07-09
 */
public class ThreadCommunication {
    public static Object object = new Object();
    private int queueSize = 10;
    private PriorityQueue<Integer> queue = new PriorityQueue<Integer>(queueSize);

    public static void main(String[] args) {
        //用wait(),notiyf()实现的生产者和消费者系统
        ThreadCommunication test = new ThreadCommunication();
        Producer producer = test.new Producer();
        Consumer consumer = test.new Consumer();

        producer.start();
        consumer.start();

        /**
         * 测试wait(),notify()方法
         */
        Thread1 thread1 = new Thread1();
        Thread2 thread2 = new Thread2();

        thread1.start();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread2.start();
    }

    static class Thread1 extends Thread {
        @Override
        public void run() {
            synchronized (object) {
                System.out.println("Thread1线程" + Thread.currentThread().getName() + "获取到了object的锁");
                try {
                    System.out.println("Thread1线程" + Thread.currentThread().getName() + "调用wait释放锁");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    object.wait();
                } catch (InterruptedException e) {
                }

                System.out.println("Thread1线程" + Thread.currentThread().getName() + "获取到了锁");
            }
        }
    }

    static class Thread2 extends Thread {
        @Override
        public void run() {
            synchronized (object) {
                System.out.println("Thread2线程" + Thread.currentThread().getName() + "获取到了object的锁");
                object.notify();
                System.out.println("Thread2线程" + Thread.currentThread().getName() + "调用了object.notify()");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread2线程" + Thread.currentThread().getName() + "释放了锁");

            }

        }
    }

    class Consumer extends Thread {

        @Override
        public void run() {
            consume();
        }

        private void consume() {
            while (true) {
                synchronized (queue) {
                    while (queue.size() == 0) {
                        try {
                            System.out.println("队列空，等待数据");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            queue.notify();
                        }
                    }
                    queue.poll();          //每次移走队首元素
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    queue.notify();
                    System.out.println("从队列取走一个元素，队列剩余" + queue.size() + "个元素");
                }
            }
        }
    }

    class Producer extends Thread {

        @Override
        public void run() {
            produce();
        }

        private void produce() {
            while (true) {
                synchronized (queue) {
                    while (queue.size() == queueSize) {
                        try {
                            System.out.println("队列满，等待有空余空间");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            queue.notify();
                        }
                    }
                    queue.offer(1);        //每次插入一个元素
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    queue.notify();
                    System.out.println("向队列中插入一个元素，队列剩余空间：" + (queueSize - queue.size()));
                }
            }
        }
    }
}