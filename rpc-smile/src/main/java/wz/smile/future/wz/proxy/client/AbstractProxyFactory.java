package wz.smile.future.wz.proxy.client;

import com.alibaba.dubbo.rpc.RpcException;
import wz.smile.future.wz.Constants;
import wz.smile.future.wz.ProxyFactory;
import wz.smile.future.wz.invoker.WZInvoker;

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