package com.wz;

import com.wz.process.Person;
import com.wz.process.Son;
import com.wz.process.TestMethod;

/**
 * wangzhen23
 * 2018/11/8.
 */
public class Main {
    public static void main(String[] args) {
//        Person p = new Person();
//        p.setAge(100);
//        p.setId("111222333");
//        System.out.println("id:" + p.getId());

        TestMethod method = new TestMethod();

        Person p = new Person();
        p.setId("1111111111");
        p.setAge(100);
        p.setTelephone("138xxxxxxxx");

        Son son = new Son();
        son.setAge(100);
        son.setName("xiaoxiao");
        son.setMobilePhone("183yyyyyyyy");
        son.setMailbox("xiao@163.com");
        p.setSon(son);

        method.method("a_zhen@126.com", "183xxxxxxxx", "333");
        method.method1(p);
        method.method2(1, 2, 3f, 4f, 5d, 6);
        System.out.println("一天的秒数：" + method.method3());

    }
}
