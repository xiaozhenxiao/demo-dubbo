package com.wz.java.designpatterns.Singleton;

/**
 * TODO
 * wangzhen23
 * 2017/9/22.
 */
public class Singleton1 {
    private static Singleton1 instance = new Singleton1();
    private Singleton1(){
        System.out.println("初始化Singleton1……");
    }

    public static Singleton1 getInstance() {
        try {
            Thread.sleep(2000);
            System.out.println("Singleton饿汉睡了2秒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return instance;
    }
}
