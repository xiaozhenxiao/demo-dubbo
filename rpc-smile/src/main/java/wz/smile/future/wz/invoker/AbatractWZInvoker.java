package wz.smile.future.wz.invoker;


import com.alibaba.dubbo.rpc.RpcException;
import wz.smile.future.wz.Constants;
import wz.smile.future.wz.result.Result;
import wz.smile.future.wz.result.WZRpcResult;

import java.lang.reflect.InvocationTargetException;

/**
 * 抽象调用类
 * wangzhen23
 * 2018/2/8.
 */
public abstract class AbatractWZInvoker<T> implements WZInvoker {
    private final Class<T> type;

    public AbatractWZInvoker(Class<T> type) {
        this.type = type;
    }

    public Result invoke(WZInvocation inv) throws RpcException {
        WZRpcInvocation invocation = (WZRpcInvocation) inv;
        invocation.setInvoker(this);
        invocation.setAttachment(Constants.INTERFACE_KEY, this.type.getName());
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
}
