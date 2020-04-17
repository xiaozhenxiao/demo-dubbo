package com.wz.ali;

public class Egg2 {
    private int t;//t层楼
    private int n;//n个鸡蛋
    private int[][] m;//最优解，在（i(楼层)，j(鸡蛋)）时候的最优解。最优解是指，一定能找到临界楼层情况下的最少抛投次数。

    private Egg2(int t, int n) {
        this.t = t;
        this.n = n;
        this.m = new int[t + 1][n + 1];// 舍弃零行零列的数据不要
        // 填充1行1列的简单数据
        for (int i = 1; i < t + 1; i++) {
            m[i][1] = i;//一层楼的话，至少试一次
        }
        for (int j = 1; j < n + 1; j++) {
            m[1][j] = 1;//一个鸡蛋的话，要试的次数为楼层数
        }
    }

    public int result() {//填充大于1行1列的空格
        for (int tt = 2; tt < t + 1; tt++) {//从第二层楼开始
            for (int nn = 2; nn < n + 1; nn++) {//从有两个鸡蛋开始
                int min = Integer.MAX_VALUE;
                for (int k = 1; k < t + 1 && tt > k; k++) {//第一次从k层楼抛切分
                    min = Math.min(min, Math.max(m[tt - k][nn], m[k - 1][nn - 1]) + 1);
                    // 如果失败，问题变为m[k-1][n-1]，如果成功，问题变为m[t-k][n]，现在每一层都切分一次，然后找到每一层切分需要的最大值。
                    // 然后从每层楼中取最小值，这里要注意我们求的是什么：保证一定能找到临界楼层的最小抛投次数。
                }
                m[tt][nn] = min;
            }
        }
        return m[t][n];
    }


    public static void main(String[] args) {

        for (int i = 1; i < 100; i++) {
            System.out.print(i + " floors\t");
            for (int j = 1; j < 10; j++) {
                System.out.print(new Egg2(i, j).result() + "\t");
            }
            System.out.println();
        }
    }

}
