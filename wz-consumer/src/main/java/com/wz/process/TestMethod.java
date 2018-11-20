package com.wz.process;

import com.smile.wz.processor.Safety;

/**
 * wangzhen23
 * 2018/11/16.
 */
@Safety
public class TestMethod {

    public String method(String param0, String param1, String param2) {
        return param0 + param1 + param2;
    }

    public String method1(Person p) {
        return p.getClass().getName();
    }

    public String method2(Integer i1, int i2, float f, Float f1, Double d, double d1) {
        return "";
    }
}
