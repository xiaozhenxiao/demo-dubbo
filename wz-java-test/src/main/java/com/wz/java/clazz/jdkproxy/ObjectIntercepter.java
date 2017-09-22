package com.wz.java.clazz.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * TODO
 * wangzhen23
 * 2017/9/22.
 */
public class ObjectIntercepter implements InvocationHandler {
    private Object target;

    public Object bind(Object target) {
        this.target = target;
        //取得代理对象
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), this);   //要绑定接口(这是一个缺陷，cglib弥补了这一缺陷)
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("TODO代理class:" + method.getDeclaringClass());
               //执行方法
        return method.invoke(target, args);
    }
}
