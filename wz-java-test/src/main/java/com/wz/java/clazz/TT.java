package com.wz.java.clazz;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * TODO
 * wangzhen23
 * 2017/5/10.
 */
public class TT {
    public static void main(String ss[]) {
        test();
    }

    static void test() {
        while (true) {
            Enhancer e = new Enhancer();
            e.setSuperclass(OOMObject.class);//要生成OOMObject类的子类
            e.setUseCache(false);
            e.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object obj, Method method,
                                        Object[] args, MethodProxy proxy) throws Throwable {
                    System.out.println("Before invoke ");
                    Object result = proxy.invokeSuper(obj, args);
                    System.out.println("After invoke");
                    return result;
                }
            });
            OOMObject ee = (OOMObject) e.create();
            ee.test();
        }
    }

    static class OOMObject {
        public void test() {
            System.out.println("invokinginginginging....");
        }

    }
}
