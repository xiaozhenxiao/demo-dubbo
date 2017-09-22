package com.jd.jvm.init;

/**
 * TODO
 * wangzhen23
 * 2017/9/18.
 */
public class SuperClass {
    public static int value = 123;
    static {
        System.out.println("SuperClass init!");
    }
}
