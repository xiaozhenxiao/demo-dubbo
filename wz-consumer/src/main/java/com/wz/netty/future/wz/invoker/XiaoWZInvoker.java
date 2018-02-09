package com.wz.netty.future.wz.invoker;

import com.wz.netty.future.wz.netty.client.WZNettyClient;
import com.wz.netty.future.wz.result.Result;

import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO
 * wangzhen23
 * 2018/2/8.
 */
public class XiaoWZInvoker<T> extends AbatractWZInvoker<T> {
    private final WZNettyClient[] clients;

    private final String version;

    private final ReentrantLock destroyLock = new ReentrantLock();

    private final Set<WZInvoker<?>> invokers;

    public XiaoWZInvoker(Class<T> serviceType, WZNettyClient[] clients) {
        this(serviceType, clients,"1.0", null);
    }

    public XiaoWZInvoker(Class<T> type, WZNettyClient[] clients, String version, Set<WZInvoker<?>> invokers) {
        super(type);
        this.clients = clients;
        this.version = version;
        this.invokers = invokers;
    }

    @Override
    protected Result doInvoke(WZInvocation invocation) throws Throwable {
        return null;
    }
}
