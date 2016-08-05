package com.wz.jdk.java.jdk8;

/**
 * Created by wangzhen on 2016-08-05.
 */
public class LambdaTest {
    public static void main(String... args) {
        Hello h = new Hello();
        h.r.run();
    }
}
class Hello {
    /*public Runnable r = new Runnable() {
        public void run() {
            System.out.println(this);
            System.out.println(toString());
            System.out.println("===============================");
            System.out.println(Hello.this);
            System.out.println(Hello.this.toString());
        }
    };*/

    public Runnable r = () -> {
        System.out.println(this);
        System.out.println(toString());
    };

    public String toString() {
        return "Hello's custom toString()";
    }
}