package com.wz.java.reflection;

/**
 * wangzhen23
 * 2017/7/15.
 */
public class User extends People{
    private int age;
    private String name;
    public Long hobby;

    public User() {
        super();
    }

    public User(String name) {
        super();
        this.name = name;
    }

    public User(int age, String name) {
        super();
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getHobby() {
        return hobby;
    }

    public void setHobby(Long hobby) {
        this.hobby = hobby;
    }

    @Override
    public String toString() {
        return "User [age=" + age + ", name=" + name + "]";
    }
}
