package com.wz.algorithms;

/**
 * 在一个二维数组中，每一行都按照从左到右递增的顺序排序，
 * 每一列都按照从上到下递增的顺序排序。
 * 请完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
 * wangzhen23
 * 2018/7/17.
 */
public class TwoDimArrayFind {

    public static void main(String[] args) {
        int[][] arrays = {
                {1, 3, 6, 8, 9, 12, 15, 18},
                {2, 4, 7, 10, 11, 13, 16, 19},
                {3, 5, 14, 17, 20, 28, 31, 33},
                {4, 9, 18, 21, 25, 29, 32, 40},
                {5, 6, 7, 8, 22, 27, 30, 45},
                {6, 7, 8, 9, 27, 28, 45, 50},
        };

        int[][] arr = {{1,2,8,9},{2,4,9,12},{4,7,10,13},{6,8,11,15}};

        int[][] arra = {{}};

        System.out.println(findFromTwoDimArray(16, arra));
    }

    public static boolean findFromTwoDimArray(int target, int [][] array) {
        int j = 1;
        System.out.println("length:" + array.length);
        if(array.length < 1)
            return false;
        for (int i = 0; i < array.length; i++) {
            if(array[i].length < 1)
                return false;
            int k = array[i].length - j;
            int m = array[i][k];
            if (target < m && k > 0) {
                //向左
                i--;
                j++;
            } else if (target == m) {
                return true;
            }
        }
        return false;
    }
}
