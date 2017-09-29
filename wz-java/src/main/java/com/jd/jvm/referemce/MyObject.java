package com.jd.jvm.referemce;

/**
 * 引用测试类
 * wangzhen23
 * 2017/9/26.
 */
public class MyObject {
    public Integer test;
    @Override
    public String toString() {
        return "I am MyObject";
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("MyObject's finalize called");
    }
}
