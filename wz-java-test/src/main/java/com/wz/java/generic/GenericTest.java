package com.wz.java.generic;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhen23 on 2017/5/9.
 */
public class GenericTest {
    public static void main(String[] args) {
        //协变
        Fruit[] fruit = new Apple[10];

        // ArrayList<Fruit> flist = new ArrayList<Apple>(); // 无法通过编译，泛型不支持协变

        ArrayList<? extends Fruit> flist = new ArrayList<Apple>();// 使用通配符解决协变问题

        List<? super Apple> list = Lists.newArrayList();
//        list.add(new Fruit());

        Apple<String> apple = new Apple<>("jjjjjj");
        System.out.println("result : "+ apple.delete(apple));

    }
}
