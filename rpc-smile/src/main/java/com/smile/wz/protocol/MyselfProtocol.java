package com.smile.wz.protocol;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.alibaba.dubbo.rpc.RpcException;
import com.smile.wz.Constants;
import com.smile.wz.export.WZExporter;
import com.smile.wz.export.WZServer;
import com.smile.wz.invoker.WZInvocation;
import com.smile.wz.invoker.WZInvoker;
import com.smile.wz.invoker.XiaoWZInvoker;
import com.smile.wz.netty.client.WZNettyClient;
import com.smile.wz.netty.server.WZNettyServer;
import com.smile.wz.util.InetAddressUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.smile.wz.export.Exporter;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * wangzhen23
 * 2018/3/7.
 */
public class MyselfProtocol implements Protocol {
    private Logger logger = LoggerFactory.getLogger(MyselfProtocol.class);
    private Integer port = 22222;
    protected final Map<String, Exporter<?>> exporterMap = new ConcurrentHashMap<String, Exporter<?>>();
    private final Map<String, WZServer> serverMap = new ConcurrentHashMap<String, WZServer>();

    //TODO SOFEREFENCE
    protected final Set<WZInvoker<?>> invokers = new ConcurrentHashSet<WZInvoker<?>>();

    private final Map<String, WZNettyClient> referenceClientMap = new ConcurrentHashMap<String, WZNettyClient>(); // <host:port,Exchanger>

    @Override
    public int getDefaultPort() {
        return port;
    }

    @Override
    public <T> Exporter<T> export(WZInvoker<T> invoker, Integer port) throws RpcException {
        // export service.
        this.port = port;
        String key = serviceKey(invoker);
        WZExporter<T> exporter = new WZExporter<T>(invoker, key, exporterMap);
        exporterMap.put(key, exporter);

        openServer(invoker, port);
        return exporter;
    }

    /**
     * @param invoker
     * @param port
     * @param <T>
     */
    private <T> void openServer(WZInvoker<T> invoker, Integer port) {
        InetSocketAddress address = InetAddressUtil.getLocalAddress(port);
        String key = address.getAddress().getHostAddress() + ":" + address.getPort();
        WZServer server = serverMap.get(key);
        if (server == null) {
            serverMap.put(key, createServer(port));
        } else {
            // server supports reset, use together with override
//            server.reset(invoker);
            System.out.println("server Already started, " + invoker.getInterface().getName());
        }
    }

    private <T> WZServer createServer(Integer port) {
        return new WZNettyServer(port == null ? getDefaultPort() : port, (Channel channel, Object ms) -> {
            if (ms instanceof WZInvocation) {
                WZInvocation inv = (WZInvocation) ms;
                WZInvoker<?> invoker = getInvoker(channel, inv);

                return invoker.invoke(inv);
            }
            throw new RuntimeException("Unsupported request: " + ms == null ? null : (ms.getClass().getName() + ": " + ms) + ", channel: consumer: " + channel.remoteAddress() + " --> provider: " + channel.localAddress());
        });
    }

    private WZInvoker<?> getInvoker(Channel channel, WZInvocation inv) {
        String key = inv.getAttachment(Constants.INTERFACE_KEY);
        WZExporter<?> exporter = (WZExporter<?>) exporterMap.get(key);

        if (exporter == null)
            throw new RuntimeException("Not found exported service: " + key + " in " + exporterMap.keySet() + ", may be version or group mismatch " + ", channel: consumer: " + channel.remoteAddress() + " --> provider: " + channel.localAddress() + ", message:" + inv);

        return exporter.getInvoker();
    }

    private <T> String serviceKey(WZInvoker<T> invoker) {
        return invoker.getInterface().getName();
    }

    @Override
    public <T> WZInvoker<T> refer(Class<T> type, String address, boolean isAsync) throws RpcException {
        return new XiaoWZInvoker(type, isAsync, getClient(address.split(":")[0], Integer.valueOf(address.split(":")[1])));
    }

    private WZNettyClient[] getClient(String host, Integer port) {
        WZNettyClient[] wzNettyClients = new WZNettyClient[1];
        WZNettyClient wzNettyClient = referenceClientMap.get(host);
        if (wzNettyClient == null) {
            wzNettyClients[0] = new WZNettyClient(host, port);
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
