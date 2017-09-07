package com.wz.java.threadLocal;

/**
 * TODO
 * wangzhen23
 * 2017/8/30.
 */
public class ThreadLocalExample {
    public static class MyRunnable implements Runnable {
        private ThreadLocal threadLocal = new ThreadLocal();
        public void run() {
            int i = (int) (Math.random() * 100D);
            threadLocal.set(i);
            System.out.println(threadLocal + "---" + Thread.currentThread().getName() + " set- " + i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {

            }
            System.out.println(threadLocal + "---" + Thread.currentThread().getName() + " - " + threadLocal.get());
        }
    }

    public static void main(String[] args) {
        MyRunnable sharedRunnableInstance = new MyRunnable();
        Thread thread1 = new Thread(sharedRunnableInstance);
        Thread thread2 = new Thread(sharedRunnableInstance);
        thread1.start();
        thread2.start();
    }
}
