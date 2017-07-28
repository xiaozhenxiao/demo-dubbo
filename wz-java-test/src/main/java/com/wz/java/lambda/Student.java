package com.wz.java.lambda;

/**
 * wangzhen23
 * 2017/7/19.
 */
public class Student{
    private Integer age;
    private Integer weight;

    public boolean isStudent() {
        return false;
    }

    public String getName(){
        return "name";
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public boolean isGood(){
        return true;
    }
}
