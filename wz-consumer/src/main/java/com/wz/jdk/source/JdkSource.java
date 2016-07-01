package com.wz.jdk.source;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wangzhen on 2016-06-17.
 */
public class JdkSource {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        LinkedList<String> linkedList = new LinkedList<String>();
        HashMap<String,String> hashMap = new HashMap<String, String>();
        ConcurrentHashMap<String,String> concurrentHashMap;
        LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<String, String>();
        TreeMap<String,String> treeMap = new TreeMap<String, String>();

        linkedHashMap.put("","");

        System.out.println("list:" + list);
    }
}
