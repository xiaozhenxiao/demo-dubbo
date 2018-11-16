package com.wz.dubbo.api;

import com.smile.wz.processor.Getter;

import java.io.Serializable;

/**
 * wangzhen23
 * 2018/3/20.
 */
public class Person implements Serializable{
    private String id;
    private String name;
    private Integer age;

    public Person() {
    }

    public Person(String id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Person [ id=" + this.id +",name=" + this.name + ",age=" + this.age + " ]";
    }
}
