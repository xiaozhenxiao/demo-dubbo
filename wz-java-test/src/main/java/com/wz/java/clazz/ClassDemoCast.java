package com.wz.java.clazz;

/**
 * Created by wangzhen on 2016-07-13.
 */
public class ClassDemoCast {

        public static void main(String[] args) {
            ClassDemoCast cls = new ClassDemoCast();
            Class c = cls.getClass();
            System.out.println("class:: " + c);

            Object obj = new A();
            B b1 = new B();
            b1.show();
            // casts object
            A a = A.class.cast(b1);
            System.out.println(obj.getClass());
            System.out.println(b1.getClass()+"-"+b1.name);
            System.out.println(a.getClass()+"-"+a.name);
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
