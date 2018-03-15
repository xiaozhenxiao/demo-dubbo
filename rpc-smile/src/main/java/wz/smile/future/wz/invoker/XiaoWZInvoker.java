package wz.smile.future.wz.invoker;

import com.alibaba.dubbo.common.utils.AtomicPositiveInteger;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.dubbo.rpc.RpcException;
import wz.smile.future.wz.Constants;
import wz.smile.future.wz.netty.client.WZNettyClient;
import wz.smile.future.wz.result.Result;
import wz.smile.future.wz.result.WZRpcResult;
import wz.smile.future.wz.rpc.FutureAdapter;
import wz.smile.future.wz.rpc.WZResponseFuture;
import wz.smile.future.wz.rpc.WZRpcContext;

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

    public XiaoWZInvoker(Class<T> serviceType, boolean isAsync, WZNettyClient[] clients) {
        this(serviceType, isAsync, clients, "1.0", null);
    }

    public XiaoWZInvoker(Class<T> type, boolean isAsync, WZNettyClient[] clients, String version, Set<WZInvoker<?>> invokers) {
        super(type, isAsync);
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
            if (Boolean.TRUE.toString().equals(inv.getAttachment(Constants.ASYNC_KEY))) {
                /** 异步调用 **/
                WZResponseFuture future = currentClient.request(inv, 5000);
                WZRpcContext.getContext().setFuture(new FutureAdapter<Object>(future));
                return new WZRpcResult();
            }
            /** 同步调用 **/
            return (Result) currentClient.request(inv, 5000).get();
        } catch (TimeoutException e) {
            throw new RpcException(RpcException.TIMEOUT_EXCEPTION, "Invoke remote method timeout. method: " + invocation.getMethodName() + ", cause: " + e.getMessage(), e);
        } catch (RemotingException e) {
            throw new RpcException(RpcException.NETWORK_EXCEPTION, "Failed to invoke remote method: " + invocation.getMethodName() + ", cause: " + e.getMessage(), e);
        }
    }
}
