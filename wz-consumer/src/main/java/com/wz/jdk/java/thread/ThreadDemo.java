package com.wz.jdk.java.thread;

/**
 * Created by wangzhen on 2016-08-02.
 */
public class ThreadDemo {
    public static void main(String[] args) {
        Thread mythread = new MyselfThread("mythread");

        System.out.println(Thread.currentThread().getName()+" call mythread.run()");
        mythread.run();

        System.out.println(Thread.currentThread().getName()+" call mythread.start()");
        mythread.start();

        ThreadDemo demo = new ThreadDemo();
        try {
            demo.waitTest(6000);
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        demo.notifyTest();

    }

    public synchronized void waitTest(long mill) throws InterruptedException {
        System.out.println("=================wait================");
        wait(mill);
    }

    public synchronized void notifyTest(){
        System.out.println("=================notify===============");
        notify();
    }
}
class MyselfThread extends Thread{
    public MyselfThread(String name) {
        super(name);
    }

    public void run(){
        System.out.println(Thread.currentThread().getName()+" is running");
    }
}
