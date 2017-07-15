package com.wz.java.clazz;

/**
 * 对 Java  的  class.cast 的测试
 * Created by wangzhen on 2016-07-13.
 */
public class ClassDemoCast {
        /** 输出为
         * class:: class com.wz.java.clazz.ClassDemoCast
        Class B show() function
        class com.wz.java.clazz.ClassDemoCast$A
        class com.wz.java.clazz.ClassDemoCast$B-bbb
        class com.wz.java.clazz.ClassDemoCast$B-aaa
        Class B show() function
         */
        public static void main(String[] args) {
            ClassDemoCast cls = new ClassDemoCast();
            Class c = cls.getClass();
            System.out.println("class:: " + c);

            Object obj = new A();
            B b1 = new B();
            b1.show();
            // casts object  对B进行强转
            A a = A.class.cast(b1);
            System.out.println(obj.getClass());
            System.out.println(b1.getClass()+"-"+b1.name);
            System.out.println(a.getClass()+"-"+a.name);
            a.show();
        }
   static class A {
        public String name = "aaa";
        public void show() {
            System.out.println("Class A show() function");
        }
    }

    static class B extends A {
        public String name = "bbb";
        public void show() {
            System.out.println("Class B show() function");
        }
    }
}
