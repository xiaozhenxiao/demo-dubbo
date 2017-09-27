package com.jd.jvm.init;

/**
 * TODO
 * wangzhen23
 * 2017/9/18.
 */
public class SuperClass {
    public static int value = 123;
    private String superString = "superString";
    static {
        System.out.println("SuperClass static init!");
    }

    public SuperClass() {
        System.out.println("super Class constructor:" + superString);
    }
}
