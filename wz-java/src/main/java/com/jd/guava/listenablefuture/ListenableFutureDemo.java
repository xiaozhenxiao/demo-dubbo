package com.jd.guava.listenablefuture;

import com.google.common.util.concurrent.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * wangzhen23
 * 2018/3/24.
 */
public class ListenableFutureDemo {
    public static void main(String[] args) {
        testListenableFuture();
    }

    public static void testListenableFuture() {
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

        final ListenableFuture<Integer> listenableFuture = executorService
                .submit(new Task("testListenableFuture"));

        //同步获取调用结果
        try {
            System.out.println(Thread.currentThread().getName() + " " + listenableFuture.get());
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }

        //第一种方式
        listenableFuture.addListener(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " get listenable future's result " + listenableFuture.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }, executorService);

        //第二种方式
        Futures.addCallback(listenableFuture, new FutureCallback<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                System.out.println(Thread.currentThread().getName() + " get listenable future's result with callback " + result);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }
}

class Task implements Callable<Integer> {
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
