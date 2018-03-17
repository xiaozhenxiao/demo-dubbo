package com.smile.wz.invoker;


import com.alibaba.dubbo.rpc.RpcException;
import com.smile.wz.result.WZRpcResult;
import com.smile.wz.Constants;
import com.smile.wz.result.Result;

import java.lang.reflect.InvocationTargetException;

/**
 * 抽象调用类
 * wangzhen23
 * 2018/2/8.
 */
public abstract class AbatractWZInvoker<T> implements WZInvoker {
    private final Class<T> type;
    private Boolean isAsync;

    public AbatractWZInvoker(Class<T> type, boolean isAsync) {
        this.type = type;
        this.isAsync = isAsync;
    }

    public Result invoke(WZInvocation inv) throws RpcException {
        WZRpcInvocation invocation = (WZRpcInvocation) inv;
        invocation.setInvoker(this);
        invocation.setAttachment(Constants.INTERFACE_KEY, this.type.getName());
        invocation.setAttachment(Constants.ASYNC_KEY, isAsync().toString());
        try {
            return doInvoke(invocation);
        } catch (InvocationTargetException e) { // biz exception
            Throwable te = e.getTargetException();
            if (te == null) {
                return new WZRpcResult(e);
            } else {
                if (te instanceof RpcException) {
                    ((RpcException) te).setCode(RpcException.BIZ_EXCEPTION);
                }
                return new WZRpcResult(te);
            }
        } catch (RpcException e) {
            if (e.isBiz()) {
                return new WZRpcResult(e);
            } else {
                throw e;
            }
        } catch (Throwable e) {
            return new WZRpcResult(e);
        }
    }

    public Class<T> getInterface() {
        return type;
    }

    protected abstract Result doInvoke(WZInvocation invocation) throws Throwable;

    @Override
    public void destroy() {
        System.out.println("==========================");
    }

    public Boolean isAsync() {
        return isAsync;
    }
}
