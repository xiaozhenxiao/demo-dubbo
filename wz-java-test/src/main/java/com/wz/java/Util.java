package com.wz.java;

import com.wz.java.lambda.Person;

/**
 * TODO
 * wangzhen23
 * 2017/7/20.
 */
public class Util {
    public static int test(Person a) {
        Integer aa = 0;
        Integer bb = 10;
        try {
            aa = a.getAge();
            bb = a.getAge() * a.getAge();
            System.out.println("===================" + aa);
            System.out.println("-------------------" + bb);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aa + bb;
    }
}
