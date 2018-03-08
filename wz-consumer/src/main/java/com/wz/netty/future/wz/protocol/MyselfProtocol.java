package com.wz.netty.future.wz.protocol;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.alibaba.dubbo.rpc.RpcException;
import com.wz.netty.future.Exporter;
import com.wz.netty.future.wz.invoker.WZInvoker;
import com.wz.netty.future.wz.invoker.XiaoWZInvoker;
import com.wz.netty.future.wz.netty.client.WZNettyClient;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * wangzhen23
 * 2018/3/7.
 */
public class MyselfProtocol implements Protocol {
    protected final Map<String, Exporter<?>> exporterMap = new ConcurrentHashMap<String, Exporter<?>>();

    //TODO SOFEREFENCE
    protected final Set<WZInvoker<?>> invokers = new ConcurrentHashSet<WZInvoker<?>>();

    private final Map<String, WZNettyClient> referenceClientMap = new ConcurrentHashMap<String, WZNettyClient>(); // <host:port,Exchanger>

    @Override
    public int getDefaultPort() {
        return 22222;
    }

    @Override
    public <T> Exporter<T> export(WZInvoker<T> invoker) throws RpcException {
        return null;
    }

    @Override
    public <T> WZInvoker<T> refer(Class<T> type, String address) throws RpcException {
        return new XiaoWZInvoker(type, getClient(address));
    }

    private WZNettyClient[] getClient(String host) {
        WZNettyClient[] wzNettyClients = new WZNettyClient[0];
        WZNettyClient wzNettyClient = referenceClientMap.get(host);
        if (wzNettyClient == null) {
            wzNettyClients[0] = new WZNettyClient(host);
            referenceClientMap.put(host, wzNettyClients[0]);
        } else {
            wzNettyClients[0] = wzNettyClient;
        }
        return wzNettyClients;
    }

    @Override
    public void destroy() {

    }
}
