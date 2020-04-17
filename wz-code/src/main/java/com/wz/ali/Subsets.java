package com.wz.ali;

import java.util.*;

/**
 * 集合的子集
 * 请编写一个方法，返回某集合的所有非空子集。
 * 给定一个int数组A和数组的大小int n，请返回A的所有非空子集。保证A的元素个数小于等于20，且元素互异。
 * 各子集内部从大到小排序,子集之间字典逆序排序，见样例
 * 测试样例：
 * [123,456,789]
 * 返回：
 * {[789,456,123],
 * [789,456],
 * [789,123],
 * [789],
 * [456 123],
 * [456],
 * [123]
 * }
 * 解答:
 * 0(000)：{}
 * 1(001)：{a}
 * 2(010)：{b}
 * 3(011)：{ab}
 * 4(100)：{c}
 * 5(101)：{a,c}
 * 6(110)：{b,c}
 * 7(111)：{a,b,c}
 */
public class Subsets {

    public static void main(String[] args) {
//        int[] a = new int[]{123, 456, 789};
        int[] a = new int[]{1, 2, 3, 4};

        List<Integer> list = new ArrayList<Integer>();
        for (int i : a) {
            list.add(i);
        }
        List<List<Integer>> result = getSubsets(list);

        for (List<Integer> integers : result) {
            Collections.sort(integers, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o2 - o1;
                }
            });
        }
        for (List<Integer> integers : result) {
            System.out.println("原===" + integers);
        }
        Collections.sort(result, new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                int l = o1.size();
                int l2 = o2.size();
                int minl = l < l2 ? l : l2;
                for (int i = 0; i < minl; i++) {
                    if (o1.get(i).equals(o2.get(i))) {
                        continue;
                    }
                    return o2.get(i) - o1.get(i);
                }
                return l2 - l;

            }
        });

        for (List<Integer> integers : result) {
            System.out.println("--" + integers);
        }
    }

    public static List<List<Integer>> getSubsets(List<Integer> subList) {
        List<List<Integer>> allsubsets = new ArrayList<List<Integer>>();
        int max = 1 << subList.size();
        for (int loop = 0; loop < max; loop++) {
            int index = 0;
            int temp = loop;
            List<Integer> currentCharList = new ArrayList<Integer>();
            while (temp > 0) {
                if ((temp & 1) > 0) {// 每次判断index最低位是否为1，为1则把集合set的第j个元素放到子集中
                    currentCharList.add(subList.get(index));
                }
                temp >>= 1;// 右移一位
                index++;
            }
            allsubsets.add(currentCharList);
        }
        return allsubsets;
    }
}
