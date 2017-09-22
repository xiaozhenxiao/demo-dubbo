package com.jd.jvm.exec;

import java.io.Serializable;

/**
 * 重载方法匹配优先级
 * wangzhen23
 * 2017/9/20.
 */
public class Overload {
//    public static void sayHello(Object arg){
//        System.out.println("Hello Object");
//    }

//    public static void sayHello(int arg){
//        System.out.println("Hello int");
//    }

//    public static void sayHello(long arg){
//        System.out.println("Hello long");
//    }

//    public static void sayHello(Character arg){
//        System.out.println("Hello Character");
//    }

//    public static void sayHello(char arg){
//        System.out.println("Hello char");
//    }

//    public static void sayHello(char... arg){
//        System.out.println("Hello char...");
//    }

    public static void sayHello(long... arg){
        System.out.println("Hello int...");
    }

//    public static void sayHello(Serializable arg){
//        System.out.println("Hello Serializable");
//    }

//    public static void sayHello(Comparable arg){
//        System.out.println("Hello Comparable");
//    }

    public static void main(String[] args) {
        sayHello('a');
    }
}
