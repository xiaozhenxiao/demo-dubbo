package com.wz.java.classloader;

import com.alibaba.fastjson.JSON;

/**
 * TODO
 * wangzhen23
 * 2017/7/29.
 */
public class ClassLoaderTest {
    public static void main(String[] args) {
        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
        System.out.println(classLoader.getParent().getParent());
        System.out.println(classLoader.getParent());
        System.out.println(classLoader);

        System.out.println(ClassLoaderTest.class.getClassLoader().getResource(""));
    }
}
