package com.wz.java;

import com.wz.java.lambda.Person;

import java.util.ArrayList;

/**
 * wangzhen23
 * 2017/7/20.
 */
public class Main {
    public static void main(String[] args) {
        Person jt = new Person("name", 55);

        String s = "helloworld!";
        byte[] bt = null;

        ArrayList<String> list = new ArrayList<String>();

        ArrayList<String> byte_list= new ArrayList<String>();

        byte_list.add(s);

        bt=jt.getByte(byte_list);//通过调用getByte()方法得到bt[]数组。
        list=jt.getArrayList(bt);
    }
}
