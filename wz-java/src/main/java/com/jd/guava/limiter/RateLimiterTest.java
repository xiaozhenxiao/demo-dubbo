package com.jd.guava.limiter;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.RateLimiter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * wangzhen23
 * 2019/1/23.
 */
public class RateLimiterTest {

    public static void main(String[] args) {
        testRateLimiter();
    }

    /**
     * RateLimiter类似于JDK的信号量Semphore，他用来限制对资源并发访问的线程数
     */
    public static void testRateLimiter() {
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

        RateLimiter limiter = RateLimiter.create(5.0); // 每秒不超过4个任务被提交

        System.out.println("start " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        for (int i = 0; i < 10; i++) {
            limiter.acquire(); // 请求RateLimiter, 超过permits会被阻塞

            final ListenableFuture<Integer> listenableFuture = executorService.submit(new Task("is "+ i));
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }
    }

    static class Task implements Callable<Integer> {
        String str;

        public Task(String str) {
            this.str = str;
        }

        @Override
        public Integer call() throws Exception {
            System.out.println(Thread.currentThread().getName() + " call execute.." + str);
            TimeUnit.SECONDS.sleep(1);
            return 7;
        }
    }
}
