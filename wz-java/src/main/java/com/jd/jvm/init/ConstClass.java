package com.jd.jvm.init;

/**
 * TODO
 * wangzhen23
 * 2017/9/18.
 */
public class ConstClass {
    static {
        System.out.println("ConstClass init!");
    }
    public static final String HELLOWORLD = "hello world";
}
