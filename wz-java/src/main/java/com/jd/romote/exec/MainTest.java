package com.jd.romote.exec;

/**
 * TODO
 * wangzhen23
 * 2017/9/22.
 */
public class MainTest {
    public static void main(String[] args) {
        String result = JavaClassExecuter.execute("E:/myworkspaces/idea-dubbo/wz-java/target/test-classes/JavaTest.class");
        System.out.println("=========================================");
        System.out.println("result = " + result);
        System.out.println("=========================================");
    }
}
