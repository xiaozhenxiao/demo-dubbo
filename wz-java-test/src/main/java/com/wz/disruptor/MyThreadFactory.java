package com.wz.disruptor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO
 * wangzhen23
 * 2018/1/4.
 */
public class MyThreadFactory extends AtomicLong implements ThreadFactory {
    /** */
    private static final long serialVersionUID = -8841098858898482335L;

    final String prefix;

    public static final ThreadFactory NONE = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            throw new AssertionError("No threads allowed.");
        }
    };

    public MyThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, prefix + incrementAndGet());
        t.setDaemon(true);
        return t;
    }
}
