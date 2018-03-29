package com.smile.wz.invoker;


import com.alibaba.dubbo.common.utils.ReflectUtils;
import com.alibaba.dubbo.rpc.RpcException;
import com.smile.wz.Constants;
import com.smile.wz.StaticContext;
import com.smile.wz.result.Result;
import com.smile.wz.result.WZRpcResult;
import com.smile.wz.rpc.FutureAdapter;
import com.smile.wz.rpc.ResponseCallback;
import com.smile.wz.rpc.WZResponseFuture;
import com.smile.wz.rpc.WZRpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 抽象调用类
 * wangzhen23
 * 2018/2/8.
 */
public abstract class AbatractWZInvoker<T> implements WZInvoker {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private final Class<T> type;
    private final Map<String, String> attachment;

    public AbatractWZInvoker(Class<T> type, Map<String, String> attachment) {
        this.type = type;
        this.attachment = attachment;
    }

    public Result invoke(WZInvocation inv) throws RpcException {
        WZRpcInvocation invocation = (WZRpcInvocation) inv;
        invocation.setInvoker(this);
        String methodName = invocation.getMethodName();
        invocation.setAttachment(Constants.INTERFACE_KEY, this.type.getName().endsWith(Constants.ASYNC) ? this.type.getName().substring(0, this.type.getName().length() - 5) : this.type.getName());
        invocation.setAttachment(Constants.ASYNC_KEY, getAsync(invocation.getMethodName()));
        try {
            if(methodName.endsWith(Constants.ASYNC)){
                invocation.setMethodName(methodName.substring(0, methodName.length()-5));
                invocation.setAttachment(Constants.ASYNC_KEY, Boolean.TRUE.toString());
                invocation.setAttachment(Constants.RETURN_FUTURE, Boolean.TRUE.toString());
            }
            fireInvokeCallback(this, invocation);
            Result result = doInvoke(invocation);
            if (Boolean.TRUE.toString().equals(inv.getAttachment(Constants.ASYNC_KEY))) {
                asyncCallback(this, invocation);
            } else {
                syncCallback(this, invocation, result);
            }
            return result;
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

    private void syncCallback(final WZInvoker<?> invoker, final WZInvocation invocation, final Result result) {
        if (result.hasException()) {
            fireThrowCallback(invoker, invocation, result.getException());
        } else {
            fireReturnCallback(invoker, invocation, result.getValue());
        }
    }

    private void asyncCallback(final WZInvoker<?> invoker, final WZInvocation invocation) {
        Future<?> f = WZRpcContext.getContext().getFuture();
        if (f instanceof FutureAdapter) {
            WZResponseFuture future = ((FutureAdapter<?>) f).getFuture();
            future.setCallback(new ResponseCallback() {
                public void done(Object rpcResult) {
                    if (rpcResult == null) {
                        logger.error("", new IllegalStateException("invalid result value : null, expected " + Result.class.getName()));
                        return;
                    }
                    ///must be rpcResult
                    if (!(rpcResult instanceof Result)) {
                        logger.error("", new IllegalStateException("invalid result type :" + rpcResult.getClass() + ", expected " + Result.class.getName()));
                        return;
                    }
                    Result result = (Result) rpcResult;
                    if (result.hasException()) {
                        fireThrowCallback(invoker, invocation, result.getException());
                    } else {
                        fireReturnCallback(invoker, invocation, result.getValue());
                    }
                }

                public void caught(Throwable exception) {
                    fireThrowCallback(invoker, invocation, exception);
                }
            });
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

    public String getAsync(String method) {
        if (!Boolean.valueOf(attachment.get(method + "." + Constants.ASYNC_KEY))) {
            return attachment.get(Constants.ASYNC_KEY);
        } else {
            return attachment.get(method + "." + Constants.ASYNC_KEY);
        }
    }

    private void fireReturnCallback(final WZInvoker<?> invoker, final WZInvocation invocation, final Object result) {
        String onReturnMethodKey = StaticContext.getKey(this.attachment, invocation.getMethodName(), Constants.ON_RETURN_METHOD_KEY);
        String onReturnInstancedKey = StaticContext.getKey(this.attachment, invocation.getMethodName(), Constants.ON_RETURN_INSTANCE_KEY);
        final Method onReturnMethod = (Method) StaticContext.getSystemContext().get(onReturnMethodKey);
        final Object onReturnInst = StaticContext.getSystemContext().get(onReturnInstancedKey);

        //not set onreturn callback
        if (onReturnMethod == null && onReturnInst == null) {
            return;
        }

        if (onReturnMethod == null || onReturnInst == null) {
            throw new IllegalStateException("service:" + onReturnMethodKey + " has a onreturn callback config , but no such " + (onReturnMethod == null ? "method" : "instance") + " found. url:" + onReturnMethodKey);
        }
        if (!onReturnMethod.isAccessible()) {
            onReturnMethod.setAccessible(true);
        }

        Object[] args = invocation.getArguments();
        Class<?>[] rParaTypes = onReturnMethod.getParameterTypes();
        Object[] params = getParams(result, args, rParaTypes);
        try {
            onReturnMethod.invoke(onReturnInst, params);
        } catch (InvocationTargetException e) {
            fireThrowCallback(invoker, invocation, e.getTargetException());
        } catch (Throwable e) {
            fireThrowCallback(invoker, invocation, e);
        }
    }

    private Object[] getParams(Object result, Object[] args, Class<?>[] rParaTypes) {
        Object[] params;
        if (rParaTypes.length > 1) {
            if (rParaTypes.length == 2 && rParaTypes[1].isAssignableFrom(Object[].class)) {
                params = new Object[2];
                params[0] = result;
                params[1] = args;
            } else {
                params = new Object[args.length + 1];
                params[0] = result;
                System.arraycopy(args, 0, params, 1, args.length);
            }
        } else {
            params = new Object[]{result};
        }
        return params;
    }

    private void fireInvokeCallback(final WZInvoker<?> invoker, final WZInvocation invocation) {
        String onInvokeMethodKey = StaticContext.getKey(this.attachment, invocation.getMethodName(), Constants.ON_INVOKE_METHOD_KEY);
        String onInvokeInstancedKey = StaticContext.getKey(this.attachment, invocation.getMethodName(), Constants.ON_INVOKE_INSTANCE_KEY);
        final Method onInvokeMethod = (Method) StaticContext.getSystemContext().get(onInvokeMethodKey);
        final Object onInvokeInst = StaticContext.getSystemContext().get(onInvokeInstancedKey);

        if (onInvokeMethod == null && onInvokeInst == null) {
            return;
        }
        if (onInvokeMethod == null || onInvokeInst == null) {
            throw new IllegalStateException("service:" + onInvokeMethodKey + " has a onreturn callback config , but no such " + (onInvokeMethod == null ? "method" : "instance") + " found. url:" + onInvokeMethodKey);
        }
        if (!onInvokeMethod.isAccessible()) {
            onInvokeMethod.setAccessible(true);
        }

        Object[] params = invocation.getArguments();
        try {
            onInvokeMethod.invoke(onInvokeInst, params);
        } catch (InvocationTargetException e) {
            fireThrowCallback(invoker, invocation, e.getTargetException());
        } catch (Throwable e) {
            fireThrowCallback(invoker, invocation, e);
        }
    }

    private void fireThrowCallback(final WZInvoker<?> invoker, final WZInvocation invocation, final Throwable exception) {
        String onThrowMethodKey = StaticContext.getKey(this.attachment, invocation.getMethodName(), Constants.ON_THROW_METHOD_KEY);
        String onThrowInstancedKey = StaticContext.getKey(this.attachment, invocation.getMethodName(), Constants.ON_THROW_INSTANCE_KEY);
        final Method onthrowMethod = (Method) StaticContext.getSystemContext().get(onThrowMethodKey);
        final Object onthrowInst = StaticContext.getSystemContext().get(onThrowInstancedKey);

        //onthrow callback not configured
        if (onthrowMethod == null && onthrowInst == null) {
            return;
        }
        if (onthrowMethod == null || onthrowInst == null) {
            throw new IllegalStateException("service:" + onThrowMethodKey + " has a onthrow callback config , but no such " + (onthrowMethod == null ? "method" : "instance") + " found. url:" + onThrowMethodKey);
        }
        if (!onthrowMethod.isAccessible()) {
            onthrowMethod.setAccessible(true);
        }
        Class<?>[] rParaTypes = onthrowMethod.getParameterTypes();
        if (rParaTypes[0].isAssignableFrom(exception.getClass())) {
            try {
                Object[] args = invocation.getArguments();
                Object[] params = getParams(exception, args, rParaTypes);
                onthrowMethod.invoke(onthrowInst, params);
            } catch (Throwable e) {
                logger.error(invocation.getMethodName() + ".call back method invoke error . callback method :" + onthrowMethod + ", url:" + onThrowMethodKey, e);
            }
        } else {
            logger.error(invocation.getMethodName() + ".call back method invoke error . callback method :" + onthrowMethod + ", url:" + onThrowMethodKey, exception);
        }
    }
}
