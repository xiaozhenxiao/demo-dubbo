package com.jd.jvm.exec;

/**
 * TODO
 * wangzhen23
 * 2017/9/25.
 */
public class GenericTypes {
    public static void main(String[] args) {
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 128;
        Integer f = 128;
        Long g = 3L;
        Long h = 3L;
        System.out.println("c == d : " + (c == d));
        System.out.println("e == f " + (e == f));
        System.out.println("e ==2 f " + (e.equals(f)));
        System.out.println(c == (a + b));
        System.out.println(c == 3);
        System.out.println(c.equals(a + b));
        System.out.println(g == h);
        System.out.println(g == (a + b));
        System.out.println(g.equals(a + b));
    }
}
