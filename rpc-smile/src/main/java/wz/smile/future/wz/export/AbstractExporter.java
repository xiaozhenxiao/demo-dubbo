package wz.smile.future.wz.export;

import wz.smile.future.wz.invoker.WZInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * wangzhen23
 * 2018/3/13.
 */
public abstract class AbstractExporter<T> implements Exporter<T> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final WZInvoker<T> invoker;

    private volatile boolean unexported = false;

    public AbstractExporter(WZInvoker<T> invoker) {
        if (invoker == null)
            throw new IllegalStateException("service invoker == null");
        if (invoker.getInterface() == null)
            throw new IllegalStateException("service type == null");
        this.invoker = invoker;
    }

    public WZInvoker<T> getInvoker() {
        return invoker;
    }

    public void unexport() {
        if (unexported) {
            return;
        }
        unexported = true;
        getInvoker().destroy();
    }

    public String toString() {
        return getInvoker().toString();
    }
}
