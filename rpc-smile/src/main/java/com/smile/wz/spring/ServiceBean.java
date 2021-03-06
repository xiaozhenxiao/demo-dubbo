package com.smile.wz.spring;

import com.alibaba.dubbo.config.spring.extension.SpringExtensionFactory;
import com.smile.wz.ProxyFactory;
import com.smile.wz.invoker.WZInvoker;
import com.smile.wz.export.Exporter;
import com.smile.wz.protocol.MyselfProtocol;
import com.smile.wz.protocol.Protocol;
import com.smile.wz.proxy.client.JdkProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * wangzhen23
 * 2018/3/13.
 */
public class ServiceBean<T> implements InitializingBean, DisposableBean, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent>, BeanNameAware {
    private String id;
    private Integer port;
    private String interfaceName;
    private Class<?> interfaceClass;
    private transient volatile T ref;

    private transient String beanName;
    private transient ApplicationContext applicationContext;
    private static transient ApplicationContext SPRING_CONTEXT;
    private transient boolean supportedApplicationListener;
    private final List<Exporter<?>> exporters = new ArrayList<Exporter<?>>();
    private static final Protocol protocol = new MyselfProtocol();

    private static final ProxyFactory proxyFactory = new JdkProxyFactory();

    @Override
    public void afterPropertiesSet() throws Exception {
        if (interfaceName == null || interfaceName.length() == 0) {
            throw new IllegalStateException("<dubbo:reference interface=\"\" /> interface not allow null!");
        }

        try {
            interfaceClass = Class.forName(interfaceName, true, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        if (!isDelay()) {
            doExport();
        }
    }

    private boolean isDelay() {
        Integer delay = -1;
        /** 处理是否延迟export **/
        return supportedApplicationListener && (delay == null || delay == -1);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        doExport();
    }

    private void doExport() {
        WZInvoker<?> invoker = proxyFactory.getInvoker(ref, (Class) interfaceClass);

        Exporter<?> exporter = protocol.export(invoker, port);
        exporters.add(exporter);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        SpringExtensionFactory.addApplicationContext(applicationContext);
        if (applicationContext != null) {
            SPRING_CONTEXT = applicationContext;
            try {
                Method method = applicationContext.getClass().getMethod("addApplicationListener", new Class<?>[]{ApplicationListener.class}); // backward compatibility to spring 2.0.1
                method.invoke(applicationContext, new Object[]{this});
                supportedApplicationListener = true;
            } catch (Throwable t) {
                if (applicationContext instanceof AbstractApplicationContext) {
                    try {
                        Method method = AbstractApplicationContext.class.getDeclaredMethod("addListener", new Class<?>[]{ApplicationListener.class}); // backward compatibility to spring 2.0.1
                        if (!method.isAccessible()) {
                            method.setAccessible(true);
                        }
                        method.invoke(applicationContext, new Object[]{this});
                        supportedApplicationListener = true;
                    } catch (Throwable t2) {
                    }
                }
            }
        }
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void destroy() throws Exception {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public T getRef() {
        return ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }
}
