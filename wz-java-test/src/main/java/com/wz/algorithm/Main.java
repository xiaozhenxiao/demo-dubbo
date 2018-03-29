package com.wz.algorithm;

import com.wz.annotation.OrderBuilder;

/**
 * wangzhen23
 * 2018/3/28.
 */
public class Main {
    public static void main(String[] args) {
        Order build = new OrderBuilder().buildId(2).buildAddTime(System.currentTimeMillis()).build();
        System.out.println(build);
    }
}
