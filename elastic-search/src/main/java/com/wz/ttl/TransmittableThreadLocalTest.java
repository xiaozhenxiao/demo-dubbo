package com.wz.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import com.alibaba.ttl.threadpool.TtlExecutors;
import org.slf4j.MDC;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TransmittableThreadLocal
 * wangzhen23
 * 2019/7/10.
 */
public class TransmittableThreadLocalTest {

    public static void main(String[] args) {
        ThreadLocal<String> parent = new TransmittableThreadLocal<>();
        parent.set(Thread.currentThread().getName() + " ;" + "value-set-in-parent");
        System.out.println(Thread.currentThread().getName() + " ;" + "***********parent: " + parent.get());
        ExecutorService executorService = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
//                TtlExecutors.getTtlExecutorService(new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()));
        for (int i = 0; i < 10; i++) {

            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " ;" + "***********child: " + parent.get());
                parent.set("value-set-in-child" + new Random().nextInt());
                System.out.println("---------------------------" + parent.get());
                executorService.execute(TtlRunnable.get(new Task("runnable", parent)));
                Callable call = new Call("callable", parent);
                executorService.submit(TtlCallable.get(call));

            }).start();
        }

        // =====================================================
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    private static void t() {
        TransmittableThreadLocal<String> parent = new TransmittableThreadLocal<String>();
        parent.set("value-set-in-parent");
        // ===========================================================================
        // 线程 A
        // ===========================================================================

        // (1) 抓取当前线程的所有TTL值
        final Object captured = TransmittableThreadLocal.Transmitter.capture();

        // ===========================================================================
        // 线程 B（异步线程）
        // ===========================================================================

        // (2) 在线程 B中回放在capture方法中抓取的TTL值，并返回 回放前TTL值的备份
        final Object backup = TransmittableThreadLocal.Transmitter.replay(captured);
        try {
            // 你的业务逻辑，这里你可以获取到外面设置的TTL值
            String value = parent.get();

            System.out.println("Hello: " + value);
            //    ...
            String result = "World: " + value;
        } finally {
            // (3) 恢复线程 B执行replay方法之前的TTL值（即备份）
            TransmittableThreadLocal.Transmitter.restore(backup);
        }
    }
}