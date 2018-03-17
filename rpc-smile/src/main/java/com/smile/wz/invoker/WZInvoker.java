package com.smile.wz.invoker;

import com.alibaba.dubbo.rpc.RpcException;
import com.smile.wz.result.Result;

/**
 * 调用类
 * wangzhen23
 * 2018/2/8.
 */
public interface WZInvoker<T> {

    Class<T> getInterface();

    /**
     * invoke.
     *
     * @param invocation
     * @return result
     * @throws RpcException
     */
    Result invoke(WZInvocation invocation) throws RpcException;

    /**
     * destroy.
     */
    void destroy();
}
