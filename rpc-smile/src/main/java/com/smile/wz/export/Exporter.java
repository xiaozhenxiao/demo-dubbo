package com.smile.wz.export;

import com.smile.wz.invoker.WZInvoker;

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