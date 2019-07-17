package com.wz.process;

import com.smile.wz.processor.Safety;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * wangzhen23
 * 2018/11/16.
 */
@Safety
public class TestMethod {
    private String name;

    public String method(String email, String phone, String phoneCard) {
        return email + phone + phoneCard;
    }
    @Safety
    public Son method1(@Param Person p) {
        return p.getSon();
    }

    public void method2(Integer i1, int i2, float f, Float f1, Double d, double d1) {
        return;
    }

    public Integer method3(Class clazz) {
        return 60 * 60 * 24;
    }
}
