package com.jd.romote.exec.bytecode;

import com.jd.romote.exec.bytecode.java.FieldInfo;

import java.io.Serializable;

/**
 * wangzhen23
 * 2017/9/22.
 */
public class Person implements Serializable{
    private static final String hobby = "music";
    private String firstName;
    private String lastName;
    private float weight = 88.999f;
    private int age = 18;
    private double money = 99999.7788;
    private long high = 5555566l;
    private FieldInfo fieldInfo;

    public int add(int a, int b){
        int i = a + b;
        int j = a * b;
        int k = i + j;
        return k + 1 + 2;
    }

    public int add1(int a, int b){
        int x = 0;
        if(a > b){
            x = 1;
//            x = x + a + b;
        }/*else if( b + a > a){
            x = a;
        }else if(a > 1){
            a = 0;
        }else if(b >5){
            b = 1;
        }*/
        return x + a + b;
    }

    public int getNum(int a, int b, int c){
        return a * b * c;
    }
    /*public Person(String firstName, String lastName, float weight) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.weight = weight;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int methodTest(){
        int result = 0;
        try {
            int i = getAge();
            int j = new MyObject().test;
            result = i + j;
        } catch (Exception e) {
            e.printStackTrace();
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return result;
    }*/
}