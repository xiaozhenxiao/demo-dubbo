package com.smile.wz.proxy.client;

import com.smile.wz.proxy.server.AbstractProxyInvoker;
import com.smile.wz.invoker.WZInvoker;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JdkProxyFactory
 */
public class JdkProxyFactory extends AbstractProxyFactory {

    @Override
    public <T> T getProxy(WZInvoker<T> invoker, Class<?>[] interfaces) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaces, new InvokerInvocationHandler(invoker));
    }

    @Override
    public <T> WZInvoker<T> getInvoker(T proxy, Class<T> type) {
        return new AbstractProxyInvoker<T>(proxy, type) {
            @Override
            protected Object doInvoke(T proxy, String methodName,
                                      Class<?>[] parameterTypes,
                                      Object[] arguments) throws Throwable {
                Method method = proxy.getClass().getMethod(methodName, parameterTypes);
                return method.invoke(proxy, arguments);
            }
        };
    }

}