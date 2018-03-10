package com.wz.netty.future.wz.spring;

import com.wz.netty.future.wz.Constants;
import com.wz.netty.future.wz.ProxyFactory;
import com.wz.netty.future.wz.invoker.WZInvoker;
import com.wz.netty.future.wz.protocol.MyselfProtocol;
import com.wz.netty.future.wz.protocol.Protocol;
import com.wz.netty.future.wz.proxy.client.JdkProxyFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * wangzhen23
 * 2018/3/7.
 */
public class ReferenceBean<T> implements FactoryBean {
    private Logger logger = LoggerFactory.getLogger(getClass());
    //host
    private String address;
    private transient volatile T ref;
    private transient volatile WZInvoker<?> invoker;
    private transient volatile boolean initialized;
    // interface name
    private String interfaceName;
    private Class<?> interfaceClass;
    private static final Protocol refprotocol = new MyselfProtocol();
    private static final ProxyFactory proxyFactory = new JdkProxyFactory();

    @Override
    public Object getObject() throws Exception {
        return get();
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public synchronized T get() {
        if (ref == null) {
            init();
        }
        return ref;
    }

    private void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        if (interfaceName == null || interfaceName.length() == 0) {
            throw new IllegalStateException("<dubbo:reference interface=\"\" /> interface not allow null!");
        }

        try {
            interfaceClass = Class.forName(interfaceName, true, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        Map<String, String> map = new HashMap<String, String>();

        String[] methods = getMethodNames(interfaceClass);
        if (methods.length == 0) {
            logger.warn("NO method found in service interface " + interfaceClass.getName());
            map.put("methods", Constants.ANY_VALUE);
        } else {
            map.put("methods", StringUtils.join(new HashSet<String>(Arrays.asList(methods)), ","));
        }

        map.put(Constants.INTERFACE_KEY, interfaceName);

        ref = createProxy(map);
    }

    private String[] getMethodNames(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        String[] methodNames = new String[methods.length];
        for (int i = 0; i < methods.length; i++) {
            methodNames[i] = methods[i].getName();
        }
        return methodNames;
    }

    private T createProxy(Map<String, String> map) {

        invoker = refprotocol.refer(interfaceClass, address);

        // create service proxy
        return (T) proxyFactory.getProxy(invoker);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }
}
