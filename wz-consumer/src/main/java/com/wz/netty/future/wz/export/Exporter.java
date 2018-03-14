package com.wz.netty.future.wz.export;

import com.wz.netty.future.wz.invoker.WZInvoker;

public interface Exporter<T> {

    /**
     * get invoker.
     *
     * @return invoker
     */
    WZInvoker<T> getInvoker();

    /**
     * unexport.
     * <p>
     * <code>
     * getInvoker().destroy();
     * </code>
     */
    void unexport();

}