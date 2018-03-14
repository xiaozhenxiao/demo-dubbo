package com.wz.netty.future.wz.export;

import com.wz.netty.future.wz.invoker.WZInvoker;

import java.util.Map;

/**
 * TODO
 * wangzhen23
 * 2018/3/13.
 */
public class WZExporter<T> extends AbstractExporter<T> {
    private final String key;

    private final Map<String, Exporter<?>> exporterMap;

    public WZExporter(WZInvoker<T> invoker, String key, Map<String, Exporter<?>> exporterMap) {
        super(invoker);
        this.key = key;
        this.exporterMap = exporterMap;
    }

    @Override
    public void unexport() {
        super.unexport();
        exporterMap.remove(key);
    }
}
