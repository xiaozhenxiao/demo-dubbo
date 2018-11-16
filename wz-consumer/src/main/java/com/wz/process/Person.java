package com.wz.process;

import com.smile.wz.processor.Getter;

/**
 * wangzhen23
 * 2018/11/8.
 */
@Getter
public class Person {
    private String id;
    private Integer age;

    public void setId(String id) {
        this.id = id;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
