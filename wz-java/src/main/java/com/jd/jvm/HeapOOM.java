package com.jd.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * VM Args: -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 * wangzhen23
 * 2017/9/8.
 */
public class HeapOOM {
    public static void main(String[] args) {
        List<ClassA> list = new ArrayList<ClassA>();
        while (true){
            list.add(new ClassA());
        }
    }
}
