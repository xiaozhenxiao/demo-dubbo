package com.jd.jvm.init;

/**
 * wangzhen23
 * 2017/9/18.
 */
public class SubClass extends SuperClass{

    private String subString = "subString";
    static {
        System.out.println("SubClass static init!");
    }

    public SubClass() {
        System.out.println("sub class constructor:" + subString);
    }
}
