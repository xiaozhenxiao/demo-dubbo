package com.wz.netty.future.wz.proxy.client;

import com.alibaba.dubbo.rpc.RpcException;
import com.wz.netty.future.wz.Constants;
import com.wz.netty.future.wz.ProxyFactory;
import com.wz.netty.future.wz.invoker.WZInvoker;

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