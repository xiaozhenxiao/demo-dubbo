package com.wz.jdk.java.jdk7;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2016/8/6.
 * Date:2016-08-06
 */
public class Fibonacci extends RecursiveTask<Integer> {
    final int n;

    Fibonacci(int n) {
        this.n = n;
    }

    private int compute(int small) {
        final int[] results = { 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89 };
        return results[small];
    }
    @Override
    public Integer compute() {
        if (n <= 10) {
            return compute(n);
        }
        Fibonacci f1 = new Fibonacci(n - 1);
        Fibonacci f2 = new Fibonacci(n - 2);
        f1.fork();
        f2.fork();
        return f1.join() + f2.join();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinTask<Integer> fjt = new Fibonacci(40);
        ForkJoinPool fjpool = new ForkJoinPool();
        Future<Integer> result = fjpool.submit(fjt);

        // do something
        System.out.println("主线程继续做一些事情");
        System.out.println(result.get());
    }
}
