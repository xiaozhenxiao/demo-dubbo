package com.jd.romote.exec.bytecode;

import java.io.Serializable;

/**
 * TODO
 * wangzhen23
 * 2017/9/22.
 */
public class Person implements Serializable{
    private static final String hobby = "music";
    private String firstName;
    private String lastName;
    private float weight = 88.999f;
    private int age = 188888888;
    private double money = 99999.7788;
    private long high = 5555566l;

    public Person(String firstName, String lastName, float weight) {
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

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }


    public int methodTest(){
        int result = 0;
        try {
            int i = 0;
            int j = 1;
            result = i + j;
        } catch (Exception e) {
            e.printStackTrace();
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return result;
    }
}