package com.wz.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * wangzhen23
 * 2019/7/10.
 */
public class Task implements Runnable{
    private String name;
    private ThreadLocal<String> threadLocal;

    public Task(String name, ThreadLocal threadLocal) {
        this.name = name;
        this.threadLocal = threadLocal;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " ;" + name + " ++++++++++++++++ " + threadLocal.get());
//        threadLocal.set("task inner");
//        System.out.println(Thread.currentThread().getName() + " ;" + "task inner !!!!!!!!! " + threadLocal.get());
    }
}
