package wz.smile.future.wz.export;

import wz.smile.future.wz.invoker.WZInvoker;

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