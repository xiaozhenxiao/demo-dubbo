package com.wz.jdk.java.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
        CountDownLatch latch = new CountDownLatch(1);

        StartThread(latch);

        StartThread(latch);

        System.out.println("等待主线程执行countDown...");
        try {
            TimeUnit.SECONDS.sleep(7);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程执行countDown完毕");
        latch.countDown();
        System.out.println("执行主线程");

    }

    private static void StartThread(final CountDownLatch latch) {
        new Thread(){
            public void run() {
                try {
                    latch.await();
                    System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
                    Thread.sleep(3000);
                    System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
