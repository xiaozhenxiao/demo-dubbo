package com.smile.wz.proxy.server;

import com.alibaba.dubbo.rpc.RpcException;
import com.smile.wz.invoker.WZInvocation;
import com.smile.wz.invoker.WZInvoker;
import com.smile.wz.result.WZRpcResult;
import com.smile.wz.result.Result;

import java.lang.reflect.InvocationTargetException;

/**
 * server调用
 * wangzhen23
 * 2018/3/13.
 */
public abstract class AbstractProxyInvoker<T> implements WZInvoker<T> {
    private final T proxy;

    private final Class<T> type;

    public AbstractProxyInvoker(T proxy, Class<T> type) {
        if (proxy == null) {
            throw new IllegalArgumentException("proxy == null");
        }
        if (type == null) {
            throw new IllegalArgumentException("interface == null");
        }
        if (!type.isInstance(proxy)) {
            throw new IllegalArgumentException(proxy.getClass().getName() + " not implement interface " + type);
        }
        this.proxy = proxy;
        this.type = type;
    }

    public Class<T> getInterface() {
        return type;
    }

    public boolean isAvailable() {
        return true;
    }

    public void destroy() {
    }

    public Result invoke(WZInvocation invocation) throws RpcException {
        try {
            return new WZRpcResult(doInvoke(proxy, invocation.getMethodName(), invocation.getParameterTypes(), invocation.getArguments()));
        } catch (InvocationTargetException e) {
            return new WZRpcResult(e.getTargetException());
        } catch (Throwable e) {
            throw new RpcException("Failed to invoke remote proxy method " + invocation.getMethodName() + " to " + ", cause: " + e.getMessage(), e);
        }
    }

    protected abstract Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable;

    @Override
    public String toString() {
        return getInterface().getName();
    }
}
