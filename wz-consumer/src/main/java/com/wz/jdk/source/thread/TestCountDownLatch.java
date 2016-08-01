package com.wz.jdk.source.thread;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch类位于java.util.concurrent包下，
 * 利用它可以实现类似计数器的功能。
 * 比如有一个任务A，它要等待其他4个任务执行完毕之后才能执行
 * Created by Administrator on 2016/7/9.
 * Date:2016-07-09
 * * 1）CountDownLatch和CyclicBarrier都能够实现线程之间的等待，只不过它们侧重点不同：
 * CountDownLatch一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行；
 * 而CyclicBarrier一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；
 * 另外，CountDownLatch是不能够重用的，而CyclicBarrier是可以重用的。
 * 2）Semaphore其实和锁有点类似，它一般用于控制对某组资源的访问权限。
 */
public class TestCountDownLatch {
    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(2);

        StartThread(latch);

        StartThread(latch);

        try {
            System.out.println("等待2个子线程执行完毕...");
            latch.await();
            System.out.println("2个子线程已经执行完毕");
            System.out.println("继续执行主线程");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void StartThread(final CountDownLatch latch) {
        new Thread(){
            public void run() {
                try {
                    System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
                    Thread.sleep(3000);
                    System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }
}
