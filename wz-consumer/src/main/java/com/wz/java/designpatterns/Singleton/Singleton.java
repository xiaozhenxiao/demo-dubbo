package com.wz.java.designpatterns.Singleton;

/**
 * 饿汉式 private static Singleton instance=new Singleton();  初始化时即创建实例
 * 懒汉式 if (single == null) { single = new Singleton(); } 多线程时须加锁，造成锁竞争
 * 静态内部内 只有在第一次调用的时候才创建实例
 * @author wangzhen
 * @version 1.0
 * @date 2016-10-26 10:26
 **/
public class Singleton {
    private Singleton() {
        System.out.println("初始化了 Singleton");
    }

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
        private static Singleton INSTANCE = new Singleton();

        public LazyHolder(){
            System.out.println("初始化LazyHolder…………");
        }
    }
    public static Singleton getStaticInstance() {
        try {
            Thread.sleep(2000);
            System.out.println("Singleton静态内部类睡了2秒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return LazyHolder.INSTANCE;
    }


    public synchronized void methodA(){
        System.out.println("this is methodA! " + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("this is methodA End! " + Thread.currentThread().getName());
    }
    public synchronized void methodB(){
        System.out.println("this is methodB! " + Thread.currentThread().getName());
        System.out.println("this is methodB End! " + Thread.currentThread().getName());
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main Class start…………");
        Singleton1 singleton1 = Singleton1.getInstance();
        Singleton singleton = Singleton.getStaticInstance();
        Thread.sleep(2000);
//        new Thread(()-> singleton.methodA()).start();
//        new Thread(()-> singleton.methodB()).start();

    }
}
