package com.jd.jvm.exec;

/**
 * 方法静态分派演示
 * wangzhen23
 * 2017/9/20.
 */
public class StaticDispatch {
    static abstract class Human{}

    static class Man extends Human{}

    static class Woman extends Human{}

    public void sayHello(Human guy){
        System.out.println("Hello, guy! " + guy);
    }

    public void sayHello(Man guy){
        System.out.println("Hello, gentleman! " + guy);
    }

    public void sayHello(Woman guy){
        System.out.println("Hello, lady! " + guy);
    }

    public static void main(String[] args) {
        Human man = new Man();
        Human woman = new Woman();

        StaticDispatch sd = new StaticDispatch();
        sd.sayHello(man);
        sd.sayHello(woman);

        sd.sayHello((Man) man);
        sd.sayHello((Woman) woman);
    }
}
