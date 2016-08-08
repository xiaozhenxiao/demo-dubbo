package com.wz.jdk.java.jdk8;

/**
 * Created by wangzhen on 2016-08-08.
 */
public class LambdaFunctionalMethod {
    public static void main(String[] args) {
        A a = () -> System.out.println("A");
        B b = () -> System.out.println("B");

        a.apply();
        b.apply();

        A aa = () -> {
            System.out.println("A=================A");
            System.out.println("do something other");
        };
        aa.apply();
    }
}
@FunctionalInterface
interface A{
    void apply();
}
interface B extends A{
    @Override
    void apply();
    //B和A中只能有一个虚拟的功能函数声明
//    void illegal();
    //B和A中只能有一个虚拟的功能函数声明
//    void doSoming(String ss);
}