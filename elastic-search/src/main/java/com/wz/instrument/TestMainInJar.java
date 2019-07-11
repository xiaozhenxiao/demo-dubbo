package com.wz.instrument;

/**
 * main
 * wangzhen23
 * 2019/7/10.
 */
public class TestMainInJar {
    public static void main(String[] args) {
        System.out.println("---------------------------" + new TransClass().getNumber());
    }

    /*public static void main(String[] args) throws InterruptedException {
        System.out.println("===========" + new TransClass().getNumber());
        int count = 0;
        while (true) {
            Thread.sleep(500);
            count++;
            int number = new TransClass().getNumber();
            System.out.println("+++++++++++++++++" + number);
            if (3 == number || count >= 10) {
                break;
            }
        }
    }*/

}
