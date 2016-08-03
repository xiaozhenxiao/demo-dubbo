package com.wz.jdk.java.thread;

/**
 * Created by wangzhen on 2016-08-02.
 */
public class TestSyncAndModify implements Runnable{
    private A syncA;

    @Override
    public void run() {
        synchronized (syncA ) {
            System.out.println(Thread.currentThread().getName() + syncA.toString() + " start");
//            syncA = new A();//锁住的是对中的对象，不是栈中的引用，当对象变了，syncA指向的堆中新的对象没有被锁住
            try {
                Thread. sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + syncA.toString() + " end");
        }
    }

    static class A {
    }

    public static void main(String[] args) {
        TestSyncAndModify sync = new TestSyncAndModify();
        A testA = new A();
        sync.setSyncA(testA);
        Thread one = new Thread(sync);
        Thread two = new Thread(sync);
        one.start();
        try {
            Thread. sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sync.setSyncA(new A());//锁住的是对中的对象，不是栈中的引用，当对象变了，syncA指向的堆中新的对象没有被锁住
        System.out.println("main: "+sync.getSyncA());
        System.out.println("main: "+sync.getSyncA().equals(testA));
        two.start();
    }

    public A getSyncA() {
        return syncA;
    }

    public void setSyncA(A syncA) {
        this.syncA = syncA;
    }
}
