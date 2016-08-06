package com.wz.jdk.java.jdk7;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/8/6.
 * Date:2016-08-06
 */
public class TestPhaser {
    public static void main(String[] args) {
        Phaser phaser = new Phaser(1); //此处可使用CountDownLatch(1)
        for(int i=0; i<3; i++) {
            new MypPaserThread((char)(97+i), phaser).start();
        }
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main thread runing ");
        phaser.arrive();  //此处可使用latch.countDown()
        System.out.println("main thread runed ");
    }
}

class MypPaserThread extends Thread {
    private char c;
    private Phaser phaser;

    public MypPaserThread(char c, Phaser phaser) {
        this.c = c;
        this.phaser = phaser;
    }

    @Override
    public void run() {
        phaser.awaitAdvance(phaser.getPhase()); //此处可使用latch.await()
        for(int i=0; i<100; i++) {
            System.out.print(c+" ");
            if(i % 10 == 9) {
                System.out.println();
            }
        }
    }
}
