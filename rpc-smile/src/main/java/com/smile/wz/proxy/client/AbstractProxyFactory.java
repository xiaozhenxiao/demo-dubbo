package com.smile.wz.proxy.client;

import com.alibaba.dubbo.rpc.RpcException;
import com.smile.wz.ProxyFactory;
import com.smile.wz.invoker.WZInvoker;

/**
 * AbstractProxyFactory
 */
public abstract class AbstractProxyFactory implements ProxyFactory {

    public <T> T getProxy(WZInvoker<T> invoker) throws RpcException {
        Class<?>[] interfaces = new Class<?>[]{invoker.getInterface()};

        return getProxy(invoker, interfaces);
    }

    public abstract <T> T getProxy(WZInvoker<T> invoker, Class<?>[] types);

}