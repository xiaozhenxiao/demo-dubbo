package com.smile.wz.spring.schema;

import com.smile.wz.spring.ReferenceBean;
import com.smile.wz.spring.ServiceBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * wangzhen23
 * 2018/3/15.
 */
public class WZNamespaceHandler  extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("service", new WZBeanDefinitionParser(ServiceBean.class));
        registerBeanDefinitionParser("reference", new WZBeanDefinitionParser(ReferenceBean.class));
    }
}
