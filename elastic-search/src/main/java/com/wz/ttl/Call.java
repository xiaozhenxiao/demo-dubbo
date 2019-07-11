package com.wz.ttl;

import java.util.concurrent.Callable;

/**
 * Callable
 * wangzhen23
 * 2019/7/10.
 */
public class Call implements Callable<String> {
    private String name;
    private ThreadLocal<String> threadLocal;

    public Call(String name, ThreadLocal threadLocal) {
        this.name = name;
        this.threadLocal = threadLocal;
    }

    @Override
    public String call() {
        System.out.println(Thread.currentThread().getName() + " ;" + name + " ============= " + threadLocal.get());
        return name;
    }
}
