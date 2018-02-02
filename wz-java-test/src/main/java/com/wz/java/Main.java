package com.wz.java;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wz.java.lambda.Person;
import org.apache.curator.utils.CloseableExecutorService;

import java.util.ArrayList;
import java.util.concurrent.Executors;

/**
 * wangzhen23
 * 2017/7/20.
 */
public class Main {
    public static void main(String[] args) {
        Person jt = new Person("name", 55);

        String s = "helloworld!";
        byte[] bt = null;

        ArrayList<String> list = new ArrayList<String>();

        ArrayList<String> byte_list = new ArrayList<String>();

        byte_list.add(s);

        bt = jt.getByte(byte_list);//通过调用getByte()方法得到bt[]数组。
        list = jt.getArrayList(bt);

        int i16 = 0x1;
        int i16_2 = 0x2;
        System.out.println(Thread.currentThread().getName() + "============" + (i16 + i16_2));

        loop();

        while (true){

        }
}

    public static void loop() {
        CloseableExecutorService executorService = new CloseableExecutorService(Executors.newSingleThreadExecutor((new ThreadFactoryBuilder()).setNameFormat("xiao-%d").setDaemon(true).build()), true);

        executorService.submit(() -> {
                try {
                    doWorkLoop();
                } finally {
                    System.out.println(Thread.currentThread().getName() + "-------------------finally");
                    loop();
                }
                return null;
        });
    }

    private static void doWorkLoop() {
        int i = 0;
        System.out.println(Thread.currentThread().getName() + "=====================doWorkLoop");
        while (true) {
            i++;
            if (i % 10 == 0) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
