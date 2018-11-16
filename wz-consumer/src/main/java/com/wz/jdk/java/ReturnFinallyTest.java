package com.wz.jdk.java;

/**
 * Created by wangzhen on 2016-07-29.
 */
public class ReturnFinallyTest {
    public static void main(String[] args) {
        System.out.println(returnFinally());
//        System.out.println(returnFinally3());
//        System.out.println(test11());
//          System.out.println(getMap().get("KEY").toString());
//          System.out.println(test4());
    }

    public static int returnFinally(){
        int a = 10;
        try {
            return a = a + 20;
        } catch (Exception e) {
        } finally {
            a = a + 30;
        }
        return a;
    }

    /*public static String test11() {
        try {
//            System.out.println("try block");
            return test12();//先执行return,再执行finally,最后返回return
        } finally {
//            System.out.println("finally block");
        }
    }

    public static String test12() {
//        System.out.println("return statement");
        return "after return";
    }*/

    /*public static int returnFinally3() {
        int b = 20;
        try {
            System.out.println("try block");
            return b += 80;
        } catch (Exception e) {
            System.out.println("catch block");
        } finally {
            System.out.println("finally block");
            if (b > 25) {
                System.out.println("b>25, b = " + b);
            }
            b = 150; //此处不会被运行
        }
        return b;
    }*/

    /*public static Map<String, String> getMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("KEY", "INIT");
        try {
            map.put("KEY", "TRY");
            return map;
        }catch (Exception e) {
            map.put("KEY", "CATCH");
        }finally {
            map.put("KEY", "FINALLY");
            map = null;
        }
        return map;
    }*/

    /*public static int test4() {
        int b = 20;
        try {
            System.out.println("try block");
            b = b / 1;
            return b += 80;
        } catch (Exception e) {
            b += 15;
            System.out.println("catch block");
//            return b;
        } finally {
            System.out.println("finally block");
            if (b > 25) {
                System.out.println("b>25, b = " + b);
            }
            b += 50;
        }
        return b;
    }*/

}