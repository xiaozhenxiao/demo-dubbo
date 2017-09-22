package com.jd.jvm;

/**
 * 验证jdk1.6 和 jdk1.7的差别
 * 1.6   false false
 * 1.7   true false
 * wangzhen23
 * 2017/9/8.
 */
public class RuntimeConstantPoolOOM1 {
    public static void main(String[] args) {
        String str1 = new StringBuilder("计算机").append("软件").toString();
        System.out.println(str1.intern() == str1);

        /**
         * str2 指向的是堆上的地址
         * str2.intern返回的是常量池中的地址，所以两者不相等
         */
        String str2 = new StringBuilder("ja").append("va").toString();
        System.out.println(str2.intern() == str2);
    }
}
