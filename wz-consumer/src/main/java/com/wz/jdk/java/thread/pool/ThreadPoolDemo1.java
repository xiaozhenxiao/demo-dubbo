package com.wz.jdk.java.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangzhen on 2016-08-02.
 */
public class ThreadPoolDemo1 {

        public static void main(String[] args) {
            // 创建一个可重用固定线程数的线程池
            ExecutorService pool = Executors.newFixedThreadPool(2);
            // 创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口
            Thread ta = new MyDemoThread();
            Thread tb = new MyDemoThread();
            Thread tc = new MyDemoThread();
            Thread td = new MyDemoThread();
            Thread te = new MyDemoThread();
            // 将线程放入池中进行执行
            pool.execute(ta);
            pool.execute(tb);
            pool.execute(tc);
            pool.execute(td);
            pool.execute(te);

            pool.submit(ta);
            // 关闭线程池
            pool.shutdown();
        }
}
class MyDemoThread extends Thread {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+ " is running.");
    }
}