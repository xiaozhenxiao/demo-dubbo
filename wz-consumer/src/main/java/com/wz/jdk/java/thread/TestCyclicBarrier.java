package com.wz.jdk.java.thread;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;

/**
 * 字面意思回环栅栏，通过它可以实现让一组线程等待至某个状态之后再全部同时执行。
 * 叫做回环是因为当所有等待线程都被释放以后，CyclicBarrier可以被重用
 * Created by wangzhen on 2016-06-23.
 * 1）CountDownLatch和CyclicBarrier都能够实现线程之间的等待，只不过它们侧重点不同：
 * CountDownLatch一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行；
 * 而CyclicBarrier一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；
 * 另外，CountDownLatch是不能够重用的，而CyclicBarrier是可以重用的。
 * 2）Semaphore其实和锁有点类似，它一般用于控制对某组资源的访问权限。
 */

public class TestCyclicBarrier {

    private static final int THREAD_NUM = 5;

    public static class WorkerThread implements Runnable{

//        CyclicBarrier barrier;
        Phaser phaser;

        public WorkerThread(Phaser b){
            this.phaser = b;
        }

        public void run() {
            try{
                System.out.println(Thread.currentThread().getName()+" Worker's waiting");
                //线程在这里等待，直到所有线程都到达barrier。
//                System.out.println("numberWaiting:" + barrier.getNumberWaiting());
//                System.out.println("Parties:" + barrier.getParties());
                phaser.arriveAndAwaitAdvance();
                System.out.println("ID:"+Thread.currentThread().getName()+" Working");
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        CyclicBarrier cb = new CyclicBarrier(THREAD_NUM, () -> {
            System.out.println(Thread.currentThread().getName()+" Inside Barrier");
        });

        Phaser phaser = new Phaser(THREAD_NUM);

        for(int i=0;i<THREAD_NUM;i++){
//            new Thread(new WorkerThread(cb)).start();
            new Thread(new WorkerThread(phaser)).start();
        }
    }

}
 /*
 以下是输出：
 Worker's waiting
 Worker's waiting
 Worker's waiting
 Worker's waiting
 Worker's waiting
 Inside Barrier
 ID:12 Working
 ID:8 Working
 ID:11 Working
 ID:9 Working
 ID:10 Working
 */

