package com.smile.wz;

import com.alibaba.dubbo.rpc.RpcException;
import com.smile.wz.invoker.WZInvoker;

public interface ProxyFactory {


    <T> T getProxy(WZInvoker<T> invoker) throws RpcException;

    <T> WZInvoker<T> getInvoker(T proxy, Class<T> type) throws RpcException;

}