package com.wz.netty.future.wz.invoker;

import com.alibaba.dubbo.common.utils.AtomicPositiveInteger;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.dubbo.rpc.RpcException;
import com.wz.netty.future.wz.netty.client.WZNettyClient;
import com.wz.netty.future.wz.result.Result;

import java.util.Set;

/**
 * TODO
 * wangzhen23
 * 2018/2/8.
 */
public class XiaoWZInvoker<T> extends AbatractWZInvoker<T> {
    private final WZNettyClient[] clients;

    private final AtomicPositiveInteger index = new AtomicPositiveInteger();

    private final String version;

    private final Set<WZInvoker<?>> invokers;

    public XiaoWZInvoker(Class<T> serviceType, WZNettyClient[] clients) {
        this(serviceType, clients, "1.0", null);
    }

    public XiaoWZInvoker(Class<T> type, WZNettyClient[] clients, String version, Set<WZInvoker<?>> invokers) {
        super(type);
        this.clients = clients;
        this.version = version;
        this.invokers = invokers;
    }

    @Override
    protected Result doInvoke(WZInvocation invocation) throws Throwable {
        WZInvocation inv = (WZInvocation) invocation;
        final String methodName = invocation.getMethodName();

        WZNettyClient currentClient;
        if (clients.length == 1) {
            currentClient = clients[0];
        } else {
            currentClient = clients[index.getAndIncrement() % clients.length];
        }
        try {
            /** 异步调用 **/
            /*WZResponseFuture future = currentClient.request(inv, 5000);
            WZRpcContext.getContext().setFuture(new FutureAdapter<Object>(future));
            return new WZRpcResult();*/

            /** 同步调用 **/
            return (Result) currentClient.request(inv, 5000).get();

        } catch (TimeoutException e) {
            throw new RpcException(RpcException.TIMEOUT_EXCEPTION, "Invoke remote method timeout. method: " + invocation.getMethodName() + ", cause: " + e.getMessage(), e);
        } catch (RemotingException e) {
            throw new RpcException(RpcException.NETWORK_EXCEPTION, "Failed to invoke remote method: " + invocation.getMethodName() + ", cause: " + e.getMessage(), e);
        }
    }
}
