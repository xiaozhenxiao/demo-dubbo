package com.jd.jvm.compiler;

/**
 * JIT
 * -XX:+UnlockDiagnosticVMOptions -XX: +PrintCompilation -XX: PrintInlining
 * wangzhen23
 * 2017/9/25.
 */
public class JITTest {
    public static final int NUM = 15000;

    public static int doubleValue(int i) {
        // 这个空循环用于后面演示JIT代码优化过程
        for(int j=0; j<100000; j++);
        return i * 2;
    }

    public static long calcSum() {
        long sum = 0;
        for (int i = 1; i <= 100; i++) {
            sum += doubleValue(i);
        }
        return sum;
    }

    public static void main(String[] args) {
        long sum = 0;
        for (int i = 0; i < NUM; i++) {
            sum += calcSum();
        }
        System.out.println("sum:" + sum);
    }
}
