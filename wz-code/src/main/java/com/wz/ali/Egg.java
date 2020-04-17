package com.wz.ali;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 设M(t,n)为在从t层楼，n个蛋的情况下需要抛投的最少次数，情况有多少种呢。当然是t种，从每一层都抛出一个鸡蛋试一下。
 * 现在就要得到这t个抛投实验中的最小抛投次数。设Mk(t,n)为从k（1<=k<=t）层楼抛下1个鸡蛋进行测试而得到的抛投数。这t种抛投有通用的递推模式：
 * 假设在k层楼进行抛投，会产生两种情况：
 * 碎了：问题规模变为：M(k-1, n-1)
 * 没碎：问题规模变为：M(t-k, n)
 * 则Mk(t,n) = max(M(k-1, n-1), M(t-k, n)) + 1，也就是求出这种情况下最大的抛投次数。
 * 而M(t,n)= min(M1(t,n), M2(t,n), M3(t,n) ...... Mk(t,n))
 * ————————————————
 */
public class Egg {
    private static int m[][];
    private static int t = 100;
    private static int n = 10;

    static {
        m = new int[t + 1][n + 1];
        for (int i = 1; i < t + 1; i++) {
            m[i][1] = i;
        }
        for (int i = 0; i < n + 1; i++) {
            m[1][i] = 1;
        }
    }

    public static void main(String[] args) {
        for (int i = 2; i < t + 1; i++) {
            for (int j = 2; j < n; j++) {
                int min = Integer.MAX_VALUE;
                for (int k = 1; k < i; k++) {
                    int mk = Math.max(m[k - 1][j - 1], m[i - k][j]) + 1;
                    min = Math.min(min, mk);
                }
                m[i][j] = min;
            }
        }
        for (int i = 1; i < t; i++) {
            System.out.print(i + "--floors\t");
            for (int j = 1; j < n; j++) {
                System.out.print(m[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println("================" + getMinTimes(2, 3));

        /**
         * 递归实现:性能差 一天都没有跑完
         */
        /*int tt = 100;
        for (int i = 1; i < tt; i++) {
            System.out.print(i + " floors\t");
            for (int j = 1; j < 10; j++) {
                int times = getMinTimes(i, j);
                System.out.print(times + "\t");
            }
            System.out.println();
        }*/

    }

    private static int getMinTimes(int t, int egg) {
        if (t == 1) {
            return 1;
        }
        if (egg == 1) {
            return t;
        }
        int minLevel = Integer.MAX_VALUE;
        for (int i = 1; i < t; i++) {
            int mini = getMk(t, i, egg);
            minLevel = minLevel < mini ? minLevel : mini;
        }
        return minLevel;
    }

    private static int getMk(int t, int k, int n) {
        return Math.max(k == 1 ? 1 : getMinTimes(k - 1, n - 1), getMinTimes(t - k, n)) + 1;
    }
}
