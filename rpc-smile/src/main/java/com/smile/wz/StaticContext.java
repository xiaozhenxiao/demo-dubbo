package com.smile.wz;

import com.alibaba.dubbo.common.utils.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * System context, for internal use only
 */
public class StaticContext extends ConcurrentHashMap<Object, Object> {
    private static final long serialVersionUID = 1L;
    private static final String SYSTEMNAME = "system";
    private static final ConcurrentMap<String, StaticContext> context_map = new ConcurrentHashMap<String, StaticContext>();
    private String name;

    private StaticContext(String name) {
        super();
        this.name = name;
    }

    public static StaticContext getSystemContext() {
        return getContext(SYSTEMNAME);
    }

    public static StaticContext getContext(String name) {
        StaticContext appContext = context_map.get(name);
        if (appContext == null) {
            appContext = context_map.putIfAbsent(name, new StaticContext(name));
            if (appContext == null) {
                appContext = context_map.get(name);
            }
        }
        return appContext;
    }

    public static StaticContext remove(String name) {
        return context_map.remove(name);
    }

    public static String getKey(Map<String, String> paras, String methodName, String suffix) {
        return getKey(StringUtils.getServiceKey(paras), methodName, suffix);
    }

    private static String getKey(String servicekey, String methodName, String suffix) {
        StringBuffer sb = new StringBuffer().append(servicekey).append(".").append(methodName).append(".").append(suffix);
        return sb.toString();
    }

    public String getName() {
        return name;
    }
}