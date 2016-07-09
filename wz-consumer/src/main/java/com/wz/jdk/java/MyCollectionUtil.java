package com.wz.jdk.java;

import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * Created by Administrator on 2016/7/2.
 * Date:2016-07-02
 */
public class MyCollectionUtil {
    public static void main(String[] args) {
        // a集合[a,a,b,b,b,c]
        List<String> a = Arrays.asList("a", "b", "d", "e", "c");
        // b集合[a,b,b,c,c]
        List<String> b = Arrays.asList("a", "b", "c", "r");

        Collection c = CollectionUtils.intersection(a,b);
        for (Object s : c ) {
            System.out.print(s + "\t");
        }
        System.out.println();
        Collection cc = CollectionUtils.union(a,b);
        for (Object s : cc ) {
            System.out.print(s + "\t");
        }
        System.out.println();
        Collection ccc = CollectionUtils.subtract(a,b);
        for (Object s : ccc ) {
            System.out.print(s + "\t");
        }
        System.out.println();

    }
    private static void testIntersection() {
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        list1.add("abc");  list2.add("abc");
        list1.add("123");  list2.add("123");
        list1.add("ABC");
        list2.add("XYZ");
        list1.removeAll(list2);
        list1.addAll(list2);
        System.out.println("并集元素个数是：" + list1.size());

    }
    private static void testIntersectionSet() {
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        list1.add("abc");  list2.add("abc");
        list1.add("123");  list2.add("123");
        list1.add("ABC");
        list2.add("XYZ");
        Set<String> set =new HashSet<String>();
        addList2Set(set, list1);
        addList2Set(set, list2);
        System.out.println("并集元素个数是：" + set.size());

    }
    private static void testUnion() {
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        list1.add("abc");  list2.add("abc");
        list1.add("123");  list2.add("123");
        list1.add("ABC");
        list2.add("XYZ");
        list1.retainAll(list2);
        System.out.println("交集元素个数是："+list1.size());
    }
    private static void addList2Set(Set<String> set,List<String> list){
        for (String str : list) {
            set.add(str);
        }
    }
}
