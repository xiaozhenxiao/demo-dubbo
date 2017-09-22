package com.jd.java.gc;

/**
 * TODO
 * wangzhen23
 * 2017/9/20.
 */
public class TestSlot {
    public static void main(String[] args) {
        {
            byte[] placeholder = new byte[64 * 1024 * 1024];
        }
        int a = 0;
        System.gc();
    }
}
