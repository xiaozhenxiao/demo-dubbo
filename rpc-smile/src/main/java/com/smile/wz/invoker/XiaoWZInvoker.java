package com.smile.wz.invoker;

import com.alibaba.dubbo.common.utils.AtomicPositiveInteger;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.dubbo.rpc.RpcException;
import com.smile.wz.result.WZRpcResult;
import com.smile.wz.Constants;
import com.smile.wz.netty.client.WZNettyClient;
import com.smile.wz.result.Result;
import com.smile.wz.rpc.FutureAdapter;
import com.smile.wz.rpc.WZResponseFuture;
import com.smile.wz.rpc.WZRpcContext;

import java.util.Map;
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

    public XiaoWZInvoker(Class<T> serviceType, Map<String, String> attachment, WZNettyClient[] clients) {
        this(serviceType, attachment, clients, "1.0", null);
    }

    public XiaoWZInvoker(Class<T> type, Map<String, String> attachment, WZNettyClient[] clients, String version, Set<WZInvoker<?>> invokers) {
        super(type, attachment);
        this.clients = clients;
        this.version = version;
        this.invokers = invokers;
    }

    @Override
    protected Result doInvoke(WZInvocation invocation) throws Throwable {
        final String methodName = invocation.getMethodName();

        WZNettyClient currentClient;
        if (clients.length == 1) {
            currentClient = clients[0];
        } else {
            currentClient = clients[index.getAndIncrement() % clients.length];
        }
        try {
            if (Boolean.TRUE.toString().equals(invocation.getAttachment(Constants.ASYNC_KEY))) {
                /** 异步调用 **/
                WZResponseFuture future = currentClient.request(invocation, 5000);
                WZRpcContext.getContext().setFuture(new FutureAdapter<Object>(future));
                return new WZRpcResult();
            }
            /** 同步调用 **/
            return (Result) currentClient.request(invocation, 5000).get();
        } catch (TimeoutException e) {
            throw new RpcException(RpcException.TIMEOUT_EXCEPTION, "Invoke remote method timeout. method: " + invocation.getMethodName() + ", cause: " + e.getMessage(), e);
        } catch (RemotingException e) {
            throw new RpcException(RpcException.NETWORK_EXCEPTION, "Failed to invoke remote method: " + invocation.getMethodName() + ", cause: " + e.getMessage(), e);
        }
    }
}
