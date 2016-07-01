package com.wz.jdk.source.thread;

import java.util.concurrent.CyclicBarrier;

/**
 * Created by wangzhen on 2016-06-23.
 */

public class TestCyclicBarrier {

    private static final int THREAD_NUM = 5;

    public static class WorkerThread implements Runnable{

        CyclicBarrier barrier;

        public WorkerThread(CyclicBarrier b){
            this.barrier = b;
        }

        public void run() {
            try{
                System.out.println("Worker's waiting");
                //线程在这里等待，直到所有线程都到达barrier。
                barrier.await();
                System.out.println("ID:"+Thread.currentThread().getId()+" Working");
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        CyclicBarrier cb = new CyclicBarrier(THREAD_NUM, new Runnable() {
            //当所有线程到达barrier时执行
            public void run() {
                System.out.println("Inside Barrier");
            }
        });

        for(int i=0;i<THREAD_NUM;i++){
            new Thread(new WorkerThread(cb)).start();
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

