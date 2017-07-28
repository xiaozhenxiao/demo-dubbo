package com.wz.java.lambda;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * wangzhen23
 * 2017/7/19.
 */
public class Person {

    String name;
    int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isStudent() {
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static void main(String args[]) {
        Person jt = new Person("name", 23);

        String s = "helloworld!";
        byte[] bt = null;
        bt = s.getBytes();
        ArrayList<String> list = new ArrayList<String>();
        list.add(s);
        bt = list.toString().getBytes();
        list = jt.getArrayList(bt);//这一行会出错。

    }


    public ArrayList<String> getArrayList(byte[] bt) {
        ArrayList<String> list = new ArrayList<String>();
        ObjectInputStream objIps;
        //注意这里，ObjectInputStream 对以前使用 ObjectOutputStream 写入的基本数据和对象进行反序列化。问题就在这里，你是直接将byte[]数组传递过来，而这个byte数组不是使用ObjectOutputStream类写入的。所以问题解决的办法就是：用输出流得到byte[]数组。
        try {
            objIps = new ObjectInputStream(new ByteArrayInputStream(bt));
            list = (ArrayList<String>) objIps.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public byte[] getByte(ArrayList<String> list) {
        byte[] bt = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            if (list != null) {
                ObjectOutputStream objos = new ObjectOutputStream(baos);
                objos.writeObject(list);
                bt = baos.toByteArray();
            }
        } catch (Exception e) {
            bt = (byte[]) null;
            e.printStackTrace();

        }
        return bt;
    }

}

