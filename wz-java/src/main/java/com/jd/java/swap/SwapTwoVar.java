package com.jd.java.swap;

/**
 * 不适用中间变量交换两个变量的值
 * wangzhen23
 * 2018/7/17.
 */
public class SwapTwoVar {
    public static void main(String[] args) {
        int a = 3, b = 6;
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        System.out.println("a=" + a);
        System.out.println("b=" + b);

        int n = 3846;
        System.out.println("任何数和0做异或结果是自身:" + (n ^ 0));
        System.out.println("任何数和-1做与结果是自身:" + (n & -1));
        System.out.println("任何数和0做或结果是自身:" + (n | 0));

    }
}
