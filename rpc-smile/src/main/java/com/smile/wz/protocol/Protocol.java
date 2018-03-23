package com.smile.wz.protocol;

import com.alibaba.dubbo.rpc.RpcException;
import com.smile.wz.export.Exporter;
import com.smile.wz.invoker.WZInvoker;

import java.util.Map;

/**
 * wangzhen23
 * 2018/3/7.
 */
public interface Protocol {

    /**
     * Get default port when user doesn't config the port.
     *
     * @return default port
     */
    int getDefaultPort();

    /**
     * Export service for remote invocation: <br>
     * 1. Protocol should record request source address after receive a request:
     * WZRpcContext.getContext().setRemoteAddress();<br>
     * 2. export() must be idempotent, that is, there's no difference between invoking once and invoking twice when
     * export the same URL<br>
     * 3. WZInvoker instance is passed in by the framework, protocol needs not to care <br>
     *
     * @param <T>     Service type
     * @param invoker Service WZInvoker
     * @return exporter reference for exported service, useful for unexport the service later
     * @throws RpcException thrown when error occurs during export the service, for example: port is occupied
     */
    <T> Exporter<T> export(WZInvoker<T> invoker, Integer port) throws RpcException;

    /**
     * Refer a remote service: <br>
     * 1. When user calls `invoke()` method of `WZInvoker` object which's returned from `refer()` call, the protocol
     * needs to correspondingly execute `invoke()` method of `WZInvoker` object <br>
     * 2. It's protocol's responsibility to implement `WZInvoker` which's returned from `refer()`. Generally speaking,
     * protocol sends remote request in the `WZInvoker` implementation. <br>
     * 3. When there's check=false set in URL, the implementation must not throw exception but try to recover when
     * connection fails.
     *
     * @param <T>  Service type
     * @param type Service class
     * @param address  URL address for the remote service
     * @return invoker service's local proxy
     * @throws RpcException when there's any error while connecting to the service provider
     */
    <T> WZInvoker<T> refer(Class<T> type, String address,  Map<String, String> attachment) throws RpcException;

    /**
     * Destroy protocol: <br>
     * 1. Cancel all services this protocol exports and refers <br>
     * 2. Release all occupied resources, for example: connection, port, etc. <br>
     * 3. Protocol can continue to export and refer new service even after it's destroyed.
     */
    void destroy();
}