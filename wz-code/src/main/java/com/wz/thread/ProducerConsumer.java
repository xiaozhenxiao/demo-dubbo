package com.wz.thread;

public class ProducerConsumer {

    private final static String LOCK = "lock";

    private static int count = 0;

    private static final int FULL = 10;

    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            new Thread(new Producer(), "生产者-" + i).start();
            new Thread(new Consumer(), "消费者-" + i).start();
        }

    }

    static class Producer implements Runnable {

        @Override
        public void run() {

            for (int i = 0; i < 10; i++) {

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (LOCK) {

                    while (count == FULL) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    count++;
                    System.out.println("生产者 " + Thread.currentThread().getName() + " 总共有 " + count + " 个资源");
                    LOCK.notifyAll();

                }

            }

        }

    }

    static class Consumer implements Runnable {

        @Override
        public void run() {

            for (int i = 0; i < 10; i++) {

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (LOCK) {

                    while (count == 0) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    count--;
                    System.out.println("消费者 " + Thread.currentThread().getName() + " 总共有 " + count + " 个资源");
                    LOCK.notifyAll();

                }

            }

        }

    }

}
