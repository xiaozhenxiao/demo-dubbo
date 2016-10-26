package com.wz.java.designpatterns.Singleton;

import java.util.concurrent.TimeUnit;

/**
 * @author wangzhen
 * @version 1.0
 * @date 2016-10-26 10:26
 **/
public class Singleton {
    private Singleton() {}

    /**
     * 懒汉式单例类.在第一次调用的时候实例化自己
     */
    private static Singleton single=null;
    //静态工厂方法
    public static Singleton getInstance() {
        if (single == null) {
            single = new Singleton();
        }
        return single;
    }
    /**
     * 静态内部类
     */
    private static class LazyHolder {
        private static final Singleton INSTANCE = new Singleton();
    }
    public static final Singleton getStaticInstance() {
        return LazyHolder.INSTANCE;
    }


    public synchronized void methodA(){
        System.out.println("this is methodA! " + Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("this is methodA End! " + Thread.currentThread().getName());
    }
    public synchronized void methodB(){
        System.out.println("this is methodB! " + Thread.currentThread().getName());
        System.out.println("this is methodB End! " + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        Singleton singleton = Singleton.getStaticInstance();
        new Thread(()-> singleton.methodA()).start();
        new Thread(()-> singleton.methodB()).start();
    }
}
